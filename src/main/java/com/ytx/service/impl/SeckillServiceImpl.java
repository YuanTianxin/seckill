package com.ytx.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.ytx.dao.SeckillDao;
import com.ytx.dao.SuccessKilledDao;
import com.ytx.dao.cache.RedisDao;
import com.ytx.dto.Exposer;
import com.ytx.dto.SeckillExecution;
import com.ytx.entity.Seckill;
import com.ytx.entity.SuccessKilled;
import com.ytx.enums.SeckillStateEnum;
import com.ytx.exception.RepeatKillException;
import com.ytx.exception.SeckillCloseException;
import com.ytx.exception.SeckillException;
import com.ytx.service.SeckillService;

/**
 * service接口实现类
 * @author yuantian xin
 *
 */
@Service
public class SeckillServiceImpl implements SeckillService{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	@Autowired
	private RedisDao redisDao;
	
	/**
	 * md5盐值字符串，混淆字符串，用户不能反推出原来的密文，自定义越复杂越好
	 */
	private  final String  salt = "feoi$#!@feo5245622>:fef77842e1f5e4*&^%*fef:_L44fE85fefWFEW55";
	
	
	/**
	 * 获取秒杀商品的列表
	 */
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	
	/**
	 * 通过id获取秒杀商品信息
	 */
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	
	
	public Exposer exportSeckillUrl(long seckillId) {
		/**优化点：缓存优化：超时的基础上维护一致性
		 * 通过商品id，获取秒杀商品的全部信息
		 */
		//1.访问redis缓存
		Seckill seckill = redisDao.getSeckill(seckillId);
		if(seckill == null) {
			//2.缓存找不到，到mysql中找
			seckill = seckillDao.queryById(seckillId);
			if(seckill == null) {
				return new Exposer(false,seckillId);
			} else {
				//3.mysql找到了，写入redis缓存
				redisDao.putSeckill(seckill);
			}
		}
		
		//商品开始秒杀时间
		Date startTime = seckill.getStartTime();
		//商品结束秒杀时间
		Date endTime   = seckill.getEndTime();
		//系统当前时间
		Date nowTime = new Date();
		
		//通过getTime()方法获取时间的毫秒数，如果商品秒杀还没有开始或者商品秒杀已经结束了
		// 则不允许用户进行秒杀，返回false的标识，并且返回当前时间，商品秒杀开启时间，商品秒杀结束时间
		if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
			return  new Exposer(false,seckillId, nowTime.getTime(), startTime.getTime(),endTime.getTime());
		}
		
		//通过md5规则把商品id进行加密，得到密文串
		//转化特定字符串的过程，不可逆
		String md5 = getMD5(seckillId);
		//条件满足，允许用户开启秒杀，并传入对商品id加密后的密文串
		return new Exposer(true,md5,seckillId);
	}
	/**
	 * 商品id拼接上面定义的盐值字符串后，根据spring提供的md5方法进行加密，返回密文串
	 * 如果不加盐值字符串，用户是能够根据MD5规则算出原来的商品id的
	 * @param seckillId
	 * @return
	 */
	private String getMD5(long seckillId) {
		String base = seckillId + "/" + salt;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	
	
	
	
	//执行秒杀的方法。秒杀是否成功，成功 ：减库存，增加明细；失败：抛出异常，事务回滚
	@Transactional
	/**
	 * 使用注解的声明式事务管理
	 * 保证事务方法的执行时间尽可能的短，不要穿插其他网络操作RPC/HTTP请求,缓存等毫秒级操作
	 * 如果非常必要进行这些操作，剥离到事务方法的外部处理
	 *
	 */
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		//检查两次的md5的值是否相等，id有没有被篡改，不相等就说明被改了，抛出异常
		if(md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		// 执行秒杀逻辑： 记录购买行为 + 减去库存，这是一个事务，需要进行事务处理
		Date nowTime = new Date();
		try {
			//记录购买行为
            int insertCount=successKilledDao.insertSuccessKilled(seckillId,userPhone);
            //看是否该明细被重复插入，即用户是否重复秒杀
            if (insertCount<=0){
                throw new RepeatKillException("seckill repeated");
            }else {
                //减库存,热点商品竞争
                int updateCount=seckillDao.reduceNumber(seckillId,nowTime);
                if (updateCount<=0){
                    //没有更新库存记录，说明秒杀结束 rollback
                    throw new SeckillCloseException("seckill is closed");
                }else {
                    //秒杀成功,得到成功插入的明细记录,并返回成功秒杀的信息 commit
                    SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }
           
		} catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 所有编译期异常，转化为运行期异常
			throw new SeckillException("seckill inner error: " + e.getMessage());
		}
	}
	
	
	/**
	 * 执行秒杀操作，通过存储过程
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 * @throws SeckillException
	 * @throws RepeatKillException
	 * @throws SeckillCloseException
	 */
	public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5){
		if(md5 == null || !md5.equals(getMD5(seckillId))) {
			return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
		}
		Date killTime = new Date();
		Map<String, Object> smap = new HashMap<String, Object>();
		smap.put("seckillId", seckillId);
		smap.put("phone", userPhone);
		smap.put("killTime", killTime);
		smap.put("result", null);
		//执行存储过程，result被复制
		try {
			seckillDao.killByProcedure(smap);
			//获取result
			int result = MapUtils.getInteger(smap, "result",-2);
			if(result == 1) {
				SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,sk);
			} else {
				return new SeckillExecution(seckillId,SeckillStateEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
		}
	
	}

}

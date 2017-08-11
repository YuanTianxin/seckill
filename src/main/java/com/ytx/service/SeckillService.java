package com.ytx.service;

import java.util.List;

import com.ytx.dto.Exposer;
import com.ytx.dto.SeckillExecution;
import com.ytx.entity.Seckill;
import com.ytx.exception.RepeatKillException;
import com.ytx.exception.SeckillCloseException;
import com.ytx.exception.SeckillException;

/**
 * 业务接口：站在“使用者”的角度设计接口
 * 三个方面：1.方法定义粒度： 设计的接口不要过于细节,不要太繁琐也不要太抽象，简单的说，如果你的一个service方法只涉及到一个dao层方法，那接口粒度太细了
 * 			2.参数 ：越简单越好
 * 			3.返回类型：返回类型要友好
 * 业务需求：
 * 		1.需要获取一个所有的秒杀商品列表，供用户选择
 * 		2.能够根据id查询单个秒杀商品
 * 		3.用户能够进入到某个商品的秒杀页面，但是为了安全性和公平性，需要采取一些措施，只有当秒杀时间到了，才主动暴露地址，否则用户是不能够提前秒杀
 * 		4.用户进入某商品的秒杀页面后能够执行秒杀，返回秒杀的结果
 * @author yuantian xin
 *
 */
public interface SeckillService {
	
	/**
	 * 查询所有的秒杀商品
	 * @return
	 */
	List<Seckill> getSeckillList();
	
	/**
	 * 查询单个秒杀商品
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	
	
	
	
	
	/**
	 * 秒杀开启时输出秒杀接口地址，否则输出系统时间和秒杀时间
	 * 这个意义是只有当秒杀开始时间到了，才输出地址。秒杀未开始前，用户不能通过url规则，拼接地址进入秒杀页面
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);
	
	
	
	
	
	/**
	 * 执行秒杀操作
	 * md5的作用是在用户秒杀商品时进行验证
	 * 暴露接口地址的方法是先被调用的，Exposer里面的生成的密文串md5值，
	 * 和本方法中利用相同的getMD5方法生成的密文串比较，如果不同，说明用户的url被篡改了，秒杀失败
	 * 秒杀失败抛出我们自定义的异常
	 * 执行秒杀逻辑：减去库存 + 记录购买行为，这是一个事务，需要进行事务处理
	 * 若发生异常，进行回滚
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException,RepeatKillException,SeckillCloseException;
	
	
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
	SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}

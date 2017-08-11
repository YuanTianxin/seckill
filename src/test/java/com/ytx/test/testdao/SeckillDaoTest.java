package com.ytx.test.testdao;



import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.ytx.dao.SeckillDao;
import com.ytx.entity.Seckill;
import com.ytx.test.base.SpringBaseTest;

/**
 * SeckillDao的测试类
 * @author yuantian xin
 *
 */
public class SeckillDaoTest extends SpringBaseTest{
	
	//使用注解自动注入Dao
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void queryByIdTest() {
		long seckillId = 1000L;
		Seckill seckill = seckillDao.queryById(seckillId);
		System.out.println(seckill);
	}
	
	@Test
	public void queryAllTest() {
		List<Seckill> seckills = seckillDao.queryAll(0, 4);
		for(Seckill seckill : seckills) {
			System.out.println(seckill);
		}
	}
	
	
	@Test
	public void reduceNumberTest() {
		long seckillId = 1000L;
		Date killTime = new Date();
		System.out.println(killTime);
		//秒杀测试不会成功，因为创建的秒杀时间是当前的时间，没有在你设定的秒杀开始时间和结束时间之间
		int updateCount = seckillDao.reduceNumber(seckillId, killTime);
		System.out.println(updateCount);
	}
	
}

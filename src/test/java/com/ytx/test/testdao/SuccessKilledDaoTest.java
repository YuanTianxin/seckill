package com.ytx.test.testdao;

import javax.annotation.Resource;

import org.junit.Test;

import com.ytx.dao.SuccessKilledDao;
import com.ytx.entity.SuccessKilled;
import com.ytx.test.base.SpringBaseTest;

/**
 * SuccessKilledDao的测试类
 * @author yuantian xin
 *
 */
public class SuccessKilledDaoTest extends SpringBaseTest{
	
	@Resource
	private SuccessKilledDao successKilledDao;
	
	@Test
	public void insertSuccessKilledTest() {
		long seckillId = 1000L;
		long userPhone = 15648596666L;
		int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
		System.out.println("insertCount=" + insertCount);
	}
	
	@Test
	public void queryByIdWithSeckill() {
		
		long seckillId = 1000L;
		long userPhone = 15648596666L;
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
		System.out.println(successKilled);
		System.out.println(successKilled.getSeckill());
		
	}
}

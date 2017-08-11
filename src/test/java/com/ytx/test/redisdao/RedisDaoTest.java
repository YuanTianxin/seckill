package com.ytx.test.redisdao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ytx.dao.SeckillDao;
import com.ytx.dao.cache.RedisDao;
import com.ytx.entity.Seckill;
import com.ytx.test.base.SpringBaseTest;

public class RedisDaoTest extends SpringBaseTest{
	
	private long id = 1001;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Test
	public void testRedisSeckill() throws Exception{
		Seckill  seckill= redisDao.getSeckill(id);
		if(seckill == null) {
			seckill = seckillDao.queryById(id);
			if(seckill != null) {
				String result = redisDao.putSeckill(seckill);
				System.out.println(result);
				seckill = redisDao.getSeckill(id);
				System.out.println(seckill);
			}
		}
	}
}

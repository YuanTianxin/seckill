package com.ytx.dao;

import org.apache.ibatis.annotations.Param;

import com.ytx.entity.SuccessKilled;

public interface SuccessKilledDao {

	/**
	 * 插入购买明细，可以过滤重复
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	int insertSuccessKilled(@Param("seckillId")long seckillId, @Param("userPhone")long userPhone);
	
	/**
	 * 根据商品id和用户手机号查询SuccessKilled秒杀成功对象并携带秒杀商品对象实体
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId, @Param("userPhone")long userPhone);
}

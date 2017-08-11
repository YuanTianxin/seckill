package com.ytx.dao.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.ytx.entity.Seckill;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 使用Redis缓存优化
 * 优化获取exposer过程中对秒杀商品信息seckill的查询，加入缓存中
 * @author yuantian xin
 *
 */
public class RedisDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private JedisPool jedisPool;
	
	public RedisDao(String ip,int port) {
		jedisPool = new JedisPool(ip,port);
	}
	//创建一个schema来描述class的结构
	private RuntimeSchema<Seckill> schema =RuntimeSchema.createFrom(Seckill.class);
	
	/**
	 * 
	 * @param seckillId
	 * @return
	 */
	public Seckill getSeckill(long seckillId) {
		//redis操作逻辑
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" +seckillId;
				//并没有实现内部序列化操作
				//get->byte[]二进制数组->反序列化->Object(Seckill)
				//采用自定义序列化，把对象转化成二进制数组，传到redis中缓存起来
				//创建一个schema来描述class的结构，protostuff序列化的对象是pojo，有getset方法，不能像是String一样的对象
				byte[] bytes = jedis.get(key.getBytes());
				//根据key的值在缓存中重获取到对象
				if(bytes != null) {
					Seckill seckill = schema.newMessage();
					//Seckill被反序列化了
					ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
					return seckill;
				}
				
			} finally {
				jedis.close();
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
		
	}
	
	public String putSeckill(Seckill seckill) {
		// set Object(Seckill)->序列化->byte[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" +seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));//缓存器，防止对象过大
				//超时缓存
				int timeout = 60 * 60;//1小时
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

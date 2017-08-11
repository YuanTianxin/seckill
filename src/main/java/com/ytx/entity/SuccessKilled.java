package com.ytx.entity;

import java.util.Date;

/**
 * 用户秒杀记录实体
 * @author yuantian xin
 *
 */
public class SuccessKilled {
	
	/**
	 * 商品id
	 */
	private long seckillId;
	
	/**
	 * 用户手机号
	 */
	private long userPhone;
	
	/**
	 * 秒杀状态，默认为-1，秒杀成功后变成0
	 */
	private short  state;
	
	/**
	 * 秒杀记录单创建时间
	 */
	private Date createTime;
	
	//多对一
	private Seckill seckill;

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public long getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(long userPhone) {
		this.userPhone = userPhone;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Seckill getSeckill() {
		return seckill;
	}

	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}

	@Override
	public String toString() {
		return "SuccessKilled [seckillId=" + seckillId + ", userPhone=" + userPhone + ", state=" + state
				+ ", createTime=" + createTime + "]";
	}
	
	
	
}

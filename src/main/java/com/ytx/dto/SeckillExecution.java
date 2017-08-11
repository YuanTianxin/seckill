package com.ytx.dto;

import com.ytx.entity.SuccessKilled;
import com.ytx.enums.SeckillStateEnum;

/**
 * 封装秒杀执行后的结果
 * 业务层秒杀执行方法返回的数据
 * @author yuantian xin
 *
 */
public class SeckillExecution {
	
	/**
	 * 商品id
	 */
	private long seckillId;
	
	/**
	 * 秒杀执行结果的状态编码
	 */
	private int state;
	
	/**
	 * 状态的文字描述
	 */
	private String stateInfo;
	
	/**
	 * 秒杀成功对象，记录秒杀成功信息
	 */
	private SuccessKilled successKilled;
	
	/**
	 * 重写toString方法，方便测试输出
	 */
	public String toString() {
		return "SeckillExecution [seckillId=" + seckillId + ", state=" + state + ", stateInfo=" + stateInfo
				+ ", successKilled=" + successKilled + "]";
	}


	/**
	 * 秒杀成功后返回所有信息
	 * @param seckillId
	 * @param stateEnum
	 * @param successKilled
	 */
	public SeckillExecution(long seckillId, SeckillStateEnum stateEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.successKilled = successKilled;
    }
	
	
	/**
	 * 秒杀失败后返回的信息
	 * @param seckillId
	 * @param stateEnum
	 */
	public SeckillExecution(long seckillId, SeckillStateEnum stateEnum) {
		super();
		this.seckillId = seckillId;
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();;
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}

	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}
	
}

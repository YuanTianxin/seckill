package com.ytx.enums;

/**
 * 定义一个秒杀过程的状态枚举
 * 枚举可以看成一个普通的类，但是它是不能extends其它类的，因为它已经默认继承了java.lang.Enum类了
 * @author yuantian xin
 *
 */
public enum SeckillStateEnum {
	
	SUCCESS(1,"秒杀成功"),
	END(0,"秒杀结束"),
	REPEAT_KILL(-1,"重复秒杀"),
	INNER_ERROR(-2,"系统异常"),
	DATA_REWRITE(-3,"数据篡改");
	
	/**
	 * 状态编号
	 */
	private int state;
	
	
	/**
	 * 状态描述信息
	 */
	private String stateInfo;

	SeckillStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	public int getState() {
		return state;
	}
	
	public String getStateInfo() {
		return stateInfo;
	}
	
	/**
	 * 根据状态编号返回对应的枚举对象
	 * @param index 状态编号
	 * @return
	 */
	public static SeckillStateEnum stateOf(int index) {
		for (SeckillStateEnum state : values()) {
			if(state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}

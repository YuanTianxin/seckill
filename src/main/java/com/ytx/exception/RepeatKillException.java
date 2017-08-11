package com.ytx.exception;

/**
 * 重复秒杀异常（运行期异常）
 * spring的声明式事务只对非检查型异常和运行期异常进行回滚，而对检查型异常则不进行回滚操作
 * @author yuantian xin
 *
 */
public class RepeatKillException extends SeckillException{

	public RepeatKillException() {
		super();
	}

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepeatKillException(String message) {
		super(message);
	}

}

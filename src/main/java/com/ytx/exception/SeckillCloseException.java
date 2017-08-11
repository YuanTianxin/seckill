package com.ytx.exception;

/**
 * 秒杀关闭异常，当秒杀结束，用户还继续秒杀
 * @author yuantian xin
 *
 */
public class SeckillCloseException extends SeckillException{

	public SeckillCloseException() {
		super();
	}

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillCloseException(String message) {
		super(message);
	}
}

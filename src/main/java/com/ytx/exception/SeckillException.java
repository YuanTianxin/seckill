package com.ytx.exception;

/**
 * 所有的秒杀相关业务异常
 * @author yuantian xin
 *
 */
public class SeckillException extends RuntimeException{

	public SeckillException() {
		super();
	}

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}

	public SeckillException(String message) {
		super(message);
	}
}

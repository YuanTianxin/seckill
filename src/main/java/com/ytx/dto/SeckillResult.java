package com.ytx.dto;

/**
 * 所有的ajax请求返回类型，封装json结果
 * @author yuantian xin
 *
 * @param <T>
 */
public class SeckillResult<T> {
	
	/**
	 * 判断请求是否成功
	 */
	private boolean success;
	
	/**
	 * 泛型类型数据
	 */
    private T data;
    
    /**
     * 返回的错误信息
     */
    private String error;

  
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    } 
}

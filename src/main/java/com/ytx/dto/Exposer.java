package com.ytx.dto;

/**
 * 暴露秒杀地址
 * DTO数据传输层
 * @author yuantian xin
 *
 */
public class Exposer {
	/**
	 * 用来标识是否开启秒杀，是否允许用户进入秒杀页面
	 */
	private boolean exposed;
	
	/**
	 * 一种加密措施
	 */
	private String md5;
	
	/**
	 * 商品id
	 */
	private long seckillId;
	
	/**
	 * 系统当前时间(毫秒)
	 */
	private long now;
	
	/**
	 * 秒杀开启时间(毫秒)
	 */
	private long start;
	
	/**
	 * 秒杀结束时间(毫秒)
	 */
	private long end;
	
	/**重写toString方法，便于测试打印
	 * 
	 */
	public String toString() {
		return "Exposer [exposed=" + exposed + ", md5=" + md5 + ", seckillId=" + seckillId + ", now=" + now + ", start="
				+ start + ", end=" + end + "]";
	}
    
	/**
	 * 条件满足，暴露地址，允许用户进入秒杀页面，并传回商品id和通过md5加密id后的密文串
	 * @param exposed
	 * @param md5
	 * @param seckillId
	 */
	public Exposer(boolean exposed, String md5, long seckillId) {
		super();
		this.exposed = exposed;
		this.md5 = md5;
		this.seckillId = seckillId;
	}

	public Exposer(boolean exposed, long seckillId, long now, long start, long end) {
		super();
		this.exposed = exposed;
		this.seckillId = seckillId;
		this.now = now;
		this.start = start;
		this.end = end;
	}
     
	/**
	 * 若传过来的商品id为空，则拒绝用户进入秒杀页面
	 * @param exposed
	 * @param seckillId
	 */
	public Exposer(boolean exposed, long seckillId) {
		super();
		this.exposed = exposed;
		this.seckillId = seckillId;
	}

	public boolean isExposed() {
		return exposed;
	}

	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
}

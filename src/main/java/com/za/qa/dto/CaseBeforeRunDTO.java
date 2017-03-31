package com.za.qa.dto; 

import java.io.Serializable;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年12月6日 下午3:43:01 
 * 类说明 
 */
public class CaseBeforeRunDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String            request;              //解析后的入参
	private String            checkpoint;           //解析后的检查点
	private String            orderNumOfSuite;    //测试集顺序编号
	
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}

	public String getCheckpoint() {
		return checkpoint;
	}
	public void setCheckpoint(String checkpoint) {
		this.checkpoint = checkpoint;
	}
	

	public String getOrderNumOfSuite() {
		return orderNumOfSuite;
	}
	public void setOrderNumOfSuite(String orderNumOfSuite) {
		this.orderNumOfSuite = orderNumOfSuite;
	}
	@Override
	public String toString() {
		return "CaseBeforeRunDTO [request=" + request + ", checkpoint="
				+ checkpoint + "]";
	}

}
 
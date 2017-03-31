package com.za.qa.dto; 

import java.io.Serializable;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月28日 下午3:10:41 
 * 类说明 
 */
public class CaseAfterRunDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String response;
	private String result;
	
	public String getResponse() {
		return response;
	}
	public void setReponse(String response) {
		this.response = response;
	}

	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "CaseAfterRunDTO [response=" + response + ", result=" + result + "]";
	}


	

}
 
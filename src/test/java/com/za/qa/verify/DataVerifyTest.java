package com.za.qa.verify; 

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年12月3日 上午11:42:04 
 * 类说明 
 */
public class DataVerifyTest {

	@Test
	public void test() {
		String str =  "{\""+"outTradeNo"+"\""+":"+ "\""+"#()"+"\""+","
				   +"\""+"zaOrderNo"+"\""+":"+ "\""+"#"+"\"}";
		System.out.println(str);
		System.out.println(DataVerify.verifyPayload(str));
	}

}
 
package com.za.qa.keywords; 

import static org.junit.Assert.*;

import org.junit.Test;

import com.za.qa.http.HttpConstant;

/** 
 * @author jujinxin 
 * @version 创建时间：2017年3月9日 下午4:38:16 
 * 类说明 
 */
public class CommonSSOLoginTest {

	@Test
	public void testRequestCookie() {
		String cookiestr = CommonSSOLogin.requestCookie(HttpConstant.NSSO_USERNAME_LIN, HttpConstant.NSSO_PASSWORD_LIN);
		System.out.println(cookiestr);
	}

}
 
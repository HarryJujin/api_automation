package com.za.qa.constants; 

import static org.junit.Assert.*;

import org.junit.Test;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年12月2日 下午3:25:42 
 * 类说明 
 */
public class CaseStatusTest {

	@Test
	public void test() 
	{
		System.out.println(CaseStatus.FAIL.getName());
		System.out.println(CaseStatus.FAIL.getCode());
	}

}
 
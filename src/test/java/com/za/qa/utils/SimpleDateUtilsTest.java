package com.za.qa.utils; 

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年12月14日 上午10:44:51 
 * 类说明 
 */
public class SimpleDateUtilsTest {

/*	@Test
	public void test() {
		//SimpleDateUtils.StringPattern("2018.12.10", 10);
		System.out.println(SimpleDateUtils.StringPattern("2018 12 10", 3));
	}*/
	
	@Test
	public void test2(){
		String time= SimpleDateUtils.timeStamp();
		System.out.println(time);
		String Date= SimpleDateUtils.timeStamp2Date(time, "yyyy-MM-dd HH:mm:ss");
		System.out.println(Date);
	}

}
 
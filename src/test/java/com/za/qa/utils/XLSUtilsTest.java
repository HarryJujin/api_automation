package com.za.qa.utils; 

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年12月12日 下午2:29:54 
 * 类说明 
 */
public class XLSUtilsTest {

	@Test
	public void testGetSheetName() throws Exception {
		String path ="D:\\workspace\\TestCase\\健康险_金鑫V1 - 副本.xlsx";
		List<String> list = XLSUtils.getSheetName(path);
		for(String str:list){
			System.out.println(str);
		}
	}

}
 
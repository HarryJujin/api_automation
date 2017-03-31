package com.za.qa.verify; 

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月22日 上午11:00:20 
 * 类说明：用于判断数据中是否包含未解析的数据，包含return true or false
 */
public class DataVerify {
	public static boolean verifyPayload(String bizcontent){
		String regx = "[@&\\$]([\\+a-zA-Z=_'\\s\u4e00-\u9fa5\\\\(\\\\),#0-9.%\\\\\\\"\\/:\\-]*[\\)])";// 过滤表达式
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(bizcontent);
		boolean bool = m.find();
		return bool;			
	}

}
 
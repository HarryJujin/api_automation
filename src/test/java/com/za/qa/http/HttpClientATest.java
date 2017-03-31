package com.za.qa.http; 

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.za.qa.utils.FileUtils;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月29日 下午7:40:55 
 * 类说明 
 */
public class HttpClientATest {

	@Test
	public void testHsfHttpPost() throws ParseException {
		Date start = new Date(System.currentTimeMillis());
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		System.out.println(df.parse(df.format(start)));
		
		HttpClientA hca = new HttpClientA();
		String word1="hello";
		String word2="world";
		Object[] hw = new  Object[]{word1,word2};
		String ArgsTypes = "["+"\""+"com.zhongan.core.cdc.dto.EndorsementCondition"+"\""+","+"\""+"com.zhongan.core.cdc.dto.Options"+"\""+"]";
		String ArgsObjects ="{\""+"insuranceCertiNo"+":"+"\""+"1131520004215921"+"\"}"+","+"{}";
		
		String ArgsTypes2 = "[\""+"com.zhongan.core.cdc.dto.BillReceiptDTO"+"\"]";
		String ArgsObjects2 = "[{\""+"productId"+":"+"10056"+"\"}]";
		
		String ArgsTypesreplace = ArgsTypes.replace("\"", "").replace("[", "").replace("]", "");
		String ArgsObjectsreplace = ArgsObjects.replace("\"", "").replace("[", "").replace("]", "");
		Object[] o = ArgsTypesreplace.split(",");

		String postDataStr ="["+ArgsObjectsreplace+"]";
		System.out.println(ArgsObjects);
		System.out.println(JSONObject.toJSONString(o));
		//System.out.println(JSONObject.toJSONString(list));
		String postType = JSONObject.toJSONString(o);
		postDataStr = postDataStr.replaceAll("\\n","");
		String hsfurl = "http://10.253.4.122:9096/com.zhongan.core.cdc.service.EndorsementQueryService:1.0.0/queryEndorsementList";
		String hsfur2 = "http://10.253.4.122:9096/com.zhongan.core.cdc.service.BillQueryService:1.0.0/getBillReceipt";
		Map<Object, Object> postData = new HashMap<Object, Object>();
		postData.put("ArgsTypes", postType); 
		postData.put("ArgsObjects", postDataStr); 

    	String hsfHttpResponse = "";
		try {
			
			System.out.println(postData); 
			hsfHttpResponse = hca.doRequest(hsfurl, postData, null, "utf-8");	
			//hsfHttpResponse=hsfHttpResponse.replace("\\s","");
			hsfHttpResponse=hsfHttpResponse.replace("\\","");
			hsfHttpResponse=hsfHttpResponse.replace("\"{","{");
			hsfHttpResponse=hsfHttpResponse.replace("}\"","}");

			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String mymessage="error";
	}
		
	}
	
	public void test() throws IOException, ParseException{
		Date start = new Date(System.currentTimeMillis());
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int sum =0;
		int sum2=0;
		for(int i=0;i<9999999;i++){
			sum +=i;
			for(int j=0;j<99999;j++){
				sum2 =sum+j;
				for(int n=0;n<9999999;n++){
					sum +=n;
					for(int m=0;m<99999;m++){
						sum2 =sum+m;
					}
				}
			}
		}
		for(int i=0;i<9999999;i++){
			sum +=i;
			for(int j=0;j<99999;j++){
				sum2 =sum+j;
			}
		}
		for(int i=0;i<9999999;i++){
			sum +=i;
			for(int j=0;j<99999;j++){
				sum2 =sum+j;
			}
		}
		for(int i=0;i<9999999;i++){
			sum +=i;
			for(int j=0;j<99999;j++){
				sum2 =sum+j;
			}
		}
		Date end = new Date(System.currentTimeMillis());
		long ct2 = System.currentTimeMillis();
		System.out.println(df.format(start));
		System.out.println(df.parse(df.format(start)));
		System.out.println(df.parse(df.format(end)).getTime()-df.parse(df.format(start)).getTime());
		System.out.println(df.format(end));
	}

}
 
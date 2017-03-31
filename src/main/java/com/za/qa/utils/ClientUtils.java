package com.za.qa.utils;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.za.qa.analyze.DataAnalyze;
import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseDataDTO;
import com.za.qa.log.LogMan;
import com.za.qa.verify.DataVerify;
import com.za.qa.verify.ResponseVerify;
import com.zhongan.scorpoin.biz.common.CommonRequest;
import com.zhongan.scorpoin.biz.common.CommonResponse;
import com.zhongan.scorpoin.common.ZhongAnApiClient;
import com.zhongan.scorpoin.common.ZhongAnOpenException;


public class ClientUtils {

	
	
	public ClientUtils(){
		
	}
	
	public static void caseStartLog (CaseDataDTO casedatadto){
		String type = casedatadto.getType();
		String caseNo = casedatadto.getCaseNo();
		LogMan.getLoger().info("---------" + "当前用例执行开始，用例编号:"+caseNo+",接口类型: "+type);
	}
	
	
	public static void caseEndLog(CaseDataDTO casedatadto){
		String caseNo = casedatadto.getCaseNo();
		LogMan.getLoger().info("---------" + "当前用例执行完毕，用例编号:"+ caseNo + "---------");
	}
	
	public static JSONObject Str2JsonObject(String bizcontent){
		Map<String, String> businessParamsMap = Utilities.serializeFormat(bizcontent);
		JSONObject reqJson=new JSONObject();
		for(String key: businessParamsMap.keySet()){
			reqJson.put(key, businessParamsMap.get(key));
		}
		LogMan.getLoger().info("Excel参数JSON: " + reqJson);
		return reqJson;
	}
	
	public static Map<String, String> headerMap(String bizcontent){
		 Map<String, String> headerMap = new HashMap<String, String>();
		 //包含"|",header 和form数据放在一起，用"|"隔开
        if (bizcontent.contains("|")) {
        	bizcontent = bizcontent.replace("\n", "");
            String header = bizcontent.split("\\|")[0];
            String[] headerArray = header.split("&");
            for (String str : headerArray) {
                if (str.indexOf("=") > -1) {
                    headerMap.put(str.split("=")[0], str.split("=")[1]);
                }
            }        
        }
       return  headerMap;
	}
	
	public static Map<String, String> bodyMap(String bizcontent){
	        Map<String, String> bodyMap = new HashMap<String, String>();
		 //包含"|",header 和form数据放在一起，用"|"隔开
       if (bizcontent.contains("|")) {
       	bizcontent = bizcontent.replace("\n", "");
           String body = bizcontent.split("\\|")[1];
           String[] bodyArray = body.split("&");
           for (String str : bodyArray) {
               if (str.split("=").length > 1) {
                   bodyMap.put(str.split("=")[0], str.split("=")[1]);
               } else {
                   bodyMap.put(str.split("=")[0], "");
               }
           }
         //不包含"|"
       }else if(!bizcontent.contains("|")&&bizcontent.length()>0){
    	   String[] bodyArray = bizcontent.split("\n");
    	   for (String str : bodyArray){
    		   if (str.split("=").length > 1) {
                   bodyMap.put(str.split("=")[0], str.split("=")[1]);
               } else {
                   bodyMap.put(str.split("=")[0], "");
               }
    	   }
       }
      return  bodyMap;
	}
	
	
	
}


package com.za.qa.keywords;

import com.alibaba.fastjson.JSONArray;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.za.qa.bo.BaseSqlQueryResult;
import com.za.qa.bo.SqlQueryResultContent;
import com.za.qa.http.HttpClientA;
import com.za.qa.log.LogMan;
import com.za.qa.utils.SimpleDateUtils;
import com.za.qa.utils.XLSUtils;
import com.za.qa.verify.ResponseVerify;
import com.zhongan.qa.utils.HttpClientUtils;
import com.zhongan.qa.utils.HttpResponseUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * Created by Administrator on 2016/7/11.
 */
public class KeywordDefinition {


    public class StringReleated extends ExpressionRegister {
        /**
         * param: arg1:[u, l, m] , arg2: length, 代表生成字符串长度
         * u: upper,代表大写字母
         * l:lower代表小写字母
         * m:mix代表大小写混合字符
         */

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
            String flag = FunctionUtils.getStringValue(arg1, env);
            String lengthStrValue = FunctionUtils.getStringValue(arg2, env);
            int length = Integer.parseInt(lengthStrValue);

            char[] upperCase = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

            char[] lowCase = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                    's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            String statement = "";
            while (statement.length() < length)
                if (flag.equals("u")) {
                    statement = statement + String.valueOf(upperCase[((int) (Math.random() * 26))]);

                } else if (flag.equals("l")) {

                    statement = statement + String.valueOf(lowCase[((int) (Math.random() * 26))]);
                } else if (flag.equals("m")) {

                    statement = statement + String.valueOf(upperCase[((int) (Math.random() * 26))]) + String.valueOf(lowCase[((int) (Math.random() * 26))]);
                }

            statement = statement.substring(0, length);
            return new AviatorString(statement);
        }

        public String getName() {
            return "RString";
        }
    }
    public class MirrorStr extends ExpressionRegister{
    	public AviatorObject call(Map<String, Object> map){
    		int length = 8;

            char[] upperCase = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            String statement = "";
            while (statement.length() < length){
            	 statement = statement + String.valueOf(upperCase[((int) (Math.random() * 26))]);
            }
            statement=statement+"_"+statement;
            return new AviatorString(statement);
    	}
    	public String getName() {
            return "MirrorStr";
        }
    	
    }
 
    public class SubStr extends ExpressionRegister {
        @Override
        public AviatorObject call(Map<String, Object> map, AviatorObject str, AviatorObject start,AviatorObject end) {
        	String value = FunctionUtils.getStringValue(str, map);
            String startnum = FunctionUtils.getStringValue(start, map);
            String endnum = FunctionUtils.getStringValue(end, map);

            Map<String, Object> payloadEnv = getPayloadEnv();
            String target= value.substring(Integer.parseInt(startnum),Integer.parseInt(endnum));           
            return new AviatorString(target);
        }
        
        public String getName() {
            return "RsubStr";
        }
    }

    public class NameReleated extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env) {
            NameGenerator n = new NameGenerator();
            String name = n.generate();
            return new AviatorString(name);
        }

        public String getName() {
            return "RName";
        }
    }
  
    public class PhoneReleated extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env) {
            PhoneNoGenerator n = new PhoneNoGenerator();
            String phone = n.getTel();
            return new AviatorString(phone);
        }

        public String getName() {
            return "RPhone";
        }
    }

    public class IDReleated1 extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env) {
            IdCardGenerator g = new IdCardGenerator();
            String id = g.generate();
            return new AviatorString(id);

        }

        public String getName() {
            return "RId";
        }
    }

    public class IDReleated2 extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject age) {
            String ageStr = FunctionUtils.getStringValue(age,env);
            int ageInt = Integer.parseInt(ageStr);
            IdCardGenerator g = new IdCardGenerator();
            String id = g.generate(ageInt);
            return new AviatorString(id);

        }

        public String getName() {
            return "RIdWithSpecifiedAge";
        }
    }

    
    public class IDReleated3 extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject age,  AviatorObject day) {
            String ageStr = FunctionUtils.getStringValue(age,env);
            int ageInt = Integer.parseInt(ageStr);
            String dayStr = FunctionUtils.getStringValue(day,env);
            int dayInt = Integer.parseInt(dayStr);
            IdCardGenerator g = new IdCardGenerator();
            String id = g.generate(ageInt,dayInt);
            return new AviatorString(id);
        }

        public String getName() {
            return "RIdWithCondition";
        }
    }

    /**
     * myBirthday 格式19880819
     * mySex 1是boy,2是girl
     */
    public class IDReleated4 extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject myBirthday,  AviatorObject mySex) {
            String BirthdayStr = FunctionUtils.getStringValue(myBirthday,env);
            String SexStr = FunctionUtils.getStringValue(mySex,env);
            IdCardGenerator g = new IdCardGenerator();
            String id = g.IdCreate(BirthdayStr,SexStr);
            return new AviatorString(id);
        }

        public String getName() {
            return "RIdWithBirthday";
        }
    }
    
    public class IDReleated5 extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject date,AviatorObject offset) {
        	String value = FunctionUtils.getStringValue(date, env);
            String offsetnum = FunctionUtils.getStringValue(offset,env);
        	String BirthdayStr =  SimpleDateUtils.StringPattern(value, Integer.parseInt(offsetnum));
            String SexStr = "1";
            IdCardGenerator g = new IdCardGenerator();
            String id = g.IdCreate(BirthdayStr,SexStr);
            return new AviatorString(id);
        }

        public String getName() {
            return "RIdWithSpecificDate";
        }
    }

    public class Replacement extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject str) {
        	String replacment = FunctionUtils.getStringValue(str,env);
        	String result ="";
        	if(replacment.trim().length()<=0||replacment.trim()==null){
        		return new AviatorString(result);
        	}else {
        		int num = Integer.valueOf(replacment);
        		for(int i=0;i<num;i++){
        			result =result+" ";
        		}
        		return new AviatorString(result);
        	}
            
        }

        public String getName() {
            return "Rreplace";
        }
    }
    public class DigitReleated extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject len) {
            String statement = String.valueOf(Math.random());
            String lengthStr = FunctionUtils.getStringValue(len,env);
            int length = Integer.parseInt(lengthStr);
            statement = statement.replaceAll("0.0*", "");
            while (statement.length() < length)
                statement = statement + String.valueOf(Math.random()).replaceAll("0.0*", "");
            statement = statement.substring(0, length);
            return new AviatorString(statement);
        }

        public String getName() {
            return "RNum";		//随机生成指定位数的数字
        }
    }
    public class Date extends ExpressionRegister{
    	  @Override
          public AviatorObject call(Map<String, Object> env, AviatorObject format,AviatorObject offset) {
              String formatStr = FunctionUtils.getStringValue(format,env);
              String offsetStr = FunctionUtils.getStringValue(offset,env);
              DateGenerator d = new DateGenerator();
              String date="";
			try {
				date = d.getOffsetDate(formatStr, offsetStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
              return new AviatorString(date);
          }

          public String getName() {
              return "RDate";
          }
    }
    
    public class FormatDate extends ExpressionRegister{
  	  @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject datestr,AviatorObject formatstr) {
            String formatStr = FunctionUtils.getStringValue(formatstr,env);
            String dateStr = FunctionUtils.getStringValue(datestr,env);
            String date="";
			try {
				date = SimpleDateUtils.FormateDate(dateStr, formatStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
            return new AviatorString(date);
        }

        public String getName() {
            return "RFormatDate";
        }
  }

    

    public class DependencyReponseReleated extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject caseNum, AviatorObject keyName) {
            String case_num = FunctionUtils.getStringValue(caseNum, env);
            String key_name = FunctionUtils.getStringValue(keyName, env);
            Map<String, Object> rspEnv = getResponseEnv();
            if (rspEnv.containsKey(case_num)) {

                try {
                    String rsp = rspEnv.get(case_num).toString();
                    /*Pattern p = Pattern.compile(String.format("%s\":[\"]?([^\",]+)", key_name));
                    Matcher mm = p.matcher(rsp);
                    mm.find();*/
                    String value ="";
                    if(key_name.equals("AllResponse")){
                    	value=rsp.trim();
                    }else{
                        value = ResponseVerify.getTestValue(key_name,rsp);
                    }
                    if(value.length()==0){
                        String regExp = key_name+"[\\\\]*[\"]?[\\s]*:[\\s]*[\\\\]*[\"]?([^\",\\\\]+)";
                        Pattern p = Pattern.compile(regExp);
                        Matcher mm = p.matcher(rsp);
                        if(mm.find()){
                            value = mm.group(1);
                        }
                    }

                    return new AviatorString(value);

                } catch (IllegalStateException e) {
                    LogMan.getLoger().error(String.format("解析依赖函数‘ResponseDepend’未能找到Case:%s 对应的Payload数据", case_num));
                    System.out.println(String.format("解析依赖函数‘ResponseDepend’未能找到Case:%s 对应的Key：%s ", case_num, key_name));
                }

            } else {
                LogMan.getLoger().error(String.format("解析依赖函数‘ResponseDepend’未能找到Case:%s 对应的数据", case_num));
                System.out.println(String.format("解析依赖函数‘ResponseDepend’未能找到Case:%s 对应的数据", case_num));
            }
            return new AviatorString("");
        }

        public String getName() {
            return "ResponseDepend";
        }
    }
    
    public class DependencyReponseReleatedCurrent extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject caseNum, AviatorObject keyName) {
            String case_num = FunctionUtils.getStringValue(caseNum, env);
            String key_name = FunctionUtils.getStringValue(keyName, env);
            Map<String, Object> rspEnv = getResponseEnv();
            if (rspEnv.containsKey(case_num)) {

                try {
                    String rsp = rspEnv.get(case_num).toString();
                    /*Pattern p = Pattern.compile(String.format("%s\":[\"]?([^\",]+)", key_name));
                    Matcher mm = p.matcher(rsp);
                    mm.find();*/
                    String value ="";
                    if(key_name.equals("AllResponse")){
                    	value=rsp;
                    }else{
                        value = ResponseVerify.getTestValue(key_name,rsp);
                    }
                    if(value.length()==0){
                        String regExp = key_name+"[\\\\]*[\"]?[\\s]*:[\\s]*[\\\\]*[\"]?([^\",\\\\]+)";
                        Pattern p = Pattern.compile(regExp);
                        Matcher mm = p.matcher(rsp);
                        if(mm.find()){
                            value = mm.group(1);
                        }
                    }

                    return new AviatorString(value);

                } catch (IllegalStateException e) {
                    LogMan.getLoger().error(String.format("解析依赖函数‘ResponseDepend’未能找到Case:%s 对应的Payload数据", case_num));
                    System.out.println(String.format("解析依赖函数‘ResponseDepend’未能找到Case:%s 对应的Key：%s ", case_num, key_name));
                }

            } else {
                LogMan.getLoger().error(String.format("解析依赖函数‘ResponseDepend’未能找到Case:%s 对应的数据", case_num));
                System.out.println(String.format("解析依赖函数‘ResponseDepend’未能找到Case:%s 对应的数据", case_num));
            }
            return new AviatorString("");
        }

        public String getName() {
            return "ResponseCurrent";
        }
    }
    
    public class DependencyPostReleatedCurrent extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject caseNum, AviatorObject keyName) {
            String case_num = FunctionUtils.getStringValue(caseNum, env);
            String key_name = FunctionUtils.getStringValue(keyName, env);
            Map<String, Object> payloadEnv = getPayloadEnv();
    		String value = "";
            if (payloadEnv.containsKey(case_num)) {
                String payload = (String) payloadEnv.get(case_num);
               
                //String regExp = key_name + "[=]([\\w]*[^(,|})]*)";
                String regExp= String.format("%s\":[\"]?([^\",]+)", key_name);//小改，map中存放的是json字符串。
                Pattern p = Pattern.compile(regExp);
                Matcher mm = p.matcher(payload);
                mm.find();
                value = ResponseVerify.getTestValue(key_name,
						payload);
                if (value.length() == 0){
                	try {
						value = mm.group(1);
					} catch (Exception e) {
						LogMan.getLoger().info("PayloadDepend正则未能解析成功，进入下一个解析程序");
					}
                }
                
				/*try {
					value = mm.group(1);

				} catch (Exception e) {
					LogMan.getLoger().info("PayloadDepend正则未能解析成功，进入下一个解析程序");
				}

				if (value.length() == 0)// 先用正则取值，取不到再用递归遍历
				{
					value = VerificationKeyWordsImp.getTestValue(key_name,
							payload);
				}*/
				if(value.length() == 0){
					LogMan.getLoger().error(String
									.format("‘PayloadDepend’未能找到Case:%s , Key: %s 对应的数据数据",
											case_num, key_name));
				}
			
            } else {
                LogMan.getLoger().error(String.format("‘PayloadDepend’未能找到Case:%s 对应的数据", case_num));
            }
            return new AviatorString(value);
        }

        public String getName() {
            return "PayloadCurrent";
    
        }
    }
    
    public class DependencyPostReleated extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject caseNum, AviatorObject keyName) {
            String case_num = FunctionUtils.getStringValue(caseNum, env);
            String key_name = FunctionUtils.getStringValue(keyName, env);
            Map<String, Object> payloadEnv = getPayloadEnv();
    		String value = "";
            if (payloadEnv.containsKey(case_num)) {
                String payload = (String) payloadEnv.get(case_num);
               
                //String regExp = key_name + "[=]([\\w]*[^(,|})]*)";
                String regExp= String.format("%s\":[\"]?([^\",]+)", key_name);//小改，map中存放的是json字符串。
                Pattern p = Pattern.compile(regExp);
                Matcher mm = p.matcher(payload);
                mm.find();
                value = ResponseVerify.getTestValue(key_name,
						payload);
                if (value.length() == 0){
                	try {
						value = mm.group(1);
					} catch (Exception e) {
						LogMan.getLoger().info("PayloadDepend正则未能解析成功，进入下一个解析程序");
					}
                }
                
				/*try {
					value = mm.group(1);

				} catch (Exception e) {
					LogMan.getLoger().info("PayloadDepend正则未能解析成功，进入下一个解析程序");
				}

				if (value.length() == 0)// 先用正则取值，取不到再用递归遍历
				{
					value = VerificationKeyWordsImp.getTestValue(key_name,
							payload);
				}*/
				if(value.length() == 0){
					LogMan.getLoger().error(String
									.format("‘PayloadDepend’未能找到Case:%s , Key: %s 对应的数据数据",
											case_num, key_name));
				}
			
            } else {
                LogMan.getLoger().error(String.format("‘PayloadDepend’未能找到Case:%s 对应的数据", case_num));
            }
            return new AviatorString(value);
        }

        public String getName() {
            return "PayloadDepend";//PayloadDepend("case1","key")
    
        }
    }
   
    
    
    public class DependencyPostReleatedMulti extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject caseNum, AviatorObject keyName,AviatorObject num) {
            String case_num = FunctionUtils.getStringValue(caseNum, env);
            String key_name = FunctionUtils.getStringValue(keyName, env);
            String str_num = FunctionUtils.getStringValue(num, env);
            List <String> testValueList=new ArrayList<String>();
            Map<String, Object> payloadEnv = getPayloadEnv();
            String payload = (String) payloadEnv.get(case_num);
    		String value = "";
    		//大节点和平行节点条件混合使用做处理（#分隔）
    		String[] arrayBegin=str_num.split("#");
    		if(arrayBegin.length>1){
    			payload = ResponseVerify.getTestValue(arrayBegin[0],payload);
    			str_num = arrayBegin[1];
    		}

    		//判断1.用数字选取同名节点，还是2.用平行节点=某值，或用3.大节点来选取
            if(ResponseVerify.isNumeric(str_num)){
                int int_num=Integer.parseInt(str_num);
                if (payloadEnv.containsKey(case_num)) {
                    testValueList = ResponseVerify.getTestValueMulti(key_name,
    						payload);
                    try {
    					value=testValueList.get(int_num);
    				} catch (Exception e1) {
    					// TODO Auto-generated catch block
    					value="";
    					e1.printStackTrace();
    					LogMan.getLoger().error("num参数值过大溢出");
    				}
                    if (value.length() == 0){
                            String regExp= String.format("%s\"[\\s]*:[\\s]*[\"]?([^\",]+)", key_name);//小改，map中存放的是json字符串。
                            Pattern p = Pattern.compile(regExp);
                            Matcher mm = p.matcher(payload);
                            if(mm.find()){
                            	value = mm.group(1);
                            }else{
        						LogMan.getLoger().info("PayloadDependMulti正则未能解析成功，进入下一个解析程序");
                            }
                    }
                }
            } else if(str_num.contains("=")){
            	String[] array = str_num.split("=");
            	String indexKey = array[0];
            	String indexValue = array[1];
                List <String> indexValueList=new ArrayList<String>();
                indexValueList = ResponseVerify.getTestValueMulti(indexKey,
						payload);
                testValueList = ResponseVerify.getTestValueMulti(key_name,
						payload);
                for(int i=0;i<indexValueList.size();i++){
                	if(indexValue.equals(indexValueList.get(i))){
                		try {
							value = testValueList.get(i);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
                }
            	
            }else{
            	String fatherValue = ResponseVerify.getTestValue(str_num,payload);
            	value = ResponseVerify.getTestValue(key_name,fatherValue);
            }
           //返回最后的结果日志
			if(value.length() == 0){
					LogMan.getLoger().error(String
									.format("‘PayloadDependMulti’未能找到Case:%s , Key: %s 对应的数据数据",
											case_num, key_name));
				}
            return new AviatorString(value);
        }

        public String getName() {
            return "PayloadDependMulti";//PayloadDepend("case1","key")
    
        }
    }
    
    
    public class DependencyReponseReleatedMulti extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject caseNum, AviatorObject keyName,AviatorObject num) {
            String case_num = FunctionUtils.getStringValue(caseNum, env);
            String key_name = FunctionUtils.getStringValue(keyName, env);
            String str_num = FunctionUtils.getStringValue(num, env);
            List <String> testValueList=new ArrayList<String>();
            Map<String, Object> rspEnv = getResponseEnv();
            String payload = rspEnv.get(case_num).toString();
    		String value = "";

    		//大节点和平行节点条件混合使用做处理（#分隔）
    		String[] arrayBegin=str_num.split("#");
    		if(arrayBegin.length>1){
    			payload = ResponseVerify.getTestValue(arrayBegin[0],payload);
    			str_num = arrayBegin[1];
    		}

    		//判断1.用数字选取同名节点，还是2.用平行节点=某值，或用3.大节点来选取
            if(ResponseVerify.isNumeric(str_num)){
                int int_num=Integer.parseInt(str_num);
                if (rspEnv.containsKey(case_num)) {
                    testValueList = ResponseVerify.getTestValueMulti(key_name,
    						payload);
                    try {
    					value=testValueList.get(int_num);
    				} catch (Exception e1) {
    					// TODO Auto-generated catch block
    					value="";
    					e1.printStackTrace();
    					LogMan.getLoger().error("num参数值过大溢出");
    				}
                    if (value.length() == 0){
                            String regExp= String.format("%s\"[\\s]*:[\\s]*[\"]?([^\",]+)", key_name);//小改，map中存放的是json字符串。
                            Pattern p = Pattern.compile(regExp);
                            Matcher mm = p.matcher(payload);
                            if(mm.find()){
                            	value = mm.group(1);
                            }else{
        						LogMan.getLoger().info("PayloadDependMulti正则未能解析成功，进入下一个解析程序");
                            }
                    }
                }
            } else if(str_num.contains("=")){
            	String[] array = str_num.split("=");
            	String indexKey = array[0];
            	String indexValue = array[1];
                List <String> indexValueList=new ArrayList<String>();
                indexValueList = ResponseVerify.getTestValueMulti(indexKey,
						payload);
                testValueList = ResponseVerify.getTestValueMulti(key_name,
						payload);
                //目标变量个数和定位变量个数不相等时做特殊处理
                Double times=1.0;
                Double sizeIndex =  (double) indexValueList.size();
                Double sizeTest  =  (double) testValueList.size();
                if(indexValueList.size()%testValueList.size()==0||testValueList.size()%indexValueList.size()==0){
                	times = sizeTest/sizeIndex;
                }
                for(int i=0;i<indexValueList.size();i++){
                	if(indexValue.equals(indexValueList.get(i))){
                		try {
							value = testValueList.get((int) (i*times));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
                }
            	
            }else{
            	String fatherValue = ResponseVerify.getTestValue(str_num,payload);
            	value = ResponseVerify.getTestValue(key_name,fatherValue);
            }
           //返回最后的结果日志
			if(value.length() == 0){
					LogMan.getLoger().error(String
									.format("‘PayloadDependMulti’未能找到Case:%s , Key: %s 对应的数据数据",
											case_num, key_name));
				}
            return new AviatorString(value);
        }

        public String getName() {
            return "ResponseDependMulti";
        }
    }
    
   /* public class DependencyPostCurrent extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject keyName,AviatorObject Excelpath, AviatorObject sheetname,AviatorObject currentRow) {
            String key_name = FunctionUtils.getStringValue(keyName, env);
            String path = FunctionUtils.getStringValue(Excelpath, env);
            String sheet = FunctionUtils.getStringValue(sheetname, env);
            String row =FunctionUtils.getStringValue(currentRow, env);
            ParamUtil paramutil = new ParamUtil();
            Map<String, String[]> mapData = new HashMap<String, String[]>();
            try {
				mapData = XLSUtils.getXlsData(path, sheet, 4);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            String case_num = paramutil.getCaseDataDTO(mapData, Integer.parseInt(row));
            Map<String, Object> payloadEnv = getPayloadEnv();
    		String value = "";
            if (payloadEnv.containsKey(case_num)) {
                String payload = (String) payloadEnv.get(case_num);
                //String regExp = key_name + "[=]([\\w]*[^(,|})]*)";
                String regExp= String.format("%s\":[\"]?([^\",]+)", key_name);//小改，map中存放的是json字符串。
                Pattern p = Pattern.compile(regExp);
                Matcher mm = p.matcher(payload);
                mm.find();
				try {
					value = mm.group(1);

				} catch (Exception e) {
					LogMan.getLoger().info("PayloadDepend正则未能解析成功，进入下一个解析程序");
				}

				if (value.length() == 0)// 先用正则取值，取不到再用递归遍历
				{
					value = ResponseVerify.getTestValue(key_name,
							payload);
				}
				if(value.length() == 0){
					LogMan.getLoger().error(String
									.format("‘PayloadDepend’未能找到Case:%s , Key: %s 对应的数据数据",
											case_num, key_name));
				}
			
            } else {
                LogMan.getLoger().error(String.format("‘PayloadDepend’未能找到Case:%s 对应的数据", case_num));
            }
            return new AviatorString(value);
        }

        public String getName() {
            return "PayloadCurrent";//@PayloadCurrent("key")    
        }
    }*/
    
    public class MyLocalExcelData extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env,AviatorObject key,AviatorObject sheetName,AviatorObject pathExcel,AviatorObject beginRowNum,AviatorObject currentRowNum) {
            
            String sheet_Name = FunctionUtils.getStringValue(sheetName, env);
            String my_key = FunctionUtils.getStringValue(key, env);
            String path_Excel = FunctionUtils.getStringValue(pathExcel, env);
            String row_beginNum = FunctionUtils.getStringValue(beginRowNum, env);
            String current_RowNum = FunctionUtils.getStringValue(currentRowNum, env);
            String value="";
            try {
				int row_intNum=Integer.parseInt(row_beginNum);
				int current_intRowNum =Integer.parseInt(current_RowNum);
				Map<String,String[]> map=new HashMap<String,String[]>();
				map=XLSUtils.getXlsData(path_Excel, sheet_Name, row_intNum);
				System.out.println(map.keySet().toString());
				String testKey[]=map.get(my_key);
				value=testKey[current_intRowNum];			
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
        	return new AviatorString(value);
        }

        public String getName() {
            return "GetLocalExcelData";
            
        }
    }
    
    //读取另一个sheet中数据
    public class ExcelData extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env,AviatorObject key,AviatorObject sheetName,AviatorObject pathExcel,AviatorObject beginRowNum,AviatorObject currentRowNum,AviatorObject testEnv) {
            String sheet_Name = FunctionUtils.getStringValue(sheetName, env);
            String my_key = FunctionUtils.getStringValue(key, env);
            String path_Excel = FunctionUtils.getStringValue(pathExcel, env);
            String row_beginNum = FunctionUtils.getStringValue(beginRowNum, env);
            String current_RowNum = FunctionUtils.getStringValue(currentRowNum, env);
            String test_env = FunctionUtils.getStringValue(testEnv, env);

            String value="";
            try {
				int row_intNum=Integer.parseInt(row_beginNum);
				int current_intRowNum =Integer.parseInt(current_RowNum);
				Map<String,String[]> map=new HashMap<String,String[]>();
				map=XLSUtils.getXlsData(path_Excel, sheet_Name, row_intNum);
				System.out.println(map.keySet().toString());
				String testKey[]=map.get(my_key);
				value=testKey[current_intRowNum].trim();
				if(value.length()==0){
					if("uat".equalsIgnoreCase(test_env)){
						value=testKey[2];
					}else if("itest".equalsIgnoreCase(test_env)){
						value=testKey[1];
					}else{
						value=testKey[0];
					}
				}
				if(value.length()==0){
					value=testKey[0];
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
        	return new AviatorString(value);
        }

        public String getName() {
            return "GetExcelData";
            
        }
    }

    public class SleepTime extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env,AviatorObject time) {
            
            String timeStr = FunctionUtils.getStringValue(time, env);
			double timeInt =Double.parseDouble(timeStr);
			double sleeptime = timeInt*1000;
			try {
				Thread.sleep((long) sleeptime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				timeStr="RSleep执行失败;\n";
			}
			timeStr="已等待"+timeStr+"秒;\n";
        	return new AviatorString(timeStr);
        }

	    public String getName() {
	        return "RSleep";
	        
	    }
    }
    public class SleepLongTime extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env,AviatorObject time) {
            
            String timeStr = FunctionUtils.getStringValue(time, env);
			int timeInt =Integer.parseInt(timeStr);
			DateGenerator dateGenerator=new DateGenerator();
			boolean bool=dateGenerator.sleepLong(timeInt);
			if(bool){
				timeStr="已等待"+timeStr+"分钟;\n";
			}else{
				timeStr="sleepL异常！";
			}
        	return new AviatorString(timeStr);
        }

	    public String getName() {
	        return "RSleepL";
	        
	    }
    }  

    public class HttpRequest extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env,AviatorObject Url,AviatorObject CharSet) {
            
            String url = FunctionUtils.getStringValue(Url, env);
            String charSet = FunctionUtils.getStringValue(CharSet, env);
            int num = url.indexOf("?");
            String urlNew = url.substring(0, num);
            String formData = url.substring(num+1);
            String reponse="";
			try {
				reponse = HttpClientA.postForm(urlNew, formData, charSet);
				/*CloseableHttpResponse closehttpresponse = HttpClientUtils.testWithKeyValue(null, null, url, "GET", null);
				 reponse = HttpResponseUtils.getResponseContent(closehttpresponse);*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				reponse="Http请求异常;\n";
			}
        	return new AviatorString(reponse);
        }

    public String getName() {
        return "HttpGet";
        
    }
}
    
    public class getJson extends ExpressionRegister {

      	 @Override
           public AviatorObject call(Map<String, Object> env) {
               GetJson n = new GetJson();
               String json = n.getJson();
               return new AviatorString(json);
           }

           public String getName() {
               return "RJson";
           }
      }
  
    public class JSONKeyValueEquals extends ExpressionRegister {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject json, AviatorObject key, AviatorObject value) {
            String jsonStr = FunctionUtils.getStringValue(json, env);
            String keyStr = FunctionUtils.getStringValue(key, env);
            String valueStr = FunctionUtils.getStringValue(value, env);
            ResponseVerify v = new ResponseVerify();
            boolean verificationJson = v.verifyJSONKeyValueEquals(jsonStr, keyStr, valueStr);
            String vcJson = String.valueOf(verificationJson);
            return new AviatorString(vcJson);
        }
        public String getName() {
            return "RKeyJsonValueEquals";
        }
    }
    public class JSONKeyValueNotEquals extends ExpressionRegister {
        
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject json, AviatorObject key, AviatorObject value) {
            String jsonStr = FunctionUtils.getStringValue(json, env);
            String keyStr = FunctionUtils.getStringValue(key, env);
            String valueStr = FunctionUtils.getStringValue(value, env);
            ResponseVerify v = new ResponseVerify();
            boolean verificationJson = v.verifyJSONKeyValueNotEquals(jsonStr, keyStr, valueStr);
            String vcJson = String.valueOf(verificationJson);
            return new AviatorString(vcJson);
        }
        
        public String getName() {
            return "RKeyJsonValueNotEquals";
        }
    }
    public class JSONKeyValueContains extends ExpressionRegister {
        
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject json, AviatorObject key, AviatorObject value) {
            String jsonStr = FunctionUtils.getStringValue(json, env);
            String keyStr = FunctionUtils.getStringValue(key, env);
            String valueStr = FunctionUtils.getStringValue(value, env);
            ResponseVerify v = new ResponseVerify();
            boolean verificationJson = v.verifyJSONKeyValueContains(jsonStr, keyStr, valueStr);
            String vcJson = String.valueOf(verificationJson);
            return new AviatorString(vcJson);
        }
        
        public String getName() {
            return "RKeyJsonValueContains";
        }
    }
    
    
    
    public class JSONKeyValueNotNull extends ExpressionRegister {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject json, AviatorObject key) {
            String jsonStr = FunctionUtils.getStringValue(json, env);
            String keyStr = FunctionUtils.getStringValue(key, env);
            ResponseVerify v = new ResponseVerify();
            boolean verificationJson = v.verifyJSONKeyValueNotNull(jsonStr, keyStr);
            String vcJson = String.valueOf(verificationJson);
            return new AviatorString(vcJson);
        }
        
        public String getName() {
            return "RKeyJsonValueNotNull";
        }
    }
    
    public class Pay extends ExpressionRegister {
        @Override
        public AviatorObject call(Map<String, Object> map, AviatorObject uIenv, AviatorObject outTradeNo, AviatorObject amt) {
        	String env = FunctionUtils.getStringValue(uIenv, map);
            String orderNo = FunctionUtils.getStringValue(outTradeNo, map);
            String sum = FunctionUtils.getStringValue(amt, map);
            MockPay v = new MockPay();
            String mPay = "";
			try {
				mPay = v.pay(env, orderNo, sum);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return new AviatorString(mPay);
        }
        
        public String getName() {
            return "RPay";
        }
    }
    
    public class Idb extends ExpressionRegister {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject dbenv, AviatorObject dbname,AviatorObject tbname,AviatorObject sql,AviatorObject column) {
            String environment = FunctionUtils.getStringValue(dbenv, env);
            String databasename = FunctionUtils.getStringValue(dbname, env);
            String tablename = FunctionUtils.getStringValue(tbname, env);
            String sqlname = FunctionUtils.getStringValue(sql, env);
            String columnname = FunctionUtils.getStringValue(column, env);
            
            String queryStr,queryfield ="";
			try {
//				queryStr = IDBQuery.idbString(environment, databasename, tablename, sqlname);
//				queryfield = IDBQuery.getTableSql(columnname, queryStr);
		        BaseSqlQueryResult result = IDBQueryResults.getQueryResult(environment, databasename, tablename, sqlname);
		        String code = result.getCode();
		        if ("0".equals(code)) {
			        List<SqlQueryResultContent> resultContent = result.getContentList();
		            String dataStr = resultContent.get(0).getResultContent().toString();
		        	JSONArray jsonArray = JSONArray.parseArray(dataStr);
		        	String dataStrNew = jsonArray.get(0).toString();
		            queryfield = ResponseVerify.getTestValue(columnname, dataStrNew);
		        }else{
		        	queryfield = result.getErrorMsg();
		        }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}                
            return new AviatorString(queryfield);
        }
        
        public String getName() {
            return "RIdb";
        }
    }
    
    public class CommonSSOlogin extends ExpressionRegister {
        @Override
        public AviatorObject call(Map<String, Object> env,  AviatorObject username,AviatorObject password) {
            String userName = FunctionUtils.getStringValue(username, env);
            String passWord = FunctionUtils.getStringValue(password, env);
            String cookie = CommonSSOLogin.requestCookie(userName, passWord);
            cookie = "_za_sso_session_="+cookie;
            return new AviatorString(cookie);
        }
        public String getName() {
            return "RssoLogin";
        }
    }
    
    public class EncoderByMd5 extends ExpressionRegister {
        @Override
        public AviatorObject call(Map<String, Object> env,  AviatorObject sstr) {
            String str = FunctionUtils.getStringValue(sstr, env);
            String md5Str = DigestUtils.md5Hex(str);
            return new AviatorString(md5Str);
        }
        public String getName() {
            return "Rmd5";
        }
    }
    public class QueryPolicy extends ExpressionRegister{
    	 public AviatorObject call(Map<String, Object> env,AviatorObject test_env,AviatorObject polno,AviatorObject field){
    		 String querypolicyNo= FunctionUtils.getStringValue(polno, env);
    		 String testenv =  FunctionUtils.getStringValue(test_env, env);
    		 String queryfield =  FunctionUtils.getStringValue(field, env);
    		 CDCSearchPolicy searchPolicy = new CDCSearchPolicy(testenv);
    		
    		 String policyRespone="";
			try {
				policyRespone = searchPolicy.queryPolicyList(querypolicyNo,queryfield);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		String dbpolicyNo = ResponseVerify.getTestValue("policyNo", policyRespone);
    		return new AviatorString(dbpolicyNo);
    	 }
    	 public String getName() {
             return "QueryPolicyNo";
         }
    }
    
    
    public class SqlQueryReleated extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject tbnamePath, AviatorObject columnName, AviatorObject num) {
            String tb_namePath = FunctionUtils.getStringValue(tbnamePath,env);
            String column_name = FunctionUtils.getStringValue(columnName,env);
            String column_num = FunctionUtils.getStringValue(num,env);
            int numInt=Integer.parseInt(column_num);
            Map<String,Object> sqlResultMap=ExpressionRegister.getSqlSelectEnv();
            String oneResult = (String) sqlResultMap.get(tb_namePath);
            String value="未查询到结果";
			try {
				value = IDBQuery.getTableColumnSet(column_name,oneResult).get(numInt);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
            return new AviatorString(value);
        }

        public String getName() {
            return "SqlQueryDepend";
        }
    }
    
    public class SqlQuery extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject Sql) {
            String sql = FunctionUtils.getStringValue(Sql,env);
    		String driver = "com.mysql.jdbc.Driver";
    		// URL指向要访问的数据库名scutcs
    		String url = "jdbc:mysql://rds6fbqrf6fbqrf.mysql.rds.aliyuncs.com/auto_test_00";
    		// MySQL配置时的用户名
    		String user = "auto_test";
    		// Java连接MySQL配置时的密码
    		String password = "auto_test_9ac5ee";
    		String result="";
			try {
				// 加载驱动程序
				Class.forName(driver);
	    		// 连接数据库
	    		Connection conn = DriverManager.getConnection(url, user, password);
	    		Statement stmt = conn.createStatement(); //创建Statement对象
	    		ResultSet rs = stmt.executeQuery(sql);//创建数据对象
	    		ResultSetMetaData data = rs.getMetaData();  
	    		rs.next();
	    		HashMap<String, String> map = new HashMap<String, String>();  
	    		if(data.getColumnCount()<2){
	    			result = rs.getString(1);
	    		}else{
		              for (int i = 1; i <= data.getColumnCount(); i++) {// 数据库里从 1 开始  
		                  String c = data.getColumnName(i);  
		                  String v = rs.getString(c);  
		                  map.put(c, v);  
		              }  
		    		result = map.toString();
	    		}
	            rs.close();  
	            stmt.close();  
	            conn.close();  
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
            return new AviatorString(result);
        }

        public String getName() {
            return "Select";
        }
    }
    
    public class SqlInsert extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject Sql) {
        	String sql = FunctionUtils.getStringValue(Sql,env);
        	sql = sql.replace("[", "(").replace("]", ")");
    		String driver = "com.mysql.jdbc.Driver";
    		// URL指向要访问的数据库名scutcs
    		String url = "jdbc:mysql://rds6fbqrf6fbqrf.mysql.rds.aliyuncs.com/auto_test_00";
    		// MySQL配置时的用户名
    		String user = "auto_test";
    		// Java连接MySQL配置时的密码
    		String password = "auto_test_9ac5ee";
    		String result="";
			try {
				// 加载驱动程序
				Class.forName(driver);
	    		// 连接数据库
	    		Connection conn = DriverManager.getConnection(url, user, password);
	    		Statement stmt = conn.createStatement(); //创建Statement对象
	    		boolean rs = stmt.execute(sql);
	    		if(rs){
	    			result="InsertInto sql:" + sql+"插入失败！";
	    		}else{
	    			result="InsertInto sql:" + sql+"插入成功！";
	    		}
	            stmt.close();  
	            conn.close();  
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
            return new AviatorString(result);
        }

        public String getName() {
            return "InsertInto";
        }
    }
    
    public class getTestValueByKeyName extends ExpressionRegister {

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject keyName,AviatorObject Str) {
        	String key_name = FunctionUtils.getStringValue(keyName,env);
        	String dataStr = FunctionUtils.getStringValue(Str,env);
        	String value="";
        	try {
				value = ResponseVerify.getTestValue(key_name, dataStr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return new AviatorString(value);
        }
        public String getName() {
            return "getTestValue";
        }
    }
    
}

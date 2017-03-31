package com.za.qa.verify;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.za.qa.analyze.DataAnalyze;
import com.za.qa.constants.CaseStatus;
import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseDataDTO;
import com.za.qa.dto.CaseReportDTO;
import com.za.qa.dto.CaseAfterRunDTO;
import com.za.qa.dto.CheckResultDTO;
import com.za.qa.utils.ClientUtils;

/**
 * 
 * 接口递归解析获取 key vaule
 * 
 * @author huanghuisong
 *
 */
public class ResponseVerify {

	private static boolean isJsonObject(String json) {
		return isJson(json, false);
	}

	private static boolean isJsonArray(String json) {
		return isJson(json, true);
	}

	private static boolean isJson(String json, boolean isArray) {
		try {
			if (json == null || json.trim().equals(""))
				return false;
			if (isArray)
				JSONArray.parseArray(json);
			else
				JSONObject.parseObject(json);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private static boolean isJson(String json) {
		return isJsonObject(json) || isJsonArray(json);
	}

	/**
	 * @param jsonStr
	 *            传进来的json接口
	 * @param keyName
	 *            key
	 * @param target
	 *            value
	 * @return
	 */
	public static boolean verifyJSONKeyValueEquals(String jsonStr,
			String keyName, String target) {
		if (isJsonObject(jsonStr)) {
			JSONObject jsonObject = JSONObject.parseObject(jsonStr);
			// System.out.println("--------jsonObject: " + jsonObject);
			for (String k : jsonObject.keySet()) {
				Object value = jsonObject.get(k);
				if (keyName.equals(k)) {
					System.out.println("--------key: " + k + ", value: "
							+ value);
					if (value.equals(target)) {
						return true;
					}
				} else {
					if (value != null && isJson(value.toString())) {
						if (verifyJSONKeyValueEquals(value.toString(), keyName,
								target)) {
							return true;
						}
					}
				}
			}
		} else if (isJsonArray(jsonStr)) {
			JSONArray jsonArray = JSONArray.parseArray(jsonStr);
			// System.out.println("--------jsonArray: " + jsonArray);
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (o != null && isJson(o.toString())) {
					if (verifyJSONKeyValueEquals(o.toString(), keyName, target))
						return true;
				}
			}
		} else {
			return false;
		}
		return false;
	}

	public static boolean verifyJSONKeyValueNotEquals(String jsonStr,
			String keyName, String target) {
		if (isJsonObject(jsonStr)) {
			JSONObject jsonObject = JSONObject.parseObject(jsonStr);
			// System.out.println("--------jsonObject: " + jsonObject);
			for (String k : jsonObject.keySet()) {
				Object value = jsonObject.get(k);
				if (keyName.equals(k)) {
					System.out.println("--------key: " + k + ", value: "
							+ value);
					if (!value.equals(target)) {
						return true;
					} else {
						return false;
					}
				} else {
					if (value != null && isJson(value.toString())) {
						if (verifyJSONKeyValueNotEquals(value.toString(),
								keyName, target)) {
							return true;
						}
					}
				}
			}
		} else if (isJsonArray(jsonStr)) {
			JSONArray jsonArray = JSONArray.parseArray(jsonStr);
			// System.out.println("--------jsonArray: " + jsonArray);
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (o != null && isJson(o.toString())) {
					if (verifyJSONKeyValueNotEquals(o.toString(), keyName,
							target))
						return true;
				}
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * verifyJSONKeyValueContains 值包含某个字符返回true
	 * 
	 * @param jsonStr
	 * @param keyName
	 * @param target
	 * @return
	 */
	public static boolean verifyJSONKeyValueContains(String jsonStr,
			String keyName, String target) {
		if (isJsonObject(jsonStr)) {
			JSONObject jsonObject = JSONObject.parseObject(jsonStr);
			// System.out.println("--------jsonObject: " + jsonObject);
			for (String k : jsonObject.keySet()) {
				Object value = jsonObject.get(k);
				if (keyName.equals(k)) {
					System.out.println("--------key: " + k + ", value: "
							+ value);
					if (value.toString().contains(target) && !target.equals("")) {
						return true;
					} else {
						return false;
					}
				} else {
					if (value != null && isJson(value.toString())) {
						if (verifyJSONKeyValueContains(value.toString(),
								keyName, target)) {
							return true;
						}
					}
				}
			}
		} else if (isJsonArray(jsonStr)) {
			JSONArray jsonArray = JSONArray.parseArray(jsonStr);
			// System.out.println("--------jsonArray: " + jsonArray);
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (o != null && isJson(o.toString())) {
					if (verifyJSONKeyValueContains(o.toString(), keyName,
							target))
						return true;
				}
			}
		} else {
			return false;
		}
		return false;
	}

	/**
	 * key的值为null返回false不为null返回true
	 * 
	 * @param jsonStr
	 * @param keyName
	 * @param target
	 * @return
	 */
	public static boolean verifyJSONKeyValueNotNull(String jsonStr, String keyName) {

		if (isJsonObject(jsonStr)) {
			JSONObject jsonObject = JSONObject.parseObject(jsonStr);
			// System.out.println("--------jsonObject: " + jsonObject);
			for (String k : jsonObject.keySet()) {
				Object value = jsonObject.get(k);
				if (keyName.equals(k)) {
					System.out.println("--------key: " + k + ", value: " + value);
					if (!value.equals("") && value != null && !value.equals(" ")) {
						return true;
					} else {
						return false;
					}
				} else {
					if (value != null && isJson(value.toString())) {
						if (verifyJSONKeyValueNotNull(value.toString(), keyName)) {
							return true;
						}
					}
				}
			}
		} else if (isJsonArray(jsonStr)) {
			JSONArray jsonArray = JSONArray.parseArray(jsonStr);
			// System.out.println("--------jsonArray: " + jsonArray);
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (o != null && isJson(o.toString())) {
					if (verifyJSONKeyValueNotNull(o.toString(), keyName))
						return true;
				}
			}
		} else {
			return false;
		}
		return false;
	}

	public static void main(String[] args) {
		String json = "{\"bizContent\":{\"beginDate\":\"2016-06-25 02:20:33\",\"channelOrderId\":\"TbVaIqUhGi\",\"endDate\":\"2016-06-25 22:20:33\",\"errorMsg\":\"\",\"msgCode\":\"1\",\"policyNo\":\"827701502283041311\",\"premium\":\"2\",\"sumInsured\":\"54000\"},\"charset\":\"UTF-8\",\"format\":\"json\",\"signType\":\"RSA\",\"timestamp\":\"20160927173847772\"}";
		String json2 = "{\"bizContent\":[{\"beginDate\":\"2016-06-25 02:20:33\",\"channelOrderId\":\"TbVaIqUhGi\",\"endDate\":\"2016-06-25 22:20:33\",\"errorMsg\":\"\",\"msgCode\":\"1\",\"policyNo\":\"827701502283041311\",\"premium\":\"2\",\"sumInsured\":\"54000\"},1,2],\"charset\":\"UTF-8\",\"format\":\"json\",\"signType\":\"RSA\",\"timestamp\":\"20160927173847772\"}";

		String test = " {\"bizContent\":{\"failureReason\":\"\",\"items\":[{\"certiNo\":\"T872809928\",\"value\":{\"policyNo\":\"837901502283034760\",\"policyUrl\":\"http: //114.55.158.217: 6080/open/common/downloadFileScreen/downloadDoc.do?data=afec37288e8161928f1189b0b4c96860565954cd7e52dd392a1a0d28853d330d\"}},{\"certiNo\":\"jozzdlnkaq\",\"value\":{\"policyNo\":\"827901502283034890\",\"policyUrl\":\"http: //114.55.158.217: 6080/open/common/downloadFileScreen/downloadDoc.do?data=bcb905201add8e3fc53fadf9f7b025410377deb795e6a9902e47d705e70d6ce8\"}},{\"certiNo\":\"kzpdpbqigy\",\"value\":{\"policyNo\":\"827901502283034923\",\"policyUrl\":\"http: //114.55.158.217: 6080/open/common/downloadFileScreen/downloadDoc.do?data=bcb905201add8e3ff45a675735ee5d270ee7eb5b3ad6a10f3c6f51ebb3f3f9f9\"}},{\"certiNo\":\"rljgpbsbov\",\"value\":{\"policyNo\":\"827901502283035053\",\"policyUrl\":\"http: //114.55.158.217: 6080/open/common/downloadFileScreen/downloadDoc.do?data=bcb905201add8e3f03d962139115a0c6dcb4d93d3fb81701a4b0c783415d6432\"}}],\"result\":\"0\",\"transactionId\":\"diuwcbhgvf\"},\"charset\":\"UTF-8\",\"format\":\"json\",\"signType\":\"RSA\",\"timestamp\":\"20160927155749275\"}";
		String llll = "{\"timestamp\":\"20160928172729584\",\"signType\":\"RSA\",\"charset\":\"UTF-8\",\"format\":\"json\",\"bizContent\":{\"transactionId\":\"wdfsfegdsfgdf\",\"items\":\"[{\\\"certiNo\\\":\\\"T872809928\\\",\\\"value\\\":{\\\"policyNo\\\":\\\"837901502292013054\\\",\\\"policyUrl\\\":\\\"http://114.55.158.217:6080/open/common/downloadFileScreen/downloadDoc.do?data=afec37288e81619286b7569d40cfbc3c68e05ad6f8cbbd932a1a0d28853d330d\\\"}},{\\\"certiNo\\\":\\\"gdrfgsregs\\\",\\\"value\\\":{\\\"policyNo\\\":\\\"827901502292013184\\\",\\\"policyUrl\\\":\\\"http://114.55.158.217:6080/open/common/downloadFileScreen/downloadDoc.do?data=bcb905201add8e3f7b7effe793f54da623c612abb6eb1a9d76451393e1b5042c\\\"}},{\\\"certiNo\\\":\\\"fnbgfjngfdhnb\\\",\\\"value\\\":{\\\"policyNo\\\":\\\"827901502292013217\\\",\\\"policyUrl\\\":\\\"http://114.55.158.217:6080/open/common/downloadFileScreen/downloadDoc.do?data=bcb905201add8e3f9c4e88c3fbbc07f245e8c877993742655c28d87b76b61e7e2c692fb43b0daead\\\"}},{\\\"certiNo\\\":\\\"gfdhfghfzd\\\",\\\"value\\\":{\\\"policyNo\\\":\\\"827901502292013347\\\",\\\"policyUrl\\\":\\\"http://114.55.158.217:6080/open/common/downloadFileScreen/downloadDoc.do?data=bcb905201add8e3f7ed6538b6cb3359a6dad45cd4b33c92de86a459400bbcdcb\\\"}}]\",\"failureReason\":\"\",\"result\":\"0\"}}";
		String fuza = "{\"a\": \"vv\",\"biggz\": {\"ah\": \"gb\",\"ijjt\": [{\"hch\": \"bjhjhgg\",\"kja\": \"gHFGHFGHfs\",\"njjo\": \"g45485135456\",\"yhjhgh\": [{\"ejh\": \"jhgr\",\"qjhgj\": \"jfghw\",\"ttjfgh\": [{\"rguu\": \"ujhgu\",\"rhgr\": \"hguu\",\"tjft\": \"ujju\"}]}]}],\"v\": \"c\"},\"biz\": {\"a\": \"b\",\"it\": [{\"a\": \"s\",\"c\": \"b\",\"no\": \"45485135456\",\"yhh\": [{\"e\": \"r\",\"q\": \"w\",\"tt\": [{ \"rr\": \"uu\", \"ruu\": \"uu\",\"tt\": \"uu\"}]} ]}], \"v\": \"c\"}}";
		String lll = "{\"bizContent\":{\"beginDate\":\"2016-06-25 02:20:33\",\"channelOrderId\":\"CdRsBnXrUc\",\"endDate\":\"2016-06-25 22:20:33\",\"errorMsg\":\"\",\"msgCode\":\"1\",\"policyNo\":\"827701502283041571\",\"premium\":\"2\",\"sumInsured\":\"54000\"},\"charset\":\"UTF-8\",\"format\":\"json\",\"signType\":\"RSA\",\"timestamp\":\"20160927173901653\"}";
		String key = "channelOrderId";
		String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <AuibInsuranceRequest> <Schedule> <AirlineNo>781</AirlineNo> <IataNo>08677349</IataNo> <OfficeCode>sha777b</OfficeCode> ";
		String checkPoint = "nno==45485135456" + "\n" + "chgf==b" + "\n"
				+ "it>>chgf";

		String cf = "{\"a\": \"vv\",\"bfiz\": {\"a\": \"b\",\"it\": [{\"aggf\": \"s\",\"chgf\": \"b\",\"nno\": \"45485135456\",\"yhcgh\": [{\"fde\": \"r\",\"gsdfq\": \"w\",\"tgt\": [{\"rdfgr\": \"usdu\",\"rdguu\": \"ugfu\",\"ttsfg\": \"ugfdu\"}]}]}],\"v\": \"c\"},\"bigfgz\": {\"ag\": \"b\",\"its\": [{\"a\": \"s\",\"c\": \"b\",\"no\": \"45485135456\",\"yhh\": [{\"e\": \"r\",\"q\": \"w\",\"tt\": [{\"rr\": \"uu\",\"ruu\": \"uu\",\"tt\": \"uu\"}]}]}],\"v\": \"c\"},\"biggz\": {\"adh\": \"ghgb\",\"ijhgjt\": [{\"hrch\": \"bg\",\"keja\": \"gfs\",\"njzsdjo\": \"g45485135456\",\"ythh\": [{\"eghh\": \"jhgr\",\"qjgj\": \"jfghw\",\"tgh\": [{\"rghgr\": \"hguu\",\"rsdguu\": \"ujhgu\",\"tjft\": \"ujju\"}]}]}],\"itt\": [{\"hrch\": [{\"hrch\": \"bg\",\"keja\": \"gfs\",\"njzsdjo\":\"g45485135456\",\"ythh\":[{\"eghh\": \"jhgr\",\"qjgj\": \"jfghw\",\"tgh\":[{\"rghgr\":[{\"hrch\": \"bg\",\"keja\": \"gfs\",\"njzsdjo\": \"g45485135456\",\"ythh\": [{\"eghh\": \"jhgr\",\"qjgj\": \"jfghw\",\"tgh\": [{\"rghgr\": \"hguu\",\"rsdguu\": \"ujhgu\",\"tjft\": \"ujju\"}]}]}],\"rsdguu\": \"ujhgu\",\"tjft\": \"ujju\"}]}]}],\"keja\": \"gfs\",\"njzsdjo\": \"g45485135456\",\"ythh\": [{\"eghh\": \"jhgr\",\"qjgj\": \"jfghw\",\"tgh\": [{\"rghgr\": \"hguu\",\"rsdguu\": \"ujhgu\",\"tjft\": \"ujju\"}]}]}],\"v\": \"c\"}}";

		System.out.println(verifyJSONKeyValueNotNull(cf, "eghh"));
		System.out.println(verifyJSONKeyValueContains(cf, "rghgr", "jhk"));
		System.out.println(verifyJSONKeyValueNotEquals(cf, "rsdguu", "ujhg"));
		System.out.println(verifyJSONKeyValueEquals(cf, "njzsdjo",
				"g45485135456"));
		// System.out.println("test: "+getJSONKeyValue(cf,"it"));
		System.out.println("test-1: " + getTestValue("aggf", cf));
		System.out.println("test1: " + getTestValue("it", cf));
		//System.out.println("test2: " + check(checkPoint, cf));
		System.out.println("test3: " + getTestValue("IataNo", xml1));

	}

	/**
	 * 根据key取dataStr字符串中对应的value
	 * 
	 * @param dataStr必须是json串或xml
	 * @param keyName
	 * @return testValue
	 */

	public static String getTestValue(String keyName, String dataStr) {
		String testValue = "";
		String Response = dataStr.trim().replace("\\", "").replace("\"", "");
		String firstChar = Response.substring(0, 1);
		String lastChar = Response.substring(Response.length() - 1,
				Response.length());
		if("\"".equals(dataStr.substring(0, 1))){
			dataStr=dataStr.replace("\\\\", "").replace("\\\"", "\"").replace("\"[", "[").replace("]\"", "]").replace("\"{", "{").replace("}\"", "}");
		}
		// 处理xml字符串
		if (firstChar.equals("<") && lastChar.equals(">")) {
			int i = 0, j = 0, k = 0;
			String testStr1 = "";
			testStr1 = "<" + keyName + ">";
			String testStr2 = "";
			testStr2 = "</" + keyName + ">";
			i = dataStr.indexOf(testStr1);
			j = dataStr.indexOf(testStr2);
			k = testStr1.length();
			if (i > 0 && j > i) {
				testValue = dataStr.substring(i + k, j);
			} else {
				String regx = keyName+"[\\s]*[\"]:[\\s]*[\"]*[\\s]*[a-zA-Z\\s\u4e00-\u9fa50-9]*[\"]*";
				Pattern p = Pattern.compile(regx);
				Matcher m = p.matcher(dataStr);
				if(m.find()){
					testValue =	m.group();
					testValue = testValue.split(":")[1].trim().replace("\"", "");
				}else{
					testValue = "";
				}
			}
			//如果正则匹配失败，使用：查询法
			if(testValue.length()==0){
                String regExp = keyName+"[\\\\]*[\"]?[\\s]*[:：][\\s]*[\\\\]*[\"]?([^\",\\\\<]+)";
                Pattern p = Pattern.compile(regExp);
                Matcher mm = p.matcher(dataStr);
                if(mm.find()){
                	testValue = mm.group(1);
                }
            }
		}
		// 处理json字符串
		else if(firstChar.equals("{") || firstChar.equals("[")){
			if (isJsonObject(dataStr)) {
				JSONObject jsonObject = JSONObject.parseObject(dataStr);
				for (String k : jsonObject.keySet()) {
					Object value = jsonObject.get(k);
					if (keyName.equals(k)) {
						testValue = value.toString();
						break;

					} else {
						if (value != null && isJson(value.toString())) {
							if (getTestValue(keyName, value.toString())
									.length() > 0) {
								testValue = getTestValue(keyName,
										value.toString());

							}
						}
					}
				}
			} else if (isJsonArray(dataStr)) {
				JSONArray jsonArray = JSONArray.parseArray(dataStr);
				Iterator<Object> iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					Object o = iterator.next();
					if (o != null && isJson(o.toString())) {
						if (getTestValue(keyName, o.toString()).length() > 0)
							testValue = getTestValue(keyName, o.toString());
					}
				}
			} else {
				String regx = keyName+"[\\s]*[\"]:[\\s]*[\"]*[\\s]*[a-zA-Z\\s\u4e00-\u9fa50-9]*[\"]*";
				Pattern p = Pattern.compile(regx);
				Matcher m = p.matcher(dataStr);
				if(m.find()){
					testValue =	m.group();
					testValue = testValue.split(":")[1].trim().replace("\"", "");
				}else{
					testValue = "";
				}
			}
			//如果递归没有找到则用正则匹配
			if(testValue.length()==0){
                String regExp = keyName+"[\\\\]*[\"]?[\\s]*:[\\s]*[\\\\]*[\"]?([^\",\\\\]+)";
                Pattern p = Pattern.compile(regExp);
                Matcher mm = p.matcher(dataStr);
                if(mm.find()){
                	testValue = mm.group(1);
                }
            }
		}
	//解析自定义的formdata入参
		else{
			String regx = keyName+"=([^&]*)";
			Pattern p = Pattern.compile(regx);
			Matcher m = p.matcher(dataStr);
			if(m.find()){
				testValue=m.group(1);
			}
			if(testValue.length()==0){
				String regExp1 = keyName+"[\\\\]*[\"]?[\\s]*:[\\s]*[\\\\]*[\"]?([^\",\\\\]+)";
                Pattern p1 = Pattern.compile(regExp1);
                Matcher mm1 = p1.matcher(dataStr);
                if(mm1.find()){
                	testValue = mm1.group(1);
                }
			}
		}
		return testValue;
	}

	/**
	 * 根据key取dataStr字符串中对应的value
	 * 
	 * @param dataStr必须是json串或xml
	 * @param keyName
	 * @return testValue
	 */	
	public static List<String> getTestValueMulti(String keyName, String dataStr) {
		List <String> testValueList=new ArrayList<String>();
		String testValue = "";
		String Response = dataStr.trim().replace("\\", "").replace("\"", "");
		String firstChar = Response.substring(0, 1);
		String lastChar = Response.substring(Response.length() - 1,
				Response.length());
		// 处理xml字符串
		if (firstChar.equals("<") && lastChar.equals(">")) {
			int i = 0, j = 0, k = 0;
			String testStr1 = "";
			testStr1 = "<" + keyName + ">";
			String testStr2 = "";
			testStr2 = "</" + keyName + ">";
			i = dataStr.indexOf(testStr1);
			j = dataStr.indexOf(testStr2);
			k = testStr1.length();
			if (i > 0 && j > i) {
				testValue = dataStr.substring(i + k, j);
				testValueList.add(testValue);
			} else {
				testValue = "";
			}
		}
		// 处理json字符串
		else {
			if (isJsonObject(dataStr)) {
				JSONObject jsonObject = JSONObject.parseObject(dataStr);
				for (String k : jsonObject.keySet()) {
					Object value = jsonObject.get(k);
					if (keyName.equals(k)) {
						testValue = value.toString();
						testValueList.add(testValue);
					} else {
						if (value != null && isJson(value.toString())) {
							if (getTestValueMulti(keyName, value.toString())
									.size() > 0) {
								for(String str : getTestValueMulti(keyName, value.toString())){
									testValueList.add(str);
								}						

							}
						}
					}
				}
			} else if (isJsonArray(dataStr)) {
				JSONArray jsonArray = JSONArray.parseArray(dataStr);
				Iterator<Object> iterator = jsonArray.iterator();
				while (iterator.hasNext()) {
					Object o = iterator.next();
					if (o != null && isJson(o.toString())) {
						if (getTestValueMulti(keyName, o.toString())
								.size() > 0)
							for(String str : getTestValueMulti(keyName, o.toString())){
								testValueList.add(str);
							}		
					}
				}
			} else {
				testValue = "";
			}

		}
		return testValueList;
		
	}
	
	public static List<String> getTestValueByLocation(String keyName, String dataStr) {
		List <String> testValueList=new ArrayList<String>();
		String Response = dataStr.trim().replace("\\", "").replace("\"", "");
		String firstChar = Response.substring(0, 1);
		String lastChar = Response.substring(Response.length() - 1,
				Response.length());
		String regExp="";
        String regExpJson = keyName+"[\\\\]*\"[\\s]*:[\\s]*[\\\\]*[\"]?([^\"\\\\,]+)";
        String regExpXml = keyName +">([^<]*)</";
        if (firstChar.equals("<") && lastChar.equals(">")){
        	regExp = regExpXml;
        }else{
        	regExp = regExpJson;
        }
        Pattern p = Pattern.compile(regExp);
        Matcher mm = p.matcher(dataStr);
        while(mm.find()){
        	testValueList.add(mm.group(1));
        }
		return testValueList;
		
	}

	
	/**
	 * 根据Excel中的检查点checkPoint和接口返回值Response来判断接口返回是否符合预期
	 * 
	 * @param checkPoint
	 * @param Response
	 * @return pass或者checkMsg
	 * @throws Exception 
	 */
	public static CheckResultDTO check(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO,CaseAfterRunDTO caseafterrunDTO) throws Exception {

		boolean bool = true;
		boolean Ture = true;
		boolean False = false;
		String checkMsg = "";
		List<String>  msgList= new ArrayList<String>();
		List<String>  numList= new ArrayList<String>();
			String[] checkarray = casedataDTO.getCheckPoint().split("\n");
			for (int i = 0; i < checkarray.length; i++) {
				String str = checkarray[i];
				int j = i + 1;
				// 判断是否相等
				if (str.indexOf("==") > 0) {
					String analyzeLeft,analyzeRight = "";
					//取==左边边值解析
					String strLeft = str.substring(0, str.indexOf("==")).trim();
					if(DataVerify.verifyPayload(strLeft)){
						analyzeLeft = DataAnalyze.analyzeStr(strLeft,casedataDTO,caseConfigurationDTO).trim();
						analyzeLeft = DataAnalyze.analyzeCurrent(analyzeLeft,casedataDTO,caseConfigurationDTO).trim();
					}else{
						analyzeLeft = getTestValue(strLeft, caseafterrunDTO.getResponse());
						if(analyzeLeft.isEmpty()){
							analyzeLeft = strLeft;
						}
					}
					
					//取==右边值解析
					String strRight = str.substring(str.indexOf("==") + 2,str.length()).trim();
					if(DataVerify.verifyPayload(strRight)){
						analyzeRight= DataAnalyze.analyzeStr(strRight,casedataDTO,caseConfigurationDTO).trim();
						analyzeRight= DataAnalyze.analyzeCurrent(analyzeRight,casedataDTO,caseConfigurationDTO).trim();
					}
					else{
						analyzeRight = strRight;
//						analyzeRight = getTestValue(strRight, caseafterrunDTO.getResponse());
//						if(analyzeRight.isEmpty()){
//							analyzeRight = strRight;
//						}
					}
					//判断是否左右两边字符串是否为数字，数字则转换为
					if(isNumeric(strLeft)&isNumeric(strRight)){
						//数字比较
						double numLeft = Double.parseDouble(strLeft);
						double numRight = Double.parseDouble(strRight);
						if (numLeft==numRight) {
							bool = bool && Ture;
							String message =strLeft+":"+analyzeLeft+";\n";
							msgList.add(message);
						} else {
							bool = bool && False;
							checkMsg = checkMsg
									+ str.substring(0, str.indexOf("==")).trim()
									+ "的验证：" + "左侧值("+numLeft+")"+"与右侧值("+numRight+")不等；" + "\n";
						}
					}else{
						//字符串比较
						if (analyzeLeft.equals(analyzeRight)) {
							bool = bool && Ture;
							String message =strLeft+":"+analyzeLeft+";\n";
							msgList.add(message);
						} else {
							bool = bool && False;
							checkMsg = checkMsg
									+ str.substring(0, str.indexOf("==")).trim()
									+ "的验证：" + "左侧值("+analyzeLeft+")"+"与右侧值("+analyzeRight+")不等；" + "\n";
						}
						
					}
					
				}
				// 判断是否包含
				else if (str.indexOf(">>") > 0) {
					String analyzeLeft,analyzeRight = "";
					//取>>左边边值解析
					String strLeft = str.substring(0, str.indexOf(">>")).trim();
					if(DataVerify.verifyPayload(strLeft)){
						analyzeLeft= DataAnalyze.analyzeStr(strLeft,casedataDTO,caseConfigurationDTO).trim();
						analyzeLeft = DataAnalyze.analyzeCurrent(analyzeLeft,casedataDTO,caseConfigurationDTO).trim();

					}else{
						analyzeLeft = getTestValue(strLeft, caseafterrunDTO.getResponse());
						if(analyzeLeft.isEmpty()){
							analyzeLeft = strLeft;
						}
					}
					//取>>右边值解析
					String strRight =  str.substring(str.indexOf(">>") + 2,str.length()).trim();
					if(DataVerify.verifyPayload(strRight)){
						analyzeRight= DataAnalyze.analyzeStr(strRight,casedataDTO,caseConfigurationDTO).trim();
						analyzeRight= DataAnalyze.analyzeCurrent(analyzeRight,casedataDTO,caseConfigurationDTO).trim();

					}else{
						analyzeRight = getTestValue(strRight, caseafterrunDTO.getResponse());
						if(analyzeRight.isEmpty()){
							analyzeRight = strRight;
						}
					}
					//判断是否包含
					if (analyzeLeft.indexOf(analyzeRight) > -1) {
						bool = bool && Ture;
						String message =strLeft+":"+analyzeLeft+";\n";
						msgList.add(message);
					} else {
						bool = bool && False;
						checkMsg = checkMsg
								+ str.substring(0, str.indexOf(">>")).trim()
								+ "的验证：" + "左侧值("+analyzeLeft+")"+"不包含右侧值("+analyzeRight+")；" + "\n";
					}

				}
				// 判断是否非空
				else if (str.indexOf("!!") > 0) {
					String analyzeLeft,analyzeRight = "";
					//取!!左边边值解析
					String strLeft = str.substring(0, str.indexOf("!!")).trim();
					if(DataVerify.verifyPayload(strLeft)){
						analyzeLeft= DataAnalyze.analyzeStr(strLeft,casedataDTO,caseConfigurationDTO).trim();
						analyzeLeft = DataAnalyze.analyzeCurrent(analyzeLeft,casedataDTO,caseConfigurationDTO).trim();

					}else{
						analyzeLeft = getTestValue(strLeft, caseafterrunDTO.getResponse());
//						if(analyzeLeft.isEmpty()){
//							analyzeLeft = strLeft;
//						}
					}
					if (analyzeLeft.length() > 0&&!DataVerify.verifyPayload(analyzeLeft)) {
						bool = bool && Ture;
						String message =strLeft+":"+analyzeLeft+";\n";
						msgList.add(message);
					} else {
						bool = bool && False;
						checkMsg = checkMsg
								+ str.substring(0, str.indexOf("!!")).trim()
								+ "的验证：" +analyzeLeft+ "字段值不存在；" + "\n";
					}
				} 
				//check条件里只想显示的关键字使用%%
				else if(str.indexOf("%%") > 0){
					String analyzeLeft = "";
					String strLeft = str.substring(0, str.indexOf("%%")).trim();
					analyzeLeft= DataAnalyze.analyzeStr(strLeft,casedataDTO,caseConfigurationDTO).trim();
					analyzeLeft = DataAnalyze.analyzeCurrent(analyzeLeft,casedataDTO,caseConfigurationDTO).trim();
					checkMsg = checkMsg + str.substring(0, str.indexOf("%%")).trim() +":" +analyzeLeft+ ";\n";
				}
				//check条件里需要提取的如0,1,3~~
				else if(str.indexOf("~~") > 0){
					String analyzeLeft = "";
					String strLeft = str.substring(0, str.indexOf("~~")).trim();
					String array[] = strLeft.split(",");
					for(String str1 : array){
						numList.add(str1);
					}
				}
				else if (str.trim().length() == 0) {
					bool = bool;
				} else {
					bool = bool && False;
					checkMsg = checkMsg + str + "的验证：" + "无效的格式；" + "\n";
				}

			}			
		// 如果比较通过则返回Pass，否则返回checkMsg
		CheckResultDTO checkResult=new CheckResultDTO();
		if (bool) {
			if(numList.size()==0){
				checkMsg=checkMsg;
			}else{
				for(int i=0;i<numList.size();i++){
					//生成新msg---------
					int j=Integer.parseInt(numList.get(i));
					checkMsg = checkMsg+msgList.get(j);
				}
			}
			checkResult.setResultCode(CaseStatus.PASS.getCode());
			checkResult.setResultMessage(checkMsg);

		} else {
			checkResult.setResultCode("NotPass");
			checkResult.setResultMessage(checkMsg);
		}
		return checkResult;
	}


	/**
	 * 根据key取json字符串中对应的value
	 * 
	 * @param jsonStr必须是json串
	 * @param keyName
	 * @return testValue
	 */
	public static String getJSONKeyValue(String jsonStr, String keyName) {
		String testValue = "";
		if (isJsonObject(jsonStr)) {
			JSONObject jsonObject = JSONObject.parseObject(jsonStr);
			for (String k : jsonObject.keySet()) {
				Object value = jsonObject.get(k);
				if (keyName.equals(k)) {
					testValue = value.toString();

				} else {
					if (value != null && isJson(value.toString())) {
						if (getJSONKeyValue(value.toString(), keyName).length() > 0) {
							testValue = getJSONKeyValue(value.toString(),
									keyName);

						}
					}
				}
			}
		} else if (isJsonArray(jsonStr)) {
			JSONArray jsonArray = JSONArray.parseArray(jsonStr);
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (o != null && isJson(o.toString())) {
					if (getJSONKeyValue(o.toString(), keyName).length() > 0)
						testValue = getJSONKeyValue(o.toString(), keyName);
				}
			}
		} else {
			testValue = "";
		}
		return testValue;
	}
	public static boolean isNumeric(String str){  
	    Pattern pattern = Pattern.compile("[0-9]+");  
	    return pattern.matcher(str).matches();     
	}  
}


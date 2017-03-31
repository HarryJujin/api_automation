package com.za.qa.analyze;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.za.qa.constants.EnvType;
import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseDataDTO;
import com.za.qa.dto.CaseSuiteDTO;
import com.za.qa.keywords.ExpressionEngine;
import com.za.qa.keywords.ExpressionRegister;
import com.za.qa.keywords.KeywordDefinition;
import com.za.qa.log.LogMan;
import com.za.qa.utils.ClientUtils;
import com.za.qa.verify.ResponseVerify;


public class DataAnalyze {
	
	public static String getAnalyzeData(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO,CaseSuiteDTO casesuiteDTO) {
		LogMan.getLoger().info("\n表达式引擎解析前的参数: " +"\n" +casedataDTO.getData());
		String bizcontent ="";
		try {
			bizcontent = analyzeStr(casedataDTO, caseConfigurationDTO,casesuiteDTO);
			bizcontent = analyzeStr(bizcontent,casedataDTO, caseConfigurationDTO);
			bizcontent = analyzeCurrent(bizcontent,casedataDTO,caseConfigurationDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogMan.getLoger().error("Excel参数: " +"表达式引擎解析失败" );
			e.printStackTrace();
		}
		LogMan.getLoger().info("\n表达式引擎解析后的参数: " +"\n" +bizcontent);
		return bizcontent;
	}
	/**
	 * 
	 * @param CaseDataDTO, CaseConfigurationDTO
	 * 方法说明:第一步解析$开头的关键字
	 */
	public static  String analyzeStr(CaseDataDTO casedataDTO, CaseConfigurationDTO caseConfigurationDTO,CaseSuiteDTO casesuiteDTO) throws Exception {
		//处理#开头的关键字
		// **主动加载所有定义的关键字注解,如果有新的keyword 类需要加载，则需要将keyword对象加入到数组*//*
				String env=caseConfigurationDTO.getEnv();
				String listPath = caseConfigurationDTO.getListPath();
				String bizcontent = casedataDTO.getData();
				int i = casesuiteDTO.getI();
				Object[] keywords = { new KeywordDefinition() };
				ExpressionEngine engine1 = new ExpressionEngine(keywords);
				// **解析表达式，并进行数据组装*//*
				String MARK = "MYMARKBEGIN";

				String regx = "\\$([a-zA-Z=_'\\s\u4e00-\u9fa5\\\\(\\\\),#0-9.%\\\\\\\"\\/:\\-]*[\\)])";// 过滤表达式
				Pattern p = Pattern.compile(regx);
				Matcher m = p.matcher(bizcontent);
				while (m.find()) {
					LogMan.getLoger().info("表达式: [" + m.group(1) + "]开始解析...");
					String replaceRegx  = "$" + m.group(1);
					bizcontent = bizcontent.replace(replaceRegx, MARK);
					String replaceRegxNew = m.group(1);	
					//GetExcelData方法特殊处理
					if(replaceRegx.indexOf("GetExcelData")>0){
						String value="";
						Map<String,String[]> map =caseConfigurationDTO.getDataResource().get(listPath);
						replaceRegxNew=replaceRegxNew.replace(" ", "");
						int beginIndex=replaceRegxNew.indexOf("(\"")+2;
						int endIndex=replaceRegxNew.length()-2;
						String key=replaceRegxNew.substring(beginIndex, endIndex);
						String [] valueArry=map.get(key);
						value=valueArry[i];
								if(value.length()==0){
									if("uat".equalsIgnoreCase(env)){
										value=valueArry[2];
									}else if("itest".equalsIgnoreCase(env)){
										value=valueArry[1];
									}else{
										value=valueArry[0];
									}
								}
								if(value.length()==0){
									value=valueArry[0];
								}
						 		bizcontent = bizcontent.replace(MARK, value);
					}
					/*
					 * 此处需要替换表达式，由于String.replaceFrist在替换表达式时碰到问题，所以替换成自定义算法 具体实现为先将json
					 * 串中相同的表达式全部替换成MARK,然后再获取表达式的值进行替换
					 */
					while (bizcontent.indexOf(MARK) != -1) {
						String keywordExp = replaceRegxNew;
						try {
							String result="";
							try {
								result = (String) engine1.execute(keywordExp);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								LogMan.getLoger().info(keywordExp+"解析数据失败，解析下一关键字");
							}
							if (!result.equals("")) {
								bizcontent = bizcontent.replace(MARK, result);
							} else {
								bizcontent = bizcontent.replaceFirst(MARK, replaceRegx);
						LogMan.getLoger()
								.error(String
										.format("表达式: %s 解析获取到的值为空值,放弃执行后续步骤,请检查",keywordExp));
						bizcontent = bizcontent.replaceFirst(MARK, result);
						/*throw new Exception(
								String.format("表达式: %s 解析获取到的值为空值,放弃执行后续步骤,请检查",keywordExp));*/
					}

						} catch (com.googlecode.aviator.exception.CompileExpressionErrorException e) {
							bizcontent = bizcontent.replaceFirst(MARK, replaceRegx);
							throw new Exception(String.format(
									"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
						} catch (com.googlecode.aviator.exception.ExpressionRuntimeException e) {
							bizcontent = bizcontent.replaceFirst(MARK, replaceRegx);
							throw new Exception(String.format(
									"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
						} catch (com.googlecode.aviator.exception.ExpressionSyntaxErrorException e) {
							bizcontent = bizcontent.replaceFirst(MARK, replaceRegx);
							throw new Exception(String.format(
									"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
						}
					}
				}
				return bizcontent;
	}

	/**
	 * 
	 * @param bizcontent 
	 * 方法说明:第二步解析@开头的关键字
	 */
	public static  String analyzeStr(String bizcontent) throws Exception {

		// **主动加载所有定义的关键字注解,如果有新的keyword 类需要加载，则需要将keyword对象加入到数组*//*
		Object[] keywords = { new KeywordDefinition() };
		ExpressionEngine engine = new ExpressionEngine(keywords);
		// **解析表达式，并进行数据组装*//*
		String MARK = "MYMARK";

		//String regx1 = "@([a-zA-Z\u4e00-\u9fa5\\\\(\\\\),0-9\u4e00-\u9fa5\\\\\\\"]*[\\)])";// 过滤表达式
		String regx = "@([\\+a-zA-Z\\[\\]=_'\\s\u4e00-\u9fa5\\\\(\\\\),#0-9.\\\\\\\"\\/:\\-]*[\\)])";
					  
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(bizcontent);
		while (m.find()) {
			LogMan.getLoger().info("表达式: [" + m.group(1) + "]开始解析...");
			String replaceRegx = "@" + m.group(1);
			String replaceRegxNew=m.group(1).toString();
			bizcontent = bizcontent.replace(replaceRegx, MARK);
			/*
			 * 此处需要替换表达式，由于String.replaceFrist在替换表达式时碰到问题，所以替换成自定义算法 具体实现为先将json
			 * 串中相同的表达式全部替换成MARK,然后再获取表达式的值进行替换
			 */
			while (bizcontent.indexOf(MARK) != -1) {
				String keywordExp = replaceRegxNew;
				try {
					String result="";
					try {
						result = (String) engine.execute(keywordExp);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						LogMan.getLoger().info(keywordExp+"解析数据失败，解析下一关键字");
					}
					if (!result.equals("")) {
						bizcontent = bizcontent.replaceFirst(MARK, result);
					} else {
						LogMan.getLoger()
								.error(String
										.format("表达式: %s 解析获取到的值为空值,放弃执行后续步骤,请检查",keywordExp));
						bizcontent = bizcontent.replaceFirst(MARK, result);
						/*throw new Exception(
								String.format("表达式: %s 解析获取到的值为空值,放弃执行后续步骤,请检查",keywordExp));*/
					}

				} catch (com.googlecode.aviator.exception.CompileExpressionErrorException e) {
					
					throw new Exception(String.format(
							"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
				} catch (com.googlecode.aviator.exception.ExpressionRuntimeException e) {
					throw new Exception(String.format(
							"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
				} catch (com.googlecode.aviator.exception.ExpressionSyntaxErrorException e) {
					throw new Exception(String.format(
							"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
				}
			}
		}
		return bizcontent;
	}
	
	public static  String analyzeStr(String bizcontent,CaseDataDTO casedataDTO, CaseConfigurationDTO caseConfigurationDTO) throws Exception {

		// **主动加载所有定义的关键字注解,如果有新的keyword 类需要加载，则需要将keyword对象加入到数组*//*
		Object[] keywords = { new KeywordDefinition() };
		ExpressionEngine engine = new ExpressionEngine(keywords);
		// **解析表达式，并进行数据组装*//*
		String MARK = "MYMARK";

		//String regx1 = "@([a-zA-Z\u4e00-\u9fa5\\\\(\\\\),0-9\u4e00-\u9fa5\\\\\\\"]*[\\)])";// 过滤表达式
		String regx = "@([\\+a-zA-Z\\[\\]=_'\\s\u4e00-\u9fa5\\\\(\\\\),#0-9.%\\\\\\\"\\/:\\-]*[\\)])";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(bizcontent);
		while (m.find()) {
			LogMan.getLoger().info("表达式: [" + m.group(1) + "]开始解析...");
			String replaceRegx = "@" + m.group(1);
			String replaceRegxNew=m.group(1).toString();
			bizcontent = bizcontent.replace(replaceRegx, MARK);		
			replaceRegxNew = replaceParameter(replaceRegxNew,"ResponseDepend",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceParameter(replaceRegxNew,"PayloadDepend",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceParameter(replaceRegxNew,"PayloadDependMulti",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceParameter(replaceRegxNew,"ResponseDependMulti",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceParameter(replaceRegxNew,"PayloadCurrent",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceParameter(replaceRegxNew,"ResponseCurrent",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceSqlQueryDepend(replaceRegxNew,"SqlQueryDepend",casedataDTO,caseConfigurationDTO);
			replaceRegxNew = replacePayloadDependS(replaceRegxNew,"PayloadDependS",casedataDTO,caseConfigurationDTO);
			replaceRegxNew = repalceIdb(replaceRegxNew,"RIdb",casedataDTO);
			replaceRegxNew = insert_env(replaceRegxNew,"QueryPolicyNo",casedataDTO);
			replaceRegxNew = insert_env(replaceRegxNew,"RPay",casedataDTO);
			/*
			 * 此处需要替换表达式，由于String.replaceFrist在替换表达式时碰到问题，所以替换成自定义算法 具体实现为先将json
			 * 串中相同的表达式全部替换成MARK,然后再获取表达式的值进行替换
			 */
			while (bizcontent.indexOf(MARK) != -1) {
				String keywordExp = replaceRegxNew;
				try {
					String result="";
					try {
						result = (String) engine.execute(keywordExp);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						LogMan.getLoger().info(keywordExp+"解析数据失败，解析下一关键字");
					}
					if (!result.equals("")) {
						bizcontent = bizcontent.replaceFirst(MARK, result);
					} else {
						LogMan.getLoger()
								.error(String
										.format("表达式: %s 解析获取到的值为空值,放弃执行后续步骤,请检查",keywordExp));
						//bizcontent = bizcontent.replaceFirst(MARK, result);
						if(replaceRegxNew.contains("Rreplace")){
							bizcontent = bizcontent.replaceFirst(MARK, "");	
						}else{
							bizcontent = bizcontent.replaceFirst(MARK, replaceRegx);
						}
						/*throw new Exception(
								String.format("表达式: %s 解析获取到的值为空值,放弃执行后续步骤,请检查",keywordExp));*/
					}

				} catch (com.googlecode.aviator.exception.CompileExpressionErrorException e) {
					bizcontent = bizcontent.replaceFirst(MARK, replaceRegx);
					throw new Exception(String.format(
							"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
				} catch (com.googlecode.aviator.exception.ExpressionRuntimeException e) {
					bizcontent = bizcontent.replaceFirst(MARK, replaceRegx);
					throw new Exception(String.format(
							"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
				} catch (com.googlecode.aviator.exception.ExpressionSyntaxErrorException e) {
					bizcontent = bizcontent.replaceFirst(MARK, replaceRegx);
					throw new Exception(String.format(
							"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
				}
			}
		}
		return bizcontent;
	}
	
	/**
	 * 
	 * @param bizcontent 
	 * 方法说明:第三步解析&开头的关键字（解析本身依赖）
	 */
	public static  String analyzeCurrent(String bizcontent,CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO) throws Exception {

		// **主动加载所有定义的关键字注解,如果有新的keyword 类需要加载，则需要将keyword对象加入到数组*//*
		Object[] keywords = { new KeywordDefinition() };
		ExpressionEngine engine = new ExpressionEngine(keywords);
		// **解析表达式，并进行数据组装*//*
		String MARK = "MYMARK";

		String regx = "&([a-zA-Z=_'\\s\u4e00-\u9fa5\\\\(\\\\),#0-9.%\\\\\\\"\\/:\\-]*[\\)])";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(bizcontent);
		while (m.find()) {
			LogMan.getLoger().info("表达式: [" + m.group(1) + "]开始解析...");
			String replaceRegx = "&" + m.group(1);
			bizcontent = bizcontent.replace(replaceRegx, MARK);
			String replaceRegxNew=m.group(1).toString();
			replaceRegxNew = replaceParameter(replaceRegxNew,"ResponseDepend",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceParameter(replaceRegxNew,"PayloadDepend",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceParameter(replaceRegxNew,"PayloadDependMulti",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceParameter(replaceRegxNew,"ResponseDependMulti",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceParameter(replaceRegxNew,"PayloadCurrent",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceParameter(replaceRegxNew,"ResponseCurrent",caseConfigurationDTO,casedataDTO);
			replaceRegxNew = replaceSqlQueryDepend(replaceRegxNew,"SqlQueryDepend",casedataDTO,caseConfigurationDTO);
			replaceRegxNew = replacePayloadDependS(replaceRegxNew,"PayloadDependS",casedataDTO,caseConfigurationDTO);
			replaceRegxNew = replacePayloadDependCurrent(replaceRegxNew,"PayloadDependCurrent",bizcontent,casedataDTO, caseConfigurationDTO);
			replaceRegxNew = replaceSubString(replaceRegxNew,"RsubStr",bizcontent);
			replaceRegxNew = repalceIdb(replaceRegxNew,"RIdb",casedataDTO);
			replaceRegxNew = insert_env(replaceRegxNew,"QueryPolicyNo",casedataDTO);
			replaceRegxNew = insert_env(replaceRegxNew,"RPay",casedataDTO);
			/*
			 * 此处需要替换表达式，由于String.replaceFrist在替换表达式时碰到问题，所以替换成自定义算法 具体实现为先将json
			 * 串中相同的表达式全部替换成MARK,然后再获取表达式的值进行替换
			 */
			while (bizcontent.indexOf(MARK) != -1) {
				String keywordExp = replaceRegxNew;
				try {
					String result="";
					try {
						result = (String) engine.execute(keywordExp);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						LogMan.getLoger().info(keywordExp+"解析数据失败，解析下一关键字");
					}
					if (!result.equals("")) {
						bizcontent = bizcontent.replace(MARK, result);
					} else {
						LogMan.getLoger()
								.error(String
										.format("表达式: %s 解析获取到的值为空值,放弃执行后续步骤,请检查",keywordExp));
						//bizcontent = bizcontent.replaceFirst(MARK, result);
						if(replaceRegxNew.contains("Rreplace")){
							bizcontent = bizcontent.replaceFirst(MARK, "");	
						}else{
							bizcontent = bizcontent.replaceFirst(MARK, replaceRegx);
						}
						/*throw new Exception(
								String.format("表达式: %s 解析获取到的值为空值,放弃执行后续步骤,请检查",keywordExp));*/
					}

				} catch (com.googlecode.aviator.exception.CompileExpressionErrorException e) {
					
					throw new Exception(String.format(
							"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
				} catch (com.googlecode.aviator.exception.ExpressionRuntimeException e) {
					throw new Exception(String.format(
							"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
				} catch (com.googlecode.aviator.exception.ExpressionSyntaxErrorException e) {
					throw new Exception(String.format(
							"非法表达式:[%s],未定义，解析数据失败，忽略后续测试", keywordExp));
				}
			}
		}
		return bizcontent;
	}
	
	public static String replaceParameter(String str1,String str2,CaseConfigurationDTO caseConfigurationDTO,CaseDataDTO casedataDTO){
		if(str1.indexOf(str2+'(')>-1){
			str1.replace(" ", "");
			if(str2.equals("ResponseCurrent")||str2.equals("PayloadCurrent")){
				String index=str2+"(";
				str1 = str1.replace(index, index+"\""+caseConfigurationDTO.getListPath()+"#"+casedataDTO.getCaseNo()+"\",");
			}else{
			String index=str2+"(\"";
			str1 = str1.replace(index, index+caseConfigurationDTO.getListPath()+"#");
			}
		}
		return str1;
	}
	
	public static String replaceSubString(String str1,String str2,String bizcontent){
		if(str1.indexOf(str2+"(")>-1){
			String index=str2+"(";
			String value="";
			String	bizcontentnew= bizcontent; 
			String keyName=str1.substring(index.length(), str1.indexOf(","));
			keyName=keyName.replace("\"", "").replace("\\s", "");
    		//处理开放平台=分隔的入参
    		String Response = bizcontent.trim().replace("\\", "").replace("\"", "").replace("|", "");
    		String firstChar = Response.substring(0, 1);
	  		if(!firstChar.equals("<")&&!firstChar.equals("{")&&!firstChar.equals("[")&&bizcontent.indexOf("=")>0){
    			ClientUtils ClientUtils=new ClientUtils();
    			JSONObject jsonBzcontent=new JSONObject();
    			jsonBzcontent=ClientUtils.Str2JsonObject(bizcontent);
    		    bizcontentnew=jsonBzcontent.toString();
    		}
	 		try {
				value=ResponseVerify.getTestValue(keyName,bizcontentnew);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				value=keyName;
			}
	 		//bizcontent = bizcontent.replaceFirst(MARK, value);
	 	   str1= str1.replace(keyName, value);
		}
		return str1;
	}
	
	public static String repalceIdb(String str1,String str2,CaseDataDTO casedataDTO){
		if (str1.indexOf(str2+'(')>-1){
			StringBuffer stringBuffer = new StringBuffer(str1);
			String index = str2+'(';
			int num = str1.indexOf(index)+index.length();
			if(EnvType.iTest.toString().equals(casedataDTO.getListflag())){
				str1 = stringBuffer.insert(num, "\""+"tst"+"\",").toString();
			}else if(EnvType.uat.toString().equals(casedataDTO.getListflag())){
				str1	= stringBuffer.insert(num, "\""+"pre"+"\",").toString();
			}
		}
		return str1;
	}
	
	public static String insert_env(String str1,String str2,CaseDataDTO casedataDTO){
		String index=str2+"(";
		int beginIndex=str1.indexOf(index)+index.length();
		if (str1.indexOf(str2+'(')>-1){
			StringBuffer stringBuffer = new StringBuffer(str1);
			if(EnvType.iTest.toString().equals(casedataDTO.getListflag())){
				str1 = stringBuffer.insert(beginIndex, "\""+"iTest"+"\",").toString();
			}else if(EnvType.uat.toString().equals(casedataDTO.getListflag())){
				str1	= stringBuffer.insert(beginIndex, "\""+"uat"+"\",").toString();
			}
		}
		return str1;
	}
	
	public static String replaceSqlQueryDepend(String str1,String str2,CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO){
		if(str1.indexOf(str2+'(')>-1){
			str1.replace(" ", "");
			String index=str2+"(\"";
			int beginIndex=str1.indexOf(index)+index.length();
			StringBuffer stringBuffer = new StringBuffer(str1);
			str1 = stringBuffer.insert(beginIndex, caseConfigurationDTO.getListPath()+"#"+casedataDTO.getCaseNo()+"#").toString();
		}
		return str1;
	}
	
	public static String replacePayloadDependS(String replaceRegxNew,String methodName,CaseDataDTO casedataDTO, CaseConfigurationDTO caseConfigurationDTO){
		if(replaceRegxNew.indexOf(methodName+'(')>-1){
			String returnStr=replaceRegxNew;
			String target="";
			String ListPath=caseConfigurationDTO.getListPath();
			String path=ListPath.split("#")[0];
			String oldkey_name="";
			String regx = "PayloadDependS\\([\\s]*\"([^,\"]*)\"[\\s]*,[\\s]*\"([^,\"]*)[\\s]*\"[\\s]*\\)";
			Pattern p = Pattern.compile(regx);
			Matcher m = p.matcher(replaceRegxNew);
			while (m.find()) {
				String oldApiNo = getJoinMapData("CASE_NO",m.group(1),caseConfigurationDTO.getDataResource().get(ListPath),"API_NO");
				Map <String,String> RenameVariableMap=caseConfigurationDTO.getRenameVariableMapping().get(path+"接口模板"+"#"+oldApiNo);
				String case_num=caseConfigurationDTO.getListPath()+"#"+m.group(1);
				String key_name=m.group(2);
				Map<String, Object> payloadEnv = ExpressionRegister.getPayloadEnv();
	    		String value = "";
                String payload = (String) payloadEnv.get(case_num);
                value = ResponseVerify.getTestValue(key_name,
						payload);
                //判断value未获取到且有重命名
                if (value.length()==0 && RenameVariableMap.containsKey(m.group(2))){
                	String[] array=RenameVariableMap.get(m.group(2)).split("#");
                	//判断重命名变量是否有重名标记location，#后面的数字是数字顺序位置
                	if(array.length>1){
                		oldkey_name =array[0];
                    	int num = Integer.parseInt(array[1]);
                    	List<String> list =new ArrayList<String>();
                    	list = ResponseVerify.getTestValueByLocation(oldkey_name,
        						payload);
                    	value =list.get(num);
                	}else{
                		oldkey_name = RenameVariableMap.get(m.group(2));
                		value = ResponseVerify.getTestValue(oldkey_name,
        						payload);
                	}
                }
            	//判断是嵌套关键字 返回增加双引号
    			if(!replaceRegxNew.equals(m.group(0))){
    				value="\""+value+"\"";
    			}
                returnStr=returnStr.replace(m.group(0), value);
			}
		
			return returnStr;
		}else{
			return replaceRegxNew;
		}
	
	}
	
	
	public static String replacePayloadDependCurrent(String replaceRegxNew,String methodName,String bizcontent,CaseDataDTO casedataDTO, CaseConfigurationDTO caseConfigurationDTO){
		if(replaceRegxNew.indexOf(methodName+'(')>-1){
			String listPath=caseConfigurationDTO.getListPath();
			String  caseNo=casedataDTO.getCaseNo();
			String regx = methodName+"\\([\\s]*\"(.*)\"[\\s]*\\)";
			Pattern p = Pattern.compile(regx);
			Matcher m = p.matcher(replaceRegxNew);
			while (m.find()) {
				String  replaceValue=listPath+"#"+caseNo+"#"+methodName;
				replaceRegxNew=replaceRegxNew.replace(m.group(1), replaceValue+"#"+m.group(1));
				//存入数据到payLoad环境变量
				ExpressionRegister.setPayloadEnv(replaceValue, bizcontent);
			}
		}
		return replaceRegxNew;
	}
	//map连接查询key值
		public static  String getJoinMapData(String knownMapKey,String knownMapValue,Map<String,String[]> getMapData,String getKey){
			String getValue="";
			String knownArry[]=getMapData.get(knownMapKey);
			int num=0;
			for(int i=0;i<knownArry.length;i++){
				String value=knownArry[i];
				if(knownMapValue.equals(value)){
					num=i;
				}
			}
			
			String getKeyArry[]=getMapData.get(getKey);
			getValue=getKeyArry[num];
			return getValue;
		}
		
		//获取配置表中的模板
		public  String getRequestData(CaseConfigurationDTO caseConfigurationDTO,CaseDataDTO caseDataDTO,CaseSuiteDTO casesuiteDTO){
			Map<String,Map<String,String[]>> DataConfiguration=caseConfigurationDTO.getDataConfiguration();
			Map<String,Map<String,String[]>> DataResource=caseConfigurationDTO.getDataResource();
			int i=casesuiteDTO.getI();
			String CaseListNo= caseDataDTO.getCaseListNo();
			String path= caseConfigurationDTO.getListPath();
			String ApiNo=casesuiteDTO.getApiNo();
					
			String keyDataSource=path+"#"+CaseListNo;
			String keyConfig=path+"接口模板";
			Map <String,String[]> dataConfiguration=DataConfiguration.get(keyConfig);
			Map <String,String[]> dataResource=DataResource.get(keyDataSource);
			String ApiNoArryDataSource[]=dataResource.get(ApiNo);
			//map连接获取需要字段的数据bizcontent
			String bizcontent=getJoinMapData("API_NO", ApiNoArryDataSource[i], dataConfiguration, "DATA");
			return bizcontent;
			
		}
}
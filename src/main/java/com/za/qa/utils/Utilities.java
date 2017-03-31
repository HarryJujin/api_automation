package com.za.qa.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.aviator.exception.CompileExpressionErrorException;
import com.za.qa.constants.CaseConf;
import com.za.qa.keywords.ExpressionEngine;
import com.za.qa.log.LogMan;

/**
 * Created by zhaoguocai on 2016/7/13. zhaoguocai@zhongan.com
 */
public class Utilities {

    public static Map<String, String> serializeFormat(String msg) {
        Map<String, String> infoMap = new HashMap<String, String>();
        int num = isContainNumstr(msg,"=");// 
        if(num>1){
            String[] contents = msg.split("\n");
            for (String i : contents) {
                String[] v = i.split("=");
                if (v.length == 1) {
                    infoMap.put(v[0].trim(), "");
                }
                if (v.length == 2) {
                    infoMap.put(v[0].trim(), v[1].trim());
                }
            }
        
        }else{
        	if(msg.contains("|")){

                String[] contents = msg.split("\\|");
                for (String i : contents) {
                    String[] v = i.split("=");
                    if (v.length == 1) {
                        infoMap.put(v[0].trim(), "");
                    }
                    if (v.length == 2) {
                        infoMap.put(v[0].trim(), v[1].trim());
                    }
                }
            
        	}else{
                String[] v = msg.split("=");
                if (v.length == 1) {
                    infoMap.put(v[0].trim(), "");
                }
                if (v.length == 2) {
                    infoMap.put(v[0].trim(), v[1].trim());
                }            
        	}        	
        }
        return infoMap;
    }
    //检查str1包含几个str2
    public static int isContainNumstr(String str1,String str2){
    	int num=0;
    	String regx = str2;
        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(str1);
        while (m.find()) {
        	num=num+1;
        }
    	return num;
    }

	/**
	 * 根据传入的KEY的名字查找JSON中对应的key的Value(包含对JSON中转移字符的过滤)
	 * 
	 * @param jsonStr
	 * @param key
	 * @return
	 */
	public static String getValeByKeyNameInJson(String jsonStr, String key) {
		String statmentStr = jsonStr.replace("\\", "");
		statmentStr = statmentStr.replace("\"{", "{");
		statmentStr = statmentStr.replace("}\"", "}");
		try {
			Pattern p = Pattern.compile(String.format("%s\":[\"]?([^\",]+)", key));
			Matcher m = p.matcher(statmentStr);
			m.find();
			return m.group(1);
		} catch (IllegalStateException e) {
			return "";
		}
	}

	/**
	 * 执行关键字表达式
	 * 
	 * @param expression
	 * @return
	 */
	private static String executeKeyWordExp(String expression) {
		LogMan.getLoger().info(String.format("开始解析关键字\"%s\"", expression));
		String result = "关键字解析失败";
		try {
			result = (String) ExpressionEngine.getEngine().execute(expression);
			LogMan.getLoger().info(String.format("关键字\"%s\"解析完毕. Value：%s", expression, result));
			return result;
		} catch (CompileExpressionErrorException e) {
			String errMsg = String.format("非法关键字\"%s\", Caused By:%s", expression, e.getMessage());
			LogMan.getLoger().error(errMsg, e);
			return result;
		} catch (Exception e) {
			String errMsg = String.format("关键字\"%s\"解析失败, Caused By:%s", expression, e.getMessage());
			LogMan.getLoger().error(errMsg, e);
			return result;
		}

	}

	/**
	 * 对字符串进行操作，替换字符串中第一个匹配的关键字替换成目标字符
	 * 
	 * @param str
	 *            ：需要进行替换操作的字符串
	 * @param target：需要被替换的字段
	 * @param replacement：替换成的字段
	 * @return
	 */
	public static String replaceFirst(String str, String target, String replacement) {
		LogMan.getLoger().info("获取到字符串：" + str);
		LogMan.getLoger().info("开始进行字符替换处理, 目标字段：" + target + "替换为：" + replacement);
		int index = str.indexOf(target);
		int targetLength = target.toCharArray().length;
		int endIndex = index + targetLength;
		// 如果没找到，直接返回原传入字符串
		if (index == -1) {
			LogMan.getLoger().warn("未找到目标替换字符串，返回原字符串");
			LogMan.getLoger().warn("返回原字符串：" + str);
			return str;
		}
		String str1 = str.substring(0, endIndex);
		String str2 = str.substring(endIndex);
		String str3 = str1.replace(target, replacement);
		String result = str3 + str2;
		LogMan.getLoger().info("完成替换操作，返回值：：" + result);
		return result;

	}

	/**
	 * 实现对输入字符串中包含的关键字进行解析,生成解析后的字符串
	 * 
	 * @param str：需要被解析的字符串
	 * @return
	 */
	public static String keyWordsParse(String str) {
		String pureStr = str.trim();
		String regEx = "(@\\w+\\()";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(pureStr);
		while (m.find()) {
			String subStr = m.group(1);
			int startIndex = pureStr.indexOf(subStr);
			char[] charStr = pureStr.toCharArray();
			int times = 0;
			int tmpStatus = 0;
			for (int i = startIndex; i < charStr.length; i++) {
				if (String.valueOf(charStr[i]).equals("(")) {
					times += 1;
					tmpStatus += 1;
				} else if (String.valueOf(charStr[i]).equals(")")) {
					times -= 1;
					tmpStatus += 1;
				} else {
					continue;
				}
				if (times == 0 && tmpStatus != 0) {
					int endIndex = i;
					String oldStr = pureStr.substring(startIndex, endIndex + 1);
					LogMan.getLoger().info("捕获关键字表达式：" + oldStr);
					String keyword = pureStr.substring(startIndex + 1, endIndex + 1);
					LogMan.getLoger().info("捕获关键字：" + keyword);
					String result = Utilities.executeKeyWordExp(keyword);
					pureStr = Utilities.replaceFirst(pureStr, oldStr, result);
					break;
				}
			}

		}
		// 如果没有检测到字符串中包含表达式，则返回原字符串
		LogMan.getLoger().info("解析后的值为：" + pureStr);
		return pureStr;
	}
	
	public static void main(String args[]){
		String res = "@RKeyJsonValueEquals(\"returnMsg\",\"00\")";
		String res2= keyWordsParse(res);
		System.out.println(res2);
	}

	/**
	 * getExcelList读取excelpath路径下所有excel文件，取出每个excel文件的路径放到paths中
	 * 
	 * @param excelpath
	 * @return paths
	 */
	public static File[] getExcelList(String excelpath) {
		File f = null;
		File[] paths = null;
		try {
			f = new File(excelpath);
 			FilenameFilter fileNameFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.lastIndexOf('.') > 0) {
						int lastIndex = name.lastIndexOf('.');
						String str = name.substring(lastIndex);
						if (str.equals(".xlsx")) {
							return true;
						}
					}
					return false;
				}
			};
			paths = f.listFiles(fileNameFilter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paths;
	}
	
	/**
	 * Copy Excel到指定文件夹
	 * @param resourceFile
	 * @throws IOException 
	 */
	public static String copyExcel(String resourceFile) throws IOException{
		//**开始复制文件，并按照当前的时间戳生成新文件*//*
        File fso = new File(resourceFile);
        String target_path="";
		if (fso.exists()) {
			SimpleDateFormat format_second = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat format_day = new SimpleDateFormat("yyyyMMdd");
			String currentTimeStamp1 = format_second.format(System
					.currentTimeMillis());
			String currentTimeStamp2 = format_day.format(System
					.currentTimeMillis());
			String new_file = fso.getName().split("\\.")[0]
					+ currentTimeStamp1 + "."
					+ fso.getName().split("\\.")[1];
			File file = new File(fso.getParent() + File.separator+currentTimeStamp2);
			if(!file.exists()&&!file.isDirectory()){
				file.mkdir();
			}
			 target_path = fso.getParent() + File.separator+currentTimeStamp2+File.separator
					+ new_file;
			int byteread = 0;
			InputStream inStream = new FileInputStream(resourceFile);
			FileOutputStream fs = new FileOutputStream(target_path);
			byte[] buffer = new byte[2048];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
			fs.close();
	}
		return target_path;
	}
	/**
	 * used for HSF
	 * @param msg 
	 */
/*	public static String Args(String msg) {
		String[] array = msg.split("\\|\n");
		List<Object> Args = new ArrayList<Object>();
		for (int i = 0; i < array.length; i++) {
			if(array[i].contains("=")){
				Map<String, String> infoMap = serializeFormat(array[i]);
				Args.add(JSONUtils.convertMapToJSON(infoMap));
			}else{
				String array1= "\""+array[i]+"\"";
				Args.add(array1);
			}
		}
		return Args.toString();
	}*/
	
	public static String Args(String msg){
		List<Object> Args = new ArrayList<Object>();
		Args.add(msg);
		return Args.toString();
	}

	/**
	 * 关闭HTTPClient 网络端log
	 */
	public static void shutDownHttpClientLog() {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "stdout");
	}
	
	
	/**
	 * 获取配置信息
	 * @return
	 */

	public static Map<String, String> getConfig() {
		Map<String, String> conf = new HashMap<String, String>();
		try {
			String CONFIG = new File(CaseConf.confpath).getCanonicalPath();
			Properties pp = new Properties();
			pp.load(new FileInputStream(CONFIG));
			conf.put("GateWay", pp.getProperty("GateWay"));
			conf.put("AppID", pp.getProperty("AppID"));
			conf.put("RSAPrivateKey", pp.getProperty("RSAPrivateKey"));
			conf.put("RSAPublicKey", pp.getProperty("RSAPublicKey"));
			conf.put("CDCHost", pp.getProperty("CDCHost"));
		} catch (Exception e) {
			LogMan.getLoger().error("加载配置文件异常,退出...", e);
		}
		return conf;

	}
	
	/**
	 * 获取命令行启动参数
	 * @param args
	 * @return
	 */
	public static Map<String, String> getStartArgs(String[] args){
		String resourceFile = null;
		try {
			resourceFile = new File(args[0]).getCanonicalPath();
		} catch (IOException e) {
			LogMan.getLoger().error("启动参数文件名加载故障,退出...", e);
			System.exit(0);
		}
		String sheet = args[1];
		Map<String, String> sArgs = new HashMap<String, String>();
		sArgs.put("FileName", resourceFile);
		sArgs.put("SheetNameOrIndex", sheet);
		return sArgs;
	}

}

package com.za.qa.dataprepare;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.za.qa.constants.CaseConf;
import com.za.qa.utils.FileUtils;
import com.za.qa.utils.Utilities;
import com.za.qa.utils.XLSUtils;

/**
 * 项目名称：za-api-automation 类名称：DataConfiguration 类描述： 获取用例集开关，接口模板，接口配置，并存放到map
 * 创建人：jujinxin 创建时间：2016年11月11日 上午11:54:25 修改人：jujinxin 修改时间：2016年11月11日
 * 上午11:54:25 修改备注：
 */
public class DataConfiguration {
    static File          path;
    Object[][]           o    = null;

    static Callable<Map> call = new Callable<Map>() {
                                  public Map call() throws IOException {
                                      return singleConf();
                                  }
                              };

    public static Map<String, Map<String, String[]>> mapConf() throws IOException, InterruptedException,
            ExecutionException {
        Map<String, Map<String, String[]>> map = null;
        List<Map> list = new LinkedList<Map>();
        String excelHome = FileUtils.readProperties(CaseConf.confpath).getProperty("ExcelFile");
        excelHome =new String(excelHome.getBytes("ISO-8859-1"),"gbk");
        File[] paths = Utilities.getExcelList(excelHome);
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < paths.length; i++) {
            path = paths[i];
            Future<Map> task = pool.submit(call);
            map = task.get();
            list.add(map);
        }
        pool.shutdown();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Map<String, String[]>> map2 = list.get(i);
            for (String key : map2.keySet()) {
                map.put(key, map2.get(key));
            }
        }
        return map;
    }

    public static Map<String, Map<String, String[]>> singleConf() throws IOException {
        Map<String, Map<String, String[]>> map = new HashMap<String, Map<String, String[]>>();
        String resourceFile = path.toString();
        //List<String> sheetlist = XLSUtils.getSheetName(paths[i].toString());
        map.put(resourceFile + "接口模板", XLSUtils.getXlsData(resourceFile, "接口模板", 2));
        map.put(resourceFile + "接口配置Up", XLSUtils.getXlsData(resourceFile, "接口配置", 1));
        map.put(resourceFile + "接口配置Down", XLSUtils.getXlsData(resourceFile, "接口配置", 5));
        map.put(resourceFile + "用例集开关", XLSUtils.getXlsData(resourceFile, "用例集开关", 1));
        try {
			map.put(resourceFile + "#Sql校验", XLSUtils.getXlsData(resourceFile, "Sql校验", 2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(resourceFile+"无Sql校验");
		}
        return map;
    }

    public static Map<String,Map<String,String>> renameVariableMapping(Map<String, Map<String, String[]>> configMap){
		Map <String,Map<String,String>> mapReturn=new HashMap<String,Map<String,String>>();
		Map <String,String> singleMapping=new HashMap<String,String>();
    	for(String templetPath:configMap.keySet()){
    		if(templetPath.indexOf("接口模板")>-1){
    			Map<String,String[]> mapTemplet=configMap.get(templetPath);
    			String[] dataArray=mapTemplet.get("DATA");
    			for(int i=0;i<dataArray.length;i++){
    				String data=dataArray[i];
    				String API_NO=mapTemplet.get("API_NO")[i];
    				singleMapping=singleRenameVariableMapping(data);
    				String key=templetPath+"#"+API_NO;
    				mapReturn.put(key, singleMapping);
    			}
    		}
    	}
		return mapReturn;
    }
    
    public static Map<String,String> singleRenameVariableMapping(String Data){
		Map <String,String> mappingReturn=new HashMap<String,String>();
    	if(Data !=null&&Data.length()>1){
    		if(Data.replace(" ", "").replace("\"", "").substring(0, 1).equals("<")){
    			String regx="<([a-zA-Z0-9.=_-]+)>\\$GetExcelData\\(\"(.*)\"\\)<";
    			Pattern p = Pattern.compile(regx);
    			Matcher m = p.matcher(Data);
    			while (m.find()) {
    				String oldVariable=m.group(1);
    				String newVariable=m.group(2);
    				String oldVariablePut="";
    				int num=0;
    				if(!oldVariable.equals(newVariable)){
    					Pattern p1 = Pattern.compile("<"+oldVariable+">");
            			Matcher m1 = p1.matcher(Data);
            			while(m1.find()){
            				if(m1.start()==m.start()){
            					oldVariablePut =  oldVariable+"#"+num;
            				}
            				num++;
            			}
            			if(num==1){
            				oldVariablePut=oldVariable;
            			}
    					mappingReturn.put(newVariable, oldVariablePut);
    				}
    			}
    		}else if(Data.replace(" ", "").replace("\"", "").substring(0, 1).equals("{")){
    			String regx="\"([a-zA-Z0-9.=_-]+)\"[\\s]*:[\\s]*[\"]?\\$GetExcelData\\(\"(.*)\"\\)\"";
    			Pattern p = Pattern.compile(regx);
    			Matcher m = p.matcher(Data);
    			while (m.find()) {
    				String oldVariable=m.group(1);
    				String newVariable=m.group(2);
    				String oldVariablePut="";
    				int num=0;
    				if(!oldVariable.equals(newVariable)){
    					Pattern p1 = Pattern.compile("\""+oldVariable+"\"");
            			Matcher m1 = p1.matcher(Data);
            			while(m1.find()){
            				if(m1.start()==m.start()){
            					oldVariablePut =  oldVariable+"#"+num;
            				}
            				num++;
            			}
            			if(num==1){
            				oldVariablePut=oldVariable;
            			}
    					mappingReturn.put(newVariable, oldVariablePut);
    				}
    			}
    		
    		}else{
    			//String regx="&([a-zA-Z0-9.=_-]+)=\\$GetExcelData\\(\"(.*)\"\\)\"";
    			
    		}
    	}
		return mappingReturn;
    }

    public Map<String, Map<String, String[]>> mapFlag() throws IOException, InterruptedException, ExecutionException {
        Map<String, Map<String, String[]>> Flag = new HashMap<String, Map<String, String[]>>();
        for (String key : mapConf().keySet()) {
            if (key.endsWith("用例集开关")) {
                Flag.put(key, mapConf().get(key));
            }
        }
        return Flag;
    }

    public Map<String, Map<String, String[]>> mapConfigureUp() throws IOException, InterruptedException,
            ExecutionException {
        Map<String, Map<String, String[]>> ConfigureUp = new HashMap<String, Map<String, String[]>>();
        for (String key : mapConf().keySet()) {
            if (key.endsWith("接口配置Up")) {
                ConfigureUp.put(key, mapConf().get(key));
            }
        }
        return ConfigureUp;
    }

    public Map<String, Map<String, String[]>> mapConfigureDown() throws IOException, InterruptedException,
            ExecutionException {
        Map<String, Map<String, String[]>> ConfigureDown = new HashMap<String, Map<String, String[]>>();
        for (String key : mapConf().keySet()) {
            if (key.endsWith("接口配置Down")) {
                ConfigureDown.put(key, mapConf().get(key));
            }
        }
        return ConfigureDown;
    }

    public Map<String, Map<String, String[]>> mapTemplate() throws IOException, InterruptedException,
            ExecutionException {
        Map<String, Map<String, String[]>> DataTemplate = new HashMap<String, Map<String, String[]>>();
        for (String key : mapConf().keySet()) {
            if (key.endsWith("接口模板")) {
                DataTemplate.put(key, mapConf().get(key));
            }
        }
        return DataTemplate;
    }

}

package com.za.qa.dataprepare; 

import java.util.HashMap;
import java.util.Map;

import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseSuiteDTO;

/**   
*    
* 项目名称：za-api-automation   
* 类名称：CaseParameter   
* 类描述：   获取参数表中关键参数值
* 创建人：jujinxin   
* 创建时间：2016年11月14日 上午10:04:11   
* 修改人：jujinxin   
* 修改时间：2016年11月14日 上午10:04:11   
* 修改备注：      
*    
*/
public class CaseParameter {
	
//	private   Map<String, Map<String,String[]>> DataResource;
//	public  String listPath;
//	private  int i;
//	private  String env;
	
	
	public static Map<String,String> getParameter(CaseConfigurationDTO caseConfigurationDTO,CaseSuiteDTO casesuiteDTO){
		Map<String, Map<String,String[]>>  DataResource = caseConfigurationDTO.getDataResource();
		String listPath = caseConfigurationDTO.getListPath();
		String env = caseConfigurationDTO.getEnv();
        int i = casesuiteDTO.getI();
		Map<String,String> map=new HashMap<String,String>();
		String caseListNo = DataResource.get(listPath).get("CASElist_NO")[i].trim();
		String caseListDesc = DataResource.get(listPath).get("CASElist_Desc")[i].trim();
		String caseNo = DataResource.get(listPath).get("CASE_NO")[i].trim();

		String caseDesc = DataResource.get(listPath).get("CASE_Desc")[i].trim();
		String apiNo = DataResource.get(listPath).get("API_NO")[i].trim();
		
		String checkPoint = getValue("CHECKPOINT", caseConfigurationDTO, casesuiteDTO);
		String proProcessor = getValue("ProProcessor", caseConfigurationDTO, casesuiteDTO);
		String runFlag = getValue("RunFLAG", caseConfigurationDTO, casesuiteDTO);
		String recordFlag = getValue("RecFLAG", caseConfigurationDTO, casesuiteDTO);
		String postProcessor = getValue("PostProcessor", caseConfigurationDTO, casesuiteDTO);
		
		map.put("CASElist_NO", caseListNo);
		map.put("CASElist_Desc", caseListDesc);
		map.put("CASE_NO", caseNo);
		map.put("CASE_Desc", caseDesc);
		map.put("API_NO", apiNo);
		map.put("CHECKPOINT", checkPoint);
		map.put("ProProcessor", proProcessor);
		map.put("RunFLAG", runFlag);
		map.put("RecFLAG", recordFlag);
		map.put("PostProcessor", postProcessor);
		return map;

	}
	
	public static String getValue(String key,CaseConfigurationDTO caseConfigurationDTO,CaseSuiteDTO casesuiteDTO){
		Map<String, Map<String,String[]>>  DataResource = caseConfigurationDTO.getDataResource();
		String listPath = caseConfigurationDTO.getListPath();
		String env = caseConfigurationDTO.getEnv();
        int i = casesuiteDTO.getI();
        String Value="";
        String[] valueArry=DataResource.get(listPath).get(key);
    	if(valueArry!=null&&valueArry.length>i){
    		
            Value=valueArry[i].trim();
    		if(Value==""){
    			if(env.equalsIgnoreCase("uat")){
    				Value = valueArry[2].trim();
    				if(Value==""){
    					Value = valueArry[0].trim();
    				}
    			}else if(env.equalsIgnoreCase("iTest")){
    				Value = valueArry[1].trim();
    				if(Value==""){
    					Value = valueArry[0].trim();
    				}
    			}
    		}
    		
    	}

		return Value;
	}


}
 
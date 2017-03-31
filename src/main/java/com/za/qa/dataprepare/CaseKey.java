package com.za.qa.dataprepare;

import java.util.HashMap;
import java.util.Map;

import com.za.qa.dto.CaseConfigurationDTO;

/**   
*    
* 项目名称：za-api-automation   
* 类名称：CaseKey   
* 类描述：   获取接口配置中appKey和devPrivateKey
* 创建人：jujinxin   
* 创建时间：2016年11月11日 上午11:52:16   
* 修改人：jujinxin   
* 修改时间：2016年11月15日 上午18:52:16   
* 修改备注：      
*    
*/
public class CaseKey {

	public static Map<String,String> getAppPrivate_Key(CaseConfigurationDTO caseConfigurationDTO){
        Map<String, Map<String, String[]>> DataConfiguration = caseConfigurationDTO.getDataConfiguration();
        String listPath = caseConfigurationDTO.getListPath();
        String listflag = caseConfigurationDTO.getListFlag();
		Map<String,String> map=new HashMap<String,String>();
		String appKey="";
		String devPrivateKey="";
		String[] pathsplit=listPath.split("#");
		String excelPath = pathsplit[0];
			for(String configurationKey:DataConfiguration.keySet()){
				if(configurationKey.equals(excelPath+"接口配置Up")){
			   if(listflag.equalsIgnoreCase("iTest")){
							appKey = DataConfiguration.get(excelPath+"接口配置Up").get("AppKey")[0].trim();
							devPrivateKey = DataConfiguration.get(excelPath+"接口配置Up").get("DevPrivateKey")[0].trim();
						}else if(listflag.equalsIgnoreCase("uat")){
							appKey = DataConfiguration.get(excelPath+"接口配置Up").get("AppKey")[1].trim();
							devPrivateKey = DataConfiguration.get(excelPath+"接口配置Up").get("DevPrivateKey")[1].trim();

						}
					}
				
				}
			map.put("AppKey", appKey);
			map.put("DevPrivateKey", devPrivateKey);
		return map;
	}
	
}

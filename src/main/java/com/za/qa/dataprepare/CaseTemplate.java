package com.za.qa.dataprepare;

import java.util.HashMap;
import java.util.Map;

import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseSuiteDTO;

/**   
*    
* 项目名称：za-api-automation   
* 类名称：CaseTemplate   
* 类描述：   获取接口模板中参数
* 创建人：jujinxin   
* 创建时间：2016年11月11日 上午11:51:07   
* 修改人：jujinxin   
* 修改时间：2016年11月16日 上午10:11:07   
* 修改备注：      
*    
*/
public class CaseTemplate {
	

	public static Map<String,String> getTemplateData(CaseConfigurationDTO caseConfigurationDTO,CaseSuiteDTO casesuiteDTO){
		Map<String, Map<String,String[]>> DataConfiguration = caseConfigurationDTO.getDataConfiguration();
		String listPath = caseConfigurationDTO.getListPath();
		String apiNo = casesuiteDTO.getApiNo();
		Map<String,String> map=new HashMap<String,String>();
		String type= "";
		String data= "";
		String serviceName= "";
		String resourcePath= "";
		String requestDto= "";
		String checkpoint= "";
		String charset= "";
		String [] pathsplit=listPath.split("#");
		String excelPath = pathsplit[0];
		for(String configurationKey:DataConfiguration.keySet()){
			if(configurationKey.equalsIgnoreCase(excelPath+"接口模板")){
				String api_no_configuration[] = DataConfiguration.get(configurationKey).get("API_NO");
				for(int i=0;i<api_no_configuration.length;i++){
					if(api_no_configuration[i].equals(apiNo)){
						try {
							type = DataConfiguration.get(configurationKey).get("TYPE")[i].trim();
							data = DataConfiguration.get(configurationKey).get("DATA")[i].trim(); 
							serviceName = DataConfiguration.get(configurationKey).get("SERVICE_NAME")[i].trim();
							resourcePath = DataConfiguration.get(configurationKey).get("ResourcePath")[i].trim();
							requestDto = DataConfiguration.get(configurationKey).get("REQUEST_DTO")[i].trim();
							String []checkpointArry = DataConfiguration.get(configurationKey).get("CHECKPOINT");
							//以后新增字段均使用以下方式（为了兼容以前版本excel），避免以前的excel无新增字段
							if(checkpointArry!=null&&checkpointArry.length>i)	{
								checkpoint=checkpointArry[i].trim();
							}
							String[]	charsetArry = DataConfiguration.get(configurationKey).get("charset");
							if(charsetArry!=null&&charsetArry.length>i)	{
								charset=charsetArry[i].trim();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
						
				}
				break;
			}
		}
		map.put("TYPE", type);
		map.put("DATA", data);
		map.put("SERVICE_NAME", serviceName);
		map.put("ResourcePath", resourcePath);
		map.put("REQUEST_DTO", requestDto);
		map.put("CHECKPOINT", checkpoint);
		map.put("charset", charset);
		return map;
	}

	
}

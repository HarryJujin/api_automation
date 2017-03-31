package com.za.qa.dataprepare;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.log.LogMan;

/**   
*    
* 项目名称：za-api-automation   
* 类名称：CaseListFlag   
* 类描述：   获取用例集开关
* 创建人：jujinxin   
* 创建时间：2016年11月11日 上午11:53:16   
* 修改人：jujinxin   
* 修改时间：2016年11月15日 上午18:33:16   
* 修改备注：      
*    
*/
public class CaseListFlag {

	public static String getList_Flag(CaseConfigurationDTO caseConfigurationDTO){
		Map<String, Map<String, String[]>> DataConfiguration = caseConfigurationDTO.getDataConfiguration();
		String listPath = caseConfigurationDTO.getListPath();
		String listName = caseConfigurationDTO.getListName();
		List<String> list= new LinkedList<String>();
		String list_Flag="";
		String [] pathsplit=listPath.split("#");
		String excelPath = pathsplit[0];
			for(String configurationKey:DataConfiguration.keySet()){
				if(configurationKey.equals(excelPath+"用例集开关")){
				String array_name_configuration[] = DataConfiguration.get(configurationKey).get("CASElist_NO");
				for(int i=1;i<array_name_configuration.length;i++){
					list.add(array_name_configuration[i]);
					}
                    //判断用例集开关里是否包含改用例集
					if(list.toString().contains(listName)){
						for(int i=1;i<array_name_configuration.length;i++){
							if(array_name_configuration[i].equalsIgnoreCase(listName)){
								try {
									list_Flag = DataConfiguration.get(configurationKey).get("List_Flag")[i].trim();
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							}
					    }
					}else{
						try {
							list_Flag = DataConfiguration.get(configurationKey).get("List_Flag")[0].trim();
						} catch (Exception e) {
							e.printStackTrace();
						}
						LogMan.getLoger().error("[用例集开关]: "+configurationKey+" 未包含 "+listName);
					}
				}
			}
		
		return list_Flag;
	}


}

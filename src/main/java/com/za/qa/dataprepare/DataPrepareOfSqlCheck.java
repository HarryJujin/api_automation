package com.za.qa.dataprepare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseDataDTO;
import com.za.qa.keywords.ExpressionRegister;

public class DataPrepareOfSqlCheck {
	/**
     * @author heyuan 
     */
	public static void setSqlExpectdata(CaseConfigurationDTO caseConfigurationDTO){
    	//设置sql校验预期数据
        for(String key:caseConfigurationDTO.getDataConfiguration().keySet()){
        	if(key.contains("Sql校验")){
        		String[] listNameArray=key.split("#");
        		Map<String,String[]> sqlMap=caseConfigurationDTO.getDataConfiguration().get(key);
        		if(sqlMap!=null){
        			DataPrepareOfSqlCheck.setOneSqlExpectdata(sqlMap, listNameArray[0]);
        		}
        	}
        }
	}
    public static void setOneSqlExpectdata(Map<String,String[]> oneSheetSqlMapData,String excelPath){
    	///////***************
    	Map<String,Map<String, List<Integer>>> map=getTargetKeySet(oneSheetSqlMapData);
    	Set <String> set=map.keySet();
    	//Set<Map<String, List<Integer>>> set2=(Set<Map<String, List<Integer>>>) map.values();
    	for(String caseKey:set){
    		String newCaseKey=excelPath+"#"+caseKey;
			Map<String,Map<String,List<String>>> mapTemp=new HashMap<String,Map<String,List<String>>>();
    		for(String tableKey:map.get(caseKey).keySet()){
    			List<Integer> list= map.get(caseKey).get(tableKey);
    			
    			//存入最小单元的数据Map<String,List<String>>
				Map<String,List<String>> mapStr=new HashMap<String,List<String>>();
				for(String key:oneSheetSqlMapData.keySet()){
					List<String> listStr=new ArrayList<String>();
					for(int i:list){
    					listStr.add(oneSheetSqlMapData.get(key)[i]);
    				}
					mapStr.put(key, listStr);
    			}
				
				mapTemp.put(tableKey, mapStr);
    		}
    		ExpressionRegister.sqlExpectEnv.put(newCaseKey, mapTemp);
    	}
    }

    
    public static Map<String,Map<String, List<Integer>>> getTargetKeySet(Map<String,String[]> oneSheetSqlMap){
		String[] CASElist_NOArray=oneSheetSqlMap.get("CASElist_NO");
		String[] CASE_NOArray=oneSheetSqlMap.get("CASE_NO");
		String[] dbnameArray=oneSheetSqlMap.get("dbname");
		String[] tbnameArray=oneSheetSqlMap.get("tbname");
		String[] WhereCondition=oneSheetSqlMap.get("WhereCondition");

		Map<String,List<Integer>> mapTemp=new HashMap<String,List<Integer>>();
		//获取测试案例数据集合名称及对应的坐标
        Set<String> setOfListCase = new LinkedHashSet<String>();
		for(int i=0;i<CASElist_NOArray.length;i++){
			String keyOfListCase = CASElist_NOArray[i]+"#"+CASE_NOArray[i];
			int sizeBegin=setOfListCase.size();
			setOfListCase.add(keyOfListCase);
			int sizeAfter=setOfListCase.size();
			if(sizeAfter>sizeBegin){
				List<Integer> list=new ArrayList<Integer>();
				list.add(i);
				mapTemp.put(keyOfListCase,list);
			}else{
				//List<Integer> list=new ArrayList<Integer>();
				mapTemp.get(keyOfListCase).add(i);
				//mapTemp.put(keyOfListCase,list);
			}
		}
		//获取测试案例内的相同查询集合及对应的坐标
		Map <String,Map<String,List<Integer>>> mapReturn=new HashMap<String,Map<String,List<Integer>>>();
        Set<String> setOfTableWhereCondion = new LinkedHashSet<String>();
		for(String key:mapTemp.keySet()){
			Map<String,List<Integer>> mapOftabletemp=new HashMap <String,List<Integer>>();
			int count=0;
			for(int j:mapTemp.get(key)){
				String TableWhereCondion=dbnameArray[j]+"#"+tbnameArray[j]+"#"+WhereCondition[j];
				int sizeBegin=setOfTableWhereCondion.size();
				setOfTableWhereCondion.add(TableWhereCondion);
				int sizeAfter=setOfTableWhereCondion.size();
				if(sizeAfter>sizeBegin){
					count++;
					List<Integer> list=new ArrayList<Integer>();
					list.add(j);
					mapOftabletemp.put(TableWhereCondion+"#"+count,list);
				}else{
					mapOftabletemp.get(TableWhereCondion+"#"+count).add(j);
				}
			}
			mapReturn.put(key, mapOftabletemp);
		}
		return mapReturn;
    }

}

package com.za.qa.processor; 

import com.za.qa.analyze.DataAnalyze;
import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseDataDTO;
import com.za.qa.log.LogMan;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月25日 上午10:15:06 
 * 类说明 
 */
public class Proprocessor {
	public static void doProprocessor(CaseDataDTO casedatadto, CaseConfigurationDTO caseConfigurationDTO){
		String proprocessor ="";
		try {
			proprocessor = casedatadto.getProProcessor();
		} catch (Exception e1) {
			//e1.printStackTrace();
			LogMan.getLoger().info("前置处理ProProcessor对象为空，忽略此操作，执行下一步");
		}
		//前置处理ProProcessor
			if(proprocessor.length()>0){
				String ProProcessorResult = "";
				try {
					ProProcessorResult = DataAnalyze.analyzeStr(proprocessor,casedatadto, caseConfigurationDTO);
				} catch (Exception e) {
					e.printStackTrace();
				}
				LogMan.getLoger().info("前置处理ProProcessor解析：\n" + ProProcessorResult );
			}
	}
}
 
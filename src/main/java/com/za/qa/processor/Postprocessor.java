package com.za.qa.processor; 

import com.za.qa.analyze.DataAnalyze;
import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseDataDTO;
import com.za.qa.log.LogMan;

/** 
 * @author jujinxin 
 * @version 创建时间：2017年1月10日 下午4:47:33 
 * 类说明 
 */
public class Postprocessor {

	public static void doPostprocessor(CaseDataDTO casedatadto, CaseConfigurationDTO caseConfigurationDTO){
		String postprocessor ="";
		try {
			postprocessor = casedatadto.getPostProcessor();
		} catch (Exception e1) {
			//e1.printStackTrace();
			LogMan.getLoger().info("后置处理PostProcessor对象为空，忽略此操作，执行下一步");
		}
		//前置处理ProProcessor
			if(postprocessor.length()>0){
				String PostProcessorResult = "";
				try {
					PostProcessorResult = DataAnalyze.analyzeStr(postprocessor,casedatadto, caseConfigurationDTO);
					PostProcessorResult = DataAnalyze.analyzeCurrent(PostProcessorResult,casedatadto, caseConfigurationDTO);
				} catch (Exception e) {
					e.printStackTrace();
				}
				LogMan.getLoger().info("后置处理PostProcessor解析：\n" + PostProcessorResult );
			}
	}


}
 
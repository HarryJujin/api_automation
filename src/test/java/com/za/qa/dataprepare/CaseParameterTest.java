package com.za.qa.dataprepare; 

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.za.qa.dto.CaseConfigurationDTO;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月14日 上午10:57:12 
 * 类说明 
 */
public class CaseParameterTest {

	@Test
	public void test() throws Exception {
		String path = "D:\\workspace\\TestCase\\健康险_康盛_团体意外伤害_金鑫V1.xlsx#出单—尊享款";
		 CaseConfigurationDTO caseConfigurationDTO = new CaseConfigurationDTO();
		 caseConfigurationDTO.setDataResource(DataResource.TestSuiteOfSheet());
		 caseConfigurationDTO.setListPath(path);
		 caseConfigurationDTO.setEnv("uat");
		 caseConfigurationDTO.setI(3);
		 
		System.out.println(CaseParameter.getParameter(caseConfigurationDTO).get("CASElist_NO"));
		System.out.println(CaseParameter.getParameter(caseConfigurationDTO).get("CASElist_Desc"));
		System.out.println(CaseParameter.getParameter(caseConfigurationDTO).get("CASE_NO"));
		System.out.println(CaseParameter.getParameter(caseConfigurationDTO).get("CASE_Desc"));
		System.out.println(CaseParameter.getParameter(caseConfigurationDTO).get("API_NO"));
		System.out.println(CaseParameter.getParameter(caseConfigurationDTO).get("CHECKPOINT"));
		System.out.println(CaseParameter.getParameter(caseConfigurationDTO).get("ProProcessor"));
		System.out.println(CaseParameter.getParameter(caseConfigurationDTO).get("RunFLAG"));
		System.out.println(CaseParameter.getParameter(caseConfigurationDTO).get("RecFLAG"));
		System.out.println(CaseParameter.getParameter(caseConfigurationDTO).get("Depend"));
	}

}
 
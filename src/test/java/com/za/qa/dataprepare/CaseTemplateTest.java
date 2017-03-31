package com.za.qa.dataprepare; 

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.za.qa.dto.CaseConfigurationDTO;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月12日 下午6:49:44 
 * 类说明 
 */
public class CaseTemplateTest {

	@Test
	public void test() throws IOException, InterruptedException, ExecutionException {
		String path = "D:\\workspace\\TestCase\\健康险_康盛_团体意外伤害_金鑫V1.xlsx#支付宝乐牙网儿童齿科";
		CaseConfigurationDTO caseConfigurationDTO = new CaseConfigurationDTO();
		caseConfigurationDTO.setDataConfiguration(DataConfiguration.mapConf());
		caseConfigurationDTO.setListPath(path);
		caseConfigurationDTO.setApiNo("支付宝核保");
		System.out.println("Type:"+CaseTemplate.getTemplateData(caseConfigurationDTO).get("TYPE"));
		System.out.println("Data:"+CaseTemplate.getTemplateData(caseConfigurationDTO).get("DATA"));
		System.out.println("ServiceName:"+CaseTemplate.getTemplateData(caseConfigurationDTO).get("SERVICE_NAME"));
		System.out.println("ResourcePath:"+CaseTemplate.getTemplateData(caseConfigurationDTO).get("ResourcePath"));
		System.out.println("RequestDto:"+CaseTemplate.getTemplateData(caseConfigurationDTO).get("REQUEST_DTO"));
		System.out.println("Checkpoint:"+CaseTemplate.getTemplateData(caseConfigurationDTO).get("CHECKPOINT"));
		
	}

}
 
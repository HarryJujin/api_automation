package com.za.qa.dataprepare; 

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.za.qa.dto.CaseConfigurationDTO;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月12日 下午6:34:06 
 * 类说明 
 */
public class CaseKeyTest {

	@Test
	public void testGetAppPrivate_Key() throws IOException, InterruptedException, ExecutionException {
		String path = "D:\\workspace\\TestCase\\健康险_康盛_团体意外伤害_金鑫V1.xlsx#支付宝乐牙网儿童齿科";
        CaseConfigurationDTO caseConfigurationDTO = new CaseConfigurationDTO();
        caseConfigurationDTO.setDataConfiguration(DataConfiguration.mapConf());
        caseConfigurationDTO.setListPath(path);
        caseConfigurationDTO.setRun_flag("uat");
        Map<String, String> config = CaseKey.getAppPrivate_Key(caseConfigurationDTO);
		System.out.println("Appkey:"+config.get("AppKey")+"DevPrivateKey:"+config.get("DevPrivateKey"));
	}



}
 
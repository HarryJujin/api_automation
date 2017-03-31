package com.za.qa.dataprepare; 

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.za.qa.dto.CaseConfigurationDTO;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月12日 下午7:58:21 
 * 类说明 
 */
public class CaseListFlagTest {

	@Test
	public void test() throws IOException, InterruptedException, ExecutionException {
		String path = "D:\\workspace\\TestCase\\健康险_康盛_团体意外伤害_金鑫V1.xlsx#出单—尊享款";
        CaseConfigurationDTO caseConfigurationDTO = new CaseConfigurationDTO();
        caseConfigurationDTO.setDataConfiguration(DataConfiguration.mapConf());
        caseConfigurationDTO.setListPath(path);
        caseConfigurationDTO.setListName("出单—尊享款");
        System.out.println("用例集开关:"+CaseListFlag.getRun_Flag(caseConfigurationDTO));
        
		
	}

}
 
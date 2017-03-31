package com.za.qa.dataprepare; 

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseSuiteDTO;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月15日 下午5:57:36 
 * 类说明 
 */
public class CaseIpVersionTest {
    @Test
    public void testGetIp() throws IOException, InterruptedException, ExecutionException {
      
        File directory = new File("");// 参数为空  
        String path = directory.getCanonicalPath()+"\\src\\test\\resources\\Test.xlsx";
        CaseConfigurationDTO caseConfigurationDTO = new CaseConfigurationDTO();
        caseConfigurationDTO.setDataConfiguration(DataConfiguration.mapConf());
        caseConfigurationDTO.setListPath(path);
        caseConfigurationDTO.setEnv("uat");
        CaseSuiteDTO  casesuiteDTO = new CaseSuiteDTO();
        casesuiteDTO.setApiNo("核保");
        casesuiteDTO.setI(4);
        Map<String, String> config = CaseIpVersion.getIP_Version(caseConfigurationDTO, casesuiteDTO);
        System.out.println("IP:" + config.get("IP") + "---" + "VERSION:" + config.get("VERSION"));


    }

}
 
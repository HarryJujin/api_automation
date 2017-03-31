package com.za.qa.dataprepare;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseSuiteDTO;
import com.za.qa.log.LogMan;

/**
 * 项目名称：za-api-automation 类名称：CaseIpVersion 类描述： 获取接口配置模板中 Ip,Version
 * 创建人：jujinxin 创建时间：2016年11月11日 上午11:49:48 修改人：jujinxin 修改时间：2016年11月12日
 * 下午5:06:54 修改备注：
 */

public class CaseIpVersion {

    public static Map<String, String> getIP_Version(CaseConfigurationDTO caseConfigurationDTO,CaseSuiteDTO casesuiteDTO) {
        Map<String, Map<String, String[]>> DataConfiguration = caseConfigurationDTO.getDataConfiguration();
        String apiNo = casesuiteDTO.getApiNo();
        String listflag = caseConfigurationDTO.getListFlag();
        List<String> list = new LinkedList<String>();
        Map<String, String> map = new HashMap<String, String>();
        String ip = "";
        String version = "";
        String[] pathsplit = caseConfigurationDTO.getListPath().split("#");
        String excelPath = pathsplit[0];
        for (String configurationKey : DataConfiguration.keySet()) {
            if (configurationKey.equals(excelPath + "接口配置Down")) {
                String api_no_configuration[] = DataConfiguration.get(configurationKey).get("API_NO");
                for (int i = 0; i < api_no_configuration.length; i++) {
                    list.add(api_no_configuration[i]);
                }
                if (list.toString().contains(apiNo)) {
                    for (int i = 0; i < api_no_configuration.length; i++) {
                        if (api_no_configuration[i].equalsIgnoreCase(apiNo)) {
                            String env = DataConfiguration.get(configurationKey).get("Env")[i].trim();
                            if (env.equalsIgnoreCase(listflag)) {
                                try {
                                    ip = DataConfiguration.get(configurationKey).get("IP")[i].trim();
                                    version = DataConfiguration.get(configurationKey).get("VERSION")[i].trim();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (ip == "") {
                                    LogMan.getLoger().info("[接口配置Down]: " + configurationKey + " 未配置IP,取接口配置Up中的值");
                                    if (listflag.equalsIgnoreCase("iTest")) {
                                        ip = DataConfiguration.get(excelPath + "接口配置Up").get("IP")[0].trim();
                                    } else if (listflag.equalsIgnoreCase("uat")) {
                                        ip = DataConfiguration.get(excelPath + "接口配置Up").get("IP")[1].trim();
                                    }
                                }
                                if (version == "") {
                                    LogMan.getLoger().info("[接口配置Down]: " + configurationKey + " 未配置VERSION,取接口配置Up中的值");
                                    if (listflag.equalsIgnoreCase("iTest")) {
                                        version = DataConfiguration.get(excelPath + "接口配置Up").get("VERSION")[0].trim();
                                    } else if (listflag.equalsIgnoreCase("uat")) {
                                        version = DataConfiguration.get(excelPath + "接口配置Up").get("VERSION")[1].trim();
                                    }
                                }
                                break;
                            }
                            else if (listflag.equalsIgnoreCase("iTest")) {
                                ip = DataConfiguration.get(excelPath + "接口配置Up").get("IP")[0].trim();
                                version = DataConfiguration.get(excelPath + "接口配置Up").get("VERSION")[0].trim();
                            } else if (listflag.equalsIgnoreCase("uat")) {
                                ip = DataConfiguration.get(excelPath + "接口配置Up").get("IP")[1].trim();
                                version = DataConfiguration.get(excelPath + "接口配置Up").get("VERSION")[1].trim();
                            }
                        }
                    }
                } else if (listflag.equalsIgnoreCase("iTest")) {
                    ip = DataConfiguration.get(excelPath + "接口配置Up").get("IP")[0].trim();
                    version = DataConfiguration.get(excelPath + "接口配置Up").get("VERSION")[0].trim();
                } else if (listflag.equalsIgnoreCase("uat")) {
                    ip = DataConfiguration.get(excelPath + "接口配置Up").get("IP")[1].trim();
                    version = DataConfiguration.get(excelPath + "接口配置Up").get("VERSION")[1].trim();
                }
            }
        }
        map.put("IP", ip);
        map.put("VERSION", version);
        return map;
    }

}

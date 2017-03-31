package com.za.qa.constants;

import java.io.File;
import java.io.IOException;

/**
 * @author jujinxin
 * @version 创建时间：2016年12月8日 上午10:21:02 类说明
 */
public class CaseConf {
    public static final String confpath = getConfigPath();
    public static final String vmpath   = "vmTemplates/localTestReport.vm";

    public static String getConfigPath() {
        try {
            String[] tmpDir = new File("").getCanonicalPath().split("/");
            String rootDir = "";
            for (int i = 0; i < tmpDir.length; i++) {
                if (!tmpDir[i].contains("jar")) {
                    rootDir += tmpDir[i] + "/";
                }
            }
            return rootDir + "conf.properties";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

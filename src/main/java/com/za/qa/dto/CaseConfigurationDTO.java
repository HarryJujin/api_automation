package com.za.qa.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * 类CaseConfigurationDTO.java的实现描述：接口模板配置DTO
 * 
 * @author linyun 2016年11月15日 下午3:01:47
 */
public class CaseConfigurationDTO implements Serializable {

    /**
     * 
     */
    private static final long                  serialVersionUID = 4382641441872123581L;

    private Map<String, Map<String, String[]>> DataConfiguration;
    private Map<String, Map<String, String[]>> DataResource;
    private String                             listPath;
    private String                             listName;
    private String                             listFlag;
    private String                             env;
    private Map<String,Map<String, String>>    renameVariableMapping;

    public Map<String, Map<String, String[]>> getDataConfiguration() {
        return DataConfiguration;
    }

    public void setDataConfiguration(Map<String, Map<String, String[]>> dataConfiguration) {
        DataConfiguration = dataConfiguration;
    }

    public Map<String, Map<String, String[]>> getDataResource() {
        return DataResource;
    }

    public void setDataResource(Map<String, Map<String, String[]>> dataResource) {
        DataResource = dataResource;
    }

    public String getListPath() {
        return listPath;
    }

    public void setListPath(String listPath) {
        this.listPath = listPath;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getListFlag() {
        return listFlag;
    }

    public void setListFlag(String listFlag) {
        this.listFlag = listFlag;
    }

    public Map<String, Map<String, String>> getRenameVariableMapping() {
		return renameVariableMapping;
	}

	public void setRenameVariableMapping(
			Map<String, Map<String, String>> renameVariableMapping) {
		this.renameVariableMapping = renameVariableMapping;
	}

	@Override
    public String toString() {
        return "CaseConfigurationDTO [DataConfiguration=" + DataConfiguration + ", DataResource=" + DataResource
                + ", listPath=" + listPath + ", listName=" + listName + ", listFlag=" + listFlag + ", env=" + env + "]";
    }

}

package com.za.qa.dto;

import java.io.Serializable;

/**
 * @author jujinxin
 * @version 创建时间：2016年11月16日 上午10:54:11 类说明
 */
public class CaseDataDTO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6413555459347497062L;

    private String            ip;
    private String            version;
    private String            appkey;
    private String            devprivatekey;
    private String            type;
    private String            data;
    private String            serviceName;
    private String            resourcePath;
    private String            requestDto;
    private String            caseListNo;
    private String            caseListDesc;
    private String            caseNo;
    private String            caseDesc;
    private String            apiNo;
    private String            checkPoint;
    private String            proProcessor;
    private String            runFlag;
    private String            listflag;
    private String            recordFlag;
    private String            postProcessor;
	private String            charset;




    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getDevprivatekey() {
        return devprivatekey;
    }

    public void setDevprivatekey(String devprivatekey) {
        this.devprivatekey = devprivatekey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getRequestDto() {
        return requestDto;
    }

    public void setRequestDto(String requestDto) {
        this.requestDto = requestDto;
    }

    public String getCaseListNo() {
        return caseListNo;
    }

    public void setCaseListNo(String caseListNo) {
        this.caseListNo = caseListNo;
    }

    public String getCaseListDesc() {
        return caseListDesc;
    }

    public void setCaseListDesc(String caseListDesc) {
        this.caseListDesc = caseListDesc;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    public String getApiNo() {
        return apiNo;
    }

    public void setApiNo(String apiNo) {
        this.apiNo = apiNo;
    }

    public String getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint) {
        this.checkPoint = checkPoint;
    }

    public String getProProcessor() {
        return proProcessor;
    }

    public void setProProcessor(String proProcessor) {
        this.proProcessor = proProcessor;
    }

    public String getRunFlag() {
        return runFlag;
    }

    public void setRunFlag(String runFlag) {
        this.runFlag = runFlag;
    }

    public String getListflag() {
        return listflag;
    }

    public void setListflag(String listflag) {
        this.listflag = listflag;
    }

    public String getRecordFlag() {
        return recordFlag;
    }

    public void setRecordFlag(String recordFlag) {
        this.recordFlag = recordFlag;
    }

    public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
    public String getPostProcessor() {
		return postProcessor;
	}

	public void setPostProcessor(String postProcessor) {
		this.postProcessor = postProcessor;
	}

	@Override
	public String toString() {
		return "CaseDataDTO [ip=" + ip + ", version=" + version + ", appkey="
				+ appkey + ", devprivatekey=" + devprivatekey + ", type="
				+ type + ", data=" + data + ", serviceName=" + serviceName
				+ ", resourcePath=" + resourcePath + ", requestDto="
				+ requestDto + ", caseListNo=" + caseListNo + ", caseListDesc="
				+ caseListDesc + ", caseNo=" + caseNo + ", caseDesc="
				+ caseDesc + ", apiNo=" + apiNo + ", checkPoint=" + checkPoint
				+ ", proProcessor=" + proProcessor + ", runFlag=" + runFlag
				+ ", listflag=" + listflag + ", recordFlag=" + recordFlag
				+ ", postProcessor=" + postProcessor + ", charset=" + charset
				+ "]";
	}

}

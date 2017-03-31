package com.za.qa.dto;

import java.io.Serializable;

/**
 * @author jujinxin
 * @version 创建时间：2016年11月23日 下午4:47:35 类说明
 */
public class CaseReportDTO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 607044678313659605L;
    private String            url;
    private String            payloaddata;                           //解析后的入参
    private String            reponse;
    private String            checkpoint;
    private String            test_result;
    private String            errorInfo;                             //错误信息
    private CaseDataDTO       caseDataDTO;                           //完整的测试数据
    private String            orderNumOfSuite;   					 //测试集顺序编号

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayloaddata() {
        return payloaddata;
    }

    public void setPayloaddata(String payloaddata) {
        this.payloaddata = payloaddata;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }

    public String getTest_result() {
        return test_result;
    }

    public void setTest_result(String test_result) {
        this.test_result = test_result;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public CaseDataDTO getCaseDataDTO() {
        return caseDataDTO;
    }

    public void setCaseDataDTO(CaseDataDTO caseDataDTO) {
        this.caseDataDTO = caseDataDTO;
    }

    public String getOrderNumOfSuite() {
		return orderNumOfSuite;
	}

	public void setOrderNumOfSuite(String orderNumOfSuite) {
		this.orderNumOfSuite = orderNumOfSuite;
	}

	@Override
    public String toString() {
        return "CaseReportDTO [url=" + url + ", payloaddata=" + payloaddata + ", reponse=" + reponse + ", checkpoint="
                + checkpoint + ", test_result=" + test_result + ", errorInfo=" + errorInfo + ", caseDataDTO="
                + caseDataDTO + "]";
    }

}

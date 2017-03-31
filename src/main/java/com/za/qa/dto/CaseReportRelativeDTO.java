package com.za.qa.dto;

import java.io.Serializable;

import com.za.qa.constants.CaseConf;

/**
 * 类CaseReportRelativeDTO.java的实现描述：测试报告相关数据
 * 
 * @author linyun 2016年11月30日 上午9:39:59
 */
public class CaseReportRelativeDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 349578566499908425L;
    private String            reportFileName;                                       //报告名称
    private String            reportVmPath     = CaseConf.vmpath; //测试报告模板路径
    private String            beginTime;                                            //测试开始时间
    private String            duringTime;                                           //测试耗时
    private String            endTime;                                              //测试结束时间
    private String            passedPercent;                                        //通过率
    private String            failedPercent;                                        //失败率
    private String            skippedPercent;                                       //跳过率
    private String            caseNum;                                              //案例总数
    private String            passedNum;                                            //通过数
    private String            failedNum;                                            //失败数
    private String            skippedNum;                                           //跳过数

    public String getReportFileName() {
        return reportFileName;
    }

    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getDuringTime() {
        return duringTime;
    }

    public void setDuringTime(String duringTime) {
        this.duringTime = duringTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPassedPercent() {
        return passedPercent;
    }

    public void setPassedPercent(String passedPercent) {
        this.passedPercent = passedPercent;
    }

    public String getFailedPercent() {
        return failedPercent;
    }

    public void setFailedPercent(String failedPercent) {
        this.failedPercent = failedPercent;
    }

    public String getSkippedPercent() {
        return skippedPercent;
    }

    public void setSkippedPercent(String skippedPercent) {
        this.skippedPercent = skippedPercent;
    }

    public String getReportVmPath() {
        return reportVmPath;
    }

    public void setReportVmPath(String reportVmPath) {
        this.reportVmPath = reportVmPath;
    }

    public String getPassedNum() {
        return passedNum;
    }

    public void setPassedNum(String passedNum) {
        this.passedNum = passedNum;
    }

    public String getFailedNum() {
        return failedNum;
    }

    public void setFailedNum(String failedNum) {
        this.failedNum = failedNum;
    }

    public String getSkippedNum() {
        return skippedNum;
    }

    public void setSkippedNum(String skippedNum) {
        this.skippedNum = skippedNum;
    }

    public String getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(String caseNum) {
        this.caseNum = caseNum;
    }

    @Override
    public String toString() {
        return "CaseReportRelativeDTO [reportFileName=" + reportFileName + ", reportVmPath=" + reportVmPath
                + ", beginTime=" + beginTime + ", duringTime=" + duringTime + ", endTime=" + endTime
                + ", passedPercent=" + passedPercent + ", failedPercent=" + failedPercent + ", skippedPercent="
                + skippedPercent + ", caseNum=" + caseNum + ", passedNum=" + passedNum + ", failedNum=" + failedNum
                + ", skippedNum=" + skippedNum + "]";
    }
}

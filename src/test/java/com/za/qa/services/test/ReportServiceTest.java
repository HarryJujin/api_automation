package com.za.qa.services.test;

import java.util.LinkedList;

import org.junit.Test;

import com.za.qa.dto.CaseDataDTO;
import com.za.qa.dto.CaseReportDTO;
import com.za.qa.dto.CaseReportRelativeDTO;
import com.za.qa.services.ReportService;

public class ReportServiceTest {

    @Test
    public void testOutputReport() {
        String testContent = "很多很多文字很多很多文字很多很多文字很多很多文字很多很多文字很多很多文字很多很多文字很多很多文字很多很多文字";
        LinkedList<CaseReportDTO> caseReportDTOList = new LinkedList<CaseReportDTO>();
        CaseReportRelativeDTO caseReportRelativeDTO = new CaseReportRelativeDTO();
        CaseReportDTO passDTO = new CaseReportDTO();
        CaseReportDTO failDTO = new CaseReportDTO();
        CaseReportDTO skipDTO = new CaseReportDTO();
        CaseDataDTO caseDataDTO = new CaseDataDTO();
        caseReportRelativeDTO.setReportFileName("众安在线接口测试报告");
        caseReportRelativeDTO.setBeginTime("2016-11-30 09:55:00");
        caseReportRelativeDTO.setEndTime("2016-11-30 10:00:00");
        caseReportRelativeDTO.setDuringTime("5min 0sec");
        caseReportRelativeDTO.setPassedPercent("60%");
        caseReportRelativeDTO.setSkippedPercent("25%");
        caseReportRelativeDTO.setFailedPercent("15%");
        caseReportRelativeDTO.setPassedNum("60");
        caseReportRelativeDTO.setSkippedNum("25");
        caseReportRelativeDTO.setFailedNum("15");
        caseReportRelativeDTO.setCaseNum("100");
        //初始化caseDataDTO
        caseDataDTO.setCaseListNo("testEnglish");
        caseDataDTO.setCaseNo("中文测试一下");
        caseDataDTO.setCaseDesc("测试环境试一下/测试环境试一下");
        caseDataDTO.setListflag("itest");
        caseDataDTO.setApiNo("测试接口");
        caseDataDTO.setServiceName("com.zhongan.qa.test.services");
        //初始化caseReportDTO
        passDTO.setCaseDataDTO(caseDataDTO);
        passDTO.setCheckpoint(testContent);
        passDTO.setPayloaddata(testContent);
        passDTO.setReponse(testContent);
        passDTO.setErrorInfo(testContent);
        passDTO.setTest_result("NotRun");
        passDTO.setUrl("https://www.baidu.com");
        failDTO.setCaseDataDTO(caseDataDTO);
        failDTO.setCheckpoint(testContent);
        failDTO.setPayloaddata(testContent);
        failDTO.setReponse(testContent);
        failDTO.setErrorInfo(testContent);
        failDTO.setTest_result("Fail");
        failDTO.setUrl("https://www.baidu.com");
        skipDTO.setCaseDataDTO(caseDataDTO);
        skipDTO.setCheckpoint(testContent);
        skipDTO.setPayloaddata(testContent);
        skipDTO.setReponse("");
        skipDTO.setErrorInfo(testContent);
        skipDTO.setTest_result("Skip");
        skipDTO.setUrl("https://www.baidu.com");
        //初始化caseReportDTOList
        caseReportDTOList.add(passDTO);
        caseReportDTOList.add(failDTO);
        caseReportDTOList.add(skipDTO);
        ReportService reportService = new ReportService();
        reportService.outputReport(caseReportDTOList, caseReportRelativeDTO);
    }
}

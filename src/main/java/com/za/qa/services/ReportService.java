package com.za.qa.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.velocity.VelocityContext;

import com.za.qa.constants.CaseConf;
import com.za.qa.constants.CaseStatus;
import com.za.qa.dto.CaseReportDTO;
import com.za.qa.dto.CaseReportRelativeDTO;
import com.za.qa.log.LogMan;
import com.za.qa.utils.FileUtils;
import com.za.qa.utils.SimpleDateUtils;
import com.za.qa.utils.VelocityUtils;

/**
 * 类ReportService.java的实现描述：测试报告服务
 * 
 * @author linyun 2016年11月29日 下午5:12:27
 */
public class ReportService {

    /**
     * @param caseReportDTOList reportDTO列表
     * @param caseReportRelativeDTO 测试报告关联数据
     * @return html报告的字符串，可用于发邮件、生成文件等
     */
    public String constructReport(LinkedList<CaseReportDTO> caseReportDTOList,
                                  CaseReportRelativeDTO caseReportRelativeDTO) {
        VelocityContext ctx = new VelocityContext();
        ctx.put("resultList", caseReportDTOList);
        ctx.put("relativeData", caseReportRelativeDTO);
        ctx.put("reportTime", SimpleDateUtils.getCurrentDateTime("yyyy年MM月dd日  HH:mm:ss"));
        return VelocityUtils.getVelocityString(ctx, caseReportRelativeDTO.getReportVmPath());
    }

    /**
     * @param caseReportDTOList reportDTO列表
     * @param CaseReportRelativeDTO 测试报告关联数据
     * @return 返回标志位，00成功，01失败
     */
    public String outputReport(LinkedList<CaseReportDTO> caseReportDTOList, CaseReportRelativeDTO caseReportRelativeDTO) {
        try {
            Properties reportProperties = FileUtils.readProperties(CaseConf.confpath);
            String dir = new String(reportProperties.getProperty("report.dir").getBytes("ISO-8859-1"),"gbk");           
            String outPutDir="";
            for(CaseReportDTO casereportdto:caseReportDTOList){
            	if(casereportdto.getTest_result().equals(CaseStatus.PASS.getCode())){
            		outPutDir = dir + caseReportRelativeDTO.getReportFileName()
                            + SimpleDateUtils.getCurrentDateTime("yyyyMMddHHmmss") + ".html";
            		break;
            	}else{
            		outPutDir = dir + "FailReport"
                            + SimpleDateUtils.getCurrentDateTime("yyyyMMddHHmmss") + ".html";
            	}
            }
            String htmlString = constructReport(caseReportDTOList, caseReportRelativeDTO);
            FileUtils.createFile(outPutDir);
            InputStream is = new ByteArrayInputStream(htmlString.getBytes());
            OutputStream os = new FileOutputStream(new File(outPutDir));
            FileUtils.saveTo(is, os);
            String returnStr = outPutDir.substring(0,outPutDir.length()-5);
            int num = returnStr.lastIndexOf("\\");
            returnStr = returnStr.substring(num+1);
            return returnStr;//成功返回报告文件名
        } catch (Exception e) {
            e.printStackTrace();
            LogMan.getLoger().error(e);
            return "01";//异常返回01
        }
    }
}

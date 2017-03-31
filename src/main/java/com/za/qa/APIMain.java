package com.za.qa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.za.qa.dataprepare.DataConfiguration;
import com.za.qa.dataprepare.DataPrepareOfSqlCheck;
import com.za.qa.dataprepare.DataResource;
import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseDTO;
import com.za.qa.dto.CaseReportDTO;
import com.za.qa.dto.CaseReportRelativeDTO;
import com.za.qa.log.LogMan;
import com.za.qa.services.ApiCallable;
import com.za.qa.services.ReportService;
import com.za.qa.utils.SimpleDateUtils;
import com.za.qa.verify.SqlVerify;


public class APIMain {
	
	public static void main(String args[]) throws Exception {
		Date start = new Date(System.currentTimeMillis());
		String BeginTime= SimpleDateUtils.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		int threadNum =40;
		List<Future> list = new ArrayList<Future>();
		LinkedList <CaseReportDTO> caseReportDTOList = new LinkedList<CaseReportDTO>();
		ExecutorService pool = Executors.newFixedThreadPool(threadNum);
		CaseConfigurationDTO caseConfigurationDTO = new CaseConfigurationDTO();

		//set CaseConfigurationDTO 
        caseConfigurationDTO.setDataConfiguration(DataConfiguration.mapConf());
        caseConfigurationDTO.setDataResource(DataResource.allSuiteMap());
        caseConfigurationDTO.setRenameVariableMapping(DataConfiguration.renameVariableMapping(caseConfigurationDTO.getDataConfiguration()));
        DataPrepareOfSqlCheck.setSqlExpectdata(caseConfigurationDTO);
		
		for(String listpath:caseConfigurationDTO.getDataResource().keySet()){
			if(listpath.indexOf("#")<listpath.length()-1){
				
				Callable <List> apiService =  new ApiCallable(caseConfigurationDTO,listpath);
				Future <List>  f= pool.submit(apiService);
		        list.add(f);
			}
		}
		pool.shutdown();
		for (Future<?> f : list) {
			caseReportDTOList.addAll((LinkedList<CaseReportDTO>) f.get());
		}
		 
		 Date end =new Date(System.currentTimeMillis());
		 String EndTime= SimpleDateUtils.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 long DuringTime = (sf.parse(EndTime).getTime()-sf.parse(BeginTime).getTime())/1000;
		 CaseReportRelativeDTO caseReportRelativeDTO =CaseDTO.getCaseReportRelativeDTO(BeginTime, EndTime, DuringTime, caseReportDTOList);
		 ReportService rs = new ReportService();
		 caseReportDTOList=CaseDTO.orderBySuite(caseReportDTOList);
//		 for(int i=0;i<caseReportDTOList.size();i++){
//			System.out.println(caseReportDTOList.get(i).getOrderNumOfSuite());
//		 }
		 String reportName = rs.outputReport(caseReportDTOList, caseReportRelativeDTO);
		 SqlVerify.insertIntoDataBase(caseReportDTOList, reportName);
		 caseReportDTOList = CaseDTO.showfailonly(caseReportDTOList);
		 if(caseReportDTOList.size()>0){
		 rs.outputReport(caseReportDTOList, caseReportRelativeDTO); 
		 }
		 LogMan.getLoger().info("----程序结束运行----，程序运行时间【"+ (end.getTime() - start.getTime()) + "毫秒】");
    }
}




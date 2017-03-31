package com.za.qa.services; 

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.poi.util.SystemOutLogger;

import com.za.qa.dataprepare.CaseListFlag;
import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseReportDTO;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月29日 下午3:55:23 
 * 类说明 
 */
public class ApiCallable implements Callable <List>{
	private String listpath;
	private CaseConfigurationDTO caseConfigurationDTONew;
	public ApiCallable(CaseConfigurationDTO caseConfigurationDTO,String listpath) { 
	  this.listpath = listpath;
	  this.caseConfigurationDTONew = new CaseConfigurationDTO();
	  caseConfigurationDTONew.setDataConfiguration(caseConfigurationDTO.getDataConfiguration());
	  caseConfigurationDTONew.setDataResource(caseConfigurationDTO.getDataResource());
	  String caseListNo = listpath.split("#")[1];
	  caseConfigurationDTONew.setListPath(listpath);
	  caseConfigurationDTONew.setListName(caseListNo);
      String flag=CaseListFlag.getList_Flag(caseConfigurationDTONew);
      caseConfigurationDTONew.setEnv(flag);
      caseConfigurationDTONew.setListFlag(flag);
      caseConfigurationDTONew.setRenameVariableMapping(caseConfigurationDTO.getRenameVariableMapping());
	} 
	public List<CaseReportDTO>  call()throws Exception{
		  Date dateTmp1 = new Date();
		  ClientServices cs = new ClientServices();
		  LinkedList<CaseReportDTO>list=cs.run(caseConfigurationDTONew);
		  Date dateTmp2 = new Date(); 
		  long time = dateTmp2.getTime() - dateTmp1.getTime(); 
		  return  list;
	}
	

}
 
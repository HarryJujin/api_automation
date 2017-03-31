package com.za.qa.dto; 

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;











import java.util.Properties;

import com.za.qa.analyze.DataAnalyze;
import com.za.qa.constants.CaseConf;
import com.za.qa.constants.CaseStatus;
import com.za.qa.dataprepare.CaseIpVersion;
import com.za.qa.dataprepare.CaseKey;
import com.za.qa.dataprepare.CaseListFlag;
import com.za.qa.dataprepare.CaseParameter;
import com.za.qa.dataprepare.CaseTemplate;
import com.za.qa.keywords.ExpressionRegister;
import com.za.qa.log.LogMan;
import com.za.qa.processor.Postprocessor;
import com.za.qa.processor.Proprocessor;
import com.za.qa.services.ClientServices;
import com.za.qa.utils.FileUtils;
import com.za.qa.verify.DataVerify;
import com.za.qa.verify.ResponseVerify;
import com.za.qa.verify.SqlVerify;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年12月6日 下午3:52:08 
 * 类说明 
 */
public class CaseDTO {


	/**
	 * @author jujinxin
	 *
	 */
  
    public static CaseDataDTO getCaseDataDTO(CaseConfigurationDTO caseConfigurationDTO,CaseSuiteDTO casesuiteDTO){
    	 CaseDataDTO casedataDTO = new CaseDataDTO();
    	 casedataDTO.setListflag(CaseListFlag.getList_Flag(caseConfigurationDTO));
    	 
  		 Map<String,String> mapCaseKey=new HashMap <String,String>();
  		 mapCaseKey=CaseKey.getAppPrivate_Key(caseConfigurationDTO);
  		 casedataDTO.setAppkey(mapCaseKey.get("AppKey"));
  		 casedataDTO.setDevprivatekey(mapCaseKey.get("DevPrivateKey"));
  		 
  		 Map<String,String> mapIP_Version=new HashMap <String,String>();
  		 mapIP_Version=CaseIpVersion.getIP_Version(caseConfigurationDTO,casesuiteDTO);
  	 	 casedataDTO.setIp(mapIP_Version.get("IP"));
  	 	 casedataDTO.setVersion(mapIP_Version.get("VERSION"));
  	 	 

  	 	 
  	 	 Map<String,String> mapParameter=new HashMap <String,String>();
  	 	 mapParameter= CaseParameter.getParameter(caseConfigurationDTO,casesuiteDTO);
  	 	 casedataDTO.setProProcessor(mapParameter.get("ProProcessor"));
  	 	 casedataDTO.setCaseListNo(mapParameter.get("CASElist_NO"));
  	 	 casedataDTO.setCaseListDesc(mapParameter.get("CASElist_Desc"));
  	 	 casedataDTO.setCaseNo(mapParameter.get("CASE_NO"));
  	 	 casedataDTO.setCaseDesc(mapParameter.get("CASE_Desc"));
  	 	 casedataDTO.setApiNo(mapParameter.get("API_NO"));
  	 	 casedataDTO.setCheckPoint(mapParameter.get("CHECKPOINT"));
  	 	 casedataDTO.setRunFlag(mapParameter.get("RunFLAG"));
  	 	 casedataDTO.setRecordFlag(mapParameter.get("RecFLAG"));
  	 	 casedataDTO.setPostProcessor(mapParameter.get("PostProcessor"));
  	 	 
  	 	 Map<String,String> mapCaseTemplate=new HashMap <String,String>();
  	 	 mapCaseTemplate=CaseTemplate.getTemplateData(caseConfigurationDTO,casesuiteDTO);
  	 	 casedataDTO.setType(mapCaseTemplate.get("TYPE"));
  	 	 casedataDTO.setData(mapCaseTemplate.get("DATA"));
  	 	 casedataDTO.setServiceName(mapCaseTemplate.get("SERVICE_NAME"));
  	 	 casedataDTO.setResourcePath(mapCaseTemplate.get("ResourcePath"));
  	 	 casedataDTO.setRequestDto(mapCaseTemplate.get("REQUEST_DTO"));
  	 	 casedataDTO.setCharset(mapCaseTemplate.get("charset"));
  	 	 
  	 	 return casedataDTO;
    }
    
    public static CaseSuiteDTO getCaseSuiteDTO(CaseConfigurationDTO caseConfigurationDTO,int i){
    	CaseSuiteDTO casesuiteDTO = new CaseSuiteDTO();
    	String listpath = caseConfigurationDTO.getListPath();
    	casesuiteDTO.setApiNo(caseConfigurationDTO.getDataResource().get(listpath).get("API_NO")[i]);
    	casesuiteDTO.setI(i);
    	return casesuiteDTO;
    }
    
    //解析后DTO
    public static CaseBeforeRunDTO getCaseBeforeRunDTO(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO,CaseSuiteDTO casesuiteDTO) throws Exception{
    	CaseBeforeRunDTO casebeforeDTO = new CaseBeforeRunDTO();
    	Proprocessor.doProprocessor(casedataDTO,  caseConfigurationDTO);
    	String request =DataAnalyze.getAnalyzeData(casedataDTO, caseConfigurationDTO,casesuiteDTO);
    	String checkpoint= casedataDTO.getCheckPoint();
    	String ListPath=caseConfigurationDTO.getListPath();
    	String orderNumOfSuite="";
    	try {
			String[] orderNumOfSuiteArry=caseConfigurationDTO.getDataResource().get(ListPath).get("orderNumOfSuite");
			orderNumOfSuite=orderNumOfSuiteArry[0];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			orderNumOfSuite="0";
			e.printStackTrace();
		}
    	casebeforeDTO.setRequest(request);
    	casebeforeDTO.setCheckpoint(checkpoint);
    	casebeforeDTO.setOrderNumOfSuite(orderNumOfSuite);
    	return casebeforeDTO;
    }
    //run client
    public static CaseAfterRunDTO getCaseAfterRunDTO(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO,CaseBeforeRunDTO casebeforeDTO,CaseSuiteDTO casesuiteDTO) throws Exception{
    	CaseAfterRunDTO caseafterrunDTO = new CaseAfterRunDTO();
    	String response="";
		try {
			response = ClientServices.runClient(casedataDTO, caseConfigurationDTO, casebeforeDTO, casesuiteDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response="接口请求异常";
		}
    	caseafterrunDTO.setReponse(response);
    	Postprocessor.doPostprocessor(casedataDTO, caseConfigurationDTO);
    	return caseafterrunDTO;
    }
    
    public static CaseReportDTO getCaseReportDTO(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO,CaseAfterRunDTO caseafterrunDTO,CaseBeforeRunDTO casebeforerunDTO,CaseSuiteDTO casesuiteDTO) throws Exception{
    	CaseReportDTO caseReportDTO = new CaseReportDTO();    	
    		caseReportDTO.setCaseDataDTO(casedataDTO);
        	caseReportDTO.setCheckpoint(casebeforerunDTO.getCheckpoint());	
        	caseReportDTO.setOrderNumOfSuite(casebeforerunDTO.getOrderNumOfSuite());
    		String Payloaddata=casebeforerunDTO.getRequest();
    		//入参解析失败set report
    		if(DataVerify.verifyPayload(Payloaddata)){			
    			caseReportDTO.setPayloaddata(casebeforerunDTO.getRequest()); //重点关注入参中哪个字段解析失败
    			caseReportDTO.setReponse(caseafterrunDTO.getResponse());     //response应为空
    			caseReportDTO.setErrorInfo(" ");
            	caseReportDTO.setTest_result(CaseStatus.SKIP.getCode());     //只要参数未解析成功，接口结果为SKIP 
    		}else{
    			String Response=caseafterrunDTO.getResponse();
    			String result="";
    			String resultMessage="";
				try {
					CheckResultDTO checkResult=new CheckResultDTO();
					checkResult = ResponseVerify.check(casedataDTO,caseConfigurationDTO,caseafterrunDTO);
					result = checkResult.resultCode;
					resultMessage = checkResult.resultMessage;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Response =Response+";结果校验异常";
					e.printStackTrace();
				}
				//增加sql查询校验
				try {
					String sqlResult = SqlVerify.Sqlcheck(casedataDTO, caseConfigurationDTO, caseafterrunDTO);
					if (!CaseStatus.PASS.getCode().equals(sqlResult)){
						result = result +";"+"\n"+sqlResult;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					result=result+";Sql校验异常";
					e.printStackTrace();
				}
    			//判断是否是xml报文进行请求和返回字符串转义
    			if(Payloaddata!=null&&Payloaddata.length()>1){
    				if(isXmlStr(Payloaddata)){
    					Payloaddata=Payloaddata.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
    				}
    			}
    			if(Response!=null&&Payloaddata.length()>1){
    				if(isXmlStr(Response)){
    					Response=Response.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
    				}
    			}
    			//测试
    			//Response=Response+ExpressionRegister.getSqlSelectEnv().get(caseConfigurationDTO.getListPath()+"#"+casedataDTO.getCaseNo());
    			caseReportDTO.setPayloaddata(Payloaddata);
    			caseReportDTO.setReponse(Response);
    	    	
    			caseReportDTO.setUrl(casedataDTO.getIp()+casedataDTO.getResourcePath());
    	    	if(CaseStatus.PASS.getCode().equalsIgnoreCase(result)){
    	    		caseReportDTO.setTest_result(result);   //验证成功，接口结果为Pass
    	    		caseReportDTO.setErrorInfo(resultMessage);
    	    	}else{
    	    		caseReportDTO.setTest_result(CaseStatus.FAIL.getCode());  //验证失败，接口结果为Fail
    	    		caseReportDTO.setErrorInfo(resultMessage);
    	    	}
    		}
    	return caseReportDTO;
    }
    

    
    public static CaseReportRelativeDTO getCaseReportRelativeDTO(String BeginTime,String EndTime,long DuringTime,LinkedList <CaseReportDTO> caseReportDTOList){
    	CaseReportRelativeDTO caseReportRelativeDTO = new CaseReportRelativeDTO();

    	caseReportRelativeDTO.setReportFileName("众安接口测试报告");
        caseReportRelativeDTO.setBeginTime(BeginTime);
        caseReportRelativeDTO.setEndTime(EndTime);
        caseReportRelativeDTO.setDuringTime(DuringTime+"秒");
        caseReportRelativeDTO.setPassedPercent(PassedPercent(caseReportDTOList));
        caseReportRelativeDTO.setSkippedPercent(SkippedPercent(caseReportDTOList));
        caseReportRelativeDTO.setFailedPercent(FailedPercent(caseReportDTOList));
        caseReportRelativeDTO.setCaseNum(CaseNum(caseReportDTOList)+"");
        caseReportRelativeDTO.setFailedNum(FailedNum(caseReportDTOList)+"");
        caseReportRelativeDTO.setSkippedNum(SkippedNum(caseReportDTOList)+"");
        caseReportRelativeDTO.setPassedNum(PassedNum(caseReportDTOList)+"");
    	return caseReportRelativeDTO;
    }
    
 
    
    public static int PassedNum(LinkedList <CaseReportDTO> caseReportDTOList){
    	int num=0;
    	for(int i=0;i<caseReportDTOList.size();i++){
    		if(caseReportDTOList.get(i).getTest_result().equalsIgnoreCase(CaseStatus.PASS.getCode())){
    			num++;
    		}
    	}
    	return num;
    }
    public static int FailedNum(LinkedList <CaseReportDTO> caseReportDTOList){
    	int num=0;
    	for(int i=0;i<caseReportDTOList.size();i++){
    		if(caseReportDTOList.get(i).getTest_result().equalsIgnoreCase(CaseStatus.FAIL.getCode())){
    			num++;
    		}
    	}
    	return num;
    }
    public static int SkippedNum(LinkedList <CaseReportDTO> caseReportDTOList){
    	int num=0;
    	for(int i=0;i<caseReportDTOList.size();i++){
    		if(caseReportDTOList.get(i).getTest_result().equalsIgnoreCase(CaseStatus.SKIP.getCode())){
    			num++;
    		}
    	}
    	return num;
    }
    
    public static int CaseNum(LinkedList <CaseReportDTO> caseReportDTOList){
    	int num=0;
    	for(int i=0;i<caseReportDTOList.size();i++){
    		if(!caseReportDTOList.get(i).getTest_result().equalsIgnoreCase(CaseStatus.NOTRUN.getCode())){
    			num++;
    		}
    	}
    	return num;
    }
    
    public static String PassedPercent(LinkedList <CaseReportDTO> caseReportDTOList){
    	NumberFormat numberFormat = NumberFormat.getInstance();
    	numberFormat.setMaximumFractionDigits(2);
    	String result =numberFormat.format((float)PassedNum(caseReportDTOList)/(float)CaseNum(caseReportDTOList)*100);
    	String percent = result+"%";
    	return percent;
    }
    
    public static String FailedPercent(LinkedList <CaseReportDTO> caseReportDTOList){
    	NumberFormat numberFormat = NumberFormat.getInstance();
    	numberFormat.setMaximumFractionDigits(2);
    	String result =numberFormat.format((float)FailedNum(caseReportDTOList)/(float)CaseNum(caseReportDTOList)*100);
    	String percent = result+"%";
    	return percent;
    }
    
    public static String SkippedPercent(LinkedList <CaseReportDTO> caseReportDTOList){
    	NumberFormat numberFormat = NumberFormat.getInstance();
    	numberFormat.setMaximumFractionDigits(1);
    	String result =numberFormat.format((float)SkippedNum(caseReportDTOList)/(float)CaseNum(caseReportDTOList)*100);
    	String percent = result+"%";
    	return percent;
    }
    public static boolean isXmlStr(String dataStr){
    	String Response = dataStr.trim().replace("\\", "").replace("\"", "").replace(" ", "");
    	String firstChar = Response.substring(0, 1);
    	String lastChar = Response.substring(Response.length() - 1,
    			Response.length());
    	// 处理xml字符串
    	if (firstChar.equals("<") && lastChar.equals(">")) {
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public static LinkedList <CaseReportDTO> showfailonly(LinkedList <CaseReportDTO> caseReportDTOList){
    	LinkedList <CaseReportDTO> failreportlist = new LinkedList <CaseReportDTO>();
    	for(CaseReportDTO casereportdto:caseReportDTOList){
    		if(casereportdto.getTest_result()!=CaseStatus.PASS.getCode()&&casereportdto.getTest_result()!=CaseStatus.NOTRUN.getCode())
    			failreportlist.add(casereportdto);
    	}
        try {
			Properties reportProperties = FileUtils.readProperties(CaseConf.confpath);
			String dir = new String(reportProperties.getProperty("report.dir").getBytes("ISO-8859-1"),"gbk");           
			String outPutDir="";
			outPutDir = dir + "TestResult.txt";
			File f = new File(outPutDir);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"GBK")));
			if (failreportlist.size()>0){
		    	out.write("失败");
			}else{
		    	out.write("通过");
			}
			out.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return failreportlist;
    }
    public static LinkedList <CaseReportDTO> orderBySuite(LinkedList <CaseReportDTO> caseReportDTOList){
    	
    	CaseReporComparator caseReporComparator=new CaseReporComparator();
    	//System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    	Collections.sort(caseReportDTOList,caseReporComparator);
    	return caseReportDTOList;
    }
  
}

class CaseReporComparator implements Comparator{ 
	public int compare(Object o1, Object o2) { 
	  int flag=0;
	  if(null!=o1&&null!=o2) 
	  { 
		  CaseReportDTO caseReportDTO1=(CaseReportDTO)o1;
		  CaseReportDTO caseReportDTO2=(CaseReportDTO)o2;
		  String Str1= caseReportDTO1.getOrderNumOfSuite(); 
		  String Str2= caseReportDTO2.getOrderNumOfSuite(); 
		  flag=Integer.parseInt(Str1)-Integer.parseInt(Str2);
	  }
	  return flag;
	  
	 }
}  


package com.za.qa.services; 

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.alibaba.fastjson.JSONObject;
import com.za.qa.constants.CaseStatus;
import com.za.qa.constants.CaseType;
import com.za.qa.constants.EnvType;
import com.za.qa.dataprepare.CaseListFlag;
import com.za.qa.dto.CaseAfterRunDTO;
import com.za.qa.dto.CaseBeforeRunDTO;
import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseDTO;
import com.za.qa.dto.CaseDataDTO;
import com.za.qa.dto.CaseReportDTO;
import com.za.qa.dto.CaseSuiteDTO;
import com.za.qa.http.HttpClientA;
import com.za.qa.keywords.ExpressionRegister;
import com.za.qa.log.LogMan;
import com.za.qa.utils.ClientUtils;
import com.za.qa.verify.DataVerify;
import com.zhongan.qa.utils.HttpClientUtils;
import com.zhongan.qa.utils.HttpResponseUtils;
import com.zhongan.scorpoin.biz.common.CommonRequest;
import com.zhongan.scorpoin.biz.common.CommonResponse;
import com.zhongan.scorpoin.common.ZhongAnApiClient;

/** 
 * @author jujinxin 
 * @version 创建时间：2016年11月14日 下午3:02:47 
 * 类说明 
 */
public class ClientServices {
	
	
	public LinkedList<CaseReportDTO> run(CaseConfigurationDTO caseConfigurationDTO) throws Exception{
		LinkedList<CaseReportDTO> caseReportDTOList = new LinkedList<CaseReportDTO>();
	     //String Listflag = CaseListFlag.getList_Flag(caseConfigurationDTO);
	     String Listflag = caseConfigurationDTO.getListFlag();
	     String listpath = caseConfigurationDTO.getListPath();
	     //String caseListNo = listpath.split("#")[1];
     	 String[] s =caseConfigurationDTO.getDataResource().get(listpath).get("CASE_NO");

	        //判断用例集开关
	        if(Listflag.equalsIgnoreCase(EnvType.uat.toString())||Listflag.equalsIgnoreCase(EnvType.iTest.toString())){
	        	for(int i=3;i<s.length;i++){	
	        		//set DTO
	        		CaseSuiteDTO casesuiteDTO = CaseDTO.getCaseSuiteDTO(caseConfigurationDTO, i);
	        		CaseDataDTO casedataDTO = CaseDTO.getCaseDataDTO(caseConfigurationDTO, casesuiteDTO);

	        		if(casedataDTO.getRunFlag().equalsIgnoreCase("YES")){
	        			ClientUtils.caseStartLog(casedataDTO);
		        		CaseBeforeRunDTO casebeforerunDTO = CaseDTO.getCaseBeforeRunDTO(casedataDTO, caseConfigurationDTO, casesuiteDTO);
		        		CaseAfterRunDTO caseafterrunDTO = CaseDTO.getCaseAfterRunDTO(casedataDTO, caseConfigurationDTO, casebeforerunDTO, casesuiteDTO);	        	
		        		CaseReportDTO caseReportDTO = CaseDTO.getCaseReportDTO(casedataDTO,caseConfigurationDTO,caseafterrunDTO, casebeforerunDTO,casesuiteDTO); 
			        	caseReportDTOList.add(caseReportDTO);
			        	ClientUtils.caseEndLog(casedataDTO);
	        		}
//	        		else{
//	        			CaseReportDTO caseReportDTO = new CaseReportDTO();
//	        			caseReportDTO.setOrderNumOfSuite("0");
//			        	caseReportDTO.setTest_result(CaseStatus.NOTRUN.getCode());
//			        	caseReportDTOList.add(caseReportDTO);
//	        			LogMan.getLoger().info(String.format("当前用例:%s开关关闭,忽略,开始执行下一条",casedataDTO.getCaseNo()));
//	        		}
	        	}
	        }
//	        else{
//	        	for(int i=3;i<s.length;i++){
//	        		CaseReportDTO caseReportDTO = new CaseReportDTO();
//	        		caseReportDTO.setOrderNumOfSuite("0");
//		        	caseReportDTO.setTest_result(CaseStatus.NOTRUN.getCode());
//		        	caseReportDTOList.add(caseReportDTO);
//	        	}
//	        	LogMan.getLoger().info(String.format("当前用例:%s用例集开关关闭,忽略,开始执行下一用例集",caseListNo));
//	        	
//	        }
	        return caseReportDTOList;
	}
	
	
	public static void saveDataToMap(String key,String bizcontent,String response ){
		ExpressionRegister.setPayloadEnv(key, bizcontent);
		ExpressionRegister.setResponseEnv(key, response);
	}
	
	public static String runClient(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO, CaseBeforeRunDTO caserunningdatDTO,CaseSuiteDTO casesuiteDTO) throws Exception{
		String response="";
			
				if(casedataDTO.getType().equalsIgnoreCase(CaseType.OpenAPI.toString())){
					response = runSDKclient(casedataDTO,caseConfigurationDTO,caserunningdatDTO,casesuiteDTO);
				}else if((casedataDTO.getType().equalsIgnoreCase(CaseType.HSF.toString()))){
					response = runHSFclient(casedataDTO,caseConfigurationDTO,caserunningdatDTO,casesuiteDTO);
				}else if((casedataDTO.getType().equalsIgnoreCase(CaseType.HTTP.toString()))){
					response = runHttpclient(casedataDTO,caseConfigurationDTO,caserunningdatDTO,casesuiteDTO);
				}else{
					LogMan.getLoger().info(String.format("当前用例:%s接口类型异常,忽略,开始执行下一条",casedataDTO.getType()));
				}
			return response;
	}
	
	public static String runSDKclient(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO,CaseBeforeRunDTO casebeforerunDTO,CaseSuiteDTO casesuiteDTO) throws Exception{
		String response_bizcontent = "";
		String bizcontent = casebeforerunDTO.getRequest();
		if(DataVerify.verifyPayload(bizcontent)){
			LogMan.getLoger().info(String.format("当前用例:%s入参解析失败,忽略,开始执行下一条",bizcontent));
			
		}else{
			JSONObject reqJson = ClientUtils.Str2JsonObject(bizcontent);
			ZhongAnApiClient client = new ZhongAnApiClient(casedataDTO.getListflag(), casedataDTO.getAppkey(), casedataDTO.getDevprivatekey(),casedataDTO.getVersion());
			CommonRequest request = new CommonRequest(casedataDTO.getServiceName());
			request.setParams(reqJson);
			try {
				CommonResponse response = new CommonResponse();
				try {
					response = (CommonResponse) client.call(request,casedataDTO.getIp());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(response.getBizContent() == null){
					JSONObject responseJson =new JSONObject();
					responseJson.put("errorCode", response.getErrorCode());
					responseJson.put("errorMsg", response.getErrorMsg());
					responseJson.put("charset", response.getCharset());
					responseJson.put("format", response.getFormat());
					responseJson.put("signType", response.getSignType());
					responseJson.put("timestamp", response.getTimestamp());
					response_bizcontent = responseJson.toJSONString();
				}else{
					response_bizcontent = response.getBizContent();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				response_bizcontent ="";
				e.printStackTrace();
			}
			saveDataToMap(caseConfigurationDTO.getListPath()+"#"+casedataDTO.getCaseNo(),reqJson.toJSONString(),response_bizcontent);
			LogMan.getLoger().info("bizcontent: " + response_bizcontent);
		}
		return response_bizcontent;
	}
	
	public static String runHSFclient(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO,CaseBeforeRunDTO casebeforerunDTO,CaseSuiteDTO casesuiteDTO) throws Exception{
		String response = "";
		String ArgsObjects = casebeforerunDTO.getRequest();
		if(DataVerify.verifyPayload(ArgsObjects)){
			LogMan.getLoger().info(String.format("当前用例:%s入参解析失败,忽略,开始执行下一条",ArgsObjects));			
		}else{
			String ArgsTypes = casedataDTO.getRequestDto();
			String url = "http://" + casedataDTO.getIp() + casedataDTO.getResourcePath();
			LogMan.getLoger().info("接口访问地址: " + url);
			 try {
					CloseableHttpResponse closehttp = HttpClientUtils.testHSF(null, null, url, "POST", ArgsTypes, ArgsObjects);
					 response = HttpResponseUtils.getResponseContent(closehttp);
				//response = HttpClientA.hsfHttpPost(url, ArgsTypes, ArgsObjects);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				response = "";
				e.printStackTrace();
			}		
			saveDataToMap(caseConfigurationDTO.getListPath()+"#"+casedataDTO.getCaseNo(),ArgsObjects,response);
			LogMan.getLoger().info("bizcontent: " + response);
		}
		return response;
	}
	
	public static String runHttpclient(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO,CaseBeforeRunDTO casebeforerunDTO,CaseSuiteDTO casesuiteDTO) throws Exception{
		String response = "";
		String bizcontent = casebeforerunDTO.getRequest();
		String url = "http://" + casedataDTO.getIp() + casedataDTO.getResourcePath();
		String charset="utf-8";
		if(casedataDTO.getCharset()!=null&&casedataDTO.getCharset().length()>0){
			charset=casedataDTO.getCharset();
		}
		//bizconten为空
		if(bizcontent.isEmpty()){
			/*CloseableHttpResponse closehttp = HttpClientUtils.testWithKeyValue(null, null, url, "GET", null);
			response = HttpResponseUtils.getResponseContent(closehttp);*/
			response=HttpClientA.postForm(url, bizcontent, charset);
		}
		//bizconten非空
		else{
			String verifyBizcontent = bizcontent.replace(" ", "").replace("\n", "").replace("\\", "").replace("\"", "");
			String lastChar =verifyBizcontent.substring(verifyBizcontent.length()-1);
			String firstChar =verifyBizcontent.substring(0,1);
			if(DataVerify.verifyPayload(bizcontent)){
				LogMan.getLoger().info(String.format("当前用例:%s入参解析失败,忽略,开始执行下一条",bizcontent));
			}else{
				LogMan.getLoger().info("接口访问地址: " + url);
				if(lastChar.equals(">")){
					try {
						Map<String, String> headerMap = new HashMap<String, String> ();
						headerMap.put("Content-Type","text/xml; charset="+charset);
						//response=HttpClientA.postXml(url,bizcontent,charset);
						CloseableHttpResponse closehttp = HttpClientUtils.testWithXml(headerMap, null, url, "POST", bizcontent);
						response = HttpResponseUtils.getResponseContent(closehttp);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						response = "";
						e.printStackTrace();
					}
					if(response!=null){
						if(response.indexOf("&lt;")>-1&response.indexOf("&gt;")>-1){
							response=response.replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&quot;","\"");
						}
					}
				}
				else if((verifyBizcontent.contains("|{")&&lastChar.equals("}"))||firstChar.equals("{")){
					try {
						/*Map<String, String> headerMap = new HashMap<String, String> ();
						headerMap.put("Content-Type", "application/json");
						CloseableHttpResponse closehttp =HttpClientUtils.testWithJson(headerMap, null, url, "POST", bizcontent);
						response = HttpResponseUtils.getResponseContent(closehttp);*/
						response=HttpClientA.postJson(url, bizcontent);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						response = "";
						e.printStackTrace();
					}
				}else{
					/*Map<String, String> headerMap = new HashMap<String, String> ();
					Map<String, String> parameterMap = new HashMap<String, String>();
					headerMap = ClientUtils.headerMap(bizcontent);
					parameterMap = ClientUtils.bodyMap(bizcontent);
					CloseableHttpResponse closehttp = HttpClientUtils.testWithKeyValue(headerMap, null, url, "POST", parameterMap);
					response = HttpResponseUtils.getResponseContent(closehttp);*/
					response=HttpClientA.postForm(url, bizcontent, charset);
				}
				saveDataToMap(caseConfigurationDTO.getListPath()+"#"+casedataDTO.getCaseNo(),bizcontent,response);
				LogMan.getLoger().info("bizcontent: " + response);
			}
		}
		
		return response;
	}
}
 
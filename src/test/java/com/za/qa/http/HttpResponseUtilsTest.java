package com.za.qa.http; 

import static org.junit.Assert.*;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

/** 
 * @author jujinxin 
 * @version 创建时间：2017年3月17日 下午5:36:23 
 * 类说明 
 */
public class HttpResponseUtilsTest {

	@Test
	public void test() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String url = "http://10.139.34.119:8086/com.zhongan.core.user.service.CusCompanyService:1.0.0/findListByCondition";
		String method = "POST";
		String ArgTypes="com.zhongan.core.user.dto.CusUserCompanyDTO";
		String ArgsObjects="[{\"userId\":4165025}]";
		CloseableHttpResponse response = HttpResponseUtils.getHSFResponse(httpclient, url, method, null, ArgTypes, ArgsObjects);
		String result = HttpResponseUtils.getResponseContent(response);
		System.out.println(result);
	}

}
 
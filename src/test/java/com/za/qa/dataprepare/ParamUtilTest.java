package com.za.qa.dataprepare;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

public class ParamUtilTest {

	@Test
	public void testGetRun_Flag() throws Exception, InterruptedException, ExecutionException, Exception {
         String path="D:\\workspace\\TestCase\\健康险_康盛_团体意外伤害_金鑫V1.xlsx#支付宝乐牙网儿童齿科";
		String flag = ParamUtil.getRun_Flag(DataConfiguration.mapConf(), path,"支付宝用例集1");
		System.out.println("【Flag】: "+flag);
	}

	@Test
	public void testgetIP_Version() throws IOException, InterruptedException, ExecutionException, Exception {
		String path="D:\\workspace\\TestCase\\健康险_康盛_团体意外伤害_金鑫V1.xlsx#支付宝乐牙网儿童齿科";
		Map<String, String> ip_version = ParamUtil.getIP_Version(DataConfiguration.mapConf(), path, "支付宝核保", "uat");
		System.out.println("【IP,VERSION】: "+ip_version.get("IP")+"----"+ip_version.get("VERSION"));
	}

	@Test
	public void testgetAppPrivate_Key() throws IOException, InterruptedException, ExecutionException {
		String path="D:\\workspace\\TestCase\\健康险_康盛_团体意外伤害_金鑫V1.xlsx#支付宝乐牙网儿童齿科";
		Map<String, String> app_privatekey = ParamUtil.getAppPrivate_Key(DataConfiguration.mapConf(), path, "itest");
		System.out.println("【appkey】"+app_privatekey.get("AppKey")+"【privatekey】"+app_privatekey.get("DevPrivateKey"));
	}

	@Test
	public void testGetTemplateData() throws IOException, InterruptedException, ExecutionException {
		String path="D:\\workspace\\TestCase\\健康险_康盛_团体意外伤害_金鑫V1.xlsx#支付宝乐牙网儿童齿科";
		Map<String, String> template_data =ParamUtil.getTemplateData(DataConfiguration.mapConf(), path, "支付宝核保");
		System.out.println("type:"+template_data.get("TYPE")+"\n"
				+"data:"+template_data.get("DATA")+"\n"
				+"serviceName:"+template_data.get("SERVICE_NAME")+"\n"
				+"resourcePath:"+template_data.get("ResourcePath")+"\n"
				+"requestDto:"+template_data.get("REQUEST_DTO")+"\n"
				+"checkpoint:"+template_data.get("CHECKPOINT"));
	}
	/*
	@Test
	public void testGetDevPrivateKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTypeMapOfStringMapOfStringStringMapOfStringMapOfStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDataMapOfStringMapOfStringStringMapOfStringMapOfStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetServiceNameMapOfStringMapOfStringStringMapOfStringMapOfStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetResourcePathMapOfStringMapOfStringStringMapOfStringMapOfStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRequestDtoMapOfStringMapOfStringStringMapOfStringMapOfStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCheckpointMapOfStringMapOfStringStringMapOfStringMapOfStringString() {
		fail("Not yet implemented");
	}*/

}

package com.za.qa.dataprepare;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

public class DataConfigurationTest {

	@Test
	public void testMapConf() throws IOException, InterruptedException, ExecutionException {
		DataConfiguration dataconf = new DataConfiguration();
		System.out.println(dataconf.mapConf().toString());
	}

/*	@Test
	public void testMapFlag() throws IOException, InterruptedException, ExecutionException {
		DataConfiguration dataconf = new DataConfiguration();
		System.out.println(dataconf.mapFlag().toString());
	}*/

	/*@Test
	public void testMapConfigureUp() throws IOException, InterruptedException, ExecutionException {
		DataConfiguration dataconf = new DataConfiguration();
		System.out.println(dataconf.mapConfigureUp());
	}

	@Test
	public void testMapConfigureDown() throws IOException, InterruptedException, ExecutionException {
		DataConfiguration dataconf = new DataConfiguration();
		System.out.println(dataconf.mapConfigureDown());
	}

	@Test
	public void testMapTemplate() throws IOException, InterruptedException, ExecutionException {
		DataConfiguration dataconf = new DataConfiguration();
		System.out.println(dataconf.mapTemplate());
	}*/

}

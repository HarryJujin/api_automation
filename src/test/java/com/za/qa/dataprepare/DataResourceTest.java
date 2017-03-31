package com.za.qa.dataprepare;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.junit.Test;

import com.za.qa.utils.Utilities;
import com.za.qa.utils.XLSUtils;

public class DataResourceTest {

	@Test
	public void TestSuiteOfSheet() throws Exception {
		DataResource datare = new DataResource();
		datare.TestSuiteOfSheet();
	}

}

package com.za.qa.keywords;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.chainsaw.Main;

/**
 * Created by Administrator on 2016/7/19.
 */
public abstract class VerifyKeyWords {
	
    public static final String isNotNull = "@!null";
    public static final String isContains = "@Contains";
    public static final String containsRegExp = "@Contains\\(([^\\)]*)";
  
}

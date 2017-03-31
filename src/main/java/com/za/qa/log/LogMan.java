package com.za.qa.log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by Administrator on 2016/7/14.
 * Add PropertyConfiguration on 2016/9/13 by jujinxin
 */
public class LogMan {
	static {
		Properties pro = new Properties();
		pro.put("log4j.rootLogger", "INFO,Console,log"); //输出INFO级别的即可

		// 输出到控制台
		pro.put("log4j.appender.Console", "org.apache.log4j.ConsoleAppender");
		pro.put("log4j.appender.Console.Target", "System.out");
		pro.put("log4j.appender.Console.layout",
				"org.apache.log4j.PatternLayout");
		pro.put("log4j.appender.Console.layout.ConversionPattern",
				"%-d{yyyy-MM-dd HH:mm:ss}  " + "[File: \"%F\" Method: \"%M\" " + "Line: "+ "%L]" + " - [%p]  " + "%m%n");  //小改动
		//"%-d{yyyy-MM-dd HH:mm:ss}  " + "[File: \"%F\" Method: \"%M\" " + "Line:"+ "\"%L\" ]" + " - [%p]  " + "%m%n"); //原版显示
		// 输出到日志文件
		pro.put("log4j.appender.log",
				"org.apache.log4j.DailyRollingFileAppender");
		pro.put("log4j.appender.log.File", "D:/workspace/za-api-automation/logs/log_"+time()+".log");
		//pro.put("log4j.appender.log.DatePattern", "yyyy-MM-dd-HH-mm-ss'.log'");
		pro.put("log4j.appender.log.Append", "true");
		pro.put("log4j.appender.log.Threshold", "DEBUG");
		pro.put("log4j.appender.log.layout", "org.apache.log4j.PatternLayout");
		pro.put("log4j.appender.log.layout.ConversionPattern",
				"%-d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n");
		PropertyConfigurator.configure(pro);
	}
    private static Logger log = null;
    public static Logger getLoger() {
        if(log == null) {
            log = Logger.getLogger(LogMan.class);
        }
        return log;
    }
    
    public static String time()
	{
	Date date=new Date();
	DateFormat format=new SimpleDateFormat("yyyy_MM_dd_HH_mm_sss");
	String time=format.format(date);
	return time;
	} 
}

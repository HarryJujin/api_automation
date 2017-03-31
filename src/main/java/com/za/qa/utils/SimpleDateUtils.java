package com.za.qa.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SimpleDateUtils {

    public static String getCurrentDateTime(String dateFormat) {
        SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
        return sf.format(new Date());
    }
    
    /**  
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日"). 
     * @param date String 想要格式化的日期 
     * @param oldPattern String 想要格式化的日期的现有格式 
     * @param newPattern String 想要格式化成什么格式 
     * @return String  
     */   
    public static String StringPattern(String date,int age) {   
    	GregorianCalendar gc =new GregorianCalendar();
    	SimpleDateFormat sdf1=null;
        if (date == null)   
            return ""; 
        if(date.matches("[0-9]*")&&date.length()==8){
        	sdf1 = new SimpleDateFormat("yyyyMMdd");
        }else if(date.matches("[0-9\\s]*")&&date.length()==10){
        	sdf1 = new SimpleDateFormat("yyyy MM dd");
        }else if(date.matches("[0-9-]*")&&date.length()==10){
        	sdf1 = new SimpleDateFormat("yyyy-MM-dd") ;
        }else if(date.matches("[0-9.]*")&&date.length()==10){
        	sdf1 = new SimpleDateFormat("yyyy.MM.dd") ;
        }else if(date.matches("[0-9//]*")&&date.length()==10){
        	sdf1 = new SimpleDateFormat("yyyy/MM/dd") ;
        }else if (date.matches("[0-9\\\\]*")&&date.length()==10){
        	sdf1 = new SimpleDateFormat("yyyy\\MM\\dd") ;
        }else if(date.matches("[0-9\u4e00-\u9fa5]*")){
        	sdf1 = new SimpleDateFormat("yyyy年MM月dd日") ;
        }else if(date.matches("[0-9_]*")&&date.length()==10){
        	sdf1 = new SimpleDateFormat("yyyy_MM_dd") ;
        }else{
        	return ""; 
        }
        //SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern) ;        // 实例化模板对象    
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd") ;        // 实例化模板对象    
        Date d = null ;    
        try{    
            d = sdf1.parse(date) ;   // 将给定的字符串中的日期提取出来    
        }catch(Exception e){            // 如果提供的字符串格式有错误，则进行异常处理    
            e.printStackTrace() ;       // 打印异常信息    
        }    
        gc.setTime(d);
        gc.add(1,age);
        gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
        return sdf2.format(gc.getTime());  
    } 
    
    public static String FormateDate(String date,String formate) {  
    	String str="";
    	SimpleDateFormat sdf1=null;
    	for(int i=0;i<date.length();i++){
    		String item =date.substring(i,i+1) ;
    		if(item.matches("[0-9]*")){
    			str=str+item;
    		}
    	}
    	if(str.length()==8){
    		sdf1 = new SimpleDateFormat("yyyyMMdd");
    	}else if(str.length()==14){
    		sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    	}else if(str.length()==17){
    		sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    	}
    	
    	Date d = null ; 
    	if(sdf1!=null){
    		//SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern) ;        // 实例化模板对象    
            // 实例化模板对象    
		     try{    
		         d = sdf1.parse(str) ;       // 将给定的字符串中的日期提取出来    
		     }catch(Exception e){            // 如果提供的字符串格式有错误，则进行异常处理    
		         e.printStackTrace() ;       // 打印异常信息    
		     }  
    	}else{
    		long lt = new Long(str);
            d = new Date(lt);
    	}
        
        SimpleDateFormat sdf2 = new SimpleDateFormat(formate) ; 
        String time=sdf2.format(d);       
        return time;  
    } 
    
         /** 
          * 时间戳转换成日期格式字符串 
          * @param seconds 精确到秒的字符串 
          * @param formatStr 
          * @return 
          */  
        public static String timeStamp2Date(String seconds,String format) {  
            if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
                return "";  
            }  
            if(format == null || format.isEmpty()){
                format = "yyyy-MM-dd HH:mm:ss";
            }   
            SimpleDateFormat sdf = new SimpleDateFormat(format);  
            return sdf.format(new Date(Long.valueOf(seconds+"000")));  
        }  
        /** 
         * 日期格式字符串转换成时间戳 
         * @param date 字符串日期 
         * @param format 如：yyyy-MM-dd HH:mm:ss 
         * @return 
         */  
        public static String date2TimeStamp(String date_str,String format){  
            try {  
                SimpleDateFormat sdf = new SimpleDateFormat(format);  
                return String.valueOf(sdf.parse(date_str).getTime()/1000);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return "";  
        }  
          
        /** 
         * 取得当前时间戳（精确到秒） 
         * @return 
         */  
        public static String timeStamp(){  
            long time = System.currentTimeMillis();
            String t = String.valueOf(time/1000);  
            return t;  
        }  
        
        /** 
         * 取得当前时间戳（精确到豪秒） 
         * @return 
         */  
        public static String timeStamp2(){  
            long time = System.currentTimeMillis();
            String t = String.valueOf(time);  
            return t;  
        }  
    

}

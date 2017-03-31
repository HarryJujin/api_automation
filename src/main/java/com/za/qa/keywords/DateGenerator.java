package com.za.qa.keywords;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;





public class DateGenerator{
	
	public boolean sleepLong(int num){
		Date dateBegin = new Date();
		Date dateEnd = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, num);
		dateEnd=cal.getTime();
		Date dateNow=dateBegin;
		while(dateNow.getTime()<dateEnd.getTime()){
			try {
				Thread.sleep(30000);
				System.out.println("sleep 一次");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dateNow = new Date(System.currentTimeMillis());
		} 
		if(dateNow.getTime()>dateEnd.getTime()){
			return true;
		}else{
			return false;
		}
		
	}
	/**
	 * getSystemDate,get currentDay,return with dateFormat
	 * @param dateFormat could be yyyy/MM/dd, dd-MM-yyyy, dd/MM/yyyy hh:mm:ss, yyy-MM-dd hh:mm:ss etc.
	 * @return
	 */
	public String getSystemDate(String dateFormat){
		
		Calendar cal = Calendar.getInstance(); 

		Date date = cal.getTime(); 
	    DateFormat format=new SimpleDateFormat(dateFormat); 
	    String systemDateWithFormat = format.format(date); 

		return systemDateWithFormat;
	}
	
	/**
	 * setValueForwardOrBackward,if forwardOrbackWard=false,then backward date,others forward date
	 * @param dateFormat format could be yyyy/MM/dd, dd-MM-yyyy, dd/MM/yyyy hh:mm:ss, yyy-MM-dd hh:mm:ss etc.
	 * @param offSet format must be:yyyy-MM-dd or yyyy-MM-dd hh:mm:ss   
	 * @param forwardOrbackWard false or other
	 * @throws ParseException 
	 */
	public String  getOffsetDate(String format, String offset) throws Exception
	{ 
		String time="";
		 Calendar ca = Calendar.getInstance();
			try {
				if(offset.contains("/")){
					String[] offSets = offset.split("/");

						ca.add(Calendar.YEAR, Integer.parseInt(offSets[0]));
						ca.add(Calendar.MONTH, Integer.parseInt(offSets[1]));
						ca.add(Calendar.DATE, Integer.parseInt(offSets[2]));
						if (offSets.length > 3) {
							ca.add(Calendar.HOUR, Integer.parseInt(offSets[3]));
							ca.add(Calendar.MINUTE, Integer.parseInt(offSets[4]));
							ca.add(Calendar.SECOND, Integer.parseInt(offSets[5]));
						}
				}else if(null!=offset){
					ca.add(Calendar.YEAR, Integer.parseInt(offset));
				}
			} catch (Exception e) {
				throw new Exception("Please check date format. And offset format must be 'yyyy-MM-dd' or 'yyyy-MM-dd HH:mm:ss'");
			}
		if(format.indexOf("sign")>-1){
			time=String.valueOf(ca.getTimeInMillis());
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			time = sdf.format(ca.getTime());
		}
			return time;
	}
	
/*@Test
	public void test() throws Exception{
		System.out.println(getOffsetDate("YYYY/MM/dd","0003/02/1"));
		System.out.println(getOffsetDate("YYYY-MM-dd HH:mm:ss","0003/02/1"));
    	System.out.println(getOffsetDate("YYYY-MM-dd","0003/02/1"));
    	System.out.println(getOffsetDate("YYYY-MM-dd","1/02/-1"));
		System.out.println(getOffsetDate("YYYY-MM-dd HH:mm:ss","0000/00/-05/08/0/0"));
		System.out.println(getOffsetDate("YYYY-MM-dd HH:mm:ss","-2"));
		System.out.println(getOffsetDate("YYYY-MM-dd HH:mm:ss","0"));
		System.out.println(getOffsetDate("YYYYMMddHHmmssSSS","0"));
	}*/
	
	
	
}
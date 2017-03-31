package com.za.qa.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.jsoup.Jsoup;


import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.zhongan.qa.utils.HttpClientUtils;
import com.zhongan.qa.utils.HttpResponseUtils;

/*import org.junit.Test;*/





/**
 * @author jujinxin
 *
 */

public class IDBQuery {
	
	 static String url_login ="http://idb.zhonganonline.com:9999/login";
	 static String url_getqueryrst="http://idb.zhonganonline.com:9999/getqueryrst";
	
	public static String idbString(String dbenv,String dbname,String tbname,String sql)   {
		Map<String, String> queryrst = new HashMap<String, String>();
		queryrst.put("dbenv", dbenv);//环境预发pre
		queryrst.put("dbname", dbname);//数据库名"investment_link"
		queryrst.put("iscount", "0");
		queryrst.put("selectmod", "1");
		queryrst.put("splitcol", "-1");
		queryrst.put("splitcolmode", "=");
		queryrst.put("sql", sql);
		queryrst.put("tbname", tbname);//表名
		String queryStr="";
		try {
			//queryStr = HttpClientA.doRequest(url_getqueryrst, queryrst, null, "utf-8");
			CloseableHttpResponse response = HttpClientUtils.testWithKeyValue(null, null, url_getqueryrst, "POST", queryrst);
			queryStr = HttpResponseUtils.getResponseContent(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queryStr;
	}
	
	public static String getTableSql(String columnName,String tableSqlStr) throws Exception{
		int thNum=0;
		int tdNum=0;
		String columnValue="";
		String tableSqlStrTem= tableSqlStr;
		columnName="<th>"+columnName+"</th>";

		if(tableSqlStrTem.indexOf(columnName)>0){
			tableSqlStrTem=tableSqlStrTem.substring(0, tableSqlStr.indexOf(columnName));
			//计数求columnName位置
			while(tableSqlStrTem.indexOf("<th>")>0){
				tableSqlStrTem=tableSqlStrTem.substring(tableSqlStrTem.indexOf("<th>")+4, tableSqlStrTem.length());
				thNum++;
			}
			thNum=thNum+1;
			//columnName位置对应个数的td值
			while(tableSqlStr.indexOf("<td>")>0){
				tableSqlStr=tableSqlStr.substring(tableSqlStr.indexOf("<td>")+4, tableSqlStr.length());
				tdNum++;
				if(thNum==tdNum&&thNum>0){
					columnValue=tableSqlStr.substring(0, tableSqlStr.indexOf("</td>"));
					break;
				}
			}
		}
		columnValue=columnValue.replace("&nbsp;"," ").replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("&quot;","\"").replace("&apos; ","'");
		return columnValue;
		
	}	
	
	public static  List<String> getTableColumnSet(String columnName,String tableSqlStr) throws Exception{
		int columnNum=-1;
		List<String> columnValueList=new ArrayList<String>();
		try {
			Document doc =  Jsoup.parse(tableSqlStr);
			Elements trs = doc.select("table").select("tr");
			Elements ths = trs.get(0).select("th");

			for(int i = 0;i<ths.size();i++){
				String name=ths.get(i).text();
				if(columnName.equals(name)){
					columnNum=i;
				}
			}
			//没有所查询字段
			if(columnNum>0){
				for(int j = 1;j<trs.size();j++){
				    Elements tds = trs.get(j).select("td");
				    String value;
					try {
						value = tds.get(columnNum).text();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						value="";
					}
					value=value.replace("&nbsp;"," ").replace("&lt;","<").replace("&gt;",">").replace("&amp;","&").replace("&quot;","\"").replace("&apos; ","'");
				    columnValueList.add(value);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return columnValueList;
	}	
/*	@Test
	public void test() throws Exception{
	String idbString = idbString("pre","investment_link","il_channelaccount_account_xref","trade_account='201608251458546374'");
	String A=getTableSql("cert_no",idbString);
	String A2=getTableSql("id",idbString);
	System.out.println("a = "+A+"  "+A2);
	}*/

	
	
	/*String lastException = "";

    String listCommand;
    Document doc;
	Elements trs;
	Elements ths;

	List<String> columns = null;

    private long lastRunTime;

    *//**
     * Constructor.
     * 
     * @param command
     *            The list type command to be executed
     *//*
    public IDBQuery(String str) {
        listCommand = str;
        System.out.println("waiting to be prased string is : " + listCommand.toString());
        listCommand = StringEscapeUtils.unescapeHtml(listCommand);
        System.out.println("*****After html : " + listCommand.toString());

    	doc = Jsoup.parse(listCommand);
    	trs = doc.select("table").select("tr");
    	ths = trs.get(0).select("th");
    }

    public IDBQuery() {
		// TODO Auto-generated constructor stub
	}

	*//**
     * The table method is called first and allows us to extract the first row
     * and get the column headings. This allows the building of a list of lists
     * of strings to match the columns in the table on the page.
     * 
     * @param table
     *            The table being executed on the page
     *//*
    public void table(List<List<String>> table) {
        columns = table.get(0);
        System.out.println("fitnesse expected table's columns is " +columns.toString());
    }

    public void investigateQuery(String str){
    	for (int i = 0;i < columns.size();i++){
    		
    	}
    }
    *//**
     * Required method to be used in a QueryTable in Slim
     * 
     * @return A List of Lists of Strings that contains the output of the
     *         command, filtered to the columns specified by the page creator.
     * @throws Exception
     *//*
    public List<Object> query() throws Exception {
        List<Object> outerList = new ArrayList<Object>();
       
        if (listCommand != null) {
        	for(int k = 1; k <trs.size(); k++) {
        		System.out.println("KKKKKKKK" + k );
                outerList.add(generateListForCurrentRow(k));
            }
  
        }
        System.out.println("outerList is: " + outerList.toString());
        return outerList;
    }

    
    private List<Object> generateListForCurrentRow(int k) throws Exception {
        List<Object> row = new ArrayList<Object>();
        for (Iterator<String> it = columns.iterator(); it.hasNext();) {
            String columnName = it.next();
            String value = getValueFromColumnInCurrentRow(columnName, k);
            row.add(list(columnName, value.toString()));
        }
        System.out.println("current row is: " + row.toString());
        return row;
    }

    public String getValueFromColumnInCurrentRow(String columnName, int i) {
    	
    	 System.out.println("IIII is: " + i);
    	 String columnValue="";
     	int j;
    	for (j = 0; j < ths.size(); j++) {
			String text = ths.get(j).text();
			if (text.equals(columnName)){
				break;
			}			
    	}
    	//从第二行开始取对应的数据，因为第一行是表头
    	
    		Elements tds = trs.get(i).select("td");
    		columnValue = tds.get(j).text();
    	
    	
    	System.out.println("columnName's related column value is " + columnValue);
    	return columnValue;
    }

    public String getValueForColumn(String columnName, String htmlTable ){
    	
    	String value = "";
    	listCommand = htmlTable;
    	doc = Jsoup.parse(listCommand);
    	trs = doc.select("table").select("tr");
    	ths = trs.get(0).select("th");
    	value = getValueFromColumnInCurrentRow(columnName,1);
		return value;
    	
    }
    
    public Float lastRunTimeInSeconds() {
        Long runTime = new Long(lastRunTime);
        return runTime.floatValue() / 1000;
    }
*/
}

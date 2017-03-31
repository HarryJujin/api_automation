package com.za.qa.verify;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.za.qa.analyze.DataAnalyze;
import com.za.qa.constants.CaseStatus;
import com.za.qa.dto.CaseAfterRunDTO;
import com.za.qa.dto.CaseConfigurationDTO;
import com.za.qa.dto.CaseDataDTO;
import com.za.qa.dto.CaseReportDTO;
import com.za.qa.keywords.ExpressionRegister;
import com.za.qa.keywords.IDBQuery;
import com.za.qa.log.LogMan;
import com.za.qa.utils.SimpleDateUtils;

public class SqlVerify {
	public static String Sqlcheck1(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO,CaseAfterRunDTO caseafterrunDTO){
		String checkResult="";
		Boolean bool=true;
		String[] listNameArray=caseConfigurationDTO.getListPath().split("#");
		String AllSqlOfOneCase=listNameArray[0]+"Sql校验";
		String listNo = listNameArray[1];
		String caseNo = casedataDTO.getCaseNo();
		String dbenv  = casedataDTO.getListflag();
		if (dbenv.equals("uat")){
			dbenv="pre";
		}else{
			dbenv="tst";
		}
		Map<String,String[]> sqlMap=caseConfigurationDTO.getDataConfiguration().get(AllSqlOfOneCase);
		String[] ListArray=sqlMap.get("CASElist_NO");
		String[] CaseArray=sqlMap.get("CASE_NO");
		String queryStr="";
		for(int i=0;i<ListArray.length;i++){
			//查找对应的集合和对应的case，找到之后try下做IDBQuery
			if(ListArray[i].equals(listNo)){
				if(CaseArray[i].equals(caseNo)){
						String dbname=sqlMap.get("dbname")[i];
						String tbname=sqlMap.get("tbname")[i];
						String sql=sqlMap.get("WhereCondition")[i];
						try {
							sql=DataAnalyze.analyzeStr(sql, casedataDTO, caseConfigurationDTO);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							bool=bool&&false;
							checkResult="sql关键字解析失败";
							e1.printStackTrace();
							break;
						}
						try {
							queryStr=IDBQuery.idbString(dbenv, dbname, tbname, sql);
							//查询后遍历所有需要比较的字段，如果字段预期不为空做比较
							for(String key:sqlMap.keySet()){
								if(!key.equals("CASElist_NO")&&!key.equals("CASE_NO")&&!key.equals("CASE_Desc")&&!key.equals("dbname")&&!key.equals("tbname")&&!key.equals("WhereCondition")){
									String expectResult = sqlMap.get(key)[i];
									expectResult = DataAnalyze.analyzeStr(expectResult, casedataDTO, caseConfigurationDTO);
									if(expectResult.length()>0){
										String actualResult = IDBQuery.getTableSql(key, queryStr);
										if (expectResult.equals(actualResult)){
											bool=bool&&true;
										}else{
											bool=bool&&false;
											checkResult=checkResult+dbname+"的表"+tbname+"的字段"+key+"错误["+"预期："+expectResult+",实际："+actualResult+"];\n";
										}
										//保存结果到环境变量
										String sqlKey=caseConfigurationDTO.getListPath()+"#"+caseNo+"#"+dbname+"#"+tbname;
										ExpressionRegister.setSqlSelectEnv(sqlKey, actualResult);
									}

								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		}
		//返回结果
		if (bool) {
			return CaseStatus.PASS.getCode();
		} else {
			return checkResult;
		}
	}
	
	public static String Sqlcheck(CaseDataDTO casedataDTO,CaseConfigurationDTO caseConfigurationDTO,CaseAfterRunDTO caseafterrunDTO){
		
		String checkResult="";
		List<String> CaseSelectList=new ArrayList<String>();
		
		Boolean bool=true;
		//String[] listNameArray=caseConfigurationDTO.getListPath().split("#");
		//String AllSqlOfOneCase=listNameArray[0]+"Sql校验";
		//String listNo = listNameArray[1];
		String listPath =caseConfigurationDTO.getListPath();
		String caseNo = casedataDTO.getCaseNo();
		String dbenv  = casedataDTO.getListflag();
		if (dbenv.equals("uat")){
			dbenv="pre";
		}else{
			dbenv="tst";
		}
		//sqlMap为excel表中listNameArray[0]+"Sql校验"的数据
		//Map<String,String[]> sqlMap=caseConfigurationDTO.getDataConfiguration().get(AllSqlOfOneCase);
		if(ExpressionRegister.sqlExpectEnv.get(listPath+"#"+caseNo)!=null){
			//DataPrepareOfSqlCheck.setOneSqlExpectdata(sqlMap, listNameArray[0]);
			//Map< String,       Map<String  , List<String>>>
			//<dbname#tbname#sql,<columnName , valueList>-----checkContent为一个案例所有表的查询，遍历所有键值查询这个案例需要查询的所有表
			Map<String, Map<String, List<String>>> checkContent=ExpressionRegister.sqlExpectEnv.get(caseConfigurationDTO.getListPath()+"#"+casedataDTO.getCaseNo());
			if(checkContent!=null){
				//checkContent排序
				String[] oneCaseSqlArray=new  String [checkContent.size()];
				for(String oneTableSelectStr:checkContent.keySet()){
					String[] Array=oneTableSelectStr.split("#");
					String numStr=Array[3];
					oneCaseSqlArray[Integer.parseInt(numStr)-1]=oneTableSelectStr;
				}
				//循环排序后的checkContent的所有key值，并组合后查询开始查询到后的循环
				for(String oneTableSelectStr:oneCaseSqlArray){
					String[] Array=oneTableSelectStr.split("#");
					String dbname=Array[0];
					String tbname=Array[1];
					String sql=Array[2];
					String queryStr="";
					//前置处理如sleep（where后用|分隔）
					if(sql.contains("|")){
						String ProProcessor=sql.split("\\|")[1];
						sql=sql.split("\\|")[0];
						try {
							DataAnalyze.analyzeStr(ProProcessor, casedataDTO, caseConfigurationDTO);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					//解析sql（where后的字符串）
					try {
						sql=DataAnalyze.analyzeStr(sql, casedataDTO, caseConfigurationDTO);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						bool=bool&&false;
						checkResult=dbname+"的表"+tbname+"的sql关键字解析失败";
						e1.printStackTrace();
						break;
					}
					try {
						Date start = new Date(System.currentTimeMillis());
						String BeginTime= SimpleDateUtils.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
						//查询单条sql
						queryStr=IDBQuery.idbString(dbenv, dbname, tbname, sql);
						Date end =new Date(System.currentTimeMillis());
						LogMan.getLoger().info("----*************************----，程序运行时间【"+ (end.getTime() - start.getTime()) + "毫秒】");

						if(queryStr.length()>288){
							CaseSelectList.add(queryStr);
							String sqlKey=caseConfigurationDTO.getListPath()+"#"+caseNo+"#"+dbname+"."+tbname;
							ExpressionRegister.setSqlSelectEnv(sqlKey, queryStr);
							Map<String, List<String>> oneTableCheckContent=checkContent.get(oneTableSelectStr);
							
							//循环所有字段对应值的集合并与实际结果比较
							for(String columnName:oneTableCheckContent.keySet()){
								if(!columnName.equals("CASElist_NO")&&!columnName.equals("CASE_NO")&&!columnName.equals("CASE_Desc")&&!columnName.equals("dbname")&&!columnName.equals("tbname")&&!columnName.equals("WhereCondition")){
									//预期结果解析并放入set
									List<String> expectList=new ArrayList<String>();
									for(String expectValue:oneTableCheckContent.get(columnName)){
										expectValue = DataAnalyze.analyzeStr(expectValue, casedataDTO, caseConfigurationDTO);
										if(expectValue.length()>0){
											expectList.add(expectValue);
										}
									}
									if(expectList.size()>0){
										//预期与实际结果比较
										List<String> actualList = IDBQuery.getTableColumnSet(columnName, queryStr);
										if(actualList.size()==expectList.size()){
											for(String value:actualList){
												if(expectList.contains(value)){
													bool=bool&&true;
												}else{
													bool=bool&&false;
													checkResult=checkResult+tbname+"."+columnName+"查询结果："+actualList+"；\n";
												}
											}
										}else{
											bool=bool&&false;
											checkResult=checkResult+tbname+"."+columnName+"值的数目："+actualList.size()+"；\n";
										}
									}
								}
							}
						}else{
							//queryStr.length()不大于288，说明查询中无table记录
							bool=bool&&false;
							checkResult=checkResult+tbname+"无查询记录";
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						bool=bool&&false;
						checkResult=checkResult+tbname+"查询sql异常，无返回";
					}

				}
			}
		}
		//String key = caseConfigurationDTO.getListPath()+"#"+caseNo;
		//ExpressionRegister.setSqlSelectEnv(key, CaseSelectList.toString());
		//返回结果
		if (bool) {
			return CaseStatus.PASS.getCode();
		} else {
			return checkResult;
		}
	}
	//测试全部结果存入数据库
	public static void insertIntoDataBase(List<CaseReportDTO> list,String testSequence){
		Map<String,String> map = new HashMap<String,String>();
//		for(int i=0;i<list.size();i++){
//			CaseReportDTO caseReportDTO=list.get(i);
//		}
		//String insertSql=insertSqlStr(map,"auto_test_00.t_tongyongexport1");
		String sql = "INSERT auto_test_00.t_tongyongexport1(testCaseId,testProduct,testInsuranceNo,testResult,testRequest,testResponse,testDate,creator_time) VALUES(?,?,?,?,?,?,?,?)"; 

		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名scutcs
		String url = "jdbc:mysql://rds6fbqrf6fbqrf.mysql.rds.aliyuncs.com/auto_test_00";
		// MySQL配置时的用户名
		String user = "auto_test";
		// Java连接MySQL配置时的密码
		String password = "auto_test_9ac5ee";
		try {
		// 加载驱动程序
		Class.forName(driver);
		// 连续数据库
		Connection conn = DriverManager.getConnection(url, user, password);
		conn.setAutoCommit(false);
		if(!conn.isClosed())
		System.out.println("Succeeded connecting to the Database!");
		// statement用来执行SQL语句
		PreparedStatement  prest = conn.prepareStatement(sql);
		// 要执行的SQL语句
		 for(int x = 0; x < list.size(); x++){ 
			 CaseReportDTO caseReportDTO=list.get(x);
			 String result=caseReportDTO.getTest_result();
			 String response=caseReportDTO.getReponse();
			 if(response.length()>3000){
				 response = response.substring(0,3000);
			 }
			 String request=caseReportDTO.getPayloaddata();
			 String Url=caseReportDTO.getUrl();
			 String testSuite=caseReportDTO.getCaseDataDTO().getCaseListNo();
			 String testCase=caseReportDTO.getCaseDataDTO().getCaseNo();
			 String testProduct= testSuite+"#"+testCase;
			 SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
			 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


			  prest.setString(1, testSequence); 
			  prest.setString(2, testProduct); 
			  prest.setString(3, ""); 
			  prest.setString(4, result); 
			  prest.setString(5, request); 
			  prest.setString(6, response); 
			  prest.setString(7, fmt.format(new Date())); 
			  prest.setString(8, sdf.format(new Date())); 
			  prest.addBatch(); 
			 } 
			 prest.executeBatch(); 
			 conn.commit();  
		conn.close();
		System.out.println("Succeeded insert Sql into the Database!");
		}
		catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 
	
	}
	
	public static String insertSqlStr(Map<String,String> map,String table){
		String sql1="INSERT INTO "+table+"(";
		String sql2=") VALUES (";
		for(String s : map.keySet()){	
			sql1=sql1+s+",";
			sql2=sql2+"'"+map.get(s)+"'"+",";
		}
		String sql=sql1+sql2;
		sql=sql.substring(0,sql.length()-1);
		return sql;
	}
	
}

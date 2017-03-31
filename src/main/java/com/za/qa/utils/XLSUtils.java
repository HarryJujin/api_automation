package com.za.qa.utils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSUtils {
	
	public static List<String> getSheetName(String path) throws Exception{
		InputStream is =new FileInputStream(path);
		XSSFWorkbook XSSFWorkbook = new XSSFWorkbook(is);
		int sheet_num= XSSFWorkbook.getNumberOfSheets();
		List<String> sheetlist= new ArrayList<String>();
		for(int i=0;i<sheet_num;i++){
			sheetlist.add(XSSFWorkbook.getSheetName(i));
		}
		return sheetlist;
	}

	public static Map <String,String[]> getXlsData(String path,String sheetname,int beginLineNum) throws IOException{
		Map <String,String[]> map=new HashMap<String,String[]>();
		DecimalFormat decimalformat = new DecimalFormat("0");
		InputStream is =new FileInputStream(path);
		XSSFWorkbook XSSFWorkbook = new XSSFWorkbook(is);
		XSSFSheet XSSFSheet = XSSFWorkbook.getSheet(sheetname);
		XSSFRow XSSFFirstRow = XSSFSheet.getRow(beginLineNum-1);
		try {
			int lastCellNum=XSSFFirstRow.getLastCellNum();
			//key 值数组array赋值
			String []array = new  String[lastCellNum];
			int i=0;
			for(i=0;i<lastCellNum;i++){
				String value="";
				Cell cell = XSSFFirstRow.getCell(i);
			    if (cell != null) {
			       switch (cell.getCellType()) {
			       case Cell.CELL_TYPE_FORMULA:
			          break;
			       case Cell.CELL_TYPE_NUMERIC:
			          value = decimalformat.format(cell.getNumericCellValue());
			          break;
			       case Cell.CELL_TYPE_STRING:
			          value = cell.getStringCellValue();
			          break;
			       default:
			          value = "";
			          break;
			       }
			    }        
				array[i]=value;
				//System.out.println(array[i]);
			}	
			//数据存入二维数组
			int lastRowNum=XSSFSheet.getLastRowNum()+1;
			//System.out.println("行数："+lastRowNum);
			String [][]arrayTwo=new String[lastRowNum-beginLineNum][lastCellNum];
			for(int j=0;j<lastRowNum-beginLineNum;j++) {
				for(int k=0;k<lastCellNum;k++){
					String value="";
					Cell cell = XSSFSheet.getRow(j+beginLineNum).getCell(k);
			        if (cell != null) {
			           switch (cell.getCellType()) {
			           case Cell.CELL_TYPE_FORMULA:
			              break;
			           case Cell.CELL_TYPE_NUMERIC:
			              value = decimalformat.format(cell.getNumericCellValue());
			              break;
			           case Cell.CELL_TYPE_STRING:
			              value = cell.getStringCellValue();
			              break;
			           default:
			              value = "";
			              break;
			           }
			        }        
					arrayTwo[j][k]=value;
				}
				
			}

			//数据放入map
			for(i=0;i<lastCellNum;i++)
			{	
				String arraylist[]=new String[lastRowNum-beginLineNum];
				for(int j=0;j<lastRowNum-beginLineNum;j++){arraylist[j]=arrayTwo[j][i];
				//System.out.println(arraylist[j]);
				}
				map.put(array[i], arraylist);
				//System.out.println("key:"+array[i]);
				//System.out.println("----------------------");
			}
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			String [] logArray=new String [1];
			logArray[0]="excel格式不规范";
			map.put("log", logArray);
			return map;
		}
	}
	
	public static boolean putXlsData(String path,String sheetname,int beginLineNum,String key,int insertRowNum,String cellValue) throws IOException{
		DecimalFormat decimalformat = new DecimalFormat("0");
		InputStream is =new FileInputStream(path);
		XSSFWorkbook XSSFWorkbook = new XSSFWorkbook(is);
		XSSFSheet XSSFSheet = XSSFWorkbook.getSheet(sheetname);
		XSSFRow XSSFFirstRow = XSSFSheet.getRow(beginLineNum-1);
		int lastCellNum=XSSFFirstRow.getLastCellNum();
		//key 值数组array赋值
		//查找key所在列，不存在时：新增最后一列
		int insertCellNum=-1;
		//String []array = new  String[lastCellNum];
		for(int i=0;i<lastCellNum;i++){
			String value="";
			Cell cell = XSSFFirstRow.getCell(i);
            if (cell != null) {
               switch (cell.getCellType()) {
               case Cell.CELL_TYPE_FORMULA:
                  break;
               case Cell.CELL_TYPE_NUMERIC:
                  value = decimalformat.format(cell.getNumericCellValue());
                  break;
               case Cell.CELL_TYPE_STRING:
                  value = cell.getStringCellValue();
                  break;
               default:
                  value = "";
                  break;
               }
            }        
			//array[i]=value;
			if(key.equals(value)){
				insertCellNum=i;	
				break;
			}
			//System.out.println(array[i]);
			
		}	
		//列不存在则插入新列
		
			try {
				if(insertCellNum==-1){
					XSSFFirstRow.createCell(lastCellNum).setCellValue(key);							 
					XSSFRow insertRow = XSSFSheet.getRow(insertRowNum+beginLineNum);
					insertRow.createCell(lastCellNum).setCellValue(cellValue);
				}else{
					XSSFRow insertRow = XSSFSheet.getRow(insertRowNum+beginLineNum);
					insertRow.createCell(insertCellNum).setCellValue(cellValue);
				}
			        //创建文件流   
			        OutputStream stream = new FileOutputStream(path); 
			        //写入数据   
			        XSSFWorkbook.write(stream); 
			       
			        //关闭文件流   
			        stream.close(); 
					return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
	 		 	
	}

}

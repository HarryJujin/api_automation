package com.za.qa.dataprepare;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.za.qa.constants.CaseConf;
import com.za.qa.log.LogMan;
import com.za.qa.utils.FileUtils;
import com.za.qa.utils.Utilities;
import com.za.qa.utils.XLSUtils;

public class DataResource {

    public static Map<String, Map<String, String[]>> allSuiteMap() throws Exception {

        //线程池信息
        int threadNum = 3;
        ExecutorService pool = Executors.newFixedThreadPool(threadNum);

        //excel所在目录
        String ExcelFile = FileUtils.readProperties(CaseConf.confpath).getProperty("ExcelFile");
        ExcelFile =new String(ExcelFile.getBytes("ISO-8859-1"),"gbk");

        Map<String, Map<String, String[]>> mapAll = new HashMap<String, Map<String, String[]>>();
        @SuppressWarnings("rawtypes")
        List<Future> list = new ArrayList<Future>();
        File[] paths = Utilities.getExcelList(ExcelFile);
        int orderNumOfSuite=0;
        for (File pathFile : paths) {
            String path = pathFile.toString();
            List<String> sheetlist = XLSUtils.getSheetName(path);
            for (int m = 0; m < sheetlist.size(); m++) {
            	//发放orderNumOfSuite为报告中的suite排序
                String sheetname = sheetlist.get(m);
                System.out.println(pathFile);
                String str=new String(String.valueOf(orderNumOfSuite));
                Callable<Map<String, Map<String, String[]>>> call = new DataResourceCallable(path, sheetname,str);
                Future<Map<String, Map<String, String[]>>> f = pool.submit(call);
                list.add(f);
                orderNumOfSuite++;
            }

        }
        pool.shutdown();
        for (Future<?> f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            mapAll.putAll((Map<String, Map<String, String[]>>) f.get());
        }
        return mapAll;

    }

    public static Map<String, Map<String, String[]>> TestSuiteOfSheet() throws Exception {
        String excelHome = FileUtils.readProperties(CaseConf.confpath).getProperty("ExcelFile");
        excelHome =new String(excelHome.getBytes("ISO-8859-1"),"gbk");
        File[] paths = Utilities.getExcelList(excelHome);
        Map<String, Map<String, String[]>> map = new HashMap<String, Map<String, String[]>>();
        Map<String, String[]> mapData = new HashMap<String, String[]>();
        for (File pathFile : paths) {
            String path = pathFile.toString();
            List<String> sheetlist = XLSUtils.getSheetName(path);
            for (int m = 0; m < sheetlist.size(); m++) {
                String sheetname = sheetlist.get(m);
                System.out.println("sheetname:" + sheetname);
                int keyNum = 3;
                String targetKey = "CASElist_NO";
                mapData = XLSUtils.getXlsData(path, sheetname, keyNum);
                if (mapData.entrySet().toString().contains(targetKey)) {
                    mapData = XLSUtils.getXlsData(path, sheetname, keyNum);
                    /** 获取对应的案例集所在行的集合 **/
                    Map<String, List<Integer>> suiteNameMap = DataResource.getTargetKeySet(path, sheetname, keyNum,
                            targetKey);
                    /** 开始获取数据到map **/
                    map = DataResource.getTestSuite(path, sheetname, keyNum, suiteNameMap, "");

                }
            }
        }
        return map;
    }

    /**
     * @author heyuan 获取sheet页面内的测试集名称对应行号集合 keyNum:key值所在行 targetKey:划分的key名称
     * targetKey:测试集字段名case_list
     */
    public static Map<String, List<Integer>> getTargetKeySet(String path, String sheetname, int keyNum, String targetKey)
            throws IOException {
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        DecimalFormat decimalformat = new DecimalFormat("0");
        InputStream is = new FileInputStream(path);
        XSSFWorkbook XSSFWorkbook = new XSSFWorkbook(is);
        XSSFSheet XSSFSheet = XSSFWorkbook.getSheet(sheetname);
        XSSFRow XSSFFirstRow = XSSFSheet.getRow(keyNum - 1);
        //获取最后一列
        int lastCellNum = XSSFFirstRow.getLastCellNum();
        //获取最后一行
        int lastRow = XSSFSheet.getLastRowNum();
        int targetKeyCellNum = 0;
        Set<String> set = new LinkedHashSet<String>();
        System.out.println(path+"#"+sheetname+"lastCellNum:" + lastCellNum);
        System.out.println(path+"#"+sheetname+"lastRow:" + lastRow);

        //查找目标key所在的列
        String[] array = new String[lastCellNum];
        int i = 0;
        for (i = 0; i < lastCellNum; i++) {
            String value = "";
            Cell cell = XSSFFirstRow.getCell(i);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        value = decimalformat.format(cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        break;
                    default:
                        value = "";
                        break;
                }
            }
            array[i] = value;
            if (targetKey.equals(value)) {
                targetKeyCellNum = i;
                break;
            }
            //System.out.println(array[i]);
        }
        //查找key所在行后面的所有集合名称
        for (int j = keyNum; j <= lastRow; j++) {
            String value = "";
            Cell cell = XSSFSheet.getRow(j).getCell(targetKeyCellNum);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        value = decimalformat.format(cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        break;
                    default:
                        value = "";
                        break;
                }
            }
            set.add(value);
        }
     //定义排序变量
        int orderNum=0;
        for (String SuiteName : set) {
            List<Integer> listSuite = new ArrayList<Integer>();
            //特殊处理commen的三行
            listSuite.add(keyNum);
            listSuite.add(keyNum + 1);
            listSuite.add(keyNum + 2);
            for (int j = keyNum + 3; j <= lastRow; j++) {
                String value = "";
                Cell cell = XSSFSheet.getRow(j).getCell(targetKeyCellNum);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            value = decimalformat.format(cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            break;
                        default:
                            value = "";
                            break;
                    }
                }

                if (SuiteName.equals(value)) {
                    listSuite.add(j);
                }
            }
            //SuiteName后面增加排序
            SuiteName=SuiteName+","+orderNum;
            map.put(SuiteName, listSuite);
            orderNum++;
        }

        return map;
    }

    /**
     * @author heyuan 获取指定的测试集对应的map数据 keyNum:key值所在行 targetKey:划分的key名称
     *         targetKeyValue:划分的key值
     */
    public static Map<String, Map<String, String[]>> getTestSuite(String path, String sheetname, int keyNum,
                                                                  Map<String, List<Integer>> suiteNameMap,String orderNumOfSuite)
            throws IOException {
        Map<String, Map<String, String[]>> mapAll = new HashMap<String, Map<String, String[]>>();

        DecimalFormat decimalformat = new DecimalFormat("0");
        InputStream is = new FileInputStream(path);
        XSSFWorkbook XSSFWorkbook = new XSSFWorkbook(is);
        XSSFSheet XSSFSheet = XSSFWorkbook.getSheet(sheetname);
        XSSFRow XSSFFirstRow = XSSFSheet.getRow(keyNum - 1);

        int lastCellNum = XSSFFirstRow.getLastCellNum();

        //key 值数组array赋值
        String[] array = new String[lastCellNum];
        int i = 0;
        for (i = 0; i < lastCellNum; i++) {
            String value = "";
            Cell cell = XSSFFirstRow.getCell(i);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        value = decimalformat.format(cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        break;
                    default:
                        value = "";
                        break;
                }
            }
            array[i] = value;
        }

        //遍历所有的suite并设置对应的map数据
        for (String suitName : suiteNameMap.keySet()) {
        	//取出去掉序号的suitName
        	String[] suitNameArray=suitName.split(",");
        	String suitNameLeft=suitNameArray[0];
            Map<String, String[]> map = new HashMap<String, String[]>();
            List<Integer> listSuite = suiteNameMap.get(suitName);
            if (suitNameLeft.length()>0&&listSuite.size() > 3) {
                //数据(对应的案例集合的map数据)存入二维数组		
                String[][] arrayTwo = new String[listSuite.size()][lastCellNum];
                int l = 0;
                for (int rowNum : listSuite) {
                    for (int k = 0; k < lastCellNum; k++) {
                        String value = "";
                        Cell cell = XSSFSheet.getRow(rowNum).getCell(k);
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
                        arrayTwo[l][k] = value;
                    }
                    l++;
                }

                //数据放入map
                for (i = 0; i < lastCellNum; i++) {
                    String arraylist[] = new String[listSuite.size()];
                    for (int j = 0; j < listSuite.size(); j++) {
                        arraylist[j] = arrayTwo[j][i];
                    }
                    map.put(array[i], arraylist);
                }
                //放入orderNumOfSuite---------------------------
                String[] orderNumOfSuiteArry=new String[listSuite.size()];
                for(i = 0; i < listSuite.size(); i++){
                	int intOrderNumOfSuite=Integer.parseInt(orderNumOfSuite)*1000+Integer.parseInt(suitNameArray[1]);
                	orderNumOfSuiteArry[i]=String.valueOf(intOrderNumOfSuite);
                }
                map.put("orderNumOfSuite", orderNumOfSuiteArry);
                suitName = path + "#" + suitNameLeft;
                mapAll.put(suitName, map);
            }

        }
        return mapAll;
    }

}

class DataResourceCallable implements Callable {
    private String path;
    private String sheetName;
    private String orderNumOfSuite;

    public DataResourceCallable(String path, String sheetName,String orderNumOfSuite) {
        this.path = path;
        this.sheetName = sheetName;
        this.orderNumOfSuite = new String(orderNumOfSuite);
    }

    public Map<String, Map<String, String[]>> call() throws Exception {
        // TODO Auto-generated method stub
        Map<String, Map<String, String[]>> map = new HashMap<String, Map<String, String[]>>();
        Map<String, String[]> mapData = new HashMap<String, String[]>();
        int keyNum = 3;
        String targetKey = "CASElist_NO";
        mapData = XLSUtils.getXlsData(path, sheetName, keyNum);
        if (mapData.entrySet().toString().contains(targetKey)) {
            /** 获取对应的案例集所在行的集合 **/
            Map<String, List<Integer>> suiteNameMap = DataResource.getTargetKeySet(path, sheetName, keyNum, targetKey);
            /** 开始获取数据到map **/
            map = DataResource.getTestSuite(path, sheetName, keyNum, suiteNameMap,this.orderNumOfSuite);
        }
        return map;
    }

}

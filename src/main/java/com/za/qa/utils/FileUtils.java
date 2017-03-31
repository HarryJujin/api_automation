package com.za.qa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class FileUtils {
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
            return false;
        }
        //判断目标文件所在的目录是否存在  
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录  
            System.out.println("目标文件所在目录不存在，准备创建它！");
            if (!file.getParentFile().mkdirs()) {
                System.out.println("创建目标文件所在目录失败！");
                return false;
            }
        }
        //创建目标文件  
        try {
            if (file.createNewFile()) {
                System.out.println("创建单个文件" + destFileName + "成功！");
                return true;
            } else {
                System.out.println("创建单个文件" + destFileName + "失败！");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());
            return false;
        }
    }

    public static void saveTo(InputStream in, OutputStream out) {
        try {
            byte[] data = new byte[1024];
            int index = 0;
            while ((index = in.read(data)) != -1) {
                out.write(data, 0, index);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void outPutList2File(String filePath, ArrayList<String> outList) {
        FileUtils.createFile(filePath);
        try {
            FileOutputStream fileName = new FileOutputStream(filePath);
            OutputStreamWriter outWriter = new OutputStreamWriter(fileName);
            Iterator<String> outListIterator = outList.iterator();
            while (outListIterator.hasNext()) {
                outWriter.write(outListIterator.next() + "\r\n");
            }
            outWriter.flush();
            outWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取conf.properties 配置文件中excel的路径
     */
    public static Properties readProperties(String filePath) throws IOException {
        File CONFIG = new File(filePath);
        Properties pp = new Properties();
        pp.load(new FileInputStream(CONFIG));
        return pp;
    }

}

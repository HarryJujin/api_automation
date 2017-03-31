package com.za.qa.bo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 类SqlQueryResultContent.java的实现描述：查询结果详细内容
 * 
 * @author linyun 2017年3月10日 上午9:45:46
 */
public class SqlQueryResultContent {
    private String           dbName;       //数据库名.表名
    private List<JSONObject> resultContent; //查询结果

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<JSONObject> getResultContent() {
        return resultContent;
    }

    public void setResultContent(List<JSONObject> resultContent) {
        this.resultContent = resultContent;
    }

    @Override
    public String toString() {
        return "SqlQueryResultContent [dbName=" + dbName + ", resultContent=" + resultContent + "]";
    }
}

package com.za.qa.bo;

import java.util.List;

/**
 * 类BaseSqlQueryResult.java的实现描述：数据库查询结果对象
 * 
 * @author linyun 2017年3月10日 上午9:42:26
 */
public class BaseSqlQueryResult {

    private String                      code;       //0成功，1失败
    private String                      errorMsg;   //错误信息
    private List<SqlQueryResultContent> contentList; //结果集

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<SqlQueryResultContent> getContentList() {
        return contentList;
    }

    public void setContentList(List<SqlQueryResultContent> contentList) {
        this.contentList = contentList;
    }

    @Override
    public String toString() {
        return "BaseSqlQueryResult [code=" + code + ", errorMsg=" + errorMsg + ", contentList=" + contentList + "]";
    }

}

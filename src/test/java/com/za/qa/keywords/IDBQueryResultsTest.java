package com.za.qa.keywords;

import java.util.List;

import org.junit.Test;

import com.za.qa.bo.BaseSqlQueryResult;
import com.za.qa.bo.SqlQueryResultContent;

public class IDBQueryResultsTest {

    @Test
    public void getQueryResultTest() {
        String dbEnv = "tst";
        String dbName = "policy";
        String tbName = "policy";
        String whereCondition = "policy_no='ha1100001940000437'";
        BaseSqlQueryResult result = IDBQueryResults.getQueryResult(dbEnv, dbName, tbName, whereCondition);
        String code = result.getCode();
        if ("0".equals(code)) {
            List<SqlQueryResultContent> resultContent = result.getContentList();
            System.out.println(resultContent.get(0).getResultContent().toString());
        }
    }

    @Test
    public void getQueryResultTestWithErrorInput() {
        String dbEnv = "tst";
        String dbName = "policy";
        String tbName = "policy";
        String whereCondition = "policy='ha1100001940000437'";
        BaseSqlQueryResult result = IDBQueryResults.getQueryResult(dbEnv, dbName, tbName, whereCondition);
        String code = result.getCode();
        if ("1".equals(code)) {
            System.out.println(result.getErrorMsg());
        }
    }

    @Test
    public void getQueryResultTestWithErrorData() {
        String dbEnv = "tst";
        String dbName = "policy";
        String tbName = "policy";
        String whereCondition = "policy_no='123'";
        BaseSqlQueryResult result = IDBQueryResults.getQueryResult(dbEnv, dbName, tbName, whereCondition);
        String code = result.getCode();
        if ("2".equals(code)) {
            System.out.println(result.getErrorMsg());
        }
    }
}

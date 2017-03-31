package com.za.qa.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.za.qa.bo.BaseSqlQueryResult;
import com.za.qa.bo.SqlQueryResultContent;
import com.za.qa.http.HttpConstant;
import com.zhongan.qa.http.HttpUrlConstructor;
import com.zhongan.qa.utils.HttpClientUtils;
import com.zhongan.qa.utils.HttpResponseUtils;

public class IDBQueryResults {

    private static String IDB_URL    = "http://10.139.1.71:9999";
    private static String QUERY_NODE = "/getqueryrst2";

    /**
     * @param dbEnv tst/pre
     * @param dbName 库名
     * @param tbName 表名
     * @param whereCondition where后面的查询条件
     * @return 返回BaseSqlQueryResult对象，先判断code再取结果，0成功，1查询条件错，2未查到结果
     */
    public static BaseSqlQueryResult getQueryResult(String dbEnv, String dbName, String tbName, String whereCondition) {
        String url = HttpUrlConstructor.urlBuild(IDB_URL, QUERY_NODE, null);
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("dbEnv", dbEnv);
        parameterMap.put("dbName", dbName);
        parameterMap.put("tbName", tbName);
        parameterMap.put("whereCondition", whereCondition);
        String requestJson = JSON.toJSONString(parameterMap);
        CloseableHttpResponse response = HttpClientUtils.testWithJson(null, null, url, HttpConstant.HTTPPOST,
                requestJson);
        String result = HttpResponseUtils.getResponseContent(response);
        return dealIDBResult(result);
    }

    @SuppressWarnings("unchecked")
    private static BaseSqlQueryResult dealIDBResult(String input) {
        JSONObject originalResult = JSON.parseObject(input);
        BaseSqlQueryResult result = new BaseSqlQueryResult();
        if (originalResult.isEmpty()) {
            result.setCode("1");
            result.setErrorMsg("错误的查询，请检查查询条件！");
        } else {
            List<SqlQueryResultContent> contentList = new ArrayList<SqlQueryResultContent>();
            for (Map.Entry<String, Object> entry : originalResult.entrySet()) {
                List<JSONObject> resultList = (List<JSONObject>) entry.getValue();
                if (!resultList.isEmpty()) {
                    SqlQueryResultContent contentDetail = new SqlQueryResultContent();
                    contentDetail.setDbName(entry.getKey());
                    contentDetail.setResultContent(resultList);
                    if ("No Row Found.".equals(resultList.get(0).getString("query result"))) {
                        break;
                    }
                    contentList.add(contentDetail);
                }
            }
            if (contentList.isEmpty()) {
                result.setCode("2");
                result.setErrorMsg("指定条件未查询到结果！");
            } else {
                result.setCode("0");
                result.setErrorMsg(null);
                result.setContentList(contentList);
            }
        }
        return result;
    }
}

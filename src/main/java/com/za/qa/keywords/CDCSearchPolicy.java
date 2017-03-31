package com.za.qa.keywords;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.alibaba.fastjson.JSON;
import com.za.qa.http.HttpConstant;
import com.zhongan.core.cdc.dto.Options;
import com.zhongan.core.cdc.dto.PolicyCondition;
import com.zhongan.qa.http.HttpUrlConstructor;
import com.zhongan.qa.utils.HttpClientUtils;
import com.zhongan.qa.utils.HttpResponseUtils;

public class CDCSearchPolicy {

    private String serverUrl;

    public CDCSearchPolicy(String env) {
        if ("iTest".equals(env)) {
            serverUrl = HttpConstant.CDC_ITEST_POLICY;
        }
        if ("uat".equals(env)) {
            serverUrl = HttpConstant.CDC_UAT_POLICY;
        }
    }

    public String queryPolicyList(String polno, String field) throws Exception {
        String url = HttpUrlConstructor.urlBuild(serverUrl,
                "/com.zhongan.core.cdc.service.PolicyQueryService:1.0.0/queryPolicyList", null);
        String[] argTypes = new String[] { "com.zhongan.core.cdc.dto.PolicyCondition",
                "com.zhongan.core.cdc.dto.Options" };
        PolicyCondition paramPolicyCondition = new PolicyCondition();
        Options paramOption = new Options();
        if (field.equals("applyNo")) {
            paramPolicyCondition.setApplyNo(polno);
        } else if (field.equals("policyNo")) {
            paramPolicyCondition.setPolicyNo(polno);
        } else if (field.equals("policyId")) {
            long value = Long.valueOf(polno);
            paramPolicyCondition.setPolicyId(value);
        }
        Object[] argObjects = new Object[] { paramPolicyCondition, paramOption };
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("ArgTypes", JSON.toJSONString(argTypes));
        parameterMap.put("ArgsObjects", JSON.toJSONString(argObjects));
        CloseableHttpResponse response = HttpClientUtils.testHSF(null, null, url, HttpConstant.HTTPPOST, JSON.toJSONString(argTypes),JSON.toJSONString(argObjects));
        String result = HttpResponseUtils.getResponseContent(response);
        System.out.println(result);
        return result;
    }

}

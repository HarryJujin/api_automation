package com.za.qa.keywords;
import com.googlecode.aviator.runtime.function.AbstractFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/13.
 */
public abstract class ExpressionRegister extends AbstractFunction {
    private static Map<String,Object> payloadEnv = new HashMap<String, Object>();
    private static Map<String,Object> responseEnv = new HashMap<String, Object>();
    private static Map<String,Object> sqlSelectEnv = new HashMap<String, Object>();
    public static Map<String,Map<String,Map<String,List<String>>>> sqlExpectEnv = new HashMap<String,Map<String,Map<String,List<String>>>>();

    private static Map<String,Map<String,Object>> n_payloadEnv = new HashMap<String,Map<String,Object>>();
    private static Map<String,Map<String,Object>> n_responseEnv = new HashMap<String,Map<String,Object>>();
    

    public static void setPayloadEnv(String key, String value) {
        payloadEnv.put(key,value);
    }
    public static void setResponseEnv(String key, String value) {
        responseEnv.put(key,value);
    }
    public static void setSqlSelectEnv(String key, String value) {
    	sqlSelectEnv.put(key,value);
    }
    public static Map<String, Object> getPayloadEnv() {
        return payloadEnv;
    }
    public static Map<String, Object> getResponseEnv() {
        return responseEnv;
    }
    public static Map<String, Object> getSqlSelectEnv() {
        return sqlSelectEnv;
    }
}

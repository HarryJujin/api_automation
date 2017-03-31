package com.za.qa.keywords;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.za.qa.http.HttpConstant;
import com.zhongan.qa.http.HttpUrlConstructor;
import com.zhongan.qa.utils.CookieUtils;
import com.zhongan.qa.utils.HttpClientUtils;
import com.zhongan.qa.utils.HttpResponseUtils;

public class CommonSSOLogin {

    private static String getlt() {
        Map<String, String> urlParameterMap = new HashMap<String, String>();
        urlParameterMap.put("service", HttpConstant.NSSO_SERVICENAME);
        urlParameterMap.put("target", "test");
        String url = HttpUrlConstructor.urlBuild(HttpConstant.NSSO_URL, "/login", urlParameterMap);
        try {
            Document doc = Jsoup.connect(url).timeout(10000).get();
            Elements eles = doc.select("input[name=\"lt\"]");
            String lt = eles.first().attr("value");
            return lt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//http://nsso.zhonganonline.com/login?service=za-artemis-bops&target=test
    private static String getTicket(String userName, String passWord) {
        Map<String, String> urlParameterMap = new HashMap<String, String>();
        urlParameterMap.put("service", HttpConstant.NSSO_SERVICENAME);
        urlParameterMap.put("target", "test");
        String url = HttpUrlConstructor.urlBuild(HttpConstant.NSSO_URL, "/login", urlParameterMap);
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("username", userName);
        parameterMap.put("password", passWord);
        parameterMap.put("lt", getlt());
        CloseableHttpResponse response = HttpClientUtils.testWithKeyValue(null, null, url, HttpConstant.HTTPPOST,
                parameterMap);
        String ticket = response.getFirstHeader("Location").getValue().split("ticket=")[1];
        try {
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public static String requestCookie(String userName, String passWord) {
        Map<String, String> urlParameterMap = new HashMap<String, String>();
        urlParameterMap.put("service", HttpConstant.NSSO_SERVICENAME);
        urlParameterMap.put("ticket", getTicket(userName, passWord));
        String url = HttpUrlConstructor.urlBuild(HttpConstant.NSSO_URL, "/validate", urlParameterMap);

        CloseableHttpResponse response = HttpClientUtils.testWithKeyValue(null, null, url, HttpConstant.HTTPGET, null);
        try {
            String result = HttpResponseUtils.getResponseContent(response);
            CookieUtils cookieUtil = CookieUtils.getInstance(HttpConstant.ARTEMISSECRETYKEY);
            return cookieUtil.encryptCookie(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    

}

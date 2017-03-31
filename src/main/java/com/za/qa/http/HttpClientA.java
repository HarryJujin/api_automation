package com.za.qa.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import com.alibaba.fastjson.JSONObject;
import com.za.qa.log.LogMan;
import com.zhongan.qa.http.HttpClientBase;
import com.zhongan.qa.utils.HttpResponseUtils;

public class HttpClientA {

    private static HttpClient client;
    static {
        //每主机最大连接数
        //Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
        //Protocol.registerProtocol("https", myhttps);
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        client = new HttpClient(connectionManager);
        //client = new HttpClient();
        client.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(500);
        //总最大连接数
        client.getHttpConnectionManager().getParams().setMaxTotalConnections(500);
        //超时时间 30sec
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        client.getHttpConnectionManager().getParams().setSoTimeout(30000);

    }

    public static HttpClient getHttpClient() {
        return client;
    }

    /**
     * 用法： HttpRequestProxy hrp = new HttpRequestProxy();
     * hrp.doRequest("http://www.163.com",null,null,"utf-8");
     * 
     * @param url 请求的资源ＵＲＬ
     * @param postData POST请求时form表单封装的数据 没有时传null
     * @param header request请求时附带的头信息(header) 没有时传null
     * @param encoding response返回的信息编码格式 没有时传null
     * @return response返回的文本数据
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static String doRequest(String url, Map postData, Map header, String encoding) throws Exception {
        String responseString = null;
        //头部请求信息  
        Header[] headers = initHeader(header);
        if (postData != null) {
            //post方式请求
            responseString = executePost(url, postData, encoding, headers);
        } else {
            //get方式 请求
            if ("https".equals(url.substring(0, 5))) {
                responseString = executeGets(url);

            } else {
                responseString = executeGet(url, encoding, headers);
            }
        }
        return responseString;
    }

    //get方式 请求
    public static String executeGet(String url, String encoding, Header[] headers) throws Exception {
        String responseString = "";
        GetMethod getRequest = new GetMethod(url.trim());
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                getRequest.setRequestHeader(headers[i]);
            }
        }
        try {
            // logger.info("BexecuteGet=" + url + ", object headers:"
            //+ ToStringBuilder.reflectionToString(headers, ToStringStyle.SHORT_PREFIX_STYLE).toString());
            responseString = executeMethod(getRequest, encoding);
            System.out.println(responseString);
            // logger.info("BexecuteGet=" + url + ", object headers:"
            //+ ToStringBuilder.reflectionToString(headers, ToStringStyle.SHORT_PREFIX_STYLE).toString());
        } catch (Exception e) {
            //logger.warn("executeGet error" + url, e);
            throw e;
        } finally {
            getRequest.releaseConnection();
        }

        return responseString;
    }

    //get请求https
    public static String executeGets(String url) {

        String responseString = "";
        try {
            HttpClientBase httpBase = new HttpClientBase();
            CloseableHttpClient client = httpBase.initHttpsClient(null, null);
            CloseableHttpResponse response = HttpResponseUtils.getResponseByJsonParameter(client, url,
                    HttpConstant.HTTPGET, null, null);
            responseString = HttpResponseUtils.getResponseContent(response);
            //responseString = new String(HttpsClientGet.getMethod(url));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return responseString;
    }

    //post方式请求
    private static String executePost(String url, @SuppressWarnings("rawtypes") Map postData, String encoding,
                                      Header[] headers) throws Exception {
        String responseString = "";
        PostMethod postRequest = new PostMethod(url.trim());
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                postRequest.setRequestHeader(headers[i]);
            }
        }
        @SuppressWarnings("rawtypes")
        Set entrySet = postData.entrySet();
        int dataLength = entrySet.size();
        NameValuePair[] params = new NameValuePair[dataLength];
        int i = 0;
        for (@SuppressWarnings("rawtypes")
        Iterator itor = entrySet.iterator(); itor.hasNext();) {
            @SuppressWarnings("rawtypes")
            Map.Entry entry = (Map.Entry) itor.next();
            params[i++] = new NameValuePair(entry.getKey().toString(), entry.getValue().toString());
        }
        postRequest.setRequestBody(params);
        try {
            //logger.info("BexecutePost=" + url + ", object postData:"
            //+ ToStringBuilder.reflectionToString(params, ToStringStyle.SHORT_PREFIX_STYLE).toString());
            responseString = executeMethod(postRequest, encoding);
            //  System.out.println(responseString);
            //logger.info("EexecutePost=" + url + ", object postData:"
            //+ ToStringBuilder.reflectionToString(params, ToStringStyle.SHORT_PREFIX_STYLE).toString());
        } catch (Exception e) {
            throw e;
        } finally {
            postRequest.releaseConnection();
        }
        return responseString;
    }

    //请求头部信息
    @SuppressWarnings("rawtypes")
    private static Header[] initHeader(Map header) {
        Header[] headers = null;
        if (header != null) {
            Set entrySet = header.entrySet();
            int dataLength = entrySet.size();
            headers = new Header[dataLength];
            int i = 0;
            for (Iterator itor = entrySet.iterator(); itor.hasNext();) {
                Map.Entry entry = (Map.Entry) itor.next();
                headers[i++] = new Header(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        return headers;
    }

    //调用并获取返回
    private static String executeMethod(HttpMethod request, String encoding) throws Exception {
        String responseContent = null;
        InputStream responseStream = null;
        BufferedReader rd = null;
        try {
            Long start = System.currentTimeMillis();
            //logger.info("B. request param:"
            // + ToStringBuilder.reflectionToString(request, ToStringStyle.SHORT_PREFIX_STYLE).toString());
            request.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
            getHttpClient().executeMethod(request);

            // logger.info("E. cost " + (System.currentTimeMillis() - start) + "msec, request param:"
            //+ ToStringBuilder.reflectionToString(request, ToStringStyle.SHORT_PREFIX_STYLE).toString());
            if (encoding != null) {
                responseStream = request.getResponseBodyAsStream();
                rd = new BufferedReader(new InputStreamReader(responseStream, encoding));
                String tempLine = rd.readLine();
                StringBuffer tempStr = new StringBuffer();
                String crlf = System.getProperty("line.separator");
                while (tempLine != null) {
                    tempStr.append(tempLine);
                    tempStr.append(crlf);
                    tempLine = rd.readLine();
                }
                responseContent = tempStr.toString();
            } else {
                responseContent = request.getResponseBodyAsString();
            }
            Header locationHeader = request.getResponseHeader("location");
            //返回代码为302,301时，表示页面己经重定向，则重新请求location的url，这在  
            //一些登录授权取cookie时很重要
            //如果需要处理重定向请求，请在下面代码中改造
            if (locationHeader != null) {
                //String redirectUrl = locationHeader.getValue();
                //doRequest(redirectUrl, null, null, null);
            }
        } catch (HttpException e) {
            throw e;
        } catch (IOException e) {
            throw e;

        } finally {
            if (rd != null)
                try {
                    rd.close();
                } catch (IOException e) {
                    throw e;
                }
            if (responseStream != null)
                try {
                    responseStream.close();
                } catch (IOException e) {
                    throw e;

                }
            request.releaseConnection();
        }
        return responseContent;
    }

    public static String executeMethod(HttpMethod request, String encoding, HostConfiguration hostConfiguration)
            throws Exception {
        String responseContent = null;
        InputStream responseStream = null;
        BufferedReader rd = null;
        try {
            Long start = System.currentTimeMillis();
            //logger.info("B. request param:"
            // + ToStringBuilder.reflectionToString(request, ToStringStyle.SHORT_PREFIX_STYLE).toString());
            request.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
            getHttpClient().executeMethod(hostConfiguration, request);
            // logger.info("E. cost " + (System.currentTimeMillis() - start) + "msec, request param:"
            //+ ToStringBuilder.reflectionToString(request, ToStringStyle.SHORT_PREFIX_STYLE).toString());
            if (encoding != null) {
                responseStream = request.getResponseBodyAsStream();
                rd = new BufferedReader(new InputStreamReader(responseStream, encoding));
                String tempLine = rd.readLine();
                StringBuffer tempStr = new StringBuffer();
                String crlf = System.getProperty("line.separator");
                while (tempLine != null) {
                    tempStr.append(tempLine);
                    tempStr.append(crlf);
                    tempLine = rd.readLine();
                }
                responseContent = tempStr.toString();
            } else {
                responseContent = request.getResponseBodyAsString();
            }
            Header locationHeader = request.getResponseHeader("location");
            //返回代码为302,301时，表示页面己经重定向，则重新请求location的url，这在  
            //一些登录授权取cookie时很重要
            //如果需要处理重定向请求，请在下面代码中改造
            if (locationHeader != null) {
                //String redirectUrl = locationHeader.getValue();
                //doRequest(redirectUrl, null, null, null);
            }
        } catch (HttpException e) {
            throw e;
        } catch (IOException e) {
            throw e;

        } finally {
            if (rd != null)
                try {
                    rd.close();
                } catch (IOException e) {
                    throw e;
                }
            if (responseStream != null)
                try {
                    responseStream.close();
                } catch (IOException e) {
                    throw e;

                }
        }
        return responseContent;
    }

    /**
     * 特殊请求数据,这样的请求往往会出现redirect本身而出现递归死循环重定向 所以单独写成一个请求方法
     * 比如现在请求的url为：http://localhost:8080/demo/index.jsp 返回代码为302
     * 头部信息中location值为:http://localhost:8083/demo/index.jsp
     * 这时httpclient认为进入递归死循环重定向，抛出CircularRedirectException异常
     * 
     * @param url
     * @return
     * @throws Exception
     */
    public String doSpecialRequest(String url, int count, String encoding) throws Exception {
        String str = null;
        InputStream responseStream = null;
        BufferedReader rd = null;
        GetMethod getRequest = new GetMethod(url);
        //关闭httpclient自动重定向动能  
        getRequest.setFollowRedirects(false);
        try {

            client.executeMethod(getRequest);
            Header header = getRequest.getResponseHeader("location");
            if (header != null) {
                //请求重定向后的ＵＲＬ，count同时加1  
                this.doSpecialRequest(header.getValue(), count + 1, encoding);
            }
            //这里用count作为标志位，当count为0时才返回请求的ＵＲＬ文本,  
            //这样就可以忽略所有的递归重定向时返回文本流操作，提高性能  
            if (count == 0) {
                getRequest = new GetMethod(url);
                getRequest.setFollowRedirects(false);
                client.executeMethod(getRequest);
                responseStream = getRequest.getResponseBodyAsStream();
                rd = new BufferedReader(new InputStreamReader(responseStream, encoding));
                String tempLine = rd.readLine();
                StringBuilder tempStr = new StringBuilder();
                String crlf = System.getProperty("line.separator");
                while (tempLine != null) {
                    tempStr.append(tempLine);
                    tempStr.append(crlf);
                    tempLine = rd.readLine();
                }
                str = tempStr.toString();
            }

        } catch (HttpException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            getRequest.releaseConnection();
            if (rd != null)
                try {
                    rd.close();
                } catch (IOException e) {
                    throw e;
                }
            if (responseStream != null)
                try {
                    responseStream.close();
                } catch (IOException e) {
                    throw e;
                }
        }
        return str;
    }

    /**
     * 发送xml数据请求到server端
     * 
     * @param url xml请求数据地址
     * @param xmlString 发送的xml数据流
     * @return null发送失败，否则返回响应内容
     */
    @SuppressWarnings("deprecation")
    public static String postXml(String url, String xmlString, String charset) {
        //关闭   
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "stdout");

        //创建httpclient工具对象   
        HttpClient client = new HttpClient();
        //创建post请求方法   
        PostMethod myPost = new PostMethod(url);
        //设置请求超时时间   
        client.setConnectionTimeout(300 * 1000);
        String responseString = null;
        try {
            //设置请求头部类型   
            myPost.setRequestHeader("Content-Type", "text/xml");
            myPost.setRequestHeader("charset", charset);
            //设置请求体，即xml文本内容，注：这里写了两种方式，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式   
            myPost.setRequestBody(xmlString);

            //            InputStream body=this.getClass().getResourceAsStream("/"+xmlFileName);  
            //            myPost.setRequestBody(body);  
            myPost.setRequestEntity(new StringRequestEntity(xmlString, "text/xml", charset));
            int statusCode = client.executeMethod(myPost);
            if (statusCode == HttpStatus.SC_OK) {
                BufferedInputStream bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
                byte[] bytes = new byte[1024];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int count = 0;
                while ((count = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, count);
                }
                byte[] strByte = bos.toByteArray();
                responseString = new String(strByte, 0, strByte.length, charset);
                bos.close();
                bis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myPost.releaseConnection();
        return responseString;
    }

    public static String postJson(String url, String json) {
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);
        //处理headerStr，method设置header
        if (json.contains("|")) {
        	json = json.replace("\n", "");
            String header = json.split("\\|")[0];
            json = json.split("\\|")[1];
    		if(header.length()>0){
                String[] headerArray = header.split("&");
                for (String str : headerArray) {
                    if (str.indexOf("=") > -1) {
                        method.setRequestHeader(str.split("=")[0], str.split("=")[1]);
                    }
                }
    		}
        }
        
        try {
            if (json != null && !json.trim().equals("")) {
                RequestEntity requestEntity = new StringRequestEntity(json, "application/json", "UTF-8");
                method.setRequestEntity(requestEntity);
            }
            method.releaseConnection();
            httpClient.executeMethod(method);
            String responses = method.getResponseBodyAsString();
            return responses;
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * @Test public void test(){ String jsondata =
     * "{\"productCode\": \"ff633ad8736f6cf5b50ea190394ca91225d6b9e06c\"," +
     * "\"channelOrderNo\": \"4565465743\"," + "\"policyHolderType\": \"1\"," +
     * "\"policyHolderUserName\": \"zhangsan\"," +
     * "\"policyHolderCertiType\": \"P\"," +
     * "\"policyHolderCertiNo\": \"A0000001\"," +
     * "\"policyHolderGender\": \"M\"," +
     * "\"policyHolderBirthDate\": \"19980101\"," +
     * "\"policyHolderPhone\": \"18621770000\"," + "\"insuredList\":    [" + "{"
     * + "\"insuredUserName\": \"zhangsan\"," + "\"insuredCertiType\": \"P\"," +
     * "\"insuredCertiNo\": 222222," + "\"insuredBirthday\": \"20160101\"," +
     * "\"insuredPhone\": \"18007320000\"," + "\"insuredGender\": \"M\"," +
     * "\"premium\": 40" + "}," + "{" + "\"insuredUserName\": \"lisi\"," +
     * "\"insuredCertiType\": \"P\"," + "\"insuredCertiNo\": 333333," +
     * "\"insuredBirthday\": \"19911010\"," +
     * "\"insuredPhone\": \"18007320000\"," + "\"insuredGender\": \"F\"," +
     * "\"premium\": 40" + "}" + "]," + "\"insureDate\": \"20161023100101\"," +
     * "\"travelStartDate\": \"20161025080101\"," + "\"travelDay\": \"3\"," +
     * "\"destination\": \"\"," + "\"flightDate\": \"\"," +
     * "\"flightNo\": \"\"," + "\"destinationCode\": \"\"," +
     * "\"ifGenerateSubEle\": \"Y\"," + "\"requireInvoice\": \"N\"," +
     * "\"contactMail\": \"\"," + "\"extraInfo\":    {" +
     * "\"orderUserId\": \"1111\"," + "\"hasSpecifiedPolicyHolder\": \"N\"" +
     * "}" + "}"; String url=
     * "http://10.253.8.103:7080/handle.do?serviceName=zhongan.open.comm.galatea.insure.uw"
     * ; System.out.println(postJson(url,jsondata)); }
     */

    public static String hsfHttpPost(String hsfurl, String postType, String postDataStr) {
        //调用接口  MessageSendService.sendWeChat(String weChatInfo) 
        Map<Object, Object> postData = new HashMap<Object, Object>();
        String ArgsTypesreplace = postType.replace("\"", "").replace("[", "").replace("]", "");
        String ArgsObjectsreplace = postDataStr.substring(1, postDataStr.length() - 1);
        Object[] type = ArgsTypesreplace.split(",");
        String postDataStr1 = "[" + ArgsObjectsreplace + "]";
        //postDataStr1=postDataStr1.replaceAll("\\s","");

        postDataStr1 = postDataStr1.replaceAll("\\n", "");

        postData.put("ArgsObjects", postDataStr1);
        postData.put("ArgsTypes", JSONObject.toJSONString(type));

        String hsfHttpResponse;
        try {

            System.out.println(postData);
            hsfHttpResponse = doRequest(hsfurl, postData, null, "utf-8");
            //hsfHttpResponse=hsfHttpResponse.replace("\\s","");
            hsfHttpResponse = hsfHttpResponse.replace("\\", "");
            hsfHttpResponse = hsfHttpResponse.replace("\"{", "{");
            hsfHttpResponse = hsfHttpResponse.replace("}\"", "}");

            return hsfHttpResponse;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            String mymessage = "error";
            return mymessage;
        }

    }

    public static String postForm(String url, String formData, String charSet) {
        String response = "";
        Map<String, String> headerMap = new HashMap<String, String>();
        Map<Object, Object> bodyMap = new HashMap<Object, Object>();
        //包含|的使用post
        if (formData.contains("|")) {
            formData = formData.replace("\n", "");
            String []formDataArray = formData.split("\\|");
            String header = formDataArray[0];
            String[] headerArray = header.split("&");

            for (String str : headerArray) {
                if (str.indexOf("=") > -1) {
                	String key = str.split("=")[0] ;
                	String value = str.substring(key.length()+1) ;
                    headerMap.put(key, value);
                }
            }
            if(formDataArray.length>1){
                String body = formDataArray[1];
                String[] bodyArray = body.split("&");
                for (String str : bodyArray) {
                    if (str.split("=").length > 1) {
                        bodyMap.put(str.split("=")[0], str.split("=")[1]);
                    } else {
                        bodyMap.put(str.split("=")[0], "");
                    }
                }
            }
        }
        //不包含|使用get
        else if (!formData.contains("|") && formData.length() > 0) {
            bodyMap = null;
            headerMap = null;
            formData = formData.replace("\n", "");
            //兼容“？”号数据：输入不输入都可以
            if (formData.substring(0, 1).equals("?")) {
                formData = formData.substring(1);
            }
            String newFormData = "";
            if (formData.length() > 0 && formData.contains("=")) {
                String[] parameterArray = formData.split("&");
                if (parameterArray.length > 1) {
                    for (String str : parameterArray) {
                        String[] array = str.split("=");
                        if (array.length == 2) {
                            try {
                                array[1] = java.net.URLEncoder.encode(array[1], charSet);
                            } catch (UnsupportedEncodingException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        newFormData = newFormData + array[0] + "=" + array[1] + "&";
                    }
                    newFormData = newFormData.substring(0, newFormData.length());
                } else {
                    String[] array = formData.split("=");
                    try {
                        array[1] = java.net.URLEncoder.encode(array[1], charSet);
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    newFormData = array[0] + "=" + array[1];
                }
                url = url + "?" + newFormData;
            }
        }
        //postDataStr为空
        else {
            bodyMap = null;
            headerMap = null;
        }

        try {
            LogMan.getLoger().info("请求地址：" + url);
            LogMan.getLoger().info("字符编码：" + charSet);
            response = HttpClientA.doRequest(url, bodyMap, headerMap, charSet);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response;
    }

    public static String hsfHttpPost(String hsfurl, List<String> listKey, List<String> listVal) {
        System.out.println(121111);
        //调用接口  MessageSendService.sendWeChat(String weChatInfo) 
        Map<String, String> postData = new HashMap<String, String>();
        String testKey = "";
        String testVal = "";
        for (int i = 0; i < listKey.size(); i++) {
            testKey = testKey + "\"" + listKey.get(i) + "\"" + ",";

            if (listVal.get(i).toString().indexOf("{") < 0) {
                testVal = testVal + "\"" + "\\\"" + listVal.get(i) + "\\\"" + "\"" + ",";
            } else {
                testVal = testVal + listVal.get(i) + ",";
            }
        }
        testKey = testKey.substring(0, testKey.length() - 1);
        testKey = "[" + testKey + "]";
        testVal = testVal.substring(0, testVal.length() - 1);
        testVal = "[" + testVal + "]";

        postData.put("ArgsObjects", testVal);
        postData.put("ArgsTypes", testKey);
        System.out.println(testKey);
        System.out.println(testVal);

        System.out.println("-------------------------------------------------------");
        String hsfHttpResponse;
        try {
            //System.out.println(postData); 
            hsfHttpResponse = doRequest(hsfurl, postData, null, "utf-8");
            //hsfHttpResponse=hsfHttpResponse.replace("\\s","");
            hsfHttpResponse = hsfHttpResponse.replace("\\", "");
            hsfHttpResponse = hsfHttpResponse.replace("\"{", "{");
            hsfHttpResponse = hsfHttpResponse.replace("}\"", "}");
            return hsfHttpResponse;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            String mymessage = "error";
            return mymessage;
        }

    }

}

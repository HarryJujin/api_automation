package com.za.qa.keywords;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.alibaba.fastjson.JSON;
import com.za.qa.http.HttpConstant;
import com.za.qa.log.LogMan;
import com.za.qa.verify.ResponseVerify;
import com.zhongan.qa.http.HttpUrlConstructor;
import com.zhongan.qa.utils.HttpClientUtils;
import com.zhongan.qa.utils.HttpResponseUtils;

public class MockPay {

    public static final String UAT       = "http://10.253.27.175:8086/";
    public static final String ITEST     = "http://10.139.58.193:56643/";
    private String             serverUrl = ITEST;

    /**
     * 生成支付二维码
     * 
     * @param outTradeNo 交易订单号
     * @param amt 交易金额
     * @return 调用HSF接口，返回zaOrderNo等
     * @throws Exception
     */
    public String addMerchantOrder(String outTradeNo, String amt) throws Exception {
        String url = HttpUrlConstructor.urlBuild(serverUrl,
                "com.zhongan.cashier.service.CashierOrderService:1.0.0/addMerchantOrder", null);
        String[] argTypes = new String[] { "com.zhongan.cashier.dto.AddCashierOrderRequest" };
        Map<String, String> orderInfo = new HashMap<String, String>();
        orderInfo.put("产品明证", "autotest");
        orderInfo.put("保障期限", "autotest");
        orderInfo.put("保费", amt);
        Map<String, Object> payInfo = new HashMap<String, Object>();
        payInfo.put("outTradeNo", outTradeNo);
        payInfo.put("merchantCode", "1512000401");
        payInfo.put("amt", amt);
        payInfo.put("expireTime", "60");
        payInfo.put("scrType", "1");
        payInfo.put("payChannel", "wxpay");
        payInfo.put("orderType", "1");
        payInfo.put("subject", "团险保费");
        payInfo.put("body", "自动化测试");
        payInfo.put("showUrl", "https://www.zhongan.com");
        payInfo.put("notifyUrl", "http://openweb.daily.zhongan.com/api/cashierTestToolAction/merchantAccept.json");
        payInfo.put("errorNotifyUrl", "");
        payInfo.put("returnUrl", "https://www.zhongan.com");
        payInfo.put("backUrl", "https://www.zhongan.com");
        payInfo.put("notifyInfo", orderInfo);
        payInfo.put("orderInfo", orderInfo);
        Object[] argObjects = new Object[] { payInfo };
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("ArgTypes", JSON.toJSONString(argTypes));
        parameterMap.put("ArgsObjects", JSON.toJSONString(argObjects));
        CloseableHttpResponse response = HttpClientUtils.testHSF(null, null, url, HttpConstant.HTTPPOST, JSON.toJSONString(argTypes),JSON.toJSONString(argObjects));
        String result = HttpResponseUtils.getResponseContent(response);
        System.out.println(result);
        return result;
    }

    /**
     * 补充交易详细信息
     * 
     * @param outTradeNo 交易订单号
     * @param zaOrderNo 生成二维码接口中返回的订单号
     * @param amt 交易金额
     * @return
     * @throws Exception
     */
    public String addPayDetail(String outTradeNo, String zaOrderNo, String amt) throws Exception {
        String url = HttpUrlConstructor.urlBuild(serverUrl,
                "com.zhongan.cashier.service.CashierOrderService:1.0.0/addPayDetail", null);
        String[] argTypes = new String[] { "com.zhongan.cashier.dto.AddPayDetailRequest" };
        Map<String, String> payRequstInfo = new HashMap<String, String>();
        payRequstInfo.put("out_trade_no", outTradeNo);
        Map<String, Object> payInfo = new HashMap<String, Object>();
        payInfo.put("outTradeNo", outTradeNo);
        payInfo.put("zaOrderNo", zaOrderNo);
        payInfo.put("payChannelUserNo", "DIDI015");
        payInfo.put("payChannelId", "2");
        payInfo.put("payAccountId", "90001");
        payInfo.put("payAmount", amt);
        payInfo.put("payRequstInfo", payRequstInfo);
        Object[] argObjects = new Object[] { payInfo };
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("ArgTypes", JSON.toJSONString(argTypes));
        parameterMap.put("ArgsObjects", JSON.toJSONString(argObjects));
        CloseableHttpResponse response = HttpClientUtils.testHSF(null, null, url, HttpConstant.HTTPPOST, JSON.toJSONString(argTypes),JSON.toJSONString(argObjects));
        String result = HttpResponseUtils.getResponseContent(response);
        System.out.println(result);
        return result;
    }

    /**
     * 支付确认接口
     * 
     * @param zaOrderNo
     * @return 支付结果
     * @throws Exception
     */
    public String confirmSpecialOrderPay(String zaOrderNo) throws Exception {// 确认支付
        Object[] o = { new KeywordDefinition() };
        ExpressionEngine engine = new ExpressionEngine(o);
        String rNum = (String) engine.execute("RNum(\"13\")");
        String url = HttpUrlConstructor.urlBuild(serverUrl,
                "com.zhongan.cashier.service.CashierOrderService:1.0.0/confirmSpecialOrderPay", null);
        String[] argTypes = new String[] { "com.zhongan.cashier.dto.ConfirmOrderPayRequest" };
        Map<String, Object> payInfo = new HashMap<String, Object>();
        payInfo.put("zaOrderNo", zaOrderNo);
        payInfo.put("payTradeNo", rNum);
        payInfo.put("payChannelUserNo", "DIDI015");
        payInfo.put("payChannel", "wxpay");
        Object[] argObjects = new Object[] { payInfo };
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("ArgTypes", JSON.toJSONString(argTypes));
        parameterMap.put("ArgsObjects", JSON.toJSONString(argObjects));
        CloseableHttpResponse response = HttpClientUtils.testHSF(null, null, url, HttpConstant.HTTPPOST, JSON.toJSONString(argTypes),JSON.toJSONString(argObjects));
        String result = HttpResponseUtils.getResponseContent(response);
        System.out.println(result);
        return result;
    }

    /**
     * @param env uat or itest
     * @param outTradeNo 交易订单号
     * @param amt 交易金额
     * @return zaOrderNo
     * @throws Exception
     */
    public String pay(String env, String outTradeNo, String amt) throws Exception {
        if ("uat".equals(env)) {
            serverUrl = UAT;
        }
        String orderInfo = addMerchantOrder(outTradeNo, amt);
        String zaOrderNo = "";
        if (JSON.parseObject(orderInfo).containsKey("zaOrderNo")) {
            zaOrderNo = JSON.parseObject(orderInfo).getString("zaOrderNo");
        } else {
            throw new Exception("支付创建二维码[addMerchantOrder]失败");
        }
        String orderDetails = addPayDetail(outTradeNo, zaOrderNo, amt);
        LogMan.getLoger().info("支付详细信息：" + orderDetails);
        String payResult = confirmSpecialOrderPay(zaOrderNo);
        String getZaOrderNo = ResponseVerify.getTestValue("zaOrderNo", payResult);
        return getZaOrderNo;
    }
}

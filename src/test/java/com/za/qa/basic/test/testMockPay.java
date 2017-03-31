package com.za.qa.basic.test;



import org.junit.Test;

import com.za.qa.keywords.MockPay;

public class testMockPay {

    @Test
    public void testAddMerchantOrder() {
        MockPay mockPay = new MockPay();
        try {
            mockPay.addMerchantOrder("5849513374", "60.00");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddPayDetail() {
        MockPay mockPay = new MockPay();
        try {
            mockPay.addPayDetail("5849513374", "90151200040120161102105410000520", "60.00");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConfirmSpecialOrderPay() {
        MockPay mockPay = new MockPay();
        try {
            mockPay.confirmSpecialOrderPay("90151200040120161102105410000520");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPay() {
        MockPay mockPay = new MockPay();
        try {
            mockPay.pay("itest", "7979143631", "60.00");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

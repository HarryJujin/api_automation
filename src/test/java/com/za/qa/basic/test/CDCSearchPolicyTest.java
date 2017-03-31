package com.za.qa.basic.test;

import org.junit.Test;

import com.za.qa.keywords.CDCSearchPolicy;

public class CDCSearchPolicyTest {

    @Test
    public void queryPolicyListTest() {
        try {
            CDCSearchPolicy searchPolicy = new CDCSearchPolicy("iTest");
            searchPolicy.queryPolicyList("ha0100003712001222","applyNo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

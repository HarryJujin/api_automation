package com.za.qa.dto;

import java.io.Serializable;

/**
 * @author jujinxin
 * @version 创建时间：2016年11月29日 下午2:02:13 类说明
 */
public class CaseSuiteDTO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String            apiNo;                //接口编号
    private int               i;                    //集合用例

    public String getApiNo() {
        return apiNo;
    }

    public void setApiNo(String apiNo) {
        this.apiNo = apiNo;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "CaseSuiteDTO [apiNo=" + apiNo + ", i=" + i + "]";
    }

}

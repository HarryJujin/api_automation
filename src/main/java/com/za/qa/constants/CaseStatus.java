package com.za.qa.constants;

public enum CaseStatus {

    PASS("Pass", "通过"),
    FAIL("Fail", "失败"),
    SKIP("Skip", "跳过"),
    NOTRUN("NotRun", "未运行");

    private String code;
    private String name;

    private CaseStatus(String code, String name) {
        this.setCode(code);
        this.setName(name);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

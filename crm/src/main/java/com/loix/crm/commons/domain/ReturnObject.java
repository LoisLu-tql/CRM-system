package com.loix.crm.commons.domain;

public class ReturnObject {
    private String code;    // 1 success  0 fail
    private String message; // warning
    private Object retData; // Other

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}

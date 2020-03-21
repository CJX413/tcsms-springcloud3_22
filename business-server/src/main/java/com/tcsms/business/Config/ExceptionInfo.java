package com.tcsms.business.Config;

/**
 * 所有400-500间的代码表示业务系统异常码！
 */
public enum ExceptionInfo {

    ;
    private int code;
    private String msg;

    ExceptionInfo(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return this.code;
    }
}

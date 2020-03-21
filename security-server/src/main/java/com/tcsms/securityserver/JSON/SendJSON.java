package com.tcsms.securityserver.JSON;

public class SendJSON {
    private Integer code;
    private String message;
    private Object data;

    public SendJSON(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public String getMassege() {
        return message;
    }

    public void setMassege(String massege) {
        this.message = massege;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"message\":" + "\"" + message + "\"" +
                ", \"data\":" + data +
                "}";
    }
}

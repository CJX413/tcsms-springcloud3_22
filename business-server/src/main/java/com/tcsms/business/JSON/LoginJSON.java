package com.tcsms.business.JSON;

public class LoginJSON {
    private Integer code;//状态码
    private String token;
    private Boolean success;//状态
    private String message;//消息

    public LoginJSON() {

    }

    public LoginJSON(Integer code, String token, Boolean success, String massege) {
        this.code = code;
        this.token = token;
        this.success = success;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMassege() {
        return message;
    }

    public void setMassege(String massege) {
        this.message = massege;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString() {
        return "{" +
                "\"code\":" + code +
                ",\"token\":" + "\"" + token + "\"" +
                ", \"success\":" + success +
                ", \"message\":" + "\"" + message + "\"" +
                "}";
    }
}

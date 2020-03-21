package com.tcsms.business.Entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SmsParams {

    /**
     * 信息
     */
    private  String message;

    /**
     * 手机号码
     */
    private String[] phone;

    public SmsParams(String[] phone, String message) {
        this.phone = phone;
        this.message = message;
    }
}

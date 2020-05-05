package com.tcsms.business.Entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SmsParams {

    /**
     * 信息
     */
    private  String[] params;

    /**
     * 手机号码
     */
    private String[] phone;

    public SmsParams(String[] phone, String[] params) {
        this.phone = phone;
        this.params = params;
    }
}

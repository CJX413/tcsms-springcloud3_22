package com.tcsms.business.Entity;

import lombok.Data;

@Data
public class WarningCount {
    private String message;
    private Integer count;

    public WarningCount() {

    }

    public WarningCount(String message, Integer count) {
        this.message = message;
        this.count = count;
    }

    @Override
    public String toString() {
        return "{" +
                "\"count\":" + count + "," +
                "\"message\":" + "\"" + message + "\"" +
                "}";
    }
}

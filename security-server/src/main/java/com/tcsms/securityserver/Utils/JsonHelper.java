package com.tcsms.securityserver.Utils;

public class JsonHelper {
    public static String replaceIllegalChar(String string) {
        return string.replaceAll("\n", "/n")
                .replaceAll("\r", "/r")
                .replaceAll("\t", "/t");
    }
}

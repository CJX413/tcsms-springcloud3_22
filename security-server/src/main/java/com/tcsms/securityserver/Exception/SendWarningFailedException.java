package com.tcsms.securityserver.Exception;

public class SendWarningFailedException extends Exception {

    public SendWarningFailedException() {
        super("发送警报信息失败！");
    }
}

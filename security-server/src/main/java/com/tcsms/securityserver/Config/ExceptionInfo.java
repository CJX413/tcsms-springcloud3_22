package com.tcsms.securityserver.Config;

public enum ExceptionInfo {
    DEVICE_COLLISION_MONITOR_SEND_WARNING(600, "设备碰撞监听器发送预警失败！"),
    DEVICE_COLLISION_MONITOR_STOP(601, "设备碰撞监听器停止！"),
    OTHER_MONITOR_SEND_WARNING(602, "力矩监听器发送预警失败！"),
    OTHER_MONITOR_STOP(603, "力矩监听器停止！"),
    MANAGER_MONITOR_ACCIDENTALLY_STOP(604, "监听器管理线程意外停止了！"),
    ;
    private int code;
    private String msg;

    ExceptionInfo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}

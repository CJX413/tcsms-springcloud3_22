package com.tcsms.securityserver.Monitor;

import com.google.gson.JsonArray;
import com.tcsms.securityserver.Config.ExceptionInfo;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Exception.SendWarningFailedException;
import com.tcsms.securityserver.Service.ServiceImp.RedisServiceImp;
import com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp;

import java.util.ArrayList;
import java.util.List;

public abstract class TcsmsMonitor extends Thread {

    private String threadName;
    protected RestTemplateServiceImp restTemplateServiceImp;
    protected RedisServiceImp redisServiceImp;
    protected final static long SLEEP_TIME = 500;
    protected int lastOperationLogHashCode = 0;
    private boolean pause = false;
    private int notRunningTimes;

    TcsmsMonitor(String threadName) {
        super(threadName);
        this.threadName = threadName;
    }

    abstract List<WarningInfo> isWarning();

    abstract JsonArray getData();

    abstract boolean isRunning();

    void pause() {
        this.pause = true;
    }

    void awake() {
        synchronized (this) {
            this.pause = false;
            this.notify();
        }
    }

    public List<String> getRelatedDevice() {
        List<String> list = new ArrayList<>();
        String[] devices = this.threadName.split("-");
        for (int i = 1; i < devices.length; i++) {
            list.add(devices[i]);
        }
        return list;
    }

    void sendWarning(WarningInfo warningInfo, JsonArray data) throws SendWarningFailedException {
        restTemplateServiceImp.sendWarning(warningInfo, data);
    }

    void sendException(ExceptionInfo exceptionInfo, JsonArray data) {
        restTemplateServiceImp.sendException(exceptionInfo, data);
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void isWait() throws InterruptedException {
        synchronized (this) {
            if (pause) {
                this.wait();
            }
        }
    }

    public boolean isPause() {
        return this.pause;
    }

    public int getNotRunningTimes() {
        return notRunningTimes;
    }

    public void setNotRunningTimes(int notRunningTimes) {
        this.notRunningTimes = notRunningTimes;
    }

    @Override
    public int hashCode() {
        return (this.isInterrupted() ? 1 : 0) + (isPause() ? 1 : 0) * 2;
    }

}

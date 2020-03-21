package com.tcsms.securityserver.Monitor;

import com.google.gson.JsonArray;
import com.tcsms.securityserver.Config.ExceptionInfo;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Service.ServiceImp.RedisServiceImp;
import com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp;
import com.tcsms.securityserver.Utils.SpringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Log4j2
public class ManagerMonitor extends TcsmsMonitor {
    private final static String THREAD_PREFIX = "管理监听器";

    private final static long ALLOWED_NOT_RUNNING_TIMES = 20;//设备多久不运行就休眠监控器---1800000(半个小时)

    private int lastMonitorStatus = 0;

    public ManagerMonitor() {
        super(THREAD_PREFIX);
    }

    @Override
    public void run() {
        try {
            redisServiceImp = SpringUtil.getBean(RedisServiceImp.class);
            restTemplateServiceImp = SpringUtil.getBean(RestTemplateServiceImp.class);
            while (!Thread.interrupted()) {
                log.info("ManagerMonitor正在运行--------------");
                int thisMonitorStatus = 0;
                for (TcsmsMonitor monitor : MonitorManager.getMonitor()) {
                    thisMonitorStatus = thisMonitorStatus + monitor.hashCode();
                    if (monitor.getNotRunningTimes() > ALLOWED_NOT_RUNNING_TIMES) {
                        log.info(monitor.getThreadName() + "--暂停运行！");
                        MonitorManager.pauseMonitorByName(monitor.getThreadName());
                    }
                    List<String> devices = monitor.getRelatedDevice();
                    if (monitor instanceof DeviceCollisionMonitor) {
                        log.info("DeviceCollisionMonitor-------------");
                        DeviceCollisionMonitor deviceCollisionMonitor = (DeviceCollisionMonitor) monitor;
                        if (deviceCollisionMonitor.isRunning(devices)) {
                            MonitorManager.notifyMonitorByName(deviceCollisionMonitor.getThreadName());
                            deviceCollisionMonitor.setNotRunningTimes(0);
                        }
                    } else if (monitor instanceof OtherMonitor) {
                        log.info("OtherMonitor-------------");
                        OtherMonitor otherMonitor = (OtherMonitor) monitor;
                        if (otherMonitor.isRunning(devices)) {
                            MonitorManager.notifyMonitorByName(otherMonitor.getThreadName());
                            otherMonitor.setNotRunningTimes(0);
                        }
                    }
                }
                if (lastMonitorStatus != thisMonitorStatus) {
                    lastMonitorStatus = thisMonitorStatus;
                    restTemplateServiceImp.sendMonitorStatus();
                    log.info(">>>>>>>>>>>>>>>>>监控器转态改变");
                }
                Thread.sleep(SLEEP_TIME);
            }
        } catch (InterruptedException e) {
            sendException(ExceptionInfo.MANAGER_MONITOR_ACCIDENTALLY_STOP, null);
        }
    }

    @Override
    List<WarningInfo> isWarning() {
        return null;
    }

    @Override
    JsonArray getData() {
        return null;
    }

    @Override
    public boolean isRunning() {
        return true;
    }

}

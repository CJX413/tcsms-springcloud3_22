package com.tcsms.securityserver.Controller;


import com.tcsms.securityserver.JSON.ResultJSON;
import com.tcsms.securityserver.Monitor.MonitorManager;
import com.tcsms.securityserver.Service.ServiceImp.SecurityServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class SecurityController {

    @Autowired
    SecurityServiceImp securityServiceImp;

    @RequestMapping("/openSecuritySystem")
    public String openSecuritySystem() {
        try {
            MonitorManager.shutDownAllMonitor();
            securityServiceImp.openManagerMonitor();
            securityServiceImp.openDeviceCollisionMonitor();
            securityServiceImp.openOtherMonitor();
            MonitorManager.turn_on = true;
        } catch (RuntimeException e) {
            MonitorManager.shutDownAllMonitor();
            MonitorManager.turn_on = false;
            e.printStackTrace();
            return new ResultJSON
                    (200, false, e.getMessage(), MonitorManager.getMonitorStatus()).toString();
        }
        return new ResultJSON
                (200, true, "开启监控安全系统状态成功！", MonitorManager.getMonitorStatus()).toString();
    }

    @RequestMapping("/closeSecuritySystem")
    public String closeSecuritySystem() {
        try {
            MonitorManager.shutDownAllMonitor();
            MonitorManager.turn_on = false;
        } catch (RuntimeException e) {
            MonitorManager.shutDownAllMonitor();
            MonitorManager.turn_on = false;
            e.printStackTrace();
            return new ResultJSON
                    (200, false, e.getMessage(), MonitorManager.getMonitorStatus()).toString();
        }
        return new ResultJSON
                (200, true, "关闭监控安全系统状态成功！", MonitorManager.getMonitorStatus()).toString();
    }

    @RequestMapping("/restartSecuritySystem")
    public String restartSecuritySystem() {
        try {
            MonitorManager.shutDownAllMonitor();
            securityServiceImp.openManagerMonitor();
            securityServiceImp.openDeviceCollisionMonitor();
            securityServiceImp.openOtherMonitor();
            MonitorManager.turn_on = true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            MonitorManager.shutDownAllMonitor();
            MonitorManager.turn_on = false;
            e.printStackTrace();
            return new ResultJSON
                    (200, false, e.getMessage(), MonitorManager.getMonitorStatus()).toString();
        }
        return new ResultJSON
                (200, true, "重启安全监控系统成功！", MonitorManager.getMonitorStatus()).toString();
    }

    @RequestMapping("/monitorStatus")
    public String monitorStatus() {
        return new ResultJSON
                (200, true, "获取安全监控系统状态成功！", MonitorManager.getMonitorStatus()).toString();
    }

//    @RequestMapping("/notifyMonitor")
//    public String notifyMonitor() {
//        MonitorManager.notifyAllMonitor();
//        return "唤醒所有监听器";
//    }

    @RequestMapping("test")
    public String test() {
        return "------------------";
    }

}

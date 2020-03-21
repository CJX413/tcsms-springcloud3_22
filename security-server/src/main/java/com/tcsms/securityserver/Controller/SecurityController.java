package com.tcsms.securityserver.Controller;


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

    @RequestMapping("/securitySystemSwitch")
    public String securitySystemSwitch() {
        if (MonitorManager.turn_on) {
            MonitorManager.shutDownAllMonitor();
            if (MonitorManager.getMonitorCount() == 0) {
                MonitorManager.turn_on = false;
            }
        } else {
            securityServiceImp.openManagerMonitor();
            securityServiceImp.openDeviceCollisionMonitor();
            securityServiceImp.openOtherMonitor();
            if (MonitorManager.getRunningCount() == MonitorManager.getMonitorCount()) {
                MonitorManager.turn_on = true;
            } else {
                MonitorManager.shutDownAllMonitor();
                MonitorManager.turn_on = false;
            }
        }
        return MonitorManager.getMonitorStatus().toString();
    }

    @RequestMapping("/monitorStatus")
    public String monitorStatus() {
        return MonitorManager.getMonitorStatus().toString();
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

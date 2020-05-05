package com.tcsms.securityserver.Service.ServiceImp;

import com.tcsms.securityserver.Dao.DeviceRegistryDao;
import com.tcsms.securityserver.Dao.OperatorDao;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Monitor.DeviceCollisionMonitor;
import com.tcsms.securityserver.Monitor.ManagerMonitor;
import com.tcsms.securityserver.Monitor.MonitorManager;
import com.tcsms.securityserver.Monitor.OtherMonitor;
import com.tcsms.securityserver.Service.SecurityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Log4j2
@Service
@Component
public class SecurityServiceImp implements SecurityService {
    @Autowired
    DeviceRegistryDao deviceRegistryDao;
    @Autowired
    OperatorDao operatorDao;
    @Autowired
    RedisServiceImp redisServiceImp;

    public void openDeviceCollisionMonitor() throws RuntimeException {
        List<DeviceRegistry> deviceRegistryList = deviceRegistryDao.findByIsRegistered(true);
        for (int i = 0; i < deviceRegistryList.size(); i++) {
            for (int j = i + 1; j < deviceRegistryList.size(); j++) {
                DeviceCollisionMonitor monitor = new DeviceCollisionMonitor(deviceRegistryList.get(i),
                        deviceRegistryList.get(j));
                if (!monitor.isCompleteSafe()) {
                    monitor.start();
                    MonitorManager.addMonitor(monitor);
                }
            }
        }
    }

    public void openOtherMonitor() throws RuntimeException {
        List<DeviceRegistry> deviceRegistryList = deviceRegistryDao.findByIsRegistered(true);
        HashMap<String, String> operatorMap = new HashMap<>();
        operatorDao.findAll().forEach(operator -> {
            operatorMap.put(operator.getWorkerId(), operator.getName());
        });
        for (DeviceRegistry deviceRegistry : deviceRegistryList) {
            OtherMonitor monitor = new OtherMonitor(deviceRegistry, operatorMap);
            monitor.start();
            MonitorManager.addMonitor(monitor);
        }
    }

    public void openManagerMonitor() throws RuntimeException {
        ManagerMonitor monitor = new ManagerMonitor();
        monitor.start();
        MonitorManager.addMonitor(monitor);
    }
}

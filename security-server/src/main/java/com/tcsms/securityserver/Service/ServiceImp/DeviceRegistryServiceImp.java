package com.tcsms.securityserver.Service.ServiceImp;


import com.tcsms.securityserver.Dao.DeviceRegistryDao;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Service.DeviceRegistryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Log4j2
@Service
public class DeviceRegistryServiceImp implements DeviceRegistryService {
    @Autowired
    DeviceRegistryDao deviceRegistryDao;

    public DeviceRegistryDao getDao() {
        return deviceRegistryDao;
    }

    public boolean isRegisteredChange(DeviceRegistry device) throws RuntimeException {
        Optional<DeviceRegistry> optional = deviceRegistryDao.findById(device.getDeviceId());
        if (optional.isPresent()) {
            if (optional.get().getIsRegistered() == device.getIsRegistered()) {
                return false;
            }
            return true;
        }
        return false;
    }


}

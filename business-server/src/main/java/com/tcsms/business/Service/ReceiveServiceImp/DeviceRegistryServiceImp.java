package com.tcsms.business.Service.ReceiveServiceImp;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tcsms.business.Dao.DeviceRegistryDao;
import com.tcsms.business.Entity.DeviceRegistry;
import com.tcsms.business.Service.DeviceRegistryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Log4j2
@Service
public class DeviceRegistryServiceImp implements DeviceRegistryService {
    @Autowired
    DeviceRegistryDao deviceRegistryDao;
    @Autowired
    RedisServiceImp redisServiceImp;

    public DeviceRegistryDao getDao() {
        return deviceRegistryDao;
    }


    public int updateDeviceRegistry(DeviceRegistry deviceRegistry) throws RuntimeException{
        return deviceRegistryDao.update(deviceRegistry);
    }

    public JsonArray getAllDeviceInfo() {
        JsonArray jsonArray = new JsonArray();
        deviceRegistryDao.findAll().forEach(deviceRegistry -> {
            jsonArray.add(new Gson().fromJson(deviceRegistry.toString(), JsonObject.class));
        });
        return jsonArray;
    }
}

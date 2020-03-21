package com.tcsms.business.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.tcsms.business.Entity.DeviceRegistry;
import com.tcsms.business.JSON.ResultJSON;
import com.tcsms.business.Service.ReceiveServiceImp.DeviceRegistryServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class DeviceController {
    @Autowired
    DeviceRegistryServiceImp deviceRegistryServiceImp;

    @Autowired

    @RequestMapping("/deviceInfo")
    public String getDeviceInfo() {
        JsonArray jsonArray = deviceRegistryServiceImp.getAllDeviceInfo();
        log.info(jsonArray);
        return jsonArray.toString();
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @RequestMapping(value = "/deleteDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deleteDevice(@RequestBody String json) {
        DeviceRegistry deviceRegistry = new Gson().fromJson(json, DeviceRegistry.class);
        log.info(deviceRegistry.toString());
        deviceRegistryServiceImp.getDao().deleteById(deviceRegistry.getDeviceId());
        return new ResultJSON(200,true,"删除设备成功！",null).toString();
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @RequestMapping(value = "/updateDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateDevice(@RequestBody String json) {
        DeviceRegistry deviceRegistry = new Gson().fromJson(json, DeviceRegistry.class);
        log.info(deviceRegistry.toString());
        deviceRegistryServiceImp.updateDeviceRegistry(deviceRegistry);
        return new ResultJSON(200,true,"修改设备信息成功！",null).toString();
    }


}

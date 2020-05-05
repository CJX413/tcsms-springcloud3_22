package com.tcsms.securityserver.Controller;

import com.google.gson.Gson;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Filter.ReceiveFilter;
import com.tcsms.securityserver.JSON.ResultJSON;
import com.tcsms.securityserver.Service.ServiceImp.DeviceRegistryServiceImp;
import com.tcsms.securityserver.Utils.JsonHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class DeviceController {
    @Autowired
    private DeviceRegistryServiceImp deviceRegistryServiceImp;

    @RequestMapping(value = "/updateDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateDevice(@RequestBody String json) {
        try {
            log.info(json);
            DeviceRegistry device = new Gson().fromJson(json, DeviceRegistry.class);
            ReceiveFilter.updateDeviceRegistryHashMap(device);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResultJSON(200, false,
                    "修改接收设备运行数据的拦截器失败！" + JsonHelper.replaceIllegalChar(e.getMessage()), null).toString();
        }
        return new ResultJSON(200, true, "修改接收设备运行数据的拦截器成功！", null).toString();
    }

    @RequestMapping(value = "/deleteDevice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deleteDevice(@RequestBody String json) {
        try {
            DeviceRegistry device = new Gson().fromJson(json, DeviceRegistry.class);
            ReceiveFilter.deleteDeviceRegistryHashMap(device);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResultJSON(200, false,
                    "删除接收设备运行数据的拦截器失败！" + JsonHelper.replaceIllegalChar(e.getMessage()), null).toString();
        }
        return new ResultJSON(200, true, "删除接收设备运行数据的拦截器成功！", null).toString();
    }
}

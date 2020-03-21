package com.tcsms.business.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tcsms.business.JSON.ResultJSON;
import com.tcsms.business.Utils.JwtTokenUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@Log4j2
@RestController
public class OperationLogController {

    @Autowired
    WebSocket webSocket;

    @PreAuthorize(value = "hasAnyAuthority('MONITOR','ADMIN')")
    @RequestMapping(value = "/openOperationLog", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String openOperationLog(@RequestBody String json, HttpServletRequest request) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        String deviceId = jsonObject.get("deviceId").getAsString();
        String token = request.getHeader("authorization");
        String name = JwtTokenUtils.getUsername(token);
        webSocket.openOperationLogSendThread(name, deviceId);
        return new ResultJSON(200, true, "获取设备运行信息成功！", null).toString();
    }
    @PreAuthorize(value = "hasAnyAuthority('MONITOR','ADMIN')")
    @RequestMapping(value = "/closeOperationLog", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String closeOperationLog(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String name = JwtTokenUtils.getUsername(token);
        webSocket.closeOperationLogSendThread(name);
        return new ResultJSON(200, true, "结束获取设备运行信息！", null).toString();
    }

    @PreAuthorize(value = "hasAnyAuthority('MONITOR','ADMIN')")
    @RequestMapping(value = "/openOperationLogDate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String openOperationLogDate(@RequestBody String json, HttpServletRequest request) {
        try {
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            String deviceId = jsonObject.get("deviceId").getAsString();
            String time = jsonObject.get("time").getAsString();
            log.info(time);
            String token = request.getHeader("authorization");
            String name = JwtTokenUtils.getUsername(token);
            webSocket.openOperationLogDateSendThread(name, deviceId,time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ResultJSON(200, true, "获取设备运行信息成功！", null).toString();
    }

    @PreAuthorize(value = "hasAnyAuthority('MONITOR','ADMIN')")
    @RequestMapping(value = "/closeOperationLogDate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String closeOperationLogDate(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String name = JwtTokenUtils.getUsername(token);
        webSocket.closeOperationLogDateSendThread(name);
        return new ResultJSON(200, true, "结束获取设备运行信息！", null).toString();
    }
}

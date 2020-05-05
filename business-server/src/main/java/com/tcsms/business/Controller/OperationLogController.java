package com.tcsms.business.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tcsms.business.JSON.ResultJSON;
import com.tcsms.business.Service.ReceiveServiceImp.OperationLogDateServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.OperationLogServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.WarningLogServiceImp;
import com.tcsms.business.Utils.JsonHelper;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
@RestController
public class OperationLogController {

    @Autowired
    WebSocket webSocket;
    @Autowired
    OperationLogDateServiceImp operationLogDateServiceImp;
    @Autowired
    OperationLogServiceImp operationLogServiceImp;
    @Autowired
    WarningLogServiceImp warningLogServiceImp;

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
    @RequestMapping(value = "/closeAllKindOfOperationLogSend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String closeOperationLog(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String name = JwtTokenUtils.getUsername(token);
        webSocket.closeAllKindOfOperationLogSendThread(name);
        return new ResultJSON(200, true, "结束获取设备运行信息！", null).toString();
    }

    @PreAuthorize(value = "hasAnyAuthority('MONITOR','ADMIN')")
    @RequestMapping(value = "/openAllOperationLog", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String openAllOperationLog(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String name = JwtTokenUtils.getUsername(token);
        webSocket.openAllOperationLogSendThread(name);
        return new ResultJSON(200, true, "获取全部设备运行信息成功！", null).toString();
    }

    @PreAuthorize(value = "hasAnyAuthority('MONITOR','ADMIN')")
    @RequestMapping(value = "/openAllOperationLogDate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String openAllOperationLogDate(@RequestBody String json, HttpServletRequest request) {
        try {
            String token = request.getHeader("authorization");
            String name = JwtTokenUtils.getUsername(token);
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            String time = jsonObject.get("time").getAsString();
            log.info(time);
            webSocket.openAllOperationLogDateSendThread(name, time);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResultJSON(200, false, JsonHelper.replaceIllegalChar(e.getMessage()), null).toString();
        }
        return new ResultJSON(200, true, "获取历史全部设备运行信息成功！", null).toString();
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
            webSocket.openOperationLogDateSendThread(name, deviceId, time);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResultJSON(200, false, JsonHelper.replaceIllegalChar(e.getMessage()), null).toString();
        }
        return new ResultJSON(200, true, "获取设备运行信息成功！", null).toString();
    }

    @PreAuthorize(value = "hasAnyAuthority('MONITOR','ADMIN')")
    @RequestMapping(value = "/lineChartOfOperationLog", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String lineChartOfOperationLog(@RequestBody String json) {
        JsonObject data = null;
        try {
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            log.info(jsonObject.get("date").getAsString());
            String now = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
            String date = jsonObject.get("date").getAsString();
            String deviceId = jsonObject.get("deviceId").getAsString();
            String time = jsonObject.get("time").getAsString();
            log.info(time);
            if (date.equals(now)) {
                //查OperationLog表
                data = operationLogServiceImp.getOperationLogByDeviceIdAndTime(deviceId, time);
            } else {
                //查OperationLogDate表
                data = operationLogDateServiceImp.getOperationLogByDeviceIdAndDateAndTime(deviceId, date, time);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return new ResultJSON(200, false, JsonHelper.replaceIllegalChar(e.getMessage()), null).toString();
        }
        return new ResultJSON(200, true, "获取折线图数据成功", data).toString();
    }

    @PreAuthorize(value = "hasAnyAuthority('MONITOR','ADMIN')")
    @RequestMapping(value = "/barChartOfWarningLog", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String barChartOfWarningLog(@RequestBody String json) {
        JsonObject result = new JsonObject();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        String now = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        String deviceId = jsonObject.get("deviceId").getAsString();
        String date = jsonObject.get("date").getAsString();//格式为yyyy_MM_dd
        String warningLogDate = date.replaceAll("_", "-");//格式为yyyy-MM-dd
        log.info(deviceId + "---" + date);
        try {
            log.info(warningLogDate);
            JsonArray data = warningLogServiceImp.countWarningLogByDeviceIdAndDate(deviceId, warningLogDate);
            log.info(data);
            int length;
            if (date.equals(now)) {
                length = operationLogServiceImp.getCountByDeviceId(deviceId);
            } else {
                length = operationLogDateServiceImp.getCountByDeviceId(deviceId, date);
            }
            result.addProperty("length", length);
            result.add("data", data);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResultJSON(200, false, JsonHelper.replaceIllegalChar(e.getMessage()), null).toString();
        }
        return new ResultJSON(200, true, "获取饼状图数据成功！", result).toString();
    }

}

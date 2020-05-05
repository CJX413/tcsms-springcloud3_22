package com.tcsms.securityserver.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Entity.WarningDetail;
import com.tcsms.securityserver.Entity.WarningLog;
import com.tcsms.securityserver.Exception.SendWarningFailedException;
import com.tcsms.securityserver.Monitor.MonitorManager;
import com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
public class TestController {
    @Autowired
    RestTemplateServiceImp restTemplateServiceImp;

    @RequestMapping("/test/sendWarning")
    public String send() throws SendWarningFailedException, InterruptedException {
        WarningInfo[] Warning = {

                WarningInfo.OPERATOR_RED_WARNING,
                WarningInfo.TORQUE_YELLOW_WARNING,
                WarningInfo.TORQUE_RED_WARNING,
                WarningInfo.DEVICE_COLLISION_YELLOW_WARNING,
                WarningInfo.DEVICE_COLLISION_RED_WARNING,
        };
        for (int i = 0; i < 20; i++) {
            JsonArray array = new JsonArray();
            Date date = new Date();
            String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            OperationLog log = new OperationLog(i, "QTZ5010", "D1", "陈嘉兴", "1600300211", datetime, 116.481231,
                    39.920597, 20.0, 30.0, 50.0, 200.0, 50.0, 5.0, 2);
            OperationLog log2 = new OperationLog(i + 1, "QTZ5013", "D2", "谢植赞", "1600301332", datetime, 116.481231,
                    39.920597, 20.0, 30.0, 50.0, 200.0, 50.0, 5.0, 2);
            array.add(new Gson().fromJson(log.toString(), JsonObject.class));
            array.add(new Gson().fromJson(log2.toString(), JsonObject.class));
            sendWarning(Warning[i % Warning.length], array);
            Thread.sleep(1000);
        }
        return "发送一次警报";
    }

    void sendWarning(WarningInfo warningInfo, JsonArray data) throws SendWarningFailedException {
        WarningLog warningLog = new WarningLog();
        warningLog.setCode(warningInfo.getCode());
        warningLog.setMessage(warningInfo.getMsg());
        warningLog.setTime(new Timestamp(System.currentTimeMillis()));
        List<WarningDetail> list = new ArrayList<>();
        Gson gson = new Gson();
        Optional.ofNullable(data).ifPresent(jsonArray -> {
            jsonArray.forEach(jsonElement -> {
                list.add(gson.fromJson(jsonElement.toString(), WarningDetail.class));
            });
        });
        warningLog.setWarningDetails(list);
        restTemplateServiceImp.sendWarning(warningLog);
    }

    @RequestMapping("/test/pauseMonitor")
    public void pauseMonitor() {
        MonitorManager.pauseMonitorByName("D2");
    }

    @RequestMapping("/test/notifyMonitor")
    public void notifyMonitor() {
        MonitorManager.notifyMonitorByName("D2");
    }
}

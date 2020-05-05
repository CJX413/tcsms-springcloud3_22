package com.tcsms.securityserver.Controller;


import com.google.gson.Gson;
import com.tcsms.securityserver.Config.ConstantConfig;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Service.ServiceImp.OperationLogServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;


@RestController
@Log4j2
public class ReceiveController {
    @Autowired
    OperationLogServiceImp operationLogServiceImp;
    private static final double G = 10;

    /**
     * 接收设备运行信息的接口
     * @param json；格式为OperationLog
     */
    @RequestMapping(value = "/operationLog", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void receiveOperationLog(@RequestBody String json) {
        OperationLog operationLog = new Gson().fromJson(json, OperationLog.class);
        BigDecimal radius = new BigDecimal(operationLog.getRadius());
        BigDecimal weight = new BigDecimal(operationLog.getWeight());
        BigDecimal torque = radius.multiply(weight).multiply(new BigDecimal(G));
        double Torque = torque.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        operationLog.setTorque(Torque);
        operationLogServiceImp.receiveOperationLog(operationLog);
    }

}

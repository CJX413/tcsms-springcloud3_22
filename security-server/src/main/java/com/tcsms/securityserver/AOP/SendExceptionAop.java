package com.tcsms.securityserver.AOP;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.tcsms.securityserver.Config.ExceptionInfo;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Entity.WarningDetail;
import com.tcsms.securityserver.Entity.WarningLog;
import com.tcsms.securityserver.Service.ServiceImp.WarningLogServiceImp;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@Log4j2
public class SendExceptionAop {
    @Autowired
    private WarningLogServiceImp warningLogServiceImp;

    @Pointcut("execution(* com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp.sendException(..))")
    public void sendException() {
    }


    @Around(value = "sendException()")
    public void exceptionLog(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        ExceptionInfo exceptionInfo = (ExceptionInfo) args[0];
        JsonArray jsonArray = (JsonArray) args[1];
        List<WarningDetail> list = new ArrayList<>();
        if (jsonArray != null) {
            jsonArray.forEach(jsonElement -> {
                WarningDetail warningDetail = new Gson().fromJson(jsonElement, WarningDetail.class);
                list.add(warningDetail);
            });
        }
        WarningLog warningLog = new WarningLog();
        warningLog.setCode(exceptionInfo.getCode());
        warningLog.setMessage(exceptionInfo.getMsg());
        warningLogServiceImp.save(warningLog, list);
        point.proceed();

    }
}

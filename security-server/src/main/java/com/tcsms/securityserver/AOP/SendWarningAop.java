package com.tcsms.securityserver.AOP;


import com.tcsms.securityserver.Entity.WarningLog;
import com.tcsms.securityserver.Service.ServiceImp.WarningLogServiceImp;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;


@Aspect
@Component
@Log4j2
public class SendWarningAop {

    @Autowired
    private WarningLogServiceImp warningLogServiceImp;

    private static CopyOnWriteArraySet<Integer> warningSet = new CopyOnWriteArraySet<>();

    @Pointcut("execution(* com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp.sendWarning(..))")
    public void sendWarning() {
    }


    @Around(value = "sendWarning()")
    public void warningAround(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        WarningLog warningLog = (WarningLog) args[0];
        warningLogServiceImp.save(warningLog);
        if (!warningSet.contains(warningLog.hashCode())) {
            log.info("还未发送过");
            warningSet.add(warningLog.hashCode());
            point.proceed();
        } else {
            log.info("已经发送过");
        }

    }

    public static void clearWarningSet() {
        warningSet.clear();
    }
}

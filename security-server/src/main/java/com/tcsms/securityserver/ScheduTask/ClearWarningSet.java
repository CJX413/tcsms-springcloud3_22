package com.tcsms.securityserver.ScheduTask;

import com.tcsms.securityserver.AOP.SendWarningAop;
import com.tcsms.securityserver.Service.ServiceImp.OperationLogDateServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ClearWarningSet {
    @Autowired
    OperationLogDateServiceImp operationLogDateServiceImp;


    /**
     * 清空SendWarningAop中的WarningSet
     */
    //每五分钟执行一次
    @Scheduled(cron = "0 */5 * * * ?")
    public void clearWarningSet() {
        log.info(">>>>>>>>>>>>>>>>>>>>>清空SendWarningAop中的WarningSet>>>>>>>>>>>>>>>>");
        SendWarningAop.clearWarningSet();
    }
}

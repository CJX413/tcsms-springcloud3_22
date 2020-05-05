package com.tcsms.securityserver.ScheduTask;

import com.tcsms.securityserver.Service.ServiceImp.OperationLogDateServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class RefreshOperationLogTab {
    @Autowired
    OperationLogDateServiceImp operationLogDateServiceImp;


    /**
     * 在每天的1点刷新operation_log表
     *
     * @throws InterruptedException
     */
    //每天23点执行
    @Scheduled(cron = "0 0 23 * * ?")
    public void refreshOperationLogTab() throws InterruptedException {
        if (operationLogDateServiceImp.backupOperationLog()) {
            log.info(">>>>>>>>>>>>>>>>>备份每日设备运行日志表成功<<<<<<<<<<<<<<<<");
            if (operationLogDateServiceImp.refreshOperationLogTab()) {
                log.info(">>>>>>>>>>>>>>>>>刷新每日设备运行日志表成功<<<<<<<<<<<<<<<<！");
            } else {
                log.info(">>>>>>>>>>>>>>>>>刷新每日设备运行日志表失败<<<<<<<<<<<<<<<<！");
            }
        } else {
            log.info(">>>>>>>>>>>>>>>>>备份每日设备运行日志表失败<<<<<<<<<<<<<<<<");
        }
    }
}

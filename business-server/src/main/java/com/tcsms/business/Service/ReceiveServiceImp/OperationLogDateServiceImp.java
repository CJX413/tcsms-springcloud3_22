package com.tcsms.business.Service.ReceiveServiceImp;


import com.tcsms.business.Dao.SqlMapper;
import com.tcsms.business.Entity.OperationLog;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.Optional;

@Log4j2
@Service
public class OperationLogDateServiceImp {
    @Autowired
    SqlMapper sqlMapper;


    public void queryOperationLogDateByDeviceIdAndTime(Deque<OperationLog> deque, String deviceId, String time, String date) {
        log.info("队列中的消息不足，重新重数据库获取消息");
        Optional.ofNullable(sqlMapper.queryOperationLogDateByDeviceIdAndTime(deviceId, time, date))
                .ifPresent(operationLogs -> {
                    operationLogs.forEach(operationLog -> deque.addLast(operationLog));
                });
    }

    @Async
    public void refreshOperationLogDateQueue(Deque<OperationLog> deque, String deviceId, String date) {
        log.info("队列中的消息不足，重新重数据库获取消息");
        OperationLog lastOperationLog = deque.getLast();
        String time = lastOperationLog.getTime();
        Optional.ofNullable(sqlMapper.queryOperationLogDateByDeviceIdAndTime(deviceId, time, date))
                .ifPresent(operationLogs -> {
                   operationLogs.forEach(operationLog -> deque.addLast(operationLog));
                });
    }
}

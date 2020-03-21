package com.tcsms.business.Service.ReceiveServiceImp;


import com.tcsms.business.Dao.SqlMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Log4j2
@Service
public class OperationLogDateServiceImp {
    @Autowired
    SqlMapper sqlMapper;

    @Async
    public void queryOperationLogDateByDeviceIdAndTime(ConcurrentLinkedQueue queue, String deviceId, String time, String date) {
        log.info("队列中的消息不足，重新重数据库获取消息");
        sqlMapper.queryOperationLogDateByDeviceIdAndTime(deviceId, time, date).forEach(operationLog -> {
            queue.add(operationLog);
        });
    }
}

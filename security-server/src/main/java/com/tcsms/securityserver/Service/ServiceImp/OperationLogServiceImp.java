package com.tcsms.securityserver.Service.ServiceImp;

import com.tcsms.securityserver.Dao.OperationLogDao;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Service.OperationLogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class OperationLogServiceImp implements OperationLogService {

    @Autowired
    OperationLogDao operationLogDao;
    @Autowired
    RedisServiceImp redisServiceImp;

    public OperationLogDao getDao() {
        return operationLogDao;
    }

    public void receiveOperationLog(OperationLog operationLog) {
        redisServiceImp.set(operationLog.getDeviceId(), operationLog.toString());
        log.info(operationLog.toString());
        operationLogDao.save(operationLog);
    }
}

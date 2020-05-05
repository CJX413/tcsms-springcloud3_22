package com.tcsms.securityserver;

import com.tcsms.securityserver.Dao.OperationLogDao;
import com.tcsms.securityserver.Dao.SqlMapper;
import com.tcsms.securityserver.Dao.WarningDetailDao;
import com.tcsms.securityserver.Dao.WarningLogDao;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.ScheduTask.RefreshOperationLogTab;
import com.tcsms.securityserver.Service.ServiceImp.OperationLogDateServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityServerApplicationTests {

    @Autowired
    RefreshOperationLogTab task;
    @Autowired
    SqlMapper sqlMapper;
    @Autowired
    OperationLogDateServiceImp operationLogDateServiceImp;
    @Autowired
    WarningDetailDao warningDetailDao;
    @Autowired
    WarningLogDao warningLogDao;
    @Autowired
    OperationLogDao operationLogDao;

    @Test
    void contextLoads() throws InterruptedException {
        int i = 1;
        while (true) {
            OperationLog operationLog = new OperationLog(i, "1", "D1", "1", "1", "1", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1);
            operationLogDao.save(operationLog);
            OperationLog operationLog1 = new OperationLog(i + 1, "1", "D3", "1", "1", "1", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1);
            operationLogDao.save(operationLog1);
            i = i + 2;
            Thread.sleep(500);
        }
    }

}

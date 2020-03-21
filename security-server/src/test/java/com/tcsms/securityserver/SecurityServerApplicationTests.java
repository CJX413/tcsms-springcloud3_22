package com.tcsms.securityserver;

import com.tcsms.securityserver.Dao.SqlMapper;
import com.tcsms.securityserver.Dao.WarningDetailDao;
import com.tcsms.securityserver.Dao.WarningLogDao;
import com.tcsms.securityserver.Entity.WarningDetail;
import com.tcsms.securityserver.Entity.WarningLog;
import com.tcsms.securityserver.ScheduTask.Task;
import com.tcsms.securityserver.Service.ServiceImp.OperationLogDateServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SecurityServerApplicationTests {

    @Autowired
    Task task;
    @Autowired
    SqlMapper sqlMapper;
    @Autowired
    OperationLogDateServiceImp operationLogDateServiceImp;
    @Autowired
    WarningDetailDao warningDetailDao;
    @Autowired
    WarningLogDao warningLogDao;

    @Test
    void contextLoads() throws InterruptedException {
        // sqlMapper.dropOperationLogBackup();
        // task.refreshOperationLogTab();
        WarningLog warningLog = new WarningLog();
        warningLog.setCode(200);
        warningLog.setMessage("55555555555555");
        List<WarningDetail> warningDetails = new ArrayList<>();
        WarningDetail warningDetail = new WarningDetail();
        warningDetail.setDeviceModel("161651");
        warningDetail.setDeviceId("XXXXXXXXXXXX");
        warningDetail.setWarningLog(warningLog);
        warningDetails.add(warningDetail);
        warningLog.setWarningDetails(warningDetails);
        warningLogDao.save(warningLog);

    }

}

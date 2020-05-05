package com.tcsms.securityserver.Service.ServiceImp;


import com.tcsms.securityserver.Dao.WarningDetailDao;
import com.tcsms.securityserver.Dao.WarningLogDao;
import com.tcsms.securityserver.Entity.WarningLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarningLogServiceImp {
    @Autowired
    private WarningLogDao warningLogDao;
    @Autowired
    private WarningDetailDao warningDetailDao;

    public WarningLog save(WarningLog warningLog) {
        warningLog.getWarningDetails().forEach(warningDetail -> {
            warningDetail.setWarningLog(warningLog);
        });
        return warningLogDao.save(warningLog);
    }
}

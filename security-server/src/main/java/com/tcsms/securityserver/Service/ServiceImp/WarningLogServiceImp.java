package com.tcsms.securityserver.Service.ServiceImp;


import com.tcsms.securityserver.Dao.WarningDetailDao;
import com.tcsms.securityserver.Dao.WarningLogDao;
import com.tcsms.securityserver.Entity.WarningDetail;
import com.tcsms.securityserver.Entity.WarningLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningLogServiceImp {
    @Autowired
    private WarningLogDao warningLogDao;
    @Autowired
    private WarningDetailDao warningDetailDao;

    public WarningLog save(WarningLog warningLog, List<WarningDetail> list) {
        list.forEach(warningDetail -> {
            warningDetail.setWarningLog(warningLog);
        });
        warningLog.setWarningDetails(list);
        return warningLogDao.save(warningLog);
    }
}

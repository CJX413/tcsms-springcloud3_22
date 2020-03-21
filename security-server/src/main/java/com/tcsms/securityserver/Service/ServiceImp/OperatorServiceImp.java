package com.tcsms.securityserver.Service.ServiceImp;

import com.tcsms.securityserver.Dao.OperatorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorServiceImp {
    @Autowired
    OperatorDao operatorDao;

    public OperatorDao getDao() {
        return operatorDao;
    }
}

package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Dao.OperatorDao;
import com.tcsms.business.Entity.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OperatorServiceImp {
    @Autowired
    OperatorDao operatorDao;

    public OperatorDao getDao() {
        return operatorDao;
    }

    public Operator isOperator(String username) {
        Optional<Operator> operator = operatorDao.findById(username);
        return operator.orElse(null);
    }
}

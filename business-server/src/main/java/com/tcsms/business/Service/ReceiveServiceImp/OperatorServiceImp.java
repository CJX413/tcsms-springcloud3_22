package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Dao.OperatorDao;
import com.tcsms.business.Entity.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorServiceImp {
    @Autowired
    OperatorDao operatorDao;

    public OperatorDao getDao() {
        return operatorDao;
    }

    public Operator isOperator(String username) {
        return operatorDao.findById(username).orElse(null);
    }

    public void deleteOperator(Operator operator) throws RuntimeException{
        operatorDao.deleteById(operator.getUsername());
    }

    public void addOperator(Operator operator) throws RuntimeException {
        operatorDao.save(operator);
    }
}

package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Dao.OperatorApplyDao;
import com.tcsms.business.Dao.UserDao;
import com.tcsms.business.Entity.OperatorApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OperatorApplyServiceImp {
    @Autowired
    OperatorApplyDao operatorApplyDao;
    @Autowired
    UserDao userDao;

    public OperatorApplyDao getDao() {
        return operatorApplyDao;
    }

    public boolean applyOperator(OperatorApply operatorInfo) {
        if (operatorApplyDao.findById(operatorInfo.getUsername()).isPresent()) {
            return false;
        }
        Optional<OperatorApply> optional = Optional.ofNullable(operatorApplyDao.save(operatorInfo));
        return optional.isPresent();
    }

    public boolean updateOperator(OperatorApply operatorInfo) {
        Optional<OperatorApply> optional = Optional.ofNullable(operatorApplyDao.save(operatorInfo));
        return optional.isPresent();
    }

    public OperatorApply isAppliedOperator(String username) {
        Optional<OperatorApply> operatorApply = operatorApplyDao.findById(username);
        return operatorApply.orElse(null);
    }
}

package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Dao.OperatorApplyDao;
import com.tcsms.business.Dao.OperatorDao;
import com.tcsms.business.Entity.Operator;
import com.tcsms.business.Entity.OperatorApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperatorApplyServiceImp {
    @Autowired
    OperatorApplyDao operatorApplyDao;
    @Autowired
    OperatorDao operatorDao;

    public OperatorApplyDao getDao() {
        return operatorApplyDao;
    }

    public void applyOperator(OperatorApply operatorInfo) throws RuntimeException {
        operatorApplyDao.save(operatorInfo);
    }

    public void updateOperator(OperatorApply operatorInfo) throws RuntimeException {
        operatorApplyDao.save(operatorInfo);
    }

    public OperatorApply isAppliedOperator(String username) {
        return operatorApplyDao.findById(username).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void agreeApply(OperatorApply operatorApply) throws RuntimeException {
        operatorDao.findById(operatorApply.getUsername()).ifPresent(operator -> {
            class AgreeApplyRuntimeException extends RuntimeException{
                private AgreeApplyRuntimeException(String message){
                    super(message);
                }
            }
            throw new AgreeApplyRuntimeException("该用户已经是驾驶员了！");
        });
        Operator operator = new Operator();
        operator.setName(operatorApply.getName());
        operator.setSpecialOperationCertificateNumber(operatorApply.getSpecialOperationCertificateNumber());
        operator.setUsername(operatorApply.getUsername());
        operator.setWorkerId(operatorApply.getWorkerId());
        operatorDao.save(operator);
        operatorApplyDao.deleteById(operatorApply.getUsername());
    }

    public void refuseApply(OperatorApply operatorApply) throws RuntimeException {
        operatorApplyDao.deleteById(operatorApply.getUsername());
    }
}

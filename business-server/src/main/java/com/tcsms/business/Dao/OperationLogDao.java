package com.tcsms.business.Dao;

import com.tcsms.business.Entity.OperationLog;
import com.tcsms.business.Entity.OperationLogPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface OperationLogDao extends JpaRepository<OperationLog, OperationLogPK>, JpaSpecificationExecutor<OperationLog>, Serializable {

}

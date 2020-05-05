package com.tcsms.securityserver.Dao;


import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Entity.OperationLogPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface OperationLogDao extends JpaRepository<OperationLog, OperationLogPK>, Serializable {
}

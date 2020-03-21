package com.tcsms.securityserver.Dao;

import com.tcsms.securityserver.Entity.WarningLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface WarningLogDao extends JpaRepository<WarningLog, String>, JpaSpecificationExecutor<WarningLog>, Serializable {
}

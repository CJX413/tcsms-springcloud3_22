package com.tcsms.securityserver.Dao;

import com.tcsms.securityserver.Entity.WarningDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface WarningDetailDao extends JpaRepository<WarningDetail, Integer>, Serializable {
}

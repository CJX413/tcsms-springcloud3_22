package com.tcsms.securityserver.Dao;

import com.tcsms.securityserver.Entity.WarningDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface WarningDetailDao extends JpaRepository<WarningDetail, Integer>, Serializable {
}

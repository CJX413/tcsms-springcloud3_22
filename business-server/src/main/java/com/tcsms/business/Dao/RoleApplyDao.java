package com.tcsms.business.Dao;

import com.tcsms.business.Entity.RoleApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface RoleApplyDao extends JpaRepository<RoleApply, String>, JpaSpecificationExecutor<RoleApply>, Serializable {
}

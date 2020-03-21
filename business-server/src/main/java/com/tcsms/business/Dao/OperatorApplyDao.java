package com.tcsms.business.Dao;

import com.tcsms.business.Entity.OperatorApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface OperatorApplyDao extends JpaRepository<OperatorApply,String>, JpaSpecificationExecutor<OperatorApply>, Serializable {
}

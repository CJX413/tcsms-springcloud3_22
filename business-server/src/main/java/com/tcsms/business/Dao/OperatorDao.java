package com.tcsms.business.Dao;

import com.tcsms.business.Entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface OperatorDao extends JpaRepository<Operator,String>, JpaSpecificationExecutor<Operator>, Serializable {
}

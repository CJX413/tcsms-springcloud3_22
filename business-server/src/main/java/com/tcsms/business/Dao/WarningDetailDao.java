package com.tcsms.business.Dao;


import com.tcsms.business.Entity.WarningDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface WarningDetailDao extends JpaRepository<WarningDetail, Integer>, Serializable {

}

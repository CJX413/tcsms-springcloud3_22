package com.tcsms.business.Dao;

import com.tcsms.business.Entity.InvitationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface InvitationCodeDao extends JpaRepository<InvitationCode, Integer>, JpaSpecificationExecutor<InvitationCode>, Serializable {

}

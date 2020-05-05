package com.tcsms.business.Dao;

import com.tcsms.business.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface UserInfoDao extends JpaRepository<UserInfo, String>, JpaSpecificationExecutor<UserInfo>, Serializable {
    UserInfo findByPhoneNumber(String phone);
    UserInfo findByWorkerId(String workerId);
}

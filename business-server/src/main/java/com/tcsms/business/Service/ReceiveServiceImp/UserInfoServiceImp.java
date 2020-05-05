package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Dao.UserInfoDao;
import com.tcsms.business.Entity.UserInfo;
import com.tcsms.business.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoServiceImp implements UserInfoService {
    @Autowired
    UserInfoDao userInfoDao;

    public UserInfoDao getDao() {
        return userInfoDao;
    }

    public UserInfo findByUserName(String username) {
        Optional<UserInfo> optional = userInfoDao.findById(username);
        return optional.orElse(null);
    }

    public void updateAdminInfo(UserInfo userInfo) throws RuntimeException {
        userInfoDao.save(userInfo);
    }

    public void updateUserInfo(UserInfo userInfo) throws RuntimeException {
        userInfoDao.findById(userInfo.getUsername()).ifPresent(info -> {
            info.setName(userInfo.getName());
            info.setWorkerId(userInfo.getWorkerId());
            info.setSex(userInfo.getSex());
            userInfoDao.save(info);
        });
    }
}

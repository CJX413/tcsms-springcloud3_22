package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Dao.UserDao;
import com.tcsms.business.Entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class UserServiceImp {
    @Autowired
    UserDao userDao;

    public UserDao getDao() {
        return userDao;
    }

    public boolean isRoleMonitor(String username) {
        return "MONITOR".equals(userDao.findByUsername(username).getRole());
    }
    public boolean isRolAdmin(String username) {
        return "ADMIN".equals(userDao.findByUsername(username).getRole());
    }

    public String[] getPhoneOfMonitor() {
        List<User> list = userDao.findAllByRole("ROLE_MONITOR");
        String[] phone = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            phone[i] = list.get(i).getUsername();
        }
        return phone;
    }

}

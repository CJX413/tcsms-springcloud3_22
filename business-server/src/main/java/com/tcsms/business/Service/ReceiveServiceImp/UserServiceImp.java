package com.tcsms.business.Service.ReceiveServiceImp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tcsms.business.Dao.UserDao;
import com.tcsms.business.Dao.UserInfoDao;
import com.tcsms.business.Entity.User;
import com.tcsms.business.Entity.UserInfo;
import com.tcsms.business.JSON.ResultJSON;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Log4j2
@Service
public class UserServiceImp {
    @Autowired
    UserDao userDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        List<User> list = userDao.findAllByRole("MONITOR");
        String[] phone = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            phone[i] = list.get(i).getUserInfo().getPhoneNumber();
        }
        return phone;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultJSON register(User user) {
        try {
            if (userDao.findByUsername(user.getUsername()) != null) {
                return new ResultJSON(200, false, "账号已存在！", null);
            }
            user.setRole("USER");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(user.getUsername());
            userInfo.setPhoneNumber(user.getUsername());
            userDao.save(user);
            userInfoDao.save(userInfo);
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            e.printStackTrace();
            return new ResultJSON(200, false, e.getMessage(), null);
        }
        return new ResultJSON(200, true, "注册成功！", null);
    }

    public void changeRoleByUsername(String username, String role) throws RuntimeException {
        User user = userDao.findByUsername(username);
        if (user != null) {
            user.setRole(role);
            userDao.save(user);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(User user) throws RuntimeException {
        userInfoDao.deleteById(user.getUsername());
        userDao.deleteById(user.getUsername());
    }

    public JsonArray getAllUsersRole() {
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        Iterable<User> list = userDao.findAll();
        for (User user : list) {
            user.setPassword("");
            JsonObject jsonObject = gson.fromJson(user.toString(), JsonObject.class);
            jsonObject.add("userInfo",
                    gson.fromJson(user.getUserInfo().toString(), JsonObject.class));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


}

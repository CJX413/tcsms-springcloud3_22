package com.tcsms.business.Service.ReceiveServiceImp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tcsms.business.Dao.UserDao;
import com.tcsms.business.Dao.UserInfoDao;
import com.tcsms.business.Entity.User;
import com.tcsms.business.Entity.UserInfo;
import com.tcsms.business.JSON.ResultJSON;
import com.tcsms.business.Utils.JsonHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
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
    @Autowired
    private RedisServiceImp redisServiceImp;
    public static final String ADMIN = "ADMIN";
    public static final String MONITOR = "MONITOR";
    public static final String USER = "USER";
    public static final String SERVER = "SERVER";


    public UserDao getDao() {
        return userDao;
    }

    public boolean isRoleMonitorOrAdmin(String username) {
        return MONITOR.equals(userDao.findByUsername(username).getRole())
                || ADMIN.equals(userDao.findByUsername(username).getRole());
    }

    public boolean isRolAdmin(String username) {
        return ADMIN.equals(userDao.findByUsername(username).getRole());
    }

    public List<String> getPhoneOfMonitor() {
        List<User> list = userDao.findAllByRole(MONITOR);
        List<String> phone = new ArrayList<>();
        for (User user : list) {
            phone.add(user.getUserInfo().getPhoneNumber());
        }
        return phone;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultJSON register(User user) {
        try {
            if (userDao.findByUsername(user.getUsername()) != null) {
                return new ResultJSON(200, false, "账号已存在！", null);
            }
            user.setRole(USER);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(user.getUsername());
            userInfo.setPhoneNumber(user.getUsername());
            userInfoDao.save(userInfo);
            userDao.save(user);
        } catch (RuntimeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            e.printStackTrace();
            return new ResultJSON(200, false, JsonHelper.replaceIllegalChar(e.getMessage()), null);
        }
        redisServiceImp.delete(user.getUsername());
        return new ResultJSON(200, true, "注册成功！", null);
    }

    public void changeRoleByUsername(String username, String role) throws RuntimeException {
        User user = userDao.findByUsername(username);
        if (user != null) {
            user.setRole(role);
            userDao.save(user);
        }
    }

    public ResultJSON updatePassword(String phone, String password) {
        try {
            UserInfo userInfo = userInfoDao.findByPhoneNumber(phone);
            if (userInfo == null) {
                return new ResultJSON(200, false, "该用户不存在！", null);
            }
            User user = userDao.findByUsername(userInfo.getUsername());
            if (user == null) {
                return new ResultJSON(200, false, "该用户不存在！", null);
            }
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userDao.save(user);
        } catch (RuntimeException e) {
            e.printStackTrace();
            new ResultJSON(200, false, JsonHelper.replaceIllegalChar(e.getMessage()), null);
        }
        return new ResultJSON(200, true, "修改密码成功！", null);
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

    public User getAdmin() {
        return userDao.getAdmin();
    }


}

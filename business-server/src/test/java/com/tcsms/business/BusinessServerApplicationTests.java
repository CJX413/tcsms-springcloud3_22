package com.tcsms.business;

import com.tcsms.business.Dao.OperationLogDao;
import com.tcsms.business.Dao.SqlMapper;
import com.tcsms.business.Dao.UserDao;
import com.tcsms.business.Entity.OperationLog;
import com.tcsms.business.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
class BusinessServerApplicationTests {

    @Autowired
    UserDao dao;
    @Autowired
    OperationLogDao operationLogDao;

    @Autowired
    SqlMapper sqlMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void contextLoads() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(bCryptPasswordEncoder.encode("user"));
        user.setRole("USER");
       dao.save(user);
    }

    //@Test
    void getData() {
        int i = 0;
        while (true) {
            try {
                Date date = new Date();
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                OperationLog log1 = new OperationLog(i, "QTZ5010", "D1", "陈嘉兴", "1600300211", time, 116.481231,
                        39.920597, 20.0, 30.0, 50.0, 200.0, 50.0, 5.0, 2);
                OperationLog log2 = new OperationLog(i + 1, "QTZ5013", "D2", "谢植赞", "1600301332", time, 116.481231,
                        39.920597, 20.0, 30.0, 50.0, 200.0, 50.0, 5.0, 2);
                OperationLog log3 = new OperationLog(i + 2, "QTZ5610", "D3", "陈涛", "1600300113", time, 116.481231,
                        39.920597, 20.0, 30.0, 50.0, 200.0, 50.0, 5.0, 2);
//                OperationLog log2=new OperationLog();
//                OperationLog log3=new OperationLog();
//                log1.setDeviceId("D1");
//                log1.setWorkerId();
//                log1.setTime();
//                log1.setOperator();
//                log1.setRadius();
//                log1.setAngle();
//                log1.setDeviceModel();
//                log1.setHeight();
//                log1.setLatitude();
//                log1.set
                operationLogDao.save(log1);
                operationLogDao.save(log2);
                operationLogDao.save(log3);
                i = i + 1;
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

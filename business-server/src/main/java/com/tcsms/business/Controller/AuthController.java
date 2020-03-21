package com.tcsms.business.Controller;


import com.tcsms.business.Entity.User;
import com.tcsms.business.Service.ReceiveServiceImp.UserServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Log4j2
public class AuthController {
    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/auth/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRole("ROLE_MONITOR");
        //user.setRole("ROLE_SERVER");
        User save = userServiceImp.getDao().save(user);
        return save.toString();
    }

    @RequestMapping("/auth/verificationCode")
    public void getVerificationCode() {

    }

    }

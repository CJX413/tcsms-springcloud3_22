package com.tcsms.securityserver.Config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tcsms.securityserver.Service.ServiceImp.RedisServiceImp;
import com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Log4j2
@Component
@PropertySource("classpath:application.properties")
@Order(value = 1)
public class TokenConfig implements CommandLineRunner {
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${spring.application.password}")
    private String applicationPassword;
    @Autowired
    RestTemplateServiceImp restTemplateServiceImp;
    @Autowired
    RedisServiceImp redisServiceImp;

    private static final String LOGIN_URL = "http://business-server/auth/login";

    @Override
    public void run(String... args) {
        log.info(">>>>>>>>>>>>>>初始化Token>>>>>>>>>>>");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", applicationName);
        jsonObject.addProperty("password", applicationPassword);
        jsonObject.addProperty("rememberMe", true);
        JsonObject result = new Gson().fromJson(
                restTemplateServiceImp.sendJson(LOGIN_URL, jsonObject.toString()), JsonObject.class
        );
        String token = "Bearer " + result.get("token").getAsString();
        redisServiceImp.set("token", token);
    }
}

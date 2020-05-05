package com.tcsms.business.Controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tcsms.business.Entity.User;
import com.tcsms.business.Exception.CustomizeException;
import com.tcsms.business.JSON.ResultJSON;
import com.tcsms.business.Service.ReceiveServiceImp.InvitationCodeServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.RedisServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.TxCloudSmsServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.UserServiceImp;
import com.tcsms.business.Utils.JsonHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class AuthController {
    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    TxCloudSmsServiceImp txCloudSmsServiceImp;
    @Autowired
    RedisServiceImp redisServiceImp;
    @Autowired
    InvitationCodeServiceImp invitationCodeServiceImp;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @RequestMapping(value = "/auth/addUser")
    public void addUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("role") String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        userServiceImp.register(user);
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String register(@RequestBody String json) {
        try {
            log.info(json);
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            String phone = jsonObject.get("phone").getAsString();
            String verificationCode = jsonObject.get("verificationCode").getAsString();
            String invitationCode = jsonObject.get("invitationCode").getAsString();
            String pass = jsonObject.get("pass").getAsString();
            if (invitationCodeServiceImp.checkInvitationCode(invitationCode) &&
                    checkVerificationCode(phone, verificationCode)) {
                User user = new User();
                user.setUsername(phone);
                user.setPassword(pass);
                return userServiceImp.register(user).toString();
            }
            return new ResultJSON(200, false, "手机验证码或邀请码错误！", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJSON(200, false, JsonHelper.replaceIllegalChar(e.getMessage()), null).toString();
        }
    }

    @RequestMapping(value = "/auth/updatePassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updatePassword(@RequestBody String json) {
        log.info("/auth/updatePassword");
        try {
            log.info(json);
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            String phone = jsonObject.get("phone").getAsString();
            String verificationCode = jsonObject.get("verificationCode").getAsString();
            String pass = jsonObject.get("pass").getAsString();
            if (checkVerificationCode(phone, verificationCode)) {
                return userServiceImp.updatePassword(phone, pass).toString();
            }
            return new ResultJSON(200, false, "手机验证码或邀请码错误！", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJSON(200, false, JsonHelper.replaceIllegalChar(e.getMessage()), null).toString();
        }
    }

    /**
     * templateId:发送验证码的模板ID
     * @param json
     * @return
     */
    @RequestMapping(value = "/auth/registerVerificationCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String registerVerificationCode(@RequestBody String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        String phone = jsonObject.get("phone").getAsString();
        return txCloudSmsServiceImp.sendSmsVerifyCode(phone, TxCloudSmsServiceImp.REGISTER_TEMPLATE_ID).toString();
    }
    @RequestMapping(value = "/auth/resetPwdVerificationCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String resetPwdVerificationCode(@RequestBody String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        String phone = jsonObject.get("phone").getAsString();
        return txCloudSmsServiceImp.sendSmsVerifyCode(phone, TxCloudSmsServiceImp.RESET_PWD_TEMPLATE_ID).toString();
    }
    private boolean checkVerificationCode(String phone, String verificationCode) throws Exception {
        String value = redisServiceImp.getVerifyCode(phone);
        if (value != null) {
            log.info(value + "---" + verificationCode);
            if (value.equals(verificationCode)) {
                return true;
            } else {
                return false;
            }
        }
        throw new CustomizeException("验证码已失效！请重发。");
    }

}

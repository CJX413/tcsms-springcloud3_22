package com.tcsms.business.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tcsms.business.Entity.*;
import com.tcsms.business.JSON.ResultJSON;
import com.tcsms.business.Service.ReceiveServiceImp.*;
import com.tcsms.business.Utils.JwtTokenUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Log4j2
public class UserController {

    @Autowired
    UserInfoServiceImp userInfoServiceImp;
    @Autowired
    OperatorApplyServiceImp operatorApplyServiceImp;
    @Autowired
    OperatorServiceImp operatorServiceImp;
    @Autowired
    UserServiceImp userServiceImp;
    @Autowired
    RoleApplyServiceImp roleApplyServiceImp;


    @RequestMapping("/isLogin")
    public String isLogin(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String username = JwtTokenUtils.getUsername(token);
        return new ResultJSON(200, true, username, null).toString();
    }

    @RequestMapping("/isAdmin")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public void isAdmin() {

    }

    @RequestMapping("/isMonitor")
    @PreAuthorize(value = "hasAnyAuthority('MONITOR','ADMIN')")
    public void isMonitor() {
    }


    @RequestMapping("/userInfo")
    public String getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String username = JwtTokenUtils.getUsername(token);
        UserInfo userInfo = userInfoServiceImp.findByUserName(username);
        return userInfo.toString();
    }

    @RequestMapping("/role")
    public String getRole(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String username = JwtTokenUtils.getUsername(token);
        String role = userServiceImp.getDao().findByUsername(username).getRole();
        return new ResultJSON(200, true, role, null).toString();
    }

    @RequestMapping("/applyMonitor")
    public String applyMonitor(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String username = JwtTokenUtils.getUsername(token);
        try {
            roleApplyServiceImp.applyMonitor(username);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "成功发出MONITOR权限申请！", null).toString();
    }

    @RequestMapping("/isApplyRole")
    public String isApplyRole(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String username = JwtTokenUtils.getUsername(token);
        if (roleApplyServiceImp.isApplyRole(username)) {
            return new ResultJSON(200, true, "true", null).toString();
        }
        return new ResultJSON(200, true, "false", null).toString();
    }

    @RequestMapping("/allRoleApply")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String allRoleApply() {
        List<RoleApply> list = roleApplyServiceImp.getDao().findAll();
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        for (RoleApply roleApply : list) {
            JsonObject jsonObject = gson.fromJson(roleApply.toString(), JsonObject.class);
            UserInfo userInfo = userInfoServiceImp.findByUserName(roleApply.getUsername());
            jsonObject.add("userInfo", gson.fromJson(userInfo.toString(), JsonObject.class));
            jsonArray.add(jsonObject);
        }
        return new ResultJSON(200, true, "获取所有权限申请成功！", jsonArray).toString();
    }

    @RequestMapping("/allUsersRole")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String allUsersRole() {
        JsonArray jsonArray = userServiceImp.getAllUsersRole();
        return new ResultJSON(200, true, "获取所有用户的权限消息成功！", jsonArray).toString();
    }


    @RequestMapping("/agreeApplyRole")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String agreeApplyRole(@RequestBody String json) {
        RoleApply roleApply = new Gson().fromJson(json, RoleApply.class);
        try {
            roleApplyServiceImp.agreeApplyRole(roleApply);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "同意权限申请成功！", null).toString();
    }

    @RequestMapping("/refuseApplyRole")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String refuseApplyRole(@RequestBody String json) {
        RoleApply roleApply = new Gson().fromJson(json, RoleApply.class);
        try {
            roleApplyServiceImp.refuseApplyRole(roleApply);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "拒绝权限申请成功！", null).toString();
    }

    @RequestMapping("/updateUsersRole")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String updateUsersRole(@RequestBody String json) {
        User user = new Gson().fromJson(json, User.class);
        try {
            userServiceImp.changeRoleByUsername(user.getUsername(), user.getRole());
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "修改用户权限成功！", null).toString();
    }

    @RequestMapping("/deleteUser")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String deleteUser(@RequestBody String json) {
        User user = new Gson().fromJson(json, User.class);
        try {
            userServiceImp.deleteUser(user);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "删除用户成功！", null).toString();
    }

    @RequestMapping("/updateUserInfo")
    public String updateUserInfo(@RequestBody String json, HttpServletRequest request) {
        UserInfo userInfo = new Gson().fromJson(json, UserInfo.class);
        String token = request.getHeader("authorization");
        String username = JwtTokenUtils.getUsername(token);
        userInfo.setUsername(username);
        try {
            userInfoServiceImp.getDao().save(userInfo);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "修改个人信息成功！", null).toString();
    }


    /**
     * 注册驾驶员接口
     *
     * @param json；格式为OperatorApply
     * @param request
     * @return
     */
    @RequestMapping(value = "/applyOperator", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String applyOperator(@RequestBody String json, HttpServletRequest request) {
        OperatorApply operator = new Gson().fromJson(json, OperatorApply.class);
        String token = request.getHeader("authorization");
        String username = JwtTokenUtils.getUsername(token);
        operator.setUsername(username);
        try {
            operatorApplyServiceImp.applyOperator(operator);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "申请驾驶员成功！", null).toString();
    }

    /**
     * 修改注册驾驶员信息接口
     *
     * @param json；格式为OperatorApply
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateOperatorApply", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String updateOperatorApply(@RequestBody String json, HttpServletRequest request) {
        OperatorApply operator = new Gson().fromJson(json, OperatorApply.class);
        String token = request.getHeader("authorization");
        String username = JwtTokenUtils.getUsername(token);
        operator.setUsername(username);
        try {
            operatorApplyServiceImp.updateOperator(operator);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "修改申请成功！", null).toString();
    }

    @RequestMapping("/isAppliedOperator")
    public String isAppliedOperator(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String username = JwtTokenUtils.getUsername(token);
        OperatorApply operatorApply = operatorApplyServiceImp.isAppliedOperator(username);
        if (operatorApply != null) {
            return new ResultJSON(200, true, "true", operatorApply).toString();
        }
        return new ResultJSON(200, true, "false", null).toString();
    }

    @RequestMapping("/isOperator")
    public String isOperator(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String username = JwtTokenUtils.getUsername(token);
        Operator operator = operatorServiceImp.isOperator(username);
        if (operator != null) {
            return new ResultJSON(200, true, "true", operator).toString();
        }
        return new ResultJSON(200, true, "false", null).toString();
    }

    @RequestMapping("/operatorApplyInfo")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String operatorApplyInfo() {
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        operatorApplyServiceImp.getDao().findAll().forEach(operatorApply -> {
            jsonArray.add(gson.fromJson(operatorApply.toString(), JsonElement.class));
        });
        log.info(jsonArray.toString());
        return new ResultJSON(200, true, "获取驾驶员申请表成功！", jsonArray).toString();
    }

    @RequestMapping("/operatorInfo")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String operatorInfo() {
        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();
        operatorServiceImp.getDao().findAll().forEach(operator -> {
            jsonArray.add(gson.fromJson(operator.toString(), JsonElement.class));
        });
        log.info(jsonArray.toString());
        return new ResultJSON(200, true, "获取驾驶员表成功！", jsonArray).toString();
    }

    @RequestMapping(value = "/agreeApplyOperator", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String agreeApplyOperator(@RequestBody String json) {
        try {
            OperatorApply operatorApply = new Gson().fromJson(json, OperatorApply.class);
            operatorApplyServiceImp.agreeApply(operatorApply);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "同意驾驶员申请！", null).toString();
    }

    @RequestMapping(value = "/refuseApplyOperator", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String refuseApplyOperator(@RequestBody String json) {
        OperatorApply operatorApply = new Gson().fromJson(json, OperatorApply.class);
        try {
            operatorApplyServiceImp.refuseApply(operatorApply);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "拒绝驾驶员申请！", null).toString();
    }

    @RequestMapping(value = "/deleteOperator", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String deleteOperator(@RequestBody String json) {
        Operator operator = new Gson().fromJson(json, Operator.class);
        try {
            operatorServiceImp.deleteOperator(operator);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "删除驾驶员成功！", null).toString();
    }

    @RequestMapping(value = "/addOperator", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String addOperator(@RequestBody String json) {
        try {
            Operator operator = new Gson().fromJson(json, Operator.class);
            operatorServiceImp.addOperator(operator);
        } catch (RuntimeException e) {
            return new ResultJSON(200, false, e.getMessage(), null).toString();
        }
        return new ResultJSON(200, true, "注册驾驶员失败！", null).toString();
    }

    @RequestMapping("/usersIsNotOperator")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String usersIsNotOperator() {
        JsonArray jsonArray = new JsonArray();
        Iterable<User> users = userServiceImp.getDao().findAll();
        Gson gson = new Gson();
        users.forEach(user -> {
            if (operatorServiceImp.isOperator(user.getUsername()) == null) {
                Operator operator = new Operator();
                operator.setUsername(user.getUserInfo().getUsername());
                operator.setWorkerId(user.getUserInfo().getWorkerId());
                operator.setName(user.getUserInfo().getName());
                operator.setSpecialOperationCertificateNumber("");
                JsonObject jsonObject = gson.fromJson(operator.toString(), JsonObject.class);
                jsonArray.add(jsonObject);
            }
        });
        return new ResultJSON(200, true, "获取非驾驶员信息成功", jsonArray).toString();
    }

}

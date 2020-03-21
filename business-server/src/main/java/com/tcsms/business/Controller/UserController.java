package com.tcsms.business.Controller;

import com.google.gson.Gson;
import com.tcsms.business.Entity.Operator;
import com.tcsms.business.Entity.OperatorApply;
import com.tcsms.business.Entity.UserInfo;
import com.tcsms.business.JSON.ResultJSON;
import com.tcsms.business.Service.ReceiveServiceImp.OperatorApplyServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.OperatorServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.UserInfoServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.UserServiceImp;
import com.tcsms.business.Utils.JwtTokenUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping("/isLogin")
    public void isLogin() {
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
        if (operatorApplyServiceImp.applyOperator(operator)) {
            return new ResultJSON(200, true, "申请驾驶员成功！", null).toString();
        }
        return new ResultJSON(404, false, "申请驾驶员失败！", null).toString();
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
        if (operatorApplyServiceImp.updateOperator(operator)) {
            return new ResultJSON(200, true, "修改申请成功！", null).toString();
        }
        return new ResultJSON(404, false, "修改申请失败！", null).toString();
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

}

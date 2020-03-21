package com.tcsms.business.Controller;


import com.tcsms.business.JSON.ResultJSON;
import com.tcsms.business.Service.ReceiveServiceImp.TxCloudSmsServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.UserDetailsServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Log4j2
public class ReceiveController {

    @Autowired
    WebSocket webSocket;
    @Autowired
    UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    TxCloudSmsServiceImp txCloudSmsServiceImp;

    /**
     * 接收警报的接口
     *
     * @param json；格式为SendJSON类的toString方法
     * @return
     */
    @PreAuthorize(value = "hasAnyAuthority('SERVER')")
    @RequestMapping(value = "/receiveWarning", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String receiveWarning(@RequestBody String json) {
        log.info("接收到警报warning---------------");
        webSocket.sendWarningToMonitor(json);
        txCloudSmsServiceImp.sendWarningToMonitor(json);
        return new ResultJSON(200, true, "接收到报警信号！", null).toString();
    }
    /**
     * 接收监控器状态的接口
     *
     * @param json；格式为特定的格式，可直接转发给前端
     * @return
     */
    @PreAuthorize(value = "hasAnyAuthority('SERVER')")
    @RequestMapping(value = "/receiveMonitorStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String receiveMonitorStatus(@RequestBody String json) {
        log.info("接收到monitorStatus---------------");
        webSocket.sendMonitorStatusToAdmin(json);
        return new ResultJSON(200, true, "接收到监控器状态！", null).toString();
    }
}

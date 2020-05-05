package com.tcsms.securityserver.Service.ServiceImp;

import com.tcsms.securityserver.Config.TokenConfig;
import com.tcsms.securityserver.Entity.WarningLog;
import com.tcsms.securityserver.Monitor.MonitorManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Log4j2
@Service
public class RestTemplateServiceImp {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    RedisServiceImp redisServiceImp;
    @Autowired
    TokenConfig tokenConfig;

    private static final String WARNING_RECEIVE_URL = "http://business-server/receiveWarning";
    private static final String MONITOR_STATUS_RECEIVE_URL = "http://business-server/receiveMonitorStatus";
    private final static int RESEND_TIMES = 5;

    public String sendJson(String url, String json) {
        String result = "";
        try {
            String token = redisServiceImp.get("token");
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.add("authorization", token);
            HttpEntity<String> formEntity = new HttpEntity<>(json, headers);
            result = restTemplate.postForObject(url, formEntity, String.class);
        } catch (RestClientException e) {
            tokenConfig.run();
            this.reSendJson(url, json);
            e.printStackTrace();
        }
        return result;
    }

    private void reSendJson(String url, String json) {
        boolean success = true;
        for (int i = 0; i < RESEND_TIMES; i++) {
            try {
                String token = redisServiceImp.get("token");
                HttpHeaders headers = new HttpHeaders();
                MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
                headers.setContentType(type);
                headers.add("Accept", MediaType.APPLICATION_JSON.toString());
                headers.add("authorization", token);
                HttpEntity<String> formEntity = new HttpEntity<>(json, headers);
                restTemplate.postForObject(url, formEntity, String.class);
            } catch (RestClientException e) {
                success = false;
                e.printStackTrace();
            }
            if (success) {
                return;
            }
        }
    }

    @Async
    public void sendWarning(WarningLog warningLog) {
        log.info("发送一次警报");
        sendJson(WARNING_RECEIVE_URL, warningLog.toString());
    }


    @Async
    public void sendMonitorStatus() {
        Optional.ofNullable(MonitorManager.getMonitorStatus()).ifPresent(monitorStatus -> {
            sendJson(MONITOR_STATUS_RECEIVE_URL, monitorStatus.toString());
        });
    }
}

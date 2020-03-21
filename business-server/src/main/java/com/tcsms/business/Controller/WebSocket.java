package com.tcsms.business.Controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tcsms.business.Entity.OperationLog;
import com.tcsms.business.JSON.SendJSON;
import com.tcsms.business.Service.ReceiveServiceImp.OperationLogDateServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.RedisServiceImp;
import com.tcsms.business.Service.ReceiveServiceImp.UserServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Log4j2
@Component
@ServerEndpoint(value = "/webSocket/{name}")
public class WebSocket {

    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 标识当前连接客户端的用户名
     */
    private String name;

    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<String, WebSocket> webSocketSet = new ConcurrentHashMap<>();
    /**
     * 用于存储所有连接的用户发送设备运行状况的线程
     */
    private static ConcurrentHashMap<String, Thread> operationLogSendManager = new ConcurrentHashMap<>();

    @Autowired
    private RedisServiceImp redisServiceImp;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private OperationLogDateServiceImp operationLogDateServiceImp;

    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "name") String name) {
        this.session = session;
        this.name = name;
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        webSocketSet.put(name, this);
        log.info(name + "[WebSocket] 连接成功，当前连接人数为：={}", webSocketSet.size());
    }


    @OnClose
    public void OnClose() {
        log.info(this.name);
        log.info(webSocketSet.get(this.name) == null);
        webSocketSet.remove(this.name);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}", webSocketSet.size());
    }

    @OnMessage
    public void OnMessage(String message) {
        try {
            log.info("[WebSocket] 收到消息：{}", message);
            //判断是否需要指定发送，具体规则自定义
            if (message.indexOf("TOUSER") == 0) {
                String name = message.substring(message.indexOf("TOUSER") + 6, message.indexOf(";"));
                AppointSending(name, message.substring(message.indexOf(";") + 1, message.length()));
            } else {
                GroupSending(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发
     *
     * @param message
     */
    public void GroupSending(String message) {
        for (String name : webSocketSet.keySet()) {
            try {
                webSocketSet.get(name).session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定发送
     *
     * @param name
     * @param message
     */
    public void AppointSending(String name, String message) {
        try {
            //log.info(name + "发送消息成功" + "=>" + message);
            log.info(webSocketSet.get(name) == null);
            webSocketSet.get(name).session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToAllUser(String message) {
        for (String key : webSocketSet.keySet()) {
            AppointSending(key, message);
        }
    }

    public void sendWarningToMonitor(String warningJson) {
        String message = new SendJSON(200, "warning", warningJson).toString();
        for (String key : webSocketSet.keySet()) {
            if (userServiceImp.isRoleMonitor(key)) {
                AppointSending(key, message);
            }
        }
    }

    public void sendMonitorStatusToAdmin(String monitorStatus) {
        String message = new SendJSON(200, "monitorStatus", monitorStatus).toString();
        for (String key : webSocketSet.keySet()) {
            if (userServiceImp.isRolAdmin(key)) {
                AppointSending(key, message);
            }
        }
    }

    public void closeOperationLogSendThread(String name) {
        Optional.ofNullable(operationLogSendManager.get(name)).ifPresent(thread -> {
            thread.interrupt();
            operationLogSendManager.remove(name);
        });
    }

    public void openOperationLogSendThread(String name, String deviceId) {
        Thread thread = new Thread(() -> {
            Jedis jedis = redisServiceImp.getJedis();
            try {
                while (!Thread.interrupted()) {
                    JsonObject operationLog = new Gson().fromJson(jedis.get(deviceId), JsonObject.class);
                    SendJSON sendJSON = new SendJSON(200, "operationLog", operationLog.toString());
                    AppointSending(name, sendJSON.toString());
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        });
        Optional.ofNullable(operationLogSendManager.getOrDefault(name, null))
                .ifPresent(oldThread -> {
                    oldThread.interrupt();
                    operationLogSendManager.remove(name);
                });
        operationLogSendManager.put(name, thread);
        thread.start();
    }

    public void openOperationLogDateSendThread(String name, String deviceId, String time) throws ParseException {
        Date datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        String date = new SimpleDateFormat("yyyy_MM_dd").format(datetime);
        ConcurrentLinkedQueue<OperationLog> queue = new ConcurrentLinkedQueue<>();
        Thread thread = new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    log.info("消息队列的长度为："+queue.size());
                    if (queue.size() < 90)
                        operationLogDateServiceImp.queryOperationLogDateByDeviceIdAndTime(queue, deviceId, time, date);
                    Optional.ofNullable(queue.poll()).ifPresent(operationLog -> {
                        SendJSON sendJSON = new SendJSON(200, "operationLogDate", operationLog.toString());
                        AppointSending(name, sendJSON.toString());
                    });
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queue.clear();
            }
        });
        Optional.ofNullable(operationLogSendManager.getOrDefault(name, null))
                .ifPresent(oldThread -> {
                    oldThread.interrupt();
                    operationLogSendManager.remove(name);
                });
        operationLogSendManager.put(name, thread);
        thread.start();
    }

    public void closeOperationLogDateSendThread(String name) {
        Optional.ofNullable(operationLogSendManager.get(name)).ifPresent(thread -> {
            thread.interrupt();
            operationLogSendManager.remove(name);
        });
    }

}

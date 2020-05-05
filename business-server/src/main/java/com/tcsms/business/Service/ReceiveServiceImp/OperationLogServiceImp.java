package com.tcsms.business.Service.ReceiveServiceImp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tcsms.business.Dao.OperationLogDao;
import com.tcsms.business.Entity.DeviceRegistry;
import com.tcsms.business.Entity.OperationLog;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
public class OperationLogServiceImp {
    @Autowired
    private OperationLogDao operationLogDao;

    public OperationLogDao getDao() {
        return operationLogDao;
    }

    public JsonObject getOperationLogByDeviceIdAndTime(String deviceId, String time) throws RuntimeException {
        JsonObject jsonObject = new JsonObject();
        List<OperationLog> list;
        JsonArray timeArray = new JsonArray();
        JsonArray radiusArray = new JsonArray();
        JsonArray angleArray = new JsonArray();
        JsonArray heightArray = new JsonArray();
        JsonArray weightArray = new JsonArray();
        JsonArray torqueArray = new JsonArray();
        JsonArray windVelocityArray = new JsonArray();
        if (time.equals("")) {
            list = operationLogDao.queryOperationLogByDeviceId(deviceId);
        } else {
            list = operationLogDao.queryOperationLogByDeviceIdAndTimeL3600(deviceId, time);
        }
        Optional.ofNullable(list).ifPresent(operationLogs -> {
            operationLogs.forEach(operationLog -> {
                timeArray.add(operationLog.getTime());
                radiusArray.add(operationLog.getRadius());
                angleArray.add(operationLog.getAngle());
                heightArray.add(operationLog.getHeight());
                weightArray.add(operationLog.getWeight());
                torqueArray.add(operationLog.getTorque());
                windVelocityArray.add(operationLog.getWindVelocity());
            });
        });
        jsonObject.add("time", timeArray);
        jsonObject.add("radius", radiusArray);
        jsonObject.add("angle", angleArray);
        jsonObject.add("height", heightArray);
        jsonObject.add("weight", weightArray);
        jsonObject.add("torque", torqueArray);
        jsonObject.add("windVelocity", windVelocityArray);
        return jsonObject;
    }

    public int getCountByDeviceId(String deviceId) throws RuntimeException {
        return operationLogDao.countByDeviceId(deviceId);
    }

    public void queryOperationLogByDeviceIdAndTime(Deque<OperationLog> deque, String deviceId, String time) throws RuntimeException{
        log.info("队列中的消息不足，重新重数据库获取消息");
        Optional.ofNullable(operationLogDao.queryOperationLogByDeviceIdAndTimeL600(deviceId, time))
                .ifPresent(operationLogs -> {
                    operationLogs.forEach(operationLog -> deque.addLast(operationLog));
                });
    }
    @Async
    public void refreshOperationLogQueue(Deque<OperationLog> deque, String deviceId) throws RuntimeException{
        log.info("队列中的消息不足，重新重数据库获取消息");
        OperationLog lastOperationLog = deque.getLast();
        String time = lastOperationLog.getTime();
        Optional.ofNullable(operationLogDao.queryOperationLogByDeviceIdAndTimeL600(deviceId, time))
                .ifPresent(operationLogs -> {
                    operationLogs.forEach(operationLog -> deque.addLast(operationLog));
                });
    }

    public void queryAllDeviceOperationLogDateByDeviceIdAndTime(ConcurrentHashMap<String, ConcurrentHashMap<Long, OperationLog>> hashMap,
                                                                List<DeviceRegistry> devices, String time)
            throws ParseException {
        log.info("初始化运行日志Map");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (DeviceRegistry device : devices) {
            List<OperationLog> list = operationLogDao.queryOperationLogByDeviceIdAndTimeL600(device.getDeviceId(), time);
            if (list != null) {
                for (OperationLog operationLog : list) {
                    ConcurrentHashMap<Long, OperationLog> innerHashMap = new ConcurrentHashMap<>();
                    Date dateTime = simpleDateFormat.parse(operationLog.getTime());
                    innerHashMap.put(dateTime.getTime(), operationLog);
                    hashMap.put(device.getDeviceId(), innerHashMap);
                }
            }
        }
    }

    @Async
    public void refreshAllDeviceOperationLogDateHashMap(ConcurrentHashMap<String, ConcurrentHashMap<Long, OperationLog>> hashMap, List<DeviceRegistry> devices, String time) throws ParseException {
        log.info("队列中的消息不足，重新重数据库获取消息");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long newTime = simpleDateFormat.parse(time).getTime();
        for (DeviceRegistry device : devices) {
            List<OperationLog> list = operationLogDao.queryOperationLogByDeviceIdAndTimeL600(device.getDeviceId(), time);
            if (list != null) {
                ConcurrentHashMap<Long, OperationLog> innerHashMap = hashMap.getOrDefault(device.getDeviceId(), null);
                if (innerHashMap != null) {
                    clearExpiredDataOfMap(innerHashMap, newTime);
                    for (OperationLog operationLog : list) {
                        Date dateTime = simpleDateFormat.parse(operationLog.getTime());
                        innerHashMap.put(dateTime.getTime(), operationLog);
                        hashMap.put(device.getDeviceId(), innerHashMap);
                    }
                }
            }
        }
    }
    private void clearExpiredDataOfMap(ConcurrentHashMap<Long, OperationLog> innerHashMap, Long newTime) {
        innerHashMap.keySet().forEach(oldTime -> {
            if (oldTime <= newTime) {
                innerHashMap.remove(oldTime);
            }
        });
    }
}

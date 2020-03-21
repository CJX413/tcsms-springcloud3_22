package com.tcsms.securityserver.Monitor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tcsms.securityserver.Config.ExceptionInfo;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Exception.SendWarningFailedException;
import com.tcsms.securityserver.Service.ServiceImp.RedisServiceImp;
import com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp;
import com.tcsms.securityserver.Utils.SpringUtil;
import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
public class OtherMonitor extends TcsmsMonitor {
    private final static String THREAD_PREFIX = "其他运行数据监听器-";
    private final static double MODERATE_BREEZE = 5.5;//四级风速
    private final static double STRONG_BREEZE = 10.8;//六级风速

    private final static double SAFE_TORQUE = 0.95;//塔吊的安全力矩百分比

    private DeviceRegistry device;
    private OperationLog operationLog;

    private Double rlt;
    private Map<String, String> operator;

    public OtherMonitor(DeviceRegistry device, Map<String, String> operator) {
        super(THREAD_PREFIX + device.getDeviceId());
        this.device = device;
        this.rlt = device.getRlt();
        this.operator = operator;
    }

    @Override
    public void run() {
        redisServiceImp = SpringUtil.getBean(RedisServiceImp.class);
        restTemplateServiceImp = SpringUtil.getBean(RestTemplateServiceImp.class);
        Jedis jedis = redisServiceImp.getJedis();
        try {
            Gson gson = new Gson();
            while (!Thread.interrupted()) {
                isWait();
                log.info(device.getDeviceId() + "正在运行--------------");
                String value = jedis.get(device.getDeviceId());
                if (value != null) {
                    operationLog = gson.fromJson(value, OperationLog.class);
                    if (isRunning() || operationLog.getWindVelocity() > MODERATE_BREEZE) {
                        setNotRunningTimes(0);
                        List<WarningInfo> warningInfos = isWarning();
                        if (!warningInfos.isEmpty()) {
                            for (WarningInfo warningInfo : warningInfos) {
                                sendWarning(warningInfo, getData());
                            }
                        }
                    } else {
                        int newNotRunningTimes = getNotRunningTimes() + 1;
                        setNotRunningTimes(newNotRunningTimes);
                    }
                } else {
                    int newNotRunningTimes = getNotRunningTimes() + 1;
                    setNotRunningTimes(newNotRunningTimes);
                }
                Thread.sleep(SLEEP_TIME);
            }
        } catch (InterruptedException e) {
            sendException(ExceptionInfo.OTHER_MONITOR_STOP, getData());
        } catch (SendWarningFailedException e) {
            sendException(ExceptionInfo.OTHER_MONITOR_SEND_WARNING, getData());
        } finally {
            jedis.close();
        }

    }


    public List<WarningInfo> isWarning() {
        List<WarningInfo> warning = new ArrayList<>();
        String name = operator.getOrDefault(operationLog.getWorkerId(), "");
        if (operationLog.getTorque() / rlt > SAFE_TORQUE) {
            warning.add(WarningInfo.TORQUE_YELLOW_WARNING);
        }
        if (operationLog.getTorque() > rlt) {
            warning.add(WarningInfo.TORQUE_RED_WARNING);
        }
        if (operationLog.getWindVelocity() >= MODERATE_BREEZE) {
            warning.add(WarningInfo.WIND_VELOCITY_YELLOW_WARNING);
        }
        if (operationLog.getWindVelocity() >= STRONG_BREEZE) {
            warning.add(WarningInfo.WIND_VELOCITY_RED_WARNING);
        }
        if (!operationLog.getOperator().equals(name)) {
            warning.add(WarningInfo.OPERATOR_RED_WARNING);
        }
        return warning;
    }

    @Override
    JsonArray getData() {
        JsonArray data = new JsonArray();
        if (operationLog != null) {
            data.add(new Gson().fromJson(this.operationLog.toString(), JsonObject.class));
        }
        return data;
    }

    /**
     * 用于运行时线程内部判断是否在运行
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        if (operationLog.hashCode() != lastOperationLogHashCode) {
            lastOperationLogHashCode = operationLog.hashCode();
            return true;
        }
        return false;
    }

    /**
     * 用于暂停时线程外部判断是否在运行
     *
     * @param devices
     * @return
     */
    public boolean isRunning(List<String> devices) {
        Gson gson = new Gson();
        long hashCode = 0;
        boolean overWindVelocity = false;
        for (String device : devices) {
            String value = redisServiceImp.get(device);
            if (value != null) {
                OperationLog operationLog = gson.fromJson(redisServiceImp.get(device), OperationLog.class);
                overWindVelocity = operationLog.getWindVelocity() >= MODERATE_BREEZE;
                hashCode = hashCode + operationLog.hashCode();
            }
        }
        return (hashCode != lastOperationLogHashCode) || overWindVelocity;
    }

}

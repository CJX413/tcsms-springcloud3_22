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
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;


@Log4j2
public class DeviceCollisionMonitor extends TcsmsMonitor {
    private final static String THREAD_PREFIX = "设备碰撞监听器-";

    private final static int SAFE_DISTANCE = 2;//塔吊的安全距离

    private DeviceRegistry device_1;
    private DeviceRegistry device_2;

    private OperationLog operationLog_device_1;
    private OperationLog operationLog_device_2;


    private Double distance;
    private Double bearing;

    private Double bigHeight_1;
    private Double bigLength_1;

    private Double bigHeight_2;
    private Double bigLength_2;


    public DeviceCollisionMonitor(DeviceRegistry device_1, DeviceRegistry device_2) {
        super(THREAD_PREFIX + device_1.getDeviceId() + "-" + device_2.getDeviceId());
        this.device_1 = device_1;
        this.device_2 = device_2;
        this.bigHeight_1 = device_1.getBigHeight();
        this.bigLength_1 = device_1.getBigLength();
        this.bigHeight_2 = device_2.getBigHeight();
        this.bigLength_2 = device_2.getBigLength();
        this.distance = getDistance();
        this.bearing = getBearing();

    }

    public double getDistance() {
        GlobalCoordinates source = new GlobalCoordinates(device_1.getLatitude(), device_1.getLongitude());
        GlobalCoordinates target = new GlobalCoordinates(device_2.getLatitude(), device_2.getLongitude());
        GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
        return geodeticCalculator.calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance();
    }

    public double getBearing() {
        double longitudeFrom = device_2.getLongitude();
        double longitudeTo = device_1.getLongitude();
        double latitudeFrom = Math.toRadians(device_2.getLatitude());
        double latitudeTo = Math.toRadians(device_1.getLatitude());
        double longDiff = Math.toRadians(longitudeTo - longitudeFrom);
        double y = Math.sin(longDiff) * Math.cos(latitudeTo);
        double x = Math.cos(latitudeFrom) * Math.sin(latitudeTo) - Math.sin(latitudeFrom) * Math.cos(latitudeTo) * Math.cos(longDiff);
        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

    public boolean isCompleteSafe() {
        if (bigLength_1 + bigLength_2 < distance) {
            return true;
        }
        return false;
    }

    @Override
    List<WarningInfo> isWarning() {
        List<WarningInfo> warning = new ArrayList<>();
        double Ox2 = distance * Math.cos((bearing / 180) * Math.PI);
        double Oy2 = distance * Math.sin((bearing / 180) * Math.PI);
        double safe_r1 = bigLength_1 + SAFE_DISTANCE;
        double safe_r2 = bigLength_2 + SAFE_DISTANCE;
        double r1 = bigLength_1;
        double r2 = bigLength_2;
        double x1 = operationLog_device_1.getRadius() * Math.cos((operationLog_device_1.getAngle() / 180) * Math.PI);
        double y1 = operationLog_device_1.getRadius() * Math.sin((operationLog_device_1.getAngle() / 180) * Math.PI);
        double x2 = operationLog_device_2.getRadius() * Math.cos((operationLog_device_2.getAngle() / 180) * Math.PI) + Ox2;
        double y2 = operationLog_device_2.getRadius() * Math.sin((operationLog_device_2.getAngle() / 180) * Math.PI) + Oy2;
        double p1toO2 = Math.sqrt((x1 - Ox2) * (x1 - Ox2) + (y1 - Oy2) * (y1 - Oy2));
        double p2toO1 = Math.sqrt(x2 * x2 + y2 * y2);
        if (p1toO2 < safe_r2 && p2toO1 < safe_r1) {
            warning.add(WarningInfo.DEVICE_COLLISION_YELLOW_WARNING);
        } else if ((p1toO2 < safe_r2 && p2toO1 > safe_r1) && (bigHeight_1 > bigHeight_2)) {
            warning.add(WarningInfo.DEVICE_COLLISION_YELLOW_WARNING);
        } else if ((p1toO2 > safe_r2 && p2toO1 < safe_r1) && (bigHeight_1 < bigHeight_2)) {
            warning.add(WarningInfo.DEVICE_COLLISION_YELLOW_WARNING);
        }
        if (p1toO2 < r2 && p2toO1 < r1) {
            warning.add(WarningInfo.DEVICE_COLLISION_RED_WARNING);
        } else if ((p1toO2 < r2 && p2toO1 > r1) && (bigHeight_1 > bigHeight_2)) {
            warning.add(WarningInfo.DEVICE_COLLISION_RED_WARNING);
        } else if ((p1toO2 > r2 && p2toO1 < r1) && (bigHeight_1 < bigHeight_2)) {
            warning.add(WarningInfo.DEVICE_COLLISION_RED_WARNING);
        }
        return warning;
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
                log.info(device_1.getDeviceId() + device_2.getDeviceId() + "正在运行--------------");
                String value1 = jedis.get(this.device_1.getDeviceId());
                String value2 = jedis.get(this.device_2.getDeviceId());
                if (value1 != null && value2 != null) {
                    operationLog_device_1 = gson.fromJson(value1, OperationLog.class);
                    operationLog_device_2 = gson.fromJson(value2, OperationLog.class);
                    if (isRunning()) {
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
            sendException(ExceptionInfo.DEVICE_COLLISION_MONITOR_STOP, getData());
        } catch (SendWarningFailedException e) {
            sendException(ExceptionInfo.DEVICE_COLLISION_MONITOR_SEND_WARNING, getData());
        } finally {
            jedis.close();
        }
    }

    @Override
    JsonArray getData() {
        JsonArray data = new JsonArray();
        if (operationLog_device_1 != null && operationLog_device_2 != null) {
            data.add(new Gson().fromJson(operationLog_device_1.toString(), JsonObject.class));
            data.add(new Gson().fromJson(operationLog_device_2.toString(), JsonObject.class));
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
        if ((operationLog_device_1.hashCode() + operationLog_device_2.hashCode()) != lastOperationLogHashCode) {
            lastOperationLogHashCode = operationLog_device_1.hashCode() + operationLog_device_2.hashCode();
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
        int hashCode = 0;
        OperationLog operationLog;
        for (String device : devices) {
            String value = redisServiceImp.get(device);
            if (value != null) {
                operationLog = gson.fromJson(redisServiceImp.get(device), OperationLog.class);
                hashCode = hashCode + operationLog.hashCode();
            }
        }
        return hashCode != lastOperationLogHashCode;
    }

}

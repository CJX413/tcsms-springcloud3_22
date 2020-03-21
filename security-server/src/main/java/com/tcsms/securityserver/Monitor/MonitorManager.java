package com.tcsms.securityserver.Monitor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class MonitorManager {
    private static ConcurrentHashMap<String, TcsmsMonitor> map = new ConcurrentHashMap<>();

    public static boolean turn_on = false;

    public static List<TcsmsMonitor> getMonitor() {
        List<TcsmsMonitor> list = new ArrayList<>();
        for (Map.Entry<String, TcsmsMonitor> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public static int getMonitorCount() {
        return map.size();
    }

    public static int getRunningCount() {
        int count = 0;
        for (Map.Entry<String, TcsmsMonitor> entry : map.entrySet()) {
            if (!entry.getValue().isInterrupted()) {
                count++;
            }
        }
        return count;
    }

    public static JsonObject getMonitorStatus() {
        JsonObject result = new JsonObject();
        result.addProperty("switch", turn_on);
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<String, TcsmsMonitor> entry : map.entrySet()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", entry.getKey());
            jsonObject.addProperty("living", !entry.getValue().isInterrupted());
            jsonObject.addProperty("running", !entry.getValue().isPause());
            jsonArray.add(jsonObject);
        }
        result.add("status", jsonArray);
        return result;
    }

    public static void addMonitor(TcsmsMonitor monitor) {
        map.put(monitor.getName(), monitor);
    }

    public static void shutDownAllMonitor() {
        for (Map.Entry<String, TcsmsMonitor> entry : map.entrySet()) {
            entry.getValue().interrupt();
        }
        map.clear();
    }

    public static void pauseMonitorByName(String deviceName) {
        TcsmsMonitor monitor = map.getOrDefault(deviceName, null);
        if (monitor != null && !monitor.isPause()) {
            monitor.pause();
            log.info(deviceName + "-------这个设备的监听器已经暂停！");
        }
    }

    public static void notifyAllMonitor() {
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            TcsmsMonitor monitor = map.getOrDefault(key, null);
            if (monitor != null && monitor.isPause()) {
                monitor.awake();
                log.info(key + "-------这个设备的监听器已经唤醒！");
            }
        }
    }

    public static void notifyMonitorByName(String deviceName) {
        TcsmsMonitor monitor = map.getOrDefault(deviceName, null);
        if (monitor != null && monitor.isPause()) {
            monitor.awake();
            log.info(deviceName + "-------这个设备的监听器已经唤醒！");
        }
    }
}

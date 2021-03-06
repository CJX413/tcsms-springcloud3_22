package com.tcsms.securityserver.Filter;

import com.google.gson.Gson;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Service.ServiceImp.DeviceRegistryServiceImp;
import com.tcsms.securityserver.Utils.RequestReaderHttpServletRequestWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Log4j2
@WebFilter(filterName = "ReceiveFilter", urlPatterns = {"/operationLog"})
public class ReceiveFilter implements Filter {
    @Autowired
    DeviceRegistryServiceImp deviceRegistryServiceImp;
    private static ConcurrentHashMap<String, Boolean> deviceRegistryHashMap = new ConcurrentHashMap<>();

    public static void updateDeviceRegistryHashMap(DeviceRegistry device) throws RuntimeException {
        log.info("-------------------------------------");
        deviceRegistryHashMap.put(device.getDeviceId(), device.getIsRegistered());
        log.info(deviceRegistryHashMap);
    }

    public static void deleteDeviceRegistryHashMap(DeviceRegistry device) throws RuntimeException {
        deviceRegistryHashMap.remove(device.getDeviceId());
        log.info(deviceRegistryHashMap);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("----------------------->过滤器被创建");
        List<DeviceRegistry> list = deviceRegistryServiceImp.getDao().findAll();
        for (DeviceRegistry deviceRegistry : list) {
            deviceRegistryHashMap.put(deviceRegistry.getDeviceId(), deviceRegistry.getIsRegistered());
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        RequestReaderHttpServletRequestWrapper requestWrapper = new RequestReaderHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        String json = getJson(requestWrapper);
        OperationLog operationLog = new Gson().fromJson(json, OperationLog.class);

        Boolean value = deviceRegistryHashMap.getOrDefault(operationLog.getDeviceId(), null);
        if (value == null) {
            DeviceRegistry deviceRegistry = new DeviceRegistry();
            deviceRegistry.setDeviceModel(operationLog.getDeviceModel());
            deviceRegistry.setIsRegistered(false);
            deviceRegistry.setDeviceId(operationLog.getDeviceId());
            deviceRegistry.setLatitude(operationLog.getLatitude());
            deviceRegistry.setLongitude(operationLog.getLongitude());
            deviceRegistryHashMap.put(deviceRegistry.getDeviceId(), deviceRegistry.getIsRegistered());
            deviceRegistryServiceImp.getDao().save(deviceRegistry);
        } else {
            if (value.equals(true)) {
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("----------------------->过滤器被销毁");
    }

    private String getJson(HttpServletRequestWrapper req) {
        try {
            try (BufferedReader reader = req.getReader()) {
                StringBuilder json = new StringBuilder();
                String s;
                while ((s = reader.readLine()) != null) {
                    json.append(s.trim());
                }
                return json.toString();
            }
        } catch (IOException e) {
            return null;
        }
    }
}

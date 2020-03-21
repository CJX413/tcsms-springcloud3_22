package com.tcsms.securityserver.Service.ServiceImp;


import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisServiceImp implements RedisService {
    @Autowired
    JedisPool jedisPool;

    private static final String REGISTRY_SUFFIX = "_registry";//每台塔吊的注册情况在redis缓存中的key值的后缀

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void set(String key, String value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
        } finally {
            jedis.close();
        }
    }

    //根据key获取value
    public String get(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public void setDeviceRegistry(DeviceRegistry deviceRegistry) {
        set(deviceRegistry.getDeviceId() + REGISTRY_SUFFIX, deviceRegistry.getIsRegistered());
    }

    public String getDeviceRegistry(String deviceId) {
        return get(deviceId + REGISTRY_SUFFIX);
    }

}

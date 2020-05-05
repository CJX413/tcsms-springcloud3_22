package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisServiceImp implements RedisService {
    @Autowired
    JedisPool jedisPool;

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

    public void delete(String key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    public void setVerifyCode(String key, String value, int expireSecond) {
        Jedis jedis = getJedis();
        try {
            jedis.setex(key, expireSecond, value);
        } finally {
            jedis.close();
        }
    }

    public String getVerifyCode(String key) {
        Jedis jedis = getJedis();
        try {
            if (jedis.exists(key)) {
                return jedis.get(key);
            }
            return null;
        } finally {
            jedis.close();
        }
    }
}

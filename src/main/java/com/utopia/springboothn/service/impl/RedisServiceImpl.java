package com.utopia.springboothn.service.impl;

import com.utopia.springboothn.core.RedisLock;
import com.utopia.springboothn.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author hn
 * @date 2018/08/03
 */
@Service(value = "redisService")
public class RedisServiceImpl implements IRedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisLock redisLock;

    @Override
    public boolean set(String key, String value) {
        ValueOperations<String,String> operations=  redisTemplate.opsForValue();
        operations.set(key,value);
        return Boolean.TRUE;
    }

    @Override
    public boolean setIfAbsent(String key, String value ) {
        ValueOperations<String,String> operations =  redisTemplate.opsForValue();
        boolean flag = operations.setIfAbsent(key,value);

        return flag;
    }

    @Override
    public boolean remove(String key) {
        return false;
    }

    @Override
    public String get(String key) {
        ValueOperations<String,String> operations =  redisTemplate.opsForValue();
        String value = operations.get(key);
        return value;
    }

    @Override
    public Boolean exist(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean set(String key, String obj, Long expireTime,TimeUnit timeUnit) {
        ValueOperations<String,String> operations = redisTemplate.opsForValue();
        operations.set(key,obj,expireTime, timeUnit);
        return false;
    }

    @Override
    public boolean getLock(String key, String sessionid) {
       return redisLock.getLock(key,sessionid,6000L);
    }
    @Override
    public boolean unLock(String key, String sessionid) {
        return redisLock.release(key,sessionid);
    }
}

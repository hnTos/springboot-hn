package com.utopia.springboothn.service;

import java.util.concurrent.TimeUnit;

/**
 * @author hn
 * @date 2018/08/03
 * redis的服务类
 */
public interface IRedisService {
    public boolean set(final String key, final String obj);
    public boolean set(final String key, final String obj,Long expireTime,TimeUnit timeUnit);
    public boolean setIfAbsent(final String key, final String obj);
    public boolean remove(final String key);
    public String get(String key);
    public Boolean exist(String key);
    public boolean getLock(String key,String sessionid);
    public boolean unLock(String key,String sessionid);
}

package com.utopia.springboothn.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author hn
 * @date 2018/08/02
 * 实现分布式锁
 */
public class RedisLock {
    private Logger logger = LoggerFactory.getLogger(RedisLock.class);
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 设置键的过期时间为 second 秒
     */
    private static String EX_SECOND = "EX";
    /**
     * 设置键的过期时间为 millisecond 毫秒
     */
    private static String PX_SECOND = "PX";
    /**
     * 只在键不存在时，才对键进行设置操作
     */
    private static String NX_OPERATION = "NX";
    /**
     * 只在键已经存在时，才对键进行设置操作
     */
    private static String XX_OPERATION = "XX";

    /**
     * 可以将当前会话的sessionId当作value，避免unlock的时候误删
     * @param key
     * @param value
     * @param timeOut 默认超时时间单位是millisecond
     * @param retryNum
     * @return
     */
    private  boolean acquire(final String key,String value,long timeOut, int retryNum){
        long begin = System.currentTimeMillis();
        logger.info("  try lock begin:");
        boolean flag =false;
        do {
            retryNum--;
            Object status = redisTemplate.execute(new RedisCallback() {
                @Nullable
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    Object nativeConnection=  redisConnection.getNativeConnection();
                    boolean  status = redisConnection.set(key.getBytes(),value.getBytes(), Expiration.from(timeOut,TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
                    return status;
                }
            });
            flag = Boolean.valueOf(String.valueOf(status));
        } while (!flag && retryNum > 0);
        long end = System.currentTimeMillis();
        logger.info("get lock end elapse:"+(end-begin)+" millisecond");
        return flag;
    }

    /**
     * 可以将当前会话的sessionId当作value，避免unlock的时候误删
     * @param key
     * @param value
     * @param timeOut
     * @return
     */
    public boolean getLock(final String key,final String value,final Long timeOut){
        return acquire(key,value,timeOut,1);
    }

    /**
     * 可以将当前会话的sessionId当作value，避免unlock的时候误删
     * @param key
     * @param value
     * @param timeOut
     * @param timeUnit
     * @return
     */
    public Boolean getLock(final String key, final String value, final Long timeOut, TimeUnit timeUnit){

        return acquire(key,value,getTime(timeOut,timeUnit),1);
    }

    /**
     * 将timeOut 转换成以millisecond为单位
     * @param timeOut
     * @param timeUnit
     * @return
     */
    private Long getTime(Long timeOut,TimeUnit timeUnit){
       return timeUnit.toMillis(timeOut);
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public boolean release(final String key,final String value){
        Long result =  (Long) redisTemplate.execute(new DefaultRedisScript<>(getUnLock_LuaScript(),Long.class), Collections.singletonList(key),value);
        if (result.equals(1L)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    private String getUnLock_LuaScript(){
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call('get',KEYS[1]) == ARGV[1]").append("\n");
        sb.append("then").append("\n");
        sb.append("return redis.call('").append("del',KEYS[1])").append("\n");
        sb.append("else").append("\n");
        sb.append("return 0").append("\n");
        sb.append("end");
        logger.info("\n luaScript:"+sb.toString());
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        return sb.toString();
      //  return sb.toString();
    }

}

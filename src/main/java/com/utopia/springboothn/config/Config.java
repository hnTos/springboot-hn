package com.utopia.springboothn.config;

import com.utopia.springboothn.core.RedisLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hn
 * @date 2018/08/02
 */
@Configuration
public class Config {
    @Bean(value = "redisLock")
    public RedisLock getRedisLockBean(){
        return new RedisLock();
    }
}

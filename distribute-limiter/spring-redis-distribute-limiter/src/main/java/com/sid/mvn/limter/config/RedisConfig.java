package com.sid.mvn.limter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 类概述：Redis相关配置类
 *
 * @author tangqi
 * @date 2021-11-10
 */
@Configuration
public class RedisConfig {

    /**
     * 地址
     */
    @Value(value = "${spring.redis.host:localhost}")
    private String host;


    /**
     * jedis 客户端池
     *
     * @return 客户端池
     */
    @Bean(value = "jedisPool")
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(200);
        return new JedisPool(jedisPoolConfig, host);
    }
}

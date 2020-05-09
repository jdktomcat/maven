package com.jdktomcat.pack.redis.lettuce.runner;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * redis测试类
 *
 * @author: 汤旗
 * @date: 2020-05-09 15:49
 **/
@Log4j
@Component
public class RedisCommandLineRunner implements CommandLineRunner {

    @Autowired
    @Qualifier("singleRedisConnection")
    private StatefulRedisConnection<String, String> connection;

    @Override
    public void run(String... args) {
        RedisCommands<String, String> redisCommands = connection.sync();
        redisCommands.setex("name", 5, "throwable");
        log.info(String.format("Get value:%s", redisCommands.get("name")));
    }
}

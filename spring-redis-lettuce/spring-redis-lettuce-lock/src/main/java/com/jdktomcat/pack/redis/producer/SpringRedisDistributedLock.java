package com.jdktomcat.pack.redis.producer;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 类描述：kafka生产者
 *
 * @author 11072131
 * @date 2020-03-2020/3/28 16:33
 */
@Log4j
@Component
public class SpringRedisDistributedLock {

    @Autowired
    private RedisClient redisClient;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        StatefulRedisPubSubConnection<String, String> statefulRedisPubSubConnection = redisClient.connectPubSub();
        statefulRedisPubSubConnection.addListener(new RedisPubSubListener<String,String>(){

            @Override
            public void message(String s, String s2) {

            }

            @Override
            public void message(String s, String k1, String s2) {

            }

            @Override
            public void subscribed(String s, long l) {

            }

            @Override
            public void psubscribed(String s, long l) {

            }

            @Override
            public void unsubscribed(String s, long l) {

            }

            @Override
            public void punsubscribed(String s, long l) {

            }
        });

    }
}

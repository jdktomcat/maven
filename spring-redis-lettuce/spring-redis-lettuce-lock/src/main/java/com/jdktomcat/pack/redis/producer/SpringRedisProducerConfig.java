package com.jdktomcat.pack.redis.producer;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：Kafka生产者配置
 *
 * @author 11072131
 * @date 2020-03-2020/3/19 20:36
 */
@Configuration
public class SpringRedisProducerConfig {

    /**
     * ip地址
     */
    @Value("${redis.host}")
    private String host;

    /**
     * 端口
     */
    @Value("${redis.port}")
    private Integer port;

    @Bean(destroyMethod = "shutdown")
    ClientResources clientResources() {
        return DefaultClientResources.create();
    }

    @Bean(destroyMethod = "shutdown")
    RedisClient redisClient(ClientResources clientResources) {
        return RedisClient.create(clientResources, RedisURI.create(host, port));
    }

    @Bean(destroyMethod = "close")
    StatefulRedisConnection<String, String> connection(RedisClient redisClient) {
        return redisClient.connect();
    }
}

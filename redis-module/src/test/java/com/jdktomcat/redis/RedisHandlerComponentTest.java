package com.jdktomcat.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

/**
 * 类描述：redis测试类
 *
 * @author 11072131
 * @date 2020-03-2020/3/12 17:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-application.xml")
public class RedisHandlerComponentTest {

    /**
     * redis客户端
     */
    @Autowired
    private JedisCluster jedisCluster;

    @Test
    public void test() {
        String sourceList = "source-list-1";
        String disList = "{" + sourceList + "}:BAK";
        while (true) {
            String message = jedisCluster.brpoplpush(sourceList, disList, 0);
            System.out.println(message);
            jedisCluster.lrem(disList, 1, message);
        }
    }
}

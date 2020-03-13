package com.jdktomcat.redis;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

import java.util.*;

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
        String sourceList = "source-set-1";
        String disList = "{" + sourceList + "}:BAK";

        int pageSize = 100;
        List<String> dataList = new ArrayList<>(pageSize);
        long startTime = System.currentTimeMillis();
        while (true) {
            String message = jedisCluster.brpoplpush(sourceList, disList, 0);
            if (StringUtils.isNotBlank(message)) {
                dataList.add(message);
            }
            if (dataList.size() >= 100 || System.currentTimeMillis() - startTime > 2000) {
                System.out.println(Arrays.toString(dataList.toArray()));
                dataList.clear();
                startTime = System.currentTimeMillis();
            }
            jedisCluster.lrem(disList, 1, message);
        }
    }
}

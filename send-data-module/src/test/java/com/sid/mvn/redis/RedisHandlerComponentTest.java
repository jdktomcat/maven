package com.sid.mvn.redis;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void test() throws UnknownHostException {
        String sourceList = "source-list-1";
        String disList = "{" + sourceList + "}:BAK";

        int pageSize = 100;
        List<String> dataList = new ArrayList<>(pageSize);
        long startTime = System.currentTimeMillis();
        InetAddress inetAddress = InetAddress.getLocalHost();
        String hostAddress = inetAddress.getHostAddress();
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

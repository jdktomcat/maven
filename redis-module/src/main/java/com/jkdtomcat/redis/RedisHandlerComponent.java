package com.jkdtomcat.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;

/**
 * 类描述：redis处理组件
 *
 * @author 11072131
 * @date 2020-03-2020/3/12 17:51
 */
@Component
public class RedisHandlerComponent {

    /**
     * redis客户端
     */
    @Autowired
    private JedisCluster jedisCluster;

    @PostConstruct
    public void init() {
        for (int i = 0; i < RedisConstant.LIST_NUM; i++) {
            String listName = RedisConstant.SEND_CLICK_LIST_NAME + ":" + i;
            String bakListName = String.format(RedisConstant.BAK_LIST_PATTERN, listName);
            while (jedisCluster.rpoplpush(bakListName, listName) == null) ;
        }
    }
}

package com.jdktomcat.redis.task;

import com.jdktomcat.redis.constant.SendDataConstant;
import com.jdktomcat.redis.zk.ZkCuratorDistributedState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 类描述：消息处理组件
 *
 * @author 11072131
 * @date 2020-03-2020/3/23 11:19
 */
@Component
public class MessageHandlerComponent {

    /**
     * redis客户端
     */
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 分布式配置
     */
    @Autowired
    private ZkCuratorDistributedState zkCuratorDistributedState;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        if (!zkCuratorDistributedState.isOpenSend()) {
            return;
        }
        Executor executor = Executors.newFixedThreadPool(SendDataConstant.LIST_NUM);
        for (int i = 0; i < SendDataConstant.LIST_NUM; i++) {
            executor.execute(new MessageHandlerTask(jedisCluster, i));
        }
    }
}

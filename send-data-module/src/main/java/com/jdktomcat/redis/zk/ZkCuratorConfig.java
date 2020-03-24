package com.jdktomcat.redis.zk;

import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述：zk curator配置类
 *
 * @author 11072131
 * @date 2020-03-2020/3/24 14:54
 */
@Data
@Configuration
public class ZkCuratorConfig {

    /**
     * 重试次数
     */
    @Value("${zk.curator.retryCount:5}")
    private int retryCount;

    /**
     * 重试间隔时间
     */
    @Value("${zk.curator.elapsedTimeMs:5000}")
    private int elapsedTimeMs;

    /**
     * 服务器地址
     */
    @Value("${zk.curator.server.address}")
    private String serverAddress;

    /**
     * session超时时间
     */
    @Value("${zk.curator.sessionTimeoutMs:60000}")
    private int sessionTimeoutMs;

    /**
     * 连接超时时间
     */
    @Value("${zk.curator.connectionTimeoutMs:5000}")
    private int connectionTimeoutMs;

    /**
     * 初始化注册实例
     *
     * @return zk实例
     */
    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(serverAddress, sessionTimeoutMs, connectionTimeoutMs, new RetryNTimes(retryCount, elapsedTimeMs));
    }


}

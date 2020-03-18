package com.jdktomcat.redis.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 类描述：kafka配置
 *
 * @author 11072131
 * @date 2020-03-2020/3/18 15:33
 */
@Data
@Component
public class KafkaConfig {

    /**
     * zookeeper地址配置
     */
    @Value("${kafka.zookeeper.address}")
    private String zk;

    /**
     * 主题
     */
    @Value("${kafka.send.click.topic:send_click_topic}")
    private String topic;

    /**
     * 消费组标识
     */
    @Value("${kafka.customer.group.id:ads_marketing}")
    private String groupId;

    /**
     * zk会话超时时长
     */
    @Value("${kafka.zookeeper.session.timeout:40000}")
    private Long zkSessionTimeout;

    /**
     * zk同步时间
     */
    @Value("${kafka.zookeeper.sync.time:200}")
    private Long zkSyncTime;

    /**
     * 自动提交间隔
     */
    @Value("${kafka.auto.commit.interval:1000}")
    private Long autoCommitInterval;
}

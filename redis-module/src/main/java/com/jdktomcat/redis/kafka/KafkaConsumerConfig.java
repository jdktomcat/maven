package com.jdktomcat.redis.kafka;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 类描述：kafka配置
 *
 * @author 11072131
 * @date 2020-03-2020/3/18 15:33
 */
@Data
@Component
public class KafkaConsumerConfig {
    /**
     * 日志
     */
    private static final Logger logger = Logger.getLogger(KafkaConsumerConfig.class);

    /**
     * zookeeper地址配置
     */
    @Value("${kafka.server.address}")
    private String kafkaServer;

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
     * 客户端标识
     */
    @Value("${kafka.customer.client.id:ads_marketing}")
    private String clientId;

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
    private Integer autoCommitInterval;

    /**
     * 一次获取条数
     */
    @Value("${kafka.max.poll.records:10}")
    private Integer maxPollRecords;


    /**
     * 获取配置信息
     *
     * @return 配置
     */
    private Properties getConfigProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaServer);
        props.put("group.id", groupId);
        props.put("client.id", clientId);
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("enable.auto.commit", "false");
        props.put("max.poll.records", maxPollRecords);
        logger.info(String.format("kafka消费者配置信息：%s", props.toString()));
        return props;
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        Properties props = this.getConfigProperties();
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            while (true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100L));
                if (!consumerRecords.isEmpty()) {
                    Iterator<ConsumerRecord<String, String>> iterator = consumerRecords.iterator();
                    while (iterator.hasNext()) {
                        ConsumerRecord consumerRecord = iterator.next();
                        logger.info(String.format("offset:%s,消息键：%s，消息值：%s", consumerRecord.offset(), consumerRecord.key(), consumerRecord.value()));
                    }
                }
            }
        });
    }
}
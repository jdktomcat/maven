package com.jdktomcat.redis.kafka;

import com.jdktomcat.redis.constant.RedisConstant;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
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
    @Value("${kafka.max.poll.records:200}")
    private Integer maxPollRecords;


    /**
     * redis客户端
     */
    @Autowired
    private JedisCluster jedisCluster;


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
        Executor executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                KafkaConsumer<String, Object> kafkaConsumer = new KafkaConsumer<>(props);
                kafkaConsumer.subscribe(Arrays.asList(topic));
                while (true) {
                    ConsumerRecords<String, Object> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100L));
                    if (!consumerRecords.isEmpty()) {
                        Iterator<ConsumerRecord<String, Object>> iterator = consumerRecords.iterator();
                        Map<Integer, List<String>> dataMap = new HashMap<>();
                        while (iterator.hasNext()) {
                            ConsumerRecord consumerRecord = iterator.next();
                            String url = RandomStringUtils.randomAlphabetic(20);
                            Integer queueIndex = Math.abs(url.hashCode() % 24);
                            String message = consumerRecord.value().toString();
                            String member = String.format("%s||%s||%s||%s", url, message, System.currentTimeMillis(), 0);
                            List<String> dataList = dataMap.get(queueIndex);
                            if (CollectionUtils.isEmpty(dataList)) {
                                dataList = new ArrayList<>();
                                dataMap.put(queueIndex, dataList);
                            }
                            dataList.add(member);
                            logger.info(String.format("partition:%s,offset:%s,消息键：%s，消息值：%s", consumerRecord.partition(), consumerRecord.offset(), consumerRecord.key(), consumerRecord.value()));
                        }
                        Long count = 0L;
                        for (Map.Entry<Integer, List<String>> entry : dataMap.entrySet()) {
                            count += jedisCluster.lpush(RedisConstant.SEND_CLICK_LIST_NAME + ":" + entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
                        }
                        try {
                            Thread.sleep(count);
                        } catch (InterruptedException e) {
                            logger.error("消费kafka消息异常：" + e.getMessage());
                        }
                    }
                }
            });
        }
    }
}
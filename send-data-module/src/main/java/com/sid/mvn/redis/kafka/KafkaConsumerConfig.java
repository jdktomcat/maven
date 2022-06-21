package com.sid.mvn.redis.kafka;

import com.sid.mvn.redis.constant.SendDataConstant;
import com.sid.mvn.redis.zk.ZkCuratorDistributedState;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述：kafka配置
 *
 * @author 11072131
 * @date 2020-03-2020/3/18 15:33
 */
@Data
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    /**
     * 日志
     */
    private static final Logger logger = Logger.getLogger("custom");

    /**
     * redis客户端
     */
    @Resource
    private JedisCluster jedisCluster;

    /**
     * 消费组
     */
    @Value("${kafka.customer.group.id:ads_marketing}")
    private String groupIdConfig;

    /**
     * 重置
     */
    @Value("${kafka.auto.offset.reset.config:earliest}")
    private String autoOffsetReset;

    /**
     * kafka服务器配置
     */
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * kafka一次消费信息后多长时间自动提交（ms）
     */
    @Value("${kafka.max.auto.commit.interval:1000}")
    private Integer maxPollInterval;


    /**
     * kafka一次消费多少条数据
     */
    @Value("${kafka.max.poll.record:10}")
    private Integer maxPollRecord;

    /**
     * 并行处理线程数
     */
    @Value("${kafka.customer.poll.concurrency:6}")
    private Integer concurrency;

    /**
     * 分布式配置
     */
    @Resource
    private ZkCuratorDistributedState zkCuratorDistributedState;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollInterval);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecord);
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<?> batchFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // 并行多线程数
        factory.setConcurrency(concurrency);
        //设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.setBatchListener(true);
        //设置提交偏移量的方式
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        //设置每个分区
        factory.getContainerProperties().setSubBatchPerPartition(true);
        return factory;
    }

    @Bean
    public KafkaStreamsConfiguration defaultKafkaStreamsConfig() {
        Map<String, Object> dataMap = consumerConfigs();
        dataMap.put("application.id", "ads");
        return new KafkaStreamsConfiguration(dataMap);
    }

    /**
     * kafka主题监控监听器回调函数
     *
     * @param records 消息
     * @param ack     消息回执
     */
    @KafkaListener(topics = "send_click_topic_13", containerFactory = "batchFactory")
    public void listenBatch(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        long startTime = System.currentTimeMillis();
        if (!zkCuratorDistributedState.isOpenCustomMessage()) {
            return;
        }
        Map<Integer, List<String>> dataMap = new HashMap<>();
        for (ConsumerRecord<String, String> record : records) {
            logger.info(String.format("消费者获取，分区：%s,消息offset:%s,消息键:%s,消息体：%s", record.partition(), record.offset(), record.key(), record.value()));
            String url = RandomStringUtils.randomAlphabetic(20);
            Integer queueIndex = Math.abs(url.hashCode() % SendDataConstant.LIST_NUM);
            String message = record.value();
            String member = String.format("%s||%s||%s||%s", url, message, System.currentTimeMillis(), 0);
            List<String> dataList = dataMap.get(queueIndex);
            if (CollectionUtils.isEmpty(dataList)) {
                dataList = new ArrayList<>();
                dataMap.put(queueIndex, dataList);
            }
            dataList.add(member);
        }
        Long count = 0L;
        for (Map.Entry<Integer, List<String>> entry : dataMap.entrySet()) {
            count += jedisCluster.lpush(SendDataConstant.SEND_CLICK_LIST_NAME + ":" + entry.getKey(), entry.getValue().toArray(new String[0]));
        }
        ack.acknowledge();
        logger.info(String.format("消费消息耗时：%s ms", System.currentTimeMillis() - startTime));
        logger.info(String.format("消息队列中消息总数：%d", count));
        try {
            Thread.sleep(count);
        } catch (InterruptedException e) {
            logger.error("消费kafka消息异常：" + e.getMessage());
        }
    }
}
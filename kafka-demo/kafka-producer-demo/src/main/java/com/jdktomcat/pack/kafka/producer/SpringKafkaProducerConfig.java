package com.jdktomcat.pack.kafka.producer;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 类描述：Kafka生产者配置
 *
 * @author 11072131
 * @date 2020-03-2020/3/19 20:36
 */
@Configuration
public class SpringKafkaProducerConfig {

    /**
     * 日志
     */
    private static final Logger logger = Logger.getLogger(SpringKafkaProducerConfig.class);

    /**
     * kafka服务器配置
     */
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 属性配置
     *
     * @return 集合
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    /**
     * 生产者工厂类
     *
     * @return 生产者工厂类
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * kafka客户端
     *
     * @return kafka客户端
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * 发送数据
     *
     * @param message 数据
     */
    public void send(String message) {
        logger.info("发送消息：" + message);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("send_click_topic_13", message);
        future.addCallback(result -> {
            logger.info("发送成功：" + result.toString());
        }, ex -> {
            logger.error("发送失败：" + ex.toString());
        });
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            for (int index = 0; index < 100; index++) {
                send("send-click-message-data-" + index);
            }
        });
    }
}

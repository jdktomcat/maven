package com.sid.mvn.pack.kafka.consumer;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 类描述：Kafka消费者配置
 *
 * @author 11072131
 * @date 2020-03-2020/3/19 20:36
 */
@Configuration
public class KafkaClientConsumerConfig {

    /**
     * 配置服务器地址
     */
    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    /**
     * 消费组标识
     */
    @Value("${kafka.group.id:marketing_api_23}")
    private String groupId;

    /**
     * 自动提交间隔时长ms
     */
    @Value("${kafka.auto.commit.interval.ms:1000}")
    private Integer autoCommitIntervalMs;

    /**
     * 最大拉取条数
     */
    @Value("${kafka.max.poll.records.show_click_log_v1:10}")
    private Integer maxPollRecord;

    /**
     * 订阅主题名称
     */
    @Value("${kafka.show_click_log_v1.topic:stat_ads-log_show-click}")
    private String topic;

    /**
     * 需要sasl安全协议
     */
    @Value("${kafka.need.sasl:false}")
    private boolean needSasl;

    /**
     * 用户名称
     */
    @Value("${kafka.show.click.username:adverse_market}")
    private String username;

    /**
     * 密码
     */
    @Value("${kafka.show.click.password:bDFncFh5YzRqVQ==}")
    private String password;

    /**
     * 获取消费者配置信息
     *
     * @return 配置信息
     */
    private Properties getConsumerProp() {
        // 参数设置
        Properties props = new Properties();
        // kafka消费的的地址
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 组名 不同组名可以重复消费
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_7");
        // 超时时间
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        // 一次最大拉取的条数
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
//				topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 序列化
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "client_1");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        // 是否开启sasl
        if (needSasl) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            props.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
            props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format("org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";", username, password));
        }
        return props;
    }

    @PostConstruct
    public void init() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(getConsumerProp())) {
                kafkaConsumer.subscribe(Collections.singletonList("stat_ads-log_show-click"));
                while (true) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(500L));
                    for (ConsumerRecord<String, String> record : records) {
                        System.out.println(String.format("kafka消息：%s", record.value()));
                    }
                }
            }
        });
    }
}

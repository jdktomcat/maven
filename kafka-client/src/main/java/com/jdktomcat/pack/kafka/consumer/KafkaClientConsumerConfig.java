package com.jdktomcat.pack.kafka.consumer;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
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

    private static final Logger log = Logger.getLogger(KafkaClientConsumerConfig.class);

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
    @Value("${kafka.max.poll.records.show_click_log_v1:200}")
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
        // 服务地址配置
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 消费组配置
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 自动提交间隔时长配置
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs);
        // 一次拉取最大条数配置
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecord);
        // 是否开启sasl
        if (needSasl) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            props.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
            props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
                    "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";",
                    username, password));
        }
        return props;
    }


    @PostConstruct
    public void init() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Properties props = getConsumerProp();
            log.info(String.format("kafka消费者配置信息：%s", props.toString()));
            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
            kafkaConsumer.subscribe(Collections.singletonList(topic));
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(500L);
                for (ConsumerRecord<String, String> record : records) {
                    log.info(String.format("kafka消息：%s", record.value()));
                }
            }
        });
    }
}

package com.vivo.jdk.pack.kafka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;

/**
 * 类描述：kafka消费者监听器
 *
 * @author 汤旗
 * @date 2019-11-23 17:42
 */
@Configuration
public class SpringKafkaConsumerListener {

    /**
     * 主题
     */
    private static final String TOPIC = "ads-marketing-operation-log-dev";


    /**
     * kafka主题监控监听器回调函数
     *
     * @param message 消息
     */
    @KafkaListener(id = SpringKafkaCustomerConfig.GROUP_ID_CONFIG, topics = TOPIC)
    public void listen(Message<String> message) {
        System.out.println("Received: " + message);
    }
}

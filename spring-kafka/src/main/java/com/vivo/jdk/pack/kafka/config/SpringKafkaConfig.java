package com.vivo.jdk.pack.kafka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;

/**
 * 类描述：
 *
 * @author 汤旗
 * @date 2019-11-23 17:42
 */
@Configuration
public class SpringKafkaConfig {

    /**
     * kafka主题监控监听器回调函数
     *
     * @param message 消息
     */
    @KafkaListener(id = "fooGroup", topics = "ads-marketing-operation-log-dev")
    public void listen(Message<String> message) {
        System.out.println("Received: " + message);
    }
}

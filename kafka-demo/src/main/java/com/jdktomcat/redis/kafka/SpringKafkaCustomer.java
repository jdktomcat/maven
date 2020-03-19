package com.jdktomcat.redis.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类描述：kafka消费者配置
 *
 * @author 汤旗
 * @date 2019-11-26 09:56
 */
@Service
@EnableKafka
public class SpringKafkaCustomer {

    /**
     * 日志
     */
    private static final Logger logger = Logger.getLogger(SpringKafkaCustomer.class);

    /**
     * kafka主题监控监听器回调函数
     *
     * @param records 消息
     * @param ack     消息回执
     */
    @KafkaListener(topics = "send_click_topic",containerFactory = "batchFactory")
    public void listen(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        for (ConsumerRecord<String, String> record : records) {
            logger.info("Received: " + record.key() + "====" + record.value());
        }
        ack.acknowledge();
    }
}

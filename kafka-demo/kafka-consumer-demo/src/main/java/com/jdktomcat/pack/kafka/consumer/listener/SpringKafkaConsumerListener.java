package com.jdktomcat.pack.kafka.consumer.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.Message;
import org.apache.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 类描述：kafka消费者监听器
 *
 * @author 11072131
 * @date 2020-03-2020/3/20 11:07
 */
@Component
public class SpringKafkaConsumerListener {
    /**
     * 日志
     */
    private static final Logger logger = Logger.getLogger(SpringKafkaConsumerListener.class);


    /**
     * kafka主题监控监听器回调函数
     *
     * @param message 消息
     */
    @KafkaListener(topics = "send_click_topic", containerFactory = "consumerFactory")
    public void listen(String message) {
        logger.info("接收到消息：" + message);
    }

    /**
     * kafka主题监控监听器回调函数
     *
     * @param records 消息
     * @param ack     消息回执
     */
    @KafkaListener(topics = "send_click_topic", containerFactory = "batchFactory")
    public void listenBatch(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        for (ConsumerRecord<String, String> record : records) {
            logger.info("Received: " + record.key() + "====" + record.value());
        }
        ack.acknowledge();
    }
}

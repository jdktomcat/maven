package com.jdktomcat.pack.kafka.consumer.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
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
@EnableKafka
public class SpringKafkaConsumerListener {
    /**
     * 日志
     */
    private static final Logger logger = Logger.getLogger(SpringKafkaConsumerListener.class);

    /**
     * kafka主题监控监听器回调函数
     *
     * @param records 消息
     * @param ack     消息回执
     */
    @KafkaListener(containerFactory = "batchFactory", topicPartitions = {@TopicPartition(topic = "send_click_topic", partitions = {"0", "1", "2", "3", "4"})})
    public void listenBatch(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        for (ConsumerRecord<String, String> record : records) {
            logger.info(String.format("消费者获取，分区：%s,消息offset:%s,消息键:%s,消息体：%s", record.partition(), record.offset(), record.key(), record.value()));
        }
        ack.acknowledge();
    }
}

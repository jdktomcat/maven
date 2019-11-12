package com.vivo.jdk.pack.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

/**
 * 类描述：kafka消费者测试类
 *
 * @author 汤旗
 * @date 2019-11-12 17:45
 */
public class KafkaCustomerTest {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        //消费者的组id
        props.put("group.id", "customer1");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //订阅主题列表topic
        TopicPartition partition0 = new TopicPartition("kafka-topic-learn", 0);
        consumer.assign(Arrays.asList(partition0));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            Map<MetricName, ? extends Metric> metricData = consumer.metrics();
            Map<String, List<PartitionInfo>> topicData = consumer.listTopics();
            Set<TopicPartition> dataSet = consumer.assignment();
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value() + "\n");
            }
        }
    }
}

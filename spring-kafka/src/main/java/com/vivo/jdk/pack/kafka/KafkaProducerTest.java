package com.vivo.jdk.pack.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：kafka生产者测试类
 *
 * @author 汤旗
 * @date 2019-11-12 16:29
 */
public class KafkaProducerTest {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //生产者发送消息
        String topic = "kafka-topic-learn";
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 1; i <= 10; i++) {
            String value = "value_" + i;
            ProducerRecord<String, String> msg = new ProducerRecord<>(topic, value);
            producer.send(msg);
        }
        //列出topic的相关信息
        List<PartitionInfo> partitions = producer.partitionsFor(topic);
        for (PartitionInfo p : partitions) {
            System.out.println(p);
        }
        System.out.println("send message over.");
        producer.close(100, TimeUnit.MILLISECONDS);
    }
}

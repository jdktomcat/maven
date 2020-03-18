package com.jdktomcat.redis.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 类描述：kafka配置
 *
 * @author 11072131
 * @date 2020-03-2020/3/18 15:33
 */
@Data
@Component
public class KafkaConsumerConfig {

    /**
     * zookeeper地址配置
     */
    @Value("${kafka.zookeeper.address}")
    private String zk;

    /**
     * 主题
     */
    @Value("${kafka.send.click.topic:send_click_topic}")
    private String topic;

    /**
     * 消费组标识
     */
    @Value("${kafka.customer.group.id:ads_marketing}")
    private String groupId;

    /**
     * zk会话超时时长
     */
    @Value("${kafka.zookeeper.session.timeout:40000}")
    private Long zkSessionTimeout;

    /**
     * zk同步时间
     */
    @Value("${kafka.zookeeper.sync.time:200}")
    private Long zkSyncTime;

    /**
     * 自动提交间隔
     */
    @Value("${kafka.auto.commit.interval:1000}")
    private Long autoCommitInterval;


    /**
     * 初始化
     */
    @PostConstruct
    public void init(){
        Properties props = new Properties();
        props.put("zookeeper.connect", zk);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", zkSessionTimeout);
        props.put("zookeeper.sync.time.ms", zkSyncTime);
        props.put("auto.commit.interval.ms", autoCommitInterval);
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
        final ConsumerIterator<byte[], byte[]> it = stream.iterator();
        Runnable executor = () -> {
            while (it.hasNext()) {
                System.out.println("************** receive：" + new String(it.next().message()));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(executor).start();
    }
}

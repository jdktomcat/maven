package com.jdktomcat.redis.kafka;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 类描述：kafka生产者示例
 *
 * @author 11072131
 * @date 2020-03-2020/3/19 20:09
 */
@Configuration
public class SpringKafkaProducer {

    /**
     * 日志
     */
    private static final Logger logger = Logger.getLogger(SpringKafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送数据
     *
     * @param message 数据
     */
    public void send(String message) {
        logger.info("发送消息：" + message);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("send_click_topic", message);
        future.addCallback(result -> {
            logger.info("发送成功：" + result.toString());
        }, ex -> {
            logger.error("发送失败：" + ex.toString());
        });
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        Executor executor = Executors.newFixedThreadPool(coreNum);
        for (int i = 0; i < coreNum; i++) {
            executor.execute(() -> {
                while (true) {
                    send(RandomStringUtils.randomAlphabetic(20));
                }
            });
        }
    }
}

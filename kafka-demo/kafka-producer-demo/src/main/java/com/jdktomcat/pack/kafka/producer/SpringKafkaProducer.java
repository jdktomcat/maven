package com.jdktomcat.pack.kafka.producer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;

/**
 * 类描述：kafka生产者
 *
 * @author 11072131
 * @date 2020-03-2020/3/28 16:33
 */
@Component
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
     * @param message 消息
     */
    public void send(String message) throws Exception {
        ListenableFuture<SendResult<String, String>> future1 = kafkaTemplate.send("task_log_monitor_topic", message);
        future1.addCallback(result -> {
            logger.info("发送队列1成功：" + result.toString());
        }, ex -> {
            logger.error("发送队列1失败：" + ex.toString());
        });
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        String message = "3434343";
        for (int index = 0; index < 100; index++) {
            try {
                send(message);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
    }
}

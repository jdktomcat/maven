package com.jdktomcat.pack.kafka.producer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
     * @param index 数据索引
     */
    @Transactional(transactionManager = "transactionManager")
    public void send(int index) throws Exception {
        String message1 = "item-1-" + index;
        logger.info("发送队列1消息：" + message1);
        ListenableFuture<SendResult<String, String>> future1 = kafkaTemplate.send("trans-topic-1", message1);
        future1.addCallback(result -> {
            logger.info("发送队列1成功：" + result.toString());
        }, ex -> {
            logger.error("发送队列1失败：" + ex.toString());
        });
        String message2 = "item-2-" + index;
        logger.info("发送队列2消息：" + message1);
        ListenableFuture<SendResult<String, String>> future2 = kafkaTemplate.send("trans-topic-2", message2);
        future2.addCallback(result -> {
            logger.info("发送队列2成功：" + result.toString());
        }, ex -> {
            logger.error("发送队列2失败：" + ex.toString());
        });
        if (index % 2 == 0) {
            throw new Exception("异常，事务回滚！");
        }
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        for (int index = 0; index < 100; index++) {
            try {
                send(index);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
    }
}

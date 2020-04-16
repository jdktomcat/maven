package com.jdktomcat.pack.rabbitmq.consumer;


import com.google.common.util.concurrent.RateLimiter;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbitMQ消费者示例
 *
 * @author jdktomcat
 * @date 2020-04-16
 */
public class RabbitMQConsumerTest {
    /**
     * 消息队列名
     */
    private final static String QUEUE_NAME = "RabbitMQ_Hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        // 指定每秒放1个令牌
        RateLimiter limiter = RateLimiter.create(5000);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.101.25.248");
        //设置端口
        factory.setPort(5672);
        //设置用户名
        factory.setUsername("guest");
        //设置密码
        factory.setPassword("guest");
        // 打开连接和创建频道，与发送端一样
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 创建队列消费者
        channel.basicConsume(QUEUE_NAME, true, "myConsumerTag",
                new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties,
                                               byte[] body) {
                        System.out.println("获取令牌耗时：" + limiter.acquire(1));
                        String message = new String(body);
                        System.out.println("消费消息：" + message);
                    }
                });


    }
}

package com.sid.mvn.pack.rabbitmq.producer;


import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * rabbitMQ生产者示例
 *
 * @author jdktomcat
 * @date 2020-04-16
 */
public class RabbitMQProducerTest {

    /**
     * 消息队列名
     */
    private final static String QUEUE_NAME = "RabbitMQ_Hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接连接到RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
//        // 设置ip
        factory.setHost("10.101.25.248");
        //设置端口
        factory.setPort(5672);
        //设置用户名
        factory.setUsername("guest");
        //设置密码
        factory.setPassword("guest");

        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个频道
        Channel channel = connection.createChannel();
        // 指定一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        Map<String, Object> map = new HashMap<>();
        map.put("java", "hello");
        map.put("RabbitMQ", "Hello");
        //发送的消息
        String message = JSON.toJSONString(map);
        // 往队列中发出一条消息
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}

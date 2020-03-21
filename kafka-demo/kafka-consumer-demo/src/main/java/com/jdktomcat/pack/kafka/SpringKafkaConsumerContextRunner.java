package com.jdktomcat.pack.kafka;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 类描述：Spring消费者上下文运行
 *
 * @author 11072131
 * @date 2020-03-2020/3/20 10:37
 */
public class SpringKafkaConsumerContextRunner {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-application.xml");
        System.in.read();
        applicationContext.close();
    }
}

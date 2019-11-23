package com.vivo.jdk.pack.kafka;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 类描述：Spring-kafka测试类
 *
 * @author 汤旗
 * @date 2019-11-23 17:38
 */
@Log4j
@SpringBootApplication
public class SpringKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaApplication.class, args);
    }
}

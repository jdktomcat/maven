package com.sid.mvn.limter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author tangqi
 */
@SpringBootApplication
public class SpringRedisDistributeLimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedisDistributeLimiterApplication.class, args);
    }

}

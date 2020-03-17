package com.jkdtomcat.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 类描述：SpringContext执行器
 *
 * @author 11072131
 * @date 2020-03-2020/3/17 11:10
 */
public class SpringContextRunner {

    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("spring-application.xml");
        System.in.read();
    }
}

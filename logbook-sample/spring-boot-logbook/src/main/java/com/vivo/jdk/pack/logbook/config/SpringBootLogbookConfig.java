package com.sid.mvn.pack.logbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.CurlHttpLogFormatter;
import org.zalando.logbook.HttpLogFormatter;

/**
 * 类概述：配置类
 *
 * @author tangqi
 * @date 2021-09-19
 */
@Configuration
public class SpringBootLogbookConfig {

    @Bean
    public HttpLogFormatter httpLogFormatter() {
        // 使用默认的http日志格式
        return new CurlHttpLogFormatter();
    }
}

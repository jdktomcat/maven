package com.vivo.jdk.pack.logbook.config;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 〈连接池管理〉
 *
 * @author yinglie
 * @date 2019-04-09
 * @since 1.0.0
 */
@Configuration
public class PoolingHttpClientConnectionManagerBean {

    /**
     * 连接池总路由最大连接数
     */
    private static final int CONN_MAX_TOTAL = 500;

    /**
     * 连接池每个路由最大连接数
     */
    private static final int MAX_PRE_ROUTE = 300;

    /**
     * 连接存活时间，单位为s
     */
    private static final int TIME_TO_LIVE = 60;

    private final static int VALIDATE_AFTER_INACTIVITY  = 1000;

    @Bean("poolingClientConnectionManager")
    public PoolingHttpClientConnectionManager poolingClientConnectionManager() {

        PoolingHttpClientConnectionManager
            poolingClientConnectionManager = new PoolingHttpClientConnectionManager(TIME_TO_LIVE, TimeUnit.SECONDS);
        poolingClientConnectionManager.setMaxTotal(CONN_MAX_TOTAL);
        poolingClientConnectionManager.setDefaultMaxPerRoute(MAX_PRE_ROUTE);
        poolingClientConnectionManager.setValidateAfterInactivity(VALIDATE_AFTER_INACTIVITY);

        return poolingClientConnectionManager;
    }
}

package com.sid.mvn.pack.logbook.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 类概述：OkHttp管理工厂
 *
 * @author tangqi
 * @date 2021-09-27
 */
@Component
public class OkHttp2ManagerFactory implements FactoryBean<OkHttpClient>, InitializingBean, DisposableBean {


    @Override
    public void destroy() throws Exception {
    }

    @Override
    public OkHttpClient getObject() throws Exception {
//        OkHttpClient client = new OkHttpClient().newBuilder().connectionPool().build();
//        return client;
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}

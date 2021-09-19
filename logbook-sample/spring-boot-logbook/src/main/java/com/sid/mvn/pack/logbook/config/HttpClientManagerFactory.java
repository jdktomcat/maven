package com.sid.mvn.pack.logbook.config;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.zalando.logbook.*;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * http客户端管理工厂
 *
 * @author tangqi
 * @date 2021-02-20
 */
@Component
public class HttpClientManagerFactory implements FactoryBean<CloseableHttpClient>, InitializingBean, DisposableBean {
    @Resource(name = "poolingClientConnectionManager")
    private PoolingHttpClientConnectionManager poolingClientConnectionManager;

    /**
     * 连接超时时间
     */
    private static final int CONNECT_TIME_OUT = 2000;

    /**
     * 从连接池中取连接的超时时间
     */
    private static final int CONNECT_REQUEST_TIME_OUT = 2000;

    /**
     * 请求超时时间
     */
    private static final int SOCKET_TIME_OUT = 10000;

    /**
     * 最大重试次数，默认为2次
     */
    private static final int MAX_RETRY_COUNT = 2;

    /**
     * FactoryBean生成的目标对象
     */
    private CloseableHttpClient client;

    /**
     * 销毁上下文时，销毁HttpClient实例
     */
    @Override
    public void destroy() throws Exception {
        /*
         * 调用httpClient.close()会先shut down connection manager，然后再释放该HttpClient所占用的所有资源，
         * 关闭所有在使用或者空闲的connection包括底层socket。由于这里把它所使用的connection manager关闭了，
         * 所以在下次还要进行http请求的时候，要重新new一个connection manager来build一个HttpClient,
         * 也就是在需要关闭和新建Client的情况下，connection manager不能是单例的.
         */
        if (null != this.client) {
            this.client.close();
        }
    }

    /**
     * 初始化实例
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 建议此处使用HttpClients.custom的方式来创建HttpClientBuilder，而不要使用HttpClientBuilder.create()方法来创建HttpClientBuilder
        // 从官方文档可以得出，HttpClientBuilder是非线程安全的，但是HttpClients确实Immutable的，immutable 对象不仅能够保证对象的状态不被改变，
        // 而且还可以不使用锁机制就能被其他线程共享
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT)
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setSocketTimeout(SOCKET_TIME_OUT)
                .build();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }}, new SecureRandom());

        Logbook logbook = Logbook.builder().sink(new DefaultSink(new CurlHttpLogFormatter(), new DefaultHttpLogWriter())).build();

        this.client = HttpClients.custom().setConnectionManager(poolingClientConnectionManager)
                .setRetryHandler((exception, executionCount, context) -> {
                    // Do not retry if over max retry count,如果重试次数超过了retryTime,则不再重试请求
                    if (executionCount >= MAX_RETRY_COUNT) {
                        return false;
                    }
                    // 服务端断掉客户端的连接异常
                    if (exception instanceof NoHttpResponseException) {
                        return true;
                    }
                    // time out 超时重试
                    if (exception instanceof InterruptedIOException) {
                        return true;
                    }
                    // Unknown host
                    if (exception instanceof UnknownHostException) {
                        return false;
                    }
                    // SSL handshake exception
                    if (exception instanceof SSLException) {
                        return false;
                    }
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();

                    return !(request instanceof HttpEntityEnclosingRequest);
                })
                .setSslcontext(sslContext)
                .setDefaultRequestConfig(requestConfig)
                .addInterceptorFirst(new LogbookHttpRequestInterceptor(logbook))
                .addInterceptorFirst(new LogbookHttpResponseInterceptor())
                .build();
    }

    /**
     * 返回实例的类型
     */
    @Override
    public CloseableHttpClient getObject() {
        return this.client;
    }

    @Override
    public Class<?> getObjectType() {
        return (this.client == null ? CloseableHttpClient.class : this.client.getClass());
    }

    /**
     * 构建的实例为单例
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}


package com.vivo.jdk.pack.logbook.component;

import com.vivo.jdk.pack.logbook.config.HttpClientManagerFactory;
import com.vivo.jdk.pack.logbook.util.UriUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * HTTP服务接口实现
 *
 * @author tangqi
 * @date 2021-02-20
 */
@Component
public class HttpServiceComponent {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServiceComponent.class);

    /**
     * HTTP客户端管理工厂
     */
    @Autowired
    private HttpClientManagerFactory httpClientManagerFactory;

    /**
     * 发送普通POST请求，带Header
     *
     * @param url         请求路径
     * @param queryString 查询参数
     * @param headers     头部
     * @param body        请求体
     * @return 响应结果
     */
    public String post(String url, Map<String, String> queryString, Map<String, String> headers, String body) {
        HttpPost httpPost = new HttpPost(UriUtil.buildUri(url, queryString));
        httpPost.setHeader(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
        httpPost.setEntity(new StringEntity(body, "UTF-8"));
        Optional.ofNullable(headers).ifPresent(
                v -> v.forEach((key, value) -> httpPost.setHeader(new BasicHeader(key, value))));
        return executeRequest(httpPost);
    }

    /**
     * 发送普通GET请求，带Header
     *
     * @param url     请求地址
     * @param params  查询参数
     * @param headers 头部信息
     * @return 请求响应结果
     */
    public String get(String url, Map<String, String> params, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(UriUtil.buildUri(url, params));
        Optional.ofNullable(headers).ifPresent(
                v -> v.forEach((key, value) -> httpGet.setHeader(new BasicHeader(key, value))));
        return executeRequest(httpGet);
    }

    /**
     * GET请求
     *
     * @param url         请求地址
     * @param queryString 查询参数
     * @return 返回结果
     */
    public String get(String url, Map<String, String> queryString) {
        return executeRequest(new HttpGet(UriUtil.buildUri(url, queryString)));
    }


    private String executeRequest(HttpRequestBase httpRequestBase) {
        HttpEntity entity = null;
        HttpResponse httpResponse;
        try {
            //真实业务请求
            HttpClient httpClient = httpClientManagerFactory.getObject();
            if (httpClient != null) {
                httpResponse = httpClient.execute(httpRequestBase);
                if (httpResponse == null) {
                    LOGGER.warn("execute request fail. url={}, response={}", httpRequestBase.getURI(),
                            "null");
                    return "";
                }
                if (!isNeedReadStringEntity(httpResponse)) {
                    LOGGER.warn("execute request fail. url={}, response={}", httpRequestBase.getURI(),
                            httpResponse.getStatusLine());
                    return httpResponse.toString();
                }
                entity = httpResponse.getEntity();
                if (entity == null) {
                    LOGGER.warn("response entity is null. url={}, response={}", httpRequestBase.getURI(),
                            httpResponse.getStatusLine());
                    return httpResponse.toString();
                }
                return EntityUtils.toString(entity, "UTF-8");
            } else {
                LOGGER.error("get http client fail!");
                return "";
            }
        } catch (Exception ex) {
            LOGGER.warn("executeRequest unknown error. url={}, e={}", httpRequestBase.getURI(), ex);
            return ex.getMessage();
        } finally {
            consumeEntity(entity);
            httpRequestBase.releaseConnection();
        }
    }

    /**
     * 如果接口返回500/200需要尝试读取response entity
     *
     * @param httpResponse 响应
     * @return true：需要 false：不需要
     */
    private boolean isNeedReadStringEntity(HttpResponse httpResponse) {
        return httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK || httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }


    private void consumeEntity(HttpEntity entity) {
        try {
            if (entity == null) {
                return;
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            LOGGER.warn("consumeEntity exception.", e);
        }
    }
}

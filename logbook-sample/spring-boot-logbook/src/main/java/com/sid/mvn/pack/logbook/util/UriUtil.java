package com.sid.mvn.pack.logbook.util;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Optional;

/**
 * uri工具类
 *
 * @author tangqi
 * @date 2021-02-21
 */
public class UriUtil {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UriUtil.class);

    /**
     * 构建uri
     *
     * @param url        url
     * @param queryParam 查询参数集合
     * @return URI对象
     */
    public static URI buildUri(String url, Map<String, String> queryParam) {
        try {
            URIBuilder builder = new URIBuilder(url);
            Optional.ofNullable(queryParam).ifPresent(v -> v.forEach(builder::addParameter));
            return builder.build();
        } catch (Exception e) {
            LOGGER.warn("buildUri error. url={}, qs={},e={}", url, queryParam, e);
            return null;
        }
    }

    /**
     * 解码
     *
     * @param content 内容
     * @return 解码后内容
     */
    public static String decode(String content) {
        try {
            return URLDecoder.decode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("decode error. content={},e={}", content, e);
            return "";
        }
    }
}

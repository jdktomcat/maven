package com.jdktomcat.redis.util;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * 类描述：发送工具类
 *
 * @author 11072131
 * @date 2020-03-2020/3/17 14:33
 */
public class HttpSendUtil {

    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger("send");

    /**
     * 发送信息
     *
     * @param url      第三方接口路径
     * @param dataList 点击数据
     * @return 成功：true 失败：false
     */
    public static Boolean send(String url, List<String> dataList) {
        boolean sendOk = Math.abs(url.hashCode()) % 2 == 0;
        logger.info(String.format("接口：%s 发送数据：%s 发送结果:%s 记录日志", url, Arrays.toString(dataList.toArray()), sendOk));
        return sendOk;
    }
}

package com.jdktomcat.redis;

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
     * 发送信息
     *
     * @param url      第三方接口路径
     * @param dataList 点击数据
     * @return 成功：true 失败：false
     */
    public static boolean send(String url, List<String> dataList) {
        boolean sendOk;
        System.out.println(String.format("接口：%s 发送数据：%s", url, Arrays.toString(dataList.toArray())));
        if (url.hashCode() % 2 == 0) {
            sendOk = true;
        } else {
            sendOk = false;
        }
        // 打印日志
        System.out.println("记录日志");
        return sendOk;
    }
}

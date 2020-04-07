package com.jdktomcat.pack.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;

/**
 * 类描述：测试服务
 *
 * @author 11072131
 * @date 2020-04-2020/4/7 19:35
 */
public class HelloWorldService {

    @SentinelResource("HelloWorld")
    public void helloWorld() {
        // 资源中的逻辑
        System.out.println("hello world");
    }
}

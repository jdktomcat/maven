package com.sid.mvn.limter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类概述：
 *
 * @author tangqi
 * @date 2021-11-11
 */
@Slf4j
@RestController
public class DistributeLimiterController {


    /**
     * 数据接收接口
     *
     * @return 响应体
     */
    @RequestMapping(method = RequestMethod.GET, value = "/call")
    public String call() {
        return "call";
    }
}

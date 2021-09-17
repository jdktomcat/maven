package com.vivo.jdk.pack.logbook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类概述：日志记录控制器
 *
 * @author tangqi
 * @date 2021-09-17
 */
@RestController
public class LogbookController {

    /**
     * 数据接收接口
     *
     * @return 响应体
     */
    @RequestMapping(method = RequestMethod.GET, value = "/logbook")
    public String receiveData() {
        return "Logbook";
    }
}

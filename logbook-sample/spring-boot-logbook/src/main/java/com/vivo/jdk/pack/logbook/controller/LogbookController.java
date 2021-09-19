package com.sid.mvn.pack.logbook.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 类概述：日志记录控制器
 *
 * @author tangqi
 * @date 2021-09-17
 */
@Slf4j
@RestController
public class LogbookController {

    /**
     * 数据接收接口
     *
     * @return 响应体
     */
    @RequestMapping(method = RequestMethod.GET, value = "/logbook")
    public String receiveData(HttpServletRequest request) {
        log.info("request from:{}", request.getRemoteHost());
        return "Logbook";
    }
}

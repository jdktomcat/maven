package com.sid.mvn.pack.logbook.controller;

import com.sid.mvn.pack.logbook.component.HttpServiceComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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
     * http请求服务组件
     */
    @Autowired
    private HttpServiceComponent httpServiceComponent;

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

    /**
     * 请求转发接口
     *
     * @return 响应体
     */
    @RequestMapping(method = RequestMethod.GET, value = "/redirect")
    public String redirect() {
        String url = "http://apoi.poi.amap.com/bgc/store/push-link-parameter?uid=62648947&v=2.1.6&shopId=xgc_606d89aa5d00cb0703c33bef%7Cxiniu_xgc_bgc&order_type=1&tid=62648947&timestamp=1617194147776";
        return httpServiceComponent.get(url, new HashMap<>());
    }
}

package com.sid.mvn.test.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类概述：home控制类
 *
 * @author tangqi
 * @date 2021-12-02
 */
@RestController
public class HomeController {

    @RequestMapping("/")
    public String greeting() {
        return "Hello, World";
    }
}

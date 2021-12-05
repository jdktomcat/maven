package com.sid.mvn.test.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 类概述：主页控制器测试类
 *
 * @author tangqi
 * @date 2021-12-02
 */
@SpringBootTest
public class HomeControllerTests {

    @Autowired
    private HomeController homeController;

    @Test
    public void contextLoads() {
        assertThat(homeController).isNotNull();
    }
}

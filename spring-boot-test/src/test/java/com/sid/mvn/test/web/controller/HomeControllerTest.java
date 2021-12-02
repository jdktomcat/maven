package com.sid.mvn.test.web.controller;

import com.sid.mvn.test.SpringBootTestApplication;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 类概述：主页控制器测试类
 *
 * @author tangqi
 * @date 2021-12-02
 */
@SpringBootTest(classes = {SpringBootTestApplication.class})
public class HomeControllerTest {

    @Autowired
    private HomeController homeController;

    @Test
    public void contextLoads() {
        assertThat(homeController).isNotNull();
    }
}

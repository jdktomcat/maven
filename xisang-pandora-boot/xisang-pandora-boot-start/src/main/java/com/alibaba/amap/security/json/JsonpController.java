package com.alibaba.amap.security.json;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JSONP的例子，详见 http://gitlab.alibaba-inc.com/middleware-container/pandora-boot/wikis/spring-boot-security-jsonp
 * 
 * @author chengxu
 */
@RestController
@RequestMapping("/jsonp")
public class JsonpController {

    @RequestMapping(value = "/data")
    public Map<String, Object> data() {
        Map<String, Object> data = new HashMap<String, Object>(8);
        data.put("name", "PandoraBoot");
        data.put("age", "30");
        data.put("html-code", "<html>");
        data.put("url", "http://www.alibaba.com/?q=hello,world");
        return data;
    }

    @RequestMapping(value = "/raw_data")
    public User rawData() {
        User user = new User();
        user.setAge(18);
        user.setName("<script>alert(1);</script>");
        return user;
    }
}

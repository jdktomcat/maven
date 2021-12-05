package com.alibaba.amap.security.xss;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 使用了XSS过滤的例子，详见 http://gitlab.alibaba-inc.com/middleware-container/pandora-boot/wikis/spring-boot-security-xss
 * 
 * @author chengxu
 */
@Controller
@RequestMapping("/xss")
public class XssController {

    @RequestMapping("/form")
    public String form(Model model) {
        model.addAttribute("ignoredName", "<script>alert(\"ignoredName\")</script>");
        return "security/xss/form";
    }

    @RequestMapping("/ignored")
    public String ignored(Model model) {
        return "security/xss/ignored";
    }

    @RequestMapping("/save")
    public String save(@RequestParam(required = true) String name, Model model) {
        model.addAttribute("name", name);
        return "security/xss/submit";
    }
}

package com.alibaba.amap.security.http;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.security.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * HTTP安全域名校验的例子，详见 http://gitlab.alibaba-inc.com/middleware-container/pandora-boot/wikis/spring-boot-security-http
 * 
 * @author chengxu
 */
@Controller
@RequestMapping("/http")
public class RedirectController {

    @RequestMapping("/redirect")
    public String gotoURL(@RequestParam String url) {
        return SecurityUtil.getSafeUrl("redirect:" + url);
    }

    @RequestMapping("/page")
    public String page(HttpServletRequest request, Model model) {
        String from = request.getHeader("referer");
        model.addAttribute("from", from);
        return "security/http/page";
    }
}

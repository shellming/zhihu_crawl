package com.shellming.zhihu.controllers;

import com.google.gson.Gson;
import com.shellming.zhihu.models.User;
import com.shellming.zhihu.services.DuanziSelectService;
import com.shellming.zhihu.utils.ZhiHuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ruluo1992 on 10/1/2016.
 */
@RestController
@Profile("!test")
public class TestController {
    public static Gson gson = new Gson();

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    private DuanziSelectService duanziSelectService;

    @RequestMapping(value = "/duanzi")
    @ResponseBody
    public String duanzi() {
        List<String> duanzi = duanziSelectService.getDuanziUrls();
        StringBuilder sb = new StringBuilder();
        for (String d : duanzi) {
            String content = String.format("<a href=\"%s\">%s</a>", d, d);
            sb.append(content).append("<br>");
        }
        return sb.toString();
    }

    @RequestMapping(value = "/clear")
    @ResponseBody
    public void clear() {
        duanziSelectService.clear();
    }

    @RequestMapping(value = "/index")
    @ResponseBody
    public String test() {
        User user = new User("wawaye", "123");
        user.hash = "a153a311a8d1bb73e250ba548a6d65a0";
        try {
            ZhiHuUtil.getFollowee(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "hello world";
    }
}

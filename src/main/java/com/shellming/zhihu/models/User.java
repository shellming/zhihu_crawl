package com.shellming.zhihu.models;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.Objects;

/**
 * Created by ruluo1992 on 10/1/2016.
 */
public class User {
    public Integer id;
    public String url = "";
    public String name = "";
    public Integer followee = 0;
    public String hash = "";
//    public Long last_check = 0L;
//    public Long last_answer = 0L;

    public User(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public User() {

    }

    public static User fromMap(Map map) {
        String url = String.valueOf(map.get("url"));
        String name = String.valueOf(map.get("name"));

        return new User(url, name);
    }

    public static User fromElementInFollowee(Element element) {
        element = element.getElementsByClass("zm-profile-section-item").first();
        Element rightElement = element.getElementsByClass("zg-right").first();
        Elements children = rightElement.children();
        if (children == null || children.first() == null) {
            return null;
        }
        Element followBtn = children.first();
        String hash = followBtn.attr("data-id");
        User user = fromElementInAnswer(element);
        if (user == null) {
            return null;
        } else {
            user.hash = hash;
            return user;
        }
    }

    public static User fromElementInAnswer(Element element) {
        Element userElement = element.getElementsByClass("author-link").first();
        if (userElement == null) {
            return null;
        } else {
            String usrUrl = userElement.attr("href");
            int lastIndex = usrUrl.lastIndexOf("/");
            usrUrl = usrUrl.substring(lastIndex + 1);
            String name = userElement.html();
            return new User(usrUrl, name);
        }
    }
}

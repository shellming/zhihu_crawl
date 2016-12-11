package com.shellming.zhihu.models;

import org.jsoup.nodes.Element;

/**
 * Created by ruluo1992 on 10/1/2016.
 */
public class Question {
    public String url = "";
    public String title = "";

    public Question() {

    }

    public Question(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public static Question fromElement(Element element) {
        Element qElement = element.getElementsByClass("question_link").first();
        if (qElement == null) {
            return new Question();
        } else {
            String url = qElement.attr("href");
            int lastIndex = url.lastIndexOf("/");
            url = url.substring(lastIndex + 1);
            String title = qElement.html();
            return new Question(title, url);
        }
    }
}

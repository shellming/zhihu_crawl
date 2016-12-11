package com.shellming.zhihu.models;

import java.util.Map;

/**
 * Created by ruluo1992 on 10/2/2016.
 */
public class Comment {
    public String url;
    public String id;
    public String content;
    public User user;

    public static Comment fromMap(Map map) {
        String url = map.get("href").toString();
        Double id = (Double) map.get("id");
        Map author = (Map) map.get("author");
        String content = map.get("content").toString();

        Comment comment = new Comment();
        comment.url = url;
        comment.id = String.valueOf(id.longValue());
        comment.content = content;
        comment.user = User.fromMap(author);
        return comment;
    }
}

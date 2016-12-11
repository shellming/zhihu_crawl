package com.shellming.zhihu.models;

import com.shellming.zhihu.utils.DomUtil;
import com.shellming.zhihu.utils.ZhiHuUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by ruluo1992 on 10/1/2016.
 */
public class Answer {
//    public Question question;
//    public User user;
//    public String id;
//    public String url = "";
//    public String content = "";
//    public Integer upCount = 0;
//    public List<Comment> comments;
    public Integer id;
    public String url = "";
    public String userUrl = "";
    public String questionUrl = "";
    public Integer needCheck = 1;
    public String token = "";
    public Long createTime = 0L;
    public Integer commentCount = 0;

    public static Answer fromElement(Element element) {
        element = element.getElementsByClass("zm-item").first();
        if (element == null ||
                (!"".equals(element.attr("data-type")) && !"Answer".equals(element.attr("data-type")))) {
            return null;
        }
        Answer answer = new Answer();
        User user = User.fromElementInAnswer(element);
        if (user != null) {
            answer.userUrl = user.url;
        }
//        Element qElement = element.getElementsByClass("question_link").first();
//        if (qElement != null) {
//            String answerUrl = qElement.attr("href");
//            String[] parts = answerUrl.split("/");
//            answer.questionUrl = parts[2];
//            answer.token = parts[4];
//        }
        Element answerElement = element.getElementsByClass("zm-item-answer").first();
        answer.createTime = Long.valueOf(answerElement.attr("data-created")) * 1000;
        for (Element e : answerElement.getElementsByTag("meta")) {
            if ("answer-id".equals(e.attr("itemprop"))) {
                answer.url = e.attr("content");
                break;
            }
        }
        for (Element e : answerElement.getElementsByTag("link")) {
            if ("url".equals(e.attr("itemprop"))) {
                String answerUrl = e.attr("href");
                String[] parts = answerUrl.split("/");
                answer.questionUrl = parts[2];
                answer.token = parts[4];
                break;
            }
        }
        Element commentElement = DomUtil.getFirstElementByClass(answerElement, "toggle-comment");
        if (commentElement != null) {
            String commentText = commentElement.ownText();
            String numStr = commentText.split(" ")[0];
            int count = ZhiHuUtil.parseInt(numStr);
            answer.commentCount = count;
        }
        return answer;
    }
}

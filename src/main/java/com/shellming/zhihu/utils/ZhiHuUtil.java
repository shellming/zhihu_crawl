package com.shellming.zhihu.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shellming.zhihu.models.Answer;
import com.shellming.zhihu.models.Collection;
import com.shellming.zhihu.models.Comment;
import com.shellming.zhihu.models.User;
import okhttp3.Request;
import org.json.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by ruluo1992 on 10/1/2016.
 */
public class ZhiHuUtil {
    public static Gson gson = new Gson();

    public static final String ANSWER_URL = "http://www.zhihu.com/node/ExploreRecommendListV2";
    public static final String COMMENT_URL_TEMPLATE = "http://www.zhihu.com/r/answers/%s/comments";
    public static final String COLLECTION_URL = "https://www.zhihu.com/node/ExploreHotFavlistsInnerV2?params=%7B%22offset%22%3A5%7D";
    public static final String USER_ANSWER_URL = "http://www.zhihu.com/people/%s/answers?order_by=created&page=%d";
    public static final String COLLECTION_ANSWERS_URL = "http://www.zhihu.com/collection/%s?page=%d";

    // used in answer response and followee response
    static class AnswerResponse {
        public Integer r;
        public List<String> msg;
    }

    public static List<Collection> getCollections() {
        Request request = new Request.Builder()
                .url(COLLECTION_URL)
                .build();
        return null;
    }

    public static List<Answer> getAnswersFromCollection(String collectionId) throws IOException, InterruptedException {
        int page = 1;
        List<Answer> answers = new ArrayList<>();
        while(true) {
            String url = String.format(COLLECTION_ANSWERS_URL, collectionId, page);
            String response = HttpUtil.doGet(url, false, true);
            Element root = Jsoup.parse(response);
            Element container = root.getElementById("zh-list-collection-wrap");
            if (container == null || container.children().size() == 0) {
                break;
            }
            Elements children = container.children();
            for (Element answerElement : children) {
                Answer answer = Answer.fromElement(answerElement);
                if (answer != null) {
                    answers.add(answer);
                }
            }
            page++;
        }
        return answers;
    }

    public static List<Comment> getComments(Answer answer) throws Exception {
        String url = String.format(COMMENT_URL_TEMPLATE, answer.url);
        String response = HttpUtil.doGet(url, false);
//        if (response.startsWith("<!DOCTYPE html>") ||
//                response.startsWith("<!doctype html>")) {
//            throw new Exception("");
//        }
//        return response;
        List<Comment> result = new ArrayList<>();
        Map map = null;
        try {
            map = gson.fromJson(response, HashMap.class);
        } catch (Exception e) {
            System.out.println(response);
            throw e;
        }
        List<Map> data = (List<Map>) map.get("data");
        for (Map m : data) {
            Comment comment = Comment.fromMap(m);
            if (comment != null) {
                result.add(comment);
            }
        }
        return result;
    }

//    public static List<Answer> getAnswers(int size, int offset) throws IOException {
//        String payload = String.format(
//                "method=next&params={\"limit\":%d,\"offset\":%d}",
//                size, offset
//        );
//        RequestBody body = RequestBody.create(TYPE, payload);
//        Request request = new Request.Builder()
//                .url(ANSWER_URL)
//                .post(body)
//                .build();
//        List<Answer> result = new ArrayList<>();
//        String r = client.newCall(request).execute().body().string();
//        AnswerResponse answerResponse = gson.fromJson(r, AnswerResponse.class);
//        if (answerResponse.r == 0) {
//            for (String str : answerResponse.msg) {
//                Element element = Jsoup.parse(str);
//                Answer answer = Answer.fromElement(element);
//                if (answer != null) {
//                    result.add(answer);
//                }
//            }
//        }
//        return result;
//    }

    public static User getUserInfo(String url) throws IOException, InterruptedException, ExecutionException {
        String infoUrl = "http://www.zhihu.com/people/" + url;
        String response = HttpUtil.doGet(infoUrl, false);
//        String response = HttpUtil.doGet(infoUrl);
        Element root = Jsoup.parse(response);
        if (root.getElementsByClass("zm-profile-side-following") == null ||
                root.getElementsByClass("zm-profile-side-following").size() == 0) {
            return null;
        }
        Element profileElement = root.getElementsByClass("zm-profile-side-following").first();
        int followee = Integer.valueOf(profileElement.child(0).getElementsByTag("strong").html());
        Element followBtnOuter = root.getElementsByClass("zm-profile-header-op-btns").first();
        Element followBtn = followBtnOuter.getElementsByTag("button").first();
        String hash = followBtn.attr("data-id");
        User user = new User();
        user.followee = followee;
        user.hash = hash;
        user.url = url;
//        user.last_check = System.currentTimeMillis();
        return user;
    }

    public static List<User> getFollowee(User user) throws IOException, InterruptedException, ExecutionException, JsonSyntaxException {
        String url = "https://www.zhihu.com/node/ProfileFolloweesListV2";
        List<User> users = new ArrayList<>();
        int offset = 0;
        while(true) {
            try {
                Map<String, String> params = new HashMap<>();
                String payload = String.format("{\"offset\":%d,\"order_by\":\"created\",\"hash_id\":\"%s\"}",
                        offset, user.hash);
                params.put("method", "next");
                params.put("params", payload);
                String r = HttpUtil.doPost(url, params, true, true);
//                System.out.println(r);
                System.out.println("user " + user.url + " offset " + offset + " done");
                AnswerResponse followeeResponse = null;
                try {
                    followeeResponse = gson.fromJson(r, AnswerResponse.class);
                } catch (JsonSyntaxException e) {
                    System.out.println("malform json:" + r);
                    throw e;
                }
                if (followeeResponse.r == 0) {
                    if (followeeResponse.msg.size() == 0) {
                        break;
                    }
                    for (String msg : followeeResponse.msg) {
                        Element element = Jsoup.parse(msg);
                        User fullUser = User.fromElementInFollowee(element);
                        if (fullUser != null) {
                            users.add(fullUser);
                        }
                    }
                    offset += followeeResponse.msg.size();
                } else {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return users;
    }

    public static List<Answer> getAnswersFromUser(User user, Long from) throws IOException, InterruptedException, ExecutionException {
        int page = 1;
        boolean stop = false;
        List<Answer> result = new ArrayList<>();
        while(!stop) {
            String url = String.format(USER_ANSWER_URL, user.url, page);
            String response = HttpUtil.doGet(url, false);
            Element root = Jsoup.parse(response);
            Element answerContainer = root.getElementById("zh-profile-answer-list");
            if (answerContainer == null) {
                stop = true;
                continue;
            }
            Elements answerElements = answerContainer.getElementsByClass("zm-item");
            if (answerElements != null && answerElements.size() > 0) {
                for (Element answerElement : answerElements) {
                    Answer answer = Answer.fromElement(answerElement);
                    if (answer != null) {
                        if (answer.createTime <= from) {
                            stop = true;
                            break;
                        } else {
                            result.add(answer);
                        }
                    }
                }
            } else {
                stop = true;
            }
            page++;
        }
        return result;
    }

    public static Integer parseInt(String input) {
        int d = 1;
        int result = 0;
        for (int i = input.length() - 1; i >= 0; i--) {
            Character c = input.charAt(i);
            if (c == 'K') {
                d = d * 1000;
            } else if (c >= '0' && c <= '9'){
                result += (c - '0') * d;
                d = d * 10;
            }
        }
        return result;
    }
}

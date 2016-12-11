package com.shellming.zhihu.utils;

import com.shellming.zhihu.Config;
import com.shellming.zhihu.dao.AnswerDao;
import com.shellming.zhihu.models.Answer;
import com.shellming.zhihu.models.Comment;
import com.shellming.zhihu.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by ruluo1992 on 10/5/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ZhiHuUtilTest {

    @Autowired
    private AnswerDao answerDao;

    @Test
    public void testGetUserInfo() throws InterruptedException, ExecutionException, IOException {
        User newUser = ZhiHuUtil.getUserInfo("oskar-huang");
        System.out.println(newUser);
        List<User> followees = ZhiHuUtil.getFollowee(newUser);
        for (User u : followees) {
            System.out.println(u.url);
        }
         ZhiHuUtil.getAnswersFromUser(newUser, Config.answerFrom.getTime());
    }

    @Test
    public void testGetAnswerFromCollection() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("F:\\Code\\intellij\\zhihu\\1.txt", true)));
            List<Answer> answers = ZhiHuUtil.getAnswersFromCollection("38522649");
            for (int i = 0; i < answers.size(); i++) {
                Answer answer = answers.get(i);
                try {
                    List<Comment> comments = ZhiHuUtil.getComments(answer);
                    for (Comment comment : comments) {
                        writer.write(comment.content + "\r\n");
                    }
                } catch (Exception e) {
                    Thread.sleep(1000 * 60);
                    System.out.println("current id " + i);
                }
                Thread.sleep(100);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAnswer() throws IOException, InterruptedException, ExecutionException {
//        User user = new User("wawaye", "123");
//        user.hash = "a153a311a8d1bb73e250ba548a6d65a0";
//
//        System.out.println(ZhiHuUtil.getAnswersFromUser(user, Config.answerFrom.getTime()).size());

//        User user1 = ZhiHuUtil.getUserInfo("tian-ji-shun");
//        List<Answer> answers = ZhiHuUtil.getAnswersFromUser(user1, Config.answerFrom.getTime());
//        Answer answer = answers.get(0);
//        if (answerDao.isExistUrl(answer.url)) {
//            answerDao.deleteByUrl(answer.url);
//        }
//        answerDao.save(answer);
//        System.out.println(answerDao.count());
//        System.out.println(ZhiHuUtil.getAnswersFromUser(user1, Config.answerFrom.getTime()).size());
    }
}

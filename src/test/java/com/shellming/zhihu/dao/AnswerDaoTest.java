package com.shellming.zhihu.dao;

import com.shellming.zhihu.models.Answer;
import com.shellming.zhihu.models.User;
import com.shellming.zhihu.utils.ZhiHuUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by ruluo1992 on 10/5/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AnswerDaoTest {

    @Autowired
    private AnswerDao answerDao;

    @Test
    public void testGet() throws InterruptedException, ExecutionException, IOException {
//        Answer answer = answerDao.getLastByUserUrl("tian-ji-shun");
//        System.out.println(answer);
        Answer answer = answerDao.getLastByUserUrl("sijichun");
        Date date = new Date(answer.createTime);
        System.out.println(date);
        User user = new User();
        user.url = "sijichun";
        ZhiHuUtil.getAnswersFromUser(user, answer.createTime);
    }
}

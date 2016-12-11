package com.shellming.zhihu.models;

import com.shellming.zhihu.utils.ZhiHuUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by ruluo1992 on 10/4/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserTest {

    @Test
    public void testUserInFollowee() throws IOException, InterruptedException, ExecutionException {
        User user = new User("wawaye", "123");
        user.hash = "a153a311a8d1bb73e250ba548a6d65a0";
        ZhiHuUtil.getFollowee(user);
    }

    @Test
    public void testGetUser() throws IOException, InterruptedException, ExecutionException {
        User user = ZhiHuUtil.getUserInfo("wawaye");
        System.out.println(user.followee);

    }

}

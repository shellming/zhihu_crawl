package com.shellming.zhihu.dao;

import com.shellming.zhihu.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by ruluo1992 on 10/4/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

//    @Test
    public void testDao() {
        User user = new User("wawaye", "123");
        user.hash = "a153a311a8d1bb73e250ba548a6d65a0";
        user.followee = 10;

        // test count
        System.out.println(userDao.count());

        // test delete
        userDao.deleteByUrl(user.url);
        System.out.println(userDao.count());

        // test save
        userDao.save(user);
        System.out.println(userDao.count());

        // test exist
        System.out.println(userDao.isExistUrl(user.url));

        // test get and update
        List<User> users = userDao.getUsersById(0,10);
        for (User u : users) {
            System.out.println(u.url);
            u.followee = 20;
            userDao.update(u);
        }

        // test delete
        userDao.deleteByUrl(user.url);
        System.out.println(userDao.count());
    }

    @Test
    public void testInsert() {
        User user = new User("wawaye", "123");
        user.hash = "a153a311a8d1bb73e250ba548a6d65a0";
        user.followee = 0;
        userDao.deleteByUrl(user.url);
        userDao.save(user);
    }

    @Test
    public void testGetById() {
        List<User> users = userDao.getUsersById(100, 10);
        for (User u : users) {
            System.out.println(u.id);
        }
    }
}

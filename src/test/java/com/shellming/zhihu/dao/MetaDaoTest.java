package com.shellming.zhihu.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ruluo1992 on 10/5/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MetaDaoTest {

    @Autowired
    private MetaDao metaDao;

    @Test
    public void testMeta() {
//        metaDao.saveOrUpdate(MetaDao.ANSWER_KEY, "1234567890");
//        System.out.println(metaDao.getLast(MetaDao.ANSWER_KEY));
//        System.out.println(metaDao.getLast("1234"));
    }
}

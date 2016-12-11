package com.shellming.zhihu.utils;

import okhttp3.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created by ruluo1992 on 10/10/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProxyUtilTest {

    @Test
    public void testUtil() throws IOException, InterruptedException {
    }
}

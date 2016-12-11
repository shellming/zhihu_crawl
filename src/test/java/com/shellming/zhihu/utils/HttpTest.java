package com.shellming.zhihu.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ruluo1992 on 10/10/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HttpTest {

    @Test
    public void testProxy() throws IOException, InterruptedException {
        final List<String[]> address = new ArrayList<>();
//        address.add(new String[]{"143.160.210.41", "80"});
//        address.add(new String[]{"84.10.19.2", "3128"});
//        OkHttpClient client = new OkHttpClient.Builder()
//                .proxySelector(new ProxySelector() {
//                    @Override
//                    public List<Proxy> select(URI uri) {
//                        System.out.println("select one");
//                        List<Proxy> proxies = new ArrayList<>();
//                        Random random = new Random();
//                        int index = random.nextInt(address.size()) % address.size();
//                        proxies.add(new Proxy(Proxy.Type.HTTP,
//                                new InetSocketAddress(address.get(index)[0], Integer.valueOf(address.get(index)[1]))));
//                        return proxies;
//                    }
//
//                    @Override
//                    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
//
//                    }
//                })
//                .build();
//        Request request = new Request.Builder()
//                .url("http://www.baidu.com")
//                .build();
        for (int i = 0; i < 10; i++) {
//            System.out.println("123:123:123".split(":")[0]);
            System.out.println(HttpUtil.doGet("http://www.baidu.com", false));
            Thread.sleep(1000);
        }
    }
}

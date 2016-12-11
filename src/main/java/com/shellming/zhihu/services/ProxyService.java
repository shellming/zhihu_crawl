package com.shellming.zhihu.services;

import com.shellming.zhihu.utils.HttpUtil;
import com.shellming.zhihu.utils.ProxyUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by ruluo1992 on 10/11/2016.
 */
@Service
public class ProxyService {
    private final String URL = "http://api.xicidaili.com/free2016.txt";

    @Autowired
    private ThreadPoolExecutor fixedExecutor;

    @Autowired
    private ProgressService progressService;

    private AtomicInteger goodCount;
    private AtomicInteger badCount;

    class CheckTask implements Runnable{
        private String ip;
        private Integer port;
        public CheckTask(String[] proxy) {
            ip = proxy[0];
            port = Integer.valueOf(proxy[1]);
        }

        @Override
        public void run() {
            Integer timeout = HttpUtil.TIMEOUT / 3 * 2;
            OkHttpClient.Builder clientBuilder
                    = new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS);
            Request request = new Request.Builder()
                    .url("http://www.zhihu.com/people/wang-yan-bin-17/answers?order_by=created&page=1")
                    .build();
            Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
            clientBuilder.proxy(p);
            OkHttpClient client = clientBuilder.build();
            try {
                client.newCall(request).execute().close();
                System.out.println("good count" + goodCount.incrementAndGet());
            } catch (Exception e) {
                System.out.println("bad count" + badCount.incrementAndGet());
                ProxyUtil.remove(ip, port.toString());
//                e.printStackTrace();
            }

        }
    }

    @PostConstruct
    private void init() {
        List<String[]> goodProxies = progressService.getGoodProxyList();
        List<String[]> badProxies = progressService.getBadProxyList();
        ProxyUtil.write(goodProxies);
        ProxyUtil.writeBlackList(badProxies);
    }

    @PreDestroy
    private void destroy() {
        List<String[]> goodProxies = ProxyUtil.get();
        List<String[]> badProxies = ProxyUtil.getBlackList();
        progressService.setGoodProxyList(goodProxies);
        progressService.setBadProxyList(badProxies);
    }

    @Scheduled(fixedDelay = 1000 * 60 * 20)
    private void refresh() throws IOException, InterruptedException {
        List<String[]> proxies = getAvaliableProxies();
        ProxyUtil.write(proxies);

        List<String[]> goodProxies = ProxyUtil.get();
        List<String[]> badProxies = ProxyUtil.getBlackList();
        progressService.setGoodProxyList(goodProxies);
        progressService.setBadProxyList(badProxies);
    }

    @Scheduled(fixedDelay = 1000 * 60 * 20)
    private void check() throws InterruptedException {
        List<String[]> proxies = new ArrayList<>();
        goodCount = new AtomicInteger(0);
        badCount = new AtomicInteger(0);
        while (proxies.size() == 0) {
            proxies = ProxyUtil.get();
            Thread.sleep(100);
        }
        for (String[] proxy : proxies) {
            fixedExecutor.execute(new CheckTask(proxy));
        }
    }

    public List<String[]> getAvaliableProxies() throws IOException, InterruptedException {
        String response = HttpUtil.doGet(URL, false, false);
        System.out.println("!!! new proxy " + response);
        String[] parts = response.split("\r\n");
        List<String[]> old = ProxyUtil.get();
        List<String[]> result = new ArrayList<>();
        for (String part : parts) {
            String[] item = part.split(":");
            if (item.length != 2 || !isNumber(item[1])) {
                return old;
            }
            result.add(item);
        }
        return result;
    }

    public boolean isNumber(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

package com.shellming.zhihu.utils;

import com.shellming.zhihu.Config;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by ruluo1992 on 10/4/2016.
 */
public class HttpUtil {
    private static final MediaType TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
    public static final Integer TIMEOUT = 60;
    private static final Integer LOGIN_REQUEST_GAP = 1000 * 1;
    public static OkHttpClient client
            = new OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .proxySelector(new ProxySelector() {
                @Override
                public List<Proxy> select(URI uri) {
                    List<String[]> proxies = ProxyUtil.get();
                    if (proxies.size() == 0) {
                        return null;
                    } else {
                        Random random = new Random();
                        int size = proxies.size();
                        int index = random.nextInt(size) % size;
//                        System.out.println(uri.getPath());
                        String[] p = proxies.get(index);
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(p[0], Integer.valueOf(p[1])));
                        System.out.println("use proxy " + p[0] + ":" + p[1]);
                        List<Proxy> result = new ArrayList<Proxy>();
                        result.add(proxy);
                        return result;
                    }
                }

                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

                }
            })
            .build();

    public static OkHttpClient clientWithoutProxy
            = new OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

//    private static ExecutorService fixedExecutor = Executors.newFixedThreadPool(10);

//    public static Future<String> doSynGet(final String url) {
//        return fixedExecutor.submit(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return doGet(url, false);
//            }
//        });
//    }
//
//    public static Future<String> doSynGet(final String url, final Map<String,String> params) {
//        return fixedExecutor.submit(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return doGet(url, params, false);
//            }
//        });
//    }
//
//    public static Future<String> doSynPost(final String url, final Map<String,String> params) {
//        ProxySelector.getDefault();
//        return fixedExecutor.submit(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return doPost(url, params);
//            }
//        });
//    }

    public static String doGet(String url, Boolean needLogin, Boolean needProxy) throws IOException, InterruptedException {
        Request.Builder builder = null;
        OkHttpClient okHttpClient = null;
        if (needProxy) {
            okHttpClient = client;
        } else {
            okHttpClient = clientWithoutProxy;
        }
        if (needLogin) {
            builder = getLoginBuilder();
        } else {
            builder = getBuilder();
        }
        Request request = builder.url(url).build();
        Call call = okHttpClient.newCall(request);
        Response r = call.execute();
        String response = r.body().string();
        System.out.println("get request done " + url);
        r.close();
        if (needLogin) {
            Thread.sleep(LOGIN_REQUEST_GAP);
        }
        return response;

    }

    public static String doGet(String url, Boolean needLogin) throws IOException, InterruptedException {
        return doGet(url, needLogin, true);
    }

    public static String doGet(String url, Map<String,String> params, Boolean needLogin) throws IOException, InterruptedException {
        String paramStr = getUrlParamsByMap(params);
        if (!paramStr.equals("")) {
            url = url + "?" + paramStr;
        }
        Request.Builder builder = null;
        if (needLogin) {
            builder = getLoginBuilder();
        } else {
            builder = getBuilder();
        }
        Request request = builder.url(url).build();
        Response response = client.newCall(request).execute();
        String responseContent = response.body().string();
        response.close();
        if(needLogin) {
            Thread.sleep(LOGIN_REQUEST_GAP);
        }
//        System.out.println("get request done " + url);
//        Thread.sleep(500);
        return responseContent;
    }

    public static String doPost(String url, Map<String,String> params, Boolean needLogin, Boolean needProxy) throws IOException, InterruptedException {
        OkHttpClient okHttpClient = null;
        if (needProxy) {
            okHttpClient = client;
        } else {
            okHttpClient = clientWithoutProxy;
        }
        String paramStr = getUrlParamsByMap(params);
        RequestBody body = RequestBody.create(TYPE, paramStr);
        Request request = null;
        if (needLogin) {
            request = getLoginBuilder().url(url).post(body).build();
        } else {
            request = getBuilder().url(url).post(body).build();
        }
        Response response = okHttpClient.newCall(request).execute();
        String r = response.body().string();
        response.close();
//        System.out.println("post request done " + url);
        Thread.sleep(LOGIN_REQUEST_GAP);
        return r;
    }

    private static String getUrlParamsByMap(Map<String, String> map) throws UnsupportedEncodingException {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf8"));
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private static Request.Builder getBuilder() {
        Request.Builder builder = new Request.Builder();
        builder.addHeader("X-Requested-With", "XMLHttpRequest");
        return builder;
    }

    private static Request.Builder getLoginBuilder() {
        Request.Builder builder = new Request.Builder();
        builder.addHeader("Cookie", Config.COOKIE)
//                .addHeader("Upgrade-Insecure-Requests", "1");
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("X-Xsrftoken", Config.TOKEN);
        return builder;
    }

}

package com.shellming.zhihu.services;

import com.shellming.zhihu.dao.MetaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruluo1992 on 10/15/2016.
 */
@Service
public class ProgressService {
    @Autowired
    private MetaDao metaDao;

    private final String ANSWER_KEY = "ANSWER_KEY";
    private final String PROXY_LIST = "PROXY_LIST";
    private final String PROXY_BLACK_LIST = "PROXY_BLACK_LIST";
    private final String USER_KEY = "USER_KEY";
    private final String DUANZI_CHECKED_KEY = "DUANZI_CHECKED_KEY";
    private final String CURRENT_DUANZI = "CURRENT_DUANZI";

    private String toProxyString(List<String[]> proxies) {
        StringBuilder sb = new StringBuilder("");
        for (String[] proxy : proxies) {
            sb.append(proxy[0]).append(":").append(proxy[1]).append("\r\n");
        }
        return sb.toString();
    }

    private List<String[]> fromProxyString(String str) {
        String[] lines = str.split("\r\n");
        List<String[]> proxies = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                proxies.add(parts);
            }
        }
        return proxies;
    }

    public void setGoodProxyList(List<String[]> proxies) {
        String proxyString = toProxyString(proxies);
        metaDao.saveOrUpdate(PROXY_LIST, proxyString);
    }

    public void setBadProxyList(List<String[]> proxies) {
        String proxyString = toProxyString(proxies);
        metaDao.saveOrUpdate(PROXY_BLACK_LIST, proxyString);
    }

    public List<String[]> getGoodProxyList() {
        String proxyString = metaDao.getLast(PROXY_LIST);
        List<String[]> proxies = null;
        if (proxyString == null) {
            proxies = new ArrayList<>();
        } else {
            proxies = fromProxyString(proxyString);
        }
        return proxies;
    }

    public List<String[]> getBadProxyList() {
        String proxyString = metaDao.getLast(PROXY_BLACK_LIST);
        List<String[]> proxies = null;
        if (proxyString == null) {
            proxies = new ArrayList<>();
        } else {
            proxies = fromProxyString(proxyString);
        }
        return proxies;
    }

    public Integer getLastCheckedUserId() {
        String lastId = metaDao.getLast(ANSWER_KEY);
        Integer id = 0;
        if (lastId != null) {
            id = Integer.valueOf(lastId);
        }
        return id;
    }

    public Integer getLastFollowerId() {
        String lastId = metaDao.getLast(USER_KEY);
        Integer id = 0;
        if (lastId != null) {
            id = Integer.valueOf(lastId);
        }
        return id;
    }

    public Integer getLastCheckedDuanzi() {
        String lastId = metaDao.getLast(DUANZI_CHECKED_KEY);
        int id = 0;
        if (lastId != null) {
            id = Integer.valueOf(lastId);
        }
        return id;
    }

    public void setLastCheckedDuanzi(int id) {
        metaDao.saveOrUpdate(DUANZI_CHECKED_KEY, String.valueOf(id));
    }

    public void setLastCheckedUserId(Integer id) {
        metaDao.saveOrUpdate(ANSWER_KEY, id.toString());
    }

    public void setLastFollowerId(Integer id) {
        metaDao.saveOrUpdate(USER_KEY, id.toString());
    }
}

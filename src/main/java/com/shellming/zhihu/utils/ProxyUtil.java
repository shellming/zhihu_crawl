package com.shellming.zhihu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by ruluo1992 on 10/11/2016.
 */
public class ProxyUtil {
    private static List<String[]> proxies = new ArrayList<>();
    private static List<String[]> blackList = new ArrayList<>();
    private static Lock readLock;
    private static Lock writeLock;

    static {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public static List<String[]> get() {
        try {
            readLock.lock();
            List<String[]> result = new ArrayList<>(proxies);
            return result;
        } finally {
            readLock.unlock();
        }
    }

    public static List<String[]> getBlackList() {
        try {
            readLock.lock();
            List<String[]> result = new ArrayList<>(blackList);
            return result;
        } finally {
            readLock.unlock();
        }
    }

    public static void write(List<String[]> newProxies) {
        try {
            writeLock.lock();
            for (String[] proxy : newProxies) {
                if (find(blackList, proxy[0], proxy[1]) == -1 &&
                        find(proxies, proxy[0], proxy[1]) == -1) {
                    proxies.add(proxy);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    public static void writeBlackList(List<String[]> newProxies) {
        try {
            writeLock.lock();
            for (String[] proxy : newProxies) {
                if (find(blackList, proxy[0], proxy[1]) == -1 &&
                        find(proxies, proxy[0], proxy[1]) == -1) {
                    blackList.add(proxy);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    private static int find(List<String[]> data, String ip, String port) {
        int index = -1;
        for (int i = 0; i < data.size(); i++) {
            String[] proxy = data.get(i);
            if (proxy[0].equals(ip) && proxy[1].equals(port)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static void remove(String ip, String port) {
        try {
            writeLock.lock();
            int index = -1;
            for (int i = 0; i < proxies.size(); i++) {
                String[] proxy = proxies.get(i);
                if (proxy[0].equals(ip) && proxy[1].equals(port)) {
                    index = i;
                    break;
                }
            }
            String[] toRemove = proxies.get(index);
            proxies.remove(index);
            blackList.add(toRemove);
        } finally {
            writeLock.unlock();
        }
    }

}

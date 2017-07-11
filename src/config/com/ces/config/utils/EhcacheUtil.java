package com.ces.config.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheUtil {

    private static CacheManager cacheManager = null;

    static {
        if (cacheManager == null) {
            InputStream is = null;
            try {
                is = new FileInputStream(ComponentFileUtil.getConfigPath() + "ehcache/ehcache.xml");
                cacheManager = CacheManager.create(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (CacheException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 将对象缓存
     * 
     * @param cacheName ehcache.xml中定义的Cache的name
     * @param key 缓存名称
     * @param value 需要缓存的对象
     */
    public static void setCache(String cacheName, String key, Object value) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            cacheManager.addCache(cacheName);
            cache = cacheManager.getCache(cacheName);
        }
        Element element = new Element(key, value);
        cache.put(element);
    }

    /**
     * 取得已经被缓存的对象
     * 
     * @param cacheName ehcache.xml中定义的Cache的name
     * @param key 缓存名称
     * @return 已经被缓存的对象
     */
    public static Object getCache(String cacheName, String key) {
        Object value = null;
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            Element element = cache.get(key);
            if (element != null) {
                value = element.getObjectValue();
            }
        }
        return value;
    }

    /**
     * 取得缓存
     * 
     * @param cacheName ehcache.xml中定义的Cache的name
     * @return 缓存
     */
    public static Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            cacheManager.addCache(cacheName);
            cache = cacheManager.getCache(cacheName);
        }
        return cache;
    }

    /**
     * 移除缓存
     * 
     * @param cacheName ehcache.xml中定义的Cache的name
     * @param key 缓存名称
     */
    public static void removeCache(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.remove(key);
        }
    }

    /**
     * 移除所有缓存
     * 
     * @param cacheName ehcache.xml中定义的Cache的name
     * @param key 缓存名称
     */
    public static void removeAllCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.removeAll();
        }
    }
}

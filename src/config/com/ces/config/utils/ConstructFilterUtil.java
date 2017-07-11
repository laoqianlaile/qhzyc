package com.ces.config.utils;

import java.util.List;

import com.ces.config.dhtmlx.service.construct.ConstructFilterService;
import com.ces.xarch.core.web.listener.XarchListener;

import net.sf.ehcache.Cache;

/**
 * 数据过滤模块工具类
 * 
 * @author wanglei
 * @date 2015-5-21
 */
public class ConstructFilterUtil {

    private static ConstructFilterUtil instance = new ConstructFilterUtil();

    /** * 数据过滤在Ehcache中的cache名称 （存储name-value） */
    private static final String CONSTRUCT_FILTER = "CONSTRUCT_FILTER";

    private ConstructFilterUtil() {
    }

    /**
     * 获取ConstructFilterUtil实例
     * 
     * @return ConstructFilterUtil
     */
    public static ConstructFilterUtil getInstance() {
        return instance;
    }

    /**
     * 获取数据过滤条件
     * 
     * @param topComVersionId 上层组合构件版本ID
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @return Map<String, List<Menu>>
     */
    public String getConstructFilter(String topComVersionId, String componentVersionId, String tableId) {
        String key = topComVersionId + "_" + componentVersionId + "_" + tableId;
        String filter = (String) EhcacheUtil.getCache(CONSTRUCT_FILTER, key);
        if (filter == null) {
            filter = XarchListener.getBean(ConstructFilterService.class).buildGridFilter(topComVersionId, componentVersionId, tableId);
            EhcacheUtil.setCache(CONSTRUCT_FILTER, key, filter);
        }
        return filter;
    }

    /**
     * 移除数据过滤条件
     * 
     * @param key topComVersionId_componentVersionId_tableId或者3个中的1个
     */
    @SuppressWarnings("unchecked")
    public void removeConstructFilter(String key) {
        String[] strs = key.split("_");
        if (strs.length == 3) {
            EhcacheUtil.removeCache(CONSTRUCT_FILTER, key);
        } else {
            Cache cache = EhcacheUtil.getCache(CONSTRUCT_FILTER);
            List<String> keys = cache.getKeysNoDuplicateCheck();
            for (String k : keys) {
                if (k.indexOf(key) != -1) {
                    cache.remove(k);
                }
            }
        }
    }

}

package com.ces.config.utils;

import java.util.List;

import net.sf.ehcache.Cache;

import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.xarch.core.web.listener.XarchListener;

public class ModuleUtil {
    
    private static final String MODULE_ENTITY = "MODULE_ENTITY"; 
    private static final String MODULE = "MODULE"; 
    
    /**
     * qiucs 2014-2-10 
     * <p>描述: 根据模块ID取得构件版本ID</p>
     */
    public static String getComponentVersionId(String moduleId) {
        String componentVersionId = StringUtil.null2empty(EhcacheUtil.getCache(MODULE, moduleId));
        if (StringUtil.isEmpty(componentVersionId)) {
            componentVersionId = XarchListener.getBean(ModuleService.class).getComponentVersionId(moduleId);
            EhcacheUtil.setCache(MODULE, moduleId, componentVersionId);
        }
        return componentVersionId;
    }
    
    /**
     * qiucs 2014-2-10 
     * <p>描述: 在缓存添加指定构件版本ID</p>
     */
    public static void addComponentVersionId(String moduleId, String componentVersionId) {
        EhcacheUtil.setCache(MODULE, moduleId, componentVersionId);
    }
    
    /**
     * qiucs 2014-2-10 
     * <p>描述: 将指定构件版本ID从缓存中清除</p>
     */
    public static void removeComponentVersionId(String moduleId) {
        EhcacheUtil.removeCache(MODULE, moduleId);
    }
    
    /**
     * qiucs 2014-10-22 
     * <p>描述: 根据构件版本ID取得模块ID</p>
     */
    public static String getModuleId(String componentVersionId) {
        Cache cache = EhcacheUtil.getCache(MODULE);
        @SuppressWarnings("unchecked")
        List<String> cacheKeys = cache.getKeysNoDuplicateCheck();
        for (String key : cacheKeys) {
            if (componentVersionId.equals(cache.get(key))) {
                return key;
            }
        }
        String moduleId = XarchListener.getBean(ModuleService.class).getIdByComponentVersionId(componentVersionId);
        EhcacheUtil.setCache(MODULE, moduleId, componentVersionId);
        return moduleId;
    }
    
    /**
     * qiucs 2014-10-29 
     * <p>描述: 获取自定义构件对象</p>
     */
    public static Module getModule(String id) {
        Cache cache = EhcacheUtil.getCache(MODULE_ENTITY);
        if (cache.isKeyInCache(id)) {
            return (Module)EhcacheUtil.getCache(MODULE_ENTITY, id);
        }
        Module entity = XarchListener.getBean(ModuleService.class).getByID(id);
        EhcacheUtil.setCache(MODULE_ENTITY, id, entity);
        return entity;
    }
    
    /**
     * qiucs 2014-10-29 
     * <p>描述: 向缓存中添加自定义构件对象</p>
     */
    public static void addModule(Module entity) {
        EhcacheUtil.setCache(MODULE_ENTITY, entity.getId(), entity);
    }
    
    /**
     * qiucs 2014-10-29 
     * <p>描述: 根据ID从缓存中删除自定义构件对象</p>
     */
    public static void removeModule(String id) {
        EhcacheUtil.removeCache(MODULE_ENTITY, id);
    }
    
    /**
     * qiucs 2014-10-29 
     * <p>描述: 根据对象从缓存中删除自定义构件对象</p>
     */
    public static void removeModule(Module entity) {
        EhcacheUtil.removeCache(MODULE_ENTITY, entity.getId());
    }
}

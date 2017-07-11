package com.ces.config.utils;

import java.util.Map;

import net.sf.ehcache.Cache;

import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupDefineService;
import com.ces.xarch.core.web.listener.XarchListener;

public class GroupUtil {

    /** * 在Ehcache中物理表组的cache名称 */
    private static final String LOGIC_GROUP_CODE = "LOGIC_GROUP_CODE";
    
    private static final String PHYSICAL_GROUP_TABLES = "PHYSICAL_GROUP_TABLES";

    /**
     * qiucs 2014-11-28 
     * <p>描述: 从EHCACHE中获取物理表组对应的逻辑表组编码</p>
     */
    public static String getLogicGroupCode(String physicalGroupId) {
        Cache cache = EhcacheUtil.getCache(LOGIC_GROUP_CODE);
        if (cache.isKeyInCache(physicalGroupId)) {
            return String.valueOf(EhcacheUtil.getCache(LOGIC_GROUP_CODE, physicalGroupId));
        }
        String logicGroupCode = XarchListener.getBean(PhysicalGroupDefineService.class).getLogicGroupCode(physicalGroupId);
        addLogicGroupCode(physicalGroupId, logicGroupCode);
        return logicGroupCode;
    }
        
    /**
     * qiucs 2014-11-28 
     * <p>描述: 向EHCACHE中添加物理表组对应的逻辑表组编码</p>
     */
    public static void addLogicGroupCode(String physicalGroupId, String logicGroupCode) {
        EhcacheUtil.setCache(LOGIC_GROUP_CODE, physicalGroupId, logicGroupCode);
    }
    
    /**
     * qiucs 2014-11-28 
     * <p>描述: 删除EHCACHE中的物理表组对应的逻辑表组编码</p>
     */
    public static void removeLogicGroupCode(String physicalGroupId) {
        EhcacheUtil.removeCache(LOGIC_GROUP_CODE, physicalGroupId);
    }
    
    /**
     * qiucs 2015-7-4 上午9:31:19
     * <p>描述: 获取物理表组对应的物理表ID </p>
     * @return Map<String,String>
     */
    @SuppressWarnings("unchecked")
	public static Map<String, String> getPhysicalGroupTables(String physicalGroupId) {
    	Cache cache = EhcacheUtil.getCache(PHYSICAL_GROUP_TABLES);
        if (cache.isKeyInCache(physicalGroupId)) {
            return ((Map<String, String>) EhcacheUtil.getCache(PHYSICAL_GROUP_TABLES, physicalGroupId));
        }
        Map<String, String> data = XarchListener.getBean(PhysicalGroupDefineService.class).getPhysicalGroupTables(physicalGroupId);
        addPhysicalGroupTables(physicalGroupId, data);
        return data;
    }

	/**
	 * qiucs 2015-7-4 上午9:30:41
	 * <p>描述: 向缓存添加物理表组对应的物理表ID </p>
	 * @return void
	 */
	private static void addPhysicalGroupTables(String physicalGroupId, Map<String, String> data) {
		EhcacheUtil.setCache(PHYSICAL_GROUP_TABLES, physicalGroupId, data);
	}
	
	/**
	 * qiucs 2015-7-4 上午9:35:11
	 * <p>描述: 移除缓存中的物理表组对应的物理表ID </p>
	 * @return void
	 */
    public static void removePhysicalGroupTables(String physicalGroupId) {
        EhcacheUtil.removeCache(PHYSICAL_GROUP_TABLES, physicalGroupId);
    }
}

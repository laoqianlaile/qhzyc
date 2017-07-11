package com.ces.config.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Cache;

import org.apache.commons.collections.CollectionUtils;

import com.ces.config.dhtmlx.entity.component.ComponentArea;
import com.ces.config.dhtmlx.entity.component.ComponentAssembleArea;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * 构件工具类
 * 
 * @author wanglei
 * @date 2015-06-29
 */
public class ComponentInfoUtil {

    private static ComponentInfoUtil instance = new ComponentInfoUtil();

    /** * ComponentArea在Ehcache中的cache名称 */
    private static final String COMPONENT_AREA = "COMPONENT_AREA";

    /** * ComponentAssembleArea在Ehcache中的cache名称 */
    private static final String ASSEMBLE_AREA = "ASSEMBLE_AREA";

    /** * ComponentVersion在Ehcache中的cache名称 */
    private static final String COMPONENT_VERSION = "COMPONENT_VERSION";

    /** * Construct在Ehcache中的cache名称，key为constructId */
    private static final String CONSTRUCT = "CONSTRUCT";

    /** * Construct在Ehcache中的cache名称，key为assembleComponentVersionId */
    private static final String ASSEMBLE_COMPONENT_VERSION = "ASSEMBLE_COMPONENT_VERSION";

    /** * ConstructDetail在Ehcache中的cache名称，key为constructId */
    private static final String CONSTRUCT_DETAIL = "CONSTRUCT_DETAIL";

    /** * ConstructDetail在Ehcache中的cache名称，key为componentVersinId + "_" + zoneName */
    private static final String CONSTRUCT_DETAIL_LIST = "CONSTRUCT_DETAIL_LIST";

    private ComponentInfoUtil() {
    }

    /**
     * 获取ComponentInfoUtil实例
     * 
     * @return ComponentInfoUtil
     */
    public static ComponentInfoUtil getInstance() {
        return instance;
    }

    /**
     * 添加构件分类
     * 
     * @param componentArea 构件分类
     */
    public void putComponentArea(ComponentArea componentArea) {
        if (componentArea != null && StringUtil.isNotEmpty(componentArea.getId())) {
            EhcacheUtil.setCache(COMPONENT_AREA, componentArea.getId(), componentArea);
        }
    }

    /**
     * 添加构件分类
     * 
     * @param List<ComponentArea> 构件分类List
     */
    public void putComponentAreas(List<ComponentArea> componentAreaList) {
        if (CollectionUtils.isNotEmpty(componentAreaList)) {
            for (ComponentArea componentArea : componentAreaList) {
                if (componentArea != null && StringUtil.isNotEmpty(componentArea.getId())) {
                    EhcacheUtil.setCache(COMPONENT_AREA, componentArea.getId(), componentArea);
                }
            }
        }
    }

    /**
     * 获取构件分类
     * 
     * @param componentAreaId 构件分类ID
     * @return ComponentArea
     */
    public ComponentArea getComponentArea(String componentAreaId) {
        return (ComponentArea) EhcacheUtil.getCache(COMPONENT_AREA, componentAreaId);
    }

    /**
     * 移除构件分类
     * 
     * @param componentAreaId 构件分类ID
     */
    public void removeComponentArea(String componentAreaId) {
        EhcacheUtil.removeCache(COMPONENT_AREA, componentAreaId);
    }

    /**
     * 获取构件分类全路径
     * 
     * @param componentAreaId 构件分类ID
     * @return String
     */
    public String getComponentAreaAllPath(String componentAreaId) {
        String path = "";
        ComponentArea componentArea = getComponentArea(componentAreaId);
        while (componentArea != null) {
            path = "/" + componentArea.getId() + path;
            componentArea = getComponentArea(componentArea.getParentId());
        }
        return path;
    }

    /**
     * 添加组合构件分类
     * 
     * @param componentAssembleArea 构件分类
     */
    public void putComponentAssembleArea(ComponentAssembleArea componentAssembleArea) {
        if (componentAssembleArea != null && StringUtil.isNotEmpty(componentAssembleArea.getId())) {
            EhcacheUtil.setCache(ASSEMBLE_AREA, componentAssembleArea.getId(), componentAssembleArea);
        }
    }

    /**
     * 添加组合构件分类
     * 
     * @param List<ComponentAssembleArea> 构件组合分类List
     */
    public void putComponentAssembleAreas(List<ComponentAssembleArea> componentAssembleAreaList) {
        if (CollectionUtils.isNotEmpty(componentAssembleAreaList)) {
            for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                if (componentAssembleArea != null && StringUtil.isNotEmpty(componentAssembleArea.getId())) {
                    EhcacheUtil.setCache(COMPONENT_AREA, componentAssembleArea.getId(), componentAssembleArea);
                }
            }
        }
    }

    /**
     * 获取组合构件分类
     * 
     * @param componentAssembleAreaId 组合构件分类ID
     * @return ComponentAssembleArea
     */
    public ComponentAssembleArea getComponentAssembleArea(String componentAssembleAreaId) {
        return (ComponentAssembleArea) EhcacheUtil.getCache(ASSEMBLE_AREA, componentAssembleAreaId);
    }

    /**
     * 移除组合构件分类
     * 
     * @param componentAssembleAreaId 组合构件分类ID
     */
    public void removeComponentAssembleArea(String componentAssembleAreaId) {
        EhcacheUtil.removeCache(ASSEMBLE_AREA, componentAssembleAreaId);
    }

    /**
     * 获取组合构件分类全路径
     * 
     * @param componentAssembleAreaId 组合构件分类ID
     * @return String
     */
    public String getComponentAssembleAreaAllPath(String componentAssembleAreaId) {
        String path = "";
        ComponentAssembleArea componentAssembleArea = getComponentAssembleArea(componentAssembleAreaId);
        while (componentAssembleArea != null) {
            path = "/" + componentAssembleArea.getId() + path;
            componentAssembleArea = getComponentAssembleArea(componentAssembleArea.getParentId());
        }
        return path;
    }

    /**
     * 添加构件版本
     * 
     * @param componentVersion 构件版本
     */
    public void putComponentVersion(ComponentVersion componentVersion) {
        if (componentVersion != null && StringUtil.isNotEmpty(componentVersion.getId())) {
            EhcacheUtil.setCache(COMPONENT_VERSION, componentVersion.getId(), componentVersion);
        }
    }

    /**
     * 获取构件版本
     * 
     * @param componentVersionId 构件版本ID
     * @return ComponentVersion
     */
    public ComponentVersion getComponentVersion(String componentVersionId) {
        return (ComponentVersion) EhcacheUtil.getCache(COMPONENT_VERSION, componentVersionId);
    }

    /**
     * 移除构件版本
     * 
     * @param componentVersionId 构件版本ID
     */
    public void removeComponentVersion(String componentVersionId) {
        EhcacheUtil.removeCache(COMPONENT_VERSION, componentVersionId);
    }

    /**
     * 添加组合构件绑定关系
     * 
     * @param constructId 组合构件绑定关系ID
     * @param construct 组合构件绑定关系
     */
    public void putConstruct(Construct construct) {
        if (construct != null && construct.getAssembleComponentVersion() != null && StringUtil.isNotEmpty(construct.getId())) {
            EhcacheUtil.setCache(CONSTRUCT, construct.getId(), construct);
            EhcacheUtil.setCache(ASSEMBLE_COMPONENT_VERSION, construct.getAssembleComponentVersion().getId(), construct);
        }
    }

    /**
     * 获取组合构件绑定关系
     * 
     * @param constructId 组合构件绑定关系ID
     * @return Construct
     */
    public Construct getConstruct(String constructId) {
        return (Construct) EhcacheUtil.getCache(CONSTRUCT, constructId);
    }

    /**
     * 获取组合构件绑定关系
     * 
     * @param constructId 组合构件绑定关系ID
     * @return Construct
     */
    public Construct getConstructByAssembleId(String assembleComponentVersionId) {
        return (Construct) EhcacheUtil.getCache(ASSEMBLE_COMPONENT_VERSION, assembleComponentVersionId);
    }

    /**
     * 移除组合构件绑定关系
     * 
     * @param constructId 组合构件绑定关系ID
     */
    public void removeConstruct(Construct construct) {
        if (construct != null && StringUtil.isNotEmpty(construct.getId())) {
            EhcacheUtil.removeCache(CONSTRUCT, construct.getId());
            EhcacheUtil.removeCache(ASSEMBLE_COMPONENT_VERSION, construct.getAssembleComponentVersion().getId());
            removeAppConstructDetails(construct);
        }
    }

    /**
     * 添加ConstructDetail
     * 
     * @param constructDetail 组合构件中构件和预留区绑定关系
     */
    public void putConstructDetail(ConstructDetail constructDetail) {
        if (constructDetail != null && StringUtil.isNotEmpty(constructDetail.getId()) && StringUtil.isNotEmpty(constructDetail.getConstructId())) {
            Map<String, ConstructDetail> constructDetailMap = getConstructDetailMap(constructDetail.getConstructId());
            constructDetailMap.put(constructDetail.getId(), constructDetail);
        }
    }

    /**
     * 获取ConstructDetail
     * 
     * @param constructId 组合构件绑定关系ID
     * @return Map<String, ConstructDetail>
     */
    @SuppressWarnings("unchecked")
    public Map<String, ConstructDetail> getConstructDetailMap(String constructId) {
        Map<String, ConstructDetail> constructDetailMap = (Map<String, ConstructDetail>) EhcacheUtil.getCache(CONSTRUCT_DETAIL, constructId);
        if (constructDetailMap == null) {
            constructDetailMap = new ConcurrentHashMap<String, ConstructDetail>();
            EhcacheUtil.setCache(CONSTRUCT_DETAIL, constructId, constructDetailMap);
        }
        return constructDetailMap;
    }

    /**
     * 获取ConstructDetail
     * 
     * @param constructId 组合构件绑定关系ID
     * @return Set<ConstructDetail>
     */
    public List<ConstructDetail> getConstructDetails(String constructId) {
        Map<String, ConstructDetail> constructDetailMap = getConstructDetailMap(constructId);
        List<ConstructDetail> constructDetailList = new ArrayList<ConstructDetail>(constructDetailMap.values());
        return constructDetailList;
    }

    /**
     * 移除ConstructDetail
     * 
     * @param constructDetail 组合构件中构件和预留区绑定关系
     */
    public void removeConstructDetail(ConstructDetail constructDetail) {
        if (constructDetail != null && StringUtil.isNotEmpty(constructDetail.getId()) && StringUtil.isNotEmpty(constructDetail.getConstructId())) {
            Map<String, ConstructDetail> constructDetailMap = getConstructDetailMap(constructDetail.getConstructId());
            constructDetailMap.remove(constructDetail.getId());
        }
    }

    /**
     * 移除ConstructDetail
     * 
     * @param constructId 组合构件绑定关系ID
     */
    public void removeConstructDetail(String constructId) {
        if (StringUtil.isNotEmpty(constructId)) {
            Map<String, ConstructDetail> constructDetailMap = getConstructDetailMap(constructId);
            constructDetailMap.clear();
        }
    }
    
    /**
     * qiucs 2015-10-19 下午5:44:41
     * <p>描述: 从缓存中获取工具条 </p>
     * @return List<ConstructDetail>
     */
    @SuppressWarnings("unchecked")
	public List<ConstructDetail> getAppConstructDetails(String cvId, String zoneName) {
    	String key = new StringBuffer(cvId).append("_").append(zoneName).toString();
    	Cache cache = EhcacheUtil.getCache(CONSTRUCT_DETAIL_LIST);
    	List<ConstructDetail> list = null;
    	if (cache.isKeyInCache(key)) {
    		list = (List<ConstructDetail>)EhcacheUtil.getCache(CONSTRUCT_DETAIL_LIST, key);
    	} else {
    		list = new Vector<ConstructDetail>(XarchListener.getBean(ConstructDetailService.class).getAppConstructDetails(cvId, zoneName));
    		EhcacheUtil.setCache(CONSTRUCT_DETAIL_LIST, key, list);
    	}
    	return list;
    }
    
    /**
     * qiucs 2015-10-19 下午6:30:36
     * <p>描述: 向工具条缓存添加按钮 </p>
     * @return void
     */
    @SuppressWarnings("unchecked")
	public void putAppConstructDetails(ConstructDetail entity) {
    	Cache cache = EhcacheUtil.getCache(CONSTRUCT_DETAIL_LIST);
    	Construct construct = getConstruct(entity.getConstructId());
    	if (construct != null) {
    		String zoneId = entity.getReserveZoneId();
    		ComponentReserveZone zone = XarchListener.getBean(ComponentReserveZoneService.class).getByID(zoneId);
        	String cvId = construct.getAssembleComponentVersion().getId();
        	List<ConstructDetail> list = null;
        	String key = new StringBuffer(cvId).append("_").append(zone.getName()).toString();
        	if (cache.isKeyInCache(key)) {
        		list = (List<ConstructDetail>)EhcacheUtil.getCache(CONSTRUCT_DETAIL_LIST, key);
        		if (list.contains(entity)) {
        			list.remove(entity);
        		}
        		list.add(entity);
        		Collections.sort(list);
        	}
    	}
    }
    
    /**
     * qiucs 2015-10-19 下午6:30:41
     * <p>描述: 移除工具条缓存中的按钮 </p>
     * @return void
     */
    @SuppressWarnings("unchecked")
    public void removeAppConstructDetails(ConstructDetail entity) {
    	Cache cache = EhcacheUtil.getCache(CONSTRUCT_DETAIL_LIST);
		String zoneId = entity.getReserveZoneId();
		ComponentReserveZone zone = XarchListener.getBean(ComponentReserveZoneService.class).getByID(zoneId);
    	Construct construct = getConstruct(entity.getConstructId());
    	String cvId = construct.getAssembleComponentVersion().getId();
    	List<ConstructDetail> list = null;
    	String key = new StringBuffer(cvId).append("_").append(zone.getName()).toString();
    	if (cache.isKeyInCache(key)) {
    		list = (List<ConstructDetail>)EhcacheUtil.getCache(CONSTRUCT_DETAIL_LIST, key);
    		if (list.contains(entity)) {
    			list.remove(entity);
    		}
    	}
    }
    
    /**
     * qiucs 2015-10-26 下午1:50:26
     * <p>描述: 根据组合构件移除工具条缓存中的按钮 </p>
     * @return void
     */
    @SuppressWarnings("unchecked")
    public void removeAppConstructDetails(Construct construct) {
    	Cache cache = EhcacheUtil.getCache(CONSTRUCT_DETAIL_LIST);
    	String cvId = construct.getAssembleComponentVersion().getId();
    	List<String> keyList = cache.getKeys();
    	String key = null;
    	for (int i = 0, len = keyList.size(); i < len; i++) {
    		key = keyList.get(i);
    		if (key.startsWith(cvId)) {
    			EhcacheUtil.removeCache(CONSTRUCT_DETAIL_LIST, key);
    		}
    	}
    }
}

package com.ces.config.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.ces.config.dhtmlx.entity.resource.Resource;

/**
 * 资源模块工具类
 * 
 * @author wanglei
 * @date 2014-4-24
 */
public class ResourceUtil {

    private static ResourceUtil instance = new ResourceUtil();

    /** * 按钮资源下的构件按钮 */
    private static final String BUTTONRESOURCE_RESOURCEBUTTON = "BUTTONRESOURCE_RESOURCEBUTTON";

    /** * 构件组装的按钮 */
    public static final String CONSTRUCT_BUTTON = "CONSTRUCT_BUTTON";

    /** * 开发的页面构件中需控制权限的按钮 */
    public static final String PAGE_COMPONENT_BUTTON = "PAGE_COMPONENT_BUTTON";

    /** * 构件菜单下的按钮资源 */
    private static final String MENU_BUTTONRESOURCE = "MENU_BUTTONRESOURCE";

    /** * 构件菜单下被版本过滤掉的的按钮资源 */
    private static final String MENU_BUTTONRESOURCE_CANNOT_USE = "MENU_BUTTONRESOURCE_CANNOT_USE";

    private ResourceUtil() {
    }

    /**
     * 获取ResourceUtil实例
     * 
     * @return ResourceUtil
     */
    public static ResourceUtil getInstance() {
        return instance;
    }

    /**
     * 获取按钮资源下的构件按钮
     * 
     * @param buttonResourceId 按钮资源
     * @param type CONSTRUCT_BUTTON 或者 PAGE_COMPONENT_BUTTON
     * @return Vector<String>
     */
    @SuppressWarnings("unchecked")
    public Vector<String> getResourceButtons(String buttonResourceId, String type) {
        Vector<String> resourceButtonVector = null;
        Map<String, Vector<String>> resourceButtonMap = (Map<String, Vector<String>>) EhcacheUtil.getCache(BUTTONRESOURCE_RESOURCEBUTTON, buttonResourceId);
        if (MapUtils.isNotEmpty(resourceButtonMap)) {
            resourceButtonVector = resourceButtonMap.get(type);
        }
        return resourceButtonVector;
    }

    /**
     * 存储按钮资源下的构件按钮
     * 
     * @param buttonResourceId 按钮资源
     * @param type CONSTRUCT_BUTTON 或者 PAGE_COMPONENT_BUTTON
     * @param resourceButtonVector 按钮IDVector
     */
    @SuppressWarnings("unchecked")
    public void putResourceButtons(String buttonResourceId, String type, Vector<String> resourceButtonVector) {
        Map<String, Vector<String>> resourceButtonMap = (Map<String, Vector<String>>) EhcacheUtil.getCache(BUTTONRESOURCE_RESOURCEBUTTON, buttonResourceId);
        if (resourceButtonMap == null) {
            resourceButtonMap = new ConcurrentHashMap<String, Vector<String>>();
            EhcacheUtil.setCache(BUTTONRESOURCE_RESOURCEBUTTON, buttonResourceId, resourceButtonMap);
        }
        resourceButtonMap.put(type, resourceButtonVector);
    }

    /**
     * 存储按钮资源下的构件按钮
     * 
     * @param buttonResourceId 按钮资源
     * @param type CONSTRUCT_BUTTON 或者 PAGE_COMPONENT_BUTTON
     * @param resourceButtonId 按钮ID
     */
    public void putResourceButton(String buttonResourceId, String type, String resourceButtonId) {
        Vector<String> resourceButtonVector = getResourceButtons(buttonResourceId, type);
        if (resourceButtonVector == null) {
            resourceButtonVector = new Vector<String>();
            putResourceButtons(buttonResourceId, type, resourceButtonVector);
        }
        resourceButtonVector.add(resourceButtonId);
    }

    /**
     * 移除按钮资源下的构件按钮
     * 
     * @param buttonResourceId 按钮资源
     * @param type CONSTRUCT_BUTTON 或者 PAGE_COMPONENT_BUTTON
     * @param resourceButtonId 按钮ID
     */
    public void removeResourceButton(String buttonResourceId, String type, String resourceButtonId) {
        Vector<String> resourceButtonVector = getResourceButtons(buttonResourceId, type);
        if (CollectionUtils.isNotEmpty(resourceButtonVector)) {
            resourceButtonVector.remove(resourceButtonId);
        }
    }

    /**
     * 清空按钮资源下的构件按钮缓存
     * 
     * @param buttonResourceId 按钮资源ID
     */
    public void removeResourceButtonCache(String buttonResourceId) {
        EhcacheUtil.removeCache(BUTTONRESOURCE_RESOURCEBUTTON, buttonResourceId);
    }

    /**
     * 获取构件菜单下的按钮资源
     * 
     * @param menuId 菜单ID
     * @return Vector<Resource>
     */
    @SuppressWarnings("unchecked")
    public Vector<Resource> getButtonResources(String menuId) {
        return (Vector<Resource>) EhcacheUtil.getCache(MENU_BUTTONRESOURCE, menuId);
    }

    /**
     * 存储构件菜单下的按钮资源
     * 
     * @param menuId 菜单ID
     * @param buttonResourceVector 按钮资源Vector
     */
    public void putButtonResources(String menuId, Vector<Resource> buttonResourceVector) {
        EhcacheUtil.setCache(MENU_BUTTONRESOURCE, menuId, buttonResourceVector);
    }

    /**
     * 存储构件菜单下的按钮资源
     * 
     * @param menuId 菜单ID
     * @param buttonResource 按钮资源
     */
    public void putButtonResource(String menuId, Resource buttonResource) {
        Vector<Resource> buttonResourceVector = getButtonResources(menuId);
        if (buttonResourceVector == null) {
            buttonResourceVector = new Vector<Resource>();
            putButtonResources(menuId, buttonResourceVector);
        }
        buttonResourceVector.add(buttonResource);
    }

    /**
     * 移除构件菜单下的按钮资源
     * 
     * @param menuId 菜单ID
     * @param buttonResource 按钮资源
     */
    public void removeButtonResource(String menuId, Resource buttonResource) {
        Vector<Resource> buttonResourceVector = getButtonResources(menuId);
        if (CollectionUtils.isNotEmpty(buttonResourceVector)) {
            Resource resource = null;
            for (Iterator<Resource> it = buttonResourceVector.iterator(); it.hasNext();) {
                resource = it.next();
                if (resource.getId().equals(buttonResource.getId())) {
                    it.remove();
                    break;
                }
            }
        }
    }

    /**
     * 清空构件菜单下的按钮资源缓存
     * 
     * @param menuId 菜单ID
     */
    public void removeMenuButtonResourceCache(String menuId) {
        EhcacheUtil.removeCache(MENU_BUTTONRESOURCE, menuId);
    }

    /**
     * 获取构件菜单下被版本过滤掉的的按钮资源
     * 
     * @param menuId 菜单ID
     * @return Vector<Resource>
     */
    @SuppressWarnings("unchecked")
    public Vector<Resource> getCannotUseButtonResources(String menuId) {
        return (Vector<Resource>) EhcacheUtil.getCache(MENU_BUTTONRESOURCE_CANNOT_USE, menuId);
    }

    /**
     * 存储构件菜单下被版本过滤掉的的按钮资源
     * 
     * @param menuId 菜单ID
     * @param buttonResourceVector 按钮资源Vector
     */
    public void putCannotUseButtonResources(String menuId, Vector<Resource> buttonResourceVector) {
        EhcacheUtil.setCache(MENU_BUTTONRESOURCE_CANNOT_USE, menuId, buttonResourceVector);
    }

    /**
     * 存储构件菜单下被版本过滤掉的的按钮资源
     * 
     * @param menuId 菜单ID
     * @param buttonResource 按钮资源
     */
    public void putCannotUseButtonResource(String menuId, Resource buttonResource) {
        Vector<Resource> cannotUseResourceVector = getCannotUseButtonResources(menuId);
        if (cannotUseResourceVector == null) {
            cannotUseResourceVector = new Vector<Resource>();
            putCannotUseButtonResources(menuId, cannotUseResourceVector);
        }
        cannotUseResourceVector.add(buttonResource);
    }
}

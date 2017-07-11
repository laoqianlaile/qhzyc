package com.ces.config.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * 菜单模块工具类
 * 
 * @author wanglei
 * @date 2015-5-11
 */
public class MenuUtil {

    private static MenuUtil instance = new MenuUtil();

    /** * 菜单在Ehcache中的cache名称 （存储name-value） */
    private static final String MENU = "MENU";

    private MenuUtil() {
    }

    /**
     * 获取MenuUtil实例
     * 
     * @return MenuUtil
     */
    public static MenuUtil getInstance() {
        return instance;
    }

    /**
     * 获取菜单
     * 
     * @param systemId 系统Id
     * @return Map<String, List<Menu>>
     */
    @SuppressWarnings("unchecked")
    public Map<String, List<Menu>> getMenuMap(String systemId) {
        Map<String, List<Menu>> menuMap = (Map<String, List<Menu>>) EhcacheUtil.getCache(MENU, systemId);
        Map<String, List<Menu>> copyMenuMap = new HashMap<String, List<Menu>>();
        if (MapUtils.isNotEmpty(menuMap)) {
            String parentMenuId = null;
            List<Menu> menuList = null;
            List<Menu> copyMenuList = null;
            for (Iterator<String> it = menuMap.keySet().iterator(); it.hasNext();) {
                parentMenuId = it.next();
                menuList = menuMap.get(parentMenuId);
                copyMenuList = BeanUtils.mapList(menuList, Menu.class);
                copyMenuMap.put(parentMenuId, copyMenuList);
            }
        }
        return copyMenuMap;
    }

    /**
     * 存储菜单
     * 
     * @param menu 菜单
     */
    @SuppressWarnings("unchecked")
    public void putMenu(Menu menu) {
        Map<String, List<Menu>> menuMap = (Map<String, List<Menu>>) EhcacheUtil.getCache(MENU, menu.getRootMenuId());
        if (menuMap == null) {
            menuMap = new HashMap<String, List<Menu>>();
            EhcacheUtil.setCache(MENU, menu.getRootMenuId(), menuMap);
        }
        List<Menu> menuList = null;
        if (menu.getRootMenuId() == null || menu.getRootMenuId().equals(menu.getParentId())) {
            menuList = menuMap.get("root");
            if (CollectionUtils.isEmpty(menuList)) {
                menuList = new ArrayList<Menu>();
                menuMap.put("root", menuList);
            }
        } else {
            menuList = menuMap.get(menu.getParentId());
            if (CollectionUtils.isEmpty(menuList)) {
                menuList = new ArrayList<Menu>();
                menuMap.put(menu.getParentId(), menuList);
            }
        }
        if (menuList.contains(menu)) {
            menuList.remove(menu);
        }
        menu.setUrl(getComponentMenuUrl(menu));
        if (StringUtil.isNotEmpty(menu.getComponentVersionId()) && StringUtil.isEmpty(menu.getBaseComponentVersionId())) {
            ComponentVersion componentVersion = XarchListener.getBean(ComponentVersionService.class).getByID(menu.getComponentVersionId());
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct construct = XarchListener.getBean(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                menu.setBaseComponentVersionId(construct.getBaseComponentVersionId());
            } else {
                menu.setBaseComponentVersionId(menu.getComponentVersionId());
            }
        }
        menuList.add(menu);
    }

    /**
     * 移除菜单
     * 
     * @param menu 菜单
     */
    @SuppressWarnings("unchecked")
    public void removeMenu(Menu menu) {
        Map<String, List<Menu>> menuMap = (Map<String, List<Menu>>) EhcacheUtil.getCache(MENU, menu.getRootMenuId());
        if (MapUtils.isNotEmpty(menuMap)) {
            List<Menu> menuList = null;
            if (menu.getRootMenuId() == null || menu.getRootMenuId().equals(menu.getParentId())) {
                menuList = menuMap.get("root");
            } else {
                menuList = menuMap.get(menu.getParentId());
            }
            if (CollectionUtils.isNotEmpty(menuList)) {
                menuList.remove(menu);
            }
        }
    }

    /**
     * 获取菜单(不包括构件的一系列参数)
     * 
     * @param menu 菜单
     */
    public String getComponentMenuUrl(Menu menu) {
        String url = null;
        if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
            ComponentVersion componentVersion = XarchListener.getBean(ComponentVersionService.class).getByID(menu.getComponentVersionId());
            url = "/cfg-resource/" + componentVersion.getViews() + "/views/" + componentVersion.getUrl();
            if (url.indexOf("?") == -1) {
                url += "?bindingType=menu&menuId=" + menu.getId() + "&componentVersionId=" + componentVersion.getId();
            } else {
                url += "&bindingType=menu&menuId=" + menu.getId() + "&componentVersionId=" + componentVersion.getId();
            }
            if (StringUtil.isNotEmpty(menu.getCode())) {
                url += "&menuCode=" + menu.getCode();
            }
            url += "&topComVersionId=" + componentVersion.getId();
            if ("1".equals(menu.getUseNavigation())) {
                url += "&useNavigation=1";
            }
        } else if (ConstantVar.Menu.BindingType.URL.equals(menu.getBindingType())) {
            url = menu.getUrl();
            if ("1".equals(menu.getUseNavigation())) {
                if (url.indexOf("?") == -1) {
                    url += "?useNavigation=1";
                } else {
                    url += "&useNavigation=1";
                }
            }
            if (StringUtil.isNotEmpty(menu.getCode())) {
                url += "&menuCode=" + menu.getCode();
            }
        }
        return url;
    }

    /**
     * 重新缓存菜单
     */
    public void cacheMenu() {
        EhcacheUtil.removeAllCache(MENU);
        List<Menu> menuList = XarchListener.getBean(MenuService.class).getMenuList4Cache();
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                MenuUtil.getInstance().putMenu(menu);
            }
        }
    }
}

package com.ces.config.dhtmlx.action.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.menu.MenuDao;
import com.ces.config.dhtmlx.entity.authority.Authority;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.menu.MenuInputParam;
import com.ces.config.dhtmlx.entity.menu.MenuSelfParam;
import com.ces.config.dhtmlx.entity.resource.Resource;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuInputParamService;
import com.ces.config.dhtmlx.service.menu.MenuSelfParamService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.dhtmlx.service.resource.ResourceService;
import com.ces.config.utils.AuthDatabaseUtil;
import com.ces.config.utils.CfgCommonUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.MenuUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.exception.FatalException;

/**
 * 菜单Controller
 * 
 * @author wanglei
 * @date 2013-07-15
 */
public class MenuController extends ConfigDefineServiceDaoController<Menu, MenuService, MenuDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new Menu());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("menuService")
    @Override
    protected void setService(MenuService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        Integer maxShowOrder = getService().getMaxShowOrder(model.getParentId());
        int showOrder = 0;
        if (maxShowOrder == null) {
            showOrder = 1;
        } else {
            showOrder = maxShowOrder + 1;
        }
        model.setShowOrder(showOrder);
        model.setHasChild(false);
        model = getService().save(model);
        if (!"-1".equals(model.getParentId())) {
            Menu parentMenu = getService().getByID(model.getParentId());
            parentMenu.setBindingType(ConstantVar.Menu.BindingType.NOT_BINDING);
            parentMenu.setUrl("");
            parentMenu.setComponentVersionId("");
            parentMenu.setHasChild(true);
            getService().save(parentMenu);
        }
        return SUCCESS;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        String[] idArray = model.getId().split(",");
        String parentId = getService().getByID(idArray[0]).getParentId();
        getService().delete(model.getId());
        List<Menu> childMenuList = getService().getMenuByParentId(parentId);
        if (CollectionUtils.isEmpty(childMenuList)) {
            Menu parentMenu = getService().getByID(parentId);
            parentMenu.setHasChild(false);
            getService().save(parentMenu);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 查询菜单项
     * 
     * @return Object
     */
    public Object loadMenu() {
        String parentId = getParameter("Q_EQ_parentId");
        List<Menu> menuList = null;
        if ("-1".equals(parentId)) {
            menuList = getService().getMenuByParentId("sys_0");
        } else {
            menuList = getService().getMenuByParentId(parentId);
        }
        if (CfgCommonUtil.isReleasedSystem() && CollectionUtils.isNotEmpty(menuList)) {
            // 过滤掉系统发布和构件库
            for (Iterator<Menu> i = menuList.iterator(); i.hasNext();) {
                Menu m = i.next();
                if ("系统发布".equals(m.getName()) || "版本定义".equals(m.getName()) || "构件库".equals(m.getName()) || "系统更新".equals(m.getName())) {
                    i.remove();
                }
            }
        }
        boolean isSuperRole = CommonUtil.isSuperRole();
        if (!isSuperRole) {
            List<String> resourceIdList = AuthorityUtil.getInstance().getMenuAuthority("sys_0");
            if (CollectionUtils.isNotEmpty(menuList)) {
                if (CollectionUtils.isNotEmpty(resourceIdList)) {
                    for (Iterator<Menu> menuIterator = menuList.iterator(); menuIterator.hasNext();) {
                        Menu menu = menuIterator.next();
                        if (!resourceIdList.contains(menu.getId())) {
                            menuIterator.remove();
                        }
                    }
                } else {
                    menuList.clear();
                }
            }
        }
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
                    ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(menu.getComponentVersionId());
                    String url = "";
                    if ("1".equals(menu.getUseNavigation())) {
                        url += "/cfg-resource/dhtmlx/views/config/navigation.jsp?CFG_componentUrl=";
                    }
                    url += "/cfg-resource/dhtmlx/views/" + componentVersion.getUrl();
                    if (url.indexOf("?") == -1) {
                        url += "?bindingType=menu&menuId=" + menu.getId() + "&componentVersionId=" + componentVersion.getId();
                    } else {
                        url += "&bindingType=menu&menuId=" + menu.getId() + "&componentVersionId=" + componentVersion.getId();
                    }
                    if (StringUtil.isNotEmpty(menu.getCode())) {
                        url += "&menuCode=" + menu.getCode();
                    }
                    url += ComponentParamsUtil.getParamsOfMenu(menu);
                    menu.setUrl(url);
                }
            }
        }
        list = getDataModel(getModelTemplate());
        processFilter(list);
        list.setData(menuList);
        return SUCCESS;
    }

    /**
     * 获取菜单Map
     * 
     * @return Map<String, List<Menu>>
     */
    private Map<String, List<Menu>> getMenuMap() {
        Map<String, List<Menu>> menuMap = new HashMap<String, List<Menu>>();
        String systemId = getSystemId();
        if (StringUtil.isNotEmpty(systemId)) {
            menuMap.putAll(MenuUtil.getInstance().getMenuMap(systemId));
            for (Iterator<String> it = menuMap.keySet().iterator(); it.hasNext();) {
                Collections.sort(menuMap.get(it.next()));
            }
        } else {
            List<Menu> rootChildMenuList = new ArrayList<Menu>();
            menuMap.put("root", rootChildMenuList);
            List<Menu> rootMenuList = getService().getMenuByParentId("-1");
            for (Menu menu : rootMenuList) {
                if ("sys_0".equals(menu.getId())) {
                    continue;
                }
                Map<String, List<Menu>> tempMenuMap = MenuUtil.getInstance().getMenuMap(menu.getId());
                if (MapUtils.isNotEmpty(tempMenuMap)) {
                    String tempParentMenuId = null;
                    List<Menu> tempMenuList = null;
                    for (Iterator<String> it = tempMenuMap.keySet().iterator(); it.hasNext();) {
                        tempParentMenuId = it.next();
                        tempMenuList = tempMenuMap.get(tempParentMenuId);
                        Collections.sort(tempMenuList);
                        if ("root".equals(tempParentMenuId)) {
                            menuMap.get("root").addAll(tempMenuList);
                        } else {
                            menuMap.put(tempParentMenuId, tempMenuList);
                        }
                    }
                }
            }
        }
        return menuMap;
    }

    /**
     * 加载组件库4.0的应用系统菜单
     * 
     * @return Object
     */
    public Object loadAppMenuForCoral40() {
        boolean isSuperRole = CommonUtil.isSuperRole();
        // 是否需要使用权限过滤
        boolean notUseAuth = isSuperRole && !CfgCommonUtil.isReleasedSystem();
        // 获取有权限的菜单ID
        List<String> authResourceMenuIdList = null;
        if (!notUseAuth) {
            authResourceMenuIdList = getAuthResourceMenuIdList(isSuperRole);
        }
        Map<String, List<Menu>> menuMap = getMenuMap();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        List<Menu> menuList = menuMap.get("root");
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                if (notUseAuth || (CollectionUtils.isNotEmpty(authResourceMenuIdList) && authResourceMenuIdList.contains(menu.getId()))) {
                    if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
                        menu.setUrl(menu.getUrl() + ComponentParamsUtil.getParamsOfMenu(menu));
                    }
                    String menuJson = parseMenuToCoral40Json(menu, menuMap, notUseAuth, authResourceMenuIdList);
                    if (StringUtil.isNotEmpty(menuJson)) {
                        sb.append(menuJson);
                        sb.append(",");
                    }
                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        sb.append("]");
        setReturnData(sb.toString());
        return SUCCESS;
    }

    /**
     * 获取有权限的菜单ID
     * 
     * @return List<String>
     */
    private List<String> getAuthResourceMenuIdList(boolean isSuperRole) {
        List<String> resourceMenuIdList = null;
        if (!isSuperRole) {
            resourceMenuIdList = AuthorityUtil.getInstance().getMenuAuthority(getSystemId());
        } else {
            if (CfgCommonUtil.isReleasedSystem()) {
                resourceMenuIdList = getService(ResourceService.class).getAllCanUseMenuId();
            }
        }
        return resourceMenuIdList;
    }

    /**
     * 获取系统ID
     * 
     * @return String
     */
    private String getSystemId() {
        String systemId = getParameter("systemId");
        if (StringUtil.isEmpty(systemId)) {
            systemId = CommonUtil.getSystemId();
        }
        return systemId;
    }

    /**
     * 将菜单转换成组件库4.0的json格式
     * 
     * @param menu 菜单
     * @param menuMap 菜单Map
     * @param resourceIdList 菜单资源权限
     * @return String
     */
    private String parseMenuToCoral40Json(Menu menu, Map<String, List<Menu>> menuMap, boolean notUseAuth, List<String> authResourceMenuIdList) {
        StringBuilder sb = new StringBuilder();
        if (notUseAuth || (CollectionUtils.isNotEmpty(authResourceMenuIdList) && authResourceMenuIdList.contains(menu.getId()))) {
            if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
                menu.setUrl(menu.getUrl() + ComponentParamsUtil.getParamsOfMenu(menu));
            }
            sb.append("{\"id\":\"").append(menu.getId()).append("\", \"name\":\"").append(menu.getName()).append("\", \"code\":\"").append(menu.getCode())
                    .append("\", \"iconclass\":\"coral-icon-document\",\"url\":\"").append(menu.getUrl()).append("\", \"icon1\":\"")
                    .append(StringUtil.null2empty(menu.getIcon1())).append("\", \"icon2\":\"").append(StringUtil.null2empty(menu.getIcon2()))
                    .append("\", \"response\":\"click\", \"items\":[");
            List<Menu> childList = menuMap.get(menu.getId());
            if (CollectionUtils.isNotEmpty(childList)) {
                for (int i = 0; i < childList.size(); i++) {
                    String jsonItem = parseMenuToCoral40Json(childList.get(i), menuMap, notUseAuth, authResourceMenuIdList);
                    if (StringUtil.isNotEmpty(jsonItem)) {
                        sb.append(jsonItem);
                        if (i != childList.size() - 1) {
                            sb.append(",");
                        }
                    }
                }
            }
            sb.append("]}");
        }
        return sb.toString();
    }

    /***
     * 纵向菜单
     * 
     * @return Object
     */
    public Object loadAppVerticalMenuForCoral40() {
        boolean isSuperRole = CommonUtil.isSuperRole();
        // 是否需要使用权限过滤
        boolean notUseAuth = isSuperRole && !CfgCommonUtil.isReleasedSystem();
        // 获取有权限的菜单ID
        List<String> authResourceMenuIdList = null;
        if (!notUseAuth) {
            authResourceMenuIdList = getAuthResourceMenuIdList(isSuperRole);
        }
        StringBuffer buffer = new StringBuffer();
        Map<String, List<Menu>> menuMap = getMenuMap();
        List<Menu> menuList = getService().getMenuByParentId("-1");
        if (null != menuList) {
            String systemId = getSystemId();
            buffer.append("<ul id=\"leftMenu_" + new Date().getTime() + "\" class=\"left-menu\">");
            for (Menu menu : menuList) {
                if (null != menu && !"sys_0".equals(menu.getId())) {
                    if (StringUtil.isNotEmpty(systemId)) {
                        if (!systemId.equals(menu.getId())) {
                            continue;
                        }
                    }
                    List<Menu> aMenuList = getService().getMenuByParentId(menu.getId());
                    if (null != aMenuList) {
                        for (Menu aMenu : aMenuList) {
                            if (notUseAuth || (CollectionUtils.isNotEmpty(authResourceMenuIdList) && authResourceMenuIdList.contains(aMenu.getId()))) {
                                buffer.append("<li><a onclick=\"menuClick('" + aMenu.getId() + "')\"")
                                		.append("><span class=\"icon-folder icon\"></span>").append(aMenu.getName()).append("</a>")
                                        .append("<ul class=\"left-menu\">");
                                String menuAccordion = parseChildMenuForCoral40(aMenu, menuMap, notUseAuth, authResourceMenuIdList, false);
                                if (StringUtil.isNotEmpty(menuAccordion)) {
                                    buffer.append(menuAccordion);
                                }
                                buffer.append("</ul>").append("</li>");
                            }
                        }
                    }
                }
            }
            buffer.append("</ul>");
        }
        setReturnData(buffer.toString());
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /***
     * 菜单转换
     * 
     * @param menu 菜单
     * @param menuMap 菜单Map
     * @param resourceIdList 菜单资源权限
     * @param bool 是否子菜单
     * @return String
     */
    private String parseChildMenuForCoral40(Menu menu, Map<String, List<Menu>> menuMap, boolean notUseAuth, List<String> authResourceMenuIdList, boolean bool) {
        StringBuffer buffer = new StringBuffer();
        if (notUseAuth || (CollectionUtils.isNotEmpty(authResourceMenuIdList) && authResourceMenuIdList.contains(menu.getId()))) {
            List<Menu> childList = menuMap.get(menu.getId());
            if (CollectionUtils.isNotEmpty(childList)) {
                for (int i = 0; i < childList.size(); i++) {
                    String menuAccordion = parseChildMenuForCoral40(childList.get(i), menuMap, notUseAuth, authResourceMenuIdList, true);
                    if (StringUtil.isNotEmpty(menuAccordion)) {
                        buffer.append(menuAccordion);
                    }
                }
            } else if (bool) {
                buffer.append("<li><a onclick=\"menuClick('" + menu.getId() + "')\">").append(menu.getName()).append("</a></li>");
            }
        }

        return buffer.toString();
    }

    /***
     * 加载手风琴式菜单树
     * 
     * @return Object
     */
    public Object getAppMenuTreeOfAccordionForCoral40() {
        boolean isSuperRole = CommonUtil.isSuperRole();
        // 是否需要使用权限过滤
        boolean notUseAuth = isSuperRole && !CfgCommonUtil.isReleasedSystem();
        // 获取有权限的菜单ID
        List<String> authResourceMenuIdList = null;
        if (!notUseAuth) {
            authResourceMenuIdList = getAuthResourceMenuIdList(isSuperRole);
        }
        list = getDataModel(getModelTemplate());
        processFilter(list);
        PageRequest pageRequest = buildPageRequest();
        Sort sort = (null != pageRequest ? (null != pageRequest.getSort() ? pageRequest.getSort() : null) : null);
        List<Menu> menuList = null;
        if (null == sort) {
            menuList = getService().find(buildSpecification());
        } else {
            menuList = getService().find(buildSpecification(), sort);
        }
        if (CollectionUtils.isNotEmpty(menuList)) {
            Menu menu = null;
            for (Iterator<Menu> iterator = menuList.iterator(); iterator.hasNext();) {
                menu = iterator.next();
                if (!(notUseAuth || (CollectionUtils.isNotEmpty(authResourceMenuIdList) && authResourceMenuIdList.contains(menu.getId())))) {
                    iterator.remove();
                }
            }
        }
        setReturnData(beforeProcessTreeData(menuList));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /***
     * 加载组件库4.0快捷菜单
     * 
     * @return Object
     */
    public Object loadAppQuickMenuForCoral40() {
        boolean isSuperRole = CommonUtil.isSuperRole();
        // 是否需要使用权限过滤
        boolean notUseAuth = isSuperRole && !CfgCommonUtil.isReleasedSystem();
        // 获取有权限的菜单ID
        List<String> authResourceMenuIdList = null;
        if (!notUseAuth) {
            authResourceMenuIdList = getAuthResourceMenuIdList(isSuperRole);
        }
        Map<String, List<Menu>> menuMap = getMenuMap();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        List<Menu> menuList = menuMap.get("root");
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                String quickMenuJson = parseQuickMenuToCoral40Json(menu, menuMap, notUseAuth, authResourceMenuIdList);
                if (StringUtil.isNotEmpty(quickMenuJson)) {
                    sb.append(quickMenuJson);
                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        sb.append("]");
        setReturnData(sb.toString());
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取树菜单根节点
     * 
     * @return Object
     */
    public Object getAppRootMenuById() {
        setReturnData(getService().getByID(model.getId()));
        return SUCCESS;
    }

    /***
     * 获取ComponentVersion对象
     * 
     * @return Object
     */
    public Object getComponentVersionById() {
        Menu menu = getService().getByID(model.getId());
        setReturnData(getService(ComponentVersionService.class).getByID(menu.getComponentVersionId()));
        return SUCCESS;
    }

    /***
     * 获取菜单对应的参数
     * 
     * @return Object
     */
    public Object getParamsOfMenu() {
        String menuId = getParameter("menuId");
        Menu menu = getService().getByID(menuId);
        String param = ComponentParamsUtil.getParamsOfMenu(menu);
        setReturnData(param);
        return SUCCESS;
    }

    /**
     * 将快捷菜单转换成组件库4.0的json格式
     * 
     * @param menu 菜单
     * @param menuMap 菜单Map
     * @param resourceIdList 菜单资源权限
     * @return String
     */
    private String parseQuickMenuToCoral40Json(Menu menu, Map<String, List<Menu>> menuMap, boolean notUseAuth, List<String> authResourceMenuIdList) {
        StringBuffer sb = new StringBuffer();
        if (notUseAuth || (CollectionUtils.isNotEmpty(authResourceMenuIdList) && authResourceMenuIdList.contains(menu.getId()))) {
            List<Menu> childList = menuMap.get(menu.getId());
            if (CollectionUtils.isNotEmpty(childList)) {
                for (int i = 0; i < childList.size(); i++) {
                    String json = parseQuickMenuToCoral40Json(childList.get(i), menuMap, notUseAuth, authResourceMenuIdList);
                    if (StringUtil.isNotEmpty(json)) {
                        sb.append(json);
                    }
                }
            } else {
                if (!"1".equals(menu.getIsQuickMenu())) {
                    sb.append("");
                } else {
                    if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
                        menu.setUrl(menu.getUrl() + ComponentParamsUtil.getParamsOfMenu(menu));
                    }
                    sb.append("{\"id\":\"").append(menu.getId()).append("\", \"name\":\"").append(menu.getName()).append("\", \"code\":\"")
                            .append(menu.getCode()).append("\", \"iconclass\":\"coral-icon-document\",\"url\":\"").append(menu.getUrl())
                            .append("\", \"icon1\":\"").append(StringUtil.null2empty(menu.getIcon1())).append("\", \"icon2\":\"")
                            .append(StringUtil.null2empty(menu.getIcon2())).append("\", \"response\":\"click\",").append(" \"quickIcon\":\"")
                            .append(menu.getQuickIcon()).append("\"},");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateMenu() {
        Menu menu = (Menu) getModel();
        Menu temp = getService().getMenuByNameAndParentId(menu.getName(), menu.getParentId());
        Menu temp1 = null;
        if (StringUtil.isNotEmpty(menu.getCode())) {
            temp1 = getService().getMenuByCodeAndRootMenuId(menu.getCode(), menu.getRootMenuId());
        }
        boolean nameExist = false;
        boolean codeExist = false;
        if (null != menu.getId() && !"".equals(menu.getId())) {
            Menu oldMenu = getService().getByID(menu.getId());
            if (null != temp && null != oldMenu && !temp.getId().equals(oldMenu.getId())) {
                nameExist = true;
            }
            if (null != temp1 && null != oldMenu && !temp1.getId().equals(oldMenu.getId())) {
                codeExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
            if (null != temp1) {
                codeExist = true;
            }
        }
        boolean componentValid = true;
        if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
            componentValid = getService().checkComponentVersion(menu);
        }
        setReturnData("{'nameExist':" + nameExist + ",'codeExist':" + codeExist + ",'componentValid':" + componentValid + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验是否需要配置
     * 
     * @return Object
     */
    public Object validateConfig() {
        String menuId = getParameter("menuId");
        List<MenuSelfParam> menuSelfParamList = getService(MenuSelfParamService.class).getByMenuId(menuId);
        boolean flag = false;
        if (CollectionUtils.isNotEmpty(menuSelfParamList)) {
            flag = true;
        } else {
            List<MenuInputParam> menuInputParamList = getService(MenuInputParamService.class).getByMenuId(menuId);
            if (CollectionUtils.isNotEmpty(menuInputParamList)) {
                flag = true;
            }
        }
        setReturnData("{'needConfig':" + flag + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 排序
     * 
     * @return Object
     */
    public Object sort() {
        String start = getParameter("start");
        String end = getParameter("end");
        Menu startMenu = getService().getByID(start);
        Menu endMenu = getService().getByID(end);
        if (startMenu.getShowOrder() > endMenu.getShowOrder()) {
            // 向上
            List<Menu> menuList = getService().getByShowOrderBetweenAndParentId(endMenu.getShowOrder(), startMenu.getShowOrder(), startMenu.getParentId());
            startMenu.setShowOrder(endMenu.getShowOrder());
            getService().save(startMenu);
            for (Menu menu : menuList) {
                if (menu.getId().equals(startMenu.getId())) {
                    continue;
                }
                menu.setShowOrder(menu.getShowOrder() + 1);
                getService().save(menu);
            }
        } else {
            // 向下
            List<Menu> menuList = getService().getByShowOrderBetweenAndParentId(startMenu.getShowOrder(), endMenu.getShowOrder(), startMenu.getParentId());
            startMenu.setShowOrder(endMenu.getShowOrder());
            getService().save(startMenu);
            for (Menu menu : menuList) {
                if (menu.getId().equals(startMenu.getId())) {
                    continue;
                }
                menu.setShowOrder(menu.getShowOrder() - 1);
                getService().save(menu);
            }
        }
        MenuUtil.getInstance().cacheMenu();
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 排序
     * 
     * @return Object
     */
    public Object treeSort() {
        String start = getParameter("start");
        String end = getParameter("end");
        String targetId = getParameter("targetId");
        Menu startMenu = getService().getByID(start);
        if (startMenu.getParentId().equals(targetId)) {
            // 同个父菜单中拖动排序
            if (StringUtil.isNotEmpty(end)) {
                Menu endMenu = getService().getByID(end);
                if (startMenu.getShowOrder() > endMenu.getShowOrder()) {
                    // 向上
                    List<Menu> menuList = getService().getByShowOrderBetweenAndParentId(endMenu.getShowOrder(), startMenu.getShowOrder(),
                            startMenu.getParentId());
                    startMenu.setShowOrder(endMenu.getShowOrder());
                    getService().save(startMenu);
                    for (Menu menu : menuList) {
                        if (menu.getId().equals(startMenu.getId())) {
                            continue;
                        }
                        menu.setShowOrder(menu.getShowOrder() + 1);
                        getService().save(menu);
                    }
                } else {
                    // 向下
                    List<Menu> menuList = getService().getByShowOrderBetweenAndParentId(startMenu.getShowOrder(), endMenu.getShowOrder(),
                            startMenu.getParentId());
                    startMenu.setShowOrder(endMenu.getShowOrder() - 1);
                    getService().save(startMenu);
                    for (Menu menu : menuList) {
                        if (menu.getId().equals(startMenu.getId()) || menu.getId().equals(endMenu.getId())) {
                            continue;
                        }
                        menu.setShowOrder(menu.getShowOrder() - 1);
                        getService().save(menu);
                    }
                }
            }
            setReturnData("排序成功!");
        } else {
            String oldParentAreaId = startMenu.getParentId();
            // 拖动到不同的父菜单中
            startMenu.setParentId(targetId);
            if (StringUtil.isNotEmpty(end)) {
                Menu endMenu = getService().getByID(end);
                startMenu.setShowOrder(endMenu.getShowOrder());
                getService().save(startMenu);
                getService().updateShowOrderPlusOne(endMenu.getShowOrder(), targetId);
            } else {
                Integer maxShowOrder = getService().getMaxShowOrder(targetId);
                int showOrder = 0;
                if (maxShowOrder == null) {
                    showOrder = 1;
                } else {
                    showOrder = maxShowOrder + 1;
                }
                startMenu.setShowOrder(showOrder);
                getService().save(startMenu);
                if (!"-1".endsWith(targetId)) {
                    Menu parent = getService().getByID(targetId);
                    if (!parent.getHasChild()) {
                        parent.setHasChild(true);
                        getService().save(parent);
                    }
                }
            }
            // 查询拖动菜单原来的父菜单中是否还有子菜单，如果没有，将该菜单hasChild设置成false
            List<Menu> oldChildAreaList = getService().getMenuByParentId(oldParentAreaId);
            if (CollectionUtils.isEmpty(oldChildAreaList)) {
                Menu oldParentMenu = getService().getByID(oldParentAreaId);
                oldParentMenu.setHasChild(false);
                getService().save(oldParentMenu);
            }
            // 更改对应资源的父ID
            Resource resource = getService(ResourceService.class).getByTargetId(startMenu.getId());
            Resource parentResource = getService(ResourceService.class).getByTargetId(startMenu.getParentId());
            if (resource != null && parentResource != null) {
                resource.setParentId(parentResource.getId());
                getService(ResourceService.class).save(resource);
            }
            // 加载菜单
            setReturnData("改变父菜单成功!");
        }
        MenuUtil.getInstance().cacheMenu();
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取根菜单
     * 
     * @return Object
     */
    public Object getRootMenuTree() {
        String treeId = getId();
        if ("-1".equals(treeId)) {
            list = getDataModel(getModelTemplate());
            processFilter(list);
            PageRequest pageRequest = buildPageRequest();
            Sort sort = (null != pageRequest ? (null != pageRequest.getSort() ? pageRequest.getSort() : null) : null);
            List<Menu> menuList = null;
            if (null == sort) {
                menuList = getService().find(buildSpecification());
            } else {
                menuList = getService().find(buildSpecification(), sort);
            }
            if (CollectionUtils.isNotEmpty(menuList)) {
                Menu menu = null;
                for (Iterator<Menu> iterator = menuList.iterator(); iterator.hasNext();) {
                    menu = iterator.next();
                    if ("sys_0".equals(menu.getId())) {
                        iterator.remove();
                    }
                    menu.setHasChild(false);
                }
            }
            list.setData(beforeProcessTreeData(menuList));
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取菜单
     * 
     * @return Object
     */
    public Object getMenuTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        PageRequest pageRequest = buildPageRequest();
        Sort sort = (null != pageRequest ? (null != pageRequest.getSort() ? pageRequest.getSort() : null) : null);
        List<Menu> menuList = null;
        if (null == sort) {
            menuList = getService().find(buildSpecification());
        } else {
            menuList = getService().find(buildSpecification(), sort);
        }
        if (CollectionUtils.isNotEmpty(menuList)) {
            Menu menu = null;
            for (Iterator<Menu> iterator = menuList.iterator(); iterator.hasNext();) {
                menu = iterator.next();
                if ("sys_0".equals(menu.getId())) {
                    iterator.remove();
                }
            }
        }
        list.setData(beforeProcessTreeData(menuList));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取绑定构件的菜单
     * 
     * @return Object
     */
    public Object getComponentMenuTree() {
        String objectId = getParameter("objectId");
        String objectType = getParameter("objectType");
        Set<String> authorityMenuIdSet = getAuthorityMenuId(objectId, objectType);
        list = getDataModel(getModelTemplate());
        processFilter(list);
        PageRequest pageRequest = buildPageRequest();
        Sort sort = (null != pageRequest ? (null != pageRequest.getSort() ? pageRequest.getSort() : null) : null);
        List<Menu> menuList = null;
        if (null == sort) {
            menuList = getService().find(buildSpecification());
        } else {
            menuList = getService().find(buildSpecification(), sort);
        }
        if (CollectionUtils.isNotEmpty(menuList)) {
            Menu menu = null;
            for (Iterator<Menu> iterator = menuList.iterator(); iterator.hasNext();) {
                menu = iterator.next();
                if ("sys_0".equals(menu.getId())) {
                    iterator.remove();
                } else if (!authorityMenuIdSet.contains(menu.getId())) {
                    iterator.remove();
                } else {
                    if (ConstantVar.Menu.BindingType.NOT_BINDING.equals(menu.getBindingType())
                            || ConstantVar.Menu.BindingType.URL.equals(menu.getBindingType())) {
                        iterator.remove();
                    } else if (StringUtil.isEmpty(menu.getBindingType()) && !menu.getHasChild()) {
                        iterator.remove();
                    } else {
                        if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
                            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(menu.getComponentVersionId());
                            if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1
                                    || ConstantVar.Component.Type.TAB.equals(componentVersion.getComponent().getType())) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }
        list.setData(beforeProcessTreeData(menuList));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取绑定组合构件的菜单
     * 
     * @return Object
     */
    public Object getAssemblyComponentMenuTree() {
        String objectId = getParameter("objectId");
        String objectType = getParameter("objectType");
        Set<String> authorityMenuIdSet = getAuthorityMenuId(objectId, objectType);
        list = getDataModel(getModelTemplate());
        processFilter(list);
        PageRequest pageRequest = buildPageRequest();
        Sort sort = (null != pageRequest ? (null != pageRequest.getSort() ? pageRequest.getSort() : null) : null);
        List<Menu> menuList = null;
        if (null == sort) {
            menuList = getService().find(buildSpecification());
        } else {
            menuList = getService().find(buildSpecification(), sort);
        }
        if (CollectionUtils.isNotEmpty(menuList)) {
            Menu menu = null;
            for (Iterator<Menu> iterator = menuList.iterator(); iterator.hasNext();) {
                menu = iterator.next();
                if ("sys_0".equals(menu.getId())) {
                    iterator.remove();
                } else if (!authorityMenuIdSet.contains(menu.getId())) {
                    iterator.remove();
                } else {
                    if (ConstantVar.Menu.BindingType.NOT_BINDING.equals(menu.getBindingType())
                            || ConstantVar.Menu.BindingType.URL.equals(menu.getBindingType())) {
                        iterator.remove();
                    } else if (StringUtil.isEmpty(menu.getBindingType()) && !menu.getHasChild()) {
                        iterator.remove();
                    } else {
                        if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
                            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(menu.getComponentVersionId());
                            if (!ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }
        list.setData(beforeProcessTreeData(menuList));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取绑定的构件中包含自定义构件的菜单
     * 
     * @return Object
     */
    public Object getSelfDefineComponentMenuTree() {
        String objectId = getParameter("objectId");
        String objectType = getParameter("objectType");
        Set<String> authorityMenuIdSet = getAuthorityMenuId(objectId, objectType);
        list = getDataModel(getModelTemplate());
        processFilter(list);
        PageRequest pageRequest = buildPageRequest();
        Sort sort = (null != pageRequest ? (null != pageRequest.getSort() ? pageRequest.getSort() : null) : null);
        List<Menu> menuList = null;
        if (null == sort) {
            menuList = getService().find(buildSpecification());
        } else {
            menuList = getService().find(buildSpecification(), sort);
        }
        if (CollectionUtils.isNotEmpty(menuList)) {
            Menu menu = null;
            for (Iterator<Menu> iterator = menuList.iterator(); iterator.hasNext();) {
                menu = iterator.next();
                if ("sys_0".equals(menu.getId())) {
                    iterator.remove();
                } else if ("-1".equals(menu.getParentId())) {
                    continue;
                } else if (!authorityMenuIdSet.contains(menu.getId())) {
                    iterator.remove();
                } else {
                    if (ConstantVar.Menu.BindingType.NOT_BINDING.equals(menu.getBindingType())
                            || ConstantVar.Menu.BindingType.URL.equals(menu.getBindingType())) {
                        iterator.remove();
                    } else if (StringUtil.isEmpty(menu.getBindingType()) && !menu.getHasChild()) {
                        iterator.remove();
                    } else {
                        if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
                            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(menu.getComponentVersionId());
                            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                                if (!getService(ConstructService.class).existSelfDefineInAssemble(componentVersion.getId())) {
                                    iterator.remove();
                                }
                            } else if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1) {
                                continue;
                            } else {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }
        list.setData(beforeProcessTreeData(menuList));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取资源权限
     * 
     * @param objectId 角色或用户ID
     * @param objectType 角色或用户
     * @return Set<String>
     */
    @SuppressWarnings("unchecked")
    private Set<String> getAuthorityMenuId(String objectId, String objectType) {
        Set<String> menuIdSet = new HashSet<String>();
        List<String> resourceIdList = null;
        if (Authority.OT_ROLE.equals(objectType)) {
            String systemId = getSystemId();
            String sql = "select r.resourcekey from t_resource r,t_system_res sr,t_role_res rr,t_system s where s.system_id=sr.system_id and r.resource_id=rr.resource_id and r.resource_id=sr.resource_id and r.resourcekey is not null and r.resource_type_name='0' and s.system_code='"
                    + systemId + "' and rr.role_id ='" + objectId + "'";
            resourceIdList = AuthDatabaseUtil.queryForList(sql);
        } else if (Authority.OT_USER.equals(objectType)) {
            String sql = "select r.resourcekey from t_resource r,t_role_res rr where r.resource_id=rr.resource_id and r.resourcekey is not null and r.resource_type_name='0' and rr.role_id in (select ru.role_id from t_role_user ru where ru.user_id='"
                    + objectId + "')";
            resourceIdList = AuthDatabaseUtil.queryForList(sql);
        }
        if (CollectionUtils.isNotEmpty(resourceIdList)) {
            StringBuilder resourceIdSb = new StringBuilder();
            for (String resourceId : resourceIdList) {
                resourceIdSb.append("'").append(resourceId).append("',");
            }
            resourceIdSb.deleteCharAt(resourceIdSb.length() - 1);
            String menuIdSql = "select t.target_id from T_XTPZ_RESOURCE t where t.type='0' and t.id in (" + resourceIdSb.toString() + ")";
            menuIdSet.addAll(DatabaseHandlerDao.getInstance().queryForList(menuIdSql));
        }
        return menuIdSet;
    }

    /**
     * 获取菜单图标
     * 
     * @return Object
     */
    public Object getMenuIcons() {
        String level = getParameter("level");
        String projectPath = ComponentFileUtil.getProjectPath();
        String imgPath;
        if ("2".equals(level)) {
            imgPath = projectPath + "cfg-resource/coral40/common/themes/base/images/menuicon/twolevel";
        } else {
            imgPath = projectPath + "cfg-resource/coral40/common/themes/base/images/menuicon/onelevel";
        }
        File imgDir = new File(imgPath);
        List<String> imgNameList = new ArrayList<String>();
        if (imgDir.exists()) {
            String[] imgNames = imgDir.list();
            for (String imgName : imgNames) {
                if (imgName.toLowerCase().endsWith(".png")) {
                    imgNameList.add(imgName);
                }
            }
        }
        setReturnData(imgNameList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /***
     * 获取快捷菜单图标
     * 
     * @return Object
     */
    public Object getQuickMenuIcons() {
        String projectPath = ComponentFileUtil.getProjectPath();
        String imgPath = projectPath + "cfg-resource/coral40/common/themes/base/images/menuicon/quickicon";
        File imgDir = new File(imgPath);
        List<String> imgNameList = new ArrayList<String>();
        if (imgDir.exists()) {
            String[] imgNames = imgDir.list();
            for (String imgName : imgNames) {
                if (imgName.toLowerCase().endsWith(".png")) {
                    imgNameList.add(imgName);
                }
            }
        }
        setReturnData(imgNameList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 保存菜单图标
     * 
     * @return Object
     */
    public Object saveMenuIcon() {
        String menuId = getParameter("menuId");
        String icon1 = getParameter("icon1");
        String icon2 = getParameter("icon2");
        Menu menu = getService().getByID(menuId);
        if (StringUtil.isNotEmpty(icon1)) {
            menu.setIcon1(icon1);
        }
        if (StringUtil.isNotEmpty(icon2)) {
            menu.setIcon2(icon2);
        }
        getService().save(menu);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /***
     * 保存快捷菜单图标
     * 
     * @return Object
     */
    public Object saveQuickMenuIcon() {
        String quickIcon = getParameter("quickIcon");
        Menu menu = getService().getByID(model.getId());
        if (StringUtil.isNotEmpty(quickIcon)) {
            menu.setQuickIcon(quickIcon);
        }
        getService().save(menu);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 取消菜单图标
     * 
     * @return Object
     */
    public Object cancelMenuIcon() {
        String menuId = getParameter("menuId");
        Menu menu = getService().getByID(menuId);
        menu.setIcon1(null);
        menu.setIcon2(null);
        getService().save(menu);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /***
     * 取消快捷菜单图标
     * 
     * @return Object
     */
    public Object cancelQuickMenuIcon() {
        Menu menu = getService().getByID(model.getId());
        menu.setQuickIcon(null);
        getService().save(menu);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取根菜单
     * 
     * @return Object
     */
    public Object getAppMenuTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        PageRequest pageRequest = buildPageRequest();
        Sort sort = (null != pageRequest ? (null != pageRequest.getSort() ? pageRequest.getSort() : null) : null);
        List<Menu> menuList = new ArrayList<Menu>();
        String tableId = getParameter("P_tableId");
        if (StringUtil.isNotEmpty(tableId)) {
            if (null == sort) {
                menuList = getService().find(buildSpecification());
            } else {
                menuList = getService().find(buildSpecification(), sort);
            }
            getService().processTableMenuData(menuList, tableId);
        }
        list.setData(menuList);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * qiucs 2015-6-25 下午1:50:08
     * <p>描述: 应用定义应用到对就的菜单树 </p>
     * 
     * @return Object
     */
    public Object getApplyMenuTree() {
        try {
            String tableId = getParameter("P_tableId");
            // 0-菜单，1-构件
            String type = getParameter("P_type");
            setReturnData(getService().getApplyMenuTree(getId(), tableId, type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }

    /**
     * 获取第一层菜单配置(系统/平台)
     * 
     * @return Object
     */
    public Object getSysMenu() {
        setReturnData(getService().getTreeNode());
        return new DefaultHttpHeaders("success").disableCaching();
    }

}

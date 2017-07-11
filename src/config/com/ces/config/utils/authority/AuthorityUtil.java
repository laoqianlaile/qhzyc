package com.ces.config.utils.authority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ces.config.dhtmlx.service.resource.ResourceService;
import com.ces.config.utils.*;
import net.sf.ehcache.Cache;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import ces.sdk.system.bean.RoleUserInfo;

import com.ces.config.dhtmlx.dao.appmanage.PhysicalGroupRelationDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.entity.authority.Authority;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.code.CodeType;
import com.ces.config.dhtmlx.entity.component.ComponentButton;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.dhtmlx.service.authority.AuthorityTreeDataService;
import com.ces.config.dhtmlx.service.code.CodeTypeService;
import com.ces.config.dhtmlx.service.component.ComponentButtonService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.xarch.core.web.listener.XarchListener;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.dhtmlx.entity.menu.Menu;

/**
 * 权限管理工具类
 * 
 * @author wanglei
 * @date 2014-12-16
 */
public class AuthorityUtil implements Serializable {

    private static final long serialVersionUID = -4720198000349778678L;

    /** * 菜单权限在Ehcache中的cache名称 */
    public static final String AUTHORITY_MENU = "AUTHORITY_MENU";

    /** * 树权限在Ehcache中的cache名称 */
    public static final String AUTHORITY_TREE = "AUTHORITY_TREE";

    /** * 数据权限在Ehcache中的cache名称 */
    public static final String AUTHORITY_DATA = "AUTHORITY_DATA";

    /** * 关联表数据权限在Ehcache中的cache名称 */
    public static final String AUTHORITY_RELATE_TABLE_DATA = "AUTHORITY_RELATE_TABLE_DATA";

    /** * 编码权限在Ehcache中的cache名称 **/
    public static final String AUTHORITY_CODE = "AUTHORITY_CODE";

    /** * 组装的按钮权限在Ehcache中的cache名称 */
    public static final String AUTHORITY_CONSTRUCT_BUTTON = "AUTHORITY_CONSTRUCT_BUTTON";

    /** * 页面构件的按钮权限在Ehcache中的cache名称 */
    public static final String AUTHORITY_COMPONENT_BUTTON = "AUTHORITY_COMPONENT_BUTTON";

    /** * 数据权限部门数据模型在Ehcache中的cache名称 */
    public static final String AUTHORITY_DEPT_DATA_MODEL = "AUTHORITY_DEPT_DATA_MODEL";

    private static AuthorityUtil instance = null;

    private AuthorityManager authorityManager;

    /** * 是否开启缓存 */
    private boolean cached = false;

    /**
     * 私有构造方法
     */
    private AuthorityUtil() {

    }

    /**
     * 创建实例方法
     * 
     * @return AuthorityUtil
     */
    public static AuthorityUtil getInstance() {
        if (instance == null) {
            synchronized (AuthorityUtil.class) {
                if (instance == null) {
                    instance = new AuthorityUtil();
                }
            }
        }
        return instance;
    }

    public AuthorityManager getAuthorityManager() {
        if (authorityManager == null) {
            authorityManager = new DefaultAuthorityManagerImpl();
        }
        return authorityManager;
    }

    public void setAuthorityManager(AuthorityManager authorityManager) {
        this.authorityManager = authorityManager;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    /**
     * 当前登录用户的菜单权限（菜单权限在系统管理平台控制，需要控制是否缓存）
     * 
     * @param systemId 系统ID
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    public List<String> getMenuAuthority(String systemId) {
        List<String> menuIds = null;
        if (isCached()) {
            Map<String, List<String>> menuIdsMap = (Map<String, List<String>>) EhcacheUtil.getCache(AUTHORITY_MENU, CommonUtil.getCurrentUserId());
            if (MapUtils.isEmpty(menuIdsMap)) {
                menuIdsMap = new HashMap<String, List<String>>();
                menuIds = getAuthorityManager().getMenuIds(systemId);
                if (StringUtil.isEmpty(systemId)) {
                    menuIdsMap.put("ALL", menuIds);
                } else {
                    menuIdsMap.put(systemId, menuIds);
                }
                EhcacheUtil.setCache(AUTHORITY_MENU, CommonUtil.getCurrentUserId(), menuIdsMap);
            } else {
                if (StringUtil.isEmpty(systemId)) {
                    menuIds = menuIdsMap.get("ALL");
                    if (menuIds == null) {
                        menuIds = getAuthorityManager().getMenuIds(systemId);
                        menuIdsMap.put("ALL", menuIds);
                    }
                } else {
                    menuIds = menuIdsMap.get(systemId);
                    if (menuIds == null) {
                        menuIds = getAuthorityManager().getMenuIds(systemId);
                        menuIdsMap.put(systemId, menuIds);
                    }
                }
            }
        } else {
            menuIds = getAuthorityManager().getMenuIds(systemId);
        }
        return menuIds;
    }

    /**
     * 当前登录用户的可见树节点（树权限在系统配置平台控制，直接使用缓存）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return String
     */
    public String getTreeAuthority(String menuId, String componentVersionId) {
        if (StringUtil.isEmpty(menuId))
            return "";
        String treeNodeIds = null;
        AuthorityDataContainer authorityDataContainer = (AuthorityDataContainer) EhcacheUtil.getCache(AUTHORITY_TREE, CommonUtil.getCurrentUserId());
        if (null == authorityDataContainer) {
            authorityDataContainer = new AuthorityDataContainer();
            EhcacheUtil.setCache(AUTHORITY_TREE, CommonUtil.getCurrentUserId(), authorityDataContainer);
        }
        if (!authorityDataContainer.contains(componentVersionId, menuId)) {
            authorityDataContainer.add(componentVersionId, menuId, getTreeDefineIdsStr(menuId, componentVersionId));
        }
        treeNodeIds = authorityDataContainer.get(componentVersionId, menuId);
        return treeNodeIds;
    }

    /**
     * 当前登录用户的列表过滤条件（数据权限在系统配置平台控制，直接使用缓存）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @param treeNodeId 树节点ID
     * @param masterTableId 关联表ID
     * @param masterDataId 关联表数据ID
     * @return String
     */
    public String getDataAuthority(String menuId, String componentVersionId, String tableId, String topComVersionId, String treeNodeId, String masterTableId,
            String masterDataId) {
        StringBuffer filter = new StringBuffer();
        // 列表过滤条件
        filter.append(StringUtil.null2empty(ConstructFilterUtil.getInstance().getConstructFilter(topComVersionId, componentVersionId, tableId)));
        // 树数据权限
        filter.append(getTreeDataAuthority(menuId, componentVersionId, tableId, topComVersionId, treeNodeId));
        // 数据权限
        AuthorityDataContainer authorityDataContainer = (AuthorityDataContainer) EhcacheUtil.getCache(AUTHORITY_DATA, CommonUtil.getCurrentUserId());
        if (null == authorityDataContainer) {
            authorityDataContainer = new AuthorityDataContainer();
            EhcacheUtil.setCache(AUTHORITY_DATA, CommonUtil.getCurrentUserId(), authorityDataContainer);
        }
        if (StringUtil.isNotEmpty(menuId) && !authorityDataContainer.contains(tableId, menuId + componentVersionId)) {
            authorityDataContainer.add(tableId, menuId + componentVersionId, StringUtil.null2empty(getDataFilter(menuId, componentVersionId, tableId)));
        }
        filter.append(StringUtil.null2empty(authorityDataContainer.get(tableId, menuId + componentVersionId)));
        // 关联表数据权限
        if (StringUtil.isNotEmpty(masterTableId) && StringUtil.isNotEmpty(masterDataId)) {
            AuthorityRelateDataContainer authorityRelateDataContainer = (AuthorityRelateDataContainer) EhcacheUtil.getCache(AUTHORITY_RELATE_TABLE_DATA,
                    CommonUtil.getCurrentUserId());
            if (null == authorityRelateDataContainer) {
                authorityRelateDataContainer = new AuthorityRelateDataContainer();
                EhcacheUtil.setCache(AUTHORITY_RELATE_TABLE_DATA, CommonUtil.getCurrentUserId(), authorityRelateDataContainer);
            }
            if (StringUtil.isNotEmpty(menuId) && !authorityRelateDataContainer.contains(tableId + menuId + componentVersionId)) {
                authorityRelateDataContainer.add(tableId + menuId + componentVersionId, getRelateDataFilter(menuId, componentVersionId, tableId));
            }
            filter.append(authorityRelateDataContainer.getFilter(tableId + menuId + componentVersionId, tableId, masterTableId, masterDataId));
        }
        return String.valueOf(filter);
    }

    /**
     * 是否匹配关联表的数据权限
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @param controlTableId 关联表ID
     * @param controlDataId 关联表数据ID
     * @return String
     */
    public boolean matchRelateDataAuthority(String menuId, String componentVersionId, String tableId, String controlTableId, String controlDataId) {
        AuthorityRelateDataContainer authorityRelateDataContainer = (AuthorityRelateDataContainer) EhcacheUtil.getCache(AUTHORITY_RELATE_TABLE_DATA,
                CommonUtil.getCurrentUserId());
        if (null == authorityRelateDataContainer) {
            authorityRelateDataContainer = new AuthorityRelateDataContainer();
            EhcacheUtil.setCache(AUTHORITY_RELATE_TABLE_DATA, CommonUtil.getCurrentUserId(), authorityRelateDataContainer);
        }
        if (StringUtil.isNotEmpty(menuId) && !authorityRelateDataContainer.contains(tableId + menuId + componentVersionId)) {
            authorityRelateDataContainer.add(tableId + menuId + componentVersionId, getRelateDataFilter(menuId, componentVersionId, tableId));
        }
        return authorityRelateDataContainer.isMatchCondition(tableId + menuId + componentVersionId, tableId, controlTableId, controlDataId);
    }

    /**
     * 树数据权限
     * 
     * @param menuId 菜单ID
     * @param moduleId 模块ID
     * @param tableId 表ID
     * @param treeNodeId 树节点ID
     * @return String
     */
    private String getTreeDataAuthority(String menuId, String componentVersionId, String tableId, String topComVersionId, String treeNodeId) {
        if (StringUtil.isEmpty(treeNodeId)) {
            return "";
        }
        // 获取树节点
        TreeDefine treeDefine = XarchListener.getBean(TreeDefineService.class).getByID(treeNodeId);
        // 如果树节点不存在或该表不是主表，则无需 树数据权限
        if (treeDefine == null
                || !tableId.equals(treeDefine.getTableId())
                || (!TreeDefine.T_TABLE.equals(treeDefine.getType()) && !TreeDefine.T_GROUP.equals(treeDefine.getType()) && !TreeDefine.T_COLUMN_TAB
                        .equals(treeDefine.getType()))) {
            return "";
        }
        String filter = "";
        // 顶层构件是否是树组合构件
        Construct construct = XarchListener.getBean(ConstructService.class).getByAssembleComponentVersionId(topComVersionId);
        if (construct != null) {
            ComponentVersion baseComponentVersion = XarchListener.getBean(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
            if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())) {
                // 树上字段节点作为表节点的数据权限
                String treeControlDataAuth = XarchListener.getBean(AuthorityTreeDataService.class).getTreeControlDataAuth(menuId, topComVersionId);
                if ("1".equals(treeControlDataAuth)) {
                    // 构件是否是绑定在树组合构件的表节点（或物理表组节点）上
                    boolean bindingTable = false;
                    List<ConstructDetail> constructDetailList = XarchListener.getBean(ConstructDetailService.class).getByConstructIdAndReserveZoneId(
                            construct.getId(), "TREE");
                    TreeDefine tableTreeDefine = treeDefine;
                    if (CollectionUtils.isNotEmpty(constructDetailList)) {
                        if (TreeDefine.T_COLUMN_TAB.equals(treeDefine.getType())) {
                            // 向上获取表节点（物理表组节点）
                            List<TreeDefine> allParents = XarchListener.getBean(TreeDefineService.class).getAllParents(treeDefine.getParentId(), null);
                            for (int i = allParents.size() - 1; i >= 0; i--) {
                                tableTreeDefine = allParents.get(i);
                                if (TreeDefine.T_TABLE.equals(tableTreeDefine.getType()) || TreeDefine.T_GROUP.equals(tableTreeDefine.getType())) {
                                    break;
                                }
                            }
                        }
                        if (TreeDefine.T_TABLE.equals(tableTreeDefine.getType())) {
                            for (ConstructDetail cd : constructDetailList) {
                                if (componentVersionId.equals(cd.getComponentVersionId()) && TreeDefine.T_TABLE.equals(cd.getTreeNodeType())
                                        && tableTreeDefine.getTableId().equals(cd.getTreeNodeProperty())) {
                                    bindingTable = true;
                                    break;
                                }
                            }
                        } else if (TreeDefine.T_GROUP.equals(tableTreeDefine.getType())) {
                            PhysicalGroupDefine physicalGroupDefine = XarchListener.getBean(PhysicalGroupDefineService.class)
                                    .getByID(tableTreeDefine.getDbId());
                            String logicGroupCode = physicalGroupDefine.getLogicGroupCode();
                            for (ConstructDetail cd : constructDetailList) {
                                if (componentVersionId.equals(cd.getComponentVersionId()) && TreeDefine.T_GROUP.equals(cd.getTreeNodeType())
                                        && logicGroupCode.equals(cd.getTreeNodeProperty())) {
                                    bindingTable = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!bindingTable) {
                        return "";
                    }
                    String authTreeNodeIds = getTreeAuthority(menuId, topComVersionId);
                    if (StringUtil.isNotEmpty(authTreeNodeIds)) {
                        // 获取树上表节点下最下层表字段节点的过滤条件的并集
                        List<TreeDefine> authTreeNodeList = XarchListener.getBean(TreeDefineService.class).getTreeDefinesByIds(authTreeNodeIds);
                        List<TreeDefine> childrenTreeDefineList = XarchListener.getBean(TreeDefineService.class).getAllChildren(treeDefine.getId(), null);
                        authTreeNodeList.retainAll(childrenTreeDefineList);
                        TreeDefine tempTreeDefine = null;
                        for (Iterator<TreeDefine> it = authTreeNodeList.iterator(); it.hasNext();) {
                            tempTreeDefine = it.next();
                            if (!TreeDefine.T_COLUMN_TAB.equals(tempTreeDefine.getType())) {
                                it.remove();
                            } else if (!"0".equals(tempTreeDefine.getNodeRule())) {
                                it.remove();
                            }
                        }
                        // 获取树数据权限
                        if (CollectionUtils.isNotEmpty(authTreeNodeList)) {
                            // 转换成Map
                            Map<String, TreeDefine> treeDefineMap = new HashMap<String, TreeDefine>();
                            for (TreeDefine td : authTreeNodeList) {
                                treeDefineMap.put(td.getId(), td);
                            }
                            List<ColumnDefine> columnDefineList = XarchListener.getBean(ColumnDefineService.class).findByTableId(treeDefine.getTableId());
                            Map<String, ColumnDefine> columnDefineMap = new HashMap<String, ColumnDefine>();
                            for (ColumnDefine cd : columnDefineList) {
                                columnDefineMap.put(cd.getId(), cd);
                            }
                            StringBuilder filterSb = new StringBuilder();
                            for (TreeDefine td : authTreeNodeList) {
                                if (!td.getChild()) {
                                    if (filterSb.length() > 0) {
                                        filterSb.append(AppDefineUtil.RELATION_OR);
                                    }
                                    filterSb.append("(");
                                    filterSb.append(columnDefineMap.get(td.getDbId()).getColumnName()).append("='").append(td.getValue()).append("'");
                                    TreeDefine parentTreeDefine = treeDefineMap.get(td.getParentId());
                                    while (parentTreeDefine != null) {
                                        filterSb.append(AppDefineUtil.RELATION_AND).append(columnDefineMap.get(parentTreeDefine.getDbId()).getColumnName())
                                                .append("='").append(parentTreeDefine.getValue()).append("'");
                                        parentTreeDefine = treeDefineMap.get(parentTreeDefine.getParentId());
                                    }
                                    filterSb.append(")");
                                }
                            }
                            if (filterSb.length() > 0) {
                                filter = AppDefineUtil.RELATION_AND + "(" + filterSb.toString() + ")";
                            }
                        }
                    }
                }
            }
        }
        return filter;
    }

    /**
     * 根据编码权限获取编码（编码权限在系统配置平台控制，直接使用缓存）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件ID
     * @param codeTypeCode 编码code
     * @return List<Code>
     */
    @SuppressWarnings("unchecked")
    public List<Code> getCodeAuthority(String menuId, String componentVersionId, String codeTypeCode) {
        List<Code> codeList = (List<Code>) EhcacheUtil.getCache(AUTHORITY_CODE, CommonUtil.getCurrentUserId() + menuId + componentVersionId + codeTypeCode);
        CodeType entity = XarchListener.getBean(CodeTypeService.class).getCodeTypeByCode(codeTypeCode);
        if (null == entity)
            return null;
        if (CodeUtil.NO_CACHE.equals(entity.getIsCache()))
            codeList = null;
        if (CollectionUtils.isEmpty(codeList)) {
            codeList = getAuthorityManager().getCodeAuthority(menuId, componentVersionId, codeTypeCode);
            EhcacheUtil.setCache(AUTHORITY_CODE, CommonUtil.getCurrentUserId() + menuId + componentVersionId + codeTypeCode, codeList);
        }
        if (CollectionUtils.isEmpty(codeList))
            return null;
        Collections.sort(codeList);
        return codeList;
    }

    /**
     * 获取组装的按钮权限（减权限）（按钮权限在系统管理平台控制，需要控制是否缓存）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    public List<String> getConstructButtonAuthority(String menuId, String componentVersionId) {
        List<String> constructButtonIds = null;
        if (isCached()) {
            // 系统管理平台中获取按钮是按照菜单获取的，根构件无关，所有可以根据userId+menuId作为key进行缓存
            constructButtonIds = (List<String>) EhcacheUtil.getCache(AUTHORITY_CONSTRUCT_BUTTON, CommonUtil.getCurrentUserId() + menuId);
            if (CollectionUtils.isEmpty(constructButtonIds)) {
                constructButtonIds = notUsedConstructButtonIds(menuId, componentVersionId);
                EhcacheUtil.setCache(AUTHORITY_CONSTRUCT_BUTTON, CommonUtil.getCurrentUserId() + menuId, constructButtonIds);
            }
        } else {
            constructButtonIds = notUsedConstructButtonIds(menuId, componentVersionId);
        }
        return constructButtonIds;
    }

    /**
     * 获取页面构件的按钮权限（减权限）（按钮权限在系统管理平台控制，需要控制是否缓存）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    public List<String> getPageComponentButtonAuthority(String menuId, String componentVersionId) {
        List<String> pageComponentButtonIds = null;
        if (isCached()) {
            // 系统管理平台中获取按钮是按照菜单获取的，根构件无关，所有可以根据userId+menuId作为key进行缓存
            pageComponentButtonIds = (List<String>) EhcacheUtil.getCache(AUTHORITY_COMPONENT_BUTTON, CommonUtil.getCurrentUserId() + menuId);
            if (CollectionUtils.isEmpty(pageComponentButtonIds)) {
                pageComponentButtonIds = notUsedPageComponentButtonIds(menuId, componentVersionId);
                EhcacheUtil.setCache(AUTHORITY_COMPONENT_BUTTON, CommonUtil.getCurrentUserId() + menuId, pageComponentButtonIds);
            }
        } else {
            pageComponentButtonIds = notUsedPageComponentButtonIds(menuId, componentVersionId);
        }
        if (CollectionUtils.isNotEmpty(pageComponentButtonIds)) {
            // 构件中的权限按钮
            List<ComponentButton> componentButtonList = XarchListener.getBean(ComponentButtonService.class).getByComponentVersionId(componentVersionId);
            if (CollectionUtils.isNotEmpty(componentButtonList)) {
                List<String> notAuthorityCBList = new ArrayList<String>();
                for (ComponentButton cb : componentButtonList) {
                    if (pageComponentButtonIds.contains(cb.getId())) {
                        notAuthorityCBList.add(cb.getName());
                    }
                }
                pageComponentButtonIds = notAuthorityCBList;
            }
        }
        return pageComponentButtonIds;
    }

    /**
     * 根据用户或角色清除菜单或按钮缓存数据
     * 
     * @param authorityType 权限类型
     * @param objectId 对象ID
     * @param objectType 对象类型：0-角色 1-人员
     */
    public void clearMenuOrButtonAuthority(String authorityType, String objectId, String objectType) {
        if (isCached()) {
            List<String> userIds = cast2userId(objectId, objectType);
            for (String userId : userIds) {
                EhcacheUtil.removeCache(authorityType, userId);
            }
        }
    }

    /**
     * 清除菜单或按钮缓存数据
     * 
     * @param authorityType 权限类型
     */
    public void clearMenuOrButtonAuthority(String authorityType) {
        if (isCached()) {
            EhcacheUtil.removeAllCache(authorityType);
        }
    }

    /**
     * 根据用户或角色清除缓存数据
     * 
     * @param authorityType 权限类型
     * @param objectId 对象ID
     * @param objectType 对象类型：0-角色 1-人员
     * @param preKey
     * @param key
     */
    public void clearAuthority(String authorityType, String objectId, String objectType, String preKey, String key) {
        List<String> userIds = cast2userId(objectId, objectType);
        for (String userId : userIds) {
            AuthorityDataContainer authorityDataContainer = (AuthorityDataContainer) EhcacheUtil.getCache(authorityType, userId);
            if (authorityDataContainer != null) {
                authorityDataContainer.remove(preKey, key);
            }
        }
    }

    /**
     * 根据用户或角色清除缓存数据
     * 
     * @param authorityType 权限类型
     * @param objectId 对象ID
     * @param objectType 对象类型：0-角色 1-人员
     * @param subKey
     */
    public void clearAuthority(String authorityType, String objectId, String objectType, String subKey) {
        List<String> userIds = cast2userId(objectId, objectType);
        for (String userId : userIds) {
            AuthorityDataContainer authorityDataContainer = (AuthorityDataContainer) EhcacheUtil.getCache(authorityType, userId);
            if (authorityDataContainer != null) {
                authorityDataContainer.remove(subKey);
            }
        }
    }

    /**
     * 清除缓存数据
     * 
     * @param authorityType 权限类型
     * @param preKey
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void clearAuthority(String authorityType, String preKey, String key) {
        Cache cache = EhcacheUtil.getCache(authorityType);
        List<String> cacheKeys = cache.getKeysNoDuplicateCheck();
        for (String cacheKey : cacheKeys) {
            AuthorityDataContainer authorityDataContainer = (AuthorityDataContainer) EhcacheUtil.getCache(authorityType, cacheKey);
            if (authorityDataContainer != null) {
                authorityDataContainer.remove(preKey, key);
            }
        }
    }

    /**
     * 清除缓存数据
     * 
     * @param authorityType 权限类型
     * @param subKey KEY中的一部分tableId 或 relateId
     */
    @SuppressWarnings("unchecked")
    public void clearAuthority(String authorityType, String subKey) {
        Cache cache = EhcacheUtil.getCache(authorityType);
        List<String> keys = cache.getKeysNoDuplicateCheck();
        for (String key : keys) {
            AuthorityDataContainer authorityDataContainer = (AuthorityDataContainer) EhcacheUtil.getCache(authorityType, key);
            if (authorityDataContainer != null) {
                authorityDataContainer.remove(subKey);
            }
        }
    }

    /**
     * 根据用户或角色清除缓存数据
     * 
     * @param authorityType 权限类型
     * @param objectId 对象ID
     * @param objectType 对象类型：0-角色 1-人员
     * @param subKey
     */
    public void clearRelateAuthority(String objectId, String objectType, String tableId, String menuId, String componentVersionId) {
        String key = tableId + menuId + componentVersionId;
        List<String> userIds = cast2userId(objectId, objectType);
        for (String userId : userIds) {
            AuthorityRelateDataContainer authorityRelateDataContainer = (AuthorityRelateDataContainer) EhcacheUtil
                    .getCache(AUTHORITY_RELATE_TABLE_DATA, userId);
            if (authorityRelateDataContainer != null) {
                authorityRelateDataContainer.removeByKey(key);
            }
        }
    }

    /**
     * 根据用户或角色清除缓存数据
     * 
     * @param objectId 对象ID
     * @param objectType 对象类型：0-角色 1-人员
     * @param subKey
     */
    public void clearRelateAuthority(String objectId, String objectType, String subKey) {
        List<String> userIds = cast2userId(objectId, objectType);
        for (String userId : userIds) {
            AuthorityRelateDataContainer authorityRelateDataContainer = (AuthorityRelateDataContainer) EhcacheUtil
                    .getCache(AUTHORITY_RELATE_TABLE_DATA, userId);
            if (authorityRelateDataContainer != null) {
                authorityRelateDataContainer.removeBySubKey(subKey);
            }
        }
    }

    /**
     * 清除缓存数据
     * 
     * @param authorityType 权限类型
     * @param preKey
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void clearRelateAuthority(String subKey) {
        Cache cache = EhcacheUtil.getCache(AUTHORITY_RELATE_TABLE_DATA);
        List<String> cacheKeys = cache.getKeysNoDuplicateCheck();
        for (String cacheKey : cacheKeys) {
            AuthorityRelateDataContainer authorityRelateDataContainer = (AuthorityRelateDataContainer) EhcacheUtil.getCache(AUTHORITY_RELATE_TABLE_DATA,
                    cacheKey);
            if (authorityRelateDataContainer != null) {
                authorityRelateDataContainer.removeBySubKey(subKey);
            }
        }
    }

    /**
     * 将角色或用户统一转换为用户ID
     * 
     * @param objectId 对象ID
     * @param objectType 对象类型：0-角色 1-人员
     * @return List<String>
     */
    private List<String> cast2userId(String objectId, String type) {
        Set<String> set = new HashSet<String>();
        if (Authority.OT_ROLE.equals(type)) {
            if (Authority.OT_ROLE.equals(type)) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("roleId", objectId);
                List<RoleUserInfo> roleUserList = CommonUtil.getRoleInfoFacade().findUserInfosByRoleIdPage(params, 0, 9999999);
                for (RoleUserInfo roleUser : roleUserList) {
                    set.add(String.valueOf(roleUser.getUserId()));
                }
            } else {
                set.add(objectId);
            }
        } else {
            set.add(objectId);
        }
        return new ArrayList<String>(set);
    }

    /**
     * 获取树上有权限的节点
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<String>
     */
    public List<String> getTreeDefineIds(String menuId, String componentVersionId) {
        return getAuthorityManager().getTreeDefineIds(menuId, componentVersionId);
    }

    /**
     * 获取树上有权限的节点，','分开
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return String
     */
    public String getTreeDefineIdsStr(String menuId, String componentVersionId) {
        String treeDefineIds = "";
        List<String> list = getAuthorityManager().getTreeDefineIds(menuId, componentVersionId);
        for (String id : list) {
            treeDefineIds += ("," + id);
        }
        if (treeDefineIds.length() > 0) {
            treeDefineIds = treeDefineIds.substring(1);
        }
        return treeDefineIds;
    }

    /**
     * 获取数据权限的过滤条件
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @return String
     */
    public String getDataFilter(String menuId, String componentVersionId, String tableId) {
        return getAuthorityManager().getDataFilter(menuId, componentVersionId, tableId);
    }

    /**
     * 获取关联表数据权限的过滤条件
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @return String
     */
    public Map<String, Map<String, String>> getRelateDataFilter(String menuId, String componentVersionId, String tableId) {
        return getAuthorityManager().getRelateDataFilter(menuId, componentVersionId, tableId);
    }

    /**
     * 获取不用的组装的按钮（减按钮）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<String>
     */
    public List<String> notUsedConstructButtonIds(String menuId, String componentVersionId) {
        return getAuthorityManager().notUsedConstructButtonIds(menuId, componentVersionId);
    }

    /**
     * 获取不用的页面构件的按钮（减按钮）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 页面构件版本ID
     * @return List<String>
     */
    public List<String> notUsedPageComponentButtonIds(String menuId, String componentVersionId) {
        return getAuthorityManager().notUsedPageComponentButtonIds(menuId, componentVersionId);
    }

    /**
     * 获取不用的报表ID（减报表）
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @return List<String>
     */
    public List<String> notUsedReportIds(String menuId, String componentVersionId, String tableId) {
        return getAuthorityManager().notUsedReportIds(menuId, componentVersionId, tableId);
    }

    /**
     * 数据权限
     */
    private class AuthorityDataContainer implements Serializable {
        private static final long serialVersionUID = 8590532606488298708L;

        private Map<String, String> authorityMap = new HashMap<String, String>();

        public AuthorityDataContainer() {
            init();
        }

        public void init() {
            authorityMap.clear();
        }

        public boolean contains(String preKey, String key/* moduleId/menuId. */) {
            return authorityMap.keySet().contains(preKey + key);
        }

        public String get(String preKey, String key/* moduleId/menuId. */) {
            return authorityMap.get(preKey + key);
        }

        public void add(String preKey, String key/* moduleId/menuId. */, String authorityValue) {
            authorityMap.put(preKey + key, authorityValue);
        }

        public void remove(String preKey, String key/* moduleId/menuId. */) {
            authorityMap.remove(preKey + key);
        }

        public void remove(String subKey) {
            if (null == subKey)
                return;
            Iterator<String> it = authorityMap.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (key.indexOf(subKey) > -1) {
                    it.remove();
                }
            }
        }
    }

    /**
     * 关联表数据权限
     */
    private class AuthorityRelateDataContainer implements Serializable {
        private static final long serialVersionUID = -5778172331649613807L;

        // 第一个Map的key为tableId + menuId + componentVersionId，第二个Map的key为authorityDataId，第三个Map的key为controlTableId
        private Map<String, Map<String, Map<String, String>>> authorityMap = new HashMap<String, Map<String, Map<String, String>>>();

        public AuthorityRelateDataContainer() {
            init();
        }

        public void init() {
            authorityMap.clear();
        }

        public boolean contains(String key) {
            return authorityMap.keySet().contains(key);
        }

        public Map<String, Map<String, String>> get(String key) {
            return authorityMap.get(key);
        }

        /**
         * 获取满足关联表权限的本表权限的条件（目前只实现了一张关联表，再上一层的关联表没有实现；关联表数据权限满足一个条件后，不再查找其他满足条件的记录，那么要求数据权限的设置要互斥）
         */
        public String getFilter(String key, String tableId, String masterTableId, String masterDataId) {
            String filter = "";
            if (StringUtil.isNotEmpty(masterTableId) && StringUtil.isNotEmpty(masterDataId)) {
                Map<String, Map<String, String>> relateDataAuthorityMap = get(key);
                if (MapUtils.isNotEmpty(relateDataAuthorityMap)) {
                    // 数据权限最多考虑物理表组中4层结构，本Table和masterTable各算一层，再向上找两层
                    String firstLevelTableId = null;
                    String firstLevelTableName = null;
                    String firstLevelDataId = null;
                    String secondLevelTableId = null;
                    String secondLevelTableName = null;
                    String secondLevelDataId = null;
                    // 获取物理表组
                    String groupId = null;
                    try {
                        groupId = XarchListener.getBean(PhysicalGroupDefineService.class).getByTableId(tableId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                    if (StringUtil.isEmpty(groupId)) {
                        return "";
                    }
                    List<PhysicalTableDefine> physicalTableDefineList = XarchListener.getBean(PhysicalGroupRelationDao.class).getPhysicalTableDefineByGroupId(
                            groupId);
                    if (physicalTableDefineList.size() > 2) {
                        for (int i = physicalTableDefineList.size() - 1; i >= 0; i--) {
                            if (masterTableId.equals(physicalTableDefineList.get(i).getId())) {
                                if (i >= 2) {
                                    secondLevelTableId = physicalTableDefineList.get(i - 1).getId();
                                    secondLevelTableName = physicalTableDefineList.get(i - 1).getTableName();
                                    firstLevelTableId = physicalTableDefineList.get(i - 2).getId();
                                    firstLevelTableName = physicalTableDefineList.get(i - 2).getTableName();
                                } else if (i == 1) {
                                    firstLevelTableId = physicalTableDefineList.get(i - 1).getId();
                                    firstLevelTableName = physicalTableDefineList.get(i - 1).getTableName();
                                }
                            }
                        }
                    }
                    String masterTableName = TableUtil.getTableName(masterTableId);
                    // 获取表关系
                    if (secondLevelTableId != null) {
                        Map<String, List<String>> rMap = TableUtil.getTableRelation(secondLevelTableId, masterTableId);
                        List<String> mList = rMap.get(secondLevelTableId);
                        List<String> dList = rMap.get(masterTableId);
                        StringBuilder conditions = new StringBuilder();
                        for (int i = 0; i < mList.size(); i++) {
                            conditions.append(AppDefineUtil.RELATION_AND).append("m.").append(mList.get(i)).append("=").append("d.").append(dList.get(i));
                        }
                        String sql = "select m.id from " + secondLevelTableName + " m, " + masterTableName + " d where d.id='" + masterDataId + "'"
                                + conditions.toString();
                        Object mId = DatabaseHandlerDao.getInstance().queryForObject(sql);
                        secondLevelDataId = String.valueOf(mId);
                        if (StringUtil.isNotEmpty(secondLevelDataId)) {
                            Map<String, List<String>> rMap1 = TableUtil.getTableRelation(firstLevelTableId, secondLevelTableId);
                            List<String> mList1 = rMap1.get(firstLevelTableId);
                            List<String> dList1 = rMap1.get(secondLevelTableId);
                            StringBuilder conditions1 = new StringBuilder();
                            for (int i = 0; i < mList1.size(); i++) {
                                conditions1.append(AppDefineUtil.RELATION_AND).append("m.").append(mList1.get(i)).append("=").append("d.")
                                        .append(dList1.get(i));
                            }
                            String sql1 = "select m.id from " + firstLevelTableName + " m, " + secondLevelTableName + " d where d.id='" + secondLevelDataId
                                    + "'" + conditions1.toString();
                            Object mId1 = DatabaseHandlerDao.getInstance().queryForObject(sql1);
                            firstLevelDataId = String.valueOf(mId1);
                        }
                    } else if (firstLevelTableId != null) {
                        Map<String, List<String>> rMap = TableUtil.getTableRelation(firstLevelTableId, masterTableId);
                        List<String> mList = rMap.get(firstLevelTableId);
                        List<String> dList = rMap.get(masterTableId);
                        StringBuilder conditions = new StringBuilder();
                        for (int i = 0; i < mList.size(); i++) {
                            conditions.append(AppDefineUtil.RELATION_AND).append("m.").append(mList.get(i)).append("=").append("d.").append(dList.get(i));
                        }
                        String sql = "select m.id from " + firstLevelTableName + " m, " + masterTableName + " d where d.id='" + masterDataId + "'"
                                + conditions.toString();
                        Object mId = DatabaseHandlerDao.getInstance().queryForObject(sql);
                        firstLevelDataId = String.valueOf(mId);
                    }
                    for (Iterator<Map<String, String>> it = relateDataAuthorityMap.values().iterator(); it.hasNext();) {
                        // 是否匹配关联表权限
                        boolean matchCondition = true;
                        Map<String, String> tableFilterMap = it.next();
                        if (MapUtils.isNotEmpty(tableFilterMap)) {
                            String masterTableFilter = tableFilterMap.get(masterTableId);
                            if (StringUtil.isNotEmpty(masterTableFilter)) {
                                String sql = "select count(*) from " + masterTableName + " where id='" + masterDataId + "'" + masterTableFilter;
                                Object cnt = DatabaseHandlerDao.getInstance().queryForObject(sql);
                                long count = 0;
                                if (null != cnt) {
                                    count = Long.parseLong(cnt.toString());
                                }
                                // 关联表不满足条件
                                if (count == 0) {
                                    matchCondition = false;
                                    continue;
                                }
                            }
                            if (StringUtil.isNotEmpty(secondLevelTableId) && StringUtil.isNotEmpty(secondLevelDataId)) {
                                String secondLevelTableFilter = tableFilterMap.get(secondLevelTableId);
                                if (StringUtil.isNotEmpty(secondLevelTableFilter)) {
                                    String sql = "select count(*) from " + secondLevelTableName + " where id='" + secondLevelDataId + "'"
                                            + secondLevelTableFilter;
                                    Object cnt = DatabaseHandlerDao.getInstance().queryForObject(sql);
                                    long count = 0;
                                    if (null != cnt) {
                                        count = Long.parseLong(cnt.toString());
                                    }
                                    // 关联表不满足条件
                                    if (count == 0) {
                                        matchCondition = false;
                                        continue;
                                    }
                                }
                            }
                            if (StringUtil.isNotEmpty(firstLevelTableId) && StringUtil.isNotEmpty(firstLevelDataId)) {
                                String firstLevelTableFilter = tableFilterMap.get(firstLevelTableId);
                                if (StringUtil.isNotEmpty(firstLevelTableFilter)) {
                                    String sql = "select count(*) from " + firstLevelTableName + " where id='" + firstLevelDataId + "'" + firstLevelTableFilter;
                                    Object cnt = DatabaseHandlerDao.getInstance().queryForObject(sql);
                                    long count = 0;
                                    if (null != cnt) {
                                        count = Long.parseLong(cnt.toString());
                                    }
                                    // 关联表不满足条件
                                    if (count == 0) {
                                        matchCondition = false;
                                        continue;
                                    }
                                }
                            }
                        }
                        if (matchCondition) {
                            // 获取本表条件
                            String tableFilter = tableFilterMap.get(tableId);
                            if (StringUtil.isNotEmpty(tableFilter)) {
                                filter = tableFilter;
                            } else {
                                filter = AppDefineUtil.RELATION_AND + "1=1";
                            }
                            break;
                        }
                    }
                    // 关联表都不满足条件
                    if ("".equals(filter)) {
                        filter = AppDefineUtil.RELATION_AND + "1=0";
                    }
                }
            }
            return filter;
        }

        /**
         * 关联表的记录是否满足条件
         */
        public boolean isMatchCondition(String key, String tableId, String controlTableId, String controlDataId) {
            String filter = getFilter(key, tableId, controlTableId, controlDataId);
            boolean flag = true;
            if ((AppDefineUtil.RELATION_AND + "1=0").equals(filter)) {
                flag = false;
            }
            return flag;
        }

        public void add(String key, Map<String, Map<String, String>> relateDataAuthorityMap) {
            if (MapUtils.isNotEmpty(relateDataAuthorityMap)) {
                authorityMap.put(key, relateDataAuthorityMap);
            }
        }

        public void removeByKey(String key) {
            authorityMap.remove(key);
        }

        public void removeBySubKey(String subKey) {
            if (null == subKey)
                return;
            Iterator<String> it = authorityMap.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (key.indexOf(subKey) > -1) {
                    it.remove();
                }
            }
        }
    }

    /**
     * 判断首页菜单是否在用户权限内
     * @param systemId
     * @param menuName
     * @param parentId
     * @return
     */
    public static boolean  isHomeMenu(String systemId,String menuName,String parentId) {
        boolean isHome =false;
        List<String> resourceMenuIdList = null;
        resourceMenuIdList = AuthorityUtil.getInstance().getMenuAuthority(systemId);
        MenuService menuService = new MenuService();
        Menu menu = menuService.getMenuByNameAndParentId(menuName,parentId);
        if(resourceMenuIdList.contains(menu.getId()))
        {
            isHome = true;
        }
       return isHome;
    }

    public static void main(String[] args) {
        isHomeMenu("8a8a4afc5403e19a0154045cc8c40000","采收管理","8a8a4afc54045ddd0154045dddb10002");
    }
}

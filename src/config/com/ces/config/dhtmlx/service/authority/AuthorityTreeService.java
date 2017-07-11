package com.ces.config.dhtmlx.service.authority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.authority.AuthorityTreeDao;
import com.ces.config.dhtmlx.dao.authority.AuthorityTreeDataDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.authority.Authority;
import com.ces.config.dhtmlx.entity.authority.AuthorityData;
import com.ces.config.dhtmlx.entity.authority.AuthorityTree;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeData;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.dhtmlx.utils.DhtmlxCommonUtil;
import com.ces.config.utils.AuthDatabaseUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.security.entity.SysUser;
import com.google.common.collect.Lists;

/**
 * 树权限Service
 * 
 * @author wanglei
 * @date 2014-09-25
 */
@Component
public class AuthorityTreeService extends ConfigDefineDaoService<AuthorityTree, AuthorityTreeDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityTreeDao")
    @Override
    protected void setDaoUnBinding(AuthorityTreeDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取数据权限配置列表数据
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @return List<Object[]>
     */
    public List<Object[]> getDataConfigGridData(String objectId, String objectType, String menuId) {
        List<Object[]> constructList = new ArrayList<Object[]>();
        String imgPath = ServletActionContext.getRequest().getContextPath() + DhtmlxCommonUtil.DHX_FOLDER + "/common/images/";
        Object[] menuDefault = new Object[5];
        menuDefault[0] = AuthorityData.DEFAULT_ID;
        menuDefault[1] = "<font color='red'>本菜单默认权限<font>";
        menuDefault[2] = imgPath + "deny.gif";
        menuDefault[3] = imgPath + "icon/setup.gif^alt 配置数据权限^javascript:dataAuthority(\"" + objectId + "\",\"" + objectType + "\",\"" + menuId + "\",\""
                + AuthorityData.DEFAULT_ID + "\")^_self";
        menuDefault[4] = imgPath + "icon/setup.gif^alt 配置编码权限^javascript:codeAuthority(\"" + objectId + "\",\"" + objectType + "\",\"" + menuId + "\",\""
                + AuthorityData.DEFAULT_ID + "\")^_self";
        constructList.add(menuDefault);
        Menu menu = getService(MenuService.class).getByID(menuId);
        if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(menu.getComponentVersionId());
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct tempConstruct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                if (tempConstruct != null) {
                    constructList.add(translateComponentVersion(objectId, objectType, menuId, tempConstruct, 0));
                    constructList.addAll(getDataConfigs(objectId, objectType, menuId, tempConstruct, 1));
                }
            }
        }
        return constructList;
    }

    /**
     * 获取数据权限配置列表数据
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param construct 组合构件
     * @param level 缩进
     * @return List<Object[]>
     */
    private List<Object[]> getDataConfigs(String objectId, String objectType, String menuId, Construct construct, int level) {
        List<Object[]> constructList = new ArrayList<Object[]>();
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                    continue;
                }
                ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                if (componentVersion != null && ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    Construct tempConstruct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                    if (tempConstruct != null) {
                        constructList.add(translateComponentVersion(objectId, objectType, menuId, tempConstruct, level));
                        constructList.addAll(getDataConfigs(objectId, objectType, menuId, tempConstruct, level + 1));
                    }
                }
            }
        }
        return constructList;
    }

    /**
     * 转换成图片链接
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param construct 组合构件
     * @param level 缩进
     * @return Object[]
     */
    private Object[] translateComponentVersion(String objectId, String objectType, String menuId, Construct construct, int level) {
        ComponentVersion componentVersion = construct.getAssembleComponentVersion();
        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
        String imgPath = ServletActionContext.getRequest().getContextPath() + DhtmlxCommonUtil.DHX_FOLDER + "/common/images/";
        Object[] objs = new Object[5];
        objs[0] = componentVersion.getId();
        objs[1] = getPrefixSpace(level) + componentVersion.getComponent().getAlias() + componentVersion.getVersion();
        if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())) {
            objs[2] = imgPath + "icon/setup.gif^alt 配置树权限^javascript:treeAuthority(\"" + objectId + "\",\"" + objectType + "\",\"" + menuId + "\",\""
                    + componentVersion.getId() + "\")^_self";
            objs[3] = imgPath + "deny.gif";
            objs[4] = imgPath + "deny.gif";
        } else {
            objs[2] = imgPath + "deny.gif";
            objs[3] = imgPath + "icon/setup.gif^alt 配置数据权限^javascript:dataAuthority(\"" + objectId + "\",\"" + objectType + "\",\"" + menuId + "\",\""
                    + componentVersion.getId() + "\")^_self";
            objs[4] = imgPath + "icon/setup.gif^alt 配置编码权限^javascript:codeAuthority(\"" + objectId + "\",\"" + objectType + "\",\"" + menuId + "\",\""
                    + componentVersion.getId() + "\")^_self";
        }
        return objs;
    }

    /**
     * 获取层级的缩进前缀
     * 
     * @return String
     */
    private String getPrefixSpace(int level) {
        String str = "";
        for (int i = 0; i < level; i++)
            str += "　  ";
        return str;
    }

    /**
     * 根据版本构件ID获取树根节点ID
     * 
     * @param componentVersionId 构件版本ID
     * @return String
     */
    public String getTreeRootId(String componentVersionId) {
        ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
        if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
            Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersionId);
            componentVersionId = construct.getBaseComponentVersionId();
        }
        String treeId = getService(ModuleService.class).getTreeIdByComponentVersionId(componentVersionId);
        return StringUtil.null2empty(treeId);
    }

    /**
     * 删除树权限
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteAuthorityTree(String objectId, String objectType, String menuId, String componentVersionId) {
        getDao().deleteAuthorityTree(objectId, objectType, menuId, componentVersionId);
        getService(AuthorityTreeDataService.class).deleteAuthorityTreeData(objectId, objectType, menuId, componentVersionId);
        // 清除缓存
        AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_TREE, objectId, objectType, componentVersionId, menuId);
    }

    /**
     * 保存树权限配置
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param treeDefineIds 有权限的树节点
     */
    @Transactional
    public boolean saveAuthorityTree(String objectId, String objectType, String menuId, String componentVersionId, String treeDefineIds, String controlDataAuth) {
        boolean flag = true;
        try {
            // 删除旧的树权限
            getDao().deleteAuthorityTree(objectId, objectType, menuId, componentVersionId);
            // 保存新的树权限
            String[] treeDefineIdArray = treeDefineIds.split(",");
            if (treeDefineIdArray != null && treeDefineIdArray.length > 0) {
                List<AuthorityTree> authorityTreeList = Lists.newArrayList();
                AuthorityTree authorityTree = null;
                for (String treeDefineId : treeDefineIdArray) {
                    authorityTree = new AuthorityTree();
                    authorityTree.setObjectId(objectId);
                    authorityTree.setObjectType(objectType);
                    authorityTree.setMenuId(menuId);
                    authorityTree.setComponentVersionId(componentVersionId);
                    authorityTree.setTreeNodeId(treeDefineId);
                    authorityTreeList.add(authorityTree);
                }
                save(authorityTreeList);
                getService(AuthorityTreeDataService.class).saveAuthorityTreeData(objectId, objectType, menuId, componentVersionId, controlDataAuth);
            }
            // 清除缓存
            AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_TREE, objectId, objectType, componentVersionId, menuId);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 获取树权限treeNodeIds
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<String>
     */
    public List<String> getAuthorityTreeNodeIds(String objectId, String objectType, String menuId, String componentVersionId) {
        return getDao().getAuthorityTreeNodeIds(objectId, objectType, menuId, componentVersionId);
    }

    /**
     * 树权限复制到其他角色或用户
     * 
     * @param roleIds 角色IDs
     * @param userIds 用户IDs
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param treeDefineIds 树节点ID
     */
    @Transactional
    public void copyAuthorityTree(String roleIds, String userIds, String objectId, String objectType, String menuId, String componentVersionId,
            String treeDefineIds) {
        AuthorityTree authorityTree = null;
        List<AuthorityTree> authorityTreeList = Lists.newArrayList();
        AuthorityTreeData sourceAuthorityTreeData = getDaoFromContext(AuthorityTreeDataDao.class).getAuthorityTreeData(objectId, objectType, menuId,
                componentVersionId);
        AuthorityTreeData authorityTreeData = null;
        List<AuthorityTreeData> authorityTreeDataList = Lists.newArrayList();
        if (StringUtil.isNotEmpty(roleIds)) {
            String[] roleIdArray = roleIds.split(",");
            for (int i = 0; i < roleIdArray.length; i++) {
                String roleId = "";
                if (roleIdArray[i].indexOf("R_") != -1) {
                    roleId = roleIdArray[i].substring(2, roleIdArray[i].length());
                } else {
                    roleId = roleIdArray[i];
                }
                if ("0".equals(objectType) && roleId.equals(objectId)) {
                    continue;
                }
                // 删除旧的树权限
                getDao().deleteAuthorityTree(roleId, "0", menuId, componentVersionId);
                getDaoFromContext(AuthorityTreeDataDao.class).deleteAuthorityTreeData(roleId, "0", menuId, componentVersionId);
                // 清除缓存
                AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_TREE, roleId, "0", componentVersionId, menuId);
                if (canCopyAuthorityTree(roleId, "0", menuId)) {
                    String[] treeDefineIdArray = treeDefineIds.split(",");
                    for (String treeDefineId : treeDefineIdArray) {
                        authorityTree = new AuthorityTree();
                        authorityTree.setObjectId(roleId);
                        authorityTree.setObjectType("0");
                        authorityTree.setMenuId(menuId);
                        authorityTree.setComponentVersionId(componentVersionId);
                        authorityTree.setTreeNodeId(treeDefineId);
                        authorityTreeList.add(authorityTree);
                    }
                    if (sourceAuthorityTreeData != null && "1".equals(sourceAuthorityTreeData.getControlDataAuth())) {
                        authorityTreeData = new AuthorityTreeData();
                        authorityTreeData.setObjectId(roleId);
                        authorityTreeData.setObjectType("0");
                        authorityTreeData.setMenuId(menuId);
                        authorityTreeData.setComponentVersionId(componentVersionId);
                        authorityTreeData.setControlDataAuth("1");
                        authorityTreeDataList.add(authorityTreeData);
                    }
                }
            }
        }
        if (StringUtil.isNotEmpty(userIds)) {
            String[] userIdArray = userIds.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
                String userId = "";
                if (userIdArray[i].indexOf("U_") != -1) {
                    userId = userIdArray[i].substring(2, userIdArray[i].length());
                } else {
                    userId = userIdArray[i];
                }
                if ("1".equals(objectType) && userId.equals(objectId)) {
                    continue;
                }
                // 删除旧的树权限
                getDao().deleteAuthorityTree(userId, "1", menuId, componentVersionId);
                getDaoFromContext(AuthorityTreeDataDao.class).deleteAuthorityTreeData(userId, "1", menuId, componentVersionId);
                // 清除缓存
                AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_TREE, userId, "1", componentVersionId, menuId);
                if (canCopyAuthorityTree(userId, "1", menuId)) {
                    String[] treeDefineIdArray = treeDefineIds.split(",");
                    for (String treeDefineId : treeDefineIdArray) {
                        authorityTree = new AuthorityTree();
                        authorityTree.setObjectId(userId);
                        authorityTree.setObjectType("1");
                        authorityTree.setMenuId(menuId);
                        authorityTree.setComponentVersionId(componentVersionId);
                        authorityTree.setTreeNodeId(treeDefineId);
                        authorityTreeList.add(authorityTree);
                    }
                    if (sourceAuthorityTreeData != null && "1".equals(sourceAuthorityTreeData.getControlDataAuth())) {
                        authorityTreeData = new AuthorityTreeData();
                        authorityTreeData.setObjectId(userId);
                        authorityTreeData.setObjectType("1");
                        authorityTreeData.setMenuId(menuId);
                        authorityTreeData.setComponentVersionId(componentVersionId);
                        authorityTreeData.setControlDataAuth("1");
                        authorityTreeDataList.add(authorityTreeData);
                    }
                }
            }
        }
        getDao().save(authorityTreeList);
        if (CollectionUtils.isNotEmpty(authorityTreeDataList)) {
            getDaoFromContext(AuthorityTreeDataDao.class).save(authorityTreeDataList);
        }
    }

    /**
     * 判断树权限能否复制到该角色或用户
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     */
    @SuppressWarnings("rawtypes")
    private boolean canCopyAuthorityTree(String objectId, String objectType, String menuId) {
        boolean flag = true;
        if ("1".equals(objectType)) {
            flag = true;
        } else {
            String sql = "select count(r.resource_id) from t_resource r, t_role_res rr where r.resource_id=rr.resource_id and rr.role_id='" + objectId
                    + "' and r.resourcekey='" + menuId + "'";
            List list = AuthDatabaseUtil.queryForList(sql);
            BigDecimal count = (BigDecimal) list.get(0);
            if (count.intValue() == 0) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 获取当前登录人员的有权限的树节点IDs
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    public List<String> getTreeIdsByAuthority(String menuId, String componentVersionId) {
        SysUser user = CommonUtil.getUser();
        List<String> treeNodeIds = new ArrayList<String>();
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        String hql = null;
        // 当前登录人的树节点权限
        hql = "select t.treeNodeId from AuthorityTree t where t.menuId='" + menuId + "' and t.componentVersionId='" + componentVersionId
                + "' and t.objectType='" + Authority.OT_USER + "' and t.objectId='" + user.getId() + "'";
        treeNodeIds = dao.queryEntityForList(hql, String.class);
        if (null == treeNodeIds || treeNodeIds.isEmpty()) {
            // 如果当前登录人没有配置，则取当前登录人员的拥有的角色的树节点权限
            String roleIds = CommonUtil.getRoleIds();
            if (StringUtil.isNotEmpty(roleIds)) {
                hql = "select t.treeNodeId from AuthorityTree t where t.menuId='" + menuId + "' and t.componentVersionId='" + componentVersionId
                        + "' and t.objectType='" + Authority.OT_ROLE + "' and t.objectId in('" + roleIds.replace(",", "','") + "')";
                treeNodeIds = dao.queryEntityForList(hql, String.class);
            }
        }
        return treeNodeIds;
    }

    /**
     * 根据角色IDs和菜单IDs获取树权限
     * 
     * @param roleIds 角色IDs
     * @param menuIds 菜单IDs
     * @return List<AuthorityTree>
     */
    public List<AuthorityTree> getByRoleIdsAndMenuIds(String roleIds, String menuIds) {
        List<AuthorityTree> authorityTreeList = new ArrayList<AuthorityTree>();
        if (StringUtil.isNotEmpty(roleIds) && StringUtil.isNotEmpty(menuIds)) {
            String hql = "from AuthorityTree t where t.objectType='0' and t.objectId in ('" + roleIds.replace(",", "','") + "') and t.menuId in ('"
                    + menuIds.replace(",", "','") + "')";
            authorityTreeList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, AuthorityTree.class);
        }
        return authorityTreeList;
    }

    /**
     * 根据菜单IDs获取用户的树权限
     * 
     * @param menuIds 菜单IDs
     * @return List<AuthorityTree>
     */
    public List<AuthorityTree> getByMenuIdsOfUser(String menuIds) {
        List<AuthorityTree> authorityTreeList = new ArrayList<AuthorityTree>();
        if (StringUtil.isNotEmpty(menuIds)) {
            String hql = "from AuthorityTree t where t.objectType='1' and t.menuId in ('" + menuIds.replace(",", "','") + "')";
            authorityTreeList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, AuthorityTree.class);
        }
        return authorityTreeList;
    }
}

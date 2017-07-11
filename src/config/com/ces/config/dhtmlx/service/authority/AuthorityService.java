package com.ces.config.dhtmlx.service.authority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.facade.OrgInfoFacade;
import ces.sdk.system.facade.RoleInfoFacade;
import ces.sdk.system.facade.UserInfoFacade;
import ces.sdk.system.factory.SystemFacadeFactory;

import com.ces.config.dhtmlx.dao.authority.AuthorityDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.authority.Authority;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.AuthDatabaseUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.security.entity.SysUser;
import com.google.common.collect.Lists;

/**
 * 菜单权限Service
 * 
 * @author wanglei
 * @date 2014-09-25
 */
@Component
public class AuthorityService extends ConfigDefineDaoService<Authority, AuthorityDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityDao")
    @Override
    protected void setDaoUnBinding(AuthorityDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取组织用户树
     * 
     * @param orgId 组织ID
     * @return Object
     */
    public Object getOrgUserList(String orgId, String currentUserId) {
        List<DhtmlxTreeNode> list = new ArrayList<DhtmlxTreeNode>();
        try {
            OrgInfoFacade orgInfoFacade = CommonUtil.getOrgInfoFacade();
            List<OrgInfo> orgInfoList = orgInfoFacade.findChildsByParentId(orgId);
            if (CollectionUtils.isNotEmpty(orgInfoList)) {
                for (OrgInfo orgInfo : orgInfoList) {
                    // 排除掉独立用户
                    if ("1".equals(orgInfo.getId())) {
                        continue;
                    }
                    if (!"-1".equals(orgInfo.getId())) {
                        DhtmlxTreeNode node = new DhtmlxTreeNode();
                        node.setId("O_" + String.valueOf(orgInfo.getId()));
                        node.setText(orgInfo.getName());
                        node.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        node.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        node.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        node.setType("2");
                        node.setChild("1");
                        list.add(node);
                    }
                }
            }
            List<UserInfo> userInfoList = CommonUtil.getUserInfoFacade().findUsersByOrgId(orgId);
            if (CollectionUtils.isNotEmpty(userInfoList)) {
                for (UserInfo userInfo : userInfoList) {
                    if (StringUtil.isNotEmpty(currentUserId) && currentUserId.equals(userInfo.getId())) {
                        continue;
                    }
                    DhtmlxTreeNode node = new DhtmlxTreeNode();
                    node.setId("U_" + String.valueOf(userInfo.getId()));
                    node.setText(userInfo.getLoginName());
                    node.setIm0(ConstantVar.IconTreeNode.LEAF);
                    node.setIm1(ConstantVar.IconTreeNode.LEAF);
                    node.setIm2(ConstantVar.IconTreeNode.LEAF);
                    node.setType("1");
                    node.setChild("0");
                    list.add(node);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    /**
     * 获取系统管理平台用户
     * 
     * @return Object
     * @throws SystemFacadeException
     */
    @SuppressWarnings("rawtypes")
    public Object getAllUserList() throws SystemFacadeException {
        // 获得操作代理工厂：
        SystemFacadeFactory systemFacadeFactory = SystemFacadeFactory.newInstance();
        // 获得操作代理：
        UserInfoFacade userFacade = systemFacadeFactory.createUserInfoFacade();
        // 获得实体工厂：
        List<UserInfo> userlist = userFacade.findAll();
        List<DhtmlxTreeNode> list = Lists.newArrayList();
        for (int i = 0; i < userlist.size(); i++) {
        	UserInfo user = userlist.get(i);
            DhtmlxTreeNode tree = new DhtmlxTreeNode();
            tree.setId("U_" + user.getId());
            tree.setText(user.getLoginName());
            tree.setIm0(ConstantVar.IconTreeNode.LEAF);
            tree.setIm1(ConstantVar.IconTreeNode.LEAF);
            tree.setIm2(ConstantVar.IconTreeNode.LEAF);
            // 用户节点[0-角色 1-人员 2-部门]
            tree.setType("1");
            tree.setChild("0");
            list.add(tree);
        }
        return list;
    }

    /**
     * 获取用户管理平台所有角色
     * 
     * @return Object
     * @throws SystemFacadeException
     */
    @SuppressWarnings("rawtypes")
    public Object getAllRoleList() throws SystemFacadeException {
        // 获得操作代理工厂：
        SystemFacadeFactory systemFacadeFactory = SystemFacadeFactory.newInstance();
        // 获得操作代理：
        RoleInfoFacade roleFacade = systemFacadeFactory.createRoleInfoFacade();
        // 获得实体工厂：
        List<RoleInfo> rolelist = roleFacade.findAllRoleInfos();
        List<DhtmlxTreeNode> list = Lists.newArrayList();
        for (int i = 0; i < rolelist.size(); i++) {
        	RoleInfo role = rolelist.get(i);
            DhtmlxTreeNode tree = new DhtmlxTreeNode();
            tree.setId("R_" + String.valueOf(role.getId()));
            tree.setText(role.getName());
            tree.setIm0(ConstantVar.IconTreeNode.LEAF);
            tree.setIm1(ConstantVar.IconTreeNode.LEAF);
            tree.setIm2(ConstantVar.IconTreeNode.LEAF);
            // 角色节点[0-角色 1-人员 2-部门]
            tree.setType("0");
            tree.setChild("0");
            list.add(tree);
        }

        return list;
    }

    /**
     * 获取用户管理平台某系统下的角色
     * 
     * @return Object
     * @throws SystemFacadeException
     */
    @SuppressWarnings("rawtypes")
    public Object getSystemRoleList(String systemId, String currentRoleId) throws SystemFacadeException {
        // 获取系统管理平台中的系统ID
        String sql = "select s.system_id from t_system s where s.system_code='" + systemId + "'";
        List systemIdList = AuthDatabaseUtil.queryForList(sql);
        if (CollectionUtils.isNotEmpty(systemIdList)) {
            String sId = String.valueOf(systemIdList.get(0));
            // 获得操作代理工厂：
            SystemFacadeFactory systemFacadeFactory = SystemFacadeFactory.newInstance();
            // 获得操作代理：
            RoleInfoFacade roleFacade = systemFacadeFactory.createRoleInfoFacade();
            // 获得实体工厂：
            List<RoleInfo> rolelist = roleFacade.findRolesBySystemId(sId, null);
            List<DhtmlxTreeNode> list = Lists.newArrayList();
            for (int i = 0; i < rolelist.size(); i++) {
            	RoleInfo role = rolelist.get(i);
                if (StringUtil.isNotEmpty(currentRoleId) && currentRoleId.equals(role.getId())) {
                    continue;
                }
                DhtmlxTreeNode tree = new DhtmlxTreeNode();
                tree.setId("R_" + String.valueOf(role.getId()));
                tree.setText(role.getName());
                tree.setIm0(ConstantVar.IconTreeNode.LEAF);
                tree.setIm1(ConstantVar.IconTreeNode.LEAF);
                tree.setIm2(ConstantVar.IconTreeNode.LEAF);
                // 角色节点[0-角色 1-人员 2-部门]
                tree.setType("0");
                tree.setChild("0");
                list.add(tree);
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * 获取有权限的菜单IDs
     * 
     * @param objectId 角色或用户ID
     * @param objectType 类型：0-角色 1-人员
     * @return List<String>
     */
    public List<String> getMenuIds(String objectId, String objectType) {
        return getDao().getMenuIds(objectId, objectType);
    }

    /**
     * 根据菜单ID删除菜单权限设定
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        if (count("EQ_menuId=" + menuId) > 0) {
            getDao().deleteByMenuId(menuId);
            AuthorityUtil.getInstance().clearMenuOrButtonAuthority(AuthorityUtil.AUTHORITY_MENU);
        }
    }

    /**
     * 保存菜单权限
     * 
     * @param objectId 角色或用户ID
     * @param objectType 类型：0-角色 1-人员
     * @param authorityIds 菜单IDs
     */
    @Transactional
    public void saveAuthoritys(String objectId, String objectType, String authorityIds) {
        // 删除旧的权限
        getDao().deleteAuthoritys(objectId, objectType);
        // 保存新的权限
        String[] authorityIdArray = authorityIds.split(",");
        if (authorityIdArray.length > 0) {
            List<Authority> authorityList = Lists.newArrayList();
            for (String authorityId : authorityIdArray) {
                Authority authority = new Authority();
                authority.setObjectId(objectId);
                authority.setObjectType(objectType);
                authority.setMenuId(authorityId);
                authorityList.add(authority);
            }
            save(authorityList);
        }
        AuthorityUtil.getInstance().clearMenuOrButtonAuthority(AuthorityUtil.AUTHORITY_MENU, objectId, objectType);
    }

    /**
     * 获取当前登录用户的菜单权限，如果用户配置了菜单权限，直接使用用户本身的菜单权限；如果没有，则使用用户绑定的角色的菜单权限合集
     * 
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    public List<String> getCurrentUserMenu(String systemId) {
        SysUser user = CommonUtil.getUser();
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        List<String> resourceList = null;
        if (StringUtil.isEmpty(systemId)) {
            // 当前登录人的菜单权限
            String hql = "select t.menuId from Authority t where t.objectType='" + Authority.OT_USER + "' and t.objectId='" + user.getId() + "'";
            resourceList = dao.queryEntityForList(hql, String.class);
            // 取当前登录人员的拥有的角色的菜单权限
            if (CollectionUtils.isEmpty(resourceList)) {
                String roleIds = CommonUtil.getRoleIds();
                if (StringUtil.isNotEmpty(roleIds)) {
                    hql = "select t.menuId from Authority t where t.objectType='" + Authority.OT_ROLE + "' and t.objectId in('" + roleIds.replace(",", "','")
                            + "')";
                    resourceList = dao.queryEntityForList(hql, String.class);
                }
            }
        } else {
            // 当前登录人的菜单权限
            String sql = "select t.menuId from t_xtpz_authority t, t_xtpz_menu m where t.menu_id=m.id and m.root_menu_id='" + systemId + "' and t.objectType='"
                    + Authority.OT_USER + "' and t.objectId='" + user.getId() + "'";
            resourceList = dao.queryForList(sql);
            // 取当前登录人员的拥有的角色的菜单权限
            if (CollectionUtils.isEmpty(resourceList)) {
                String roleIds = CommonUtil.getRoleIds();
                if (StringUtil.isNotEmpty(roleIds)) {
                    sql = "select t.menuId from t_xtpz_authority t, t_xtpz_menu m where t.menu_id=m.id and m.root_menu_id='" + systemId
                            + "' and t.objectType='" + Authority.OT_ROLE + "' and t.objectId in('" + roleIds.replace(",", "','") + "')";
                    resourceList = dao.queryForList(sql);
                }
            }
        }
        return resourceList;
    }

}

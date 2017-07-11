package com.ces.config.dhtmlx.service.authority;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ces.config.dhtmlx.dao.authority.AuthorityConstructButtonDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityConstructButton;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.security.entity.SysUser;
import com.google.common.collect.Lists;

/**
 * 构件组装按钮权限Service
 * 
 * @author wanglei
 * @date 2014-05-08
 */
@Component
public class AuthorityConstructButtonService extends ConfigDefineDaoService<AuthorityConstructButton, AuthorityConstructButtonDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityConstructButtonDao")
    @Override
    protected void setDaoUnBinding(AuthorityConstructButtonDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 保存不可用构件组装按钮
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 组合构件ID
     * @param constructDetailIds 预留区和构件绑定关系IDs
     */
    @Transactional
    public void saveAuthButton(String objectId, String objectType, String menuId, String componentVersionId, String constructDetailIds) {
        if (objectType.equals("-U")) {
            objectType = "1";
        } else if (objectType.equals("-R")) {
            objectType = "0";
        }
        getDao().deleteByMenuIdAndComponentVersionId(menuId, componentVersionId);
        if (StringUtil.isNotEmpty(constructDetailIds)) {
            String[] constructDetailIdArray = constructDetailIds.split(",");
            List<AuthorityConstructButton> list = Lists.newArrayList();
            AuthorityConstructButton authButton = null;
            for (int i = 0; i < constructDetailIdArray.length; i++) {
                authButton = new AuthorityConstructButton();
                authButton.setObjectId(objectId);
                authButton.setObjectType(objectType);
                authButton.setMenuId(menuId);
                authButton.setComponentVersionId(componentVersionId);
                authButton.setConstructDetailId(constructDetailIdArray[i]);
                list.add(authButton);
            }
            getDao().save(list);
        }
        AuthorityUtil.getInstance().clearMenuOrButtonAuthority(AuthorityUtil.AUTHORITY_CONSTRUCT_BUTTON, objectId, objectType);
    }

    /**
     * 获取无权限的构件组装按钮的constructDetailId，如果用户配置了权限，直接使用用户本身的权限；如果没有，则使用用户绑定的角色的权限合集
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 组合构件ID
     */
    public List<String> notAuthorityConstructButtons(String menuId, String componentVersionId) {
        SysUser user = CommonUtil.getUser();
        String hql = getNotAuthorityConstructButtonsQuery(user.getId(), "1", menuId, componentVersionId);
        List<String> list = DatabaseHandlerDao.getInstance().queryEntityForList(hql, String.class);
        if (CollectionUtils.isEmpty(list)) {
            String roleIds = CommonUtil.getRoleIds();
            if (StringUtil.isNotEmpty(roleIds)) {
                hql = getNotAuthorityConstructButtonsQuery(roleIds, "0", menuId, componentVersionId);
                list = DatabaseHandlerDao.getInstance().queryEntityForList(hql, String.class);
            }
        }
        return list;
    }

    /**
     * 获取无权限的构件组装按钮的查询语句
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 组合构件ID
     */
    private String getNotAuthorityConstructButtonsQuery(String objectId, String objectType, String menuId, String componentVersionId) {
        String sql = "select t.constructDetailId from AuthorityConstructButton t where";
        if ("0".equals(objectType)) {
            sql += " t.objectId in ('" + objectId.replace(",", "','") + "')";
        } else {
            sql += " t.objectId='" + objectId + "'";
        }
        sql += " and t.objectType='" + objectType + "' and t.menuId='" + menuId + "' and t.componentVersionId='" + componentVersionId + "'";
        return sql;
    }

    /**
     * 根据菜单ID删除构件组装按钮权限设定
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        if (count("EQ_menuId=" + menuId) > 0) {
            getDao().deleteByMenuId(menuId);
            AuthorityUtil.getInstance().clearMenuOrButtonAuthority(AuthorityUtil.AUTHORITY_CONSTRUCT_BUTTON);
        }
    }

    /**
     * 根据组合构件ID删除构件组装按钮权限设定
     * 
     * @param componentVersionId 组合构件ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        if (count("EQ_componentVersionId=" + componentVersionId) > 0) {
            getDao().deleteByComponentVersionId(componentVersionId);
            AuthorityUtil.getInstance().clearMenuOrButtonAuthority(AuthorityUtil.AUTHORITY_CONSTRUCT_BUTTON);
        }
    }

    /**
     * 根据预留区和构件绑定关系ID删除构件组装按钮权限设定
     * 
     * @param constructDetailId 预留区和构件绑定关系ID
     */
    @Transactional
    public void deleteByConstructDetailId(String constructDetailId) {
        if (count("EQ_constructDetailId=" + constructDetailId) > 0) {
            getDao().deleteByConstructDetailId(constructDetailId);
            AuthorityUtil.getInstance().clearMenuOrButtonAuthority(AuthorityUtil.AUTHORITY_CONSTRUCT_BUTTON);
        }
    }
}

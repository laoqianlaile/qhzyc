package com.ces.config.dhtmlx.service.authority;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ces.config.dhtmlx.dao.authority.AuthorityComponentButtonDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityComponentButton;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.security.entity.SysUser;
import com.google.common.collect.Lists;

/**
 * 开发的构件按钮权限Service
 * 
 * @author wanglei
 * @date 2014-07-31
 */
@Component
public class AuthorityComponentButtonService extends ConfigDefineDaoService<AuthorityComponentButton, AuthorityComponentButtonDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityComponentButtonDao")
    @Override
    protected void setDaoUnBinding(AuthorityComponentButtonDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 保存不可用开发的构件按钮
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 开发的构件版本ID
     * @param componentButtonIds 开发构件中按钮IDs
     */
    @Transactional
    public void saveAuthButton(String objectId, String objectType, String menuId, String componentVersionId, String componentButtonIds) {
        if (objectType.equals("-U")) {
            objectType = "1";
        } else if (objectType.equals("-R")) {
            objectType = "0";
        }
        getDao().deleteByComponentVersionId(componentVersionId);
        if (StringUtil.isNotEmpty(componentButtonIds)) {
            String[] componentButtonIdArray = componentButtonIds.split(",");
            List<AuthorityComponentButton> list = Lists.newArrayList();
            AuthorityComponentButton authButton = null;
            for (int i = 0; i < componentButtonIdArray.length; i++) {
                authButton = new AuthorityComponentButton();
                authButton.setObjectId(objectId);
                authButton.setObjectType(objectType);
                authButton.setMenuId(menuId);
                authButton.setComponentVersionId(componentVersionId);
                authButton.setComponentButtonId(componentButtonIdArray[i]);
                list.add(authButton);
            }
            getDao().save(list);
        }
        AuthorityUtil.getInstance().clearMenuOrButtonAuthority(AuthorityUtil.AUTHORITY_COMPONENT_BUTTON, objectId, objectType);
    }

    /**
     * 获取无权限的构件按钮，如果用户配置了权限，直接使用用户本身的权限；如果没有，则使用用户绑定的角色的权限合集
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 开发的构件版本ID
     */
    public List<String> notAuthorityComponentButtons(String menuId, String componentVersionId) {
        SysUser user = CommonUtil.getUser();
        String hql = getNotAuthorityComponentButtonsQuery(user.getId(), "1", menuId, componentVersionId);
        List<String> list = DatabaseHandlerDao.getInstance().queryEntityForList(hql, String.class);
        if (CollectionUtils.isEmpty(list)) {
            String roleIds = CommonUtil.getRoleIds();
            if (StringUtil.isNotEmpty(roleIds)) {
                hql = getNotAuthorityComponentButtonsQuery(roleIds, "0", menuId, componentVersionId);
                list = DatabaseHandlerDao.getInstance().queryEntityForList(hql, String.class);
            }
        }
        return list;
    }

    /**
     * 获取无权限的开发的构件按钮的查询语句
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 开发的构件版本ID
     */
    private String getNotAuthorityComponentButtonsQuery(String objectId, String objectType, String menuId, String componentVersionId) {
        String sql = "select b.name from AuthorityComponentButton t, ComponentButton b where t.componentButtonId=b.id and";
        if ("0".equals(objectType)) {
            sql += " t.objectId in ('" + objectId.replace(",", "','") + "')";
        } else {
            sql += " t.objectId='" + objectId + "'";
        }
        sql += " and t.objectType='" + objectType + "' and t.menuId='" + menuId + "' and t.componentVersionId='" + componentVersionId + "'";
        return sql;
    }

    /**
     * 根据菜单ID删除开发的构件按钮权限设定
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        if (count("EQ_menuId=" + menuId) > 0) {
            getDao().deleteByMenuId(menuId);
            AuthorityUtil.getInstance().clearMenuOrButtonAuthority(AuthorityUtil.AUTHORITY_COMPONENT_BUTTON);
        }
    }

    /**
     * 根据开发构件版本ID删除开发的构件按钮权限设定
     * 
     * @param componentVersionId 开发的构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
    }
}

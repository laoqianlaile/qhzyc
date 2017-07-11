package com.ces.config.dhtmlx.service.authority;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.authority.AuthorityTreeDataDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.authority.Authority;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeData;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.security.entity.SysUser;

/**
 * 树数据权限Service
 * 
 * @author wanglei
 * @date 2015-06-08
 */
@Component
public class AuthorityTreeDataService extends ConfigDefineDaoService<AuthorityTreeData, AuthorityTreeDataDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityTreeDataDao")
    @Override
    protected void setDaoUnBinding(AuthorityTreeDataDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取树数据权限
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    public AuthorityTreeData getAuthorityTreeData(String objectId, String objectType, String menuId, String componentVersionId) {
        return getDao().getAuthorityTreeData(objectId, objectType, menuId, componentVersionId);
    }

    /**
     * 删除树数据权限
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteAuthorityTreeData(String objectId, String objectType, String menuId, String componentVersionId) {
        getDao().deleteAuthorityTreeData(objectId, objectType, menuId, componentVersionId);
    }

    /**
     * 保存树权限配置
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param controlDataAuth 控制数据权限
     */
    @Transactional
    public AuthorityTreeData saveAuthorityTreeData(String objectId, String objectType, String menuId, String componentVersionId, String controlDataAuth) {
        AuthorityTreeData authorityTreeData = getDao().getAuthorityTreeData(objectId, objectType, menuId, componentVersionId);
        if (authorityTreeData == null) {
            authorityTreeData = new AuthorityTreeData();
            authorityTreeData.setObjectId(objectId);
            authorityTreeData.setObjectType(objectType);
            authorityTreeData.setMenuId(menuId);
            authorityTreeData.setComponentVersionId(componentVersionId);
        }
        authorityTreeData.setControlDataAuth(controlDataAuth);
        getDao().save(authorityTreeData);
        return authorityTreeData;
    }

    /**
     * 获取当前登录人是否通过树来控制数据权限
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    public String getTreeControlDataAuth(String menuId, String componentVersionId) {
        SysUser user = CommonUtil.getUser();
        List<String> controlDataAuths = new ArrayList<String>();
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        String hql = null;
        // 当前登录人的树数据权限
        hql = "select t.controlDataAuth from AuthorityTreeData t where t.menuId='" + menuId + "' and t.componentVersionId='" + componentVersionId
                + "' and t.objectType='" + Authority.OT_USER + "' and t.objectId='" + user.getId() + "'";
        controlDataAuths = dao.queryEntityForList(hql, String.class);
        if (null == controlDataAuths || controlDataAuths.isEmpty()) {
            // 如果当前登录人没有配置，则取当前登录人员的拥有的角色的树数据权限
            String roleIds = CommonUtil.getRoleIds();
            if (StringUtil.isNotEmpty(roleIds)) {
                hql = "select t.controlDataAuth from AuthorityTreeData t where t.menuId='" + menuId + "' and t.componentVersionId='" + componentVersionId
                        + "' and t.objectType='" + Authority.OT_ROLE + "' and t.objectId in('" + roleIds.replace(",", "','") + "')";
                controlDataAuths = dao.queryEntityForList(hql, String.class);
            }
        }
        String controlDataAuth = "0";
        if (CollectionUtils.isNotEmpty(controlDataAuths)) {
            for (String controlDA : controlDataAuths) {
                if ("1".equals(controlDA)) {
                    controlDataAuth = "1";
                    break;
                }
            }
        }
        return controlDataAuth;
    }
}

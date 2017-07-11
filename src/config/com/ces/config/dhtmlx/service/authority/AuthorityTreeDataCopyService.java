package com.ces.config.dhtmlx.service.authority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.authority.AuthorityTreeDataCopyDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeDataCopy;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 树数据权限Service（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
@Component
public class AuthorityTreeDataCopyService extends ConfigDefineDaoService<AuthorityTreeDataCopy, AuthorityTreeDataCopyDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityTreeDataCopyDao")
    @Override
    protected void setDaoUnBinding(AuthorityTreeDataCopyDao dao) {
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
    public AuthorityTreeDataCopy getAuthorityTreeData(String objectId, String objectType, String menuId, String componentVersionId) {
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
     * @param treeDefineIds 有权限的树节点
     */
    @Transactional
    public AuthorityTreeDataCopy saveAuthorityTreeData(String objectId, String objectType, String menuId, String componentVersionId, String controlDataAuth) {
        AuthorityTreeDataCopy authorityTreeDataCopy = getDao().getAuthorityTreeData(objectId, objectType, menuId, componentVersionId);
        if (authorityTreeDataCopy == null) {
            authorityTreeDataCopy = new AuthorityTreeDataCopy();
            authorityTreeDataCopy.setObjectId(objectId);
            authorityTreeDataCopy.setObjectType(objectType);
            authorityTreeDataCopy.setMenuId(menuId);
            authorityTreeDataCopy.setComponentVersionId(componentVersionId);
        }
        authorityTreeDataCopy.setControlDataAuth(controlDataAuth);
        getDao().save(authorityTreeDataCopy);
        return authorityTreeDataCopy;
    }
}

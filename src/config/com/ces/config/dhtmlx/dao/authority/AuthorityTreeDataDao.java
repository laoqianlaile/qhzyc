package com.ces.config.dhtmlx.dao.authority;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.authority.AuthorityTreeData;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 树数据权限DAO
 * 
 * @author wanglei
 * @date 2015-06-08
 */
public interface AuthorityTreeDataDao extends StringIDDao<AuthorityTreeData> {

    /**
     * 获取树数据权限
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    @Query("from AuthorityTreeData where objectId=? and objectType=? and menuId=? and componentVersionId=?")
    public AuthorityTreeData getAuthorityTreeData(String objectId, String objectType, String menuId, String componentVersionId);

    /**
     * 删除树数据权限
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityTreeData where objectId=? and objectType=? and menuId=? and componentVersionId=?")
    public void deleteAuthorityTreeData(String objectId, String objectType, String menuId, String componentVersionId);

}

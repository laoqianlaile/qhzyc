package com.ces.config.dhtmlx.dao.authority;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.authority.AuthorityTree;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 树权限DAO
 * 
 * @author wanglei
 * @date 2014-09-25
 */
public interface AuthorityTreeDao extends StringIDDao<AuthorityTree> {

    /**
     * 删除树权限
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityTree where objectId=? and objectType=? and menuId=? and componentVersionId=?")
    public void deleteAuthorityTree(String objectId, String objectType, String menuId, String componentVersionId);

    /**
     * 获取树权限treeNodeIds
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<String>
     */
    @Query("select t.treeNodeId from AuthorityTree t where t.objectId=?1 and t.objectType=?2 and t.menuId=?3 and t.componentVersionId=?4")
    public List<String> getAuthorityTreeNodeIds(String objectId, String objectType, String menuId, String componentVersionId);
}

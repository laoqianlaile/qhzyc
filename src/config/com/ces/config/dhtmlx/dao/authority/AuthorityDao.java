package com.ces.config.dhtmlx.dao.authority;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.authority.Authority;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 菜单权限DAO
 * 
 * @author wanglei
 * @date 2014-09-25
 */
public interface AuthorityDao extends StringIDDao<Authority> {

    /**
     * 获取有权限的菜单IDs
     * 
     * @param objectId 角色或用户ID
     * @param objectType 类型：0-角色 1-人员
     * @return List<String>
     */
    @Query("select t.menuId from Authority t where t.objectId=?1 and t.objectType=?2")
    public List<String> getMenuIds(String objectId, String objectType);

    /**
     * 根据菜单ID删除菜单权限设定
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    @Modifying
    @Query("delete from Authority where menuId=?")
    public void deleteByMenuId(String menuId);

    /**
     * 删除菜单权限
     * 
     * @param objectId 角色或用户ID
     * @param objectType 类型：0-角色 1-人员
     */
    @Transactional
    @Modifying
    @Query(value = "delete Authority where objectId=?1 and objectType=?2")
    public void deleteAuthoritys(String objectId, String objectType);

    /**
     * 获取菜单权限
     * 
     * @param objectId 角色或用户ID
     * @param objectType 类型：0-角色 1-人员
     * @param menuId 菜单ID
     * @return Authority
     */
    @Query("from Authority where objectId=?1 and objectType=?2 and menuId=?3")
    public Authority getAuthority(String objectId, String objectType, String menuId);

}

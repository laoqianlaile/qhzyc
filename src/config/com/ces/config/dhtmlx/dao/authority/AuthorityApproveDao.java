package com.ces.config.dhtmlx.dao.authority;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.authority.AuthorityApprove;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 数据权限待审批DAO（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
public interface AuthorityApproveDao extends StringIDDao<AuthorityApprove> {

    /**
     * 获取数据权限待审批的记录（树权限使用）
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return AuthorityApprove
     */
    @Query("from AuthorityApprove t where t.objectId=? and t.objectType=? and t.menuId=? and t.componentVersionId=? and authorityType='1'")
    public AuthorityApprove getTreeAuthorityApprove(String objectId, String objectType, String menuId, String componentVersionId);

    /**
     * 获取数据权限待审批的记录（数据权限、编码权限使用）
     * 
     * @param relateAuthId 关联的权限ID
     * @return AuthorityApprove
     */
    @Query("from AuthorityApprove t where t.relateAuthId=?")
    public AuthorityApprove getByRelateAuthId(String relateAuthId);

    /**
     * 获取数据权限待审批的记录
     * 
     * @return List<AuthorityApprove>
     */
    @Query("from AuthorityApprove t where status='0'")
    public List<AuthorityApprove> getAllApprovingAuthorityList();

    /**
     * 删除的数据权限待审批的记录（树权限使用）
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityApprove t where t.objectId=? and t.objectType=? and t.menuId=? and t.componentVersionId=? and authorityType='1'")
    public void deleteAuthorityData(String objectId, String objectType, String menuId, String componentVersionId);

    /**
     * 删除的数据权限待审批的记录（数据权限、编码权限使用）
     * 
     * @param relateAuthId 关联的权限ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityApprove t where t.relateAuthId=?")
    public void deleteByRelateAuthId(String relateAuthId);

    /**
     * 删除的数据权限待审批的记录
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityApprove t where t.menuId=?")
    public void deleteByMenuId(String menuId);
}

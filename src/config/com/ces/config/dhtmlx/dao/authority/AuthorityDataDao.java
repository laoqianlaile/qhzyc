package com.ces.config.dhtmlx.dao.authority;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.authority.AuthorityData;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 数据权限DAO
 * 
 * @author wanglei
 * @date 2014-09-25
 */
public interface AuthorityDataDao extends StringIDDao<AuthorityData> {

    /**
     * 获取某菜单某构件下的数据权限
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<AuthorityData>
     */
    @Query("from AuthorityData where objectId=? and objectType=? and menuId=? and componentVersionId=? order by showOrder")
    public List<AuthorityData> getAuthorityDataList(String objectId, String objectType, String menuId, String componentVersionId);

    /**
     * 根据名称获取该构件的数据权限
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param name 名称
     * @return AuthorityData
     */
    @Query("from AuthorityData where objectId=? and objectType=? and menuId=? and componentVersionId=? and name=?")
    public AuthorityData getAuthorityDataByName(String objectId, String objectType, String menuId, String componentVersionId, String name);

    /**
     * 根据菜单ID和构件版本ID获取最大顺序
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return Integer
     */
    @Query(value = "select max(showOrder) from AuthorityData where menuId=? and componentVersionId=?")
    public Integer getMaxShowOrder(String menuId, String componentVersionId);

    /**
     * 删除的数据权限
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityData t where t.objectId=? and t.objectType=? and t.menuId=? and t.componentVersionId=?")
    public void deleteAuthorityData(String objectId, String objectType, String menuId, String componentVersionId);

    /**
     * 根据菜单ID删除数据权限
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityData where menuId=?")
    public void deleteByMenuId(String menuId);

    /**
     * 根据构件版本ID删除数据权限
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityData where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * 根据表ID删除数据权限详情
     * 
     * @param tableId 表ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityData t where t.tableId=?1")
    public void deleteByTableId(String tableId);
}

package com.ces.config.dhtmlx.dao.authority;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.authority.AuthorityDataDetailCopy;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 数据权限详情DAO（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
public interface AuthorityDataDetailCopyDao extends StringIDDao<AuthorityDataDetailCopy> {

    /**
     * 根据数据权限ID获取数据权限详情
     * 
     * @param authorityDataId 数据权限ID
     * @return List<AuthorityDataDetailCopy>
     */
    @Query("from AuthorityDataDetailCopy t where t.authorityDataId=?1 order by t.showOrder")
    public List<AuthorityDataDetailCopy> getByAuthorityDataId(String authorityDataId);

    /**
     * 根据数据权限ID和表ID获取数据权限详情
     * 
     * @param authorityDataId 数据权限ID
     * @param tableId 表ID
     * @return List<AuthorityDataDetailCopy>
     */
    @Query("from AuthorityDataDetailCopy t where t.authorityDataId=?1 and t.tableId=?2 order by t.showOrder")
    public List<AuthorityDataDetailCopy> getByAuthorityDataIdAndTableId(String authorityDataId, String tableId);

    /**
     * 根据数据权限ID删除数据权限详情
     * 
     * @param authorityDataId 数据权限ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityDataDetailCopy t where t.authorityDataId=?1")
    public void deleteByAuthorityDataId(String authorityDataId);

    /**
     * 根据字段ID删除数据权限详情
     * 
     * @param columnId 字段ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityDataDetailCopy t where t.columnId=?1")
    public void deleteByColumnId(String columnId);

    /**
     * 根据菜单ID删除数据权限
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_auth_data_detail_copy t where t.authority_data_id in (select ad.id from t_xtpz_auth_data_copy ad where ad.menu_id=?)", nativeQuery = true)
    public void deleteByMenuId(String menuId);

    /**
     * 根据构件版本ID删除数据权限
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_auth_data_detail_copy t where t.authority_data_id in (select ad.id from t_xtpz_auth_data_copy ad where ad.component_version_id=?)", nativeQuery = true)
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * 根据表ID删除数据权限详情
     * 
     * @param tableId 表ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_auth_data_detail_copy t where t.table_id=?", nativeQuery = true)
    public void deleteByTableId(String tableId);
}

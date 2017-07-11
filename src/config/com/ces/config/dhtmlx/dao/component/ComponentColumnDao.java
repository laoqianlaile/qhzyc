package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.component.ComponentColumn;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件相关表的字段Dao
 * 
 * @author wanglei
 * @date 2013-08-16
 */
public interface ComponentColumnDao extends StringIDDao<ComponentColumn> {

    /**
     * 根据构件版本ID及表ID获取构件版本相关表的字段
     * 
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @return List<ComponentColumn>
     */
    @Query(value = "select * from t_xtpz_component_column where id in (select column_id from t_xtpz_component_table_column where component_version_id=? and table_id=?)", nativeQuery = true)
    public List<ComponentColumn> getByComponentVersionIdAndTableId(String componentVersionId, String tableId);

    /**
     * 根据表ID获取构件版本相关表的字段
     * 
     * @param tableId 表ID
     * @return List<ComponentColumn>
     */
    @Query(value = "select * from t_xtpz_component_column where id in (select distinct column_id from t_xtpz_component_table_column where table_id=?)", nativeQuery = true)
    public List<ComponentColumn> getByTableId(String tableId);

    /**
     * 根据构件版本ID获取构件版本相关表的字段
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentColumn>
     */
    @Query(value = "select * from t_xtpz_component_column where id in (select column_id from t_xtpz_component_table_column where component_version_id=?)", nativeQuery = true)
    public List<ComponentColumn> getByComponentVersionId(String componentVersionId);
}

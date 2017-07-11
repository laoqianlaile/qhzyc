package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.component.ComponentTable;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件相关表Dao
 * 
 * @author wanglei
 * @date 2013-08-16
 */
public interface ComponentTableDao extends StringIDDao<ComponentTable> {

    /**
     * 根据构件版本ID获取构件版本相关表
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentTable>
     */
    @Query(value = "select * from t_xtpz_component_table where id in (select table_id from t_xtpz_component_table_column where component_version_id=?)", nativeQuery = true)
    public List<ComponentTable> getByComponentVersionId(String componentVersionId);

    /**
     * 根据名称获取构件相关表
     * 
     * @param name 表名称
     * @return ComponentTable
     */
    public ComponentTable getByName(String name);
}

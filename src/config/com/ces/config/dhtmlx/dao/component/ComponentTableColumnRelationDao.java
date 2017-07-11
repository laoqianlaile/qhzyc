package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentTableColumnRelation;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件版本、构件表、关联字段三者关联关系Dao
 * 
 * @author wanglei
 * @date 2013-08-16
 */
public interface ComponentTableColumnRelationDao extends StringIDDao<ComponentTableColumnRelation> {

    /**
     * 根据构件版本ID获取该构件版本下的（构件版本、构件表和关联字段）的关联关系
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentTableColumnRelation>
     */
    public List<ComponentTableColumnRelation> getByComponentVersionId(String componentVersionId);

    /**
     * 根据构件版本ID删除该构件版本下的（构件版本、构件表和关联字段）的关联关系
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentTableColumnRelation where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * 删除构件表的列中的脏数据
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_component_column where id not in (select column_id from t_xtpz_component_table_column)", nativeQuery = true)
    public void deleteComponentColumn();

    /**
     * 删除构件表中的脏数据
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_component_table where id not in (select table_id from t_xtpz_component_table_column)", nativeQuery = true)
    public void deleteComponentTable();
}

package com.ces.config.dhtmlx.dao.construct;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.construct.ConstructFilterDetail;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 数据过滤条件详情DAO
 * 
 * @author wanglei
 * @date 2015-05-20
 */
public interface ConstructFilterDetailDao extends StringIDDao<ConstructFilterDetail> {

    /**
     * 根据数据过滤条件ID获取数据过滤条件详情
     * 
     * @param constructFilterId 数据过滤条件ID
     * @return List<ConstructFilterDetail>
     */
    @Query("from ConstructFilterDetail t where t.constructFilterId=?1 order by showOrder")
    public List<ConstructFilterDetail> getByConstructFilterId(String constructFilterId);

    /**
     * 根据数据过滤条件ID删除数据过滤条件详情
     * 
     * @param constructFilterId 数据过滤条件ID
     */
    @Transactional
    @Modifying
    @Query("delete ConstructFilterDetail t where t.constructFilterId=?1")
    public void deleteByConstructFilterId(String constructFilterId);

    /**
     * 根据字段ID删除数据过滤条件详情
     * 
     * @param columnId 字段ID
     */
    @Transactional
    @Modifying
    @Query("delete ConstructFilterDetail t where t.columnId=?1")
    public void deleteByColumnId(String columnId);

    /**
     * 删除数据过滤条件
     * 
     * @param topComVersionId 上层的组合构件的版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_construct_filter_detail t where t.construct_filter_id in (select cf.id from t_xtpz_construct_filter cf where cf.top_com_version_id=?)", nativeQuery = true)
    public void deleteByTopComVersionId(String topComVersionId);

    /**
     * 删除数据过滤条件
     * 
     * @param inputParamId 构件入参ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_construct_filter_detail t where t.construct_filter_id in (select cf.id from t_xtpz_construct_filter cf where cf.component_version_id=?)", nativeQuery = true)
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * 删除数据过滤条件
     * 
     * @param tableId 表ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_construct_filter_detail t where t.construct_filter_id in (select cf.id from t_xtpz_construct_filter cf where cf.table_id=?)", nativeQuery = true)
    public void deleteByTableId(String tableId);
}

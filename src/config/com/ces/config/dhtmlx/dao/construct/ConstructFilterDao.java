package com.ces.config.dhtmlx.dao.construct;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.construct.ConstructFilter;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 数据过滤条件Dao
 * 
 * @author wanglei
 * @date 2015-05-20
 */
public interface ConstructFilterDao extends StringIDDao<ConstructFilter> {

    /**
     * 获取数据过滤条件
     * 
     * @param topComVersionId 上层的组合构件的版本ID
     * @param componentVersionId 控制权限的构件版本ID
     * @param tableId 表ID
     * @return ConstructFilter
     */
    @Query("from ConstructFilter c where c.topComVersionId=? and componentVersionId=? and tableId=?")
    public ConstructFilter getConstructFilter(String topComVersionId, String componentVersionId, String tableId);

    /**
     * 删除数据过滤条件
     * 
     * @param topComVersionId 上层的组合构件的版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructFilter where topComVersionId=?")
    public void deleteByTopComVersionId(String topComVersionId);

    /**
     * 删除数据过滤条件
     * 
     * @param inputParamId 构件入参ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructFilter where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * 删除数据过滤条件
     * 
     * @param tableId 表ID
     */
    @Transactional
    @Modifying
    @Query("delete from ConstructFilter where tableId=?")
    public void deleteByTableId(String tableId);
}

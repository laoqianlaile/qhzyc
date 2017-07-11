package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentFunctionData;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件前台JS方法返回值Dao
 * 
 * @author wanglei
 * @date 2013-08-08
 */
public interface ComponentFunctionDataDao extends StringIDDao<ComponentFunctionData> {

    /**
     * 根据返回值名称和构件方法ID获取该方法的返回值
     * 
     * @param name 返回值名称
     * @param functionId 构件方法ID
     * @return ComponentFunctionData
     */
    public ComponentFunctionData getByNameAndFunctionId(String name, String functionId);

    /**
     * 根据构件方法ID获取该方法的返回值
     * 
     * @param functionId 构件方法ID
     * @return List<ComponentFunctionData>
     */
    public List<ComponentFunctionData> getByFunctionId(String functionId);

    /**
     * 根据方法ID删除该方法下的返回值
     * 
     * @param functionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentFunctionData where functionId=?")
    public void deleteByFunctionId(String functionId);

    /**
     * 根据构件版本ID删除该构件版本下的页面方法的返回值
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_xtpz_component_function_data where function_id in (select id from t_xtpz_component_function where component_version_id=?)", nativeQuery = true)
    public void deleteByComponentVersionId(String componentVersionId);
}

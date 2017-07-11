package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentFunction;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件前台JS方法Dao
 * 
 * @author wanglei
 * @date 2013-08-08
 */
public interface ComponentFunctionDao extends StringIDDao<ComponentFunction> {

    /**
     * 根据名称和构件版本ID获取该构件版本下的页面方法
     * 
     * @param name 名称
     * @param componentVersionId 构件版本ID
     * @return ComponentFunction
     */
    public ComponentFunction getByNameAndComponentVersionId(String name, String componentVersionId);

    /**
     * 根据名称和构件版本ID获取该构件版本下的页面方法(自定义构件使用，因为自定义构件存在方法名相同的方法)
     * 
     * @param name 名称
     * @param componentVersionId 构件版本ID
     * @param remark 介绍
     * @return ComponentFunction
     */
    @Query("from ComponentFunction where name=? and componentVersionId=? and remark=?")
    public ComponentFunction getComponentFunction(String name, String componentVersionId, String remark);

    /**
     * 根据构件版本ID获取该构件版本下的页面方法
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentFunction>
     */
    public List<ComponentFunction> getByComponentVersionId(String componentVersionId);

    /**
     * 获取自定义构件公用的页面方法
     * 
     * @return List<ComponentFunction>
     */
    @Query("from ComponentFunction where componentVersionId is null")
    public List<ComponentFunction> getCommonFunctions();

    /**
     * 根据构件版本ID删除该构件版本下的页面方法
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete from ComponentFunction where componentVersionId=?")
    public void deleteByComponentVersionId(String componentVersionId);
}

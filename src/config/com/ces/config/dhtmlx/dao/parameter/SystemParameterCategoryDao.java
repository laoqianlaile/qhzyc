package com.ces.config.dhtmlx.dao.parameter;

import java.util.List;

import com.ces.config.dhtmlx.entity.parameter.SystemParameterCategory;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 系统参数类别Dao
 * 
 * @author wanglei
 * @date 2013-08-12
 */
public interface SystemParameterCategoryDao extends StringIDDao<SystemParameterCategory> {

    /**
     * 根据名称和父节点获取系统参数类型
     * 
     * @param name 系统参数类型名称
     * @param parentId 系统参数类型父节点ID
     * @return SystemParameterCategory
     */
    public SystemParameterCategory getByNameAndParentId(String name, String parentId);

    /**
     * 根据父节点获取系统参数类型
     * 
     * @param parentId 系统参数类型父节点ID
     * @return SystemParameterCategory
     */
    public List<SystemParameterCategory> getByParentId(String parentId);
}

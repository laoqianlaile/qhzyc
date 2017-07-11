package com.ces.component.moduledemo.dhtmlx.dao;

import java.util.List;

import com.ces.component.moduledemo.dhtmlx.entity.ModuleCategory;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ModuleCategoryDao extends StringIDDao<ModuleCategory> {

    /**
     * <p>标题: getByNameAndParentId</p>
     * <p>描述: 根据名称和父节点获取ModuleCategory</p>
     * @param name ModuleCategory名称
     * @param parentId ModuleCategory父节点ID
     * @return ModuleCategory 返回类型
     */
    public ModuleCategory getByNameAndParentId(String name, String parentId);

    /**
     * <p>标题: getByParentId</p>
     * <p>描述: 根据父节点获取ModuleCategory</p>
     * @param parentId ModuleCategory父节点ID
     * @return ModuleCategory 返回类型
     */
    public List<ModuleCategory> getByParentId(String parentId);
}

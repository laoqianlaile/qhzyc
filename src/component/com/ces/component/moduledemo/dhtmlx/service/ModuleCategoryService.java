package com.ces.component.moduledemo.dhtmlx.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.component.moduledemo.dhtmlx.dao.ModuleCategoryDao;
import com.ces.component.moduledemo.dhtmlx.entity.ModuleCategory;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

@Component("moduleCategoryService")
public class ModuleCategoryService extends ConfigDefineDaoService<ModuleCategory, ModuleCategoryDao> {

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("moduleCategoryDao")
    @Override
    protected void setDaoUnBinding(ModuleCategoryDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据名称和父节点获取ModuleCategory
     * 
     * @param name ModuleCategory名称
     * @param parentId ModuleCategory父节点ID
     * @return ModuleCategory
     */
    public ModuleCategory getByNameAndParentId(String name, String parentId) {
        return getDao().getByNameAndParentId(name, parentId);
    }

    /**
     * 根据父节点获取ModuleCategory
     * 
     * @param parentId ModuleCategory父节点ID
     * @return List<ModuleCategory>
     */
    public List<ModuleCategory> getCategoryListByParentId(String parentId) {
        return getDao().getByParentId(parentId);
    }
}

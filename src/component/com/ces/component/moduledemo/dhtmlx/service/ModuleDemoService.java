package com.ces.component.moduledemo.dhtmlx.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.component.moduledemo.dhtmlx.dao.ModuleDemoDao;
import com.ces.component.moduledemo.dhtmlx.entity.ModuleDemo;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

@Component("moduleDemoService")
public class ModuleDemoService extends ConfigDefineDaoService<ModuleDemo, ModuleDemoDao> {

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("moduleDemoDao")
    @Override
    protected void setDaoUnBinding(ModuleDemoDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据名称获取ModuleDemo
     * 
     * @param name ModuleDemo名称
     * @return ModuleDemo
     */
    public ModuleDemo getModuleDemoByName(String name) {
        return getDao().getByName(name);
    }

    /**
     * 获取显示顺序范围内的ModuleDemo
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param categoryId 参数分类ID
     * @return List<ModuleDemo>
     */
    public List<ModuleDemo> getModuleDemoListByShowOrder(Integer start, Integer end, String categoryId) {
        return getDao().getByShowOrderBetweenAndCategoryId(start, end, categoryId);
    }

    /**
     * 获取ModuleDemoCategory下编码的最大显示顺序
     * 
     * @param categoryId ModuleDemoCategoryID
     * @return Integer
     */
    public Integer getMaxShowOrder(String categoryId) {
        Integer maxShowOrder = getDao().getMaxShowOrder(categoryId);
        if (maxShowOrder == null) {
            maxShowOrder = new Integer(0);
        }
        return maxShowOrder;
    }

    /**
     * 根据分类ID获取ModuleDemo
     * 
     * @param categoryId 分类ID
     * @return List<ModuleDemo>
     */
    public List<ModuleDemo> getByCategoryId(String categoryId) {
        return getDao().getByCategoryId(categoryId);
    }
}

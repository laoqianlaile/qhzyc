package com.ces.component.moduledemo.dhtmlx.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.moduledemo.dhtmlx.entity.ModuleDemo;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ModuleDemoDao extends StringIDDao<ModuleDemo> {

    /**
     * <p>标题: getByName</p>
     * <p>描述: 根据名称获取ModuleDemo</p>
     * @param name ModuleDemo名称
     * @return ModuleDemo 返回类型
     */
    public ModuleDemo getByName(String name);

    /**
     * <p>标题: getByCategoryId</p>
     * <p>描述: 根据分类ID获取ModuleDemo</p>
     * @param categoryId 分类ID
     * @return List<ModuleDemo> 返回类型
     */
    public List<ModuleDemo> getByCategoryId(String categoryId);

    /**
     * <p>标题: getByShowOrderBetweenAndCategoryId</p>
     * <p>描述: 获取显示顺序范围内的ModuleDemo</p>
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param categoryId 参数分类ID
     * @return List<ModuleDemo> 返回类型
     */
    public List<ModuleDemo> getByShowOrderBetweenAndCategoryId(Integer start, Integer end, String categoryId);

    /**
     * <p>标题: getMaxShowOrder</p>
     * <p>描述: 获取ModuleDemoCategory下的ModuleDemo最大显示顺序</p>
     * @param categoryId ModuleDemoCategoryID
     */
    @Query("select max(showOrder) from ModuleDemo where categoryId=?")
    public Integer getMaxShowOrder(String categoryId);
}

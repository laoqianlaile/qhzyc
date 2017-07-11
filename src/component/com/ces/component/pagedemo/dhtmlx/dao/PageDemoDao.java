package com.ces.component.pagedemo.dhtmlx.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.pagedemo.dhtmlx.entity.PageDemo;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface PageDemoDao extends StringIDDao<PageDemo> {

    /**
     * <p>标题: getByName</p>
     * <p>描述: 根据名称获取PageDemo</p>
     * @param name PageDemo名称
     * @return PageDemo 返回类型
     */
    public PageDemo getByName(String name);

    /**
     * <p>标题: getByModuleId</p>
     * <p>描述: 根据分类ID获取PageDemo</p>
     * @param moduleId 分类ID
     * @return List<PageDemo> 返回类型
     */
    public List<PageDemo> getByModuleId(String moduleId);

    /**
     * <p>标题: getByShowOrderBetweenAndModuleId</p>
     * <p>描述: 获取显示顺序范围内的PageDemo</p>
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param moduleId 参数分类ID
     * @return List<PageDemo> 返回类型
     */
    public List<PageDemo> getByShowOrderBetweenAndModuleId(Integer start, Integer end, String moduleId);

    /**
     * <p>标题: getMaxShowOrder</p>
     * <p>描述: 获取PageDemoCategory下的PageDemo最大显示顺序</p>
     * @param moduleId PageDemoCategoryID
     */
    @Query("select max(showOrder) from PageDemo where moduleId=?")
    public Integer getMaxShowOrder(String moduleId);
}

package com.ces.config.dhtmlx.dao.label;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.label.ColumnLabelCategory;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 字段标签分类Dao
 * 
 * @author wanglei
 * @date 2013-07-15
 */
public interface ColumnLabelCategoryDao extends StringIDDao<ColumnLabelCategory> {

    /**
     * 获取字段标签分类
     * 
     * @return List<ColumnLabelCategory>
     */
    @Query("from ColumnLabelCategory order by showOrder")
    public List<ColumnLabelCategory> getAllColumnLabelCategory();

    /**
     * 根据名称获取字段标签分类
     * 
     * @param name 字段标签分类名称
     * @return ColumnLabelCategory
     */
    public ColumnLabelCategory getByName(String name);
    
    /**
     * 根据名称获取字段标签分类(区分菜单)
     * 
     * @param name 字段标签分类名称
     * @return ColumnLabelCategory
     */
    public ColumnLabelCategory getByNameAndMenuId(String name, String menuId);

    /**
     * 获取字段标签分类最大显示顺序
     * 
     * @return Integer
     */
    @Query("select max(showOrder) from ColumnLabelCategory")
    public Integer getMaxShowOrder();

    /**
     * 获取显示顺序范围内的字段标签分类
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @return List<ColumnLabelCategory>
     */
    public List<ColumnLabelCategory> getByShowOrderBetween(Integer start, Integer end);
    
    /**
     * 获取显示顺序范围内的字段标签分类(区分菜单)
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @return List<ColumnLabelCategory>
     */
    public List<ColumnLabelCategory> getByShowOrderBetweenAndMenuId(Integer start, Integer end, String menuId);
    
    /**
     * 根据菜单ID获取字段标签分类
     * 
     * @return List<ColumnLabelCategory>
     */
    public List<ColumnLabelCategory> getByMenuId(String menuId);

}

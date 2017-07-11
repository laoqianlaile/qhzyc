package com.ces.config.dhtmlx.dao.label;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.label.TypeLabel;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 分类标签Dao
 * 
 * @author wangjianmin
 * @date 2014-07-25
 */
public interface TypeLabelDao extends StringIDDao<TypeLabel> {

    /**
     * 根据名称获取表标签
     * 
     * @param name 表标签名称
     * @return TypeLabel
     */
    public TypeLabel getByName(String name);

    /**
     * 根据编码获取表标签
     * 
     * @param code 表标签编码
     * @return TypeLabel
     */
    public TypeLabel getByCode(String code);
    
    /**
     * 根据名称获取表标签(区分菜单)
     * 
     * @param name 表标签名称
     * @return TypeLabel
     */
    public TypeLabel getByNameAndMenuId(String name, String menuId);

    /**
     * 根据编码获取表标签(区分菜单)
     * 
     * @param code 表标签编码
     * @return TypeLabel
     */
    public TypeLabel getByCodeAndMenuId(String code, String menuId);

    /**
     * 获取表标签最大显示顺序
     * 
     * @return Integer
     */
    @Query("select max(showOrder) from TypeLabel")
    public Integer getMaxShowOrder();

    /**
     * 获取显示顺序范围内的表标签
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @return List<TypeLabel>
     */
    public List<TypeLabel> getByShowOrderBetween(Integer start, Integer end);
    
    /**
     * 获取显示顺序范围内的表标签(区分菜单)
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @return List<TypeLabel>
     */
    public List<TypeLabel> getByShowOrderBetweenAndMenuId(Integer start, Integer end, String menuId);
}

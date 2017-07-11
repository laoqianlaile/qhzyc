package com.ces.config.dhtmlx.dao.parameter;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.parameter.SystemParameter;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 系统参数Dao
 * 
 * @author wanglei
 * @date 2013-08-12
 */
public interface SystemParameterDao extends StringIDDao<SystemParameter> {

    /**
     * 根据名称获取系统参数
     * 
     * @param name 系统参数名称
     * @return SystemParameter
     */
    public SystemParameter getByName(String name);

    /**
     * 根据分类ID获取系统参数
     * 
     * @param categoryId 分类ID
     * @return List<SystemParameter>
     */
    public List<SystemParameter> getByCategoryId(String categoryId);

    /**
     * 获取显示顺序范围内的系统参数
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param categoryId 参数分类ID
     * @return List<SystemParameter>
     */
    public List<SystemParameter> getByShowOrderBetweenAndCategoryId(Integer start, Integer end, String categoryId);

    /**
     * 获取系统参数类型下的系统参数最大显示顺序
     * 
     * @param categoryId 系统参数类型ID
     */
    @Query("select max(showOrder) from SystemParameter where categoryId=?")
    public Integer getMaxShowOrder(String categoryId);
}

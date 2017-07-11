package com.ces.config.dhtmlx.dao.label;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.label.ColumnLabel;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 字段标签Dao
 * 
 * @author wanglei
 * @date 2014-07-23
 */
public interface ColumnLabelDao extends StringIDDao<ColumnLabel> {

    /**
     * 获取字段标签
     * 
     * @return List<ColumnLabel>
     */
    @Query("from ColumnLabel order by showOrder")
    public List<ColumnLabel> getAllColumnLabel();

    /**
     * 根据分类Id获取字段标签
     * 
     * @param categoryId 分类ID
     * @return List<ColumnLabel>
     */
    @Query("from ColumnLabel where categoryId=? order by showOrder")
    public List<ColumnLabel> getColumnLabelList(String categoryId);

    /**
     * 根据名称获取字段标签
     * 
     * @param name 字段标签名称
     * @return ColumnLabel
     */
    public ColumnLabel getByName(String name);

    /**
     * 根据编码获取字段标签
     * 
     * @param code 字段标签编码
     * @return ColumnLabel
     */
    public ColumnLabel getByCode(String code);

    /**
     * 获取字段标签最大显示顺序
     * 
     * @param categoryId 分类ID
     * @return Integer
     */
    @Query("select max(showOrder) from ColumnLabel where categoryId=?")
    public Integer getMaxShowOrder(String categoryId);

    /**
     * 获取显示顺序范围内的字段标签
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param categoryId 分类ID
     * @return List<ColumnLabel>
     */
    public List<ColumnLabel> getByShowOrderBetweenAndCategoryId(Integer start, Integer end, String categoryId);

    /**
     * 剔除本表已经绑定的字段标签
     * 
     * @param tableId
     * @return
     */
    @Query(value = "select code,name from t_xtpz_column_label a where not EXISTS (select column_label from t_xtpz_column_define b where table_id = ?1 and a.code = b.column_label) order by show_order", nativeQuery = true)
    public List<Object[]> getUnBindedLabel(String tableId);

    /**
     * 根据分类ID删除字段标签
     * 
     * @param categoryId 分类ID
     */
    @Transactional
    @Modifying
    @Query("delete from ColumnLabel where categoryId=?")
    public void deleteByCategoryId(String categoryId);
}

package com.ces.config.dhtmlx.dao.completecomponent;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponentArea;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 成品构件分类Dao
 * 
 * @author wanglei
 * @date 2014-02-17
 */
public interface CompleteComponentAreaDao extends StringIDDao<CompleteComponentArea> {

    /**
     * 根据名称获取成品构件分类
     * 
     * @param name 成品构件分类名称
     * @return CompleteComponentArea
     */
    public CompleteComponentArea getByName(String name);

    /**
     * 获取子成品构件分类
     * 
     * @param parentId 父分类ID
     * @return List<CompleteComponentArea>
     */
    @Query("from CompleteComponentArea where parentId=? order by showOrder")
    public List<CompleteComponentArea> getByParentId(String parentId);

    /**
     * 获取成品构件分类下子分类最大显示顺序
     * 
     * @param parentId 父分类ID
     */
    @Query("select max(showOrder) from CompleteComponentArea where parentId=?")
    public Integer getMaxShowOrder(String parentId);

    /**
     * 获取显示顺序范围内的成品构件分类
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param parentId 父分类ID
     * @return List<CompleteComponentArea>
     */
    public List<CompleteComponentArea> getByShowOrderBetweenAndParentId(Integer start, Integer end, String parentId);

    /**
     * 将成品构件分类的显示顺序加一
     * 
     * @param start 开始显示顺序
     * @param parentId 父分类ID
     */
    @Transactional
    @Modifying
    @Query("update CompleteComponentArea set showOrder=showOrder+1 where showOrder>? and parentId=?")
    public void updateShowOrderPlusOne(Integer start, String parentId);
}

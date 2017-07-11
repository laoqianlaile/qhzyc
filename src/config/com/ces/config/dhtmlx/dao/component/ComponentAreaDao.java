package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentArea;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 构件分类Dao
 * 
 * @author wanglei
 * @date 2013-07-18
 */
public interface ComponentAreaDao extends StringIDDao<ComponentArea> {

    /**
     * 根据名称获取构件分类
     * 
     * @param name 构件分类名称
     * @param parentId 父构件分类ID
     * @return ComponentArea
     */
    public ComponentArea getByNameAndParentId(String name, String parentId);

    /**
     * 获取子构件分类
     * 
     * @param parentId 父构件分类ID
     * @return List<ComponentArea>
     */
    @Query("from ComponentArea where parentId=? order by showOrder")
    public List<ComponentArea> getByParentId(String parentId);

    /**
     * 获取构件分类下子构件分类最大显示顺序
     * 
     * @param parentId 父构件分类ID
     */
    @Query("select max(showOrder) from ComponentArea where parentId=?")
    public Integer getMaxShowOrder(String parentId);

    /**
     * 获取显示顺序范围内的构件分类
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param parentId 父构件分类ID
     * @return List<ComponentArea>
     */
    public List<ComponentArea> getByShowOrderBetweenAndParentId(Integer start, Integer end, String parentId);

    /**
     * 将构件分类的显示顺序加一
     * 
     * @param start 开始显示顺序
     * @param parentId 父构件分类ID
     */
    @Transactional
    @Modifying
    @Query("update ComponentArea set showOrder=showOrder+1 where showOrder>? and parentId=?")
    public void updateShowOrderPlusOne(Integer start, String parentId);
}

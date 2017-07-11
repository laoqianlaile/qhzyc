package com.ces.config.dhtmlx.dao.component;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.component.ComponentAssembleArea;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 组合构件分类Dao
 * 
 * @author qiujinwei
 * @date 2015-04-08
 */
public interface ComponentAssembleAreaDao extends StringIDDao<ComponentAssembleArea> {

    /**
     * 根据名称获取构件分类
     * 
     * @param name 构件分类名称
     * @param parentId 父构件分类ID
     * @return ComponentAssembleArea
     */
    public ComponentAssembleArea getByNameAndParentId(String name, String parentId);

    /**
     * 获取子构件分类
     * 
     * @param parentId 父构件分类ID
     * @return List<ComponentAssembleArea>
     */
    @Query("from ComponentAssembleArea where parentId=? order by showOrder")
    public List<ComponentAssembleArea> getByParentId(String parentId);

    /**
     * 获取构件分类下子构件分类最大显示顺序
     * 
     * @param parentId 父构件分类ID
     */
    @Query("select max(showOrder) from ComponentAssembleArea where parentId=?")
    public Integer getMaxShowOrder(String parentId);

    /**
     * 获取显示顺序范围内的构件分类
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param parentId 父构件分类ID
     * @return List<ComponentAssembleArea>
     */
    public List<ComponentAssembleArea> getByShowOrderBetweenAndParentId(Integer start, Integer end, String parentId);

    /**
     * 将构件分类的显示顺序加一
     * 
     * @param start 开始显示顺序
     * @param parentId 父构件分类ID
     */
    @Transactional
    @Modifying
    @Query("update ComponentAssembleArea set showOrder=showOrder+1 where showOrder>? and parentId=?")
    public void updateShowOrderPlusOne(Integer start, String parentId);
}

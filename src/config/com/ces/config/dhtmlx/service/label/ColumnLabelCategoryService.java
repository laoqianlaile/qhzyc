package com.ces.config.dhtmlx.service.label;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ces.config.dhtmlx.dao.label.ColumnLabelCategoryDao;
import com.ces.config.dhtmlx.dao.label.ColumnLabelDao;
import com.ces.config.dhtmlx.entity.label.ColumnLabelCategory;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 字段标签分类Service
 * 
 * @author wanglei
 * @date 2013-07-15
 */
@Component("columnLabelCategoryService")
public class ColumnLabelCategoryService extends ConfigDefineDaoService<ColumnLabelCategory, ColumnLabelCategoryDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("columnLabelCategoryDao")
    @Override
    protected void setDaoUnBinding(ColumnLabelCategoryDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        Assert.notNull(ids, "要删除的实体ID不能为空");
        String[] idArr = ids.split(",");
        for (int i = 0; i < idArr.length; i++) {
            getDaoFromContext(ColumnLabelDao.class).deleteByCategoryId(idArr[i]);
            getDao().delete(idArr[i]);
        }
    }

    /**
     * 获取字段标签分类
     * 
     * @return List<ColumnLabelCategory>
     */
    public List<ColumnLabelCategory> getAllColumnLabelCategory() {
        return getDao().getAllColumnLabelCategory();
    }

    /**
     * 根据名称获取字段标签分类
     * 
     * @param name 字段标签分类名称
     * @return ColumnLabelCategory
     */
    public ColumnLabelCategory getColumnLabelCategoryByName(String name) {
        return getDao().getByName(name);
    }
    
    /**
     * 根据名称获取字段标签分类(区分菜单)
     * 
     * @param name 字段标签分类名称
     * @return ColumnLabelCategory
     */
    public ColumnLabelCategory getColumnLabelCategoryByName(String name, String menuId) {
        return getDao().getByNameAndMenuId(name, menuId);
    }

    /**
     * 获取字段标签分类最大显示顺序
     * 
     * @return Integer
     */
    public Integer getMaxShowOrder() {
        return getDao().getMaxShowOrder();
    }

    /**
     * 获取显示顺序范围内的字段标签分类
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @return List<ColumnLabelCategory>
     */
    public List<ColumnLabelCategory> getByShowOrder(Integer start, Integer end) {
        return getDao().getByShowOrderBetween(start, end);
    }
    
    /**
     * 获取显示顺序范围内的字段标签分类(区分菜单)
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @return List<ColumnLabelCategory>
     */
    public List<ColumnLabelCategory> getByShowOrder(Integer start, Integer end, String menuId) {
        return getDao().getByShowOrderBetweenAndMenuId(start, end, menuId);
    }
    
    /**
     * 根据菜单ID获取字段标签分类
     * 
     * @return List<ColumnLabelCategory>
     */
    public List<ColumnLabelCategory> getByMenuId(String menuId){
    	return getDao().getByMenuId(menuId);
    }
}

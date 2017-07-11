package com.ces.config.dhtmlx.service.completecomponent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.completecomponent.CompleteComponentAreaDao;
import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponentArea;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 成品构件分类Service
 * 
 * @author wanglei
 * @date 2014-02-17
 */
@Component("completeComponentAreaService")
public class CompleteComponentAreaService extends ConfigDefineDaoService<CompleteComponentArea, CompleteComponentAreaDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("completeComponentAreaDao")
    @Override
    protected void setDaoUnBinding(CompleteComponentAreaDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据名称获取成品构件分类
     * 
     * @param name 成品构件分类名称
     * @return CompleteComponentArea
     */
    public CompleteComponentArea getByName(String name) {
        return getDao().getByName(name);
    }

    /**
     * 获取子成品构件分类
     * 
     * @param parentId 父成品构件分类ID
     * @return List<CompleteComponentArea>
     */
    public List<CompleteComponentArea> getByParentId(String parentId) {
        return getDao().getByParentId(parentId);
    }

    /**
     * 获取成品构件分类下子分类最大显示顺序
     * 
     * @param parentId 父分类ID
     * @return Integer
     */
    public Integer getMaxShowOrder(String parentId) {
        return getDao().getMaxShowOrder(parentId);
    }

    /**
     * 获取显示顺序范围内的成品构件分类
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param parentId 父分类ID
     * @return List<CompleteComponentArea>
     */
    public List<CompleteComponentArea> getCompleteComponentAreaListByShowOrder(Integer start, Integer end, String parentId) {
        return getDao().getByShowOrderBetweenAndParentId(start, end, parentId);
    }

    /**
     * 将成品构件分类的显示顺序加一
     * 
     * @param start 开始显示顺序
     * @param parentId 父分类ID
     */
    public void updateShowOrderPlusOne(Integer start, String parentId) {
        getDao().updateShowOrderPlusOne(start, parentId);
    }
}

package com.ces.component.pagedemo.dhtmlx.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.component.pagedemo.dhtmlx.dao.PageDemoDao;
import com.ces.component.pagedemo.dhtmlx.entity.PageDemo;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

@Component("pageDemoService")
public class PageDemoService extends ConfigDefineDaoService<PageDemo, PageDemoDao> {

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("pageDemoDao")
    @Override
    protected void setDaoUnBinding(PageDemoDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据名称获取PageDemo
     * 
     * @param name PageDemo名称
     * @return PageDemo
     */
    public PageDemo getPageDemoByName(String name) {
        return getDao().getByName(name);
    }

    /**
     * 获取显示顺序范围内的PageDemo
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param moduleId 参数分类ID
     * @return List<PageDemo>
     */
    public List<PageDemo> getPageDemoListByShowOrder(Integer start, Integer end, String moduleId) {
        return getDao().getByShowOrderBetweenAndModuleId(start, end, moduleId);
    }

    /**
     * 获取PageDemoModule下编码的最大显示顺序
     * 
     * @param moduleId PageDemoModuleID
     * @return Integer
     */
    public Integer getMaxShowOrder(String moduleId) {
        Integer maxShowOrder = getDao().getMaxShowOrder(moduleId);
        if (maxShowOrder == null) {
            maxShowOrder = new Integer(0);
        }
        return maxShowOrder;
    }

    /**
     * 根据分类ID获取PageDemo
     * 
     * @param moduleId 分类ID
     * @return List<PageDemo>
     */
    public List<PageDemo> getByModuleId(String moduleId) {
        return getDao().getByModuleId(moduleId);
    }
}

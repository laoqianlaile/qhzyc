package com.ces.config.dhtmlx.service.parameter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.parameter.SystemParameterDao;
import com.ces.config.dhtmlx.entity.parameter.SystemParameter;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 系统参数Service
 * 
 * @author wanglei
 * @date 2013-08-12
 */
@Component("systemParameterService")
public class SystemParameterService extends ConfigDefineDaoService<SystemParameter, SystemParameterDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("systemParameterDao")
    @Override
    protected void setDaoUnBinding(SystemParameterDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据名称获取系统参数
     * 
     * @param name 系统参数名称
     * @return SystemParameter
     */
    public SystemParameter getSystemParameterByName(String name) {
        return getDao().getByName(name);
    }

    /**
     * 获取显示顺序范围内的系统参数
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param categoryId 参数分类ID
     * @return List<SystemParameter>
     */
    public List<SystemParameter> getSystemParameterListByShowOrder(Integer start, Integer end, String categoryId) {
        return getDao().getByShowOrderBetweenAndCategoryId(start, end, categoryId);
    }

    /**
     * 获取系统参数类型下编码的最大显示顺序
     * 
     * @param categoryId 系统参数类型ID
     * @return Integer
     */
    public Integer getMaxShowOrder(String categoryId) {
        Integer maxShowOrder = getDao().getMaxShowOrder(categoryId);
        if (maxShowOrder == null) {
            maxShowOrder = new Integer(0);
        }
        return maxShowOrder;
    }

    /**
     * 根据分类ID获取系统参数
     * 
     * @param categoryId 分类ID
     * @return List<SystemParameter>
     */
    public List<SystemParameter> getByCategoryId(String categoryId) {
        return getDao().getByCategoryId(categoryId);
    }
}

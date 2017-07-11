package com.ces.config.dhtmlx.service.parameter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.parameter.SystemParameterCategoryDao;
import com.ces.config.dhtmlx.entity.parameter.SystemParameterCategory;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 系统参数类别Service
 * 
 * @author wanglei
 * @date 2013-08-12
 */
@Component("systemParameterCategoryService")
public class SystemParameterCategoryService extends ConfigDefineDaoService<SystemParameterCategory, SystemParameterCategoryDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("systemParameterCategoryDao")
    @Override
    protected void setDaoUnBinding(SystemParameterCategoryDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据名称和父节点获取系统参数类型
     * 
     * @param name 系统参数类型名称
     * @param parentId 系统参数类型父节点ID
     * @return SystemParameterCategory
     */
    public SystemParameterCategory getByNameAndParentId(String name, String parentId) {
        return getDao().getByNameAndParentId(name, parentId);
    }

    /**
     * 根据父节点获取系统参数类型
     * 
     * @param parentId 系统参数类型父节点ID
     * @return List<SystemParameterCategory>
     */
    public List<SystemParameterCategory> getCategoryListByParentId(String parentId) {
        return getDao().getByParentId(parentId);
    }
}

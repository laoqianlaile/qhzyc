package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentFunctionDao;
import com.ces.config.dhtmlx.dao.component.ComponentFunctionDataDao;
import com.ces.config.dhtmlx.dao.construct.ConstructFunctionDao;
import com.ces.config.dhtmlx.entity.component.ComponentFunction;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;

/**
 * 构件前台JS方法Service
 * 
 * @author wanglei
 * @date 2013-08-08
 */
@Component("componentFunctionService")
public class ComponentFunctionService extends ConfigDefineDaoService<ComponentFunction, ComponentFunctionDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentFunctionDao")
    @Override
    protected void setDaoUnBinding(ComponentFunctionDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ComponentFunction save(ComponentFunction entity) {
        if (StringUtil.isEmpty(entity.getPage())) {
            entity.setPage(AppDefineUtil.getPage(0));
        }
        return getDao().save(entity);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] functionIds = ids.split(",");
        for (int i = 0; i < functionIds.length; i++) {
            getDaoFromContext(ConstructFunctionDao.class).deleteByFunctionId(functionIds[i]);
            getDaoFromContext(ComponentFunctionDataDao.class).deleteByFunctionId(functionIds[i]);
            getDao().delete(functionIds[i]);
        }
    }

    /**
     * 根据名称和构件版本ID获取页面方法
     * 
     * @param name 名称
     * @param componentVersionId 构件版本ID
     * @return ComponentFunction
     */
    public ComponentFunction getByNameAndComponentVersionId(String name, String componentVersionId) {
        return getDao().getByNameAndComponentVersionId(name, componentVersionId);
    }

    /**
     * 根据名称和构件版本ID获取该构件版本下的页面方法(自定义构件使用，因为自定义构件存在方法名相同的方法)
     * 
     * @param name 名称
     * @param componentVersionId 构件版本ID
     * @param remark 介绍
     * @return ComponentFunction
     */
    public ComponentFunction getComponentFunction(String name, String componentVersionId, String remark) {
        return getDao().getComponentFunction(name, componentVersionId, remark);
    }

    /**
     * 根据构件版本ID获取该构件版本下的页面方法
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentFunction>
     */
    public List<ComponentFunction> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取该构件版本下的页面方法
     * 
     * @param componentVersionIds 构件版本IDs
     * @return List<ComponentFunction>
     */
    public List<ComponentFunction> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentFunction> componentFunctionList = new ArrayList<ComponentFunction>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentFunction t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentFunctionList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentFunction.class);
        }
        return componentFunctionList;
    }

    /**
     * 获取自定义构件公用的页面方法
     * 
     * @return List<ComponentFunction>
     */
    public List<ComponentFunction> getCommonFunctions() {
        return getDao().getCommonFunctions();
    }

    /**
     * 根据构件版本ID删除该构件版本下的页面方法
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDaoFromContext(ConstructFunctionDao.class).deleteFunctionByComponentVersionId(componentVersionId);
        getDaoFromContext(ComponentFunctionDataDao.class).deleteByComponentVersionId(componentVersionId);
        getDao().deleteByComponentVersionId(componentVersionId);
    }
}

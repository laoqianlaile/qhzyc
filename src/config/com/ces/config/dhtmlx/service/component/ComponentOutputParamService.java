package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentOutputParamDao;
import com.ces.config.dhtmlx.dao.construct.ConstructCallbackDao;
import com.ces.config.dhtmlx.entity.component.ComponentOutputParam;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 构件出参Service
 * 
 * @author wanglei
 * @date 2013-09-27
 */
@Component("componentOutputParamService")
public class ComponentOutputParamService extends ConfigDefineDaoService<ComponentOutputParam, ComponentOutputParamDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentOutputParamDao")
    @Override
    protected void setDaoUnBinding(ComponentOutputParamDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] outputParamIds = ids.split(",");
        for (int i = 0; i < outputParamIds.length; i++) {
            getDaoFromContext(ConstructCallbackDao.class).deleteByOutputParamId(outputParamIds[i]);
            getDao().delete(outputParamIds[i]);
        }
    }

    /**
     * 根据构件出参名称和构件版本ID获取构件出参
     * 
     * @param name 构件出参名称
     * @param componentVersionId 构件版本ID
     * @return ComponentParameter
     */
    public ComponentOutputParam getByNameAndComponentVersionId(String name, String componentVersionId) {
        return getDao().getByNameAndComponentVersionId(name, componentVersionId);
    }

    /**
     * 根据构件版本ID获取构件出参
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentParameter>
     */
    public List<ComponentOutputParam> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取构件出参
     * 
     * @param componentVersionIds 构件版本IDs
     * @return List<ComponentParameter>
     */
    public List<ComponentOutputParam> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentOutputParam> componentOutputParamList = new ArrayList<ComponentOutputParam>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentOutputParam t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentOutputParamList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentOutputParam.class);
        }
        return componentOutputParamList;
    }

    /**
     * 根据构件版本ID删除构件出参
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDaoFromContext(ConstructCallbackDao.class).deleteOutParamByComponentVersionId(componentVersionId);
        getDao().deleteByComponentVersionId(componentVersionId);
    }
}

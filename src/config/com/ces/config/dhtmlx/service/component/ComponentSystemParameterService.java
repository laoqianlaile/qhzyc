package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentSystemParameterDao;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameter;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.StringUtil;

/**
 * 构件版本中系统参数Service
 * 
 * @author wanglei
 * @date 2013-08-20
 */
@Component("componentSystemParameterService")
public class ComponentSystemParameterService extends ConfigDefineDaoService<ComponentSystemParameter, ComponentSystemParameterDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentSystemParameterDao")
    @Override
    protected void setDaoUnBinding(ComponentSystemParameterDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据构件版本ID获取该构件版本下的系统参数
     * 
     * @param componentVersionId 构件版本ID
     */
    public List<ComponentSystemParameter> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取该构件版本下的系统参数
     * 
     * @param componentVersionIds 构件版本IDs
     */
    public List<ComponentSystemParameter> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentSystemParameter> componentSystemParameterList = new ArrayList<ComponentSystemParameter>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentSystemParameter t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentSystemParameterList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentSystemParameter.class);
        }
        return componentSystemParameterList;
    }

    /**
     * 根据构件版本ID删除该构件版本下的系统参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
        ComponentParamsUtil.removeComponentSystemParamMap(componentVersionId);
    }
}

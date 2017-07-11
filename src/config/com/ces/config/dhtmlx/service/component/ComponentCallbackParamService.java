package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentCallbackParamDao;
import com.ces.config.dhtmlx.dao.construct.ConstructCallbackDao;
import com.ces.config.dhtmlx.entity.component.ComponentCallbackParam;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 构件回调函数(供构件关闭时使用)参数Service
 * 
 * @author wanglei
 * @date 2013-09-27
 */
@Component("componentCallbackParamService")
public class ComponentCallbackParamService extends ConfigDefineDaoService<ComponentCallbackParam, ComponentCallbackParamDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentCallbackParamDao")
    @Override
    protected void setDaoUnBinding(ComponentCallbackParamDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] callbackParamIds = ids.split(",");
        for (int i = 0; i < callbackParamIds.length; i++) {
            getDaoFromContext(ConstructCallbackDao.class).deleteByInputParamId(callbackParamIds[i]);
            getDao().delete(callbackParamIds[i]);
        }
    }

    /**
     * 根据构件回调函数ID获取该回调函数的参数
     * 
     * @param name 参数名称
     * @param callbackId 回调函数ID
     * @return ComponentCallbackParam
     */
    public ComponentCallbackParam getByNameAndCallbackId(String name, String functionId) {
        return getDao().getByNameAndCallbackId(name, functionId);
    }

    /**
     * 根据构件回调函数ID获取该回调函数的参数
     * 
     * @param callbackId 回调函数ID
     * @return List<ComponentCallbackParam>
     */
    public List<ComponentCallbackParam> getByCallbackId(String callbackId) {
        return getDao().getByCallbackId(callbackId);
    }

    /**
     * 根据构件回调函数ID获取该回调函数的参数
     * 
     * @param callbackIds 回调函数IDs
     * @return List<ComponentCallbackParam>
     */
    public List<ComponentCallbackParam> getByCallbackIds(String callbackIds) {
        List<ComponentCallbackParam> componentCallbackParamList = new ArrayList<ComponentCallbackParam>();
        if (StringUtil.isNotEmpty(callbackIds)) {
            String hql = "from ComponentCallbackParam t where t.callbackId in ('" + callbackIds.replace(",", "','") + "')";
            componentCallbackParamList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentCallbackParam.class);
        }
        return componentCallbackParamList;
    }
}

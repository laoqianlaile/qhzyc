package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentCallbackDao;
import com.ces.config.dhtmlx.dao.component.ComponentCallbackParamDao;
import com.ces.config.dhtmlx.dao.construct.ConstructCallbackDao;
import com.ces.config.dhtmlx.entity.component.ComponentCallback;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;

/**
 * 构件回调函数(供构件关闭时使用)Service
 * 
 * @author wanglei
 * @date 2013-09-10
 */
@Component("componentCallbackService")
public class ComponentCallbackService extends ConfigDefineDaoService<ComponentCallback, ComponentCallbackDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentCallbackDao")
    @Override
    protected void setDaoUnBinding(ComponentCallbackDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ComponentCallback save(ComponentCallback entity) {
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
        String[] callbackIds = ids.split(",");
        for (int i = 0; i < callbackIds.length; i++) {
            getDaoFromContext(ConstructCallbackDao.class).deleteByCallbackId(callbackIds[i]);
            getDaoFromContext(ComponentCallbackParamDao.class).deleteByCallbackId(callbackIds[i]);
            getDao().delete(callbackIds[i]);
        }
    }

    /**
     * 根据回调函数名称和构件版本ID获取回调函数
     * 
     * @param name 回调函数名称
     * @param componentVersionId 构件版本ID
     * @return ComponentCallback
     */
    public ComponentCallback getByNameAndComponentVersionId(String name, String componentVersionId) {
        return getDao().getByNameAndComponentVersionId(name, componentVersionId);
    }

    /**
     * 根据构件版本ID获取该构件版本下的页面回调函数
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentCallback>
     */
    public List<ComponentCallback> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取该构件版本下的页面回调函数
     * 
     * @param componentVersionIds 构件版本IDs
     * @return List<ComponentCallback>
     */
    public List<ComponentCallback> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentCallback> componentCallbackList = new ArrayList<ComponentCallback>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentCallback t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentCallbackList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentCallback.class);
        }
        return componentCallbackList;
    }

    /**
     * 获取自定义构件公用的页面回调函数
     * 
     * @return List<ComponentCallback>
     */
    public List<ComponentCallback> getCommonCallbacks() {
        return getDao().getCommonCallbacks();
    }

    /**
     * 根据构件版本ID删除该构件版本下的页面回调函数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDaoFromContext(ConstructCallbackDao.class).deleteCallbackByComponentVersionId(componentVersionId);
        getDaoFromContext(ComponentCallbackParamDao.class).deleteByComponentVersionId(componentVersionId);
        getDao().deleteByComponentVersionId(componentVersionId);
    }
}

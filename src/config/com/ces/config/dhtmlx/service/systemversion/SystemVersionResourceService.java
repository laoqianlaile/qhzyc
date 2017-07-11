package com.ces.config.dhtmlx.service.systemversion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.systemvesion.SystemVersionResourceDao;
import com.ces.config.dhtmlx.entity.systemversion.SystemVersionResource;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 系统版本与资源关联关系Service
 * 
 * @author wanglei
 * @date 2015-04-18
 */
@Component("systemVersionResourceService")
public class SystemVersionResourceService extends ConfigDefineDaoService<SystemVersionResource, SystemVersionResourceDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("systemVersionResourceDao")
    @Override
    protected void setDaoUnBinding(SystemVersionResourceDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取系统版本与资源关联关系
     * 
     * @param systemVersionId 系统版本ID
     * @return List<SystemVersionResource>
     */
    public List<SystemVersionResource> getBySystemVersionId(String systemVersionId) {
        return getDao().getBySystemVersionId(systemVersionId);
    }

    /**
     * 获取系统版本与资源关联关系IDs
     * 
     * @param systemVersionId 系统版本ID
     * @return List<ResourceButton>
     */
    public List<String> getResourceIdsBySystemVersionId(String systemVersionId) {
        return getDao().getResourceIdsBySystemVersionId(systemVersionId);
    }

    /**
     * 根据系统版本ID删除（系统版本与资源关联关系）
     * 
     * @param systemVersionId 系统版本ID
     */
    @Transactional
    public void deleteBySystemVersionId(String systemVersionId) {
        getDao().deleteBySystemVersionId(systemVersionId);
    }

    /**
     * 根据资源ID删除（系统版本与资源关联关系）
     * 
     * @param resourceId 资源ID
     */
    @Transactional
    public void deleteByResourceId(String resourceId) {
        getDao().deleteByResourceId(resourceId);
    }
}

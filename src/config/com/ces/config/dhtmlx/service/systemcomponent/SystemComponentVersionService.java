package com.ces.config.dhtmlx.service.systemcomponent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.systemcomponent.SystemComponentVersionDao;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.systemcomponent.SystemComponentVersion;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;

/**
 * 系统中直接绑定的构件（非通过菜单方式）Service
 * 
 * @author wanglei
 * @date 2014-04-24
 */
@Component("systemComponentVersionService")
public class SystemComponentVersionService extends ConfigDefineDaoService<SystemComponentVersion, SystemComponentVersionDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("systemComponentVersionDao")
    @Override
    protected void setDaoUnBinding(SystemComponentVersionDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取系统与构件绑定关系
     * 
     * @param rootMenuId 根菜单ID
     * @return List<SystemComponentVersion>
     */
    public List<SystemComponentVersion> getByRootMenuId(String rootMenuId) {
        return getDao().getByRootMenuId(rootMenuId);
    }

    /**
     * 获取系统中直接绑定的构件
     * 
     * @param rootMenuId 根菜单ID
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getComponentVersions(String rootMenuId) {
        return getDao().getComponentVersions(rootMenuId);
    }

    /**
     * 保存系统与构件绑定关系
     * 
     * @param rootMenuId 根菜单ID
     * @param componentVersionIds 构件版本IDs
     */
    @Transactional
    public void saveSystemComponentVersion(String rootMenuId, String[] componentVersionIds) {
        if (componentVersionIds.length > 0) {
            ComponentVersion componentVersion = null;
            SystemComponentVersion systemComponentVersion = null;
            for (String componentVersionId : componentVersionIds) {
                componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
                if (componentVersion != null) {
                    systemComponentVersion = new SystemComponentVersion();
                    systemComponentVersion.setComponentVersion(componentVersion);
                    systemComponentVersion.setRootMenuId(rootMenuId);
                    getDao().save(systemComponentVersion);
                }
            }
        }
    }

    /**
     * 根据构件版本ID删除系统中直接绑定的构件
     * 
     * @param componentVersionId 构件版本ID
     */
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
    }
}

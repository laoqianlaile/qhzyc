package com.ces.config.dhtmlx.service.resource;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.resource.ResourceButtonDao;
import com.ces.config.dhtmlx.entity.resource.ResourceButton;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ResourceUtil;

/**
 * 按钮资源与实际按钮关系Service
 * 
 * @author wanglei
 * @date 2015-04-15
 */
@Component("resourceButtonService")
public class ResourceButtonService extends ConfigDefineDaoService<ResourceButton, ResourceButtonDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("resourceButtonDao")
    @Override
    protected void setDaoUnBinding(ResourceButtonDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取按钮资源与实际按钮关系
     * 
     * @param resourceId 资源ID
     * @return List<ResourceButton>
     */
    public List<ResourceButton> getByResourceId(String resourceId) {
        return getDao().getByResourceId(resourceId);
    }

    /**
     * 获取按钮资源与实际按钮关系IDs
     * 
     * @param resourceId 资源ID
     * @return List<ResourceButton>
     */
    public List<String> getButtonIdsByResourceId(String resourceId) {
        return getDao().getButtonIdsByResourceId(resourceId);
    }

    /**
     * 获取菜单资源下所有（按钮资源与实际按钮关系）IDs
     * 
     * @param resourceId 资源ID
     * @return List<ResourceButton>
     */
    public List<String> getButtonIdsByMenuResourceId(String menuResourceId) {
        return getDao().getButtonIdsByMenuResourceId(menuResourceId);
    }

    /**
     * 获取按钮资源与实际按钮关系
     * 
     * @param systemId 系统ID
     * @return List<ResourceButton>
     */
    public List<ResourceButton> getBySystemId(String systemId) {
        return getDao().getBySystemId(systemId);
    }

    /**
     * 根据按钮资源ID删除（按钮资源与实际按钮关系）
     * 
     * @param resourceId 按钮资源ID
     */
    @Transactional
    public void deleteByResourceId(String resourceId) {
        getDao().deleteByResourceId(resourceId);
        ResourceUtil.getInstance().removeResourceButtonCache(resourceId);
    }

    /**
     * 根据资源ID删除（按钮资源与实际按钮关系）
     * 
     * @param buttonId 按钮ID
     */
    @Transactional
    public void deleteByButtonId(String buttonId) {
        List<ResourceButton> resourceButtonList = getDao().getByButtonId(buttonId);
        if (CollectionUtils.isNotEmpty(resourceButtonList)) {
            getDao().deleteByButtonId(buttonId);
            for (ResourceButton resourceButton : resourceButtonList) {
                ResourceUtil.getInstance().removeResourceButton(resourceButton.getId(), ResourceUtil.CONSTRUCT_BUTTON, buttonId);
                ResourceUtil.getInstance().removeResourceButton(resourceButton.getId(), ResourceUtil.PAGE_COMPONENT_BUTTON, buttonId);
            }
        }
    }
}

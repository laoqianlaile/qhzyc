package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentButtonDao;
import com.ces.config.dhtmlx.entity.component.ComponentButton;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.resource.ResourceButtonService;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;

/**
 * 构件按钮Service
 * 
 * @author wanglei
 * @date 2013-07-29
 */
@Component("componentButtonService")
public class ComponentButtonService extends ConfigDefineDaoService<ComponentButton, ComponentButtonDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentButtonDao")
    @Override
    protected void setDaoUnBinding(ComponentButtonDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#delete(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    public void delete(ComponentButton entity) {
        getService(ResourceButtonService.class).deleteByButtonId(entity.getId());
        super.delete(entity);
    }

    /**
     * 根据名称和构件版本ID获取构件按钮
     * 
     * @param name 名称
     * @param componentVersionId 构件版本ID
     * @return ComponentButton
     */
    public ComponentButton getByNameAndComponentVersionId(String name, String componentVersionId) {
        return getDao().getByNameAndComponentVersionId(name, componentVersionId);
    }

    /**
     * 根据构件版本ID获取构件按钮
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentParameter>
     */
    public List<ComponentButton> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取构件按钮
     * 
     * @param componentVersionIds 构件版本IDs
     * @return List<ComponentParameter>
     */
    public List<ComponentButton> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentButton> componentButtonList = new ArrayList<ComponentButton>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentButton t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentButtonList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentButton.class);
        }
        return componentButtonList;
    }

    /**
     * 根据构件版本ID删除构件按钮
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        List<ComponentButton> componentButtonList = getByComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(componentButtonList)) {
            getDao().deleteByComponentVersionId(componentVersionId);
            AuthorityUtil.getInstance().clearMenuOrButtonAuthority(AuthorityUtil.AUTHORITY_COMPONENT_BUTTON);
            for (ComponentButton componentButton : componentButtonList) {
                getService(ResourceButtonService.class).deleteByButtonId(componentButton.getId());
            }
        }
    }
}

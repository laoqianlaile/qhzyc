package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentJarDao;
import com.ces.config.dhtmlx.entity.component.ComponentJar;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 构件中的jar文件Service
 * 
 * @author wanglei
 * @date 2013-08-09
 */
@Component("componentJarService")
public class ComponentJarService extends ConfigDefineDaoService<ComponentJar, ComponentJarDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentJarDao")
    @Override
    protected void setDaoUnBinding(ComponentJarDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据构件版本ID获取该构件版本下的jar包
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentJar>
     */
    public List<ComponentJar> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取该构件版本下的jar包
     * 
     * @param componentVersionIds 构件版本IDs
     * @return List<ComponentJar>
     */
    public List<ComponentJar> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentJar> componentJarList = new ArrayList<ComponentJar>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentJar t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentJarList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentJar.class);
        }
        return componentJarList;
    }
}

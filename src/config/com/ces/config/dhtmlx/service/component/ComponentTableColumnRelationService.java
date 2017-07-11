package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentTableColumnRelationDao;
import com.ces.config.dhtmlx.entity.component.ComponentTableColumnRelation;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 构件版本、构件表、关联字段三者关联关系Service
 * 
 * @author wanglei
 * @date 2013-08-16
 */
@Component("componentTableColumnRelationService")
public class ComponentTableColumnRelationService extends ConfigDefineDaoService<ComponentTableColumnRelation, ComponentTableColumnRelationDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentTableColumnRelationDao")
    @Override
    protected void setDaoUnBinding(ComponentTableColumnRelationDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据构件版本ID获取该构件版本下的（构件版本、构件表和关联字段）的关联关系
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentTableColumnRelation>
     */
    public List<ComponentTableColumnRelation> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取该构件版本下的（构件版本、构件表和关联字段）的关联关系
     * 
     * @param componentVersionIds 构件版本IDs
     * @return List<ComponentTableColumnRelation>
     */
    public List<ComponentTableColumnRelation> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentTableColumnRelation> componentTableColumnRelationList = new ArrayList<ComponentTableColumnRelation>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentTableColumnRelation t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentTableColumnRelationList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentTableColumnRelation.class);
        }
        return componentTableColumnRelationList;
    }

}

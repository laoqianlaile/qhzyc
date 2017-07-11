package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentTableDao;
import com.ces.config.dhtmlx.entity.component.ComponentTable;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 构件相关表Service
 * 
 * @author wanglei
 * @date 2013-08-16
 */
@Component("componentTableService")
public class ComponentTableService extends ConfigDefineDaoService<ComponentTable, ComponentTableDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentTableDao")
    @Override
    protected void setDaoUnBinding(ComponentTableDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据构件版本ID获取构件版本相关表
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentTable>
     */
    public List<ComponentTable> getComponentTableList(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据名称获取构件相关表
     * 
     * @param name 表名称
     * @return ComponentTable
     */
    public ComponentTable getComponentTableByName(String name) {
        return getDao().getByName(name);
    }

    /**
     * 获取构件相关表
     * 
     * @param ids 表IDs
     * @return List<ComponentTable>
     */
    public List<ComponentTable> getByIds(String ids) {
        List<ComponentTable> componentTableList = new ArrayList<ComponentTable>();
        if (StringUtil.isNotEmpty(ids)) {
            String hql = "from ComponentTable t where t.id in ('" + ids.replace(",", "','") + "')";
            componentTableList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentTable.class);
        }
        return componentTableList;
    }
}

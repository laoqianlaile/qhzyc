package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentColumnDao;
import com.ces.config.dhtmlx.entity.component.ComponentColumn;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 构件相关表的字段Service
 * 
 * @author wanglei
 * @date 2013-08-16
 */
@Component("componentColumnService")
public class ComponentColumnService extends ConfigDefineDaoService<ComponentColumn, ComponentColumnDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentColumnDao")
    @Override
    protected void setDaoUnBinding(ComponentColumnDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据构件版本ID及表ID获取构件版本相关表的字段
     * 
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     * @return List<ComponentColumn>
     */
    public List<ComponentColumn> getByComponentVersionIdAndTableId(String componentVersionId, String tableId) {
        return getDao().getByComponentVersionIdAndTableId(componentVersionId, tableId);
    }

    /**
     * 根据表ID获取构件版本相关表的字段
     * 
     * @param tableId 表ID
     * @return List<ComponentColumn>
     */
    public List<ComponentColumn> getByTableId(String tableId) {
        return getDao().getByTableId(tableId);
    }

    /**
     * 根据构件版本ID获取构件版本相关表的字段
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentColumn>
     */
    public List<ComponentColumn> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 获取构件相关表的字段
     * 
     * @param ids 字段IDs
     * @return List<ComponentColumn>
     */
    public List<ComponentColumn> getByIds(String ids) {
        List<ComponentColumn> componentColumnList = new ArrayList<ComponentColumn>();
        if (StringUtil.isNotEmpty(ids)) {
            String hql = "from ComponentColumn t where t.id in ('" + ids.replace(",", "','") + "')";
            componentColumnList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentColumn.class);
        }
        return componentColumnList;
    }
}

package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentClassDao;
import com.ces.config.dhtmlx.entity.component.ComponentClass;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 构件中的class文件Service
 * 
 * @author wanglei
 * @date 2013-08-09
 */
@Component("componentClassService")
public class ComponentClassService extends ConfigDefineDaoService<ComponentClass, ComponentClassDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentClassDao")
    @Override
    protected void setDaoUnBinding(ComponentClassDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据构件版本ID获取构件类
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentClass>
     */
    public List<ComponentClass> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取构件类
     * 
     * @param componentVersionIds 构件版本IDs
     * @return List<ComponentClass>
     */
    public List<ComponentClass> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentClass> componentClassList = new ArrayList<ComponentClass>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentClass t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentClassList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentClass.class);
        }
        return componentClassList;
    }
}

package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentFunctionDataDao;
import com.ces.config.dhtmlx.dao.construct.ConstructFunctionDao;
import com.ces.config.dhtmlx.entity.component.ComponentFunctionData;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 构件前台JS方法返回值Service
 * 
 * @author wanglei
 * @date 2013-08-08
 */
@Component("componentFunctionDataService")
public class ComponentFunctionDataService extends ConfigDefineDaoService<ComponentFunctionData, ComponentFunctionDataDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentFunctionDataDao")
    @Override
    protected void setDaoUnBinding(ComponentFunctionDataDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] functionDataIds = ids.split(",");
        for (int i = 0; i < functionDataIds.length; i++) {
            getDaoFromContext(ConstructFunctionDao.class).deleteByOutputParamId(functionDataIds[i]);
            getDao().delete(functionDataIds[i]);
        }
    }

    /**
     * 根据返回值名称和构件方法ID获取该方法的返回值
     * 
     * @param name 返回值名称
     * @param functionId 构件方法ID
     * @return ComponentFunctionData
     */
    public ComponentFunctionData getByNameAndFunctionId(String name, String functionId) {
        return getDao().getByNameAndFunctionId(name, functionId);
    }

    /**
     * 根据构件方法ID获取该方法的返回值
     * 
     * @param functionId 构件方法ID
     * @return List<ComponentFunctionData>
     */
    public List<ComponentFunctionData> getByFunctionId(String functionId) {
        return getDao().getByFunctionId(functionId);
    }

    /**
     * 根据构件方法ID获取该方法的返回值
     * 
     * @param functionIds 构件方法IDs
     * @return List<ComponentFunctionData>
     */
    public List<ComponentFunctionData> getByFunctionIds(String functionIds) {
        List<ComponentFunctionData> componentFunctionDataList = new ArrayList<ComponentFunctionData>();
        if (StringUtil.isNotEmpty(functionIds)) {
            String hql = "from ComponentFunctionData t where t.functionId in ('" + functionIds.replace(",", "','") + "')";
            componentFunctionDataList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentFunctionData.class);
        }
        return componentFunctionDataList;
    }
}

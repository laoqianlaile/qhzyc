package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentSystemParameterRelationDao;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameterRelation;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.StringUtil;
import com.google.common.collect.Lists;

/**
 * 构件版本中系统参数和系统中系统参数的关联关系Service
 * 
 * @author wanglei
 * @date 2013-08-20
 */
@Component("componentSystemParameterRelationService")
public class ComponentSystemParameterRelationService extends ConfigDefineDaoService<ComponentSystemParameterRelation, ComponentSystemParameterRelationDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentSystemParameterRelationDao")
    @Override
    protected void setDaoUnBinding(ComponentSystemParameterRelationDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取构件系统参数列表数据
     * 
     * @param componentVersionId 模块构件版本ID
     * @return List<Object[]>
     */
    public List<Object[]> getComponentSystemParamList(String componentVersionId) {
        return getDao().getComponentSystemParamList(componentVersionId);
    }

    /**
     * 获取系统参数列表数据
     * 
     * @param componentVersionId 模块构件版本ID
     * @return List<Object[]>
     */
    public List<Object[]> getSystemParamList(String componentVersionId) {
        List<Object[]> params = new ArrayList<Object[]>();
        List<Object[]> systemParams = getDao().getSystemParamList();
        List<Object[]> componentSystemParams = getDao().getAllComponentSystemParamList(componentVersionId);
        if (CollectionUtils.isEmpty(componentSystemParams)) {
            params = systemParams;
        } else {
            for (Object[] componentSystemParam : componentSystemParams) {
                for (Object[] systemParam : systemParams) {
                    if (componentSystemParam[1].equals(systemParam[1])) {
                        params.add(systemParam);
                        systemParams.remove(systemParam);
                        break;
                    }
                }
            }
            params.addAll(systemParams);
        }
        return params;
    }

    /**
     * 获取方法构件系统参数和系统参数绑定关系列表数据
     * 
     * @param componentVersionId 模块构件版本ID
     * @return List<Object[]>
     */
    public Object getComponentSystemParamRelationList(String componentVersionId) {
        List<Object[]> constructSystemParamList = getDao().getComponentSystemParamRelationList(componentVersionId);
        List<String[]> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(constructSystemParamList)) {
            for (Object[] objects : constructSystemParamList) {
                String[] strs = new String[5];
                strs[0] = String.valueOf(objects[0] + "-" + objects[1]);
                strs[1] = String.valueOf(objects[2]);
                strs[2] = String.valueOf(objects[3]);
                strs[3] = String.valueOf(objects[4]);
                strs[4] = String.valueOf(objects[5]);
                list.add(strs);
            }
        }
        return list;
    }

    /**
     * 获取构件系统参数名称和值
     * 
     * @param componentVersionId 模块构件版本ID
     * @return List<Object[]>
     */
    public List<Object[]> getComponentSystemParams(String componentVersionId) {
        return getDao().getComponentSystemParams(componentVersionId);
    }

    /**
     * 根据构件版本ID获取该构件系统参数关联配置
     * 
     * @param componentVersionId 构件版本ID
     */
    public List<ComponentSystemParameterRelation> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID获取该构件系统参数关联配置
     * 
     * @param componentVersionIds 构件版本IDs
     */
    public List<ComponentSystemParameterRelation> getByComponentVersionIds(String componentVersionIds) {
        List<ComponentSystemParameterRelation> componentSystemParameterRelationList = new ArrayList<ComponentSystemParameterRelation>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            String hql = "from ComponentSystemParameterRelation t where t.componentVersionId in ('" + componentVersionIds.replace(",", "','") + "')";
            componentSystemParameterRelationList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentSystemParameterRelation.class);
        }
        return componentSystemParameterRelationList;
    }

    /**
     * 保存构件系统参数和系统参数绑定关系列表数据
     * 
     * @param rowIds 构件系统参数和系统参数绑定关系列表IDs
     * @param constructDetailId 预留区和构件关系ID
     */
    @Transactional
    public void saveComponentSystemParameterRelation(String rowIds, String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
        String[] rowIdArray = rowIds.split(",");
        String systemParamId = null;
        String componentSystemParamId = null;
        List<ComponentSystemParameterRelation> list = new ArrayList<ComponentSystemParameterRelation>();
        ComponentSystemParameterRelation componentSystemParameterRelation = null;
        for (String rowId : rowIdArray) {
            String[] paramIds = rowId.split("-");
            componentSystemParamId = paramIds[0];
            systemParamId = paramIds[1];
            componentSystemParameterRelation = new ComponentSystemParameterRelation();
            componentSystemParameterRelation.setComponentVersionId(componentVersionId);
            componentSystemParameterRelation.setComponentSystemParamId(componentSystemParamId);
            componentSystemParameterRelation.setSystemParamId(systemParamId);
            list.add(componentSystemParameterRelation);
        }
        getDao().save(list);
        ComponentParamsUtil.putComponentSystemParamRelationList(componentVersionId, list);
    }

    /**
     * 删除构件系统参数关联配置
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
        ComponentParamsUtil.removeComponentSystemParamRelationList(componentVersionId);
    }
}

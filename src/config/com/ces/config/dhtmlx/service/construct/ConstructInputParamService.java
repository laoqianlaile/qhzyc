package com.ces.config.dhtmlx.service.construct;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.construct.ConstructInputParamDao;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructInputParam;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.StringUtil;

/**
 * 组合构件中基础构件的入参Service
 * 
 * @author wanglei
 * @date 2013-09-03
 */
@Component("constructInputParamService")
public class ConstructInputParamService extends ConfigDefineDaoService<ConstructInputParam, ConstructInputParamDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("constructInputParamDao")
    @Override
    protected void setDaoUnBinding(ConstructInputParamDao dao) {
        super.setDaoUnBinding(dao);
    }

    @Override
    @Transactional
    public ConstructInputParam save(ConstructInputParam entity) {
        ConstructInputParam constructInputParam = super.save(entity);
        ComponentParamsUtil.putConstructInputParamList(constructInputParam.getConstructId(), constructInputParam);
        return constructInputParam;
    }

    /**
     * 获取构件入参列表数据
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<Object[]>
     */
    public List<Object[]> getInputParamList(String constructId) {
        return getDao().getInputParamList(constructId);
    }

    /**
     * 获取组合构件中基础构件的自身配置参数
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructInputParam>
     */
    public List<ConstructInputParam> getByConstructId(String constructId) {
        return getDao().getByConstructId(constructId);
    }

    /**
     * 获取组合构件中基础构件的自身配置参数
     * 
     * @param constructIds 组合构件绑定关系IDs
     * @return List<ConstructInputParam>
     */
    public List<ConstructInputParam> getByConstructIds(String constructIds) {
        List<ConstructInputParam> constructInputParamList = new ArrayList<ConstructInputParam>();
        if (StringUtil.isNotEmpty(constructIds)) {
            String hql = "from ConstructInputParam t where t.constructId in ('" + constructIds.replace(",", "','") + "')";
            constructInputParamList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ConstructInputParam.class);
        }
        return constructInputParamList;
    }

    /**
     * 根据组合构件绑定关系ID获取构件入参
     * 
     * @param inputParamId 构件入参ID
     * @return List<ConstructInputParam>
     */
    public List<ConstructInputParam> getByInputParamId(String inputParamId) {
        return getDao().getByInputParamId(inputParamId);
    }

    /**
     * 删除构件入参
     * 
     * @param constructId 组合构件绑定关系ID
     */
    @Transactional
    public void deleteByConstructId(String constructId) {
        getDao().deleteByConstructId(constructId);
        ComponentParamsUtil.removeConstructInputParamList(constructId);
    }

    /**
     * 删除构件入参
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
        List<Construct> constructList = getService(ConstructService.class).getByBaseComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(constructList)) {
            for (Construct construct : constructList) {
                ComponentParamsUtil.removeConstructInputParamList(construct.getId());
            }
        }
    }
}

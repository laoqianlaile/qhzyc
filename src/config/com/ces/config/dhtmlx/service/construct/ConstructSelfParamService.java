package com.ces.config.dhtmlx.service.construct;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.construct.ConstructSelfParamDao;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructSelfParam;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.StringUtil;

/**
 * 组合构件中基础构件的自身配置参数Service
 * 
 * @author wanglei
 * @date 2013-08-26
 */
@Component("constructSelfParamService")
public class ConstructSelfParamService extends ConfigDefineDaoService<ConstructSelfParam, ConstructSelfParamDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("constructSelfParamDao")
    @Override
    protected void setDaoUnBinding(ConstructSelfParamDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ConstructSelfParam save(ConstructSelfParam entity) {
        ConstructSelfParam constructSelfParam = super.save(entity);
        ComponentParamsUtil.putConstructSelfParamList(constructSelfParam.getConstructId(), constructSelfParam);
        return constructSelfParam;
    }

    /**
     * 获取组合构件中基础构件的自身配置参数
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructSelfParam>
     */
    public List<ConstructSelfParam> getByConstructId(String constructId) {
        return getDao().getByConstructId(constructId);
    }

    /**
     * 获取组合构件中基础构件的自身配置参数
     * 
     * @param constructIds 组合构件绑定关系IDs
     * @return List<ConstructSelfParam>
     */
    public List<ConstructSelfParam> getByConstructIds(String constructIds) {
        List<ConstructSelfParam> constructSelfParamList = new ArrayList<ConstructSelfParam>();
        if (StringUtil.isNotEmpty(constructIds)) {
            String hql = "from ConstructSelfParam t where t.constructId in ('" + constructIds.replace(",", "','") + "')";
            constructSelfParamList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ConstructSelfParam.class);
        }
        return constructSelfParamList;
    }

    /**
     * 获取组合构件中基础构件的自身配置参数
     * 
     * @param selfParamId 构件自身参数ID
     * @return List<ConstructSelfParam>
     */
    public List<ConstructSelfParam> getBySelfParamId(String selfParamId) {
        return getDao().getBySelfParamId(selfParamId);
    }

    /**
     * 删除组合构件中基础构件的自身配置参数
     * 
     * @param constructId 组合构件绑定关系ID
     */
    @Transactional
    public void deleteByConstructId(String constructId) {
        getDao().deleteByConstructId(constructId);
        ComponentParamsUtil.removeConstructSelfParamList(constructId);
    }

    /**
     * 删除组合构件中基础构件的自身配置参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
        List<Construct> constructList = getService(ConstructService.class).getByBaseComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(constructList)) {
            for (Construct construct : constructList) {
                ComponentParamsUtil.removeConstructSelfParamList(construct.getId());
            }
        }
    }
}

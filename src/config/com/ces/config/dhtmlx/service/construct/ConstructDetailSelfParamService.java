package com.ces.config.dhtmlx.service.construct;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDetailSelfParamDao;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.construct.ConstructDetailSelfParam;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.StringUtil;

/**
 * 构件绑定预留区后的自身配置参数Service
 * 
 * @author wanglei
 * @date 2013-08-26
 */
@Component("constructDetailSelfParamService")
public class ConstructDetailSelfParamService extends ConfigDefineDaoService<ConstructDetailSelfParam, ConstructDetailSelfParamDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("constructDetailSelfParamDao")
    @Override
    protected void setDaoUnBinding(ConstructDetailSelfParamDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ConstructDetailSelfParam save(ConstructDetailSelfParam entity) {
        ConstructDetailSelfParam constructDetailSelfParam = super.save(entity);
        ComponentParamsUtil.putConstructDetailSelfParamList(constructDetailSelfParam.getConstructDetailId(), constructDetailSelfParam);
        return constructDetailSelfParam;
    }

    /**
     * 获取构件绑定预留区后的自身配置参数
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<ConstructDetailSelfParam>
     */
    public List<ConstructDetailSelfParam> getByConstructDetailId(String constructDetailId) {
        return getDao().getByConstructDetailId(constructDetailId);
    }

    /**
     * 获取构件绑定预留区后的自身配置参数
     * 
     * @param constructDetailIds 组合构件中构件和预留区绑定关系IDs
     * @return List<ConstructDetailSelfParam>
     */
    public List<ConstructDetailSelfParam> getByConstructDetailIds(String constructDetailIds) {
        List<ConstructDetailSelfParam> constructDetailSelfParamList = new ArrayList<ConstructDetailSelfParam>();
        if (StringUtil.isNotEmpty(constructDetailIds)) {
            String hql = "from ConstructDetailSelfParam t where t.constructDetailId in ('" + constructDetailIds.replace(",", "','") + "')";
            constructDetailSelfParamList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ConstructDetailSelfParam.class);
        }
        return constructDetailSelfParamList;
    }

    /**
     * 获取构件绑定预留区后的自身配置参数
     * 
     * @param selfParamId 构件自身参数ID
     * @return List<ConstructDetailSelfParam>
     */
    public List<ConstructDetailSelfParam> getBySelfParamId(String selfParamId) {
        return getDao().getBySelfParamId(selfParamId);
    }

    /**
     * 删除构件绑定预留区后的自身配置参数
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     */
    @Transactional
    public void deleteByConstructDetailId(String constructDetailId) {
        getDao().deleteByConstructDetailId(constructDetailId);
        ComponentParamsUtil.removeConstructDetailSelfParamList(constructDetailId);
    }

    /**
     * 删除构件绑定预留区后的自身配置参数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
        List<ConstructDetail> constuctDetailList = getService(ConstructDetailService.class).getByComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(constuctDetailList)) {
            for (ConstructDetail constructDetail : constuctDetailList) {
                ComponentParamsUtil.removeConstructDetailSelfParamList(constructDetail.getId());
            }
        }
    }
}

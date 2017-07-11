package com.ces.config.dhtmlx.service.construct;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.PhysicalTableDefineDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.construct.ConstructFilterDetailDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.construct.ConstructFilter;
import com.ces.config.dhtmlx.entity.construct.ConstructFilterDetail;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ConstructFilterUtil;
import com.ces.config.utils.StringUtil;

/**
 * 数据过滤条件详情Service
 * 
 * @author wanglei
 * @date 2015-05-20
 */
@Component
public class ConstructFilterDetailService extends ConfigDefineDaoService<ConstructFilterDetail, ConstructFilterDetailDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("constructFilterDetailDao")
    @Override
    protected void setDaoUnBinding(ConstructFilterDetailDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.service.base.StringIDConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] idArray = ids.split(",");
        ConstructFilterDetail constructFilterDetail = getByID(idArray[0]);
        ConstructFilter constructFilter = getService(ConstructFilterService.class).getByID(constructFilterDetail.getConstructFilterId());
        // 清除缓存
        ConstructFilterUtil.getInstance().removeConstructFilter(
                constructFilter.getTopComVersionId() + "_" + constructFilter.getComponentVersionId() + "_" + constructFilter.getTableId());
        // 如果是逻辑表，同时删除对应的物理表的缓存
        LogicTableDefine logicTableDefine = getService(LogicTableDefineService.class).getByCode(constructFilter.getTableId());
        if (logicTableDefine != null) {
            List<PhysicalTableDefine> pTableDefineList = getDaoFromContext(PhysicalTableDefineDao.class).getByLogicTableCode(logicTableDefine.getCode());
            if (CollectionUtils.isNotEmpty(pTableDefineList)) {
                for (PhysicalTableDefine pTableDefine : pTableDefineList) {
                    ConstructFilterUtil.getInstance().removeConstructFilter(
                            constructFilter.getTopComVersionId() + "_" + constructFilter.getComponentVersionId() + "_" + pTableDefine.getId());
                }
            }
        }
        super.delete(ids);
    }

    /**
     * 根据数据过滤条件ID获取数据过滤条件详情
     * 
     * @param constructFilterId 数据过滤条件ID
     * @return List<ConstructFilterDetail>
     */
    public List<ConstructFilterDetail> getByConstructFilterId(String constructFilterId) {
        return getDao().getByConstructFilterId(constructFilterId);
    }

    /**
     * 根据上层的组合构件的版本ID获取数据过滤条件详情
     * 
     * @param topComVersionIds 上层的组合构件的版本IDs
     * @return List<ConstructFilterDetail>
     */
    public List<ConstructFilterDetail> getByTopComVersionIds(String topComVersionIds) {
        List<ConstructFilterDetail> constructFilterDetailList = new ArrayList<ConstructFilterDetail>();
        if (StringUtil.isNotEmpty(topComVersionIds)) {
            String hql = "select fd from ConstructFilter t, ConstructFilterDetail fd where t.id=fd.constructFilterId and t.topComVersionId in ('"
                    + topComVersionIds.replace(",", "','") + "')";
            constructFilterDetailList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ConstructFilterDetail.class);
        }
        return constructFilterDetailList;
    }

    /**
     * 保存数据过滤条件详情
     * 
     * @param constructFilter 数据过滤条件
     * @param rowsValue 数据过滤条件详情
     */
    @Transactional
    public void saveConstructFilterDetails(ConstructFilter constructFilter, String rowsValue) {
        getDao().deleteByConstructFilterId(constructFilter.getId());
        String[] details = rowsValue.split(";");
        List<ConstructFilterDetail> list = new ArrayList<ConstructFilterDetail>();
        ConstructFilterDetail constructFilterDetail = null;
        int i = 0;
        for (String detail : details) {
            if (StringUtil.isNotEmpty(detail)) {
                // 此处是防止detail字符串已,,,结尾而导致strs数组长度变短，加上", "使得数组长度+1
                detail = detail + ", ";
                String[] strs = detail.split(",");
                constructFilterDetail = new ConstructFilterDetail();
                constructFilterDetail.setConstructFilterId(constructFilter.getId());
                constructFilterDetail.setRelation(strs[0]);
                constructFilterDetail.setLeftParenthesis(strs[1]);
                constructFilterDetail.setColumnId(strs[2]);
                constructFilterDetail.setOperator(strs[3]);
                constructFilterDetail.setValue(strs[4]);
                constructFilterDetail.setRightParenthesis(strs[5]);
                constructFilterDetail.setShowOrder(i++);
                list.add(constructFilterDetail);
            }
        }
        getDao().save(list);
    }
}

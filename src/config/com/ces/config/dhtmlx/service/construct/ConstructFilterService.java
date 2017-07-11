package com.ces.config.dhtmlx.service.construct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.PhysicalTableDefineDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.construct.ConstructFilterDao;
import com.ces.config.dhtmlx.dao.construct.ConstructFilterDetailDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.construct.ConstructFilter;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.ConstructFilterUtil;
import com.ces.config.utils.EhcacheUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;

/**
 * 数据过滤条件Service
 * 
 * @author wanglei
 * @date 2015-05-20
 */
@Component
public class ConstructFilterService extends ConfigDefineDaoService<ConstructFilter, ConstructFilterDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("constructFilterDao")
    @Override
    protected void setDaoUnBinding(ConstructFilterDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取数据过滤条件
     * 
     * @param topComVersionId 上层的组合构件的版本ID
     * @param componentVersionId 控制权限的构件版本ID
     * @param tableId 表ID
     * @return ConstructFilter
     */
    public ConstructFilter getConstructFilter(String topComVersionId, String componentVersionId, String tableId) {
        return getDao().getConstructFilter(topComVersionId, componentVersionId, tableId);
    }

    /**
     * 根据上层的组合构件的版本ID获取数据过滤条件
     * 
     * @param topComVersionIds 上层的组合构件的版本IDs
     * @return List<ConstructFilter>
     */
    public List<ConstructFilter> getByTopComVersionIds(String topComVersionIds) {
        List<ConstructFilter> constructFilterList = new ArrayList<ConstructFilter>();
        if (StringUtil.isNotEmpty(topComVersionIds)) {
            String hql = "from ConstructFilter t where t.topComVersionId in ('" + topComVersionIds.replace(",", "','") + "')";
            constructFilterList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ConstructFilter.class);
        }
        return constructFilterList;
    }

    /**
     * 保存数据过滤条件
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param tableId 表ID
     */
    @Transactional
    public void saveConstructFilter(ConstructFilter constructFilter, String rowsValue) {
        if (StringUtil.isEmpty(constructFilter.getId())) {
            constructFilter = getDao().save(constructFilter);
        }
        getService(ConstructFilterDetailService.class).saveConstructFilterDetails(constructFilter, rowsValue);
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
    }

    /**
     * 删除数据过滤条件
     * 
     * @param topComVersionId 上层的组合构件的版本ID
     */
    @Transactional
    public void deleteByTopComVersionId(String topComVersionId) {
        getDaoFromContext(ConstructFilterDetailDao.class).deleteByTopComVersionId(topComVersionId);
        getDao().deleteByTopComVersionId(topComVersionId);
        // 移除缓存
        ConstructFilterUtil.getInstance().removeConstructFilter(topComVersionId);
    }

    /**
     * 删除数据过滤条件
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDaoFromContext(ConstructFilterDetailDao.class).deleteByComponentVersionId(componentVersionId);
        getDao().deleteByComponentVersionId(componentVersionId);
        // 移除缓存
        ConstructFilterUtil.getInstance().removeConstructFilter(componentVersionId);
    }

    /**
     * 删除数据过滤条件
     * 
     * @param tableId 表ID
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        getDaoFromContext(ConstructFilterDetailDao.class).deleteByTableId(tableId);
        getDao().deleteByTableId(tableId);
        // 移除缓存
        ConstructFilterUtil.getInstance().removeConstructFilter(tableId);
        // 如果是逻辑表，同时删除对应的物理表的缓存
        LogicTableDefine logicTableDefine = getService(LogicTableDefineService.class).getByCode(tableId);
        if (logicTableDefine != null) {
            List<PhysicalTableDefine> pTableDefineList = getDaoFromContext(PhysicalTableDefineDao.class).getByLogicTableCode(logicTableDefine.getCode());
            if (CollectionUtils.isNotEmpty(pTableDefineList)) {
                for (PhysicalTableDefine pTableDefine : pTableDefineList) {
                    ConstructFilterUtil.getInstance().removeConstructFilter(pTableDefine.getId());
                }
            }
        }
    }

    /**
     * 获取数据过滤条件
     */
    @SuppressWarnings("unchecked")
    public String buildGridFilter(String topComVersionId, String componentVersionId, String tableId) {
        if (StringUtil.isNotEmpty(topComVersionId) && StringUtil.isNotEmpty(componentVersionId) && StringUtil.isNotEmpty(tableId)) {
            StringBuffer filter = new StringBuffer();
            StringBuffer sb = new StringBuffer(500);
            sb.append("select c.column_name, t.value, t.relation, t.operator, t.left_parenthesis, t.right_parenthesis")
                    .append(" from t_xtpz_construct_filter_detail t").append(" join t_xtpz_column_define c on(t.column_id=c.id and c.table_id='")
                    .append(tableId).append("')").append(" join t_xtpz_construct_filter d on (t.construct_filter_id = d.id)").append(" where d.table_id='")
                    .append(tableId).append("' and d.top_com_version_id='").append(topComVersionId).append("'").append(" and d.component_version_id='")
                    .append(componentVersionId).append("' order by t.show_order");
            List<String[]> list = DatabaseHandlerDao.getInstance().queryForList(sb.toString());
            if (CollectionUtils.isEmpty(list)) {
                PhysicalTableDefine physicalTableDefine = getService(PhysicalTableDefineService.class).getByID(tableId);
                if (physicalTableDefine != null && !ConstantVar.TableClassification.VIEW.equals(physicalTableDefine.getClassification())
                        && StringUtil.isNotEmpty(physicalTableDefine.getLogicTableCode())) {
                    sb = new StringBuffer(500);
                    sb.append("select c.column_name, t.value, t.relation, t.operator, t.left_parenthesis, t.right_parenthesis")
                            .append(" from t_xtpz_construct_filter_detail t").append(" join t_xtpz_column_define c on(t.column_id=c.id and c.table_id='")
                            .append(physicalTableDefine.getLogicTableCode()).append("')")
                            .append(" join t_xtpz_construct_filter d on (t.construct_filter_id = d.id)").append(" where d.table_id='")
                            .append(physicalTableDefine.getLogicTableCode()).append("' and d.top_com_version_id='").append(topComVersionId).append("'")
                            .append(" and d.component_version_id='").append(componentVersionId).append("' order by t.show_order");
                    list = DatabaseHandlerDao.getInstance().queryForList(sb.toString());
                }
            }
            if (CollectionUtils.isNotEmpty(list)) {
                filter.append(AppDefineUtil.RELATION_AND).append("(");
                for (int i = 0; i < list.size(); i++) {
                    // c.column_name, t.value, t.relation, t.operator, t.left_parenthesis, t.left_parenthesis
                    Object[] obj = list.get(i);
                    if (i != 0) {
                        if (AppDefineUtil.RELATION_OR.trim().equals(StringUtil.null2empty(obj[2]).toUpperCase())) {
                            filter.append(AppDefineUtil.RELATION_OR);
                        } else {
                            filter.append(AppDefineUtil.RELATION_AND);
                        }
                    }
                    String value = authorityFilterConversion(StringUtil.null2empty(obj[1]));
                    filter.append(StringUtil.null2empty(obj[4]))
                            .append(AppDefineUtil.processColumnFilter(null, String.valueOf(obj[0]), String.valueOf(obj[3]), StringUtil.null2empty(value)))
                            .append(StringUtil.null2empty(obj[5]));
                }
                filter.append(")");
            }
            return String.valueOf(filter);
        } else {
            return "";
        }
    }

    /**
     * 数据过滤条件条件值转换
     * 
     * @param filter
     * @return String
     */
    @SuppressWarnings("unchecked")
    private String authorityFilterConversion(String filter) {
        Map<String, Object> map = (Map<String, Object>) EhcacheUtil.getCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId());
        if (map == null) {
            map = AuthorityUtil.getInstance().getAuthorityManager().getAuthorityDeptFilterModelData();
            EhcacheUtil.setCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId(), map);
        }
        return AuthorityUtil.getInstance().getAuthorityManager().authorityFilterConversion(map, filter);
    }

}

package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.option.OptionModel;
import com.ces.config.dhtmlx.dao.appmanage.ColumnDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.LogicTableDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalTableDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxComboOption;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;
import com.ces.utils.BeanUtils;
import com.google.common.collect.Lists;

/**
 * 逻辑表定义表处理层
 * 
 * @author qiujinwei
 */
@Component
public class LogicTableDefineService extends ConfigDefineDaoService<LogicTableDefine, LogicTableDefineDao> {

    /*
     * (非 Javadoc)
     * <p>标题: bindingDao</p>
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("logicTableDefineDao")
    @Override
    protected void setDaoUnBinding(LogicTableDefineDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public LogicTableDefine save(LogicTableDefine entity) {
        ComponentReserveZone reserveZone1 = null;
        ComponentReserveZone reserveZone2 = null;
        ComponentReserveZone reserveZone4 = null;
        ComponentReserveZone reserveZone3 = null;
        if (StringUtil.isNotEmpty(entity.getId())) {
            LogicTableDefine oldLogicTableDefine = getByID(entity.getId());
            reserveZone1 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(oldLogicTableDefine.getCode() + "_GRID");
            reserveZone2 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(oldLogicTableDefine.getCode() + "_FORM_0");
            reserveZone4 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(oldLogicTableDefine.getCode() + "_FORM_1");
            reserveZone3 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(oldLogicTableDefine.getCode() + "_GRID_LINK");
            if (!oldLogicTableDefine.getCode().equals(entity.getCode())) {
                // 同步逻辑表列
                getService(ColumnDefineService.class).batchUpdateTableCode(oldLogicTableDefine.getCode(), entity.getCode());
                // 同步逻辑表组关系
                getService(LogicGroupRelationService.class).batchUpdateTableCode(oldLogicTableDefine.getCode(), entity.getCode());
                getService(LogicGroupRelationService.class).batchUpdateParentTableCode(oldLogicTableDefine.getCode(), entity.getCode());
                // 同步逻辑表组关系列
                getService(LogicTableRelationService.class).batchUpdateTableCode(oldLogicTableDefine.getCode(), entity.getCode());
                getService(LogicTableRelationService.class).batchUpdateParentTableCode(oldLogicTableDefine.getCode(), entity.getCode());
                // 同步物理表
                getService(PhysicalTableDefineService.class).batchUpdateLogicTableCode(oldLogicTableDefine.getCode(), entity.getCode());
            }
        } else {
            Integer maxShowOrder = getDao().getMaxShowOrder();
            if (null == maxShowOrder) {
                maxShowOrder = 1;
            }
            entity.setShowOrder(maxShowOrder);
            entity.setStatus("0");
        }
        Integer reserveZoneMaxShowOrder = entity.getShowOrder() * 4;
        if (reserveZone1 == null) {
            reserveZone1 = new ComponentReserveZone();
            reserveZone1.setName(entity.getCode() + "_GRID");
            reserveZone1.setAlias(entity.getShowName() + "_列表_工具条");
            reserveZone1.setType("0");
            reserveZone1.setPage(AppDefineUtil.getPage(0));
            reserveZone1.setIsCommon(true);
        } else {
            reserveZone1.setName(entity.getCode() + "_GRID");
            reserveZone1.setAlias(entity.getShowName() + "_列表_工具条");
        }
        reserveZone1.setShowOrder(reserveZoneMaxShowOrder - 3);
        if (reserveZone2 == null) {
            reserveZone2 = new ComponentReserveZone();
            reserveZone2.setName(entity.getCode() + "_FORM_0");
            reserveZone2.setAlias(entity.getShowName() + "_表单_上工具条");
            reserveZone2.setType("0");
            reserveZone2.setPage(AppDefineUtil.getPage(0));
            reserveZone2.setIsCommon(true);
        } else {
            reserveZone2.setName(entity.getCode() + "_FORM_0");
            reserveZone2.setAlias(entity.getShowName() + "_表单_上工具条");
        }
        reserveZone2.setShowOrder(reserveZoneMaxShowOrder - 2);
        if (reserveZone4 == null) {
            reserveZone4 = new ComponentReserveZone();
            reserveZone4.setName(entity.getCode() + "_FORM_1");
            reserveZone4.setAlias(entity.getShowName() + "_表单_下工具条");
            reserveZone4.setType("0");
            reserveZone4.setPage(AppDefineUtil.getPage(0));
            reserveZone4.setIsCommon(true);
        } else {
            reserveZone4.setName(entity.getCode() + "_FORM_1");
            reserveZone4.setAlias(entity.getShowName() + "_表单_下工具条");
        }
        reserveZone4.setShowOrder(reserveZoneMaxShowOrder - 1);
        if (reserveZone3 == null) {
            reserveZone3 = new ComponentReserveZone();
            reserveZone3.setName(entity.getCode() + "_GRID_LINK");
            reserveZone3.setAlias(entity.getShowName() + "_列表_超链接");
            reserveZone3.setType("1");
            reserveZone3.setPage(AppDefineUtil.getPage(0));
            reserveZone3.setIsCommon(true);
        } else {
            reserveZone3.setName(entity.getCode() + "_GRID_LINK");
            reserveZone3.setAlias(entity.getShowName() + "_列表_超链接");
        }
        reserveZone3.setShowOrder(reserveZoneMaxShowOrder);
        getService(ComponentReserveZoneService.class).save(reserveZone1);
        getService(ComponentReserveZoneService.class).save(reserveZone2);
        getService(ComponentReserveZoneService.class).save(reserveZone4);
        getService(ComponentReserveZoneService.class).save(reserveZone3);
        return super.save(entity);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.service.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] idArr = ids.split(",");
        for (int i = 0; i < idArr.length; i++) {
            LogicTableDefine logicTableDefine = getByID(idArr[i]);
            List<ColumnDefine> list = getService(ColumnDefineService.class).findByTableId(logicTableDefine.getCode());
            getService(ColumnDefineService.class).delete(list);
            getService(ComponentReserveZoneService.class).deleteCommonReserveZoneByName(logicTableDefine.getCode() + "_GRID");
            getService(ComponentReserveZoneService.class).deleteCommonReserveZoneByName(logicTableDefine.getCode() + "_FORM");
            getService(ComponentReserveZoneService.class).deleteCommonReserveZoneByName(logicTableDefine.getCode() + "_GRID_LINK");
            getDaoFromContext(PhysicalTableDefineDao.class).batchUpdateLogicTableCode(logicTableDefine.getCode(), null);
            getDao().delete(idArr[i]);
        }
    }

    /**
     * 根据编码获取逻辑表
     * 
     * @param code 编码
     * @return LogicTableDefine
     */
    public LogicTableDefine getByCode(String code) {
        return getDao().getByCode(code);
    }

    /**
     * qiujinwei 2014-11-14
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整显示顺序</p>
     * 
     * @param beginIds
     * @param endId 设定参数
     * @return void 返回类型
     * @throws
     */
    @Transactional
    public void adjustShowOrder(String sourceIds, String targetId) {
        String[] idArr = sourceIds.split(",");
        int len = idArr.length;

        int sBeginShowOrder = getDao().getShowOrderById(idArr[0]);
        int sEndShowOrder = (len > 1) ? getDao().getShowOrderById(idArr[len - 1]) : sBeginShowOrder;
        if (sBeginShowOrder > sEndShowOrder) {
            sBeginShowOrder = sBeginShowOrder ^ sEndShowOrder;
            sEndShowOrder = sBeginShowOrder ^ sEndShowOrder;
            sBeginShowOrder = sBeginShowOrder ^ sEndShowOrder;
        }
        int tShowOrder = getDao().getShowOrderById(targetId);

        int increaseNum = 0, differLen = 0, begin = 0, end = 0;
        boolean isUp = false;
        if (sBeginShowOrder > tShowOrder) {
            isUp = true;
        }
        if (isUp) {
            increaseNum = len;
            differLen = tShowOrder - sBeginShowOrder;
            begin = tShowOrder - 1;
            end = sBeginShowOrder;
        } else {
            increaseNum = -len;
            differLen = tShowOrder - sEndShowOrder;
            begin = sEndShowOrder;
            end = tShowOrder + 1;
        }
        // update between sourceIds and targetId
        getDao().batchUpdateShowOrder(begin, end, increaseNum);
        // update sourceIds
        for (int i = 0; i < len; i++) {
            getDao().updateShowOrderById(idArr[i], differLen);
        }
        getService(ComponentReserveZoneService.class).adjustCommonShowOrder();
    }

    /**
     * qiucs 2014-11-18
     * <p>描述: 封闭为树节点json</p>
     * 
     * @return List<Map<String,Object>> 返回类型
     */
    public List<Map<String, Object>> getTreeNode() {
        return getTreeNode(null);
    }

    /**
     * qiucs 2014-12-10
     * <p>描述: 封闭为树节点json</p>
     * 
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    public List<Map<String, Object>> getTreeNode(String idPre) {
        Sort sort = new Sort("showOrder");
        idPre = StringUtil.null2empty(idPre);
        List<LogicTableDefine> list = getDao().findAll(sort);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (LogicTableDefine entity : list) {
            data.add(beanToTreeNode(entity, idPre, false));
        }
        return data;
    }

    /**
     * qiucs 2014-12-10
     * <p>描述: 封闭为树节点json</p>
     * 
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    public List<Map<String, Object>> getTreeNode(String logicGroupCode, String idPre) {
        idPre = StringUtil.null2empty(idPre);
        List<LogicTableDefine> list = getDao().getByLogicTableGroupCode(logicGroupCode);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (LogicTableDefine entity : list) {
            data.add(beanToTreeNode(entity, idPre, true));
        }
        return data;
    }

    /**
     * qiujinwei 2014-12-15
     * <p>描述: 封闭为树节点json(for getCopyToLogicTableTree)</p>
     * 
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    public List<Map<String, Object>> getTreeNode2(String code) {
        Sort sort = new Sort("showOrder");
        List<LogicTableDefine> list = getDao().findAll(sort);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (LogicTableDefine entity : list) {
            if (entity.getCode().equals(code)) {
                continue;
            }
            data.add(beanToTreeNode(entity, "-LT", false));
        }
        return data;
    }

    /**
     * qiujinwei 2015-06-05
     * <p>描述: 封闭为树节点json(for logicTableTree)</p>
     * 
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    public List<Map<String, Object>> getTreeNode3(String tableTreeId) {
        String filter = "EQ_tableTreeId=" + tableTreeId;
        List<LogicTableDefine> list = find(filter, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (LogicTableDefine entity : list) {
            data.add(beanToTreeNode(entity, "", false));
        }
        return data;
    }

    /**
     * qiujinwei 2015-01-29
     * <p>描述: 根据逻辑表分类封闭为树节点json</p>
     * 
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    public List<Map<String, Object>> getTreeNodeByClassification(String classification) {
        String filter = "EQ_classification=" + classification.substring(1);
        List<LogicTableDefine> list = find(filter, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (LogicTableDefine entity : list) {
            data.add(beanToTreeNode(entity, "", false));
        }
        return data;
    }

    /**
     * qiucs 2014-11-18
     * <p>描述: 实体bean转为树节点</p>
     * 
     * @return Map<String,Object> 返回类型
     * @throws
     */
    private Map<String, Object> beanToTreeNode(LogicTableDefine entity, String idPre, boolean isParent) {
        Map<String, Object> data = new HashMap<String, Object>();

        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "1");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", "LT");
        userdata.add(item);

        data.put("id", idPre.concat(entity.getCode()));
        data.put("text", entity.getShowName());
        data.put("type", "1");
        data.put("child", isParent);
        data.put("userdata", userdata);
        return data;
    }

    /**
     * qiujinwei 2014-11-20
     * <p>标题: getComboOfTables2TabelCopy</p>
     * <p>描述: 获取逻辑表下所有数据</p>
     * 
     * @return void 返回类型
     * @throws
     */
    public Object getComboOfTables2TabelCopy() {
        List<OptionModel> opts = Lists.newArrayList();
        List<Object[]> list = getDao().getLogicTables();
        OptionModel nullOption = new OptionModel();
        nullOption.setValue(null);
        nullOption.setText("不选择逻辑表");
        opts.add(nullOption);
        for (Object[] table : list) {
            OptionModel option = new OptionModel();
            option.setValue(String.valueOf(table[0]));
            option.setText(String.valueOf(table[1]));
            opts.add(option);
        }
        return opts;
    }

    /**
     * 将物理表生成逻辑表
     * 
     * @param showName 逻辑表显示名称
     * @param copyTableId 复制于的表ID
     */
    @Transactional
    public void copyToLogic(String showName, String code, String copyTableId) {
        LogicTableDefine logic = new LogicTableDefine();
        logic.setShowName(showName);
        logic.setCode(code);
        logic.setRemark("");
        Integer maxShowOrder = getDao().getMaxShowOrder();
        if (null == maxShowOrder) {
            maxShowOrder = new Integer(0);
        }
        logic.setShowOrder((maxShowOrder + 1));
        getDao().save(logic);
        List<ColumnDefine> columnDefineList = getDaoFromContext(ColumnDefineDao.class).findByTableId(copyTableId);
        if (CollectionUtils.isNotEmpty(columnDefineList)) {
            for (ColumnDefine columnDefine : columnDefineList) {
                ColumnDefine dest = new ColumnDefine();
                BeanUtils.copy(columnDefine, dest);
                dest.setId(null);
                dest.setTableId(logic.getCode());
                dest.setCreated("0");
                getDaoFromContext(ColumnDefineDao.class).save(dest);
            }
        }
    }

    /**
     * qiujinwei 2014-11-28
     * <p>标题: getLTExcludingLG</p>
     * <p>描述: 获取不在选中逻辑表组的逻辑表</p>
     * 
     * @param groupCode
     * @return List<LogicTableDefine> 返回类型
     * @throws
     */
    public List<LogicTableDefine> getLTExcludingLG(String groupCode) {
        return getDao().getLTExcludingLG(groupCode);
    }

    /**
     * qiujinwei 2014-12-08
     * <p>标题: getLTIncludingLG</p>
     * <p>描述: 获取在选中逻辑表组的逻辑表</p>
     * 
     * @param groupCode
     * @return List<LogicTableDefine> 返回类型
     * @throws
     */
    public List<LogicTableDefine> getLTIncludingLG(String groupCode, String id) {
        return getDao().getLTIncludingLG(groupCode, getService(LogicGroupRelationService.class).getByID(id).getShowOrder());
    }

    /**
     * 根据逻辑表组Code获取其下的逻辑表
     * 
     * @param logicTableGroupCode 逻辑表组Code
     * @return List<LogicTableDefine>
     */
    public List<LogicTableDefine> getByLogicTableGroupCode(String logicTableGroupCode) {
        return getDao().getByLogicTableGroupCode(logicTableGroupCode);
    }

    /**
     * 根据逻辑表组Code和父逻辑表Code获取逻辑表
     * 
     * @param logicTableGroupCode 逻辑表组Code
     * @param parentTableCode 父逻辑表Code
     * @return List<LogicTableDefine>
     */
    public List<LogicTableDefine> getByGroupIdAndParentTableCode(String logicTableGroupCode, String parentTableCode) {
        return getDao().getByGroupIdAndParentTableCode(logicTableGroupCode, parentTableCode);
    }

    /**
     * 获取逻辑表下拉框选项(构件定义中)
     * 
     * @param logicTableGroupCode 逻辑表组Code
     * @param parentTableCode 逻辑表组中父逻辑表Code
     * @return Object
     */
    public Object comboOfLogicTables(String logicTableGroupCode, String parentTableCode) {
        List<DhtmlxComboOption> opts = Lists.newArrayList();
        try {
            List<LogicTableDefine> logicTableList = null;
            if (StringUtil.isNotEmpty(parentTableCode)) {
                logicTableList = new ArrayList<LogicTableDefine>();
                logicTableList.add(getDao().getByCode(parentTableCode));
                logicTableList.addAll(getDao().getByGroupIdAndParentTableCode(logicTableGroupCode, parentTableCode));
            } else {
                logicTableList = getDao().getByLogicTableGroupCode(logicTableGroupCode);
            }
            if (CollectionUtils.isNotEmpty(logicTableList)) {
                for (LogicTableDefine logicTable : logicTableList) {
                    DhtmlxComboOption option = new DhtmlxComboOption();
                    option.setValue(logicTable.getCode());
                    option.setText(logicTable.getShowName());
                    opts.add(option);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return opts;
    }

    /**
     * qiujinwei 2014-12-01
     * <p>描述: 封闭为树节点json</p>
     * 
     * @return List<Map<String,Object>> 返回类型
     * @throws
     */
    public List<Map<String, Object>> getGroupTreeNode(String groupId) {
        List<LogicTableDefine> list = getDao().getLogicTablesByPhysicalGroup(groupId);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (LogicTableDefine entity : list) {
            data.add(getLogicTablesOfGroupToTreeNode(entity, groupId));
        }
        return data;
    }

    /**
     * qiujinwei 2014-12-01
     * <p>描述: 实体bean转为树节点</p>
     * 
     * @return Map<String,Object> 返回类型
     * @throws
     */
    private Map<String, Object> getLogicTablesOfGroupToTreeNode(LogicTableDefine entity, String groupId) {
        Map<String, Object> data = new HashMap<String, Object>();

        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "1");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", groupId);
        userdata.add(item);

        data.put("id", entity.getCode());
        data.put("text", entity.getShowName() + "(逻辑表)");
        data.put("type", "1");
        data.put("child", Boolean.TRUE);
        data.put("radio", "1");
        data.put("open", Boolean.TRUE);
        data.put("userdata", userdata);
        return data;
    }

    public List<LogicTableDefine> getAllLogicTableDefine() {
        return getDao().getAllLogicTableDefine();
    }

    /**
     * <p>描述: 更新逻辑表状态信息</p>
     * 
     * @param logicTableGroupCode 逻辑表组Code
     * @param parentTableCode 逻辑表组中父逻辑表Code
     * @return void 返回类型
     * @throws
     */
    public void updateStatus(String code, boolean status) {
        LogicTableDefine entity = getByCode(code);
        if (entity != null) {
            if (status) {// 逻辑表应用状态更新为已应用
                entity.setStatus("1");
                getDao().save(entity);
            } else {// 逻辑表应用状态更新为未应用
                if (getDao().getTableInGroupRelation(code).isEmpty()) {
                    entity.setStatus("0");
                    getDao().save(entity);
                }
            }
        }
    }

    /**
     * qiujinwei 2015-04-22
     * <p>描述: 根据逻辑表组code获取第一张逻辑表(没有父逻辑表的)</p>
     * 
     * @return LogicTableDefine 返回类型
     * @throws
     */
    public LogicTableDefine getFirstByGroupCode(String groupCode) {
        return getDao().getFirstByGroupCode(groupCode);
    }
}

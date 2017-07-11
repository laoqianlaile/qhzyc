package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.ModuleDao;
import com.ces.config.dhtmlx.dao.component.ComponentReserveZoneDao;
import com.ces.config.dhtmlx.dao.component.ComponentVersionDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentSelfParamService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FormUtil;
import com.ces.config.utils.GroupUtil;
import com.ces.config.utils.ModuleUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.SystemParameterUtil;
import com.ces.config.utils.TableUtil;
import com.ces.utils.BeanUtils;

@Component
public class ModuleService extends ConfigDefineDaoService<Module, ModuleDao> {

    /*
     * (非 Javadoc)
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("moduleDao")
    @Override
    protected void setDaoUnBinding(ModuleDao dao) {
        super.setDaoUnBinding(dao);
    }

    protected ComponentVersionService getComponentVersionService() {
        return getService(ComponentVersionService.class);
    }

    /*
     * (非 Javadoc)
     * <p>描述: </p>
     * @param entity
     * @return
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Transactional
    public Module save(Module entity) {
        boolean isNew = false;
        entity.updateAreaLayout();
        if (StringUtil.isEmpty(entity.getId())) {
            isNew = true;
            Integer maxShowOrder = getDao().getMaxShowOrderByComponentAreaId(entity.getComponentAreaId());
            if (null == maxShowOrder) {
                maxShowOrder = 0;
            }
            entity.setShowOrder(++maxShowOrder);
            entity = super.save(entity);
            // 更新逻辑表组状态信息
            if (StringUtil.isNotEmpty(entity.getLogicTableGroupCode())) {
                getService(LogicGroupDefineService.class).updateStatus(entity.getLogicTableGroupCode(), Boolean.TRUE);
            }
        } else {
            // 更新逻辑表组状态信息
            Module oldEntity = ModuleUtil.getModule(entity.getId());
            if (StringUtil.isNotEmpty(oldEntity.getLogicTableGroupCode())) {
                if (!oldEntity.getLogicTableGroupCode().equals(entity.getLogicTableGroupCode())) {
                    getService(LogicGroupDefineService.class).updateStatus(oldEntity.getLogicTableGroupCode(), Boolean.FALSE);
                    getService(LogicGroupDefineService.class).updateStatus(entity.getLogicTableGroupCode(), Boolean.TRUE);
                }
            }
            // 清除缓存信息
            ModuleUtil.removeComponentVersionId(entity.getId());
        }
        entity.setUiType(SystemParameterUtil.getInstance().isCoralUI() ? "1" : "0");
        // 保存构件相关数据
        ComponentVersion version = saveComponentVersion(entity, isNew);
        entity.setComponentVersionId(version.getId());
        if (!ConstantVar.Component.Type.TAB.equals(entity.getType())) {
            entity.setComponentUrl("/ces/Q1!toUI?P_moduleId=" + entity.getId()
                    + (entity.isCoralUI() ? "" : "&P_uiType=" + entity.getUiType() + "&P_layoutType=" + entity.getTemplateType()));
        }
        // 更新
        super.save(entity);
        // 同步缓存信息
        ModuleUtil.addModule(entity);
        if (isNew) {
            ModuleUtil.addComponentVersionId(entity.getId(), entity.getComponentVersionId());
        }
        return entity;
    }

    /** 保存构件相关数据. */
    @Transactional
    private ComponentVersion saveComponentVersion(Module entity, boolean isNew) {
        // 1. 保存或更新构件
        ComponentVersion version = getComponentVersionService().saveComponentVersion(entity);
        if ("1".equals(entity.getUpdateComponentConfig())
                && (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(entity.getType()) || ConstantVar.Component.Type.NO_TABLE.equals(entity.getType()) || ConstantVar.Component.Type.LOGIC_TABLE
                        .equals(entity.getType()))) {
            // 2. 删除构件关联信息，包括预留区、方法、回调函数、自身参数、输入参数、输出参数
            getComponentVersionService().deleteSelfDefineComponentVersionRelation(version.getId());
            // 3. 保存构件关联信息，包括预留区
            saveReserveZones(entity, version.getId());
        }
        // 新增标签页构件时
        if (isNew && ConstantVar.Component.Type.TAB.equals(entity.getType())) {
            ComponentSelfParam selfParam = new ComponentSelfParam();
            selfParam.setComponentVersionId(version.getId());
            selfParam.setName("tabPosition");
            selfParam.setType("1");
            selfParam.setValue("1");
            selfParam.setRemark("标签位置，默认在上方");
            selfParam
                    .setOptions("[{value:'',text:'请选择',selected:true},{value:'1',text:'上方'},{value:'2',text:'下方'},{value:'3',text:'左侧'},{value:'4',text:'右侧'}]");
            selfParam.setText("上方");
            getService(ComponentSelfParamService.class).save(selfParam);
            selfParam = new ComponentSelfParam();
            selfParam.setComponentVersionId(version.getId());
            selfParam.setName("activeIndex");
            selfParam.setType("0");
            selfParam.setValue("0");
            selfParam.setRemark("默认激活第几个标签，从0开始");
            getService(ComponentSelfParamService.class).save(selfParam);
            ComponentReserveZone reserveZone = new ComponentReserveZone();
            reserveZone.setComponentVersionId(version.getId());
            reserveZone.setName("tabbar");
            reserveZone.setAlias("标签页");
            reserveZone.setType("4");
            reserveZone.setPage("tab.jsp");
            reserveZone.setIsCommon(false);
            getService(ComponentReserveZoneService.class).save(reserveZone);
        }
        return version;
    }

    /** 保存预留区 */
    @Transactional
    public void saveReserveZones(Module entity, String versionId) {
        if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(entity.getType())) {
            // 方法和回调函数使用公用的，这里无需添加了
            if (AppDefineUtil.TYPE_1C.equals(entity.getTemplateType())) {
                // 保存工具条预留区，如果是列表，同时要保存表单工具条
                if (StringUtil.isNotEmpty(entity.getTable1Id()) && StringUtil.isNotEmpty(entity.getArea1Id())) {
                    if (!AppDefineUtil.L_FORM.equals(entity.getArea1Id())) {
                        saveReserveZone(versionId, AppDefineUtil.getPage(1), entity.getArea1Id(), entity.getName(), entity.getTable1Id(), 1);
                    }
                    saveReserveZone(versionId, AppDefineUtil.getPage(1), AppDefineUtil.L_FORM, entity.getName(), entity.getTable1Id(), 1);
                }
            } else if (AppDefineUtil.TYPE_2E.equals(entity.getTemplateType()) ||
            		AppDefineUtil.TYPE_2E_S.equals(entity.getTemplateType())) {
                if (StringUtil.isNotEmpty(entity.getTable1Id()) && StringUtil.isNotEmpty(entity.getArea1Id()) && !AppDefineUtil.L_SEARCH.equals(entity.getArea1Id())) {
                    saveReserveZone(versionId, AppDefineUtil.getPage(1), entity.getArea1Id(), entity.getName(), entity.getTable1Id(), 1);
                    if (!AppDefineUtil.L_FORM.equals(entity.getArea1Id())) {
                        if (!entity.getTable2Id().equals(entity.getTable1Id())) {
                            saveReserveZone(versionId, AppDefineUtil.getPage(1), AppDefineUtil.L_FORM, entity.getName(), entity.getTable1Id(), 1);
                        }
                    }
                }
                if (StringUtil.isNotEmpty(entity.getTable2Id()) && StringUtil.isNotEmpty(entity.getArea2Id())) {
                    saveReserveZone(versionId, AppDefineUtil.getPage(2), entity.getArea2Id(), entity.getName(), entity.getTable2Id(), 2);
                    if (!AppDefineUtil.L_FORM.equals(entity.getArea2Id())) {
                        if (!entity.getTable2Id().equals(entity.getTable1Id()) ||
                        		AppDefineUtil.TYPE_2E_S.equals(entity.getTemplateType())) {
                            saveReserveZone(versionId, AppDefineUtil.getPage(2), AppDefineUtil.L_FORM, entity.getName(), entity.getTable2Id(), 2);
                        }
                    }
                }
            } else if (AppDefineUtil.TYPE_3L.equals(entity.getTemplateType()) || AppDefineUtil.TYPE_3E.equals(entity.getTemplateType())) {
                if (StringUtil.isNotEmpty(entity.getTable1Id()) && StringUtil.isNotEmpty(entity.getArea1Id())) {
                    saveReserveZone(versionId, AppDefineUtil.getPage(1), entity.getArea1Id(), entity.getName(), entity.getTable1Id(), 1);
                    if (!AppDefineUtil.L_FORM.equals(entity.getArea1Id())) {
                        if (!entity.getTable2Id().equals(entity.getTable1Id())) {
                            saveReserveZone(versionId, AppDefineUtil.getPage(1), AppDefineUtil.L_FORM, entity.getName(), entity.getTable1Id(), 1);
                        }
                    }
                }
                if (StringUtil.isNotEmpty(entity.getTable2Id()) && StringUtil.isNotEmpty(entity.getArea2Id())) {
                    saveReserveZone(versionId, AppDefineUtil.getPage(2), entity.getArea2Id(), entity.getName(), entity.getTable2Id(), 2);
                    if (!AppDefineUtil.L_FORM.equals(entity.getArea2Id())) {
                        if (!entity.getTable1Id().equals(entity.getTable2Id()) && !entity.getTable3Id().equals(entity.getTable2Id())) {
                            saveReserveZone(versionId, AppDefineUtil.getPage(2), AppDefineUtil.L_FORM, entity.getName(), entity.getTable2Id(), 2);
                        }
                    }
                }
                if (StringUtil.isNotEmpty(entity.getTable3Id()) && StringUtil.isNotEmpty(entity.getArea3Id())) {
                    saveReserveZone(versionId, AppDefineUtil.getPage(3), entity.getArea3Id(), entity.getName(), entity.getTable3Id(), 3);
                    if (!AppDefineUtil.L_FORM.equals(entity.getArea3Id())) {
                        if (!entity.getTable2Id().equals(entity.getTable3Id())) {
                            saveReserveZone(versionId, AppDefineUtil.getPage(3), AppDefineUtil.L_FORM, entity.getName(), entity.getTable3Id(), 3);
                        }
                    }
                }
            }
            // 生成表单中下拉列表的按钮预留区
            Set<String> tableIdSet = new HashSet<String>();
            if (StringUtil.isNotEmpty(entity.getTable1Id()) && StringUtil.isNotEmpty(entity.getArea1Id())) {
                tableIdSet.add(entity.getTable1Id());
            }
            if (StringUtil.isNotEmpty(entity.getTable2Id()) && StringUtil.isNotEmpty(entity.getArea2Id())) {
                tableIdSet.add(entity.getTable2Id());
            }
            if (StringUtil.isNotEmpty(entity.getTable3Id()) && StringUtil.isNotEmpty(entity.getArea3Id())) {
                tableIdSet.add(entity.getTable3Id());
            }
            if (CollectionUtils.isNotEmpty(tableIdSet)) {
                List<ColumnDefine> comboGridColumnList = new ArrayList<ColumnDefine>();
                for (String tableId : tableIdSet) {
                    comboGridColumnList.addAll(getService(ColumnDefineService.class).findByTableIdAndInputType(tableId, FormUtil.CoralInputType.COMBOGRID));
                }
                if (CollectionUtils.isNotEmpty(comboGridColumnList)) {
                    for (ColumnDefine cd : comboGridColumnList) {
                        PhysicalTableDefine tableDefine = getService(PhysicalTableDefineService.class).getByID(cd.getTableId());
                        saveReserveZone(versionId, AppDefineUtil.getPage(0), tableDefine.getShowName(), cd);
                    }
                }
                List<ColumnDefine> textBoxButtonColumnList = new ArrayList<ColumnDefine>();
                for (String tableId : tableIdSet) {
                    textBoxButtonColumnList.addAll(getService(ColumnDefineService.class).findByTableIdAndInputOption(tableId,
                            FormUtil.CoralInputOption.TEXTBOXBUTTON));
                }
                if (CollectionUtils.isNotEmpty(textBoxButtonColumnList)) {
                    for (ColumnDefine cd : textBoxButtonColumnList) {
                        PhysicalTableDefine tableDefine = getService(PhysicalTableDefineService.class).getByID(cd.getTableId());
                        saveReserveZone(versionId, AppDefineUtil.getPage(0), tableDefine.getShowName(), cd);
                    }
                }
            }
        } else if (ConstantVar.Component.Type.NO_TABLE.equals(entity.getType())) {
            // 保存工具条预留区，如果是列表，同时要保存表单工具条
            if (StringUtil.isNotEmpty(entity.getArea1Id())) {
                if (!AppDefineUtil.L_FORM.equals(entity.getArea1Id())) {
                    saveReserveZone(versionId, AppDefineUtil.getPage(1), entity.getArea1Id(), entity.getName(), "", 1);
                }
                saveReserveZone(versionId, AppDefineUtil.getPage(1), AppDefineUtil.L_FORM, entity.getName(), "", 1);
            }
        } else if (ConstantVar.Component.Type.LOGIC_TABLE.equals(entity.getType())) {
            Set<String> tableIdSet = new HashSet<String>();
            if (StringUtil.isNotEmpty(entity.getTable1Id()) && StringUtil.isNotEmpty(entity.getArea1Id())) {
                tableIdSet.add(entity.getTable1Id());
            }
            if (StringUtil.isNotEmpty(entity.getTable2Id()) && StringUtil.isNotEmpty(entity.getArea2Id())) {
                tableIdSet.add(entity.getTable2Id());
            }
            if (StringUtil.isNotEmpty(entity.getTable3Id()) && StringUtil.isNotEmpty(entity.getArea3Id())) {
                tableIdSet.add(entity.getTable3Id());
            }
            if (CollectionUtils.isNotEmpty(tableIdSet)) {
                List<ColumnDefine> textBoxButtonColumnList = new ArrayList<ColumnDefine>();
                for (String tableId : tableIdSet) {
                    textBoxButtonColumnList.addAll(getService(ColumnDefineService.class).findByTableIdAndInputOption(tableId,
                            FormUtil.CoralInputOption.TEXTBOXBUTTON));
                }
                if (CollectionUtils.isNotEmpty(textBoxButtonColumnList)) {
                    for (ColumnDefine cd : textBoxButtonColumnList) {
                        LogicTableDefine tableDefine = getService(LogicTableDefineService.class).getByCode(cd.getTableId());
                        saveReserveZone(versionId, AppDefineUtil.getPage(0), tableDefine.getShowName(), cd);
                    }
                }
            }
        }
    }

    /** 保存预留区. */
    @Transactional
    private void saveReserveZone(String versionId, String page, String type, String text, String tableId, int areaIndex) {
        ComponentReserveZone entity = new ComponentReserveZone();
        if (AppDefineUtil.isGrid(type)) {
            String reserveZoneName = AppDefineUtil.getZoneName(tableId, type, areaIndex, null, null);
            ComponentReserveZone componentReserveZone = getDaoFromContext(ComponentReserveZoneDao.class).getByComponentVersionIdAndNameAndPage(versionId,
                    reserveZoneName, page);
            if (componentReserveZone != null) {
                return;
            }
            entity.setComponentVersionId(versionId);
            entity.setName(reserveZoneName);
            entity.setPage(page);
            entity.setAlias(AppDefineUtil.getZoneAlias(text, type, areaIndex, null, null));
            entity.setType("0");
            entity.setIsCommon(false);
            getService(ComponentReserveZoneService.class).save(entity);
            // 列表超链接预留区
            ComponentReserveZone gridLinkReserveZone = new ComponentReserveZone();
            gridLinkReserveZone.setComponentVersionId(versionId);
            gridLinkReserveZone.setName(AppDefineUtil.getZoneName(tableId, type, areaIndex, "LINK", null));
            gridLinkReserveZone.setPage(page);
            gridLinkReserveZone.setAlias(AppDefineUtil.getZoneAlias(text, type, areaIndex, "超链接", null));
            gridLinkReserveZone.setType("1");
            gridLinkReserveZone.setIsCommon(false);
            getService(ComponentReserveZoneService.class).save(gridLinkReserveZone);
        } else if (AppDefineUtil.isForm(type)) {
            // 表单上工具条
            String reserveZoneName = AppDefineUtil.getZoneName(tableId, type, areaIndex, null, "0");
            ComponentReserveZone componentReserveZone = getDaoFromContext(ComponentReserveZoneDao.class).getByComponentVersionIdAndNameAndPage(versionId,
                    reserveZoneName, page);
            if (componentReserveZone != null) {
                return;
            }
            entity.setComponentVersionId(versionId);
            entity.setName(reserveZoneName);
            entity.setPage(page);
            entity.setAlias(AppDefineUtil.getZoneAlias(text, type, areaIndex, null, "0"));
            entity.setType("0");
            entity.setIsCommon(false);
            getService(ComponentReserveZoneService.class).save(entity);
            // 表单下工具条
            String reserveZoneName1 = AppDefineUtil.getZoneName(tableId, type, areaIndex, null, "1");
            ComponentReserveZone componentReserveZone1 = getDaoFromContext(ComponentReserveZoneDao.class).getByComponentVersionIdAndNameAndPage(versionId,
                    reserveZoneName1, page);
            if (componentReserveZone1 != null) {
                return;
            }
            entity = new ComponentReserveZone();
            entity.setComponentVersionId(versionId);
            entity.setName(reserveZoneName1);
            entity.setPage(page);
            entity.setAlias(AppDefineUtil.getZoneAlias(text, type, areaIndex, null, "1"));
            entity.setType("0");
            entity.setIsCommon(false);
            getService(ComponentReserveZoneService.class).save(entity);
        }
    }

    /** 保存下拉列表按钮预留区. */
    @Transactional
    public void saveReserveZone(String versionId, String page, String TableShowName, ColumnDefine columnDefine) {
        ComponentReserveZone entity = new ComponentReserveZone();
        String reserveZoneName = AppDefineUtil.RZ_NAME_PRE.concat(columnDefine.getId());
        ComponentReserveZone componentReserveZone = getDaoFromContext(ComponentReserveZoneDao.class).getByComponentVersionIdAndNameAndPage(versionId,
                reserveZoneName, page);
        if (componentReserveZone != null) {
            return;
        }
        entity.setComponentVersionId(versionId);
        entity.setName(reserveZoneName);
        entity.setPage(page);
        entity.setAlias(TableShowName + "-" + columnDefine.getShowName() + "-按钮");
        entity.setType("2");
        entity.setIsCommon(false);
        getService(ComponentReserveZoneService.class).save(entity);
    }

    /*
     * (非 Javadoc)
     * <p>标题: delete</p>
     * <p>描述: </p>
     * @param ids
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            Module entity = ModuleUtil.getModule(id);
            // 删除构件库中相关记录
            ComponentVersion version = null;
            if (StringUtil.isNotEmpty(entity.getComponentVersionId())) {
                version = getComponentVersionService().getByID(entity.getComponentVersionId());
            }
            if (null != version) {
                getComponentVersionService().deleteComponentVersion(version.getId(), false);
            }
            // 删除相关的所在配置信息
            deleteRelation(entity);
            // 删除自身记录
            getDao().delete(entity);
            // 更新逻辑表组状态信息
            getService(LogicGroupDefineService.class).updateStatus(entity.getLogicTableGroupCode(), Boolean.FALSE);
        }
    }

    /**
     * wanglei 2013-11-13
     * 删除模块（删除导入的自定义模块构件时使用）
     * 
     * @param id 模块ID
     */
    @Transactional
    public void deleteModule(String id) {
        Module entity = ModuleUtil.getModule(id);
        if (entity != null) {
            // 删除相关的所在配置信息
            deleteRelation(entity);
            // 删除自身记录
            getDao().deleteById(id);
        }
    }

    /**
     * qiucs 2013-9-9
     * <p>描述: 拖拽调整显示顺序</p>
     * 
     * @param beginIds
     * @param endId 设定参数
     * @return void 返回类型
     * @throws
     */
    @Transactional
    public void adjustShowOrder(String componentAreaId, String sourceIds, String targetId) {
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
            // 向上拖动
            // getDao().upShowOrder(parentId, eShowOrder, bShowOrder);
            increaseNum = len;
            differLen = tShowOrder - sBeginShowOrder;
            // tShowOrder-1 < showOrder < sBeginShowOrder
            begin = tShowOrder - 1;
            end = sBeginShowOrder;
        } else {
            // 向下拖动
            // getDao().downShowOrder(parentId, bShowOrder, eShowOrder);
            increaseNum = -len;
            differLen = tShowOrder - sEndShowOrder;
            // sEndShowOrder < showOrder < tShowOrder + 1
            begin = sEndShowOrder;
            end = tShowOrder + 1;
        }
        // update between sourceIds and targetId
        getDao().batchUpdateShowOrder(componentAreaId, begin, end, increaseNum);
        // update sourceIds
        for (int i = 0; i < len; i++) {
            getDao().updateShowOrderById(idArr[i], differLen);
        }
    }

    /**
     * 查询源表和关联表记录数目
     * 
     * @param tableId, rId
     */
    @Transactional
    public long getTotalTableRelation(String tableId, String rId) {
        return getDao().getTotalTableRelation(tableId, tableId);
    }

    /**
     * qiucs 2013-10-28
     * <p>描述: 获取构件版本ID</p>
     * 
     * @param id
     * @return String 返回类型
     */
    public String getComponentVersionId(String id) {
        return getDao().getComponentVersionId(id);
    }

    /**
     * qiucs 2013-11-27
     * <p>描述: 删除模块相关的所配置信息</p>
     * 
     * @param id 设定参数
     */
    @Transactional
    protected void deleteRelation(Module entity) {
        // String id = entity.getId();
        // 应用配置
        getService(AppDefineService.class).deleteByComponentVersionId(entity.getComponentVersionId());
        // 报表按钮配置
        // getService(ReportBindingService.class).deleteByModuleId(id);
        // 工作流节点与模块配置
        // getService(WorkflowBindingService.class).deleteByModuleId(id);
        // 工作流表单与模块配置
        // getService(WorkflowFormSettingService.class).deleteByModuleId(id);
        ModuleUtil.removeComponentVersionId(entity.getId());
        ModuleUtil.removeModule(entity);
    }

    /**
     * qiucs 2013-11-28
     * <p>描述: 根据树ID更新模块配置</p>
     * 
     * @param treeId 设定参数
     */
    @Transactional
    public void updateByTreeId(String treeId) {
        getDao().updateByTreeId(treeId);
    }

    /**
     * <p>描述: 根据构件版本ID获取模块</p>
     * 
     * @param componentVersionId 构件版本ID
     * @return Module 返回类型
     */
    public Module findByComponentVersionId(String componentVersionId) {
        return getDao().findByComponentVersionId(componentVersionId);
    }

    /**
     * qiucs 2014-2-10
     * <p>描述: 根据版本ID获取模块ID</p>
     */
    public String getIdByComponentVersionId(String componentVersionId) {
        return getDao().getIdByComponentVersionId(componentVersionId);
    }

    /**
     * liaomingsong 2015-5-21
     * <p>描述: 根据版本ID获取模块ID(首先判断是否是组合构件)</p>
     */
    public String getIdByComponentVersionIdOr(String componentVersionId) {
        ComponentVersion cv = getDaoFromContext(ComponentVersionDao.class).findOne(componentVersionId);
        if ("4".equals(cv.getComponent().getType())) {// 为组合构件
            Construct construct = getDaoFromContext(ConstructDao.class).getByAssembleComponentVersionId(componentVersionId);
            return getDao().getIdByComponentVersionId(construct.getBaseComponentVersionId());
        } else {
            return getDao().getIdByComponentVersionId(componentVersionId);
        }
    }

    /**
     * wl 2014-4-29
     * <p>描述: 根据模块名称获取模块ID</p>
     * 
     * @param name 模块名称
     */
    public String getIdByName(String name) {
        return getDao().getIdByName(name);
    }

    /**
     * qiucs 2014-2-10
     * <p>描述: 根据构件域ID获取模块ID</p>
     * 
     * @param componentVersionId
     */
    public List<Module> getByComponentAreaId(String componentAreaId) {
        return getDao().getByComponentAreaId(componentAreaId);
    }

    /**
     * qiucs 2014-3-20
     * <p>描述: 根据版本构件ID获取树ID</p>
     */
    public String getTreeIdByComponentVersionId(String componentVersionId) {
        return getDao().getTreeIdByComponentVersionId(componentVersionId);
    }

    /**
     * 将被选择构件的应用配置应用到其他构件中
     * 
     * @return
     */
    @Transactional
    public void appApplyToModules(String tableId, String moduleId, String appToModuleIds, String userId) {
        /*
         * String[] allModuleIds = appToModuleIds.split(",");
         * for (String appToModuleId : allModuleIds) {
         * if ("-1".equals(appToModuleId) || appToModuleId.equals(moduleId)) {
         * continue;
         * }
         * // 检索定义应用到
         * AppSearchPanel asp = getService(AppSearchPanelService.class).findUserEntityByFk(tableId, moduleId, "-1",
         * userId);
         * getService(AppSearchPanelService.class).clear(asp);// 清除目标构件的检索定义设置
         * AppSearchPanel aspNew = new AppSearchPanel();
         * BeanUtils.copy(asp, aspNew);
         * List<AppSearch> objs1 = getService(AppSearchService.class).findDefineList(tableId, moduleId, "-1", userId);
         * String rowsValue = "";
         * if (objs1.size() > 0) {
         * for (Object[] obj : objs1) {
         * String rGridValue = obj[0] + "," + obj[3];
         * rowsValue += ";" + rGridValue;
         * }
         * rowsValue = rowsValue.substring(1);
         * getService(AppSearchPanelService.class).copy(aspNew, userId, appToModuleId, rowsValue);
         * }
         * // 列表字段定义应用到
         * AppGrid fromGrid = getService(AppGridService.class).findByFk(tableId, moduleId, "-1", userId);
         * getService(AppGridService.class).clear(fromGrid);
         * String rowsValueColunm = "";
         * List<Object[]> objs2 = getService(AppColumnService.class).getDefineColumn(tableId, moduleId, "-1", userId);
         * AppGrid toGrid = new AppGrid();
         * BeanUtils.copy(fromGrid, toGrid);
         * if (objs2.size() > 0) {
         * for (Object[] obj : objs2) {
         * rowsValueColunm += ";" + obj[5] + "|" + obj[6] + "|" + obj[1] + "|" + obj[2] + "|" + obj[3] + "|" + obj[4] +
         * "|"
         * + StringUtil.null2empty(obj[9]) + "|" + obj[11];
         * }
         * rowsValueColunm = rowsValueColunm.substring(1);
         * toGrid.setId(null);
         * toGrid.setTableId(tableId);
         * toGrid.setModuleId(appToModuleId);
         * toGrid.setUserId(userId);
         * getService(AppGridService.class).save(toGrid, rowsValueColunm, userId);
         * }
         * // 列表过滤条件应用到
         * getService(AppFilterService.class).clear(tableId, appToModuleId, "-1");
         * String rowsValueFilter = "";
         * List<Object[]> objs3 = getService(AppFilterService.class).getDefineColumn(tableId, moduleId, "-1");
         * if (objs3.size() > 0) {
         * for (Object[] obj : objs3) {
         * rowsValueFilter += ";" + obj[5] + "," + StringUtil.null2empty(obj[6]) + "," + StringUtil.null2empty(obj[7]) +
         * "," + obj[4] + "," + obj[2]
         * + "," + obj[3] + "," + StringUtil.null2empty(obj[8]);
         * }
         * rowsValueFilter = rowsValueFilter.substring(1);
         * getService(AppFilterService.class).save(tableId, appToModuleId, "-1", rowsValueFilter);
         * }
         * // 列表排序定义应用到
         * List<AppSort> objs4 = getService(AppSortService.class).findDefineList(tableId, moduleId, "-1", userId);
         * getService(AppSortService.class).clear(tableId, appToModuleId, "-1", userId);
         * String rowsValueSort = "";
         * if (objs4.size() > 0) {
         * for (AppSort obj : objs4) {
         * rowsValueSort += ";" + obj.getColumnId() + "," + obj.getSortType();
         * }
         * rowsValueSort = rowsValueSort.substring(1);
         * getService(AppSortService.class).save(tableId, appToModuleId, "-1", userId, rowsValueSort);
         * }
         * // 列表工具条应用到
         * getService(AppButtonService.class).clear(tableId, appToModuleId, AppButton.BUTTON_GRID);
         * String rowsValueButton = "";
         * String reportIds = "";
         * List<AppButton> obj5 = getService(AppButtonService.class).getDefineButton(tableId, moduleId,
         * AppButton.BUTTON_GRID,
         * ConstantVar.AppDefine.USE_DAFAULT);
         * if (obj5.size() > 0) {
         * for (AppButton obj : obj5) {
         * rowsValueButton += ";" + obj.getButtonCode() + "," + obj.getShowName() + "," +
         * StringUtil.null2empty(obj.getRemark());
         * }
         * rowsValueButton = rowsValueButton.substring(1);
         * }
         * List<String> reports = getDaoFromContext(ReportBindingDao.class).getReportIdsByFk(tableId, moduleId);
         * if (reports.size() > 0) {
         * for (String obj : reports) {
         * reportIds += "," + obj;
         * }
         * reportIds = reportIds.substring(1);
         * }
         * if (obj5.size() > 0 || reports.size() > 0) {
         * getService(AppButtonService.class).save(tableId, appToModuleId, AppButton.BUTTON_GRID, rowsValueButton,
         * reportIds);
         * }
         * // 界面定义应用到
         * getService(AppFormService.class).clear(tableId, appToModuleId);
         * String elementsValue = "";
         * AppForm af = getService(AppFormService.class).findByFk(tableId, moduleId, ConstantVar.AppDefine.USE_DAFAULT);
         * AppForm afNew = new AppForm();
         * BeanUtils.copy(af, afNew);
         * afNew.setId("");
         * afNew.setModuleId(appToModuleId);
         * List<AppFormElement> elements = getService(AppFormElementService.class).elements(tableId, moduleId,
         * ConstantVar.AppDefine.USE_DAFAULT);
         * if (elements.size() > 0) {
         * for (AppFormElement obj : elements) {
         * elementsValue += ";" + obj.getId() + "," + obj.getColspan() + "," + obj.getRequired() + "," +
         * obj.getReadonly() + "," + obj.getHidden()
         * + "," + obj.getTextarea() + "," + obj.getDefaultValue() + "," + obj.getValidation() + "," + obj.getTooltip()
         * + "," + obj.getKept();
         * }
         * elementsValue = elementsValue.substring(1);
         * getService(AppFormService.class).save(afNew, elementsValue);
         * }
         * // 界面工具条应用到
         * getService(AppButtonService.class).clear(tableId, appToModuleId, AppButton.BUTTON_FORM);
         * String rowsValueButtonForm = "";
         * List<AppButton> obj7 = getService(AppButtonService.class).getDefineButton(tableId, moduleId,
         * AppButton.BUTTON_FORM,
         * ConstantVar.AppDefine.USE_DAFAULT);
         * if (obj7.size() > 0) {
         * for (AppButton obj : obj7) {
         * rowsValueButtonForm += ";" + obj.getButtonCode() + "," + obj.getShowName() + "," +
         * StringUtil.null2empty(obj.getRemark());
         * }
         * rowsValueButtonForm = rowsValueButtonForm.substring(1);
         * getService(AppButtonService.class).save(tableId, appToModuleId, AppButton.BUTTON_FORM, rowsValueButtonForm,
         * "");
         * }
         * }
         */

        /*
         * for(String appToModuleId : allModuleIds){
         * }
         */
    }

    /**
     * qiucs 2014-9-23
     * <p>描述: 获取与表ID相关的模块</p>
     */
    public List<Module> getWorkflowModuleByTableId(String tableId) {
        return getDao().getWorkflowModuleByTableId("%" + tableId + "%");
    }

    /**
     * qiucs 2014-12-10
     * <p>描述: 根据表ID获取自定义构件ID</p>
     */
    public List<String> getComponentVersionIdsByTableId(String tableId) {
        List<String> idList = new ArrayList<String>();
        List<Module> mList = getByTableId(tableId);
        for (Module m : mList)
            idList.add(m.getComponentVersionId());
        return idList;
    }

    /**
     * qiucs 2014-12-10
     * <p>描述: 根据表ID获取自定义构件</p>
     */
    public List<Module> getByTableId(String tableId) {
        String logicTableCode = TableUtil.getLogicTableCode(tableId);
        if (StringUtil.isEmpty(logicTableCode)) {
            return getDao().getByTableId("%" + tableId + "%");
        }
        return getDao().getByTableId("%" + tableId + "%", "%\"" + logicTableCode + "\"%");
    }

    /**
     * qiucs 2015-2-3 下午2:11:32
     * <p>描述: 判断指定table(tableId or logicTableCode)是否在指定的构件中 </p>
     * 
     * @return boolean
     */
    public boolean containTable(String id, String table/* tableId or logicTableCode */, String type) {

        if (ConstantVar.Component.Type.TREE.equals(type)) {
            Object cnt = getDao().countTreeNodeByIdAndTableId(id, table);
            return Integer.parseInt(cnt.toString()) > 0;
        }

        return (null != getDao().getByIdAndTableId(id, "%\"" + table + "\"%"));
    }
    
    /**
     * qiucs 2015-7-2 上午10:30:02
     * <p>描述: 如果逻辑表构件，则把相应的逻辑表编码转成对应的物理表ID </p>
     * @return MessageModel
     */
    public MessageModel toPageModule(String id, String physicalGroupId) {
    	Module entity = ModuleUtil.getModule(id);
    	
    	if ("5".equals(entity.getType())) {
    		if (StringUtil.isEmpty(physicalGroupId)) {
    			return MessageModel.falseInstance(0, entity);
    		}
    		String logicGroupCode = GroupUtil.getLogicGroupCode(physicalGroupId);
    		if (!logicGroupCode.equals(entity.getLogicTableGroupCode())) {
    			return MessageModel.falseInstance(1, entity);
    		}
    		Module destEntity = new Module();
    		BeanUtils.copy(entity, destEntity);
    		//List<PhysicalGroupRelation> list = getService(PhysicalGroupRelationService.class).findByGroupId(physicalGroupId);
    		Map<String, String> groupMap = GroupUtil.getPhysicalGroupTables(physicalGroupId);//new HashMap<String, String>();
            /*for (PhysicalGroupRelation r : list) {
            	groupMap.put(TableUtil.getLogicTableCode(r.getTableId()), r.getTableId());
            }*/
            String code = null;
            code = destEntity.getTable1Id();
            if (StringUtil.isNotEmpty(code)) {
            	destEntity.setTable1Id(groupMap.get(code));
            }
            code = destEntity.getTable2Id();
            if (StringUtil.isNotEmpty(code)) {
            	destEntity.setTable2Id(groupMap.get(code));
            }
            code = destEntity.getTable3Id();
            if (StringUtil.isNotEmpty(code)) {
            	destEntity.setTable3Id(groupMap.get(code));
            }
            entity = destEntity;
    	}
    	
    	return MessageModel.trueInstance(entity);
    }

    /**
     * 根据表ID获取用到该表的构件版本的IDs
     * 
     * @return Set<String>
     */
    public Set<String> getCVIdsByTableId(String tableId) {
        Set<String> cvIds = new HashSet<String>();
        // 物理表构件
        cvIds.addAll(getDao().getCVIdsByTableId("%\"" + tableId + "\"%"));
        // 逻辑表构件
        cvIds.addAll(getDao().getCVIdsByTableId("%\"" + TableUtil.getLogicTableCode(tableId) + "\"%"));
        // 树构件
        cvIds.addAll(getDao().getCVIdsByTableIdOfTree(tableId));
        return cvIds;
    }
}

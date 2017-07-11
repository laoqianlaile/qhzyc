package com.ces.config.dhtmlx.service.construct;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentDao;
import com.ces.config.dhtmlx.dao.component.ComponentVersionDao;
import com.ces.config.dhtmlx.dao.construct.ConstructCallbackDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDetailDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDetailSelfParamDao;
import com.ces.config.dhtmlx.dao.construct.ConstructFunctionDao;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.component.Component;
import com.ces.config.dhtmlx.entity.component.ComponentAssembleArea;
import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructCallback;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.construct.ConstructDetailSelfParam;
import com.ces.config.dhtmlx.entity.construct.ConstructFunction;
import com.ces.config.dhtmlx.entity.construct.ConstructInputParam;
import com.ces.config.dhtmlx.entity.construct.ConstructSelfParam;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.authority.AuthorityConstructButtonService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.CommonComponentRelationService;
import com.ces.config.dhtmlx.service.component.ComponentAssembleAreaService;
import com.ces.config.dhtmlx.service.component.ComponentInputParamService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentSelfParamService;
import com.ces.config.dhtmlx.service.component.ComponentService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.utils.CfgCommonUtil;
import com.ces.config.utils.ComponentInfoUtil;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.PreviewUtil;
import com.ces.config.utils.StringUtil;
import com.ces.utils.BeanUtils;

/**
 * 组合构件绑定关系Service
 * 
 * @author wanglei
 * @date 2013-08-26
 */
@org.springframework.stereotype.Component("constructService")
public class ConstructService extends ConfigDefineDaoService<Construct, ConstructDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("constructDao")
    @Override
    protected void setDaoUnBinding(ConstructDao dao) {
        super.setDaoUnBinding(dao);
    }

    @Override
    public Construct getByID(String id) {
        Construct construct = ComponentInfoUtil.getInstance().getConstruct(id);
        if (construct == null) {
            construct = super.getByID(id);
        }
        return construct;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public Construct save(Construct entity) {
        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(entity.getBaseComponentVersionId());
        // 先保存组合构件
        ComponentVersion assembleComponentVersion = entity.getAssembleComponentVersion();
        Component dbComponent = null;
        String dbComponentName = null;
        String dbComponentAlias = null;
        if (StringUtil.isNotEmpty(assembleComponentVersion.getId())) {
            ComponentVersion dbComponentVersion = getDaoFromContext(ComponentVersionDao.class).findOne(assembleComponentVersion.getId());
            dbComponent = dbComponentVersion.getComponent();
            dbComponentName = dbComponent.getName();
            dbComponentAlias = dbComponent.getAlias();
        }
        assembleComponentVersion = getService(ComponentVersionService.class).saveAssembleComponentVersion(assembleComponentVersion,
                baseComponentVersion.getViews());
        entity.setAssembleComponentVersion(assembleComponentVersion);
        // 改了构件名称或别名
        if (dbComponent != null
                && (!assembleComponentVersion.getComponent().getName().equals(dbComponentName) || !assembleComponentVersion.getComponent().getAlias()
                        .equals(dbComponentAlias))) {
            List<ComponentVersion> componentVersionList = getDaoFromContext(ComponentVersionDao.class).getByComponentId(dbComponent.getId());
            if (CollectionUtils.isNotEmpty(componentVersionList)) {
                for (ComponentVersion tempCV : componentVersionList) {
                    if (tempCV.getId().equals(assembleComponentVersion.getId())) {
                        continue;
                    }
                    Construct tempConstruct = getDao().getByAssembleComponentVersionId(tempCV.getId());
                    if (tempConstruct != null) {
                        ComponentInfoUtil.getInstance().putConstruct(tempConstruct);
                    }
                }
            }
        }
        // 继承指定的 预设的公用预留区和构件的绑定关系
        Set<String> commonReserveZoneIds = new HashSet<String>();
        boolean flag = true;
        if (StringUtil.isNotEmpty(entity.getId())) {
            Construct oldEntity = getDao().findOne(entity.getId());
            if (null != oldEntity) {
                if (!oldEntity.getBaseComponentVersionId().equals(entity.getBaseComponentVersionId())) {
                    // 删除自身参数和输入参数
                    getService(ConstructInputParamService.class).deleteByConstructId(oldEntity.getId());
                    getService(ConstructSelfParamService.class).deleteByConstructId(oldEntity.getId());
                    // 删除构件所有预留区绑定的构件
                    getService(ConstructDetailService.class).deleteByConstructId(oldEntity.getId());
                    getService(AuthorityConstructButtonService.class).deleteByComponentVersionId(oldEntity.getAssembleComponentVersion().getId());
                    // 公用预留区处理方法：只有逻辑表构件使用公用预留区，如果更改后的基础构件是逻辑表构件，则添加公用预留区
                    if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                        Module module = getService(ModuleService.class).findByComponentVersionId(entity.getBaseComponentVersionId());
                        if (StringUtil.isNotEmpty(module.getTable1Id())) {
                            commonReserveZoneIds.addAll(getCommonReserveZoneIdsByTableId(module.getTable1Id()));
                        }
                        if (StringUtil.isNotEmpty(module.getTable2Id())) {
                            commonReserveZoneIds.addAll(getCommonReserveZoneIdsByTableId(module.getTable2Id()));
                        }
                        if (StringUtil.isNotEmpty(module.getTable3Id())) {
                            commonReserveZoneIds.addAll(getCommonReserveZoneIdsByTableId(module.getTable3Id()));
                        }
                    }
                } else {
                    flag = false;
                }
            } else {
                // 公用预留区处理方法：只有逻辑表构件使用公用预留区，如果更改后的基础构件是逻辑表构件，则添加公用预留区
                if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                    Module module = getService(ModuleService.class).findByComponentVersionId(entity.getBaseComponentVersionId());
                    if (StringUtil.isNotEmpty(module.getTable1Id())) {
                        commonReserveZoneIds.addAll(getCommonReserveZoneIdsByTableId(module.getTable1Id()));
                    }
                    if (StringUtil.isNotEmpty(module.getTable2Id())) {
                        commonReserveZoneIds.addAll(getCommonReserveZoneIdsByTableId(module.getTable2Id()));
                    }
                    if (StringUtil.isNotEmpty(module.getTable3Id())) {
                        commonReserveZoneIds.addAll(getCommonReserveZoneIdsByTableId(module.getTable3Id()));
                    }
                }
            }
        } else {
            // 公用预留区处理方法：只有逻辑表构件使用公用预留区，如果更改后的基础构件是逻辑表构件，则添加公用预留区
            if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                Module module = getService(ModuleService.class).findByComponentVersionId(entity.getBaseComponentVersionId());
                if (StringUtil.isNotEmpty(module.getTable1Id())) {
                    commonReserveZoneIds.addAll(getCommonReserveZoneIdsByTableId(module.getTable1Id()));
                }
                if (StringUtil.isNotEmpty(module.getTable2Id())) {
                    commonReserveZoneIds.addAll(getCommonReserveZoneIdsByTableId(module.getTable2Id()));
                }
                if (StringUtil.isNotEmpty(module.getTable3Id())) {
                    commonReserveZoneIds.addAll(getCommonReserveZoneIdsByTableId(module.getTable3Id()));
                }
            }
        }
        Construct construct = getDao().save(entity);
        if (flag) {
            List<ComponentInputParam> componentParameterList = null;
            if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(baseComponentVersion.getComponent().getType())
                    || ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())
                    || ConstantVar.Component.Type.NO_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                componentParameterList = getService(ComponentInputParamService.class).getCommonInputParams();
            } else {
                componentParameterList = getService(ComponentInputParamService.class).getByComponentVersionId(construct.getBaseComponentVersionId());
            }
            if (CollectionUtils.isNotEmpty(componentParameterList)) {
                ConstructInputParam constructInputParam = null;
                for (ComponentInputParam componentInputParam : componentParameterList) {
                    constructInputParam = new ConstructInputParam();
                    constructInputParam.setConstructId(construct.getId());
                    constructInputParam.setInputParamId(componentInputParam.getId());
                    constructInputParam.setName(componentInputParam.getName());
                    constructInputParam.setValue(componentInputParam.getValue());
                    getService(ConstructInputParamService.class).save(constructInputParam);
                }
            }
            List<ComponentSelfParam> componentSelfParamList = getService(ComponentSelfParamService.class).getByComponentVersionId(
                    construct.getBaseComponentVersionId());
            if (CollectionUtils.isNotEmpty(componentSelfParamList)) {
                ConstructSelfParam constructSelfParam = null;
                for (ComponentSelfParam componentSelfParam : componentSelfParamList) {
                    constructSelfParam = new ConstructSelfParam();
                    constructSelfParam.setComponentVersionId(componentSelfParam.getComponentVersionId());
                    constructSelfParam.setConstructId(construct.getId());
                    constructSelfParam.setSelfParamId(componentSelfParam.getId());
                    constructSelfParam.setName(componentSelfParam.getName());
                    constructSelfParam.setOptions(componentSelfParam.getOptions());
                    constructSelfParam.setRemark(componentSelfParam.getRemark());
                    constructSelfParam.setType(componentSelfParam.getType());
                    constructSelfParam.setText(componentSelfParam.getText());
                    constructSelfParam.setValue(componentSelfParam.getValue());
                    getService(ConstructSelfParamService.class).save(constructSelfParam);
                }
            }
        }
        if (!commonReserveZoneIds.isEmpty()) {
            List<ConstructDetail> commonConstructDetails = new ArrayList<ConstructDetail>();
            for (String commonReserveZoneId : commonReserveZoneIds) {
                commonConstructDetails.addAll(getDaoFromContext(ConstructDetailDao.class).getConstructDetailsOfCommonBinding(commonReserveZoneId));
            }
            inheritCommonConstructDetail(commonConstructDetails, construct);
        }

        String url = "";
        if (ConstantVar.Component.Type.PAGE.equals(baseComponentVersion.getComponent().getType())
                || ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())
                || ConstantVar.Component.Type.SELF_DEFINE.indexOf(baseComponentVersion.getComponent().getType()) != -1) {
            // 带页面的构件
            url = baseComponentVersion.getUrl();
            if (StringUtil.isNotEmpty(url)) {
                if (url.indexOf("?") != -1) {
                    url += "&constructId=" + construct.getId();
                } else {
                    url += "?constructId=" + construct.getId();
                }
            }
        }
        assembleComponentVersion.setUrl(url);
        getDaoFromContext(ComponentVersionDao.class).save(assembleComponentVersion);
        ComponentInfoUtil.getInstance().putConstruct(construct);
        return construct;
    }

    /**
     * 根据逻辑表Code取该表对应的公用预留区的IDs
     * 
     * @param logicTableCode 逻辑表Code
     * @return Set<String>
     */
    private Set<String> getCommonReserveZoneIdsByTableId(String logicTableCode) {
        Set<String> commonReserveZoneIds = new HashSet<String>();
        if (StringUtil.isNotEmpty(logicTableCode)) {
            ComponentReserveZone commonReserveZone1 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(logicTableCode + "_FORM");
            if (commonReserveZone1 != null) {
                commonReserveZoneIds.add(commonReserveZone1.getId());
            }
            ComponentReserveZone commonReserveZone2 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(logicTableCode + "_GRID");
            if (commonReserveZone2 != null) {
                commonReserveZoneIds.add(commonReserveZone2.getId());
            }
            ComponentReserveZone commonReserveZone3 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(logicTableCode + "_GRID_LINK");
            if (commonReserveZone3 != null) {
                commonReserveZoneIds.add(commonReserveZone3.getId());
            }
        }
        return commonReserveZoneIds;
    }

    /**
     * 继承预设的公用预留区和构件的绑定关系
     * 
     * @param commonConstructDetails 继承的内容
     * @param construct 组合构件组装关系
     */
    private void inheritCommonConstructDetail(List<ConstructDetail> commonConstructDetails, Construct construct) {
        ConstructDetail distConstructDetail = null;
        if (CollectionUtils.isNotEmpty(commonConstructDetails)) {
            for (ConstructDetail constructDetail : commonConstructDetails) {
                distConstructDetail = new ConstructDetail();
                BeanUtils.copy(constructDetail, distConstructDetail);
                distConstructDetail.setId(null);
                distConstructDetail.setConstructId(construct.getId());
                getDaoFromContext(ConstructDetailDao.class).save(distConstructDetail);
                ComponentInfoUtil.getInstance().putConstructDetail(distConstructDetail);
                // 复制ConstructDetailSelfParam
                List<ConstructDetailSelfParam> selfParamList = getService(ConstructDetailSelfParamService.class)
                        .getByConstructDetailId(constructDetail.getId());
                if (CollectionUtils.isNotEmpty(selfParamList)) {
                    List<ConstructDetailSelfParam> distSelfParamList = new ArrayList<ConstructDetailSelfParam>();
                    ConstructDetailSelfParam distSelfParam = null;
                    for (ConstructDetailSelfParam selfParam : selfParamList) {
                        distSelfParam = new ConstructDetailSelfParam();
                        BeanUtils.copy(selfParam, distSelfParam);
                        distSelfParam.setId(null);
                        distSelfParam.setConstructDetailId(distConstructDetail.getId());
                        distSelfParamList.add(distSelfParam);
                    }
                    getDaoFromContext(ConstructDetailSelfParamDao.class).save(distSelfParamList);
                    ComponentParamsUtil.putConstructDetailSelfParamList(distConstructDetail.getId(), distSelfParamList);
                }
                // 复制ConstructFunction
                List<ConstructFunction> constructFunctionList = getService(ConstructFunctionService.class).getByConstructDetailId(constructDetail.getId());
                if (CollectionUtils.isNotEmpty(constructFunctionList)) {
                    ConstructFunction distConstructFunction = null;
                    for (ConstructFunction constructFunction : constructFunctionList) {
                        distConstructFunction = new ConstructFunction();
                        BeanUtils.copy(constructFunction, distConstructFunction);
                        distConstructFunction.setId(null);
                        distConstructFunction.setConstructDetailId(distConstructDetail.getId());
                        getDaoFromContext(ConstructFunctionDao.class).save(distConstructFunction);
                    }
                    ComponentParamsUtil.putParamFunctions(distConstructDetail.getId(), ComponentParamsUtil.getParamFunctions(constructDetail.getId()));
                }
                // 复制ConstructCallback
                List<ConstructCallback> constructCallbackList = getService(ConstructCallbackService.class).getByConstructDetailId(constructDetail.getId());
                if (CollectionUtils.isNotEmpty(constructCallbackList)) {
                    ConstructCallback distConstructCallback = null;
                    for (ConstructCallback constructCallback : constructCallbackList) {
                        distConstructCallback = new ConstructCallback();
                        BeanUtils.copy(constructCallback, distConstructCallback);
                        distConstructCallback.setId(null);
                        distConstructCallback.setConstructDetailId(distConstructDetail.getId());
                        getDaoFromContext(ConstructCallbackDao.class).save(distConstructCallback);
                    }
                    ComponentParamsUtil.putParamCallbacks(distConstructDetail.getId(), ComponentParamsUtil.getParamCallbacks(constructDetail.getId()));
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String id) {
        Construct construct = getByID(id);
        // 删除组合构件
        getService(ComponentVersionService.class).deleteComponentVersion(construct.getAssembleComponentVersion().getId(), false);
        getService(ConstructDetailService.class).deleteByConstructId(id);
        getService(ConstructSelfParamService.class).deleteByConstructId(id);
        getService(ConstructInputParamService.class).deleteByConstructId(id);
        getService(AuthorityConstructButtonService.class).deleteByComponentVersionId(construct.getAssembleComponentVersion().getId());
        getService(ConstructFilterService.class).deleteByTopComVersionId(construct.getAssembleComponentVersion().getId());
        getDao().deleteById(id);
        ComponentInfoUtil.getInstance().removeConstruct(construct);
    }

    /**
     * 组合构件的预览预操作
     * 
     * @param assembleComponentVersionId 组合构件ID
     */
    public void preview(String assembleComponentVersionId) {
        Set<ComponentVersion> componentVersionSet = getComponentVersionOfConstruct(assembleComponentVersionId);
        PreviewUtil.previewComponents(componentVersionSet);
    }

    /**
     * 根据组合构件版本ID获取组合构件绑定关系
     * 
     * @param assembleComponentVersionId 组合构件版本ID
     * @return Construct
     */
    public Construct getByAssembleComponentVersionId(String assembleComponentVersionId) {
        Construct construct = ComponentInfoUtil.getInstance().getConstructByAssembleId(assembleComponentVersionId);
        if (construct == null) {
            construct = getDao().getByAssembleComponentVersionId(assembleComponentVersionId);
        }
        return construct;
    }

    /**
     * 根据组合构件版本ID获取组合构件绑定关系
     * 
     * @param assembleComponentVersionIds 组合构件版本IDs
     * @return List<Construct>
     */
    public List<Construct> getByAssembleComponentVersionIds(String assembleComponentVersionIds) {
        List<Construct> constructList = new ArrayList<Construct>();
        if (StringUtil.isNotEmpty(assembleComponentVersionIds)) {
            String hql = "from Construct t where t.assembleComponentVersion.id in ('" + assembleComponentVersionIds.replace(",", "','") + "')";
            constructList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, Construct.class);
        }
        return constructList;
    }

    /**
     * 根据基础构件版本ID获取组合构件绑定关系
     * 
     * @param baseComponentVersionId 基础构件版本ID
     * @return List<Construct>
     */
    public List<Construct> getByBaseComponentVersionId(String baseComponentVersionId) {
        return getDao().getByBaseComponentVersionId(baseComponentVersionId);
    }

    /**
     * 根据构件ID获取由该构件组成的所有组合构件
     * 
     * @param componentVersionId 构件ID
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getAllAssembleComponentVersion(String componentVersionId) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        ComponentVersion assembleComponentVersion = null;
        // 作为组合构件的基础构件
        List<Construct> constructList = getByBaseComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(constructList)) {
            for (Construct constuct : constructList) {
                assembleComponentVersion = constuct.getAssembleComponentVersion();
                componentVersionSet.add(assembleComponentVersion);
                componentVersionSet.addAll(getAllAssembleComponentVersion(assembleComponentVersion.getId()));
            }
        }
        // 绑定组合构件中预留区的
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail constructDetail : constructDetailList) {
                Construct constuct = getDao().findOne(constructDetail.getConstructId());
                assembleComponentVersion = constuct.getAssembleComponentVersion();
                componentVersionSet.add(assembleComponentVersion);
                componentVersionSet.addAll(getAllAssembleComponentVersion(assembleComponentVersion.getId()));
            }
        }
        return componentVersionSet;
    }

    /**
     * 根据根菜单ID获取组合构件绑定关系
     * 
     * @param rootMenuId 根菜单ID
     * @return Set<Construct>
     */
    public Set<Construct> getConstructsByRootMenuId(String rootMenuId) {
        Set<Construct> constructSet = new HashSet<Construct>();
        List<String> constructIdList = getDao().getByRootMenuId(rootMenuId);
        if (CollectionUtils.isNotEmpty(constructIdList)) {
            for (String constructId : constructIdList) {
                constructSet.add(getDao().findOne(constructId));
                constructSet.addAll(getConstructsOfConstruct(constructId));
            }
        }
        return constructSet;
    }

    /**
     * 获取组合构件下预留区绑定的组合构件（迭代）
     * 
     * @param constructId 组合构件ID
     * @return Set<Construct>
     */
    public Set<Construct> getConstructsOfConstruct(String constructId) {
        Set<Construct> constructSet = new HashSet<Construct>();
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(constructId);
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                    continue;
                }
                ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                if (componentVersion != null && ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                    if (construct != null) {
                        constructSet.add(construct);
                        constructSet.addAll(getConstructsOfConstruct(construct.getId()));
                    }
                }
            }
        }
        return constructSet;
    }

    /**
     * 获取组合构件下预留区绑定的组合构件（迭代）
     * 
     * @param assembleComponentVersionId 组合构件ID
     * @return Set<Construct>
     */
    public Set<ComponentVersion> getAssemblesOfConstruct(String assembleComponentVersionId) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        // Construct construct = getByAssembleComponentVersionId(assembleComponentVersionId);
        Construct construct = ComponentInfoUtil.getInstance().getConstructByAssembleId(assembleComponentVersionId);
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                    continue;
                }
                ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                if (componentVersion != null && ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    componentVersionSet.add(componentVersion);
                    componentVersionSet.addAll(getAssemblesOfConstruct(componentVersion.getId()));
                }
            }
        }
        return componentVersionSet;
    }

    /**
     * 获取组合构件下预留区绑定的组合构件（不迭代）
     * 
     * @param assembleComponentVersionId 组合构件ID
     * @return Set<Construct>
     */
    public List<ComponentVersion> getAssemblesOfAssemble(String assembleComponentVersionId) {
        List<ComponentVersion> componentVersionList = new ArrayList<ComponentVersion>();
        // Construct construct = getByAssembleComponentVersionId(assembleComponentVersionId);
        Construct construct = ComponentInfoUtil.getInstance().getConstructByAssembleId(assembleComponentVersionId);
        if (null == construct)
            return componentVersionList;
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                    continue;
                }
                ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                if (componentVersion != null && ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    componentVersionList.add(componentVersion);
                }
            }
        }
        return componentVersionList;
    }

    /**
     * 根据组合构件版本ID获取其下绑定的构件版本(基础构件)
     * 
     * @param assembleComponentVersionId 组合构件版本ID
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getComponentVersionOfConstruct(String assembleComponentVersionId) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        Construct construct = getByAssembleComponentVersionId(assembleComponentVersionId);
        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
        componentVersionSet.add(baseComponentVersion);
        // 基础构件关联的公用构件
        componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(baseComponentVersion.getId()));
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            ComponentVersion componentVersion = null;
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                    continue;
                }
                componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    // 组合构件
                    componentVersionSet.addAll(getComponentVersionOfConstruct(componentVersion.getId()));
                } else {
                    componentVersionSet.add(componentVersion);
                    // 绑定构件关联的公用构件
                    componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
                }
            }
        }
        return componentVersionSet;
    }

    /**
     * 根据组合构件版本ID获取其下绑定的自定义构件版本
     * 
     * @param assembleComponentVersionId 组合构件版本ID
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getSelfDefinesOfConstruct(String assembleComponentVersionId) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        Construct construct = getByAssembleComponentVersionId(assembleComponentVersionId);
        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
        if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(baseComponentVersion.getComponent().getType()) != -1) {
            componentVersionSet.add(baseComponentVersion);
        }
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            ComponentVersion componentVersion = null;
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                    continue;
                }
                componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    // 组合构件
                    componentVersionSet.addAll(getSelfDefinesOfConstruct(componentVersion.getId()));
                } else if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1) {
                    componentVersionSet.add(componentVersion);
                }
            }
        }
        return componentVersionSet;
    }

    /**
     * 根据组合构件版本ID获取其下绑定的页面构件版本
     * 
     * @param assembleComponentVersionId 组合构件版本ID
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getPageComponentVersionsOfConstruct(String assembleComponentVersionId) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        Construct construct = getByAssembleComponentVersionId(assembleComponentVersionId);
        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
        if (ConstantVar.Component.Type.PAGE.equals(baseComponentVersion.getComponent().getType())) {
            componentVersionSet.add(baseComponentVersion);
        }
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            ComponentVersion componentVersion = null;
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                    continue;
                }
                componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    // 组合构件
                    componentVersionSet.addAll(getPageComponentVersionsOfConstruct(componentVersion.getId()));
                } else if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())) {
                    componentVersionSet.add(componentVersion);
                }
            }
        }
        return componentVersionSet;
    }

    /**
     * 获取预设的公用预留区上绑定的构件(基础构件)
     * 
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getComponentVersionOfCommonBinding() {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        List<ComponentVersion> componentVersionList = getDaoFromContext(ConstructDetailDao.class).getComponentVersionsOfCommonBinding();
        if (CollectionUtils.isNotEmpty(componentVersionList)) {
            componentVersionSet.addAll(componentVersionList);
            for (ComponentVersion componentVersion : componentVersionList) {
                if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    // 组合构件
                    componentVersionSet.addAll(getComponentVersionOfConstruct(componentVersion.getId()));
                } else {
                    // 绑定构件关联的公用构件
                    componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
                }
            }
        }
        return componentVersionSet;
    }

    /**
     * 根据组合构件版本ID获取其下绑定的构件版本(包括组合构件)
     * 
     * @param assembleComponentVersionId 组合构件版本ID
     * @param notUsedConstructDetailIds 不使用的按钮
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getComponentVersionOfConstruct(String assembleComponentVersionId, List<String> notUsedConstructDetailIds) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        Construct construct = getByAssembleComponentVersionId(assembleComponentVersionId);
        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
        componentVersionSet.add(baseComponentVersion);
        // 基础构件关联的公用构件
        componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(baseComponentVersion.getId()));
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            ComponentVersion componentVersion = null;
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isEmpty(constructDetail.getComponentVersionId())
                        || (CollectionUtils.isNotEmpty(notUsedConstructDetailIds) && notUsedConstructDetailIds.contains(constructDetail.getId()))) {
                    continue;
                }
                componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                componentVersionSet.add(componentVersion);
                if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    // 组合构件
                    componentVersionSet.addAll(getComponentVersionOfConstruct(componentVersion.getId(), notUsedConstructDetailIds));
                } else {
                    // 绑定构件关联的公用构件
                    componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
                }
            }
        }
        return componentVersionSet;
    }

    /**
     * 根据组合构件版本ID获取其下绑定的基础构件版本
     * 
     * @param assembleComponentVersionId 组合构件版本ID
     * @param usedConstructDetailIds 使用的按钮
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getUsedComponentVersionOfConstruct(String assembleComponentVersionId, List<String> usedConstructDetailIds) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        Construct construct = getByAssembleComponentVersionId(assembleComponentVersionId);
        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
        componentVersionSet.add(baseComponentVersion);
        // 基础构件关联的公用构件
        componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(baseComponentVersion.getId()));
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            ComponentVersion componentVersion = null;
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId()) && usedConstructDetailIds.contains(constructDetail.getId())) {
                    componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                    componentVersionSet.add(componentVersion);
                    if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                        // 组合构件
                        componentVersionSet.addAll(getComponentVersionOfConstruct(componentVersion.getId(), null));
                    } else {
                        // 绑定构件关联的公用构件
                        componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
                    }
                }
            }
        }
        return componentVersionSet;
    }

    /**
     * 判断组合构件中是否存在自定义构件
     * 
     * @return boolean
     */
    public boolean existSelfDefineInAssemble(String assembleComponentVersionId) {
        boolean exist = false;
        Construct construct = getByAssembleComponentVersionId(assembleComponentVersionId);
        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
        if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(baseComponentVersion.getComponent().getType()) != -1
                || ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())) {
            exist = true;
        } else {
            List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
            if (CollectionUtils.isNotEmpty(constructDetailList)) {
                ComponentVersion componentVersion = null;
                for (ConstructDetail constructDetail : constructDetailList) {
                    if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                        continue;
                    }
                    componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                    if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                        // 组合构件
                        exist = existSelfDefineInAssemble(componentVersion.getId());
                        if (exist) {
                            break;
                        }
                    } else if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1) {
                        exist = true;
                        break;
                    }
                }
            }
        }
        return exist;
    }

    /**
     * qiucs 2014-12-19 下午1:21:46
     * <p>描述: 根据组合构件ID获取基础构件ID </p>
     * 
     * @return String
     */
    public String getBaseComponentVersionId(String componentVersionId) {
        return getDao().getBaseComponentVersionId(componentVersionId);
    }

    /**
     * qiucs 2014-12-19 下午6:03:20
     * <p>描述: 判断组合构件对应的基础构件是否为树型构件 </p>
     * 
     * @return boolean
     */
    public boolean isTreeComponent(String componentVersionId) {
        Object obj = getDao().isTreeComponent(componentVersionId);
        if ("0".equals(obj.toString()))
            return false;
        return true;
    }

    /**
     * 根据逻辑表编码获取使用到该公用预留区的组合构件
     * 
     * @param logicTableCode 逻辑表编码
     * @return List<Object[]>
     */
    public List<Object[]> getAssembleComponentByLogicTableCode(String logicTableCode) {
        return getDao().getAssembleComponentByLogicTableCode(logicTableCode);
    }

    /**
     * 根据构件分类ID和逻辑表编码获取使用到该公用预留区的组合构件
     * 
     * @param assembleAreaId 构件分类ID
     * @param logicTableCode 逻辑表编码
     * @return List<Object[]>
     */
    public List<Object[]> getAssembleComponentByLogicTableCode(String assembleAreaId, String logicTableCode) {
        return getDao().getAssembleComponentByLogicTableCode(assembleAreaId, logicTableCode);
    }

    /**
     * 保存组合构件信息
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public void saveConstructInfo(Map<String, Object> assembleMap, String newAssembleAreaId, boolean useNewArea, Set<Construct> constructSetOfAssemble,
            Map<String, Map<String, String>> modifiedIdMap) {
        Map<String, Object> assembleComponentVersionMap = (Map<String, Object>) assembleMap.get("assembleComponentVersion");
        Map<String, String> assembleComponentMap = (Map<String, String>) assembleComponentVersionMap.get("Component");
        Map<String, String> baseInfo = (Map<String, String>) assembleComponentVersionMap.get("baseInfo");
        // Construct
        Map<String, String> constructMap = (Map<String, String>) assembleMap.get("construct");
        Construct construct = getByID(constructMap.get("id"));
        if (construct != null) {
            if (constructSetOfAssemble.contains(construct)) {
                return;
            } else {
                delete(construct.getId());
            }
        } else {
            construct = getByAssembleComponentVersionId(constructMap.get("assembleComponentVersionId"));
            if (construct != null) {
                if (constructSetOfAssemble.contains(construct)) {
                    return;
                } else {
                    delete(construct.getId());
                }
            }
        }
        construct = new Construct();
        construct.setId(constructMap.get("id"));
        String baseComponentVersionId = getModifiedId(modifiedIdMap, "ComponentVersion", StringUtil.null2empty(constructMap.get("baseComponentVersionId")));
        construct.setBaseComponentVersionId(baseComponentVersionId);
        constructSetOfAssemble.add(construct);
        String constructSql = "insert into t_xtpz_construct(id,component_version_id,base_component_version_id) values ('" + constructMap.get("id") + "','"
                + constructMap.get("assembleComponentVersionId") + "','" + baseComponentVersionId + "')";
        DatabaseHandlerDao.getInstance().executeSql(constructSql);
        // Component
        String componentId = assembleComponentMap.get("id");
        String componentName = assembleComponentMap.get("name");
        Component component = getService(ComponentService.class).getComponentByName(componentName);
        if (component == null) {
            String componentSql = "insert into t_xtpz_component(id,code,name,alias,type) values('" + componentId + "','" + assembleComponentMap.get("code")
                    + "','" + componentName + "','" + assembleComponentMap.get("alias") + "','" + assembleComponentMap.get("type") + "')";
            DatabaseHandlerDao.getInstance().executeSql(componentSql);
            component = new Component();
            component.setId(componentId);
            component.setName(componentName);
            component.setCode(assembleComponentMap.get("code"));
            component.setAlias(assembleComponentMap.get("alias"));
            component.setType(assembleComponentMap.get("type"));
        } else {
            component.setCode(assembleComponentMap.get("code"));
            component.setAlias(assembleComponentMap.get("alias"));
            component.setType(assembleComponentMap.get("type"));
            getDaoFromContext(ComponentDao.class).save(component);
        }
        // ComponentVersion
        ComponentVersion assembleComponentVersion = null;
        String componentVersionId = baseInfo.get("id");
        String version = baseInfo.get("version");
        String assembleAreaId = baseInfo.get("assembleAreaId");
        if (useNewArea) {
            assembleAreaId = newAssembleAreaId;
        } else {
            ComponentAssembleArea oldAssembleArea = getService(ComponentAssembleAreaService.class).getByID(assembleAreaId);
            if (oldAssembleArea == null) {
                assembleAreaId = newAssembleAreaId;
            }
        }
        ComponentVersion dbComponentVersion = getService(ComponentVersionService.class).getByComponentNameAndVersion(componentName, version);
        Date date = new Date();
        String isSystemUsed = ConstantVar.Component.SystemUsed.NO;
        if (CfgCommonUtil.isReleasedSystem()) {
            isSystemUsed = ConstantVar.Component.SystemUsed.YES;
        }
        String buttonUse = StringUtil.isEmpty(baseInfo.get("buttonUse")) ? "1" : baseInfo.get("buttonUse");
        String menuUse = StringUtil.isEmpty(baseInfo.get("menuUse")) ? "1" : baseInfo.get("menuUse");
        String areaId = StringUtil.null2empty(baseInfo.get("areaId"));
        String areaPath = ComponentInfoUtil.getInstance().getComponentAreaAllPath(areaId);
        String assembleAreaPath = ComponentInfoUtil.getInstance().getComponentAssembleAreaAllPath(assembleAreaId);
        if (dbComponentVersion == null) {
            String importDate = null;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (DatabaseHandlerDao.isOracle()) {
                importDate = "to_date('" + dateFormat.format(date) + "','yyyy-mm-dd hh24:mi:ss')";
            } else {
                importDate = "'" + dateFormat.format(date) + "'";
            }
            String componentVersionSql = "insert into t_xtpz_component_version(id,component_id,version,url,remark,area_id,assemble_area_id,path"
                    + ",import_date,views,system_param_config,is_package,is_system_used,package_time,before_click_js,button_use,menu_use,area_path,assemble_area_path) values('"
                    + componentVersionId
                    + "','"
                    + componentId
                    + "','"
                    + version
                    + "','"
                    + baseInfo.get("url")
                    + "','"
                    + StringUtil.null2empty(baseInfo.get("remark"))
                    + "','1','"
                    + assembleAreaId
                    + "','"
                    + StringUtil.null2empty(baseInfo.get("path"))
                    + "',"
                    + importDate
                    + ",'"
                    + StringUtil.null2empty(baseInfo.get("views"))
                    + "','"
                    + baseInfo.get("systemParamConfig")
                    + "','1','"
                    + isSystemUsed
                    + "','"
                    + StringUtil.null2empty(baseInfo.get("packageTime"))
                    + "','','"
                    + buttonUse
                    + "','"
                    + menuUse
                    + "','"
                    + StringUtil.null2empty(areaPath) + "','" + StringUtil.null2empty(assembleAreaPath) + "')";
            DatabaseHandlerDao.getInstance().executeSql(componentVersionSql);
            assembleComponentVersion = new ComponentVersion();
            assembleComponentVersion.setId(componentVersionId);
            assembleComponentVersion.setVersion(version);
            assembleComponentVersion.setViews(baseInfo.get("views"));
            assembleComponentVersion.setUrl(baseInfo.get("url"));
            assembleComponentVersion.setRemark(StringUtil.null2empty(baseInfo.get("remark")));
            assembleComponentVersion.setAreaId("1");
            assembleComponentVersion.setAssembleAreaId(assembleAreaId);
            assembleComponentVersion.setPath(baseInfo.get("path"));
            assembleComponentVersion.setImportDate(date);
            assembleComponentVersion.setIsPackage("1");
            assembleComponentVersion.setComponent(component);
            assembleComponentVersion.setSystemParamConfig(StringUtil.null2zero(baseInfo.get("systemParamConfig")));
            assembleComponentVersion.setIsSystemUsed("1");
            assembleComponentVersion.setPackageTime(StringUtil.null2empty(baseInfo.get("packageTime")));
            assembleComponentVersion.setBeforeClickJs("");
            assembleComponentVersion.setAreaId(areaId);
            assembleComponentVersion.setButtonUse(buttonUse);
            assembleComponentVersion.setMenuUse(menuUse);
            assembleComponentVersion.setAreaPath(areaPath);
            assembleComponentVersion.setAssembleAreaPath(assembleAreaPath);
        } else {
            assembleComponentVersion = dbComponentVersion;
            assembleComponentVersion.setViews(baseInfo.get("views"));
            assembleComponentVersion.setUrl(baseInfo.get("url"));
            assembleComponentVersion.setRemark(baseInfo.get("remark"));
            assembleComponentVersion.setAssembleAreaId(assembleAreaId);
            assembleComponentVersion.setPath(baseInfo.get("path"));
            assembleComponentVersion.setImportDate(date);
            assembleComponentVersion.setIsPackage("1");
            assembleComponentVersion.setComponent(component);
            assembleComponentVersion.setSystemParamConfig(baseInfo.get("systemParamConfig"));
            assembleComponentVersion.setIsSystemUsed(isSystemUsed);
            assembleComponentVersion.setPackageTime(baseInfo.get("packageTime"));
            assembleComponentVersion.setBeforeClickJs("");
            assembleComponentVersion.setAreaId(areaId);
            assembleComponentVersion.setButtonUse(buttonUse);
            assembleComponentVersion.setMenuUse(menuUse);
            assembleComponentVersion.setAreaPath(areaPath);
            assembleComponentVersion.setAssembleAreaPath(assembleAreaPath);
            getService(ComponentVersionService.class).save(assembleComponentVersion);
            if (!componentVersionId.equals(dbComponentVersion.getId())) {
                getModifiedIdMap(modifiedIdMap, "ComponentVersion").put(componentVersionId, dbComponentVersion.getId());
            }
        }
        construct.setAssembleComponentVersion(assembleComponentVersion);
        ComponentInfoUtil.getInstance().putConstruct(construct);
        ComponentInfoUtil.getInstance().putComponentVersion(assembleComponentVersion);
        // ConstructSelfParam
        List<Map<String, String>> constructSelfParamMapList = (List<Map<String, String>>) assembleMap.get("constructSelfParams");
        if (CollectionUtils.isNotEmpty(constructSelfParamMapList)) {
            for (Map<String, String> constructSelfParamMap : constructSelfParamMapList) {
                String options = constructSelfParamMap.get("options");
                if (DatabaseHandlerDao.isOracle()) {
                    options = parseStrByDatabaseType(constructSelfParamMap.get("options"), DatabaseHandlerDao.DB_ORACLE);
                } else {
                    options = parseStrByDatabaseType(constructSelfParamMap.get("options"), DatabaseHandlerDao.DB_SQLSERVER);
                }
                String selfParamSql = "insert into t_xtpz_construct_self_param"
                        + "(id,component_version_id,construct_id,self_param_id,name,type,value,remark,options,text) values('" + constructSelfParamMap.get("id")
                        + "','" + getModifiedId(modifiedIdMap, "ComponentVersion", constructSelfParamMap.get("componentVersionId")) + "','"
                        + constructSelfParamMap.get("constructId") + "','" + constructSelfParamMap.get("seldParamId") + "','"
                        + constructSelfParamMap.get("name") + "','" + constructSelfParamMap.get("type") + "','"
                        + StringUtil.null2empty(constructSelfParamMap.get("value")) + "','" + StringUtil.null2empty(constructSelfParamMap.get("remark"))
                        + "','" + options + "','" + StringUtil.null2empty(constructSelfParamMap.get("text")) + "')";
                DatabaseHandlerDao.getInstance().executeSql(selfParamSql);
            }
        }
        // ConstructInputParam
        List<Map<String, String>> constructInputParamMapList = (List<Map<String, String>>) assembleMap.get("constructInputParams");
        if (CollectionUtils.isNotEmpty(constructInputParamMapList)) {
            for (Map<String, String> constructInputParamMap : constructInputParamMapList) {
                String inputParamSql = "insert into t_xtpz_construct_input_param(id,construct_id,input_param_id,value,name) values('"
                        + constructInputParamMap.get("id") + "','" + constructInputParamMap.get("constructId") + "','"
                        + constructInputParamMap.get("inputParamId") + "','" + StringUtil.null2empty(constructInputParamMap.get("value")) + "','"
                        + constructInputParamMap.get("name") + "')";
                DatabaseHandlerDao.getInstance().executeSql(inputParamSql);
            }
        }
        List<Map<String, Object>> constructDetailMapList = (List<Map<String, Object>>) assembleMap.get("constructDetails");
        if (CollectionUtils.isNotEmpty(constructDetailMapList)) {
            Map<String, String> reserveZoneMap = new HashMap<String, String>();
            List<ComponentReserveZone> reserveZoneList = getService(ComponentReserveZoneService.class).getByComponentVersionId(baseComponentVersionId);
            reserveZoneList.addAll(getService(ComponentReserveZoneService.class).getAllCommonReserveZone());
            for (ComponentReserveZone reserveZone : reserveZoneList) {
                reserveZoneMap.put(reserveZone.getName(), reserveZone.getId());
            }
            reserveZoneMap.put("TREE", "TREE");
            for (Map<String, Object> constructDetailMap : constructDetailMapList) {
                saveConstructDetailInfo(newAssembleAreaId, reserveZoneMap, constructSetOfAssemble, constructDetailMap, modifiedIdMap);
            }
        }
        // ConstructFilter、ConstructFilterDetail
        List<Map<String, String>> constructFilterMapList = (List<Map<String, String>>) assembleMap.get("constructFilters");
        List<Map<String, Object>> constructFilterDetailMapList = (List<Map<String, Object>>) assembleMap.get("constructFilterDetails");
        if (CollectionUtils.isNotEmpty(constructFilterMapList)) {
            for (Map<String, String> constructFilterMap : constructFilterMapList) {
                String constructFilterSql = "insert into t_xtpz_construct_filter(id,top_com_version_id,component_version_id,table_id) values('"
                        + constructFilterMap.get("id") + "','" + getModifiedId(modifiedIdMap, "ComponentVersion", constructFilterMap.get("topComVersionId"))
                        + "','" + getModifiedId(modifiedIdMap, "ComponentVersion", constructFilterMap.get("componentVersionId")) + "','"
                        + constructFilterMap.get("tableId") + "')";
                DatabaseHandlerDao.getInstance().executeSql(constructFilterSql);
            }
        }
        if (CollectionUtils.isNotEmpty(constructFilterDetailMapList)) {
            for (Map<String, Object> constructFilterDetailMap : constructFilterDetailMapList) {
                String constructFilterDetailSql = "insert into t_xtpz_construct_filter_detail(id,construct_filter_id,column_id,operator,value,show_order,relation,left_parenthesis,right_parenthesis) values('"
                        + constructFilterDetailMap.get("id")
                        + "','"
                        + constructFilterDetailMap.get("constructFilterId")
                        + "','"
                        + constructFilterDetailMap.get("columnId")
                        + "','"
                        + constructFilterDetailMap.get("operator")
                        + "','"
                        + constructFilterDetailMap.get("value")
                        + "',"
                        + (constructFilterDetailMap.get("showOrder") != null ? ((Integer) constructFilterDetailMap.get("showOrder")).intValue() : 0)
                        + ",'"
                        + StringUtil.null2empty(constructFilterDetailMap.get("relation"))
                        + "','"
                        + StringUtil.null2empty(constructFilterDetailMap.get("leftParenthesis"))
                        + "','"
                        + StringUtil.null2empty(constructFilterDetailMap.get("rightParenthesis")) + "')";
                DatabaseHandlerDao.getInstance().executeSql(constructFilterDetailSql);
            }
        }
    }

    /**
     * 获取各种实体的修改过的IDs
     * 
     * @param modifiedIdMap 修改过的IDs
     * @param key 实体类
     * @return Map<String, String>
     */
    private Map<String, String> getModifiedIdMap(Map<String, Map<String, String>> modifiedIdMap, String key) {
        Map<String, String> map = modifiedIdMap.get(key);
        if (map == null) {
            map = new HashMap<String, String>();
            modifiedIdMap.put(key, map);
        }
        return map;
    }

    /**
     * 获取各种实体的修改过的IDs
     * 
     * @param modifiedIdMap 修改过的IDs
     * @param key 实体类
     * @param subKey id
     * @return String
     */
    private String getModifiedId(Map<String, Map<String, String>> modifiedIdMap, String key, String subKey) {
        Map<String, String> map = getModifiedIdMap(modifiedIdMap, key);
        String id = map.get(subKey);
        if (id == null) {
            id = subKey;
        }
        return id;
    }

    /**
     * 保存预留区及组装的按钮
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private void saveConstructDetailInfo(String areaId, Map<String, String> reserveZoneMap, Set<Construct> constructSetOfAssemble,
            Map<String, Object> constructDetailMap, Map<String, Map<String, String>> modifiedIdMap) {
        Map<String, String> constructDetailBaseInfo = null;
        List<Map<String, String>> constructDetailSelfParamMapList = null;
        List<Map<String, String>> constructFunctionMapList = null;
        List<Map<String, String>> constructCallbackMapList = null;
        Map<String, Object> constructDetailAssembleMap = null;
        constructDetailBaseInfo = (Map<String, String>) constructDetailMap.get("baseInfo");
        String beforeClickJs = constructDetailBaseInfo.get("beforeClickJs");
        if (DatabaseHandlerDao.isOracle()) {
            beforeClickJs = parseStrByDatabaseType(beforeClickJs, DatabaseHandlerDao.DB_ORACLE);
        } else {
            beforeClickJs = parseStrByDatabaseType(beforeClickJs, DatabaseHandlerDao.DB_SQLSERVER);
        }
        String constructDetailSql = "insert into t_xtpz_construct_detail(id,construct_id,component_version_id,"
                + "reserve_zone_id,is_common_reserve_zone,button_code,button_name,button_display_name,"
                + "button_type,parent_button_code,button_img,button_source,position,show_order,width,height,"
                + "assemble_type,tree_node_type,tree_node_property,before_click_js,search_combo_options) values('"
                + constructDetailBaseInfo.get("id")
                + "',"
                + (constructDetailBaseInfo.get("constructId") == null ? "null" : ("'" + constructDetailBaseInfo.get("constructId") + "'"))
                + ",'"
                + getModifiedId(modifiedIdMap, "ComponentVersion", StringUtil.null2empty(constructDetailBaseInfo.get("componentVersionId")))
                + "','"
                + reserveZoneMap.get(constructDetailBaseInfo.get("reserveZoneName"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("isCommonReserveZone"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonCode"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonName"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonDisplayName"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonType"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("parentButtonCode"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonImg"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonSource"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("position"))
                + "',"
                + StringUtil.null2zero(constructDetailBaseInfo.get("showOrder"))
                + ",'"
                + StringUtil.null2empty(constructDetailBaseInfo.get("width"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("height"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("assembleType"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("treeNodeType"))
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("treeNodeProperty"))
                + "','"
                + beforeClickJs
                + "','"
                + StringUtil.null2empty(constructDetailBaseInfo.get("searchComboOptions")) + "')";
        DatabaseHandlerDao.getInstance().executeSql(constructDetailSql);
        ConstructDetail constructDetail = new ConstructDetail();
        constructDetail.setId(constructDetailBaseInfo.get("id"));
        constructDetail.setConstructId(constructDetailBaseInfo.get("constructId"));
        constructDetail.setComponentVersionId(getModifiedId(modifiedIdMap, "ComponentVersion",
                StringUtil.null2empty(constructDetailBaseInfo.get("componentVersionId"))));
        constructDetail.setReserveZoneId(reserveZoneMap.get(constructDetailBaseInfo.get("reserveZoneName")));
        constructDetail.setIsCommonReserveZone(StringUtil.null2empty(constructDetailBaseInfo.get("isCommonReserveZone")));
        constructDetail.setButtonCode(StringUtil.null2empty(constructDetailBaseInfo.get("buttonCode")));
        constructDetail.setButtonName(StringUtil.null2empty(constructDetailBaseInfo.get("buttonName")));
        constructDetail.setButtonDisplayName(StringUtil.null2empty(constructDetailBaseInfo.get("buttonDisplayName")));
        constructDetail.setButtonType(StringUtil.null2empty(constructDetailBaseInfo.get("buttonType")));
        constructDetail.setParentButtonCode(StringUtil.null2empty(constructDetailBaseInfo.get("parentButtonCode")));
        constructDetail.setButtonCls(StringUtil.null2empty(constructDetailBaseInfo.get("buttonCls")));
        constructDetail.setButtonIcon(StringUtil.null2empty(constructDetailBaseInfo.get("buttonIcon")));
        constructDetail.setButtonSource(StringUtil.null2empty(constructDetailBaseInfo.get("buttonSource")));
        constructDetail.setPosition(StringUtil.null2empty(constructDetailBaseInfo.get("position")));
        constructDetail.setShowOrder(Integer.valueOf(StringUtil.null2zero(constructDetailBaseInfo.get("showOrder"))));
        constructDetail.setWidth(StringUtil.null2empty(constructDetailBaseInfo.get("width")));
        constructDetail.setHeight(StringUtil.null2empty(constructDetailBaseInfo.get("height")));
        constructDetail.setAssembleType(StringUtil.null2empty(constructDetailBaseInfo.get("assembleType")));
        constructDetail.setTreeNodeType(StringUtil.null2empty(constructDetailBaseInfo.get("treeNodeType")));
        constructDetail.setTreeNodeProperty(StringUtil.null2empty(constructDetailBaseInfo.get("treeNodeProperty")));
        constructDetail.setBeforeClickJs(beforeClickJs);
        constructDetail.setSearchComboOptions(StringUtil.null2empty(constructDetailBaseInfo.get("searchComboOptions")));
        ComponentInfoUtil.getInstance().putConstructDetail(constructDetail);
        if (StringUtil.isNotEmpty(constructDetailBaseInfo.get("componentVersionId"))) {
            constructDetailSelfParamMapList = (List<Map<String, String>>) constructDetailMap.get("constructDetailSelfParams");
            // ConstructDetailSelfParam
            if (CollectionUtils.isNotEmpty(constructDetailSelfParamMapList)) {
                for (Map<String, String> constructDetailSelfParamMap : constructDetailSelfParamMapList) {
                    String options = constructDetailSelfParamMap.get("options");
                    if (DatabaseHandlerDao.isOracle()) {
                        options = parseStrByDatabaseType(constructDetailSelfParamMap.get("options"), DatabaseHandlerDao.DB_ORACLE);
                    } else {
                        options = parseStrByDatabaseType(constructDetailSelfParamMap.get("options"), DatabaseHandlerDao.DB_SQLSERVER);
                    }
                    String selfParamSql = "insert into t_xtpz_cons_detail_self_param"
                            + "(id,component_version_id,construct_detail_id,self_param_id,name,type,value,remark,options,text) values('"
                            + constructDetailSelfParamMap.get("id") + "','"
                            + getModifiedId(modifiedIdMap, "ComponentVersion", constructDetailSelfParamMap.get("componentVersionId")) + "','"
                            + constructDetailSelfParamMap.get("constructDetailId") + "','" + constructDetailSelfParamMap.get("seldParamId") + "','"
                            + constructDetailSelfParamMap.get("name") + "','" + constructDetailSelfParamMap.get("type") + "','"
                            + StringUtil.null2empty(constructDetailSelfParamMap.get("value")) + "','"
                            + StringUtil.null2empty(constructDetailSelfParamMap.get("remark")) + "','" + options + "','"
                            + StringUtil.null2empty(constructDetailSelfParamMap.get("text")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(selfParamSql);
                }
            }
            constructFunctionMapList = (List<Map<String, String>>) constructDetailMap.get("constructFunctions");
            if (CollectionUtils.isNotEmpty(constructFunctionMapList)) {
                for (Map<String, String> constructFunctionMap : constructFunctionMapList) {
                    String cdFunctionSql = "insert into t_xtpz_construct_function(id,construct_detail_id,function_id,output_param_id,input_param_id) values('"
                            + constructFunctionMap.get("id") + "','" + constructFunctionMap.get("constructDetailId") + "','"
                            + constructFunctionMap.get("functionId") + "','" + constructFunctionMap.get("outputParamId") + "','"
                            + constructFunctionMap.get("inputParamId") + "')";
                    DatabaseHandlerDao.getInstance().executeSql(cdFunctionSql);
                }
            }
            constructCallbackMapList = (List<Map<String, String>>) constructDetailMap.get("constructCallbacks");
            if (CollectionUtils.isNotEmpty(constructCallbackMapList)) {
                for (Map<String, String> constructCallbackMap : constructCallbackMapList) {
                    String cdCallbackSql = "insert into t_xtpz_construct_callback(id,construct_detail_id,callback_id,output_param_id,input_param_id) values('"
                            + constructCallbackMap.get("id") + "','" + constructCallbackMap.get("constructDetailId") + "','"
                            + constructCallbackMap.get("callbackId") + "','" + StringUtil.null2empty(constructCallbackMap.get("outputParamId")) + "','"
                            + StringUtil.null2empty(constructCallbackMap.get("inputParamId")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(cdCallbackSql);
                }
            }
            constructDetailAssembleMap = (Map<String, Object>) constructDetailMap.get("assembleInfo");
            if (constructDetailAssembleMap != null) {
                saveConstructInfo(constructDetailAssembleMap, areaId, false, constructSetOfAssemble, modifiedIdMap);
            }
        }
    }

    /**
     * 保存公用预留区及组装的按钮
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public void saveCommonConstructDetail(Map<String, Object> assembleConfig, Map<String, Map<String, String>> modifiedIdMap) {
        List<Map<String, Object>> commonConstructDetailMapList = (List<Map<String, Object>>) assembleConfig.get("CommonConstructDetails");
        if (CollectionUtils.isNotEmpty(commonConstructDetailMapList)) {
            Map<String, Object> commonReserveZoneMap = null;
            List<Map<String, Object>> constructDetailMapList = null;
            ComponentReserveZone dbCommonReserveZone = null;
            String reserveZoneId = null;
            for (Map<String, Object> commonConstructDetailMap : commonConstructDetailMapList) {
                commonReserveZoneMap = (Map<String, Object>) commonConstructDetailMap.get("commonReserveZone");
                reserveZoneId = StringUtil.null2empty(commonReserveZoneMap.get("id"));
                dbCommonReserveZone = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(
                        StringUtil.null2empty(commonReserveZoneMap.get("name")));
                if (dbCommonReserveZone != null) {
                    reserveZoneId = dbCommonReserveZone.getId();
                    if (!dbCommonReserveZone.getAlias().equals(commonReserveZoneMap.get("alias"))) {
                        dbCommonReserveZone.setAlias(StringUtil.null2empty(commonReserveZoneMap.get("alias")));
                        getService(ComponentReserveZoneService.class).save(dbCommonReserveZone);
                    }
                } else {
                    String reserveZoneSql = "insert into t_xtpz_component_reserve_zone(id,component_version_id,name,alias,type,page,is_common,show_order) values('"
                            + reserveZoneId
                            + "','"
                            + getModifiedId(modifiedIdMap, "ComponentVersion", StringUtil.null2empty(commonReserveZoneMap.get("componentVersionId")))
                            + "','"
                            + commonReserveZoneMap.get("name")
                            + "','"
                            + commonReserveZoneMap.get("alias")
                            + "','"
                            + commonReserveZoneMap.get("type")
                            + "','"
                            + commonReserveZoneMap.get("page")
                            + "','"
                            + ((Boolean) commonReserveZoneMap.get("isCommon") ? "1" : "0")
                            + "',"
                            + commonReserveZoneMap.get("showOrder") + ")";
                    DatabaseHandlerDao.getInstance().executeSql(reserveZoneSql);
                }
                constructDetailMapList = (List<Map<String, Object>>) commonConstructDetailMap.get("constructDetails");
                if (CollectionUtils.isNotEmpty(constructDetailMapList)) {
                    Map<String, String> constructDetailBaseInfo = null;
                    List<Map<String, String>> constructDetailSelfParamMapList = null;
                    List<Map<String, String>> constructFunctionMapList = null;
                    List<Map<String, String>> constructCallbackMapList = null;
                    ConstructDetail dbConstructDetail = null;
                    ConstructDetail tempDbConstructDetail = null;
                    List<ConstructDetail> dbConstructDetailList = getService(ConstructDetailService.class).getByReserveZoneIdOfCommonBinding(reserveZoneId);
                    for (Map<String, Object> constructDetailMap : constructDetailMapList) {
                        constructDetailBaseInfo = (Map<String, String>) constructDetailMap.get("baseInfo");
                        for (Iterator<ConstructDetail> it = dbConstructDetailList.iterator(); it.hasNext();) {
                            tempDbConstructDetail = it.next();
                            if (tempDbConstructDetail.getButtonCode().endsWith(constructDetailBaseInfo.get("buttonCode"))) {
                                dbConstructDetail = tempDbConstructDetail;
                                it.remove();
                                break;
                            }
                        }
                        if (dbConstructDetail != null) {
                            getService(ConstructDetailService.class).delete(dbConstructDetail.getId());
                        }
                        String beforeClickJs = constructDetailBaseInfo.get("beforeClickJs");
                        if (DatabaseHandlerDao.isOracle()) {
                            beforeClickJs = parseStrByDatabaseType(beforeClickJs, DatabaseHandlerDao.DB_ORACLE);
                        } else {
                            beforeClickJs = parseStrByDatabaseType(beforeClickJs, DatabaseHandlerDao.DB_SQLSERVER);
                        }
                        String constructDetailSql = "insert into t_xtpz_construct_detail(id,construct_id,component_version_id,"
                                + "reserve_zone_id,is_common_reserve_zone,button_code,button_name,button_display_name,"
                                + "button_type,parent_button_code,button_img,button_source,position,show_order,width,height,"
                                + "assemble_type,tree_node_type,tree_node_property,before_click_js,search_combo_options) values('"
                                + constructDetailBaseInfo.get("id")
                                + "',"
                                + (constructDetailBaseInfo.get("constructId") == null ? "null" : ("'" + constructDetailBaseInfo.get("constructId") + "'"))
                                + ",'"
                                + getModifiedId(modifiedIdMap, "ComponentVersion",
                                        StringUtil.null2empty(StringUtil.null2empty(constructDetailBaseInfo.get("componentVersionId"))))
                                + "','"
                                + reserveZoneId
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("isCommonReserveZone"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonCode"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonName"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonDisplayName"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonType"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("parentButtonCode"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonImg"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("buttonSource"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("position"))
                                + "',"
                                + StringUtil.null2zero(constructDetailBaseInfo.get("showOrder"))
                                + ",'"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("width"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("height"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("assembleType"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("treeNodeType"))
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("treeNodeProperty"))
                                + "','"
                                + beforeClickJs
                                + "','"
                                + StringUtil.null2empty(constructDetailBaseInfo.get("searchComboOptions")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(constructDetailSql);
                        if (StringUtil.isNotEmpty(constructDetailBaseInfo.get("componentVersionId"))) {
                            constructDetailSelfParamMapList = (List<Map<String, String>>) constructDetailMap.get("constructDetailSelfParams");
                            // ConstructDetailSelfParam
                            if (CollectionUtils.isNotEmpty(constructDetailSelfParamMapList)) {
                                for (Map<String, String> constructDetailSelfParamMap : constructDetailSelfParamMapList) {
                                    String options = constructDetailSelfParamMap.get("options");
                                    if (DatabaseHandlerDao.isOracle()) {
                                        options = parseStrByDatabaseType(constructDetailSelfParamMap.get("options"), DatabaseHandlerDao.DB_ORACLE);
                                    } else {
                                        options = parseStrByDatabaseType(constructDetailSelfParamMap.get("options"), DatabaseHandlerDao.DB_SQLSERVER);
                                    }
                                    String selfParamSql = "insert into t_xtpz_cons_detail_self_param"
                                            + "(id,component_version_id,construct_detail_id,self_param_id,name,type,value,remark,options,text) values('"
                                            + constructDetailSelfParamMap.get("id") + "','"
                                            + getModifiedId(modifiedIdMap, "ComponentVersion", constructDetailSelfParamMap.get("componentVersionId")) + "','"
                                            + constructDetailSelfParamMap.get("constructDetailId") + "','" + constructDetailSelfParamMap.get("seldParamId")
                                            + "','" + constructDetailSelfParamMap.get("name") + "','" + constructDetailSelfParamMap.get("type") + "','"
                                            + StringUtil.null2empty(constructDetailSelfParamMap.get("value")) + "','"
                                            + StringUtil.null2empty(constructDetailSelfParamMap.get("remark")) + "','" + options + "','"
                                            + StringUtil.null2empty(constructDetailSelfParamMap.get("text")) + "')";
                                    DatabaseHandlerDao.getInstance().executeSql(selfParamSql);
                                }
                            }
                            constructFunctionMapList = (List<Map<String, String>>) constructDetailMap.get("constructFunctions");
                            if (CollectionUtils.isNotEmpty(constructFunctionMapList)) {
                                for (Map<String, String> constructFunctionMap : constructFunctionMapList) {
                                    String cdFunctionSql = "insert into t_xtpz_construct_function(id,construct_detail_id,function_id,output_param_id,input_param_id) values('"
                                            + constructFunctionMap.get("id")
                                            + "','"
                                            + constructFunctionMap.get("constructDetailId")
                                            + "','"
                                            + constructFunctionMap.get("functionId")
                                            + "','"
                                            + constructFunctionMap.get("outputParamId")
                                            + "','"
                                            + constructFunctionMap.get("inputParamId") + "')";
                                    DatabaseHandlerDao.getInstance().executeSql(cdFunctionSql);
                                }
                            }
                            constructCallbackMapList = (List<Map<String, String>>) constructDetailMap.get("constructCallbacks");
                            if (CollectionUtils.isNotEmpty(constructCallbackMapList)) {
                                for (Map<String, String> constructCallbackMap : constructCallbackMapList) {
                                    String cdCallbackSql = "insert into t_xtpz_construct_callback(id,construct_detail_id,callback_id,output_param_id,input_param_id) values('"
                                            + constructCallbackMap.get("id")
                                            + "','"
                                            + constructCallbackMap.get("constructDetailId")
                                            + "','"
                                            + constructCallbackMap.get("callbackId")
                                            + "','"
                                            + StringUtil.null2empty(constructCallbackMap.get("outputParamId"))
                                            + "','" + StringUtil.null2empty(constructCallbackMap.get("inputParamId")) + "')";
                                    DatabaseHandlerDao.getInstance().executeSql(cdCallbackSql);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据数据库类型转换字符串
     * 
     * @param source 原字符串
     * @param databaseType 数据类型
     * @return 转换后的Str
     */
    private String parseStrByDatabaseType(String source, String databaseType) {
        String str = StringUtil.null2empty(source);
        if (DatabaseHandlerDao.DB_ORACLE.equals(databaseType)) {
            return str.replaceAll("'", "''").replaceAll("&", "'||chr(38)||'").replaceAll("\n", "'\n||chr(10)||'");
        } else {
            return str.replaceAll("'", "''");
        }
    }

    /**
     * 获取所有的组合构件绑定关系
     * 
     * @return List<Construct>
     */
    public List<Construct> getAll() {
        List<Construct> constructList = new ArrayList<Construct>();
        List<Object[]> list = getDao().getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            Construct construct = null;
            for (Object[] objs : list) {
                construct = new Construct();
                construct.setId(String.valueOf(objs[0]));
                construct.setBaseComponentVersionId(String.valueOf(objs[2]));
                String assembleComponentVersionId = String.valueOf(objs[1]);
                ComponentVersion cv = ComponentInfoUtil.getInstance().getComponentVersion(assembleComponentVersionId);
                construct.setAssembleComponentVersion(cv);
                constructList.add(construct);
            }
        }
        return constructList;
    }
}

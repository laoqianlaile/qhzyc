package com.ces.config.dhtmlx.service.construct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentVersionDao;
import com.ces.config.dhtmlx.dao.construct.ConstructCallbackDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDetailDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDetailSelfParamDao;
import com.ces.config.dhtmlx.dao.construct.ConstructFunctionDao;
import com.ces.config.dhtmlx.dao.construct.ConstructInputParamDao;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
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
import com.ces.config.dhtmlx.service.authority.AuthorityConstructButtonService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentInputParamService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentSelfParamService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.AppDefineUtil.ButtonUI;
import com.ces.config.utils.ComponentInfoUtil;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.SystemParameterUtil;
import com.ces.utils.BeanUtils;

/**
 * 组合构件中构件和预留区绑定关系Service
 * 
 * @author wanglei
 * @date 2013-09-27
 */
@Component("constructDetailService")
public class ConstructDetailService extends ConfigDefineDaoService<ConstructDetail, ConstructDetailDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("constructDetailDao")
    @Override
    protected void setDaoUnBinding(ConstructDetailDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据组合构件绑定关系ID获取预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getConstructInfoByConstructId(String constructId) {
        List<Object[]> constructDetails = getDao().getConstructInfoByConstructId(constructId);
        List<ConstructDetail> constructDetailList = new ArrayList<ConstructDetail>();
        ConstructDetail constructDetail = null;
        if (CollectionUtils.isNotEmpty(constructDetails)) {
            for (Object[] o : constructDetails) {
                constructDetail = new ConstructDetail();
                constructDetail.setId(String.valueOf(o[0]));
                constructDetail.setButtonCode(String.valueOf(o[1]));
                constructDetail.setButtonDisplayName(String.valueOf(o[2]));
                constructDetail.setReserveZoneAlias(String.valueOf(o[3]));
                if (StringUtil.isNotEmpty(o[4])) {
                    constructDetail.setComponentAliasAndVersion(String.valueOf(o[4]) + "_" + String.valueOf(o[5]));
                }
                constructDetail.setComponentVersionId(String.valueOf(o[6]));
                constructDetail.setReserveZoneId(String.valueOf(o[7]));
                constructDetail.setButtonType(String.valueOf(o[8]));
                if (StringUtil.isNotEmpty(o[9])) {
                    constructDetail.setParentButtonCode(String.valueOf(o[9]));
                }
                constructDetail.setButtonSource(String.valueOf(o[10]));
                constructDetail.setTreeNodeProperty(String.valueOf(o[11]));
                constructDetail.setPosition(String.valueOf(o[12]));
                constructDetailList.add(constructDetail);
            }
        }
        return constructDetailList;
    }

    /**
     * 根据组合构件绑定关系ID和预留区ID获取预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param treeNodeType 树节点类型
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getConstructInfos(String constructId, String reserveZoneId, String treeNodeType) {
        List<Object[]> constructDetails = null;
        if (StringUtil.isNotEmpty(constructId)) {
            if ("TREE".equals(reserveZoneId)) {
                if ("TREE".equals(treeNodeType)) {
                    constructDetails = getDao().getCommonConstructInfosOfTree(constructId);
                } else {
                    constructDetails = getDao().getConstructInfosOfTree(constructId, treeNodeType);
                }
            } else {
                constructDetails = getDao().getConstructInfos(constructId, reserveZoneId);
            }
        } else {
            constructDetails = getDao().getConstructInfosOfCommonBinding(reserveZoneId);
        }
        List<ConstructDetail> constructDetailList = new ArrayList<ConstructDetail>();
        ConstructDetail constructDetail = null;
        List<ConstructDetail> sortConstructDetailList = new ArrayList<ConstructDetail>();
        if (CollectionUtils.isNotEmpty(constructDetails)) {
            List<ConstructDetail> parentButtonCodes = getParentButtonCodes(constructId);
            Map<String, String> parentButtonMap = new HashMap<String, String>();
            parentButtonMap.put("more", "更多");
            if (CollectionUtils.isNotEmpty(parentButtonCodes)) {
                for (ConstructDetail cd : parentButtonCodes) {
                    parentButtonMap.put(cd.getButtonCode(), cd.getButtonDisplayName());
                }
            }
            for (Object[] o : constructDetails) {
                constructDetail = new ConstructDetail();
                constructDetail.setId(String.valueOf(o[0]));
                constructDetail.setButtonCode(String.valueOf(o[1]));
                constructDetail.setButtonDisplayName(String.valueOf(o[2]));
                constructDetail.setReserveZoneAlias(String.valueOf(o[3]));
                if (StringUtil.isNotEmpty(o[4])) {
                    constructDetail.setComponentAliasAndVersion(String.valueOf(o[4]) + "_" + String.valueOf(o[5]));
                }
                constructDetail.setComponentVersionId(String.valueOf(o[6]));
                constructDetail.setReserveZoneId(String.valueOf(o[7]));
                constructDetail.setButtonType(String.valueOf(o[8]));
                if (StringUtil.isNotEmpty(o[9])) {
                    constructDetail.setParentButtonCode(String.valueOf(o[9]));
                }
                constructDetail.setButtonSource(String.valueOf(o[10]));
                constructDetail.setTreeNodeProperty(String.valueOf(o[11]));
                constructDetail.setShowOrder(Integer.valueOf(StringUtil.null2zero(o[12])));
                constructDetail.setTreeNodeType(String.valueOf(o[13]));
                constructDetail.setPosition(String.valueOf(o[14]));
                constructDetailList.add(constructDetail);
            }
            sortConstructDetailList = sortConstructDetailList(constructDetailList);
            for (ConstructDetail cd : sortConstructDetailList) {
                if ("2".equals(cd.getButtonType())) {
                    cd.setParentButtonCode(parentButtonMap.get(cd.getParentButtonCode()));
                }
            }
        }
        return sortConstructDetailList;
    }

    /**
     * 构建列表上的显示顺序
     * 
     * @param constructDetailList
     * @return List<ConstructDetail>
     */
    private List<ConstructDetail> sortConstructDetailList(List<ConstructDetail> constructDetailList) {
        List<ConstructDetail> oneLevelOrGroupButtonList = new ArrayList<ConstructDetail>();
        Map<String, List<ConstructDetail>> twoLevelButtonMap = new HashMap<String, List<ConstructDetail>>();
        List<ConstructDetail> twoLevelOrGroupButtonList = null;
        for (ConstructDetail cd : constructDetailList) {
            if ("2".equals(cd.getButtonType())) {
                twoLevelOrGroupButtonList = twoLevelButtonMap.get(cd.getParentButtonCode());
                if (CollectionUtils.isEmpty(twoLevelOrGroupButtonList)) {
                    twoLevelOrGroupButtonList = new ArrayList<ConstructDetail>();
                    twoLevelButtonMap.put(cd.getParentButtonCode(), twoLevelOrGroupButtonList);
                } else {
                    twoLevelOrGroupButtonList = twoLevelButtonMap.get(cd.getParentButtonCode());
                }
                twoLevelOrGroupButtonList.add(cd);
            } else {
                oneLevelOrGroupButtonList.add(cd);
            }
        }
        List<ConstructDetail> buttonList = new ArrayList<ConstructDetail>();
        Collections.sort(oneLevelOrGroupButtonList);
        for (ConstructDetail cd : oneLevelOrGroupButtonList) {
            buttonList.add(cd);
            if ("1".equals(cd.getButtonType())) {
                twoLevelOrGroupButtonList = twoLevelButtonMap.get(cd.getButtonCode());
                if (CollectionUtils.isNotEmpty(twoLevelOrGroupButtonList)) {
                    Collections.sort(twoLevelOrGroupButtonList);
                    buttonList.addAll(twoLevelOrGroupButtonList);
                }
            }
        }
        return buttonList;
    }

    /**
     * 获取预留区和构件绑定关系列表
     * 
     * @param ids 预留区和构件绑定关系IDs
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByIds(String ids) {
        List<ConstructDetail> constructDetailList = new ArrayList<ConstructDetail>();
        if (StringUtil.isNotEmpty(ids)) {
            String hql = "from ConstructDetail t where t.id in ('" + ids.replace(",", "','") + "')";
            constructDetailList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ConstructDetail.class);
        }
        return constructDetailList;
    }

    /**
     * 根据组合构件绑定关系ID和预留区类型获取预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @param isCommonReserveZone 是否使用公共预留区
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getConstructDetails(String constructId, String isCommonReserveZone) {
        return getDao().getConstructDetails(constructId, isCommonReserveZone);
    }

    /**
     * 根据组合构件绑定关系ID获取预留区和构件绑定关系列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByConstructId(String constructId) {
        // return getDao().getByConstructId(constructId);
        List<ConstructDetail> constructDetailList = new ArrayList<ConstructDetail>();
        constructDetailList.addAll(ComponentInfoUtil.getInstance().getConstructDetails(constructId));
        return constructDetailList;
    }

    /**
     * 获取公用预留区和构件绑定关系列表
     * 
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getOfCommonBinding() {
        return getDao().getOfCommonBinding();
    }

    /**
     * 根据预留区ID获取预留区和构件绑定关系列表
     * 
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByReserveZoneId(String reserveZoneId) {
        return getDao().getByReserveZoneId(reserveZoneId);
    }

    /**
     * 根据预留区ID获取预留区和构件绑定关系列表
     * 
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByReserveZoneIdOfCommonBinding(String reserveZoneId) {
        return getDao().getByReserveZoneIdOfCommonBinding(reserveZoneId);
    }

    /**
     * 根据构件版本ID获取预留区和构件绑定关系列表
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 根据根菜单ID获取预留区和页面构件绑定关系列表
     * 
     * @param rootMenuId 根菜单ID
     * @return List<ConstructDetail>
     */
    public Set<ConstructDetail> getByRootMenuId(String rootMenuId) {
        Set<ConstructDetail> constructDetailSet = new HashSet<ConstructDetail>();
        List<String> constructDetailIdList = getDao().getByRootMenuId(rootMenuId);
        if (CollectionUtils.isNotEmpty(constructDetailIdList)) {
            for (String constructDetailId : constructDetailIdList) {
                ConstructDetail constructDetail = getDao().findOne(constructDetailId);
                constructDetailSet.add(constructDetail);
                constructDetailSet.addAll(getConstructDetailsOfConstructDetail(constructDetail));
            }
        }
        return constructDetailSet;
    }

    /**
     * 根据菜单IDs获取预留区和页面构件绑定关系列表
     * 
     * @param menuIds 菜单IDs
     * @return List<ConstructDetail>
     */
    @SuppressWarnings("unchecked")
    public Set<ConstructDetail> getByMenuIds(String menuIds) {
        Set<ConstructDetail> constructDetailSet = new HashSet<ConstructDetail>();
        List<String> constructDetailIdList = null;
        if (StringUtil.isNotEmpty(menuIds)) {
            DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
            String sql = "select cd.id from t_xtpz_menu m, t_xtpz_component_version cv, t_xtpz_component c, t_xtpz_construct co, t_xtpz_construct_detail cd"
                    + " where m.component_version_id=cv.id and cv.component_id=c.id and cv.id=co.component_version_id and co.id=cd.construct_id and c.type='9' and m.id in('"
                    + menuIds.replace(",", "','") + "')";
            constructDetailIdList = dao.queryForList(sql);
        }
        if (CollectionUtils.isNotEmpty(constructDetailIdList)) {
            StringBuilder cdIds = new StringBuilder();
            for (String constructDetailId : constructDetailIdList) {
                cdIds.append(constructDetailId).append(",");
            }
            if (cdIds.length() > 0) {
                cdIds.deleteCharAt(cdIds.length() - 1);
            }
            List<ConstructDetail> cdList = getByIds(cdIds.toString());
            if (CollectionUtils.isNotEmpty(cdList)) {
                constructDetailSet.addAll(cdList);
                for (ConstructDetail cd : cdList) {
                    constructDetailSet.addAll(getConstructDetailsOfConstructDetail(cd));
                }
            }
        }
        return constructDetailSet;
    }

    /**
     * 获取组合构件中构件和预留区绑定关系下的其他绑定关系（迭代）
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return Set<Construct>
     */
    private Set<ConstructDetail> getConstructDetailsOfConstructDetail(ConstructDetail constructDetail) {
        Set<ConstructDetail> constructDetailSet = new HashSet<ConstructDetail>();
        if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
            if (componentVersion != null && ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                if (construct != null) {
                    List<ConstructDetail> constructDetailList = getByConstructId(construct.getId());
                    if (CollectionUtils.isNotEmpty(constructDetailList)) {
                        for (ConstructDetail constructDetail1 : constructDetailList) {
                            constructDetailSet.add(constructDetail1);
                            constructDetailSet.addAll(getConstructDetailsOfConstructDetail(constructDetail1));
                        }
                    }
                }
            }
        }
        return constructDetailSet;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ConstructDetail save(ConstructDetail entity) {
        // 是否需要添加ConstructDetailSelfParam，新增时需要，修改时如果修改了componentVersionId也需要
        boolean flag = true;
        if (StringUtil.isEmpty(entity.getComponentVersionId())) {
            flag = false;
        } else if (StringUtil.isNotEmpty(entity.getId())) {
            ConstructDetail oldEntity = getDao().findOne(entity.getId());
            if (oldEntity != null) {
                if (!oldEntity.getComponentVersionId().equals(entity.getComponentVersionId())) {
                    getService(ConstructDetailSelfParamService.class).deleteByConstructDetailId(oldEntity.getId());
                    getService(ConstructFunctionService.class).deleteConstructFunctions(oldEntity.getId());
                    getService(ConstructCallbackService.class).deleteConstructCallbacks(oldEntity.getId());
                    if (StringUtil.isNotEmpty(entity.getConstructId())) {
                        Construct construct = getService(ConstructService.class).getByID(entity.getConstructId());
                        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                        if (ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())) {
                            getDaoFromContext(ConstructInputParamDao.class).deleteConstructInputParams(entity.getConstructId(), entity.getButtonCode() + "%");
                        }
                    }
                } else {
                    flag = false;
                }
                if (!oldEntity.getReserveZoneId().equals(entity.getReserveZoneId())) {
                    entity.setShowOrder(getMaxShowOrder(entity.getConstructId(), entity.getReserveZoneId(), entity.getTreeNodeType(),
                            entity.getParentButtonCode()) + 1);
                }
                if (!StringUtil.null2empty(oldEntity.getParentButtonCode()).equals(StringUtil.null2empty(entity.getParentButtonCode()))) {
                    entity.setShowOrder(getMaxShowOrder(entity.getConstructId(), entity.getReserveZoneId(), entity.getTreeNodeType(),
                            entity.getParentButtonCode()) + 1);
                }
                if (!entity.getButtonCode().equals(oldEntity.getButtonCode()) && StringUtil.isNotEmpty(entity.getConstructId())) {
                    Construct construct = getService(ConstructService.class).getByID(entity.getConstructId());
                    ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                    if (ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())) {
                        List<ConstructInputParam> constructInputParamList = getService(ConstructInputParamService.class).getByConstructId(
                                entity.getConstructId());
                        if (CollectionUtils.isNotEmpty(constructInputParamList)) {
                            for (ConstructInputParam constructInputParam : constructInputParamList) {
                                if (constructInputParam.getName().startsWith(oldEntity.getButtonCode())) {
                                    constructInputParam.setName(constructInputParam.getName().replace(oldEntity.getButtonCode() + "--",
                                            entity.getButtonCode() + "--"));
                                    getService(ConstructInputParamService.class).save(constructInputParam);
                                }
                            }
                        }
                    }
                }
                entity.setBeforeClickJs(oldEntity.getBeforeClickJs());
            }
        }
        if (StringUtil.isEmpty(entity.getAssembleType())) {
            entity.setAssembleType("0");
        }
        // 新增时加载绑定构件的beforeClickJs到constructDetail中
        if (StringUtil.isEmpty(entity.getId()) && flag) {
            ComponentVersion bindingComponentVersion = getService(ComponentVersionService.class).getByID(entity.getComponentVersionId());
            if (bindingComponentVersion != null) {
                if (ConstantVar.Component.Type.ASSEMBLY.equals(bindingComponentVersion.getComponent().getType())) {
                    String baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(bindingComponentVersion.getId());
                    ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(baseComponentVersionId);
                    if (baseComponentVersion != null && StringUtil.isNotEmpty(baseComponentVersion.getBeforeClickJs())) {
                        entity.setBeforeClickJs(baseComponentVersion.getBeforeClickJs());
                    }
                } else {
                    if (StringUtil.isNotEmpty(bindingComponentVersion.getBeforeClickJs())) {
                        entity.setBeforeClickJs(bindingComponentVersion.getBeforeClickJs());
                    }
                }
            }
        }
        ConstructDetail constructDetail = getDao().save(entity);
        if (flag) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
            String componentVersionId;
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct bindingConstruct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                componentVersionId = bindingConstruct.getBaseComponentVersionId();
            } else {
                componentVersionId = componentVersion.getId();
            }
            List<ComponentSelfParam> componentSelfParamList = getService(ComponentSelfParamService.class).getByComponentVersionId(componentVersionId);
            if (CollectionUtils.isNotEmpty(componentSelfParamList)) {
                ConstructDetailSelfParam constructDetailSelfParam = null;
                for (ComponentSelfParam componentSelfParam : componentSelfParamList) {
                    constructDetailSelfParam = new ConstructDetailSelfParam();
                    constructDetailSelfParam.setComponentVersionId(componentSelfParam.getComponentVersionId());
                    constructDetailSelfParam.setConstructDetailId(constructDetail.getId());
                    constructDetailSelfParam.setSelfParamId(componentSelfParam.getId());
                    constructDetailSelfParam.setName(componentSelfParam.getName());
                    constructDetailSelfParam.setOptions(componentSelfParam.getOptions());
                    constructDetailSelfParam.setRemark(componentSelfParam.getRemark());
                    constructDetailSelfParam.setType(componentSelfParam.getType());
                    constructDetailSelfParam.setText(componentSelfParam.getText());
                    constructDetailSelfParam.setValue(componentSelfParam.getValue());
                    getService(ConstructDetailSelfParamService.class).save(constructDetailSelfParam);
                }
            }
            if (StringUtil.isNotEmpty(entity.getConstructId())) {
                Construct construct = getService(ConstructService.class).getByID(entity.getConstructId());
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                if (ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())) {
                    List<ComponentInputParam> componentInputParamList = getService(ComponentInputParamService.class)
                            .getByComponentVersionId(componentVersionId);
                    ComponentVersion baseCV = getService(ComponentVersionService.class).getByID(componentVersionId);
                    if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(baseCV.getComponent().getType()) != -1) {
                        componentInputParamList.addAll(getService(ComponentInputParamService.class).getCommonInputParams());
                    }
                    if (CollectionUtils.isNotEmpty(componentInputParamList)) {
                        ConstructInputParam constructInputParam = null;
                        for (ComponentInputParam componentInputParam : componentInputParamList) {
                            constructInputParam = new ConstructInputParam();
                            constructInputParam.setConstructId(construct.getId());
                            constructInputParam.setInputParamId(componentInputParam.getId());
                            constructInputParam.setName(entity.getButtonCode() + "--" + componentInputParam.getName());
                            constructInputParam.setValue(componentInputParam.getValue());
                            getService(ConstructInputParamService.class).save(constructInputParam);
                        }
                    }
                }
            }
        }
        ComponentInfoUtil.getInstance().putConstructDetail(constructDetail);
        ComponentInfoUtil.getInstance().putAppConstructDetails(constructDetail);
        return constructDetail;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        Assert.notNull(ids, "要删除的实体ID不能为空");
        String[] idArr = ids.split(",");
        for (int i = 0; i < idArr.length; i++) {
            ConstructDetail constructDetail = getByID(idArr[i]);
            if (constructDetail != null) {
                // 如果是下拉按钮，同时删除下拉子按钮
                if ("1".equals(constructDetail.getButtonType())) {
                    deleteByParentButtonCode(constructDetail.getButtonCode(), constructDetail.getConstructId());
                }
                getService(ConstructDetailSelfParamService.class).deleteByConstructDetailId(idArr[i]);
                getService(ConstructFunctionService.class).deleteConstructFunctions(idArr[i]);
                getService(ConstructCallbackService.class).deleteConstructCallbacks(idArr[i]);
                getService(AuthorityConstructButtonService.class).deleteByConstructDetailId(idArr[i]);
                if (StringUtil.isNotEmpty(constructDetail.getConstructId())) {
                    Construct construct = getService(ConstructService.class).getByID(constructDetail.getConstructId());
                    ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                    if (ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())) {
                        getDaoFromContext(ConstructInputParamDao.class).deleteConstructInputParams(constructDetail.getConstructId(),
                                constructDetail.getButtonCode() + "%");
                    }
                }
                getDao().deleteById(idArr[i]);
                ComponentInfoUtil.getInstance().removeConstructDetail(constructDetail);
                ComponentInfoUtil.getInstance().removeAppConstructDetails(constructDetail);
            }
        }
    }

    /**
     * 删除下拉按钮的子按钮
     * 
     * @param parentButtonCode 所属按钮组名称
     * @param constructId 组合构件绑定关系ID
     */
    @Transactional
    public void deleteByParentButtonCode(String parentButtonCode, String constructId) {
        List<ConstructDetail> constructDetailList = getDao().getByParentButtonCodeAndConstructId(parentButtonCode, constructId);
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail constructDetail : constructDetailList) {
                if (constructDetail != null) {
                    getService(ConstructDetailSelfParamService.class).deleteByConstructDetailId(constructDetail.getId());
                    getService(ConstructFunctionService.class).deleteConstructFunctions(constructDetail.getId());
                    getService(ConstructCallbackService.class).deleteConstructCallbacks(constructDetail.getId());
                    getService(AuthorityConstructButtonService.class).deleteByConstructDetailId(constructDetail.getId());
                    if (StringUtil.isNotEmpty(constructDetail.getConstructId())) {
                        Construct construct = getService(ConstructService.class).getByID(constructDetail.getConstructId());
                        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                        if (ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())) {
                            getDaoFromContext(ConstructInputParamDao.class).deleteConstructInputParams(constructDetail.getConstructId(),
                                    constructDetail.getButtonCode() + "%");
                        }
                    }
                    getDao().deleteById(constructDetail.getId());
                    ComponentInfoUtil.getInstance().removeConstructDetail(constructDetail);
                }
            }
        }
    }

    /**
     * 删除预留区和构件绑定关系
     * 
     * @param constructId 组合构件绑定关系ID
     */
    @Transactional
    public void deleteByConstructId(String constructId) {
        Assert.notNull(constructId, "constructId不能为空");
        List<ConstructDetail> constructDetailList = getByConstructId(constructId);
        for (ConstructDetail constructDetail : constructDetailList) {
            getService(ConstructDetailSelfParamService.class).deleteByConstructDetailId(constructDetail.getId());
            getService(ConstructFunctionService.class).deleteConstructFunctions(constructDetail.getId());
            getService(ConstructCallbackService.class).deleteConstructCallbacks(constructDetail.getId());
            getDao().deleteById(constructDetail.getId());
            ComponentInfoUtil.getInstance().removeConstructDetail(constructId);
        }
    }

    /**
     * 删除预留区和构件绑定关系
     * 
     * @param constructId 组合构件绑定关系ID
     * @param isCommonReserveZone 是否使用公共预留区
     */
    @Transactional
    public void deleteConstructDetails(String constructId, String isCommonReserveZone) {
        Assert.notNull(constructId, "constructId不能为空");
        List<ConstructDetail> constructDetailList = getConstructDetails(constructId, isCommonReserveZone);
        for (ConstructDetail constructDetail : constructDetailList) {
            getService(ConstructDetailSelfParamService.class).deleteByConstructDetailId(constructDetail.getId());
            getService(ConstructFunctionService.class).deleteConstructFunctions(constructDetail.getId());
            getService(ConstructCallbackService.class).deleteConstructCallbacks(constructDetail.getId());
            if (StringUtil.isNotEmpty(constructDetail.getConstructId())) {
                Construct construct = getService(ConstructService.class).getByID(constructDetail.getConstructId());
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                if (ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())) {
                    getDaoFromContext(ConstructInputParamDao.class).deleteConstructInputParams(constructDetail.getConstructId(),
                            constructDetail.getButtonCode() + "%");
                }
            }
            getDao().deleteById(constructDetail.getId());
            ComponentInfoUtil.getInstance().removeConstructDetail(constructDetail);
        }
    }

    /**
     * 删除预留区和构件绑定关系
     * 
     * @param reserveZoneId 预留区ID
     */
    @Transactional
    public void deleteByReserveZoneId(String reserveZoneId) {
        Assert.notNull(reserveZoneId, "reserveZoneId不能为空");
        List<ConstructDetail> constructDetailList = getByReserveZoneId(reserveZoneId);
        for (ConstructDetail constructDetail : constructDetailList) {
            getService(ConstructDetailSelfParamService.class).deleteByConstructDetailId(constructDetail.getId());
            getService(ConstructFunctionService.class).deleteConstructFunctions(constructDetail.getId());
            getService(ConstructCallbackService.class).deleteConstructCallbacks(constructDetail.getId());
            if (StringUtil.isNotEmpty(constructDetail.getConstructId())) {
                Construct construct = getService(ConstructService.class).getByID(constructDetail.getConstructId());
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                if (ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())) {
                    getDaoFromContext(ConstructInputParamDao.class).deleteConstructInputParams(constructDetail.getConstructId(),
                            constructDetail.getButtonCode() + "%");
                }
            }
            getDao().deleteById(constructDetail.getId());
            ComponentInfoUtil.getInstance().removeConstructDetail(constructDetail);
        }
    }

    /**
     * 获取预留区和构件绑定关系的最大显示顺序
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param treeNodeType 树节点类型
     * @param parentButtonCode 按钮组
     * @return Integer
     */
    public Integer getMaxShowOrder(String constructId, String reserveZoneId, String treeNodeType, String parentButtonCode) {
        Integer maxShowOrder = null;
        if (StringUtil.isNotEmpty(constructId)) {
            if ("Tree".equals(reserveZoneId)) {
                maxShowOrder = getDao().getMaxShowOrder(constructId, reserveZoneId, treeNodeType);
            } else {
                if (StringUtil.isNotEmpty(parentButtonCode)) {
                    maxShowOrder = getDao().getMaxShowOrder(constructId, reserveZoneId, parentButtonCode);
                } else {
                    maxShowOrder = getDao().getMaxShowOrder(constructId, reserveZoneId);
                }
            }
        } else {
            maxShowOrder = getDao().getMaxShowOrderOfCommonBinding(reserveZoneId);
        }
        if (maxShowOrder == null) {
            maxShowOrder = 0;
        }
        return maxShowOrder;
    }

    /**
     * 获取显示顺序范围内的预留区和构件绑定关系
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByShowOrder(Integer start, Integer end, String constructId, String reserveZoneId, String parentButtonCode) {
        if (StringUtil.isEmpty(parentButtonCode)) {
            if (StringUtil.isEmpty(constructId)) {
                return getDao().getByShowOrderOfCommonBinding(start, end, reserveZoneId);
            } else {
                return getDao().getByShowOrder(start, end, constructId, reserveZoneId);
            }
        } else {
            if (StringUtil.isEmpty(constructId)) {
                return getDao().getByShowOrderOfCommonBinding(start, end, reserveZoneId, parentButtonCode);
            } else {
                return getDao().getByShowOrder(start, end, constructId, reserveZoneId, parentButtonCode);
            }
        }
    }

    /**
     * 获取某预留区和构件绑定关系
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByConstructIdAndReserveZoneId(String constructId, String reserveZoneId) {
        if (StringUtil.isNotEmpty(constructId)) {
            return getDao().getByConstructIdAndReserveZoneIdOrderByShowOrderAsc(constructId, reserveZoneId);
        } else {
            return getDao().getByReserveZoneIdOfCommonBinding(reserveZoneId);
        }
    }

    /**
     * 获取某预留区和构件绑定关系
     * 
     * @param buttonDisplayName 按钮显示名称
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return ConstructDetail
     */
    public ConstructDetail getByButtonDisplayName(String buttonDisplayName, String constructId, String reserveZoneId) {
        if (StringUtil.isNotEmpty(constructId)) {
            return getDao().getByButtonDisplayNameAndConstructIdAndReserveZoneId(buttonDisplayName, constructId, reserveZoneId);
        } else {
            return getDao().getByButtonDisplayNameAndReserveZoneIdOfCommonBinding(buttonDisplayName, reserveZoneId);
        }
    }

    /**
     * 获取某预留区和构件绑定关系
     * 
     * @param buttonCode 按钮编码
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return ConstructDetail
     */
    public ConstructDetail getByButtonCode(String buttonCode, String constructId, String reserveZoneId) {
        if (StringUtil.isNotEmpty(constructId)) {
            return getDao().getByButtonCodeAndConstructIdAndReserveZoneId(buttonCode, constructId, reserveZoneId);
        } else {
            return getDao().getByButtonCodeAndReserveZoneIdOfCommonBinding(buttonCode, reserveZoneId);
        }
    }

    /**
     * 获取某预留区和构件绑定关系
     * 
     * @param buttonCode 按钮编码
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getByButtonCode(String buttonCode) {
        return getDao().getByButtonCode(buttonCode);
    }

    /**
     * 校验该预留区绑定的构件在该组合构件中是否能用
     * 
     * @param constructDetail 组合构件中构件和预留区绑定关系
     * @return boolean
     */
    public boolean checkComponentVersion(ConstructDetail constructDetail) {
        // 存储该ConstructDetail下的所有基础构件
        Set<ComponentVersion> detailComponentVersionSet = new HashSet<ComponentVersion>();
        // 获取该ConstructDetail下绑定的构件版本ID
        ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
        if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
            // 组合构件
            detailComponentVersionSet = getService(ConstructService.class).getComponentVersionOfConstruct(componentVersion.getId());
        } else {
            detailComponentVersionSet.add(componentVersion);
        }
        Set<ComponentVersion> constructComponentVersionSet = null;
        if (StringUtil.isNotEmpty(constructDetail.getConstructId())) {
            // 取得该Construct下的所有构件版本ID
            Construct construct = getService(ConstructService.class).getByID(constructDetail.getConstructId());
            constructComponentVersionSet = getService(ConstructService.class).getComponentVersionOfConstruct(construct.getAssembleComponentVersion().getId());
        } else {
            constructComponentVersionSet = getService(ConstructService.class).getComponentVersionOfCommonBinding();
        }
        boolean flag = true;
        if (CollectionUtils.isNotEmpty(constructComponentVersionSet)) {
            for (ComponentVersion constructComponentVersion : constructComponentVersionSet) {
                for (ComponentVersion detailComponentVersion : detailComponentVersionSet) {
                    if (constructComponentVersion.getComponent().getId().equals(detailComponentVersion.getComponent().getId())
                            && !constructComponentVersion.getVersion().equals(detailComponentVersion.getVersion())) {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 复制ConstructDetail
     * 
     * @param constructDetailIds 要复制的绑定关系的ID
     * @param reserveZoneIds 复制到的预留区IDs
     * @param itemChecks check选项
     * @param syncToAllComponent 同时复制到所有使用选中的预留区的构件
     */
    @Transactional
    public void copyConstructDetails(String[] constructDetailIds, String[] reserveZoneIds, String itemChecks, String syncToAllComponent) {
        if (constructDetailIds != null && constructDetailIds.length > 0) {
            boolean sourceIsSelfDefine = false;
            String sourceReserveZoneType = null;
            String targetReserveZoneType = null;
            String[] itemsArray = itemChecks.split(",");
            boolean isCommonReserveZone = false;
            String constructId = null;
            // for循环第一个对象是初始化sourceIsSelfDefine和sourceReserveZoneType的值
            boolean init = false;
            for (String constructDetailId : constructDetailIds) {
                ConstructDetail constructDetail = getByID(constructDetailId);
                if (constructDetail == null) {
                    continue;
                }
                if (!init) {
                    constructId = constructDetail.getConstructId();
                    if (constructId != null) {
                        Construct construct = getService(ConstructService.class).getByID(constructDetail.getConstructId());
                        ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                        if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(baseComponentVersion.getComponent().getType()) != -1) {
                            sourceIsSelfDefine = true;
                            ComponentReserveZone sourceReserveZone = getService(ComponentReserveZoneService.class).getByID(constructDetail.getReserveZoneId());
                            sourceReserveZoneType = AppDefineUtil.getZoneType(sourceReserveZone.getName(), sourceReserveZone.getIsCommon());
                        }
                    } else {
                        sourceIsSelfDefine = true;
                        isCommonReserveZone = true;
                        ComponentReserveZone sourceReserveZone = getService(ComponentReserveZoneService.class).getByID(constructDetail.getReserveZoneId());
                        sourceReserveZoneType = AppDefineUtil.getZoneType(sourceReserveZone.getName(), sourceReserveZone.getIsCommon());
                    }
                    init = true;
                }
                if (reserveZoneIds != null && reserveZoneIds.length > 0) {
                    for (String reserveZoneId : reserveZoneIds) {
                        if ("Common".equals(reserveZoneId) || "Component".equals(reserveZoneId) || constructDetail.getReserveZoneId().equals(reserveZoneId)) {
                            continue;
                        }
                        if (sourceIsSelfDefine) {
                            ComponentReserveZone targetReserveZone = getService(ComponentReserveZoneService.class).getByID(reserveZoneId);
                            targetReserveZoneType = AppDefineUtil.getZoneType(targetReserveZone.getName(), targetReserveZone.getIsCommon());
                            if ("0".equals(constructDetail.getButtonSource()) && !sourceReserveZoneType.equals(targetReserveZoneType)) {
                                continue;
                            }
                        }
                        copyConstructDetail(itemsArray, constructDetail, constructId, reserveZoneId);

                        // 同时复制到所有使用选中的预留区的构件
                        if (isCommonReserveZone && "1".equals(syncToAllComponent)) {
                            Set<String> constructIdSet = getConstructIdsByReserveZoneId(reserveZoneId);
                            String[] constructIdArray = constructIdSet.toArray(new String[constructIdSet.size()]);
                            for (String targetConstructId : constructIdArray) {
                                copyConstructDetail(itemsArray, constructDetail, targetConstructId, reserveZoneId);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 将该按钮的配置复制到相同编码的按钮
     * 
     * @param constructDetailId 要复制的绑定关系的ID
     */
    @Transactional
    public void copyConstructDetailWithSameCode(String constructDetailId) {
        if (StringUtil.isNotEmpty(constructDetailId)) {
            ConstructDetail constructDetail = getByID(constructDetailId);
            if (constructDetail != null) {
                List<ConstructDetail> cdList = getByButtonCode(constructDetail.getButtonCode());
                if (CollectionUtils.isNotEmpty(cdList)) {
                    for (ConstructDetail distConstructDetail : cdList) {
                        if (distConstructDetail.getId().equals(constructDetailId)) {
                            continue;
                        }
                        distConstructDetail.setComponentVersionId(constructDetail.getComponentVersionId());
                        // 构件显示名称
                        distConstructDetail.setButtonDisplayName(constructDetail.getButtonDisplayName());
                        // 页面组装类型
                        distConstructDetail.setAssembleType(constructDetail.getAssembleType());
                        // 宽
                        distConstructDetail.setWidth(constructDetail.getWidth());
                        // 高
                        distConstructDetail.setHeight(constructDetail.getHeight());
                        // 前置事件
                        distConstructDetail.setBeforeClickJs(constructDetail.getBeforeClickJs());
                        getDaoFromContext(ConstructDetailDao.class).save(distConstructDetail);
                        ComponentInfoUtil.getInstance().putConstructDetail(distConstructDetail);
                        // 复制ConstructDetailSelfParam
                        List<ConstructDetailSelfParam> oldSelfParmList = getService(ConstructDetailSelfParamService.class).getByConstructDetailId(
                                distConstructDetail.getId());
                        if (CollectionUtils.isNotEmpty(oldSelfParmList)) {
                            getDaoFromContext(ConstructDetailSelfParamDao.class).deleteInBatch(oldSelfParmList);
                        }
                        List<ConstructDetailSelfParam> selfParamList = getService(ConstructDetailSelfParamService.class).getByConstructDetailId(
                                constructDetail.getId());
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
                        List<ConstructFunction> oldConstructFunctionList = getService(ConstructFunctionService.class).getByConstructDetailId(
                                distConstructDetail.getId());
                        if (CollectionUtils.isNotEmpty(oldConstructFunctionList)) {
                            getDaoFromContext(ConstructFunctionDao.class).deleteInBatch(oldConstructFunctionList);
                        }
                        List<ConstructFunction> constructFunctionList = getService(ConstructFunctionService.class).getByConstructDetailId(
                                constructDetail.getId());
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
                        List<ConstructCallback> oldConstructCallbackList = getService(ConstructCallbackService.class).getByConstructDetailId(
                                distConstructDetail.getId());
                        if (CollectionUtils.isNotEmpty(oldConstructCallbackList)) {
                            getDaoFromContext(ConstructCallbackDao.class).deleteInBatch(oldConstructCallbackList);
                        }
                        List<ConstructCallback> constructCallbackList = getService(ConstructCallbackService.class).getByConstructDetailId(
                                constructDetail.getId());
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
        }
    }

    /**
     * 复制单个ConstructDetail
     * 
     * @param itemsArray
     * @param constructDetail
     * @param constructId
     * @param reserveZoneId
     */
    @Transactional
    private void copyConstructDetail(String[] itemsArray, ConstructDetail constructDetail, String constructId, String reserveZoneId) {
        ConstructDetail distConstructDetail = null;
        if (StringUtil.isEmpty(constructId)) {
            distConstructDetail = getDao().getButtonOfCommonBinding(reserveZoneId, constructDetail.getButtonCode());
        } else {
            distConstructDetail = getDao().getButton(constructId, reserveZoneId, constructDetail.getButtonCode());
        }
        if (null != distConstructDetail) {
            distConstructDetail.setComponentVersionId(constructDetail.getComponentVersionId());
            if (Boolean.parseBoolean(itemsArray[0])) {
                // 构件显示名称
                distConstructDetail.setButtonDisplayName(constructDetail.getButtonDisplayName());
            }
            if (Boolean.parseBoolean(itemsArray[1])) {
                // 页面组装类型
                distConstructDetail.setAssembleType(constructDetail.getAssembleType());
            }
            if (Boolean.parseBoolean(itemsArray[2])) {
                // 宽
                distConstructDetail.setWidth(constructDetail.getWidth());
            }
            if (Boolean.parseBoolean(itemsArray[3])) {
                // 高
                distConstructDetail.setHeight(constructDetail.getHeight());
            }
            if (Boolean.parseBoolean(itemsArray[7])) {
                // 前置事件
                distConstructDetail.setBeforeClickJs(constructDetail.getBeforeClickJs());
            }
            getDaoFromContext(ConstructDetailDao.class).save(distConstructDetail);
            if (Boolean.parseBoolean(itemsArray[4])) {
                // 复制ConstructDetailSelfParam
                List<ConstructDetailSelfParam> oldSelfParmList = getService(ConstructDetailSelfParamService.class).getByConstructDetailId(
                        distConstructDetail.getId());
                if (CollectionUtils.isNotEmpty(oldSelfParmList)) {
                    getDaoFromContext(ConstructDetailSelfParamDao.class).deleteInBatch(oldSelfParmList);
                }
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
            }
            if (Boolean.parseBoolean(itemsArray[5])) {
                // 复制ConstructFunction
                List<ConstructFunction> oldConstructFunctionList = getService(ConstructFunctionService.class).getByConstructDetailId(
                        distConstructDetail.getId());
                if (CollectionUtils.isNotEmpty(oldConstructFunctionList)) {
                    getDaoFromContext(ConstructFunctionDao.class).deleteInBatch(oldConstructFunctionList);
                }
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
            }
            if (Boolean.parseBoolean(itemsArray[6])) {
                // 复制ConstructCallback
                List<ConstructCallback> oldConstructCallbackList = getService(ConstructCallbackService.class).getByConstructDetailId(
                        distConstructDetail.getId());
                if (CollectionUtils.isNotEmpty(oldConstructCallbackList)) {
                    getDaoFromContext(ConstructCallbackDao.class).deleteInBatch(oldConstructCallbackList);
                }
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
        } else if (distConstructDetail == null) {
            distConstructDetail = new ConstructDetail();
            BeanUtils.copy(constructDetail, distConstructDetail);
            distConstructDetail.setId(null);
            distConstructDetail.setConstructId(constructId);
            distConstructDetail.setReserveZoneId(reserveZoneId);
            // distConstructDetail.setShowOrder(getMaxShowOrder(constructId, constructDetail.getReserveZoneId(), null,
            // constructDetail.getParentButtonCode()) + 1);
            getDaoFromContext(ConstructDetailDao.class).save(distConstructDetail);
            // 复制ConstructDetailSelfParam
            List<ConstructDetailSelfParam> selfParamList = getService(ConstructDetailSelfParamService.class).getByConstructDetailId(constructDetail.getId());
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
        ComponentInfoUtil.getInstance().putConstructDetail(distConstructDetail);
    }

    /**
     * 根据预留区ID获取ConstructIds
     * 
     * @param reserveZoneId
     * @return Set<String>
     */
    private Set<String> getConstructIdsByReserveZoneId(String reserveZoneId) {
        List<ConstructDetail> constructDetailList = getByReserveZoneId(reserveZoneId);
        Set<String> constructIdSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail constructDetail : constructDetailList) {
                if (constructDetail.getConstructId() != null) {
                    constructIdSet.add(constructDetail.getConstructId());
                }
            }
        }
        return constructIdSet;
    }

    /**
     * 获取按钮组
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getParentButtonCodesOfReserveZone(String constructId, String reserveZoneId) {
        if (StringUtil.isNotEmpty(constructId)) {
            return getDao().getParentButtonCodesOfReserveZone(constructId, reserveZoneId);
        } else {
            return getDao().getParentButtonCodesOfReserveZoneOfCommonBinding(reserveZoneId);
        }
    }

    /**
     * 获取按钮组
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getParentButtonCodes(String constructId) {
        if (StringUtil.isNotEmpty(constructId)) {
            return getDao().getParentButtonCodes(constructId);
        } else {
            return getDao().getParentButtonCodesOfCommonBinding();
        }
    }

    /**
     * 保存按钮的前置事件处理
     * 
     * @param constructDetailId 预留区和构件绑定关系Id
     * @param beforeClickJs 前置事件处理Js
     */
    @Transactional
    public void saveBeforeClickJs(String constructDetailId, String beforeClickJs) {
        ConstructDetail constructDetail = getByID(constructDetailId);
        if (constructDetail != null) {
            constructDetail.setBeforeClickJs(beforeClickJs);
            getDao().save(constructDetail);
            ComponentInfoUtil.getInstance().putConstructDetail(constructDetail);
        }
    }

    /**
     * qiucs 2014-10-22
     * <p>描述: 获取一级组装按钮</p>
     * 
     * @return List<ConstructDetail> 返回类型
     * @throws
     */
    public List<ConstructDetail> getAppConstructDetails(String cvId, String zoneName) {
        return getDao().getAppConstructDetails(cvId, zoneName);
    }

    /**
     * 将预设的公用预留区和构件的绑定关系同步到选中的构件的预留区中
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param constructDetailIds 需要同步的预留区和构件的绑定关系的ID
     */
    @Transactional
    public void syncConstructDetails(String constructId, String reserveZoneId, String constructDetailIds) {
        List<ConstructDetail> commonConstructDetails = new ArrayList<ConstructDetail>();
        if (constructDetailIds.indexOf(",") != -1) {
            String hql = "from ConstructDetail t where t.id in ('" + constructDetailIds.replace(",", "','") + "')";
            commonConstructDetails.addAll(DatabaseHandlerDao.getInstance().queryEntityForList(hql, ConstructDetail.class));
        } else {
            commonConstructDetails.add(getByID(constructDetailIds));
        }
        ConstructDetail distConstructDetail = null;
        if (CollectionUtils.isNotEmpty(commonConstructDetails)) {
            Integer maxShowOrder = getMaxShowOrder(constructId, reserveZoneId, null, null);
            for (ConstructDetail constructDetail : commonConstructDetails) {
                distConstructDetail = new ConstructDetail();
                BeanUtils.copy(constructDetail, distConstructDetail);
                distConstructDetail.setId(null);
                distConstructDetail.setConstructId(constructId);
                distConstructDetail.setShowOrder(++maxShowOrder);
                getDaoFromContext(ConstructDetailDao.class).save(distConstructDetail);
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
                ComponentInfoUtil.getInstance().putConstructDetail(distConstructDetail);
            }
        }
    }

    /**
     * 将预设的公用预留区和构件的绑定关系同步到选中的构件的预留区中
     * 
     * @param constructDetailIds 需要同步的预留区和构件的绑定关系的ID
     * @param constructIds 同步到的组合构件绑定关系ID
     */
    @Transactional
    public void syncConstructDetailsTo(String constructDetailIds, String constructIds) {
        if (StringUtil.isNotEmpty(constructDetailIds) && StringUtil.isNotEmpty(constructIds)) {
            String[] constructDetailIdArray = constructDetailIds.split(",");
            String[] constructIdArray = constructIds.split(",");
            ConstructDetail constructDetail = null;
            ConstructDetail distConstructDetail = null;
            for (String constructDetailId : constructDetailIdArray) {
                constructDetail = getByID(constructDetailId);
                if (constructDetail == null) {
                    continue;
                }
                for (String constructId : constructIdArray) {
                    distConstructDetail = getDao().getButton(constructId, constructDetail.getReserveZoneId(), constructDetail.getButtonCode());
                    if (distConstructDetail == null) {
                        distConstructDetail = new ConstructDetail();
                        BeanUtils.copy(constructDetail, distConstructDetail);
                        distConstructDetail.setId(null);
                        distConstructDetail.setConstructId(constructId);
                        distConstructDetail.setShowOrder(getMaxShowOrder(constructId, constructDetail.getReserveZoneId(), null,
                                constructDetail.getParentButtonCode()) + 1);
                        getDaoFromContext(ConstructDetailDao.class).save(distConstructDetail);
                        // 复制ConstructDetailSelfParam
                        List<ConstructDetailSelfParam> selfParamList = getService(ConstructDetailSelfParamService.class).getByConstructDetailId(
                                constructDetail.getId());
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
                        List<ConstructFunction> constructFunctionList = getService(ConstructFunctionService.class).getByConstructDetailId(
                                constructDetail.getId());
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
                        List<ConstructCallback> constructCallbackList = getService(ConstructCallbackService.class).getByConstructDetailId(
                                constructDetail.getId());
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
                        ComponentInfoUtil.getInstance().putConstructDetail(distConstructDetail);
                    }
                }
            }
        }
    }

    /**
     * 将预设的公用预留区和构件的绑定关系同步到选中的构件
     * 
     * @param constructDetailIds 需要同步的预留区和构件的绑定关系的ID
     * @param constructIds 同步到的组合构件绑定关系ID
     * @param itemChecks check选项
     */
    @Transactional
    public void syncConstructDetailToConstruct(String constructDetailIds, String constructIds, String itemChecks, String syncToAllComponent) {
        if (StringUtil.isEmpty(constructDetailIds)) {
            return;
        }
        String[] constructIdArray = null;
        String[] constructDetailIdArray = constructDetailIds.split(",");
        String currentConstructId = null;
        if ("1".equals(syncToAllComponent)) {
            ConstructDetail firstCD = getByID(constructDetailIdArray[0]);
            currentConstructId = firstCD.getConstructId();
            ComponentReserveZone componentReserveZone = getService(ComponentReserveZoneService.class).getByID(firstCD.getReserveZoneId());
            String logicTableCode = componentReserveZone.getName().replace("_FORM_0", "").replace("_FORM_1", "").replace("_GRID_LINK", "").replace("_GRID", "");
            // 获取所有使用该预留区的组合构件
            List<Object[]> constructs = getDaoFromContext(ConstructDao.class).getAssembleComponentByLogicTableCode("%\"" + logicTableCode + "\"%");
            if (CollectionUtils.isNotEmpty(constructs)) {
                constructIdArray = new String[constructs.size()];
                for (int i = 0; i < constructs.size(); i++) {
                    Object[] objs = constructs.get(i);
                    constructIdArray[i] = StringUtil.null2empty(objs[3]);
                }
            }
        } else {
            constructIdArray = constructIds.split(",");
        }
        if (constructIdArray == null || constructIdArray.length == 0) {
            return;
        }
        String[] itemsArray = itemChecks.split(",");
        ConstructDetail constructDetail = null;
        for (String constructDetailId : constructDetailIdArray) {
            constructDetail = getByID(constructDetailId);
            if (constructDetail == null) {
                continue;
            }
            for (String constructId : constructIdArray) {
                if (constructId.equals(currentConstructId)) {
                    continue;
                }
                copyConstructDetail(itemsArray, constructDetail, constructId, constructDetail.getReserveZoneId());
            }
        }
    }

    /**
     * 获取列表工具条预置按钮
     * 
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getGridDefaultButtonList() {
        List<ConstructDetail> constructDetailList = new ArrayList<ConstructDetail>();
        constructDetailList.add(newDefaultButton(ButtonUI.Code.CREATE, ButtonUI.Name.CREATE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.UPDATE, ButtonUI.Name.UPDATE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.BATCH_UPDATE, ButtonUI.Name.BATCH_UPDATE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.MODIFY, ButtonUI.Name.MODIFY, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.DELETE, ButtonUI.Name.DELETE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.LOGICAL_DELETE, ButtonUI.Name.LOGICAL_DELETE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.REMOVE, ButtonUI.Name.REMOVE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.REFRESH, ButtonUI.Name.REFRESH, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.REPORT, ButtonUI.Name.REPORT, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.UPLOAD, ButtonUI.Name.UPLOAD, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.VIEW_DOCUMENT, ButtonUI.Name.VIEW_DOCUMENT, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.MORE, ButtonUI.Name.MORE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.EXPORT, ButtonUI.Name.EXPORT, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.EXPORT_SETTING, ButtonUI.Name.EXPORT_SETTING, "0"));
        // constructDetailList.add(newDefaultButton(ButtonUI.Code.NESTED_SEARCH, ButtonUI.Name.NESTED_SEARCH, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.TRACK, ButtonUI.Name.TRACK, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.INTEGRATION_SEARCH, ButtonUI.Name.INTEGRATION_SEARCH, "1"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.BASE_SEARCH, ButtonUI.Name.BASE_SEARCH, "1"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.GREAT_SEARCH, ButtonUI.Name.GREAT_SEARCH, "1"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.SETTING, ButtonUI.Name.SETTING, "1"));
        return constructDetailList;
    }

    /**
     * 获取表单工具条预置按钮
     * 
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getFormDefaultButtonList() {
        List<ConstructDetail> constructDetailList = new ArrayList<ConstructDetail>();
        constructDetailList.add(newDefaultButton(ButtonUI.Code.CREATE, ButtonUI.Name.CREATE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.ADD, ButtonUI.Name.ADD, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.SAVE, ButtonUI.Name.SAVE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.SAVE_ALL, ButtonUI.Name.SAVE_ALL, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.SAVE_AND_CREATE, ButtonUI.Name.SAVE_AND_CREATE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.SAVE_AND_CLOSE, ButtonUI.Name.SAVE_AND_CLOSE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.ADD_AND_CLOSE, ButtonUI.Name.ADD_AND_CLOSE, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.FIRST_RECORD, ButtonUI.Name.FIRST_RECORD, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.PREVIOUS_RECORD, ButtonUI.Name.PREVIOUS_RECORD, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.NEXT_RECORD, ButtonUI.Name.NEXT_RECORD, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.LAST_RECORD, ButtonUI.Name.LAST_RECORD, "0"));
        return constructDetailList;
    }

    /**
     * 获取列表超链接预置按钮
     * 
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getLinkDefaultButtonList() {
        List<ConstructDetail> constructDetailList = new ArrayList<ConstructDetail>();
        constructDetailList.add(newDefaultButton(ButtonUI.Code.LINK_VIEW_DFORM, ButtonUI.Name.LINK_VIEW_DFORM, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.LINK_VIEW_DGRID, ButtonUI.Name.LINK_VIEW_DGRID, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.LINK_DELETE_DB, ButtonUI.Name.LINK_DELETE_DB, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.LINK_DELETE_LG, ButtonUI.Name.LINK_DELETE_LG, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.LINK_DELETE_GD, ButtonUI.Name.LINK_DELETE_GD, "0"));
        constructDetailList.add(newDefaultButton(ButtonUI.Code.LINK_VIEW_DOCUMENT, ButtonUI.Name.LINK_VIEW_DOCUMENT, "0"));
        return constructDetailList;
    }

    /**
     * 构造一个默认的预置按钮
     * 
     * @param buttonCode 按钮编码
     * @param buttonName 按钮名称
     * @param position 按钮位置
     * @return ConstructDetail
     */
    private ConstructDetail newDefaultButton(String buttonCode, String buttonName, String position) {
        ConstructDetail constructDetail = new ConstructDetail();
        constructDetail.setId(buttonCode);
        constructDetail.setButtonCode(buttonCode);
        constructDetail.setButtonName(buttonName);
        constructDetail.setButtonDisplayName(buttonName);
        if (ButtonUI.Code.MORE.equals(buttonCode) || ButtonUI.Code.EXPORT.equals(buttonCode)) {
            constructDetail.setButtonType("1");
        } else if (ButtonUI.Code.EXPORT_SETTING.equals(buttonCode)) {
            constructDetail.setButtonType("2");
            constructDetail.setParentButtonCode(ButtonUI.Code.EXPORT);
        } else {
            constructDetail.setButtonType("0");
        }
        constructDetail.setButtonSource("0");
        constructDetail.setPosition(position);
        return constructDetail;
    }

    /**
     * 获取选中的预置按钮
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getCheckedDefaultButtons(String constructId, String reserveZoneId) {
        if (StringUtil.isNotEmpty(constructId)) {
            return getDao().getCheckedDefaultButtons(constructId, reserveZoneId);
        } else {
            return getDao().getCheckedDefaultButtonsOfCommonBinding(reserveZoneId);
        }
    }

    /**
     * 保存选中的预置按钮
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param buttonCodes 选中的按钮的Code
     */
    @Transactional
    public void saveCheckedDefaultButtons(String constructId, String reserveZoneId, String buttonCodes) {
        if (StringUtil.isNotEmpty(buttonCodes)) {
            ComponentReserveZone reserveZone = getService(ComponentReserveZoneService.class).getByID(reserveZoneId);
            String[] buttonCodeArray = buttonCodes.split(",");
            List<String> buttonCodeList = new ArrayList<String>(Arrays.asList(buttonCodeArray));
            List<ConstructDetail> oldCheckedButtonList = getCheckedDefaultButtons(constructId, reserveZoneId);
            ConstructDetail oldCheckedButton = null;
            String tempButtonCode = null;
            if (CollectionUtils.isNotEmpty(oldCheckedButtonList)) {
                for (Iterator<ConstructDetail> oldCheckedButtonIt = oldCheckedButtonList.iterator(); oldCheckedButtonIt.hasNext();) {
                    oldCheckedButton = oldCheckedButtonIt.next();
                    for (Iterator<String> buttonCodeIt = buttonCodeList.iterator(); buttonCodeIt.hasNext();) {
                        tempButtonCode = buttonCodeIt.next();
                        if (tempButtonCode.equals(oldCheckedButton.getButtonCode())) {
                            oldCheckedButtonIt.remove();
                            buttonCodeIt.remove();
                            break;
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(oldCheckedButtonList)) {
                getDao().delete(oldCheckedButtonList);
                for (ConstructDetail tempOldCheckedButton : oldCheckedButtonList) {
                    ComponentInfoUtil.getInstance().removeConstructDetail(tempOldCheckedButton);
                }
            }
            if (CollectionUtils.isNotEmpty(buttonCodeList)) {
                List<ConstructDetail> newCheckedButtonList = new ArrayList<ConstructDetail>();
                ConstructDetail newCheckedButton = null;
                Integer maxShowOrder = getMaxShowOrder(constructId, reserveZoneId, null, null);
                for (String buttonCode : buttonCodeList) {
                    newCheckedButton = new ConstructDetail();
                    newCheckedButton.setConstructId(StringUtil.isEmpty(constructId) ? null : constructId);
                    newCheckedButton.setReserveZoneId(reserveZoneId);
                    newCheckedButton.setIsCommonReserveZone(reserveZone.getIsCommon() ? "1" : "0");
                    newCheckedButton.setButtonCode(buttonCode);
                    newCheckedButton.setButtonName(AppDefineUtil.getButtonMap().get(buttonCode));
                    newCheckedButton.setButtonDisplayName(AppDefineUtil.getButtonMap().get(buttonCode));
                    if (ButtonUI.Code.MORE.equals(buttonCode) || ButtonUI.Code.EXPORT.equals(buttonCode)) {
                        newCheckedButton.setButtonType("1");
                    } else if (ButtonUI.Code.EXPORT_SETTING.equals(buttonCode)) {
                        newCheckedButton.setButtonType("2");
                        newCheckedButton.setParentButtonCode(ButtonUI.Code.EXPORT);
                    } else {
                        newCheckedButton.setButtonType("0");
                    }
                    newCheckedButton.setButtonSource("0");
                    newCheckedButton.setShowOrder(++maxShowOrder);
                    if (ButtonUI.Code.INTEGRATION_SEARCH.equals(buttonCode) || ButtonUI.Code.BASE_SEARCH.equals(buttonCode)
                            || ButtonUI.Code.GREAT_SEARCH.equals(buttonCode) || ButtonUI.Code.SETTING.equals(buttonCode)) {
                        newCheckedButton.setPosition("2");
                    } else {
                        newCheckedButton.setPosition("0");
                    }
                    if (ButtonUI.Code.CREATE.equals(buttonCode) || ButtonUI.Code.UPDATE.equals(buttonCode)) {
                        String assembleType = SystemParameterUtil.getInstance().getSystemParamValue("表单默认显示方式");
                        newCheckedButton.setAssembleType(StringUtil.null2zero(assembleType));
                        if ("0".equals(assembleType)) {
                            newCheckedButton.setWidth("800");
                            newCheckedButton.setHeight("600");
                        }
                    }
                    newCheckedButtonList.add(newCheckedButton);
                    ComponentInfoUtil.getInstance().putConstructDetail(newCheckedButton);
                }
                getDao().save(newCheckedButtonList);
            }
        }
    }

    /**
     * 获取树预留区上节点绑定的构件
     * 
     * @param constructId 组合构件绑定关系ID
     * @param treeNodeType 树节点类型
     * @param treeNodeProperty 树节点属性
     * @return ConstructDetail
     */
    public ConstructDetail getOfTreeNode(String constructId, String treeNodeType, String treeNodeProperty) {
        return getDao().getOfTreeNode(constructId, treeNodeType, treeNodeProperty);
    }

    /**
     * 获取树预留区上根节点绑定的构件
     * 
     * @param constructId 组合构件绑定关系ID
     * @return ConstructDetail
     */
    public ConstructDetail getOfRootTreeNode(String constructId) {
        return getDao().getOfRootTreeNode(constructId);
    }

    /**
     * 获取逻辑表对应的逻辑表构件列表数据
     * 
     * @param logicTableGroupCode 逻辑表组编码
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getLogicGroupComponentList(String logicTableGroupCode) {
        List<ComponentVersion> componentVersionList = new ArrayList<ComponentVersion>();
        List<ComponentVersion> tempList = null;
        if ("ThirdParty".equals(logicTableGroupCode)) {
            List<ComponentVersion> pageComponentVersionList = getDaoFromContext(ComponentVersionDao.class).getByComponentType(ConstantVar.Component.Type.PAGE);
            List<ComponentVersion> physicalTableComponentVersionList = getDaoFromContext(ComponentVersionDao.class).getByComponentType(
                    ConstantVar.Component.Type.PHYSICAL_TABLE);
            if (CollectionUtils.isNotEmpty(pageComponentVersionList)) {
                componentVersionList.addAll(pageComponentVersionList);
                for (ComponentVersion pageComponentVersion : pageComponentVersionList) {
                    tempList = getDaoFromContext(ConstructDao.class).getAssembleByBaseComponentVersionId(pageComponentVersion.getId());
                    if (CollectionUtils.isNotEmpty(tempList)) {
                        componentVersionList.addAll(tempList);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(physicalTableComponentVersionList)) {
                for (ComponentVersion physicalTableComponentVersion : physicalTableComponentVersionList) {
                    tempList = getDaoFromContext(ConstructDao.class).getAssembleByBaseComponentVersionId(physicalTableComponentVersion.getId());
                    if (CollectionUtils.isNotEmpty(tempList)) {
                        componentVersionList.addAll(tempList);
                    }
                }
            }
        } else {
            List<ComponentVersion> logicComponentVersionList = getDaoFromContext(ComponentVersionDao.class).getLogicComponentList(logicTableGroupCode);
            if (CollectionUtils.isNotEmpty(logicComponentVersionList)) {
                for (ComponentVersion logicComponentVersion : logicComponentVersionList) {
                    tempList = getDaoFromContext(ConstructDao.class).getAssembleByBaseComponentVersionId(logicComponentVersion.getId());
                    if (CollectionUtils.isNotEmpty(tempList)) {
                        componentVersionList.addAll(tempList);
                    }
                }
            }
        }
        return componentVersionList;
    }

    /**
     * 获取树预留区上物理表组节点绑定的构件
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getOfRootPhysicalGroupNode(String constructId) {
        return getDao().getOfRootPhysicalGroupNode(constructId);
    }

    /**
     * 保存树预留区上物理表组节点绑定的构件
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param checkedLogicGroupComs 选择的逻辑表组构件
     */
    @Transactional
    public void saveLogicGroupComponents(String constructId, String reserveZoneId, String checkedLogicGroupComs) {
        if (StringUtil.isNotEmpty(checkedLogicGroupComs)) {
            Map<String, String> logicGroupCompMap = new HashMap<String, String>();
            String[] groups = checkedLogicGroupComs.split(";");
            String[] strs = null;
            for (String group : groups) {
                strs = group.split(",");
                logicGroupCompMap.put(strs[0], strs[1]);
            }
            // 获取 旧的 树预留区上物理表组节点绑定的构件
            List<ConstructDetail> oldLogicGroupComponents = getOfRootPhysicalGroupNode(constructId);
            if (CollectionUtils.isNotEmpty(oldLogicGroupComponents)) {
                ConstructDetail oldLogicGroupComponent = null;
                String newComponentId = null;
                for (Iterator<ConstructDetail> oldIt = oldLogicGroupComponents.iterator(); oldIt.hasNext();) {
                    oldLogicGroupComponent = oldIt.next();
                    newComponentId = logicGroupCompMap.get(oldLogicGroupComponent.getTreeNodeProperty());
                    if (StringUtil.isNotEmpty(newComponentId)) {
                        if (!newComponentId.equals(oldLogicGroupComponent.getComponentVersionId())) {
                            getDao().delete(oldLogicGroupComponent);
                            ComponentInfoUtil.getInstance().removeConstructDetail(oldLogicGroupComponent);
                            oldIt.remove();
                        } else {
                            logicGroupCompMap.remove(oldLogicGroupComponent.getTreeNodeProperty());
                        }
                    } else {
                        getDao().delete(oldLogicGroupComponent);
                        ComponentInfoUtil.getInstance().removeConstructDetail(oldLogicGroupComponent);
                        logicGroupCompMap.remove(oldLogicGroupComponent.getTreeNodeProperty());
                    }
                }
            }
            List<ConstructDetail> newConstructDetais = new ArrayList<ConstructDetail>();
            ConstructDetail newConstructDetail = null;
            Entry<String, String> entry = null;
            Integer maxShowOrder = getMaxShowOrder(constructId, reserveZoneId, "5", null);
            for (Iterator<Entry<String, String>> it = logicGroupCompMap.entrySet().iterator(); it.hasNext();) {
                entry = it.next();
                newConstructDetail = new ConstructDetail();
                newConstructDetail.setConstructId(constructId);
                newConstructDetail.setComponentVersionId(entry.getValue());
                newConstructDetail.setReserveZoneId(reserveZoneId);
                newConstructDetail.setIsCommonReserveZone("0");
                newConstructDetail.setShowOrder(++maxShowOrder);
                newConstructDetail.setAssembleType("1");
                newConstructDetail.setTreeNodeType(TreeDefine.T_GROUP);
                newConstructDetail.setTreeNodeProperty(entry.getKey());
                newConstructDetais.add(newConstructDetail);
                ComponentInfoUtil.getInstance().putConstructDetail(newConstructDetail);
            }
            getDao().save(newConstructDetais);
        } else {
            getDao().deleteAllPhysicalGroupNode(constructId);
            ComponentInfoUtil.getInstance().removeConstructDetail(constructId);
        }
    }

    /**
     * 获取按钮
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param buttonCode 按钮编码
     * @return ConstructDetail
     */
    public ConstructDetail getButton(String constructId, String reserveZoneId, String buttonCode) {
        if (StringUtil.isNotEmpty(constructId)) {
            return getDao().getButton(constructId, reserveZoneId, buttonCode);
        } else {
            return getDao().getButtonOfCommonBinding(reserveZoneId, buttonCode);
        }
    }

    /**
     * 获取二级按钮
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @param parentButtonCode 按钮组编码
     * @return List<ConstructDetail>
     */
    public List<ConstructDetail> getSecondButtonList(String constructId, String reserveZoneId, String parentButtonCode) {
        if (StringUtil.isNotEmpty(constructId)) {
            return getDao().getSecondButtonList(constructId, reserveZoneId, parentButtonCode);
        } else {
            return getDao().getSecondButtonListOfCommonBinding(reserveZoneId, parentButtonCode);
        }
    }

    /**
     * 判断某预留区上是否绑定了构件
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return List<ConstructDetail>
     */
    public boolean isBindingComponent(String constructId, String reserveZoneId) {
        boolean flag = false;
        List<ConstructDetail> constructDetailList = getDao().getByConstructIdAndReserveZoneIdOrderByShowOrderAsc(constructId, reserveZoneId);
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 获取组合构件的按钮数
     * 
     * @param constructId 组合构件绑定关系ID
     * @return long
     */
    public long getConstructDetailCount(String constructId) {
        return getDao().getConstructDetailCount(constructId);
    }

    /**
     * 获取预留区上按钮数
     * 
     * @param constructId 组合构件绑定关系ID
     * @param reserveZoneId 预留区ID
     * @return long
     */
    public long getConstructDetailCount(String constructId, String reserveZoneId) {
        return getDao().getConstructDetailCount(constructId, reserveZoneId);
    }
}

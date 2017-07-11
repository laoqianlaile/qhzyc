package com.ces.config.dhtmlx.action.construct;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.construct.ConstructDetailDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupDefine;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.label.ColumnLabel;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.label.ColumnLabelService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.SystemParameterUtil;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

/**
 * 组合构件中构件和预留区绑定关系Controller
 * 
 * @author wanglei
 * @date 2013-09-27
 */
public class ConstructDetailController extends ConfigDefineServiceDaoController<ConstructDetail, ConstructDetailService, ConstructDetailDao> {

    private static final long serialVersionUID = 9099376023325191240L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ConstructDetail());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("constructDetailService")
    @Override
    protected void setService(ConstructDetailService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        Integer maxShowOrder = getService().getMaxShowOrder(model.getConstructId(), model.getReserveZoneId(), model.getTreeNodeType(),
                model.getParentButtonCode());
        model.setShowOrder(maxShowOrder + 1);
        if (!"TREE".equals(model.getReserveZoneId())) {
            model.setButtonName(model.getButtonDisplayName());
            model.setButtonSource("1");
            model.setPosition("0");
        }
        model = getService().save(model);
        return SUCCESS;
    }

    /**
     * 获取ConstructDetail
     * 
     * @return Object
     */
    public Object getConstructDetailList() {
        String constructId = getParameter("constructId");
        String reserveZoneId = getParameter("reserveZoneId");
        String treeNodeType = getParameter("treeNodeType");
        list = getDataModel(getModelTemplate());
        processFilter(list);
        List<ConstructDetail> constructInfos = getService().getConstructInfos(constructId, reserveZoneId, treeNodeType);
        if (CollectionUtils.isNotEmpty(constructInfos)) {
            ConstructDetail temp = null;
            for (Iterator<ConstructDetail> it = constructInfos.iterator(); it.hasNext();) {
                temp = it.next();
                if (ConstructDetail.COMBOBOX_SEARCH.equals(temp.getButtonCode())) {
                    temp.setButtonDisplayName("下拉框检索配置");
                    break;
                }
            }
        }
        list.setData(constructInfos);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ConstructDetail constructDetail = (ConstructDetail) getModel();
        boolean buttonDisplayNameExist = false;
        if (StringUtil.isNotEmpty(constructDetail.getButtonDisplayName())) {
            ConstructDetail temp = getService().getByButtonDisplayName(constructDetail.getButtonDisplayName(), constructDetail.getConstructId(),
                    constructDetail.getReserveZoneId());
            if (null != constructDetail.getId() && !"".equals(constructDetail.getId())) {
                if (null != temp && !temp.getId().equals(constructDetail.getId())) {
                    // 逻辑表与按钮预设中可能一个按钮在不同的组合构件中配置不一样，这种情况需要同一个按钮配置多次（按钮code不同，按钮名称相同）
                    // buttonDisplayNameExist = true;
                }
            } else {
                if (null != temp) {
                    // buttonDisplayNameExist = true;
                }
            }
        }
        boolean buttonCodeExist = false;
        if (StringUtil.isNotEmpty(constructDetail.getButtonCode())) {
            ConstructDetail temp = getService().getByButtonCode(constructDetail.getButtonCode(), constructDetail.getConstructId(),
                    constructDetail.getReserveZoneId());
            if (null != constructDetail.getId() && !"".equals(constructDetail.getId())) {
                if (null != temp && !temp.getId().equals(constructDetail.getId())) {
                    buttonCodeExist = true;
                }
            } else {
                if (null != temp) {
                    buttonCodeExist = true;
                }
            }
        }
        boolean canotChangeButtonGroupToOther = false;
        if (null != constructDetail.getId() && !"".equals(constructDetail.getId())) {
            ConstructDetail oldConstructDetail = getService().getByID(constructDetail.getId());
            if ("1".equals(oldConstructDetail.getButtonType()) && !"1".equals(constructDetail.getButtonType())) {
                List<ConstructDetail> secondButtonList = getService().getSecondButtonList(oldConstructDetail.getConstructId(),
                        oldConstructDetail.getReserveZoneId(), oldConstructDetail.getButtonCode());
                if (CollectionUtils.isNotEmpty(secondButtonList)) {
                    canotChangeButtonGroupToOther = true;
                }
            }
        }
        // 按钮预留区、树节点预留区、标签页预留区和树预留区中的根节点 只能绑定一次
        boolean bindingOnce = false;
        // 树预留区上空节点、字段节点（跨表）、表节点 每个属性值只能绑定一个构件
        boolean treeNodePropertyBindingOnce = false;
        if (ConstantVar.Component.ReserveZoneType.TREE.equals(constructDetail.getReserveZoneId())) {
            // 根节点
            if (TreeDefine.T_ROOT.equals(constructDetail.getTreeNodeType())) {
                ConstructDetail cd = getService().getOfRootTreeNode(constructDetail.getConstructId());
                if (cd != null && !cd.getId().equals(constructDetail.getId())) {
                    bindingOnce = true;
                }
            } else {
                ConstructDetail cd = getService().getOfTreeNode(constructDetail.getConstructId(), constructDetail.getTreeNodeType(),
                        constructDetail.getTreeNodeProperty());
                if (cd != null && !cd.getId().equals(constructDetail.getId())) {
                    treeNodePropertyBindingOnce = true;
                }
            }
        } else {
            ComponentReserveZone reserveZone = getService(ComponentReserveZoneService.class).getByID(constructDetail.getReserveZoneId());
            if (reserveZone != null
                    && (ConstantVar.Component.ReserveZoneType.BUTTON.equals(reserveZone.getType()) || ConstantVar.Component.ReserveZoneType.TREE_NODE
                            .equals(reserveZone.getType()))) {
                List<ConstructDetail> constructDetailList = getService().getByConstructIdAndReserveZoneId(constructDetail.getConstructId(),
                        constructDetail.getReserveZoneId());
                if (CollectionUtils.isNotEmpty(constructDetailList)) {
                    for (ConstructDetail cd : constructDetailList) {
                        if (!cd.getId().equals(constructDetail.getId())) {
                            bindingOnce = true;
                            break;
                        }
                    }
                }
            }
        }
        boolean componentValid = true;
        if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
            componentValid = getService().checkComponentVersion(constructDetail);
        }
        setReturnData("{'buttonDisplayNameExist':" + buttonDisplayNameExist + ",'buttonCodeExist':" + buttonCodeExist + ",'componentValid':" + componentValid
                + ",'bindingOnce':" + bindingOnce + ",'treeNodePropertyBindingOnce':" + treeNodePropertyBindingOnce + ",'canotChangeButtonGroupToOther':"
                + canotChangeButtonGroupToOther + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 排序
     * 
     * @return Object
     */
    public Object sort() {
        String start = getParameter("start");
        String end = getParameter("end");
        ConstructDetail startConstructDetail = getService().getByID(start);
        ConstructDetail endConstructDetail = getService().getByID(end);
        if (startConstructDetail.getShowOrder().intValue() > endConstructDetail.getShowOrder().intValue()) {
            // 向上
            List<ConstructDetail> constructDetailList = getService().getByShowOrder(endConstructDetail.getShowOrder(), startConstructDetail.getShowOrder(),
                    startConstructDetail.getConstructId(), startConstructDetail.getReserveZoneId(), startConstructDetail.getParentButtonCode());
            startConstructDetail.setShowOrder(endConstructDetail.getShowOrder());
            getService().save(startConstructDetail);
            for (ConstructDetail constructDetail : constructDetailList) {
                if (constructDetail.getId().equals(startConstructDetail.getId())) {
                    continue;
                }
                constructDetail.setShowOrder(constructDetail.getShowOrder() + 1);
                getService().save(constructDetail);
            }
        } else {
            // 向下
            List<ConstructDetail> constructDetailList = getService().getByShowOrder(startConstructDetail.getShowOrder(), endConstructDetail.getShowOrder(),
                    startConstructDetail.getConstructId(), startConstructDetail.getReserveZoneId(), startConstructDetail.getParentButtonCode());
            startConstructDetail.setShowOrder(endConstructDetail.getShowOrder());
            getService().save(startConstructDetail);
            for (ConstructDetail constructDetail : constructDetailList) {
                if (constructDetail.getId().equals(startConstructDetail.getId())) {
                    continue;
                }
                constructDetail.setShowOrder(constructDetail.getShowOrder() - 1);
                getService().save(constructDetail);
            }
        }
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取按钮图标
     * 
     * @return Object
     */
    public Object getButtonStyles() {
        List<Code> codeList = CodeUtil.getInstance().getCodeList("OPERATE_COLUMN_BUTTON_STYLE");
        setReturnData(codeList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取按钮图标
     * 
     * @return Object
     */
    public Object getButtonImgs() {
        String assembleComponentVersionId = getParameter("assembleComponentVersionId");
        ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(assembleComponentVersionId);
        String imgPath = null;
        if (componentVersion == null) {
            imgPath = ComponentFileUtil.getProjectPath() + "cfg-resource/" + SystemParameterUtil.getInstance().getReleaseSystemUI() + "/common/images/icon";
        } else {
            imgPath = ComponentFileUtil.getProjectPath() + "cfg-resource/" + componentVersion.getViews() + "/common/images/icon";
        }
        File imgDir = new File(imgPath);
        List<String> imgNameList = new ArrayList<String>();
        if (imgDir.exists()) {
            String[] imgNames = imgDir.list();
            for (String imgName : imgNames) {
                if (imgName.toLowerCase().endsWith(".gif")) {
                    imgNameList.add(imgName);
                }
            }
        }
        setReturnData(imgNameList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取按钮组
     * 
     * @return Object
     */
    public Object getParentButtonCodesOfReserveZone() {
        String constructId = getParameter("constructId");
        String reserveZoneId = getParameter("reserveZoneId");
        List<ConstructDetail> constructDetailList = getService().getParentButtonCodesOfReserveZone(constructId, reserveZoneId);
        setReturnData(constructDetailList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取按钮组
     * 
     * @return Object
     */
    public Object getParentButtonCodes() {
        String constructId = getParameter("constructId");
        List<ConstructDetail> constructDetailList = getService().getParentButtonCodes(constructId);
        setReturnData(constructDetailList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 复制组合构件中构件和预留区绑定关系
     * 
     * @return Object
     */
    public Object copyConstructDetail() {
        String constructDetailIds = getParameter("constructDetailIds");
        String reserveZoneIds = getParameter("reserveZoneIds");
        String itemChecks = getParameter("itemChecks");
        String syncToAllComponent = getParameter("syncToAllComponent");
        try {
            getService().copyConstructDetails(constructDetailIds.split(","), reserveZoneIds.split(","), itemChecks, syncToAllComponent);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData("{'success':false}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 将该按钮的配置复制到相同编码的按钮
     * 
     * @return Object
     */
    public Object copyConstructDetailWithSameCode() {
        String constructDetailId = getParameter("constructDetailId");
        try {
            getService().copyConstructDetailWithSameCode(constructDetailId);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData("{'success':false}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 保存按钮的前置事件处理
     * 
     * @return Object
     */
    public Object saveBeforeClickJs() {
        String constructDetailId = getParameter("constructDetailId");
        String beforeClickJs = getParameter("beforeClickJs");
        getService().saveBeforeClickJs(constructDetailId, beforeClickJs);
        setReturnData("保存成功！");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取同步按钮
     * 
     * @return Object
     */
    public Object getSyncButtons() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String constructId = getParameter("constructId");
        String reserveZoneId = getParameter("reserveZoneId");
        List<ConstructDetail> commonConstructDetailList = getService().getConstructInfos(null, reserveZoneId, null);
        List<ConstructDetail> constructDetailList = getService().getConstructInfos(constructId, reserveZoneId, null);
        Set<String> exitButtonCodeSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail cd : constructDetailList) {
                exitButtonCodeSet.add(cd.getButtonCode());
            }
        }
        if (CollectionUtils.isNotEmpty(commonConstructDetailList)) {
            ConstructDetail cd = null;
            for (Iterator<ConstructDetail> iterator = commonConstructDetailList.iterator(); iterator.hasNext();) {
                cd = iterator.next();
                if (exitButtonCodeSet.contains(cd.getButtonCode())) {
                    iterator.remove();
                }
                if (ConstructDetail.COMBOBOX_SEARCH.equals(cd.getButtonCode())) {
                    cd.setButtonDisplayName("下拉框检索配置");
                }
            }
        }
        list.setData(commonConstructDetailList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 将预设的公用预留区和构件的绑定关系同步到选中的构件的预留区中
     * 
     * @return Object
     */
    public Object syncConstructDetails() {
        String constructId = getParameter("constructId");
        String reserveZoneId = getParameter("reserveZoneId");
        String constructDetailIds = getParameter("constructDetailIds");
        try {
            getService().syncConstructDetails(constructId, reserveZoneId, constructDetailIds);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData("{'success':false}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 将预设的公用预留区和构件的绑定关系同步到选中的构件的预留区中
     * 
     * @return Object
     */
    public Object syncConstructDetailsTo() {
        String constructDetailIds = getParameter("constructDetailIds");
        String constructIds = getParameter("constructIds");
        try {
            getService().syncConstructDetailsTo(constructDetailIds, constructIds);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData("{'success':false}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /***
     * 将预设的公用的预留区和构件的绑定关系同步到选中的构件中
     * 
     * @return Object
     */
    public Object syncConstructDetailToConstruct() {
        String constructDetailIds = getParameter("constructDetailIds");
        String constructIds = getParameter("constructIds");
        String itemChecks = getParameter("itemChecks");
        String syncToAllComponent = getParameter("syncToAllComponent");
        try {
            getService().syncConstructDetailToConstruct(constructDetailIds, constructIds, itemChecks, syncToAllComponent);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData("{'success':false}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取预置按钮
     * 
     * @return Object
     */
    public Object getDefaultButtons() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String reserveZoneId = getParameter("reserveZoneId");
        List<ConstructDetail> constructDetailList = null;
        ComponentReserveZone reserveZone = getService(ComponentReserveZoneService.class).getByID(reserveZoneId);
        if (reserveZone != null) {
            String zoneType = AppDefineUtil.getZoneType(reserveZone.getName(), reserveZone.getIsCommon());
            if (AppDefineUtil.RZ_TYPE_GRID.equals(zoneType)) {
                constructDetailList = getService().getGridDefaultButtonList();
            } else if (AppDefineUtil.RZ_TYPE_FORM.equals(zoneType)) {
                constructDetailList = getService().getFormDefaultButtonList();
            } else if (AppDefineUtil.RZ_TYPE_GRID_LINK.equals(zoneType)) {
                constructDetailList = getService().getLinkDefaultButtonList();
            }
        }
        list.setData(constructDetailList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取选中的预置按钮
     * 
     * @return Object
     */
    public Object getCheckedDefaultButtons() {
        String constructId = getParameter("constructId");
        String reserveZoneId = getParameter("reserveZoneId");
        setReturnData(getService().getCheckedDefaultButtons(constructId, reserveZoneId));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 保存选中的预置按钮
     * 
     * @return Object
     */
    public Object saveCheckedDefaultButtons() {
        String constructId = getParameter("constructId");
        String reserveZoneId = getParameter("reserveZoneId");
        String buttonCodes = getParameter("buttonCodes");
        getService().saveCheckedDefaultButtons(constructId, reserveZoneId, buttonCodes);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 初始化树节点类型下拉框，字段节点（跨表）对应字段标签下拉框，表节点对应表节点下拉框
     * 
     * @return Object
     */
    public Object getTreeNodePropertyCombo() {
        String constructId = getParameter("constructId");
        String treeNodeType = getParameter("treeNodeType");
        List<Object[]> comboList = new ArrayList<Object[]>();
        Construct construct = getService(ConstructService.class).getByID(constructId);
        Module module = getService(ModuleService.class).findByComponentVersionId(construct.getBaseComponentVersionId());
        if (module != null && ConstantVar.Component.Type.TREE.equals(module.getType())) {
            Object[] objs = null;
            if (TreeDefine.T_COLUMN_EMP.equals(treeNodeType)) {
                // 获取所有的字段标签
                List<ColumnLabel> columnLabelList = getService(ColumnLabelService.class).getAllColumnLabel();
                if (CollectionUtils.isNotEmpty(columnLabelList)) {
                    for (ColumnLabel columnLabel : columnLabelList) {
                        objs = new Object[2];
                        objs[0] = columnLabel.getCode();
                        objs[1] = columnLabel.getName();
                        comboList.add(objs);
                    }
                }
            } else if (TreeDefine.T_TABLE.equals(treeNodeType)) {
                // 获取树上的所有表节点
                List<TreeDefine> treeDefineList = getService(TreeDefineService.class).getByRootIdAndType(module.getTreeId(), treeNodeType);
                if (CollectionUtils.isNotEmpty(treeDefineList)) {
                    for (TreeDefine treeDefine : treeDefineList) {
                        objs = new Object[2];
                        objs[0] = treeDefine.getId();
                        objs[1] = treeDefine.getName();
                        comboList.add(objs);
                    }
                }
            } else if (TreeDefine.T_GROUP.equals(treeNodeType)) {
                // 获取所有的逻辑表组
                List<LogicGroupDefine> logicGroupDefineList = getService(LogicGroupDefineService.class).getAllLogicGroupDefines();
                if (CollectionUtils.isNotEmpty(logicGroupDefineList)) {
                    for (LogicGroupDefine logicGroupDefine : logicGroupDefineList) {
                        objs = new Object[2];
                        objs[0] = logicGroupDefine.getCode();
                        objs[1] = logicGroupDefine.getGroupName();
                        comboList.add(objs);
                    }
                }
            }
        }
        setReturnData(comboList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取逻辑表树
     * 
     * @return Object
     */
    public Object getLogicGroupTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("Root".equals(treeNodeId)) {
            List<LogicGroupDefine> logicGroupDefineList = getService(LogicGroupDefineService.class).getAllLogicGroupDefines();
            for (LogicGroupDefine logicGroupDefine : logicGroupDefineList) {
                treeNode = new DhtmlxTreeNode();
                treeNode.setId(logicGroupDefine.getCode());
                treeNode.setText(logicGroupDefine.getGroupName());
                treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                treeNode.setChild("0");
                treeNodelist.add(treeNode);
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取逻辑表对应的逻辑表构件列表数据
     * 
     * @return Object
     */
    public Object getLogicGroupComponentList() {
        String logicTableGroupCode = getParameter("logicTableGroupCode");
        List<Object[]> list = new ArrayList<Object[]>();
        List<ComponentVersion> componentVersionList = getService().getLogicGroupComponentList(logicTableGroupCode);
        if (CollectionUtils.isNotEmpty(componentVersionList)) {
            Object[] objs = null;
            for (ComponentVersion componentVersion : componentVersionList) {
                objs = new Object[3];
                objs[0] = componentVersion.getId();
                objs[1] = componentVersion.getId();
                objs[2] = componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion();
                list.add(objs);
            }
        }
        setReturnData(list);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取组合构件中的逻辑表构件信息
     * 
     * @return Object
     */
    public Object getCheckedLogicGroupComponentList() {
        String constructId = getParameter("constructId");
        setReturnData(getService().getOfRootPhysicalGroupNode(constructId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取组合构件中的逻辑表构件信息
     * 
     * @return Object
     */
    public Object saveLogicGroupComponents() {
        String constructId = getParameter("constructId");
        String reserveZoneId = getParameter("reserveZoneId");
        String checkedLogicGroupComs = getParameter("checkedLogicGroupComs");
        try {
            getService().saveLogicGroupComponents(constructId, reserveZoneId, checkedLogicGroupComs);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData("{'success':false}");
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取下拉框检索配置
     */
    public Object showComboboxSearch() {
        try {
            String constructId = getParameter("constructId");
            String reserveZoneId = getParameter("reserveZoneId");
            model = getService().getButton(constructId, reserveZoneId, ConstructDetail.COMBOBOX_SEARCH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取下拉框检索配置
     */
    public Object saveComboboxSearch() throws FatalException {
        if (model.getPosition() == null) {
            model.setPosition("2");
        }
        model = getService().save(model);
        return SUCCESS;
    }
}
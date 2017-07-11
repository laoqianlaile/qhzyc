package com.ces.config.dhtmlx.action.construct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.construct.ConstructFilterDetailDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.construct.ConstructFilter;
import com.ces.config.dhtmlx.entity.construct.ConstructFilterDetail;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructFilterDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructFilterService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.google.common.collect.Lists;

/**
 * 数据过滤详情Controller
 * 
 * @author wanglei
 * @date 2015-05-20
 */
public class ConstructFilterDetailController extends
        ConfigDefineServiceDaoController<ConstructFilterDetail, ConstructFilterDetailService, ConstructFilterDetailDao> {

    private static final long serialVersionUID = 1L;

    @Override
    protected void initModel() {
        setModel(new ConstructFilterDetail());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("constructFilterDetailService")
    protected void setService(ConstructFilterDetailService service) {
        super.setService(service);
    }

    /**
     * 获取数据过滤树
     * 
     * @return Object
     */
    public Object getFilterTree() {
        String topComVersionId = getParameter("P_topComVersionId");
        list = getDataModel(getModelTemplate());
        processFilter(list);
        boolean open = BooleanUtils.toBoolean(getParameter("P_OPEN"));
        String treeNodeIdSuffix = CommonUtil.generateUIId("");
        // 树节点ID，"C_"表示构件，"T_"表示表
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeNodeId) || treeNodeId.startsWith("C_")) {
            String parentCVId = null;
            if ("-1".equals(treeNodeId)) {
                parentCVId = topComVersionId;
            } else {
                String[] strs = treeNodeId.split("_");
                parentCVId = strs[1];
            }
            // 表列表 和 绑定的构件
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(parentCVId);
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())) {
                    List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getConstructInfoByConstructId(construct.getId());
                    if (CollectionUtils.isNotEmpty(constructDetailList)) {
                        ComponentVersion bindingComponentVersion = null;
                        ComponentVersion bindingBaseComponentVersion = null;
                        for (ConstructDetail constructDetail : constructDetailList) {
                            if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
                                bindingComponentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                                if (ConstantVar.Component.Type.ASSEMBLY.equals(bindingComponentVersion.getComponent().getType())) {
                                    Construct tempConstruct = getService(ConstructService.class).getByAssembleComponentVersionId(
                                            bindingComponentVersion.getId());
                                    bindingBaseComponentVersion = getService(ComponentVersionService.class).getByID(tempConstruct.getBaseComponentVersionId());
                                    if (ConstantVar.Component.Type.LOGIC_TABLE.equals(bindingBaseComponentVersion.getComponent().getType())
                                            || ConstantVar.Component.Type.PHYSICAL_TABLE.equals(bindingBaseComponentVersion.getComponent().getType())) {
                                        treeNode = new DhtmlxTreeNode();
                                        treeNode.setId("C_" + bindingComponentVersion.getId() + treeNodeIdSuffix);
                                        treeNode.setText(bindingComponentVersion.getComponent().getAlias() + "_" + bindingComponentVersion.getVersion());
                                        treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                        treeNode.setChild("1");
                                        treeNode.setOpen(open);
                                        treeNode.setProp0(bindingComponentVersion.getId());
                                        treeNode.setProp1("Component");
                                        treeNodelist.add(treeNode);
                                    }
                                }
                            }
                        }
                    }
                } else if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())
                        || ConstantVar.Component.Type.PHYSICAL_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                    // 列表
                    Module module = getService(ModuleService.class).findByComponentVersionId(baseComponentVersion.getId());
                    Set<String> tableIdSet = new HashSet<String>();
                    if (!"0".equals(module.getArea1Id()) && StringUtil.isNotEmpty(module.getTable1Id())) {
                        tableIdSet.add(module.getTable1Id());
                    }
                    if (!"0".equals(module.getArea2Id()) && StringUtil.isNotEmpty(module.getTable2Id())) {
                        tableIdSet.add(module.getTable2Id());
                    }
                    if (!"0".equals(module.getArea3Id()) && StringUtil.isNotEmpty(module.getTable3Id())) {
                        tableIdSet.add(module.getTable3Id());
                    }
                    if (tableIdSet.size() > 0) {
                        if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                            LogicTableDefine logicTableDefine = null;
                            for (String tableId : tableIdSet) {
                                logicTableDefine = getService(LogicTableDefineService.class).getByCode(tableId);
                                treeNode = new DhtmlxTreeNode();
                                treeNode.setId("T_" + tableId + treeNodeIdSuffix);
                                treeNode.setText(logicTableDefine.getShowName() + "_列表");
                                treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                                treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                                treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                treeNode.setChild("0");
                                treeNode.setOpen(open);
                                treeNode.setProp0(componentVersion.getId());
                                treeNode.setProp1("LogicTable");
                                treeNode.setProp2(tableId);
                                treeNodelist.add(treeNode);
                            }
                        } else if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                            PhysicalTableDefine physicalTableDefine = null;
                            for (String tableId : tableIdSet) {
                                physicalTableDefine = getService(PhysicalTableDefineService.class).getByID(tableId);
                                treeNode = new DhtmlxTreeNode();
                                treeNode.setId("T_" + tableId + treeNodeIdSuffix);
                                treeNode.setText(physicalTableDefine.getShowName() + "_列表");
                                treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                                treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                                treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                treeNode.setChild("0");
                                treeNode.setOpen(open);
                                treeNode.setProp0(componentVersion.getId());
                                treeNode.setProp1("PhysicalTable");
                                treeNode.setProp2(tableId);
                                treeNodelist.add(treeNode);
                            }
                        }
                    }
                    // 预留区中绑定的构件
                    List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getConstructInfoByConstructId(construct.getId());
                    if (CollectionUtils.isNotEmpty(constructDetailList)) {
                        ComponentVersion bindingComponentVersion = null;
                        ComponentVersion bindingBaseComponentVersion = null;
                        for (ConstructDetail constructDetail : constructDetailList) {
                            if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
                                bindingComponentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                                if (ConstantVar.Component.Type.ASSEMBLY.equals(bindingComponentVersion.getComponent().getType())) {
                                    Construct tempConstruct = getService(ConstructService.class).getByAssembleComponentVersionId(
                                            bindingComponentVersion.getId());
                                    bindingBaseComponentVersion = getService(ComponentVersionService.class).getByID(tempConstruct.getBaseComponentVersionId());
                                    if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(bindingBaseComponentVersion.getComponent().getType()) != -1
                                            || ConstantVar.Component.Type.TAB.equals(bindingBaseComponentVersion.getComponent().getType())) {
                                        treeNode = new DhtmlxTreeNode();
                                        treeNode.setId("C_" + bindingComponentVersion.getId() + treeNodeIdSuffix);
                                        treeNode.setText(bindingComponentVersion.getComponent().getAlias() + "_" + bindingComponentVersion.getVersion());
                                        treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                        treeNode.setChild("1");
                                        treeNode.setOpen(open);
                                        treeNode.setProp0(bindingComponentVersion.getId());
                                        treeNode.setProp1("Component");
                                        treeNodelist.add(treeNode);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取数据权限详情
     * 
     * @return Object
     */
    public Object getConstructFilterDetailList() {
        String topComVersionId = getParameter("topComVersionId");
        String componentVersionId = getParameter("componentVersionId");
        String tableId = getParameter("tableId");
        ConstructFilter constructFilter = getService(ConstructFilterService.class).getConstructFilter(topComVersionId, componentVersionId, tableId);
        List<ConstructFilterDetail> constructFilterDetailList = null;
        if (constructFilter != null) {
            constructFilterDetailList = getService().getByConstructFilterId(constructFilter.getId());
        } else {
            constructFilterDetailList = new ArrayList<ConstructFilterDetail>();
        }
        setReturnData(constructFilterDetailList);
        return new DefaultHttpHeaders("success").disableCaching();
    }

}

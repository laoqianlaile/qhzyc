package com.ces.config.dhtmlx.action.construct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.component.ComponentArea;
import com.ces.config.dhtmlx.entity.component.ComponentAssembleArea;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.component.ComponentAreaService;
import com.ces.config.dhtmlx.service.component.ComponentAssembleAreaService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CfgCommonUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FileUtil;
import com.ces.config.utils.PreviewUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.ZipUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

/**
 * 组合构件绑定关系Controller
 * 
 * @author wanglei
 * @date 2013-08-26
 */
public class ConstructController extends ConfigDefineServiceDaoController<Construct, ConstructService, ConstructDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new Construct());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("constructService")
    @Override
    protected void setService(ConstructService service) {
        super.setService(service);
    }

    /**
     * 根据组合构件ID获取构件组装信息
     * 
     * @return Object
     */
    public Object getConstructByAssemble() {
        Construct construct = getService().getByAssembleComponentVersionId(getParameter("assembleComponentVersionId"));
        if (construct == null) {
            setModel(new Construct());
        } else {
            setModel(construct);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 判断该组合构件版本是否可以删除
     * 
     * @return Object
     */
    public Object deleteValid() {
        String componentVersionId = getParameter("componentVersionId");
        boolean flag = true;
        StringBuffer msg = new StringBuffer();
        msg.append("该构件不能删除，因为该构件");
        // 绑定了菜单，不能删除
        List<Menu> menuList = getService(MenuService.class).getByComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(menuList)) {
            flag = false;
            msg.append("绑定了菜单");
            for (Menu menu : menuList) {
                msg.append(menu.getName());
                msg.append("、");
            }
            msg.deleteCharAt(msg.length() - 1);
            msg.append("；");
        }
        // 绑定了某组合构件中的预留区，不能删除
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            flag = false;
            msg.append("绑定了");
            for (ConstructDetail constructDetail : constructDetailList) {
                if (StringUtil.isEmpty(constructDetail.getConstructId())) {
                    msg.append("‘逻辑表与按钮预设’、");
                    continue;
                }
                Construct construct = getService(ConstructService.class).getByID(constructDetail.getConstructId());
                msg.append(construct.getAssembleComponentVersion().getComponent().getAlias() + "_" + construct.getAssembleComponentVersion().getVersion());
                msg.append("、");
            }
            msg.deleteCharAt(msg.length() - 1);
            msg.append("中的按钮；");
        }
        setReturnData("{'success':" + flag + ", 'message':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 删除组合构件版本
     * 
     * @return Object
     */
    public Object deleteComponentVersion() {
        String componentVersionId = getParameter("componentVersionId");
        Construct construct = getService().getByAssembleComponentVersionId(componentVersionId);
        getService().delete(construct.getId());
        setReturnData("删除成功！");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取组合构件和构件分类的树
     * 
     * @return Object
     */
    public Object getConstructTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeNodeId)) {
            List<ComponentAssembleArea> componentAssembleAreaList = getService(ComponentAssembleAreaService.class).getComponentAssembleAreaListByParentId(
                    treeNodeId);
            if (CollectionUtils.isNotEmpty(componentAssembleAreaList)) {
                for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentAssembleArea.getId());
                    treeNode.setText(componentAssembleArea.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("1");
                    // Type （Area：构件分类，Component：组合构件）
                    treeNode.setProp0("Area");
                    treeNodelist.add(treeNode);
                }
            }
        } else {
            List<ComponentAssembleArea> componentAssembleAreaList = getService(ComponentAssembleAreaService.class).getComponentAssembleAreaListByParentId(
                    treeNodeId);
            if (CollectionUtils.isNotEmpty(componentAssembleAreaList)) {
                for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentAssembleArea.getId());
                    treeNode.setText(componentAssembleArea.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("1");
                    // Type （Area：构件分类，Component：组合构件）
                    treeNode.setProp0("Area");
                    treeNodelist.add(treeNode);
                }
            }
            List<ComponentVersion> assembleComponentVersionList = getService(ComponentVersionService.class).getComponentVersionListByAssembleAreaId(treeNodeId);
            if (CollectionUtils.isNotEmpty(assembleComponentVersionList)) {
                for (ComponentVersion componentVersion : assembleComponentVersionList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentVersion.getId());
                    treeNode.setText(componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("0");
                    // Type （Area：构件分类，Component：组合构件）
                    treeNode.setProp0("Component");
                    Construct construct = getService().getByAssembleComponentVersionId(componentVersion.getId());
                    if (construct == null) {
                        continue;
                    }
                    // constructId
                    treeNode.setProp1(construct.getId());
                    // 基础构件的构件类型
                    ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                    if (baseComponentVersion == null) {
                        continue;
                    }
                    treeNode.setProp2(baseComponentVersion.getComponent().getType());
                    // 基础构件ID
                    treeNode.setProp3(baseComponentVersion.getId());
                    treeNodelist.add(treeNode);
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取构件组装树，关联该构件的选中
     * 
     * @return Object
     */
    public Object getCheckedConstructTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeNodeId = getId();
        String componentVersionId = getParameter("componentVersionId");
        if (componentVersionId.indexOf("SCV_") != -1) {
            componentVersionId = componentVersionId.substring(4);
        }
        String baseComponentVersionId = null;
        Construct assembleComponent = getService().getByAssembleComponentVersionId(componentVersionId);
        if (assembleComponent != null) {
            baseComponentVersionId = getService(ComponentVersionService.class).getByID(assembleComponent.getBaseComponentVersionId()).getId();
        }
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeNodeId)) {
            List<ComponentAssembleArea> componentAssembleAreaList = getService(ComponentAssembleAreaService.class).getComponentAssembleAreaListByParentId(
                    treeNodeId);
            if (CollectionUtils.isNotEmpty(componentAssembleAreaList)) {
                for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentAssembleArea.getId());
                    treeNode.setText(componentAssembleArea.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setOpen(true);
                    treeNode.setChild("1");
                    // Type （Area：构件分类，Component：组合构件）
                    treeNode.setProp0("Area");
                    treeNodelist.add(treeNode);
                }
            }
        } else {
            List<ComponentAssembleArea> componentAssembleAreaList = getService(ComponentAssembleAreaService.class).getComponentAssembleAreaListByParentId(
                    treeNodeId);
            if (CollectionUtils.isNotEmpty(componentAssembleAreaList)) {
                for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentAssembleArea.getId());
                    treeNode.setText(componentAssembleArea.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setOpen(true);
                    treeNode.setChild("1");
                    // Type （Area：构件分类，Component：组合构件）
                    treeNode.setProp0("Area");
                    treeNodelist.add(treeNode);
                }
            }
            List<ComponentVersion> assembleComponentVersionList = getService(ComponentVersionService.class).getComponentVersionListByAssembleAreaId(treeNodeId);
            if (CollectionUtils.isNotEmpty(assembleComponentVersionList)) {
                for (ComponentVersion componentVersion : assembleComponentVersionList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentVersion.getId());
                    treeNode.setText(componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("0");
                    // Type （Area：构件分类，Component：组合构件）
                    treeNode.setProp0("Component");
                    Construct construct = getService().getByAssembleComponentVersionId(componentVersion.getId());
                    // constructId
                    treeNode.setProp1(construct.getId());
                    // 基础构件的构件类型
                    ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                    treeNode.setProp2(baseComponentVersion.getComponent().getType());
                    // 基础构件ID
                    treeNode.setProp3(baseComponentVersion.getId());
                    if (baseComponentVersion.getId().equals(componentVersionId) || baseComponentVersion.getId().equals(baseComponentVersionId)) {
                        treeNode.setChecked("1");
                    }
                    treeNodelist.add(treeNode);
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 树排序
     * 
     * @return Object
     */
    public Object treeSort() {
        String start = getParameter("start");
        String end = getParameter("end");
        String targetId = getParameter("targetId");
        ComponentVersion target = getService(ComponentVersionService.class).getByID(targetId);
        if (target != null) {
            // 证明target是一个构件节点，构件节点是不能作为父节点的，所有用该构件节点的父节点作为target
            targetId = target.getAreaId();
        }
        ComponentAssembleArea startComponentAssembleArea = getService(ComponentAssembleAreaService.class).getByID(start);
        // 拖动的节点是构件分类节点
        if (startComponentAssembleArea != null) {
            if (startComponentAssembleArea.getParentId().equals(targetId)) {
                // 同个父分类中拖动排序
                if (StringUtil.isNotEmpty(end)) {
                    ComponentAssembleArea endComponentAssembleArea = getService(ComponentAssembleAreaService.class).getByID(end);
                    if (endComponentAssembleArea != null) {
                        // 拖动到的节点是构件分类节点
                        if (startComponentAssembleArea.getShowOrder().intValue() > endComponentAssembleArea.getShowOrder().intValue()) {
                            // 向上
                            List<ComponentAssembleArea> componentAssembleAreaList = getService(ComponentAssembleAreaService.class)
                                    .getComponentAssembleAreaListByShowOrder(endComponentAssembleArea.getShowOrder(),
                                            startComponentAssembleArea.getShowOrder(), startComponentAssembleArea.getParentId());
                            startComponentAssembleArea.setShowOrder(endComponentAssembleArea.getShowOrder());
                            getService(ComponentAssembleAreaService.class).save(startComponentAssembleArea);
                            for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                                if (componentAssembleArea.getId().equals(startComponentAssembleArea.getId())) {
                                    continue;
                                }
                                componentAssembleArea.setShowOrder(componentAssembleArea.getShowOrder() + 1);
                                getService(ComponentAssembleAreaService.class).save(componentAssembleArea);
                            }
                        } else {
                            // 向下
                            List<ComponentAssembleArea> componentAssembleAreaList = getService(ComponentAssembleAreaService.class)
                                    .getComponentAssembleAreaListByShowOrder(startComponentAssembleArea.getShowOrder(),
                                            endComponentAssembleArea.getShowOrder(), startComponentAssembleArea.getParentId());
                            startComponentAssembleArea.setShowOrder(endComponentAssembleArea.getShowOrder() - 1);
                            getService(ComponentAssembleAreaService.class).save(startComponentAssembleArea);
                            for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                                if (componentAssembleArea.getId().equals(startComponentAssembleArea.getId())
                                        || componentAssembleArea.getId().equals(endComponentAssembleArea.getId())) {
                                    continue;
                                }
                                componentAssembleArea.setShowOrder(componentAssembleArea.getShowOrder() - 1);
                                getService(ComponentAssembleAreaService.class).save(componentAssembleArea);
                            }
                        }
                    } else {
                        // 拖动到的节点是构件节点，证明是向下拖动的，设置最大顺序
                        startComponentAssembleArea.setShowOrder(getService(ComponentAssembleAreaService.class).getMaxShowOrder(
                                startComponentAssembleArea.getParentId()) + 1);
                        getService(ComponentAssembleAreaService.class).save(startComponentAssembleArea);
                    }
                }
                setReturnData("排序成功!");
            } else {
                String oldParentAreaId = startComponentAssembleArea.getParentId();
                // 拖动到不同的父分类中
                startComponentAssembleArea.setParentId(targetId);
                if (StringUtil.isNotEmpty(end)) {
                    ComponentAssembleArea endComponentAssembleArea = getService(ComponentAssembleAreaService.class).getByID(end);
                    if (endComponentAssembleArea != null) {
                        // 拖动到的节点是构件分类节点
                        startComponentAssembleArea.setShowOrder(endComponentAssembleArea.getShowOrder());
                        getService(ComponentAssembleAreaService.class).save(startComponentAssembleArea);
                        getService(ComponentAssembleAreaService.class).updateShowOrderPlusOne(endComponentAssembleArea.getShowOrder(), targetId);
                    } else {
                        // 拖动到的节点是构件节点，证明是向下拖动的，设置最大顺序
                        startComponentAssembleArea.setShowOrder(getService(ComponentAssembleAreaService.class).getMaxShowOrder(
                                startComponentAssembleArea.getParentId()) + 1);
                        getService(ComponentAssembleAreaService.class).save(startComponentAssembleArea);
                    }
                } else {
                    Integer maxShowOrder = getService(ComponentAssembleAreaService.class).getMaxShowOrder(targetId);
                    int showOrder = 0;
                    if (maxShowOrder == null) {
                        showOrder = 1;
                    } else {
                        showOrder = maxShowOrder + 1;
                    }
                    startComponentAssembleArea.setShowOrder(showOrder);
                    getService(ComponentAssembleAreaService.class).save(startComponentAssembleArea);
                    if (!"-1".endsWith(targetId)) {
                        ComponentAssembleArea parent = getService(ComponentAssembleAreaService.class).getByID(targetId);
                        if (!parent.getHasChild()) {
                            parent.setHasChild(true);
                            getService(ComponentAssembleAreaService.class).save(parent);
                        }
                    }
                }
                // 查询拖动分类原来的父分类中是否还有子分类，如果没有，将该分类hasChild设置成false
                List<ComponentAssembleArea> oldChildAreaList = getService(ComponentAssembleAreaService.class).getComponentAssembleAreaListByParentId(
                        oldParentAreaId);
                if (CollectionUtils.isEmpty(oldChildAreaList)) {
                    ComponentAssembleArea oldParentComponentAssembleArea = getService(ComponentAssembleAreaService.class).getByID(oldParentAreaId);
                    oldParentComponentAssembleArea.setHasChild(false);
                    getService(ComponentAssembleAreaService.class).save(oldParentComponentAssembleArea);
                }
                setReturnData("改变父构件分类成功!");
            }
        } else {
            // 拖动的节点是构件节点
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(start);
            if (componentVersion != null) {
                // 同个父分类中拖动排序，不用排序；不同父分类中拖动，是改变分类
                if (!componentVersion.getAreaId().equals(targetId)) {
                    componentVersion.setAreaId(targetId);
                    getService(ComponentVersionService.class).save(componentVersion);
                    setReturnData("改变构件分类成功!");
                }
            }
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取公用预留区和构件预留区的树（复制的时候也用）
     * 
     * @return Object
     */
    public Object getReserveZoneTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeNodeId = getId();
        String assembleComponentVersionId = getParameter("assembleComponentVersionId");
        String sourceReserveZoneId = getParameter("sourceReserveZoneId");
        String reserveZoneType = null;
        if (StringUtil.isNotEmpty(sourceReserveZoneId)) {
            ComponentReserveZone componentReserveZone = getService(ComponentReserveZoneService.class).getByID(sourceReserveZoneId);
            reserveZoneType = componentReserveZone.getType();
        }
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("Common".equals(treeNodeId)) {
            if (StringUtil.isNotEmpty(assembleComponentVersionId)) {
                Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(assembleComponentVersionId);
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                    Module module = getService(ModuleService.class).findByComponentVersionId(baseComponentVersion.getId());
                    List<ComponentReserveZone> commonReserveZoneList = AppDefineUtil.getCommonReserveZone(module, reserveZoneType);
                    if (CollectionUtils.isNotEmpty(commonReserveZoneList)) {
                        for (ComponentReserveZone temp : commonReserveZoneList) {
                            if (StringUtil.isNotEmpty(reserveZoneType) && !temp.getType().equals(reserveZoneType)) {
                                continue;
                            }
                            if (StringUtil.isNotEmpty(sourceReserveZoneId) && temp.getId().equals(sourceReserveZoneId)) {
                                continue;
                            }
                            treeNode = new DhtmlxTreeNode();
                            treeNode.setId(temp.getId());
                            treeNode.setText(temp.getAlias());
                            treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                            treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                            treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                            treeNode.setChild("0");
                            // 预留区类型
                            treeNode.setProp0(temp.getType());
                            // 预留区名称
                            treeNode.setProp1(temp.getName());
                            treeNodelist.add(treeNode);
                        }
                    }
                }
                List<ComponentReserveZone> componentReserveZoneList = getService(ComponentReserveZoneService.class).getByComponentVersionId(
                        construct.getBaseComponentVersionId());
                if (CollectionUtils.isNotEmpty(componentReserveZoneList)) {
                    for (ComponentReserveZone componentReserveZone : componentReserveZoneList) {
                        if (StringUtil.isNotEmpty(reserveZoneType) && !componentReserveZone.getType().equals(reserveZoneType)) {
                            continue;
                        }
                        if (StringUtil.isNotEmpty(sourceReserveZoneId) && componentReserveZone.getId().equals(sourceReserveZoneId)) {
                            continue;
                        }
                        treeNode = new DhtmlxTreeNode();
                        treeNode.setId(componentReserveZone.getId());
                        treeNode.setText(componentReserveZone.getAlias());
                        treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setChild("0");
                        // 预留区类型
                        treeNode.setProp0(componentReserveZone.getType());
                        // 预留区名称
                        treeNode.setProp1(componentReserveZone.getName());
                        treeNodelist.add(treeNode);
                    }
                }
            } else {
                List<ComponentReserveZone> commonReserveZoneList = getService(ComponentReserveZoneService.class).getAllCommonReserveZone();
                String sourceReserveZoneType = null;
                if (StringUtil.isNotEmpty(sourceReserveZoneId)) {
                    ComponentReserveZone sourceReserveZone = getService(ComponentReserveZoneService.class).getByID(sourceReserveZoneId);
                    if (sourceReserveZone != null) {
                        sourceReserveZoneType = AppDefineUtil.getZoneType(sourceReserveZone.getName(), sourceReserveZone.getIsCommon());
                    }
                }
                if (CollectionUtils.isNotEmpty(commonReserveZoneList)) {
                    for (ComponentReserveZone temp : commonReserveZoneList) {
                        if (StringUtil.isNotEmpty(reserveZoneType) && !temp.getType().equals(reserveZoneType)) {
                            continue;
                        }
                        if (StringUtil.isNotEmpty(sourceReserveZoneId) && temp.getId().equals(sourceReserveZoneId)) {
                            continue;
                        }
                        if (StringUtil.isNotEmpty(sourceReserveZoneType)
                                && !sourceReserveZoneType.equals(AppDefineUtil.getZoneType(temp.getName(), temp.getIsCommon()))) {
                            continue;
                        }
                        treeNode = new DhtmlxTreeNode();
                        treeNode.setId(temp.getId());
                        treeNode.setText(temp.getAlias());
                        treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setChild("0");
                        // 预留区类型
                        treeNode.setProp0(temp.getType());
                        // 预留区名称
                        treeNode.setProp1(temp.getName());
                        treeNodelist.add(treeNode);
                    }
                }
            }
        } else if ("Component".equals(treeNodeId)) {
            Construct construct = getService().getByAssembleComponentVersionId(assembleComponentVersionId);
            if (construct != null) {
                List<ComponentReserveZone> componentReserveZoneList = getService(ComponentReserveZoneService.class).getByComponentVersionId(
                        construct.getBaseComponentVersionId());
                if (CollectionUtils.isNotEmpty(componentReserveZoneList)) {
                    for (ComponentReserveZone componentReserveZone : componentReserveZoneList) {
                        if (StringUtil.isNotEmpty(reserveZoneType) && !componentReserveZone.getType().equals(reserveZoneType)) {
                            continue;
                        }
                        if (StringUtil.isNotEmpty(sourceReserveZoneId) && componentReserveZone.getId().equals(sourceReserveZoneId)) {
                            continue;
                        }
                        treeNode = new DhtmlxTreeNode();
                        treeNode.setId(componentReserveZone.getId());
                        treeNode.setText(componentReserveZone.getAlias());
                        treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setChild("0");
                        // 预留区类型
                        treeNode.setProp0(componentReserveZone.getType());
                        // 预留区名称
                        treeNode.setProp1(componentReserveZone.getName());
                        treeNodelist.add(treeNode);
                    }
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 组合构件的预览预操作
     * 
     * @return Object
     */
    public Object preview() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String assembleComponentVersionId = getParameter("assembleComponentVersionId");
        ComponentVersion assembleComponentVersion = getService(ComponentVersionService.class).getByID(assembleComponentVersionId);
        if (CfgCommonUtil.isReleasedSystem()) {
            String previewUrl = request.getContextPath() + "/cfg-resource/" + assembleComponentVersion.getViews() + "/views/"
                    + assembleComponentVersion.getUrl();
            if (previewUrl.indexOf("?") == -1) {
                previewUrl += "?componentVersionId=" + assembleComponentVersionId;
            } else {
                previewUrl += "&componentVersionId=" + assembleComponentVersionId;
            }
            previewUrl += "&bindingType=menu";
            previewUrl += "&topComVersionId=" + assembleComponentVersionId;
            setReturnData("{'status':true,'url':'" + previewUrl + "'}");
        } else {
            // 查找该组合构件关联的所有构件
            Set<ComponentVersion> componentVersionSet = getService().getComponentVersionOfConstruct(assembleComponentVersionId);
            // 是否使用预览工程进行预览，如果预览的构件的相关构件全部是自定义构件，则可以在本系统预览，否则在预览系统预览
            boolean usePreviewProject = false;
            if (CollectionUtils.isNotEmpty(componentVersionSet)) {
                for (ComponentVersion componentVersion : componentVersionSet) {
                    if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.LOGIC.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                        usePreviewProject = true;
                        break;
                    }
                }
            }
            if (usePreviewProject) {
                try {
                    getService().preview(assembleComponentVersionId);
                } catch (RuntimeException e) {
                    setReturnData("{'status':false,'message':'预览预操作发生异常！'}");
                    return new DefaultHttpHeaders(SUCCESS).disableCaching();
                }
                boolean reloadStatus = PreviewUtil.restartPreviewProject();
                if (reloadStatus) {
                    String previewUrl = ComponentFileUtil.getPreviewUrl() + "cfg-resource/" + assembleComponentVersion.getViews() + "/views/"
                            + assembleComponentVersion.getUrl();
                    if (previewUrl.indexOf("?") == -1) {
                        previewUrl += "?componentVersionId=" + assembleComponentVersionId;
                    } else {
                        previewUrl += "&componentVersionId=" + assembleComponentVersionId;
                    }
                    previewUrl += "&bindingType=menu";
                    previewUrl += "&topComVersionId=" + assembleComponentVersionId;
                    setReturnData("{'status':true,'url':'" + previewUrl + "'}");
                } else {
                    setReturnData("{'status':false,'message':'预览系统未启动或重启失败！'}");
                }
            } else {
                String previewUrl = request.getContextPath() + "/cfg-resource/" + assembleComponentVersion.getViews() + "/views/"
                        + assembleComponentVersion.getUrl();
                if (previewUrl.indexOf("?") == -1) {
                    previewUrl += "?componentVersionId=" + assembleComponentVersionId;
                } else {
                    previewUrl += "&componentVersionId=" + assembleComponentVersionId;
                }
                previewUrl += "&bindingType=menu";
                previewUrl += "&topComVersionId=" + assembleComponentVersionId;
                setReturnData("{'status':true,'url':'" + previewUrl + "'}");
            }
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 刚预览过的组合构件再次预览操作，不需要将构件发布到预览系统
     * 
     * @return Object
     */
    public Object repeatPreview() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String assembleComponentVersionId = getParameter("assembleComponentVersionId");
        ComponentVersion assembleComponentVersion = getService(ComponentVersionService.class).getByID(assembleComponentVersionId);
        String previewUrl = null;
        if (CfgCommonUtil.isReleasedSystem()) {
            previewUrl = request.getContextPath() + "/cfg-resource/" + assembleComponentVersion.getViews() + "/views/" + assembleComponentVersion.getUrl();
            if (previewUrl.indexOf("?") == -1) {
                previewUrl += "?componentVersionId=" + assembleComponentVersionId;
            } else {
                previewUrl += "&componentVersionId=" + assembleComponentVersionId;
            }
        } else {
            // 查找该组合构件关联的所有构件
            Set<ComponentVersion> componentVersionSet = getService().getComponentVersionOfConstruct(assembleComponentVersionId);
            // 是否使用预览工程进行预览，如果预览的构件的相关构件全部是自定义构件，则可以在本系统预览，否则在预览系统预览
            boolean usePreviewProject = false;
            if (CollectionUtils.isNotEmpty(componentVersionSet)) {
                for (ComponentVersion componentVersion : componentVersionSet) {
                    if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.LOGIC.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                        usePreviewProject = true;
                        break;
                    }
                }
            }
            if (usePreviewProject) {
                previewUrl = ComponentFileUtil.getPreviewUrl() + "cfg-resource/" + assembleComponentVersion.getViews() + "/views/"
                        + assembleComponentVersion.getUrl();
            } else {
                previewUrl = request.getContextPath() + "/cfg-resource/" + assembleComponentVersion.getViews() + "/views/" + assembleComponentVersion.getUrl();
            }
        }
        if (previewUrl.indexOf("?") == -1) {
            previewUrl += "?componentVersionId=" + assembleComponentVersionId;
        } else {
            previewUrl += "&componentVersionId=" + assembleComponentVersionId;
        }
        previewUrl += "&bindingType=menu";
        previewUrl += "&topComVersionId=" + assembleComponentVersionId;
        previewUrl += ComponentParamsUtil.getParamsOfComponent(null, assembleComponentVersion);
        setReturnData("{'status':true,'url':'" + previewUrl + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 刚预览过的组合构件再次预览操作（带导航条），不需要将构件发布到预览系统
     * 
     * @return Object
     */
    public Object navigationPreview() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String assembleComponentVersionId = getParameter("assembleComponentVersionId");
        ComponentVersion assembleComponentVersion = getService(ComponentVersionService.class).getByID(assembleComponentVersionId);
        String previewUrl = null;
        if (CfgCommonUtil.isReleasedSystem()) {
            previewUrl = request.getContextPath() + "/cfg-resource/" + assembleComponentVersion.getViews()
                    + "/views/config/navigation.jsp?CFG_componentUrl=/cfg-resource/" + assembleComponentVersion.getViews() + "/views/"
                    + assembleComponentVersion.getUrl();
            if (assembleComponentVersion.getUrl().indexOf("?") == -1) {
                previewUrl += "?componentVersionId=" + assembleComponentVersionId;
            } else {
                previewUrl += "&componentVersionId=" + assembleComponentVersionId;
            }
        } else {
            // 查找该组合构件关联的所有构件
            Set<ComponentVersion> componentVersionSet = getService().getComponentVersionOfConstruct(assembleComponentVersionId);
            // 是否使用预览工程进行预览，如果预览的构件的相关构件全部是自定义构件，则可以在本系统预览，否则在预览系统预览
            boolean usePreviewProject = false;
            if (CollectionUtils.isNotEmpty(componentVersionSet)) {
                for (ComponentVersion componentVersion : componentVersionSet) {
                    if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.LOGIC.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                        usePreviewProject = true;
                        break;
                    }
                }
            }
            if (usePreviewProject) {
                previewUrl = ComponentFileUtil.getPreviewUrl() + "cfg-resource/" + assembleComponentVersion.getViews()
                        + "/views/config/navigation.jsp?CFG_componentUrl=/cfg-resource/" + assembleComponentVersion.getViews() + "/views/"
                        + assembleComponentVersion.getUrl();
            } else {
                previewUrl = request.getContextPath() + "/cfg-resource/" + assembleComponentVersion.getViews()
                        + "/views/config/navigation.jsp?CFG_componentUrl=/cfg-resource/" + assembleComponentVersion.getViews() + "/views/"
                        + assembleComponentVersion.getUrl();
            }
            if (assembleComponentVersion.getUrl().indexOf("?") == -1) {
                previewUrl += "?componentVersionId=" + assembleComponentVersionId;
            } else {
                previewUrl += "&componentVersionId=" + assembleComponentVersionId;
            }
        }
        previewUrl += "&bindingType=menu";
        previewUrl += "&topComVersionId=" + assembleComponentVersionId;
        previewUrl += ComponentParamsUtil.getParamsOfComponent(null, assembleComponentVersion);
        setReturnData("{'status':true,'url':'" + previewUrl + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 刚预览过的组合构件再次预览操作（带标签页），不需要将构件发布到预览系统
     * 
     * @return Object
     */
    public Object tabPreview() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String assembleComponentVersionId = getParameter("assembleComponentVersionId");
        ComponentVersion assembleComponentVersion = getService(ComponentVersionService.class).getByID(assembleComponentVersionId);
        String previewUrl = null;
        if (CfgCommonUtil.isReleasedSystem()) {
            previewUrl = request.getContextPath() + "/cfg-resource/" + assembleComponentVersion.getViews()
                    + "/views/config/tabpreview.jsp?CFG_componentUrl=/cfg-resource/" + assembleComponentVersion.getViews() + "/views/"
                    + assembleComponentVersion.getUrl();
        } else {
            // 查找该组合构件关联的所有构件
            Set<ComponentVersion> componentVersionSet = getService().getComponentVersionOfConstruct(assembleComponentVersionId);
            // 是否使用预览工程进行预览，如果预览的构件的相关构件全部是自定义构件，则可以在本系统预览，否则在预览系统预览
            boolean usePreviewProject = false;
            if (CollectionUtils.isNotEmpty(componentVersionSet)) {
                for (ComponentVersion componentVersion : componentVersionSet) {
                    if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.LOGIC.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                        usePreviewProject = true;
                        break;
                    }
                }
            }
            if (usePreviewProject) {
                previewUrl = ComponentFileUtil.getPreviewUrl() + "cfg-resource/" + assembleComponentVersion.getViews()
                        + "/views/config/tabpreview.jsp?CFG_componentUrl=/cfg-resource/" + assembleComponentVersion.getViews() + "/views/"
                        + assembleComponentVersion.getUrl();
            } else {
                previewUrl = request.getContextPath() + "/cfg-resource/" + assembleComponentVersion.getViews()
                        + "/views/config/tabpreview.jsp?CFG_componentUrl=/cfg-resource/" + assembleComponentVersion.getViews() + "/views/"
                        + assembleComponentVersion.getUrl();
            }
        }
        if (assembleComponentVersion.getUrl().indexOf("?") == -1) {
            previewUrl += "?componentVersionId=" + assembleComponentVersionId;
        } else {
            previewUrl += "&componentVersionId=" + assembleComponentVersionId;
        }
        previewUrl += "&bindingType=menu";
        previewUrl += "&topComVersionId=" + assembleComponentVersionId;
        previewUrl += ComponentParamsUtil.getParamsOfComponent(null, assembleComponentVersion);
        setReturnData("{'status':true,'url':'" + previewUrl + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取模块构件配置信息
     * 
     * @return Object
     */
    public Object getConfigInfo() {
        String constructId = getParameter("constructId");
        String constructDetailId = getParameter("constructDetailId");
        // String page = getParameter("page");
        String componentVersionId = getParameter("componentVersionId");
        String menuId = getParameter("menuId");
        // 配置Map
        Map<String, Object> configMap = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(constructDetailId)) {
            ConstructDetail constructDetail = getService(ConstructDetailService.class).getByID(constructDetailId);
            // 参数配置
            Map<String, Object> paramMap = new HashMap<String, Object>();
            configMap.put("params", paramMap);
            // 系统参数配置
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
            paramMap.put("systemParams", ComponentParamsUtil.getSystemParamMapOfComponent(componentVersion));
            // 构件自身参数
            paramMap.put("selfParams", ComponentParamsUtil.getSelfParamMapOfConstructDetail(constructDetailId));
            if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())) {
                // 构件中的权限按钮
                List<String> notAuthorityComponentButtons = AuthorityUtil.getInstance().getPageComponentButtonAuthority(menuId, componentVersion.getId());
                configMap.put("notAuthorityComponentButtons", notAuthorityComponentButtons);
            }
            configMap.put("assembleType", constructDetail.getAssembleType());
        } else if (StringUtil.isNotEmpty(constructId)) {
            Construct construct = getService().getByID(constructId);
            // 参数配置
            Map<String, Object> paramMap = new HashMap<String, Object>();
            configMap.put("params", paramMap);
            // 系统参数配置
            ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
            paramMap.put("systemParams", ComponentParamsUtil.getSystemParamMapOfComponent(baseComponentVersion));
            // 构件自身参数
            if (StringUtil.isNotEmpty(menuId)) {
                paramMap.put("selfParams", ComponentParamsUtil.getSelfParamMapOfMenu(menuId));
            } else {
                paramMap.put("selfParams", ComponentParamsUtil.getSelfParamMapOfConstruct(constructId));
            }
            // 构件入参
            if (StringUtil.isNotEmpty(menuId)) {
                paramMap.put("inputParams", ComponentParamsUtil.getInputParamMapOfMenu(menuId));
            } else {
                paramMap.put("inputParams", ComponentParamsUtil.getInputParamMapOfConstruct(constructId));
            }
            if (ConstantVar.Component.Type.PAGE.equals(baseComponentVersion.getComponent().getType())) {
                // 构件中的权限按钮
                List<String> notAuthorityComponentButtons = AuthorityUtil.getInstance().getPageComponentButtonAuthority(menuId, baseComponentVersion.getId());
                configMap.put("notAuthorityComponentButtons", notAuthorityComponentButtons);
            }
        } else if (StringUtil.isNotEmpty(componentVersionId)) {
            // 构件库模块预览
            // 参数配置
            Map<String, Object> paramMap = new HashMap<String, Object>();
            configMap.put("params", paramMap);
            // 系统参数配置
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            paramMap.put("systemParams", ComponentParamsUtil.getSystemParamMapOfComponent(componentVersion));
            // 构件自身参数
            if (StringUtil.isNotEmpty(menuId)) {
                paramMap.put("selfParams", ComponentParamsUtil.getSelfParamMapOfMenu(menuId));
            } else {
                paramMap.put("selfParams", ComponentParamsUtil.getSelfParamMapOfComponent(componentVersionId));
            }
            if (StringUtil.isNotEmpty(menuId)) {
                paramMap.put("inputParams", ComponentParamsUtil.getInputParamMapOfMenu(menuId));
            } else {
                paramMap.put("inputParams", ComponentParamsUtil.getInputParamMapOfComponent(componentVersionId));
            }
            if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())) {
                // 构件中的权限按钮
                List<String> notAuthorityComponentButtons = AuthorityUtil.getInstance().getPageComponentButtonAuthority(menuId, componentVersionId);
                configMap.put("notAuthorityComponentButtons", notAuthorityComponentButtons);
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String config = objectMapper.writeValueAsString(configMap);
            setReturnData(config);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取预留区配置信息
     * 
     * @return Object
     */
    public Object getReverseZoneConfigInfo() {
        String constructId = getParameter("constructId");
        String page = getParameter("page");
        String reserveZoneName = getParameter("reserveZoneName");
        String menuId = getParameter("menuId");
        Construct construct = getService().getByID(constructId);
        Map<String, Object> reserveZoneMap = new HashMap<String, Object>();
        List<Map<String, Object>> constructDetails = new ArrayList<Map<String, Object>>();
        if (construct != null) {
            boolean isCommon = false;
            // 只有基础构件是逻辑表构件的，使用公用预留区
            ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
            if (baseComponentVersion != null && ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType()) && 
            		!(reserveZoneName.startsWith("MT_zone_") && reserveZoneName.length() == 40)) {//过滤逻辑表中的文本框+按钮
                isCommon = true;
            }
            String reserveZoneId = null;
            ComponentReserveZone componentReserveZone = null;
            if ("TREE".equals(reserveZoneName)) {
                reserveZoneId = "TREE";
                reserveZoneMap.put("name", "TREE");
                reserveZoneMap.put("type", "TREE");
            } else {
                if (isCommon) {
                    if (reserveZoneName.startsWith(AppDefineUtil.RZ_NAME_PRE)) {
                        String[] strs = reserveZoneName.split("_");
                        String tableId = strs[2];
                        // 类型 0-表单 1-列表
                        String contentType = strs[3];
                        PhysicalTableDefine physicalTableDefine = getService(PhysicalTableDefineService.class).getByID(tableId);
                        if (physicalTableDefine != null && StringUtil.isNotEmpty(physicalTableDefine.getLogicTableCode())) {
                            if (reserveZoneName.endsWith("LINK")) {
                                reserveZoneName = physicalTableDefine.getLogicTableCode() + "_GRID_LINK";
                            } else {
                                if (AppDefineUtil.L_GRID.equals(contentType)) {
                                    reserveZoneName = physicalTableDefine.getLogicTableCode() + "_GRID";
                                } else if (AppDefineUtil.L_FORM.equals(contentType)) {
                                    reserveZoneName = physicalTableDefine.getLogicTableCode() + "_FORM";
                                }
                            }
                        }
                    }
                    componentReserveZone = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(reserveZoneName);
                } else {
                    componentReserveZone = getService(ComponentReserveZoneService.class).getByComponentVersionIdAndNameAndPage(
                            construct.getBaseComponentVersionId(), reserveZoneName, page);
                }
                reserveZoneId = componentReserveZone.getId();
                reserveZoneMap.put("name", componentReserveZone.getName());
                reserveZoneMap.put("type", componentReserveZone.getType());
            }
            if (StringUtil.isNotEmpty(reserveZoneId)) {
                String contextPath = ServletActionContext.getRequest().getContextPath();
                // 预留区和构件绑定关系
                List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructIdAndReserveZoneId(construct.getId(),
                        reserveZoneId);
                if (CollectionUtils.isNotEmpty(constructDetailList)) {
                    // 权限排除掉的按钮
                    List<String> notAuthorityConstructButtons = AuthorityUtil.getInstance().getConstructButtonAuthority(menuId,
                            construct.getAssembleComponentVersion().getId());
                    Map<String, Object> constructDetailMap = null;
                    ComponentVersion bindingComponentVersion = null;
                    for (ConstructDetail constructDetail : constructDetailList) {
                        if (CollectionUtils.isNotEmpty(notAuthorityConstructButtons)) {
                            if (notAuthorityConstructButtons.contains(constructDetail.getId())) {
                                continue;
                            }
                        }
                        constructDetailMap = new HashMap<String, Object>();
                        constructDetailMap.put("constructDetailId", constructDetail.getId());
                        constructDetailMap.put("buttonCode", constructDetail.getButtonCode());
                        constructDetailMap.put("buttonName", constructDetail.getButtonName());
                        constructDetailMap.put("buttonDisplayName", constructDetail.getButtonDisplayName());
                        constructDetailMap.put("buttonType", constructDetail.getButtonType());
                        constructDetailMap.put("parentButtonCode", constructDetail.getParentButtonCode());
                        constructDetailMap.put("buttonCls", constructDetail.getButtonCls());
                        constructDetailMap.put("buttonIcon", constructDetail.getButtonIcon());
                        constructDetailMap.put("buttonSource", constructDetail.getButtonSource());
                        constructDetailMap.put("position", constructDetail.getPosition());
                        constructDetailMap.put("assembleType", constructDetail.getAssembleType());
                        constructDetailMap.put("treeNodeType", constructDetail.getTreeNodeType());
                        constructDetailMap.put("treeNodeProperty", constructDetail.getTreeNodeProperty());
                        if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
                            bindingComponentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                            constructDetailMap.put("componentType", bindingComponentVersion.getComponent().getType());
                            // 构件访问地址
                            if (ConstantVar.Component.Type.PAGE.equals(bindingComponentVersion.getComponent().getType())
                                    || ConstantVar.Component.Type.SELF_DEFINE.indexOf(bindingComponentVersion.getComponent().getType()) != -1
                                    || ConstantVar.Component.Type.TAB.equals(bindingComponentVersion.getComponent().getType())
                                    || ConstantVar.Component.Type.ASSEMBLY.equals(bindingComponentVersion.getComponent().getType())) {
                                String url = contextPath + "/cfg-resource/" + bindingComponentVersion.getViews() + "/views/" + bindingComponentVersion.getUrl();
                                String paramsOfComponent = ComponentParamsUtil.getParamsOfComponent(constructDetail.getId(), bindingComponentVersion);
                                if (StringUtil.isNotEmpty(paramsOfComponent)) {
                                    if (url.indexOf("?") == -1) {
                                        url += "?" + paramsOfComponent.substring(1);
                                    } else {
                                        url += paramsOfComponent;
                                    }
                                }
                                if (url.indexOf("?") == -1) {
                                    url += "?componentVersionId=" + constructDetail.getComponentVersionId();
                                } else {
                                    url += "&componentVersionId=" + constructDetail.getComponentVersionId();
                                }
                                constructDetailMap.put("bindingComponentUrl", url);
                            } else if (ConstantVar.Component.Type.LOGIC.equals(bindingComponentVersion.getComponent().getType())) {
                                String url = contextPath + "/" + bindingComponentVersion.getUrl();
                                if (url.indexOf("?") == -1) {
                                    url += "?componentVersionId=" + constructDetail.getComponentVersionId();
                                } else {
                                    url += "&componentVersionId=" + constructDetail.getComponentVersionId();
                                }
                                constructDetailMap.put("bindingComponentUrl", url);
                            }
                            constructDetailMap.put("width", constructDetail.getWidth());
                            constructDetailMap.put("height", constructDetail.getHeight());
                            constructDetailMap.put("beforeClickJs", constructDetail.getBeforeClickJs());
                            constructDetailMap.put("searchComboOptions", constructDetail.getSearchComboOptions());
                            // 预留区绑定的构件和页面方法的绑定关系
                            constructDetailMap.put("paramFunctions", ComponentParamsUtil.getParamFunctions(constructDetail.getId()));
                            // 构件调用的回调方法
                            constructDetailMap.put("paramCallbacks", ComponentParamsUtil.getParamCallbacks(constructDetail.getId()));
                            // 构件的输入参数，标签页构件绑定该构件时使用
                            if (baseComponentVersion != null && ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())) {
                                if (ConstantVar.Component.Type.ASSEMBLY.equals(bindingComponentVersion.getComponent().getType())) {
                                    Construct bindingConstruct = getService(ConstructService.class).getByAssembleComponentVersionId(
                                            bindingComponentVersion.getId());
                                    constructDetailMap.put("inputParams",
                                            ComponentParamsUtil.getInputParamMapOfComponent(bindingConstruct.getBaseComponentVersionId()));
                                } else {
                                    constructDetailMap.put("inputParams", ComponentParamsUtil.getInputParamMapOfComponent(bindingComponentVersion.getId()));
                                }
                            }
                            if (ConstantVar.Component.Type.ASSEMBLY.equals(bindingComponentVersion.getComponent().getType())) {
                                Construct bindingConstruct = getService(ConstructService.class)
                                        .getByAssembleComponentVersionId(bindingComponentVersion.getId());
                                ComponentVersion bindingBaseComponentVersion = getService(ComponentVersionService.class).getByID(
                                        bindingConstruct.getBaseComponentVersionId());
                                constructDetailMap.put("baseComponentType", bindingBaseComponentVersion.getComponent().getType());
                                // 中转器构件，获取其下绑定的构件
                                if (ConstantVar.Component.Type.TRANSFER_DEVICE.equals(bindingBaseComponentVersion.getComponent().getType())) {
                                    List<ConstructDetail> transferDeviceConstructDetailList = getService(ConstructDetailService.class).getByConstructId(
                                            bindingConstruct.getId());
                                    if (CollectionUtils.isNotEmpty(transferDeviceConstructDetailList)) {
                                        List<Map<String, Object>> transferDeviceConstructDetails = new ArrayList<Map<String, Object>>();
                                        Map<String, Object> transferDeviceConstructDetailMap = null;
                                        ComponentVersion secondBindingComponentVersion = null;
                                        for (ConstructDetail transferDeviceConstructDetail : transferDeviceConstructDetailList) {
                                            transferDeviceConstructDetailMap = new HashMap<String, Object>();
                                            transferDeviceConstructDetailMap.put("constructDetailId", transferDeviceConstructDetail.getId());
                                            transferDeviceConstructDetailMap.put("buttonCode", transferDeviceConstructDetail.getButtonCode());
                                            if (StringUtil.isNotEmpty(transferDeviceConstructDetail.getComponentVersionId())) {
                                                secondBindingComponentVersion = getService(ComponentVersionService.class).getByID(
                                                        transferDeviceConstructDetail.getComponentVersionId());
                                                transferDeviceConstructDetailMap.put("componentType", secondBindingComponentVersion.getComponent().getType());
                                                // 构件访问地址
                                                if (ConstantVar.Component.Type.PAGE.equals(secondBindingComponentVersion.getComponent().getType())
                                                        || ConstantVar.Component.Type.SELF_DEFINE.indexOf(secondBindingComponentVersion.getComponent()
                                                                .getType()) != -1
                                                        || ConstantVar.Component.Type.ASSEMBLY.equals(secondBindingComponentVersion.getComponent().getType())) {
                                                    String url = contextPath + "/cfg-resource/" + secondBindingComponentVersion.getViews() + "/views/"
                                                            + secondBindingComponentVersion.getUrl();
                                                    String paramsOfComponent = ComponentParamsUtil.getParamsOfComponent(transferDeviceConstructDetail.getId(),
                                                            secondBindingComponentVersion);
                                                    if (StringUtil.isNotEmpty(paramsOfComponent)) {
                                                        if (url.indexOf("?") == -1) {
                                                            url += "?" + paramsOfComponent.substring(1);
                                                        } else {
                                                            url += paramsOfComponent;
                                                        }
                                                    }
                                                    if (url.indexOf("?") == -1) {
                                                        url += "?componentVersionId=" + transferDeviceConstructDetail.getComponentVersionId();
                                                    } else {
                                                        url += "&componentVersionId=" + transferDeviceConstructDetail.getComponentVersionId();
                                                    }
                                                    transferDeviceConstructDetailMap.put("bindingComponentUrl", url);
                                                } else if (ConstantVar.Component.Type.LOGIC.equals(secondBindingComponentVersion.getComponent().getType())) {
                                                    String url = contextPath + "/" + secondBindingComponentVersion.getUrl();
                                                    if (url.indexOf("?") == -1) {
                                                        url += "?componentVersionId=" + transferDeviceConstructDetail.getComponentVersionId();
                                                    } else {
                                                        url += "&componentVersionId=" + transferDeviceConstructDetail.getComponentVersionId();
                                                    }
                                                    transferDeviceConstructDetailMap.put("bindingComponentUrl", url);
                                                }
                                                transferDeviceConstructDetailMap.put("assembleType", transferDeviceConstructDetail.getAssembleType());
                                                transferDeviceConstructDetailMap.put("beforeClickJs", transferDeviceConstructDetail.getBeforeClickJs());
                                            }
                                            transferDeviceConstructDetails.add(transferDeviceConstructDetailMap);
                                        }
                                        constructDetailMap.put("constructDetails", transferDeviceConstructDetails);
                                    }
                                }
                            }
                        }
                        constructDetails.add(constructDetailMap);
                    }
                }
                reserveZoneMap.put("constructDetails", constructDetails);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String config = objectMapper.writeValueAsString(reserveZoneMap);
            setReturnData(config);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取开发的构件树(权限中使用)
     * 
     * @return Object
     */
    public Object getDevelopComponentTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        boolean open = BooleanUtils.toBoolean(getParameter("P_OPEN"));
        String componentVersionId = getParameter("componentVersionId");
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeNodeId)) {
            List<ComponentVersion> componentVersionList = new ArrayList<ComponentVersion>();
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            if (componentVersion != null) {
                if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    componentVersionList.addAll(getService().getPageComponentVersionsOfConstruct(componentVersion.getId()));
                } else {
                    componentVersionList.add(componentVersion);
                }
            }
            if (CollectionUtils.isNotEmpty(componentVersionList)) {
                for (ComponentVersion tempCV : componentVersionList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(tempCV.getId());
                    treeNode.setText(tempCV.getComponent().getAlias() + "_" + componentVersion.getVersion());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("0");
                    treeNode.setProp0(tempCV.getId());
                    treeNode.setOpen(open);
                    treeNodelist.add(treeNode);
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取组合构件树(权限中使用)
     * 
     * @return Object
     */
    public Object getAssemblyComponentTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        boolean open = BooleanUtils.toBoolean(getParameter("P_OPEN"));
        String assembleComponentVersionId = getParameter("assembleComponentVersionId");
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeNodeId)) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(assembleComponentVersionId);
            if (componentVersion != null) {
                treeNode = new DhtmlxTreeNode();
                treeNode.setId(componentVersion.getId());
                treeNode.setText(componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion());
                treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                treeNode.setChild("1");
                treeNode.setOpen(open);
                Construct construct = getService().getByAssembleComponentVersionId(assembleComponentVersionId);
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())) {
                    treeNode.setProp0("TREE");
                }
                treeNodelist.add(treeNode);
            }
        } else {
            List<ComponentVersion> componentVersionList = new ArrayList<ComponentVersion>();
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(treeNodeId);
            if (componentVersion != null) {
                componentVersionList.addAll(getService().getAssemblesOfConstruct(treeNodeId));
            }
            if (CollectionUtils.isNotEmpty(componentVersionList)) {
                for (ComponentVersion tempCV : componentVersionList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(tempCV.getId());
                    treeNode.setText(tempCV.getComponent().getAlias() + "_" + componentVersion.getVersion());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("1");
                    treeNode.setOpen(open);
                    Construct construct = getService().getByAssembleComponentVersionId(tempCV.getId());
                    ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                    if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())) {
                        treeNode.setProp0("TREE");
                    }
                    treeNodelist.add(treeNode);
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取组合构件中的按钮
     * 
     * @return Object
     */
    public Object getButtonsOfConstruct() {
        String componentVersionId = getParameter("componentVersionId");
        Construct construct = getService().getByAssembleComponentVersionId(componentVersionId);
        List<String[]> list = new ArrayList<String[]>();
        if (construct != null) {
            List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getConstructInfoByConstructId(construct.getId());
            if (CollectionUtils.isNotEmpty(constructDetailList)) {
                String[] strs = null;
                for (ConstructDetail constructDetail : constructDetailList) {
                    strs = new String[5];
                    strs[0] = constructDetail.getId();
                    strs[1] = constructDetail.getId();
                    strs[2] = StringUtil.null2empty(constructDetail.getReserveZoneAlias());
                    strs[3] = StringUtil.null2empty(constructDetail.getButtonDisplayName());
                    strs[4] = StringUtil.null2empty(constructDetail.getComponentAliasAndVersion());
                    list.add(strs);
                }
            }
        }
        setReturnData(list);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取组合构件中的按钮（绑定了构件的）
     * 
     * @return Object
     */
    public Object getComponentButtonsOfConstruct() {
        String componentVersionId = getParameter("componentVersionId");
        Construct construct = getService().getByAssembleComponentVersionId(componentVersionId);
        List<String[]> list = new ArrayList<String[]>();
        if (construct != null) {
            List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getConstructInfoByConstructId(construct.getId());
            if (CollectionUtils.isNotEmpty(constructDetailList)) {
                String[] strs = null;
                for (ConstructDetail constructDetail : constructDetailList) {
                    if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
                        strs = new String[5];
                        strs[0] = constructDetail.getId();
                        strs[1] = constructDetail.getId();
                        strs[2] = StringUtil.null2empty(constructDetail.getReserveZoneAlias());
                        strs[3] = StringUtil.null2empty(constructDetail.getButtonDisplayName());
                        strs[4] = StringUtil.null2empty(constructDetail.getComponentAliasAndVersion());
                        list.add(strs);
                    }
                }
            }
        }
        setReturnData(list);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取自定义构件树
     * 
     * @return Object
     */
    public Object getSelfDefineComponentTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        boolean open = BooleanUtils.toBoolean(getParameter("P_OPEN"));
        String componentVersionId = getParameter("componentVersionId");
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        Module module = null;
        if ("-1".equals(treeNodeId)) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            treeNode = new DhtmlxTreeNode();
            treeNode.setId(componentVersion.getId());
            treeNode.setText(componentVersion.getComponent().getAlias());
            treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
            treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
            treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                treeNode.setChild("1");
                treeNode.setOpen(open);
            } else {
                treeNode.setChild("0");
                treeNode.setProp0(ConstantVar.Component.Type.SELF_DEFINE);
                module = getService(ModuleService.class).findByComponentVersionId(componentVersionId);
                if (module != null) {
                    treeNode.setProp1(module.getId());
                }
            }
            treeNodelist.add(treeNode);
        } else {
            Construct construct = getService().getByAssembleComponentVersionId(treeNodeId);
            if (construct != null) {
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(baseComponentVersion.getComponent().getType()) != -1) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(baseComponentVersion.getId());
                    treeNode.setText(baseComponentVersion.getComponent().getAlias());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("0");
                    treeNode.setProp0(ConstantVar.Component.Type.SELF_DEFINE);
                    module = getService(ModuleService.class).findByComponentVersionId(baseComponentVersion.getId());
                    if (module != null) {
                        treeNode.setProp1(module.getId());
                    }
                    treeNodelist.add(treeNode);
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
                            treeNode = new DhtmlxTreeNode();
                            treeNode.setId(componentVersion.getId());
                            treeNode.setText(componentVersion.getComponent().getAlias());
                            treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                            treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                            treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                            treeNode.setChild("1");
                            treeNode.setOpen(open);
                            treeNode.setProp0(ConstantVar.Component.Type.ASSEMBLY);
                            treeNodelist.add(treeNode);
                        } else if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1) {
                            treeNode = new DhtmlxTreeNode();
                            treeNode.setId(componentVersion.getId());
                            treeNode.setText(componentVersion.getComponent().getAlias());
                            treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                            treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                            treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                            treeNode.setChild("0");
                            treeNode.setProp0(ConstantVar.Component.Type.SELF_DEFINE);
                            module = getService(ModuleService.class).findByComponentVersionId(componentVersionId);
                            if (module != null) {
                                treeNode.setProp1(module.getId());
                            }
                            treeNodelist.add(treeNode);
                        }
                    }
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取同步到构件树
     * 
     * @return Object
     */
    public Object getSyncButtonToTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String reserveZoneId = getParameter("reserveZoneId");
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("Component".equals(treeNodeId)) {
            ComponentReserveZone componentReserveZone = getService(ComponentReserveZoneService.class).getByID(reserveZoneId);
            if (componentReserveZone != null) {
                String[] strs = componentReserveZone.getName().split("_");
                String logicTableCode = strs[0];
                List<Object[]> assembleComponents = getService().getAssembleComponentByLogicTableCode("%" + logicTableCode + "%");
                if (CollectionUtils.isNotEmpty(assembleComponents)) {
                    for (Object[] tempCV : assembleComponents) {
                        treeNode = new DhtmlxTreeNode();
                        treeNode.setId(StringUtil.null2empty(tempCV[3]));
                        treeNode.setText(StringUtil.null2empty(tempCV[1]) + "_" + StringUtil.null2empty(tempCV[2]));
                        treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setChild("0");
                        treeNode.setOpen(true);
                        treeNodelist.add(treeNode);
                    }
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取同步树
     * 
     * @return Object
     */
    public Object getSyncButtonTree() {
        String reserveZoneId = getParameter("reserveZoneId");
        String currentComponentVersionId = getParameter("currentComponentVersionId");
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeNodeId)) {
            List<ComponentAssembleArea> componentAssembleAreaList = getService(ComponentAssembleAreaService.class).getComponentAssembleAreaListByParentId(
                    treeNodeId);
            if (CollectionUtils.isNotEmpty(componentAssembleAreaList)) {
                for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentAssembleArea.getId());
                    treeNode.setText(componentAssembleArea.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("1");
                    // Type （Area：构件分类，Component：组合构件）
                    treeNode.setProp0("Area");
                    treeNodelist.add(treeNode);
                }
            }
        } else {
            List<ComponentAssembleArea> componentAssembleAreaList = getService(ComponentAssembleAreaService.class).getComponentAssembleAreaListByParentId(
                    treeNodeId);
            if (CollectionUtils.isNotEmpty(componentAssembleAreaList)) {
                for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentAssembleArea.getId());
                    treeNode.setText(componentAssembleArea.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("1");
                    // Type （Area：构件分类，Component：组合构件）
                    treeNode.setProp0("Area");
                    treeNodelist.add(treeNode);
                }
            }
            ComponentReserveZone componentReserveZone = getService(ComponentReserveZoneService.class).getByID(reserveZoneId);
            if (componentReserveZone != null) {
                // String[] strs = componentReserveZone.getName().split("_");
                String zoneName = componentReserveZone.getName();
                String logicTableCode = zoneName.replace("_FORM_0", "").replace("_FORM_1", "").replace("_GRID_LINK", "").replace("_GRID", "");
                List<Object[]> assembleComponents = getService().getAssembleComponentByLogicTableCode(treeNodeId, "%\"" + logicTableCode + "\"%");
                if (CollectionUtils.isNotEmpty(assembleComponents)) {
                    for (Object[] tempCV : assembleComponents) {
                        if (StringUtil.isNotEmpty(currentComponentVersionId) && currentComponentVersionId.equals(tempCV[0])) {
                            continue;
                        }
                        treeNode = new DhtmlxTreeNode();
                        treeNode.setId(StringUtil.null2empty(tempCV[3]));
                        treeNode.setText(StringUtil.null2empty(tempCV[1]) + "_" + StringUtil.null2empty(tempCV[2]));
                        treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setChild("0");
                        // Type （Area：构件分类，Component：组合构件）
                        treeNode.setProp0("Component");
                        treeNode.setOpen(true);
                        treeNodelist.add(treeNode);
                    }
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 打包组合构件版本
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object packageComponent() {
        String componentVersionId = getParameter("id");
        ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
        String componentDirPath = ComponentFileUtil.getCompUncompressPath() + componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion();
        String componentZipPath = ComponentFileUtil.getCompPath() + componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion() + ".zip";
        // 删除旧的构件包
        if (StringUtil.isNotEmpty(componentVersion.getPath())) {
            String oldComponentDirPath = ComponentFileUtil.getCompUncompressPath()
                    + componentVersion.getPath().substring(0, componentVersion.getPath().lastIndexOf("."));
            String oldComponentZipPath = ComponentFileUtil.getCompPath() + componentVersion.getPath();
            FileUtil.deleteFile(oldComponentDirPath);
            FileUtil.deleteFile(oldComponentZipPath);
        }
        // 1、准备构件打包的目录
        File componentDir = new File(componentDirPath);
        if (!componentDir.exists()) {
            componentDir.mkdirs();
        }
        File componentLibDir = new File(componentDirPath + "/component_lib");
        if (!componentLibDir.exists()) {
            componentLibDir.mkdirs();
        }
        // 2、准备相关的构件包文件
        Set<ComponentVersion> componentVersionSet = getService().getComponentVersionOfConstruct(componentVersionId);
        Set<LogicTableDefine> logicTableDefineSet = new HashSet<LogicTableDefine>();
        if (CollectionUtils.isNotEmpty(componentVersionSet)) {
            // 将自定义构件重新打包
            ComponentVersion cv = null;
            Map<String, Object> selfDefineDataMap = null;
            List<LogicTableDefine> logicTableDefineList = null;
            try {
                for (Iterator<ComponentVersion> it = componentVersionSet.iterator(); it.hasNext();) {
                    cv = it.next();
                    if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(cv.getComponent().getType()) != -1
                            || ConstantVar.Component.Type.TAB.equals(cv.getComponent().getType())) {
                        selfDefineDataMap = ComponentFileUtil.packageComponent(cv.getId(), false);
                        logicTableDefineList = (List<LogicTableDefine>) selfDefineDataMap.get("logicTables");
                        if (CollectionUtils.isNotEmpty(logicTableDefineList)) {
                            logicTableDefineSet.addAll(logicTableDefineList);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                FileUtil.deleteFile(componentDirPath);
                setReturnData("{'success':false, 'message':'打包失败！打包自定义构件 " + cv.getComponent().getAlias() + "_" + cv.getVersion() + " 失败！'}");
                return new DefaultHttpHeaders(SUCCESS).disableCaching();
            }
            for (Iterator<ComponentVersion> it = componentVersionSet.iterator(); it.hasNext();) {
                cv = it.next();
                if (ConstantVar.Component.Type.TRANSFER_DEVICE.equals(cv.getComponent().getType())) {
                    continue;
                }
                if (!new File(ComponentFileUtil.getCompPath() + cv.getPath()).exists()) {
                    FileUtil.deleteFile(componentDirPath);
                    setReturnData("{'success':false, 'message':'打包失败！构件包 " + cv.getComponent().getAlias() + "_" + cv.getVersion() + ".zip 不存在！'}");
                    return new DefaultHttpHeaders(SUCCESS).disableCaching();
                }
                FileUtil.copyFile(ComponentFileUtil.getCompPath() + cv.getPath(), componentDirPath + "/component_lib/" + cv.getPath());
            }
        }
        // 3、准备构件配置文件
        ComponentFileUtil.createComponentConfigXmlFile(componentVersion, componentDirPath + "/component-config.xml");
        // 4、准备构件配置信息
        File dataDir = new File(componentDirPath + "/data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        if (!ComponentFileUtil.createAssembleConfigJsonFile(componentVersion, logicTableDefineSet, componentDirPath + "/data/config.json")) {
            FileUtil.deleteFile(componentDirPath);
            setReturnData("{'success':false, 'message':'创建config.json文件失败！'}");
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }
        try {
            // 5、压缩zip包
            File componentZip = new File(componentZipPath);
            ZipUtil.zip(componentZip, "", componentDir);
            componentVersion.setPath(componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion() + ".zip");
            componentVersion.setIsPackage(ConstantVar.Component.Packaged.YES);
            getService(ComponentVersionService.class).save(componentVersion);
        } catch (Exception e) {
            FileUtil.deleteFile(componentDirPath);
            FileUtil.deleteFile(componentZipPath);
            setReturnData("{'success':false, 'message':'打包失败！'}");
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }
        setReturnData("{'success':true, 'message':'打包成功！'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 导入构件
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object uploadHandler() {
        HttpServletRequest request = ServletActionContext.getRequest();
        // 1、上传文件
        // struts2 请求包装
        MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
        Enumeration<String> names = wrapper.getFileParameterNames();
        String name = "";
        while (names.hasMoreElements()) {
            name = names.nextElement();
        }
        // 获取上传文件名
        String fileName = wrapper.getFileNames(name)[0];
        // 获取上传文件
        File file = wrapper.getFiles(name)[0];
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            // 建立文件输出流
            fos = new FileOutputStream(ComponentFileUtil.getTempCompPath() + fileName);
            // 建立文件上传流
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            for (int len = 0; (len = fis.read(buffer)) > 0;) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            request.getSession().setAttribute("message", "上传失败！");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 2、解包
        String tempPath = ComponentFileUtil.getTempCompUncompressPath() + fileName.substring(0, fileName.lastIndexOf("."));
        try {
            ZipUtil.unzipFile(new File(ComponentFileUtil.getTempCompPath() + fileName), tempPath);
        } catch (Exception e1) {
            uploadError("解析构件包错误！", fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }

        // 3、解析component-config.xml文件
        File configFile = new File(tempPath + "/component-config.xml");
        ComponentConfig componentConfig = null;
        try {
            componentConfig = ComponentFileUtil.parseConfigFile(configFile);
        } catch (DocumentException e) {
            uploadError("解析component-config.xml错误！", fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }
        componentConfig.setPackageFileName(fileName);

        // 4、初步校验构件包，校验构件是否是组合构件
        String message = validateComponent(componentConfig);
        if (StringUtil.isNotEmpty(message)) {
            uploadError(message, fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }

        // 5、解析配置信息
        File dataConfigFile = new File(tempPath + "/data/config.json");
        Map<String, Object> assembleConfig = null;
        try {
            assembleConfig = ComponentFileUtil.parseAssembleConfig(dataConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
            uploadError("解析组合构件配置文件config.json错误！", fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }

        // 6、解压基础的构件包
        List<ComponentVersion> baseComponentVersionList = (List<ComponentVersion>) assembleConfig.get("BaseComponentVersions");
        String zipFileName = null;
        String uncompressZipFileName = null;
        Map<String, Object> selfDefineConfig = null;
        Map<String, Object> selfDefineConfigMap = new HashMap<String, Object>();
        try {
            for (ComponentVersion baseComponentVersion : baseComponentVersionList) {
                zipFileName = baseComponentVersion.getPath();
                uncompressZipFileName = zipFileName.substring(0, zipFileName.lastIndexOf("."));
                ZipUtil.unzipFile(new File(tempPath + "/component_lib/" + zipFileName), tempPath + "/component_lib/" + uncompressZipFileName);
                ZipUtil.unzipFile(new File(tempPath + "/component_lib/" + zipFileName), ComponentFileUtil.getTempCompUncompressPath() + uncompressZipFileName);
                if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(baseComponentVersion.getComponent().getType()) != -1) {
                    File selfDefineConfigFile = new File(tempPath + "/component_lib/" + uncompressZipFileName + "/data/config.json");
                    selfDefineConfig = ComponentFileUtil.parseSelfDefineConfig(selfDefineConfigFile, false);
                    selfDefineConfigMap.put(baseComponentVersion.getId(), selfDefineConfig);
                }
            }
        } catch (Exception e1) {
            uploadError("解析构件包错误！", zipFileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }
        assembleConfig.put("SelfDefineConfigs", selfDefineConfigMap);
        assembleConfig.put("fileName", fileName);

        String assembleComponentConfigKey = request.getParameter("assembleComponentConfigKey");
        request.getSession().setAttribute(assembleComponentConfigKey, assembleConfig);

        // dhtmlxVault上传完成时需要
        String fileId = request.getParameter("sessionId").toString().trim();
        request.getSession().setAttribute("FileUpload.Progress." + fileId, "-1");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验构件信息
     * 
     * @param componentConfig 构件配置
     * @return String 校验结果
     */
    private String validateComponent(ComponentConfig componentConfig) {
        StringBuilder message = new StringBuilder();
        if (!ConstantVar.Component.Type.ASSEMBLY.equals(componentConfig.getType())) {
            message.append("该构件不是组合构件!");
        }
        return message.toString();
    }

    /**
     * 上传构件有问题时的处理
     * 
     * @param message 错误消息
     * @param newFileName 构件文件路径
     * @param tempPath 构件解包文件目录
     */
    private void uploadError(String message, String newFileName, String tempPath) {
        HttpServletRequest request = ServletActionContext.getRequest();
        deleteComponentFile(ComponentFileUtil.getTempCompPath() + newFileName, tempPath);
        request.getSession().setAttribute("message", message);
        // dhtmlxVault上传完成时需要
        String fileId = request.getParameter("sessionId").toString().trim();
        request.getSession().setAttribute("FileUpload.Progress." + fileId, "-1");
    }

    /**
     * 删除解压的临时文件和上传的文件
     * 
     * @param newFileName 构件包名次
     * @param tempPath 构件解压的临时目录
     */
    private void deleteComponentFile(String packagePath, String uncompressPath) {
        FileUtil.deleteFile(packagePath);
        FileUtil.deleteFile(uncompressPath);
    }

    /**
     * 获取上传构件的情况
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object getUploadMessage() {
        HttpServletRequest request = ServletActionContext.getRequest();
        Object message = request.getSession().getAttribute("message");
        request.getSession().setAttribute("message", "");
        boolean existOldComponentVersion = false;
        if (StringUtil.isNotEmpty(message)) {
            setReturnData("{'success':false, 'message':'" + message + "'}");
        } else {
            String assembleComponentConfigKey = request.getParameter("assembleComponentConfigKey");
            Map<String, Object> assembleConfig = (Map<String, Object>) request.getSession().getAttribute(assembleComponentConfigKey);
            List<ComponentVersion> baseComponentVersionList = (List<ComponentVersion>) assembleConfig.get("BaseComponentVersions");
            StringBuilder msg = new StringBuilder(100);
            List<ComponentVersion> dbComponentVersionList = null;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (ComponentVersion baseComponentVersion : baseComponentVersionList) {
                // 开发的构件
                if (ConstantVar.Component.Type.DEVELOP.indexOf(baseComponentVersion.getComponent().getType()) != -1) {
                    dbComponentVersionList = getService(ComponentVersionService.class).getComponentVersionListByComponentName(
                            baseComponentVersion.getComponent().getName());
                    if (CollectionUtils.isNotEmpty(dbComponentVersionList)) {
                        for (ComponentVersion dbComponentVersion : dbComponentVersionList) {
                            if (!dbComponentVersion.getVersion().equals(baseComponentVersion.getVersion())) {
                                // 组合构件包中是否存在不同版本的构件
                                msg.append("'").append(baseComponentVersion.getComponent().getAlias()).append("'存在不同的版本！").append("\n");
                            } else {
                                if (StringUtil.isNotEmpty(baseComponentVersion.getPackageTime()) && StringUtil.isNotEmpty(dbComponentVersion.getPackageTime())) {
                                    try {
                                        Date basePackTime = df.parse(baseComponentVersion.getPackageTime());
                                        Date dbPackTime = df.parse(dbComponentVersion.getPackageTime());
                                        if (basePackTime.getTime() < dbPackTime.getTime()) {
                                            // 组合构件包中是否存在旧的构件包（打包时间比较前）
                                            msg.append("'").append(baseComponentVersion.getComponent().getAlias()).append("'的包是旧的！").append("\n");
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (StringUtil.isNotEmpty(message)) {
                setReturnData("{'success':false, 'message':'" + message + "'}");
            } else {
                Map<String, Object> assembleMap = (Map<String, Object>) assembleConfig.get("Assemble");
                Map<String, Object> assembleComponentVersionMap = (Map<String, Object>) assembleMap.get("assembleComponentVersion");
                Map<String, String> assembleComponentMap = (Map<String, String>) assembleComponentVersionMap.get("Component");
                Map<String, String> baseInfoMap = (Map<String, String>) assembleComponentVersionMap.get("baseInfo");
                ComponentVersion assembleComponentVersion = getService(ComponentVersionService.class).getByComponentNameAndVersion(
                        assembleComponentMap.get("name"), baseInfoMap.get("version"));
                if (assembleComponentVersion != null) {
                    existOldComponentVersion = true;
                }
                setReturnData("{'success':true, 'existOldComponentVersion':" + existOldComponentVersion + "}");
            }
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * dhtmlxVault获取ID方法
     * 
     * @return Object
     */
    public Object getIdHandler() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String id = request.getSession().getId().toString();
        try {
            PrintWriter writer = response.getWriter();
            writer.println(id);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("FileUpload.Progress." + id, "0");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * dhtmlxVault获取信息方法
     * 
     * @return Object
     */
    public Object getInfoHandler() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            PrintWriter writer = response.getWriter();
            writer.println(request.getSession().getAttribute("FileUpload.Progress." + request.getParameter("sessionId").toString().trim()));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 保存组合构件
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object saveAssembleComponentVersion() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String assembleComponentConfigKey = request.getParameter("assembleComponentConfigKey");
        Map<String, Object> assembleConfig = (Map<String, Object>) request.getSession().getAttribute(assembleComponentConfigKey);
        if (assembleConfig == null) {
            setReturnData("{'success':false,'message':'上传失败！'}");
        } else {
            String msg = saveAssembleComponentVersion(assembleConfig);
            // 1、将构件相关文件拷贝到正式存储目录下
            String assembleZipFileName = (String) assembleConfig.get("fileName");
            String assembleUncompressZipFileName = assembleZipFileName.substring(0, assembleZipFileName.lastIndexOf("."));
            String assembleUncompressZipFileTempPath = ComponentFileUtil.getTempCompUncompressPath() + assembleUncompressZipFileName;
            List<ComponentVersion> baseComponentVersionList = (List<ComponentVersion>) assembleConfig.get("BaseComponentVersions");
            String zipFileName = null;
            String uncompressZipFileName = null;
            for (ComponentVersion baseComponentVersion : baseComponentVersionList) {
                zipFileName = baseComponentVersion.getPath();
                uncompressZipFileName = zipFileName.substring(0, zipFileName.lastIndexOf("."));
                FileUtil.copyFile(assembleUncompressZipFileTempPath + "/component_lib/" + zipFileName, ComponentFileUtil.getCompPath() + zipFileName);
                FileUtil.deleteDir(ComponentFileUtil.getCompUncompressPath() + uncompressZipFileName);
                FileUtil.copyFolder(assembleUncompressZipFileTempPath + "/component_lib/" + uncompressZipFileName, ComponentFileUtil.getCompUncompressPath()
                        + uncompressZipFileName);
                if ("3".equals(baseComponentVersion.getComponent().getType())) {
                    String projectPath = ComponentFileUtil.getProjectPath();
                    PreviewUtil.copyComponentFile(baseComponentVersion, projectPath);
                }
            }
            FileUtil.copyFile(ComponentFileUtil.getTempCompPath() + "/" + assembleZipFileName, ComponentFileUtil.getCompPath() + assembleZipFileName);
            FileUtil.copyFolder(assembleUncompressZipFileTempPath, ComponentFileUtil.getCompUncompressPath() + assembleUncompressZipFileName);
            // 2、删除临时文件
            deleteComponentFile(ComponentFileUtil.getTempCompPath() + "/" + assembleZipFileName, assembleUncompressZipFileTempPath);
            if (msg.length() > 0) {
                setReturnData("{'success':false,'message':'" + msg + "'}");
            } else {
                setReturnData("{'success':true}");
            }
        }
        request.getSession().removeAttribute(assembleComponentConfigKey);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 保存导入的组合构件
     * 
     * @param assembleConfig 组合构件信息
     */
    @SuppressWarnings("unchecked")
    private String saveAssembleComponentVersion(Map<String, Object> assembleConfig) {
        StringBuilder sb = new StringBuilder();
        // 1、保存构件生产库中的构件分类
        List<Map<String, Object>> baseComponentAreaMapList = (List<Map<String, Object>>) assembleConfig.get("BaseComponentAreas");
        ComponentArea dbComponentArea = null;
        for (Map<String, Object> baseComponentAreaMap : baseComponentAreaMapList) {
            String areaId = StringUtil.null2empty(baseComponentAreaMap.get("id"));
            dbComponentArea = getService(ComponentAreaService.class).getByID(areaId);
            if (dbComponentArea != null) {
                if (!dbComponentArea.getName().equals(baseComponentAreaMap.get("name"))) {
                    dbComponentArea.setName(StringUtil.null2empty(baseComponentAreaMap.get("name")));
                    getService(ComponentAreaService.class).save(dbComponentArea);
                }
            } else {
                String componentAreaSql = "insert into t_xtpz_component_area(id,name,show_order,parent_id,has_child) values('" + baseComponentAreaMap.get("id")
                        + "','" + baseComponentAreaMap.get("name") + "'," + StringUtil.null2zero(baseComponentAreaMap.get("showOrder")) + ",'"
                        + baseComponentAreaMap.get("parentId") + "','" + ((Boolean) baseComponentAreaMap.get("hasChild") ? "1" : "0") + "')";
                DatabaseHandlerDao.getInstance().executeSql(componentAreaSql);
            }
        }
        // 2、保存基础构件
        Map<String, Object> selfDefineConfigMap = (Map<String, Object>) assembleConfig.get("SelfDefineConfigs");
        List<ComponentVersion> baseComponentVersionList = (List<ComponentVersion>) assembleConfig.get("BaseComponentVersions");
        Map<String, Object> selfDefineConfig = null;
        // 修改过的IDs
        Map<String, Map<String, String>> modifiedIdMap = new HashMap<String, Map<String, String>>();
        for (ComponentVersion componentVersion : baseComponentVersionList) {
            if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1
                    || ConstantVar.Component.Type.TAB.equals(componentVersion.getComponent().getType())) {
                selfDefineConfig = (Map<String, Object>) selfDefineConfigMap.get(componentVersion.getId());
            } else {
                selfDefineConfig = null;
            }
            try {
                getService(ComponentVersionService.class).saveBaseComponentVersionOfAssemble(componentVersion, selfDefineConfig, modifiedIdMap);
            } catch (Exception e) {
                sb.append("构件\"").append(componentVersion.getComponent().getAlias()).append("_").append(componentVersion.getVersion()).append("\"保存失败！");
                e.printStackTrace();
            }
        }
        // 3、保存组合构件
        String assembleAreaId = ServletActionContext.getRequest().getParameter("assembleAreaId");
        Map<String, Object> assembleMap = (Map<String, Object>) assembleConfig.get("Assemble");
        Set<Construct> constructSetOfAssemble = new HashSet<Construct>();
        try {
            getService().saveConstructInfo(assembleMap, assembleAreaId, true, constructSetOfAssemble, modifiedIdMap);
            // 4、保存公用预留区及组装的按钮
            getService().saveCommonConstructDetail(assembleConfig, modifiedIdMap);
        } catch (Exception e) {
            sb.append("保存整个组合构件失败！");
            e.printStackTrace();
        }
        return sb.toString();
    }
}
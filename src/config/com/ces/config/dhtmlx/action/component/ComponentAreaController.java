package com.ces.config.dhtmlx.action.component;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentAreaDao;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.component.ComponentArea;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.component.ComponentAreaService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.ComponentInfoUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

/**
 * 构件分类Controller
 * 
 * @author wanglei
 * @date 2013-07-18
 */
public class ComponentAreaController extends ConfigDefineServiceDaoController<ComponentArea, ComponentAreaService, ComponentAreaDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentArea());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("componentAreaService")
    @Override
    protected void setService(ComponentAreaService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        Integer maxShowOrder = getService().getMaxShowOrder(model.getParentId());
        int showOrder = 0;
        if (maxShowOrder == null) {
            showOrder = 1;
        } else {
            showOrder = maxShowOrder + 1;
        }
        model.setHasChild(false);
        model.setShowOrder(showOrder);
        model = getService().save(model);
        if (!"-1".endsWith(model.getParentId())) {
            ComponentArea parent = getService().getByID(model.getParentId());
            if (!parent.getHasChild()) {
                parent.setHasChild(true);
                getService().save(parent);
            }
        }
        return SUCCESS;
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ComponentArea componentArea = (ComponentArea) getModel();
        ComponentArea temp = getService().getByNameAndParentId(componentArea.getName(), componentArea.getParentId());
        boolean nameExist = false;
        if (null != componentArea.getId() && !"".equals(componentArea.getId())) {
            ComponentArea oldComponentArea = getService().getByID(componentArea.getId());
            if (null != temp && null != oldComponentArea && !temp.getId().equals(oldComponentArea.getId())) {
                nameExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        List<ComponentArea> componentAreaList = getService().getComponentAreaListByParentId(model.getId());
        List<ComponentVersion> componentVersionList = getService(ComponentVersionService.class).getComponentVersionListByAreaId(model.getId());
        List<Module> moduleList = getService(ModuleService.class).getByComponentAreaId(model.getId());
        boolean flag = false;
        String message = null;
        if (CollectionUtils.isNotEmpty(componentAreaList)) {
            message = "该构件分类下存在子分类，不能删除";
        } else if (CollectionUtils.isNotEmpty(componentVersionList)) {
            message = "该构件分类下存在构件，不能删除";
        } else if (CollectionUtils.isNotEmpty(moduleList)) {
            message = "该构件分类下存在未定义布局的自定义构件，不能删除";
        } else {
            ComponentArea componentArea = getService().getByID(model.getId());
            getService().delete(componentArea);
            ComponentInfoUtil.getInstance().removeComponentArea(model.getId());
            List<ComponentArea> childList = getService().getComponentAreaListByParentId(componentArea.getParentId());
            if (CollectionUtils.isEmpty(childList)) {
                ComponentArea parent = getService().getByID(componentArea.getParentId());
                parent.setHasChild(false);
                getService().save(parent);
                ComponentInfoUtil.getInstance().putComponentArea(parent);
            }
            flag = true;
        }
        setReturnData("{'success':" + flag + ", 'message':'" + message + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 树排序
     * 
     * @return Object
     */
    public Object sort() {
        String start = getParameter("start");
        String end = getParameter("end");
        String targetId = getParameter("targetId");
        ComponentArea startComponentArea = getService().getByID(start);
        if (startComponentArea.getParentId().equals(targetId)) {
            // 同个父分类中拖动排序
            if (StringUtil.isNotEmpty(end)) {
                ComponentArea endComponentArea = getService().getByID(end);
                if (startComponentArea.getShowOrder().intValue() > endComponentArea.getShowOrder().intValue()) {
                    // 向上
                    List<ComponentArea> componentAreaList = getService().getComponentAreaListByShowOrder(endComponentArea.getShowOrder(),
                            startComponentArea.getShowOrder(), startComponentArea.getParentId());
                    startComponentArea.setShowOrder(endComponentArea.getShowOrder());
                    getService().save(startComponentArea);
                    for (ComponentArea componentArea : componentAreaList) {
                        if (componentArea.getId().equals(startComponentArea.getId())) {
                            continue;
                        }
                        componentArea.setShowOrder(componentArea.getShowOrder() + 1);
                        getService().save(componentArea);
                    }
                } else {
                    // 向下
                    List<ComponentArea> componentAreaList = getService().getComponentAreaListByShowOrder(startComponentArea.getShowOrder(),
                            endComponentArea.getShowOrder(), startComponentArea.getParentId());
                    startComponentArea.setShowOrder(endComponentArea.getShowOrder() - 1);
                    getService().save(startComponentArea);
                    for (ComponentArea componentArea : componentAreaList) {
                        if (componentArea.getId().equals(startComponentArea.getId()) || componentArea.getId().equals(endComponentArea.getId())) {
                            continue;
                        }
                        componentArea.setShowOrder(componentArea.getShowOrder() - 1);
                        getService().save(componentArea);
                    }
                }
            }
            setReturnData("排序成功!");
        } else {
            String oldParentAreaId = startComponentArea.getParentId();
            // 拖动到不同的父分类中
            startComponentArea.setParentId(targetId);
            if (StringUtil.isNotEmpty(end)) {
                ComponentArea endComponentArea = getService().getByID(end);
                startComponentArea.setShowOrder(endComponentArea.getShowOrder());
                getService().save(startComponentArea);
                getService().updateShowOrderPlusOne(endComponentArea.getShowOrder(), targetId);
            } else {
                Integer maxShowOrder = getService().getMaxShowOrder(targetId);
                int showOrder = 0;
                if (maxShowOrder == null) {
                    showOrder = 1;
                } else {
                    showOrder = maxShowOrder + 1;
                }
                startComponentArea.setShowOrder(showOrder);
                getService().save(startComponentArea);
                if (!"-1".endsWith(targetId)) {
                    ComponentArea parent = getService().getByID(targetId);
                    if (!parent.getHasChild()) {
                        parent.setHasChild(true);
                        getService().save(parent);
                    }
                }
            }
            // 查询拖动分类原来的父分类中是否还有子分类，如果没有，将该分类hasChild设置成false
            List<ComponentArea> oldChildAreaList = getService().getComponentAreaListByParentId(oldParentAreaId);
            if (CollectionUtils.isEmpty(oldChildAreaList)) {
                ComponentArea oldParentComponentArea = getService().getByID(oldParentAreaId);
                oldParentComponentArea.setHasChild(false);
                getService().save(oldParentComponentArea);
            }
            setReturnData("改变父构件分类成功!");
        }
        ComponentInfoUtil.getInstance().putComponentAreas(getService().findAll());
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * qiucs 2014-2-20
     * <p>
     * 描述: 构件分类下拉框
     * </p>
     */
    public Object combo() {
        try {
            setReturnData(getService().comboOfComponentArea());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }

    /**
     * 获取构件分类树
     * 
     * @return Object
     */
    public Object getAreaTree() {
        String jsonStr = "{id:'0', item:[{id:'-1', text:'构件库', open:'1'" + getChildAreaJson("-1") + "}]}";
        setReturnData(jsonStr);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取构件分类json
     * 
     * @param parentAreaId 父构件分类ID
     * @return String
     */
    private String getChildAreaJson(String parentAreaId) {
        List<ComponentArea> areaList = getService(ComponentAreaService.class).getComponentAreaListByParentId(parentAreaId);
        String jsonStr = "";
        if (CollectionUtils.isNotEmpty(areaList)) {
            jsonStr = ", item:[";
            for (ComponentArea area : areaList) {
                jsonStr += "{id:'" + area.getId() + "', text:'" + area.getName() + "', open:'1'" + getChildAreaJson(area.getId());
                jsonStr += "},";
            }
            jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
            jsonStr += "]";
        }
        return jsonStr;
    }

    /**
     * 获取构件生产库树,关联该构件的选中
     * 
     * @return Object
     */
    public Object getCheckedTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeNodeId = getId();
        String componentVersionId = getParameter("componentVersionId");
        if (componentVersionId.indexOf("SCV_") != -1) {
            componentVersionId = componentVersionId.substring(4);
        }
        String baseComponentVersionId = null;
        Construct assembleComponent = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersionId);
        if (assembleComponent != null) {
            baseComponentVersionId = getService(ComponentVersionService.class).getByID(assembleComponent.getBaseComponentVersionId()).getId();
        }
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeNodeId)) {
            List<ComponentArea> areaList = getService(ComponentAreaService.class).getComponentAreaListByParentId(treeNodeId);
            if (CollectionUtils.isNotEmpty(areaList)) {
                for (ComponentArea componentArea : areaList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentArea.getId());
                    treeNode.setText(componentArea.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setOpen(true);
                    treeNode.setChild("1");
                    // Type （Area：构件分类，Component：构件）
                    treeNode.setProp0("Area");
                    treeNodelist.add(treeNode);
                }
            }
        } else {
            List<ComponentArea> areaList = getService(ComponentAreaService.class).getComponentAreaListByParentId(treeNodeId);
            if (CollectionUtils.isNotEmpty(areaList)) {
                for (ComponentArea componentArea : areaList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentArea.getId());
                    treeNode.setText(componentArea.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setOpen(true);
                    treeNode.setChild("1");
                    // Type （Area：构件分类，Component：构件）
                    treeNode.setProp0("Area");
                    treeNodelist.add(treeNode);
                }
            }
            List<ComponentVersion> componentVersionList = getService(ComponentVersionService.class).getComponentVersionListByAreaId(treeNodeId);
            if (CollectionUtils.isNotEmpty(componentVersionList)) {
                for (ComponentVersion componentVersion : componentVersionList) {
                    if (componentVersion.getComponent().getType().equals(ConstantVar.Component.Type.ASSEMBLY))
                        continue;
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(componentVersion.getId());
                    treeNode.setText(componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("0");
                    // Type （Area：构件分类，Component：构件）
                    treeNode.setProp0("Component");
                    if (componentVersion.getId().equals(baseComponentVersionId)) {
                        treeNode.setChecked("1");
                    }
                    treeNodelist.add(treeNode);
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }
}

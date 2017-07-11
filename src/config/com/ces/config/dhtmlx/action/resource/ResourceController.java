package com.ces.config.dhtmlx.action.resource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.resource.ResourceDao;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.component.ComponentButton;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.resource.Resource;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.component.ComponentButtonService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.dhtmlx.service.resource.ResourceButtonService;
import com.ces.config.dhtmlx.service.resource.ResourceService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

/**
 * 资源Controller
 * 
 * @author wanglei
 * @date 2015-04-15
 */
public class ResourceController extends ConfigDefineServiceDaoController<Resource, ResourceService, ResourceDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new Resource());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("resourceService")
    @Override
    protected void setService(ResourceService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        String buttonIds = getParameter("buttonIds");
        model = getService().saveButtonResource(model, buttonIds);
        return SUCCESS;
    }

    /*
     * (非 Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#update()
     */
    @Override
    public Object update() throws FatalException {
        String buttonIds = getParameter("buttonIds");
        model = getService().saveButtonResource(model, buttonIds);
        return SUCCESS;
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateResource() {
        Resource resource = (Resource) getModel();
        Resource temp = getService().getByNameAndParentId(resource.getName(), resource.getParentId());
        boolean nameExist = false;
        if (null != resource.getId() && !"".equals(resource.getId())) {
            Resource oldResource = getService().getByID(resource.getId());
            if (null != temp && null != oldResource && !temp.getId().equals(oldResource.getId())) {
                nameExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist':" + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取资源
     * 
     * @return Object
     */
    public Object getResourceTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String parentId = getId();
        list.setData(beforeProcessTreeData(getService().getResourceTree(parentId)));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取按钮树
     * 
     * @return Object
     */
    public Object getButtonTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeNodeId = getId();
        Set<String> menuButtonIdSet = new HashSet<String>();
        Set<String> resourceButtonIdSet = new HashSet<String>();
        // 获取存放在session中的相关 按钮资源与实际按钮关系IDs
        getResourceButtonOfSession(menuButtonIdSet, resourceButtonIdSet);
        boolean open = BooleanUtils.toBoolean(getParameter("P_OPEN"));
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        if (treeNodeId.startsWith("M_")) {
            // 菜单节点
            treeNodelist = getTreeNodeListOfMenu(treeNodeId, open);
        } else if (treeNodeId.startsWith("CV_")) {
            treeNodelist = getTreeNodeListOfComponentVersion(treeNodeId, menuButtonIdSet, resourceButtonIdSet, open);
        } else if (treeNodeId.startsWith("RZ_")) {
            treeNodelist = getTreeNodeListOfReserveZone(treeNodeId, menuButtonIdSet, resourceButtonIdSet);
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取存放在session中的相关 按钮资源与实际按钮关系IDs
     * 
     * @param menuButtonIdSet 菜单资源下已经存在的按钮
     * @param resourceButtonIdSet 按钮资源下已经存在的按钮
     */
    @SuppressWarnings("unchecked")
    private void getResourceButtonOfSession(Set<String> menuButtonIdSet, Set<String> resourceButtonIdSet) {
        String buttonTreeKey = getParameter("P_buttonTreeKey");
        String resourceId = getParameter("P_resourceId");
        String menuResourceId = getParameter("P_menuResourceId");
        Map<String, Object> resourceSession = (Map<String, Object>) ServletActionContext.getRequest().getSession().getAttribute("XTPZ_Resource");
        if (resourceSession != null) {
            if (resourceSession.containsKey(buttonTreeKey)) {
                Map<String, Object> buttonIdMap = (Map<String, Object>) resourceSession.get(buttonTreeKey);
                Set<String> menuButtonIdSetOfSession = (Set<String>) buttonIdMap.get("MenuButton");
                if (CollectionUtils.isNotEmpty(menuButtonIdSetOfSession)) {
                    menuButtonIdSet.addAll(menuButtonIdSetOfSession);
                }
                Set<String> resourceButtonIdSetOfSession = (Set<String>) buttonIdMap.get("ResourceButton");
                if (CollectionUtils.isNotEmpty(resourceButtonIdSetOfSession)) {
                    resourceButtonIdSet.addAll(resourceButtonIdSetOfSession);
                }
            } else {
                resourceSession = new HashMap<String, Object>();
                Map<String, Object> buttonIdMap = new HashMap<String, Object>();
                menuButtonIdSet.addAll(getService(ResourceButtonService.class).getButtonIdsByMenuResourceId(menuResourceId));
                resourceButtonIdSet.addAll(getService(ResourceButtonService.class).getButtonIdsByResourceId(resourceId));
                buttonIdMap.put("MenuButton", menuButtonIdSet);
                buttonIdMap.put("ResourceButton", resourceButtonIdSet);
                resourceSession.put(buttonTreeKey, buttonIdMap);
                ServletActionContext.getRequest().getSession().setAttribute("XTPZ_Resource", resourceSession);
            }
        } else {
            resourceSession = new HashMap<String, Object>();
            Map<String, Object> buttonIdMap = new HashMap<String, Object>();
            menuButtonIdSet.addAll(getService(ResourceButtonService.class).getButtonIdsByMenuResourceId(menuResourceId));
            resourceButtonIdSet.addAll(getService(ResourceButtonService.class).getButtonIdsByResourceId(resourceId));
            buttonIdMap.put("MenuButton", menuButtonIdSet);
            buttonIdMap.put("ResourceButton", resourceButtonIdSet);
            resourceSession.put(buttonTreeKey, buttonIdMap);
            ServletActionContext.getRequest().getSession().setAttribute("XTPZ_Resource", resourceSession);
        }
    }

    /**
     * 获取菜单节点的子节点
     * 
     * @param treeNodeId 父节点
     * @param open 自动加载下层节点
     * @return List<DhtmlxTreeNode>
     */
    private List<DhtmlxTreeNode> getTreeNodeListOfMenu(String treeNodeId, boolean open) {
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        String menuId = treeNodeId.replace("M_", "");
        Menu menu = getService(MenuService.class).getByID(menuId);
        if (menu != null && StringUtil.isNotEmpty(menu.getComponentVersionId())) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(menu.getComponentVersionId());
            if (componentVersion != null) {
                if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                    ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                    if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())) {
                        // 获取树上绑定的所有构件
                        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
                        // 重复构件过滤
                        Set<String> cvIdSet = new HashSet<String>();
                        if (CollectionUtils.isNotEmpty(constructDetailList)) {
                            for (ConstructDetail constructDetail : constructDetailList) {
                                ComponentVersion tempCV = null;
                                if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
                                    if (!cvIdSet.contains(constructDetail.getComponentVersionId())) {
                                        cvIdSet.add(constructDetail.getComponentVersionId());
                                    } else {
                                        continue;
                                    }
                                    tempCV = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                                    // 过滤掉没有按钮的构件
                                    if (ConstantVar.Component.Type.ASSEMBLY.equals(tempCV.getComponent().getType())) {
                                        Construct tempConstruct = getService(ConstructService.class).getByAssembleComponentVersionId(tempCV.getId());
                                        long cdCount = getService(ConstructDetailService.class).getConstructDetailCount(tempConstruct.getId());
                                        if (cdCount > 0) {
                                            treeNodelist.add(getComponentVersionTreeNode(tempCV, open));
                                        } else {
                                            ComponentVersion tempBaseComponentVersion = getService(ComponentVersionService.class).getByID(
                                                    tempConstruct.getBaseComponentVersionId());
                                            if (ConstantVar.Component.Type.PAGE.equals(tempBaseComponentVersion.getComponent().getType())) {
                                                List<ComponentButton> componentButtonList = getService(ComponentButtonService.class).getByComponentVersionId(
                                                        tempBaseComponentVersion.getId());
                                                if (CollectionUtils.isNotEmpty(componentButtonList)) {
                                                    treeNodelist.add(getComponentVersionTreeNode(tempCV, open));
                                                }
                                            }
                                        }
                                    } else if (ConstantVar.Component.Type.PAGE.equals(tempCV.getComponent().getType())) {
                                        List<ComponentButton> componentButtonList = getService(ComponentButtonService.class).getByComponentVersionId(
                                                tempCV.getId());
                                        if (CollectionUtils.isNotEmpty(componentButtonList)) {
                                            treeNodelist.add(getComponentVersionTreeNode(tempCV, open));
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // 过滤掉没有按钮的构件
                        long cdCount = getService(ConstructDetailService.class).getConstructDetailCount(construct.getId());
                        if (cdCount > 0) {
                            treeNodelist.add(getComponentVersionTreeNode(componentVersion, open));
                        } else if (ConstantVar.Component.Type.PAGE.equals(baseComponentVersion.getComponent().getType())) {
                            List<ComponentButton> componentButtonList = getService(ComponentButtonService.class).getByComponentVersionId(
                                    baseComponentVersion.getId());
                            if (CollectionUtils.isNotEmpty(componentButtonList)) {
                                treeNodelist.add(getComponentVersionTreeNode(componentVersion, open));
                            }
                        }
                    }
                } else if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())) {
                    List<ComponentButton> componentButtonList = getService(ComponentButtonService.class).getByComponentVersionId(componentVersion.getId());
                    if (CollectionUtils.isNotEmpty(componentButtonList)) {
                        treeNodelist.add(getComponentVersionTreeNode(componentVersion, open));
                    }
                }
            }
        }
        return treeNodelist;
    }

    /**
     * 获取构件节点的子节点
     * 
     * @param treeNodeId 父节点ID
     * @param menuButtonIdSet 菜单资源下已经存在的按钮
     * @param resourceButtonIdSet 按钮资源下已经存在的按钮
     * @param open 自动加载下层节点
     * @return List<DhtmlxTreeNode>
     */
    private List<DhtmlxTreeNode> getTreeNodeListOfComponentVersion(String treeNodeId, Set<String> menuButtonIdSet, Set<String> resourceButtonIdSet, boolean open) {
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        String componentVersionId = treeNodeId.replace("CV_", "");
        ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
        if (componentVersion != null) {
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                if (ConstantVar.Component.Type.PAGE.equals(baseComponentVersion.getComponent().getType())) {
                    // 获取工具条预留区和需要控制权限的按钮
                    List<ComponentReserveZone> reserveZoneList = getService(ComponentReserveZoneService.class).getByComponentVersionId(
                            baseComponentVersion.getId());
                    if (CollectionUtils.isNotEmpty(reserveZoneList)) {
                        for (ComponentReserveZone reserveZone : reserveZoneList) {
                            if (ConstantVar.Component.ReserveZoneType.TOOLBAR.equals(reserveZone.getType())
                                    || ConstantVar.Component.ReserveZoneType.GRID_LINK.equals(reserveZone.getType())) {
                                // 过滤掉没有按钮的预留区
                                long buttonCount = getService(ConstructDetailService.class).getConstructDetailCount(construct.getId(), reserveZone.getId());
                                if (buttonCount > 0) {
                                    treeNodelist.add(getReserveZoneTreeNode(construct, reserveZone, open));
                                }
                            }
                        }
                    }
                    List<ComponentButton> componentButtonList = getService(ComponentButtonService.class).getByComponentVersionId(baseComponentVersion.getId());
                    if (CollectionUtils.isNotEmpty(componentButtonList)) {
                        for (ComponentButton componentButton : componentButtonList) {
                            if (resourceButtonIdSet != null && resourceButtonIdSet.contains(componentButton.getId())) {
                                treeNodelist.add(getComponentButtonTreeNode(componentButton, "1"));
                                continue;
                            }
                            if (menuButtonIdSet == null || !menuButtonIdSet.contains(componentButton.getId())) {
                                treeNodelist.add(getComponentButtonTreeNode(componentButton, "0"));
                            }
                        }
                    }
                } else if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                    Module module = getService(ModuleService.class).findByComponentVersionId(baseComponentVersion.getId());
                    List<ComponentReserveZone> commonReserveZoneList = AppDefineUtil
                            .getCommonReserveZone(module, ConstantVar.Component.ReserveZoneType.TOOLBAR);
                    commonReserveZoneList.addAll(AppDefineUtil.getCommonReserveZone(module, ConstantVar.Component.ReserveZoneType.GRID_LINK));
                    if (CollectionUtils.isNotEmpty(commonReserveZoneList)) {
                        for (ComponentReserveZone reserveZone : commonReserveZoneList) {
                            // 过滤掉没有按钮的预留区
                            long buttonCount = getService(ConstructDetailService.class).getConstructDetailCount(construct.getId(), reserveZone.getId());
                            if (buttonCount > 0) {
                                treeNodelist.add(getReserveZoneTreeNode(construct, reserveZone, open));
                            }
                        }
                    }
                } else {
                    // 获取基础构件中所有工具条预留区
                    List<ComponentReserveZone> reserveZoneList = getService(ComponentReserveZoneService.class).getByComponentVersionId(
                            baseComponentVersion.getId());
                    if (CollectionUtils.isNotEmpty(reserveZoneList)) {
                        for (ComponentReserveZone reserveZone : reserveZoneList) {
                            if (ConstantVar.Component.ReserveZoneType.TOOLBAR.equals(reserveZone.getType())
                                    || ConstantVar.Component.ReserveZoneType.GRID_LINK.equals(reserveZone.getType())) {
                                // 过滤掉没有按钮的预留区
                                long buttonCount = getService(ConstructDetailService.class).getConstructDetailCount(construct.getId(), reserveZone.getId());
                                if (buttonCount > 0) {
                                    treeNodelist.add(getReserveZoneTreeNode(construct, reserveZone, open));
                                }
                            }
                        }
                    }
                }
            } else if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())) {
                List<ComponentButton> componentButtonList = getService(ComponentButtonService.class).getByComponentVersionId(componentVersion.getId());
                if (CollectionUtils.isNotEmpty(componentButtonList)) {
                    for (ComponentButton componentButton : componentButtonList) {
                        if (resourceButtonIdSet != null && resourceButtonIdSet.contains(componentButton.getId())) {
                            treeNodelist.add(getComponentButtonTreeNode(componentButton, "1"));
                            continue;
                        }
                        if (menuButtonIdSet == null || !menuButtonIdSet.contains(componentButton.getId())) {
                            treeNodelist.add(getComponentButtonTreeNode(componentButton, "0"));
                        }
                    }
                }
            }
        }
        return treeNodelist;
    }

    /**
     * 获取预留区节点的子节点
     * 
     * @param treeNodeId 上层树节点
     * @return List<DhtmlxTreeNode>
     */
    private List<DhtmlxTreeNode> getTreeNodeListOfReserveZone(String treeNodeId, Set<String> menuButtonIdSet, Set<String> resourceButtonIdSet) {
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        String[] strs = treeNodeId.split("_");
        String reserveZoneId = strs[1];
        String constructId = strs[2];
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructIdAndReserveZoneId(constructId, reserveZoneId);
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail cd : constructDetailList) {
                if (ConstructDetail.COMBOBOX_SEARCH.equals(cd.getButtonCode())) {
                    continue;
                }
                if (resourceButtonIdSet != null && resourceButtonIdSet.contains(cd.getId())) {
                    treeNodelist.add(getConstructDetailTreeNode(cd, "1"));
                    continue;
                }
                if (menuButtonIdSet == null || !menuButtonIdSet.contains(cd.getId())) {
                    treeNodelist.add(getConstructDetailTreeNode(cd, "0"));
                }
            }
        }
        return treeNodelist;
    }

    /**
     * 生成构件节点
     * 
     * @param componentVersion 构件
     * @param open 自动加载下层节点
     * @return DhtmlxTreeNode
     */
    private DhtmlxTreeNode getComponentVersionTreeNode(ComponentVersion componentVersion, boolean open) {
        DhtmlxTreeNode treeNode = new DhtmlxTreeNode();
        treeNode.setId("CV_" + componentVersion.getId());
        treeNode.setText(componentVersion.getComponent().getAlias());
        treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
        treeNode.setChild("1");
        treeNode.setOpen(open);
        return treeNode;
    }

    /**
     * 生成预留区节点
     * 
     * @param construct 组合构件
     * @param reserveZone 预留区
     * @param open 自动加载下层节点
     * @return DhtmlxTreeNode
     */
    private DhtmlxTreeNode getReserveZoneTreeNode(Construct construct, ComponentReserveZone reserveZone, boolean open) {
        DhtmlxTreeNode treeNode = new DhtmlxTreeNode();
        treeNode.setId("RZ_" + reserveZone.getId() + "_" + construct.getId());
        treeNode.setText(reserveZone.getAlias());
        treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
        treeNode.setChild("1");
        treeNode.setOpen(open);
        return treeNode;
    }

    /**
     * 生成构件按钮节点
     * 
     * @param componentButton 构件按钮
     * @param checked 是否选中
     * @return DhtmlxTreeNode
     */
    private DhtmlxTreeNode getComponentButtonTreeNode(ComponentButton componentButton, String checked) {
        DhtmlxTreeNode treeNode = new DhtmlxTreeNode();
        treeNode.setId("CVB_" + componentButton.getId());
        treeNode.setText(componentButton.getName());
        treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
        treeNode.setChild("0");
        if ("1".equals(checked)) {
            treeNode.setChecked(checked);
        }
        return treeNode;
    }

    /**
     * 生成组合构件中按钮节点
     * 
     * @param cd 构件组装中的按钮
     * @param checked 是否选中
     * @return
     */
    private DhtmlxTreeNode getConstructDetailTreeNode(ConstructDetail cd, String checked) {
        DhtmlxTreeNode treeNode;
        treeNode = new DhtmlxTreeNode();
        treeNode.setId("CDB_" + cd.getId());
        treeNode.setText(cd.getButtonName());
        treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
        treeNode.setChild("0");
        if ("1".equals(checked)) {
            treeNode.setChecked(checked);
        }
        return treeNode;
    }

    /**
     * 排序
     * 
     * @return Object
     */
    public Object sort() {
        String start = getParameter("start");
        String end = getParameter("end");
        Resource startResource = getService().getByID(start);
        Resource endResource = getService().getByID(end);
        if (startResource.getShowOrder() > endResource.getShowOrder()) {
            // 向上
            List<Resource> resourceList = getService().getByShowOrderBetweenAndParentId(endResource.getShowOrder(), startResource.getShowOrder(),
                    startResource.getParentId());
            startResource.setShowOrder(endResource.getShowOrder());
            getService().save(startResource);
            for (Resource resource : resourceList) {
                if (resource.getId().equals(startResource.getId())) {
                    continue;
                }
                resource.setShowOrder(resource.getShowOrder() + 1);
                getService().save(resource);
            }
        } else {
            // 向下
            List<Resource> resourceList = getService().getByShowOrderBetweenAndParentId(startResource.getShowOrder(), endResource.getShowOrder(),
                    startResource.getParentId());
            startResource.setShowOrder(endResource.getShowOrder());
            getService().save(startResource);
            for (Resource resource : resourceList) {
                if (resource.getId().equals(startResource.getId())) {
                    continue;
                }
                resource.setShowOrder(resource.getShowOrder() - 1);
                getService().save(resource);
            }
        }
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 同步到系统管理平台
     * 
     * @return Object
     */
    public Object syncToAuth() {
        String systemIds = getParameter("systemIds");
        boolean flag = true;
        try {
            getService().syncToAuth(systemIds);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        setReturnData("{'success':" + flag + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}

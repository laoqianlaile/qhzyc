package com.ces.config.dhtmlx.action.release;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.facade.SystemFacade;
import ces.sdk.system.factory.SystemFacadeFactory;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.release.ReleaseDao;
import com.ces.config.dhtmlx.entity.appmanage.AppButton;
import com.ces.config.dhtmlx.entity.appmanage.AppColumn;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppForm;
import com.ces.config.dhtmlx.entity.appmanage.AppFormElement;
import com.ces.config.dhtmlx.entity.appmanage.AppGrid;
import com.ces.config.dhtmlx.entity.appmanage.AppReport;
import com.ces.config.dhtmlx.entity.appmanage.AppSearch;
import com.ces.config.dhtmlx.entity.appmanage.AppSearchPanel;
import com.ces.config.dhtmlx.entity.appmanage.AppSort;
import com.ces.config.dhtmlx.entity.appmanage.ColumnBusiness;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.ColumnOperation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplice;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplit;
import com.ces.config.dhtmlx.entity.appmanage.LogicClassification;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableRelation;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.Report;
import com.ces.config.dhtmlx.entity.appmanage.ReportColumn;
import com.ces.config.dhtmlx.entity.appmanage.ReportDataSource;
import com.ces.config.dhtmlx.entity.appmanage.ReportDefine;
import com.ces.config.dhtmlx.entity.appmanage.ReportPrintSetting;
import com.ces.config.dhtmlx.entity.appmanage.ReportTable;
import com.ces.config.dhtmlx.entity.appmanage.ReportTableRelation;
import com.ces.config.dhtmlx.entity.appmanage.TableClassification;
import com.ces.config.dhtmlx.entity.appmanage.TableRelation;
import com.ces.config.dhtmlx.entity.appmanage.TableTree;
import com.ces.config.dhtmlx.entity.appmanage.TimingEntity;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowActivity;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowButtonSetting;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowFormSetting;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowTree;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowVersion;
import com.ces.config.dhtmlx.entity.authority.AuthorityCode;
import com.ces.config.dhtmlx.entity.authority.AuthorityData;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataDetail;
import com.ces.config.dhtmlx.entity.authority.AuthorityTree;
import com.ces.config.dhtmlx.entity.code.BusinessCode;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.code.CodeType;
import com.ces.config.dhtmlx.entity.component.CommonComponentRelation;
import com.ces.config.dhtmlx.entity.component.Component;
import com.ces.config.dhtmlx.entity.component.ComponentArea;
import com.ces.config.dhtmlx.entity.component.ComponentAssembleArea;
import com.ces.config.dhtmlx.entity.component.ComponentButton;
import com.ces.config.dhtmlx.entity.component.ComponentCallback;
import com.ces.config.dhtmlx.entity.component.ComponentCallbackParam;
import com.ces.config.dhtmlx.entity.component.ComponentClass;
import com.ces.config.dhtmlx.entity.component.ComponentColumn;
import com.ces.config.dhtmlx.entity.component.ComponentFunction;
import com.ces.config.dhtmlx.entity.component.ComponentFunctionData;
import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.entity.component.ComponentJar;
import com.ces.config.dhtmlx.entity.component.ComponentOutputParam;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameter;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameterRelation;
import com.ces.config.dhtmlx.entity.component.ComponentTable;
import com.ces.config.dhtmlx.entity.component.ComponentTableColumnRelation;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructCallback;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.construct.ConstructDetailSelfParam;
import com.ces.config.dhtmlx.entity.construct.ConstructFilter;
import com.ces.config.dhtmlx.entity.construct.ConstructFilterDetail;
import com.ces.config.dhtmlx.entity.construct.ConstructFunction;
import com.ces.config.dhtmlx.entity.construct.ConstructInputParam;
import com.ces.config.dhtmlx.entity.construct.ConstructSelfParam;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.config.dhtmlx.entity.label.ColumnLabel;
import com.ces.config.dhtmlx.entity.label.ColumnLabelCategory;
import com.ces.config.dhtmlx.entity.label.TypeLabel;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.menu.MenuInputParam;
import com.ces.config.dhtmlx.entity.menu.MenuSelfParam;
import com.ces.config.dhtmlx.entity.parameter.SystemParameter;
import com.ces.config.dhtmlx.entity.parameter.SystemParameterCategory;
import com.ces.config.dhtmlx.entity.release.Release;
import com.ces.config.dhtmlx.entity.release.ReleaseDetail;
import com.ces.config.dhtmlx.entity.resource.Resource;
import com.ces.config.dhtmlx.entity.resource.ResourceButton;
import com.ces.config.dhtmlx.entity.systemcomponent.SystemComponentVersion;
import com.ces.config.dhtmlx.entity.systemversion.SystemVersion;
import com.ces.config.dhtmlx.entity.systemversion.SystemVersionResource;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.appmanage.AppButtonService;
import com.ces.config.dhtmlx.service.appmanage.AppColumnService;
import com.ces.config.dhtmlx.service.appmanage.AppDefineService;
import com.ces.config.dhtmlx.service.appmanage.AppFormElementService;
import com.ces.config.dhtmlx.service.appmanage.AppFormService;
import com.ces.config.dhtmlx.service.appmanage.AppGridService;
import com.ces.config.dhtmlx.service.appmanage.AppReportService;
import com.ces.config.dhtmlx.service.appmanage.AppSearchPanelService;
import com.ces.config.dhtmlx.service.appmanage.AppSearchService;
import com.ces.config.dhtmlx.service.appmanage.AppSortService;
import com.ces.config.dhtmlx.service.appmanage.ColumnBusinessService;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.appmanage.ColumnOperationService;
import com.ces.config.dhtmlx.service.appmanage.ColumnRelationService;
import com.ces.config.dhtmlx.service.appmanage.ColumnSpliceService;
import com.ces.config.dhtmlx.service.appmanage.ColumnSplitService;
import com.ces.config.dhtmlx.service.appmanage.LogicClassificationService;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupRelationService;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.LogicTableRelationService;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupRelationService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.ReportColumnService;
import com.ces.config.dhtmlx.service.appmanage.ReportDataSourceService;
import com.ces.config.dhtmlx.service.appmanage.ReportDefineService;
import com.ces.config.dhtmlx.service.appmanage.ReportPrintSettingService;
import com.ces.config.dhtmlx.service.appmanage.ReportService;
import com.ces.config.dhtmlx.service.appmanage.ReportTableRelationService;
import com.ces.config.dhtmlx.service.appmanage.ReportTableService;
import com.ces.config.dhtmlx.service.appmanage.TableClassificationService;
import com.ces.config.dhtmlx.service.appmanage.TableRelationService;
import com.ces.config.dhtmlx.service.appmanage.TableTreeService;
import com.ces.config.dhtmlx.service.appmanage.TimingService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowActivityService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowButtonSettingService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowFormSettingService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowTreeService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
import com.ces.config.dhtmlx.service.authority.AuthorityCodeService;
import com.ces.config.dhtmlx.service.authority.AuthorityDataDetailService;
import com.ces.config.dhtmlx.service.authority.AuthorityDataService;
import com.ces.config.dhtmlx.service.authority.AuthorityTreeService;
import com.ces.config.dhtmlx.service.code.BusinessCodeService;
import com.ces.config.dhtmlx.service.code.CodeService;
import com.ces.config.dhtmlx.service.code.CodeTypeService;
import com.ces.config.dhtmlx.service.component.CommonComponentRelationService;
import com.ces.config.dhtmlx.service.component.ComponentAreaService;
import com.ces.config.dhtmlx.service.component.ComponentAssembleAreaService;
import com.ces.config.dhtmlx.service.component.ComponentButtonService;
import com.ces.config.dhtmlx.service.component.ComponentCallbackParamService;
import com.ces.config.dhtmlx.service.component.ComponentCallbackService;
import com.ces.config.dhtmlx.service.component.ComponentClassService;
import com.ces.config.dhtmlx.service.component.ComponentColumnService;
import com.ces.config.dhtmlx.service.component.ComponentFunctionDataService;
import com.ces.config.dhtmlx.service.component.ComponentFunctionService;
import com.ces.config.dhtmlx.service.component.ComponentInputParamService;
import com.ces.config.dhtmlx.service.component.ComponentJarService;
import com.ces.config.dhtmlx.service.component.ComponentOutputParamService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentSelfParamService;
import com.ces.config.dhtmlx.service.component.ComponentSystemParameterRelationService;
import com.ces.config.dhtmlx.service.component.ComponentSystemParameterService;
import com.ces.config.dhtmlx.service.component.ComponentTableColumnRelationService;
import com.ces.config.dhtmlx.service.component.ComponentTableService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructCallbackService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailSelfParamService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructFilterDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructFilterService;
import com.ces.config.dhtmlx.service.construct.ConstructFunctionService;
import com.ces.config.dhtmlx.service.construct.ConstructInputParamService;
import com.ces.config.dhtmlx.service.construct.ConstructSelfParamService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.database.DatabaseService;
import com.ces.config.dhtmlx.service.label.ColumnLabelCategoryService;
import com.ces.config.dhtmlx.service.label.ColumnLabelService;
import com.ces.config.dhtmlx.service.label.TypeLabelService;
import com.ces.config.dhtmlx.service.menu.MenuInputParamService;
import com.ces.config.dhtmlx.service.menu.MenuSelfParamService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.dhtmlx.service.parameter.SystemParameterCategoryService;
import com.ces.config.dhtmlx.service.parameter.SystemParameterService;
import com.ces.config.dhtmlx.service.release.ReleaseService;
import com.ces.config.dhtmlx.service.resource.ResourceButtonService;
import com.ces.config.dhtmlx.service.resource.ResourceService;
import com.ces.config.dhtmlx.service.systemcomponent.SystemComponentVersionService;
import com.ces.config.dhtmlx.service.systemversion.SystemVersionResourceService;
import com.ces.config.dhtmlx.service.systemversion.SystemVersionService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.AuthDatabaseUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FileUtil;
import com.ces.config.utils.PreviewUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.SystemParameterUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.config.utils.ZipUtil;
import com.ces.config.utils.release.ReleaseUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

/**
 * 系统发布Controller
 * 
 * @author wanglei
 * @date 2013-11-15
 */
public class ReleaseController extends ConfigDefineServiceDaoController<Release, ReleaseService, ReleaseDao> {

    private static final long serialVersionUID = 8371039330586502438L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new Release());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("releaseService")
    @Override
    protected void setService(ReleaseService service) {
        super.setService(service);
    }

    /**
     * 获取系统发布树
     * 
     * @return Object
     */
    public Object getReleaseTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        boolean open = BooleanUtils.toBoolean(getParameter("P_OPEN"));
        // 树节点ID，"M_"表示菜单，"RZ_"表示预留区，"CD_"表示组装的按钮
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if (treeNodeId.startsWith("M_")) {
            String parentMenuId = treeNodeId.replace("M_", "");
            Menu parentMenu = getService(MenuService.class).getByID(parentMenuId);
            if (StringUtil.isEmpty(parentMenu.getBindingType())) {
                // 加载子菜单
                List<Menu> menuList = getService(MenuService.class).getMenuByParentId(parentMenuId);
                if (CollectionUtils.isNotEmpty(menuList)) {
                    for (Menu menu : menuList) {
                        treeNode = new DhtmlxTreeNode();
                        treeNode.setId("M_" + menu.getId());
                        treeNode.setText(menu.getName());
                        treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setChild("1");
                        if (menu.getHasChild()) {
                            treeNode.setOpen(true);
                        } else {
                            treeNode.setOpen(open);
                        }
                        treeNodelist.add(treeNode);
                    }
                }
            } else if ("1".equals(parentMenu.getBindingType())) {
                // 绑定构件，如果是组合构件，树组合构件则加载绑定到树上的构件，其他则加载预留区
                ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(parentMenu.getComponentVersionId());
                if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                    ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                    if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())) {
                        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getConstructInfoByConstructId(construct.getId());
                        if (CollectionUtils.isNotEmpty(constructDetailList)) {
                            ComponentVersion bindingComponentVersion = null;
                            for (ConstructDetail constructDetail : constructDetailList) {
                                if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
                                    bindingComponentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                                    treeNode = new DhtmlxTreeNode();
                                    treeNode.setId("CD_" + constructDetail.getId());
                                    treeNode.setText(constructDetail.getComponentAliasAndVersion());
                                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                    if (ConstantVar.Component.Type.ASSEMBLY.equals(bindingComponentVersion.getComponent().getType())) {
                                        treeNode.setChild("1");
                                    } else {
                                        treeNode.setChild("0");
                                    }
                                    treeNode.setOpen(open);
                                    treeNodelist.add(treeNode);
                                }
                            }
                        }
                    } else {
                        List<ComponentReserveZone> componentReserveZoneList = null;
                        if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                            Module module = getService(ModuleService.class).findByComponentVersionId(baseComponentVersion.getId());
                            componentReserveZoneList = AppDefineUtil.getCommonReserveZone(module, null);
                        } else {
                            componentReserveZoneList = getService(ComponentReserveZoneService.class).getByComponentVersionId(
                                    construct.getBaseComponentVersionId());
                        }
                        if (CollectionUtils.isNotEmpty(componentReserveZoneList)) {
                            for (ComponentReserveZone reserveZone : componentReserveZoneList) {
                                if (getService(ConstructDetailService.class).isBindingComponent(construct.getId(), reserveZone.getId())) {
                                    treeNode = new DhtmlxTreeNode();
                                    treeNode.setId("RZ_" + reserveZone.getId() + "_" + construct.getId());
                                    treeNode.setText(reserveZone.getAlias());
                                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                    treeNode.setChild("1");
                                    treeNode.setOpen(open);
                                    treeNodelist.add(treeNode);
                                }
                            }
                        }
                    }
                }
            }
        } else if (treeNodeId.startsWith("RZ_")) {
            String[] ids = treeNodeId.split("_");
            String reserveZoneId = ids[1];
            String constructId = ids[2];
            List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructIdAndReserveZoneId(constructId, reserveZoneId);
            if (CollectionUtils.isNotEmpty(constructDetailList)) {
                ComponentVersion bindingComponentVersion = null;
                for (ConstructDetail constructDetail : constructDetailList) {
                    if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
                        bindingComponentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                        treeNode = new DhtmlxTreeNode();
                        treeNode.setId("CD_" + constructDetail.getId());
                        treeNode.setText(bindingComponentVersion.getComponent().getAlias() + "_" + bindingComponentVersion.getVersion());
                        treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        if (ConstantVar.Component.Type.ASSEMBLY.equals(bindingComponentVersion.getComponent().getType())) {
                            treeNode.setChild("1");
                        } else {
                            treeNode.setChild("0");
                        }
                        treeNode.setOpen(open);
                        treeNodelist.add(treeNode);
                    }
                }
            }
        } else if (treeNodeId.startsWith("CD_")) {
            String[] ids = treeNodeId.split("_");
            String constructDetailId = ids[1];
            ConstructDetail parentConstructDetail = getService(ConstructDetailService.class).getByID(constructDetailId);
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(parentConstructDetail.getComponentVersionId());
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())) {
                    List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getConstructInfoByConstructId(construct.getId());
                    if (CollectionUtils.isNotEmpty(constructDetailList)) {
                        ComponentVersion bindingComponentVersion = null;
                        for (ConstructDetail constructDetail : constructDetailList) {
                            if (StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
                                bindingComponentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                                treeNode = new DhtmlxTreeNode();
                                treeNode.setId("CD_" + constructDetail.getId());
                                treeNode.setText(constructDetail.getComponentAliasAndVersion());
                                treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                                treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                                treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                if (ConstantVar.Component.Type.ASSEMBLY.equals(bindingComponentVersion.getComponent().getType())) {
                                    treeNode.setChild("1");
                                } else {
                                    treeNode.setChild("0");
                                }
                                treeNode.setOpen(open);
                                treeNodelist.add(treeNode);
                            }
                        }
                    }
                } else {
                    List<ComponentReserveZone> componentReserveZoneList = null;
                    if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                        Module module = getService(ModuleService.class).findByComponentVersionId(baseComponentVersion.getId());
                        componentReserveZoneList = AppDefineUtil.getCommonReserveZone(module, null);
                    } else {
                        componentReserveZoneList = getService(ComponentReserveZoneService.class).getByComponentVersionId(construct.getBaseComponentVersionId());
                    }
                    if (CollectionUtils.isNotEmpty(componentReserveZoneList)) {
                        for (ComponentReserveZone reserveZone : componentReserveZoneList) {
                            if (getService(ConstructDetailService.class).isBindingComponent(construct.getId(), reserveZone.getId())) {
                                treeNode = new DhtmlxTreeNode();
                                treeNode.setId("RZ_" + reserveZone.getId() + "_" + construct.getId());
                                treeNode.setText(reserveZone.getAlias());
                                treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                                treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                                treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                                treeNode.setChild("1");
                                treeNode.setOpen(open);
                                treeNodelist.add(treeNode);
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
     * 校验某系统的某版本是否存在
     * 
     * @return Object
     */
    public Object validateVersions() {
        String rootMenuId = getParameter("rootMenuId");
        String version = getParameter("version");
        Release temp = getService().getByRootMenuIdAndVersion(rootMenuId, version);
        if (null != temp) {
            setReturnData("{'exist':true,'type':'" + temp.getType() + "'}");
        } else {
            setReturnData("{'exist':false}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验选择的菜单中是否包含构件
     * 
     * @return Object
     */
    public Object validateComponents() {
        String menuIds = getParameter("checkedIds");
        menuIds = menuIds.replaceAll("M_", "");
        // 发布时选择的菜单
        List<Menu> menuList = getService(MenuService.class).getMenusByIds(menuIds);
        boolean flag = false;
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                if ("1".equals(menu.getBindingType())) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            setReturnData("{'hasComponent':true}");
        } else {
            setReturnData("{'hasComponent':false}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 发布工程
     * 
     * @return Object
     */
    public Object releaseProject() {
        Map<String, Long> timeMap = new HashMap<String, Long>();
        timeMap.put("start", System.currentTimeMillis());
        String rootMenuId = getParameter("rootMenuId");
        String version = getParameter("version");
        String systemVersionId = getParameter("systemVersionId");
        // 生成文件名
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = df.format(new Date()) + "_" + new Random().nextInt(1000);
        String selfProjectPath = ComponentFileUtil.getProjectPath();
        String selfSrcPath = ComponentFileUtil.getSrcPath();
        String releaseProjectBasePath = ComponentFileUtil.getReleaseTempPath() + fileName + "/";
        String releaseProjectPath = releaseProjectBasePath + "WebRoot/";
        String releaseSrcPath = releaseProjectBasePath + "src/";
        try {
            File releaseProjectBaseDir = new File(releaseProjectBasePath);
            if (!releaseProjectBaseDir.exists()) {
                releaseProjectBaseDir.mkdirs();
            }
            // 1、复制工程文件
            copyProjectFile(selfProjectPath, selfSrcPath, releaseProjectPath, releaseSrcPath);
            timeMap.put("time1", System.currentTimeMillis());
            // 获取发布的菜单
            List<Menu> menuList = getService(MenuService.class).getMenuByRootMenuId(rootMenuId);
            StringBuilder menuIdSb = new StringBuilder();
            Set<String> menuIdSet = new HashSet<String>();
            for (Menu menu : menuList) {
                menuIdSet.add(menu.getId());
                menuIdSb.append(menu.getId()).append(",");
            }
            menuIdSb.deleteCharAt(menuIdSb.length() - 1);
            // 查找该系统关联的所有构件
            Set<ComponentVersion> componentVersionSet = getService(MenuService.class).getComponentVersions(menuIdSb.toString(), null);
            componentVersionSet.addAll(getService(SystemComponentVersionService.class).getComponentVersions(rootMenuId));
            // 2、复制构件相关文件
            copyComponentFile(selfProjectPath, selfSrcPath, releaseProjectPath, releaseSrcPath, componentVersionSet);
            timeMap.put("time2", System.currentTimeMillis());
            // 3、在数据库脚本中追加构件相关表的创建脚本
            createComponentTables(componentVersionSet, releaseProjectPath + "docs/oracle/6_component_table.sql", "oracle");
            createComponentTables(componentVersionSet, releaseProjectPath + "docs/sqlserver/6_component_table.sql", "sqlserver");
            timeMap.put("time3", System.currentTimeMillis());
            // 4、系统数据脚本
            Map<String, Object> map = getDatas(rootMenuId, menuList, menuIdSb.toString(), componentVersionSet, systemVersionId);
            timeMap.put("time4", System.currentTimeMillis());
            createDataSqls(rootMenuId, version, map, releaseProjectPath + "docs/oracle/", "oracle");
            createDataSqls(rootMenuId, version, map, releaseProjectPath + "docs/sqlserver/", "sqlserver");
            timeMap.put("time5", System.currentTimeMillis());
            // 5、更改cfg_common.properties中released_system的状态
            boolean updateReleaseSystemFlag = updateReleaseSystemFlag(releaseProjectPath);
            if (!updateReleaseSystemFlag) {
                setReturnData("{'success':false}");
                // 删除发布的临时文件
                FileUtil.deleteFile(releaseProjectBasePath);
                return new DefaultHttpHeaders(SUCCESS).disableCaching();
            }
            // 6、删除无用的文件
            FileUtil.deleteFile(releaseProjectPath + "velocity.log");
            // 删除无用的工作流文件
            boolean deleteUnusedWfFile = deleteUnusedWfFile(releaseProjectPath, map);
            if (!deleteUnusedWfFile) {
                setReturnData("{'success':false}");
                // 删除发布的临时文件
                FileUtil.deleteFile(releaseProjectBasePath);
                return new DefaultHttpHeaders(SUCCESS).disableCaching();
            }
            // 7、打包
            ZipUtil.zip(new File(ComponentFileUtil.getReleaseTempPath() + fileName + ".zip"), "", new File(releaseProjectBasePath));
            timeMap.put("time6", System.currentTimeMillis());
            // 8、删除发布的临时文件
            FileUtil.deleteFile(releaseProjectBasePath);
            // 9、保存Release
            saveReleaseProject(fileName, menuIdSet);
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData("{'success':false}");
            // 删除发布的临时文件
            FileUtil.deleteFile(releaseProjectBasePath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }
        timeMap.put("end", System.currentTimeMillis());
        System.out.println("复制系统时间:" + (timeMap.get("time1") - timeMap.get("start")));
        System.out.println("复制构件文件时间:" + (timeMap.get("time2") - timeMap.get("time1")));
        System.out.println("创建component_table.sql时间:" + (timeMap.get("time3") - timeMap.get("time2")));
        System.out.println("获取数据库信息时间:" + (timeMap.get("time4") - timeMap.get("time3")));
        System.out.println("创建init.sql和selfdefine_table.sql时间:" + (timeMap.get("time5") - timeMap.get("time4")));
        System.out.println("打zip包时间:" + (timeMap.get("time6") - timeMap.get("time5")));
        System.out.println("总耗时:" + (timeMap.get("end") - timeMap.get("start")));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 复制工程文件
     */
    private void copyProjectFile(String selfProjectPath, String selfSrcPath, String releaseProjectPath, String releaseSrcPath) {
        Set<File> unCopyDirSet = new HashSet<File>();
        unCopyDirSet.add(new File(selfSrcPath + "component/com/ces/component/"));
        unCopyDirSet.add(new File(selfSrcPath + "selfdefine/com/ces/component/"));
        unCopyDirSet.add(new File(selfProjectPath + "cfg-resource/dhtmlx/views/component/"));
        unCopyDirSet.add(new File(selfProjectPath + "cfg-resource/dhtmlx/views/selfdefine/"));
        unCopyDirSet.add(new File(selfProjectPath + "cfg-resource/coral40/views/component/"));
        unCopyDirSet.add(new File(selfProjectPath + "cfg-resource/coral40/views/selfdefine/"));
        unCopyDirSet.add(new File(selfProjectPath + "WEB-INF/classes/com/ces/component/"));
        unCopyDirSet.add(new File(selfProjectPath + "components/component_lib/"));
        unCopyDirSet.add(new File(selfProjectPath + "components/component_lib_uncompress/"));
        unCopyDirSet.add(new File(selfProjectPath + "components/temp_component_lib/"));
        unCopyDirSet.add(new File(selfProjectPath + "components/temp_component_lib_uncompress/"));
        copyProject(selfProjectPath, releaseProjectPath, unCopyDirSet);
        if (new File(selfSrcPath).exists()) {
            copyProject(selfSrcPath, releaseSrcPath, unCopyDirSet);
        }
    }

    /**
     * 复制构件相关文件
     */
    private void copyComponentFile(String selfProjectPath, String selfSrcPath, String releaseProjectPath, String releaseSrcPath,
            Set<ComponentVersion> componentVersionSet) {
        for (ComponentVersion componentVersion : componentVersionSet) {
            if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1) {
                String packageName = componentVersion.getComponent().getName().toLowerCase();
                FileUtil.copyFolder(selfSrcPath + "selfdefine/com/ces/component/" + packageName, releaseSrcPath + "selfdefine/com/ces/component/" + packageName);
                FileUtil.copyFolder(selfProjectPath + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/" + packageName, releaseProjectPath
                        + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/" + packageName);
                FileUtil.copyFolder(selfProjectPath + "WEB-INF/classes/com/ces/component/" + packageName, releaseProjectPath
                        + "WEB-INF/classes/com/ces/component/" + packageName);
            } else if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())
                    || ConstantVar.Component.Type.LOGIC.equals(componentVersion.getComponent().getType())
                    || ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                PreviewUtil.copyComponentFile(componentVersion, releaseProjectPath);
            }
            String zipName = componentVersion.getPath();
            if (StringUtil.isNotEmpty(zipName)) {
                String uncompressZipName = zipName.substring(0, zipName.lastIndexOf("."));
                File zipFile = new File(selfProjectPath + "components/component_lib/" + zipName);
                if (zipFile.exists()) {
                    FileUtil.nioTransferCopy(zipFile, new File(releaseProjectPath + "components/component_lib/" + zipName));
                    FileUtil.copyFolder(selfProjectPath + "components/component_lib/" + uncompressZipName, releaseProjectPath + "components/component_lib/"
                            + uncompressZipName);
                }
            }
        }
    }

    /**
     * 更改cfg_common.properties中released_system的状态
     * 
     * @param releaseProjectPath 发布工程的路径
     * @return boolean
     */
    private boolean updateReleaseSystemFlag(String releaseProjectPath) {
        boolean flag = true;
        BufferedReader br = null;
        String line = null;
        StringBuffer buf = new StringBuffer();
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(releaseProjectPath + "WEB-INF/conf/prop/cfg_common.properties"), "UTF-8"));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("released_system")) {
                    buf.append("released_system=true");
                } else {
                    buf.append(line);
                }
                buf.append(System.getProperty("line.separator"));
            }
            bw = new BufferedWriter(new FileWriter(releaseProjectPath + "WEB-INF/conf/prop/cfg_common.properties"));
            bw.write(buf.toString());
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    bw = null;
                }
            }
        }
        return flag;
    }

    /**
     * 删除无效的工作流文件
     * 
     * @param releaseProjectPath 发布工程的路径
     * @param map 发布系统的数据
     * @return boolean
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean deleteUnusedWfFile(String releaseProjectPath, Map<String, Object> map) {
        boolean flag = true;
        Set<String> wfXpdlFileNameSet = (Set<String>) map.get("WFXpdlFileName");
        Set<String> wfPackIdAndVersionSet = (Set<String>) map.get("WFPackIdAndVersion");
        File configDir = new File(releaseProjectPath + "WEB-INF/conf/");
        File[] configDirFiles = configDir.listFiles();
        if (configDirFiles != null && configDirFiles.length > 0) {
            for (File file : configDirFiles) {
                String name = file.getName();
                if (name.endsWith(".xpdl") && !wfXpdlFileNameSet.contains(name)) {
                    file.delete();
                }
            }
        }
        File tempxpdlDir = new File(releaseProjectPath + "WEB-INF/conf/tempxpdl/");
        File[] tempxpdlDirFiles = tempxpdlDir.listFiles();
        if (tempxpdlDirFiles != null && tempxpdlDirFiles.length > 0) {
            for (File file : tempxpdlDirFiles) {
                String name = file.getName();
                if (name.endsWith(".xpdl") && !wfXpdlFileNameSet.contains(name)) {
                    file.delete();
                }
            }
        }
        // 更改Repository.xml文件
        XMLWriter xmlWriter = null;
        Writer writer = null;
        try {
            SAXReader reader = new SAXReader();
            File repositoryFile = new File(releaseProjectPath + "WEB-INF/conf/Repository.xml");
            Document doc = reader.read(repositoryFile);
            Element root = doc.getRootElement();
            Element defineXmlFiles = root.element("DefineXmlFiles");
            if (defineXmlFiles != null) {
                Element defineXmlFile = null;
                for (Iterator parameterIterator = defineXmlFiles.elementIterator("DefineXmlFile"); parameterIterator.hasNext();) {
                    defineXmlFile = (Element) parameterIterator.next();
                    String packageId = defineXmlFile.attributeValue("packageId");
                    String packageVersion = defineXmlFile.attributeValue("packageVersion");
                    if (!wfPackIdAndVersionSet.contains(packageId + "$$" + packageVersion)) {
                        defineXmlFiles.remove(defineXmlFile);
                    }
                }
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            format.setNewLineAfterDeclaration(false);
            format.setIndent("\t");
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (xmlWriter != null) {
                try {
                    xmlWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 保存发布系统的信息
     * 
     * @param fileName 发布系统的包名
     * @param menuIdSet 发布系统中的菜单
     */
    private void saveReleaseProject(String fileName, Set<String> menuIdSet) {
        String rootMenuId = getParameter("rootMenuId");
        String version = getParameter("version");
        String remark = getParameter("remark");
        String systemVersionId = getParameter("systemVersionId");
        Release release = getService().getByRootMenuIdAndVersion(rootMenuId, version);
        if (release == null) {
            release = new Release();
            Menu rootMenu = getService(MenuService.class).getByID(rootMenuId);
            release.setRootMenuId(rootMenu.getId());
            release.setSystemName(rootMenu.getName());
            release.setReleaseDate(new Date());
            release.setVersion(version);
            release.setSystemVersionId(systemVersionId);
            release.setFileName(fileName);
            release.setRemark(remark);
            release.setType("0");
        } else {
            FileUtil.deleteFile(ComponentFileUtil.getReleaseTempPath() + release.getFileName() + ".zip");
            release.setReleaseDate(new Date());
            release.setSystemVersionId(systemVersionId);
            release.setFileName(fileName);
            release.setRemark(remark);
        }
        release = getService().save(release);
        setReturnData("{'success':true}");
        final Release fRelease = release;
        final Set<String> fMenuIdSet = new HashSet<String>(menuIdSet);
        // 开启线程，用于记录发布详情
        new Thread() {
            @Override
            public void run() {
                getService().saveReleaseSystem(fRelease, fMenuIdSet);
            }
        }.start();
    }

    /**
     * 发布更新包
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object releaseUpdatePackage() {
        String rootMenuId = getParameter("rootMenuId");
        String version = getParameter("version");
        String releaseSystemVersion = getParameter("releaseSystemVersion");
        String remark = getParameter("remark");
        String checkedIds = getParameter("checkedIds");
        // 发布时选择的菜单
        Set<String> menuIdSet = new HashSet<String>();
        // 发布时选择的预留区
        Set<String> reserveZoneIdSet = new HashSet<String>();
        // 发布是选择的构件
        Set<String> constructDetailIdSet = new HashSet<String>();
        String[] checkedIdArr = checkedIds.split(",");
        for (String checkedId : checkedIdArr) {
            String[] strs = checkedId.split("_");
            if (checkedId.startsWith("M_")) {
                menuIdSet.add(strs[1]);
            } else if (checkedId.startsWith("RZ_")) {
                reserveZoneIdSet.add(strs[1] + "_" + strs[2]);
            } else if (checkedId.startsWith("CD_")) {
                constructDetailIdSet.add(strs[1]);
            }
        }
        // 获取信息
        Map<String, Object> map = getService().getUpdatePackageInfo(rootMenuId, menuIdSet, reserveZoneIdSet, constructDetailIdSet);
        // 生成文件名
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = df.format(new Date()) + "_" + new Random().nextInt(1000);
        String releaseUpdatePackagePath = ComponentFileUtil.getReleaseTempPath() + fileName + "/";
        String packageComponentLibPath = releaseUpdatePackagePath + "component_lib/";
        File packageComponentLibDir = new File(packageComponentLibPath);
        if (!packageComponentLibDir.exists()) {
            packageComponentLibDir.mkdirs();
        }
        // 1、复制构件相关文件
        // 查找该更新包关联的所有构件
        Map<String, ComponentVersion> componentVersionMap = (Map<String, ComponentVersion>) map.get("ComponentVersion");
        Collection<ComponentVersion> componentVersions = componentVersionMap.values();
        if (CollectionUtils.isNotEmpty(componentVersions)) {
            ComponentVersion componentVersion = null;
            try {
                for (Iterator<ComponentVersion> it = componentVersions.iterator(); it.hasNext();) {
                    componentVersion = it.next();
                    if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1) {
                        // 重新打包该自定义构件
                        ComponentFileUtil.packageComponent(componentVersion.getId(), true);
                        componentVersion = getService(ComponentVersionService.class).getByID(componentVersion.getId());
                        FileUtil.copyFile(ComponentFileUtil.getCompPath() + componentVersion.getPath(), packageComponentLibPath + componentVersion.getPath());

                    } else if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.LOGIC.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                        FileUtil.copyFile(ComponentFileUtil.getCompPath() + componentVersion.getPath(), packageComponentLibPath + componentVersion.getPath());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                setReturnData("{'success':false, 'message':'发布失败，原因是打包构件'" + componentVersion.getComponent().getAlias() + "'失败！'}");
                // 删除发布的临时文件
                FileUtil.deleteFile(ComponentFileUtil.getReleaseTempPath() + fileName);
                return new DefaultHttpHeaders(SUCCESS).disableCaching();
            }
        }
        // 2、生成配置文件
        createUpdatePackageInfoJsonFile(rootMenuId, releaseSystemVersion, version, releaseUpdatePackagePath + "system.json");
        createUpdatePackageConfigJsonFile(map, releaseUpdatePackagePath + "config.json");
        // 3、打包
        ZipUtil.zip(new File(ComponentFileUtil.getReleaseTempPath() + fileName + ".zip"), "", new File(ComponentFileUtil.getReleaseTempPath() + fileName));
        // 4、删除发布的临时文件
        FileUtil.deleteFile(ComponentFileUtil.getReleaseTempPath() + fileName);
        // 5、保存Release
        Release release = getService().getByRootMenuIdAndVersion(rootMenuId, version);
        if (release == null) {
            release = new Release();
            Menu rootMenu = getService(MenuService.class).getByID(rootMenuId);
            release.setRootMenuId(rootMenu.getId());
            release.setSystemName(rootMenu.getName());
            release.setReleaseDate(new Date());
            release.setVersion(version);
            release.setFileName(fileName);
            release.setRemark(remark);
            release.setType("1");
        } else {
            FileUtil.deleteFile(ComponentFileUtil.getReleaseTempPath() + release.getFileName() + ".zip");
            release.setReleaseDate(new Date());
            release.setFileName(fileName);
            release.setRemark(remark);
        }
        try {
            List<ReleaseDetail> releaseDetailList = (List<ReleaseDetail>) map.get("ReleaseDetail");
            getService().saveReleaseUpdatePackage(release, releaseDetailList);
            setReturnData("{'success':true, 'message':'发布成功！'}");
        } catch (Exception e) {
            setReturnData("{'success':false, 'message':'发布失败！'}");
            FileUtil.deleteFile(ComponentFileUtil.getReleaseTempPath() + fileName + ".zip");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 准备更新包的系统信息
     * 
     * @param rootMenuId 根菜单ID
     * @param releaseSystemVersion 发布的系统版本
     * @param version 发布的更新包版本
     * @param initJsonPath 系统信息文件路径
     */
    private void createUpdatePackageInfoJsonFile(String rootMenuId, String releaseSystemVersion, String version, String initJsonPath) {
        // 封装的JSON信息
        Map<String, String> map = new HashMap<String, String>();
        map.put("systemId", rootMenuId);
        map.put("systemVersion", releaseSystemVersion);
        map.put("updatePackageVersion", version);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(initJsonPath);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(fos, map);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 准备更新包配置信息
     * 
     * @param map 更新包信息
     * @param initJsonPath 更新包配置信息文件路径
     */
    @SuppressWarnings("unchecked")
    private void createUpdatePackageConfigJsonFile(Map<String, Object> updatePackageMap, String initJsonPath) {
        List<Menu> menuListOfUPM = (List<Menu>) updatePackageMap.get("Menu");
        Map<String, ComponentVersion> componentVersionMapOfUPM = (Map<String, ComponentVersion>) updatePackageMap.get("ComponentVersion");
        Map<String, Construct> constructMapOfUPM = (Map<String, Construct>) updatePackageMap.get("Construct");
        Map<String, List<ConstructDetail>> constructDetailMapOfUPM = (Map<String, List<ConstructDetail>>) updatePackageMap.get("ConstructDetail");
        // 封装的JSON信息
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
        Map<String, Object> menuMap = null;
        List<Map<String, Object>> constructList = new ArrayList<Map<String, Object>>();
        Map<String, Object> constructMap = null;
        List<Map<String, Object>> constructDetailList = null;
        Map<String, Object> constructDetailMap = null;
        Map<String, String> baseInfo = null;
        for (Menu menu : menuListOfUPM) {
            if (StringUtil.isNotEmpty(menu.getComponentVersionId())) {
                menuMap = new HashMap<String, Object>();
                menuMap.put("baseInfo", menu);
                menuMap.put("MenuInputParams", getService(MenuInputParamService.class).getByMenuId(menu.getId()));
                menuMap.put("MenuSelfParams", getService(MenuSelfParamService.class).getByMenuId(menu.getId()));
                menuList.add(menuMap);
            }
        }
        map.put("Menus", menuList);
        if (MapUtils.isNotEmpty(constructMapOfUPM)) {
            Construct construct = null;
            for (Iterator<String> i = constructMapOfUPM.keySet().iterator(); i.hasNext();) {
                construct = constructMapOfUPM.get(i.next());
                constructMap = new HashMap<String, Object>();
                baseInfo = new HashMap<String, String>();
                baseInfo.put("id", construct.getId());
                baseInfo.put("baseComponentVersionId", construct.getBaseComponentVersionId());
                baseInfo.put("componentVersionId", construct.getAssembleComponentVersion().getId());
                constructMap.put("baseInfo", baseInfo);
                constructMap.put("ConstructSelfParams", getService(ConstructSelfParamService.class).getByConstructId(construct.getId()));
                constructMap.put("ConstructInputParams", getService(ConstructInputParamService.class).getByConstructId(construct.getId()));
                List<ConstructDetail> tempConstructDetailList = constructDetailMapOfUPM.get(construct.getId());
                if (CollectionUtils.isNotEmpty(tempConstructDetailList)) {
                    constructDetailList = new ArrayList<Map<String, Object>>();
                    constructMap.put("ConstructDetails", constructDetailList);
                    for (ConstructDetail constructDetail : tempConstructDetailList) {
                        constructDetailMap = new HashMap<String, Object>();
                        constructDetailMap.put("baseInfo", constructDetail);
                        constructDetailMap.put("ConstructDetailSelfParams",
                                getService(ConstructDetailSelfParamService.class).getByConstructDetailId(constructDetail.getId()));
                        constructDetailMap
                                .put("ConstructFunctions", getService(ConstructFunctionService.class).getByConstructDetailId(constructDetail.getId()));
                        constructDetailMap
                                .put("ConstructCallbacks", getService(ConstructCallbackService.class).getByConstructDetailId(constructDetail.getId()));
                        constructDetailList.add(constructDetailMap);
                    }
                }
                constructList.add(constructMap);
            }
        }
        map.put("Constructs", constructList);
        List<Map<String, Object>> componentVersionList = new ArrayList<Map<String, Object>>();
        Map<String, Object> componentVersionMap = null;
        if (MapUtils.isNotEmpty(componentVersionMapOfUPM)) {
            List<Map<String, Object>> componentFunctionMapList = null;
            Map<String, Object> componentFunctionMap = null;
            List<ComponentFunction> componentFunctionList = null;
            List<Map<String, Object>> componentCallbackMapList = null;
            Map<String, Object> componentCallbackMap = null;
            List<ComponentCallback> componentCallbackList = null;
            ComponentVersion cv = null;
            List<Map<String, Object>> componentTableMapList = null;
            Map<String, Object> componentTableMap = null;
            List<ComponentTable> componentTableList = null;
            for (Iterator<String> i = componentVersionMapOfUPM.keySet().iterator(); i.hasNext();) {
                cv = componentVersionMapOfUPM.get(i.next());
                componentVersionMap = new HashMap<String, Object>();
                baseInfo = new HashMap<String, String>();
                baseInfo.put("id", cv.getId());
                baseInfo.put("version", cv.getVersion());
                baseInfo.put("views", cv.getViews());
                baseInfo.put("url", cv.getUrl());
                baseInfo.put("remark", cv.getRemark());
                baseInfo.put("areaId", cv.getAreaId());
                baseInfo.put("path", cv.getPath());
                baseInfo.put("isPackage", cv.getIsPackage());
                baseInfo.put("systemParamConfig", cv.getSystemParamConfig());
                baseInfo.put("packageTime", cv.getPackageTime());
                componentVersionMap.put("baseInfo", baseInfo);
                componentVersionMap.put("Component", cv.getComponent());
                componentVersionMap.put("ComponentReserveZones", getService(ComponentReserveZoneService.class).getByComponentVersionId(cv.getId()));
                componentVersionMap.put("ComponentSystemParameters", getService(ComponentSystemParameterService.class).getByComponentVersionId(cv.getId()));
                componentVersionMap.put("ComponentSystemParameterRelations",
                        getService(ComponentSystemParameterRelationService.class).getByComponentVersionId(cv.getId()));
                componentVersionMap.put("ComponentSelfParams", getService(ComponentSelfParamService.class).getByComponentVersionId(cv.getId()));
                componentVersionMap.put("ComponentInputParams", getService(ComponentInputParamService.class).getByComponentVersionId(cv.getId()));
                componentVersionMap.put("ComponentOutputParams", getService(ComponentOutputParamService.class).getByComponentVersionId(cv.getId()));
                componentFunctionMapList = new ArrayList<Map<String, Object>>();
                componentVersionMap.put("ComponentFunctions", componentFunctionMapList);
                componentFunctionList = getService(ComponentFunctionService.class).getByComponentVersionId(cv.getId());
                if (CollectionUtils.isNotEmpty(componentFunctionList)) {
                    for (ComponentFunction cf : componentFunctionList) {
                        componentFunctionMap = new HashMap<String, Object>();
                        componentFunctionMap.put("baseInfo", cf);
                        componentFunctionMap.put("ComponentFunctionDatas", getService(ComponentFunctionDataService.class).getByFunctionId(cf.getId()));
                        componentFunctionMapList.add(componentFunctionMap);
                    }
                }
                componentCallbackMapList = new ArrayList<Map<String, Object>>();
                componentVersionMap.put("ComponentCallbacks", componentCallbackMapList);
                componentCallbackList = getService(ComponentCallbackService.class).getByComponentVersionId(cv.getId());
                if (CollectionUtils.isNotEmpty(componentCallbackList)) {
                    for (ComponentCallback cf : componentCallbackList) {
                        componentCallbackMap = new HashMap<String, Object>();
                        componentCallbackMap.put("baseInfo", cf);
                        componentCallbackMap.put("ComponentCallbackParams", getService(ComponentCallbackParamService.class).getByCallbackId(cf.getId()));
                        componentCallbackMapList.add(componentCallbackMap);
                    }
                }
                componentVersionMap.put("ComponentButtons", getService(ComponentButtonService.class).getByComponentVersionId(cv.getId()));
                componentVersionMap.put("ComponentClasses", getService(ComponentClassService.class).getByComponentVersionId(cv.getId()));
                componentVersionMap.put("ComponentJars", getService(ComponentJarService.class).getByComponentVersionId(cv.getId()));
                componentTableMapList = new ArrayList<Map<String, Object>>();
                componentTableList = getService(ComponentTableService.class).getComponentTableList(cv.getId());
                if (CollectionUtils.isNotEmpty(componentTableList)) {
                    for (ComponentTable componentTable : componentTableList) {
                        componentTableMap = new HashMap<String, Object>();
                        componentTableMap.put("baseInfo", componentTable);
                        componentTableMap.put("ComponentColumns",
                                getService(ComponentColumnService.class).getByComponentVersionIdAndTableId(cv.getId(), componentTable.getId()));
                        componentTableMapList.add(componentTableMap);
                    }
                }
                componentVersionMap.put("ComponentTables", componentTableMapList);
                componentVersionMap.put("ComponentTableColumnRelations",
                        getService(ComponentTableColumnRelationService.class).getByComponentVersionId(cv.getId()));
                componentVersionMap.put("CommonComponentRelations", getService(CommonComponentRelationService.class).getByComponentVersionId(cv.getId()));
                componentVersionList.add(componentVersionMap);
            }
        }
        map.put("ComponentVersions", componentVersionList);
        map.put("ComponentAreas", getService(ComponentAreaService.class).findAll());
        map.put("SystemParameterCategorys", getService(SystemParameterCategoryService.class).findAll());
        map.put("SystemParameters", getService(SystemParameterService.class).findAll());
        map.put("CodeTypes", getService(CodeTypeService.class).findAll());
        map.put("Codes", getService(CodeService.class).findAll());
        map.put("BusinessCodes", getService(BusinessCodeService.class).findAll());
        map.put("ColumnLabelCategorys", getService(ColumnLabelCategoryService.class).findAll());
        map.put("ColumnLabels", getService(ColumnLabelService.class).findAll());
        map.put("TypeLabels", getService(TypeLabelService.class).findAll());
        map.put("Timings", getService(TimingService.class).findAll());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(initJsonPath);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(fos, map);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载工程
     * 
     * @return Object
     */
    public Object downloadProject() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String releaseId = getParameter("systemReleaseId");
        Release release = getService().getByID(releaseId);
        HttpServletResponse response = ServletActionContext.getResponse();
        File file = new File(ComponentFileUtil.getReleaseTempPath() + release.getFileName() + ".zip");
        if (!file.exists()) {
            // 兼容老的系统包（老的系统包是rar包，新的是zip包）
            file = new File(ComponentFileUtil.getReleaseTempPath() + release.getFileName() + ".rar");
        }
        String newFileName = release.getSystemName();
        if ("1".equals(release.getType())) {
            newFileName += "_更新包";
        }
        newFileName += "_" + release.getVersion();
        try {
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                newFileName = new String(newFileName.getBytes("UTF-8"), "ISO8859-1");
            } else {
                newFileName = java.net.URLEncoder.encode(newFileName, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.reset();
        response.setContentType("application/x-msdownload");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + newFileName + ".zip");
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            os = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = bis.read(b, 0, 1024)) > 0) {
                os.write(b, 0, len);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            boolean flag = false;
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    flag = true;
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    flag = true;
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    flag = true;
                }
            }
            if (flag) {
                return NONE;
            }
        }
        return NONE;
    }

    /**
     * 复制工程
     * 
     * @param oldPath 原文件路径
     * @param newPath 复制后路径
     * @param unCopyDirSet 不复制的文件夹
     */
    private void copyProject(String oldPath, String newPath, Set<File> unCopyDirSet) {
        try {
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            File oldFile = new File(oldPath);
            String[] file = oldFile.list();
            if (!unCopyDirSet.contains(oldFile)) {
                File temp = null;
                for (int i = 0; i < file.length; i++) {
                    if (oldPath.endsWith(File.separator)) {
                        temp = new File(oldPath + file[i]);
                    } else {
                        temp = new File(oldPath + File.separator + file[i]);
                    }
                    if (temp.isFile()) {
                        FileUtil.nioTransferCopy(temp, new File(newPath + "/" + (temp.getName()).toString()));
                    } else if (!file[i].equals(".svn")) {
                        copyProject(oldPath + "/" + file[i], newPath + "/" + file[i], unCopyDirSet);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将该菜单关联的表创建到数据库脚本中
     * 
     * @param componentVersionSet 构件版本Set
     * @param filePath 脚本文件路径
     * @param databaseType 数据库类型
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private void createComponentTables(Set<ComponentVersion> componentVersionSet, String filePath, String databaseType) throws IOException {
        StringBuffer componentVersionIds = new StringBuffer();
        if (CollectionUtils.isNotEmpty(componentVersionSet)) {
            for (ComponentVersion componentVersion : componentVersionSet) {
                componentVersionIds.append("'");
                componentVersionIds.append(componentVersion.getId());
                componentVersionIds.append("',");
            }
            componentVersionIds.deleteCharAt(componentVersionIds.length() - 1);
        }
        if (componentVersionIds.length() > 0) {
            String sql = "select distinct ct.name as tableName,cc.id,cc.name as columnName,cc.type,cc.length,cc.is_null,cc.default_value from t_xtpz_component_column cc, t_xtpz_component_table ct, t_xtpz_component_table_column ctc"
                    + " where cc.id=ctc.column_id and ct.id=ctc.table_id" + " and ctc.component_version_id in (" + componentVersionIds.toString() + ")";
            List<Object[]> tables = DatabaseHandlerDao.getInstance().queryForList(sql);
            File componentSqlFile = new File(filePath);
            OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(componentSqlFile), "UTF-8");
            if (CollectionUtils.isNotEmpty(tables)) {
                // 整理tables
                Map<String, List<ComponentColumn>> tableMap = new HashMap<String, List<ComponentColumn>>();
                ComponentColumn componentColumn = null;
                List<ComponentColumn> componentColumnList = null;
                for (Object[] objs : tables) {
                    componentColumnList = tableMap.get(objs[0]);
                    if (componentColumnList == null) {
                        componentColumnList = new ArrayList<ComponentColumn>();
                        tableMap.put(String.valueOf(objs[0]), componentColumnList);
                    }
                    componentColumn = new ComponentColumn();
                    componentColumn.setId(String.valueOf(objs[1]));
                    componentColumn.setName(String.valueOf(objs[2]));
                    componentColumn.setType(String.valueOf(objs[3]));
                    componentColumn.setLength(objs[4] == null ? null : Integer.valueOf(String.valueOf(objs[4])));
                    componentColumn.setIsNull(objs[5] == null ? "0" : String.valueOf(objs[5]));
                    componentColumn.setDefaultValue(objs[6] == null ? null : String.valueOf(objs[6]));
                    componentColumnList.add(componentColumn);
                }
                // 创建tables
                String tableName = null;
                StringBuffer createTableSql = null;
                String dataType = null;
                for (Iterator<String> keyIterator = tableMap.keySet().iterator(); keyIterator.hasNext();) {
                    tableName = keyIterator.next();
                    createTableSql = new StringBuffer();
                    createTableSql.append("create table " + tableName + "(");
                    componentColumnList = tableMap.get(tableName);
                    for (ComponentColumn column : componentColumnList) {
                        dataType = PreviewUtil.getDataType(databaseType, column.getType(), column.getLength());
                        if (dataType != null) {
                            createTableSql.append("\n\t" + column.getName());
                            createTableSql.append(" " + dataType);
                            if (StringUtil.isNotEmpty(column.getDefaultValue())) {
                                if ("数字型".equals(column.getType())) {
                                    createTableSql.append(" default " + Integer.valueOf(column.getDefaultValue()));
                                } else {
                                    createTableSql.append(" default '" + column.getDefaultValue() + "'");
                                }
                            }
                            if ("1".equals(column.getIsNull())) {
                                createTableSql.append(" not null");
                            }
                            createTableSql.append(",");
                        }
                    }
                    createTableSql.deleteCharAt(createTableSql.length() - 1);
                    createTableSql.append("\n);");
                    ow.write(createTableSql.toString() + "\n");
                }
            }
            ow.close();
        }
    }

    /**
     * 将该菜单关联的数据创建到数据库脚本中
     * 
     * @param rootMenuId 根菜单ID
     * @param version 系统版本
     * @param map 导出的数据Map
     * @param path 文件路径
     * @param databaseType 数据库类型
     * @throws IOException
     */
    private void createDataSqls(String rootMenuId, String version, Map<String, Object> map, String path, String databaseType) throws Exception {
        File initDataSqlFile = new File(path + "8_init_data.sql");
        OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(initDataSqlFile), "UTF-8");
        // 导出菜单相关数据
        createMenuRelateSql(map, databaseType, ow);
        // 导出组合构件相关数据
        createAssembleCompRelateSql(map, databaseType, ow);
        // 导出基本构件相关数据
        List<ComponentTable> componentTableList = createBaseCompRelateSql(map, databaseType, ow);
        // 导出数据源模块数据（该模块废弃）
        createDbSql(map, ow);
        // 导出编码相关数据
        createCodeRelateSql(rootMenuId, map, ow);
        // 导出系统参数相关数据
        createSystemParamRelateSql(rootMenuId, map, ow);
        // 导出标签定义相关数据
        createLabelRelateSql(rootMenuId, map, ow);
        // 导出构件定义相关数据
        createModuleSql(map, databaseType, ow);
        // 导出树定义相关数据
        createTreeDefineSql(map, ow);
        // 导出表定义相关数据
        Set<PhysicalTableDefine> physicalTableDefineSet = createTableDefineRelateSql(map, databaseType, ow);
        // 导出字段定义相关数据
        createColumnDefineRelateSql(map, ow);
        // 导出应用定义相关数据
        createAppDefineRelateSql(map, databaseType, ow);
        // 导出工作流相关数据
        Set<String> wfRelevantdataTableNameSet = createWfRelateSql(map, ow);
        // 导出报表相关数据
        createReportRelateSql(map, ow);

        Set<String> canUseResourceIdSet = new HashSet<String>();
        // 导出系统版本、资源相关数据
        SystemVersion systemVersion = createVersionResourceRelateSql(rootMenuId, version, map, ow, canUseResourceIdSet);

        // 导出构件中表的数据
        Set<String> physicalTableNameSet = createCompTableDataSql(databaseType, ow, componentTableList, physicalTableDefineSet);
        // T_FS_INDEX_CONFIG和T_FS_INDEX_SCHEMA相关数据导出
        createIndexSql(ow, physicalTableNameSet);
        // 创建数据权限脚本
        createAuthorityDataSql(ow, map);
        // 创建额外的数据脚本
        ReleaseUtil.getInstance().createOtherInitDataSql(ow, map);
        if (DatabaseHandlerDao.DB_ORACLE.equals(databaseType)) {
            ow.write("commit;\n");
        }
        ow.close();
        // 创建自定义构件的相关表
        createSelfDefineTableSql(map, path, databaseType, wfRelevantdataTableNameSet);
        // 创建系统管理平台的相关数据脚本
        createAuthDataSql(rootMenuId, map, path, systemVersion, canUseResourceIdSet);
    }

    /**
     * 导出菜单相关数据
     */
    @SuppressWarnings("unchecked")
    private void createMenuRelateSql(Map<String, Object> map, String databaseType, OutputStreamWriter ow) throws IOException {
        List<Menu> menuList = (List<Menu>) map.get("Menu");
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                ow.write("insert into t_xtpz_menu(id,parent_id,name,code,show_order,has_child,url,binding_type,component_version_id,root_menu_id,use_navigation,icon1,icon2,quick_icon,is_quick_menu)"
                        + " values('"
                        + menu.getId()
                        + "','"
                        + menu.getParentId()
                        + "','"
                        + menu.getName()
                        + "','"
                        + StringUtil.null2empty(menu.getCode())
                        + "',"
                        + menu.getShowOrder()
                        + ",'"
                        + (menu.getHasChild() ? "1" : "0")
                        + "','"
                        + parseStrByDatabaseType(menu.getUrl(), databaseType)
                        + "','"
                        + StringUtil.null2empty(menu.getBindingType())
                        + "','"
                        + StringUtil.null2empty(menu.getComponentVersionId())
                        + "','"
                        + StringUtil.null2empty(menu.getRootMenuId())
                        + "','"
                        + (menu.getUseNavigation() == null ? "0" : menu.getUseNavigation())
                        + "','"
                        + StringUtil.null2empty(menu.getIcon1())
                        + "','"
                        + StringUtil.null2empty(menu.getIcon1())
                        + "','"
                        + StringUtil.null2empty(menu.getQuickIcon()) + "','" + StringUtil.null2zero(menu.getIsQuickMenu()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<MenuSelfParam> menuSelfParamList = (List<MenuSelfParam>) map.get("MenuSelfParam");
        if (CollectionUtils.isNotEmpty(menuSelfParamList)) {
            for (MenuSelfParam menuSelfParam : menuSelfParamList) {
                ow.write("insert into t_xtpz_menu_self_param" + "(id,component_version_id,menu_id,self_param_id,name,type,value,remark,options,text) values('"
                        + menuSelfParam.getId() + "','" + menuSelfParam.getComponentVersionId() + "','" + menuSelfParam.getMenuId() + "','"
                        + menuSelfParam.getSelfParamId() + "','" + menuSelfParam.getName() + "','" + menuSelfParam.getType() + "','"
                        + StringUtil.null2empty(menuSelfParam.getValue()) + "','" + StringUtil.null2empty(menuSelfParam.getRemark()) + "','"
                        + parseStrByDatabaseType(menuSelfParam.getOptions(), databaseType) + "','" + StringUtil.null2empty(menuSelfParam.getText()) + "');"
                        + "\n");
            }
        }
        List<MenuInputParam> menuInputParamList = (List<MenuInputParam>) map.get("MenuInputParam");
        if (CollectionUtils.isNotEmpty(menuInputParamList)) {
            for (MenuInputParam menuInputParam : menuInputParamList) {
                ow.write("insert into t_xtpz_menu_input_param(id,menu_id,input_param_id,value,name) values('" + menuInputParam.getId() + "','"
                        + menuInputParam.getMenuId() + "','" + menuInputParam.getInputParamId() + "','" + menuInputParam.getValue() + "','"
                        + menuInputParam.getName() + "');" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出组合构件相关数据
     */
    @SuppressWarnings("unchecked")
    private void createAssembleCompRelateSql(Map<String, Object> map, String databaseType, OutputStreamWriter ow) throws IOException {
        List<ComponentAssembleArea> componentAssembleAreaList = (List<ComponentAssembleArea>) map.get("ComponentAssembleArea");
        if (CollectionUtils.isNotEmpty(componentAssembleAreaList)) {
            for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                ow.write("insert into t_xtpz_component_assemble_area(id,name,show_order,parent_id,has_child) values('" + componentAssembleArea.getId() + "','"
                        + componentAssembleArea.getName() + "'," + componentAssembleArea.getShowOrder() + ",'" + componentAssembleArea.getParentId() + "','"
                        + (componentAssembleArea.getHasChild() ? "1" : "0") + "');" + "\n");
            }
            ow.write("\n");
        }
        List<Construct> constructList = (List<Construct>) map.get("Construct");
        if (CollectionUtils.isNotEmpty(constructList)) {
            for (Construct construct : constructList) {
                ow.write("insert into t_xtpz_construct(id,component_version_id,base_component_version_id) values ('" + construct.getId() + "','"
                        + construct.getAssembleComponentVersion().getId() + "','" + construct.getBaseComponentVersionId() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ConstructFilter> constructFilterList = (List<ConstructFilter>) map.get("ConstructFilter");
        if (CollectionUtils.isNotEmpty(constructFilterList)) {
            for (ConstructFilter constructFilter : constructFilterList) {
                ow.write("insert into t_xtpz_construct_filter(id,top_com_version_id,component_version_id,table_id) values('" + constructFilter.getId() + "','"
                        + constructFilter.getTopComVersionId() + "','" + constructFilter.getComponentVersionId() + "','" + constructFilter.getTableId() + "');"
                        + "\n");
            }
            ow.write("\n");
        }
        List<ConstructFilterDetail> constructFilterDetailList = (List<ConstructFilterDetail>) map.get("ConstructFilterDetail");
        if (CollectionUtils.isNotEmpty(constructFilterDetailList)) {
            for (ConstructFilterDetail constructFilterDetail : constructFilterDetailList) {
                ow.write("insert into t_xtpz_construct_filter_detail(id,construct_filter_id,column_id,operator,value,relation,show_order,left_parenthesis,right_parenthesis) values('"
                        + constructFilterDetail.getId()
                        + "','"
                        + constructFilterDetail.getConstructFilterId()
                        + "','"
                        + constructFilterDetail.getColumnId()
                        + "','"
                        + constructFilterDetail.getOperator()
                        + "','"
                        + constructFilterDetail.getValue()
                        + "','"
                        + StringUtil.null2empty(constructFilterDetail.getRelation())
                        + "',"
                        + StringUtil.null2zero(constructFilterDetail.getShowOrder())
                        + ",'"
                        + StringUtil.null2empty(constructFilterDetail.getLeftParenthesis())
                        + "','"
                        + StringUtil.null2empty(constructFilterDetail.getRightParenthesis()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ConstructSelfParam> constructSelfParamList = (List<ConstructSelfParam>) map.get("ConstructSelfParam");
        if (CollectionUtils.isNotEmpty(constructSelfParamList)) {
            for (ConstructSelfParam constructSelfParam : constructSelfParamList) {
                ow.write("insert into t_xtpz_construct_self_param"
                        + "(id,component_version_id,construct_id,self_param_id,name,type,value,remark,options,text) values('" + constructSelfParam.getId()
                        + "','" + constructSelfParam.getComponentVersionId() + "','" + constructSelfParam.getConstructId() + "','"
                        + constructSelfParam.getSelfParamId() + "','" + constructSelfParam.getName() + "','" + constructSelfParam.getType() + "','"
                        + StringUtil.null2empty(constructSelfParam.getValue()) + "','" + StringUtil.null2empty(constructSelfParam.getRemark()) + "','"
                        + parseStrByDatabaseType(constructSelfParam.getOptions(), databaseType) + "','" + StringUtil.null2empty(constructSelfParam.getText())
                        + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ConstructInputParam> constructInputParamList = (List<ConstructInputParam>) map.get("ConstructInputParam");
        if (CollectionUtils.isNotEmpty(constructInputParamList)) {
            for (ConstructInputParam constructInputParam : constructInputParamList) {
                ow.write("insert into t_xtpz_construct_input_param(id,construct_id,input_param_id,value,name) values('" + constructInputParam.getId() + "','"
                        + constructInputParam.getConstructId() + "','" + constructInputParam.getInputParamId() + "','"
                        + StringUtil.null2empty(constructInputParam.getValue()) + "','" + constructInputParam.getName() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ConstructDetail> constructDetailList = (List<ConstructDetail>) map.get("ConstructDetail");
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail constructDetail : constructDetailList) {
                ow.write("insert into t_xtpz_construct_detail(id,construct_id,component_version_id,"
                        + "reserve_zone_id,is_common_reserve_zone,button_code,button_name,button_display_name,"
                        + "button_type,parent_button_code,button_cls,button_icon,button_source,position,show_order,width,height,"
                        + "assemble_type,tree_node_type,tree_node_property,before_click_js,search_combo_options) values('"
                        + constructDetail.getId()
                        + "',"
                        + (constructDetail.getConstructId() == null ? "null" : ("'" + constructDetail.getConstructId() + "'"))
                        + ",'"
                        + StringUtil.null2empty(constructDetail.getComponentVersionId())
                        + "','"
                        + constructDetail.getReserveZoneId()
                        + "','"
                        + StringUtil.null2empty(constructDetail.getIsCommonReserveZone())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getButtonCode())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getButtonName())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getButtonDisplayName())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getButtonType())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getParentButtonCode())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getButtonCls())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getButtonIcon())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getButtonSource())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getPosition())
                        + "',"
                        + constructDetail.getShowOrder()
                        + ",'"
                        + StringUtil.null2empty(constructDetail.getWidth())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getHeight())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getAssembleType())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getTreeNodeType())
                        + "','"
                        + StringUtil.null2empty(constructDetail.getTreeNodeProperty())
                        + "','"
                        + parseStrByDatabaseType(constructDetail.getBeforeClickJs(), databaseType)
                        + "','"
                        + StringUtil.null2empty(constructDetail.getSearchComboOptions()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ConstructDetailSelfParam> constructDetailSelfParamList = (List<ConstructDetailSelfParam>) map.get("ConstructDetailSelfParam");
        if (CollectionUtils.isNotEmpty(constructDetailSelfParamList)) {
            for (ConstructDetailSelfParam constructDetailSelfParam : constructDetailSelfParamList) {
                ow.write("insert into t_xtpz_cons_detail_self_param(id,component_version_id,construct_detail_id,self_param_id,"
                        + "name,type,value,remark,options,text) values('" + constructDetailSelfParam.getId() + "','"
                        + constructDetailSelfParam.getComponentVersionId() + "','" + constructDetailSelfParam.getConstructDetailId() + "','"
                        + constructDetailSelfParam.getSelfParamId() + "','" + constructDetailSelfParam.getName() + "','" + constructDetailSelfParam.getType()
                        + "','" + StringUtil.null2empty(constructDetailSelfParam.getValue()) + "','"
                        + StringUtil.null2empty(constructDetailSelfParam.getRemark()) + "','"
                        + parseStrByDatabaseType(constructDetailSelfParam.getOptions(), databaseType) + "','"
                        + StringUtil.null2empty(constructDetailSelfParam.getText()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ConstructFunction> constructFunctionList = (List<ConstructFunction>) map.get("ConstructFunction");
        if (CollectionUtils.isNotEmpty(constructFunctionList)) {
            for (ConstructFunction constructFunction : constructFunctionList) {
                ow.write("insert into t_xtpz_construct_function(id,construct_detail_id,function_id,output_param_id" + ",input_param_id) values('"
                        + constructFunction.getId() + "','" + constructFunction.getConstructDetailId() + "','" + constructFunction.getFunctionId() + "','"
                        + constructFunction.getOutputParamId() + "','" + constructFunction.getInputParamId() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ConstructCallback> constructCallbackList = (List<ConstructCallback>) map.get("ConstructCallback");
        if (CollectionUtils.isNotEmpty(constructCallbackList)) {
            for (ConstructCallback constructCallback : constructCallbackList) {
                ow.write("insert into t_xtpz_construct_callback(id,construct_detail_id,callback_id,output_param_id" + ",input_param_id) values('"
                        + constructCallback.getId() + "','" + constructCallback.getConstructDetailId() + "','" + constructCallback.getCallbackId() + "','"
                        + StringUtil.null2empty(constructCallback.getOutputParamId()) + "','" + StringUtil.null2empty(constructCallback.getInputParamId())
                        + "');" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出基本构件相关数据
     */
    @SuppressWarnings("unchecked")
    private List<ComponentTable> createBaseCompRelateSql(Map<String, Object> map, String databaseType, OutputStreamWriter ow) throws IOException {
        List<ComponentArea> componentAreaList = (List<ComponentArea>) map.get("ComponentArea");
        if (CollectionUtils.isNotEmpty(componentAreaList)) {
            for (ComponentArea componentArea : componentAreaList) {
                ow.write("insert into t_xtpz_component_area(id,name,show_order,parent_id,has_child) values('" + componentArea.getId() + "','"
                        + componentArea.getName() + "'," + componentArea.getShowOrder() + ",'" + componentArea.getParentId() + "','"
                        + (componentArea.getHasChild() ? "1" : "0") + "');" + "\n");
            }
            ow.write("\n");
        }
        List<Component> componentList = (List<Component>) map.get("Component");
        if (CollectionUtils.isNotEmpty(componentList)) {
            for (Component component : componentList) {
                ow.write("insert into t_xtpz_component(id,code,name,alias,type) values('" + component.getId() + "','" + component.getCode() + "','"
                        + component.getName() + "','" + component.getAlias() + "','" + component.getType() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentVersion> componentVersionList = (List<ComponentVersion>) map.get("ComponentVersion");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (CollectionUtils.isNotEmpty(componentVersionList)) {
            for (ComponentVersion componentVersion : componentVersionList) {
                String importDate = null;
                if (DatabaseHandlerDao.DB_ORACLE.equals(databaseType)) {
                    importDate = "to_date('" + dateFormat.format(componentVersion.getImportDate()) + "','yyyy-mm-dd hh24:mi:ss')";
                } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(databaseType)) {
                    importDate = "'" + dateFormat.format(componentVersion.getImportDate()) + "'";
                } else if (DatabaseHandlerDao.DB_MYSQL.equals(databaseType)) {

                } else if (DatabaseHandlerDao.DB_DAMING.equals(databaseType)) {

                }
                ow.write("insert into t_xtpz_component_version(id,component_id,version,url,remark,area_id,path"
                        + ",import_date,views,system_param_config,is_package,is_system_used,package_time,before_click_js,assemble_area_id,button_use,menu_use,area_path,assemble_area_path) values('"
                        + componentVersion.getId()
                        + "','"
                        + componentVersion.getComponent().getId()
                        + "','"
                        + componentVersion.getVersion()
                        + "','"
                        + parseStrByDatabaseType(componentVersion.getUrl(), databaseType)
                        + "','"
                        + StringUtil.null2empty(componentVersion.getRemark())
                        + "','"
                        + componentVersion.getAreaId()
                        + "','"
                        + StringUtil.null2empty(componentVersion.getPath())
                        + "',"
                        + importDate
                        + ",'"
                        + StringUtil.null2empty(componentVersion.getViews())
                        + "','"
                        + componentVersion.getSystemParamConfig()
                        + "','0','1','"
                        + StringUtil.null2empty(componentVersion.getPackageTime())
                        + "','"
                        + parseStrByDatabaseType(componentVersion.getBeforeClickJs(), databaseType)
                        + "','"
                        + StringUtil.null2empty(componentVersion.getAssembleAreaId())
                        + "','"
                        + (StringUtil.isEmpty(componentVersion.getButtonUse()) ? "1" : componentVersion.getButtonUse())
                        + "','"
                        + (StringUtil.isEmpty(componentVersion.getMenuUse()) ? "1" : componentVersion.getMenuUse())
                        + "','"
                        + StringUtil.null2empty(componentVersion.getAreaPath())
                        + "','"
                        + StringUtil.null2empty(componentVersion.getAssembleAreaPath())
                        + "');"
                        + "\n");
            }
            ow.write("\n");
        }
        List<ComponentClass> componentClassList = (List<ComponentClass>) map.get("ComponentClass");
        if (CollectionUtils.isNotEmpty(componentClassList)) {
            for (ComponentClass componentClass : componentClassList) {
                ow.write("insert into t_xtpz_component_class(id,name,component_version_id) values('" + componentClass.getId() + "','"
                        + componentClass.getName() + "','" + componentClass.getComponentVersionId() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentJar> componentJarList = (List<ComponentJar>) map.get("ComponentJar");
        if (CollectionUtils.isNotEmpty(componentJarList)) {
            for (ComponentJar componentJar : componentJarList) {
                ow.write("insert into t_xtpz_component_jar(id,name,component_version_id) values('" + componentJar.getId() + "','" + componentJar.getName()
                        + "','" + componentJar.getComponentVersionId() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentSystemParameter> componentSystemParameterList = (List<ComponentSystemParameter>) map.get("ComponentSystemParameter");
        if (CollectionUtils.isNotEmpty(componentSystemParameterList)) {
            for (ComponentSystemParameter componentSystemParameter : componentSystemParameterList) {
                ow.write("insert into t_xtpz_component_system_param(id,component_version_id,name,remark) values('" + componentSystemParameter.getId() + "','"
                        + componentSystemParameter.getComponentVersionId() + "','" + componentSystemParameter.getName() + "','"
                        + StringUtil.null2empty(componentSystemParameter.getRemark()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentSystemParameterRelation> componentSystemParameterRelationList = (List<ComponentSystemParameterRelation>) map
                .get("ComponentSystemParameterRelation");
        if (CollectionUtils.isNotEmpty(componentSystemParameterRelationList)) {
            for (ComponentSystemParameterRelation componentSystemParameterRelation : componentSystemParameterRelationList) {
                ow.write("insert into t_xtpz_comp_sys_param_relation" + "(id,component_system_param_id,system_param_id,component_version_id) values('"
                        + componentSystemParameterRelation.getId() + "','" + componentSystemParameterRelation.getComponentSystemParamId() + "','"
                        + componentSystemParameterRelation.getSystemParamId() + "','" + componentSystemParameterRelation.getComponentVersionId() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentSelfParam> componentSelfParamList = (List<ComponentSelfParam>) map.get("ComponentSelfParam");
        if (CollectionUtils.isNotEmpty(componentSelfParamList)) {
            for (ComponentSelfParam componentSelfParam : componentSelfParamList) {
                ow.write("insert into t_xtpz_component_self_param" + "(id,component_version_id,name,type,value,remark,options,text) values('"
                        + componentSelfParam.getId() + "','" + componentSelfParam.getComponentVersionId() + "','" + componentSelfParam.getName() + "','"
                        + componentSelfParam.getType() + "','" + StringUtil.null2empty(componentSelfParam.getValue()) + "','"
                        + StringUtil.null2empty(componentSelfParam.getRemark()) + "','" + parseStrByDatabaseType(componentSelfParam.getOptions(), databaseType)
                        + "','" + StringUtil.null2empty(componentSelfParam.getText()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentInputParam> componentInputParamList = (List<ComponentInputParam>) map.get("ComponentInputParam");
        if (CollectionUtils.isNotEmpty(componentInputParamList)) {
            for (ComponentInputParam componentInputParam : componentInputParamList) {
                ow.write("insert into t_xtpz_component_input_param(id,component_version_id,name,value,remark) values('" + componentInputParam.getId() + "',"
                        + (componentInputParam.getComponentVersionId() == null ? null : ("'" + componentInputParam.getComponentVersionId() + "'")) + ",'"
                        + componentInputParam.getName() + "','" + StringUtil.null2empty(componentInputParam.getValue()) + "','"
                        + StringUtil.null2empty(componentInputParam.getRemark()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentOutputParam> componentOutputParamList = (List<ComponentOutputParam>) map.get("ComponentOutputParam");
        if (CollectionUtils.isNotEmpty(componentOutputParamList)) {
            for (ComponentOutputParam componentOutputParam : componentOutputParamList) {
                ow.write("insert into t_xtpz_component_output_param(id,component_version_id,name,remark) values('" + componentOutputParam.getId() + "','"
                        + componentOutputParam.getComponentVersionId() + "','" + componentOutputParam.getName() + "','"
                        + StringUtil.null2empty(componentOutputParam.getRemark()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentReserveZone> componentReserveZoneList = (List<ComponentReserveZone>) map.get("ComponentReserveZone");
        if (CollectionUtils.isNotEmpty(componentReserveZoneList)) {
            for (ComponentReserveZone componentReserveZone : componentReserveZoneList) {
                ow.write("insert into t_xtpz_component_reserve_zone(id,component_version_id,name,alias,type,page,is_common,show_order) values('"
                        + componentReserveZone.getId() + "','" + StringUtil.null2empty(componentReserveZone.getComponentVersionId()) + "','"
                        + componentReserveZone.getName() + "','" + componentReserveZone.getAlias() + "','" + componentReserveZone.getType() + "','"
                        + componentReserveZone.getPage() + "','" + (componentReserveZone.getIsCommon().booleanValue() ? "1" : "0") + "',"
                        + componentReserveZone.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentFunction> componentFunctionList = (List<ComponentFunction>) map.get("ComponentFunction");
        if (CollectionUtils.isNotEmpty(componentFunctionList)) {
            for (ComponentFunction componentFunction : componentFunctionList) {
                ow.write("insert into t_xtpz_component_function(id,component_version_id,name,page,remark) values('" + componentFunction.getId() + "',"
                        + (componentFunction.getComponentVersionId() == null ? null : ("'" + componentFunction.getComponentVersionId() + "'")) + ",'"
                        + componentFunction.getName() + "','" + componentFunction.getPage() + "','" + StringUtil.null2empty(componentFunction.getRemark())
                        + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentFunctionData> componentFunctionDataList = (List<ComponentFunctionData>) map.get("ComponentFunctionData");
        if (CollectionUtils.isNotEmpty(componentFunctionDataList)) {
            for (ComponentFunctionData componentFunctionData : componentFunctionDataList) {
                ow.write("insert into t_xtpz_component_function_data(id,function_id,name,remark) values('" + componentFunctionData.getId() + "','"
                        + componentFunctionData.getFunctionId() + "','" + componentFunctionData.getName() + "','"
                        + StringUtil.null2empty(componentFunctionData.getRemark()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentCallback> componentCallbackList = (List<ComponentCallback>) map.get("ComponentCallback");
        if (CollectionUtils.isNotEmpty(componentCallbackList)) {
            for (ComponentCallback componentCallback : componentCallbackList) {
                ow.write("insert into t_xtpz_component_callback(id,component_version_id,name,page,remark) values('" + componentCallback.getId() + "',"
                        + (componentCallback.getComponentVersionId() == null ? null : ("'" + componentCallback.getComponentVersionId() + "'")) + ",'"
                        + componentCallback.getName() + "','" + componentCallback.getPage() + "','" + StringUtil.null2empty(componentCallback.getRemark())
                        + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentCallbackParam> componentCallbackParamList = (List<ComponentCallbackParam>) map.get("ComponentCallbackParam");
        if (CollectionUtils.isNotEmpty(componentCallbackParamList)) {
            for (ComponentCallbackParam componentCallbackParam : componentCallbackParamList) {
                ow.write("insert into t_xtpz_comp_callback_param(id,callback_id,name,remark) values('" + componentCallbackParam.getId() + "','"
                        + componentCallbackParam.getCallbackId() + "','" + componentCallbackParam.getName() + "','"
                        + StringUtil.null2empty(componentCallbackParam.getRemark()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentButton> componentButtonList = (List<ComponentButton>) map.get("ComponentButton");
        if (CollectionUtils.isNotEmpty(componentButtonList)) {
            for (ComponentButton componentButton : componentButtonList) {
                ow.write("insert into t_xtpz_component_button(id,component_version_id,name,display_name) values('" + componentButton.getId() + "','"
                        + componentButton.getComponentVersionId() + "','" + componentButton.getName() + "','" + componentButton.getDisplayName() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentTableColumnRelation> componentTableColumnRelationList = (List<ComponentTableColumnRelation>) map.get("ComponentTableColumnRelation");
        if (CollectionUtils.isNotEmpty(componentTableColumnRelationList)) {
            for (ComponentTableColumnRelation componentTableColumnRelation : componentTableColumnRelationList) {
                ow.write("insert into t_xtpz_component_table_column" + "(component_version_id,table_id,column_id,id) values('"
                        + componentTableColumnRelation.getComponentVersionId() + "','" + componentTableColumnRelation.getTableId() + "','"
                        + componentTableColumnRelation.getColumnId() + "','" + componentTableColumnRelation.getId() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentTable> componentTableList = (List<ComponentTable>) map.get("ComponentTable");
        if (CollectionUtils.isNotEmpty(componentTableList)) {
            for (ComponentTable componentTable : componentTableList) {
                ow.write("insert into t_xtpz_component_table(id,name,release_with_data,is_selfdefine) values('" + componentTable.getId() + "','"
                        + componentTable.getName() + "','" + componentTable.getReleaseWithData() + "','" + componentTable.getIsSelfdefine() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ComponentColumn> componentColumnList = (List<ComponentColumn>) map.get("ComponentColumn");
        if (CollectionUtils.isNotEmpty(componentColumnList)) {
            for (ComponentColumn componentColumn : componentColumnList) {
                ow.write("insert into t_xtpz_component_column" + "(id,name,type,is_null,default_value,remark,length) values('" + componentColumn.getId()
                        + "','" + componentColumn.getName() + "','" + componentColumn.getType() + "','" + componentColumn.getIsNull() + "','"
                        + StringUtil.null2empty(componentColumn.getDefaultValue()) + "','" + StringUtil.null2empty(componentColumn.getRemark()) + "',"
                        + componentColumn.getLength() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<CommonComponentRelation> commonComponentRelationList = (List<CommonComponentRelation>) map.get("CommonComponentRelation");
        if (CollectionUtils.isNotEmpty(commonComponentRelationList)) {
            for (CommonComponentRelation commonComponentRelation : commonComponentRelationList) {
                ow.write("insert into t_xtpz_commom_comp_relation" + "(id,component_version_id,common_comp_version_id) values('"
                        + commonComponentRelation.getId() + "','" + commonComponentRelation.getComponentVersionId() + "','"
                        + commonComponentRelation.getCommonComponentVersionId() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<SystemComponentVersion> systemComponentVersionList = (List<SystemComponentVersion>) map.get("SystemComponentVersion");
        if (CollectionUtils.isNotEmpty(systemComponentVersionList)) {
            for (SystemComponentVersion systemComponentVersion : systemComponentVersionList) {
                ow.write("insert into t_xtpz_system_component(id,root_menu_id,component_version_id) values('" + systemComponentVersion.getId() + "','"
                        + systemComponentVersion.getRootMenuId() + "','" + systemComponentVersion.getComponentVersion().getId() + "');" + "\n");
            }
            ow.write("\n");
        }
        return componentTableList;
    }

    /**
     * 导出数据源模块数据
     */
    @SuppressWarnings("unchecked")
    private void createDbSql(Map<String, Object> map, OutputStreamWriter ow) throws IOException {
        List<Database> databaseList = (List<Database>) map.get("Database");
        if (CollectionUtils.isNotEmpty(databaseList)) {
            for (Database database : databaseList) {
                ow.write("insert into t_xtpz_database" + "(id,name,instance_name,ip,port,type,user_name,password) values('" + database.getId() + "','"
                        + database.getName() + "','" + database.getInstanceName() + "','" + database.getIp() + "'," + database.getPort() + ",'"
                        + database.getType() + "','" + database.getUserName() + "','" + database.getPassword() + "');" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出编码相关数据
     */
    @SuppressWarnings({"unchecked"})
    private void createCodeRelateSql(String rootMenuId, Map<String, Object> map, OutputStreamWriter ow) throws IOException {
        List<CodeType> codeTypeList = (List<CodeType>) map.get("CodeType");
        if (CollectionUtils.isNotEmpty(codeTypeList)) {
            for (CodeType codeType : codeTypeList) {
                ow.write("insert into t_xtpz_code_type(id,name,code,show_order,is_system,is_business,is_cache,system_id) values('" + codeType.getId() + "','"
                        + codeType.getName() + "','" + codeType.getCode() + "'," + (codeType.getShowOrder() == null ? 0 : codeType.getShowOrder()) + ",'"
                        + StringUtil.null2zero(codeType.getIsSystem()) + "','" + StringUtil.null2empty(codeType.getIsBusiness()) + "','"
                        + StringUtil.null2zero(codeType.getIsCache()) + "','" + StringUtil.null2empty(codeType.getSystemId()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<Code> codeList = (List<Code>) map.get("Code");
        if (CollectionUtils.isNotEmpty(codeList)) {
            for (Code code : codeList) {
                ow.write("insert into t_xtpz_code(id,name,value,show_order,most_value_show_order,code_type_code,parent_id,remark,is_system) values('"
                        + code.getId() + "','" + code.getName() + "','" + code.getValue() + "'," + code.getShowOrder() + ","
                        + (code.getMostValueShowOrder() == null ? "0" : code.getMostValueShowOrder()) + ",'" + code.getCodeTypeCode() + "','"
                        + StringUtil.null2empty(code.getParentId()) + "','" + StringUtil.null2empty(code.getRemark()) + "','"
                        + StringUtil.null2empty(code.getIsSystem()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<BusinessCode> businessCodeList = (List<BusinessCode>) map.get("BusinessCode");
        if (CollectionUtils.isNotEmpty(businessCodeList)) {
            for (BusinessCode businessCode : businessCodeList) {
                ow.write("insert into t_xtpz_business_code(id,table_name,code_name_field,code_value_field,show_order_field,id_field,parent_id_field,code_type_code,is_auth,is_timing_update,period,business_code_type,class_name) values('"
                        + businessCode.getId()
                        + "','"
                        + StringUtil.null2empty(businessCode.getTableName())
                        + "','"
                        + StringUtil.null2empty(businessCode.getCodeNameField())
                        + "','"
                        + StringUtil.null2empty(businessCode.getCodeValueField())
                        + "','"
                        + StringUtil.null2empty(businessCode.getShowOrderField())
                        + "','"
                        + StringUtil.null2empty(businessCode.getIdField())
                        + "','"
                        + StringUtil.null2empty(businessCode.getParentIdField())
                        + "','"
                        + StringUtil.null2empty(businessCode.getCodeTypeCode())
                        + "','"
                        + (businessCode.getIsAuth() == null ? "0" : businessCode.getIsAuth())
                        + "','"
                        + (businessCode.getIsTimingUpdate() == null ? "0" : businessCode.getIsTimingUpdate())
                        + "','"
                        + StringUtil.null2empty(businessCode.getPeriod())
                        + "','"
                        + StringUtil.null2empty(businessCode.getBusinessCodeType())
                        + "','"
                        + StringUtil.null2empty(businessCode.getClassName()) + "');" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出系统参数相关数据
     */
    @SuppressWarnings("unchecked")
    private void createSystemParamRelateSql(String rootMenuId, Map<String, Object> map, OutputStreamWriter ow) throws IOException {
        List<SystemParameterCategory> systemParameterCategoryList = (List<SystemParameterCategory>) map.get("SystemParameterCategory");
        List<SystemParameterCategory> resultList = new ArrayList<SystemParameterCategory>();
        // 公共系统参数分类
        List<SystemParameterCategory> rootCategoryList = getSystemParameterCategoryByParentId(systemParameterCategoryList, resultList, "-1");
        // 根据系统过滤系统参数分类
        systemParameterCategoryList = getSystemParameterCategoryByParentId(systemParameterCategoryList, rootCategoryList, rootMenuId);
        if (CollectionUtils.isNotEmpty(systemParameterCategoryList)) {
            for (SystemParameterCategory systemParameterCategory : systemParameterCategoryList) {
                ow.write("insert into t_xtpz_system_param_category(id,name,parent_id,has_child) values('" + systemParameterCategory.getId() + "','"
                        + systemParameterCategory.getName() + "','" + systemParameterCategory.getParentId() + "','"
                        + (systemParameterCategory.getHasChild() ? "1" : '0') + "');" + "\n");
            }
            ow.write("\n");
        }
        List<SystemParameter> systemParameterList = (List<SystemParameter>) map.get("SystemParameter");
        // 根据SystemParameterCategory 系统参数分类过滤数据
        for (Iterator<SystemParameter> it = systemParameterList.iterator(); it.hasNext();) {
            boolean move = true;
            SystemParameter systemParameter = (SystemParameter) it.next();
            for (SystemParameterCategory category : systemParameterCategoryList) {
                if (category.getId().equals(systemParameter.getCategoryId())) {
                    move = false;
                    break;
                }
            }
            if (move)
                it.remove();
        }
        if (CollectionUtils.isNotEmpty(systemParameterList)) {
            for (SystemParameter systemParameter : systemParameterList) {
                ow.write("insert into t_xtpz_system_param(id,category_id,name,value,type,remark,show_order) values('" + systemParameter.getId() + "','"
                        + systemParameter.getCategoryId() + "','" + systemParameter.getName() + "','" + StringUtil.null2empty(systemParameter.getValue())
                        + "','" + StringUtil.null2empty(systemParameter.getType()) + "','" + StringUtil.null2empty(systemParameter.getRemark()) + "',"
                        + systemParameter.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出标签定义相关数据
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void createLabelRelateSql(String rootMenuId, Map<String, Object> map, OutputStreamWriter ow) throws IOException {
        List<ColumnLabelCategory> columnLabelCategoryList = (List<ColumnLabelCategory>) map.get("ColumnLabelCategory");
        // 根据系统过滤字段标签分类
        for (Iterator it = columnLabelCategoryList.iterator(); it.hasNext();) {
            ColumnLabelCategory columnLabelCategory = (ColumnLabelCategory) it.next();
            if (!rootMenuId.equals(columnLabelCategory.getMenuId()) && !"1".equals(columnLabelCategory.getMenuId())) {
                it.remove();
            }
        }
        if (CollectionUtils.isNotEmpty(columnLabelCategoryList)) {
            for (ColumnLabelCategory columnLabelCategory : columnLabelCategoryList) {
                ow.write("insert into t_xtpz_column_label_category(id,name,menu_id,show_order) values('" + columnLabelCategory.getId() + "','"
                        + columnLabelCategory.getName() + "','" + columnLabelCategory.getMenuId() + "'," + columnLabelCategory.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<ColumnLabel> columnLabelList = (List<ColumnLabel>) map.get("ColumnLabel");
        // 根据字段标签分类过滤字段标签
        for (Iterator it = columnLabelList.iterator(); it.hasNext();) {
            ColumnLabel columnLabel = (ColumnLabel) it.next();
            if (!rootMenuId.equals(columnLabel.getCategoryId()) && !"1".equals(columnLabel.getCategoryId()))
                it.remove();
        }
        if (CollectionUtils.isNotEmpty(columnLabelList)) {
            for (ColumnLabel columnLabel : columnLabelList) {
                ow.write("insert into t_xtpz_column_label(id,code,name,show_order,category_id,code_type_code) values('" + columnLabel.getId() + "','"
                        + columnLabel.getCode() + "','" + columnLabel.getName() + "'," + columnLabel.getShowOrder() + ",'" + columnLabel.getCategoryId()
                        + "','" + StringUtil.null2empty(columnLabel.getCodeTypeCode()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<TypeLabel> typeLabelList = (List<TypeLabel>) map.get("TypeLabel");
        // 根据系统过滤分类标签
        for (Iterator it = typeLabelList.iterator(); it.hasNext();) {
            TypeLabel typeLabel = (TypeLabel) it.next();
            if (!rootMenuId.equals(typeLabel.getMenuId()) && !"1".equals(typeLabel.getMenuId()))
                it.remove();
        }
        if (CollectionUtils.isNotEmpty(typeLabelList)) {
            for (TypeLabel typeLabel : typeLabelList) {
                ow.write("insert into t_xtpz_type_label(id,code,name,menu_id,show_order) values('" + typeLabel.getId() + "','" + typeLabel.getCode() + "','"
                        + typeLabel.getName() + "','" + typeLabel.getMenuId() + "'," + typeLabel.getShowOrder().intValue() + ");" + "\n");
            }
            ow.write("\n");
        }
        // 定时任务
        List<TimingEntity> timingList = (List<TimingEntity>) map.get("Timing");
        if (CollectionUtils.isNotEmpty(timingList)) {
            for (TimingEntity timing : timingList) {
                ow.write("insert into t_xtpz_timing(id,name,time,bean_id,method,type,remark,command,is_operates,timing_type) values('" + timing.getId() + "','"
                        + timing.getName() + "','" + timing.getTime() + "','" + StringUtil.null2empty(timing.getBeanId()) + "','"
                        + StringUtil.null2empty(timing.getMethod()) + "','" + timing.getType() + "','" + StringUtil.null2empty(timing.getRemark()) + "','"
                        + StringUtil.null2empty(timing.getCommand()) + "','" + StringUtil.null2zero(timing.getIsOperates()) + "','"
                        + StringUtil.null2zero(timing.getTimingType()) + "');" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出构件定义相关数据
     */
    @SuppressWarnings("unchecked")
    private void createModuleSql(Map<String, Object> map, String databaseType, OutputStreamWriter ow) throws IOException {
        List<Module> moduleList = (List<Module>) map.get("Module");
        if (CollectionUtils.isNotEmpty(moduleList)) {
            for (Module module : moduleList) {
                ow.write("insert into t_xtpz_module(id,type,logic_table_group_code,template_type,"
                        + "name,component_class_name,tree_id,area_layout,ui_type,show_order,remark,component_version_id,component_url,component_area_id) values('"
                        + module.getId()
                        + "','"
                        + module.getType()
                        + "','"
                        + StringUtil.null2empty(module.getLogicTableGroupCode())
                        + "','"
                        + StringUtil.null2empty(module.getTemplateType())
                        + "','"
                        + module.getName()
                        + "','"
                        + StringUtil.null2empty(module.getComponentClassName())
                        + "','"
                        + StringUtil.null2empty(module.getTreeId())
                        + "','"
                        + parseStrByDatabaseType(module.getAreaLayout(), databaseType)
                        + "','"
                        + StringUtil.null2empty(module.getUiType())
                        + "',"
                        + StringUtil.null2zero(module.getShowOrder())
                        + ",'"
                        + StringUtil.null2empty(module.getRemark())
                        + "','"
                        + StringUtil.null2empty(module.getComponentVersionId())
                        + "','"
                        + StringUtil.null2empty(module.getComponentUrl())
                        + "','"
                        + StringUtil.null2empty(module.getComponentAreaId()) + "');" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出树定义相关数据
     */
    @SuppressWarnings("unchecked")
    private void createTreeDefineSql(Map<String, Object> map, OutputStreamWriter ow) throws IOException {
        List<TreeDefine> treeDefineList = (List<TreeDefine>) map.get("TreeDefine");
        if (CollectionUtils.isNotEmpty(treeDefineList)) {
            for (TreeDefine treeDefine : treeDefineList) {
                if (treeDefine == null) {
                    continue;
                }
                ow.write("insert into t_xtpz_tree_define(id,parent_id,name,type,value,db_id,table_id,child,show_order,dynamic,remark,"
                        + "node_rule,dynamic_from_id,column_names,column_values,parent_ids,data_source,show_node_count,sort_type,root_id,show_root,column_filter) values ('"
                        + treeDefine.getId()
                        + "','"
                        + treeDefine.getParentId()
                        + "','"
                        + treeDefine.getName()
                        + "','"
                        + treeDefine.getType()
                        + "','"
                        + StringUtil.null2empty(treeDefine.getValue())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getDbId())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getTableId())
                        + "','"
                        + ((treeDefine.getChild() != null && treeDefine.getChild()) ? "1" : "0")
                        + "',"
                        + treeDefine.getShowOrder()
                        + ",'"
                        + treeDefine.getDynamic()
                        + "','"
                        + StringUtil.null2empty(treeDefine.getRemark())
                        + "','"
                        + treeDefine.getNodeRule()
                        + "','"
                        + StringUtil.null2empty(treeDefine.getDynamicFromId())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getColumnNames())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getColumnValues())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getParentIds())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getDataSource())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getShowNodeCount())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getSortType())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getRootId())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getShowRoot())
                        + "','"
                        + StringUtil.null2empty(treeDefine.getColumnFilter()) + "');" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出表定义相关数据
     */
    @SuppressWarnings("unchecked")
    private Set<PhysicalTableDefine> createTableDefineRelateSql(Map<String, Object> map, String databaseType, OutputStreamWriter ow) throws IOException {
        List<TableClassification> tableClassificationList = (List<TableClassification>) map.get("TableClassification");
        if (CollectionUtils.isNotEmpty(tableClassificationList)) {
            for (TableClassification tableClassification : tableClassificationList) {
                ow.write("insert into t_xtpz_table_classification(id,name,code,prefix,show_order) values('" + tableClassification.getId() + "','"
                        + tableClassification.getName() + "','" + tableClassification.getCode() + "','"
                        + StringUtil.null2empty(tableClassification.getPrefix()) + "'," + tableClassification.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<TableTree> tableTreeList = (List<TableTree>) map.get("TableTree");
        if (CollectionUtils.isNotEmpty(tableTreeList)) {
            for (TableTree tableTree : tableTreeList) {
                ow.write("insert into t_xtpz_table_tree(id,name,node_type,code,parent_id,show_order,classification,type_label,table_prefix) values('"
                        + tableTree.getId() + "','" + tableTree.getName() + "','" + tableTree.getNodeType() + "','"
                        + StringUtil.null2empty(tableTree.getCode()) + "','" + tableTree.getParentId() + "'," + tableTree.getShowOrder() + ",'"
                        + tableTree.getClassification() + "','" + StringUtil.null2empty(tableTree.getTypeLabel()) + "','"
                        + StringUtil.null2empty(tableTree.getTablePrefix()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<LogicClassification> logicClassificationList = (List<LogicClassification>) map.get("LogicClassification");
        if (CollectionUtils.isNotEmpty(logicClassificationList)) {
            for (LogicClassification logicClassification : logicClassificationList) {
                ow.write("insert into t_xtpz_logic_classification(id,name,code,show_order) values('" + logicClassification.getId() + "','"
                        + logicClassification.getName() + "','" + logicClassification.getCode() + "'," + logicClassification.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<LogicTableDefine> logicTableDefineList = (List<LogicTableDefine>) map.get("LogicTableDefine");
        if (CollectionUtils.isNotEmpty(logicTableDefineList)) {
            for (LogicTableDefine logicTableDefine : logicTableDefineList) {
                ow.write("insert into t_xtpz_logic_table_define(id,show_name,code,show_order,remark,status,classification,table_tree_id) values('"
                        + logicTableDefine.getId() + "','" + logicTableDefine.getShowName() + "','" + logicTableDefine.getCode() + "',"
                        + logicTableDefine.getShowOrder() + ",'" + StringUtil.null2empty(logicTableDefine.getRemark()) + "','" + logicTableDefine.getStatus()
                        + "','" + logicTableDefine.getClassification() + "','" + logicTableDefine.getTableTreeId() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<LogicGroupDefine> logicGroupDefineList = (List<LogicGroupDefine>) map.get("LogicGroupDefine");
        if (CollectionUtils.isNotEmpty(logicGroupDefineList)) {
            for (LogicGroupDefine logicGroupDefine : logicGroupDefineList) {
                ow.write("insert into t_xtpz_logic_group_define(id,group_name,code,show_order,remark,status) values('" + logicGroupDefine.getId() + "','"
                        + logicGroupDefine.getGroupName() + "','" + logicGroupDefine.getCode() + "'," + logicGroupDefine.getShowOrder() + ",'"
                        + StringUtil.null2empty(logicGroupDefine.getRemark()) + "','" + logicGroupDefine.getStatus() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<LogicGroupRelation> logicGroupRelationList = (List<LogicGroupRelation>) map.get("LogicGroupRelation");
        if (CollectionUtils.isNotEmpty(logicGroupRelationList)) {
            for (LogicGroupRelation logicGroupRelation : logicGroupRelationList) {
                ow.write("insert into t_xtpz_logic_group_relation(id,group_code,table_code,parent_table_code,show_order) values('" + logicGroupRelation.getId()
                        + "','" + logicGroupRelation.getGroupCode() + "','" + logicGroupRelation.getTableCode() + "','"
                        + StringUtil.null2empty(logicGroupRelation.getParentTableCode()) + "'," + logicGroupRelation.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<LogicTableRelation> logicTableRelationList = (List<LogicTableRelation>) map.get("LogicTableRelation");
        if (CollectionUtils.isNotEmpty(logicTableRelationList)) {
            for (LogicTableRelation logicTableRelation : logicTableRelationList) {
                ow.write("insert into t_xtpz_logic_table_relation(id,group_code,table_code,column_id,parent_table_code,parent_column_id) values('"
                        + logicTableRelation.getId() + "','" + logicTableRelation.getGroupCode() + "','" + logicTableRelation.getTableCode() + "','"
                        + logicTableRelation.getColumnId() + "','" + logicTableRelation.getParentTableCode() + "','" + logicTableRelation.getParentColumnId()
                        + "');" + "\n");
            }
            ow.write("\n");
        }
        Set<PhysicalGroupDefine> physicalGroupDefineSet = (Set<PhysicalGroupDefine>) map.get("PhysicalGroupDefine");
        if (CollectionUtils.isNotEmpty(physicalGroupDefineSet)) {
            for (PhysicalGroupDefine physicalGroupDefine : physicalGroupDefineSet) {
                ow.write("insert into t_xtpz_physical_group_define(id,group_name,logic_group_code,code,show_order,remark) values('"
                        + physicalGroupDefine.getId() + "','" + physicalGroupDefine.getGroupName() + "','"
                        + StringUtil.null2empty(physicalGroupDefine.getLogicGroupCode()) + "','" + StringUtil.null2empty(physicalGroupDefine.getCode()) + "',"
                        + physicalGroupDefine.getShowOrder() + ",'" + StringUtil.null2empty(physicalGroupDefine.getRemark()) + "');" + "\n");
            }
            ow.write("\n");
        }
        Set<PhysicalGroupRelation> physicalGroupRelationList = (Set<PhysicalGroupRelation>) map.get("PhysicalGroupRelation");
        if (CollectionUtils.isNotEmpty(physicalGroupRelationList)) {
            for (PhysicalGroupRelation physicalGroupRelation : physicalGroupRelationList) {
                ow.write("insert into t_xtpz_physical_group_relation(id,group_id,table_id,show_order) values('" + physicalGroupRelation.getId() + "','"
                        + physicalGroupRelation.getGroupId() + "','" + physicalGroupRelation.getTableId() + "'," + physicalGroupRelation.getShowOrder() + ");"
                        + "\n");
            }
            ow.write("\n");
        }
        Set<PhysicalTableDefine> physicalTableDefineSet = (Set<PhysicalTableDefine>) map.get("PhysicalTableDefine");
        if (CollectionUtils.isNotEmpty(physicalTableDefineSet)) {
            for (PhysicalTableDefine physicalTableDefine : physicalTableDefineSet) {
                ow.write("insert into t_xtpz_physical_table_define(id,table_tree_id,table_type,classification,show_name,table_prefix,"
                        + "table_code,table_name,logic_table_code,created,show_order,release_with_data,create_index,remark) values('"
                        + physicalTableDefine.getId()
                        + "','"
                        + physicalTableDefine.getTableTreeId()
                        + "','"
                        + physicalTableDefine.getTableType()
                        + "','"
                        + StringUtil.null2empty(physicalTableDefine.getClassification())
                        + "','"
                        + physicalTableDefine.getShowName()
                        + "','"
                        + physicalTableDefine.getTablePrefix()
                        + "','"
                        + StringUtil.null2empty(physicalTableDefine.getTableCode())
                        + "','"
                        + physicalTableDefine.getTableName()
                        + "','"
                        + parseStrByDatabaseType(physicalTableDefine.getLogicTableCode(), databaseType)
                        + "','"
                        + physicalTableDefine.getCreated()
                        + "',"
                        + physicalTableDefine.getShowOrder()
                        + ",'"
                        + StringUtil.null2empty(physicalTableDefine.getReleaseWithData())
                        + "','"
                        + StringUtil.null2empty(physicalTableDefine.getCreateIndex())
                        + "','" + StringUtil.null2empty(physicalTableDefine.getRemark()) + "');" + "\n");
            }
            ow.write("\n");
        }
        return physicalTableDefineSet;
    }

    /**
     * 导出字段定义相关数据
     */
    @SuppressWarnings("unchecked")
    private void createColumnDefineRelateSql(Map<String, Object> map, OutputStreamWriter ow) throws IOException {
        List<ColumnDefine> columnDefineList = (List<ColumnDefine>) map.get("ColumnDefine");
        if (CollectionUtils.isNotEmpty(columnDefineList)) {
            for (ColumnDefine columnDefine : columnDefineList) {
                ow.write("insert into t_xtpz_column_define(id,table_id,show_name,column_name,data_type,code_type_code,"
                        + "length,column_type,inputable,updateable,searchable,listable,sortable,align,filter_type,"
                        + "width,default_value,remark,created,show_order,column_label,input_type,data_type_extend,phraseable,input_option) values('"
                        + columnDefine.getId()
                        + "','"
                        + columnDefine.getTableId()
                        + "','"
                        + columnDefine.getShowName()
                        + "','"
                        + columnDefine.getColumnName()
                        + "','"
                        + columnDefine.getDataType()
                        + "','"
                        + StringUtil.null2empty(columnDefine.getCodeTypeCode())
                        + "',"
                        + columnDefine.getLength()
                        + ",'"
                        + StringUtil.null2zero(columnDefine.getColumnType())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getInputable())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getUpdateable())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getSearchable())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getListable())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getSortable())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getAlign())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getFilterType())
                        + "',"
                        + columnDefine.getWidth()
                        + ",'"
                        + StringUtil.null2empty(columnDefine.getDefaultValue())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getRemark())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getCreated())
                        + "',"
                        + columnDefine.getShowOrder()
                        + ",'"
                        + StringUtil.null2empty(columnDefine.getColumnLabel())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getInputType())
                        + "','"
                        + StringUtil.null2empty(columnDefine.getDataTypeExtend())
                        + "','"
                        + StringUtil.null2zero(columnDefine.getPhraseable()) + "','" + StringUtil.null2empty(columnDefine.getInputOption()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<TableRelation> tableRelationList = (List<TableRelation>) map.get("TableRelation");
        if (CollectionUtils.isNotEmpty(tableRelationList)) {
            for (TableRelation tableRelation : tableRelationList) {
                ow.write("insert into t_xtpz_table_relation(id,table_id,column_id,relate_table_id," + "relate_column_id) values('" + tableRelation.getId()
                        + "','" + tableRelation.getTableId() + "','" + tableRelation.getColumnId() + "','" + tableRelation.getRelateTableId() + "','"
                        + tableRelation.getRelateColumnId() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ColumnRelation> columnRelationList = (List<ColumnRelation>) map.get("ColumnRelation");
        if (CollectionUtils.isNotEmpty(columnRelationList)) {
            for (ColumnRelation columnRelation : columnRelationList) {
                ow.write("insert into t_xtpz_column_relation(id,table_id,name,type,show_order) values('" + columnRelation.getId() + "','"
                        + columnRelation.getTableId() + "','" + columnRelation.getName() + "','" + columnRelation.getType() + "',"
                        + (columnRelation.getShowOrder() == null ? 0 : columnRelation.getShowOrder()) + ");" + "\n");
            }
            ow.write("\n");
        }
        List<ColumnSplice> columnSpliceList = (List<ColumnSplice>) map.get("ColumnSplice");
        if (CollectionUtils.isNotEmpty(columnSpliceList)) {
            for (ColumnSplice columnSplice : columnSpliceList) {
                ow.write("insert into t_xtpz_column_splice(id,column_relation_id,table_id,name,store_column_id,"
                        + "column_num,fill,filling_num1,filling_num2,filling_num3,filling_num4,filling_num5,prefix,suffix,"
                        + "seperator1,seperator2,seperator3,column1_id" + ",column2_id,column3_id,column4_id,seperator4,column5_id) values('"
                        + columnSplice.getId()
                        + "','"
                        + columnSplice.getColumnRelationId()
                        + "','"
                        + columnSplice.getTableId()
                        + "','"
                        + columnSplice.getName()
                        + "','"
                        + columnSplice.getStoreColumnId()
                        + "',"
                        + columnSplice.getColumnNum()
                        + ",'"
                        + (columnSplice.getFill() == null ? "0" : columnSplice.getFill())
                        + "',"
                        + StringUtil.null2zero(columnSplice.getFillingNum1())
                        + ","
                        + StringUtil.null2zero(columnSplice.getFillingNum2())
                        + ","
                        + StringUtil.null2zero(columnSplice.getFillingNum3())
                        + ","
                        + StringUtil.null2zero(columnSplice.getFillingNum4())
                        + ","
                        + StringUtil.null2zero(columnSplice.getFillingNum5())
                        + ",'"
                        + StringUtil.null2empty(columnSplice.getPrefix())
                        + "','"
                        + StringUtil.null2empty(columnSplice.getSuffix())
                        + "','"
                        + StringUtil.null2empty(columnSplice.getSeperator1())
                        + "','"
                        + StringUtil.null2empty(columnSplice.getSeperator2())
                        + "','"
                        + StringUtil.null2empty(columnSplice.getSeperator3())
                        + "','"
                        + StringUtil.null2empty(columnSplice.getColumn1Id())
                        + "','"
                        + StringUtil.null2empty(columnSplice.getColumn2Id())
                        + "','"
                        + StringUtil.null2empty(columnSplice.getColumn3Id())
                        + "','"
                        + StringUtil.null2empty(columnSplice.getColumn4Id())
                        + "','"
                        + StringUtil.null2empty(columnSplice.getSeperator4())
                        + "','"
                        + StringUtil.null2empty(columnSplice.getColumn5Id()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ColumnSplit> columnSplitList = (List<ColumnSplit>) map.get("ColumnSplit");
        if (CollectionUtils.isNotEmpty(columnSplitList)) {
            for (ColumnSplit columnSplit : columnSplitList) {
                ow.write("insert into t_xtpz_column_split(id,column_relation_id,table_id,name,from_column_id,"
                        + "to_column_id,start_position,end_position) values('" + columnSplit.getId() + "','" + columnSplit.getColumnRelationId() + "','"
                        + columnSplit.getTableId() + "','" + columnSplit.getName() + "','" + columnSplit.getFromColumnId() + "','"
                        + columnSplit.getToColumnId() + "'," + columnSplit.getStartPosition() + "," + columnSplit.getEndPosition() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<ColumnBusiness> columnBusinessList = (List<ColumnBusiness>) map.get("ColumnBusiness");
        if (CollectionUtils.isNotEmpty(columnBusinessList)) {
            for (ColumnBusiness columnBusiness : columnBusinessList) {
                ow.write("insert into t_xtpz_column_business(id,column_relation_id,table_id,name,"
                        + "item_column_id,pages_column_id,pageno_column_id,p_type) values('" + columnBusiness.getId() + "','"
                        + columnBusiness.getColumnRelationId() + "','" + columnBusiness.getTableId() + "','" + columnBusiness.getName() + "','"
                        + columnBusiness.getItemColumnId() + "','" + columnBusiness.getPagesColumnId() + "','" + columnBusiness.getPagenoColumnId() + "',"
                        + StringUtil.null2zero(columnBusiness.getPtype()) + ");" + "\n");
            }
            ow.write("\n");
        }
        List<ColumnOperation> columnOperationList = (List<ColumnOperation>) map.get("ColumnOperation");
        if (CollectionUtils.isNotEmpty(columnOperationList)) {
            for (ColumnOperation columnOperation : columnOperationList) {
                ow.write("insert into t_xtpz_column_operation(id,column_relation_id,name,type,table_id,column_id,"
                        + "origin_table_id,origin_column_id,operator) values('" + columnOperation.getId() + "','" + columnOperation.getColumnRelationId()
                        + "','" + columnOperation.getName() + "','" + columnOperation.getType() + "','" + columnOperation.getTableId() + "','"
                        + columnOperation.getColumnId() + "','" + columnOperation.getOriginTableId() + "','" + columnOperation.getOriginColumnId() + "','"
                        + StringUtil.null2empty(columnOperation.getOperator()) + "');" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出应用定义相关数据
     */
    @SuppressWarnings("unchecked")
    private void createAppDefineRelateSql(Map<String, Object> map, String databaseType, OutputStreamWriter ow) throws IOException {
        List<AppDefine> appDefineList = (List<AppDefine>) map.get("AppDefine");
        if (CollectionUtils.isNotEmpty(appDefineList)) {
            for (AppDefine appDefine : appDefineList) {
                ow.write("insert into t_xtpz_app_define(id,component_version_id,menu_id,searched,columned,sorted,formed,"
                        + "table_id,grid_buttoned,form_buttoned,filtered,reported,user_id) values ('"
                        + appDefine.getId()
                        + "','"
                        + appDefine.getComponentVersionId()
                        + "','"
                        + appDefine.getMenuId()
                        + "','"
                        + appDefine.getSearched()
                        + "','"
                        + appDefine.getColumned()
                        + "','"
                        + appDefine.getSorted()
                        + "','"
                        + appDefine.getFormed()
                        + "','"
                        + appDefine.getTableId()
                        + "','"
                        + appDefine.getGridButtoned()
                        + "','"
                        + appDefine.getFormButtoned()
                        + "','"
                        + appDefine.getFiltered()
                        + "','"
                        + appDefine.getReported() + "','" + appDefine.getUserId() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<AppSearch> appSearchList = (List<AppSearch>) map.get("AppSearch");
        if (CollectionUtils.isNotEmpty(appSearchList)) {
            for (AppSearch appSearch : appSearchList) {
                ow.write("insert into t_xtpz_app_search(id,table_id,component_version_id,menu_id,user_id,column_id,show_name,show_order,filter_type) values('"
                        + appSearch.getId() + "','" + appSearch.getTableId() + "','" + appSearch.getComponentVersionId() + "','" + appSearch.getMenuId()
                        + "','" + appSearch.getUserId() + "','" + appSearch.getColumnId() + "','" + appSearch.getShowName() + "'," + appSearch.getShowOrder()
                        + ",'" + appSearch.getFilterType() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<AppSearchPanel> appSearchPanelList = (List<AppSearchPanel>) map.get("AppSearchPanel");
        if (CollectionUtils.isNotEmpty(appSearchPanelList)) {
            for (AppSearchPanel appSearchPanel : appSearchPanelList) {
                ow.write("insert into t_xtpz_app_search_panel(id,table_id,component_version_id,menu_id,user_id,colspan) values('" + appSearchPanel.getId()
                        + "','" + appSearchPanel.getTableId() + "','" + appSearchPanel.getComponentVersionId() + "','" + appSearchPanel.getMenuId() + "','"
                        + appSearchPanel.getUserId() + "','" + appSearchPanel.getColspan() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<AppColumn> appColumnList = (List<AppColumn>) map.get("AppColumn");
        if (CollectionUtils.isNotEmpty(appColumnList)) {
            for (AppColumn appColumn : appColumnList) {
                ow.write("insert into t_xtpz_app_column(id,table_id,component_version_id,menu_id,column_id,align,width,"
                        + "show_order,user_id,type,column_name,column_alias,column_type,show_name,url) values('"
                        + appColumn.getId()
                        + "','"
                        + appColumn.getTableId()
                        + "','"
                        + appColumn.getComponentVersionId()
                        + "','"
                        + appColumn.getMenuId()
                        + "','"
                        + appColumn.getColumnId()
                        + "','"
                        + appColumn.getAlign()
                        + "',"
                        + appColumn.getWidth()
                        + ","
                        + appColumn.getShowOrder()
                        + ",'"
                        + appColumn.getUserId()
                        + "','"
                        + StringUtil.null2empty(appColumn.getType())
                        + "','"
                        + appColumn.getColumnName()
                        + "','"
                        + appColumn.getColumnAlias()
                        + "','"
                        + appColumn.getColumnType()
                        + "','"
                        + appColumn.getShowName()
                        + "','"
                        + parseStrByDatabaseType(appColumn.getUrl(), databaseType) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<AppGrid> appGridList = (List<AppGrid>) map.get("AppGrid");
        if (CollectionUtils.isNotEmpty(appGridList)) {
            for (AppGrid appGrid : appGridList) {
                ow.write("insert into t_xtpz_app_grid(id,table_id,component_version_id,menu_id,user_id,dblclick,has_row_number,search_type,pageable,adaptive,ope_col_position,ope_col_name,ope_col_width,header_setting,select_model) values('"
                        + appGrid.getId()
                        + "','"
                        + appGrid.getTableId()
                        + "','"
                        + appGrid.getComponentVersionId()
                        + "','"
                        + appGrid.getMenuId()
                        + "','"
                        + appGrid.getUserId()
                        + "',"
                        + StringUtil.null2zero(appGrid.getDblclick())
                        + ","
                        + StringUtil.null2zero(appGrid.getHasRowNumber())
                        + ","
                        + StringUtil.null2zero(appGrid.getSearchType())
                        + ","
                        + StringUtil.null2zero(appGrid.getPageable())
                        + ","
                        + StringUtil.null2zero(appGrid.getAdaptive())
                        + ","
                        + StringUtil.null2zero(appGrid.getOpeColPosition())
                        + ",'"
                        + StringUtil.null2empty(appGrid.getOpeColName())
                        + "',"
                        + StringUtil.null2zero(appGrid.getOpeColWidth())
                        + ","
                        + StringUtil.null2zero(appGrid.getHeaderSetting()) + ",'" + StringUtil.null2empty(appGrid.getSelectModel()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<AppSort> appSortList = (List<AppSort>) map.get("AppSort");
        if (CollectionUtils.isNotEmpty(appSortList)) {
            for (AppSort appSort : appSortList) {
                ow.write("insert into t_xtpz_app_sort(id,table_id,component_version_id,menu_id,user_id,column_id,sort_type,show_order) values('"
                        + appSort.getId() + "','" + appSort.getTableId() + "','" + appSort.getComponentVersionId() + "','" + appSort.getMenuId() + "','"
                        + appSort.getUserId() + "','" + appSort.getColumnId() + "','" + appSort.getSortType() + "'," + appSort.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<AppButton> appButtonList = (List<AppButton>) map.get("AppButton");
        if (CollectionUtils.isNotEmpty(appButtonList)) {
            for (AppButton appButton : appButtonList) {
                ow.write("insert into t_xtpz_app_button(id,table_id,component_version_id,menu_id,button_type,button_code,button_name,show_name,show_order,display,remark,button_class"
                        + ") values ('"
                        + appButton.getId()
                        + "','"
                        + appButton.getTableId()
                        + "','"
                        + appButton.getComponentVersionId()
                        + "','"
                        + appButton.getMenuId()
                        + "','"
                        + appButton.getButtonType()
                        + "','"
                        + StringUtil.null2empty(appButton.getButtonCode())
                        + "','"
                        + StringUtil.null2empty(appButton.getButtonName())
                        + "','"
                        + appButton.getShowName()
                        + "',"
                        + appButton.getShowOrder()
                        + ",'"
                        + StringUtil.null2zero(appButton.getDisplay())
                        + "','"
                        + StringUtil.null2empty(appButton.getRemark())
                        + "','"
                        + StringUtil.null2empty(appButton.getButtonClass()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<AppForm> appFormList = (List<AppForm>) map.get("AppForm");
        if (CollectionUtils.isNotEmpty(appFormList)) {
            for (AppForm appForm : appFormList) {
                ow.write("insert into t_xtpz_app_form(id,table_id,component_version_id,menu_id,type,colspan,border) values('" + appForm.getId() + "','"
                        + appForm.getTableId() + "','" + appForm.getComponentVersionId() + "','" + appForm.getMenuId() + "','"
                        + StringUtil.null2zero(appForm.getType()) + "'," + appForm.getColspan() + ",'" + appForm.getBorder() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<AppFormElement> appFormElementList = (List<AppFormElement>) map.get("AppFormElement");
        if (CollectionUtils.isNotEmpty(appFormElementList)) {
            for (AppFormElement appFormElement : appFormElementList) {
                ow.write("insert into t_xtpz_app_form_element(id,table_id,column_id,show_name,colspan,space_percent,"
                        + "required,readonly,hidden,kept,default_value,component_version_id,menu_id,show_order,validation,tooltip,increase,inherit,pattern) values ('"
                        + appFormElement.getId()
                        + "','"
                        + appFormElement.getTableId()
                        + "','"
                        + appFormElement.getColumnId()
                        + "','"
                        + appFormElement.getShowName()
                        + "',"
                        + appFormElement.getColspan()
                        + ","
                        + appFormElement.getSpacePercent()
                        + ",'"
                        + StringUtil.null2empty(appFormElement.getRequired())
                        + "','"
                        + StringUtil.null2empty(appFormElement.getReadonly())
                        + "','"
                        + StringUtil.null2empty(appFormElement.getHidden())
                        + "','"
                        + StringUtil.null2empty(appFormElement.getKept())
                        + "','"
                        + StringUtil.null2empty(appFormElement.getDefaultValue())
                        + "','"
                        + appFormElement.getComponentVersionId()
                        + "','"
                        + appFormElement.getMenuId()
                        + "',"
                        + appFormElement.getShowOrder()
                        + ",'"
                        + StringUtil.null2empty(appFormElement.getValidation())
                        + "','"
                        + StringUtil.null2empty(appFormElement.getTooltip())
                        + "','"
                        + StringUtil.null2zero(appFormElement.getIncrease())
                        + "','"
                        + StringUtil.null2zero(appFormElement.getInherit())
                        + "','"
                        + StringUtil.null2empty(appFormElement.getPattern()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<AppReport> appReportList = (List<AppReport>) map.get("AppReport");
        if (CollectionUtils.isNotEmpty(appReportList)) {
            for (AppReport appReport : appReportList) {
                ow.write("insert into t_xtpz_app_report(id,table_id,component_version_id,menu_id,user_id,report_id,show_order) values('" + appReport.getId()
                        + "','" + appReport.getTableId() + "','" + appReport.getComponentVersionId() + "','" + appReport.getMenuId() + "','"
                        + appReport.getUserId() + "','" + appReport.getReportId() + "'," + appReport.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出工作流相关数据
     */
    @SuppressWarnings("unchecked")
    private Set<String> createWfRelateSql(Map<String, Object> map, OutputStreamWriter ow) throws IOException {
        List<WorkflowTree> workflowTreeList = (List<WorkflowTree>) map.get("WorkflowTree");
        if (CollectionUtils.isNotEmpty(workflowTreeList)) {
            for (WorkflowTree workflowTree : workflowTreeList) {
                ow.write("insert into t_xtpz_workflow_tree(id,parent_id,name,show_order) values('" + workflowTree.getId() + "','" + workflowTree.getParentId()
                        + "','" + workflowTree.getName() + "'," + workflowTree.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<WorkflowDefine> workflowDefineList = (List<WorkflowDefine>) map.get("WorkflowDefine");
        if (CollectionUtils.isNotEmpty(workflowDefineList)) {
            for (WorkflowDefine workflowDefine : workflowDefineList) {
                ow.write("insert into t_xtpz_workflow_define(id,workflow_tree_id,workflow_code,workflow_name,business_table_id,"
                        + "enable_document_table,enable_confirm_table,enable_assist_table,workflow_boxes,show_order,remark) values('"
                        + workflowDefine.getId()
                        + "','"
                        + workflowDefine.getWorkflowTreeId()
                        + "','"
                        + workflowDefine.getWorkflowCode()
                        + "','"
                        + workflowDefine.getWorkflowName()
                        + "','"
                        + workflowDefine.getBusinessTableId()
                        + "','"
                        + workflowDefine.getEnableDocumentTable()
                        + "','"
                        + workflowDefine.getEnableConfirmTable()
                        + "','"
                        + workflowDefine.getEnableAssistTable()
                        + "','"
                        + workflowDefine.getWorkflowBoxes()
                        + "',"
                        + workflowDefine.getShowOrder()
                        + ",'"
                        + StringUtil.null2empty(workflowDefine.getRemark())
                        + "');" + "\n");

            }
            ow.write("\n");
        }
        List<WorkflowVersion> workflowVersionList = (List<WorkflowVersion>) map.get("WorkflowVersion");
        if (CollectionUtils.isNotEmpty(workflowVersionList)) {
            for (WorkflowVersion workflowVersion : workflowVersionList) {
                ow.write("insert into t_xtpz_workflow_version(id,workflow_id,show_order,version,status,remark) values('" + workflowVersion.getId() + "','"
                        + workflowVersion.getWorkflowId() + "'," + workflowVersion.getShowOrder() + ",'" + workflowVersion.getVersion() + "','"
                        + workflowVersion.getStatus() + "','" + StringUtil.null2empty(workflowVersion.getRemark()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<WorkflowActivity> workflowActivityList = (List<WorkflowActivity>) map.get("WorkflowActivity");
        if (CollectionUtils.isNotEmpty(workflowActivityList)) {
            for (WorkflowActivity workflowActivity : workflowActivityList) {
                ow.write("insert into t_xtpz_workflow_activity(id,show_order,package_id,package_version,process_id,activity_id,activity_name,activity_type) values('"
                        + workflowActivity.getId()
                        + "',"
                        + workflowActivity.getShowOrder()
                        + ",'"
                        + StringUtil.null2empty(workflowActivity.getPackageId())
                        + "','"
                        + StringUtil.null2empty(workflowActivity.getPackageVersion())
                        + "','"
                        + StringUtil.null2empty(workflowActivity.getProcessId())
                        + "','"
                        + StringUtil.null2empty(workflowActivity.getActivityId())
                        + "','"
                        + StringUtil.null2empty(workflowActivity.getActivityName())
                        + "','" + StringUtil.null2empty(workflowActivity.getActivityType()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<WorkflowFormSetting> workflowFormSettingList = (List<WorkflowFormSetting>) map.get("WorkflowFormSetting");
        if (CollectionUtils.isNotEmpty(workflowFormSettingList)) {
            for (WorkflowFormSetting workflowFormSetting : workflowFormSettingList) {
                ow.write("insert into t_xtpz_workflow_form_setting(id,workflow_version_id,activity_id,column_id,disable,default_value) values('"
                        + workflowFormSetting.getId() + "','" + workflowFormSetting.getWorkflowVersionId() + "','" + workflowFormSetting.getActivityId()
                        + "','" + workflowFormSetting.getColumnId() + "','" + workflowFormSetting.getDisabled() + "','"
                        + StringUtil.null2empty(workflowFormSetting.getDefaultValue()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<WorkflowButtonSetting> workflowButtonSettingList = (List<WorkflowButtonSetting>) map.get("WorkflowButtonSetting");
        if (CollectionUtils.isNotEmpty(workflowButtonSettingList)) {
            for (WorkflowButtonSetting workflowButtonSetting : workflowButtonSettingList) {
                ow.write("insert into t_xtpz_workflow_button_setting(id,workflow_version_id,activity_id,button_code,button_type) values('"
                        + workflowButtonSetting.getId() + "','" + workflowButtonSetting.getWorkflowVersionId() + "','" + workflowButtonSetting.getActivityId()
                        + "','" + workflowButtonSetting.getButtonCode() + "','" + workflowButtonSetting.getButtonType() + "');" + "\n");
            }
            ow.write("\n");
        }
        // 工作流相关表数据（t_wf_relevantdata_tablename）
        List<Object[]> wfRelevantdataList = (List<Object[]>) map.get("WorkflowRelevantdata");
        Set<String> wfRelevantdataTableNameSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(wfRelevantdataList)) {
            for (Object[] wfRelevantdata : wfRelevantdataList) {
                ow.write("insert into t_wf_relevantdata_tablename(table_name,package_id,process_id) values('" + String.valueOf(wfRelevantdata[0]) + "','"
                        + String.valueOf(wfRelevantdata[1]) + "','" + String.valueOf(wfRelevantdata[2]) + "');" + "\n");
                wfRelevantdataTableNameSet.add(wfRelevantdata[0].toString().toUpperCase());
            }
            ow.write("\n");
        }
        return wfRelevantdataTableNameSet;
    }

    /**
     * 导出报表相关数据
     */
    @SuppressWarnings("unchecked")
    private void createReportRelateSql(Map<String, Object> map, OutputStreamWriter ow) throws IOException {
        List<Report> reportList = (List<Report>) map.get("Report");
        if (CollectionUtils.isNotEmpty(reportList)) {
            for (Report report : reportList) {
                ow.write("insert into t_xtpz_report(id,parent_id,name,type,show_order) values('" + report.getId() + "','" + report.getParentId() + "','"
                        + report.getName() + "','" + report.getType() + "'," + report.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<ReportColumn> reportColumnList = (List<ReportColumn>) map.get("ReportColumn");
        if (CollectionUtils.isNotEmpty(reportColumnList)) {
            for (ReportColumn reportColumn : reportColumnList) {
                ow.write("insert into t_xtpz_report_column(id,report_id,show_order,table_id,column_id,column_name,"
                        + "column_comment,column_alias,row_index,col_index,in_celled,condition_index) values('"
                        + reportColumn.getId()
                        + "','"
                        + reportColumn.getReportId()
                        + "',"
                        + reportColumn.getShowOrder()
                        + ",'"
                        + reportColumn.getTableId()
                        + "','"
                        + reportColumn.getColumnId()
                        + "','"
                        + reportColumn.getColumnName()
                        + "','"
                        + StringUtil.null2empty(reportColumn.getColumnComment())
                        + "','"
                        + StringUtil.null2empty(reportColumn.getColumnAlias())
                        + "',"
                        + reportColumn.getRowIndex()
                        + ","
                        + reportColumn.getColIndex()
                        + ",'"
                        + reportColumn.getInCelled()
                        + "',"
                        + reportColumn.getConditionIndex() + ");" + "\n");
            }
            ow.write("\n");
        }
        List<ReportDataSource> reportDataSourceList = (List<ReportDataSource>) map.get("ReportDataSource");
        if (CollectionUtils.isNotEmpty(reportDataSourceList)) {
            for (ReportDataSource reportDataSource : reportDataSourceList) {
                ow.write("insert into t_xtpz_report_data_source(id,report_id,table_id,column_id,show_order,type,"
                        + "table_name,table_comment,column_name,column_comment) values('" + reportDataSource.getId() + "','" + reportDataSource.getReportId()
                        + "','" + reportDataSource.getTableId() + "','" + reportDataSource.getColumnId() + "'," + reportDataSource.getShowOrder() + ",'"
                        + reportDataSource.getType() + "','" + reportDataSource.getTableName() + "','" + reportDataSource.getTableComment() + "','"
                        + reportDataSource.getColumnName() + "','" + reportDataSource.getColumnComment() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ReportDefine> reportDefineList = (List<ReportDefine>) map.get("ReportDefine");
        if (CollectionUtils.isNotEmpty(reportDefineList)) {
            for (ReportDefine reportDefine : reportDefineList) {
                if (reportDefine == null || reportDefine.getId() == null) {
                    continue;
                }
                ow.write("insert into t_xtpz_report_define(id,report_id,header_start,header_end,cycle_start,cycle_end,"
                        + "tail_start,tail_end,last_start,last_end,rn_row_index,rn_col_index) values('"
                        + reportDefine.getId()
                        + "','"
                        + reportDefine.getReportId()
                        + "',"
                        + StringUtil.null2zero(reportDefine.getHeaderStart())
                        + ","
                        + StringUtil.null2zero(reportDefine.getHeaderEnd())
                        + ","
                        + StringUtil.null2zero(reportDefine.getCycleStart())
                        + ","
                        + StringUtil.null2zero(reportDefine.getCycleEnd())
                        + ","
                        + StringUtil.null2zero(reportDefine.getTailStart())
                        + ","
                        + StringUtil.null2zero(reportDefine.getTailEnd())
                        + ","
                        + StringUtil.null2zero(reportDefine.getLastStart())
                        + ","
                        + StringUtil.null2zero(reportDefine.getLastEnd())
                        + ","
                        + StringUtil.null2zero(reportDefine.getRnRowIndex())
                        + ","
                        + StringUtil.null2zero(reportDefine.getRnColIndex()) + ");" + "\n");
            }
            ow.write("\n");
        }
        List<ReportPrintSetting> reportPrintSettingList = (List<ReportPrintSetting>) map.get("ReportPrintSetting");
        if (CollectionUtils.isNotEmpty(reportPrintSettingList)) {
            for (ReportPrintSetting reportPrintSetting : reportPrintSettingList) {
                ow.write("insert into t_xtpz_report_print_setting(id,report_id,type,show_order,column_id,value) " + "values('" + reportPrintSetting.getId()
                        + "','" + reportPrintSetting.getReportId() + "','" + reportPrintSetting.getType() + "'," + reportPrintSetting.getShowOrder() + ",'"
                        + reportPrintSetting.getColumnId() + "','" + reportPrintSetting.getValue() + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ReportTable> reportTableList = (List<ReportTable>) map.get("ReportTable");
        if (CollectionUtils.isNotEmpty(reportTableList)) {
            for (ReportTable reportTable : reportTableList) {
                ow.write("insert into t_xtpz_report_table(id,report_id,show_order,table_id,table_name,table_comment,table_alias) values('"
                        + reportTable.getId() + "','" + reportTable.getReportId() + "'," + reportTable.getShowOrder() + ",'" + reportTable.getTableId() + "','"
                        + reportTable.getTableName() + "','" + StringUtil.null2empty(reportTable.getTableComment()) + "','"
                        + StringUtil.null2empty(reportTable.getTableAlias()) + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ReportTableRelation> reportTableRelationList = (List<ReportTableRelation>) map.get("ReportTableRelation");
        if (CollectionUtils.isNotEmpty(reportTableRelationList)) {
            for (ReportTableRelation reportTableRelation : reportTableRelationList) {
                ow.write("insert into t_xtpz_report_table_relation(id,report_id,table_id,column_id,relate_table_id,relate_column_id,show_order) values('"
                        + reportTableRelation.getId() + "','" + reportTableRelation.getReportId() + "','" + reportTableRelation.getTableId() + "','"
                        + reportTableRelation.getColumnId() + "','" + reportTableRelation.getRelateTableId() + "','" + reportTableRelation.getRelateColumnId()
                        + "'," + reportTableRelation.getShowOrder() + ");" + "\n");
            }
            ow.write("\n");
        }
    }

    /**
     * 导出系统版本、资源相关数据
     */
    @SuppressWarnings("unchecked")
    private SystemVersion createVersionResourceRelateSql(String rootMenuId, String version, Map<String, Object> map, OutputStreamWriter ow,
            Set<String> canUseResourceIdSet) throws IOException {
        // 系统版本、版本资源
        SystemVersion systemVersion = (SystemVersion) map.get("SystemVersion");
        ow.write("insert into t_xtpz_system_version(id,name,is_default,system_id,remark) values('" + systemVersion.getId() + "','" + systemVersion.getName()
                + "','" + systemVersion.getIsDefault() + "','" + systemVersion.getSystemId() + "','" + StringUtil.null2empty(systemVersion.getRemark()) + "');"
                + "\n");
        List<SystemVersionResource> sysVerResourceList = (List<SystemVersionResource>) map.get("SystemVersionResource");
        // 可用的资源IDs
        if (CollectionUtils.isNotEmpty(sysVerResourceList)) {
            for (SystemVersionResource systemVersionResource : sysVerResourceList) {
                canUseResourceIdSet.add(systemVersionResource.getResourceId());
                ow.write("insert into t_xtpz_sys_version_resource(id,system_version_id,resource_id) values('" + systemVersionResource.getId() + "','"
                        + systemVersionResource.getSystemVersionId() + "','" + systemVersionResource.getResourceId() + "');" + "\n");
            }
            ow.write("\n");
        }
        // 资源、资源按钮
        List<Resource> resourceList = (List<Resource>) map.get("Resource");
        if (CollectionUtils.isNotEmpty(resourceList)) {
            for (Resource resource : resourceList) {
                ow.write("insert into t_xtpz_resource(id,name,type,show_order,remark,parent_id,system_id,target_id,can_use) values('"
                        + resource.getId()
                        + "','"
                        + StringUtil.null2empty(resource.getName())
                        + "','"
                        + StringUtil.null2empty(resource.getType())
                        + "',"
                        + resource.getShowOrder()
                        + ",'"
                        + StringUtil.null2empty(resource.getRemark())
                        + "','"
                        + StringUtil.null2empty(resource.getParentId())
                        + "','"
                        + StringUtil.null2empty(resource.getSystemId())
                        + "','"
                        + StringUtil.null2empty(resource.getTargetId())
                        + "','"
                        + ("1".equals(systemVersion.getIsDefault()) || (canUseResourceIdSet.contains(resource.getId()) || "-1".equals(resource.getParentId())) ? "1"
                                : "0") + "');" + "\n");
            }
            ow.write("\n");
        }
        List<ResourceButton> resourceButtonList = (List<ResourceButton>) map.get("ResourceButton");
        if (CollectionUtils.isNotEmpty(resourceButtonList)) {
            for (ResourceButton resourceButton : resourceButtonList) {
                ow.write("insert into t_xtpz_resource_button(id,resource_id,button_id,button_source) values('" + resourceButton.getId() + "','"
                        + resourceButton.getResourceId() + "','" + resourceButton.getButtonId() + "','" + resourceButton.getButtonSource() + "');" + "\n");
            }
            ow.write("\n");
        }
        Menu rootMenu = getService(MenuService.class).getByID(rootMenuId);
        ow.write("insert into t_xtpz_system(id,name,version,system_version_id) values('" + rootMenuId + "','" + rootMenu.getName() + "','" + version + "','"
                + systemVersion.getId() + "');" + "\n");
        return systemVersion;
    }

    /**
     * 导出构件中表的数据
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Set<String> createCompTableDataSql(String databaseType, OutputStreamWriter ow, List<ComponentTable> componentTableList,
            Set<PhysicalTableDefine> physicalTableDefineSet) throws IOException {
        if (CollectionUtils.isNotEmpty(componentTableList)) {
            StringBuilder columns = null;
            String selectSql = null;
            String insertSql = null;
            for (ComponentTable componentTable : componentTableList) {
                if ("1".equals(componentTable.getReleaseWithData()) && DatabaseHandlerDao.getInstance().tableExists(componentTable.getName())) {
                    List<ComponentColumn> columnList = getService(ComponentColumnService.class).getByTableId(componentTable.getId());
                    columns = new StringBuilder();
                    for (ComponentColumn componentColumn : columnList) {
                        columns.append(componentColumn.getName()).append(",");
                    }
                    columns.deleteCharAt(columns.length() - 1);
                    selectSql = "select " + columns.toString() + " from " + componentTable.getName();
                    List list = DatabaseHandlerDao.getInstance().queryForList(selectSql);
                    if (CollectionUtils.isNotEmpty(list)) {
                        if (columns.indexOf(",") == -1) {
                            List<Object> data = (List<Object>) list;
                            for (Object obj : data) {
                                insertSql = "insert into " + componentTable.getName() + "(" + columns.toString() + ") values(";
                                if ("数字型".equals(columnList.get(0).getType())) {
                                    insertSql += obj;
                                } else {
                                    insertSql += "'" + parseStrByDatabaseType(String.valueOf(obj), databaseType) + "'";
                                }
                                insertSql += ");";
                                ow.write(insertSql + "\n");
                            }
                        } else {
                            List<Object[]> data = (List<Object[]>) list;
                            for (Object[] objs : data) {
                                insertSql = "insert into " + componentTable.getName() + "(" + columns.toString() + ") values(";
                                for (int i = 0; i < columnList.size(); i++) {
                                    if (i != columnList.size() - 1) {
                                        if ("数字型".equals(columnList.get(i).getType())) {
                                            insertSql += objs[i] + ",";
                                        } else {
                                            insertSql += "'" + parseStrByDatabaseType(String.valueOf(objs[i]), databaseType) + "',";
                                        }
                                    } else {
                                        if ("数字型".equals(columnList.get(i).getType())) {
                                            insertSql += objs[i];
                                        } else {
                                            insertSql += "'" + parseStrByDatabaseType(String.valueOf(objs[i]), databaseType) + "'";
                                        }
                                    }
                                }
                                insertSql += ");";
                                ow.write(insertSql + "\n");
                            }
                        }
                    }
                }
            }
        }
        Set<String> physicalTableNameSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(physicalTableDefineSet)) {
            StringBuilder columns = null;
            String selectSql = null;
            String insertSql = null;
            for (PhysicalTableDefine physicalTableDefine : physicalTableDefineSet) {
                physicalTableNameSet.add(physicalTableDefine.getTableName());
                if ("1".equals(physicalTableDefine.getReleaseWithData())) {
                    List<ColumnDefine> columnList = getService(ColumnDefineService.class).findByTableId(physicalTableDefine.getId());
                    columns = new StringBuilder();
                    for (ColumnDefine columnDefine : columnList) {
                        columns.append(columnDefine.getColumnName()).append(",");
                    }
                    columns.deleteCharAt(columns.length() - 1);
                    selectSql = "select " + columns.toString() + " from " + physicalTableDefine.getTableName();
                    List list = DatabaseHandlerDao.getInstance().queryForList(selectSql);
                    if (CollectionUtils.isNotEmpty(list)) {
                        if (columns.indexOf(",") == -1) {
                            List<Object> data = (List<Object>) list;
                            for (Object obj : data) {
                                insertSql = "insert into " + physicalTableDefine.getTableName() + "(" + columns.toString() + ") values(";
                                if (ConstantVar.DataType.NUMBER.equals(columnList.get(0).getDataType())) {
                                    insertSql += obj;
                                } else {
                                    insertSql += "'" + parseStrByDatabaseType(String.valueOf(obj), databaseType) + "'";
                                }
                                insertSql += ");";
                                ow.write(insertSql + "\n");
                            }
                        } else {
                            List<Object[]> data = (List<Object[]>) list;
                            for (Object[] objs : data) {
                                insertSql = "insert into " + physicalTableDefine.getTableName() + "(" + columns.toString() + ") values(";
                                for (int i = 0; i < columnList.size(); i++) {
                                    if (i != columnList.size() - 1) {
                                        if (ConstantVar.DataType.NUMBER.equals(columnList.get(i).getDataType())) {
                                            insertSql += objs[i] + ",";
                                        } else {
                                            insertSql += "'" + parseStrByDatabaseType(String.valueOf(objs[i]), databaseType) + "',";
                                        }
                                    } else {
                                        if (ConstantVar.DataType.NUMBER.equals(columnList.get(i).getDataType())) {
                                            insertSql += objs[i];
                                        } else {
                                            insertSql += "'" + parseStrByDatabaseType(String.valueOf(objs[i]), databaseType) + "'";
                                        }
                                    }
                                }
                                insertSql += ");";
                                ow.write(insertSql + "\n");
                            }
                        }
                    }
                }
            }
        }
        return physicalTableNameSet;
    }

    /**
     * 创建Index相关表sql
     */
    @SuppressWarnings("unchecked")
    private void createIndexSql(OutputStreamWriter ow, Set<String> physicalTableNameSet) throws IOException {
        String indexConfigSql = "select id,table_name,server_id,scan_status,scan_log_level,scan_last_date,table_id from t_fs_index_config";
        List<Object[]> indexConfigList = DatabaseHandlerDao.getInstance().queryForList(indexConfigSql);
        if (CollectionUtils.isNotEmpty(indexConfigList)) {
            for (Object[] indexConfig : indexConfigList) {
                if (physicalTableNameSet.contains(indexConfig[1])) {
                    ow.write("insert into t_fs_index_config(id,table_name,server_id,scan_status," + "scan_log_level,scan_last_date,table_id) values('"
                            + StringUtil.null2empty(indexConfig[0]) + "','" + StringUtil.null2empty(indexConfig[1]) + "','"
                            + StringUtil.null2empty(indexConfig[2]) + "','" + StringUtil.null2empty(indexConfig[3]) + "'," + indexConfig[4] + ",'"
                            + StringUtil.null2empty(indexConfig[5]) + "','" + StringUtil.null2empty(indexConfig[6]) + "');" + "\n");
                }
            }
            ow.write("\n");
        }
        String indexSchemaSql = "select id,table_name,column_name,data_type,sortable,cn_analyzable,table_id,column_name_affix from t_fs_index_schema";
        List<Object[]> indexSchemaList = DatabaseHandlerDao.getInstance().queryForList(indexSchemaSql);
        if (CollectionUtils.isNotEmpty(indexSchemaList)) {
            for (Object[] indexSchema : indexSchemaList) {
                if (physicalTableNameSet.contains(indexSchema[1])) {
                    ow.write("insert into t_fs_index_schema(id,table_name,column_name,data_type,sortable,cn_analyzable,table_id,column_name_affix) values('"
                            + StringUtil.null2empty(indexSchema[0]) + "','" + StringUtil.null2empty(indexSchema[1]) + "','"
                            + StringUtil.null2empty(indexSchema[2]) + "','" + StringUtil.null2empty(indexSchema[3]) + "','"
                            + StringUtil.null2empty(indexSchema[4]) + "','" + StringUtil.null2empty(indexSchema[5]) + "','"
                            + StringUtil.null2empty(indexSchema[6]) + "','" + StringUtil.null2empty(indexSchema[7]) + "');" + "\n");
                }
            }
            ow.write("\n");
        }
    }

    /**
     * 创建数据权限的相关sql
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private void createAuthorityDataSql(OutputStreamWriter ow, Map<String, Object> map) throws IOException {
        List<AuthorityTree> authorityTreeList = (List<AuthorityTree>) map.get("AuthorityTree");
        List<AuthorityData> authorityDataList = (List<AuthorityData>) map.get("AuthorityData");
        List<AuthorityDataDetail> authorityDataDetailList = (List<AuthorityDataDetail>) map.get("AuthorityDataDetail");
        List<AuthorityCode> authorityCodeList = (List<AuthorityCode>) map.get("AuthorityCode");
        if (CollectionUtils.isNotEmpty(authorityTreeList)) {
            for (AuthorityTree authorityTree : authorityTreeList) {
                ow.write("insert into t_xtpz_authority_tree(id,object_id,object_type,tree_node_id,component_version_id,menu_id) values('"
                        + authorityTree.getId() + "','" + authorityTree.getObjectId() + "','" + authorityTree.getObjectType() + "','"
                        + StringUtil.null2empty(authorityTree.getTreeNodeId()) + "','" + StringUtil.null2empty(authorityTree.getComponentVersionId()) + "','"
                        + StringUtil.null2empty(authorityTree.getMenuId()) + "');" + "\n");
            }
            ow.write("\n");
        }
        if (CollectionUtils.isNotEmpty(authorityDataList)) {
            for (AuthorityData authorityData : authorityDataList) {
                ow.write("insert into t_xtpz_authority_data(id,name,object_id,object_type,menu_id,component_version_id,table_id,show_order,control_table_ids) values ('"
                        + authorityData.getId()
                        + "','"
                        + authorityData.getName()
                        + "','"
                        + authorityData.getObjectId()
                        + "','"
                        + authorityData.getObjectType()
                        + "','"
                        + authorityData.getMenuId()
                        + "','"
                        + authorityData.getComponentVersionId()
                        + "','"
                        + authorityData.getTableId()
                        + "',"
                        + StringUtil.null2zero(authorityData.getShowOrder()) + ",'" + authorityData.getControlTableIds() + "');" + "\n");
            }
            ow.write("\n");
        }
        if (CollectionUtils.isNotEmpty(authorityDataDetailList)) {
            for (AuthorityDataDetail authorityDataDetail : authorityDataDetailList) {
                ow.write("insert into t_xtpz_authority_data_detail(id,authority_data_id,table_id,column_id,operator,value,relation,show_order,left_parenthesis,right_parenthesis) values('"
                        + authorityDataDetail.getId()
                        + "','"
                        + authorityDataDetail.getAuthorityDataId()
                        + "','"
                        + authorityDataDetail.getTableId()
                        + "','"
                        + authorityDataDetail.getColumnId()
                        + "','"
                        + authorityDataDetail.getOperator()
                        + "','"
                        + authorityDataDetail.getValue()
                        + "','"
                        + StringUtil.null2empty(authorityDataDetail.getRelation())
                        + "',"
                        + StringUtil.null2zero(authorityDataDetail.getShowOrder())
                        + ",'"
                        + StringUtil.null2empty(authorityDataDetail.getLeftParenthesis())
                        + "','"
                        + StringUtil.null2empty(authorityDataDetail.getRightParenthesis()) + "');" + "\n");
            }
            ow.write("\n");
        }
        if (CollectionUtils.isNotEmpty(authorityCodeList)) {
            List<CodeType> codeTypeList = (List<CodeType>) map.get("CodeType");
            Set<String> codeTypeCodeSet = new HashSet<String>();
            if (CollectionUtils.isNotEmpty(codeTypeList)) {
                for (CodeType codeType : codeTypeList) {
                    codeTypeCodeSet.add(codeType.getCode());
                }
            }
            for (AuthorityCode authorityCode : authorityCodeList) {
                if (codeTypeCodeSet.contains(authorityCode.getCodeTypeCode())) {
                    ow.write("insert into t_xtpz_authority_code(id,code_type_code,code_json,object_id,object_type,menu_id,component_version_id) values('"
                            + authorityCode.getId() + "','" + StringUtil.null2empty(authorityCode.getCodeTypeCode()) + "','" + authorityCode.getCodeJson()
                            + "','" + authorityCode.getObjectId() + "','" + authorityCode.getObjectType() + "','" + authorityCode.getMenuId() + "','"
                            + authorityCode.getComponentVersionId() + "');" + "\n");
                }
            }
            ow.write("\n");
        }
    }

    /**
     * 创建自定义构件的相关表sql
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void createSelfDefineTableSql(Map<String, Object> map, String path, String databaseType, Set<String> wfRelevantdataTableNameSet)
            throws UnsupportedEncodingException, FileNotFoundException, IOException {
        File selfDefineSqlFile = new File(path + "7_selfdefine_table.sql");
        OutputStreamWriter ow1 = new OutputStreamWriter(new FileOutputStream(selfDefineSqlFile), "UTF-8");
        Map<String, List<ColumnDefine>> tableColumnMap = (Map<String, List<ColumnDefine>>) map.get("TableColumn");
        String tableName = null;
        StringBuffer createTableSql = null;
        StringBuffer createBakTableSql = null;
        String bakTableName = null;
        List<ColumnDefine> selfDefineColumnList = null;
        for (Iterator<String> keyIterator = tableColumnMap.keySet().iterator(); keyIterator.hasNext();) {
            tableName = keyIterator.next();
            bakTableName = tableName + "_BAK";
            boolean bakTableExists = DatabaseHandlerDao.getInstance().tableExists(bakTableName);
            createTableSql = new StringBuffer();
            createTableSql.append("create table " + tableName + "(");
            if (bakTableExists) {
                createBakTableSql = new StringBuffer();
                createBakTableSql.append("create table " + bakTableName + "(");
            }
            selfDefineColumnList = tableColumnMap.get(tableName);
            for (ColumnDefine column : selfDefineColumnList) {
                createTableSql.append("\n\t" + column.getColumnName());
                createTableSql.append(" " + DatabaseHandlerDao.getInstance().getDataType(databaseType, column.getDataType(), column.getLength()));
                if (StringUtil.isNotEmpty(column.getDefaultValue())) {
                    if (ConstantVar.DataType.NUMBER.equals(column.getDataType())) {
                        createTableSql.append(" default " + Integer.valueOf(column.getDefaultValue()));
                    } else {
                        createTableSql.append(" default '" + column.getDefaultValue() + "'");
                    }
                }
                if ("id".equalsIgnoreCase(column.getColumnName())) {
                    createTableSql.append(" primary key");
                }
                createTableSql.append(",");
                if (bakTableExists) {
                    createBakTableSql.append("\n\t" + column.getColumnName());
                    createBakTableSql.append(" " + DatabaseHandlerDao.getInstance().getDataType(databaseType, column.getDataType(), column.getLength()));
                    if (StringUtil.isNotEmpty(column.getDefaultValue())) {
                        if (ConstantVar.DataType.NUMBER.equals(column.getDataType())) {
                            createBakTableSql.append(" default " + Integer.valueOf(column.getDefaultValue()));
                        } else {
                            createBakTableSql.append(" default '" + column.getDefaultValue() + "'");
                        }
                    }
                    if ("id".equalsIgnoreCase(column.getColumnName())) {
                        createBakTableSql.append(" primary key");
                    }
                    createBakTableSql.append(",");
                }
            }
            createTableSql.deleteCharAt(createTableSql.length() - 1);
            createTableSql.append("\n);");
            ow1.write(createTableSql.toString() + "\n");
            if (bakTableExists) {
                createBakTableSql.append("\n\tF_ID");
                createBakTableSql.append(" " + DatabaseHandlerDao.getInstance().getDataType(databaseType, ConstantVar.DataType.CHAR, 32)).append(",");
                createBakTableSql.append("\n\tP_ID");
                createBakTableSql.append(" " + DatabaseHandlerDao.getInstance().getDataType(databaseType, ConstantVar.DataType.CHAR, 32)).append(",");
                createBakTableSql.append("\n\tBAK_NEW_VALUE");
                createBakTableSql.append(" " + DatabaseHandlerDao.getInstance().getDataType(databaseType, ConstantVar.DataType.CHAR, 32)).append(",");
                createBakTableSql.append("\n\tBAK_REASON");
                createBakTableSql.append(" " + DatabaseHandlerDao.getInstance().getDataType(databaseType, ConstantVar.DataType.CHAR, 1000)).append("\n);");
                ow1.write(createBakTableSql.toString() + "\n");
            }
        }
        // 创建视图
        Map<String, PhysicalTableDefine> viewMap = (Map<String, PhysicalTableDefine>) map.get("View");
        if (MapUtils.isNotEmpty(viewMap)) {
            List<PhysicalTableDefine> physicalTableDefineList = new ArrayList<PhysicalTableDefine>();
            for (Iterator<String> it = viewMap.keySet().iterator(); it.hasNext();) {
                physicalTableDefineList.add(viewMap.get(it.next()));
            }
            Collections.sort(physicalTableDefineList);
            for (Iterator<PhysicalTableDefine> it = physicalTableDefineList.iterator(); it.hasNext();) {
                String viewName = it.next().getTableName().toUpperCase();
                String viewCreateSql = null;
                if (DatabaseHandlerDao.isOracle()) {
                    List list = DatabaseHandlerDao.getInstance().queryForList("select text from user_views where view_name='" + viewName + "'");
                    if (CollectionUtils.isNotEmpty(list)) {
                        viewCreateSql = list.get(0).toString();
                    }
                } else if (DatabaseHandlerDao.isSqlserver()) {
                    List list = DatabaseHandlerDao.getInstance().queryForList(
                            "select cm.text from syscomments cm inner join sysobjects o on o.id=cm.id where xtype ='v' and o.name='" + viewName + "'");
                    if (CollectionUtils.isNotEmpty(list)) {
                        viewCreateSql = list.get(0).toString();
                    }
                }
                if (StringUtil.isNotEmpty(viewCreateSql)) {
                    ow1.write("create or replace view " + viewName + " as "
                            + viewCreateSql.replaceAll("\n;", ";").replaceAll(",", ",\n").replaceAll(",\n'yyyy-MM-dd", ",'yyyy-MM-dd") + ";\n");
                }
            }
        }
        // 创建工作流相关表
        if (CollectionUtils.isNotEmpty(wfRelevantdataTableNameSet)) {
            List<Object[]> columnList = null;
            for (String wfRelevantdataTableName : wfRelevantdataTableNameSet) {
                if (DatabaseHandlerDao.isOracle()) {
                    columnList = DatabaseHandlerDao.getInstance().queryForList(
                            "select column_name,data_type,data_length from user_tab_columns where table_name='" + wfRelevantdataTableName
                                    + "' order by column_name");
                } else if (DatabaseHandlerDao.isSqlserver()) {
                    columnList = DatabaseHandlerDao.getInstance().queryForList(
                            "select [columns].name, [types].name, [columns].max_length from sys.tables as [tables]"
                                    + " INNER JOIN sys.columns AS [Columns] ON [Tables].object_id = [Columns].object_id"
                                    + " INNER JOIN sys.types AS [Types] ON [Columns].system_type_id = [Types].system_type_id"
                                    + " AND is_user_defined = 0 AND [Types].name <> 'sysname'"
                                    + " LEFT OUTER JOIN sys.extended_properties AS [Properties] ON [Properties].major_id = [Tables].object_id"
                                    + " AND [Properties].minor_id = [Columns].column_id AND [Properties].name = 'MS_Description' WHERE [Tables].name ='"
                                    + wfRelevantdataTableName + "' ORDER BY [Columns].column_id");
                }
                if (CollectionUtils.isNotEmpty(columnList)) {
                    createTableSql = new StringBuffer();
                    createTableSql.append("create table " + wfRelevantdataTableName + "(");
                    for (Object[] obj : columnList) {
                        createTableSql.append("\n\t" + String.valueOf(obj[0]));
                        String dataType = String.valueOf(obj[1]);
                        if ("int".equalsIgnoreCase(dataType) || "number".equalsIgnoreCase(dataType) || "decimal".equalsIgnoreCase(dataType)) {
                            createTableSql.append(" "
                                    + DatabaseHandlerDao.getInstance().getDataType(databaseType, ConstantVar.DataType.NUMBER,
                                            Integer.parseInt(String.valueOf(obj[2]))));
                        } else if ("nvarchar".equalsIgnoreCase(dataType) || "varchar2".equalsIgnoreCase(dataType)) {
                            createTableSql.append(" "
                                    + DatabaseHandlerDao.getInstance().getDataType(databaseType, ConstantVar.DataType.CHAR,
                                            Integer.parseInt(String.valueOf(obj[2]))));
                        } else if ("date".equalsIgnoreCase(dataType) || "datetime".equalsIgnoreCase(dataType) || "timestamp".equalsIgnoreCase(dataType)) {
                            createTableSql.append(" "
                                    + DatabaseHandlerDao.getInstance().getDataType(databaseType, ConstantVar.DataType.DATE,
                                            Integer.parseInt(String.valueOf(obj[2]))));
                        } else {
                            createTableSql.append(" "
                                    + DatabaseHandlerDao.getInstance().getDataType(databaseType, dataType, Integer.parseInt(String.valueOf(obj[2]))));
                        }
                        createTableSql.append(",");
                    }
                    createTableSql.deleteCharAt(createTableSql.length() - 1);
                    createTableSql.append("\n);");
                    ow1.write(createTableSql.toString() + "\n");
                }
            }
        }
        // 创建额外的自定义表脚本
        ReleaseUtil.getInstance().createOtherSelfDefineTableSql(ow1, map);
        ow1.close();
    }

    /**
     * 创建系统管理平台的相关sql
     */
    @SuppressWarnings("unchecked")
    private void createAuthDataSql(String rootMenuId, Map<String, Object> map, String path, SystemVersion systemVersion, Set<String> canUseResourceIdSet)
            throws UnsupportedEncodingException, FileNotFoundException, IOException {
        File authInitDataSqlFile = new File(path + "auth/2_auth_init_data.sql");
        OutputStreamWriter ow2 = new OutputStreamWriter(new FileOutputStream(authInitDataSqlFile), "UTF-8");
        // 生成系统管理平台中的信息，包括T_SYSTEM、T_RESOURCE、T_SYSTEM_RES、T_ROLE、T_SYSTEM_ROLE、T_ROLE_RES
        getService(ResourceService.class).syncToAuth(rootMenuId);
        // T_SYSTEM
        SystemInfo authSystem = (SystemInfo) map.get("Auth_System");
        ow2.write("insert into t_system(system_id,system_code,system_name,comments,showorder) values(" + authSystem.getId() + ",'"
                + StringUtil.null2empty(authSystem.getCode()) + "','" + StringUtil.null2empty(authSystem.getName()) + "','"
                + StringUtil.null2empty(authSystem.getComments()) + "'," + StringUtil.null2zero(authSystem.getShowOrder()) + ");" + "\n");
        ow2.write("\n");
        // T_RESOURCE
        List<Object[]> resList = (List<Object[]>) map.get("Auth_Resource");
        // 系统管理平台中可用的资源Ids
        Set<String> authCanUseResourceIdSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(resList)) {
            for (Object[] res : resList) {
                if ("1".equals(systemVersion.getIsDefault()) || canUseResourceIdSet.contains(StringUtil.null2empty(res[5]))) {
                    authCanUseResourceIdSet.add(StringUtil.null2empty(res[0]));
                    ow2.write("insert into t_resource(resource_id,parent_id,name,resource_type_name,comments,resourcekey,showorder) values("
                            + StringUtil.null2empty(res[0]) + "," + StringUtil.null2empty(res[1]) + ",'" + StringUtil.null2empty(res[2]) + "','"
                            + StringUtil.null2empty(res[3]) + "','" + StringUtil.null2empty(res[4]) + "','" + StringUtil.null2empty(res[5]) + "',"
                            + StringUtil.null2zero(res[6]) + ");" + "\n");
                }
            }
            ow2.write("\n");
        }
        // T_SYSTEM_RES
        List<Object[]> systemResList = (List<Object[]>) map.get("Auth_SystemRes");
        if (CollectionUtils.isNotEmpty(systemResList)) {
            for (Object[] systemRes : systemResList) {
                if (authCanUseResourceIdSet.contains(StringUtil.null2empty(systemRes[2]))) {
                    ow2.write("insert into t_system_res(system_resource_id,system_id,resource_id) values(" + StringUtil.null2empty(systemRes[0]) + ","
                            + StringUtil.null2empty(systemRes[1]) + "," + StringUtil.null2empty(systemRes[2]) + ");" + "\n");
                }
            }
            ow2.write("\n");
        }
        // T_ROLE
        List<Object[]> roleList = (List<Object[]>) map.get("Auth_Role");
        if (CollectionUtils.isNotEmpty(roleList)) {
            for (Object[] role : roleList) {
                ow2.write("insert into t_role(role_id,role_name,comments,rolekey) values(" + StringUtil.null2empty(role[0]) + ",'"
                        + StringUtil.null2empty(role[1]) + "','" + StringUtil.null2empty(role[2]) + "','" + StringUtil.null2empty(role[3]) + "');" + "\n");
            }
            ow2.write("\n");
        }
        // T_SYSTEM_ROLE
        List<Object[]> systemRoleList = (List<Object[]>) map.get("Auth_SystemRole");
        if (CollectionUtils.isNotEmpty(systemRoleList)) {
            for (Object[] systemRole : systemRoleList) {
                ow2.write("insert into t_system_role(system_role_id,system_id,role_id) values(" + StringUtil.null2empty(systemRole[0]) + ","
                        + StringUtil.null2empty(systemRole[1]) + "," + StringUtil.null2empty(systemRole[2]) + ");" + "\n");
            }
            ow2.write("\n");
        }
        // T_ROLE_RES
        List<Object[]> roleResList = (List<Object[]>) map.get("Auth_RoleRes");
        if (CollectionUtils.isNotEmpty(roleResList)) {
            for (Object[] roleRes : roleResList) {
                if (authCanUseResourceIdSet.contains(StringUtil.null2empty(roleRes[1]))) {
                    ow2.write("insert into t_role_res(role_id, resource_id) values(" + StringUtil.null2empty(roleRes[0]) + ","
                            + StringUtil.null2empty(roleRes[1]) + ");" + "\n");
                }
            }
            ow2.write("\n");
        }
        // 发布组织用户，包括T_ORG、T_USER、T_ORG_USER、T_ROLE_USER
        if ("1".equals(SystemParameterUtil.getInstance().getSystemParamValue("系统发布是否发布组织用户"))) {
            // T_ORG
            List<Object[]> orgList = (List<Object[]>) map.get("Auth_Org");
            if (CollectionUtils.isNotEmpty(orgList)) {
                for (Object[] org : orgList) {
                    if (AuthDatabaseUtil.columnExists("t_org", "relationorg_id")) {
                        ow2.write("insert into t_org(organize_id,parent_id,organize_name,organize_type_name,comments,showorder,abbreviation,organize_code,is_leaf,relationorg_id,status) values("
                                + StringUtil.null2empty(org[0])
                                + ","
                                + StringUtil.null2empty(org[1])
                                + ",'"
                                + StringUtil.null2empty(org[2])
                                + "','"
                                + StringUtil.null2empty(org[3])
                                + "','"
                                + StringUtil.null2empty(org[4])
                                + "',"
                                + StringUtil.null2zero(org[5])
                                + ",'"
                                + StringUtil.null2empty(org[6])
                                + "','"
                                + StringUtil.null2empty(org[7])
                                + "','"
                                + StringUtil.null2empty(org[8])
                                + "','"
                                + StringUtil.null2empty(org[9]) + "','" + StringUtil.null2empty(org[10]) + "');" + "\n");
                    } else {
                        ow2.write("insert into t_org(organize_id,parent_id,organize_name,organize_type_name,comments,showorder,abbreviation,organize_code) values("
                                + StringUtil.null2empty(org[0])
                                + ","
                                + StringUtil.null2empty(org[1])
                                + ",'"
                                + StringUtil.null2empty(org[2])
                                + "','"
                                + StringUtil.null2empty(org[3])
                                + "','"
                                + StringUtil.null2empty(org[4])
                                + "',"
                                + StringUtil.null2zero(org[5])
                                + ",'"
                                + StringUtil.null2empty(org[6]) + "','" + StringUtil.null2empty(org[7]) + "');" + "\n");
                    }
                }
                ow2.write("\n");
            }
            // T_USER
            List<Object[]> userList = (List<Object[]>) map.get("Auth_User");
            if (CollectionUtils.isNotEmpty(userList)) {
                for (Object[] user : userList) {
                    if (AuthDatabaseUtil.columnExists("t_user", "user_level")) {
                        ow2.write("insert into t_user(user_id,ic_no,login_name,password,user_name,flag_action,email,mobile,title,showorder,comments,station,status,job_no,allowclientnum,user_level) values("
                                + StringUtil.null2empty(user[0])
                                + ",'"
                                + StringUtil.null2empty(user[1])
                                + "','"
                                + StringUtil.null2empty(user[2])
                                + "','"
                                + StringUtil.null2empty(user[3])
                                + "','"
                                + StringUtil.null2empty(user[4])
                                + "','"
                                + StringUtil.null2empty(user[5])
                                + "','"
                                + StringUtil.null2empty(user[6])
                                + "','"
                                + StringUtil.null2empty(user[7])
                                + "','"
                                + StringUtil.null2empty(user[8])
                                + "','"
                                + StringUtil.null2empty(user[9])
                                + "',"
                                + StringUtil.null2zero(user[10])
                                + ",'"
                                + StringUtil.null2empty(user[11])
                                + "','"
                                + StringUtil.null2empty(user[12])
                                + "','"
                                + StringUtil.null2empty(user[13])
                                + "','"
                                + StringUtil.null2empty(user[14])
                                + "','"
                                + StringUtil.null2empty(user[15]) + "');" + "\n");
                    } else {
                        ow2.write("insert into t_user(user_id,ic_no,login_name,password,user_name,flag_action,email,mobile,title,showorder,comments,station,status,job_no,allowclientnum) values("
                                + StringUtil.null2empty(user[0])
                                + ",'"
                                + StringUtil.null2empty(user[1])
                                + "','"
                                + StringUtil.null2empty(user[2])
                                + "','"
                                + StringUtil.null2empty(user[3])
                                + "','"
                                + StringUtil.null2empty(user[4])
                                + "','"
                                + StringUtil.null2empty(user[5])
                                + "','"
                                + StringUtil.null2empty(user[6])
                                + "','"
                                + StringUtil.null2empty(user[7])
                                + "','"
                                + StringUtil.null2empty(user[8])
                                + "','"
                                + StringUtil.null2empty(user[9])
                                + "',"
                                + StringUtil.null2zero(user[10])
                                + ",'"
                                + StringUtil.null2empty(user[11])
                                + "','"
                                + StringUtil.null2empty(user[12])
                                + "','"
                                + StringUtil.null2empty(user[13])
                                + "','"
                                + StringUtil.null2empty(user[14])
                                + "');"
                                + "\n");
                    }
                }
                ow2.write("\n");
            }
            // T_ORG_USER
            List<Object[]> orgUserList = (List<Object[]>) map.get("Auth_OrgUser");
            if (CollectionUtils.isNotEmpty(orgUserList)) {
                for (Object[] orgUser : orgUserList) {
                    ow2.write("insert into t_org_user(orguserid,organize_id,user_id,usertype,user_showorder) values(" + StringUtil.null2empty(orgUser[0]) + ","
                            + StringUtil.null2empty(orgUser[1]) + "," + StringUtil.null2empty(orgUser[2]) + "," + StringUtil.null2zero(orgUser[3]) + ","
                            + StringUtil.null2zero(orgUser[4]) + ");" + "\n");
                }
                ow2.write("\n");
            }
            // T_ROLE_USER
            List<Object[]> roleUserList = (List<Object[]>) map.get("Auth_RoleUser");
            if (CollectionUtils.isNotEmpty(roleUserList)) {
                for (Object[] roleUser : roleUserList) {
                    if (("-1".equals(StringUtil.null2empty(roleUser[0])) && "1".equals(StringUtil.null2empty(roleUser[1])))
                            || ("1".equals(StringUtil.null2empty(roleUser[0])) && "1".equals(StringUtil.null2empty(roleUser[1])))) {
                        continue;
                    }
                    ow2.write("insert into t_role_user(role_id,user_id) values(" + StringUtil.null2empty(roleUser[0]) + ","
                            + StringUtil.null2empty(roleUser[1]) + ");" + "\n");
                }
                ow2.write("\n");
            }
        }
        // 修改T_ID_SEQUENCE
        List<Object[]> idSequenceList = (List<Object[]>) map.get("Auth_IdSequence");
        if (CollectionUtils.isNotEmpty(idSequenceList)) {
            for (Object[] idSequence : idSequenceList) {
                ow2.write("update t_id_sequence set maxval=" + idSequence[1] + " where id_name='" + idSequence[0] + "';" + "\n");
            }
            ow2.write("\n");
        }
        ow2.close();
    }

    /**
     * 获取系统参数分类
     * 
     * @param systemParameterCategoryList 系统参数分类List
     * @param resultList 返回结果List
     * @param parentId 父ID
     * @return List<SystemParameterCategory>
     */
    private List<SystemParameterCategory> getSystemParameterCategoryByParentId(List<SystemParameterCategory> systemParameterCategoryList,
            List<SystemParameterCategory> resultList, String parentId) {
        if (CollectionUtils.isNotEmpty(systemParameterCategoryList)) {
            for (SystemParameterCategory category : systemParameterCategoryList) {
                if (category.getParentId().equals(parentId)) {
                    resultList.add(category);
                    if (category.getHasChild()) {
                        getSystemParameterCategoryByParentId(systemParameterCategoryList, resultList, category.getId());
                    }
                }
            }
        }
        return resultList;
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
            return str.replaceAll("'", "''").replaceAll("&", "'||chr(38)||'").replaceAll("\r\n", "\n").replaceAll("\n", "'\n||chr(10)||'");
        } else {
            return str.replaceAll("'", "''");
        }
    }

    /**
     * 导出系统时的数据
     * 
     * @param rootMenuId 根菜单ID
     * @param menuList 菜单
     * @param menuIds 菜单Ids
     * @param componentVersionSet 该系统中用到的所有构件
     * @param systemVersionid 系统版本ID
     * @return Map<String, List>
     * @throws Exception
     */
    private Map<String, Object> getDatas(String rootMenuId, List<Menu> menuList, String menuIds, Set<ComponentVersion> componentVersionSet,
            String systemVersionid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 获取Menu
        menuList.addAll(getService(MenuService.class).getAllChildMenu("sys_0"));
        menuList.add(getService(MenuService.class).getByID("sys_0"));
        map.put("Menu", menuList);
        List<MenuSelfParam> menuSelfParamList = getService(MenuSelfParamService.class).getMenuSelfParams(menuIds);
        map.put("MenuSelfParam", menuSelfParamList);
        List<MenuInputParam> menuInputParamList = getService(MenuInputParamService.class).getMenuInputParams(menuIds);
        map.put("MenuInputParam", menuInputParamList);
        // 获取Construct、ConstructSelfParam、ConstructInputParam、ConstructFilter、ConstructFilterDetail
        List<Construct> constructList = new ArrayList<Construct>();
        List<ConstructFilter> constructFilterList = new ArrayList<ConstructFilter>();
        List<ConstructFilterDetail> constructFilterDetailList = new ArrayList<ConstructFilterDetail>();
        List<String> assembleComponentVersionIdList = new ArrayList<String>();
        Set<String> cvIdSet = new HashSet<String>();
        StringBuilder cvIdSb = new StringBuilder();
        StringBuilder assembleCvIdSb = new StringBuilder();
        for (ComponentVersion cv : componentVersionSet) {
            cvIdSet.add(cv.getId());
            cvIdSb.append(cv.getId()).append(",");
            if (ConstantVar.Component.Type.ASSEMBLY.equals(cv.getComponent().getType())) {
                assembleCvIdSb.append(cv.getId()).append(",");
                assembleComponentVersionIdList.add(cv.getId());
            }
        }
        if (cvIdSb.length() > 0) {
            cvIdSb.deleteCharAt(cvIdSb.length() - 1);
        }
        if (assembleCvIdSb.length() > 0) {
            assembleCvIdSb.deleteCharAt(assembleCvIdSb.length() - 1);
            List<String> assembleCvIdsList = CommonUtil.parseSqlIn(assembleCvIdSb.toString());
            for (String assembleCvIds : assembleCvIdsList) {
                constructList.addAll(getService(ConstructService.class).getByAssembleComponentVersionIds(assembleCvIds));
                constructFilterList.addAll(getService(ConstructFilterService.class).getByTopComVersionIds(assembleCvIds));
                constructFilterDetailList.addAll(getService(ConstructFilterDetailService.class).getByTopComVersionIds(assembleCvIds));
            }
        }
        map.put("Construct", constructList);
        List<ConstructSelfParam> contructSelfParamList = new ArrayList<ConstructSelfParam>();
        List<ConstructInputParam> constructInputParamList = new ArrayList<ConstructInputParam>();
        if (CollectionUtils.isNotEmpty(constructList)) {
            StringBuilder constructIdSb = new StringBuilder();
            for (Construct construct : constructList) {
                constructIdSb.append(construct.getId()).append(",");
            }
            if (constructIdSb.length() > 0) {
                constructIdSb.deleteCharAt(constructIdSb.length() - 1);
                List<String> constructIdsList = CommonUtil.parseSqlIn(constructIdSb.toString());
                for (String constructIds : constructIdsList) {
                    contructSelfParamList.addAll(getService(ConstructSelfParamService.class).getByConstructIds(constructIds));
                    constructInputParamList.addAll(getService(ConstructInputParamService.class).getByConstructIds(constructIds));
                }
            }
        }
        map.put("ConstructFilter", constructFilterList);
        map.put("ConstructFilterDetail", constructFilterDetailList);
        map.put("ConstructSelfParam", contructSelfParamList);
        map.put("ConstructInputParam", constructInputParamList);
        // 获取ConstructDetail、ConstructDetailSelfParam、ConstructFunction、ConstructCallback
        List<ConstructDetail> constructDetailList = new ArrayList<ConstructDetail>(getService(ConstructDetailService.class).getByMenuIds(menuIds));
        map.put("ConstructDetail", constructDetailList);
        List<ConstructDetailSelfParam> constructDetailSelfParamList = new ArrayList<ConstructDetailSelfParam>();
        List<ConstructFunction> constructFunctionList = new ArrayList<ConstructFunction>();
        List<ConstructCallback> constructCallbackList = new ArrayList<ConstructCallback>();
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            Set<ConstructDetail> commonConstructDetailSet = new HashSet<ConstructDetail>();
            Set<String> commonRZButtonSet = new HashSet<String>();
            for (ConstructDetail constructDetail : constructDetailList) {
                if ("1".equals(constructDetail.getIsCommonReserveZone())) {
                    commonRZButtonSet.add(constructDetail.getReserveZoneId() + "_" + constructDetail.getButtonCode());
                }
            }
            // 添加公用预留区与按钮预设
            if (CollectionUtils.isNotEmpty(commonRZButtonSet)) {
                Map<String, Set<String>> commonRZButtonMap = new HashMap<String, Set<String>>();
                Set<String> buttonCodeSet = null;
                for (String commonRZButton : commonRZButtonSet) {
                    String[] strs = commonRZButton.split("_");
                    buttonCodeSet = commonRZButtonMap.get(strs[0]);
                    if (buttonCodeSet == null) {
                        buttonCodeSet = new HashSet<String>();
                        commonRZButtonMap.put(strs[0], buttonCodeSet);
                    }
                    buttonCodeSet.add(strs[1]);
                }
                for (Iterator<String> keyIt = commonRZButtonMap.keySet().iterator(); keyIt.hasNext();) {
                    String reserveZoneId = keyIt.next();
                    buttonCodeSet = commonRZButtonMap.get(reserveZoneId);
                    List<ConstructDetail> tempCommonCDList = getService(ConstructDetailService.class).getByReserveZoneIdOfCommonBinding(reserveZoneId);
                    if (CollectionUtils.isNotEmpty(tempCommonCDList)) {
                        for (ConstructDetail tempCommonCD : tempCommonCDList) {
                            if (tempCommonCD != null
                                    && (buttonCodeSet.contains(tempCommonCD.getButtonCode()) || cvIdSet.contains(tempCommonCD.getComponentVersionId()))) {
                                commonConstructDetailSet.add(tempCommonCD);
                            }
                        }
                    }
                }
            }
            constructDetailList.addAll(commonConstructDetailSet);
            StringBuilder cdIdSb = new StringBuilder();
            for (ConstructDetail constructDetail : constructDetailList) {
                cdIdSb.append(constructDetail.getId()).append(",");
            }
            if (cdIdSb.length() > 0) {
                cdIdSb.deleteCharAt(cdIdSb.length() - 1);
            }
            List<String> cdIdsList = CommonUtil.parseSqlIn(cdIdSb.toString());
            for (String cdIds : cdIdsList) {
                constructDetailSelfParamList.addAll(getService(ConstructDetailSelfParamService.class).getByConstructDetailIds(cdIds));
                constructFunctionList.addAll(getService(ConstructFunctionService.class).getByConstructDetailIds(cdIds));
                constructCallbackList.addAll(getService(ConstructCallbackService.class).getByConstructDetailIds(cdIds));
            }
        }
        map.put("ConstructDetailSelfParam", constructDetailSelfParamList);
        map.put("ConstructFunction", constructFunctionList);
        map.put("ConstructCallback", constructCallbackList);
        // 获取构件相关数据
        List<ComponentVersion> componentVersionList = new ArrayList<ComponentVersion>(componentVersionSet);
        List<ComponentTable> componentTableList = getComponentRelateData(rootMenuId, componentVersionSet, map, cvIdSb, componentVersionList);
        // 获取Database、CodeType、Code、SystemParameterCategory、SystemParameter、TableLabel、ColumnLabel、TypeLabel
        map.put("Database", getService(DatabaseService.class).findAll());
        List<CodeType> codeTypeList = getService(CodeTypeService.class).findAll();
        // 根据选择的系统过滤
        for (Iterator<CodeType> it = codeTypeList.iterator(); it.hasNext();) {
            CodeType codeType = it.next();
            if (StringUtils.isNotEmpty(codeType.getSystemId())) {
                if (!codeType.getSystemId().equals(rootMenuId)) {
                    it.remove();
                }
            }
        }
        map.put("CodeType", codeTypeList);
        List<Code> codeList = getService(CodeService.class).findAll();
        // 根据codeType过滤
        for (Iterator<Code> it = codeList.iterator(); it.hasNext();) {
            boolean move = true;
            Code code = it.next();
            for (CodeType t : codeTypeList) {
                // 防止脏数据
                if (code == null || code.getCodeTypeCode() == null || t == null) {
                    continue;
                }
                if (code.getCodeTypeCode().equals(t.getCode())) {
                    move = false;
                    break;
                }
            }
            if (move)
                it.remove();
        }
        map.put("Code", codeList);
        map.put("BusinessCode", getService(BusinessCodeService.class).findAll());
        map.put("SystemParameterCategory", getService(SystemParameterCategoryService.class).findAll());
        map.put("SystemParameter", getService(SystemParameterService.class).findAll());
        map.put("ColumnLabelCategory", getService(ColumnLabelCategoryService.class).findAll());
        map.put("ColumnLabel", getService(ColumnLabelService.class).findAll());
        map.put("TypeLabel", getService(TypeLabelService.class).findAll());
        map.put("Timing", getService(TimingService.class).findAll());
        // 获取自定义构件相关数据
        getSelfDefineRelateData(menuIds, map, assembleComponentVersionIdList, componentTableList, componentVersionList);
        // 获取系统版本、资源相关数据
        getVersionResourceRelateData(rootMenuId, systemVersionid, map);
        // 获取系统管理平台中的信息，包括T_SYSTEM、T_RESOURCE、T_SYSTEM_RES、T_ROLE、T_SYSTEM_ROLE、T_ROLE_RES、T_ID_SEQUENCE
        // 先同步一次系统
        getService(ResourceService.class).syncToAuth(rootMenuId);
        getAuthRelateData(rootMenuId, map);
        getAuthorityRelateData(map);
        return map;
    }

    /**
     * 获取构件相关数据
     */
    private List<ComponentTable> getComponentRelateData(String rootMenuId, Set<ComponentVersion> componentVersionSet, Map<String, Object> map,
            StringBuilder cvIdSb, List<ComponentVersion> componentVersionList) {
        // 获取ComponentArea、Component、ComponentVersion、ComponentClass、ComponentJar、ComponentSystemParameter、ComponentSystemParameterRelation、
        // ComponentSelfParam、ComponentInputParam、ComponentOutputParam、ComponentReserveZone、ComponentFunction、ComponentFunctionData、
        // ComponentCallback、ComponentCallbackParam、ComponentButton、ComponentTableColumnRelation、ComponentTable、ComponentColumn、CommonComponentRelation
        List<Component> componentList = new ArrayList<Component>();
        Map<String, Component> componentMap = new HashMap<String, Component>();
        List<ComponentClass> componentClassList = new ArrayList<ComponentClass>();
        List<ComponentJar> componentJarList = new ArrayList<ComponentJar>();
        List<ComponentSystemParameter> componentSystemParameterList = new ArrayList<ComponentSystemParameter>();
        List<ComponentSystemParameterRelation> componentSystemParameterRelationList = new ArrayList<ComponentSystemParameterRelation>();
        List<ComponentSelfParam> componentSelfParamList = new ArrayList<ComponentSelfParam>();
        List<ComponentInputParam> componentInputParamList = new ArrayList<ComponentInputParam>();
        List<ComponentOutputParam> componentOutputParamList = new ArrayList<ComponentOutputParam>();
        List<ComponentReserveZone> componentReserveZoneList = new ArrayList<ComponentReserveZone>();
        List<ComponentFunction> componentFunctionList = new ArrayList<ComponentFunction>();
        List<ComponentFunctionData> componentFunctionDataList = new ArrayList<ComponentFunctionData>();
        List<ComponentCallback> componentCallbackList = new ArrayList<ComponentCallback>();
        List<ComponentCallbackParam> componentCallbackParamList = new ArrayList<ComponentCallbackParam>();
        List<ComponentButton> componentButtonList = new ArrayList<ComponentButton>();
        List<ComponentTableColumnRelation> componentTableColumnRelationList = new ArrayList<ComponentTableColumnRelation>();
        List<ComponentTable> componentTableList = new ArrayList<ComponentTable>();
        List<ComponentColumn> componentColumnList = new ArrayList<ComponentColumn>();
        List<CommonComponentRelation> commonComponentRelationList = new ArrayList<CommonComponentRelation>();
        Set<String> componentTableIdSet = new HashSet<String>();
        Set<String> componentColumnIdSet = new HashSet<String>();
        Set<String> componentAreaIdSet = new HashSet<String>();
        Set<String> componentAssembleAreaIdSet = new HashSet<String>();
        for (ComponentVersion componentVersion : componentVersionSet) {
            Component component = componentVersion.getComponent();
            componentAreaIdSet.add(componentVersion.getAreaId());
            if (ConstantVar.Component.Type.ASSEMBLY.equals(component.getType())) {
                componentAssembleAreaIdSet.add(componentVersion.getAssembleAreaId());
            }
            componentMap.put(component.getId(), component);
            if (ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                Set<ComponentVersion> tempComponentVersionSet = getService(CommonComponentRelationService.class).getAllComponentVersion(
                        componentVersion.getId());
                if (CollectionUtils.isNotEmpty(tempComponentVersionSet)) {
                    for (ComponentVersion tempComponentVersion : tempComponentVersionSet) {
                        if (componentVersionSet.contains(tempComponentVersion)) {
                            CommonComponentRelation commonComponentRelation = getService(CommonComponentRelationService.class).getCommonComponentRelation(
                                    tempComponentVersion.getId(), componentVersion.getId());
                            if (commonComponentRelation != null) {
                                commonComponentRelationList.add(commonComponentRelation);
                            }
                        }
                    }
                }
            }
        }
        // 获取基础构件分类树
        Map<String, ComponentArea> componentAreaMap = new HashMap<String, ComponentArea>();
        if (CollectionUtils.isNotEmpty(componentAreaIdSet)) {
            ComponentArea componentArea = null;
            for (String componentAreaId : componentAreaIdSet) {
                if (componentAreaMap.get(componentAreaId) == null) {
                    componentArea = getService(ComponentAreaService.class).getByID(componentAreaId);
                    if (componentArea != null) {
                        componentAreaMap.put(componentArea.getId(), componentArea);
                        // 构件分类树父节点
                        String parentId = componentArea.getParentId();
                        while (!"-1".equals(parentId)) {
                            if (componentAreaMap.containsKey(parentId)) {
                                break;
                            }
                            componentArea = getService(ComponentAreaService.class).getByID(parentId);
                            if (componentArea != null) {
                                componentAreaMap.put(componentArea.getId(), componentArea);
                                parentId = componentArea.getParentId();
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        List<ComponentArea> componentAreaList = new ArrayList<ComponentArea>(componentAreaMap.values());
        if (CollectionUtils.isNotEmpty(componentAreaIdSet) && !componentAreaIdSet.contains("1")) {
            ComponentArea componentArea = getService(ComponentAreaService.class).getByID("1");
            if (componentArea != null) {
                componentAreaList.add(componentArea);
            }
        }
        map.put("ComponentArea", componentAreaList);
        // 获取组合构件分类树
        Map<String, ComponentAssembleArea> componentAssembleAreaMap = new HashMap<String, ComponentAssembleArea>();
        if (CollectionUtils.isNotEmpty(componentAssembleAreaIdSet)) {
            ComponentAssembleArea componentAssembleArea = null;
            for (String componentAssembleAreaId : componentAssembleAreaIdSet) {
                if (componentAssembleAreaMap.get(componentAssembleAreaId) == null) {
                    componentAssembleArea = getService(ComponentAssembleAreaService.class).getByID(componentAssembleAreaId);
                    if (componentAssembleArea != null) {
                        componentAssembleAreaMap.put(componentAssembleArea.getId(), componentAssembleArea);
                        // 构件分类树父节点
                        String parentId = componentAssembleArea.getParentId();
                        while (!"-1".equals(parentId)) {
                            if (componentAssembleAreaMap.containsKey(parentId)) {
                                break;
                            }
                            componentAssembleArea = getService(ComponentAssembleAreaService.class).getByID(parentId);
                            if (componentAssembleArea != null) {
                                componentAssembleAreaMap.put(componentAssembleArea.getId(), componentAssembleArea);
                                parentId = componentAssembleArea.getParentId();
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        List<ComponentAssembleArea> componentAssembleAreaList = new ArrayList<ComponentAssembleArea>(componentAssembleAreaMap.values());
        map.put("ComponentAssembleArea", componentAssembleAreaList);
        List<String> cvIdsList = CommonUtil.parseSqlIn(cvIdSb.toString());
        for (String cvIds : cvIdsList) {
            componentClassList.addAll(getService(ComponentClassService.class).getByComponentVersionIds(cvIds));
            componentJarList.addAll(getService(ComponentJarService.class).getByComponentVersionIds(cvIds));
            componentSystemParameterList.addAll(getService(ComponentSystemParameterService.class).getByComponentVersionIds(cvIds));
            componentSystemParameterRelationList.addAll(getService(ComponentSystemParameterRelationService.class).getByComponentVersionIds(cvIds));
            componentSelfParamList.addAll(getService(ComponentSelfParamService.class).getByComponentVersionIds(cvIds));
            componentInputParamList.addAll(getService(ComponentInputParamService.class).getByComponentVersionIds(cvIds));
            componentOutputParamList.addAll(getService(ComponentOutputParamService.class).getByComponentVersionIds(cvIds));
            componentReserveZoneList.addAll(getService(ComponentReserveZoneService.class).getByComponentVersionIds(cvIds));
            componentFunctionList.addAll(getService(ComponentFunctionService.class).getByComponentVersionIds(cvIds));
            componentCallbackList.addAll(getService(ComponentCallbackService.class).getByComponentVersionIds(cvIds));
            componentButtonList.addAll(getService(ComponentButtonService.class).getByComponentVersionIds(cvIds));
            componentTableColumnRelationList.addAll(getService(ComponentTableColumnRelationService.class).getByComponentVersionIds(cvIds));
        }
        componentFunctionList.addAll(getService(ComponentFunctionService.class).getCommonFunctions());
        if (CollectionUtils.isNotEmpty(componentFunctionList)) {
            StringBuilder fIdSb = new StringBuilder();
            for (ComponentFunction componentFunction : componentFunctionList) {
                fIdSb.append(componentFunction.getId()).append(",");
            }
            if (fIdSb.length() > 0) {
                fIdSb.deleteCharAt(fIdSb.length() - 1);
                List<String> fIdsList = CommonUtil.parseSqlIn(fIdSb.toString());
                for (String fIds : fIdsList) {
                    componentFunctionDataList.addAll(getService(ComponentFunctionDataService.class).getByFunctionIds(fIds));
                }
            }
        }
        componentCallbackList.addAll(getService(ComponentCallbackService.class).getCommonCallbacks());
        if (CollectionUtils.isNotEmpty(componentCallbackList)) {
            StringBuilder cIdSb = new StringBuilder();
            for (ComponentCallback componentCallback : componentCallbackList) {
                cIdSb.append(componentCallback.getId()).append(",");
            }
            if (cIdSb.length() > 0) {
                cIdSb.deleteCharAt(cIdSb.length() - 1);
                List<String> cIdsList = CommonUtil.parseSqlIn(cIdSb.toString());
                for (String cIds : cIdsList) {
                    componentCallbackParamList.addAll(getService(ComponentCallbackParamService.class).getByCallbackIds(cIds));
                }
            }
        }
        componentInputParamList.addAll(getService(ComponentInputParamService.class).getCommonInputParams());
        if (CollectionUtils.isNotEmpty(componentTableColumnRelationList)) {
            for (ComponentTableColumnRelation relation : componentTableColumnRelationList) {
                componentTableIdSet.add(relation.getTableId());
                if (StringUtil.isNotEmpty(relation.getColumnId())) {
                    componentColumnIdSet.add(relation.getColumnId());
                }
            }
        }
        componentList.addAll(componentMap.values());
        componentReserveZoneList.addAll(getService(ComponentReserveZoneService.class).getAllCommonReserveZone());
        map.put("Component", componentList);
        map.put("ComponentVersion", componentVersionList);
        map.put("ComponentClass", componentClassList);
        map.put("ComponentJar", componentJarList);
        map.put("ComponentSystemParameter", componentSystemParameterList);
        map.put("ComponentSystemParameterRelation", componentSystemParameterRelationList);
        map.put("ComponentSelfParam", componentSelfParamList);
        map.put("ComponentInputParam", componentInputParamList);
        map.put("ComponentOutputParam", componentOutputParamList);
        map.put("ComponentReserveZone", componentReserveZoneList);
        map.put("ComponentFunction", componentFunctionList);
        map.put("ComponentFunctionData", componentFunctionDataList);
        map.put("ComponentCallback", componentCallbackList);
        map.put("ComponentCallbackParam", componentCallbackParamList);
        map.put("ComponentButton", componentButtonList);
        map.put("ComponentTableColumnRelation", componentTableColumnRelationList);
        if (CollectionUtils.isNotEmpty(componentTableIdSet)) {
            StringBuilder tableIdSb = new StringBuilder();
            for (String componentTableId : componentTableIdSet) {
                tableIdSb.append(componentTableId).append(",");
            }
            if (tableIdSb.length() > 0) {
                tableIdSb.deleteCharAt(tableIdSb.length() - 1);
                List<String> tableIdsList = CommonUtil.parseSqlIn(tableIdSb.toString());
                for (String tableIds : tableIdsList) {
                    componentTableList.addAll(getService(ComponentTableService.class).getByIds(tableIds));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(componentColumnIdSet)) {
            StringBuilder columnIdSb = new StringBuilder();
            for (String componentColumnId : componentColumnIdSet) {
                columnIdSb.append(componentColumnId).append(",");
            }
            if (columnIdSb.length() > 0) {
                columnIdSb.deleteCharAt(columnIdSb.length() - 1);
                List<String> columnIdsList = CommonUtil.parseSqlIn(columnIdSb.toString());
                for (String columnIds : columnIdsList) {
                    componentColumnList.addAll(getService(ComponentColumnService.class).getByIds(columnIds));
                }
            }
        }
        map.put("ComponentTable", componentTableList);
        map.put("ComponentColumn", componentColumnList);
        map.put("CommonComponentRelation", commonComponentRelationList);
        // 整理系统中绑定的额外构件
        List<SystemComponentVersion> systemComponentVersionList = getService(SystemComponentVersionService.class).getByRootMenuId(rootMenuId);
        map.put("SystemComponentVersion", systemComponentVersionList);
        return componentTableList;
    }

    /**
     * 获取自定义构件相关数据
     */
    @SuppressWarnings("unchecked")
    private void getSelfDefineRelateData(String menuIds, Map<String, Object> map, List<String> assembleComponentVersionIdList,
            List<ComponentTable> componentTableList, List<ComponentVersion> componentVersionList) {
        List<String> moduleIdList = new ArrayList<String>();
        // 存放所以表分类树预置分类节点
        map.put("TableClassification", getService(TableClassificationService.class).findAll());
        // 存放所有的逻辑表分类
        List<LogicClassification> logicClassificationList = getService(LogicClassificationService.class).findAll();
        map.put("LogicClassification", logicClassificationList);
        // 存放所有模块所关联到的物理表
        Map<String, PhysicalTableDefine> physicalTableMap = new HashMap<String, PhysicalTableDefine>();
        if (CollectionUtils.isNotEmpty(componentTableList)) {
            StringBuilder componentTableNameSb = new StringBuilder();
            for (ComponentTable componentTable : componentTableList) {
                if (ConstantVar.Judgment.YES.equals(componentTable.getIsSelfdefine())) {
                    componentTableNameSb.append(componentTable.getName()).append(",");
                }
            }
            if (componentTableNameSb.length() > 0) {
                componentTableNameSb.deleteCharAt(componentTableNameSb.length() - 1);
                List<String> componentTableNamesList = CommonUtil.parseSqlIn(componentTableNameSb.toString());
                List<PhysicalTableDefine> componentTableDefineList = new ArrayList<PhysicalTableDefine>();
                for (String componentTableNames : componentTableNamesList) {
                    componentTableDefineList.addAll(getService(PhysicalTableDefineService.class).getByTableNames(componentTableNames.toString()));
                }
                if (CollectionUtils.isNotEmpty(componentTableDefineList)) {
                    for (PhysicalTableDefine tableDefine : componentTableDefineList) {
                        if (tableDefine != null) {
                            physicalTableMap.put(tableDefine.getId(), tableDefine);
                        }
                    }
                }
            }
        }
        // 整理模块、树和工作流
        Set<WorkflowDefine> workflowDefineSet = new HashSet<WorkflowDefine>();
        Map<String, Map<String, PhysicalTableDefine>> moduleTableRelationMap = getModuleAndPhysicalTableDefine(componentVersionList, map, moduleIdList,
                physicalTableMap, workflowDefineSet);
        List<Module> moduleList = (List<Module>) map.get("Module");
        Set<String> logicGroupCodeSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(moduleList)) {
            for (Module module : moduleList) {
                if (ConstantVar.Component.Type.LOGIC_TABLE.equals(module.getType())) {
                    logicGroupCodeSet.add(module.getLogicTableGroupCode());
                }
            }
        }
        Set<LogicTableDefine> logicTableDefineSet = new HashSet<LogicTableDefine>();
        List<LogicGroupDefine> logicGroupDefineList = getService(LogicGroupDefineService.class).findAll();
        if (CollectionUtils.isNotEmpty(logicGroupDefineList)) {
            LogicGroupDefine logicGroupDefine = null;
            for (Iterator<LogicGroupDefine> iterator = logicGroupDefineList.iterator(); iterator.hasNext();) {
                logicGroupDefine = iterator.next();
                if (!logicGroupCodeSet.contains(logicGroupDefine.getCode())) {
                    iterator.remove();
                } else {
                    logicTableDefineSet.addAll(getService(LogicTableDefineService.class).getByLogicTableGroupCode(logicGroupDefine.getCode()));
                }
            }
        }
        List<LogicGroupRelation> logicGroupRelationList = getService(LogicGroupRelationService.class).findAll();
        if (CollectionUtils.isNotEmpty(logicGroupRelationList)) {
            for (Iterator<LogicGroupRelation> iterator = logicGroupRelationList.iterator(); iterator.hasNext();) {
                if (!logicGroupCodeSet.contains(iterator.next().getGroupCode())) {
                    iterator.remove();
                }
            }
        }
        List<LogicTableRelation> logicTableRelationList = getService(LogicTableRelationService.class).findAll();
        if (CollectionUtils.isNotEmpty(logicTableRelationList)) {
            for (Iterator<LogicTableRelation> iterator = logicTableRelationList.iterator(); iterator.hasNext();) {
                if (!logicGroupCodeSet.contains(iterator.next().getGroupCode())) {
                    iterator.remove();
                }
            }
        }
        // 存放所有的逻辑表组
        map.put("LogicGroupDefine", logicGroupDefineList);
        // 存放所有的逻辑表组
        map.put("LogicGroupRelation", logicGroupRelationList);
        // 存放所有的逻辑表组中的逻辑表之间的关系
        map.put("LogicTableRelation", logicTableRelationList);
        // 存放所有的逻辑表
        // 添加工作流相关的逻辑表
        logicTableDefineSet.add(getService(LogicTableDefineService.class).getByCode("CONFIRM_OPINION"));
        logicTableDefineSet.add(getService(LogicTableDefineService.class).getByCode("ASSIST_OPINION"));
        // 添加电子全文表
        LogicTableDefine document = getService(LogicTableDefineService.class).getByCode("DOCUMENT");
        if (document != null) {
            logicTableDefineSet.add(document);
        }
        List<LogicTableDefine> logicTableDefineList = new ArrayList<LogicTableDefine>(logicTableDefineSet);
        map.put("LogicTableDefine", logicTableDefineList);
        // 整理工作流
        getWorkflows(map, workflowDefineSet, physicalTableMap);
        // 应用定义
        getApp(assembleComponentVersionIdList, menuIds, map, physicalTableMap);
        Map<String, List<ColumnDefine>> tableColumnMap = new HashMap<String, List<ColumnDefine>>();
        Set<PhysicalTableDefine> physicalTableDefineSet = new HashSet<PhysicalTableDefine>(physicalTableMap.values());
        List<ColumnDefine> columnDefineList = new ArrayList<ColumnDefine>();
        Map<String, TableRelation> tableRelationMap = new HashMap<String, TableRelation>();
        List<ColumnRelation> columnRelationList = new ArrayList<ColumnRelation>();
        List<ColumnSplice> columnSpliceList = new ArrayList<ColumnSplice>();
        List<ColumnSplit> columnSplitList = new ArrayList<ColumnSplit>();
        List<ColumnBusiness> columnBusinessList = new ArrayList<ColumnBusiness>();
        List<ColumnOperation> columnOperationList = new ArrayList<ColumnOperation>();
        // 获取基础表字段
        List<ColumnDefine> commonColumnDefineList = getService(ColumnDefineService.class).findByTableId(ColumnDefine.COMMON_TABLE_ID);
        if (CollectionUtils.isNotEmpty(commonColumnDefineList)) {
            columnDefineList.addAll(commonColumnDefineList);
        }
        if (CollectionUtils.isNotEmpty(logicTableDefineList)) {
            LogicTableDefine logicTableDefine = null;
            for (Iterator<LogicTableDefine> iterator = logicTableDefineList.iterator(); iterator.hasNext();) {
                logicTableDefine = iterator.next();
                if (logicTableDefine == null) {
                    iterator.remove();
                    continue;
                }
                // 整理字段
                getLogicTableColumns(columnDefineList, logicTableDefine);
            }
        }
        // 附件表
        Set<PhysicalTableDefine> documentPhyTableDefineSet = new HashSet<PhysicalTableDefine>();
        if (CollectionUtils.isNotEmpty(physicalTableDefineSet)) {
            for (PhysicalTableDefine physicalTableDefine : physicalTableDefineSet) {
                // 整理字段
                getPhysicalTableColumns(columnDefineList, physicalTableDefine, tableColumnMap);
                // 整理表关系
                getTableRelations(physicalTableMap, tableRelationMap, physicalTableDefine, documentPhyTableDefineSet);
                // 整理字段关联
                getColumnRelations(physicalTableMap, columnRelationList, columnSpliceList, columnSplitList, columnBusinessList, columnOperationList,
                        physicalTableDefine);
            }
        }
        // 整理报表
        List<ReportTable> reportTableList = getReports(map, moduleTableRelationMap);
        // 报表中使用的表也需要导出，视图不导出
        if (CollectionUtils.isNotEmpty(reportTableList)) {
            for (ReportTable reportTable : reportTableList) {
                if (!reportTable.getTableId().equals(reportTable.getTableName())) {
                    if (!physicalTableMap.containsKey(reportTable.getTableId())) {
                        PhysicalTableDefine physicalTableDefine = getService(PhysicalTableDefineService.class).getByID(reportTable.getTableId());
                        physicalTableDefineSet.add(physicalTableDefine);
                        physicalTableMap.put(physicalTableDefine.getId(), physicalTableDefine);
                        // 整理字段
                        getPhysicalTableColumns(columnDefineList, physicalTableDefine, tableColumnMap);
                        // 整理表关系
                        getTableRelations(physicalTableMap, tableRelationMap, physicalTableDefine, documentPhyTableDefineSet);
                        // 整理字段关联
                        getColumnRelations(physicalTableMap, columnRelationList, columnSpliceList, columnSplitList, columnBusinessList, columnOperationList,
                                physicalTableDefine);
                    }
                }
            }
        }
        List<TableRelation> tableRelationList = new ArrayList<TableRelation>(tableRelationMap.values());
        // ColumnDefine、TableRelation、ColumnRelation、ColumnSplice、ColumnSplit、ColumnBusiness、ColumnOperation、TableDefine、TableColumn、SystemComponentVersion
        map.put("TableRelation", tableRelationList);
        map.put("ColumnRelation", columnRelationList);
        map.put("ColumnSplice", columnSpliceList);
        map.put("ColumnSplit", columnSplitList);
        map.put("ColumnBusiness", columnBusinessList);
        map.put("ColumnOperation", columnOperationList);
        // 整理视图
        Map<String, PhysicalTableDefine> viewMap = getViews(tableRelationList, physicalTableDefineSet, columnDefineList);
        map.put("View", viewMap);
        if (CollectionUtils.isNotEmpty(documentPhyTableDefineSet)) {
            physicalTableDefineSet.addAll(documentPhyTableDefineSet);
            for (PhysicalTableDefine documentTable : documentPhyTableDefineSet) {
                // 整理字段
                getPhysicalTableColumns(columnDefineList, documentTable, tableColumnMap);
            }
        }
        map.put("PhysicalTableDefine", physicalTableDefineSet);
        map.put("ColumnDefine", columnDefineList);
        map.put("TableColumn", tableColumnMap);
        // 存放所以表分类树节点
        Map<String, TableTree> tableTreeMap = new HashMap<String, TableTree>();
        if (CollectionUtils.isNotEmpty(physicalTableDefineSet)) {
            String tableTreeId = null;
            TableTree tableTree = null;
            for (PhysicalTableDefine physicalTableDefine : physicalTableDefineSet) {
                tableTreeId = physicalTableDefine.getTableTreeId();
                while (!tableTreeId.startsWith("-")) {
                    if (tableTreeMap.get(tableTreeId) == null) {
                        tableTree = getService(TableTreeService.class).getByID(tableTreeId);
                        if (tableTree != null) {
                            tableTreeMap.put(tableTreeId, tableTree);
                            tableTreeId = tableTree.getParentId();
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        map.put("TableTree", new ArrayList<TableTree>(tableTreeMap.values()));
    }

    /**
     * 获取系统版本、资源相关数据
     */
    private void getVersionResourceRelateData(String rootMenuId, String systemVersionid, Map<String, Object> map) {
        // 系统版本、版本资源
        SystemVersion systemVersion = getService(SystemVersionService.class).getByID(systemVersionid);
        map.put("SystemVersion", systemVersion);
        List<SystemVersionResource> sysVerResourceList = getService(SystemVersionResourceService.class).getBySystemVersionId(systemVersionid);
        map.put("SystemVersionResource", sysVerResourceList);
        // 资源、资源按钮
        List<Resource> resourceList = getService(ResourceService.class).getBySystemId(rootMenuId);
        resourceList.add(getService(ResourceService.class).getByTargetId(rootMenuId));
        map.put("Resource", resourceList);
        List<ResourceButton> resourceButtonList = getService(ResourceButtonService.class).getBySystemId(rootMenuId);
        map.put("ResourceButton", resourceButtonList);
    }

    /**
     * 获取系统管理平台要发布的数据
     */
    @SuppressWarnings("unchecked")
    private void getAuthRelateData(String rootMenuId, Map<String, Object> map) throws Exception {
        // T_SYSTEM
        SystemFacade systemFacade = SystemFacadeFactory.newInstance().createSystemFacade();
        SystemInfo authSystem = systemFacade.findSystemsByCode(rootMenuId);
        if (authSystem == null) {
            return;
        }
        map.put("Auth_System", authSystem);
        // T_RESOURCE
        String resSql = "select r.resource_id,r.parent_id,r.name,r.resource_type_name,r.comments,r.resourcekey,r.showorder from t_resource r,t_system_res sr where r.resource_id=sr.resource_id and sr.system_id="
                + authSystem.getId() + "";
        List<Object[]> resList = AuthDatabaseUtil.queryForList(resSql);
        map.put("Auth_Resource", resList);
        // T_SYSTEM_RES
        String systemResSql = "select system_resource_id,system_id,resource_id from t_system_res where system_id='" + authSystem.getId() + "'";
        List<Object[]> systemResList = AuthDatabaseUtil.queryForList(systemResSql);
        map.put("Auth_SystemRes", systemResList);
        // T_ROLE
        String roleSql = "select r.role_id,r.role_name,r.comments,r.rolekey from t_role r,t_system_role sr where r.role_id=sr.role_id and sr.system_id='"
                + authSystem.getId() + "'";
        List<Object[]> roleList = AuthDatabaseUtil.queryForList(roleSql);
        map.put("Auth_Role", roleList);
        // T_SYSTEM_ROLE
        String systemRoleSql = "select system_role_id,system_id,role_id from t_system_role where system_id='" + authSystem.getId() + "'";
        List<Object[]> systemRoleList = AuthDatabaseUtil.queryForList(systemRoleSql);
        map.put("Auth_SystemRole", systemRoleList);
        // T_ROLE_RES
        String roleResSql = "select role_id, resource_id from t_role_res where role_id in (select sr.role_id from t_system_role sr where sr.system_id='"
                + authSystem.getId() + "')";
        List<Object[]> roleResList = AuthDatabaseUtil.queryForList(roleResSql);
        map.put("Auth_RoleRes", roleResList);
        String idNames = "'system_id','resource_id','system_resource_id','role_id','system_role_id'";
        // 发布组织用户，包括T_ORG、T_USER、T_ORG_USER、T_ROLE_USER
        if ("1".equals(SystemParameterUtil.getInstance().getSystemParamValue("系统发布是否发布组织用户"))) {
            // T_ORG
            String orgSql = null;
            if (AuthDatabaseUtil.columnExists("t_org", "relationorg_id")) {
                orgSql = "select t.organize_id,t.parent_id,t.organize_name,t.organize_type_name,t.comments,t.showorder,t.abbreviation,t.organize_code,t.is_leaf,t.relationorg_id,t.status from t_org t where t.organize_id not in (-1,1)";
            } else {
                orgSql = "select t.organize_id,t.parent_id,t.organize_name,t.organize_type_name,t.comments,t.showorder,t.abbreviation,t.organize_code from t_org t where t.organize_id not in (-1,1)";
            }
            List<Object[]> orgList = AuthDatabaseUtil.queryForList(orgSql);
            map.put("Auth_Org", orgList);
            // T_USER
            String userSql = null;
            if (AuthDatabaseUtil.columnExists("t_user", "user_level")) {
                userSql = "select t.user_id,t.ic_no,t.login_name,t.password,t.user_name,t.flag_action,t.email,t.mobile,t.title,t.showorder,t.comments,t.station,t.status,t.job_no,t.allowclientnum,t.user_level from t_user t where t.user_id!=1";
            } else {
                userSql = "select t.user_id,t.ic_no,t.login_name,t.password,t.user_name,t.flag_action,t.email,t.mobile,t.title,t.showorder,t.comments,t.station,t.status,t.job_no,t.allowclientnum from t_user t where t.user_id!=1";
            }
            List<Object[]> userList = AuthDatabaseUtil.queryForList(userSql);
            map.put("Auth_User", userList);
            // T_ORG_USER
            String orgUserSql = "select t.orguserid,t.organize_id,t.user_id,t.usertype,t.user_showorder from t_org_user t";
            List<Object[]> orgUserList = AuthDatabaseUtil.queryForList(orgUserSql);
            map.put("Auth_OrgUser", orgUserList);
            // T_ROLE_USER
            if (CollectionUtils.isNotEmpty(roleList)) {
                StringBuilder roleIdSb = new StringBuilder();
                for (Object[] objs : roleList) {
                    roleIdSb.append("'").append(StringUtil.null2empty(objs[0])).append("',");
                }
                roleIdSb.append("'-1','1','2','3','4','5'");
                String roleUserSql = "select t.role_id,t.user_id from t_role_user t where t.role_id in (" + roleIdSb.toString() + ")";
                List<Object[]> roleUserList = AuthDatabaseUtil.queryForList(roleUserSql);
                map.put("Auth_RoleUser", roleUserList);
            }
            idNames += ",'organize_id','user_id','orguser_id'";
        }
        // T_ID_SEQUENCE
        String idSequenceSql = "select id_name,maxval from t_id_sequence where id_name in (" + idNames + ")";
        List<Object[]> idSequenceList = AuthDatabaseUtil.queryForList(idSequenceSql);
        map.put("Auth_IdSequence", idSequenceList);
    }

    /**
     * 获取数据权限相关数据
     */
    @SuppressWarnings("unchecked")
    private void getAuthorityRelateData(Map<String, Object> map) {
        List<AuthorityTree> authorityTreeList = new ArrayList<AuthorityTree>();
        List<AuthorityData> authorityDataList = new ArrayList<AuthorityData>();
        List<AuthorityDataDetail> authorityDataDetailList = new ArrayList<AuthorityDataDetail>();
        List<AuthorityCode> authorityCodeList = new ArrayList<AuthorityCode>();
        List<Object[]> roleList = (List<Object[]>) map.get("Auth_Role");
        StringBuilder roleIdSb = new StringBuilder();
        if (CollectionUtils.isNotEmpty(roleList)) {
            for (int i = 0; i < roleList.size(); i++) {
                if (i != 0) {
                    roleIdSb.append(",");
                }
                roleIdSb.append(roleList.get(i)[0]);
            }
        }
        List<Menu> menuList = (List<Menu>) map.get("Menu");
        StringBuilder menuIdSb = new StringBuilder();
        for (Menu menu : menuList) {
            if (StringUtil.isNotEmpty(menu.getComponentVersionId())) {
                menuIdSb.append(menu.getId()).append(",");
            }
        }
        if (menuIdSb.length() > 0) {
            menuIdSb.deleteCharAt(menuIdSb.length() - 1);
            List<String> menuIdsList = CommonUtil.parseSqlIn(menuIdSb.toString());
            for (String menuIds : menuIdsList) {
                // 获取角色数据权限
                if (roleIdSb.length() > 0) {
                    authorityTreeList.addAll(getService(AuthorityTreeService.class).getByRoleIdsAndMenuIds(roleIdSb.toString(), menuIds));
                    authorityDataList.addAll(getService(AuthorityDataService.class).getByRoleIdsAndMenuIds(roleIdSb.toString(), menuIds));
                    authorityDataDetailList.addAll(getService(AuthorityDataDetailService.class).getByRoleIdsAndMenuIds(roleIdSb.toString(), menuIds));
                    authorityCodeList.addAll(getService(AuthorityCodeService.class).getByRoleIdsAndMenuIds(roleIdSb.toString(), menuIds));
                }
                // 获取用户数据权限
                authorityTreeList.addAll(getService(AuthorityTreeService.class).getByMenuIdsOfUser(menuIds));
                authorityDataList.addAll(getService(AuthorityDataService.class).getByMenuIdsOfUser(menuIds));
                authorityDataDetailList.addAll(getService(AuthorityDataDetailService.class).getByMenuIdsOfUser(menuIds));
                authorityCodeList.addAll(getService(AuthorityCodeService.class).getByMenuIdsOfUser(menuIds));
            }
        }
        map.put("AuthorityTree", authorityTreeList);
        map.put("AuthorityData", authorityDataList);
        map.put("AuthorityDataDetail", authorityDataDetailList);
        map.put("AuthorityCode", authorityCodeList);
    }

    /**
     * 整理模块、树
     * 
     * @param componentVersionList 系统中使用到的所有构件
     * @param map 导出的数据Map
     * @param moduleIdList 模块ID列表
     * @param tableMap 表Map
     * @param workflowDefineSet 工作流Set
     * @return Map<String, Map<String, PhysicalTableDefine>> 模块和物理表对应关系
     */
    private Map<String, Map<String, PhysicalTableDefine>> getModuleAndPhysicalTableDefine(List<ComponentVersion> componentVersionList, Map<String, Object> map,
            List<String> moduleIdList, Map<String, PhysicalTableDefine> physicalTableMap, Set<WorkflowDefine> workflowDefineSet) {
        // 存放模块
        List<Module> moduleList = new ArrayList<Module>();
        // 存放所有模块所关联到的树节点
        List<TreeDefine> treeDefineList = new ArrayList<TreeDefine>();
        // 物理表组、物理表组和物理表关系
        Set<PhysicalGroupDefine> physicalGroupDefineSet = new HashSet<PhysicalGroupDefine>();
        Set<PhysicalGroupRelation> physicalGroupRelationSet = new HashSet<PhysicalGroupRelation>();
        // 存放所有模块所关联到的树根节点ID
        Set<String> treeIdSet = new HashSet<String>();
        Module module = null;
        // 存放模块中使用表对应关系
        Map<String, Map<String, PhysicalTableDefine>> moduleTableRelationMap = new HashMap<String, Map<String, PhysicalTableDefine>>();
        if (CollectionUtils.isNotEmpty(componentVersionList)) {
            // 存放某个模块所关联到的表
            Map<String, PhysicalTableDefine> tempPhysicalTableMap = null;
            List<TreeDefine> tempTreeDefineList = null;
            Set<PhysicalGroupDefine> tempPhysicalGroupDefineSet = null;
            List<PhysicalGroupRelation> tempPhysicalGroupRelationList = null;
            for (ComponentVersion componentVersion : componentVersionList) {
                if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) == -1
                        && !ConstantVar.Component.Type.TAB.equals(componentVersion.getComponent().getType())) {
                    continue;
                }
                module = getService(ModuleService.class).findByComponentVersionId(componentVersion.getId());
                if (module != null) {
                    moduleList.add(module);
                    moduleIdList.add(module.getId());
                    if (ConstantVar.Component.Type.TREE.equals(module.getType())) {
                        // 树构件
                        String treeId = module.getTreeId();
                        if (!treeIdSet.contains(treeId)) {
                            treeIdSet.add(treeId);
                            tempTreeDefineList = getService(TreeDefineService.class).getAllChildren(treeId, null);
                            TreeDefine rootNode = getService(TreeDefineService.class).getByID(treeId);
                            tempTreeDefineList.add(rootNode);
                            treeDefineList.addAll(tempTreeDefineList);
                            tempPhysicalGroupDefineSet = new HashSet<PhysicalGroupDefine>();
                            tempPhysicalGroupRelationList = new ArrayList<PhysicalGroupRelation>();
                            tempPhysicalTableMap = new HashMap<String, PhysicalTableDefine>();
                            if (CollectionUtils.isNotEmpty(tempTreeDefineList)) {
                                for (TreeDefine treeDefine : tempTreeDefineList) {
                                    if (treeDefine == null) {
                                        continue;
                                    }
                                    if (TreeDefine.T_TABLE.equals(treeDefine.getType())) {
                                        PhysicalTableDefine physicalTable = getService(PhysicalTableDefineService.class).getByID(treeDefine.getTableId());
                                        physicalTableMap.put(physicalTable.getId(), physicalTable);
                                        tempPhysicalTableMap.put(physicalTable.getId(), physicalTable);
                                    } else if (TreeDefine.T_GROUP.equals(treeDefine.getType())) {
                                        PhysicalGroupDefine physicalGroup = getService(PhysicalGroupDefineService.class).getByID(treeDefine.getDbId());
                                        if (physicalGroup != null) {
                                            tempPhysicalGroupDefineSet.add(physicalGroup);
                                        }
                                    } else if (TreeDefine.T_COFLOW.equals(treeDefine.getType())) {
                                        WorkflowDefine workflowDefine = getService(WorkflowDefineService.class).getByID(treeDefine.getDbId());
                                        if (workflowDefine != null) {
                                            workflowDefineSet.add(workflowDefine);
                                        }
                                    }
                                }
                                if (CollectionUtils.isNotEmpty(tempPhysicalGroupDefineSet)) {
                                    for (PhysicalGroupDefine physicalGroup : tempPhysicalGroupDefineSet) {
                                        tempPhysicalGroupRelationList = getService(PhysicalGroupRelationService.class).findByGroupId(physicalGroup.getId());
                                        if (CollectionUtils.isNotEmpty(tempPhysicalGroupRelationList)) {
                                            for (PhysicalGroupRelation physicalGroupRelation : tempPhysicalGroupRelationList) {
                                                PhysicalTableDefine physicalTable = getService(PhysicalTableDefineService.class).getByID(
                                                        physicalGroupRelation.getTableId());
                                                physicalTableMap.put(physicalTable.getId(), physicalTable);
                                                tempPhysicalTableMap.put(physicalTable.getId(), physicalTable);
                                            }
                                        }
                                        physicalGroupRelationSet.addAll(tempPhysicalGroupRelationList);
                                    }
                                    physicalGroupDefineSet.addAll(tempPhysicalGroupDefineSet);
                                }
                            }
                            moduleTableRelationMap.put(module.getId(), tempPhysicalTableMap);
                        }
                    } else if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(module.getType())) {
                        tempPhysicalTableMap = new HashMap<String, PhysicalTableDefine>();
                        // 物理表构件
                        if (Module.L_1C.equals(module.getTemplateType())) {
                            // 整张页面
                            PhysicalTableDefine physicalTable1 = getService(PhysicalTableDefineService.class).getByID(module.getTable1Id());
                            if (physicalTable1 != null) {
                                physicalTableMap.put(physicalTable1.getId(), physicalTable1);
                                tempPhysicalTableMap.put(physicalTable1.getId(), physicalTable1);
                            }
                        } else if (Module.L_2E.equals(module.getTemplateType())) {
                            // 上下结构页面
                            PhysicalTableDefine physicalTable1 = getService(PhysicalTableDefineService.class).getByID(module.getTable1Id());
                            if (physicalTable1 != null) {
                                physicalTableMap.put(physicalTable1.getId(), physicalTable1);
                                tempPhysicalTableMap.put(physicalTable1.getId(), physicalTable1);
                            }
                            PhysicalTableDefine physicalTable2 = getService(PhysicalTableDefineService.class).getByID(module.getTable2Id());
                            if (physicalTable2 != null) {
                                physicalTableMap.put(physicalTable2.getId(), physicalTable2);
                                tempPhysicalTableMap.put(physicalTable2.getId(), physicalTable2);
                            }
                        } else if (Module.L_3E.equals(module.getTemplateType())) {
                            // 上中下结构页面
                            PhysicalTableDefine physicalTable1 = getService(PhysicalTableDefineService.class).getByID(module.getTable1Id());
                            if (physicalTable1 != null) {
                                physicalTableMap.put(physicalTable1.getId(), physicalTable1);
                                tempPhysicalTableMap.put(physicalTable1.getId(), physicalTable1);
                            }
                            PhysicalTableDefine physicalTable2 = getService(PhysicalTableDefineService.class).getByID(module.getTable2Id());
                            if (physicalTable2 != null) {
                                physicalTableMap.put(physicalTable2.getId(), physicalTable2);
                                tempPhysicalTableMap.put(physicalTable2.getId(), physicalTable2);
                            }
                            PhysicalTableDefine physicalTable3 = getService(PhysicalTableDefineService.class).getByID(module.getTable3Id());
                            if (physicalTable3 != null) {
                                physicalTableMap.put(physicalTable3.getId(), physicalTable3);
                                tempPhysicalTableMap.put(physicalTable3.getId(), physicalTable3);
                            }
                        }
                        moduleTableRelationMap.put(module.getId(), tempPhysicalTableMap);
                    }
                }
            }
        }
        map.put("PhysicalGroupDefine", physicalGroupDefineSet);
        map.put("PhysicalGroupRelation", physicalGroupRelationSet);
        // 整理模块的分类
        // Module、TreeDefine
        map.put("Module", moduleList);
        map.put("TreeDefine", treeDefineList);
        return moduleTableRelationMap;
    }

    /**
     * 整理应用定义
     * 
     * @param assembleComponentVersionIdList 系统中使用到的所有组合构件IDs
     * @param menuIds 系统中使用到的菜单IDs
     * @param map 导出的数据Map
     * @param tableMap 表Map
     */
    private void getApp(List<String> assembleComponentVersionIdList, String menuIds, Map<String, Object> map, Map<String, PhysicalTableDefine> physicalTableMap) {
        // 存放应用定义
        List<AppDefine> appDefineList = new ArrayList<AppDefine>();
        List<AppSearch> appSearchList = new ArrayList<AppSearch>();
        List<AppSearchPanel> appSearchPanelList = new ArrayList<AppSearchPanel>();
        List<AppColumn> appColumnList = new ArrayList<AppColumn>();
        List<AppGrid> appGridList = new ArrayList<AppGrid>();
        List<AppSort> appSortList = new ArrayList<AppSort>();
        List<AppForm> appFormList = new ArrayList<AppForm>();
        List<AppFormElement> appFormElementList = new ArrayList<AppFormElement>();
        List<AppReport> appReportList = new ArrayList<AppReport>();
        List<AppButton> appButtonList = new ArrayList<AppButton>();
        // 存放所有模块所关联到的树根节点ID
        if (CollectionUtils.isNotEmpty(assembleComponentVersionIdList)) {
            List<AppDefine> tempAppDefineList = null;
            List<AppSearch> tempAppSearchList = null;
            AppSearchPanel tempAppSearchPanel = null;
            List<AppColumn> tempAppColumnList = null;
            AppGrid appGrid = null;
            List<AppSort> tempAppSortList = null;
            AppForm appForm = null;
            List<AppFormElement> tempAppFormElementList = null;
            List<AppReport> tempAppReportList = null;
            List<AppButton> tempAppButtonList = null;
            PhysicalTableDefine physicalTableDefine = null;
            Set<String> workflowBoxSet = new HashSet<String>();
            workflowBoxSet.add(AppDefine.DEFAULT_DEFINE_ID);
            workflowBoxSet.add(WorkflowUtil.Box.applyfor);
            workflowBoxSet.add(WorkflowUtil.Box.complete);
            workflowBoxSet.add(WorkflowUtil.Box.hasdone);
            workflowBoxSet.add(WorkflowUtil.Box.hasread);
            workflowBoxSet.add(WorkflowUtil.Box.todo);
            workflowBoxSet.add(WorkflowUtil.Box.toread);
            // 表中默认应用定义
            if (!physicalTableMap.isEmpty()) {
                for (Iterator<PhysicalTableDefine> iterator = physicalTableMap.values().iterator(); iterator.hasNext();) {
                    physicalTableDefine = iterator.next();
                    tempAppDefineList = getService(AppDefineService.class).findByTableIdAndUserId(physicalTableDefine.getId(), CommonUtil.SUPER_ADMIN_ID);
                    if (CollectionUtils.isNotEmpty(tempAppDefineList)) {
                        for (AppDefine appDefine : tempAppDefineList) {
                            if (appDefine != null) {
                                if (AppDefine.DEFAULT_DEFINE_ID.equals(appDefine.getComponentVersionId())
                                        || AppDefine.DEFAULT_DEFINE_ID.equals(appDefine.getMenuId())
                                        || (assembleComponentVersionIdList.contains(appDefine.getComponentVersionId()) && menuIds
                                                .indexOf(appDefine.getMenuId()) != -1) || workflowBoxSet.contains(appDefine.getComponentVersionId())) {
                                    appDefineList.add(appDefine);
                                    tempAppSearchList = getService(AppSearchService.class).findByFk(physicalTableDefine.getId(),
                                            appDefine.getComponentVersionId(), appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                    if (CollectionUtils.isNotEmpty(tempAppSearchList)) {
                                        appSearchList.addAll(tempAppSearchList);
                                    }
                                    tempAppSearchPanel = getService(AppSearchPanelService.class).findByFk(physicalTableDefine.getId(),
                                            appDefine.getComponentVersionId(), appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                    if (tempAppSearchPanel != null) {
                                        appSearchPanelList.add(tempAppSearchPanel);
                                    }
                                    tempAppColumnList = getService(AppColumnService.class).findByFk(physicalTableDefine.getId(),
                                            appDefine.getComponentVersionId(), appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                    if (CollectionUtils.isNotEmpty(tempAppColumnList)) {
                                        appColumnList.addAll(tempAppColumnList);
                                    }
                                    appGrid = getService(AppGridService.class).findByFk(physicalTableDefine.getId(), appDefine.getComponentVersionId(),
                                            appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                    if (appGrid != null) {
                                        appGridList.add(appGrid);
                                    }
                                    tempAppSortList = getService(AppSortService.class).findByFk(physicalTableDefine.getId(), appDefine.getComponentVersionId(),
                                            appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                    if (CollectionUtils.isNotEmpty(tempAppSortList)) {
                                        appSortList.addAll(tempAppSortList);
                                    }
                                    appForm = getService(AppFormService.class).findByFk(physicalTableDefine.getId(), appDefine.getComponentVersionId(),
                                            appDefine.getMenuId());
                                    if (appForm != null) {
                                        appFormList.add(appForm);
                                        tempAppFormElementList = getService(AppFormElementService.class).findByFk(physicalTableDefine.getId(),
                                                appDefine.getComponentVersionId(), appDefine.getMenuId());
                                        if (CollectionUtils.isNotEmpty(tempAppFormElementList)) {
                                            appFormElementList.addAll(tempAppFormElementList);
                                        }
                                    }
                                    tempAppReportList = getService(AppReportService.class).findByFk(physicalTableDefine.getId(),
                                            appDefine.getComponentVersionId(), appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                    if (CollectionUtils.isNotEmpty(tempAppReportList)) {
                                        appReportList.addAll(tempAppReportList);
                                    }
                                    tempAppButtonList = getService(AppButtonService.class).findByFk(physicalTableDefine.getId(),
                                            appDefine.getComponentVersionId(), appDefine.getMenuId(), AppButton.BUTTON_FORM);
                                    if (CollectionUtils.isNotEmpty(tempAppButtonList)) {
                                        appButtonList.addAll(tempAppButtonList);
                                    }
                                    tempAppButtonList = getService(AppButtonService.class).findByFk(physicalTableDefine.getId(),
                                            appDefine.getComponentVersionId(), appDefine.getMenuId(), AppButton.BUTTON_GRID);
                                    if (CollectionUtils.isNotEmpty(tempAppButtonList)) {
                                        appButtonList.addAll(tempAppButtonList);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // AppDefine、AppSearch、AppSearchPanel、AppGreatSearch、AppColumn、AppSort、AppForm、AppFormElement、AppFilter、AppReport
        map.put("AppDefine", appDefineList);
        map.put("AppSearch", appSearchList);
        map.put("AppSearchPanel", appSearchPanelList);
        map.put("AppColumn", appColumnList);
        map.put("AppGrid", appGridList);
        map.put("AppSort", appSortList);
        map.put("AppForm", appFormList);
        map.put("AppFormElement", appFormElementList);
        map.put("AppReport", appReportList);
        map.put("AppButton", appButtonList);
    }

    /**
     * 整理字段
     * 
     * @param columnDefineList 字段List
     * @param logicTableDefine 逻辑表
     */
    private void getLogicTableColumns(List<ColumnDefine> columnDefineList, LogicTableDefine logicTableDefine) {
        List<ColumnDefine> tempColumnDefineList = getService(ColumnDefineService.class).findByTableId(logicTableDefine.getCode());
        if (CollectionUtils.isNotEmpty(tempColumnDefineList)) {
            columnDefineList.addAll(tempColumnDefineList);
        }
    }

    /**
     * 整理字段
     * 
     * @param columnDefineList 字段List
     * @param physicalTableDefine 物理表
     * @param tableColumnMap 表和字段对应关系
     */
    private void getPhysicalTableColumns(List<ColumnDefine> columnDefineList, PhysicalTableDefine physicalTableDefine,
            Map<String, List<ColumnDefine>> tableColumnMap) {
        List<ColumnDefine> tempColumnDefineList = getService(ColumnDefineService.class).findByTableId(physicalTableDefine.getId());
        if (CollectionUtils.isNotEmpty(tempColumnDefineList)) {
            columnDefineList.addAll(tempColumnDefineList);
            if (tableColumnMap != null && !ConstantVar.TableClassification.VIEW.equals(physicalTableDefine.getClassification())) {
                tableColumnMap.put(physicalTableDefine.getTableName(), tempColumnDefineList);
            }
        }
    }

    /**
     * 整理表关系
     * 
     * @param tableMap 系统关联的表
     * @param tableRelationMap 表关系Map
     * @param physicalTableDefine 物理表
     */
    private void getTableRelations(Map<String, PhysicalTableDefine> physicalTableMap, Map<String, TableRelation> tableRelationMap,
            PhysicalTableDefine physicalTableDefine, Set<PhysicalTableDefine> documentPhyTableDefineSet) {
        List<TableRelation> tempTableRelationList = getService(TableRelationService.class).findByTableId(physicalTableDefine.getId());
        if (CollectionUtils.isNotEmpty(tempTableRelationList)) {
            for (TableRelation tableRelation : tempTableRelationList) {
                if (physicalTableMap.containsKey(tableRelation.getRelateTableId())) {
                    tableRelationMap.put(tableRelation.getId(), tableRelation);
                } else {
                    PhysicalTableDefine tableDefine = getService(PhysicalTableDefineService.class).getByID(tableRelation.getRelateTableId());
                    if (tableDefine != null && ConstantVar.Labels.Document.CODE.equals(tableDefine.getLogicTableCode())) {
                        physicalTableMap.put(tableDefine.getId(), tableDefine);
                        documentPhyTableDefineSet.add(tableDefine);
                        tableRelationMap.put(tableRelation.getId(), tableRelation);
                    }
                }
            }
        }
        tempTableRelationList = getService(TableRelationService.class).findByRelateTableId(physicalTableDefine.getId());
        if (CollectionUtils.isNotEmpty(tempTableRelationList)) {
            for (TableRelation tableRelation : tempTableRelationList) {
                if (!tableRelationMap.containsKey(tableRelation.getId())) {
                    tableRelationMap.put(tableRelation.getId(), tableRelation);
                } else {
                    PhysicalTableDefine tableDefine = getService(PhysicalTableDefineService.class).getByID(tableRelation.getId());
                    if (tableDefine != null && ConstantVar.Labels.Document.CODE.equals(tableDefine.getLogicTableCode())) {
                        physicalTableMap.put(tableDefine.getId(), tableDefine);
                        documentPhyTableDefineSet.add(tableDefine);
                        tableRelationMap.put(tableRelation.getId(), tableRelation);
                    }
                }
            }
        }
    }

    /**
     * 整理字段关联
     * 
     * @param physicalTableMap 系统关联的表
     * @param columnRelationList 字段关联
     * @param columnSpliceList 字段拼接
     * @param columnSplitList 字段截取
     * @param columnBusinessList 特殊业务
     * @param columnOperationList 继承、求和、最值
     * @param physicalTableDefine 物理表
     */
    private void getColumnRelations(Map<String, PhysicalTableDefine> physicalTableMap, List<ColumnRelation> columnRelationList,
            List<ColumnSplice> columnSpliceList, List<ColumnSplit> columnSplitList, List<ColumnBusiness> columnBusinessList,
            List<ColumnOperation> columnOperationList, PhysicalTableDefine physicalTableDefine) {
        List<ColumnRelation> tempColumnRelationList = getService(ColumnRelationService.class).findByTableId(physicalTableDefine.getId());
        if (CollectionUtils.isNotEmpty(tempColumnRelationList)) {
            List<ColumnSplice> tempColumnSpliceList = getService(ColumnSpliceService.class).findByTableId(physicalTableDefine.getId());
            if (CollectionUtils.isNotEmpty(tempColumnSpliceList)) {
                columnSpliceList.addAll(tempColumnSpliceList);
            }
            List<ColumnSplit> tempColumnSplitList = getService(ColumnSplitService.class).findByTableId(physicalTableDefine.getId());
            if (CollectionUtils.isNotEmpty(tempColumnSplitList)) {
                columnSplitList.addAll(tempColumnSplitList);
            }
            List<ColumnBusiness> tempColumnBusinessList = getService(ColumnBusinessService.class).findByTableId(physicalTableDefine.getId());
            if (CollectionUtils.isNotEmpty(tempColumnBusinessList)) {
                columnBusinessList.addAll(tempColumnBusinessList);
            }
            List<ColumnOperation> tempColumnOperationList = getService(ColumnOperationService.class).findByTableId(physicalTableDefine.getId());
            if (CollectionUtils.isNotEmpty(tempColumnOperationList)) {
                for (ColumnOperation columnOperation : tempColumnOperationList) {
                    if (physicalTableMap.containsKey(columnOperation.getOriginTableId())) {
                        columnOperationList.add(columnOperation);
                    } else {
                        for (Iterator<ColumnRelation> iterator = tempColumnRelationList.iterator(); iterator.hasNext();) {
                            if (iterator.next().getId().equals(columnOperation.getColumnRelationId())) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                }
            }
            columnRelationList.addAll(tempColumnRelationList);
        }
    }

    /**
     * 整理需要导出的工作流信息
     * 
     * @param map 导出的数据Map
     * @param workflowDefineSet 工作流定义Set
     * @param physicalTableMap 物理表Map
     */
    @SuppressWarnings("unchecked")
    private void getWorkflows(Map<String, Object> map, Set<WorkflowDefine> workflowDefineSet, Map<String, PhysicalTableDefine> physicalTableMap) {
        List<WorkflowDefine> workflowDefineList = new ArrayList<WorkflowDefine>(workflowDefineSet);
        Set<String> workflowTreeIdSet = new HashSet<String>();
        List<WorkflowVersion> workflowVersionList = new ArrayList<WorkflowVersion>();
        List<WorkflowFormSetting> workflowFormSettingList = new ArrayList<WorkflowFormSetting>();
        List<WorkflowButtonSetting> workflowButtonSettingList = new ArrayList<WorkflowButtonSetting>();
        List<WorkflowActivity> workflowActivityList = new ArrayList<WorkflowActivity>();
        List<String[]> wfRelevantdataList = new ArrayList<String[]>();
        Set<String> wfXpdlFileNameSet = new HashSet<String>();
        Set<String> wfPackIdAndVersionSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(workflowDefineList)) {
            List<WorkflowVersion> tempWorkflowVersionList = null;
            for (WorkflowDefine workflowDefine : workflowDefineList) {
                // 获取树节点
                workflowTreeIdSet.add(workflowDefine.getWorkflowTreeId());
                // 获取视图
                String viewName = WorkflowUtil.getViewName(workflowDefine.getWorkflowCode());
                PhysicalTableDefine viewTable = getService(PhysicalTableDefineService.class).getByTableName(viewName);
                if (viewTable != null) {
                    physicalTableMap.put(viewTable.getId(), viewTable);
                }
                // 获取相关表
                PhysicalTableDefine businessTable = getService(PhysicalTableDefineService.class).getByID(workflowDefine.getBusinessTableId());
                if (businessTable != null) {
                    physicalTableMap.put(businessTable.getId(), businessTable);
                }
                PhysicalTableDefine businessView = getService(PhysicalTableDefineService.class).getByID(WorkflowUtil.getViewId(workflowDefine.getId()));
                if (businessView != null) {
                    physicalTableMap.put(businessView.getId(), businessView);
                }
                if (ConstantVar.Judgment.YES.equals(workflowDefine.getEnableDocumentTable())) {
                    String documentTableName = WorkflowUtil.getDocumentTableName(workflowDefine.getWorkflowCode());
                    PhysicalTableDefine documentTable = getService(PhysicalTableDefineService.class).getByTableName(documentTableName);
                    if (documentTable != null) {
                        physicalTableMap.put(documentTable.getId(), documentTable);
                    }
                }
                if (ConstantVar.Judgment.YES.equals(workflowDefine.getEnableConfirmTable())) {
                    String confirmTableName = WorkflowUtil.getConfirmTableName(workflowDefine.getWorkflowCode());
                    PhysicalTableDefine confirmTable = getService(PhysicalTableDefineService.class).getByTableName(confirmTableName);
                    if (confirmTable != null) {
                        physicalTableMap.put(confirmTable.getId(), confirmTable);
                    }
                }
                if (ConstantVar.Judgment.YES.equals(workflowDefine.getEnableAssistTable())) {
                    String assistTableName = WorkflowUtil.getAssistTableName(workflowDefine.getWorkflowCode());
                    PhysicalTableDefine assistTable = getService(PhysicalTableDefineService.class).getByTableName(assistTableName);
                    if (assistTable != null) {
                        physicalTableMap.put(assistTable.getId(), assistTable);
                    }
                }
                String packageId = WorkflowUtil.getPackageIdByCode(workflowDefine.getWorkflowCode());
                String processId = WorkflowUtil.getProcessIdByCode(workflowDefine.getWorkflowCode());
                // 获取工作流版本
                tempWorkflowVersionList = getService(WorkflowVersionService.class).getByWorkflowId(workflowDefine.getId());
                if (CollectionUtils.isNotEmpty(tempWorkflowVersionList)) {
                    workflowVersionList.addAll(tempWorkflowVersionList);
                    for (WorkflowVersion workflowVersion : workflowVersionList) {
                        wfXpdlFileNameSet.add(WorkflowUtil.getFileName(workflowDefine.getWorkflowCode(), workflowVersion.getVersion()));
                        wfPackIdAndVersionSet.add(packageId + "$$" + workflowVersion.getVersion());
                    }
                }
                // 获取工作流相关数据
                List<String[]> list = DatabaseHandlerDao.getInstance().queryForList(
                        "select t.table_name,t.package_id,t.process_id from t_wf_relevantdata_tablename t where t.package_id='" + packageId
                                + "' and t.process_id='" + processId + "'");
                if (CollectionUtils.isNotEmpty(list)) {
                    wfRelevantdataList.addAll(list);
                }
            }
        }
        // 获取工作流分类树
        Map<String, WorkflowTree> workflowTreeMap = new HashMap<String, WorkflowTree>();
        if (CollectionUtils.isNotEmpty(workflowTreeIdSet)) {
            WorkflowTree workflowTree = null;
            for (String workflowTreeId : workflowTreeIdSet) {
                if (workflowTreeMap.get(workflowTreeId) == null) {
                    workflowTree = getService(WorkflowTreeService.class).getByID(workflowTreeId);
                    if (workflowTree != null) {
                        workflowTreeMap.put(workflowTree.getId(), workflowTree);
                        // 工作流分类树父节点
                        String parentId = workflowTree.getParentId();
                        while (!"-1".equals(parentId)) {
                            if (workflowTreeMap.containsKey(parentId)) {
                                break;
                            }
                            workflowTree = getService(WorkflowTreeService.class).getByID(parentId);
                            if (workflowTree != null) {
                                workflowTreeMap.put(workflowTree.getId(), workflowTree);
                                parentId = workflowTree.getParentId();
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(workflowVersionList)) {
            String packageId, processId, packageVersion;
            for (WorkflowVersion workflowVersion : workflowVersionList) {
                packageId = WorkflowUtil.getPackageIdById(workflowVersion.getWorkflowId());
                processId = WorkflowUtil.getProcessIdById(workflowVersion.getWorkflowId());
                packageVersion = workflowVersion.getVersion();
                workflowActivityList.addAll(getService(WorkflowActivityService.class).find(
                        "EQ_packageId=" + packageId + ";EQ_processId=" + processId + ";EQ_packageVersion=" + packageVersion));
                workflowFormSettingList.addAll(getService(WorkflowFormSettingService.class).findByWorkflowVersionId(workflowVersion.getId()));
                workflowButtonSettingList.addAll(getService(WorkflowButtonSettingService.class).findByWorkflowVersionId(workflowVersion.getId()));
            }
        }
        // WorkflowTree、WorkflowDefine、WorkflowVersion、WorkflowFormSetting、WorkflowButtonSetting
        map.put("WorkflowTree", new ArrayList<WorkflowTree>(workflowTreeMap.values()));
        map.put("WorkflowDefine", workflowDefineList);
        map.put("WorkflowVersion", workflowVersionList);
        map.put("WorkflowActivity", workflowActivityList);
        map.put("WorkflowFormSetting", workflowFormSettingList);
        map.put("WorkflowButtonSetting", workflowButtonSettingList);
        map.put("WorkflowRelevantdata", wfRelevantdataList);
        map.put("WFXpdlFileName", wfXpdlFileNameSet);
        map.put("WFPackIdAndVersion", wfPackIdAndVersionSet);
    }

    /**
     * 整理需要导出的报表信息
     * 
     * @param map 导出的数据Map
     * @param moduleTableRelationMap 模块和表关联关系Map
     * @return List<ReportTable>
     */
    private List<ReportTable> getReports(Map<String, Object> map, Map<String, Map<String, PhysicalTableDefine>> moduleTableRelationMap) {
        Map<String, ReportTable> reportTableMap = new HashMap<String, ReportTable>();
        for (Iterator<String> iterator = moduleTableRelationMap.keySet().iterator(); iterator.hasNext();) {
            String moduleId = iterator.next();
            Map<String, PhysicalTableDefine> tableDefineMap = moduleTableRelationMap.get(moduleId);
            for (Iterator<String> iterator2 = tableDefineMap.keySet().iterator(); iterator2.hasNext();) {
                String tableId = iterator2.next();
                List<ReportTable> tempReportTableList = getService(ReportTableService.class).findByTableId(tableId);
                if (CollectionUtils.isNotEmpty(tempReportTableList)) {
                    for (ReportTable reportTable : tempReportTableList) {
                        reportTableMap.put(reportTable.getId(), reportTable);
                    }
                }
            }
        }
        List<ReportTable> reportTableList = new ArrayList<ReportTable>(reportTableMap.values());
        List<Report> reportList = new ArrayList<Report>();
        List<ReportColumn> reportColumnList = new ArrayList<ReportColumn>();
        List<ReportDataSource> reportDataSourceList = new ArrayList<ReportDataSource>();
        List<ReportDefine> reportDefineList = new ArrayList<ReportDefine>();
        List<ReportPrintSetting> reportPrintSettingList = new ArrayList<ReportPrintSetting>();
        List<ReportTableRelation> reportTableRelationList = new ArrayList<ReportTableRelation>();
        if (!reportTableList.isEmpty()) {
            Set<String> reportIdSet = new HashSet<String>();
            for (ReportTable reportTable : reportTableList) {
                reportIdSet.add(reportTable.getReportId());
            }
            for (String reportId : reportIdSet) {
                Report report = getService(ReportService.class).getByID(reportId);
                reportList.add(report);
                List<ReportColumn> tempReportColumnList = getService(ReportColumnService.class).findByReportId(reportId);
                if (CollectionUtils.isNotEmpty(tempReportColumnList)) {
                    reportColumnList.addAll(tempReportColumnList);
                }
                List<ReportDataSource> tempReportDataSourceList = getService(ReportDataSourceService.class).getByReportId(reportId);
                if (CollectionUtils.isNotEmpty(tempReportDataSourceList)) {
                    reportDataSourceList.addAll(tempReportDataSourceList);
                }
                ReportDefine reportDefine = getService(ReportDefineService.class).getByReportId(reportId);
                if (reportDefine != null) {
                    reportDefineList.add(reportDefine);
                }
                List<ReportPrintSetting> tempReportPrintSettingList = getService(ReportPrintSettingService.class).findByReportId(reportId);
                if (CollectionUtils.isNotEmpty(tempReportPrintSettingList)) {
                    reportPrintSettingList.addAll(tempReportPrintSettingList);
                }
                List<ReportTableRelation> tempReportTableRelationList = getService(ReportTableRelationService.class).getByReportId(reportId);
                if (CollectionUtils.isNotEmpty(tempReportTableRelationList)) {
                    reportTableRelationList.addAll(tempReportTableRelationList);
                }
            }
        }
        getReportCategories(reportList);
        // Report、ReportColumn、ReportDataSource、ReportDefine、ReportPrintSetting、ReportTable、ReportTableRelation
        map.put("Report", reportList);
        map.put("ReportColumn", reportColumnList);
        map.put("ReportDataSource", reportDataSourceList);
        map.put("ReportDefine", reportDefineList);
        map.put("ReportPrintSetting", reportPrintSettingList);
        map.put("ReportTable", reportTableList);
        map.put("ReportTableRelation", reportTableRelationList);
        return reportTableList;
    }

    /**
     * 将报表的分类加入到reportList中
     * 
     * @param reportList 报表List
     */
    private void getReportCategories(List<Report> reportList) {
        Set<String> reportCategoryIdSet = new HashSet<String>();
        List<Report> reportCategoryList = new ArrayList<Report>();
        for (Report report : reportList) {
            String categoryId = report.getParentId();
            while (!"-1".equals(categoryId)) {
                if (!reportCategoryIdSet.contains(categoryId)) {
                    reportCategoryIdSet.add(categoryId);
                    Report reportCategory = getService(ReportService.class).getByID(categoryId);
                    if (reportCategory == null)
                        break;
                    reportCategoryList.add(reportCategory);
                    categoryId = reportCategory.getParentId();
                } else {
                    break;
                }
            }
        }
        reportList.addAll(reportCategoryList);
    }

    /**
     * 将要导出的视图加入到tableDefineList中
     * 
     * @param tableRelationList 表关系List
     * @param physicalTableDefineSet 系统关联的表Set
     * @param columnDefineList 字段List
     * @return Map<String, PhysicalTableDefine>
     */
    private Map<String, PhysicalTableDefine> getViews(List<TableRelation> tableRelationList, Set<PhysicalTableDefine> physicalTableDefineSet,
            List<ColumnDefine> columnDefineList) {
        Map<String, PhysicalTableDefine> viewMap = new HashMap<String, PhysicalTableDefine>();
        if (CollectionUtils.isNotEmpty(physicalTableDefineSet)) {
            for (PhysicalTableDefine physicalTableDefine : physicalTableDefineSet) {
                if (ConstantVar.TableClassification.VIEW.equals(physicalTableDefine.getClassification())
                        && !viewMap.keySet().contains(physicalTableDefine.getTableName())) {
                    viewMap.put(physicalTableDefine.getTableName(), physicalTableDefine);
                }
            }
        }
        Map<String, String> tableRelationViewNameMap = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(tableRelationList)) {
            for (TableRelation tableRelation : tableRelationList) {
                String key = tableRelation.getTableId() + "_" + tableRelation.getRelateTableId();
                if (tableRelationViewNameMap.get(key) == null) {
                    PhysicalTableDefine view = getService(PhysicalTableDefineService.class).getViewByRelation(tableRelation.getTableId(),
                            tableRelation.getRelateTableId());
                    if (view != null) {
                        tableRelationViewNameMap.put(key, view.getTableName());
                        if (!viewMap.keySet().contains(view.getTableName())) {
                            viewMap.put(view.getTableName(), view);
                        }
                        if (!physicalTableDefineSet.contains(view)) {
                            physicalTableDefineSet.add(view);
                            columnDefineList.addAll(getService(ColumnDefineService.class).findByTableId(view.getId()));
                        }
                    }
                }
            }
        }
        return viewMap;
    }

    /**
     * 获取新的更新包版本
     * 
     * @return Object
     */
    public Object getNewUpdatePackageVersion() {
        String rootMenuId = getParameter("rootMenuId");
        String releaseSystemVersion = getParameter("releaseSystemVersion");
        String newUpdatePackageVersion = releaseSystemVersion + ".1";
        List<Release> updatePackageList = getService().getByRootMenuIdAndType(rootMenuId, "1");
        if (CollectionUtils.isNotEmpty(updatePackageList)) {
            for (Iterator<Release> i = updatePackageList.iterator(); i.hasNext();) {
                if (!i.next().getVersion().startsWith(releaseSystemVersion)) {
                    i.remove();
                }
            }
            if (!updatePackageList.isEmpty()) {
                Collections.sort(updatePackageList);
                String maxVersion = updatePackageList.get(updatePackageList.size() - 1).getVersion();
                String subVersion = maxVersion.substring(maxVersion.lastIndexOf(".") + 1);
                newUpdatePackageVersion = maxVersion.substring(0, maxVersion.lastIndexOf(".")) + "." + (Integer.parseInt(subVersion) + 1);
            }
        }
        setReturnData("{'newUpdatePackageVersion' : '" + newUpdatePackageVersion + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}

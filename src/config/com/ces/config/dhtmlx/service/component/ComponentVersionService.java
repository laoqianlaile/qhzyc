package com.ces.config.dhtmlx.service.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppButtonDao;
import com.ces.config.dhtmlx.dao.appmanage.AppColumnDao;
import com.ces.config.dhtmlx.dao.appmanage.AppDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.AppFormDao;
import com.ces.config.dhtmlx.dao.appmanage.AppFormElementDao;
import com.ces.config.dhtmlx.dao.appmanage.AppGridDao;
import com.ces.config.dhtmlx.dao.appmanage.AppReportDao;
import com.ces.config.dhtmlx.dao.appmanage.AppSearchDao;
import com.ces.config.dhtmlx.dao.appmanage.AppSearchPanelDao;
import com.ces.config.dhtmlx.dao.appmanage.AppSortDao;
import com.ces.config.dhtmlx.dao.appmanage.ModuleDao;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalTableDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.TableRelationDao;
import com.ces.config.dhtmlx.dao.appmanage.TableTreeDao;
import com.ces.config.dhtmlx.dao.appmanage.TreeDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowButtonSettingDao;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowFormSettingDao;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowVersionDao;
import com.ces.config.dhtmlx.dao.authority.AuthorityComponentButtonDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.component.ComponentClassDao;
import com.ces.config.dhtmlx.dao.component.ComponentDao;
import com.ces.config.dhtmlx.dao.component.ComponentJarDao;
import com.ces.config.dhtmlx.dao.component.ComponentSystemParameterDao;
import com.ces.config.dhtmlx.dao.component.ComponentTableColumnRelationDao;
import com.ces.config.dhtmlx.dao.component.ComponentVersionDao;
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
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplice;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplit;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableRelation;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.TableRelation;
import com.ces.config.dhtmlx.entity.appmanage.TableTree;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowButtonSetting;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowFormSetting;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowTree;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowVersion;
import com.ces.config.dhtmlx.entity.component.CommonComponentRelation;
import com.ces.config.dhtmlx.entity.component.Component;
import com.ces.config.dhtmlx.entity.component.ComponentButton;
import com.ces.config.dhtmlx.entity.component.ComponentCallback;
import com.ces.config.dhtmlx.entity.component.ComponentCallbackParam;
import com.ces.config.dhtmlx.entity.component.ComponentClass;
import com.ces.config.dhtmlx.entity.component.ComponentColumn;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
import com.ces.config.dhtmlx.entity.component.ComponentFunction;
import com.ces.config.dhtmlx.entity.component.ComponentFunctionData;
import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.entity.component.ComponentJar;
import com.ces.config.dhtmlx.entity.component.ComponentOutputParam;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameter;
import com.ces.config.dhtmlx.entity.component.ComponentTable;
import com.ces.config.dhtmlx.entity.component.ComponentTableColumnRelation;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
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
import com.ces.config.dhtmlx.service.appmanage.ColumnSpliceService;
import com.ces.config.dhtmlx.service.appmanage.ColumnSplitService;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupRelationService;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.LogicTableRelationService;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupRelationService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.TableTreeService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.dhtmlx.service.appmanage.TriggerService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowButtonSettingService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowFormSettingService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowTreeService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
import com.ces.config.dhtmlx.service.authority.AuthorityDataService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.systemcomponent.SystemComponentVersionService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CfgCommonUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.ComponentInfoUtil;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.DateUtil;
import com.ces.config.utils.FileUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.SystemParameterUtil;
import com.ces.config.utils.TableUtil;

/**
 * 构件版本Service
 * 
 * @author wanglei
 * @date 2013-07-22
 */
@org.springframework.stereotype.Component("componentVersionService")
public class ComponentVersionService extends ConfigDefineDaoService<ComponentVersion, ComponentVersionDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentVersionDao")
    @Override
    protected void setDaoUnBinding(ComponentVersionDao dao) {
        super.setDaoUnBinding(dao);
    }

    @Override
    public ComponentVersion getByID(String id) {
        ComponentVersion componentVersion = ComponentInfoUtil.getInstance().getComponentVersion(id);
        if (componentVersion == null) {
            componentVersion = super.getByID(id);
        }
        return componentVersion;
    }

    /**
     * 根据构件ID和版本号获取构件版本
     * 
     * @param componentId 构件ID
     * @param version 构件版本号
     * @return ComponentVersion
     */
    public ComponentVersion getComponentVersionByComponentIdAndVersion(String componentId, String version) {
        return getDao().getByComponentIdAndVersion(componentId, version);
    }

    /**
     * 根据构件名称和构件版本号获取构件版本
     * 
     * @param componentName 构件名称
     * @param version 构件版本号
     * @return ComponentVersion
     */
    public ComponentVersion getByComponentNameAndVersion(String componentName, String version) {
        return getDao().getByComponentNameAndVersion(componentName, version);
    }

    /**
     * 根据构件名称获取构件版本
     * 
     * @param componentName 构件名称
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getComponentVersionListByComponentName(String componentName) {
        return getDao().getByComponentName(componentName);
    }

    /**
     * 根据构件ID获取构件版本
     * 
     * @param componentId 构件ID
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getComponentVersionListByComponentId(String componentId) {
        return getDao().getByComponentId(componentId);
    }

    /**
     * 获取页面构件、自定义构件(4-物理表构件 5-逻辑表构件 6-通用表构件)和组合构件
     * 
     * @param isSystemUsed 是否被本系统使用
     * @return List<String[]>
     */
    public List<String[]> getPageCVInfoList(String isSystemUsed) {
        List<String[]> pageCVInfoList = new ArrayList<String[]>();
        List<Object[]> list = getDao().getPageCVInfoList(isSystemUsed);
        if (CollectionUtils.isNotEmpty(list)) {
            String[] strs = null;
            for (Object[] objs : list) {
                strs = new String[2];
                strs[0] = String.valueOf(objs[0]);
                strs[1] = String.valueOf(objs[1]) + "_" + String.valueOf(objs[2]);
                pageCVInfoList.add(strs);
            }
        }
        return pageCVInfoList;
    }

    /**
     * 获取页面构件、自定义构件(4-物理表构件 5-逻辑表构件 6-通用表构件)和组合构件
     * 
     * @return List<Object[]>
     */
    public List<String[]> getPageCVInfoList() {
        List<String[]> pageCVInfoList = new ArrayList<String[]>();
        List<Object[]> list = getDao().getPageCVInfoList();
        if (CollectionUtils.isNotEmpty(list)) {
            String[] strs = null;
            for (Object[] objs : list) {
                strs = new String[2];
                strs[0] = String.valueOf(objs[0]);
                strs[1] = String.valueOf(objs[1]) + "_" + String.valueOf(objs[2]);
                pageCVInfoList.add(strs);
            }
        }
        return pageCVInfoList;
    }

    /**
     * 根据构件分类ID获取构件版本
     * 
     * @param areaId 构件分类ID
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getComponentVersionListByAreaId(String areaId) {
        return getDao().getByAreaId(areaId);
    }

    /**
     * 根据构件分类ID获取组合构件版本
     * 
     * @param areaId 构件分类ID
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getComponentVersionListByAssembleAreaId(String areaId) {
        return getDao().getComponentVersionListByAssembleAreaId(areaId);
    }

    /**
     * 获取自定义构件版本
     * 
     * @param code 构件编码
     * @return ComponentVersion
     */
    public ComponentVersion getSelfDefineComponent(String code) {
        ComponentVersion componentVersion = null;
        Component component = getDaoFromContext(ComponentDao.class).getByCode(code);
        if (component != null) {
            List<ComponentVersion> componentVersionList = getDao().getByComponentId(component.getId());
            if (CollectionUtils.isNotEmpty(componentVersionList)) {
                componentVersion = componentVersionList.get(0);
            }
        }
        return componentVersion;
    }

    /**
     * 获取多个构件版本
     * 
     * @param componentVersionIds 根据IDs获取构件版本
     * @return List<Menu>
     */
    public List<ComponentVersion> getComponentVersionsByIds(String componentVersionIds) {
        List<ComponentVersion> list = new ArrayList<ComponentVersion>();
        if (StringUtil.isNotEmpty(componentVersionIds)) {
            DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
            String hql = "select t from ComponentVersion t where t.id in('" + componentVersionIds.replace(",", "','") + "')";
            list = dao.queryEntityForList(hql, ComponentVersion.class);
        }
        return list;
    }

    /**
     * 保存构件
     * 
     * @param componentConfig 构件配置信息
     * @param deleteComponentFile 是否删除构件文件
     * @return ComponentVersion
     * @throws Exception
     */
    @Transactional
    public ComponentVersion saveComponentConfig(ComponentConfig componentConfig, boolean deleteComponentFile) throws Exception {
        // 如果自定义构件的config.json中包含componentVersion，那么用新的导入方式导入
        boolean flag = false;
        if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentConfig.getType()) != -1 || ConstantVar.Component.Type.TAB.equals(componentConfig.getType())) {
            flag = true;
            Map<String, Object> selfDefineConfig = componentConfig.getSelfDefineConfig();
            if (selfDefineConfig.get("componentVersion") == null) {
                throw new Exception("构件包太旧，请重新打包！");
            }
        }
        ComponentVersion componentVersion = null;
        if (flag) {
            // 如果是自定义构件，保存自定义构件的配置信息
            Map<String, Object> selfDefineConfig = componentConfig.getSelfDefineConfig();
            componentVersion = saveSelfDefineConfig(selfDefineConfig, ServletActionContext.getRequest().getParameter("areaId"), false);
            // 修改使用了该构件作为基础构件的组合构件的url
            List<Construct> constructList = getService(ConstructService.class).getByBaseComponentVersionId(componentVersion.getId());
            if (CollectionUtils.isNotEmpty(constructList)) {
                for (Construct construct : constructList) {
                    ComponentVersion assembleComponentVersion = construct.getAssembleComponentVersion();
                    String url = componentVersion.getUrl();
                    if (url.indexOf("?") != -1) {
                        url += "&constructId=" + construct.getId();
                    } else {
                        url += "?constructId=" + construct.getId();
                    }
                    assembleComponentVersion.setUrl(url);
                    getDao().save(assembleComponentVersion);
                    ComponentInfoUtil.getInstance().putComponentVersion(assembleComponentVersion);
                }
            }
        } else {
            // 构件信息入库
            componentVersion = saveComponentInfo(componentConfig, deleteComponentFile);
            // 保存构件class
            saveComponentClass(componentVersion, componentConfig);
            // 保存构件jars
            saveComponentJar(componentVersion, componentConfig);
            // 保存构件与系统参数关系
            saveSystemParameters(componentVersion, componentConfig);
            // 保存构件自身配置参数
            saveComponentSelfParam(componentVersion, componentConfig);
            // 保存构件入参
            saveComponentInputParam(componentVersion, componentConfig);
            // 保存构件出参
            saveComponentOutputParam(componentVersion, componentConfig);
            if (ConstantVar.Component.Type.PAGE.equals(componentConfig.getType())
                    || ConstantVar.Component.Type.TRANSFER_DEVICE.equals(componentConfig.getType())) {
                // 保存构件预留区
                saveComponentReserveZone(componentVersion, componentConfig);
            }
            // 保存构件页面JS方法
            saveComponentFunction(componentVersion, componentConfig);
            // 保存构件页面JS方法(供构件关闭时使用)
            saveComponentCallback(componentVersion, componentConfig);
            // 保存构件相关表、字段以及之间的关系
            saveComponentTables(componentVersion, componentConfig);
            // 保存构件权限按钮
            saveComponentButtons(componentVersion, componentConfig);
            // 修改使用了该构件作为基础构件的组合构件的url
            List<Construct> constructList = getService(ConstructService.class).getByBaseComponentVersionId(componentVersion.getId());
            if (CollectionUtils.isNotEmpty(constructList)) {
                for (Construct construct : constructList) {
                    ComponentVersion assembleComponentVersion = construct.getAssembleComponentVersion();
                    String url = componentVersion.getUrl();
                    if (StringUtil.isNotEmpty(url)) {
                        if (url.indexOf("?") != -1) {
                            url += "&constructId=" + construct.getId();
                        } else {
                            url += "?constructId=" + construct.getId();
                        }
                    }
                    assembleComponentVersion.setUrl(url);
                    getDao().save(assembleComponentVersion);
                }
            }
        }
        ComponentInfoUtil.getInstance().putComponentVersion(componentVersion);
        return componentVersion;
    }

    /**
     * 构件信息入库
     * 
     * @param componentConfig 构件配置
     * @param deleteComponentFile 是否删除构件文件
     * @return ComponentVersion
     */
    @Transactional
    private ComponentVersion saveComponentInfo(ComponentConfig componentConfig, boolean deleteComponentFile) {
        HttpServletRequest request = ServletActionContext.getRequest();
        Component component = getService(ComponentService.class).getComponentByName(componentConfig.getName());
        ComponentVersion componentVersion = null;
        if (component != null) {
            ComponentVersion oldComponentVersion = getComponentVersionByComponentIdAndVersion(component.getId(), componentConfig.getVersion());
            if (oldComponentVersion != null && deleteComponentFile) {
                // 删除旧的构件版本
                // deleteComponentVersion(oldComponentVersion.getId(), true);
                // 删除旧的构件版本的文件
                deleteComponentVersionFiles(oldComponentVersion.getId(), true);
            }
        }
        component = getService(ComponentService.class).getComponentByName(componentConfig.getName());
        if (component == null) {
            // 保存构件
            component = new Component();
            component.setCode(componentConfig.getCode());
            component.setName(componentConfig.getName());
            component.setAlias(componentConfig.getAlias());
            component.setType(componentConfig.getType());
            getService(ComponentService.class).save(component);
        } else {
            componentVersion = getDao().getByComponentIdAndVersion(component.getId(), componentConfig.getVersion());
        }
        if (componentVersion == null) {
            componentVersion = new ComponentVersion();
        }
        componentVersion.setComponent(component);
        componentVersion.setVersion(componentConfig.getVersion());
        componentVersion.setViews(componentConfig.getViews());
        if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentConfig.getType()) != -1) {
            if (componentConfig.getUrl().startsWith("selfdefine/")) {
                componentVersion.setUrl(componentConfig.getUrl());
            } else {
                if (componentConfig.getUrl().startsWith("/")) {
                    componentVersion.setUrl("selfdefine" + componentConfig.getUrl());
                } else {
                    componentVersion.setUrl("selfdefine/" + componentConfig.getUrl());
                }
            }
        } else if (ConstantVar.Component.Type.PAGE.equals(componentConfig.getType())) {
            if (componentConfig.getUrl().startsWith("component/")) {
                componentVersion.setUrl(componentConfig.getUrl());
            } else {
                if (componentConfig.getUrl().startsWith("/")) {
                    componentVersion.setUrl("component" + componentConfig.getUrl());
                } else {
                    componentVersion.setUrl("component/" + componentConfig.getUrl());
                }
            }
        } else if (ConstantVar.Component.Type.LOGIC.equals(componentConfig.getType())) {
            componentVersion.setUrl(componentConfig.getUrl());
        }
        componentVersion.setBeforeClickJs(componentConfig.getBeforeClickJs());
        componentVersion.setRemark(componentConfig.getRemark());
        if (componentVersion.getAreaId() == null) {
            componentVersion.setAreaId(request.getParameter("areaId"));
        }
        componentVersion.setPath(componentConfig.getPackageFileName());
        componentVersion.setImportDate(new Date());
        componentVersion.setIsPackage(ConstantVar.Component.Packaged.YES);
        List<ComponentSystemParameter> componentSystemParameterList = componentConfig.getComponentSystemParameters();
        if (CollectionUtils.isNotEmpty(componentSystemParameterList)) {
            componentVersion.setSystemParamConfig(ConstantVar.Component.SystemParamConfig.NOT_FINISHED);
        } else {
            componentVersion.setSystemParamConfig(ConstantVar.Component.SystemParamConfig.NOT_NEED);
        }
        if (CfgCommonUtil.isReleasedSystem() && ConstantVar.Component.Type.SELF_DEFINE.indexOf(component.getType()) != -1) {
            componentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.YES);
        } else {
            componentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.NO);
        }
        if (componentVersion.getButtonUse() == null) {
            if (ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                componentVersion.setButtonUse("0");
            } else if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())) {
                componentVersion.setButtonUse("1");
            } else if (ConstantVar.Component.Type.LOGIC.equals(componentVersion.getComponent().getType())) {
                componentVersion.setButtonUse("1");
            }
        }
        if (componentVersion.getMenuUse() == null) {
            if (ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                componentVersion.setMenuUse("0");
            } else if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())) {
                componentVersion.setMenuUse("1");
            } else if (ConstantVar.Component.Type.LOGIC.equals(componentVersion.getComponent().getType())) {
                componentVersion.setMenuUse("0");
            }
        }
        componentVersion = getDao().save(componentVersion);
        return componentVersion;
    }

    /**
     * 保存构件class
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveComponentClass(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        // 保存前先删除构件之前的类
        getDaoFromContext(ComponentClassDao.class).deleteByComponentVersionId(componentVersion.getId());
        String packageName = componentConfig.getPackageFileName().substring(0, componentConfig.getPackageFileName().lastIndexOf("."));
        String tempDir = ComponentFileUtil.getTempCompUncompressPath() + packageName + "/classes";
        List<String> classes = new ArrayList<String>();
        getClasses(new File(tempDir), classes);
        if (!classes.isEmpty()) {
            List<ComponentClass> componentClassList = new ArrayList<ComponentClass>();
            ComponentClass componentClass = null;
            for (String clazz : classes) {
                componentClass = new ComponentClass();
                componentClass.setName(clazz);
                componentClass.setComponentVersionId(componentVersion.getId());
                getService(ComponentClassService.class).save(componentClass);
                componentClassList.add(componentClass);
            }
            componentVersion.setComponentClassList(componentClassList);
        }
    }

    /**
     * 获取构件包中的class文件
     * 
     * @param classDir 构件的class文件路径
     * @param classes 构件的classes
     */
    @Transactional
    private void getClasses(File classDir, List<String> classes) {
        if (classDir.exists() && classDir.isDirectory()) {
            File[] files = classDir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    getClasses(file, classes);
                } else if (file.getName().endsWith(".class")) {
                    String path = file.getPath();
                    path = path.substring(path.indexOf("classes") + 8).replace('\\', '/');
                    classes.add(path);
                }
            }
        }
    }

    /**
     * 保存构件class
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveComponentJar(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        // 保存前先删除构件中之前的Jar
        getDaoFromContext(ComponentJarDao.class).deleteByComponentVersionId(componentVersion.getId());
        String packageName = componentConfig.getPackageFileName().substring(0, componentConfig.getPackageFileName().lastIndexOf("."));
        String tempDir = ComponentFileUtil.getTempCompUncompressPath() + packageName + "/jar";
        List<String> jars = new ArrayList<String>();
        File jarDir = new File(tempDir);
        File[] files = jarDir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    jars.add(file.getName());
                }
            }
        }
        if (!jars.isEmpty()) {
            List<ComponentJar> componentJarList = new ArrayList<ComponentJar>();
            ComponentJar componentJar = null;
            for (String jar : jars) {
                componentJar = new ComponentJar();
                componentJar.setName(jar);
                componentJar.setComponentVersionId(componentVersion.getId());
                getService(ComponentJarService.class).save(componentJar);
                componentJarList.add(componentJar);
            }
            componentVersion.setComponentJarList(componentJarList);
        }
    }

    /**
     * 保存构件入参
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveComponentInputParam(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        List<ComponentInputParam> oldInputParams = getService(ComponentInputParamService.class).getByComponentVersionId(componentVersion.getId());
        ComponentInputParam oldInputParam = null;
        List<ComponentInputParam> paramList = componentConfig.getInputParams();
        // 保存后的构件输入参数
        List<ComponentInputParam> savedInputParamList = new ArrayList<ComponentInputParam>();
        if (CollectionUtils.isNotEmpty(paramList)) {
            boolean flag = true;
            for (ComponentInputParam inputParam : paramList) {
                flag = true;
                if (CollectionUtils.isNotEmpty(oldInputParams)) {
                    for (Iterator<ComponentInputParam> iterator = oldInputParams.iterator(); iterator.hasNext();) {
                        oldInputParam = iterator.next();
                        if (inputParam.getName().equals(oldInputParam.getName())) {
                            flag = false;
                            if (!inputParam.getRemark().equals(oldInputParam.getRemark())) {
                                oldInputParam.setRemark(inputParam.getRemark());
                                getService(ComponentInputParamService.class).save(oldInputParam);
                            }
                            iterator.remove();
                            savedInputParamList.add(oldInputParam);
                            break;
                        }
                    }
                }
                if (flag) {
                    inputParam.setComponentVersionId(componentVersion.getId());
                    getService(ComponentInputParamService.class).save(inputParam);
                    savedInputParamList.add(inputParam);
                }
            }
            if (CollectionUtils.isNotEmpty(oldInputParams)) {
                for (ComponentInputParam inputParam : oldInputParams) {
                    getService(ComponentInputParamService.class).delete(inputParam.getId());
                }
            }
            ComponentParamsUtil.putComponentInputParamList(componentVersion.getId(), savedInputParamList);
        }
    }

    /**
     * 保存构件出参
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveComponentOutputParam(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        List<ComponentOutputParam> oldOutputParams = getService(ComponentOutputParamService.class).getByComponentVersionId(componentVersion.getId());
        ComponentOutputParam oldOutputParam = null;
        List<ComponentOutputParam> outputParamList = componentConfig.getOutputParams();
        if (CollectionUtils.isNotEmpty(outputParamList)) {
            boolean flag = true;
            for (ComponentOutputParam outputParam : outputParamList) {
                flag = true;
                if (CollectionUtils.isNotEmpty(oldOutputParams)) {
                    for (Iterator<ComponentOutputParam> iterator = oldOutputParams.iterator(); iterator.hasNext();) {
                        oldOutputParam = iterator.next();
                        if (outputParam.getName().equals(oldOutputParam.getName())) {
                            flag = false;
                            if (!outputParam.getRemark().equals(oldOutputParam.getRemark())) {
                                oldOutputParam.setRemark(outputParam.getRemark());
                                getService(ComponentOutputParamService.class).save(oldOutputParam);
                            }
                            iterator.remove();
                            break;
                        }
                    }
                }
                if (flag) {
                    outputParam.setComponentVersionId(componentVersion.getId());
                    getService(ComponentOutputParamService.class).save(outputParam);
                }
            }
            if (CollectionUtils.isNotEmpty(oldOutputParams)) {
                for (ComponentOutputParam outputParam : oldOutputParams) {
                    getService(ComponentOutputParamService.class).delete(outputParam.getId());
                }
            }
        }
    }

    /**
     * 保存构件预留区
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveComponentReserveZone(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        List<ComponentReserveZone> oldReserveZones = getService(ComponentReserveZoneService.class).getByComponentVersionId(componentVersion.getId());
        ComponentReserveZone oldReserveZone = null;
        List<ComponentReserveZone> reserveZoneList = componentConfig.getReserveZones();
        if (CollectionUtils.isNotEmpty(reserveZoneList)) {
            boolean flag = true;
            for (ComponentReserveZone reserveZone : reserveZoneList) {
                flag = true;
                if (CollectionUtils.isNotEmpty(oldReserveZones)) {
                    for (Iterator<ComponentReserveZone> iterator = oldReserveZones.iterator(); iterator.hasNext();) {
                        oldReserveZone = iterator.next();
                        if (reserveZone.getName().equals(oldReserveZone.getName())) {
                            flag = false;
                            oldReserveZone.setAlias(reserveZone.getAlias());
                            oldReserveZone.setType(reserveZone.getType());
                            oldReserveZone.setPage(reserveZone.getPage());
                            getService(ComponentReserveZoneService.class).save(oldReserveZone);
                            iterator.remove();
                            break;
                        }
                    }
                }
                if (flag) {
                    reserveZone.setComponentVersionId(componentVersion.getId());
                    reserveZone.setIsCommon(false);
                    getService(ComponentReserveZoneService.class).save(reserveZone);
                }
            }
            if (CollectionUtils.isNotEmpty(oldReserveZones)) {
                for (ComponentReserveZone reserveZone : oldReserveZones) {
                    getService(ComponentReserveZoneService.class).delete(reserveZone.getId());
                }
            }
        }
    }

    /**
     * 保存构件页面JS方法
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveComponentFunction(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        List<ComponentFunction> oldFunctions = getService(ComponentFunctionService.class).getByComponentVersionId(componentVersion.getId());
        ComponentFunction oldFunction = null;
        ComponentFunctionData oldFunctionData = null;
        List<ComponentFunction> functionList = componentConfig.getFunctions();
        if (CollectionUtils.isNotEmpty(functionList)) {
            boolean flag = true;
            for (ComponentFunction function : functionList) {
                flag = true;
                if (CollectionUtils.isNotEmpty(oldFunctions)) {
                    for (Iterator<ComponentFunction> iterator = oldFunctions.iterator(); iterator.hasNext();) {
                        oldFunction = iterator.next();
                        if (function.getName().equals(oldFunction.getName())) {
                            flag = false;
                            if (!function.getPage().equals(oldFunction.getPage()) || !function.getRemark().equals(oldFunction.getRemark())) {
                                oldFunction.setPage(function.getPage());
                                oldFunction.setRemark(function.getRemark());
                                getService(ComponentFunctionService.class).save(oldFunction);
                            }
                            List<ComponentFunctionData> oldFunctionDatas = getService(ComponentFunctionDataService.class).getByFunctionId(oldFunction.getId());
                            List<ComponentFunctionData> functionDataList = function.getComponentFunctionDataList();
                            if (CollectionUtils.isNotEmpty(functionDataList)) {
                                boolean dataFlag = true;
                                for (ComponentFunctionData functionData : functionDataList) {
                                    dataFlag = true;
                                    if (CollectionUtils.isNotEmpty(oldFunctionDatas)) {
                                        for (Iterator<ComponentFunctionData> dataIterator = oldFunctionDatas.iterator(); dataIterator.hasNext();) {
                                            oldFunctionData = dataIterator.next();
                                            if (functionData.getName().equals(oldFunctionData.getName())) {
                                                dataFlag = false;
                                                if (!functionData.getRemark().equals(oldFunctionData.getRemark())) {
                                                    oldFunctionData.setRemark(functionData.getRemark());
                                                    getService(ComponentFunctionDataService.class).save(oldFunctionData);
                                                }
                                                dataIterator.remove();
                                                break;
                                            }
                                        }
                                    }
                                    if (dataFlag) {
                                        functionData.setFunctionId(oldFunction.getId());
                                        getService(ComponentFunctionDataService.class).save(functionData);
                                    }
                                }
                                if (CollectionUtils.isNotEmpty(oldFunctionDatas)) {
                                    for (ComponentFunctionData functionData : oldFunctionDatas) {
                                        getService(ComponentFunctionDataService.class).delete(functionData.getId());
                                    }
                                }
                            }
                            iterator.remove();
                            break;
                        }
                    }
                }
                if (flag) {
                    function.setComponentVersionId(componentVersion.getId());
                    getService(ComponentFunctionService.class).save(function);
                    List<ComponentFunctionData> functionDataList = function.getComponentFunctionDataList();
                    if (CollectionUtils.isNotEmpty(functionDataList)) {
                        for (ComponentFunctionData functionData : functionDataList) {
                            functionData.setFunctionId(function.getId());
                            getService(ComponentFunctionDataService.class).save(functionData);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(oldFunctions)) {
                for (ComponentFunction function : oldFunctions) {
                    getService(ComponentFunctionService.class).delete(function.getId());
                }
            }
        }
    }

    /**
     * 保存构件页面JS方法(供构件关闭时使用)
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveComponentCallback(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        List<ComponentCallback> oldCallbacks = getService(ComponentCallbackService.class).getByComponentVersionId(componentVersion.getId());
        ComponentCallback oldCallback = null;
        ComponentCallbackParam oldCallbackParam = null;
        List<ComponentCallback> callbackList = componentConfig.getCallbacks();
        if (CollectionUtils.isNotEmpty(callbackList)) {
            boolean flag = true;
            for (ComponentCallback callback : callbackList) {
                flag = true;
                if (CollectionUtils.isNotEmpty(oldCallbacks)) {
                    for (Iterator<ComponentCallback> iterator = oldCallbacks.iterator(); iterator.hasNext();) {
                        oldCallback = iterator.next();
                        if (callback.getName().equals(oldCallback.getName())) {
                            flag = false;
                            if (!callback.getPage().equals(oldCallback.getPage())
                                    || !StringUtil.null2empty(callback.getRemark()).equals(oldCallback.getRemark())) {
                                oldCallback.setPage(callback.getPage());
                                oldCallback.setRemark(callback.getRemark());
                                getService(ComponentCallbackService.class).save(oldCallback);
                            }
                            List<ComponentCallbackParam> oldCallbackParams = getService(ComponentCallbackParamService.class).getByCallbackId(
                                    oldCallback.getId());
                            List<ComponentCallbackParam> callbackParamList = callback.getComponentCallbackParamList();
                            if (CollectionUtils.isNotEmpty(callbackParamList)) {
                                boolean paramFlag = true;
                                for (ComponentCallbackParam callbackParam : callbackParamList) {
                                    paramFlag = true;
                                    if (CollectionUtils.isNotEmpty(oldCallbackParams)) {
                                        for (Iterator<ComponentCallbackParam> dataIterator = oldCallbackParams.iterator(); dataIterator.hasNext();) {
                                            oldCallbackParam = dataIterator.next();
                                            if (callbackParam.getName().equals(oldCallbackParam.getName())) {
                                                paramFlag = false;
                                                if (!callbackParam.getRemark().equals(oldCallbackParam.getRemark())) {
                                                    oldCallbackParam.setRemark(callbackParam.getRemark());
                                                    getService(ComponentCallbackParamService.class).save(oldCallbackParam);
                                                }
                                                dataIterator.remove();
                                                break;
                                            }
                                        }
                                    }
                                    if (paramFlag) {
                                        callbackParam.setCallbackId(oldCallback.getId());
                                        getService(ComponentCallbackParamService.class).save(callbackParam);
                                    }
                                }
                                if (CollectionUtils.isNotEmpty(oldCallbackParams)) {
                                    for (ComponentCallbackParam callbackParam : oldCallbackParams) {
                                        getService(ComponentCallbackParamService.class).delete(callbackParam.getId());
                                    }
                                }
                            }
                            iterator.remove();
                            break;
                        }
                    }
                }
                if (flag) {
                    callback.setComponentVersionId(componentVersion.getId());
                    getService(ComponentCallbackService.class).save(callback);
                    List<ComponentCallbackParam> callbackParamList = callback.getComponentCallbackParamList();
                    if (CollectionUtils.isNotEmpty(callbackParamList)) {
                        for (ComponentCallbackParam callbackParam : callbackParamList) {
                            callbackParam.setCallbackId(callback.getId());
                            getService(ComponentCallbackParamService.class).save(callbackParam);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(oldCallbacks)) {
                for (ComponentCallback callback : oldCallbacks) {
                    getService(ComponentCallbackService.class).delete(callback.getId());
                }
            }
        }
    }

    /**
     * 保存构件自身配置参数
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveComponentSelfParam(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        List<ComponentSelfParam> oldSelfParams = getService(ComponentSelfParamService.class).getByComponentVersionId(componentVersion.getId());
        ComponentSelfParam oldSelfParam = null;
        List<ComponentSelfParam> selfParamList = componentConfig.getSelfParams();
        // 保存后的构件自身参数
        List<ComponentSelfParam> savedSelfParamList = new ArrayList<ComponentSelfParam>();
        if (CollectionUtils.isNotEmpty(selfParamList)) {
            boolean flag = true;
            for (ComponentSelfParam selfParam : selfParamList) {
                flag = true;
                if (CollectionUtils.isNotEmpty(oldSelfParams)) {
                    for (Iterator<ComponentSelfParam> iterator = oldSelfParams.iterator(); iterator.hasNext();) {
                        oldSelfParam = iterator.next();
                        if (selfParam.getName().equals(oldSelfParam.getName())) {
                            flag = false;
                            oldSelfParam.setType(selfParam.getType());
                            oldSelfParam.setValue(selfParam.getValue());
                            oldSelfParam.setText(selfParam.getText());
                            oldSelfParam.setOptions(selfParam.getOptions());
                            oldSelfParam.setRemark(selfParam.getRemark());
                            getService(ComponentSelfParamService.class).save(oldSelfParam);
                            iterator.remove();
                            savedSelfParamList.add(oldSelfParam);
                            break;
                        }
                    }
                }
                if (flag) {
                    selfParam.setComponentVersionId(componentVersion.getId());
                    getService(ComponentSelfParamService.class).save(selfParam);
                    savedSelfParamList.add(selfParam);
                }
            }
            if (CollectionUtils.isNotEmpty(oldSelfParams)) {
                for (ComponentSelfParam selfParam : oldSelfParams) {
                    getService(ComponentSelfParamService.class).delete(selfParam.getId());
                }
            }
            ComponentParamsUtil.putComponentSelfParamList(componentVersion.getId(), savedSelfParamList);
        }
    }

    /**
     * 保存构件关联的系统参数
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveSystemParameters(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        List<ComponentSystemParameter> oldSysParams = getService(ComponentSystemParameterService.class).getByComponentVersionId(componentVersion.getId());
        ComponentSystemParameter oldSysParam = null;
        List<ComponentSystemParameter> componentSystemParameterList = componentConfig.getComponentSystemParameters();
        // 保存后的构件系统参数
        List<ComponentSystemParameter> savedComponentSystemParameterList = new ArrayList<ComponentSystemParameter>();
        if (CollectionUtils.isNotEmpty(componentSystemParameterList)) {
            boolean flag = true;
            boolean configFinished = true;
            for (ComponentSystemParameter componentSystemParameter : componentSystemParameterList) {
                flag = true;
                if (CollectionUtils.isNotEmpty(oldSysParams)) {
                    for (Iterator<ComponentSystemParameter> iterator = oldSysParams.iterator(); iterator.hasNext();) {
                        oldSysParam = iterator.next();
                        if (componentSystemParameter.getName().equals(oldSysParam.getName())) {
                            flag = false;
                            if (!componentSystemParameter.getRemark().equals(oldSysParam.getRemark())) {
                                oldSysParam.setRemark(componentSystemParameter.getRemark());
                                getService(ComponentSystemParameterService.class).save(oldSysParam);
                            }
                            iterator.remove();
                            savedComponentSystemParameterList.add(oldSysParam);
                            break;
                        }
                    }
                }
                if (flag) {
                    componentSystemParameter.setComponentVersionId(componentVersion.getId());
                    getService(ComponentSystemParameterService.class).save(componentSystemParameter);
                    savedComponentSystemParameterList.add(componentSystemParameter);
                    configFinished = false;
                }
            }
            if (CollectionUtils.isNotEmpty(oldSysParams)) {
                for (ComponentSystemParameter sysParam : oldSysParams) {
                    getDaoFromContext(ComponentSystemParameterDao.class).delete(sysParam);
                }
            }
            if (!configFinished) {
                componentVersion.setSystemParamConfig(ConstantVar.Component.SystemParamConfig.NOT_FINISHED);
            } else {
                componentVersion.setSystemParamConfig(ConstantVar.Component.SystemParamConfig.FINISHED);
            }
            getDao().save(componentVersion);
            Map<String, ComponentSystemParameter> map = new HashMap<String, ComponentSystemParameter>();
            for (ComponentSystemParameter sp : savedComponentSystemParameterList) {
                map.put(sp.getId(), sp);
            }
            ComponentParamsUtil.putComponentSystemParamMap(componentVersion.getId(), map);
        }
    }

    /**
     * 保存构件关联的表
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveComponentTables(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        // 删除构件相关表
        getDaoFromContext(ComponentTableColumnRelationDao.class).deleteByComponentVersionId(componentVersion.getId());
        getDaoFromContext(ComponentTableColumnRelationDao.class).deleteComponentTable();
        getDaoFromContext(ComponentTableColumnRelationDao.class).deleteComponentColumn();
        // 新增构件相关表
        List<ComponentTable> componentTableList = componentConfig.getComponentTables();
        if (CollectionUtils.isNotEmpty(componentTableList)) {
            for (ComponentTable componentTable : componentTableList) {
                getService(ComponentTableService.class).save(componentTable);
                if (ConstantVar.Judgment.YES.equals(componentTable.getIsSelfdefine())) {
                    ComponentTableColumnRelation componentTableColumnRelation = new ComponentTableColumnRelation();
                    componentTableColumnRelation.setComponentVersionId(componentVersion.getId());
                    componentTableColumnRelation.setTableId(componentTable.getId());
                    getService(ComponentTableColumnRelationService.class).save(componentTableColumnRelation);
                }
                List<ComponentColumn> componentColumnList = componentTable.getComponentColumnList();
                if (CollectionUtils.isNotEmpty(componentColumnList)) {
                    ComponentTableColumnRelation componentTableColumnRelation = null;
                    for (ComponentColumn componentColumn : componentColumnList) {
                        // 保存构件表中的字段
                        getService(ComponentColumnService.class).save(componentColumn);
                        // 保存构件版本、构件关联表、字段的关联关系
                        componentTableColumnRelation = new ComponentTableColumnRelation();
                        componentTableColumnRelation.setComponentVersionId(componentVersion.getId());
                        componentTableColumnRelation.setTableId(componentTable.getId());
                        componentTableColumnRelation.setColumnId(componentColumn.getId());
                        getService(ComponentTableColumnRelationService.class).save(componentTableColumnRelation);
                    }
                }
            }
        }
    }

    /**
     * 保存构件权限按钮
     * 
     * @param componentVersion 构件版本
     * @param componentConfig 构件配置
     */
    @Transactional
    private void saveComponentButtons(ComponentVersion componentVersion, ComponentConfig componentConfig) {
        List<ComponentButton> oldButtons = getService(ComponentButtonService.class).getByComponentVersionId(componentVersion.getId());
        ComponentButton oldButton = null;
        List<ComponentButton> componentButtonList = componentConfig.getButtons();
        if (CollectionUtils.isNotEmpty(componentButtonList)) {
            boolean flag = true;
            for (ComponentButton componentButton : componentButtonList) {
                flag = true;
                if (CollectionUtils.isNotEmpty(oldButtons)) {
                    for (Iterator<ComponentButton> iterator = oldButtons.iterator(); iterator.hasNext();) {
                        oldButton = iterator.next();
                        if (componentButton.getName().equals(oldButton.getName())) {
                            flag = false;
                            if (!componentButton.getDisplayName().equals(oldButton.getDisplayName())) {
                                oldButton.setDisplayName(componentButton.getDisplayName());
                                getService(ComponentButtonService.class).save(oldButton);
                            }
                            iterator.remove();
                            break;
                        }
                    }
                }
                if (flag) {
                    componentButton.setComponentVersionId(componentVersion.getId());
                    getService(ComponentButtonService.class).save(componentButton);
                }
            }
            if (CollectionUtils.isNotEmpty(oldButtons)) {
                for (ComponentButton button : oldButtons) {
                    getDaoFromContext(AuthorityComponentButtonDao.class).deleteByComponentButtonId(button.getId());
                    getService(ComponentButtonService.class).delete(button);
                }
            }
        }
    }

    /**
     * 保存导入的组合构件中的基础构件
     * 
     * @param componentVersion 构件版本
     * @parem selfDefineConfig 自定义构件的配置信息
     * @param modifiedIdMap 修改过的IDs
     */
    @Transactional
    public void saveBaseComponentVersionOfAssemble(ComponentVersion componentVersion, Map<String, Object> selfDefineConfig,
            Map<String, Map<String, String>> modifiedIdMap) {
        if (selfDefineConfig != null) {
            saveSelfDefineConfig(selfDefineConfig, componentVersion.getAreaId(), false);
        } else {
            // 保存开发的基础构件版本信息
            saveDevelopComponentVersion(componentVersion, modifiedIdMap);
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
     * 保存开发的基础构件版本信息
     * 
     * @param baseComponentVersion 构件信息
     * @param modifiedIdMap 修改过的IDs
     */
    @Transactional
    private void saveDevelopComponentVersion(ComponentVersion baseComponentVersion, Map<String, Map<String, String>> modifiedIdMap) {
        ComponentVersion componentVersion = null;
        boolean existDbComponent = false;
        List<Map<String, Object>> reserveZoneMapList = baseComponentVersion.getReserveZoneMapList();
        List<Map<String, String>> systemParamMapList = baseComponentVersion.getSystemParamMapList();
        List<Map<String, String>> selfParamMapList = baseComponentVersion.getSelfParamMapList();
        List<Map<String, String>> inputParamMapList = baseComponentVersion.getInputParamMapList();
        List<Map<String, String>> outputParamMapList = baseComponentVersion.getOutputParamMapList();
        List<Map<String, Object>> functionMapList = baseComponentVersion.getFunctionMapList();
        List<Map<String, Object>> callbackMapList = baseComponentVersion.getCallbackMapList();
        List<Map<String, String>> buttonMapList = baseComponentVersion.getButtonMapList();
        List<Map<String, String>> classMapList = baseComponentVersion.getClassMapList();
        List<Map<String, String>> jarMapList = baseComponentVersion.getJarMapList();
        List<Map<String, String>> comTableColumnRelationMapList = baseComponentVersion.getComponentTableColumnRelationMapList();
        List<Map<String, Object>> tableMapList = baseComponentVersion.getTableMapList();
        List<Map<String, String>> commonComponentRelationMapList = baseComponentVersion.getCommonComponentRelationMapList();
        // Component
        String componentId = baseComponentVersion.getComponent().getId();
        String componentName = baseComponentVersion.getComponent().getName();
        Component component = getService(ComponentService.class).getComponentByName(componentName);
        if (component == null) {
            component = baseComponentVersion.getComponent();
            String componentSql = "insert into t_xtpz_component(id,code,name,alias,type) values('" + componentId + "','" + component.getCode() + "','"
                    + componentName + "','" + component.getAlias() + "','" + component.getType() + "')";
            DatabaseHandlerDao.getInstance().executeSql(componentSql);
        } else {
            component.setCode(baseComponentVersion.getComponent().getCode());
            component.setAlias(baseComponentVersion.getComponent().getAlias());
            component.setType(baseComponentVersion.getComponent().getType());
            getDaoFromContext(ComponentDao.class).save(component);
        }
        // ComponentVersion
        String componentVersionId = baseComponentVersion.getId();
        String version = baseComponentVersion.getVersion();
        ComponentVersion dbComponentVersion = getByComponentNameAndVersion(componentName, version);
        if (dbComponentVersion == null) {
            Date date = new Date();
            String importDate = null;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (DatabaseHandlerDao.isOracle()) {
                importDate = "to_date('" + dateFormat.format(date) + "','yyyy-mm-dd hh24:mi:ss')";
            } else {
                importDate = "'" + dateFormat.format(date) + "'";
            }
            String isSystemUsed = ConstantVar.Component.SystemUsed.NO;
            if (CfgCommonUtil.isReleasedSystem()) {
                isSystemUsed = ConstantVar.Component.SystemUsed.YES;
            }
            String buttonUse = "0";
            if (ConstantVar.Component.Type.PAGE.equals(component.getType()) || ConstantVar.Component.Type.PAGE.equals(component.getType())) {
                buttonUse = "1";
            }
            String menuUse = "0";
            if (ConstantVar.Component.Type.PAGE.equals(component.getType())) {
                menuUse = "1";
            }
            String componentVersionSql = "insert into t_xtpz_component_version(id,component_id,version,url,remark,area_id,path"
                    + ",import_date,views,system_param_config,is_package,is_system_used,package_time,before_click_js,button_use,menu_use,area_path) values('"
                    + componentVersionId
                    + "','"
                    + componentId
                    + "','"
                    + version
                    + "','"
                    + baseComponentVersion.getUrl()
                    + "','"
                    + StringUtil.null2empty(baseComponentVersion.getRemark())
                    + "','"
                    + baseComponentVersion.getAreaId()
                    + "','"
                    + StringUtil.null2empty(baseComponentVersion.getPath())
                    + "',"
                    + importDate
                    + ",'"
                    + StringUtil.null2empty(baseComponentVersion.getViews())
                    + "','"
                    + baseComponentVersion.getSystemParamConfig()
                    + "','1','"
                    + isSystemUsed
                    + "','"
                    + StringUtil.null2empty(baseComponentVersion.getPackageTime())
                    + "','','"
                    + buttonUse
                    + "','"
                    + menuUse
                    + "','"
                    + ComponentInfoUtil.getInstance().getComponentAreaAllPath(baseComponentVersion.getAreaId()) + "')";
            DatabaseHandlerDao.getInstance().executeSql(componentVersionSql);
            componentVersion = baseComponentVersion;
        } else {
            existDbComponent = true;
            componentVersion = dbComponentVersion;
            componentVersion.setViews(baseComponentVersion.getViews());
            componentVersion.setUrl(baseComponentVersion.getUrl());
            componentVersion.setRemark(StringUtil.null2empty(baseComponentVersion.getRemark()));
            componentVersion.setAreaId(baseComponentVersion.getAreaId());
            componentVersion.setPath(StringUtil.null2empty(baseComponentVersion.getPath()));
            componentVersion.setIsPackage("1");
            componentVersion.setComponent(component);
            componentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.NO);
            componentVersion.setPackageTime(StringUtil.null2empty(baseComponentVersion.getPackageTime()));
            componentVersion.setBeforeClickJs("");
            getDao().save(componentVersion);
            if (!baseComponentVersion.getId().equals(dbComponentVersion.getId())) {
                getModifiedIdMap(modifiedIdMap, "ComponentVersion").put(baseComponentVersion.getId(), dbComponentVersion.getId());
            }
            componentVersionId = componentVersion.getId();
        }
        ComponentInfoUtil.getInstance().putComponentVersion(componentVersion);
        // 保存构件中的预留区
        saveReserveZones(existDbComponent, reserveZoneMapList, componentVersionId);
        // 保存构件中的自身参数
        saveSystemParams(existDbComponent, systemParamMapList, componentVersionId);
        // 保存构件中的自身参数
        saveSelfParams(existDbComponent, selfParamMapList, componentVersionId);
        // 保存构件中的输入参数
        saveInputParams(existDbComponent, inputParamMapList, componentVersionId);
        // 保存构件中的输出参数
        saveOutputParams(existDbComponent, outputParamMapList, componentVersionId);
        // 保存构件中的方法
        saveFunctions(existDbComponent, functionMapList, componentVersionId);
        // 保存构件中的回调函数
        saveCallbacks(existDbComponent, callbackMapList, componentVersionId);
        // 保存构件中的按钮
        saveButtons(existDbComponent, buttonMapList, componentVersionId);
        // 保存构件中的类
        saveClasses(existDbComponent, classMapList, componentVersionId);
        // 保存构件中的jar
        saveJars(existDbComponent, jarMapList, componentVersionId);
        // 保存构件中的构件、表、字段关系
        saveComponentTableColumnRelations(existDbComponent, comTableColumnRelationMapList, componentVersionId);
        // 保存自定义构件中的表
        saveTables(existDbComponent, tableMapList, componentVersionId);
        // 保存构件与公用构件的关系
        saveCommonComponentRelations(existDbComponent, commonComponentRelationMapList, componentVersionId);
    }

    /**
     * 保存自定义构件的配置信息
     * 
     * @param componentVersion 构件版本
     * @param selfDefineConfig 自定义构件配置
     * @param containSelfDefineInfo 包含自定义信息（树定义、应用定义、工作流定义、报表定义）
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public ComponentVersion saveSelfDefineConfig(Map<String, Object> selfDefineConfig, String areaId, boolean containSelfDefineInfo) {
        if (StringUtil.isEmpty(areaId)) {
            areaId = ServletActionContext.getRequest().getParameter("areaId");
        }
        Map<String, Object> componentVersionMap = (Map<String, Object>) selfDefineConfig.get("componentVersion");
        ComponentVersion componentVersion = saveSelfDefineComponentVersion(componentVersionMap, areaId);
        Module module = (Module) selfDefineConfig.get("module");
        // 保存表树
        List<Map<String, Object>> tableTreeMapList = (List<Map<String, Object>>) selfDefineConfig.get("tableTrees");
        if (CollectionUtils.isNotEmpty(tableTreeMapList)) {
            List<TableTree> dbTableTreeList = getService(TableTreeService.class).findAll();
            Map<String, TableTree> dbTableTreeMap = new HashMap<String, TableTree>();
            for (TableTree tableTree : dbTableTreeList) {
                dbTableTreeMap.put(tableTree.getId(), tableTree);
            }
            String tableTreeId = null;
            for (Map<String, Object> tableTreeMap : tableTreeMapList) {
                tableTreeId = StringUtil.null2empty(tableTreeMap.get("id"));
                if (dbTableTreeMap.keySet().contains(tableTreeId)) {
                    TableTree dbTableTree = dbTableTreeMap.get(tableTreeId);
                    dbTableTree.setName(StringUtil.null2empty(tableTreeMap.get("name")));
                    dbTableTree.setNodeType(StringUtil.null2empty(tableTreeMap.get("nodeType")));
                    dbTableTree.setCode(StringUtil.null2empty(tableTreeMap.get("code")));
                    dbTableTree.setTypeLabel(StringUtil.null2empty(tableTreeMap.get("typeLabel")));
                    dbTableTree.setClassification(StringUtil.null2empty(tableTreeMap.get("classification")));
                    dbTableTree.setTablePrefix(StringUtil.null2empty(tableTreeMap.get("tablePrefix")));
                    getDaoFromContext(TableTreeDao.class).save(dbTableTree);
                } else {
                    String tableTreeSql = "insert into t_xtpz_table_tree(id,name,node_type,code,parent_id,show_order,classification,type_label,table_prefix) values('"
                            + StringUtil.null2empty(tableTreeMap.get("id"))
                            + "','"
                            + StringUtil.null2empty(tableTreeMap.get("name"))
                            + "','"
                            + StringUtil.null2empty(tableTreeMap.get("nodeType"))
                            + "','"
                            + StringUtil.null2empty(tableTreeMap.get("code"))
                            + "','"
                            + StringUtil.null2empty(tableTreeMap.get("parentId"))
                            + "',"
                            + StringUtil.null2zero(((Integer) tableTreeMap.get("showOrder")))
                            + ",'"
                            + StringUtil.null2empty(tableTreeMap.get("classification"))
                            + "','"
                            + StringUtil.null2empty(tableTreeMap.get("typeLabel"))
                            + "','" + StringUtil.null2empty(tableTreeMap.get("tablePrefix")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(tableTreeSql);
                }
            }
        }
        // 保存逻辑表
        List<LogicTableDefine> logicTableDefineList = (List<LogicTableDefine>) selfDefineConfig.get("logicTables");
        Map<String, Map<String, ColumnDefine>> logicColumnDefineMap = (Map<String, Map<String, ColumnDefine>>) selfDefineConfig.get("logicColumns");
        saveLogicTableDefine(logicTableDefineList, logicColumnDefineMap);
        // 保存逻辑表组
        List<LogicGroupDefine> logicGroupDefineList = (List<LogicGroupDefine>) selfDefineConfig.get("logicGroups");
        saveLogicGroupDefine(logicGroupDefineList);
        // 保存逻辑表组和逻辑表关系
        List<LogicGroupRelation> logicGroupRelationList = (List<LogicGroupRelation>) selfDefineConfig.get("logicGroupRelations");
        saveLogicGroupRelationDefine(logicGroupRelationList);
        // 保存逻辑表组中逻辑表之间的关系
        List<LogicTableRelation> logicTableRelationList = (List<LogicTableRelation>) selfDefineConfig.get("logicTableRelations");
        saveLogicTableRelationDefine(logicTableRelationList, logicColumnDefineMap);
        // 保存物理表
        Map<String, PhysicalTableDefine> physicalTableDefineMap = (Map<String, PhysicalTableDefine>) selfDefineConfig.get("physicalTable");
        Map<String, Map<String, ColumnDefine>> physicalColumnDefineMap = (Map<String, Map<String, ColumnDefine>>) selfDefineConfig.get("physicalColumn");
        savePhysicalTable(physicalTableDefineMap, physicalColumnDefineMap);
        // 保存表关系
        saveTableRelation(selfDefineConfig);
        // 保存字段关联定义
        saveColumnRelation(selfDefineConfig, physicalTableDefineMap, physicalColumnDefineMap);
        // 保存物理表组
        savePhysicalGroupDefine(selfDefineConfig);
        // 保存物理表组与物理表关系
        savePhysicalGroupRelation(selfDefineConfig);
        if (containSelfDefineInfo) {
            // 保存工作流
            if (ConstantVar.Component.Type.TREE.equals(module.getType())) {
                saveWorkflow(selfDefineConfig, physicalTableDefineMap, physicalColumnDefineMap, componentVersion);
            }
        }
        // 保存模块
        saveModule(selfDefineConfig, componentVersion, module, physicalTableDefineMap, physicalColumnDefineMap, areaId);
        // 保存预留区
        // if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(module.getType())) {
        // getService(ModuleService.class).saveReserveZones(module, componentVersion.getId());
        // }
        if (containSelfDefineInfo) {
            // 保存应用自定义
            saveAppDefine(selfDefineConfig, physicalTableDefineMap, physicalColumnDefineMap, module);
        }
        ComponentInfoUtil.getInstance().putComponentVersion(componentVersion);
        return componentVersion;
    }

    /**
     * 保存自定义构件版本信息
     * 
     * @param componentVersionMap 构件信息
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private ComponentVersion saveSelfDefineComponentVersion(Map<String, Object> componentVersionMap, String areaId) {
        ComponentVersion componentVersion = null;
        boolean existDbComponent = false;
        Map<String, String> componentMap = (Map<String, String>) componentVersionMap.get("Component");
        Map<String, String> baseInfo = (Map<String, String>) componentVersionMap.get("baseInfo");
        List<Map<String, Object>> reserveZoneMapList = (List<Map<String, Object>>) componentVersionMap.get("ComponentReserveZones");
        List<Map<String, String>> selfParamMapList = (List<Map<String, String>>) componentVersionMap.get("ComponentSelfParams");
        List<Map<String, String>> inputParamMapList = (List<Map<String, String>>) componentVersionMap.get("ComponentInputParams");
        List<Map<String, String>> outputParamMapList = (List<Map<String, String>>) componentVersionMap.get("ComponentOutputParams");
        List<Map<String, Object>> functionMapList = (List<Map<String, Object>>) componentVersionMap.get("ComponentFunctions");
        List<Map<String, Object>> callbackMapList = (List<Map<String, Object>>) componentVersionMap.get("ComponentCallbacks");
        // Component
        String componentId = componentMap.get("id");
        String componentName = componentMap.get("name");
        Component component = getService(ComponentService.class).getComponentByName(componentName);
        if (component == null) {
            String componentSql = "insert into t_xtpz_component(id,code,name,alias,type) values('" + componentId + "','" + componentMap.get("code") + "','"
                    + componentName + "','" + componentMap.get("alias") + "','" + componentMap.get("type") + "')";
            DatabaseHandlerDao.getInstance().executeSql(componentSql);
            component = new Component();
            component.setId(componentId);
            component.setName(componentName);
            component.setCode(componentMap.get("code"));
            component.setAlias(componentMap.get("alias"));
            component.setType(componentMap.get("type"));
        } else {
            component.setCode(componentMap.get("code"));
            component.setAlias(componentMap.get("alias"));
            component.setType(componentMap.get("type"));
            getDaoFromContext(ComponentDao.class).save(component);
        }
        // ComponentVersion
        String componentVersionId = baseInfo.get("id");
        String version = baseInfo.get("version");
        ComponentVersion dbComponentVersion = getByComponentNameAndVersion(componentName, version);
        Date date = new Date();
        String buttonUse = "0";
        String menuUse = "0";
        String areaPath = ComponentInfoUtil.getInstance().getComponentAreaAllPath(areaId);
        if (dbComponentVersion == null) {
            String importDate = null;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (DatabaseHandlerDao.isOracle()) {
                importDate = "to_date('" + dateFormat.format(date) + "','yyyy-mm-dd hh24:mi:ss')";
            } else {
                importDate = "'" + dateFormat.format(date) + "'";
            }
            String isSystemUsed = ConstantVar.Component.SystemUsed.NO;
            if (CfgCommonUtil.isReleasedSystem()) {
                isSystemUsed = ConstantVar.Component.SystemUsed.YES;
            }
            String componentVersionSql = "insert into t_xtpz_component_version(id,component_id,version,url,remark,area_id,path"
                    + ",import_date,views,system_param_config,is_package,is_system_used,package_time,before_click_js,button_use,menu_use,area_path) values('"
                    + componentVersionId
                    + "','"
                    + componentId
                    + "','"
                    + version
                    + "','"
                    + baseInfo.get("url")
                    + "','"
                    + StringUtil.null2empty(baseInfo.get("remark"))
                    + "','"
                    + areaId
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
                    + StringUtil.null2empty(areaPath)
                    + "')";
            DatabaseHandlerDao.getInstance().executeSql(componentVersionSql);
            componentVersion = new ComponentVersion();
            componentVersion.setId(componentVersionId);
            componentVersion.setVersion(version);
            componentVersion.setViews(baseInfo.get("views"));
            componentVersion.setUrl(baseInfo.get("url"));
            componentVersion.setRemark(StringUtil.null2empty(baseInfo.get("remark")));
            componentVersion.setAreaId(areaId);
            componentVersion.setPath(baseInfo.get("path"));
            componentVersion.setImportDate(date);
            componentVersion.setIsPackage("1");
            componentVersion.setComponent(component);
            componentVersion.setSystemParamConfig(StringUtil.null2zero(baseInfo.get("systemParamConfig")));
            componentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.NO);
            componentVersion.setPackageTime(StringUtil.null2empty(baseInfo.get("packageTime")));
            componentVersion.setBeforeClickJs("");
            componentVersion.setButtonUse(buttonUse);
            componentVersion.setMenuUse(menuUse);
            componentVersion.setAreaPath(areaPath);
        } else {
            existDbComponent = true;
            componentVersion = dbComponentVersion;
            componentVersion.setViews(baseInfo.get("views"));
            componentVersion.setUrl(baseInfo.get("url"));
            componentVersion.setRemark(baseInfo.get("remark"));
            componentVersion.setAreaId(areaId);
            componentVersion.setPath(baseInfo.get("path"));
            componentVersion.setImportDate(date);
            componentVersion.setIsPackage("1");
            componentVersion.setComponent(component);
            componentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.NO);
            componentVersion.setPackageTime(baseInfo.get("packageTime"));
            componentVersion.setBeforeClickJs("");
            componentVersion.setButtonUse(buttonUse);
            componentVersion.setMenuUse(menuUse);
            componentVersion.setAreaPath(areaPath);
            getDao().save(componentVersion);
        }
        // 保存构件中的预留区
        saveReserveZones(existDbComponent, reserveZoneMapList, componentVersionId);
        // 保存构件中的自身参数
        saveSelfParams(existDbComponent, selfParamMapList, componentVersionId);
        // 保存构件中的输入参数
        saveInputParams(existDbComponent, inputParamMapList, componentVersionId);
        // 保存构件中的输出参数
        saveOutputParams(existDbComponent, outputParamMapList, componentVersionId);
        // 保存构件中的方法
        saveFunctions(existDbComponent, functionMapList, componentVersionId);
        // 保存构件中的回调函数
        saveCallbacks(existDbComponent, callbackMapList, componentVersionId);
        return componentVersion;
    }

    /**
     * 保存构件中的预留区
     */
    @Transactional
    private void saveReserveZones(boolean existDbComponent, List<Map<String, Object>> reserveZoneMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(reserveZoneMapList)) {
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> reserveZoneIdSet = new HashSet<String>();
                Set<String> updateReserveZoneIdSet = new HashSet<String>();
                Map<String, Map<String, Object>> tempMap = new HashMap<String, Map<String, Object>>();
                for (Map<String, Object> reserveZoneMap : reserveZoneMapList) {
                    reserveZoneIdSet.add(StringUtil.null2empty(reserveZoneMap.get("id")));
                    tempMap.put(StringUtil.null2empty(reserveZoneMap.get("id")), reserveZoneMap);
                }
                List<ComponentReserveZone> dbReserveZoneList = getService(ComponentReserveZoneService.class).getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbReserveZoneList)) {
                    for (ComponentReserveZone dbReserveZone : dbReserveZoneList) {
                        if (reserveZoneIdSet.contains(dbReserveZone.getId())) {
                            // 修改
                            updateReserveZoneIdSet.add(dbReserveZone.getId());
                            dbReserveZone.setComponentVersionId(componentVersionId);
                            Map<String, Object> reserveZoneMap = tempMap.get(dbReserveZone.getId());
                            dbReserveZone.setName(StringUtil.null2empty(reserveZoneMap.get("name")));
                            dbReserveZone.setAlias(StringUtil.null2empty(reserveZoneMap.get("alias")));
                            dbReserveZone.setShowOrder(reserveZoneMap.get("showOrder") != null ? ((Integer) reserveZoneMap.get("showOrder")) : 0);
                            getService(ComponentReserveZoneService.class).save(dbReserveZone);
                        } else {
                            getService(ComponentReserveZoneService.class).delete(dbReserveZone.getId());
                        }
                    }
                }
                for (Map<String, Object> reserveZoneMap : reserveZoneMapList) {
                    if (!updateReserveZoneIdSet.contains(reserveZoneMap.get("id"))) {
                        // 新增
                        String reserveZoneSql = "insert into t_xtpz_component_reserve_zone(id,component_version_id,name,alias,type,"
                                + "page,is_common,show_order) values('" + reserveZoneMap.get("id") + "','" + componentVersionId + "','"
                                + reserveZoneMap.get("name") + "','" + reserveZoneMap.get("alias") + "','" + reserveZoneMap.get("type") + "','"
                                + (StringUtil.isNotEmpty(reserveZoneMap.get("page")) ? reserveZoneMap.get("page") : "MT_page_") + "','0',"
                                + StringUtil.null2zero(reserveZoneMap.get("showOrder")) + ")";
                        DatabaseHandlerDao.getInstance().executeSql(reserveZoneSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, Object> reserveZoneMap : reserveZoneMapList) {
                    String reserveZoneSql = "insert into t_xtpz_component_reserve_zone(id,component_version_id,name,alias,type,"
                            + "page,is_common,show_order) values('" + reserveZoneMap.get("id") + "','" + componentVersionId + "','"
                            + reserveZoneMap.get("name") + "','" + reserveZoneMap.get("alias") + "','" + reserveZoneMap.get("type") + "','"
                            + (StringUtil.isNotEmpty(reserveZoneMap.get("page")) ? reserveZoneMap.get("page") : "MT_page_") + "','0',"
                            + StringUtil.null2zero(reserveZoneMap.get("showOrder")) + ")";
                    DatabaseHandlerDao.getInstance().executeSql(reserveZoneSql);
                }
            }
        }
    }

    /**
     * 保存构件中的系统参数
     */
    @Transactional
    private void saveSystemParams(boolean existDbComponent, List<Map<String, String>> systemParamMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(systemParamMapList)) {
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> systemParamIdSet = new HashSet<String>();
                Set<String> updateSystemParamIdSet = new HashSet<String>();
                Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
                for (Map<String, String> systemParamMap : systemParamMapList) {
                    systemParamIdSet.add(systemParamMap.get("id"));
                    tempMap.put(systemParamMap.get("id"), systemParamMap);
                }
                List<ComponentSystemParameter> dbSystemParamList = getService(ComponentSystemParameterService.class)
                        .getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbSystemParamList)) {
                    for (ComponentSystemParameter dbSystemParam : dbSystemParamList) {
                        if (systemParamIdSet.contains(dbSystemParam.getId())) {
                            // 修改
                            updateSystemParamIdSet.add(dbSystemParam.getId());
                            dbSystemParam.setComponentVersionId(componentVersionId);
                            Map<String, String> selfParamMap = tempMap.get(dbSystemParam.getId());
                            dbSystemParam.setName(selfParamMap.get("name"));
                            dbSystemParam.setRemark(StringUtil.null2empty(selfParamMap.get("remark")));
                            getService(ComponentSystemParameterService.class).save(dbSystemParam);
                        } else {
                            getService(ComponentSystemParameterService.class).delete(dbSystemParam.getId());
                        }
                    }
                }
                for (Map<String, String> systemParamMap : systemParamMapList) {
                    if (!updateSystemParamIdSet.contains(systemParamMap.get("id"))) {
                        // 新增
                        String systemParamSql = "insert into t_xtpz_component_system_param(id,component_version_id,name,remark) values('"
                                + systemParamMap.get("id") + "','" + componentVersionId + "','" + systemParamMap.get("name") + "','"
                                + StringUtil.null2empty(systemParamMap.get("remark")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(systemParamSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, String> systemParamMap : systemParamMapList) {
                    // 新增
                    String systemParamSql = "insert into t_xtpz_component_system_param(id,component_version_id,name,remark) values('"
                            + systemParamMap.get("id") + "','" + componentVersionId + "','" + systemParamMap.get("name") + "','"
                            + StringUtil.null2empty(systemParamMap.get("remark")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(systemParamSql);
                }
            }
        }
    }

    /**
     * 保存构件中的自身参数
     */
    @Transactional
    private void saveSelfParams(boolean existDbComponent, List<Map<String, String>> selfParamMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(selfParamMapList)) {
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> selfParamIdSet = new HashSet<String>();
                Set<String> updateSelfParamIdSet = new HashSet<String>();
                Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
                for (Map<String, String> selfParamMap : selfParamMapList) {
                    selfParamIdSet.add(selfParamMap.get("id"));
                    tempMap.put(selfParamMap.get("id"), selfParamMap);
                }
                List<ComponentSelfParam> dbSelfParamList = getService(ComponentSelfParamService.class).getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbSelfParamList)) {
                    for (ComponentSelfParam dbSelfParam : dbSelfParamList) {
                        if (selfParamIdSet.contains(dbSelfParam.getId())) {
                            // 修改
                            updateSelfParamIdSet.add(dbSelfParam.getId());
                            dbSelfParam.setComponentVersionId(componentVersionId);
                            Map<String, String> selfParamMap = tempMap.get(dbSelfParam.getId());
                            dbSelfParam.setName(selfParamMap.get("name"));
                            dbSelfParam.setType(selfParamMap.get("type"));
                            dbSelfParam.setValue(StringUtil.null2empty(selfParamMap.get("value")));
                            dbSelfParam.setRemark(StringUtil.null2empty(selfParamMap.get("remark")));
                            dbSelfParam.setOptions(selfParamMap.get("options"));
                            dbSelfParam.setText(StringUtil.null2empty(selfParamMap.get("text")));
                            getService(ComponentSelfParamService.class).save(dbSelfParam);
                        } else {
                            getService(ComponentSelfParamService.class).delete(dbSelfParam.getId());
                        }
                    }
                }
                for (Map<String, String> selfParamMap : selfParamMapList) {
                    if (!updateSelfParamIdSet.contains(selfParamMap.get("id"))) {
                        String options = StringUtil.null2empty(selfParamMap.get("options"));
                        if (DatabaseHandlerDao.isOracle()) {
                            options = parseStrByDatabaseType(options, DatabaseHandlerDao.DB_ORACLE);
                        } else {
                            options = parseStrByDatabaseType(options, DatabaseHandlerDao.DB_SQLSERVER);
                        }
                        // 新增
                        String selfParamSql = "insert into t_xtpz_component_self_param"
                                + "(id,component_version_id,name,type,value,remark,options,text) values('" + selfParamMap.get("id") + "','"
                                + componentVersionId + "','" + selfParamMap.get("name") + "','" + selfParamMap.get("type") + "','"
                                + StringUtil.null2empty(selfParamMap.get("value")) + "','" + StringUtil.null2empty(selfParamMap.get("remark")) + "','"
                                + options + "','" + StringUtil.null2empty(selfParamMap.get("text")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(selfParamSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, String> selfParamMap : selfParamMapList) {
                    String options = StringUtil.null2empty(selfParamMap.get("options"));
                    if (DatabaseHandlerDao.isOracle()) {
                        options = parseStrByDatabaseType(options, DatabaseHandlerDao.DB_ORACLE);
                    } else {
                        options = parseStrByDatabaseType(options, DatabaseHandlerDao.DB_SQLSERVER);
                    }
                    // 新增
                    String selfParamSql = "insert into t_xtpz_component_self_param" + "(id,component_version_id,name,type,value,remark,options,text) values('"
                            + selfParamMap.get("id") + "','" + componentVersionId + "','" + selfParamMap.get("name") + "','" + selfParamMap.get("type") + "','"
                            + StringUtil.null2empty(selfParamMap.get("value")) + "','" + StringUtil.null2empty(selfParamMap.get("remark")) + "','" + options
                            + "','" + StringUtil.null2empty(selfParamMap.get("text")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(selfParamSql);
                }
            }
        }
    }

    /**
     * 保存构件中的输入参数
     */
    @Transactional
    private void saveInputParams(boolean existDbComponent, List<Map<String, String>> inputParamMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(inputParamMapList)) {
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> inputParamIdSet = new HashSet<String>();
                Set<String> updateInputParamIdSet = new HashSet<String>();
                Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
                for (Map<String, String> inputParamMap : inputParamMapList) {
                    inputParamIdSet.add(inputParamMap.get("id"));
                    tempMap.put(inputParamMap.get("id"), inputParamMap);
                }
                List<ComponentInputParam> dbInputParamList = getService(ComponentInputParamService.class).getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbInputParamList)) {
                    for (ComponentInputParam dbInputParam : dbInputParamList) {
                        if (inputParamIdSet.contains(dbInputParam.getId())) {
                            // 修改
                            updateInputParamIdSet.add(dbInputParam.getId());
                            dbInputParam.setComponentVersionId(componentVersionId);
                            Map<String, String> inputParamMap = tempMap.get(dbInputParam.getId());
                            dbInputParam.setName(inputParamMap.get("name"));
                            dbInputParam.setValue(StringUtil.null2empty(inputParamMap.get("value")));
                            dbInputParam.setRemark(StringUtil.null2empty(inputParamMap.get("remark")));
                            getService(ComponentInputParamService.class).save(dbInputParam);
                        } else {
                            getService(ComponentInputParamService.class).delete(dbInputParam.getId());
                        }
                    }
                }
                for (Map<String, String> inputParamMap : inputParamMapList) {
                    if (!updateInputParamIdSet.contains(inputParamMap.get("id"))) {
                        // 新增
                        String inputParamSql = "insert into t_xtpz_component_input_param" + "(id,component_version_id,name,value,remark) values('"
                                + inputParamMap.get("id") + "','" + componentVersionId + "','" + inputParamMap.get("name") + "','"
                                + StringUtil.null2empty(inputParamMap.get("value")) + "','" + StringUtil.null2empty(inputParamMap.get("remark")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(inputParamSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, String> inputParamMap : inputParamMapList) {
                    // 新增
                    String inputParamSql = "insert into t_xtpz_component_input_param" + "(id,component_version_id,name,value,remark) values('"
                            + inputParamMap.get("id") + "','" + componentVersionId + "','" + inputParamMap.get("name") + "','"
                            + StringUtil.null2empty(inputParamMap.get("value")) + "','" + StringUtil.null2empty(inputParamMap.get("remark")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(inputParamSql);
                }
            }
        }
    }

    /**
     * 保存构件中的输出参数
     */
    @Transactional
    private void saveOutputParams(boolean existDbComponent, List<Map<String, String>> outputParamMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(outputParamMapList)) {
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> outputParamIdSet = new HashSet<String>();
                Set<String> updateOutputParamIdSet = new HashSet<String>();
                Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
                for (Map<String, String> outputParamMap : outputParamMapList) {
                    outputParamIdSet.add(outputParamMap.get("id"));
                    tempMap.put(outputParamMap.get("id"), outputParamMap);
                }
                List<ComponentOutputParam> dbOutputParamList = getService(ComponentOutputParamService.class).getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbOutputParamList)) {
                    for (ComponentOutputParam dbOutputParam : dbOutputParamList) {
                        if (outputParamIdSet.contains(dbOutputParam.getId())) {
                            // 修改
                            updateOutputParamIdSet.add(dbOutputParam.getId());
                            dbOutputParam.setComponentVersionId(componentVersionId);
                            Map<String, String> outputParamMap = tempMap.get(dbOutputParam.getId());
                            dbOutputParam.setName(outputParamMap.get("name"));
                            dbOutputParam.setRemark(StringUtil.null2empty(outputParamMap.get("remark")));
                            getService(ComponentOutputParamService.class).save(dbOutputParam);
                        } else {
                            getService(ComponentOutputParamService.class).delete(dbOutputParam.getId());
                        }
                    }
                }
                for (Map<String, String> outputParamMap : outputParamMapList) {
                    if (!updateOutputParamIdSet.contains(outputParamMap.get("id"))) {
                        // 新增
                        String outputParamSql = "insert into t_xtpz_component_input_param" + "(id,component_version_id,name,remark) values('"
                                + outputParamMap.get("id") + "','" + componentVersionId + "','" + outputParamMap.get("name") + "','"
                                + StringUtil.null2empty(outputParamMap.get("remark")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(outputParamSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, String> outputParamMap : outputParamMapList) {
                    // 新增
                    String outputParamSql = "insert into t_xtpz_component_input_param" + "(id,component_version_id,name,remark) values('"
                            + outputParamMap.get("id") + "','" + componentVersionId + "','" + outputParamMap.get("name") + "','"
                            + StringUtil.null2empty(outputParamMap.get("remark")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(outputParamSql);
                }
            }
        }
    }

    /**
     * 保存自定义构件中的方法
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private void saveFunctions(boolean existDbComponent, List<Map<String, Object>> functionMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(functionMapList)) {
            Map<String, String> baseInfo = null;
            List<Map<String, String>> functionDataMapList = null;
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> functionIdSet = new HashSet<String>();
                Set<String> updateFunctionIdSet = new HashSet<String>();
                Map<String, Map<String, Object>> tempMap = new HashMap<String, Map<String, Object>>();
                for (Map<String, Object> functionMap : functionMapList) {
                    baseInfo = (Map<String, String>) functionMap.get("baseInfo");
                    functionIdSet.add(baseInfo.get("id"));
                    tempMap.put(baseInfo.get("id"), functionMap);
                }
                List<ComponentFunction> dbFunctionList = getService(ComponentFunctionService.class).getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbFunctionList)) {
                    Map<String, Object> functionMap = null;
                    for (ComponentFunction dbFunction : dbFunctionList) {
                        if (functionIdSet.contains(dbFunction.getId())) {
                            // 修改
                            updateFunctionIdSet.add(dbFunction.getId());
                            functionMap = tempMap.get(dbFunction.getId());
                            baseInfo = (Map<String, String>) functionMap.get("baseInfo");
                            dbFunction.setComponentVersionId(componentVersionId);
                            dbFunction.setName(baseInfo.get("name"));
                            dbFunction.setPage(baseInfo.get("page"));
                            dbFunction.setRemark(StringUtil.null2empty(baseInfo.get("remark")));
                            getService(ComponentFunctionService.class).save(dbFunction);
                            functionDataMapList = (List<Map<String, String>>) functionMap.get("ComponentFunctionDatas");
                            Set<String> functionDataIdSet = new HashSet<String>();
                            Set<String> updateFunctionDataIdSet = new HashSet<String>();
                            Map<String, Map<String, String>> tempDataMap = new HashMap<String, Map<String, String>>();
                            for (Map<String, String> functionDataMap : functionDataMapList) {
                                functionDataIdSet.add(functionDataMap.get("id"));
                                tempDataMap.put(functionDataMap.get("id"), functionDataMap);
                            }
                            List<ComponentFunctionData> dbFunctionDataList = getService(ComponentFunctionDataService.class).getByFunctionId(dbFunction.getId());
                            // 该增增，该改改，该删删
                            if (CollectionUtils.isNotEmpty(dbFunctionDataList)) {
                                Map<String, String> functionDataMap = null;
                                for (ComponentFunctionData dbFunctionData : dbFunctionDataList) {
                                    if (functionDataIdSet.contains(dbFunctionData.getId())) {
                                        // 修改
                                        updateFunctionDataIdSet.add(dbFunctionData.getId());
                                        functionDataMap = tempDataMap.get(dbFunctionData.getId());
                                        dbFunctionData.setName(functionDataMap.get("name"));
                                        dbFunctionData.setRemark(functionDataMap.get("remark"));
                                        getService(ComponentFunctionDataService.class).save(dbFunctionData);
                                    } else {
                                        getService(ComponentFunctionDataService.class).delete(dbFunctionData.getId());
                                    }
                                }
                                for (Map<String, String> tempFunctionDataMap : functionDataMapList) {
                                    if (!updateFunctionDataIdSet.contains(tempFunctionDataMap.get("id"))) {
                                        // 新增
                                        String functionDataSql = "insert into t_xtpz_component_function_data(id,function_id,name,remark) values('"
                                                + tempFunctionDataMap.get("id") + "','" + dbFunction.getId() + "','" + tempFunctionDataMap.get("name") + "','"
                                                + StringUtil.null2empty(tempFunctionDataMap.get("remark")) + "')";
                                        DatabaseHandlerDao.getInstance().executeSql(functionDataSql);
                                    }
                                }
                            } else {
                                // 都是新增
                                for (Map<String, String> tempFunctionDataMap : functionDataMapList) {
                                    // 新增
                                    String functionDataSql = "insert into t_xtpz_component_function_data(id,function_id,name,remark) values('"
                                            + tempFunctionDataMap.get("id") + "','" + dbFunction.getId() + "','" + tempFunctionDataMap.get("name") + "','"
                                            + StringUtil.null2empty(tempFunctionDataMap.get("remark")) + "')";
                                    DatabaseHandlerDao.getInstance().executeSql(functionDataSql);
                                }
                            }

                        } else {
                            getService(ComponentFunctionService.class).delete(dbFunction.getId());
                        }
                    }
                }
                for (Map<String, Object> functionMap : functionMapList) {
                    baseInfo = (Map<String, String>) functionMap.get("baseInfo");
                    if (!updateFunctionIdSet.contains(baseInfo.get("id"))) {
                        // 新增
                        String functionSql = "insert into t_xtpz_component_function(id,component_version_id,name,page,remark) values('" + baseInfo.get("id")
                                + "','" + componentVersionId + "','" + baseInfo.get("name") + "','" + baseInfo.get("page") + "','"
                                + StringUtil.null2empty(baseInfo.get("remark")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(functionSql);
                        functionDataMapList = (List<Map<String, String>>) functionMap.get("ComponentFunctionDatas");
                        for (Map<String, String> tempFunctionDataMap : functionDataMapList) {
                            // 新增
                            String functionDataSql = "insert into t_xtpz_component_function_data(id,function_id,name,remark) values('"
                                    + tempFunctionDataMap.get("id") + "','" + baseInfo.get("id") + "','" + tempFunctionDataMap.get("name") + "','"
                                    + StringUtil.null2empty(tempFunctionDataMap.get("remark")) + "')";
                            DatabaseHandlerDao.getInstance().executeSql(functionDataSql);
                        }
                    }
                }
            } else {
                // 都是新增
                for (Map<String, Object> functionMap : functionMapList) {
                    baseInfo = (Map<String, String>) functionMap.get("baseInfo");
                    // 新增
                    String functionSql = "insert into t_xtpz_component_function(id,component_version_id,name,page,remark) values('" + baseInfo.get("id")
                            + "','" + componentVersionId + "','" + baseInfo.get("name") + "','" + baseInfo.get("page") + "','"
                            + StringUtil.null2empty(baseInfo.get("remark")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(functionSql);
                    functionDataMapList = (List<Map<String, String>>) functionMap.get("ComponentFunctionDatas");
                    for (Map<String, String> tempFunctionDataMap : functionDataMapList) {
                        // 新增
                        String functionDataSql = "insert into t_xtpz_component_function_data(id,function_id,name,remark) values('"
                                + tempFunctionDataMap.get("id") + "','" + baseInfo.get("id") + "','" + tempFunctionDataMap.get("name") + "','"
                                + StringUtil.null2empty(tempFunctionDataMap.get("remark")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(functionDataSql);
                    }
                }
            }
        }
    }

    /**
     * 保存自定义构件中的回调函数
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private void saveCallbacks(boolean existDbComponent, List<Map<String, Object>> callbackMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(callbackMapList)) {
            Map<String, String> baseInfo = null;
            List<Map<String, String>> callbackParamMapList = null;
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> callbackIdSet = new HashSet<String>();
                Set<String> updateCallbackIdSet = new HashSet<String>();
                Map<String, Map<String, Object>> tempMap = new HashMap<String, Map<String, Object>>();
                for (Map<String, Object> callbackMap : callbackMapList) {
                    baseInfo = (Map<String, String>) callbackMap.get("baseInfo");
                    callbackIdSet.add(baseInfo.get("id"));
                    tempMap.put(baseInfo.get("id"), callbackMap);
                }
                List<ComponentCallback> dbCallbackList = getService(ComponentCallbackService.class).getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbCallbackList)) {
                    Map<String, Object> callbackMap = null;
                    for (ComponentCallback dbCallback : dbCallbackList) {
                        if (callbackIdSet.contains(dbCallback.getId())) {
                            // 修改
                            updateCallbackIdSet.add(dbCallback.getId());
                            callbackMap = tempMap.get(dbCallback.getId());
                            baseInfo = (Map<String, String>) callbackMap.get("baseInfo");
                            dbCallback.setComponentVersionId(componentVersionId);
                            dbCallback.setName(baseInfo.get("name"));
                            dbCallback.setPage(baseInfo.get("page"));
                            dbCallback.setRemark(StringUtil.null2empty(baseInfo.get("remark")));
                            getService(ComponentCallbackService.class).save(dbCallback);
                            callbackParamMapList = (List<Map<String, String>>) callbackMap.get("ComponentCallbackParams");
                            Set<String> callbackParamIdSet = new HashSet<String>();
                            Set<String> updateCallbackParamIdSet = new HashSet<String>();
                            Map<String, Map<String, String>> tempDataMap = new HashMap<String, Map<String, String>>();
                            if (CollectionUtils.isNotEmpty(callbackParamMapList)) {
                                for (Map<String, String> callbackParamMap : callbackParamMapList) {
                                    callbackParamIdSet.add(callbackParamMap.get("id"));
                                    tempDataMap.put(callbackParamMap.get("id"), callbackParamMap);
                                }
                            }
                            List<ComponentCallbackParam> dbCallbackParamList = getService(ComponentCallbackParamService.class).getByCallbackId(
                                    dbCallback.getId());
                            // 该增增，该改改，该删删
                            if (CollectionUtils.isNotEmpty(dbCallbackParamList)) {
                                Map<String, String> callbackParamMap = null;
                                for (ComponentCallbackParam dbCallbackParam : dbCallbackParamList) {
                                    if (callbackParamIdSet.contains(dbCallbackParam.getId())) {
                                        // 修改
                                        updateCallbackParamIdSet.add(dbCallbackParam.getId());
                                        callbackParamMap = tempDataMap.get(dbCallbackParam.getId());
                                        dbCallbackParam.setName(callbackParamMap.get("name"));
                                        dbCallbackParam.setRemark(callbackParamMap.get("remark"));
                                        getService(ComponentCallbackParamService.class).save(dbCallbackParam);
                                    } else {
                                        getService(ComponentCallbackParamService.class).delete(dbCallbackParam.getId());
                                    }
                                }
                                if (CollectionUtils.isNotEmpty(callbackParamMapList)) {
                                    for (Map<String, String> tempCallbackParamMap : callbackParamMapList) {
                                        if (!updateCallbackParamIdSet.contains(tempCallbackParamMap.get("id"))) {
                                            // 新增
                                            String callbackParamSql = "insert into t_xtpz_comp_callback_param(id,callback_id,name,remark) values('"
                                                    + tempCallbackParamMap.get("id") + "','" + dbCallback.getId() + "','" + tempCallbackParamMap.get("name")
                                                    + "','" + StringUtil.null2empty(tempCallbackParamMap.get("remark")) + "')";
                                            DatabaseHandlerDao.getInstance().executeSql(callbackParamSql);
                                        }
                                    }
                                }
                            } else {
                                // 都是新增
                                if (CollectionUtils.isNotEmpty(callbackParamMapList)) {
                                    for (Map<String, String> tempCallbackParamMap : callbackParamMapList) {
                                        // 新增
                                        String callbackParamSql = "insert into t_xtpz_comp_callback_param(id,callback_id,name,remark) values('"
                                                + tempCallbackParamMap.get("id") + "','" + dbCallback.getId() + "','" + tempCallbackParamMap.get("name")
                                                + "','" + StringUtil.null2empty(tempCallbackParamMap.get("remark")) + "')";
                                        DatabaseHandlerDao.getInstance().executeSql(callbackParamSql);
                                    }
                                }
                            }

                        } else {
                            getService(ComponentCallbackService.class).delete(dbCallback.getId());
                        }
                    }
                }
                for (Map<String, Object> callbackMap : callbackMapList) {
                    baseInfo = (Map<String, String>) callbackMap.get("baseInfo");
                    if (!updateCallbackIdSet.contains(baseInfo.get("id"))) {
                        // 新增
                        String callbackSql = "insert into t_xtpz_component_callback(id,component_version_id,name,page,remark) values('" + baseInfo.get("id")
                                + "','" + componentVersionId + "','" + baseInfo.get("name") + "','" + baseInfo.get("page") + "','"
                                + StringUtil.null2empty(baseInfo.get("remark")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(callbackSql);
                        callbackParamMapList = (List<Map<String, String>>) callbackMap.get("ComponentCallbackParams");
                        if (CollectionUtils.isNotEmpty(callbackParamMapList)) {
                            for (Map<String, String> tempCallbackParamMap : callbackParamMapList) {
                                // 新增
                                String callbackParamSql = "insert into t_xtpz_comp_callback_param(id,callback_id,name,remark) values('"
                                        + tempCallbackParamMap.get("id") + "','" + baseInfo.get("id") + "','" + tempCallbackParamMap.get("name") + "','"
                                        + StringUtil.null2empty(tempCallbackParamMap.get("remark")) + "')";
                                DatabaseHandlerDao.getInstance().executeSql(callbackParamSql);
                            }
                        }
                    }
                }
            } else {
                // 都是新增
                for (Map<String, Object> callbackMap : callbackMapList) {
                    baseInfo = (Map<String, String>) callbackMap.get("baseInfo");
                    // 新增
                    String callbackSql = "insert into t_xtpz_component_callback(id,component_version_id,name,page,remark) values('" + baseInfo.get("id")
                            + "','" + componentVersionId + "','" + baseInfo.get("name") + "','" + baseInfo.get("page") + "','"
                            + StringUtil.null2empty(baseInfo.get("remark")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(callbackSql);
                    callbackParamMapList = (List<Map<String, String>>) callbackMap.get("ComponentCallbackParams");
                    if (CollectionUtils.isNotEmpty(callbackParamMapList)) {
                        for (Map<String, String> tempCallbackParamMap : callbackParamMapList) {
                            // 新增
                            String callbackParamSql = "insert into t_xtpz_comp_callback_param(id,callback_id,name,remark) values('"
                                    + tempCallbackParamMap.get("id") + "','" + baseInfo.get("id") + "','" + tempCallbackParamMap.get("name") + "','"
                                    + StringUtil.null2empty(tempCallbackParamMap.get("remark")) + "')";
                            DatabaseHandlerDao.getInstance().executeSql(callbackParamSql);
                        }
                    }
                }
            }
        }
    }

    /**
     * 保存构件中的按钮
     */
    @Transactional
    private void saveButtons(boolean existDbComponent, List<Map<String, String>> buttonMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(buttonMapList)) {
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> buttonIdSet = new HashSet<String>();
                Set<String> updateButtonIdSet = new HashSet<String>();
                Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
                for (Map<String, String> buttonMap : buttonMapList) {
                    buttonIdSet.add(buttonMap.get("id"));
                    tempMap.put(buttonMap.get("id"), buttonMap);
                }
                List<ComponentButton> dbButtonList = getService(ComponentButtonService.class).getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbButtonList)) {
                    for (ComponentButton dbButton : dbButtonList) {
                        if (buttonIdSet.contains(dbButton.getId())) {
                            // 修改
                            updateButtonIdSet.add(dbButton.getId());
                            dbButton.setComponentVersionId(componentVersionId);
                            Map<String, String> buttonMap = tempMap.get(dbButton.getId());
                            dbButton.setName(buttonMap.get("name"));
                            dbButton.setDisplayName(buttonMap.get("displayName"));
                            getService(ComponentButtonService.class).save(dbButton);
                        } else {
                            getService(ComponentButtonService.class).delete(dbButton.getId());
                        }
                    }
                }
                for (Map<String, String> buttonMap : buttonMapList) {
                    if (!updateButtonIdSet.contains(buttonMap.get("id"))) {
                        // 新增
                        String buttonSql = "insert into t_xtpz_component_button(id,component_version_id,name,display_name) values('" + buttonMap.get("id")
                                + "','" + componentVersionId + "','" + buttonMap.get("name") + "','" + buttonMap.get("displayName") + "')";
                        DatabaseHandlerDao.getInstance().executeSql(buttonSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, String> buttonMap : buttonMapList) {
                    // 新增
                    String buttonSql = "insert into t_xtpz_component_button(id,component_version_id,name,display_name) values('" + buttonMap.get("id") + "','"
                            + componentVersionId + "','" + buttonMap.get("name") + "','" + buttonMap.get("displayName") + "')";
                    DatabaseHandlerDao.getInstance().executeSql(buttonSql);
                }
            }
        }
    }

    /**
     * 保存构件中的类
     */
    @Transactional
    private void saveClasses(boolean existDbComponent, List<Map<String, String>> classMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(classMapList)) {
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> classIdSet = new HashSet<String>();
                Set<String> updateClassIdSet = new HashSet<String>();
                Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
                for (Map<String, String> classMap : classMapList) {
                    classIdSet.add(classMap.get("id"));
                    tempMap.put(classMap.get("id"), classMap);
                }
                List<ComponentClass> dbClassList = getService(ComponentClassService.class).getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbClassList)) {
                    for (ComponentClass dbClass : dbClassList) {
                        if (classIdSet.contains(dbClass.getId())) {
                            // 修改
                            updateClassIdSet.add(dbClass.getId());
                            dbClass.setComponentVersionId(componentVersionId);
                            Map<String, String> classMap = tempMap.get(dbClass.getId());
                            dbClass.setName(classMap.get("name"));
                            getService(ComponentClassService.class).save(dbClass);
                        } else {
                            getService(ComponentClassService.class).delete(dbClass.getId());
                        }
                    }
                }
                for (Map<String, String> classMap : classMapList) {
                    if (!updateClassIdSet.contains(classMap.get("id"))) {
                        // 新增
                        String classSql = "insert into t_xtpz_component_class(id,component_version_id,name) values('" + classMap.get("id") + "','"
                                + componentVersionId + "','" + classMap.get("name") + "')";
                        DatabaseHandlerDao.getInstance().executeSql(classSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, String> classMap : classMapList) {
                    // 新增
                    String classSql = "insert into t_xtpz_component_class(id,component_version_id,name) values('" + classMap.get("id") + "','"
                            + componentVersionId + "','" + classMap.get("name") + "')";
                    DatabaseHandlerDao.getInstance().executeSql(classSql);
                }
            }
        }
    }

    /**
     * 保存构件中的jar包
     */
    @Transactional
    private void saveJars(boolean existDbComponent, List<Map<String, String>> jarMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(jarMapList)) {
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> jarIdSet = new HashSet<String>();
                Set<String> updateJarIdSet = new HashSet<String>();
                Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
                for (Map<String, String> jarMap : jarMapList) {
                    jarIdSet.add(jarMap.get("id"));
                    tempMap.put(jarMap.get("id"), jarMap);
                }
                List<ComponentJar> dbJarList = getService(ComponentJarService.class).getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbJarList)) {
                    for (ComponentJar dbJar : dbJarList) {
                        if (jarIdSet.contains(dbJar.getId())) {
                            // 修改
                            updateJarIdSet.add(dbJar.getId());
                            dbJar.setComponentVersionId(componentVersionId);
                            Map<String, String> jarMap = tempMap.get(dbJar.getId());
                            dbJar.setName(jarMap.get("name"));
                            getService(ComponentJarService.class).save(dbJar);
                        } else {
                            getService(ComponentJarService.class).delete(dbJar.getId());
                        }
                    }
                }
                for (Map<String, String> jarMap : jarMapList) {
                    if (!updateJarIdSet.contains(jarMap.get("id"))) {
                        // 新增
                        String jarSql = "insert into t_xtpz_component_jar(id,component_version_id,name) values('" + jarMap.get("id") + "','"
                                + componentVersionId + "','" + jarMap.get("name") + "')";
                        DatabaseHandlerDao.getInstance().executeSql(jarSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, String> jarMap : jarMapList) {
                    // 新增
                    String jarSql = "insert into t_xtpz_component_jar(id,component_version_id,name) values('" + jarMap.get("id") + "','" + componentVersionId
                            + "','" + jarMap.get("name") + "')";
                    DatabaseHandlerDao.getInstance().executeSql(jarSql);
                }
            }
        }
    }

    /**
     * 保存构件中的构件、表、字段关系
     */
    @Transactional
    private void saveComponentTableColumnRelations(boolean existDbComponent, List<Map<String, String>> comTableColumnRelationMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(comTableColumnRelationMapList)) {
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> relationIdSet = new HashSet<String>();
                Set<String> updateRelationIdSet = new HashSet<String>();
                Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
                for (Map<String, String> relationMap : comTableColumnRelationMapList) {
                    relationIdSet.add(relationMap.get("id"));
                    tempMap.put(relationMap.get("id"), relationMap);
                }
                List<ComponentTableColumnRelation> dbRelationList = getService(ComponentTableColumnRelationService.class).getByComponentVersionId(
                        componentVersionId);
                if (CollectionUtils.isNotEmpty(dbRelationList)) {
                    for (ComponentTableColumnRelation dbRelation : dbRelationList) {
                        if (relationIdSet.contains(dbRelation.getId())) {
                            // 修改
                            updateRelationIdSet.add(dbRelation.getId());
                            dbRelation.setComponentVersionId(componentVersionId);
                            Map<String, String> relationMap = tempMap.get(dbRelation.getId());
                            dbRelation.setTableId(relationMap.get("tableId"));
                            dbRelation.setColumnId(StringUtil.null2empty(relationMap.get("columnId")));
                            getService(ComponentTableColumnRelationService.class).save(dbRelation);
                        } else {
                            getService(ComponentTableColumnRelationService.class).delete(dbRelation.getId());
                        }
                    }
                }
                for (Map<String, String> relationMap : comTableColumnRelationMapList) {
                    if (!updateRelationIdSet.contains(relationMap.get("id"))) {
                        // 新增
                        String relationSql = "insert into t_xtpz_component_table_column(id,component_version_id,table_id,column_id) values('"
                                + relationMap.get("id") + "','" + componentVersionId + "','" + relationMap.get("table_id") + "','"
                                + StringUtil.null2empty(relationMap.get("column_id")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(relationSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, String> relationMap : comTableColumnRelationMapList) {
                    // 新增
                    String relationSql = "insert into t_xtpz_component_table_column(id,component_version_id,table_id,column_id) values('"
                            + relationMap.get("id") + "','" + componentVersionId + "','" + relationMap.get("table_id") + "','"
                            + StringUtil.null2empty(relationMap.get("column_id")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(relationSql);
                }
            }
        }
    }

    /**
     * 保存构件与公用构件的关系
     */
    @Transactional
    private void saveCommonComponentRelations(boolean existDbComponent, List<Map<String, String>> commonComponentRelationMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(commonComponentRelationMapList)) {
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> relationIdSet = new HashSet<String>();
                Set<String> updateRelationIdSet = new HashSet<String>();
                Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
                for (Map<String, String> relationMap : commonComponentRelationMapList) {
                    relationIdSet.add(relationMap.get("id"));
                    tempMap.put(relationMap.get("id"), relationMap);
                }
                List<CommonComponentRelation> dbRelationList = getService(CommonComponentRelationService.class).getByComponentVersionId(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbRelationList)) {
                    for (CommonComponentRelation dbRelation : dbRelationList) {
                        if (relationIdSet.contains(dbRelation.getId())) {
                            // 修改
                            updateRelationIdSet.add(dbRelation.getId());
                            dbRelation.setComponentVersionId(componentVersionId);
                            Map<String, String> relationMap = tempMap.get(dbRelation.getId());
                            dbRelation.setCommonComponentVersionId(relationMap.get("commonComponentVersionId"));
                            getService(CommonComponentRelationService.class).save(dbRelation);
                        } else {
                            getService(CommonComponentRelationService.class).delete(dbRelation.getId());
                        }
                    }
                }
                for (Map<String, String> relationMap : commonComponentRelationMapList) {
                    if (!updateRelationIdSet.contains(relationMap.get("id"))) {
                        // 新增
                        String relationSql = "insert into t_xtpz_commom_comp_relation(id,component_version_id,common_component_version_id) values('"
                                + relationMap.get("id") + "','" + componentVersionId + "','" + relationMap.get("common_component_version_id") + "')";
                        DatabaseHandlerDao.getInstance().executeSql(relationSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, String> relationMap : commonComponentRelationMapList) {
                    // 新增
                    String relationSql = "insert into t_xtpz_commom_comp_relation(id,component_version_id,common_component_version_id) values('"
                            + relationMap.get("id") + "','" + componentVersionId + "','" + relationMap.get("common_component_version_id") + "')";
                    DatabaseHandlerDao.getInstance().executeSql(relationSql);
                }
            }
        }
    }

    /**
     * 保存自定义构件中的表
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private void saveTables(boolean existDbComponent, List<Map<String, Object>> tableMapList, String componentVersionId) {
        if (CollectionUtils.isNotEmpty(tableMapList)) {
            Map<String, String> baseInfo = null;
            List<Map<String, String>> columnMapList = null;
            if (existDbComponent) {
                // 该增增，该改改，该删删
                Set<String> tableIdSet = new HashSet<String>();
                Set<String> updateTableIdSet = new HashSet<String>();
                Map<String, Map<String, Object>> tempMap = new HashMap<String, Map<String, Object>>();
                for (Map<String, Object> tableMap : tableMapList) {
                    baseInfo = (Map<String, String>) tableMap.get("baseInfo");
                    tableIdSet.add(baseInfo.get("id"));
                    tempMap.put(baseInfo.get("id"), tableMap);
                }
                List<ComponentTable> dbTableList = getService(ComponentTableService.class).getComponentTableList(componentVersionId);
                if (CollectionUtils.isNotEmpty(dbTableList)) {
                    Map<String, Object> tableMap = null;
                    for (ComponentTable dbTable : dbTableList) {
                        if (tableIdSet.contains(dbTable.getId())) {
                            // 修改
                            updateTableIdSet.add(dbTable.getId());
                            tableMap = tempMap.get(dbTable.getId());
                            baseInfo = (Map<String, String>) tableMap.get("baseInfo");
                            dbTable.setName(baseInfo.get("name"));
                            dbTable.setIsSelfdefine(StringUtil.null2empty(baseInfo.get("isSelfdefine")));
                            dbTable.setReleaseWithData(StringUtil.null2empty(baseInfo.get("releaseWithData")));
                            getService(ComponentTableService.class).save(dbTable);
                            columnMapList = (List<Map<String, String>>) tableMap.get("ComponentColumns");
                            Set<String> columnIdSet = new HashSet<String>();
                            Set<String> updateColumnIdSet = new HashSet<String>();
                            Map<String, Map<String, String>> tempDataMap = new HashMap<String, Map<String, String>>();
                            for (Map<String, String> columnMap : columnMapList) {
                                columnIdSet.add(columnMap.get("id"));
                                tempDataMap.put(columnMap.get("id"), columnMap);
                            }
                            List<ComponentColumn> dbColumnList = getService(ComponentColumnService.class).getByComponentVersionIdAndTableId(componentVersionId,
                                    dbTable.getId());
                            // 该增增，该改改，该删删
                            if (CollectionUtils.isNotEmpty(dbColumnList)) {
                                Map<String, String> columnMap = null;
                                for (ComponentColumn dbColumn : dbColumnList) {
                                    if (columnIdSet.contains(dbColumn.getId())) {
                                        // 修改
                                        updateColumnIdSet.add(dbColumn.getId());
                                        columnMap = tempDataMap.get(dbColumn.getId());
                                        dbColumn.setName(columnMap.get("name"));
                                        dbColumn.setType(columnMap.get("type"));
                                        dbColumn.setLength(Integer.parseInt(columnMap.get("length")));
                                        dbColumn.setIsNull(columnMap.get("isNull"));
                                        dbColumn.setDefaultValue(StringUtil.null2empty(columnMap.get("defaultValue")));
                                        dbColumn.setRemark(StringUtil.null2empty(columnMap.get("remark")));
                                        getService(ComponentColumnService.class).save(dbColumn);
                                    } else {
                                        getService(ComponentColumnService.class).delete(dbColumn.getId());
                                    }
                                }
                                for (Map<String, String> tempColumnMap : columnMapList) {
                                    if (!updateColumnIdSet.contains(tempColumnMap.get("id"))) {
                                        // 新增
                                        String columnSql = "insert into t_xtpz_component_column(id,name,type,is_null,default_value,remark,length) values('"
                                                + tempColumnMap.get("id") + "','" + tempColumnMap.get("name") + "','" + tempColumnMap.get("type") + "','"
                                                + tempColumnMap.get("isNull") + "','" + StringUtil.null2empty(tempColumnMap.get("defaultValue")) + "','"
                                                + StringUtil.null2empty(tempColumnMap.get("remark")) + "')";
                                        DatabaseHandlerDao.getInstance().executeSql(columnSql);
                                    }
                                }
                            } else {
                                // 都是新增
                                for (Map<String, String> tempColumnMap : columnMapList) {
                                    // 新增
                                    String columnSql = "insert into t_xtpz_component_column(id,name,type,is_null,default_value,remark,length) values('"
                                            + tempColumnMap.get("id") + "','" + tempColumnMap.get("name") + "','" + tempColumnMap.get("type") + "','"
                                            + tempColumnMap.get("isNull") + "','" + StringUtil.null2empty(tempColumnMap.get("defaultValue")) + "','"
                                            + StringUtil.null2empty(tempColumnMap.get("remark")) + "')";
                                    DatabaseHandlerDao.getInstance().executeSql(columnSql);
                                }
                            }

                        } else {
                            getService(ComponentTableService.class).delete(dbTable.getId());
                        }
                    }
                }
                for (Map<String, Object> tableMap : tableMapList) {
                    baseInfo = (Map<String, String>) tableMap.get("baseInfo");
                    if (!updateTableIdSet.contains(baseInfo.get("id"))) {
                        // 新增
                        String tableSql = "t_xtpz_component_table(id,name,release_with_data,is_selfdefine) values('" + baseInfo.get("id") + "','"
                                + baseInfo.get("name") + "','" + StringUtil.null2empty(baseInfo.get("releaseWithData")) + "','"
                                + StringUtil.null2empty(baseInfo.get("isSelfdefine")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(tableSql);
                    }
                    columnMapList = (List<Map<String, String>>) tableMap.get("ComponentFunctionDatas");
                    for (Map<String, String> tempColumnMap : columnMapList) {
                        // 新增
                        String columnSql = "insert into t_xtpz_component_column(id,name,type,is_null,default_value,remark,length) values('"
                                + tempColumnMap.get("id") + "','" + tempColumnMap.get("name") + "','" + tempColumnMap.get("type") + "','"
                                + tempColumnMap.get("isNull") + "','" + StringUtil.null2empty(tempColumnMap.get("defaultValue")) + "','"
                                + StringUtil.null2empty(tempColumnMap.get("remark")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(columnSql);
                    }
                }
            } else {
                // 都是新增
                for (Map<String, Object> tableMap : tableMapList) {
                    baseInfo = (Map<String, String>) tableMap.get("baseInfo");
                    // 新增
                    String tableSql = "t_xtpz_component_table(id,name,release_with_data,is_selfdefine) values('" + baseInfo.get("id") + "','"
                            + baseInfo.get("name") + "','" + StringUtil.null2empty(baseInfo.get("releaseWithData")) + "','"
                            + StringUtil.null2empty(baseInfo.get("isSelfdefine")) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(tableSql);
                    columnMapList = (List<Map<String, String>>) tableMap.get("ComponentFunctionDatas");
                    for (Map<String, String> tempColumnMap : columnMapList) {
                        // 新增
                        String columnSql = "insert into t_xtpz_component_column(id,name,type,is_null,default_value,remark,length) values('"
                                + tempColumnMap.get("id") + "','" + tempColumnMap.get("name") + "','" + tempColumnMap.get("type") + "','"
                                + tempColumnMap.get("isNull") + "','" + StringUtil.null2empty(tempColumnMap.get("defaultValue")) + "','"
                                + StringUtil.null2empty(tempColumnMap.get("remark")) + "')";
                        DatabaseHandlerDao.getInstance().executeSql(columnSql);
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
     * 保存逻辑表
     * 
     * @param logicTableDefineList 逻辑表列表
     * @param logicColumnDefineMap 逻辑表中字段
     */
    @Transactional
    private void saveLogicTableDefine(List<LogicTableDefine> logicTableDefineList, Map<String, Map<String, ColumnDefine>> logicColumnDefineMap) {
        if (CollectionUtils.isNotEmpty(logicTableDefineList)) {
            LogicTableDefine temp = null;
            String columnDefineId = null;
            ColumnDefine columnDefine = null;
            Map<String, ColumnDefine> columnDefineMap = null;
            for (LogicTableDefine logicTableDefine : logicTableDefineList) {
                temp = getService(LogicTableDefineService.class).getByCode(logicTableDefine.getCode());
                if (temp == null) {
                    String logicTableSql = "insert into t_xtpz_logic_table_define(id,show_name,code,show_order,remark,status,classification,table_tree_id) values('"
                            + logicTableDefine.getId()
                            + "','"
                            + logicTableDefine.getShowName()
                            + "','"
                            + logicTableDefine.getCode()
                            + "',"
                            + logicTableDefine.getShowOrder()
                            + ",'"
                            + StringUtil.null2empty(logicTableDefine.getRemark())
                            + "','"
                            + logicTableDefine.getStatus() + "','" + logicTableDefine.getClassification() + "','" + logicTableDefine.getTableTreeId() + "')";
                    DatabaseHandlerDao.getInstance().executeSql(logicTableSql);
                    if (MapUtils.isNotEmpty(logicColumnDefineMap)) {
                        columnDefineMap = logicColumnDefineMap.get(logicTableDefine.getCode());
                        if (MapUtils.isNotEmpty(columnDefineMap)) {
                            for (Iterator<String> iterator = columnDefineMap.keySet().iterator(); iterator.hasNext();) {
                                columnDefineId = iterator.next();
                                columnDefine = columnDefineMap.get(columnDefineId);
                                saveColumnDefineBySql(columnDefine, false);
                            }
                        }
                    }
                } else {
                    getService(LogicTableDefineService.class).save(logicTableDefine);
                    if (MapUtils.isNotEmpty(logicColumnDefineMap)) {
                        columnDefineMap = logicColumnDefineMap.get(logicTableDefine.getCode());
                        if (MapUtils.isNotEmpty(columnDefineMap)) {
                            ColumnDefine columnDefineOfDb = null;
                            for (Iterator<String> iterator = columnDefineMap.keySet().iterator(); iterator.hasNext();) {
                                columnDefineId = iterator.next();
                                columnDefine = columnDefineMap.get(columnDefineId);
                                columnDefineOfDb = getService(ColumnDefineService.class).findByColumnNameAndTableId(columnDefine.getColumnName(),
                                        temp.getCode());
                                if (columnDefineOfDb == null) {
                                    saveColumnDefineBySql(columnDefine, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 保存逻辑表组
     * 
     * @param logicGroupDefineList 逻辑表组列表
     */
    @Transactional
    private void saveLogicGroupDefine(List<LogicGroupDefine> logicGroupDefineList) {
        if (CollectionUtils.isNotEmpty(logicGroupDefineList)) {
            LogicGroupDefine temp = null;
            for (LogicGroupDefine logicGroupDefine : logicGroupDefineList) {
                temp = getService(LogicGroupDefineService.class).getByCode(logicGroupDefine.getCode());
                if (temp == null) {
                    String logicGroupSql = "insert into t_xtpz_logic_group_define(id,group_name,code,show_order,remark,status) values('"
                            + logicGroupDefine.getId() + "','" + logicGroupDefine.getGroupName() + "','" + logicGroupDefine.getCode() + "',"
                            + logicGroupDefine.getShowOrder() + ",'" + StringUtil.null2empty(logicGroupDefine.getRemark()) + "','"
                            + logicGroupDefine.getStatus() + "')";
                    DatabaseHandlerDao.getInstance().executeSql(logicGroupSql);
                }
            }
        }
    }

    /**
     * 保存逻辑表组和逻辑表关系
     * 
     * @param logicGroupRelationList 逻辑表组和逻辑表关系
     */
    @Transactional
    private void saveLogicGroupRelationDefine(List<LogicGroupRelation> logicGroupRelationList) {
        if (CollectionUtils.isNotEmpty(logicGroupRelationList)) {
            LogicGroupRelation temp = null;
            for (LogicGroupRelation logicGroupRelation : logicGroupRelationList) {
                temp = getService(LogicGroupRelationService.class).getByGroupCodeAndTableCode(logicGroupRelation.getGroupCode(),
                        logicGroupRelation.getTableCode());
                if (temp == null) {
                    String logicGroupRelationSql = "insert into t_xtpz_logic_group_relation(id,group_code,table_code,parent_table_code,show_order) values('"
                            + logicGroupRelation.getId() + "','" + logicGroupRelation.getGroupCode() + "','" + logicGroupRelation.getTableCode() + "','"
                            + StringUtil.null2empty(logicGroupRelation.getParentTableCode()) + "'," + logicGroupRelation.getShowOrder() + ")";
                    DatabaseHandlerDao.getInstance().executeSql(logicGroupRelationSql);
                }
            }
        }
    }

    /**
     * 保存逻辑表组中逻辑表之间的关系
     * 
     * @param logicTableRelationList 逻辑表组和逻辑表关系
     * @param logicColumnDefineMap 逻辑表中的字段
     */
    @Transactional
    private void saveLogicTableRelationDefine(List<LogicTableRelation> logicTableRelationList, Map<String, Map<String, ColumnDefine>> logicColumnDefineMap) {
        if (CollectionUtils.isNotEmpty(logicTableRelationList) && MapUtils.isNotEmpty(logicColumnDefineMap)) {
            LogicTableRelation temp = null;
            for (LogicTableRelation logicTableRelation : logicTableRelationList) {
                ColumnDefine columnDefine = null;
                if (logicTableRelation.getColumnId().startsWith("sys") || logicTableRelation.getColumnId().startsWith("_")) {
                    columnDefine = getService(ColumnDefineService.class).getByID(logicTableRelation.getColumnId());
                } else {
                    Map<String, ColumnDefine> columnDefineMap = logicColumnDefineMap.get(logicTableRelation.getTableCode());
                    columnDefine = columnDefineMap.get(logicTableRelation.getColumnId());
                }
                ColumnDefine parentColumnDefine = null;
                if (logicTableRelation.getParentColumnId().startsWith("sys") || logicTableRelation.getParentColumnId().startsWith("_")) {
                    parentColumnDefine = getService(ColumnDefineService.class).getByID(logicTableRelation.getParentColumnId());
                } else {
                    Map<String, ColumnDefine> parentColumnDefineMap = logicColumnDefineMap.get(logicTableRelation.getParentTableCode());
                    parentColumnDefine = parentColumnDefineMap.get(logicTableRelation.getParentColumnId());
                }
                if (columnDefine != null && parentColumnDefine != null) {
                    temp = getService(LogicTableRelationService.class).getTableRelation(logicTableRelation.getGroupCode(), logicTableRelation.getTableCode(),
                            columnDefine.getId(), logicTableRelation.getParentTableCode(), parentColumnDefine.getId());
                    if (temp == null) {
                        String logicTableRelationSql = "insert into t_xtpz_logic_table_relation(id,group_code,table_code,column_id,parent_table_code,parent_column_id) values('"
                                + logicTableRelation.getId()
                                + "','"
                                + logicTableRelation.getGroupCode()
                                + "','"
                                + logicTableRelation.getTableCode()
                                + "','"
                                + logicTableRelation.getColumnId()
                                + "','"
                                + logicTableRelation.getParentTableCode()
                                + "','"
                                + logicTableRelation.getParentColumnId() + "')";
                        DatabaseHandlerDao.getInstance().executeSql(logicTableRelationSql);
                    }
                }
            }
        }
    }

    /**
     * 保存物理表
     * 
     * @param physicalTableDefineMap 表信息
     * @param physicalColumnDefineMap 列信息
     */
    @Transactional
    private void savePhysicalTable(Map<String, PhysicalTableDefine> physicalTableDefineMap, Map<String, Map<String, ColumnDefine>> physicalColumnDefineMap) {
        if (MapUtils.isNotEmpty(physicalTableDefineMap)) {
            String oldPhysicalTableId = null;
            PhysicalTableDefine tableDefineOfSession = null;
            PhysicalTableDefine tableDefineOfDb = null;
            Map<String, ColumnDefine> columnDefineColumnMap = null;
            String oldColumnId = null;
            ColumnDefine columnDefineOfSession = null;
            ColumnDefine columnDefineOfDb = null;
            for (Iterator<String> tableIterator = physicalTableDefineMap.keySet().iterator(); tableIterator.hasNext();) {
                try {
                    oldPhysicalTableId = tableIterator.next();
                    tableDefineOfSession = physicalTableDefineMap.get(oldPhysicalTableId);
                    tableDefineOfDb = getService(PhysicalTableDefineService.class).getByTableName(tableDefineOfSession.getTableName());
                    if (tableDefineOfDb != null) {
                        tableDefineOfDb.setShowName(tableDefineOfSession.getShowName());
                        tableDefineOfDb.setLogicTableCode(tableDefineOfSession.getLogicTableCode());
                        tableDefineOfDb.setReleaseWithData(tableDefineOfSession.getReleaseWithData());
                        tableDefineOfDb.setCreateIndex(tableDefineOfSession.getCreateIndex());
                        getDaoFromContext(PhysicalTableDefineDao.class).save(tableDefineOfDb);
                        tableDefineOfSession = tableDefineOfDb;
                        physicalTableDefineMap.put(oldPhysicalTableId, tableDefineOfSession);
                    } else {
                        String logicTableCode = tableDefineOfSession.getLogicTableCode();
                        if (DatabaseHandlerDao.isOracle()) {
                            logicTableCode = parseStrByDatabaseType(logicTableCode, DatabaseHandlerDao.DB_ORACLE);
                        } else {
                            logicTableCode = parseStrByDatabaseType(logicTableCode, DatabaseHandlerDao.DB_SQLSERVER);
                        }
                        String physicalTableDefineSql = "insert into t_xtpz_physical_table_define(id,table_tree_id,table_type,classification,show_name,table_prefix,"
                                + "table_code,table_name,logic_table_code,created,show_order,release_with_data,create_index,remark) values('"
                                + tableDefineOfSession.getId()
                                + "','"
                                + tableDefineOfSession.getTableTreeId()
                                + "','"
                                + tableDefineOfSession.getTableType()
                                + "','"
                                + StringUtil.null2empty(tableDefineOfSession.getClassification())
                                + "','"
                                + tableDefineOfSession.getShowName()
                                + "','"
                                + tableDefineOfSession.getTablePrefix()
                                + "','"
                                + StringUtil.null2empty(tableDefineOfSession.getTableCode())
                                + "','"
                                + tableDefineOfSession.getTableName()
                                + "','"
                                + logicTableCode
                                + "','1',"
                                + tableDefineOfSession.getShowOrder()
                                + ",'"
                                + StringUtil.null2empty(tableDefineOfSession.getReleaseWithData())
                                + "','"
                                + StringUtil.null2empty(tableDefineOfSession.getCreateIndex())
                                + "','"
                                + StringUtil.null2empty(tableDefineOfSession.getRemark()) + "')";
                        DatabaseHandlerDao.getInstance().createTableWithStringId(tableDefineOfSession.getTableName());
                        DatabaseHandlerDao.getInstance().executeSql(physicalTableDefineSql);
                    }
                    columnDefineColumnMap = physicalColumnDefineMap.get(oldPhysicalTableId);
                    if (MapUtils.isNotEmpty(columnDefineColumnMap)) {
                        for (Iterator<String> columnIterator = columnDefineColumnMap.keySet().iterator(); columnIterator.hasNext();) {
                            oldColumnId = columnIterator.next();
                            columnDefineOfSession = columnDefineColumnMap.get(oldColumnId);
                            if (tableDefineOfDb != null) {
                                columnDefineOfDb = getService(ColumnDefineService.class).findByColumnNameAndTableId(columnDefineOfSession.getColumnName(),
                                        tableDefineOfDb.getId());
                                if (columnDefineOfDb != null) {
                                    columnDefineOfSession.setId(columnDefineOfDb.getId());
                                    columnDefineOfSession.setTableId(tableDefineOfSession.getId());
                                    columnDefineOfSession.setCreated(columnDefineOfDb.getCreated());
                                    getService(ColumnDefineService.class).save(columnDefineOfSession);
                                } else {
                                    saveColumnDefineBySql(columnDefineOfSession, true);
                                }
                            } else {
                                columnDefineOfDb = getService(ColumnDefineService.class).findByColumnNameAndTableId(columnDefineOfSession.getColumnName(),
                                        tableDefineOfSession.getId());
                                if (columnDefineOfDb == null) {
                                    saveColumnDefineBySql(columnDefineOfSession, true);
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存字段(SQL的方式)
     */
    @Transactional
    private void saveColumnDefineBySql(ColumnDefine columnDefine, boolean execToDb) {
        String tableColumnSql = "insert into t_xtpz_column_define(id,table_id,show_name,column_name,data_type,code_type_code,"
                + "length,column_type,inputable,updateable,searchable,listable,sortable,align,filter_type,"
                + "width,default_value,remark,created,show_order,column_label,input_type,data_type_extend,phraseable) values('"
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
                + StringUtil.null2empty(columnDefine.getDataTypeExtend()) + "','" + StringUtil.null2zero(columnDefine.getPhraseable()) + "')";
        DatabaseHandlerDao.getInstance().executeSql(tableColumnSql);
        if (execToDb && !AppDefineUtil.C_ID.equals(columnDefine.getColumnName())) {
            String tableName = getService(PhysicalTableDefineService.class).getTableName(columnDefine.getTableId());
            DatabaseHandlerDao.getInstance().addColumn(tableName, columnDefine.getColumnName(), columnDefine.getDataType(), columnDefine.getLength(),
                    Integer.parseInt(StringUtil.null2zero(columnDefine.getDataTypeExtend())), columnDefine.getDefaultValue());
        }
    }

    /**
     * 保存自定义配置信息中的表关系
     * 
     * @param selfDefineConfig 自定义配置信息
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private void saveTableRelation(Map<String, Object> selfDefineConfig) {
        List<Map<String, String>> tableRelationList = (List<Map<String, String>>) selfDefineConfig.get("tableRelation");
        distinctTableRelation(tableRelationList);
        if (CollectionUtils.isNotEmpty(tableRelationList)) {
            for (Map<String, String> table : tableRelationList) {
                String sourceTableName = table.get("sourceTableName");
                String targetTableName = table.get("targetTableName");
                String sourceColumnName = table.get("sourceColumnName");
                String targetColumnName = table.get("targetColumnName");
                PhysicalTableDefine sourceTableDefine = getService(PhysicalTableDefineService.class).getByTableName(sourceTableName);
                if (sourceTableDefine == null) {
                    continue;
                }
                PhysicalTableDefine targetTableDefine = getService(PhysicalTableDefineService.class).getByTableName(targetTableName);
                if (targetTableDefine == null) {
                    continue;
                }
                ColumnDefine sourceColumnDefine = getService(ColumnDefineService.class).findByColumnNameAndTableId(sourceColumnName, sourceTableDefine.getId());
                if (sourceColumnDefine == null) {
                    continue;
                }
                ColumnDefine targetColumnDefine = getService(ColumnDefineService.class).findByColumnNameAndTableId(targetColumnName, targetTableDefine.getId());
                if (targetColumnDefine == null) {
                    continue;
                }
                TableRelation tableRelation1 = getDaoFromContext(TableRelationDao.class).getTableRelation(sourceTableDefine.getId(),
                        sourceColumnDefine.getId(), targetTableDefine.getId(), targetColumnDefine.getId());
                TableRelation tableRelation2 = getDaoFromContext(TableRelationDao.class).getTableRelation(targetTableDefine.getId(),
                        targetColumnDefine.getId(), sourceTableDefine.getId(), sourceColumnDefine.getId());
                if (tableRelation1 == null && tableRelation2 == null) {
                    String tableRelationSql = "insert into t_xtpz_table_relation(id,table_id,column_id,relate_table_id," + "relate_column_id) values('"
                            + table.get("id") + "','" + sourceTableDefine.getId() + "','" + sourceColumnDefine.getId() + "','" + targetTableDefine.getId()
                            + "','" + targetColumnDefine.getId() + "')";
                    DatabaseHandlerDao.getInstance().executeSql(tableRelationSql);
                }
            }
        }
    }

    /**
     * 过滤表关系
     * 
     * @param tableRelationList 表关系
     */
    private void distinctTableRelation(List<Map<String, String>> tableRelationList) {
        Map<String, String> tempMap1 = null;
        Map<String, String> tempMap2 = null;
        if (CollectionUtils.isNotEmpty(tableRelationList)) {
            for (int i = 0; i < tableRelationList.size(); i++) {
                tempMap1 = tableRelationList.get(i);
                for (int j = i + 1; j < tableRelationList.size(); j++) {
                    tempMap2 = tableRelationList.get(j);
                    if ((tempMap1.get("sourceTableName") + "-" + tempMap1.get("sourceColumnName") + "-" + tempMap1.get("targetTableName") + "-" + tempMap1
                            .get("targetColumnName")).equals((tempMap2.get("sourceTableName") + "-" + tempMap2.get("sourceColumnName") + "-"
                            + tempMap2.get("targetTableName") + "-" + tempMap2.get("targetColumnName")))) {
                        tableRelationList.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }
    }

    /**
     * 保存自定义配置信息中的字段关联定义
     * 
     * @param selfDefineConfig 自定义构件配置信息
     * @param physicalTableDefineMap 表信息
     * @param physicalColumnDefineMap 列信息
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private void saveColumnRelation(Map<String, Object> selfDefineConfig, Map<String, PhysicalTableDefine> physicalTableDefineMap,
            Map<String, Map<String, ColumnDefine>> physicalColumnDefineMap) {
        Map<String, Object> columnRelationsMap = (Map<String, Object>) selfDefineConfig.get("columnRelations");
        if (MapUtils.isNotEmpty(columnRelationsMap)) {
            Map<String, Object> tempMap = null;
            String oldTableId = null;
            String tableId = null;
            List<Map<String, String>> relationMapList = null;
            List<Map<String, String>> columnSpliceList = null;
            List<Map<String, String>> columnSplitList = null;
            List<Map<String, String>> columnBusinessList = null;
            List<Map<String, String>> columnOperationList = null;
            Map<String, Map<String, String>> columnSpliceMap = null;
            Map<String, Map<String, String>> columnSplitMap = null;
            Map<String, Map<String, String>> columnBusinessMap = null;
            Map<String, Map<String, String>> columnOperationMap = null;
            ColumnSplice columnSplice = null;
            ColumnSplit columnSplit = null;
            ColumnBusiness columnBusiness = null;
            ColumnOperation columnOperation = null;
            Map<String, ColumnDefine> tempColumnDefineMap = null;
            List<ColumnSplice> dbColumnSpliceList = null;
            List<ColumnSplit> dbColumnSplitList = null;
            List<ColumnOperation> dbColumnOperationList = null;
            List<ColumnBusiness> dbColumnBusinessList = null;
            for (Iterator<String> iterator = columnRelationsMap.keySet().iterator(); iterator.hasNext();) {
                oldTableId = iterator.next();
                tableId = physicalTableDefineMap.get(oldTableId).getId();
                tempMap = (Map<String, Object>) columnRelationsMap.get(oldTableId);
                relationMapList = (List<Map<String, String>>) tempMap.get("columnRelation");
                if (CollectionUtils.isNotEmpty(relationMapList)) {
                    columnSpliceList = (List<Map<String, String>>) tempMap.get("columnSplice");
                    columnSplitList = (List<Map<String, String>>) tempMap.get("columnSplit");
                    columnBusinessList = (List<Map<String, String>>) tempMap.get("columnBusiness");
                    columnOperationList = (List<Map<String, String>>) tempMap.get("columnOperation");
                    columnSpliceMap = parseColumnRelationList2Map(columnSpliceList);
                    columnSplitMap = parseColumnRelationList2Map(columnSplitList);
                    columnBusinessMap = parseColumnRelationList2Map(columnBusinessList);
                    columnOperationMap = parseColumnRelationList2Map(columnOperationList);
                    dbColumnSpliceList = getService(ColumnSpliceService.class).findByTableId(tableId);
                    dbColumnSplitList = getService(ColumnSplitService.class).findByTableId(tableId);
                    dbColumnOperationList = getService(ColumnOperationService.class).findByTableId(tableId);
                    dbColumnBusinessList = getService(ColumnBusinessService.class).findByTableId(tableId);
                    for (Map<String, String> relationMap : relationMapList) {
                        if ("0".equals(relationMap.get("type"))) {
                            Map<String, String> tempColumnSpliceMap = columnSpliceMap.get(relationMap.get("id"));
                            if (tempColumnSpliceMap != null) {
                                tempColumnDefineMap = physicalColumnDefineMap.get(oldTableId);
                                columnSplice = new ColumnSplice();
                                columnSplice.setTableId(tableId);
                                columnSplice.setName(tempColumnSpliceMap.get("name"));
                                columnSplice.setStoreColumnId(tempColumnDefineMap.get(tempColumnSpliceMap.get("storeColumnId")).getId());
                                columnSplice.setColumnNum(Integer.valueOf(StringUtil.null2zero(tempColumnSpliceMap.get("columnNum"))));
                                columnSplice.setPrefix(tempColumnSpliceMap.get("prefix"));
                                columnSplice.setSuffix(tempColumnSpliceMap.get("suffix"));
                                columnSplice.setSeperator1(tempColumnSpliceMap.get("seperator1"));
                                columnSplice.setSeperator2(tempColumnSpliceMap.get("seperator2"));
                                columnSplice.setSeperator3(tempColumnSpliceMap.get("seperator3"));
                                columnSplice.setSeperator4(tempColumnSpliceMap.get("seperator4"));
                                columnSplice.setColumn5Id(StringUtil.isEmpty(tempColumnSpliceMap.get("column5Id")) ? null : tempColumnDefineMap.get(
                                        tempColumnSpliceMap.get("column5Id")).getId());
                                columnSplice.setColumn1Id(StringUtil.isEmpty(tempColumnSpliceMap.get("column1Id")) ? null : tempColumnDefineMap.get(
                                        tempColumnSpliceMap.get("column1Id")).getId());
                                columnSplice.setColumn2Id(StringUtil.isEmpty(tempColumnSpliceMap.get("column2Id")) ? null : tempColumnDefineMap.get(
                                        tempColumnSpliceMap.get("column2Id")).getId());
                                columnSplice.setColumn3Id(StringUtil.isEmpty(tempColumnSpliceMap.get("column3Id")) ? null : tempColumnDefineMap.get(
                                        tempColumnSpliceMap.get("column3Id")).getId());
                                columnSplice.setColumn4Id(StringUtil.isEmpty(tempColumnSpliceMap.get("column4Id")) ? null : tempColumnDefineMap.get(
                                        tempColumnSpliceMap.get("column4Id")).getId());
                                columnSplice.setFillingNum1(Integer.valueOf(StringUtil.null2zero(tempColumnSpliceMap.get("fillingNum1"))));
                                columnSplice.setFillingNum2(Integer.valueOf(StringUtil.null2zero(tempColumnSpliceMap.get("fillingNum2"))));
                                columnSplice.setFillingNum3(Integer.valueOf(StringUtil.null2zero(tempColumnSpliceMap.get("fillingNum3"))));
                                columnSplice.setFillingNum4(Integer.valueOf(StringUtil.null2zero(tempColumnSpliceMap.get("fillingNum4"))));
                                columnSplice.setFillingNum5(Integer.valueOf(StringUtil.null2zero(tempColumnSpliceMap.get("fillingNum5"))));
                                if (!dbColumnSpliceList.contains(columnSplice)) {
                                    getService(ColumnSpliceService.class).save(columnSplice);
                                }
                            }
                        } else if ("1".equals(relationMap.get("type"))) {
                            Map<String, String> tempColumnSplitMap = columnSplitMap.get(relationMap.get("id"));
                            if (tempColumnSplitMap != null) {
                                tempColumnDefineMap = physicalColumnDefineMap.get(oldTableId);
                                columnSplit = new ColumnSplit();
                                columnSplit.setTableId(tableId);
                                columnSplit.setName(tempColumnSplitMap.get("name"));
                                columnSplit.setFromColumnId(tempColumnDefineMap.get(tempColumnSplitMap.get("fromColumnId")).getId());
                                columnSplit.setToColumnId(tempColumnDefineMap.get(tempColumnSplitMap.get("toColumnId")).getId());
                                columnSplit.setStartPosition(Integer.valueOf(StringUtil.null2zero(tempColumnSplitMap.get("startPosition"))));
                                columnSplit.setEndPosition(Integer.valueOf(StringUtil.null2zero(tempColumnSplitMap.get("endPosition"))));
                                if (!dbColumnSplitList.contains(columnSplit)) {
                                    getService(ColumnSplitService.class).save(columnSplit);
                                }
                            }
                        } else if ("2".equals(relationMap.get("type")) || "3".equals(relationMap.get("type")) || "4".equals(relationMap.get("type"))) {
                            Map<String, String> tempColumnOperationMap = columnOperationMap.get(relationMap.get("id"));
                            if (tempColumnOperationMap != null) {
                                tempColumnDefineMap = physicalColumnDefineMap.get(oldTableId);
                                String originTableName = tempColumnOperationMap.get("originTableName");
                                String originColumnName = tempColumnOperationMap.get("originColumnName");
                                PhysicalTableDefine physicalTableDefine = getService(PhysicalTableDefineService.class).getByTableName(originTableName);
                                if (physicalTableDefine != null) {
                                    ColumnDefine columnDefine = getService(ColumnDefineService.class).findByColumnNameAndTableId(originColumnName,
                                            physicalTableDefine.getId());
                                    if (columnDefine != null) {
                                        columnOperation = new ColumnOperation();
                                        columnOperation.setTableId(tableId);
                                        columnOperation.setName(tempColumnOperationMap.get("name"));
                                        columnOperation.setType(tempColumnOperationMap.get("type"));
                                        columnOperation.setColumnId(tempColumnDefineMap.get(tempColumnOperationMap.get("columnId")).getId());
                                        columnOperation.setOriginTableId(physicalTableDefine.getId());
                                        columnOperation.setOriginColumnId(columnDefine.getId());
                                        columnOperation.setOperator(StringUtil.isEmpty(tempColumnOperationMap.get("operator")) ? null : tempColumnOperationMap
                                                .get("operator"));
                                        if (!dbColumnOperationList.contains(columnOperation)) {
                                            getService(ColumnOperationService.class).save(columnOperation);
                                        }
                                    }
                                }
                            }
                        } else if ("5".equals(relationMap.get("type"))) {
                            Map<String, String> tempColumnBusinessMap = columnBusinessMap.get(relationMap.get("id"));
                            if (tempColumnBusinessMap != null) {
                                tempColumnDefineMap = physicalColumnDefineMap.get(oldTableId);
                                columnBusiness = new ColumnBusiness();
                                columnBusiness.setTableId(tableId);
                                columnBusiness.setName(tempColumnBusinessMap.get("name"));
                                columnBusiness.setItemColumnId(tempColumnDefineMap.get(tempColumnBusinessMap.get("itemColumnId")).getId());
                                columnBusiness.setPagesColumnId(tempColumnDefineMap.get(tempColumnBusinessMap.get("pagesColumnId")).getId());
                                columnBusiness.setPagenoColumnId(tempColumnDefineMap.get(tempColumnBusinessMap.get("pagenoColumnId")).getId());
                                columnBusiness.setPtype(Integer.valueOf(StringUtil.null2zero(tempColumnBusinessMap.get("ptype"))));
                                if (!dbColumnBusinessList.contains(columnBusiness)) {
                                    getService(ColumnBusinessService.class).save(columnBusiness);
                                }
                            }
                        }
                    }
                }
                getService(TriggerService.class).generateColumnRelationTrigger(tableId);
            }
        }
    }

    /**
     * 字段关联定义List转换成Map
     * 
     * @param list 字段关联定义List
     * @return Map<String, Map<String, String>>
     */
    private Map<String, Map<String, String>> parseColumnRelationList2Map(List<Map<String, String>> list) {
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        if (CollectionUtils.isNotEmpty(list)) {
            String columnRelationId = null;
            for (Map<String, String> temp : list) {
                columnRelationId = temp.get("columnRelationId");
                map.put(columnRelationId, temp);
            }
        }
        return map;
    }

    /**
     * 保存自定义配置信息中的模块
     * 
     * @param selfDefineConfig 自定义构件配置信息
     * @param componentVersion 构件版本
     * @param module 模块
     * @param physicalTableDefineMap 表信息
     * @param physicalColumnDefineMap 列信息
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private void saveModule(Map<String, Object> selfDefineConfig, ComponentVersion componentVersion, Module module,
            Map<String, PhysicalTableDefine> physicalTableDefineMap, Map<String, Map<String, ColumnDefine>> physicalColumnDefineMap, String areaId) {
        String moduleId = getService(ModuleService.class).getIdByName(module.getName());
        if (StringUtil.isNotEmpty(moduleId)) {
            getService(ModuleService.class).deleteModule(moduleId);
        }
        Integer maxModuleShowOrder = getDaoFromContext(ModuleDao.class).getMaxShowOrderByComponentAreaId(componentVersion.getAreaId());
        module.setShowOrder(maxModuleShowOrder == null ? 0 : maxModuleShowOrder);
        module.setComponentVersionId(componentVersion.getId());
        module.setComponentAreaId(componentVersion.getAreaId());
        if (ConstantVar.Component.Type.TREE.equals(module.getType())) {
            Map<String, TreeDefine> treeDefineMap = (Map<String, TreeDefine>) selfDefineConfig.get("tree");
            if (MapUtils.isNotEmpty(treeDefineMap)) {
                String treeId = saveSelfDefineTree(selfDefineConfig, physicalTableDefineMap, physicalColumnDefineMap);
                module.setTreeId(treeId);
            }
        } else if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(module.getType())) {
            String table1Name = module.getTable1Id();
            String table2Name = module.getTable2Id();
            String table3Name = module.getTable3Id();
            PhysicalTableDefine physicalTableDefine = null;
            if (StringUtil.isNotEmpty(table1Name)) {
                physicalTableDefine = getService(PhysicalTableDefineService.class).getByTableName(table1Name);
                if (physicalTableDefine != null) {
                    module.setTable1Id(physicalTableDefine.getId());
                }
            }
            if (StringUtil.isNotEmpty(table2Name)) {
                physicalTableDefine = getService(PhysicalTableDefineService.class).getByTableName(table2Name);
                if (physicalTableDefine != null) {
                    module.setTable2Id(physicalTableDefine.getId());
                }
            }
            if (StringUtil.isNotEmpty(table3Name)) {
                physicalTableDefine = getService(PhysicalTableDefineService.class).getByTableName(table3Name);
                if (physicalTableDefine != null) {
                    module.setTable3Id(physicalTableDefine.getId());
                }
            }
        }
        module.updateAreaLayout();
        // getService(ModuleService.class).save(module);
        String areaLayout = module.getAreaLayout();
        if (DatabaseHandlerDao.isOracle()) {
            areaLayout = parseStrByDatabaseType(areaLayout, DatabaseHandlerDao.DB_ORACLE);
        } else {
            areaLayout = parseStrByDatabaseType(areaLayout, DatabaseHandlerDao.DB_SQLSERVER);
        }
        String moduleSql = "insert into t_xtpz_module(id,type,logic_table_group_code,template_type,"
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
                + areaLayout
                + "','"
                + StringUtil.null2empty(module.getUiType())
                + "',"
                + StringUtil.null2zero(module.getShowOrder())
                + ",'"
                + StringUtil.null2empty(module.getRemark())
                + "','"
                + StringUtil.null2empty(module.getComponentVersionId()) + "','" + StringUtil.null2empty(module.getComponentUrl()) + "','" + areaId + "')";
        DatabaseHandlerDao.getInstance().executeSql(moduleSql);
        // 更改构件访问地址
        // String url = componentVersion.getUrl();
        // url = url.substring(0, url.indexOf("P_moduleId=") + "P_moduleId=".length());
        // url += module.getId();
        // componentVersion.setUrl(url);
        // getDao().save(componentVersion);
        // componentVersion.getComponent().setCode(module.getId());
        // getService(ComponentService.class).save(componentVersion.getComponent());
    }

    /**
     * 保存自定义配置信息中的树
     * 
     * @param selfDefineConfig 自定义构件配置信息
     * @param physicalTableDefineMap 表信息
     * @param physicalColumnDefineMap 列信息
     * @return String
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private String saveSelfDefineTree(Map<String, Object> selfDefineConfig, Map<String, PhysicalTableDefine> physicalTableDefineMap,
            Map<String, Map<String, ColumnDefine>> physicalColumnDefineMap) {
        Map<String, TreeDefine> treeDefineMap = (Map<String, TreeDefine>) selfDefineConfig.get("tree");
        TreeDefine treeDefine = null;
        // 1、保存树节点
        String oldTableId = null;
        String oldDbId = null;
        String rootId = null;
        TreeDefine dbTreeDefine = null;
        Map<String, String> workflowDefineIdMap = (Map<String, String>) selfDefineConfig.get("workflowDefineIdMap");
        for (Iterator<Entry<String, TreeDefine>> iterator = treeDefineMap.entrySet().iterator(); iterator.hasNext();) {
            treeDefine = iterator.next().getValue();
            // 根节点
            if ("-1".equals(treeDefine.getParentId())) {
                dbTreeDefine = getDaoFromContext(TreeDefineDao.class).findOne(treeDefine.getId());
                if (dbTreeDefine == null) {
                    dbTreeDefine = getDaoFromContext(TreeDefineDao.class).getTreeByName(treeDefine.getName());
                    if (dbTreeDefine != null) {
                        if (!treeDefine.getId().equals(dbTreeDefine.getId())) {
                            dbTreeDefine.setName(treeDefine.getName());
                            dbTreeDefine.setShowRoot(treeDefine.getShowRoot());
                            treeDefine = dbTreeDefine;
                        }
                    }
                }
                getDaoFromContext(TreeDefineDao.class).save(treeDefine);
                rootId = treeDefine.getId();
                continue;
            }
            // 如果是动态节点生成的节点，则不导入
            if ("1".equals(treeDefine.getDynamic())) {
                iterator.remove();
                continue;
            }
            dbTreeDefine = getService(TreeDefineService.class).getByID(treeDefine.getId());
            if (dbTreeDefine != null) {
                // 表节点
                if (TreeDefine.T_TABLE.equals(treeDefine.getType())) {
                    oldTableId = treeDefine.getTableId();
                    if (StringUtil.isNotEmpty(oldTableId)) {
                        dbTreeDefine.setType(TreeDefine.T_TABLE);
                        dbTreeDefine.setTableId(physicalTableDefineMap.get(oldTableId).getId());
                        dbTreeDefine.setDbId(treeDefine.getTableId());
                    }
                }
                // 表字段节点
                if (TreeDefine.T_COLUMN_TAB.equals(treeDefine.getType())) {
                    oldTableId = treeDefine.getTableId();
                    if (StringUtil.isNotEmpty(oldTableId)) {
                        dbTreeDefine.setType(TreeDefine.T_COLUMN_TAB);
                        dbTreeDefine.setTableId(physicalTableDefineMap.get(oldTableId).getId());
                        oldDbId = treeDefine.getDbId();
                        if (StringUtil.isNotEmpty(oldDbId)) {
                            dbTreeDefine.setDbId(physicalColumnDefineMap.get(oldTableId).get(oldDbId).getId());
                        }
                    }
                }
                // 工作流节点
                if (TreeDefine.T_COFLOW.equals(treeDefine.getType())) {
                    oldDbId = treeDefine.getDbId();
                    if (StringUtil.isNotEmpty(oldDbId)) {
                        dbTreeDefine.setType(TreeDefine.T_COFLOW);
                        dbTreeDefine.setDbId(workflowDefineIdMap.get(oldDbId));
                        dbTreeDefine.setTableId(treeDefine.getDbId());
                    }
                }
                // 空节点
                if (TreeDefine.T_EMPTY.equals(treeDefine.getType())) {
                    dbTreeDefine.setType(TreeDefine.T_EMPTY);
                    dbTreeDefine.setDbId(treeDefine.getDbId());
                }
                // 字段节点（跨表）
                if (TreeDefine.T_COLUMN_EMP.equals(treeDefine.getType())) {
                    dbTreeDefine.setType(TreeDefine.T_COLUMN_EMP);
                    dbTreeDefine.setDbId(treeDefine.getDbId());
                    dbTreeDefine.setValue(treeDefine.getValue());
                }
                // 物理表组节点
                if (TreeDefine.T_GROUP.equals(treeDefine.getType())) {
                    dbTreeDefine.setType(TreeDefine.T_GROUP);
                    dbTreeDefine.setDbId(treeDefine.getDbId());
                }
                dbTreeDefine.setName(treeDefine.getName());
                dbTreeDefine.setChild(treeDefine.getChild());
                getDaoFromContext(TreeDefineDao.class).save(dbTreeDefine);
            } else {
                // 表节点
                if (TreeDefine.T_TABLE.equals(treeDefine.getType())) {
                    oldTableId = treeDefine.getTableId();
                    if (StringUtil.isNotEmpty(oldTableId)) {
                        treeDefine.setTableId(physicalTableDefineMap.get(oldTableId).getId());
                        treeDefine.setDbId(treeDefine.getTableId());
                    }
                }
                // 表字段节点
                if (TreeDefine.T_COLUMN_TAB.equals(treeDefine.getType())) {
                    oldTableId = treeDefine.getTableId();
                    if (StringUtil.isNotEmpty(oldTableId)) {
                        treeDefine.setTableId(physicalTableDefineMap.get(oldTableId).getId());
                        oldDbId = treeDefine.getDbId();
                        if (StringUtil.isNotEmpty(oldDbId)) {
                            treeDefine.setDbId(physicalColumnDefineMap.get(oldTableId).get(oldDbId).getId());
                        }
                    }
                }
                // 工作流节点
                if (TreeDefine.T_COFLOW.equals(treeDefine.getType())) {
                    oldDbId = treeDefine.getDbId();
                    if (StringUtil.isNotEmpty(oldDbId)) {
                        treeDefine.setDbId(workflowDefineIdMap.get(oldDbId));
                        treeDefine.setTableId(treeDefine.getDbId());
                    }
                }
                getDaoFromContext(TreeDefineDao.class).save(treeDefine);
            }
        }
        // 2、更改树节点的parentId和根节点
        for (TreeDefine temp : treeDefineMap.values()) {
            if ("-1".equals(temp.getParentId())) {
                continue;
            }
            TreeDefine parent = null;
            for (String oldArchiveTreeId : treeDefineMap.keySet()) {
                if (temp.getParentId().equals(oldArchiveTreeId)) {
                    parent = treeDefineMap.get(oldArchiveTreeId);
                    if (!temp.getParentId().equals(parent.getId())) {
                        temp.setParentId(parent.getId());
                        temp.setRootId(rootId);
                        getDaoFromContext(TreeDefineDao.class).save(temp);
                    }
                    break;
                }
            }
        }
        return rootId;
    }

    /**
     * 保存自定义配置信息中的应用自定义
     * 
     * @param selfDefineConfig 自定义构件配置信息
     * @param physicalTableDefineMap 表信息
     * @param physicalColumnDefineMap 列信息
     * @param module 模块
     */
    @SuppressWarnings({"unchecked"})
    @Transactional
    private void saveAppDefine(Map<String, Object> selfDefineConfig, Map<String, PhysicalTableDefine> physicalTableDefineMap,
            Map<String, Map<String, ColumnDefine>> physicalColumnDefineMap, Module module) {
        Map<String, Object> applicationMap = (Map<String, Object>) selfDefineConfig.get("application");
        if (applicationMap != null) {
            Map<String, String> workflowVersionIdMap = (Map<String, String>) selfDefineConfig.get("workflowVersionIdMap");
            if (workflowVersionIdMap == null) {
                workflowVersionIdMap = new HashMap<String, String>();
            }
            List<Map<String, String>> appDefineMapList = (List<Map<String, String>>) applicationMap.get("appDefines");
            List<Map<String, Object>> appSearchMapList = (List<Map<String, Object>>) applicationMap.get("appSearchs");
            List<Map<String, Object>> appColumnMapList = (List<Map<String, Object>>) applicationMap.get("appColumns");
            List<Map<String, Object>> appSortMapList = (List<Map<String, Object>>) applicationMap.get("appSorts");
            List<Map<String, Object>> appFormMapList = (List<Map<String, Object>>) applicationMap.get("appForms");
            List<Map<String, Object>> appReportMapList = (List<Map<String, Object>>) applicationMap.get("appReports");
            List<Map<String, Object>> appGridButtonMapList = (List<Map<String, Object>>) applicationMap.get("appGridButtons");
            List<Map<String, Object>> appFormButtonMapList = (List<Map<String, Object>>) applicationMap.get("appFormButtons");
            String oldTableId = null;
            PhysicalTableDefine newTable = null;
            String newTableId = null;
            Map<String, ColumnDefine> columnDefineColumnMap = null;
            String newColumnId = null;
            ColumnDefine newColumn = null;
            String newWorkflowVersionId = null;
            if (CollectionUtils.isNotEmpty(appDefineMapList)) {
                AppDefine appDefine = null;
                AppDefine dbAppDefine = null;
                for (Map<String, String> appDefineMap : appDefineMapList) {
                    oldTableId = appDefineMap.get("tableId");
                    newTable = physicalTableDefineMap.get(oldTableId);
                    if (newTable == null) {
                        continue;
                    }
                    newTableId = newTable.getId();
                    String newMenuId = null;
                    String oldMenuId = appDefineMap.get("menuId");
                    if (workflowVersionIdMap.containsKey(oldMenuId)) {
                        newWorkflowVersionId = workflowVersionIdMap.get(oldMenuId);
                        if (newWorkflowVersionId == null) {
                            continue;
                        }
                        newMenuId = newWorkflowVersionId;
                    } else {
                        newMenuId = AppDefine.DEFAULT_DEFINE_ID;
                    }
                    dbAppDefine = getDaoFromContext(AppDefineDao.class).findByFk(newTableId, appDefineMap.get("componentVersionId"), newMenuId,
                            CommonUtil.SUPER_ADMIN_ID);
                    if (dbAppDefine != null) {
                        // 删除旧的配置
                        appDefine = dbAppDefine;
                    } else {
                        appDefine = new AppDefine();
                    }
                    appDefine.setTableId(newTableId);
                    appDefine.setComponentVersionId(StringUtil.null2empty(appDefineMap.get("componentVersionId")));
                    appDefine.setMenuId(newMenuId);
                    appDefine.setUserId(CommonUtil.SUPER_ADMIN_ID);
                    appDefine.setSearched(appDefineMap.get("searched"));
                    appDefine.setColumned(appDefineMap.get("columned"));
                    appDefine.setSorted(appDefineMap.get("sorted"));
                    appDefine.setFormed(appDefineMap.get("formed"));
                    appDefine.setReported(appDefineMap.get("reported"));
                    appDefine.setGridButtoned(appDefineMap.get("gridButtoned"));
                    appDefine.setFormButtoned(appDefineMap.get("formButtoned"));
                    getService(AppDefineService.class).save(appDefine);
                }
            }
            if (CollectionUtils.isNotEmpty(appSearchMapList)) {
                AppSearchPanel appSearchPanel = null;
                AppSearch appSearch = null;
                for (Map<String, Object> appSearchMap : appSearchMapList) {
                    oldTableId = StringUtil.null2empty(appSearchMap.get("tableId"));
                    newTable = physicalTableDefineMap.get(oldTableId);
                    if (newTable == null) {
                        continue;
                    }
                    newTableId = newTable.getId();
                    String newMenuId = null;
                    String oldMenuId = StringUtil.null2empty(appSearchMap.get("menuId"));
                    if (workflowVersionIdMap.containsKey(oldMenuId)) {
                        newWorkflowVersionId = workflowVersionIdMap.get(oldMenuId);
                        if (newWorkflowVersionId == null) {
                            continue;
                        }
                        newMenuId = newWorkflowVersionId;
                    } else {
                        newMenuId = AppDefine.DEFAULT_DEFINE_ID;
                    }
                    // 删除旧的配置
                    getDaoFromContext(AppSearchPanelDao.class).deleteByFk(newTableId, StringUtil.null2empty(appSearchMap.get("componentVersionId")), newMenuId,
                            CommonUtil.SUPER_ADMIN_ID);
                    getDaoFromContext(AppSearchDao.class).deleteByFk(newTableId, StringUtil.null2empty(appSearchMap.get("componentVersionId")), newMenuId,
                            CommonUtil.SUPER_ADMIN_ID);
                    appSearchPanel = new AppSearchPanel();
                    appSearchPanel.setTableId(newTableId);
                    appSearchPanel.setComponentVersionId(StringUtil.null2empty(appSearchMap.get("componentVersionId")));
                    appSearchPanel.setMenuId(newMenuId);
                    appSearchPanel.setUserId(CommonUtil.SUPER_ADMIN_ID);
                    appSearchPanel.setColspan(Integer.valueOf(StringUtil.null2zero(appSearchMap.get("colspan"))));
                    getService(AppSearchPanelService.class).save(appSearchPanel);
                    List<Map<String, String>> baseSearchColumnMapList = (List<Map<String, String>>) appSearchMap.get("baseSearchColumns");
                    if (CollectionUtils.isNotEmpty(baseSearchColumnMapList)) {
                        for (Map<String, String> baseSearchColumnMap : baseSearchColumnMapList) {
                            columnDefineColumnMap = physicalColumnDefineMap.get(oldTableId);
                            newColumn = columnDefineColumnMap.get(baseSearchColumnMap.get("columnId"));
                            if (newColumn == null) {
                                continue;
                            }
                            newColumnId = newColumn.getId();
                            appSearch = new AppSearch();
                            appSearch.setTableId(newTableId);
                            appSearch.setComponentVersionId(StringUtil.null2empty(appSearchMap.get("componentVersionId")));
                            appSearch.setMenuId(newMenuId);
                            appSearch.setUserId(CommonUtil.SUPER_ADMIN_ID);
                            appSearch.setColumnId(newColumnId);
                            appSearch.setShowName(baseSearchColumnMap.get("showName"));
                            appSearch.setShowOrder(Integer.valueOf(baseSearchColumnMap.get("showOrder")));
                            appSearch.setFilterType(baseSearchColumnMap.get("filterType"));
                            getService(AppSearchService.class).save(appSearch);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(appColumnMapList)) {
                AppGrid appGrid = null;
                AppColumn appColumn = null;
                for (Map<String, Object> appColumnMap : appColumnMapList) {
                    oldTableId = StringUtil.null2empty(appColumnMap.get("tableId"));
                    newTable = physicalTableDefineMap.get(oldTableId);
                    if (newTable == null) {
                        continue;
                    }
                    newTableId = newTable.getId();
                    String newMenuId = null;
                    String oldMenuId = StringUtil.null2empty(appColumnMap.get("menuId"));
                    if (workflowVersionIdMap.containsKey(oldMenuId)) {
                        newWorkflowVersionId = workflowVersionIdMap.get(oldMenuId);
                        if (newWorkflowVersionId == null) {
                            continue;
                        }
                        newMenuId = newWorkflowVersionId;
                    } else {
                        newMenuId = AppDefine.DEFAULT_DEFINE_ID;
                    }
                    // 删除旧的配置
                    getDaoFromContext(AppGridDao.class).deleteByFk(newTableId, StringUtil.null2empty(appColumnMap.get("componentVersionId")), newMenuId,
                            CommonUtil.SUPER_ADMIN_ID);
                    getDaoFromContext(AppColumnDao.class).deleteByFk(newTableId, StringUtil.null2empty(appColumnMap.get("componentVersionId")), newMenuId,
                            CommonUtil.SUPER_ADMIN_ID);
                    appGrid = new AppGrid();
                    appGrid.setTableId(newTableId);
                    appGrid.setComponentVersionId(StringUtil.null2empty(appColumnMap.get("componentVersionId")));
                    appGrid.setMenuId(newMenuId);
                    appGrid.setUserId(CommonUtil.SUPER_ADMIN_ID);
                    appGrid.setHasRowNumber(Integer.valueOf(StringUtil.null2zero(appColumnMap.get("hasRowNumber"))));
                    appGrid.setDblclick(Integer.valueOf(StringUtil.null2zero(appColumnMap.get("dblclick"))));
                    appGrid.setSearchType(Integer.valueOf(StringUtil.null2zero(appColumnMap.get("searchType"))));
                    appGrid.setPageable(Integer.valueOf(StringUtil.null2zero(appColumnMap.get("pageable"))));
                    appGrid.setAdaptive(Integer.valueOf(StringUtil.null2zero(appColumnMap.get("adaptive"))));
                    appGrid.setOpeColPosition(Integer.valueOf(StringUtil.null2zero(appColumnMap.get("opeColPosition"))));
                    appGrid.setOpeColName(StringUtil.null2zero(appColumnMap.get("opeColName")));
                    appGrid.setOpeColWidth(Integer.valueOf(StringUtil.null2zero(appColumnMap.get("opeColWidth"))));
                    appGrid.setHeaderSetting(Integer.valueOf(StringUtil.null2zero(appColumnMap.get("headerSetting"))));
                    getService(AppGridService.class).save(appGrid);
                    List<Map<String, String>> gridColumnMapList = (List<Map<String, String>>) appColumnMap.get("gridColumns");
                    if (CollectionUtils.isNotEmpty(gridColumnMapList)) {
                        for (Map<String, String> gridColumnMap : gridColumnMapList) {
                            columnDefineColumnMap = physicalColumnDefineMap.get(oldTableId);
                            String oldColumnId = gridColumnMap.get("columnId");
                            if ("-1".equals(oldColumnId)) {
                                newColumnId = "-1";
                            } else {
                                newColumn = columnDefineColumnMap.get(oldColumnId);
                                if (newColumn == null) {
                                    continue;
                                }
                                newColumnId = newColumn.getId();
                            }
                            appColumn = new AppColumn();
                            appColumn.setTableId(newTableId);
                            appColumn.setComponentVersionId(StringUtil.null2empty(appColumnMap.get("componentVersionId")));
                            appColumn.setMenuId(newMenuId);
                            appColumn.setUserId(CommonUtil.SUPER_ADMIN_ID);
                            appColumn.setColumnId(newColumnId);
                            appColumn.setShowName(gridColumnMap.get("showName"));
                            appColumn.setWidth(Integer.valueOf(gridColumnMap.get("width")));
                            appColumn.setAlign(gridColumnMap.get("align"));
                            appColumn.setType(StringUtil.null2empty(gridColumnMap.get("type")));
                            appColumn.setUrl(gridColumnMap.get("url"));
                            appColumn.setColumnName(gridColumnMap.get("columnName"));
                            appColumn.setColumnAlias(gridColumnMap.get("columnAlias"));
                            appColumn.setColumnType(gridColumnMap.get("columnType"));
                            appColumn.setShowOrder(Integer.valueOf(gridColumnMap.get("showOrder")));
                            getService(AppColumnService.class).save(appColumn);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(appSortMapList)) {
                AppSort appSort = null;
                for (Map<String, Object> appSortMap : appSortMapList) {
                    oldTableId = StringUtil.null2empty(appSortMap.get("tableId"));
                    newTable = physicalTableDefineMap.get(oldTableId);
                    if (newTable == null) {
                        continue;
                    }
                    String newMenuId = null;
                    String oldMenuId = StringUtil.null2empty(appSortMap.get("menuId"));
                    if (workflowVersionIdMap.containsKey(oldMenuId)) {
                        newWorkflowVersionId = workflowVersionIdMap.get(oldMenuId);
                        if (newWorkflowVersionId == null) {
                            continue;
                        }
                        newMenuId = newWorkflowVersionId;
                    } else {
                        newMenuId = AppDefine.DEFAULT_DEFINE_ID;
                    }
                    newTableId = newTable.getId();
                    List<Map<String, String>> sortColumnMapList = (List<Map<String, String>>) appSortMap.get("sortColumns");
                    if (CollectionUtils.isNotEmpty(sortColumnMapList)) {
                        for (Map<String, String> sortColumnMap : sortColumnMapList) {
                            columnDefineColumnMap = physicalColumnDefineMap.get(oldTableId);
                            newColumn = columnDefineColumnMap.get(sortColumnMap.get("columnId"));
                            if (newColumn == null) {
                                continue;
                            }
                            newColumnId = newColumn.getId();
                            // 删除旧的配置
                            getDaoFromContext(AppSortDao.class).deleteByFk(newTableId, StringUtil.null2empty(appSortMap.get("componentVersionId")), newMenuId,
                                    CommonUtil.SUPER_ADMIN_ID);
                            appSort = new AppSort();
                            appSort.setTableId(newTableId);
                            appSort.setComponentVersionId(StringUtil.null2empty(appSortMap.get("componentVersionId")));
                            appSort.setMenuId(newMenuId);
                            appSort.setUserId(CommonUtil.SUPER_ADMIN_ID);
                            appSort.setColumnId(newColumnId);
                            appSort.setSortType(sortColumnMap.get("sortType"));
                            appSort.setShowOrder(Integer.valueOf(sortColumnMap.get("showOrder")));
                            getService(AppSortService.class).save(appSort);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(appFormMapList)) {
                AppForm appForm = null;
                AppFormElement appFormElement = null;
                for (Map<String, Object> appFormMap : appFormMapList) {
                    oldTableId = StringUtil.null2empty(appFormMap.get("tableId"));
                    newTable = physicalTableDefineMap.get(oldTableId);
                    if (newTable == null) {
                        continue;
                    }
                    newTableId = newTable.getId();
                    String newMenuId = null;
                    String oldMenuId = StringUtil.null2empty(appFormMap.get("menuId"));
                    if (workflowVersionIdMap.containsKey(oldMenuId)) {
                        newWorkflowVersionId = workflowVersionIdMap.get(oldMenuId);
                        if (newWorkflowVersionId == null) {
                            continue;
                        }
                        newMenuId = newWorkflowVersionId;
                    } else {
                        newMenuId = AppDefine.DEFAULT_DEFINE_ID;
                    }
                    // 删除旧的配置
                    getDaoFromContext(AppFormDao.class).deleteByFk(newTableId, StringUtil.null2empty(appFormMap.get("componentVersionId")), newMenuId);
                    getDaoFromContext(AppFormElementDao.class).deleteByFk(newTableId, StringUtil.null2empty(appFormMap.get("componentVersionId")), newMenuId);
                    appForm = new AppForm();
                    appForm.setTableId(newTableId);
                    appForm.setComponentVersionId(StringUtil.null2empty(appFormMap.get("componentVersionId")));
                    appForm.setMenuId(newMenuId);
                    appForm.setColspan(Integer.valueOf(StringUtil.null2zero(appFormMap.get("colspan"))));
                    appForm.setBorder(StringUtil.null2empty(appFormMap.get("border")));
                    appForm.setType(StringUtil.null2empty(appFormMap.get("type")));
                    getService(AppFormService.class).save(appForm);
                    List<Map<String, String>> appFormElementMapList = (List<Map<String, String>>) appFormMap.get("formElements");
                    if (CollectionUtils.isNotEmpty(appFormElementMapList)) {
                        for (Map<String, String> appFormElementMap : appFormElementMapList) {
                            columnDefineColumnMap = physicalColumnDefineMap.get(oldTableId);
                            String oldColumnId = appFormElementMap.get("columnId");
                            if ("-1".equals(oldColumnId)) {
                                newColumnId = "-1";
                            } else {
                                newColumn = columnDefineColumnMap.get(oldColumnId);
                                if (newColumn == null) {
                                    continue;
                                }
                                newColumnId = newColumn.getId();
                            }
                            appFormElement = new AppFormElement();
                            appFormElement.setTableId(newTableId);
                            appFormElement.setComponentVersionId(StringUtil.null2empty(appFormMap.get("componentVersionId")));
                            appFormElement.setMenuId(newMenuId);
                            appFormElement.setColumnId(newColumnId);
                            appFormElement.setShowName(appFormElementMap.get("showName"));
                            appFormElement.setColspan(Short.valueOf(appFormElementMap.get("colspan")));
                            appFormElement.setRequired(appFormElementMap.get("required"));
                            appFormElement.setReadonly(appFormElementMap.get("readonly"));
                            appFormElement.setHidden(StringUtil.null2empty(appFormElementMap.get("hidden")));
                            appFormElement.setDefaultValue(appFormElementMap.get("defaultValue"));
                            appFormElement.setKept(appFormElementMap.get("kept"));
                            appFormElement.setIncrease(appFormElementMap.get("increase"));
                            appFormElement.setInherit(appFormElementMap.get("inherit"));
                            appFormElement.setValidation(appFormElementMap.get("validation"));
                            appFormElement.setTooltip(appFormElementMap.get("tooltip"));
                            appFormElement.setShowOrder(Integer.valueOf(appFormElementMap.get("showOrder")));
                            getService(AppFormElementService.class).save(appFormElement);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(appReportMapList)) {
                AppReport appReport = null;
                for (Map<String, Object> appReportMap : appReportMapList) {
                    oldTableId = StringUtil.null2empty(appReportMap.get("tableId"));
                    newTable = physicalTableDefineMap.get(oldTableId);
                    if (newTable == null) {
                        continue;
                    }
                    newTableId = newTable.getId();
                    String newMenuId = null;
                    String oldMenuId = StringUtil.null2empty(appReportMap.get("menuId"));
                    if (workflowVersionIdMap.containsKey(oldMenuId)) {
                        newWorkflowVersionId = workflowVersionIdMap.get(oldMenuId);
                        if (newWorkflowVersionId == null) {
                            continue;
                        }
                        newMenuId = newWorkflowVersionId;
                    } else {
                        newMenuId = AppDefine.DEFAULT_DEFINE_ID;
                    }
                    // 删除旧的配置
                    getDaoFromContext(AppReportDao.class).deleteByFk(newTableId, StringUtil.null2empty(appReportMap.get("componentVersionId")), newMenuId,
                            CommonUtil.SUPER_ADMIN_ID);
                    List<Map<String, String>> buttonMapList = (List<Map<String, String>>) appReportMap.get("buttons");
                    if (CollectionUtils.isNotEmpty(buttonMapList)) {
                        for (Map<String, String> buttonMap : buttonMapList) {
                            appReport = new AppReport();
                            appReport.setTableId(newTableId);
                            appReport.setComponentVersionId(StringUtil.null2empty(appReportMap.get("componentVersionId")));
                            appReport.setMenuId(newMenuId);
                            appReport.setReportId(buttonMap.get("reportId"));
                            appReport.setShowOrder(Integer.valueOf(buttonMap.get("showOrder")));
                            getService(AppReportService.class).save(appReport);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(appGridButtonMapList)) {
                AppButton appButton = null;
                for (Map<String, Object> appButtonMap : appGridButtonMapList) {
                    oldTableId = StringUtil.null2empty(appButtonMap.get("tableId"));
                    newTable = physicalTableDefineMap.get(oldTableId);
                    if (newTable == null) {
                        continue;
                    }
                    newTableId = newTable.getId();
                    String newMenuId = null;
                    String oldMenuId = StringUtil.null2empty(appButtonMap.get("menuId"));
                    if (workflowVersionIdMap.containsKey(oldMenuId)) {
                        newWorkflowVersionId = workflowVersionIdMap.get(oldMenuId);
                        if (newWorkflowVersionId == null) {
                            continue;
                        }
                        newMenuId = newWorkflowVersionId;
                    } else {
                        newMenuId = AppDefine.DEFAULT_DEFINE_ID;
                    }
                    // 删除旧的配置
                    getDaoFromContext(AppButtonDao.class).deleteByFk(newTableId, StringUtil.null2empty(appButtonMap.get("componentVersionId")), newMenuId,
                            AppButton.BUTTON_GRID);
                    List<Map<String, String>> buttonMapList = (List<Map<String, String>>) appButtonMap.get("buttons");
                    if (CollectionUtils.isNotEmpty(buttonMapList)) {
                        for (Map<String, String> buttonMap : buttonMapList) {
                            appButton = new AppButton();
                            appButton.setTableId(newTableId);
                            appButton.setComponentVersionId(StringUtil.null2empty(appButtonMap.get("componentVersionId")));
                            appButton.setMenuId(newMenuId);
                            appButton.setButtonType(AppButton.BUTTON_GRID);
                            appButton.setButtonCode(buttonMap.get("buttonCode"));
                            appButton.setButtonName(buttonMap.get("buttonName"));
                            appButton.setShowName(buttonMap.get("showName"));
                            appButton.setDisplay(buttonMap.get("display"));
                            appButton.setRemark(buttonMap.get("remark"));
                            appButton.setShowOrder(Integer.valueOf(buttonMap.get("showOrder")));
                            getService(AppButtonService.class).save(appButton);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(appFormButtonMapList)) {
                AppButton appButton = null;
                for (Map<String, Object> appButtonMap : appFormButtonMapList) {
                    oldTableId = StringUtil.null2empty(appButtonMap.get("tableId"));
                    newTable = physicalTableDefineMap.get(oldTableId);
                    if (newTable == null) {
                        continue;
                    }
                    newTableId = newTable.getId();
                    String newMenuId = null;
                    String oldMenuId = StringUtil.null2empty(appButtonMap.get("menuId"));
                    if (workflowVersionIdMap.containsKey(oldMenuId)) {
                        newWorkflowVersionId = workflowVersionIdMap.get(oldMenuId);
                        if (newWorkflowVersionId == null) {
                            continue;
                        }
                        newMenuId = newWorkflowVersionId;
                    } else {
                        newMenuId = AppDefine.DEFAULT_DEFINE_ID;
                    }
                    // 删除旧的配置
                    getDaoFromContext(AppButtonDao.class).deleteByFk(newTableId, StringUtil.null2empty(appButtonMap.get("componentVersionId")), newMenuId,
                            AppButton.BUTTON_FORM);
                    List<Map<String, String>> buttonMapList = (List<Map<String, String>>) appButtonMap.get("buttons");
                    if (CollectionUtils.isNotEmpty(buttonMapList)) {
                        for (Map<String, String> buttonMap : buttonMapList) {
                            appButton = new AppButton();
                            appButton.setTableId(newTableId);
                            appButton.setComponentVersionId(StringUtil.null2empty(appButtonMap.get("componentVersionId")));
                            appButton.setMenuId(newMenuId);
                            appButton.setButtonType(AppButton.BUTTON_FORM);
                            appButton.setButtonCode(buttonMap.get("buttonCode"));
                            appButton.setButtonName(buttonMap.get("buttonName"));
                            appButton.setShowName(buttonMap.get("showName"));
                            appButton.setDisplay(buttonMap.get("display"));
                            appButton.setRemark(buttonMap.get("remark"));
                            appButton.setShowOrder(Integer.valueOf(buttonMap.get("showOrder")));
                            getService(AppButtonService.class).save(appButton);
                        }
                    }
                }
            }
        }
    }

    /**
     * 保存物理表组
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private void savePhysicalGroupDefine(Map<String, Object> selfDefineConfig) {
        List<PhysicalGroupDefine> physicalGroupDefineList = (List<PhysicalGroupDefine>) selfDefineConfig.get("physicalGroups");
        if (CollectionUtils.isNotEmpty(physicalGroupDefineList)) {
            PhysicalGroupDefine dbPhysicalGroupDefine = null;
            for (PhysicalGroupDefine physicalGroupDefine : physicalGroupDefineList) {
                dbPhysicalGroupDefine = getService(PhysicalGroupDefineService.class).getByID(physicalGroupDefine.getId());
                if (dbPhysicalGroupDefine == null) {
                    String physicalGroupSql = "insert into t_xtpz_physical_group_define(id,group_name,logic_group_code,code,show_order,remark) values('"
                            + physicalGroupDefine.getId() + "','" + physicalGroupDefine.getGroupName() + "','"
                            + StringUtil.null2empty(physicalGroupDefine.getLogicGroupCode()) + "','" + StringUtil.null2empty(physicalGroupDefine.getCode())
                            + "'," + physicalGroupDefine.getShowOrder() + ",'" + StringUtil.null2empty(physicalGroupDefine.getRemark()) + "')";
                    DatabaseHandlerDao.getInstance().executeSql(physicalGroupSql);
                } else {
                    dbPhysicalGroupDefine.setGroupName(physicalGroupDefine.getGroupName());
                    dbPhysicalGroupDefine.setLogicGroupCode(StringUtil.null2empty(physicalGroupDefine.getLogicGroupCode()));
                    dbPhysicalGroupDefine.setCode(StringUtil.null2empty(physicalGroupDefine.getCode()));
                    dbPhysicalGroupDefine.setRemark(StringUtil.null2empty(physicalGroupDefine.getRemark()));
                    getService(PhysicalGroupDefineService.class).save(dbPhysicalGroupDefine);
                }
            }
        }
    }

    /**
     * 保存物理表组与物理表关系
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private void savePhysicalGroupRelation(Map<String, Object> selfDefineConfig) {
        List<PhysicalGroupRelation> pGroupRelationList = (List<PhysicalGroupRelation>) selfDefineConfig.get("physicalGroupRelations");
        if (CollectionUtils.isNotEmpty(pGroupRelationList)) {
            PhysicalGroupRelation dbPhysicalGroupRelation = null;
            for (PhysicalGroupRelation physicalGroupRelation : pGroupRelationList) {
                dbPhysicalGroupRelation = getService(PhysicalGroupRelationService.class).getByID(physicalGroupRelation.getId());
                if (dbPhysicalGroupRelation == null) {
                    String physicalGroupRelationSql = "insert into t_xtpz_physical_group_relation(id,group_id,table_id,show_order) values('"
                            + physicalGroupRelation.getId() + "','" + physicalGroupRelation.getGroupId() + "','" + physicalGroupRelation.getTableId() + "',"
                            + physicalGroupRelation.getShowOrder() + ")";
                    DatabaseHandlerDao.getInstance().executeSql(physicalGroupRelationSql);
                } else {
                    dbPhysicalGroupRelation.setGroupId(physicalGroupRelation.getGroupId());
                    dbPhysicalGroupRelation.setTableId(physicalGroupRelation.getTableId());
                    getService(PhysicalGroupRelationService.class).save(dbPhysicalGroupRelation);
                }
            }
        }
    }

    /**
     * 保存工作流
     * 
     * @param selfDefineConfig 自定义构件配置信息
     * @param physicalTableDefineMap 表信息
     * @param physicalColumnDefineMap 列信息
     * @param module 模块
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Transactional
    private void saveWorkflow(Map<String, Object> selfDefineConfig, Map<String, PhysicalTableDefine> physicalTableDefineMap,
            Map<String, Map<String, ColumnDefine>> physicalColumnDefineMap, ComponentVersion componentVersion) {
        Map<String, Object> workflowMap = (Map<String, Object>) selfDefineConfig.get("workflow");
        if (workflowMap == null) {
            return;
        }
        List<Map<String, Object>> workflowTreeMapList = (List<Map<String, Object>>) workflowMap.get("WorkflowTree");
        List<Map<String, Object>> workflowDefineMapList = (List<Map<String, Object>>) workflowMap.get("WorkflowDefine");
        List<Map<String, Object>> workflowVersionMapList = (List<Map<String, Object>>) workflowMap.get("WorkflowVersion");
        List<Map<String, Object>> workflowFormSettingMapList = (List<Map<String, Object>>) workflowMap.get("WorkflowFormSetting");
        List<Map<String, Object>> workflowButtonSettingMapList = (List<Map<String, Object>>) workflowMap.get("WorkflowButtonSetting");
        // key为oldID，value为newID
        Map<String, String> workflowTreeIdMap = new HashMap<String, String>();
        Map<String, String> workflowDefineIdMap = new HashMap<String, String>();
        selfDefineConfig.put("workflowDefineIdMap", workflowDefineIdMap);
        Map<String, String> workflowVersionIdMap = new HashMap<String, String>();
        selfDefineConfig.put("workflowVersionIdMap", workflowVersionIdMap);
        if (CollectionUtils.isNotEmpty(workflowTreeMapList)) {
            workflowTreeIdMap.put("-1", "-1");
            // key为parentId
            Map<String, List<WorkflowTree>> wfTreeMap = new HashMap<String, List<WorkflowTree>>();
            List<WorkflowTree> workflowTreeList = null;
            WorkflowTree workflowTree = null;
            for (Map<String, Object> map : workflowTreeMapList) {
                workflowTree = new WorkflowTree();
                workflowTree.setId(StringUtil.null2empty(map.get("id")));
                workflowTree.setName(StringUtil.null2empty(map.get("name")));
                workflowTree.setParentId(StringUtil.null2empty(map.get("parentId")));
                workflowTree.setShowOrder(Integer.valueOf(StringUtil.null2zero(map.get("showOrder"))));
                workflowTreeList = wfTreeMap.get(workflowTree.getParentId());
                if (workflowTreeList == null) {
                    workflowTreeList = new ArrayList<WorkflowTree>();
                    wfTreeMap.put(workflowTree.getParentId(), workflowTreeList);
                }
                workflowTreeList.add(workflowTree);
            }
            List<WorkflowTree> list = wfTreeMap.get("-1");
            if (CollectionUtils.isNotEmpty(list)) {
                for (WorkflowTree wfTree : list) {
                    saveWorkflowTree(wfTree, wfTreeMap, workflowTreeIdMap);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(workflowDefineMapList)) {
            WorkflowDefine workflowDefine = null;
            WorkflowDefine dbWorkflowDefine = null;
            for (Map<String, Object> workflowDefineMap : workflowDefineMapList) {
                String businessTableId = physicalTableDefineMap.get(StringUtil.null2empty(workflowDefineMap.get("businessTableId"))).getId();
                dbWorkflowDefine = getDaoFromContext(WorkflowDefineDao.class).findByWorkflowCode(StringUtil.null2empty(workflowDefineMap.get("workflowCode")));
                if (dbWorkflowDefine != null) {
                    workflowDefine = dbWorkflowDefine;
                } else {
                    workflowDefine = new WorkflowDefine();
                }
                workflowDefine.setWorkflowTreeId(workflowTreeIdMap.get(StringUtil.null2empty(workflowDefineMap.get("workflowTreeId"))));
                workflowDefine.setWorkflowName(StringUtil.null2empty(workflowDefineMap.get("workflowName")));
                workflowDefine.setWorkflowCode(StringUtil.null2empty(workflowDefineMap.get("workflowCode")));
                workflowDefine.setBusinessTableId(businessTableId);
                workflowDefine.setEnableDocumentTable(StringUtil.null2empty(workflowDefineMap.get("enableDocumentTable")));
                workflowDefine.setEnableConfirmTable(StringUtil.null2empty(workflowDefineMap.get("enableConfirmTable")));
                workflowDefine.setEnableAssistTable(StringUtil.null2empty(workflowDefineMap.get("enableAssistTable")));
                workflowDefine.setWorkflowBoxes(StringUtil.null2empty(workflowDefineMap.get("workflowBoxes")));
                workflowDefine.setShowOrder((Integer) workflowDefineMap.get("showOrder"));
                workflowDefine.setRemark(StringUtil.null2empty(workflowDefineMap.get("remark")));
                getService(WorkflowDefineService.class).save(workflowDefine);
                workflowDefineIdMap.put(StringUtil.null2empty(workflowDefineMap.get("id")), workflowDefine.getId());
            }
            if (CollectionUtils.isNotEmpty(workflowVersionMapList)) {
                WorkflowVersion workflowVersion = null;
                WorkflowVersion dbWorkflowVersion = null;
                for (Map<String, Object> workflowVersionMap : workflowVersionMapList) {
                    dbWorkflowVersion = getDaoFromContext(WorkflowVersionDao.class).getByWorkflowIdAndVersion(
                            workflowDefineIdMap.get(StringUtil.null2empty(workflowVersionMap.get("workflowId"))),
                            StringUtil.null2empty(workflowVersionMap.get("version")));
                    if (dbWorkflowVersion != null) {
                        workflowVersion = dbWorkflowVersion;
                    } else {
                        workflowVersion = new WorkflowVersion();
                    }
                    workflowVersion.setWorkflowId(workflowDefineIdMap.get(StringUtil.null2empty(workflowVersionMap.get("workflowId"))));
                    workflowVersion.setShowOrder((Integer) workflowVersionMap.get("showOrder"));
                    workflowVersion.setVersion(StringUtil.null2empty(workflowVersionMap.get("version")));
                    workflowVersion.setStatus(StringUtil.null2empty(workflowVersionMap.get("status")));
                    workflowVersion.setRemark(StringUtil.null2empty(workflowVersionMap.get("remark")));
                    getService(WorkflowVersionService.class).save(workflowVersion);
                    workflowVersionIdMap.put(StringUtil.null2empty(workflowVersionMap.get("id")), workflowVersion.getId());
                }
            }
            if (CollectionUtils.isNotEmpty(workflowFormSettingMapList)) {
                Map<String, Object> workflowFormSettingMap0 = workflowFormSettingMapList.get(0);
                getDaoFromContext(WorkflowFormSettingDao.class).deleteByFk(
                        workflowVersionIdMap.get(StringUtil.null2empty(workflowFormSettingMap0.get("workflowVersionId"))),
                        StringUtil.null2empty(workflowFormSettingMap0.get("activityId")));
                WorkflowFormSetting workflowFormSetting = null;
                for (Map<String, Object> workflowFormSettingMap : workflowFormSettingMapList) {
                    workflowFormSetting = new WorkflowFormSetting();
                    workflowFormSetting.setWorkflowVersionId(workflowVersionIdMap.get(StringUtil.null2empty(workflowFormSettingMap.get("workflowVersionId"))));
                    workflowFormSetting.setActivityId(StringUtil.null2empty(workflowFormSettingMap.get("activityId")));
                    workflowFormSetting.setColumnId(StringUtil.null2empty(workflowFormSettingMap.get("columnId")));
                    workflowFormSetting.setDisabled(StringUtil.null2zero(workflowFormSettingMap.get("disabled")));
                    workflowFormSetting.setDefaultValue(StringUtil.null2empty(workflowFormSettingMap.get("defaultValue")));
                    getService(WorkflowFormSettingService.class).save(workflowFormSetting);
                }
            }
            if (CollectionUtils.isNotEmpty(workflowButtonSettingMapList)) {
                Map<String, Object> workflowButtonSettingMap0 = workflowButtonSettingMapList.get(0);
                getDaoFromContext(WorkflowButtonSettingDao.class).deleteByFk(
                        workflowVersionIdMap.get(StringUtil.null2empty(workflowButtonSettingMap0.get("workflowVersionId"))),
                        StringUtil.null2empty(workflowButtonSettingMap0.get("activityId")), "0");
                getDaoFromContext(WorkflowButtonSettingDao.class).deleteByFk(
                        workflowVersionIdMap.get(StringUtil.null2empty(workflowButtonSettingMap0.get("workflowVersionId"))),
                        StringUtil.null2empty(workflowButtonSettingMap0.get("activityId")), "1");
                WorkflowButtonSetting workflowButtonSetting = null;
                for (Map<String, Object> workflowButtonSettingMap : workflowButtonSettingMapList) {
                    workflowButtonSetting = new WorkflowButtonSetting();
                    workflowButtonSetting
                            .setWorkflowVersionId(workflowVersionIdMap.get(StringUtil.null2empty(workflowButtonSettingMap.get("workflowVersionId"))));
                    workflowButtonSetting.setActivityId(StringUtil.null2empty(workflowButtonSettingMap.get("activityId")));
                    workflowButtonSetting.setButtonCode(StringUtil.null2empty(workflowButtonSettingMap.get("buttonCode")));
                    workflowButtonSetting.setButtonType(StringUtil.null2empty(workflowButtonSettingMap.get("buttonType")));
                    getService(WorkflowButtonSettingService.class).save(workflowButtonSetting);
                }
            }
            // 拷贝工作流文件
            String fileName = componentVersion.getPath();
            String componentPath = ComponentFileUtil.getTempCompUncompressPath() + fileName.substring(0, fileName.lastIndexOf("."));
            String componentDataPath = componentPath + "/data/";
            String componentXpdlPath = componentDataPath + "tempxpdl/";
            String configPath = ComponentFileUtil.getProjectPath() + "WEB-INF/conf/";
            String xpdlPath = configPath + "tempxpdl/";
            File componentDataFile = new File(componentDataPath);
            if (componentDataFile.exists()) {
                File[] listFiles = componentDataFile.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    for (File file : listFiles) {
                        if (file.getName().endsWith(".xpdl")) {
                            FileUtil.copyFile(componentDataPath + file.getName(), configPath + file.getName());
                        }
                    }
                }
            }
            File componentXpdlFile = new File(componentXpdlPath);
            if (componentXpdlFile.exists()) {
                File[] listFiles = componentXpdlFile.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    for (File file : listFiles) {
                        if (file.getName().endsWith(".xpdl")) {
                            FileUtil.copyFile(componentXpdlPath + file.getName(), xpdlPath + file.getName());
                        }
                    }
                }
            }
            // 更改Repository.xml文件
            SAXReader reader = new SAXReader();
            XMLWriter xmlWriter = null;
            Writer writer = null;
            try {
                File componentRepositoryFile = new File(componentDataPath + "Repository.xml");
                Document componentDoc = reader.read(componentRepositoryFile);
                Element componentRoot = componentDoc.getRootElement();
                Element componentDefineXmlFiles = componentRoot.element("DefineXmlFiles");
                Map<String, Element> wfPackIdAndVersionMap = new HashMap<String, Element>();
                if (componentDefineXmlFiles != null) {
                    Element defineXmlFile = null;
                    for (Iterator parameterIterator = componentDefineXmlFiles.elementIterator("DefineXmlFile"); parameterIterator.hasNext();) {
                        defineXmlFile = (Element) parameterIterator.next();
                        String packageId = defineXmlFile.attributeValue("packageId");
                        String packageVersion = defineXmlFile.attributeValue("packageVersion");
                        if (!wfPackIdAndVersionMap.keySet().contains(packageId + "$$" + packageVersion)) {
                            wfPackIdAndVersionMap.put(packageId + "$$" + packageVersion, defineXmlFile);
                        }
                    }
                }

                File projectRepositoryFile = new File(ComponentFileUtil.getProjectPath() + "WEB-INF/conf/Repository.xml");
                Document projectDoc = reader.read(projectRepositoryFile);
                Element projectRoot = projectDoc.getRootElement();
                Element projectDefineXmlFiles = projectRoot.element("DefineXmlFiles");
                if (projectDefineXmlFiles != null) {
                    Element defineXmlFile = null;
                    for (Iterator parameterIterator = projectDefineXmlFiles.elementIterator("DefineXmlFile"); parameterIterator.hasNext();) {
                        defineXmlFile = (Element) parameterIterator.next();
                        String packageId = defineXmlFile.attributeValue("packageId");
                        String packageVersion = defineXmlFile.attributeValue("packageVersion");
                        if (wfPackIdAndVersionMap.keySet().contains(packageId + "$$" + packageVersion)) {
                            projectDefineXmlFiles.remove(defineXmlFile);
                        }
                    }
                    for (Iterator<String> it = wfPackIdAndVersionMap.keySet().iterator(); it.hasNext();) {
                        String key = it.next();
                        defineXmlFile = wfPackIdAndVersionMap.get(key);
                        defineXmlFile.setParent(null);
                        projectDefineXmlFiles.add(defineXmlFile);
                    }
                }
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("UTF-8");
                format.setNewLineAfterDeclaration(false);
                format.setIndent("\t");
                writer = new OutputStreamWriter(new FileOutputStream(ComponentFileUtil.getProjectPath() + "WEB-INF/conf/Repository.xml"), "UTF-8");
                xmlWriter = new XMLWriter(writer, format);
                xmlWriter.write(projectDoc);
                writer.flush();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
        }
    }

    /**
     * 保存工作流树
     */
    @Transactional
    private void saveWorkflowTree(WorkflowTree workflowTree, Map<String, List<WorkflowTree>> wfTreeMap, Map<String, String> workflowTreeIdMap) {
        String oldId = workflowTree.getId();
        String parentId = workflowTreeIdMap.get(workflowTree.getParentId());
        WorkflowTree dbWorkflowTree = getService(WorkflowTreeService.class).getByParentIdAndName(parentId, workflowTree.getName());
        if (dbWorkflowTree != null) {
            workflowTreeIdMap.put(workflowTree.getId(), dbWorkflowTree.getId());
        } else {
            workflowTree.setParentId(parentId);
            workflowTree.setId(null);
            getService(WorkflowTreeService.class).save(workflowTree);
            workflowTreeIdMap.put(oldId, workflowTree.getId());
        }
        List<WorkflowTree> list = wfTreeMap.get(oldId);
        if (CollectionUtils.isNotEmpty(list)) {
            for (WorkflowTree wfTree : list) {
                saveWorkflowTree(wfTree, wfTreeMap, workflowTreeIdMap);
            }
        }
    }

    /**
     * 保存构件版本（该方法供"构件定义"模块使用）
     * 
     * @param componentVersion 构件版本
     */
    @Transactional
    public ComponentVersion saveComponentVersion(Module module) {
        // 生成代码标识
        boolean flag = false;
        ComponentVersion componentVersion = null;
        Component component = null;
        if (StringUtil.isNotEmpty(module.getComponentVersionId())) {
            componentVersion = getByID(module.getComponentVersionId());
        }
        if (componentVersion != null) {
            // 修改构件且修改了构件名称时，重新生成代码
            if (!ConstantVar.Component.Type.TAB.equals(module.getType()) && !componentVersion.getComponent().getName().equals(module.getComponentClassName())) {
                FileUtil.deleteFile(ComponentFileUtil.getProjectPath() + "cfg-resource/" + SystemParameterUtil.getInstance().getReleaseSystemUI()
                        + "/views/selfdefine/" + componentVersion.getComponent().getName().toLowerCase());
                FileUtil.deleteFile(ComponentFileUtil.getSrcPath() + "selfdefine/com/ces/component/" + componentVersion.getComponent().getName().toLowerCase());
                flag = true;
            }
            component = componentVersion.getComponent();
        } else {
            // 新增构件时，生成代码
            if (!ConstantVar.Component.Type.TAB.equals(module.getType())) {
                flag = true;
            }
            componentVersion = new ComponentVersion();
            component = new Component();
            component.setCode(module.getId());
            componentVersion.setComponent(component);
        }
        component.setAlias(module.getName());
        component.setName(module.getComponentClassName());
        componentVersion.setAreaId(module.getComponentAreaId());
        if (!ConstantVar.Component.Type.TAB.equals(module.getType())) {
            String url = AppDefineUtil.getUrl(module.getTemplateType(), module.getId(), module.getUiType());
            url = "selfdefine/" + component.getName().toLowerCase() + url.replaceFirst("config/appmanage/show", "");
            componentVersion.setUrl(url);
        } else {
            componentVersion.setUrl("config/tab/tab.jsp");
        }
        componentVersion.setRemark(module.getRemark());
        component.setType(module.getType());
        component = getService(ComponentService.class).save(component);
        componentVersion.setComponent(component);
        componentVersion.setVersion("V1.0");
        componentVersion.setViews(SystemParameterUtil.getInstance().getReleaseSystemUI());
        componentVersion.setIsPackage(ConstantVar.Component.Packaged.NO);
        if (CfgCommonUtil.isReleasedSystem()) {
            componentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.YES);
        } else {
            componentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.NO);
        }
        componentVersion.setImportDate(new Date());
        componentVersion.setSystemParamConfig(ConstantVar.Component.SystemParamConfig.NOT_NEED);
        if (!flag) {
            // 修改构件时如果构件文件不存在，生成代码
            String packageName = component.getName().toLowerCase();
            File file = new File(ComponentFileUtil.getProjectPath() + "cfg-resource/" + SystemParameterUtil.getInstance().getReleaseSystemUI()
                    + "/views/selfdefine/" + packageName);
            if (!file.exists()) {
                flag = true;
            }
        }
        if (flag) {
            String name = component.getName();
            String className = name.substring(0, 1).toUpperCase() + name.substring(1);
            String packageName = name.toLowerCase();
            String projectPath = ComponentFileUtil.getProjectPath();
            
            // 创建构件java文件包
            String pageDirPath = projectPath + "cfg-resource/" + SystemParameterUtil.getInstance().getReleaseSystemUI() + "/views/selfdefine/" + packageName;
            String javaDirPath = ComponentFileUtil.getSrcPath() + "selfdefine/com/ces/component/" + packageName;
            FileUtil.mkDir(javaDirPath + "/action/");
            FileUtil.mkDir(javaDirPath + "/service/");
            FileUtil.mkDir(javaDirPath + "/dao/");
            FileUtil.mkDir(javaDirPath + "/entity/");
            FileUtil.mkDir(pageDirPath);
            FileUtil.mkDir(pageDirPath + "/js/");
            Context context = new VelocityContext();
            context.put("packageName", packageName);
            context.put("className", className);
            context.put("currentDate", DateUtil.currentTime());
            context.put("actionName", classNameToAction(className));
            Writer controllerWriter = null;
            Writer serviceWriter = null;
            Writer daoWriter = null;
            Writer pComponentWriter = null;
            Writer pOverrideWriter = null;
            try {
                VelocityEngine ve = new VelocityEngine();
                Properties properties = new Properties();
                properties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, ComponentFileUtil.getProjectPath() + "cfg-resource/vm/");
                properties.setProperty(VelocityEngine.ENCODING_DEFAULT, "UTF-8");
                properties.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
                properties.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
                ve.init(properties);
                Template controllerTemplate = ve.getTemplate("controller.vm");
                controllerWriter = new OutputStreamWriter(new FileOutputStream(javaDirPath + "/action/" + className + "Controller.java"), "UTF-8");
                controllerTemplate.merge(context, controllerWriter);
                Template serviceTemplate = ve.getTemplate("service.vm");
                serviceWriter = new OutputStreamWriter(new FileOutputStream(javaDirPath + "/service/" + className + "Service.java"), "UTF-8");
                serviceTemplate.merge(context, serviceWriter);
                Template daoTemplate = ve.getTemplate("dao.vm");
                daoWriter = new OutputStreamWriter(new FileOutputStream(javaDirPath + "/dao/" + className + "Dao.java"), "UTF-8");
                daoTemplate.merge(context, daoWriter);
                // 生成页面文件
                Template pComponentTemplate = ve.getTemplate("page-component.vm");
                pComponentWriter = new OutputStreamWriter(new FileOutputStream(pageDirPath + "/MT_component.jsp"), "UTF-8");
                pComponentTemplate.merge(context, pComponentWriter);
                Template pOverrideTemplate = ve.getTemplate("page-override.vm");
                pOverrideWriter = new OutputStreamWriter(new FileOutputStream(pageDirPath + "/js/JQ_" + packageName + ".jsp"), "UTF-8");
                pOverrideTemplate.merge(context, pOverrideWriter);
            } catch (ResourceNotFoundException e) {
                e.printStackTrace();
            } catch (ParseErrorException e) {
                e.printStackTrace();
            } catch (MethodInvocationException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (controllerWriter != null) {
                    try {
                        controllerWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (serviceWriter != null) {
                    try {
                        serviceWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (daoWriter != null) {
                    try {
                        daoWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (pComponentWriter != null) {
                    try {
                    	pComponentWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (pOverrideWriter != null) {
                    try {
                    	pOverrideWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        componentVersion = getDao().save(componentVersion);
        ComponentInfoUtil.getInstance().putComponentVersion(componentVersion);
        return componentVersion;
    }
    
    /**
     * qiucs 2015-10-12 上午11:57:11
     * <p>描述: 把类名转成前台访问的action </p>
     * @return String
     */
    private String classNameToAction(String className) {
    	StringBuffer sb = new StringBuffer(100);
    	int len = className.length();
    	char c;
    	for (int i = 0; i < len; i++) {
    		c = className.charAt(i);
    		if (Character.isUpperCase(c)) {
    			if (i > 0) sb.append("-");
    			c = Character.toLowerCase(c);
    		}
    		sb.append(c);
    	}
    	return sb.toString();
    }

    /**
     * 保存构件版本（该方法供"构件组装"模块使用）
     * 
     * @param componentVersion 构件版本
     * @param views 构件前台
     */
    @Transactional
    public ComponentVersion saveAssembleComponentVersion(ComponentVersion componentVersion, String views) {
        Component component = componentVersion.getComponent();
        ComponentVersion dbCV = null;
        if (StringUtil.isNotEmpty(componentVersion.getId())) {
            dbCV = getDao().findOne(componentVersion.getId());
        }
        Component componentOfDb = null;
        if (dbCV != null) {
            componentOfDb = dbCV.getComponent();
        } else {
            componentOfDb = getService(ComponentService.class).getComponentByName(component.getName());
        }
        if (componentOfDb != null) {
            component.setId(componentOfDb.getId());
        }
        component.setType(ConstantVar.Component.Type.ASSEMBLY);
        component = getService(ComponentService.class).save(component);
        componentVersion.setComponent(component);
        componentVersion.setViews(views);
        componentVersion.setIsPackage(ConstantVar.Component.Packaged.NO);
        if (CfgCommonUtil.isReleasedSystem()) {
            componentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.YES);
        } else {
            componentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.NO);
        }
        componentVersion.setImportDate(new Date());
        componentVersion.setSystemParamConfig(ConstantVar.Component.SystemParamConfig.NOT_NEED);
        if (componentVersion.getButtonUse() == null) {
            componentVersion.setButtonUse("1");
        }
        if (componentVersion.getMenuUse() == null) {
            componentVersion.setMenuUse("1");
        }
        componentVersion = getDao().save(componentVersion);
        ComponentInfoUtil.getInstance().putComponentVersion(componentVersion);
        return componentVersion;
    }

    /**
     * 删除构件版本的文件
     * 
     * @param componentVersionId 构件版本ID
     */
    public void deleteComponentVersionFiles(String componentVersionId, boolean cascadeDeleteModule) {
        ComponentVersion componentVersion = getByID(componentVersionId);
        // 自定义构件
        if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1
                || ConstantVar.Component.Type.TAB.equals(componentVersion.getComponent().getType())) {
            if (cascadeDeleteModule) {
                String url = componentVersion.getUrl();
                String moduleId = url.substring(url.indexOf("P_moduleId=") + "P_moduleId=".length());
                getService(ModuleService.class).deleteModule(moduleId);
            }
            FileUtil.deleteFile(ComponentFileUtil.getProjectPath() + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/"
                    + componentVersion.getComponent().getName().toLowerCase());
            FileUtil.deleteFile(ComponentFileUtil.getSrcPath() + "selfdefine/com/ces/component/" + componentVersion.getComponent().getName().toLowerCase());
        }
        String path = componentVersion.getPath();
        if (path != null && !"".equals(path)) {
            String packageName = path.substring(0, componentVersion.getPath().lastIndexOf("."));
            // 删除构件文件
            FileUtil.deleteFile(ComponentFileUtil.getCompPath() + path);
            FileUtil.deleteFile(ComponentFileUtil.getCompUncompressPath() + packageName);
        }
    }

    /**
     * 删除构件版本
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteComponentVersion(String componentVersionId, boolean cascadeDeleteModule) {
        ComponentVersion componentVersion = getByID(componentVersionId);
        if (componentVersion != null) {
            deleteComponentVersionFiles(componentVersionId, cascadeDeleteModule);

            // 删除构件系统参数
            getService(ComponentSystemParameterService.class).deleteByComponentVersionId(componentVersionId);
            getService(ComponentSystemParameterRelationService.class).deleteByComponentVersionId(componentVersionId);
            // 删除构件自身参数配置
            getService(ComponentSelfParamService.class).deleteByComponentVersionId(componentVersionId);
            // 删除构件入参
            getService(ComponentInputParamService.class).deleteByComponentVersionId(componentVersionId);
            // 删除输出参数
            getService(ComponentOutputParamService.class).deleteByComponentVersionId(componentVersionId);
            // 删除构件预留区
            getService(ComponentReserveZoneService.class).deleteByComponentVersionId(componentVersionId);
            // 删除构件页面方法及页面方法返回值
            getService(ComponentFunctionService.class).deleteByComponentVersionId(componentVersionId);
            // 删除构件页面回调函数
            getService(ComponentCallbackService.class).deleteByComponentVersionId(componentVersionId);
            // 删除构件的类
            getDaoFromContext(ComponentClassDao.class).deleteByComponentVersionId(componentVersionId);
            // 删除构件的Jar
            getDaoFromContext(ComponentJarDao.class).deleteByComponentVersionId(componentVersionId);
            // 删除构件相关表
            getDaoFromContext(ComponentTableColumnRelationDao.class).deleteByComponentVersionId(componentVersionId);
            getDaoFromContext(ComponentTableColumnRelationDao.class).deleteComponentTable();
            getDaoFromContext(ComponentTableColumnRelationDao.class).deleteComponentColumn();
            // 删除系统直接关联的构件
            getService(SystemComponentVersionService.class).deleteByComponentVersionId(componentVersionId);
            // 删除构件直接的关联关系
            if (ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                getService(CommonComponentRelationService.class).deleteByCommonComponentVersionId(componentVersionId);
            } else {
                getService(CommonComponentRelationService.class).deleteByComponentVersionId(componentVersionId);
            }
            // 删除构件权限按钮
            getService(ComponentButtonService.class).deleteByComponentVersionId(componentVersionId);
            // 删除数据权限
            getService(AuthorityDataService.class).deleteByComponentVersionId(componentVersionId);
            // 删除构件版本
            getDao().deleteById(componentVersionId);

            List<ComponentVersion> componentVersionList = getDao().getByComponentId(componentVersion.getComponent().getId());
            if (CollectionUtils.isEmpty(componentVersionList)) {
                getDaoFromContext(ComponentDao.class).deleteById(componentVersion.getComponent().getId());
            }
            ComponentInfoUtil.getInstance().removeComponentVersion(componentVersion.getId());
        }
    }

    /**
     * 删除自定义构件版本中的预留区、页面方法和返回值、页面回调函数
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteSelfDefineComponentVersionRelation(String componentVersionId) {
        // 删除构件自身参数配置
        // getService(ComponentSelfParamService.class).deleteByComponentVersionId(componentVersionId);
        // 删除构件入参
        // getService(ComponentInputParamService.class).deleteByComponentVersionId(componentVersionId);
        // 删除输出参数
        // getService(ComponentOutputParamService.class).deleteByComponentVersionId(componentVersionId);
        // 删除构件预留区
        getService(ComponentReserveZoneService.class).deleteByComponentVersionId(componentVersionId);
        // 删除构件页面方法及页面方法返回值
        // getService(ComponentFunctionService.class).deleteByComponentVersionId(componentVersionId);
        // 删除构件页面回调函数
        // getService(ComponentCallbackService.class).deleteByComponentVersionId(componentVersionId);
    }

    /**
     * 获取构件的相关表信息
     * 
     * @param componentVersionId 构件版本ID
     * @return List<String>
     */
    public List<Object[]> getTablesByComponentVersionId(String componentVersionId) {
        return getDao().getTablesByComponentVersionId(componentVersionId);
    }

    /**
     * 更改构件分类，如果是自定义构件，同时更改对应的模块的构件分类
     * 
     * @param areaId 构件分类ID
     * @param versionIds 构件版本IDs
     */
    @Transactional
    public void changeArea(String areaId, String[] componentVersionIds) {
        String areaPath = ComponentInfoUtil.getInstance().getComponentAreaAllPath(areaId);
        StringBuilder cvIds = new StringBuilder();
        StringBuilder selfDefineCVIds = new StringBuilder();
        for (String componentVersionId : componentVersionIds) {
            ComponentVersion componentVersion = getByID(componentVersionId);
            if (componentVersion != null) {
                cvIds.append(",'").append(componentVersion.getId()).append("'");
                if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1
                        || ConstantVar.Component.Type.TAB.equals(componentVersion.getComponent().getType())) {
                    selfDefineCVIds.append(",'").append(componentVersion.getId()).append("'");
                }
                componentVersion.setAreaId(areaId);
                componentVersion.setAreaPath(areaPath);
                ComponentInfoUtil.getInstance().putComponentVersion(componentVersion);
            }
        }
        if (selfDefineCVIds.length() > 0) {
            selfDefineCVIds.deleteCharAt(0);
            String sql = "update t_xtpz_module set component_area_id='" + areaId + "' where component_version_id in (" + selfDefineCVIds.toString() + ")";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
        if (cvIds.length() > 0) {
            cvIds.deleteCharAt(0);
            String sql = "update t_xtpz_component_version set area_id='" + areaId + "',area_path='" + areaPath + "' where id in (" + cvIds.toString() + ")";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
    }

    /**
     * 更改构件分类，如果是自定义构件，同时更改对应的模块的构件分类
     * 
     * @param areaId 构件分类ID
     * @param versionIds 构件版本IDs
     */
    @Transactional
    public void changeAssembleArea(String assembleAreaId, String[] componentVersionIds) {
        String assembleAreaPath = ComponentInfoUtil.getInstance().getComponentAssembleAreaAllPath(assembleAreaId);
        StringBuilder cvIds = new StringBuilder();
        for (String componentVersionId : componentVersionIds) {
            ComponentVersion componentVersion = getByID(componentVersionId);
            if (componentVersion != null) {
                cvIds.append(",'").append(componentVersion.getId()).append("'");
                componentVersion.setAssembleAreaId(assembleAreaId);
                componentVersion.setAssembleAreaId(assembleAreaPath);
                ComponentInfoUtil.getInstance().putComponentVersion(componentVersion);
            }
        }
        if (cvIds.length() > 0) {
            cvIds.deleteCharAt(0);
            String sql = "update t_xtpz_component_version set assemble_area_id='" + assembleAreaId + "',assemble_area_path='" + assembleAreaPath
                    + "' where id in (" + cvIds.toString() + ")";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
    }

    /**
     * 获取构件关联的公用构件列表
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getCommonComponentList(String componentVersionId) {
        return getDao().getCommonComponentList(componentVersionId);
    }

    /**
     * 获取组合构件的基础构件下拉框信息
     * 
     * @param isSystemUsed 是否应用到本系统
     * @return List<Object[]>
     */
    public List<Object[]> getBaseComponentVersionCombo(String isSystemUsed) {
        List<Object[]> comboList = new ArrayList<Object[]>();
        List<Object[]> list = getDao().getBaseComponentVersionCombo(isSystemUsed);
        if (CollectionUtils.isNotEmpty(list)) {
            Object[] combo = null;
            for (Object[] objs : list) {
                combo = new Object[2];
                combo[0] = objs[0];
                combo[1] = objs[1] + "_" + objs[2];
                comboList.add(combo);
            }
        }
        return comboList;
    }

    /**
     * 获取组合构件中绑定按钮的构件下拉框信息
     * 
     * @param isSystemUsed 是否应用到本系统
     * @return List<Object[]>
     */
    public List<Object[]> getBindingComponentVersionCombo(String reserveZoneType, String treeNodeType, String isSystemUsed) {
        List<Object[]> comboList = new ArrayList<Object[]>();
        List<Object[]> list = null;
        Object[] combo = null;
        if (ConstantVar.Component.ReserveZoneType.TOOLBAR.equals(reserveZoneType) || ConstantVar.Component.ReserveZoneType.GRID_LINK.equals(reserveZoneType)
                || ConstantVar.Component.ReserveZoneType.BUTTON.equals(reserveZoneType)
                || ConstantVar.Component.ReserveZoneType.TRANSFER_DEVICE.equals(reserveZoneType)) {
            // 页面构件
            List<ComponentVersion> pageComponentVersionList = getDao().getByComponentType(ConstantVar.Component.Type.PAGE);
            if (CollectionUtils.isNotEmpty(pageComponentVersionList)) {
                for (ComponentVersion pageComponentVersion : pageComponentVersionList) {
                    if (!"1".equals(pageComponentVersion.getButtonUse())) {
                        continue;
                    }
                    combo = new Object[2];
                    combo[0] = pageComponentVersion.getId();
                    combo[1] = pageComponentVersion.getComponent().getAlias() + "_" + pageComponentVersion.getVersion();
                    comboList.add(combo);
                }
            }
            // 逻辑构件
            List<ComponentVersion> logicComponentVersionList = getDao().getByComponentType(ConstantVar.Component.Type.LOGIC);
            if (CollectionUtils.isNotEmpty(logicComponentVersionList)) {
                for (ComponentVersion logicComponentVersion : logicComponentVersionList) {
                    if (!"1".equals(logicComponentVersion.getButtonUse())) {
                        continue;
                    }
                    combo = new Object[2];
                    combo[0] = logicComponentVersion.getId();
                    combo[1] = logicComponentVersion.getComponent().getAlias() + "_" + logicComponentVersion.getVersion();
                    comboList.add(combo);
                }
            }
            // 组合构件
            list = getDao().getAssembleComponentVersions(isSystemUsed);
            if (CollectionUtils.isNotEmpty(list)) {
                for (Object[] objs : list) {
                    if (!"1".equals(objs[3])) {
                        continue;
                    }
                    combo = new Object[2];
                    combo[0] = objs[0];
                    combo[1] = objs[1] + "_" + objs[2];
                    comboList.add(combo);
                }
            }
        } else if (ConstantVar.Component.ReserveZoneType.TREE_NODE.equals(reserveZoneType)
                || ConstantVar.Component.ReserveZoneType.TAB_PAGE.equals(reserveZoneType)) {
            // 页面构件
            List<ComponentVersion> pageComponentVersionList = getDao().getByComponentType(ConstantVar.Component.Type.PAGE);
            if (CollectionUtils.isNotEmpty(pageComponentVersionList)) {
                for (ComponentVersion pageComponentVersion : pageComponentVersionList) {
                    if (!"1".equals(pageComponentVersion.getButtonUse())) {
                        continue;
                    }
                    combo = new Object[2];
                    combo[0] = pageComponentVersion.getId();
                    combo[1] = pageComponentVersion.getComponent().getAlias() + "_" + pageComponentVersion.getVersion();
                    comboList.add(combo);
                }
            }
            // 组合构件
            list = getDao().getAssembleComponentVersions(isSystemUsed);
            if (CollectionUtils.isNotEmpty(list)) {
                for (Object[] objs : list) {
                    if (!"1".equals(objs[3])) {
                        continue;
                    }
                    combo = new Object[2];
                    combo[0] = objs[0];
                    combo[1] = objs[1] + "_" + objs[2];
                    comboList.add(combo);
                }
            }
        } else if (ConstantVar.Component.ReserveZoneType.TREE.equals(reserveZoneType)) {
            if (TreeDefine.T_ROOT.equals(treeNodeType) || TreeDefine.T_EMPTY.equals(treeNodeType) || TreeDefine.T_COLUMN_EMP.equals(treeNodeType)
                    || "TREE".equals(treeNodeType) || TreeDefine.T_TABLE.equals(treeNodeType)) {
                // 页面构件
                List<ComponentVersion> pageComponentVersionList = getDao().getByComponentType(ConstantVar.Component.Type.PAGE);
                if (CollectionUtils.isNotEmpty(pageComponentVersionList)) {
                    for (ComponentVersion pageComponentVersion : pageComponentVersionList) {
                        if (!"1".equals(pageComponentVersion.getButtonUse())) {
                            continue;
                        }
                        combo = new Object[2];
                        combo[0] = pageComponentVersion.getId();
                        combo[1] = pageComponentVersion.getComponent().getAlias() + "_" + pageComponentVersion.getVersion();
                        comboList.add(combo);
                    }
                }
                // 组合构件
                list = getDao().getAssembleComponentVersions(isSystemUsed);
                if (CollectionUtils.isNotEmpty(list)) {
                    for (Object[] objs : list) {
                        if (!"1".equals(objs[3])) {
                            continue;
                        }
                        combo = new Object[2];
                        combo[0] = objs[0];
                        combo[1] = objs[1] + "_" + objs[2];
                        comboList.add(combo);
                    }
                }
            }
        }
        return comboList;
    }

    /**
     * 根据逻辑表组Code获取相关的构件
     * 
     * @param logicTableGroupCode 逻辑表组编码
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getLogicComponentList(String logicTableGroupCode) {
        return getDao().getLogicComponentList(logicTableGroupCode);
    }

    /**
     * qiucs 2015-2-3 下午5:42:08
     * <p>描述: 判断构件是否与tableId相关 </p>
     * 
     * @return boolean
     */
    public boolean containTable(String id, Set<String> cvIdsOfTable, boolean isCycle) {
        ComponentVersion cv = getByID(id);
        return containTable(cv, cvIdsOfTable, isCycle);
    }

    /**
     * qiucs 2015-2-3 下午5:41:38
     * <p>描述: 判断构件是否与tableId相关 </p>
     * 
     * @return boolean
     */
    public boolean containTable(ComponentVersion cv, Set<String> cvIdsOfTable, boolean isCycle) {
        if (null == cv)
            return false;
        if (ConstantVar.Component.Type.ASSEMBLY.equals(cv.getComponent().getType())) {
            Construct construct = ComponentInfoUtil.getInstance().getConstructByAssembleId(cv.getId());
            if (construct == null)
                return false;
            String bcvId = construct.getBaseComponentVersionId();
            boolean contained = containTable(bcvId, cvIdsOfTable, isCycle);
            if (contained)
                return true;
            if (!isCycle)
                return false;
            return cycleAssemble(cv.getId(), cvIdsOfTable, isCycle);
        } else {
            if (cvIdsOfTable.contains(cv.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * qiucs 2015-2-3 下午5:39:50
     * <p>描述: 循环遍历组合构件下的所有构件，并判断构件是否与tableId相关 </p>
     * 
     * @return boolean
     */
    private boolean cycleAssemble(String componentVersionId, Set<String> cvIdsOfTable, boolean isCycle) {
        boolean contained = false;
        List<ComponentVersion> list = getService(ConstructService.class).getAssemblesOfAssemble(componentVersionId);
        for (ComponentVersion version : list) {
            contained = containTable(version, cvIdsOfTable, isCycle);
            if (contained)
                return true;
        }
        return contained;
    }

    /**
     * qiucs 2015-2-3 下午5:42:08
     * <p>描述: 判断构件是否与tableId相关 </p>
     * 
     * @return boolean
     */
    public boolean containTable(String id, String tableId, boolean isCycle) {
        ComponentVersion cv = getByID(id);
        return containTable(cv, tableId, isCycle);
    }

    /**
     * qiucs 2015-2-3 下午5:41:38
     * <p>描述: 判断构件是否与tableId相关 </p>
     * 
     * @return boolean
     */
    public boolean containTable(ComponentVersion cv, String tableId, boolean isCycle) {
        if (null == cv)
            return false;

        String type = cv.getComponent().getType();

        if (ConstantVar.Component.Type.NO_TABLE.equals(type))
            return false;

        if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(type)) {
            return getService(ModuleService.class).containTable(cv.getComponent().getCode(), tableId, type);
        }

        if (ConstantVar.Component.Type.LOGIC_TABLE.equals(type)) {
            return getService(ModuleService.class).containTable(cv.getComponent().getCode(), TableUtil.getLogicTableCode(tableId), type);
        }

        if (ConstantVar.Component.Type.TREE.equals(type)) {
            if (!isCycle)
                return false;
            boolean contained = getService(ModuleService.class).containTable(cv.getComponent().getCode(), tableId, type);
            if (!contained)
                return false;
            return cycleAssemble(cv.getId(), tableId, isCycle);

        }

        if (ConstantVar.Component.Type.ASSEMBLY.equals(type)) {
            String bcvId = getService(ConstructService.class).getBaseComponentVersionId(cv.getId());
            boolean contained = containTable(bcvId, tableId, isCycle);
            if (contained)
                return true;
            if (!isCycle)
                return false;
            return cycleAssemble(cv.getId(), tableId, isCycle);
        }
        return false;
    }

    /**
     * qiucs 2015-2-3 下午5:39:50
     * <p>描述: 循环遍历组合构件下的所有构件，并判断构件是否与tableId相关 </p>
     * 
     * @return boolean
     */
    private boolean cycleAssemble(String componentVersionId, String tableId, boolean isCycle) {
        boolean contained = false;
        List<ComponentVersion> list = getService(ConstructService.class).getAssemblesOfAssemble(componentVersionId);
        for (ComponentVersion version : list) {
            contained = containTable(version, tableId, isCycle);
            if (contained)
                return true;
        }
        return contained;
    }

    /**
     * 获取构件对应构件版本的数量（大于1的）
     * 
     * @return List<Object[]>
     */
    public List<Object[]> getCompCvCount() {
        return getDao().getCompCvCount();
    }

    /**
     * 获取所有的构件版本
     * 
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getAll() {
        List<ComponentVersion> cvList = new ArrayList<ComponentVersion>();
        List<Object[]> list = getDao().getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            Component c = null;
            ComponentVersion cv = null;
            for (Object[] objs : list) {
                c = new Component();
                c.setId(String.valueOf(objs[0]));
                c.setCode(String.valueOf(objs[1]));
                c.setName(String.valueOf(objs[2]));
                c.setAlias(String.valueOf(objs[3]));
                c.setType(String.valueOf(objs[4]));
                cv = new ComponentVersion();
                cv.setComponent(c);
                cv.setId(String.valueOf(objs[5]));
                cv.setVersion(String.valueOf(objs[7]));
                cv.setUrl(objs[8] == null ? null : String.valueOf(objs[8]));
                cv.setRemark(objs[9] == null ? null : String.valueOf(objs[9]));
                cv.setAreaId(String.valueOf(objs[10]));
                cv.setPath(objs[11] == null ? null : String.valueOf(objs[11]));
                cv.setImportDate((Date) objs[12]);
                cv.setViews(objs[13] == null ? null : String.valueOf(objs[13]));
                cv.setSystemParamConfig(StringUtil.null2empty(objs[14]));
                cv.setIsPackage(StringUtil.null2empty(objs[15]));
                cv.setIsSystemUsed(StringUtil.null2empty(objs[16]));
                cv.setPackageTime(objs[17] == null ? null : String.valueOf(objs[17]));
                cv.setBeforeClickJs(objs[18] == null ? null : String.valueOf(objs[18]));
                cv.setAssembleAreaId(objs[19] == null ? null : String.valueOf(objs[19]));
                cv.setButtonUse(objs[20] == null ? null : String.valueOf(objs[20]));
                cv.setMenuUse(objs[21] == null ? null : String.valueOf(objs[21]));
                cv.setAreaPath(objs[22] == null ? null : String.valueOf(objs[22]));
                cv.setAssembleAreaPath(objs[23] == null ? null : String.valueOf(objs[23]));
                cvList.add(cv);
            }
        }
        return cvList;
    }
}

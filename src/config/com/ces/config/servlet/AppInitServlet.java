package com.ces.config.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ces.workflow.wapi.Coflow;

import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.code.CodeType;
import com.ces.config.dhtmlx.entity.component.ComponentArea;
import com.ces.config.dhtmlx.entity.component.ComponentAssembleArea;
import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameter;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameterRelation;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.construct.ConstructDetailSelfParam;
import com.ces.config.dhtmlx.entity.construct.ConstructInputParam;
import com.ces.config.dhtmlx.entity.construct.ConstructSelfParam;
import com.ces.config.dhtmlx.entity.menu.MenuInputParam;
import com.ces.config.dhtmlx.entity.menu.MenuSelfParam;
import com.ces.config.dhtmlx.entity.parameter.SystemParameter;
import com.ces.config.dhtmlx.entity.resource.Resource;
import com.ces.config.dhtmlx.entity.resource.ResourceButton;
import com.ces.config.dhtmlx.service.code.BusinessCodeService;
import com.ces.config.dhtmlx.service.code.CodeService;
import com.ces.config.dhtmlx.service.code.CodeTypeService;
import com.ces.config.dhtmlx.service.component.ComponentAreaService;
import com.ces.config.dhtmlx.service.component.ComponentAssembleAreaService;
import com.ces.config.dhtmlx.service.component.ComponentInputParamService;
import com.ces.config.dhtmlx.service.component.ComponentSelfParamService;
import com.ces.config.dhtmlx.service.component.ComponentSystemParameterRelationService;
import com.ces.config.dhtmlx.service.component.ComponentSystemParameterService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructCallbackService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailSelfParamService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructFunctionService;
import com.ces.config.dhtmlx.service.construct.ConstructInputParamService;
import com.ces.config.dhtmlx.service.construct.ConstructSelfParamService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuInputParamService;
import com.ces.config.dhtmlx.service.menu.MenuSelfParamService;
import com.ces.config.dhtmlx.service.parameter.SystemParameterService;
import com.ces.config.dhtmlx.service.resource.ResourceButtonService;
import com.ces.config.dhtmlx.service.resource.ResourceService;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.AuthDatabaseUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.ComponentInfoUtil;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.DatabaseUpdate;
import com.ces.config.utils.FileUtil;
import com.ces.config.utils.IndexCommonUtil;
import com.ces.config.utils.MenuUtil;
import com.ces.config.utils.ResourceUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.SystemParameterUtil;
import com.ces.config.utils.authority.AuthAuthorityManagerImpl;
import com.ces.config.utils.authority.AuthorityUtil;

public class AppInitServlet extends HttpServlet {

    private static final long serialVersionUID = 4913350466366754546L;

    @Override
    public void init() throws ServletException {
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        // 设置工程路径
        ComponentFileUtil.setProjectPath(this.getServletContext().getRealPath("/").replaceAll("\\\\", "/"));
        ComponentFileUtil.init();
        // 删除构件包临时文件
        FileUtil.deleteDir(ComponentFileUtil.getTempCompPath());
        FileUtil.deleteDir(ComponentFileUtil.getTempCompUncompressPath());

        // 加载数据库配置文件
        String profile = getServletContext().getInitParameter("spring.profiles.default");
        AuthDatabaseUtil.loadProperties(profile);
        DatabaseUpdate.loadProperties(profile);

        // 更新数据库
        DatabaseUpdate.updateDataBase();

        // 设置权限处理类
        AuthorityUtil.getInstance().setAuthorityManager(new AuthAuthorityManagerImpl());

        // 加载编码
        try {
            CodeTypeService codeTypeService = (CodeTypeService) applicationContext.getBean("codeTypeService");
            CodeService codeService = (CodeService) applicationContext.getBean("codeService");
            BusinessCodeService businessCodeService = (BusinessCodeService) applicationContext.getBean("businessCodeService");
            List<CodeType> codeTypeList = codeTypeService.findAll();
            List<Code> codeList = codeService.getUseCacheCodeList();
            if (CollectionUtils.isNotEmpty(codeTypeList)) {
                if (codeTypeList.get(0).getShowOrder() == null) {
                    for (int i = 0; i < codeTypeList.size(); i++) {
                        codeTypeList.get(i).setShowOrder(i + 1);
                    }
                    codeTypeService.save(codeTypeList);
                }
                for (CodeType codeType : codeTypeList) {
                    CodeUtil.getInstance().putCodeType(codeType.getCode(), codeType.getName());
                    if ("1".equals(codeType.getIsBusiness()) && "1".equals(codeType.getIsCache())) {
                        try {
                            businessCodeService.syncBusinessCode(codeType.getCode());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(codeList)) {
                for (Code code : codeList) {
                    CodeUtil.getInstance().putCode(code.getCodeTypeCode(), code);
                }
            }
        } catch (BeansException e) {
            System.out.println("将编码加载到缓存失败！");
            e.printStackTrace();
        }

        // 加载系统参数
        SystemParameterService systemParameterService = (SystemParameterService) applicationContext.getBean("systemParameterService");
        List<SystemParameter> systemParameterList = systemParameterService.findAll();
        if (CollectionUtils.isNotEmpty(systemParameterList)) {
            for (SystemParameter systemParameter : systemParameterList) {
                SystemParameterUtil.getInstance().putSystemParamValue(systemParameter.getName(), systemParameter.getValue());
                SystemParameterUtil.getInstance().putSystemParamValue1(systemParameter.getId(), systemParameter.getValue());
            }
        }

        // 加载构件分类
        ComponentAreaService componentAreaService = (ComponentAreaService) applicationContext.getBean("componentAreaService");
        List<ComponentArea> componentAreaList = componentAreaService.findAll();
        if (CollectionUtils.isNotEmpty(componentAreaList)) {
            for (ComponentArea componentArea : componentAreaList) {
                ComponentInfoUtil.getInstance().putComponentArea(componentArea);
            }
        }
        ComponentAssembleAreaService componentAssembleAreaService = (ComponentAssembleAreaService) applicationContext.getBean("componentAssembleAreaService");
        List<ComponentAssembleArea> componentAssembleAreaList = componentAssembleAreaService.findAll();
        if (CollectionUtils.isNotEmpty(componentAssembleAreaList)) {
            for (ComponentAssembleArea componentAssembleArea : componentAssembleAreaList) {
                ComponentInfoUtil.getInstance().putComponentAssembleArea(componentAssembleArea);
            }
        }

        // 加载构件
        ComponentVersionService componentVersionService = (ComponentVersionService) applicationContext.getBean("componentVersionService");
        List<ComponentVersion> componentVersionList = componentVersionService.getAll();
        if (CollectionUtils.isNotEmpty(componentVersionList)) {
            for (ComponentVersion componentVersion : componentVersionList) {
                boolean flag = false;
                if (StringUtil.isEmpty(componentVersion.getAreaPath())) {
                    componentVersion.setAreaPath(ComponentInfoUtil.getInstance().getComponentAreaAllPath(componentVersion.getAreaId()));
                    flag = true;
                }
                if (StringUtil.isEmpty(componentVersion.getAssembleAreaPath())
                        && ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    componentVersion.setAssembleAreaPath(ComponentInfoUtil.getInstance().getComponentAssembleAreaAllPath(componentVersion.getAssembleAreaId()));
                    flag = true;
                }
                if (flag) {
                    componentVersionService.save(componentVersion);
                }
                ComponentInfoUtil.getInstance().putComponentVersion(componentVersion);
            }
        }
        ConstructService constructService = (ConstructService) applicationContext.getBean("constructService");
        List<Construct> constructList = constructService.getAll();
        if (CollectionUtils.isNotEmpty(constructList)) {
            for (Construct construct : constructList) {
                ComponentInfoUtil.getInstance().putConstruct(construct);
            }
        }
        ConstructDetailService constructDetailService = (ConstructDetailService) applicationContext.getBean("constructDetailService");
        List<ConstructDetail> constructDetailList = constructDetailService.findAll();
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            for (ConstructDetail constructDetail : constructDetailList) {
                ComponentInfoUtil.getInstance().putConstructDetail(constructDetail);
            }
        }

        // 加载构件中的系统参数
        ComponentSystemParameterService componentSystemParameterService = (ComponentSystemParameterService) applicationContext
                .getBean("componentSystemParameterService");
        List<ComponentSystemParameter> componentSystemParamList = componentSystemParameterService.findAll();
        if (CollectionUtils.isNotEmpty(componentSystemParamList)) {
            Map<String, Map<String, ComponentSystemParameter>> map = new HashMap<String, Map<String, ComponentSystemParameter>>();
            Map<String, ComponentSystemParameter> map1 = null;
            for (ComponentSystemParameter componentSystemParameter : componentSystemParamList) {
                map1 = map.get(componentSystemParameter.getComponentVersionId());
                if (map1 == null) {
                    map1 = new HashMap<String, ComponentSystemParameter>();
                    map.put(componentSystemParameter.getComponentVersionId(), map1);
                }
                map1.put(componentSystemParameter.getId(), componentSystemParameter);
            }
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String componentVersionId = iterator.next();
                ComponentParamsUtil.putComponentSystemParamMap(componentVersionId, map.get(componentVersionId));
            }
        }

        // 加载构件中系统参数和系统参数的关联关系
        ComponentSystemParameterRelationService componentSystemParameterRelationService = (ComponentSystemParameterRelationService) applicationContext
                .getBean("componentSystemParameterRelationService");
        List<ComponentSystemParameterRelation> componentSystemParamRelationList = componentSystemParameterRelationService.findAll();
        if (CollectionUtils.isNotEmpty(componentSystemParamRelationList)) {
            Map<String, List<ComponentSystemParameterRelation>> map = new HashMap<String, List<ComponentSystemParameterRelation>>();
            List<ComponentSystemParameterRelation> list = null;
            for (ComponentSystemParameterRelation relation : componentSystemParamRelationList) {
                list = map.get(relation.getComponentVersionId());
                if (list == null) {
                    list = new ArrayList<ComponentSystemParameterRelation>();
                    map.put(relation.getComponentVersionId(), list);
                }
                list.add(relation);
            }
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String componentVersionId = iterator.next();
                ComponentParamsUtil.putComponentSystemParamRelationList(componentVersionId, map.get(componentVersionId));
            }
        }

        // 加载构件自身参数
        ComponentSelfParamService componentSelfParamService = (ComponentSelfParamService) applicationContext.getBean("componentSelfParamService");
        List<ComponentSelfParam> componentSelfParamList = componentSelfParamService.findAll();
        if (CollectionUtils.isNotEmpty(componentSelfParamList)) {
            Map<String, List<ComponentSelfParam>> map = new HashMap<String, List<ComponentSelfParam>>();
            List<ComponentSelfParam> list = null;
            for (ComponentSelfParam componentSelfParam : componentSelfParamList) {
                list = map.get(componentSelfParam.getComponentVersionId());
                if (list == null) {
                    list = new ArrayList<ComponentSelfParam>();
                    map.put(componentSelfParam.getComponentVersionId(), list);
                }
                list.add(componentSelfParam);
            }
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String componentVersionId = iterator.next();
                ComponentParamsUtil.putComponentSelfParamList(componentVersionId, map.get(componentVersionId));
            }
        }

        // 加载构件输入参数
        ComponentInputParamService componentInputParamService = (ComponentInputParamService) applicationContext.getBean("componentInputParamService");
        List<ComponentInputParam> componentInputParamList = componentInputParamService.findAll();
        if (CollectionUtils.isNotEmpty(componentInputParamList)) {
            Map<String, List<ComponentInputParam>> map = new HashMap<String, List<ComponentInputParam>>();
            List<ComponentInputParam> list = null;
            for (ComponentInputParam componentInputParam : componentInputParamList) {
                list = map.get(componentInputParam.getComponentVersionId());
                if (list == null) {
                    list = new ArrayList<ComponentInputParam>();
                    map.put(componentInputParam.getComponentVersionId(), list);
                }
                list.add(componentInputParam);
            }
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String componentVersionId = iterator.next();
                ComponentParamsUtil.putComponentInputParamList(componentVersionId, map.get(componentVersionId));
            }
        }

        // 加载组合构件自身参数
        ConstructSelfParamService constructSelfParamService = (ConstructSelfParamService) applicationContext.getBean("constructSelfParamService");
        List<ConstructSelfParam> constructSelfParamList = constructSelfParamService.findAll();
        if (CollectionUtils.isNotEmpty(constructSelfParamList)) {
            Map<String, List<ConstructSelfParam>> map = new HashMap<String, List<ConstructSelfParam>>();
            List<ConstructSelfParam> list = null;
            for (ConstructSelfParam constructSelfParam : constructSelfParamList) {
                list = map.get(constructSelfParam.getConstructId());
                if (list == null) {
                    list = new ArrayList<ConstructSelfParam>();
                    map.put(constructSelfParam.getConstructId(), list);
                }
                list.add(constructSelfParam);
            }
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String constructId = iterator.next();
                ComponentParamsUtil.putConstructSelfParamList(constructId, map.get(constructId));
            }
        }

        // 加载组合构件输入参数
        ConstructInputParamService constructInputParamService = (ConstructInputParamService) applicationContext.getBean("constructInputParamService");
        List<ConstructInputParam> constructInputParamList = constructInputParamService.findAll();
        if (CollectionUtils.isNotEmpty(constructInputParamList)) {
            Map<String, List<ConstructInputParam>> map = new HashMap<String, List<ConstructInputParam>>();
            List<ConstructInputParam> list = null;
            for (ConstructInputParam constructInputParam : constructInputParamList) {
                list = map.get(constructInputParam.getConstructId());
                if (list == null) {
                    list = new ArrayList<ConstructInputParam>();
                    map.put(constructInputParam.getConstructId(), list);
                }
                list.add(constructInputParam);
            }
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String constructId = iterator.next();
                ComponentParamsUtil.putConstructInputParamList(constructId, map.get(constructId));
            }
        }

        // 加载构件绑定预留区后的自身配置参数
        ConstructDetailSelfParamService constructDetailSelfParamService = (ConstructDetailSelfParamService) applicationContext
                .getBean("constructDetailSelfParamService");
        List<ConstructDetailSelfParam> constructDetailSelfParamList = constructDetailSelfParamService.findAll();
        if (CollectionUtils.isNotEmpty(constructDetailSelfParamList)) {
            Map<String, List<ConstructDetailSelfParam>> map = new HashMap<String, List<ConstructDetailSelfParam>>();
            List<ConstructDetailSelfParam> list = null;
            for (ConstructDetailSelfParam constructDetailSelfParam : constructDetailSelfParamList) {
                list = map.get(constructDetailSelfParam.getConstructDetailId());
                if (list == null) {
                    list = new ArrayList<ConstructDetailSelfParam>();
                    map.put(constructDetailSelfParam.getConstructDetailId(), list);
                }
                list.add(constructDetailSelfParam);
            }
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String constructDetailId = iterator.next();
                ComponentParamsUtil.putConstructDetailSelfParamList(constructDetailId, map.get(constructDetailId));
            }
        }

        // 加载组合构件中构件和预留区绑定关系的相关信息
        ConstructFunctionService constructFunctionService = (ConstructFunctionService) applicationContext.getBean("constructFunctionService");
        ConstructCallbackService constructCallbackService = (ConstructCallbackService) applicationContext.getBean("constructCallbackService");
        Map<String, List<String[]>> allConstructFunctionMap = constructFunctionService.getAllConstructFunctions();
        if (!allConstructFunctionMap.isEmpty()) {
            String constructDetailId = null;
            for (Iterator<String> it = allConstructFunctionMap.keySet().iterator(); it.hasNext();) {
                constructDetailId = it.next();
                ComponentParamsUtil.putParamFunctions(constructDetailId,
                        ComponentParamsUtil.parseParamFunctions(allConstructFunctionMap.get(constructDetailId)));
            }
        }
        Map<String, List<String[]>> allConstructCallbackMap = constructCallbackService.getAllConstructCallbacks();
        if (!allConstructCallbackMap.isEmpty()) {
            String constructDetailId = null;
            for (Iterator<String> it = allConstructCallbackMap.keySet().iterator(); it.hasNext();) {
                constructDetailId = it.next();
                ComponentParamsUtil.putParamCallbacks(constructDetailId,
                        ComponentParamsUtil.parseParamCallbacks(allConstructCallbackMap.get(constructDetailId)));
            }
        }

        // 加载菜单自身参数
        MenuSelfParamService menuSelfParamService = (MenuSelfParamService) applicationContext.getBean("menuSelfParamService");
        List<MenuSelfParam> menuSelfParamList = menuSelfParamService.findAll();
        if (CollectionUtils.isNotEmpty(menuSelfParamList)) {
            Map<String, List<MenuSelfParam>> map = new HashMap<String, List<MenuSelfParam>>();
            List<MenuSelfParam> list = null;
            for (MenuSelfParam menuSelfParam : menuSelfParamList) {
                list = map.get(menuSelfParam.getMenuId());
                if (list == null) {
                    list = new ArrayList<MenuSelfParam>();
                    map.put(menuSelfParam.getMenuId(), list);
                }
                list.add(menuSelfParam);
            }
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String menuId = iterator.next();
                ComponentParamsUtil.putMenuSelfParamList(menuId, map.get(menuId));
            }
        }

        // 加载菜单输入参数
        MenuInputParamService menuInputParamService = (MenuInputParamService) applicationContext.getBean("menuInputParamService");
        List<MenuInputParam> menuInputParamList = menuInputParamService.findAll();
        if (CollectionUtils.isNotEmpty(menuInputParamList)) {
            Map<String, List<MenuInputParam>> map = new HashMap<String, List<MenuInputParam>>();
            List<MenuInputParam> list = null;
            for (MenuInputParam menuInputParam : menuInputParamList) {
                list = map.get(menuInputParam.getMenuId());
                if (list == null) {
                    list = new ArrayList<MenuInputParam>();
                    map.put(menuInputParam.getMenuId(), list);
                }
                list.add(menuInputParam);
            }
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String menuId = iterator.next();
                ComponentParamsUtil.putMenuInputParamList(menuId, map.get(menuId));
            }
        }

        // 加载应用定义
        AppUtil.getInstance().load();

        // 加载菜单
        MenuUtil.getInstance().cacheMenu();

        // 加载资源
        ResourceService resourceService = (ResourceService) applicationContext.getBean("resourceService");
        List<Resource> resourceList = resourceService.findAll();
        if (CollectionUtils.isNotEmpty(resourceList)) {
            Map<String, String> resourceMenuIdMap = new HashMap<String, String>();
            Resource resource = null;
            for (Iterator<Resource> it = resourceList.iterator(); it.hasNext();) {
                resource = it.next();
                if ("0".equals(resource.getType())) {
                    resourceMenuIdMap.put(resource.getId(), resource.getTargetId());
                    it.remove();
                }
            }
            String menuId = null;
            for (Iterator<Resource> it = resourceList.iterator(); it.hasNext();) {
                resource = it.next();
                menuId = resourceMenuIdMap.get(resource.getParentId());
                if (StringUtil.isNotEmpty(menuId)) {
                    if ("1".equals(resource.getCanUse())) {
                        ResourceUtil.getInstance().putButtonResource(menuId, resource);
                    } else {
                        ResourceUtil.getInstance().putCannotUseButtonResource(menuId, resource);
                    }
                }
            }
        }
        ResourceButtonService resourceButtonService = (ResourceButtonService) applicationContext.getBean("resourceButtonService");
        List<ResourceButton> resourceButtonList = resourceButtonService.findAll();
        if (CollectionUtils.isNotEmpty(resourceButtonList)) {
            for (ResourceButton resourceButton : resourceButtonList) {
                if ("0".equals(resourceButton.getButtonSource())) {
                    ResourceUtil.getInstance().putResourceButton(resourceButton.getResourceId(), ResourceUtil.CONSTRUCT_BUTTON, resourceButton.getButtonId());
                } else {
                    ResourceUtil.getInstance().putResourceButton(resourceButton.getResourceId(), ResourceUtil.PAGE_COMPONENT_BUTTON,
                            resourceButton.getButtonId());
                }
            }
        }

        // 启动工作流服务
//        Coflow.getDefineManager().startService();

        // 设置当前环境中的最大session数
        if (SystemParameterUtil.getInstance().getSystemParamValue("用户登录最大次数") != null) {
            ConcurrentSessionControlStrategy sessionAuthenticationStrategy = (ConcurrentSessionControlStrategy) applicationContext
                    .getBean(ConcurrentSessionControlStrategy.class);
            sessionAuthenticationStrategy.setMaximumSessions(Integer.parseInt(SystemParameterUtil.getInstance().getSystemParamValue("用户登录最大次数")));
        }
        // 启动同步索引库线程
//        IndexCommonUtil.startIndexThread();
    }
}

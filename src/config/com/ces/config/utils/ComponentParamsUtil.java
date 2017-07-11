package com.ces.config.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameter;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameterRelation;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetailSelfParam;
import com.ces.config.dhtmlx.entity.construct.ConstructInputParam;
import com.ces.config.dhtmlx.entity.construct.ConstructSelfParam;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.menu.MenuInputParam;
import com.ces.config.dhtmlx.entity.menu.MenuSelfParam;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.xarch.core.web.listener.XarchListener;

public class ComponentParamsUtil {

    /** * 系统参数在Ehcache中的cache名称 */
    private static final String COMPONENT_SYSTEM_PARAM = "COMPONENT_SYSTEM_PARAM";

    /** * 系统参数关联在Ehcache中的cache名称 */
    private static final String COMPONENT_SYSTEM_PARAM_RELATION = "COMPONENT_SYSTEM_PARAM_RELATION";

    /** * 构件自身参数在Ehcache中的cache名称 */
    private static final String COMPONENT_SELF_PARAM = "COMPONENT_SELF_PARAM";

    /** * 构件输入参数在Ehcache中的cache名称 */
    private static final String COMPONENT_INPUT_PARAM = "COMPONENT_INPUT_PARAM";

    /** * 组合构件自身参数在Ehcache中的cache名称 */
    private static final String CONSTRUCT_SELF_PARAM = "CONSTRUCT_SELF_PARAM";

    /** * 组合构件输入参数在Ehcache中的cache名称 */
    private static final String CONSTRUCT_INPUT_PARAM = "CONSTRUCT_INPUT_PARAM";

    /** * 构件绑定预留区后的自身配置参数在Ehcache中的cache名称 */
    private static final String CONSTRUCT_DETAIL_SELF_PARAM = "CONSTRUCT_DETAIL_SELF_PARAM";

    /** * 菜单自身参数在Ehcache中的cache名称 */
    private static final String MENU_SELF_PARAM = "MENU_SELF_PARAM";

    /** * 菜单输入参数在Ehcache中的cache名称 */
    private static final String MENU_INPUT_PARAM = "MENU_INPUT_PARAM";

    /** * ConstructDetail中的入参和方法配置 */
    private static final String CONSTRUCT_DETAIL_FUNCTION = "CONSTRUCT_DETAIL_FUNCTION";

    /** * ConstructDetail中的出参和回调函数配置 */
    private static final String CONSTRUCT_DETAIL_CALLBACK = "CONSTRUCT_DETAIL_CALLBACK";

    /**
     * 获取系统参数Map
     * 
     * @param componentVersionId 构件版本ID
     * @return Map<String, ComponentSystemParameter>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, ComponentSystemParameter> getComponentSystemParamMap(String componentVersionId) {
        Map<String, ComponentSystemParameter> map = (Map<String, ComponentSystemParameter>) EhcacheUtil.getCache(COMPONENT_SYSTEM_PARAM, componentVersionId);
        return map;
    }

    /**
     * 添加系统参数Map
     * 
     * @param componentVersionId 构件版本ID
     * @param Map<String, ComponentSystemParameter> 系统参数
     */
    public static void putComponentSystemParamMap(String componentVersionId, Map<String, ComponentSystemParameter> map) {
        EhcacheUtil.setCache(COMPONENT_SYSTEM_PARAM, componentVersionId, map);
    }

    /**
     * 删除系统参数Map
     * 
     * @param componentVersionId 构件版本ID
     */
    public static void removeComponentSystemParamMap(String componentVersionId) {
        EhcacheUtil.removeCache(COMPONENT_SYSTEM_PARAM, componentVersionId);
    }

    /**
     * 获取系统参数关联列表
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentSystemParameterRelation>
     */
    @SuppressWarnings("unchecked")
    public static List<ComponentSystemParameterRelation> getComponentSystemParamRelationList(String componentVersionId) {
        List<ComponentSystemParameterRelation> list = (List<ComponentSystemParameterRelation>) EhcacheUtil.getCache(COMPONENT_SYSTEM_PARAM_RELATION,
                componentVersionId);
        return list;
    }

    /**
     * 添加系统参数关联列表
     * 
     * @param componentVersionId 构件版本ID
     * @param list 系统参数关联列表
     */
    public static void putComponentSystemParamRelationList(String componentVersionId, List<ComponentSystemParameterRelation> list) {
        EhcacheUtil.setCache(COMPONENT_SYSTEM_PARAM_RELATION, componentVersionId, list);
    }

    /**
     * 删除系统参数关联列表
     * 
     * @param componentVersionId 构件版本ID
     */
    public static void removeComponentSystemParamRelationList(String componentVersionId) {
        EhcacheUtil.removeCache(COMPONENT_SYSTEM_PARAM_RELATION, componentVersionId);
    }

    /**
     * 获取构件自身参数列表
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentSelfParam>
     */
    @SuppressWarnings("unchecked")
    public static List<ComponentSelfParam> getComponentSelfParamList(String componentVersionId) {
        List<ComponentSelfParam> list = (List<ComponentSelfParam>) EhcacheUtil.getCache(COMPONENT_SELF_PARAM, componentVersionId);
        return list;
    }

    /**
     * 添加构件自身参数列表
     * 
     * @param componentVersionId 构件版本ID
     * @param list 构件自身参数列表
     */
    public static void putComponentSelfParamList(String componentVersionId, List<ComponentSelfParam> list) {
        EhcacheUtil.setCache(COMPONENT_SELF_PARAM, componentVersionId, list);
    }

    /**
     * 删除构件自身参数列表
     * 
     * @param componentVersionId 构件版本ID
     */
    public static void removeComponentSelfParamList(String componentVersionId) {
        EhcacheUtil.removeCache(COMPONENT_SELF_PARAM, componentVersionId);
    }

    /**
     * 获取构件输入参数列表
     * 
     * @param componentVersionId 构件版本ID
     * @return List<ComponentInputParam>
     */
    @SuppressWarnings("unchecked")
    public static List<ComponentInputParam> getComponentInputParamList(String componentVersionId) {
        List<ComponentInputParam> list = (List<ComponentInputParam>) EhcacheUtil.getCache(COMPONENT_INPUT_PARAM, componentVersionId);
        return list;
    }

    /**
     * 添加构件输入参数列表
     * 
     * @param componentVersionId 构件版本ID
     * @param list 构件输入参数列表
     */
    public static void putComponentInputParamList(String componentVersionId, List<ComponentInputParam> list) {
        EhcacheUtil.setCache(COMPONENT_INPUT_PARAM, componentVersionId, list);
    }

    /**
     * 删除构件输入参数列表
     * 
     * @param componentVersionId 构件版本ID
     */
    public static void removeComponentInputParamList(String componentVersionId) {
        EhcacheUtil.removeCache(COMPONENT_INPUT_PARAM, componentVersionId);
    }

    /**
     * 获取组合构件自身参数列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructSelfParam>
     */
    @SuppressWarnings("unchecked")
    public static List<ConstructSelfParam> getConstructSelfParamList(String constructId) {
        return (List<ConstructSelfParam>) EhcacheUtil.getCache(CONSTRUCT_SELF_PARAM, constructId);
    }

    /**
     * 添加组合构件自身参数列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @param list 构件自身参数列表
     */
    public static void putConstructSelfParamList(String constructId, List<ConstructSelfParam> list) {
        EhcacheUtil.setCache(CONSTRUCT_SELF_PARAM, constructId, list);
    }

    /**
     * 添加组合构件自身参数
     * 
     * @param constructId 组合构件绑定关系ID
     * @param constructSelfParam 构件自身参数
     */
    public static void putConstructSelfParamList(String constructId, ConstructSelfParam constructSelfParam) {
        List<ConstructSelfParam> constructSelfParamList = getConstructSelfParamList(constructId);
        if (constructSelfParamList == null) {
            constructSelfParamList = new ArrayList<ConstructSelfParam>();
            putConstructSelfParamList(constructId, constructSelfParamList);
        }
        if (CollectionUtils.isNotEmpty(constructSelfParamList)) {
            for (Iterator<ConstructSelfParam> i = constructSelfParamList.iterator(); i.hasNext();) {
                if (constructSelfParam.getId().equals(i.next().getId())) {
                    i.remove();
                    break;
                }
            }
        }
        constructSelfParamList.add(constructSelfParam);
    }

    /**
     * 删除组合构件自身参数列表
     * 
     * @param constructId 组合构件绑定关系ID
     */
    public static void removeConstructSelfParamList(String constructId) {
        EhcacheUtil.removeCache(CONSTRUCT_SELF_PARAM, constructId);
    }

    /**
     * 获取组合构件输入参数列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<ConstructInputParam>
     */
    @SuppressWarnings("unchecked")
    public static List<ConstructInputParam> getConstructInputParamList(String constructId) {
        List<ConstructInputParam> list = (List<ConstructInputParam>) EhcacheUtil.getCache(CONSTRUCT_INPUT_PARAM, constructId);
        return list;
    }

    /**
     * 添加组合构件输入参数列表
     * 
     * @param constructId 组合构件绑定关系ID
     * @param list 构件输入参数列表
     */
    public static void putConstructInputParamList(String constructId, List<ConstructInputParam> list) {
        EhcacheUtil.setCache(CONSTRUCT_INPUT_PARAM, constructId, list);
    }

    /**
     * 添加组合构件输入参数
     * 
     * @param constructId 组合构件绑定关系ID
     * @param constructInputParam 构件输入参数
     */
    public static void putConstructInputParamList(String constructId, ConstructInputParam constructInputParam) {
        List<ConstructInputParam> constructInputParamList = getConstructInputParamList(constructId);
        if (constructInputParamList == null) {
            constructInputParamList = new ArrayList<ConstructInputParam>();
            putConstructInputParamList(constructId, constructInputParamList);
        }
        if (CollectionUtils.isNotEmpty(constructInputParamList)) {
            for (Iterator<ConstructInputParam> i = constructInputParamList.iterator(); i.hasNext();) {
                if (constructInputParam.getId().equals(i.next().getId())) {
                    i.remove();
                    break;
                }
            }
        }
        constructInputParamList.add(constructInputParam);
    }

    /**
     * 删除组合构件输入参数列表
     * 
     * @param constructId 组合构件绑定关系ID
     */
    public static void removeConstructInputParamList(String constructId) {
        EhcacheUtil.removeCache(CONSTRUCT_INPUT_PARAM, constructId);
    }

    /**
     * 获取构件绑定预留区后的自身配置参数列表
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<ConstructDetailSelfParam>
     */
    @SuppressWarnings("unchecked")
    public static List<ConstructDetailSelfParam> getConstructDetailSelfParamList(String constructDetailId) {
        List<ConstructDetailSelfParam> list = (List<ConstructDetailSelfParam>) EhcacheUtil.getCache(CONSTRUCT_DETAIL_SELF_PARAM, constructDetailId);
        return list;
    }

    /**
     * 添加构件绑定预留区后的自身配置参数列表
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @param list 构件自身参数列表
     */
    public static void putConstructDetailSelfParamList(String constructDetailId, List<ConstructDetailSelfParam> list) {
        EhcacheUtil.setCache(CONSTRUCT_DETAIL_SELF_PARAM, constructDetailId, list);
    }

    /**
     * 添加构件绑定预留区后的自身配置参数
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @param constructDetailSelfParam 构件自身参数
     */
    public static void putConstructDetailSelfParamList(String constructId, ConstructDetailSelfParam constructDetailSelfParam) {
        List<ConstructDetailSelfParam> constructDetailSelfParamList = getConstructDetailSelfParamList(constructId);
        if (constructDetailSelfParamList == null) {
            constructDetailSelfParamList = new ArrayList<ConstructDetailSelfParam>();
            putConstructDetailSelfParamList(constructId, constructDetailSelfParamList);
        }
        if (CollectionUtils.isNotEmpty(constructDetailSelfParamList)) {
            for (Iterator<ConstructDetailSelfParam> i = constructDetailSelfParamList.iterator(); i.hasNext();) {
                if (constructDetailSelfParam.getId().equals(i.next().getId())) {
                    i.remove();
                    break;
                }
            }
        }
        constructDetailSelfParamList.add(constructDetailSelfParam);
    }

    /**
     * 删除构件绑定预留区后的自身配置参数列表
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     */
    public static void removeConstructDetailSelfParamList(String constructDetailId) {
        EhcacheUtil.removeCache(CONSTRUCT_DETAIL_SELF_PARAM, constructDetailId);
    }

    /**
     * 获取菜单自身参数列表
     * 
     * @param menuId 菜单ID
     * @return List<MenuSelfParam>
     */
    @SuppressWarnings("unchecked")
    public static List<MenuSelfParam> getMenuSelfParamList(String menuId) {
        List<MenuSelfParam> list = (List<MenuSelfParam>) EhcacheUtil.getCache(MENU_SELF_PARAM, menuId);
        return list;
    }

    /**
     * 添加菜单自身参数列表
     * 
     * @param menuId 菜单ID
     * @param list 菜单自身参数列表
     */
    public static void putMenuSelfParamList(String menuId, List<MenuSelfParam> list) {
        EhcacheUtil.setCache(MENU_SELF_PARAM, menuId, list);
    }

    /**
     * 添加菜单自身参数
     * 
     * @param menuId 菜单ID
     * @param menuSelfParam 菜单自身参数
     */
    public static void putMenuSelfParamList(String constructId, MenuSelfParam menuSelfParam) {
        List<MenuSelfParam> menuSelfParamList = getMenuSelfParamList(constructId);
        if (menuSelfParamList == null) {
            menuSelfParamList = new ArrayList<MenuSelfParam>();
            putMenuSelfParamList(constructId, menuSelfParamList);
        }
        if (CollectionUtils.isNotEmpty(menuSelfParamList)) {
            for (Iterator<MenuSelfParam> i = menuSelfParamList.iterator(); i.hasNext();) {
                if (menuSelfParam.getId().equals(i.next().getId())) {
                    i.remove();
                    break;
                }
            }
        }
        menuSelfParamList.add(menuSelfParam);
    }

    /**
     * 删除菜单自身参数列表
     * 
     * @param menuId 菜单ID
     */
    public static void removeMenuSelfParamList(String menuId) {
        EhcacheUtil.removeCache(MENU_SELF_PARAM, menuId);
    }

    /**
     * 获取菜单输入参数列表
     * 
     * @param menuId 菜单ID
     * @return List<MenuInputParam>
     */
    @SuppressWarnings("unchecked")
    public static List<MenuInputParam> getMenuInputParamList(String menuId) {
        List<MenuInputParam> list = (List<MenuInputParam>) EhcacheUtil.getCache(MENU_INPUT_PARAM, menuId);
        return list;
    }

    /**
     * 添加菜单输入参数列表
     * 
     * @param menuId 菜单ID
     * @param list 菜单输入参数列表
     */
    public static void putMenuInputParamList(String menuId, List<MenuInputParam> list) {
        EhcacheUtil.setCache(MENU_INPUT_PARAM, menuId, list);
    }

    /**
     * 添加菜单输入参数
     * 
     * @param menuId 菜单ID
     * @param list 菜单输入参数列表
     */
    public static void putMenuInputParamList(String constructId, MenuInputParam menuInputParam) {
        List<MenuInputParam> menuInputParamList = getMenuInputParamList(constructId);
        if (menuInputParamList == null) {
            menuInputParamList = new ArrayList<MenuInputParam>();
            putMenuInputParamList(constructId, menuInputParamList);
        }
        if (CollectionUtils.isNotEmpty(menuInputParamList)) {
            for (Iterator<MenuInputParam> i = menuInputParamList.iterator(); i.hasNext();) {
                if (menuInputParam.getId().equals(i.next().getId())) {
                    i.remove();
                    break;
                }
            }
        }
        menuInputParamList.add(menuInputParam);
    }

    /**
     * 删除菜单输入参数列表
     * 
     * @param menuId 菜单ID
     */
    public static void removeMenuInputParamList(String menuId) {
        EhcacheUtil.removeCache(MENU_INPUT_PARAM, menuId);
    }

    /**
     * 获取ConstructDetail中的入参和方法配置
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return String
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getParamFunctions(String constructDetailId) {
        return (List<Map<String, Object>>) EhcacheUtil.getCache(CONSTRUCT_DETAIL_FUNCTION, constructDetailId);
    }

    /**
     * 缓存ConstructDetail中的入参和方法配置
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @param paramFunctions ConstructDetail中的入参和方法配置
     */
    public static void putParamFunctions(String constructDetailId, List<Map<String, Object>> paramFunctions) {
        EhcacheUtil.setCache(CONSTRUCT_DETAIL_FUNCTION, constructDetailId, paramFunctions);
    }

    /**
     * 移除ConstructDetail中的入参和方法配置
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     */
    public static void removeParamFunctions(String constructDetailId) {
        EhcacheUtil.removeCache(CONSTRUCT_DETAIL_FUNCTION, constructDetailId);
    }

    /**
     * 获取ConstructDetail中的出参和回调函数配置
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return String
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getParamCallbacks(String constructDetailId) {
        return (List<Map<String, Object>>) EhcacheUtil.getCache(CONSTRUCT_DETAIL_CALLBACK, constructDetailId);
    }

    /**
     * 缓存ConstructDetail中的出参和回调函数配置
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @param paramCallbacks ConstructDetail中的出参和回调函数配置
     */
    public static void putParamCallbacks(String constructDetailId, List<Map<String, Object>> paramCallbacks) {
        EhcacheUtil.setCache(CONSTRUCT_DETAIL_CALLBACK, constructDetailId, paramCallbacks);
    }

    /**
     * 移除ConstructDetail中的出参和回调函数配置
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     */
    public static void removeParamCallbacks(String constructDetailId) {
        EhcacheUtil.removeCache(CONSTRUCT_DETAIL_CALLBACK, constructDetailId);
    }

    /**
     * 获取构件系统参数Map
     * 
     * @param componentVersion 构件版本
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getSystemParamMapOfComponent(ComponentVersion componentVersion) {
        List<Map<String, Object>> systemParams = new ArrayList<Map<String, Object>>();
        String componentVersionId = componentVersion.getId();
        if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
            Construct construct = XarchListener.getBean(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
            componentVersionId = construct.getBaseComponentVersionId();
        }
        List<ComponentSystemParameterRelation> list = getComponentSystemParamRelationList(componentVersionId);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, ComponentSystemParameter> componentSystemParamMap = getComponentSystemParamMap(componentVersionId);
            Map<String, Object> entityMap = null;
            for (ComponentSystemParameterRelation relation : list) {
                ComponentSystemParameter componentSystemParameter = componentSystemParamMap.get(relation.getComponentSystemParamId());
                if (componentSystemParameter != null) {
                    String name = componentSystemParameter.getName();
                    String value = SystemParameterUtil.getInstance().getSystemParamValue1(relation.getSystemParamId());
                    if (StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(value)) {
                        entityMap = new HashMap<String, Object>();
                        entityMap.put("name", name);
                        entityMap.put("value", value);
                        systemParams.add(entityMap);
                    }
                }
            }
        }
        return systemParams;
    }

    /**
     * 获取构件系统参数条件
     * 
     * @param componentVersion 构件版本
     * @return String
     */
    public static String getSystemParamsOfComponent(String baseComponentVersionId) {
        StringBuilder sb = new StringBuilder();
        List<ComponentSystemParameterRelation> list = getComponentSystemParamRelationList(baseComponentVersionId);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, ComponentSystemParameter> componentSystemParamMap = getComponentSystemParamMap(baseComponentVersionId);
            for (ComponentSystemParameterRelation relation : list) {
                ComponentSystemParameter componentSystemParameter = componentSystemParamMap.get(relation.getComponentSystemParamId());
                if (componentSystemParameter != null) {
                    String name = componentSystemParameter.getName();
                    String value = SystemParameterUtil.getInstance().getSystemParamValue1(relation.getSystemParamId());
                    if (StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(value)) {
                        sb.append("&").append(name).append("=").append(value);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取构件默认自身参数Map
     * 
     * @param componentVersionId 构件版本ID
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getSelfParamMapOfComponent(String componentVersionId) {
        List<Map<String, Object>> selfParams = new ArrayList<Map<String, Object>>();
        List<ComponentSelfParam> componentSelfParamList = getComponentSelfParamList(componentVersionId);
        if (CollectionUtils.isNotEmpty(componentSelfParamList)) {
            Map<String, Object> entityMap = null;
            for (ComponentSelfParam selfParam : componentSelfParamList) {
                if (StringUtil.isNotEmpty(selfParam.getName()) && StringUtil.isNotEmpty(selfParam.getValue())) {
                    entityMap = new HashMap<String, Object>();
                    entityMap.put("name", selfParam.getName());
                    entityMap.put("value", selfParam.getValue());
                    selfParams.add(entityMap);
                }
            }
        }
        return selfParams;
    }

    /**
     * 获取构件默认自身参数条件
     * 
     * @param componentVersionId 构件版本ID
     * @return String
     */
    public static String getSelfParamsOfComponent(String componentVersionId) {
        StringBuilder sb = new StringBuilder();
        List<ComponentSelfParam> componentSelfParamList = getComponentSelfParamList(componentVersionId);
        if (CollectionUtils.isNotEmpty(componentSelfParamList)) {
            for (ComponentSelfParam selfParam : componentSelfParamList) {
                if (StringUtil.isNotEmpty(selfParam.getName()) && StringUtil.isNotEmpty(selfParam.getValue())) {
                    sb.append("&").append(selfParam.getName()).append("=").append(selfParam.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取构件默认输入参数Map
     * 
     * @param componentVersionId 构件版本ID
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getInputParamMapOfComponent(String componentVersionId) {
        List<Map<String, Object>> inputParams = new ArrayList<Map<String, Object>>();
        List<ComponentInputParam> componentInputParamList = getComponentInputParamList(componentVersionId);
        if (CollectionUtils.isNotEmpty(componentInputParamList)) {
            Map<String, Object> entityMap = null;
            for (ComponentInputParam inputParam : componentInputParamList) {
                entityMap = new HashMap<String, Object>();
                entityMap.put("name", inputParam.getName());
                entityMap.put("value", inputParam.getValue());
                inputParams.add(entityMap);
            }
        }
        return inputParams;
    }

    /**
     * 获取构件默认输入参数条件
     * 
     * @param componentVersionId 构件版本ID
     * @return String
     */
    public static String getInputParamsOfComponent(String componentVersionId) {
        StringBuilder sb = new StringBuilder();
        List<ComponentInputParam> componentInputParamList = getComponentInputParamList(componentVersionId);
        if (CollectionUtils.isNotEmpty(componentInputParamList)) {
            for (ComponentInputParam inputParam : componentInputParamList) {
                if (StringUtil.isNotEmpty(inputParam.getName()) && StringUtil.isNotEmpty(inputParam.getValue())) {
                    sb.append("&").append(inputParam.getName()).append("=").append(inputParam.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取组合构件自身参数Map
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getSelfParamMapOfConstruct(String constructId) {
        List<Map<String, Object>> selfParams = new ArrayList<Map<String, Object>>();
        List<ConstructSelfParam> constructSelfParamList = getConstructSelfParamList(constructId);
        if (CollectionUtils.isNotEmpty(constructSelfParamList)) {
            Map<String, Object> entityMap = null;
            for (ConstructSelfParam selfParam : constructSelfParamList) {
                if (StringUtil.isNotEmpty(selfParam.getName()) && StringUtil.isNotEmpty(selfParam.getValue())) {
                    entityMap = new HashMap<String, Object>();
                    entityMap.put("name", selfParam.getName());
                    entityMap.put("value", selfParam.getValue());
                    selfParams.add(entityMap);
                }
            }
        }
        return selfParams;
    }

    /**
     * 获取组合构件自身参数条件
     * 
     * @param constructId 组合构件绑定关系ID
     * @return String
     */
    public static String getSelfParamsOfConstruct(String constructId) {
        StringBuilder sb = new StringBuilder();
        List<ConstructSelfParam> constructSelfParamList = getConstructSelfParamList(constructId);
        if (CollectionUtils.isNotEmpty(constructSelfParamList)) {
            for (ConstructSelfParam selfParam : constructSelfParamList) {
                if (StringUtil.isNotEmpty(selfParam.getName()) && StringUtil.isNotEmpty(selfParam.getValue())) {
                    sb.append("&").append(selfParam.getName()).append("=").append(selfParam.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取组合构件输入参数Map
     * 
     * @param constructId 组合构件绑定关系ID
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getInputParamMapOfConstruct(String constructId) {
        List<Map<String, Object>> inputParams = new ArrayList<Map<String, Object>>();
        List<ConstructInputParam> constructInputParamList = getConstructInputParamList(constructId);
        if (CollectionUtils.isNotEmpty(constructInputParamList)) {
            Map<String, Object> entityMap = null;
            for (ConstructInputParam inputParam : constructInputParamList) {
                if (StringUtil.isNotEmpty(inputParam.getName()) && StringUtil.isNotEmpty(inputParam.getValue())) {
                    entityMap = new HashMap<String, Object>();
                    entityMap.put("name", inputParam.getName());
                    entityMap.put("value", inputParam.getValue());
                    inputParams.add(entityMap);
                }
            }
        }
        return inputParams;
    }

    /**
     * 获取组合构件输入参数条件
     * 
     * @param constructId 组合构件绑定关系ID
     * @return String
     */
    public static String getInputParamsOfConstruct(String constructId) {
        StringBuilder sb = new StringBuilder();
        List<ConstructInputParam> constructInputParamList = getConstructInputParamList(constructId);
        if (CollectionUtils.isNotEmpty(constructInputParamList)) {
            for (ConstructInputParam inputParam : constructInputParamList) {
                if (StringUtil.isNotEmpty(inputParam.getName()) && StringUtil.isNotEmpty(inputParam.getValue())) {
                    sb.append("&").append(inputParam.getName()).append("=").append(inputParam.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取构件绑定预留区后的自身配置参数Map
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getSelfParamMapOfConstructDetail(String constructDetailId) {
        List<Map<String, Object>> selfParams = new ArrayList<Map<String, Object>>();
        List<ConstructDetailSelfParam> constructDetailSelfParamList = getConstructDetailSelfParamList(constructDetailId);
        if (CollectionUtils.isNotEmpty(constructDetailSelfParamList)) {
            Map<String, Object> entityMap = null;
            for (ConstructDetailSelfParam selfParam : constructDetailSelfParamList) {
                if (StringUtil.isNotEmpty(selfParam.getName()) && StringUtil.isNotEmpty(selfParam.getValue())) {
                    entityMap = new HashMap<String, Object>();
                    entityMap.put("name", selfParam.getName());
                    entityMap.put("value", selfParam.getValue());
                    selfParams.add(entityMap);
                }
            }
        }
        return selfParams;
    }

    /**
     * 获取构件绑定预留区后的自身配置参数条件
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return String
     */
    public static String getSelfParamsOfConstructDetail(String constructDetailId) {
        StringBuilder sb = new StringBuilder();
        List<ConstructDetailSelfParam> constructDetailSelfParamList = getConstructDetailSelfParamList(constructDetailId);
        if (CollectionUtils.isNotEmpty(constructDetailSelfParamList)) {
            for (ConstructDetailSelfParam selfParam : constructDetailSelfParamList) {
                if (StringUtil.isNotEmpty(selfParam.getName()) && StringUtil.isNotEmpty(selfParam.getValue())) {
                    sb.append("&").append(selfParam.getName()).append("=").append(selfParam.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取菜单自身参数Map
     * 
     * @param menuId 菜单ID
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getSelfParamMapOfMenu(String menuId) {
        List<Map<String, Object>> selfParams = new ArrayList<Map<String, Object>>();
        List<MenuSelfParam> menuSelfParamList = getMenuSelfParamList(menuId);
        if (CollectionUtils.isNotEmpty(menuSelfParamList)) {
            Map<String, Object> entityMap = null;
            for (MenuSelfParam selfParam : menuSelfParamList) {
                if (StringUtil.isNotEmpty(selfParam.getName()) && StringUtil.isNotEmpty(selfParam.getValue())) {
                    entityMap = new HashMap<String, Object>();
                    entityMap.put("name", selfParam.getName());
                    entityMap.put("value", selfParam.getValue());
                    selfParams.add(entityMap);
                }
            }
        }
        return selfParams;
    }

    /**
     * 获取菜单自身参数条件
     * 
     * @param menuId 菜单ID
     * @return String
     */
    public static String getSelfParamsOfMenu(String menuId) {
        StringBuilder sb = new StringBuilder();
        List<MenuSelfParam> menuSelfParamList = getMenuSelfParamList(menuId);
        if (CollectionUtils.isNotEmpty(menuSelfParamList)) {
            for (MenuSelfParam selfParam : menuSelfParamList) {
                if (StringUtil.isNotEmpty(selfParam.getName()) && StringUtil.isNotEmpty(selfParam.getValue())) {
                    sb.append("&").append(selfParam.getName()).append("=").append(selfParam.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取菜单输入参数Map
     * 
     * @param menuId 菜单ID
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getInputParamMapOfMenu(String menuId) {
        List<Map<String, Object>> inputParams = new ArrayList<Map<String, Object>>();
        List<MenuInputParam> menuInputParamList = getMenuInputParamList(menuId);
        if (CollectionUtils.isNotEmpty(menuInputParamList)) {
            Map<String, Object> entityMap = null;
            for (MenuInputParam inputParam : menuInputParamList) {
                if (StringUtil.isNotEmpty(inputParam.getName()) && StringUtil.isNotEmpty(inputParam.getValue())) {
                    entityMap = new HashMap<String, Object>();
                    entityMap.put("name", inputParam.getName());
                    entityMap.put("value", inputParam.getValue());
                    inputParams.add(entityMap);
                }
            }
        }
        return inputParams;
    }

    /**
     * 转换ConstructDetail中的入参和方法的格式
     * 
     * @param constructFunctionList 方法返回值和入参的绑定关系
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> parseParamFunctions(List<String[]> constructFunctionList) {
        List<Map<String, Object>> paramFunctions = new ArrayList<Map<String, Object>>();
        Map<String, List<Map<String, Object>>> functionParamMap = new HashMap<String, List<Map<String, Object>>>();
        if (CollectionUtils.isNotEmpty(constructFunctionList)) {
            Map<String, Object> entityMap = null;
            List<Map<String, Object>> paramList = null;
            for (String[] str : constructFunctionList) {
                paramList = functionParamMap.get(str[3]);
                if (paramList == null) {
                    paramList = new ArrayList<Map<String, Object>>();
                    functionParamMap.put(str[3], paramList);
                }
                entityMap = new HashMap<String, Object>();
                entityMap.put("outputParam", str[5]);
                entityMap.put("inputParam", str[1]);
                paramList.add(entityMap);
            }
            Map<String, Object> paramFunctionMap = null;
            String functionName = null;
            for (Iterator<String> iterator = functionParamMap.keySet().iterator(); iterator.hasNext();) {
                paramFunctionMap = new HashMap<String, Object>();
                functionName = iterator.next();
                paramFunctionMap.put("functionName", functionName);
                paramFunctionMap.put("params", functionParamMap.get(functionName));
                paramFunctions.add(paramFunctionMap);
            }
        }
        return paramFunctions;
    }

    /**
     * 转换ConstructDetail中的出参和回调函数的格式
     * 
     * @param constructCallbackList 回调函数参数和构件出参绑定关系
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> parseParamCallbacks(List<String[]> constructCallbackList) {
        List<Map<String, Object>> paramCallbacks = new ArrayList<Map<String, Object>>();
        Map<String, List<Map<String, Object>>> callbackParamMap = new HashMap<String, List<Map<String, Object>>>();
        if (CollectionUtils.isNotEmpty(constructCallbackList)) {
            Map<String, Object> entityMap = null;
            List<Map<String, Object>> paramList = null;
            for (String[] str : constructCallbackList) {
                paramList = callbackParamMap.get(str[1]);
                if (paramList == null) {
                    paramList = new ArrayList<Map<String, Object>>();
                    callbackParamMap.put(str[1], paramList);
                }
                entityMap = new HashMap<String, Object>();
                entityMap.put("outputParam", str[5]);
                entityMap.put("inputParam", str[3]);
                paramList.add(entityMap);
            }
            Map<String, Object> paramCallbackMap = null;
            String callbackName = null;
            for (Iterator<String> iterator = callbackParamMap.keySet().iterator(); iterator.hasNext();) {
                paramCallbackMap = new HashMap<String, Object>();
                callbackName = iterator.next();
                paramCallbackMap.put("callbackName", callbackName);
                paramCallbackMap.put("params", callbackParamMap.get(callbackName));
                paramCallbacks.add(paramCallbackMap);
            }
        }
        return paramCallbacks;
    }

    /**
     * 获取菜单输入参数条件
     * 
     * @param menuId 菜单ID
     * @return String
     */
    public static String getInputParamsOfMenu(String menuId) {
        StringBuilder sb = new StringBuilder();
        List<MenuInputParam> menuInputParamList = getMenuInputParamList(menuId);
        if (CollectionUtils.isNotEmpty(menuInputParamList)) {
            for (MenuInputParam inputParam : menuInputParamList) {
                if (StringUtil.isNotEmpty(inputParam.getName()) && StringUtil.isNotEmpty(inputParam.getValue())) {
                    sb.append("&").append(inputParam.getName()).append("=").append(inputParam.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取构件参数条件
     * 
     * @param menuId 菜单ID
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @param componentVersion 构件版本
     * @return String
     */
    public static String getParamsOfComponent(String constructDetailId, ComponentVersion componentVersion) {
        StringBuilder sb = new StringBuilder();
        // 系统参数配置
        String componentVersionId = componentVersion.getId();
        if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
            Construct construct = XarchListener.getBean(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
            componentVersionId = construct.getBaseComponentVersionId();
        }
        sb.append(getSystemParamsOfComponent(componentVersionId));
        if (StringUtil.isNotEmpty(constructDetailId)) {
            // 构件自身参数
            sb.append(getSelfParamsOfConstructDetail(constructDetailId));
        } else if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
            Construct construct = XarchListener.getBean(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
            // 构件自身参数
            sb.append(getSelfParamsOfConstruct(construct.getId()));
            // 构件输入参数
            sb.append(getInputParamsOfConstruct(construct.getId()));
        } else {
            // 构件自身参数
            sb.append(getSelfParamsOfComponent(componentVersion.getId()));
            // 构件输入参数
            sb.append(getInputParamsOfComponent(componentVersion.getId()));
        }
        return sb.toString();
    }

    /**
     * 获取菜单构件参数条件
     * 
     * @param menu 菜单
     * @return String
     */
    public static String getParamsOfMenu(Menu menu) {
        StringBuilder sb = new StringBuilder();
        // 系统参数配置
        String baseComponentVersionId = menu.getBaseComponentVersionId();
        if (StringUtil.isEmpty(baseComponentVersionId)) {
            String componentVersionId = menu.getComponentVersionId();
            ComponentVersion componentVersion = XarchListener.getBean(ComponentVersionService.class).getByID(componentVersionId);
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct construct = XarchListener.getBean(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                baseComponentVersionId = construct.getBaseComponentVersionId();
            }
        }
        sb.append(getSystemParamsOfComponent(baseComponentVersionId));
        // 构件自身参数
        sb.append(getSelfParamsOfMenu(menu.getId()));
        // 构件输入参数
        sb.append(getInputParamsOfMenu(menu.getId()));
        return sb.toString();
    }
}

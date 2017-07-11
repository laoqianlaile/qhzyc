package com.ces.config.dhtmlx.service.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.authority.AuthorityConstructButtonDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.menu.MenuDao;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.menu.MenuInputParam;
import com.ces.config.dhtmlx.entity.menu.MenuSelfParam;
import com.ces.config.dhtmlx.entity.resource.Resource;
import com.ces.config.dhtmlx.entity.systemcomponent.SystemComponentVersion;
import com.ces.config.dhtmlx.service.appmanage.AppDefineService;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.authority.AuthorityComponentButtonService;
import com.ces.config.dhtmlx.service.authority.AuthorityConstructButtonService;
import com.ces.config.dhtmlx.service.authority.AuthorityDataCopyService;
import com.ces.config.dhtmlx.service.authority.AuthorityDataService;
import com.ces.config.dhtmlx.service.authority.AuthorityService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.CommonComponentRelationService;
import com.ces.config.dhtmlx.service.component.ComponentInputParamService;
import com.ces.config.dhtmlx.service.component.ComponentSelfParamService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.resource.ResourceService;
import com.ces.config.dhtmlx.service.systemcomponent.SystemComponentVersionService;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.MenuUtil;
import com.ces.config.utils.StringUtil;
import com.ces.utils.BeanUtils;

/**
 * 菜单Service
 * 
 * @author wanglei
 * @date 2013-07-15
 */
@Component("menuService")
public class MenuService extends ConfigDefineDaoService<Menu, MenuDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("menuDao")
    @Override
    protected void setDaoUnBinding(MenuDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public Menu save(Menu entity) {
        if (StringUtil.isEmpty(entity.getUseNavigation())) {
            entity.setUseNavigation("0");
        }
        if (StringUtil.isEmpty(entity.getBindingType())) {
            entity.setBindingType("");
        }
        if (StringUtil.isEmpty(entity.getIsQuickMenu())) {
            entity.setIsQuickMenu("0");
        }
        boolean flag = true;
        Menu menu = null;
        if (StringUtil.isEmpty(entity.getId())) {
            // 新增
            menu = getDao().save(entity);
            getService(ResourceService.class).saveMenuResource(menu);
        } else {
            // 修改
            Menu oldEntity = getByID(entity.getId());
            if (ConstantVar.Menu.BindingType.COMPONENT.equals(oldEntity.getBindingType())
                    && (!ConstantVar.Menu.BindingType.COMPONENT.equals(entity.getBindingType()) || !oldEntity.getComponentVersionId().equals(
                            entity.getComponentVersionId()))) {
                // 原来绑定构件的修改成不绑定构件，或者绑定不同的构件
                // 删除权限按钮
                getDaoFromContext(AuthorityConstructButtonDao.class).deleteByMenuId(entity.getId());
                // 删除自身参数和输入参数
                getService(MenuSelfParamService.class).deleteByMenuId(entity.getId());
                getService(MenuInputParamService.class).deleteByMenuId(entity.getId());
                // 删除这个菜单下对应的应用定义配置
                getService(AppDefineService.class).deleteByMenuId(entity.getId());
                // 删除该菜单资源下的按钮资源
                Resource resource = getService(ResourceService.class).getByTargetId(entity.getId());
                if (resource != null) {
                    getService(ResourceService.class).deleteByMenuResourceId(resource.getId());
                }
            }
            if (ConstantVar.Menu.BindingType.COMPONENT.equals(oldEntity.getBindingType())
                    && oldEntity.getComponentVersionId().equals(entity.getComponentVersionId())) {
                flag = false;
            }
            if (!entity.getName().equals(oldEntity.getName())) {
                // 修改菜单资源名称
                Resource resource = getService(ResourceService.class).getByTargetId(entity.getId());
                if (resource != null) {
                    resource.setName(entity.getName());
                    getService(ResourceService.class).save(resource);
                }
            }
            menu = getDao().save(entity);
        }
        Menu cloneMenu = new Menu();
        BeanUtils.copy(menu, cloneMenu);
        MenuUtil.getInstance().putMenu(cloneMenu);
        if (ConstantVar.Menu.BindingType.COMPONENT.equals(entity.getBindingType()) && flag) {
            String componentVersionId = menu.getComponentVersionId();
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersionId);
                componentVersionId = construct.getBaseComponentVersionId();
            }
            List<ComponentInputParam> componentParameterList = getService(ComponentInputParamService.class).getByComponentVersionId(componentVersionId);
            if (CollectionUtils.isNotEmpty(componentParameterList)) {
                MenuInputParam menuInputParam = null;
                for (ComponentInputParam componentInputParam : componentParameterList) {
                    menuInputParam = new MenuInputParam();
                    menuInputParam.setMenuId(menu.getId());
                    menuInputParam.setInputParamId(componentInputParam.getId());
                    menuInputParam.setName(componentInputParam.getName());
                    menuInputParam.setValue(componentInputParam.getValue());
                    getService(MenuInputParamService.class).save(menuInputParam);
                }
            }
            List<ComponentSelfParam> componentSelfParamList = getService(ComponentSelfParamService.class).getByComponentVersionId(componentVersionId);
            if (CollectionUtils.isNotEmpty(componentSelfParamList)) {
                MenuSelfParam menuSelfParam = null;
                for (ComponentSelfParam componentSelfParam : componentSelfParamList) {
                    menuSelfParam = new MenuSelfParam();
                    menuSelfParam.setComponentVersionId(componentVersionId);
                    menuSelfParam.setMenuId(menu.getId());
                    menuSelfParam.setSelfParamId(componentSelfParam.getId());
                    menuSelfParam.setName(componentSelfParam.getName());
                    menuSelfParam.setOptions(componentSelfParam.getOptions());
                    menuSelfParam.setRemark(componentSelfParam.getRemark());
                    menuSelfParam.setType(componentSelfParam.getType());
                    menuSelfParam.setText(componentSelfParam.getText());
                    menuSelfParam.setValue(componentSelfParam.getValue());
                    getService(MenuSelfParamService.class).save(menuSelfParam);
                }
            }
        }
        return menu;
    }

    /*
     * (非 Javadoc) <p>标题: delete</p> <p>描述: 多个删除并且向下递归</p> @param ids 多个ID用逗号隔开
     * @see com.ces.xarch.core.service.AbstractService#delete(java.io.Serializable)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            MenuUtil.getInstance().removeMenu(getByID(idArray[i]));
            List<Menu> childMenuList = getDao().getByParentId(idArray[i]);
            for (Menu childMenu : childMenuList) {
                delete(childMenu.getId());
            }
            getDao().delete(idArray[i]);
            getService(AuthorityService.class).deleteByMenuId(idArray[i]);
            getService(AuthorityConstructButtonService.class).deleteByMenuId(idArray[i]);
            getService(AuthorityComponentButtonService.class).deleteByMenuId(idArray[i]);
            getService(AuthorityDataService.class).deleteByMenuId(idArray[i]);
            getService(AuthorityDataCopyService.class).deleteByMenuId(idArray[i]);
            // 删除菜单对应的应用定义配置
            getService(AppDefineService.class).deleteByMenuId(idArray[i]);
            // 删除资源
            getService(ResourceService.class).deleteByTargetId(idArray[i]);
        }
    }

    /**
     * 获取菜单
     * 
     * @param name 菜单名称
     * @param parentId 父菜单ID
     * @return Menu
     */
    public Menu getMenuByNameAndParentId(String name, String parentId) {
        return getDao().getByNameAndParentId(name, parentId);
    }

    /**
     * 获取菜单
     * 
     * @param code 菜单编码
     * @param rootMenuId 父菜单ID
     * @return Menu
     */
    public Menu getMenuByCodeAndRootMenuId(String code, String rootMenuId) {
        return getDao().getByCodeAndRootMenuId(code, rootMenuId);
    }

    /**
     * 获取子菜单
     * 
     * @param parentId 父菜单ID
     * @return List<Menu>
     */
    public List<Menu> getMenuByParentId(String parentId) {
        return getDao().getByParentId(parentId);
    }

    /**
     * 获取系统下的菜单
     * 
     * @param rootMenuId 根菜单ID
     * @return List<Menu>
     */
    public List<Menu> getMenuByRootMenuId(String rootMenuId) {
        List<Menu> menuList = getDao().getByRootMenuId(rootMenuId);
        menuList.add(getByID(rootMenuId));
        return menuList;
    }

    /**
     * 获取菜单下子菜单最大显示顺序
     * 
     * @param parentId 父菜单ID
     * @return Integer
     */
    public Integer getMaxShowOrder(String parentId) {
        return getDao().getMaxShowOrder(parentId);
    }

    /**
     * 获取显示顺序范围内的菜单
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param parentId 父菜单ID
     * @return List<Menu>
     */
    public List<Menu> getByShowOrderBetweenAndParentId(Integer start, Integer end, String parentId) {
        return getDao().getByShowOrderBetweenAndParentId(start, end, parentId);
    }

    /**
     * 根据构件版本ID获取绑定该构件的菜单
     * 
     * @param componentVersionId 版本ID
     * @return List<String>
     */
    public List<Menu> getByComponentVersionId(String componentVersionId) {
        return getDao().getByComponentVersionId(componentVersionId);
    }

    /**
     * 获取所有子孙菜单
     * 
     * @param parentId 父菜单ID
     * @return List<Menu>
     */
    public List<Menu> getAllChildMenu(String parentId) {
        List<Menu> childMenuList = getDao().getByParentId(parentId);
        if (CollectionUtils.isNotEmpty(childMenuList)) {
            int size = childMenuList.size();
            for (int i = 0; i < size; i++) {
                List<Menu> temp = getAllChildMenu(childMenuList.get(i).getId());
                if (CollectionUtils.isNotEmpty(temp)) {
                    childMenuList.addAll(temp);
                }
            }
        }
        return childMenuList;
    }

    /**
     * 根据菜单ID获取其绑定的构件版本（根菜单获取所有子菜单的）
     * 
     * @param menuId 菜单ID
     * @return Map<String, List<ComponentVersion>>
     */
    public Map<String, Set<ComponentVersion>> getComponentVersionMapByMenuId(String menuId) {
        Map<String, Set<ComponentVersion>> componentVersionMap = new HashMap<String, Set<ComponentVersion>>();
        Menu menu = getByID(menuId);
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        if ("-1".equals(menu.getParentId())) {
            // 根菜单
            List<String> componentVersionIdList = getDao().getComponentVersionIdByRootMenuId(menuId);
            StringBuilder componentVersionIds = new StringBuilder();
            if (CollectionUtils.isNotEmpty(componentVersionIdList)) {
                for (String componentVersionid : componentVersionIdList) {
                    componentVersionIds.append(",").append(componentVersionid);
                }
                componentVersionIds.deleteCharAt(0);
                componentVersionSet.addAll(getService(ComponentVersionService.class).getComponentVersionsByIds(componentVersionIds.toString()));
                componentVersionMap.put("-1", componentVersionSet);
                for (ComponentVersion componentVersion : componentVersionSet) {
                    if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                        // 组合构件
                        getComponentVersionMapOfAssemble(componentVersion, componentVersionMap);
                    } else {
                        // 关联的公用构件
                        componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
                    }
                }
            }
        } else {
            String componentVersionId = getDao().getComponentVersionIdById(menuId);
            if (StringUtil.isNotEmpty(componentVersionId)) {
                ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
                componentVersionSet.add(componentVersion);
                componentVersionMap.put("-1", componentVersionSet);
                if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                    // 组合构件
                    getComponentVersionMapOfAssemble(componentVersion, componentVersionMap);
                } else {
                    // 关联的公用构件
                    componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
                }
            }
        }
        return componentVersionMap;
    }

    /**
     * 获取组合构件下绑定的构件
     * 
     * @param assembleComponentVersion 组合构件版本
     * @param componentVersionMap 构件版本Map
     */
    public void getComponentVersionMapOfAssemble(ComponentVersion assembleComponentVersion, Map<String, Set<ComponentVersion>> componentVersionMap) {
        if (componentVersionMap.get(assembleComponentVersion.getId()) == null) {
            Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
            Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(assembleComponentVersion.getId());
            ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
            componentVersionSet.add(baseComponentVersion);
            // 基础构件关联的公用构件
            componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(baseComponentVersion.getId()));
            List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructId(construct.getId());
            if (CollectionUtils.isNotEmpty(constructDetailList)) {
                ComponentVersion componentVersion = null;
                for (ConstructDetail constructDetail : constructDetailList) {
                    if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                        continue;
                    }
                    componentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                    componentVersionSet.add(componentVersion);
                    if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                        // 组合构件
                        getComponentVersionMapOfAssemble(componentVersion, componentVersionMap);
                    } else {
                        // 绑定构件关联的公用构件
                        componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
                    }
                }
            }
            componentVersionMap.put(assembleComponentVersion.getId(), componentVersionSet);
        }
    }

    /**
     * 根据菜单ID获取绑定的构件版本
     * 
     * @param menuId 菜单ID
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getMenuComponentVersionByMenuId(String menuId) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        String componentVersionId = getDao().getComponentVersionIdById(menuId);
        if (StringUtil.isNotEmpty(componentVersionId)) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            componentVersionSet.add(componentVersion);
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                // 组合构件
                componentVersionSet.addAll(getService(ConstructService.class).getComponentVersionOfConstruct(componentVersion.getId(), null));
            } else {
                // 关联的公用构件
                componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
            }
        }
        return componentVersionSet;
    }

    /**
     * 根据根菜单ID获取其下子孙菜单绑定的构件版本和直接绑定该系统的构件(基础构件)
     * 
     * @param rootMenuId 根菜单ID
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getComponentVersionByRootMenuId(String rootMenuId) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        List<String> componentVersionIdList = getDao().getComponentVersionIdByRootMenuId(rootMenuId);
        if (CollectionUtils.isNotEmpty(componentVersionIdList)) {
            ComponentVersion componentVersion = null;
            for (String componentVersionId : componentVersionIdList) {
                if (StringUtil.isNotEmpty(componentVersionId)) {
                    componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
                    componentVersionSet.add(componentVersion);
                    if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                        // 组合构件
                        componentVersionSet.addAll(getService(ConstructService.class).getComponentVersionOfConstruct(componentVersion.getId()));
                    } else {
                        // 关联的公用构件
                        componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
                    }
                }
            }
        }
        List<SystemComponentVersion> systemComponentVersionList = getService(SystemComponentVersionService.class).getByRootMenuId(rootMenuId);
        if (CollectionUtils.isNotEmpty(systemComponentVersionList)) {
            for (SystemComponentVersion systemComponentVersion : systemComponentVersionList) {
                componentVersionSet.add(systemComponentVersion.getComponentVersion());
            }
        }
        return componentVersionSet;
    }

    /**
     * 根据菜单ID获取其下的自定义构件
     * 
     * @param menuId 菜单ID
     * @return List<ComponentVersion>
     */
    public List<ComponentVersion> getSelfDefinesByMenuId(String menuId) {
        List<ComponentVersion> componentVersionList = new ArrayList<ComponentVersion>();
        String componentVersionId = getDao().getComponentVersionIdById(menuId);
        if (StringUtil.isNotEmpty(componentVersionId)) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                // 组合构件
                componentVersionList.addAll(getService(ConstructService.class).getSelfDefinesOfConstruct(componentVersionId));
            }
        }
        return componentVersionList;
    }

    /**
     * 获取多个菜单
     * 
     * @param menuIds 根据IDs获取菜单
     * @return List<Menu>
     */
    public List<Menu> getMenusByIds(String menuIds) {
        List<Menu> list = new ArrayList<Menu>();
        if (StringUtil.isNotEmpty(menuIds)) {
            DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
            String hql = "select t from Menu t where t.id in('" + menuIds.replace(",", "','") + "')";
            list = dao.queryEntityForList(hql, Menu.class);
        }
        return list;
    }

    /**
     * 获取菜单绑定的构件版本，排除掉没有选择的按钮
     * 
     * @param menuIds 菜单Ids
     * @param notUsedButtonMap 没有选择的按钮Map<menuId, List<constructDetailId>>
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getComponentVersions(String menuIds, Map<String, List<String>> notUsedButtonMap) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        List<Menu> menuList = getMenusByIds(menuIds);
        Set<String> componentVersionIdSet = new HashSet<String>();
        // 将notUsedButtonMap中的key换成组合构件的构件版本ID
        Map<String, List<String>> notUsedConstructDetailMap = new HashMap<String, List<String>>();
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                if (StringUtil.isNotEmpty(menu.getComponentVersionId())) {
                    componentVersionIdSet.add(menu.getComponentVersionId());
                    if (MapUtils.isNotEmpty(notUsedButtonMap) && notUsedButtonMap.keySet().contains(menu.getId())) {
                        notUsedConstructDetailMap.put(menu.getComponentVersionId(), notUsedButtonMap.get(menu.getId()));
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(componentVersionIdSet)) {
            ComponentVersion componentVersion = null;
            List<String> notUsedConstructDetails = null;
            for (String componentVersionId : componentVersionIdSet) {
                if (componentVersionId != null) {
                    componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
                    componentVersionSet.add(componentVersion);
                    if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                        // 组合构件
                        notUsedConstructDetails = notUsedConstructDetailMap.get(componentVersionId);
                        if (CollectionUtils.isNotEmpty(notUsedConstructDetails)) {
                            componentVersionSet.addAll(getService(ConstructService.class).getComponentVersionOfConstruct(componentVersion.getId(),
                                    notUsedConstructDetails));
                        } else {
                            componentVersionSet.addAll(getService(ConstructService.class).getComponentVersionOfConstruct(componentVersion.getId(),
                                    notUsedConstructDetails));
                        }
                    } else {
                        // 关联的公用构件
                        componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
                    }
                }
            }
        }
        return componentVersionSet;
    }

    /**
     * 获取菜单绑定的构件，绑定的按钮构件中只有选中的按钮
     * 
     * @param rootMenuId 根菜单ID
     * @param menuIds 菜单Ids
     * @param usedButtonMap 选择的按钮Map<menuId, List<constructDetailId>>
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getUsedComponentVersions(String menuIds, Map<String, List<String>> usedButtonMap) {
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        List<Menu> menuList = getMenusByIds(menuIds);
        Set<String> componentVersionIdSet = new HashSet<String>();
        // 将usedButtonMap中的key换成组合构件的构件版本ID
        Map<String, List<String>> usedConstructDetailMap = new HashMap<String, List<String>>();
        if (CollectionUtils.isNotEmpty(menuList)) {
            for (Menu menu : menuList) {
                if (StringUtil.isNotEmpty(menu.getComponentVersionId())) {
                    componentVersionIdSet.add(menu.getComponentVersionId());
                    if (usedButtonMap.keySet().contains(menu.getId())) {
                        usedConstructDetailMap.put(menu.getComponentVersionId(), usedButtonMap.get(menu.getId()));
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(componentVersionIdSet)) {
            ComponentVersion componentVersion = null;
            List<String> usedConstructDetails = null;
            for (String componentVersionId : componentVersionIdSet) {
                if (componentVersionId != null) {
                    componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
                    componentVersionSet.add(componentVersion);
                    if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                        // 组合构件
                        usedConstructDetails = usedConstructDetailMap.get(componentVersionId);
                        if (CollectionUtils.isNotEmpty(usedConstructDetails)) {
                            componentVersionSet.addAll(getService(ConstructService.class).getUsedComponentVersionOfConstruct(componentVersion.getId(),
                                    usedConstructDetails));
                        }
                    } else {
                        // 关联的公用构件
                        componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersion.getId()));
                    }
                }
            }
        }
        return componentVersionSet;
    }

    /**
     * 校验该菜单绑定的构件在该系统中是否能用
     * 
     * @param menu 菜单
     * @return boolean
     */
    public boolean checkComponentVersion(Menu menu) {
        boolean flag = true;
        // 如果系统中所有构件都只有一个版本，那么就不会有版本冲突
        List<Object[]> compCvCount = getService(ComponentVersionService.class).getCompCvCount();
        if (CollectionUtils.isNotEmpty(compCvCount)) {
            // 构件生产库中构件版本数大于1的构件ID
            Set<String> cvCountComponentIdSet = new HashSet<String>();
            for (Object[] objs : compCvCount) {
                cvCountComponentIdSet.add(String.valueOf(objs[0]));
            }
            // 存储当前菜单下的所有基础构件
            Set<ComponentVersion> menuComponentVersionSet = new HashSet<ComponentVersion>();
            // 获取当前菜单下绑定的构件版本ID
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(menu.getComponentVersionId());
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                menuComponentVersionSet = getService(ConstructService.class).getComponentVersionOfConstruct(componentVersion.getId());
            } else {
                menuComponentVersionSet.add(componentVersion);
            }
            // 获取本菜单中可能会有多版本的构件
            Set<ComponentVersion> cvSet = new HashSet<ComponentVersion>();
            for (ComponentVersion cv : menuComponentVersionSet) {
                if ((ConstantVar.Component.Type.SELF_DEFINE.indexOf(cv.getComponent().getType()) == -1 || ConstantVar.Component.Type.TAB
                        .equals(cv.getComponent().getType())) && cvCountComponentIdSet.contains(cv.getComponent().getId())) {
                    cvSet.add(cv);
                }
            }
            if (CollectionUtils.isNotEmpty(cvSet)) {
                // 取得根菜单下的所有构件版本ID
                Set<ComponentVersion> rootMenuComponentVersionSet = getComponentVersionByRootMenuId(menu.getRootMenuId());
                ComponentVersion cv = null;
                for (Iterator<ComponentVersion> it = rootMenuComponentVersionSet.iterator(); it.hasNext();) {
                    cv = it.next();
                    if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(cv.getComponent().getType()) == -1
                            || ConstantVar.Component.Type.TAB.equals(cv.getComponent().getType())) {
                        it.remove();
                    }
                }
                if (CollectionUtils.isNotEmpty(rootMenuComponentVersionSet)) {
                    for (ComponentVersion rootMenuComponentVersion : rootMenuComponentVersionSet) {
                        for (ComponentVersion menuComponentVersion : cvSet) {
                            if (rootMenuComponentVersion.getComponent().getId().equals(menuComponentVersion.getComponent().getId())
                                    && !rootMenuComponentVersion.getVersion().equals(menuComponentVersion.getVersion())) {
                                flag = false;
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 根据菜单ID获取绑定的构件版本的ID
     * 
     * @param menuId 菜单ID
     * @return String
     */
    public String getComponentVersionIdById(String menuId) {
        return getDao().getComponentVersionIdById(menuId);
    }

    /**
     * 获取菜单ID下的自定义构件
     * 
     * @param menuId 菜单ID
     * @return Set<ComponentVersion>
     */
    public Set<ComponentVersion> getSelfDefineComponentVersion(String menuId) {
        Set<ComponentVersion> set = new HashSet<ComponentVersion>();
        Menu menu = getService(MenuService.class).getByID(menuId);
        if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(menu.getComponentVersionId());
            if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1) {
                set.add(componentVersion);
            } else if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                set.addAll(getService(ConstructService.class).getSelfDefinesOfConstruct(componentVersion.getId()));
            }
        }
        return set;
    }

    /**
     * qiucs 2015-1-30 下午2:05:40
     * <p>描述: 应用定义菜单树处理 </p>
     * 
     * @return void
     */
    public void processTableMenuData(List<Menu> data, String tableId) {
        int len = data.size(), i = len - 1;
        boolean contained = false;
        List<Menu> appMenus = getAppMenus();
        Set<String> cvIdsOfTable = getService(ModuleService.class).getCVIdsByTableId(tableId);
        if (CollectionUtils.isEmpty(appMenus) || CollectionUtils.isEmpty(cvIdsOfTable)) {
            data.clear();
            return;
        }
        for (; i > -1; i--) {
            if ("sys_0".equals(data.get(i).getId())) {
                data.remove(i);
            } else {
                contained = containTable(data.get(i), cvIdsOfTable, appMenus);
                if (!contained)
                    data.remove(i);
            }
        }
    }

    /**
     * qiucs 2015-2-3 下午5:45:05
     * <p>描述: 判断菜单与tableId是否相关 </p>
     * 
     * @return boolean
     */
    private boolean containTable(Menu menu, Set<String> cvIdsOfTable, List<Menu> appMenus) {
        if (ConstantVar.Menu.BindingType.COMPONENT.equals(menu.getBindingType())) {
            if (appMenus.contains(menu)) {
                return getService(ComponentVersionService.class).containTable(menu.getComponentVersionId(), cvIdsOfTable, true);
            } else {
                return false;
            }
        } else if (ConstantVar.Menu.BindingType.URL.equals(menu.getBindingType())) {
            return false;
        } else {
            List<Menu> list = getMenuByParentId(menu.getId());
            return containTable(list, cvIdsOfTable, appMenus);
        }
    }

    /**
     * qiucs 2015-2-3 下午5:45:11
     * <p>描述: 递归判断子菜单与tableId是否相关 </p>
     * 
     * @return boolean
     */
    private boolean containTable(List<Menu> list, Set<String> cvIdsOfTable, List<Menu> appMenus) {
        if (CollectionUtils.isEmpty(list))
            return false;
        boolean contained = false;
        for (Menu menu : list) {
            contained = containTable(menu, cvIdsOfTable, appMenus);
            if (contained)
                return true;
        }
        return false;
    }

    /**
     * qiujinwei 2015-04-01
     * <p>描述: 获取菜单中的系统/平台类树节点 </p>
     * 
     * @return Map<String, Object>
     */
    public List<Map<String, Object>> getTreeNode() {
        List<Menu> list = getMenuByParentId("-1");
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Menu entity : list) {
            if (entity.getId().equals("sys_0")) {// 系统配置平台节点不显示
                data.add(beanToTreeNode());
            } else {
                data.add(beanToTreeNode(entity));
            }
        }
        return data;
    }

    /**
     * qiujinwei 2015-04-01
     * <p>描述: 构造菜单中的系统/平台类树节点 </p>
     * 
     * @return Map<String, Object>
     */
    public Map<String, Object> beanToTreeNode(Menu entity) {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id", entity.getId());
        data.put("text", entity.getName());
        data.put("type", "0");
        data.put("child", Boolean.FALSE);
        return data;
    }

    /**
     * qiujinwei 2015-04-01
     * <p>描述: 构造菜单中的公共配置树节点 </p>
     * 
     * @return Map<String, Object>
     */
    public Map<String, Object> beanToTreeNode() {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id", "1");
        data.put("text", "公共配置");
        data.put("type", "0");
        data.put("child", Boolean.FALSE);
        return data;
    }

    /**
     * 将菜单的显示顺序加一
     * 
     * @param start 开始显示顺序
     * @param parentId 父菜单ID
     */
    public void updateShowOrderPlusOne(Integer start, String parentId) {
        getDao().updateShowOrderPlusOne(start, parentId);
    }

    /**
     * 获取所有的系统菜单
     * 
     * @return List<Menu>
     */
    public List<Menu> getRootMenuList() {
        return getDao().getRootMenuList();
    }

    /**
     * 加载菜单信息到缓存中
     * 
     * @return List<Menu>
     */
    @SuppressWarnings("unchecked")
    public List<Menu> getMenuList4Cache() {
        List<Menu> menuList = new ArrayList<Menu>();
        String sql = "select m.id,m.parent_id,m.name,m.show_order,m.has_child,m.url,m.binding_type,"
                + "m.component_version_id,m.root_menu_id,m.use_navigation,m.icon1,m.icon2,m.code,m.quick_icon,"
                + "m.is_quick_menu,c.base_component_version_id from t_xtpz_menu m left join t_xtpz_construct c"
                + " on m.component_version_id=c.component_version_id where m.id not like 'sys%' and m.parent_id<>'-1'";
        List<Object[]> list = DatabaseHandlerDao.getInstance().queryForList(sql);
        if (CollectionUtils.isNotEmpty(list)) {
            Menu menu = null;
            for (Object[] objs : list) {
                menu = new Menu();
                menu.setId(StringUtil.null2empty(objs[0]));
                menu.setParentId(StringUtil.null2empty(objs[1]));
                menu.setName(StringUtil.null2empty(objs[2]));
                menu.setShowOrder(Integer.valueOf(StringUtil.null2zero(objs[3])));
                menu.setHasChild("1".equals(StringUtil.null2empty(objs[4])) ? true : false);
                menu.setUrl(StringUtil.null2empty(objs[5]));
                menu.setBindingType(StringUtil.null2empty(objs[6]));
                menu.setComponentVersionId(StringUtil.null2empty(objs[7]));
                menu.setRootMenuId(StringUtil.null2empty(objs[8]));
                menu.setUseNavigation(StringUtil.null2empty(objs[9]));
                menu.setIcon1(StringUtil.null2empty(objs[10]));
                menu.setIcon2(StringUtil.null2empty(objs[11]));
                menu.setCode(StringUtil.null2empty(objs[12]));
                menu.setQuickIcon(StringUtil.null2empty(objs[13]));
                menu.setIsQuickMenu(StringUtil.null2empty(objs[14]));
                menu.setBaseComponentVersionId(StringUtil.null2empty(objs[15]));
                menuList.add(menu);
            }
        }
        return menuList;
    }

    /**
     * qiucs 2015-5-18 下午1:25:21
     * <p>描述: 根据指定树ID获取菜单 </p>
     * 
     * @return List<Menu>
     */
    public List<Menu> getMenuContainTree(String treeId) {
        return getDao().getMenuContainTree(treeId);
    }

    /**
     * qiucs 2015-5-18 下午1:25:26
     * <p>描述: 获取所有带树的菜单 </p>
     * 
     * @return List<Menu>
     */
    public List<Menu> getMenuContainTree() {
    	List<Menu> treeMenuList = new ArrayList<Menu>();
    	List<Menu> list = find("EQ_parentId=-1", new Sort("showOrder"));
    	
    	for (int i = 0, len = list.size(); i < len; i++) {
    		cycleTreeMenu(list.get(i).getId(), treeMenuList);
    	}
    	
        return treeMenuList;
    }
    
    /**
     * qiucs 2015-8-13 下午8:24:40
     * <p>描述: 遍历分类菜单下所有带有树构件的菜单 </p>
     * @return void
     */
    private void cycleTreeMenu(String parentId, List<Menu> treeMenuList) {
    	List<Menu> list = find("EQ_hasChild=1;EQ_parentId=" + parentId, new Sort("showOrder"));
    	
    	for (int i = 0, len = list.size(); i < len; i++) {
    		if (list.get(i).getHasChild()) {
    			cycleTreeMenu(list.get(i).getId(), treeMenuList);
    			treeMenuList.addAll(getDao().getMenuContainTreeByParentId(list.get(i).getId()));
    		}
    	}
    }

    /**
     * qiucs 2015-5-18 下午2:01:24
     * <p>描述: 更换指定菜单下的树结构 </p>
     * 
     * @return void
     */
    @Transactional
    public MessageModel changeTreeOfMenu(String menuId, String treeId) {
        Module md = getDao().getModuleOfMenu(menuId);
        if (null != md && ConstantVar.Component.Type.TREE.equals(md.getType())) {
            md.setTreeId(treeId);
            getService(ModuleService.class).save(md);
        } else {
            return MessageModel.falseInstance("该菜单绑定的不是树型构件，请检查！");
        }
        return MessageModel.trueInstance("OK");
    }

    /**
     * qiucs 2015-6-25 下午2:42:57
     * <p>描述: TODO(这里用一句话描述这个方法的作用) </p>
     * 
     * @return Object
     */
    public Object getApplyMenuTree(String id, String tableId, String type) {
        // TODO Auto-generated method stub
        List<Menu> list = getDao().getByParentId(id);

        processTableMenuData(list, tableId);
        Map<String, Object> data = new HashMap<String, Object>(10);
        List<Map<String, Object>> item = new ArrayList<Map<String, Object>>(10);
        Menu m = null;
        data.put("id", id);
        data.put("item", item);
        Map<String, Object> one = null;
        for (int i = 0, len = list.size(); i < len; i++) {
            m = list.get(i);
            one = new HashMap<String, Object>(10);
            one.put("id", m.getId());
            one.put("text", m.getName());
            if (ConstantVar.Menu.BindingType.COMPONENT.equals(m.getBindingType())) {
                one.put("child", Boolean.FALSE);
            } else {
                one.put("nocheckbox", "1");
                one.put("child", Boolean.TRUE);
            }
            item.add(one);
        }

        return data;
    }

    /**
     * 获取可以进行应用定义的菜单
     * 
     * @return List<Menu>
     */
    public List<Menu> getAppMenus() {
        return getDao().getAppMenus();
    }
}

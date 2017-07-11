package com.ces.config.dhtmlx.service.release;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.release.ReleaseDao;
import com.ces.config.dhtmlx.dao.release.ReleaseDetailDao;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.release.Release;
import com.ces.config.dhtmlx.entity.release.ReleaseDetail;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;

/**
 * 系统发布Service
 * 
 * @author wanglei
 * @date 2013-11-15
 */
@Component("releaseService")
public class ReleaseService extends ConfigDefineDaoService<Release, ReleaseDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("releaseDao")
    @Override
    protected void setDaoUnBinding(ReleaseDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 保存系统发布信息
     * 
     * @param entity 系统发布信息
     * @param menuIdSet 菜单Ids
     * @return Release
     */
    @Transactional
    public Release saveReleaseSystem(Release entity, Set<String> menuIdSet) {
        getDaoFromContext(ReleaseDetailDao.class).deleteBySystemReleaseId(entity.getId());
        Menu rootMenu = getService(MenuService.class).getByID(entity.getRootMenuId());
        List<ReleaseDetail> releaseDetailList = new ArrayList<ReleaseDetail>();
        ReleaseDetail releaseDetail = null;
        String nodeId = UUIDGenerator.uuid();
        releaseDetail = new ReleaseDetail();
        releaseDetail.setName(rootMenu.getName());
        releaseDetail.setNodeId(nodeId);
        releaseDetail.setParentNodeId(entity.getRootMenuId());
        releaseDetail.setDataId("M_" + rootMenu.getId());
        releaseDetailList.add(releaseDetail);
        releaseDetailList.addAll(getReleaseDetailsOfMenu(rootMenu, nodeId, menuIdSet, null, null, null));
        for (ReleaseDetail temp : releaseDetailList) {
            temp.setRootMenuId(entity.getRootMenuId());
            temp.setSystemReleaseId(entity.getId());
        }
        getDaoFromContext(ReleaseDetailDao.class).save(releaseDetailList);
        return entity;
    }

    /**
     * 获取更新包信息
     * 
     * @param rootMenuId 根菜单ID
     * @param menuIdSet 菜单IDs
     * @param reserveZoneIdSet 预留区IDs
     * @param constructDetailIdSet 构件绑定关系IDs
     * @return Map<String, Object>
     */
    public Map<String, Object> getUpdatePackageInfo(String rootMenuId, Set<String> menuIdSet, Set<String> reserveZoneIdSet, Set<String> constructDetailIdSet) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Menu> menuList = new ArrayList<Menu>();
        Map<String, ComponentVersion> componentVersionMap = new HashMap<String, ComponentVersion>();
        Map<String, Construct> constructMap = new HashMap<String, Construct>();
        Map<String, List<ConstructDetail>> constructDetailMap = new HashMap<String, List<ConstructDetail>>();
        List<ReleaseDetail> releaseDetailList = new ArrayList<ReleaseDetail>();
        map.put("Menu", menuList);
        map.put("ComponentVersion", componentVersionMap);
        map.put("Construct", constructMap);
        map.put("ConstructDetail", constructDetailMap);
        map.put("ReleaseDetail", releaseDetailList);
        Menu rootMenu = getService(MenuService.class).getByID(rootMenuId);
        ReleaseDetail releaseDetail = null;
        String nodeId = UUIDGenerator.uuid();
        releaseDetail = new ReleaseDetail();
        releaseDetail.setName(rootMenu.getName());
        releaseDetail.setNodeId(nodeId);
        releaseDetail.setParentNodeId(rootMenuId);
        releaseDetail.setDataId("M_" + rootMenu.getId());
        releaseDetailList.add(releaseDetail);
        releaseDetailList.addAll(getReleaseDetailsOfMenu(rootMenu, nodeId, menuIdSet, map, reserveZoneIdSet, constructDetailIdSet));
        return map;
    }

    /**
     * 获取菜单下的发布详情
     * 
     * @param menu 菜单
     * @param menuNodeId 菜单节点ID
     * @param menuIdSet 选中的菜单IDs
     * @param map 更新包信息
     * @param reserveZoneIdSet 预留区IDs
     * @param constructDetailIdSet 构件绑定关系IDs
     * @return List<ReleaseDetail>
     */
    @SuppressWarnings("unchecked")
    private List<ReleaseDetail> getReleaseDetailsOfMenu(Menu menu, String menuNodeId, Set<String> menuIdSet, Map<String, Object> map,
            Set<String> reserveZoneIdSet, Set<String> constructDetailIdSet) {
        boolean releaseUpdatePackage = false;
        Map<String, ComponentVersion> componentVersionMap = null;
        Map<String, Construct> constructMap = null;
        Map<String, List<ConstructDetail>> constructDetailMap = null;
        if (map != null) {
            releaseUpdatePackage = true;
            componentVersionMap = (Map<String, ComponentVersion>) map.get("ComponentVersion");
            constructMap = (Map<String, Construct>) map.get("Construct");
            constructDetailMap = (Map<String, List<ConstructDetail>>) map.get("ConstructDetail");
        }
        List<ReleaseDetail> releaseDetailList = new ArrayList<ReleaseDetail>();
        ReleaseDetail releaseDetail = null;
        String nodeId = null;
        if ("1".equals(menu.getBindingType())) {
            // 绑定构件，如果是组合构件，树组合构件则加载绑定到树上的构件，其他则加载预留区
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(menu.getComponentVersionId());
            if (releaseUpdatePackage) {
                componentVersionMap.put(componentVersion.getId(), componentVersion);
            }
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
                if (releaseUpdatePackage) {
                    constructMap.put(construct.getId(), construct);
                    componentVersionMap.put(baseComponentVersion.getId(), baseComponentVersion);
                }
                if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())) {
                    List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getConstructInfoByConstructId(construct.getId());
                    if (CollectionUtils.isNotEmpty(constructDetailList)) {
                        // 手动选择部分节点，还是默认选择全部
                        boolean checkedAll = true;
                        for (ConstructDetail constructDetail : constructDetailList) {
                            if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                                continue;
                            }
                            if (releaseUpdatePackage && constructDetailIdSet.contains(constructDetail.getId())) {
                                checkedAll = false;
                                List<ConstructDetail> tempConstructDetailList = constructDetailMap.get(construct.getId());
                                if (tempConstructDetailList == null) {
                                    tempConstructDetailList = new ArrayList<ConstructDetail>();
                                    constructDetailMap.put(construct.getId(), tempConstructDetailList);
                                }
                                tempConstructDetailList.add(constructDetail);
                                nodeId = UUIDGenerator.uuid();
                                releaseDetail = new ReleaseDetail();
                                releaseDetail.setName(constructDetail.getComponentAliasAndVersion());
                                releaseDetail.setNodeId(nodeId);
                                releaseDetail.setParentNodeId(menuNodeId);
                                releaseDetail.setDataId("CD_" + constructDetail.getId());
                                releaseDetailList.add(releaseDetail);
                                releaseDetailList.addAll(getReleaseDetailsOfConstructDetail(constructDetail, nodeId, map, reserveZoneIdSet,
                                        constructDetailIdSet));
                            }
                        }
                        if (checkedAll) {
                            for (ConstructDetail constructDetail : constructDetailList) {
                                if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                                    continue;
                                }
                                if (releaseUpdatePackage) {
                                    List<ConstructDetail> tempConstructDetailList = constructDetailMap.get(construct.getId());
                                    if (tempConstructDetailList == null) {
                                        tempConstructDetailList = new ArrayList<ConstructDetail>();
                                        constructDetailMap.put(construct.getId(), tempConstructDetailList);
                                    }
                                    tempConstructDetailList.add(constructDetail);
                                }
                                nodeId = UUIDGenerator.uuid();
                                releaseDetail = new ReleaseDetail();
                                releaseDetail.setName(constructDetail.getComponentAliasAndVersion());
                                releaseDetail.setNodeId(nodeId);
                                releaseDetail.setParentNodeId(menuNodeId);
                                releaseDetail.setDataId("CD_" + constructDetail.getId());
                                releaseDetailList.add(releaseDetail);
                                releaseDetailList.addAll(getReleaseDetailsOfConstructDetail(constructDetail, nodeId, map, reserveZoneIdSet,
                                        constructDetailIdSet));
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
                        // 手动选择部分节点，还是默认选择全部
                        boolean checkedAll = true;
                        for (ComponentReserveZone reserveZone : componentReserveZoneList) {
                            if (releaseUpdatePackage && reserveZoneIdSet.contains(reserveZone.getId() + "_" + construct.getId())) {
                                checkedAll = false;
                                if (getService(ConstructDetailService.class).isBindingComponent(construct.getId(), reserveZone.getId())) {
                                    nodeId = UUIDGenerator.uuid();
                                    releaseDetail = new ReleaseDetail();
                                    releaseDetail.setName(reserveZone.getAlias());
                                    releaseDetail.setNodeId(nodeId);
                                    releaseDetail.setParentNodeId(menuNodeId);
                                    releaseDetail.setDataId("RZ_" + reserveZone.getId());
                                    releaseDetailList.add(releaseDetail);
                                    releaseDetailList.addAll(getReleaseDetailsOfReserveZone(construct, reserveZone, nodeId, map, reserveZoneIdSet,
                                            constructDetailIdSet));
                                }
                            }
                        }
                        if (checkedAll) {
                            for (ComponentReserveZone reserveZone : componentReserveZoneList) {
                                if (getService(ConstructDetailService.class).isBindingComponent(construct.getId(), reserveZone.getId())) {
                                    nodeId = UUIDGenerator.uuid();
                                    releaseDetail = new ReleaseDetail();
                                    releaseDetail.setName(reserveZone.getAlias());
                                    releaseDetail.setNodeId(nodeId);
                                    releaseDetail.setParentNodeId(menuNodeId);
                                    releaseDetail.setDataId("RZ_" + reserveZone.getId());
                                    releaseDetailList.add(releaseDetail);
                                    releaseDetailList.addAll(getReleaseDetailsOfReserveZone(construct, reserveZone, nodeId, map, reserveZoneIdSet,
                                            constructDetailIdSet));
                                }
                            }
                        }
                    }
                }
            }
        } else if (menu.getHasChild()) {
            List<Menu> childMenuList = getService(MenuService.class).getMenuByParentId(menu.getId());
            if (CollectionUtils.isNotEmpty(childMenuList)) {
                for (Menu childMenu : childMenuList) {
                    if (menuIdSet.contains(childMenu.getId())) {
                        nodeId = UUIDGenerator.uuid();
                        releaseDetail = new ReleaseDetail();
                        releaseDetail.setName(childMenu.getName());
                        releaseDetail.setNodeId(nodeId);
                        releaseDetail.setParentNodeId(menuNodeId);
                        releaseDetail.setDataId("M_" + childMenu.getId());
                        releaseDetailList.add(releaseDetail);
                        releaseDetailList.addAll(getReleaseDetailsOfMenu(childMenu, nodeId, menuIdSet, map, reserveZoneIdSet, constructDetailIdSet));
                    }
                }
            }
        }
        return releaseDetailList;
    }

    /**
     * 获取按钮级构件下的发布详情
     * 
     * @param parentConstructDetail 按钮级构件绑定关系
     * @param parentConstructDetailNodeId 按钮级构件绑定关系树节点ID
     * @param map 更新包信息
     * @param reserveZoneIdSet 预留区IDs
     * @param constructDetailIdSet 构件绑定关系IDs
     * @return List<ReleaseDetail>
     */
    @SuppressWarnings("unchecked")
    private List<ReleaseDetail> getReleaseDetailsOfConstructDetail(ConstructDetail parentConstructDetail, String parentConstructDetailNodeId,
            Map<String, Object> map, Set<String> reserveZoneIdSet, Set<String> constructDetailIdSet) {
        boolean releaseUpdatePackage = false;
        Map<String, ComponentVersion> componentVersionMap = null;
        Map<String, Construct> constructMap = null;
        Map<String, List<ConstructDetail>> constructDetailMap = null;
        if (map != null) {
            releaseUpdatePackage = true;
            componentVersionMap = (Map<String, ComponentVersion>) map.get("ComponentVersion");
            constructMap = (Map<String, Construct>) map.get("Construct");
            constructDetailMap = (Map<String, List<ConstructDetail>>) map.get("ConstructDetail");
        }
        List<ReleaseDetail> releaseDetailList = new ArrayList<ReleaseDetail>();
        ReleaseDetail releaseDetail = null;
        String nodeId = null;
        ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(parentConstructDetail.getComponentVersionId());
        if (releaseUpdatePackage) {
            componentVersionMap.put(componentVersion.getId(), componentVersion);
        }
        if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
            Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
            ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
            if (releaseUpdatePackage) {
                constructMap.put(construct.getId(), construct);
                componentVersionMap.put(baseComponentVersion.getId(), baseComponentVersion);
            }
            if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())) {
                List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getConstructInfoByConstructId(construct.getId());
                if (CollectionUtils.isNotEmpty(constructDetailList)) {
                    // 手动选择部分节点，还是默认选择全部
                    boolean checkedAll = true;
                    for (ConstructDetail constructDetail : constructDetailList) {
                        if (releaseUpdatePackage && constructDetailIdSet.contains(constructDetail.getId())) {
                            if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                                continue;
                            }
                            checkedAll = false;
                            List<ConstructDetail> tempConstructDetailList = constructDetailMap.get(construct.getId());
                            if (tempConstructDetailList == null) {
                                tempConstructDetailList = new ArrayList<ConstructDetail>();
                                constructDetailMap.put(construct.getId(), tempConstructDetailList);
                            }
                            tempConstructDetailList.add(constructDetail);
                            nodeId = UUIDGenerator.uuid();
                            releaseDetail = new ReleaseDetail();
                            releaseDetail.setName(constructDetail.getComponentAliasAndVersion());
                            releaseDetail.setNodeId(nodeId);
                            releaseDetail.setParentNodeId(parentConstructDetailNodeId);
                            releaseDetail.setDataId("CD_" + constructDetail.getId());
                            releaseDetailList.add(releaseDetail);
                            releaseDetailList.addAll(getReleaseDetailsOfConstructDetail(constructDetail, nodeId, map, reserveZoneIdSet, constructDetailIdSet));
                        }
                    }
                    if (checkedAll) {
                        for (ConstructDetail constructDetail : constructDetailList) {
                            if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                                continue;
                            }
                            if (releaseUpdatePackage) {
                                List<ConstructDetail> tempConstructDetailList = constructDetailMap.get(construct.getId());
                                if (tempConstructDetailList == null) {
                                    tempConstructDetailList = new ArrayList<ConstructDetail>();
                                    constructDetailMap.put(construct.getId(), tempConstructDetailList);
                                }
                                tempConstructDetailList.add(constructDetail);
                            }
                            nodeId = UUIDGenerator.uuid();
                            releaseDetail = new ReleaseDetail();
                            releaseDetail.setName(constructDetail.getComponentAliasAndVersion());
                            releaseDetail.setNodeId(nodeId);
                            releaseDetail.setParentNodeId(parentConstructDetailNodeId);
                            releaseDetail.setDataId("CD_" + constructDetail.getId());
                            releaseDetailList.add(releaseDetail);
                            releaseDetailList.addAll(getReleaseDetailsOfConstructDetail(constructDetail, nodeId, map, reserveZoneIdSet, constructDetailIdSet));
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
                    // 手动选择部分节点，还是默认选择全部
                    boolean checkedAll = true;
                    for (ComponentReserveZone reserveZone : componentReserveZoneList) {
                        if (releaseUpdatePackage && reserveZoneIdSet.contains(reserveZone.getId() + "_" + construct.getId())) {
                            checkedAll = false;
                            if (getService(ConstructDetailService.class).isBindingComponent(construct.getId(), reserveZone.getId())) {
                                nodeId = UUIDGenerator.uuid();
                                releaseDetail = new ReleaseDetail();
                                releaseDetail.setName(reserveZone.getAlias());
                                releaseDetail.setNodeId(nodeId);
                                releaseDetail.setParentNodeId(parentConstructDetailNodeId);
                                releaseDetail.setDataId("RZ_" + reserveZone.getId());
                                releaseDetailList.add(releaseDetail);
                                releaseDetailList.addAll(getReleaseDetailsOfReserveZone(construct, reserveZone, nodeId, map, reserveZoneIdSet,
                                        constructDetailIdSet));
                            }
                        }
                    }
                    if (checkedAll) {
                        for (ComponentReserveZone reserveZone : componentReserveZoneList) {
                            if (getService(ConstructDetailService.class).isBindingComponent(construct.getId(), reserveZone.getId())) {
                                nodeId = UUIDGenerator.uuid();
                                releaseDetail = new ReleaseDetail();
                                releaseDetail.setName(reserveZone.getAlias());
                                releaseDetail.setNodeId(nodeId);
                                releaseDetail.setParentNodeId(parentConstructDetailNodeId);
                                releaseDetail.setDataId("RZ_" + reserveZone.getId());
                                releaseDetailList.add(releaseDetail);
                                releaseDetailList.addAll(getReleaseDetailsOfReserveZone(construct, reserveZone, nodeId, map, reserveZoneIdSet,
                                        constructDetailIdSet));
                            }
                        }
                    }
                }
            }
        }
        return releaseDetailList;
    }

    /**
     * 获取组合构件预留区上的发布详情
     * 
     * @param construct 组合构件
     * @param reserveZone 预留区
     * @param reserveZoneNodeId 预留区树节点ID
     * @param map 更新包信息
     * @param reserveZoneIdSet 预留区IDs
     * @param constructDetailIdSet 构件绑定关系IDs
     * @return List<ReleaseDetail>
     */
    @SuppressWarnings("unchecked")
    private List<ReleaseDetail> getReleaseDetailsOfReserveZone(Construct construct, ComponentReserveZone reserveZone, String reserveZoneNodeId,
            Map<String, Object> map, Set<String> reserveZoneIdSet, Set<String> constructDetailIdSet) {
        boolean releaseUpdatePackage = false;
        Map<String, List<ConstructDetail>> constructDetailMap = null;
        if (map != null) {
            releaseUpdatePackage = true;
            constructDetailMap = (Map<String, List<ConstructDetail>>) map.get("ConstructDetail");
        }
        List<ReleaseDetail> releaseDetailList = new ArrayList<ReleaseDetail>();
        ReleaseDetail releaseDetail = null;
        String nodeId = null;
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByConstructIdAndReserveZoneId(construct.getId(),
                reserveZone.getId());
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            ComponentVersion bindingComponentVersion = null;
            // 手动选择部分节点，还是默认选择全部
            boolean checkedAll = true;
            for (ConstructDetail constructDetail : constructDetailList) {
                if (releaseUpdatePackage && constructDetailIdSet.contains(constructDetail.getId())) {
                    if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                        continue;
                    }
                    checkedAll = false;
                    List<ConstructDetail> tempConstructDetailList = constructDetailMap.get(construct.getId());
                    if (tempConstructDetailList == null) {
                        tempConstructDetailList = new ArrayList<ConstructDetail>();
                        constructDetailMap.put(construct.getId(), tempConstructDetailList);
                    }
                    tempConstructDetailList.add(constructDetail);
                    bindingComponentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                    nodeId = UUIDGenerator.uuid();
                    releaseDetail = new ReleaseDetail();
                    releaseDetail.setName(bindingComponentVersion.getComponent().getAlias() + "_" + bindingComponentVersion.getVersion());
                    releaseDetail.setNodeId(nodeId);
                    releaseDetail.setParentNodeId(reserveZoneNodeId);
                    releaseDetail.setDataId("CD_" + constructDetail.getId());
                    releaseDetailList.add(releaseDetail);
                    releaseDetailList.addAll(getReleaseDetailsOfConstructDetail(constructDetail, nodeId, map, reserveZoneIdSet, constructDetailIdSet));
                }
            }
            if (checkedAll) {
                for (ConstructDetail constructDetail : constructDetailList) {
                    if (StringUtil.isEmpty(constructDetail.getComponentVersionId())) {
                        continue;
                    }
                    if (releaseUpdatePackage) {
                        List<ConstructDetail> tempConstructDetailList = constructDetailMap.get(construct.getId());
                        if (tempConstructDetailList == null) {
                            tempConstructDetailList = new ArrayList<ConstructDetail>();
                            constructDetailMap.put(construct.getId(), tempConstructDetailList);
                        }
                        tempConstructDetailList.add(constructDetail);
                    }
                    bindingComponentVersion = getService(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                    nodeId = UUIDGenerator.uuid();
                    releaseDetail = new ReleaseDetail();
                    releaseDetail.setName(bindingComponentVersion.getComponent().getAlias() + "_" + bindingComponentVersion.getVersion());
                    releaseDetail.setNodeId(nodeId);
                    releaseDetail.setParentNodeId(reserveZoneNodeId);
                    releaseDetail.setDataId("CD_" + constructDetail.getId());
                    releaseDetailList.add(releaseDetail);
                    releaseDetailList.addAll(getReleaseDetailsOfConstructDetail(constructDetail, nodeId, map, reserveZoneIdSet, constructDetailIdSet));
                }
            }
        }
        return releaseDetailList;
    }

    /**
     * 保存系统更新包发布信息
     * 
     * @param entity 系统发布信息
     * @param menuIds 菜单Ids
     * @param usedButtonMap 选择的按钮Map<menuId, List<constructDetailId>>
     * @return Release
     */
    @Transactional
    public Release saveReleaseUpdatePackage(Release entity, List<ReleaseDetail> releaseDetailList) {
        if (StringUtil.isNotEmpty(entity.getId())) {
            getDaoFromContext(ReleaseDetailDao.class).deleteBySystemReleaseId(entity.getId());
        }
        Release release = super.save(entity);
        if (CollectionUtils.isNotEmpty(releaseDetailList)) {
            for (ReleaseDetail temp : releaseDetailList) {
                temp.setRootMenuId(release.getRootMenuId());
                temp.setSystemReleaseId(release.getId());
            }
            getDaoFromContext(ReleaseDetailDao.class).save(releaseDetailList);
        }
        return release;
    }

    /**
     * 根据根菜单ID获取系统发布
     * 
     * @param rootMenuId 根菜单ID
     * @param version 版本号
     * @return Release
     */
    public Release getByRootMenuIdAndVersion(String rootMenuId, String version) {
        return getDao().getByRootMenuIdAndVersion(rootMenuId, version);
    }

    /**
     * 根据根菜单ID和类型获取系统发布
     * 
     * @param rootMenuId 根菜单ID
     * @param type 类型
     * @return List<Release>
     */
    public List<Release> getByRootMenuIdAndType(String rootMenuId, String type) {
        return getDao().getByRootMenuIdAndType(rootMenuId, type);
    }
}

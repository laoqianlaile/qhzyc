package com.ces.config.dhtmlx.service.toolmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.toolmanage.ToolManageDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 工具管理Service
 * 
 * @author wanglei
 * @date 2015-08-28
 */
@Component("toolManageService")
public class ToolManageService extends ConfigDefineDaoService<StringIDEntity, ToolManageDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("toolManageDao")
    @Override
    protected void setDaoUnBinding(ToolManageDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 将“表单工具条”改成“表单_上工具条”（历史数据更改）
     * 
     * @return String
     */
    @Transactional
    public String changeFormTbToTopTb() {
        String msg = "修改成功！";
        try {
            List<ComponentReserveZone> reserveZoneList = getOldSelfDefineFormToolbar();
            if (CollectionUtils.isNotEmpty(reserveZoneList)) {
                for (ComponentReserveZone reserveZone : reserveZoneList) {
                    if (reserveZone.getAlias().indexOf("表单_工具条") != -1) {
                        reserveZone.setAlias(reserveZone.getAlias().replace("表单_工具条", "表单_上工具条"));
                    } else {
                        reserveZone.setAlias(reserveZone.getAlias().replace("表单", "表单_上工具条"));
                    }
                    reserveZone.setName(reserveZone.getName() + "_0");
                }
            }
            getService(ComponentReserveZoneService.class).save(reserveZoneList);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "修改失败！";
        }
        return msg;
    }

    /**
     * 将“表单工具条”改成“表单_下工具条”（历史数据更改）
     * 
     * @return String
     */
    @Transactional
    public String changeFormTbToBottomTb() {
        String msg = "修改成功！";
        try {
            List<ComponentReserveZone> reserveZoneList = getOldSelfDefineFormToolbar();
            if (CollectionUtils.isNotEmpty(reserveZoneList)) {
                for (ComponentReserveZone reserveZone : reserveZoneList) {
                    if (reserveZone.getAlias().indexOf("表单_工具条") != -1) {
                        reserveZone.setAlias(reserveZone.getAlias().replace("表单_工具条", "表单_下工具条"));
                    } else {
                        reserveZone.setAlias(reserveZone.getAlias().replace("表单", "表单_下工具条"));
                    }
                    reserveZone.setName(reserveZone.getName() + "_1");
                }
            }
            getService(ComponentReserveZoneService.class).save(reserveZoneList);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "修改失败！";
        }
        return msg;
    }

    /**
     * 获取所有表单工具条（老数据）
     * 
     * @return List<ComponentReserveZone>
     */
    private List<ComponentReserveZone> getOldSelfDefineFormToolbar() {
        String hql = "from ComponentReserveZone rz where rz.type='0' and rz.page='MT_page_' and (rz.alias like '%表单' or rz.alias like '%表单_工具条')";
        return DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentReserveZone.class);
    }

    /**
     * 获取所有列表工具条（老数据）
     * 
     * @return List<ComponentReserveZone>
     */
    private List<ComponentReserveZone> getSelfDefineGridToolbar() {
        String hql = "from ComponentReserveZone rz where rz.type='0' and rz.page='MT_page_' and (rz.alias like '%列表' or rz.alias like '%列表_工具条')";
        return DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentReserveZone.class);
    }

    /**
     * 生成所有的“表单_上工具条”（历史数据更改）
     * 
     * @return String
     */
    @Transactional
    public String createTopTb() {
        String msg = "新增成功！";
        try {
            // 列表工具条显示顺序，key为reserveZoneName，value为showOrder
            Map<String, Integer> gridToolbarMap = new HashMap<String, Integer>();
            List<ComponentReserveZone> selfDefineGridToolbarList = getSelfDefineGridToolbar();
            if (CollectionUtils.isNotEmpty(selfDefineGridToolbarList)) {
                for (ComponentReserveZone reserveZone : selfDefineGridToolbarList) {
                    gridToolbarMap.put(reserveZone.getName(), reserveZone.getShowOrder());
                }
            }
            // 获取已经存在的表单上工具条
            Set<String> reserveZoneNameSet = new HashSet<String>();
            List<ComponentReserveZone> selfDefineFormTopToolbarList = getSelfDefineFormTopToolbar();
            if (CollectionUtils.isNotEmpty(selfDefineFormTopToolbarList)) {
                for (ComponentReserveZone reserveZone : selfDefineFormTopToolbarList) {
                    reserveZoneNameSet.add(reserveZone.getName());
                }
            }
            List<ComponentReserveZone> newReserveZoneList = new ArrayList<ComponentReserveZone>();
            ComponentReserveZone newReserveZone = null;
            // 获取物理表构件
            String physicalTableModuleHql = "from Module m where m.type='4'";
            List<Module> physicalTableModuleList = DatabaseHandlerDao.getInstance().queryEntityForList(physicalTableModuleHql, Module.class);
            if (CollectionUtils.isNotEmpty(physicalTableModuleList)) {
                for (Module module : physicalTableModuleList) {
                    if (StringUtil.isNotEmpty(module.getTable1Id())) {
                        newReserveZone = getSelfDefineFormReserveZone(reserveZoneNameSet, gridToolbarMap, module.getComponentVersionId(), module.getName(),
                                module.getTable1Id(), 1, "0");
                        if (newReserveZone != null) {
                            newReserveZoneList.add(newReserveZone);
                        }
                    }
                    if (StringUtil.isNotEmpty(module.getTable1Id()) && StringUtil.isNotEmpty(module.getTable2Id())
                            && !module.getTable2Id().equals(module.getTable1Id())) {
                        newReserveZone = getSelfDefineFormReserveZone(reserveZoneNameSet, gridToolbarMap, module.getComponentVersionId(), module.getName(),
                                module.getTable2Id(), 2, "0");
                        if (newReserveZone != null) {
                            newReserveZoneList.add(newReserveZone);
                        }
                    }
                    if (StringUtil.isNotEmpty(module.getTable2Id()) && StringUtil.isNotEmpty(module.getTable3Id())
                            && !module.getTable3Id().equals(module.getTable2Id())) {
                        newReserveZone = getSelfDefineFormReserveZone(reserveZoneNameSet, gridToolbarMap, module.getComponentVersionId(), module.getName(),
                                module.getTable3Id(), 3, "0");
                        if (newReserveZone != null) {
                            newReserveZoneList.add(newReserveZone);
                        }
                    }
                }
            }
            // 获取通用表构件
            String noTableModuleHql = "from Module m where m.type='6'";
            List<Module> noTableModuleList = DatabaseHandlerDao.getInstance().queryEntityForList(noTableModuleHql, Module.class);
            if (CollectionUtils.isNotEmpty(noTableModuleList)) {
                for (Module module : noTableModuleList) {
                    newReserveZone = getSelfDefineFormReserveZone(reserveZoneNameSet, gridToolbarMap, module.getComponentVersionId(), module.getName(), "", 1,
                            "0");
                    if (newReserveZone != null) {
                        newReserveZoneList.add(newReserveZone);
                    }
                }
            }
            // 公用预留区
            List<LogicTableDefine> logicTableDefineList = getService(LogicTableDefineService.class).getAllLogicTableDefine();
            if (CollectionUtils.isNotEmpty(logicTableDefineList)) {
                for (LogicTableDefine logicTable : logicTableDefineList) {
                    if ("_ASSIST_OPINION_".equals(logicTable.getId()) || "_CONFIRM_OPINION_".equals(logicTable.getId())) {
                        continue;
                    }
                    String reserveZoneName = logicTable.getCode() + "_FORM_0";
                    if (reserveZoneNameSet.contains(reserveZoneName)) {
                        continue;
                    }
                    String gridReserveZoneName = logicTable.getCode() + "_GRID";
                    Integer showOrder = gridToolbarMap.get(gridReserveZoneName);
                    if (showOrder == null) {
                        showOrder = getService(ComponentReserveZoneService.class).getCommonMaxShowOrder();
                    } else {
                        showOrder = showOrder + 1;
                    }
                    newReserveZone = new ComponentReserveZone();
                    newReserveZone.setName(reserveZoneName);
                    newReserveZone.setAlias(logicTable.getShowName() + "_表单_上工具条");
                    newReserveZone.setType("0");
                    newReserveZone.setPage(AppDefineUtil.getPage(0));
                    newReserveZone.setIsCommon(true);
                    newReserveZone.setShowOrder(showOrder);
                    newReserveZoneList.add(newReserveZone);
                }
            }
            if (CollectionUtils.isNotEmpty(newReserveZoneList)) {
                getService(ComponentReserveZoneService.class).save(newReserveZoneList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "新增失败！";
        }
        return msg;
    }

    /**
     * 获取所有表单上工具条
     * 
     * @return List<ComponentReserveZone>
     */
    private List<ComponentReserveZone> getSelfDefineFormTopToolbar() {
        String hql = "from ComponentReserveZone rz where rz.type='0' and rz.page='MT_page_' and rz.alias like '%表单_上工具条'";
        return DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentReserveZone.class);
    }

    /**
     * 获取物理表构件表单工具条
     * 
     * @return ComponentReserveZone
     */
    private ComponentReserveZone getSelfDefineFormReserveZone(Set<String> reserveZoneNameSet, Map<String, Integer> gridToolbarMap, String versionId,
            String text, String tableId, int areaIndex, String position) {
        String reserveZoneName = AppDefineUtil.getZoneName(tableId, AppDefineUtil.L_FORM, areaIndex, null, position);
        if (reserveZoneNameSet.contains(reserveZoneName)) {
            return null;
        }
        String gridReserveZoneName = AppDefineUtil.getZoneName(tableId, AppDefineUtil.L_GRID, areaIndex, null, null);
        Integer showOrder = gridToolbarMap.get(gridReserveZoneName);
        if (showOrder == null) {
            gridReserveZoneName = AppDefineUtil.getZoneName(tableId, AppDefineUtil.L_GRID, --areaIndex, null, null);
            showOrder = gridToolbarMap.get(gridReserveZoneName);
            if (showOrder == null) {
                showOrder = getService(ComponentReserveZoneService.class).getMaxShowOrder(versionId);
            } else {
                showOrder = showOrder + 2;
            }
        } else {
            showOrder = showOrder + 2;
        }
        ComponentReserveZone formReserveZone = new ComponentReserveZone();
        formReserveZone.setComponentVersionId(versionId);
        formReserveZone.setName(reserveZoneName);
        formReserveZone.setPage(AppDefineUtil.getPage(areaIndex));
        formReserveZone.setAlias(AppDefineUtil.getZoneAlias(text, AppDefineUtil.L_FORM, areaIndex, null, position));
        formReserveZone.setType("0");
        formReserveZone.setIsCommon(false);
        formReserveZone.setShowOrder(showOrder);
        return formReserveZone;
    }

    /**
     * 生成所有的“表单_下工具条”（历史数据更改）
     * 
     * @return String
     */
    @Transactional
    public String createBottomTb() {
        String msg = "修改成功！";
        try {
            // 列表工具条显示顺序，key为reserveZoneName，value为showOrder
            Map<String, Integer> gridToolbarMap = new HashMap<String, Integer>();
            List<ComponentReserveZone> selfDefineGridToolbarList = getSelfDefineGridToolbar();
            if (CollectionUtils.isNotEmpty(selfDefineGridToolbarList)) {
                for (ComponentReserveZone reserveZone : selfDefineGridToolbarList) {
                    gridToolbarMap.put(reserveZone.getName(), reserveZone.getShowOrder());
                }
            }
            // 获取已经存在的表单下工具条
            Set<String> reserveZoneNameSet = new HashSet<String>();
            List<ComponentReserveZone> selfDefineFormBottomToolbarList = getSelfDefineFormBottomToolbar();
            if (CollectionUtils.isNotEmpty(selfDefineFormBottomToolbarList)) {
                for (ComponentReserveZone reserveZone : selfDefineFormBottomToolbarList) {
                    reserveZoneNameSet.add(reserveZone.getName());
                }
            }
            List<ComponentReserveZone> newReserveZoneList = new ArrayList<ComponentReserveZone>();
            ComponentReserveZone newReserveZone = null;
            // 获取物理表构件
            String physicalTableModuleHql = "from Module m where m.type='4'";
            List<Module> physicalTableModuleList = DatabaseHandlerDao.getInstance().queryEntityForList(physicalTableModuleHql, Module.class);
            if (CollectionUtils.isNotEmpty(physicalTableModuleList)) {
                for (Module module : physicalTableModuleList) {
                    if (StringUtil.isNotEmpty(module.getTable1Id())) {
                        newReserveZone = getSelfDefineFormReserveZone(reserveZoneNameSet, gridToolbarMap, module.getComponentVersionId(), module.getName(),
                                module.getTable1Id(), 1, "1");
                        if (newReserveZone != null) {
                            newReserveZoneList.add(newReserveZone);
                        }
                    }
                    if (StringUtil.isNotEmpty(module.getTable1Id()) && StringUtil.isNotEmpty(module.getTable2Id())
                            && !module.getTable2Id().equals(module.getTable1Id())) {
                        newReserveZone = getSelfDefineFormReserveZone(reserveZoneNameSet, gridToolbarMap, module.getComponentVersionId(), module.getName(),
                                module.getTable2Id(), 2, "1");
                        if (newReserveZone != null) {
                            newReserveZoneList.add(newReserveZone);
                        }
                    }
                    if (StringUtil.isNotEmpty(module.getTable2Id()) && StringUtil.isNotEmpty(module.getTable3Id())
                            && !module.getTable3Id().equals(module.getTable2Id())) {
                        newReserveZone = getSelfDefineFormReserveZone(reserveZoneNameSet, gridToolbarMap, module.getComponentVersionId(), module.getName(),
                                module.getTable3Id(), 3, "1");
                        if (newReserveZone != null) {
                            newReserveZoneList.add(newReserveZone);
                        }
                    }
                }
            }
            // 获取通用表构件
            String noTableModuleHql = "from Module m where m.type='6'";
            List<Module> noTableModuleList = DatabaseHandlerDao.getInstance().queryEntityForList(noTableModuleHql, Module.class);
            if (CollectionUtils.isNotEmpty(noTableModuleList)) {
                for (Module module : noTableModuleList) {
                    newReserveZone = getSelfDefineFormReserveZone(reserveZoneNameSet, gridToolbarMap, module.getComponentVersionId(), module.getName(), "", 1,
                            "1");
                    if (newReserveZone != null) {
                        newReserveZoneList.add(newReserveZone);
                    }
                }
            }
            // 公用预留区
            List<LogicTableDefine> logicTableDefineList = getService(LogicTableDefineService.class).getAllLogicTableDefine();
            if (CollectionUtils.isNotEmpty(logicTableDefineList)) {
                for (LogicTableDefine logicTable : logicTableDefineList) {
                    if ("_ASSIST_OPINION_".equals(logicTable.getId()) || "_CONFIRM_OPINION_".equals(logicTable.getId())) {
                        continue;
                    }
                    String reserveZoneName = logicTable.getCode() + "_FORM_1";
                    if (reserveZoneNameSet.contains(reserveZoneName)) {
                        continue;
                    }
                    String gridReserveZoneName = logicTable.getCode() + "_GRID";
                    Integer showOrder = gridToolbarMap.get(gridReserveZoneName);
                    if (showOrder == null) {
                        showOrder = getService(ComponentReserveZoneService.class).getCommonMaxShowOrder();
                    } else {
                        showOrder = showOrder + 1;
                    }
                    newReserveZone = new ComponentReserveZone();
                    newReserveZone.setName(reserveZoneName);
                    newReserveZone.setAlias(logicTable.getShowName() + "_表单_下工具条");
                    newReserveZone.setType("0");
                    newReserveZone.setPage(AppDefineUtil.getPage(0));
                    newReserveZone.setIsCommon(true);
                    newReserveZone.setShowOrder(showOrder);
                    newReserveZoneList.add(newReserveZone);
                }
            }
            if (CollectionUtils.isNotEmpty(newReserveZoneList)) {
                getService(ComponentReserveZoneService.class).save(newReserveZoneList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "新增失败！";
        }
        return msg;
    }

    /**
     * 获取所有表单下工具条
     * 
     * @return List<ComponentReserveZone>
     */
    private List<ComponentReserveZone> getSelfDefineFormBottomToolbar() {
        String hql = "from ComponentReserveZone rz where rz.type='0' and rz.page='MT_page_' and rz.alias like '%表单_下工具条'";
        return DatabaseHandlerDao.getInstance().queryEntityForList(hql, ComponentReserveZone.class);
    }

    /**
     * 将所有“表单_上工具条”上的按钮复制到“表单_下工具条”
     * 
     * @return String
     */
    @Transactional
    public String copyButtonFormTopTb() {
        String msg = "复制成功！";
        try {
            // 删除表单下工具条上的按钮
            deleteButtonOfBottomTb();
            // 表单上工具条Map，key为reserveZoneId，value为reserveZoneName
            Map<String, String> topReserveZoneMap = new HashMap<String, String>();
            List<ComponentReserveZone> selfDefineFormTopToolbarList = getSelfDefineFormTopToolbar();
            if (CollectionUtils.isNotEmpty(selfDefineFormTopToolbarList)) {
                for (ComponentReserveZone reserveZone : selfDefineFormTopToolbarList) {
                    topReserveZoneMap.put(reserveZone.getId(), reserveZone.getName());
                }
            }
            // 表单下工具条Map，key为reserveZoneName，value为reserveZoneId
            Map<String, String> bottomReserveZoneMap = new HashMap<String, String>();
            List<ComponentReserveZone> selfDefineFormBottomToolbarList = getSelfDefineFormBottomToolbar();
            if (CollectionUtils.isNotEmpty(selfDefineFormBottomToolbarList)) {
                for (ComponentReserveZone reserveZone : selfDefineFormBottomToolbarList) {
                    bottomReserveZoneMap.put(reserveZone.getName(), reserveZone.getId());
                }
            }
            List<ConstructDetail> newCDList = new ArrayList<ConstructDetail>();
            if (MapUtils.isNotEmpty(topReserveZoneMap) && MapUtils.isNotEmpty(bottomReserveZoneMap)) {
                List<ConstructDetail> cdList = null;
                ConstructDetail distCD = null;
                String topReserveZoneName = null;
                String bottomReserveZoneId = null;
                String bottomReserveZoneName = null;
                for (String reserveZoneId : topReserveZoneMap.keySet()) {
                    cdList = getService(ConstructDetailService.class).getByReserveZoneId(reserveZoneId);
                    if (CollectionUtils.isNotEmpty(cdList)) {
                        for (ConstructDetail cd : cdList) {
                            topReserveZoneName = topReserveZoneMap.get(reserveZoneId);
                            if (StringUtil.isNotEmpty(topReserveZoneName)) {
                                bottomReserveZoneName = topReserveZoneName.substring(0, topReserveZoneName.length() - 1) + "1";
                                bottomReserveZoneId = bottomReserveZoneMap.get(bottomReserveZoneName);
                            }
                            if (StringUtil.isNotEmpty(bottomReserveZoneId)) {
                                distCD = new ConstructDetail();
                                BeanUtils.copy(cd, distCD);
                                distCD.setId(null);
                                distCD.setReserveZoneId(bottomReserveZoneId);
                                newCDList.add(distCD);
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(newCDList)) {
                getService(ConstructDetailService.class).save(newCDList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "复制失败！";
        }
        return msg;
    }

    /**
     * 将所有“表单_下工具条”上的按钮复制到“表单_上工具条”
     * 
     * @return String
     */
    @Transactional
    public String copyButtonFormBottomTb() {
        String msg = "复制成功！";
        try {
            // 删除表单上工具条上的按钮
            deleteButtonOfTopTb();
            // 表单下工具条Map，key为reserveZoneId，value为reserveZoneName
            Map<String, String> bottomReserveZoneMap = new HashMap<String, String>();
            List<ComponentReserveZone> selfDefineFormBottomToolbarList = getSelfDefineFormBottomToolbar();
            if (CollectionUtils.isNotEmpty(selfDefineFormBottomToolbarList)) {
                for (ComponentReserveZone reserveZone : selfDefineFormBottomToolbarList) {
                    bottomReserveZoneMap.put(reserveZone.getId(), reserveZone.getName());
                }
            }
            // 表单下工具条Map，key为reserveZoneName，value为reserveZoneId
            Map<String, String> topReserveZoneMap = new HashMap<String, String>();
            List<ComponentReserveZone> selfDefineFormTopToolbarList = getSelfDefineFormTopToolbar();
            if (CollectionUtils.isNotEmpty(selfDefineFormTopToolbarList)) {
                for (ComponentReserveZone reserveZone : selfDefineFormTopToolbarList) {
                    topReserveZoneMap.put(reserveZone.getName(), reserveZone.getId());
                }
            }
            List<ConstructDetail> newCDList = new ArrayList<ConstructDetail>();
            if (MapUtils.isNotEmpty(bottomReserveZoneMap) && MapUtils.isNotEmpty(topReserveZoneMap)) {
                List<ConstructDetail> cdList = null;
                ConstructDetail distCD = null;
                String bottomReserveZoneName = null;
                String topReserveZoneId = null;
                String topReserveZoneName = null;
                for (String reserveZoneId : bottomReserveZoneMap.keySet()) {
                    cdList = getService(ConstructDetailService.class).getByReserveZoneId(reserveZoneId);
                    if (CollectionUtils.isNotEmpty(cdList)) {
                        for (ConstructDetail cd : cdList) {
                            bottomReserveZoneName = bottomReserveZoneMap.get(reserveZoneId);
                            if (StringUtil.isNotEmpty(bottomReserveZoneName)) {
                                topReserveZoneName = bottomReserveZoneName.substring(0, bottomReserveZoneName.length() - 1) + "0";
                                topReserveZoneId = topReserveZoneMap.get(topReserveZoneName);
                            }
                            if (StringUtil.isNotEmpty(topReserveZoneId)) {
                                distCD = new ConstructDetail();
                                BeanUtils.copy(cd, distCD);
                                distCD.setId(null);
                                distCD.setReserveZoneId(topReserveZoneId);
                                newCDList.add(distCD);
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(newCDList)) {
                getService(ConstructDetailService.class).save(newCDList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "复制失败！";
        }
        return msg;
    }

    /**
     * 删除所有“表单_上工具条”上的按钮
     * 
     * @return String
     */
    @Transactional
    public String deleteButtonOfTopTb() {
        String msg = "删除成功！";
        try {
            List<ComponentReserveZone> selfDefineFormTopToolbarList = getSelfDefineFormTopToolbar();
            if (CollectionUtils.isNotEmpty(selfDefineFormTopToolbarList)) {
                for (ComponentReserveZone reserveZone : selfDefineFormTopToolbarList) {
                    getService(ConstructDetailService.class).deleteByReserveZoneId(reserveZone.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "删除失败！";
        }
        return msg;
    }

    /**
     * 删除所有“表单_下工具条”上的按钮
     * 
     * @return String
     */
    @Transactional
    public String deleteButtonOfBottomTb() {
        String msg = "删除成功！";
        try {
            List<ComponentReserveZone> selfDefineFormBottomToolbarList = getSelfDefineFormBottomToolbar();
            if (CollectionUtils.isNotEmpty(selfDefineFormBottomToolbarList)) {
                for (ComponentReserveZone reserveZone : selfDefineFormBottomToolbarList) {
                    getService(ConstructDetailService.class).deleteByReserveZoneId(reserveZone.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "删除失败！";
        }
        return msg;
    }

    /**
     * 设置新增和修改按钮的表单显示类型 “弹出”或“嵌入”
     * 
     * @return String
     */
    @Transactional
    public String setFormAssembleType(String assembleType) {
        String msg = "设置成功！";
        try {
            String hql = "update t_xtpz_construct_detail cd set cd.assemble_type='" + assembleType
                    + "' where cd.button_code='create' or cd.button_code='update'";
            DatabaseHandlerDao.getInstance().executeSql(hql);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "设置失败！";
        }
        return msg;
    }
}

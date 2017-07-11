package com.ces.config.dhtmlx.action.authority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.authority.AuthorityDataDetailDao;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.TableClassification;
import com.ces.config.dhtmlx.entity.appmanage.TableTree;
import com.ces.config.dhtmlx.entity.authority.AuthorityData;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataDetail;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupRelationService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.TableClassificationService;
import com.ces.config.dhtmlx.service.appmanage.TableTreeService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.dhtmlx.service.authority.AuthorityDataDetailService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.EhcacheUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.config.utils.authority.AuthorityUtil;

/**
 * 数据权限详情Controller
 * 
 * @author wanglei
 * @date 2014-09-25
 */
public class AuthorityDataDetailController extends ConfigDefineServiceDaoController<AuthorityDataDetail, AuthorityDataDetailService, AuthorityDataDetailDao> {

    private static final long serialVersionUID = 1L;

    @Override
    protected void initModel() {
        setModel(new AuthorityDataDetail());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("authorityDataDetailService")
    protected void setService(AuthorityDataDetailService service) {
        super.setService(service);
    }

    /**
     * 获取菜单或构件相关的表树
     * 
     * @return Object
     */
    @SuppressWarnings("unused")
    public Object getTableTree() {
        String objectId = getParameter("objectId");
        String objectType = getParameter("objectType");
        String menuId = getParameter("menuId");
        String componentVersionId = getParameter("componentVersionId");
        // 1、获取所有的带表的基础构件（树构件、物理表构件）
        Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
        if (AuthorityData.DEFAULT_ID.equals(componentVersionId)) {
            Set<ComponentVersion> menuComponentVersionSet = getService(MenuService.class).getMenuComponentVersionByMenuId(menuId);
            if (CollectionUtils.isNotEmpty(menuComponentVersionSet)) {
                for (ComponentVersion componentVersion : menuComponentVersionSet) {
                    if (ConstantVar.Component.Type.TREE.equals(componentVersion.getComponent().getType())
                            || ConstantVar.Component.Type.PHYSICAL_TABLE.equals(componentVersion.getComponent().getType())) {
                        componentVersionSet.add(componentVersion);
                    }
                }
            }
        } else {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(
                        getService(ConstructService.class).getBaseComponentVersionId(componentVersionId));
                if (ConstantVar.Component.Type.TREE.equals(baseComponentVersion.getComponent().getType())
                        || ConstantVar.Component.Type.PHYSICAL_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                    componentVersionSet.add(baseComponentVersion);
                } else if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())
                        || ConstantVar.Component.Type.NO_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                    // 逻辑表和通用表构件用菜单的
                    Set<ComponentVersion> menuComponentVersionSet = getService(MenuService.class).getMenuComponentVersionByMenuId(menuId);
                    if (CollectionUtils.isNotEmpty(menuComponentVersionSet)) {
                        for (ComponentVersion menuComponentVersion : menuComponentVersionSet) {
                            if (menuComponentVersion != null && ConstantVar.Component.Type.TREE.equals(menuComponentVersion.getComponent().getType())
                                    || ConstantVar.Component.Type.PHYSICAL_TABLE.equals(menuComponentVersion.getComponent().getType())) {
                                componentVersionSet.add(menuComponentVersion);
                            }
                        }
                    }
                }
            }
        }
        // 2、获取所有的表
        Set<PhysicalTableDefine> tableSet = new HashSet<PhysicalTableDefine>();
        if (CollectionUtils.isNotEmpty(componentVersionSet)) {
            for (ComponentVersion componentVersion : componentVersionSet) {
                if (ConstantVar.Component.Type.TREE.equals(componentVersion.getComponent().getType())) {
                    Module module = getService(ModuleService.class).findByComponentVersionId(componentVersion.getId());
                    tableSet.addAll(getService(TreeDefineService.class).getAllPhysicalTableSet(module.getTreeId()));
                } else if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(componentVersion.getComponent().getType())) {
                    Module module = getService(ModuleService.class).findByComponentVersionId(componentVersion.getId());
                    if (StringUtil.isNotEmpty(module.getTable1Id())) {
                        tableSet.add(getService(PhysicalTableDefineService.class).getByID(module.getTable1Id()));
                    }
                    if (StringUtil.isNotEmpty(module.getTable2Id())) {
                        tableSet.add(getService(PhysicalTableDefineService.class).getByID(module.getTable2Id()));
                    }
                    if (StringUtil.isNotEmpty(module.getTable3Id())) {
                        tableSet.add(getService(PhysicalTableDefineService.class).getByID(module.getTable3Id()));
                    }
                }
            }
        }
        // 3、过滤掉已经配置过的表
        /*
         * List<AuthorityData> authorityDataList = getService(AuthorityDataService.class).getAuthorityDataList(objectId,
         * objectType, menuId, componentVersionId);
         * if (CollectionUtils.isNotEmpty(authorityDataList)) {
         * Set<String> tableIdSet = new HashSet<String>();
         * for (AuthorityData authorityData : authorityDataList) {
         * tableIdSet.add(authorityData.getTableId());
         * }
         * for (Iterator<PhysicalTableDefine> it = tableSet.iterator(); it.hasNext();) {
         * if (tableIdSet.contains(it.next().getId())) {
         * it.remove();
         * }
         * }
         * }
         */
        // 4、获取预置的分类节点
        List<String> tableTreeIdList = new ArrayList<String>();
        List<TableClassification> classificationList = getService(TableClassificationService.class).findAll();
        if (CollectionUtils.isNotEmpty(classificationList)) {
            for (TableClassification tableClassification : classificationList) {
                if (StringUtil.isNotEmpty(tableClassification.getPrefix())) {
                    tableTreeIdList.add("_" + tableClassification.getCode());
                }
            }
        }
        tableTreeIdList.add("_V");
        // 5、获取表的分类节点下有多少节点
        Map<String, Set<Object>> tableMap = new HashMap<String, Set<Object>>();
        if (CollectionUtils.isNotEmpty(tableSet)) {
            for (PhysicalTableDefine tableDefine : tableSet) {
                if (tableDefine != null) {
                    Set<Object> tableTreeSet = tableMap.get(tableDefine.getTableTreeId());
                    if (tableTreeSet == null) {
                        tableTreeSet = new HashSet<Object>();
                        tableMap.put(tableDefine.getTableTreeId(), tableTreeSet);
                    }
                    tableTreeSet.add(tableDefine);
                    TableTree tableTree = getService(TableTreeService.class).getByID(tableDefine.getTableTreeId());
                    if (tableTree != null) {
                        getTableClassifications(tableTree, tableTreeIdList, tableMap);
                    }
                }
            }
        }
        // 6、生成树json
        setReturnData(getTableTreeJson(getId(), null, "0", tableMap));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取表的分类节点
     * 
     * @param tableTree 物理表分类
     * @param tableTreeIdList 预置的分类节点
     * @param tableMap
     */
    private void getTableClassifications(TableTree tableTree, List<String> tableTreeIdList, Map<String, Set<Object>> tableMap) {
        if (!tableTreeIdList.contains(tableTree.getId())) {
            tableTreeIdList.add(tableTree.getId());
            Set<Object> tableTreeSet = tableMap.get(tableTree.getParentId());
            if (tableTreeSet == null) {
                tableTreeSet = new HashSet<Object>();
                tableMap.put(tableTree.getParentId(), tableTreeSet);
            }
            tableTreeSet.add(tableTree);
            TableTree parentTableTree = getService(TableTreeService.class).getByID(tableTree.getParentId());
            if (parentTableTree != null) {
                getTableClassifications(parentTableTree, tableTreeIdList, tableMap);
            }
        }
    }

    /**
     * 获取构件树JSON
     * 
     * @param treeNodeId 表树节点
     * @param text 表树节点名称
     * @param type 表树节点类型
     * @param physicalTableDefineMap
     * @return Map<String, Object>
     */
    private Map<String, Object> getTableTreeJson(String treeNodeId, String text, String type, Map<String, Set<Object>> physicalTableDefineMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", treeNodeId);
        if (StringUtil.isNotEmpty(text)) {
            map.put("text", text);
        }
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", StringUtil.null2zero(type));
        userdata.add(item);
        map.put("userdata", userdata);
        Set<Object> tableDefineSet = physicalTableDefineMap.get(treeNodeId);
        if (CollectionUtils.isNotEmpty(tableDefineSet)) {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            map.put("open", "1");
            map.put("item", list);
            for (Object obj : tableDefineSet) {
                if (obj instanceof PhysicalTableDefine) {
                    PhysicalTableDefine tableDefine = (PhysicalTableDefine) obj;
                    list.add(getTableTreeJson(tableDefine.getId(), tableDefine.getShowName(), "1", physicalTableDefineMap));
                } else if (obj instanceof TableTree) {
                    TableTree tableTree = (TableTree) obj;
                    list.add(getTableTreeJson(tableTree.getId(), tableTree.getName(), "0", physicalTableDefineMap));
                }
            }
        }
        return map;
    }

    /**
     * 根据数据权限ID和表ID获取数据权限详情
     * 
     * @return Object
     */
    public Object getDetailListOfTable() {
        String authorityDataId = getParameter("authorityDataId");
        String tableId = getParameter("tableId");
        setReturnData(getService().getByAuthorityDataIdAndTableId(authorityDataId, tableId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /***
     * 获取权限过滤数据模型
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object getAuthorityFilterModelInitDataModel() {
        Map<String, Object> map = (Map<String, Object>) EhcacheUtil.getCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId());
        if (map == null) {
            map = AuthorityUtil.getInstance().getAuthorityManager().getAuthorityDeptFilterModelData();
            EhcacheUtil.setCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId(), map);
        }
        String options = "[{value:\"\", text:\"\"}";
        for (Entry<String, Object> e : map.entrySet()) {
            options += ",{value:\"" + e.getKey() + "\"," + " text:\"" + e.getValue() + "\"}";
        }
        options += "]";
        setReturnData(options);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取控制条件表
     * 
     * @return Object
     */
    public Object getControlTables() {
        String tableId = getParameter("tableId");
        String physcalGroupId = null;
        try {
            physcalGroupId = getService(PhysicalGroupDefineService.class).getByTableId(tableId);
        } catch (Exception e) {
            // 老数据可能会出现一张物理表在多个物理表组中出现
            e.printStackTrace();
        }
        StringBuilder returnData = new StringBuilder();
        returnData.append("[");
        if (StringUtil.isNotEmpty(physcalGroupId)) {
            List<PhysicalGroupRelation> groupRelationList = getService(PhysicalGroupRelationService.class).findByGroupId(physcalGroupId);
            List<String> ctrlTableIdList = new ArrayList<String>();
            if (CollectionUtils.isNotEmpty(groupRelationList)) {
                for (PhysicalGroupRelation groupRelation : groupRelationList) {
                    ctrlTableIdList.add(groupRelation.getTableId());
                    if (tableId.equals(groupRelation.getTableId())) {
                        break;
                    }
                }
                for (int i = ctrlTableIdList.size() - 1; i >= 0; i--) {
                    String ctlTableId = ctrlTableIdList.get(i);
                    String ctlTableName = "";
                    PhysicalTableDefine tableEntity = TableUtil.getTableEntity(ctlTableId);
                    if (tableEntity != null) {
                        ctlTableName = tableEntity.getShowName();
                    }
                    returnData.append("{'id':'" + ctlTableId + "','name':'" + ctlTableName + "'}");
                    if (i != 0) {
                        returnData.append(",");
                    }
                }
            }
        } else {
            String tableName = "";
            PhysicalTableDefine tableEntity = TableUtil.getTableEntity(tableId);
            if (tableEntity != null) {
                tableName = tableEntity.getShowName();
            }
            returnData.append("{'id':'" + tableId + "','name':'" + tableName + "'}");
        }
        returnData.append("]");
        setReturnData(returnData.toString());
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}

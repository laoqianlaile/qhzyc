package com.ces.config.dhtmlx.service.authority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.authority.AuthorityTreeCopyDao;
import com.ces.config.dhtmlx.dao.authority.AuthorityTreeDataCopyDao;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.entity.authority.AuthorityApprove;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeCopy;
import com.ces.config.dhtmlx.entity.authority.AuthorityTreeDataCopy;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.AuthDatabaseUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.google.common.collect.Lists;

/**
 * 树权限Service（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
@Component
public class AuthorityTreeCopyService extends ConfigDefineDaoService<AuthorityTreeCopy, AuthorityTreeCopyDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityTreeCopyDao")
    @Override
    protected void setDaoUnBinding(AuthorityTreeCopyDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 删除树权限
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteAuthorityTree(String objectId, String objectType, String menuId, String componentVersionId) {
        // 如果AuthorityTree中存在对应记录，那么AuthorityApprove为“删除待审批”，如没有对应记录，直接删除
        List<String> authorityTreeNodeIds = getService(AuthorityTreeService.class).getAuthorityTreeNodeIds(objectId, objectType, menuId, componentVersionId);
        if (CollectionUtils.isNotEmpty(authorityTreeNodeIds)) {
            AuthorityApprove authorityApprove = getService(AuthorityApproveService.class).getTreeAuthorityApprove(objectId, objectType, menuId,
                    componentVersionId);
            if (authorityApprove == null) {
                authorityApprove = new AuthorityApprove();
                authorityApprove.setObjectId(objectId);
                authorityApprove.setObjectType(objectType);
                authorityApprove.setMenuId(menuId);
                authorityApprove.setComponentVersionId(componentVersionId);
                authorityApprove.setAuthorityType(ConstantVar.AuthorityApprove.Type.TREE);
            }
            authorityApprove.setOperate(ConstantVar.AuthorityApprove.Operate.DELETE);
            // 删除“菜单”下“构件”的树权限
            StringBuilder detail = new StringBuilder();
            Menu menu = getService(MenuService.class).getByID(menuId);
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            String componentName = componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion();
            detail.append("删除菜单“").append(menu.getName()).append("”下构件“").append(componentName).append("”的树权限。");
            authorityApprove.setDetail(detail.toString());
            authorityApprove.setStatus(ConstantVar.AuthorityApprove.Status.APPROVING);
            getService(AuthorityApproveService.class).save(authorityApprove);
            // 操作日志
            CommonUtil.addOperateLog("数据权限-三权分立", "删除树权限", detail.toString());
        } else {
            getDao().deleteAuthorityTree(objectId, objectType, menuId, componentVersionId);
            getService(AuthorityTreeDataCopyService.class).deleteAuthorityTreeData(objectId, objectType, menuId, componentVersionId);
        }
    }

    /**
     * 保存树权限配置
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param treeDefineIds 有权限的树节点
     */
    @Transactional
    public boolean saveAuthorityTree(String objectId, String objectType, String menuId, String componentVersionId, String treeDefineIds, String controlDataAuth) {
        boolean flag = true;
        try {
            // 删除旧的树权限
            getDao().deleteAuthorityTree(objectId, objectType, menuId, componentVersionId);
            // 保存新的树权限
            String[] treeDefineIdArray = treeDefineIds.split(",");
            Set<String> treeDefineIdSet = new HashSet<String>();
            if (treeDefineIdArray != null && treeDefineIdArray.length > 0) {
                List<AuthorityTreeCopy> authorityTreeCopyList = Lists.newArrayList();
                AuthorityTreeCopy authorityTreeCopy = null;
                for (String treeDefineId : treeDefineIdArray) {
                    authorityTreeCopy = new AuthorityTreeCopy();
                    authorityTreeCopy.setObjectId(objectId);
                    authorityTreeCopy.setObjectType(objectType);
                    authorityTreeCopy.setMenuId(menuId);
                    authorityTreeCopy.setComponentVersionId(componentVersionId);
                    authorityTreeCopy.setTreeNodeId(treeDefineId);
                    authorityTreeCopyList.add(authorityTreeCopy);
                    treeDefineIdSet.add(treeDefineId);
                }
                save(authorityTreeCopyList);
                getService(AuthorityTreeDataCopyService.class).saveAuthorityTreeData(objectId, objectType, menuId, componentVersionId, controlDataAuth);
            }
            saveAuthorityApprove(objectId, objectType, menuId, componentVersionId, treeDefineIdSet, controlDataAuth);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 保存权限审批
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param treeDefineIdSet 树权限节点
     * @param controlDataAuth 控制数据权限
     */
    @Transactional
    public void saveAuthorityApprove(String objectId, String objectType, String menuId, String componentVersionId, Set<String> treeDefineIdSet,
            String controlDataAuth) {
        // 如果AuthorityTree中存在对应记录，那么AuthorityApprove为“修改待审批”，如没有对应记录，为“新增待审批”
        List<String> authorityTreeNodeIds = getService(AuthorityTreeService.class).getAuthorityTreeNodeIds(objectId, objectType, menuId, componentVersionId);
        AuthorityApprove authorityApprove = getService(AuthorityApproveService.class).getTreeAuthorityApprove(objectId, objectType, menuId, componentVersionId);
        if (authorityApprove == null) {
            authorityApprove = new AuthorityApprove();
            authorityApprove.setObjectId(objectId);
            authorityApprove.setObjectType(objectType);
            authorityApprove.setMenuId(menuId);
            authorityApprove.setComponentVersionId(componentVersionId);
            authorityApprove.setAuthorityType(ConstantVar.AuthorityApprove.Type.TREE);
        }
        if (CollectionUtils.isEmpty(authorityTreeNodeIds)) {
            authorityApprove.setOperate(ConstantVar.AuthorityApprove.Operate.NEW);
        } else {
            authorityApprove.setOperate(ConstantVar.AuthorityApprove.Operate.UPDATE);
        }
        // 设置“菜单”下“构件”的树权限
        StringBuilder detail = new StringBuilder();
        Menu menu = getService(MenuService.class).getByID(menuId);
        ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
        String componentName = componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion();
        detail.append("设置菜单“").append(menu.getName()).append("”下构件“").append(componentName).append("”的树权限。");
        String baseComponentVersionId = componentVersionId;
        Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersionId);
        if (construct != null) {
            baseComponentVersionId = construct.getBaseComponentVersionId();
        }
        Module module = getService(ModuleService.class).findByComponentVersionId(baseComponentVersionId);
        if (module != null && StringUtil.isNotEmpty(module.getTreeId())) {
            TreeDefine root = getService(TreeDefineService.class).getByID(module.getTreeId());
            List<TreeDefine> treeDefineList = getService(TreeDefineService.class).getAllChildren(module.getTreeId(), null);
            Map<String, List<TreeDefine>> treeDefineMap = new HashMap<String, List<TreeDefine>>();
            List<TreeDefine> tempTreeDefineList = null;
            for (TreeDefine tempTreeDefine : treeDefineList) {
                if (!treeDefineIdSet.contains(tempTreeDefine.getId())) {
                    continue;
                }
                tempTreeDefineList = treeDefineMap.get(tempTreeDefine.getParentId());
                if (tempTreeDefineList == null) {
                    tempTreeDefineList = new ArrayList<TreeDefine>();
                    treeDefineMap.put(tempTreeDefine.getParentId(), tempTreeDefineList);
                }
                tempTreeDefineList.add(tempTreeDefine);
            }
            detail.append("树权限：").append(root.getName()).append(getTreeDefineDetail(root, treeDefineMap)).append("。");
            if ("1".equals(controlDataAuth)) {
                detail.append("树权限作为数据权限");
            }
        }
        authorityApprove.setDetail(detail.toString());
        authorityApprove.setStatus(ConstantVar.AuthorityApprove.Status.APPROVING);
        getService(AuthorityApproveService.class).save(authorityApprove);
        // 操作日志
        CommonUtil.addOperateLog("数据权限-三权分立", "设置树权限", detail.toString());
    }

    /**
     * 获取树权限详情
     * 
     * @param parentTreeDefine 父节点
     * @param treeDefineMap 所有有权限的树节点
     * @return String
     */
    private String getTreeDefineDetail(TreeDefine parentTreeDefine, Map<String, List<TreeDefine>> treeDefineMap) {
        StringBuilder sb = new StringBuilder();
        List<TreeDefine> list = treeDefineMap.get(parentTreeDefine.getId());
        if (CollectionUtils.isNotEmpty(list)) {
            sb.append(": [");
            TreeDefine treeDefine = null;
            for (int i = 0; i < list.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                treeDefine = list.get(i);
                sb.append(treeDefine.getName());
                sb.append(getTreeDefineDetail(treeDefine, treeDefineMap));
            }
            sb.append("]");
        }
        return sb.toString();
    }

    /**
     * 获取树权限treeNodeIds
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<String>
     */
    public List<String> getAuthorityTreeNodeIds(String objectId, String objectType, String menuId, String componentVersionId) {
        return getDao().getAuthorityTreeNodeIds(objectId, objectType, menuId, componentVersionId);
    }

    /**
     * 获取树权限
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<AuthorityTreeCopy>
     */
    public List<AuthorityTreeCopy> getAuthorityTreeList(String objectId, String objectType, String menuId, String componentVersionId) {
        return getDao().getAuthorityTreeList(objectId, objectType, menuId, componentVersionId);
    }

    /**
     * 树权限复制到其他角色或用户
     * 
     * @param roleIds 角色IDs
     * @param userIds 用户IDs
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param treeDefineIds 树节点ID
     */
    @Transactional
    public void copyAuthorityTree(String roleIds, String userIds, String objectId, String objectType, String menuId, String componentVersionId,
            String treeDefineIds) {
        AuthorityTreeCopy authorityTreeCopy = null;
        List<AuthorityTreeCopy> authorityTreeCopyList = null;
        AuthorityTreeDataCopy sourceAuthorityTreeDataCopy = getDaoFromContext(AuthorityTreeDataCopyDao.class).getAuthorityTreeData(objectId, objectType,
                menuId, componentVersionId);
        AuthorityTreeDataCopy authorityTreeDataCopy = null;
        List<AuthorityTreeDataCopy> authorityTreeDataCopyList = Lists.newArrayList();
        Set<String> treeDefineIdSet = null;
        if (StringUtil.isNotEmpty(roleIds)) {
            String[] roleIdArray = roleIds.split(",");
            for (int i = 0; i < roleIdArray.length; i++) {
                String roleId = "";
                if (roleIdArray[i].indexOf("R_") != -1) {
                    roleId = roleIdArray[i].substring(2, roleIdArray[i].length());
                } else {
                    roleId = roleIdArray[i];
                }
                if ("0".equals(objectType) && roleId.equals(objectId)) {
                    continue;
                }
                // 删除旧的树权限
                getDao().deleteAuthorityTree(roleId, "0", menuId, componentVersionId);
                getDaoFromContext(AuthorityTreeDataCopyDao.class).deleteAuthorityTreeData(roleId, "0", menuId, componentVersionId);
                if (canCopyAuthorityTree(roleId, "0", menuId)) {
                    String[] treeDefineIdArray = treeDefineIds.split(",");
                    authorityTreeCopyList = Lists.newArrayList();
                    treeDefineIdSet = new HashSet<String>();
                    for (String treeDefineId : treeDefineIdArray) {
                        authorityTreeCopy = new AuthorityTreeCopy();
                        authorityTreeCopy.setObjectId(roleId);
                        authorityTreeCopy.setObjectType("0");
                        authorityTreeCopy.setMenuId(menuId);
                        authorityTreeCopy.setComponentVersionId(componentVersionId);
                        authorityTreeCopy.setTreeNodeId(treeDefineId);
                        authorityTreeCopyList.add(authorityTreeCopy);
                        treeDefineIdSet.add(treeDefineId);
                    }
                    getDao().save(authorityTreeCopyList);
                    if (sourceAuthorityTreeDataCopy != null && "1".equals(sourceAuthorityTreeDataCopy.getControlDataAuth())) {
                        authorityTreeDataCopy = new AuthorityTreeDataCopy();
                        authorityTreeDataCopy.setObjectId(roleId);
                        authorityTreeDataCopy.setObjectType("0");
                        authorityTreeDataCopy.setMenuId(menuId);
                        authorityTreeDataCopy.setComponentVersionId(componentVersionId);
                        authorityTreeDataCopy.setControlDataAuth("1");
                        authorityTreeDataCopyList.add(authorityTreeDataCopy);
                    }
                }
                saveAuthorityApprove(roleId, "0", menuId, componentVersionId, treeDefineIdSet, sourceAuthorityTreeDataCopy.getControlDataAuth());
            }
        }
        if (StringUtil.isNotEmpty(userIds)) {
            String[] userIdArray = userIds.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
                String userId = "";
                if (userIdArray[i].indexOf("U_") != -1) {
                    userId = userIdArray[i].substring(2, userIdArray[i].length());
                } else {
                    userId = userIdArray[i];
                }
                if ("1".equals(objectType) && userId.equals(objectId)) {
                    continue;
                }
                // 删除旧的树权限
                getDao().deleteAuthorityTree(userId, "1", menuId, componentVersionId);
                getDaoFromContext(AuthorityTreeDataCopyDao.class).deleteAuthorityTreeData(userId, "1", menuId, componentVersionId);
                if (canCopyAuthorityTree(userId, "1", menuId)) {
                    String[] treeDefineIdArray = treeDefineIds.split(",");
                    authorityTreeCopyList = Lists.newArrayList();
                    treeDefineIdSet = new HashSet<String>();
                    for (String treeDefineId : treeDefineIdArray) {
                        authorityTreeCopy = new AuthorityTreeCopy();
                        authorityTreeCopy.setObjectId(userId);
                        authorityTreeCopy.setObjectType("1");
                        authorityTreeCopy.setMenuId(menuId);
                        authorityTreeCopy.setComponentVersionId(componentVersionId);
                        authorityTreeCopy.setTreeNodeId(treeDefineId);
                        authorityTreeCopyList.add(authorityTreeCopy);
                        treeDefineIdSet.add(treeDefineId);
                    }
                    getDao().save(authorityTreeCopyList);
                    if (sourceAuthorityTreeDataCopy != null && "1".equals(sourceAuthorityTreeDataCopy.getControlDataAuth())) {
                        authorityTreeDataCopy = new AuthorityTreeDataCopy();
                        authorityTreeDataCopy.setObjectId(userId);
                        authorityTreeDataCopy.setObjectType("1");
                        authorityTreeDataCopy.setMenuId(menuId);
                        authorityTreeDataCopy.setComponentVersionId(componentVersionId);
                        authorityTreeDataCopy.setControlDataAuth("1");
                        authorityTreeDataCopyList.add(authorityTreeDataCopy);
                    }
                }
                saveAuthorityApprove(userId, "1", menuId, componentVersionId, treeDefineIdSet, sourceAuthorityTreeDataCopy.getControlDataAuth());
            }
        }
        if (CollectionUtils.isNotEmpty(authorityTreeDataCopyList)) {
            getDaoFromContext(AuthorityTreeDataCopyDao.class).save(authorityTreeDataCopyList);
        }
    }

    /**
     * 判断树权限能否复制到该角色或用户
     * 
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     */
    @SuppressWarnings("rawtypes")
    private boolean canCopyAuthorityTree(String objectId, String objectType, String menuId) {
        boolean flag = true;
        if ("1".equals(objectType)) {
            flag = true;
        } else {
            String sql = "select count(r.resource_id) from t_resource r, t_role_res rr where r.resource_id=rr.resource_id and rr.role_id='" + objectId
                    + "' and r.resourcekey='" + menuId + "'";
            List list = AuthDatabaseUtil.queryForList(sql);
            BigDecimal count = (BigDecimal) list.get(0);
            if (count.intValue() == 0) {
                flag = false;
            }
        }
        return flag;
    }
}

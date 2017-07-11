package com.ces.config.dhtmlx.service.authority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.ColumnDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalTableDefineDao;
import com.ces.config.dhtmlx.dao.authority.AuthorityDataCopyDao;
import com.ces.config.dhtmlx.dao.authority.AuthorityDataDetailCopyDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.authority.AuthorityApprove;
import com.ces.config.dhtmlx.entity.authority.AuthorityData;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataCopy;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataDetailCopy;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.AuthDatabaseUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;

/**
 * 数据权限Service（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
@Component
public class AuthorityDataCopyService extends ConfigDefineDaoService<AuthorityDataCopy, AuthorityDataCopyDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityDataCopyDao")
    @Override
    protected void setDaoUnBinding(AuthorityDataCopyDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取某菜单某构件下的数据权限
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<AuthorityDataCopy>
     */
    public List<AuthorityDataCopy> getAuthorityDataList(String objectId, String objectType, String menuId, String componentVersionId) {
        List<AuthorityDataCopy> authorityDataCopyList = getDao().getAuthorityDataList(objectId, objectType, menuId, componentVersionId);
        for (AuthorityDataCopy authorityDataCopy : authorityDataCopyList) {
            PhysicalTableDefine tableEntity = TableUtil.getTableEntity(authorityDataCopy.getTableId());
            if (tableEntity != null) {
                authorityDataCopy.setTableName(tableEntity.getShowName());
            }
            String controlTableIds = authorityDataCopy.getControlTableIds();
            if (StringUtil.isNotEmpty(controlTableIds)) {
                String[] ctrlTableIdArray = controlTableIds.split(",");
                StringBuilder ctrlTableNames = new StringBuilder();
                for (String tableId : ctrlTableIdArray) {
                    PhysicalTableDefine tableEntity1 = TableUtil.getTableEntity(tableId);
                    if (tableEntity1 != null) {
                        ctrlTableNames.append("、").append(tableEntity1.getShowName());
                    }
                }
                if (ctrlTableNames.length() > 0) {
                    ctrlTableNames.deleteCharAt(0);
                }
                authorityDataCopy.setControlTableNames(ctrlTableNames.toString());
            }
        }
        return authorityDataCopyList;
    }

    /**
     * 根据名称获取该构件的数据权限
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param name 名称
     * @return AuthorityDataCopy
     */
    public AuthorityDataCopy getAuthorityDataByName(String objectId, String objectType, String menuId, String componentVersionId, String name) {
        return getDao().getAuthorityDataByName(objectId, objectType, menuId, componentVersionId, name);
    }

    /**
     * 保存用户或角色与数据权限关系
     * 
     * @param authorityDataCopy 数据权限
     * @param rowsValue 数据权限详情
     */
    @Transactional
    public void saveAuthorityData(AuthorityDataCopy authorityDataCopy, String rowsValue) {
        if (StringUtil.isEmpty(authorityDataCopy.getId())) {
            authorityDataCopy.setShowOrder(getMaxShowOrder(authorityDataCopy.getMenuId(), authorityDataCopy.getComponentVersionId()));
        }
        authorityDataCopy = getDao().save(authorityDataCopy);
        List<AuthorityDataDetailCopy> authorityDataDetailCopyList = getService(AuthorityDataDetailCopyService.class).saveAuthorityDataDetails(
                authorityDataCopy, rowsValue);
        saveAuthorityApprove(authorityDataCopy, authorityDataDetailCopyList);
    }

    /**
     * 保存权限审批
     * 
     * @param authorityDataCopy 数据权限
     * @param authorityDataDetailCopyList 数据权限详情
     */
    @Transactional
    private void saveAuthorityApprove(AuthorityDataCopy authorityDataCopy, List<AuthorityDataDetailCopy> authorityDataDetailCopyList) {
        if (authorityDataCopy == null || CollectionUtils.isEmpty(authorityDataDetailCopyList)) {
            return;
        }
        PhysicalTableDefine tableEntity = TableUtil.getTableEntity(authorityDataCopy.getTableId());
        if (tableEntity != null) {
            authorityDataCopy.setTableName(tableEntity.getShowName());
        }
        String controlTableIds = authorityDataCopy.getControlTableIds();
        if (StringUtil.isNotEmpty(controlTableIds)) {
            String[] ctrlTableIdArray = controlTableIds.split(",");
            StringBuilder ctrlTableNames = new StringBuilder();
            for (String tableId : ctrlTableIdArray) {
                PhysicalTableDefine tableEntity1 = TableUtil.getTableEntity(tableId);
                if (tableEntity1 != null) {
                    ctrlTableNames.append("、").append(tableEntity1.getShowName());
                }
            }
            if (ctrlTableNames.length() > 0) {
                ctrlTableNames.deleteCharAt(0);
            }
            authorityDataCopy.setControlTableNames(ctrlTableNames.toString());
        }
        // 如果AuthorityData中存在对应记录，那么AuthorityApprove为“修改待审批”，如没有对应记录，为“新增待审批”
        AuthorityData authorityData = getService(AuthorityDataService.class).getAuthorityDataByName(authorityDataCopy.getObjectId(),
                authorityDataCopy.getObjectType(), authorityDataCopy.getMenuId(), authorityDataCopy.getComponentVersionId(), authorityDataCopy.getName());
        AuthorityApprove authorityApprove = getService(AuthorityApproveService.class).getByRelateAuthId(authorityDataCopy.getId());
        if (authorityApprove == null) {
            authorityApprove = new AuthorityApprove();
            authorityApprove.setObjectId(authorityDataCopy.getObjectId());
            authorityApprove.setObjectType(authorityDataCopy.getObjectType());
            authorityApprove.setMenuId(authorityDataCopy.getMenuId());
            authorityApprove.setComponentVersionId(authorityDataCopy.getComponentVersionId());
            authorityApprove.setAuthorityType(ConstantVar.AuthorityApprove.Type.DATA);
            authorityApprove.setRelateAuthId(authorityDataCopy.getId());
        }
        if (authorityData == null) {
            authorityApprove.setOperate(ConstantVar.AuthorityApprove.Operate.NEW);
        } else {
            authorityApprove.setOperate(ConstantVar.AuthorityApprove.Operate.UPDATE);
        }
        // 设置“菜单”下“构件”下“表”的数据权限，权限名称为“”，控制条件表为“”
        StringBuilder detail = new StringBuilder();
        Menu menu = getService(MenuService.class).getByID(authorityDataCopy.getMenuId());
        String componentName = "本菜单默认权限";
        if (!AuthorityDataCopy.DEFAULT_ID.equals(authorityDataCopy.getComponentVersionId())) {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(authorityDataCopy.getComponentVersionId());
            componentName = componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion();
        }
        detail.append("设置菜单“").append(menu.getName()).append("”下构件“").append(componentName).append("”下表“").append(authorityDataCopy.getTableName())
                .append("”的数据权限，权限名称为“").append(authorityDataCopy.getName()).append("”，控制条件表为“").append(authorityDataCopy.getControlTableNames()).append("”。");
        Map<String, List<AuthorityDataDetailCopy>> authorityDataDetailCopyMap = new HashMap<String, List<AuthorityDataDetailCopy>>();
        List<AuthorityDataDetailCopy> tempADDCList = null;
        for (AuthorityDataDetailCopy tempADDC : authorityDataDetailCopyList) {
            tempADDCList = authorityDataDetailCopyMap.get(tempADDC.getTableId());
            if (tempADDCList == null) {
                tempADDCList = new ArrayList<AuthorityDataDetailCopy>();
                authorityDataDetailCopyMap.put(tempADDC.getTableId(), tempADDCList);
            }
            tempADDCList.add(tempADDC);
        }
        String tableId = null;
        PhysicalTableDefine physicalTable = null;
        ColumnDefine columnDefine = null;
        AuthorityDataDetailCopy tempADDC = null;
        for (Iterator<String> it = authorityDataDetailCopyMap.keySet().iterator(); it.hasNext();) {
            tableId = it.next();
            physicalTable = getDaoFromContext(PhysicalTableDefineDao.class).findOne(tableId);
            tempADDCList = authorityDataDetailCopyMap.get(tableId);
            Collections.sort(tempADDCList);
            detail.append("表“").append(physicalTable.getShowName()).append("”条件：");
            for (int i = 0; i < tempADDCList.size(); i++) {
                tempADDC = tempADDCList.get(i);
                columnDefine = getDaoFromContext(ColumnDefineDao.class).findOne(tempADDC.getColumnId());
                if (i != 0) {
                    if (AppDefineUtil.RELATION_OR.trim().equals(tempADDC.getRelation())) {
                        detail.append(" 或 ");
                    } else {
                        detail.append(" 并 ");
                    }
                }
                String value = authorityFilterConversion(columnDefine, tempADDC.getValue());
                detail.append(StringUtil.null2empty(tempADDC.getLeftParenthesis()))
                        .append(AppDefineUtil.processColumnFilter(null, String.valueOf(columnDefine.getShowName()), String.valueOf(tempADDC.getOperator()),
                                StringUtil.null2empty(value))).append(StringUtil.null2empty(tempADDC.getRightParenthesis()));
            }
            detail.append("。");
        }
        authorityApprove.setDetail(detail.toString());
        authorityApprove.setStatus(ConstantVar.AuthorityApprove.Status.APPROVING);
        getService(AuthorityApproveService.class).save(authorityApprove);
        // 操作日志
        CommonUtil.addOperateLog("数据权限-三权分立", "设置数据权限", detail.toString());
    }

    /***
     * 数据权限条件值转换
     * 
     * @param columnDefine 字段定义
     * @param value 字段值
     * @return String
     */
    private String authorityFilterConversion(ColumnDefine columnDefine, String value) {
        if (ConstantVar.CurrentValue.ORG.equals(value)) {
            return "当前组织";
        } else if (ConstantVar.CurrentValue.USER.equals(value)) {
            return "当前用户";
        } else {
            if (StringUtil.isNotEmpty(columnDefine.getCodeTypeCode())) {
                String codeName = CodeUtil.getInstance().getCodeName(columnDefine.getCodeTypeCode(), value);
                if (StringUtil.isNotEmpty(codeName)) {
                    return codeName;
                } else {
                    return value;
                }
            } else {
                return value;
            }
        }
    }

    /**
     * 根据构件版本ID获取最大顺序
     * 
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return Integer
     */
    public Integer getMaxShowOrder(String menuId, String componentVersionId) {
        Integer maxShowOrder = getDao().getMaxShowOrder(menuId, componentVersionId);
        if (maxShowOrder == null) {
            maxShowOrder = 0;
        }
        return maxShowOrder++;
    }

    /**
     * 删除数据权限并及联删除数据权限详情
     * 
     * @param id 主键ID
     */
    @Override
    @Transactional
    public void delete(String id) {
        String[] idArr = id.split(",");
        for (String authorityDataId : idArr) {
            // 如果AuthorityData中存在对应记录，那么AuthorityApprove为“删除待审批”，如没有对应记录，直接删除
            AuthorityDataCopy authorityDataCopy = getByID(authorityDataId);
            AuthorityData authorityData = getService(AuthorityDataService.class).getAuthorityDataByName(authorityDataCopy.getObjectId(),
                    authorityDataCopy.getObjectType(), authorityDataCopy.getMenuId(), authorityDataCopy.getComponentVersionId(), authorityDataCopy.getName());
            if (authorityData != null) {
                AuthorityApprove authorityApprove = getService(AuthorityApproveService.class).getByRelateAuthId(authorityData.getId());
                if (authorityApprove == null) {
                    authorityApprove = new AuthorityApprove();
                    authorityApprove.setObjectId(authorityDataCopy.getObjectId());
                    authorityApprove.setObjectType(authorityDataCopy.getObjectType());
                    authorityApprove.setMenuId(authorityDataCopy.getMenuId());
                    authorityApprove.setComponentVersionId(authorityDataCopy.getComponentVersionId());
                    authorityApprove.setAuthorityType(ConstantVar.AuthorityApprove.Type.DATA);
                    authorityApprove.setRelateAuthId(authorityDataCopy.getId());
                }
                authorityApprove.setOperate(ConstantVar.AuthorityApprove.Operate.DELETE);
                // 删除“菜单”下“表”的数据权限，权限名称为“”，控制条件表为“”
                StringBuilder detail = new StringBuilder();
                Menu menu = getService(MenuService.class).getByID(authorityDataCopy.getMenuId());
                detail.append("删除菜单“").append(menu.getName()).append("”下表“").append(authorityDataCopy.getTableName()).append("”的数据权限，权限名称为“")
                        .append(authorityDataCopy.getName()).append("”，控制条件表为“").append(authorityDataCopy.getControlTableNames()).append("”。");
                authorityApprove.setDetail(detail.toString());
                authorityApprove.setStatus(ConstantVar.AuthorityApprove.Status.APPROVING);
                getService(AuthorityApproveService.class).save(authorityApprove);
                // 操作日志
                CommonUtil.addOperateLog("数据权限-三权分立", "删除数据权限", detail.toString());
            } else {
                getDao().delete(authorityDataId);
                getDaoFromContext(AuthorityDataDetailCopyDao.class).deleteByAuthorityDataId(authorityDataId);
            }
        }
    }

    /**
     * 根据菜单ID删除数据权限
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDaoFromContext(AuthorityDataDetailCopyDao.class).deleteByMenuId(menuId);
        getDao().deleteByMenuId(menuId);
    }

    /**
     * 根据构件版本ID删除数据权限
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDaoFromContext(AuthorityDataDetailCopyDao.class).deleteByComponentVersionId(componentVersionId);
        getDao().deleteByComponentVersionId(componentVersionId);
    }

    /**
     * 根据构件版本ID删除数据权限
     * 
     * @param tableId 表ID
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        getDaoFromContext(AuthorityDataDetailCopyDao.class).deleteByTableId(tableId);
        getDao().deleteByTableId(tableId);
    }

    /**
     * 数据权限复制到其他角色或用户
     * 
     * @param roleIds 角色IDs
     * @param userIds 用户IDs
     * @param objectId 对象ID：OBJECT_TYPE=0时，则为角色KEY；OBJECT_TYPE=1时，则为用户ID；
     * @param objectType 对象类型：0-角色，1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void copyAuthorityData(String roleIds, String userIds, String objectId, String objectType, String menuId, String componentVersionId) {
        List<AuthorityDataCopy> sourceAuthorityDataCopyList = getAuthorityDataList(objectId, objectType, menuId, componentVersionId);
        List<AuthorityDataDetailCopy> authorityDataDetailCopyList = null;
        AuthorityDataCopy authorityDataCopy = null;
        AuthorityDataDetailCopy authorityDataDetailCopy = null;
        if (StringUtil.isNotEmpty(roleIds) && CollectionUtils.isNotEmpty(sourceAuthorityDataCopyList)) {
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
                // 删除旧的数据权限
                List<AuthorityDataCopy> tempAuthorityDataCopyList = getDao().getAuthorityDataList(roleId, "0", menuId, componentVersionId);
                if (CollectionUtils.isNotEmpty(tempAuthorityDataCopyList)) {
                    for (AuthorityDataCopy tempAuthorityDataCopy : tempAuthorityDataCopyList) {
                        getDaoFromContext(AuthorityDataDetailCopyDao.class).deleteByAuthorityDataId(tempAuthorityDataCopy.getId());
                    }
                    getDao().deleteAuthorityData(roleId, "0", menuId, componentVersionId);
                }
                if (canCopyAuthorityDataCopyRelation(roleId, "0", menuId)) {
                    for (AuthorityDataCopy sourceAuthorityDataCopy : sourceAuthorityDataCopyList) {
                        authorityDataCopy = new AuthorityDataCopy();
                        authorityDataCopy.setObjectId(roleId);
                        authorityDataCopy.setObjectType("0");
                        authorityDataCopy.setMenuId(menuId);
                        authorityDataCopy.setComponentVersionId(componentVersionId);
                        authorityDataCopy.setName(sourceAuthorityDataCopy.getName());
                        authorityDataCopy.setTableId(sourceAuthorityDataCopy.getTableId());
                        authorityDataCopy.setControlTableIds(sourceAuthorityDataCopy.getControlTableIds());
                        authorityDataCopy.setShowOrder(sourceAuthorityDataCopy.getShowOrder());
                        authorityDataCopy = getDao().save(authorityDataCopy);
                        List<AuthorityDataDetailCopy> authDataDetailCopyList = getDaoFromContext(AuthorityDataDetailCopyDao.class).getByAuthorityDataId(
                                sourceAuthorityDataCopy.getId());
                        if (CollectionUtils.isNotEmpty(authDataDetailCopyList)) {
                            authorityDataDetailCopyList = new ArrayList<AuthorityDataDetailCopy>();
                            for (AuthorityDataDetailCopy sourceAuthDataDetailCopy : authDataDetailCopyList) {
                                authorityDataDetailCopy = new AuthorityDataDetailCopy();
                                authorityDataDetailCopy.setAuthorityDataId(authorityDataCopy.getId());
                                authorityDataDetailCopy.setTableId(sourceAuthDataDetailCopy.getTableId());
                                authorityDataDetailCopy.setColumnId(sourceAuthDataDetailCopy.getColumnId());
                                authorityDataDetailCopy.setLeftParenthesis(sourceAuthDataDetailCopy.getLeftParenthesis());
                                authorityDataDetailCopy.setOperator(sourceAuthDataDetailCopy.getOperator());
                                authorityDataDetailCopy.setRelation(sourceAuthDataDetailCopy.getRelation());
                                authorityDataDetailCopy.setRightParenthesis(sourceAuthDataDetailCopy.getRightParenthesis());
                                authorityDataDetailCopy.setShowOrder(sourceAuthDataDetailCopy.getShowOrder());
                                authorityDataDetailCopy.setValue(sourceAuthDataDetailCopy.getValue());
                                authorityDataDetailCopyList.add(authorityDataDetailCopy);
                            }
                            getDaoFromContext(AuthorityDataDetailCopyDao.class).save(authorityDataDetailCopyList);
                            saveAuthorityApprove(authorityDataCopy, authorityDataDetailCopyList);
                        }
                    }
                }
            }
        }
        if (StringUtil.isNotEmpty(userIds) && CollectionUtils.isNotEmpty(sourceAuthorityDataCopyList)) {
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
                // 删除旧的数据权限
                List<AuthorityDataCopy> tempAuthorityDataCopyList = getDao().getAuthorityDataList(userId, "1", menuId, componentVersionId);
                if (CollectionUtils.isNotEmpty(tempAuthorityDataCopyList)) {
                    for (AuthorityDataCopy tempAuthorityDataCopy : tempAuthorityDataCopyList) {
                        getDaoFromContext(AuthorityDataDetailCopyDao.class).deleteByAuthorityDataId(tempAuthorityDataCopy.getId());
                    }
                    getDao().deleteAuthorityData(userId, "1", menuId, componentVersionId);
                }
                if (canCopyAuthorityDataCopyRelation(userId, "1", menuId)) {
                    for (AuthorityDataCopy sourceAuthorityDataCopy : sourceAuthorityDataCopyList) {
                        authorityDataCopy = new AuthorityDataCopy();
                        authorityDataCopy.setObjectId(userId);
                        authorityDataCopy.setObjectType("1");
                        authorityDataCopy.setMenuId(menuId);
                        authorityDataCopy.setComponentVersionId(componentVersionId);
                        authorityDataCopy.setName(sourceAuthorityDataCopy.getName());
                        authorityDataCopy.setTableId(sourceAuthorityDataCopy.getTableId());
                        authorityDataCopy.setControlTableIds(sourceAuthorityDataCopy.getControlTableIds());
                        authorityDataCopy.setShowOrder(sourceAuthorityDataCopy.getShowOrder());
                        authorityDataCopy = getDao().save(authorityDataCopy);
                        List<AuthorityDataDetailCopy> authDataDetailCopyList = getDaoFromContext(AuthorityDataDetailCopyDao.class).getByAuthorityDataId(
                                sourceAuthorityDataCopy.getId());
                        if (CollectionUtils.isNotEmpty(authDataDetailCopyList)) {
                            authorityDataDetailCopyList = new ArrayList<AuthorityDataDetailCopy>();
                            for (AuthorityDataDetailCopy sourceAuthDataDetailCopy : authDataDetailCopyList) {
                                authorityDataDetailCopy = new AuthorityDataDetailCopy();
                                authorityDataDetailCopy.setAuthorityDataId(authorityDataCopy.getId());
                                authorityDataDetailCopy.setTableId(sourceAuthDataDetailCopy.getTableId());
                                authorityDataDetailCopy.setColumnId(sourceAuthDataDetailCopy.getColumnId());
                                authorityDataDetailCopy.setLeftParenthesis(sourceAuthDataDetailCopy.getLeftParenthesis());
                                authorityDataDetailCopy.setOperator(sourceAuthDataDetailCopy.getOperator());
                                authorityDataDetailCopy.setRelation(sourceAuthDataDetailCopy.getRelation());
                                authorityDataDetailCopy.setRightParenthesis(sourceAuthDataDetailCopy.getRightParenthesis());
                                authorityDataDetailCopy.setShowOrder(sourceAuthDataDetailCopy.getShowOrder());
                                authorityDataDetailCopy.setValue(sourceAuthDataDetailCopy.getValue());
                                authorityDataDetailCopyList.add(authorityDataDetailCopy);
                            }
                            getDaoFromContext(AuthorityDataDetailCopyDao.class).save(authorityDataDetailCopyList);
                            saveAuthorityApprove(authorityDataCopy, authorityDataDetailCopyList);
                        }
                    }
                }
            }
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
    private boolean canCopyAuthorityDataCopyRelation(String objectId, String objectType, String menuId) {
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

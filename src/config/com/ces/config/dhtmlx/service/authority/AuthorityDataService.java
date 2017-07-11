package com.ces.config.dhtmlx.service.authority;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.authority.AuthorityDataDao;
import com.ces.config.dhtmlx.dao.authority.AuthorityDataDetailDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.authority.AuthorityData;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataDetail;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.AuthDatabaseUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.EhcacheUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.security.entity.SysUser;

/**
 * 数据权限Service
 * 
 * @author wanglei
 * @date 2014-09-25
 */
@Component
public class AuthorityDataService extends ConfigDefineDaoService<AuthorityData, AuthorityDataDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityDataDao")
    @Override
    protected void setDaoUnBinding(AuthorityDataDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取某菜单某构件下的数据权限
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @return List<AuthorityData>
     */
    public List<AuthorityData> getAuthorityDataList(String objectId, String objectType, String menuId, String componentVersionId) {
        List<AuthorityData> authorityDataList = getDao().getAuthorityDataList(objectId, objectType, menuId, componentVersionId);
        for (AuthorityData authorityData : authorityDataList) {
            PhysicalTableDefine tableEntity = TableUtil.getTableEntity(authorityData.getTableId());
            if (tableEntity != null) {
                authorityData.setTableName(tableEntity.getShowName());
            }
            String controlTableIds = authorityData.getControlTableIds();
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
                authorityData.setControlTableNames(ctrlTableNames.toString());
            }
        }
        return authorityDataList;
    }

    /**
     * 根据名称获取该构件的数据权限
     * 
     * @param objectId 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID
     * @param objectType 类型：0-角色 1-用户
     * @param menuId 菜单ID
     * @param componentVersionId 构件版本ID
     * @param name 名称
     * @return AuthorityData
     */
    public AuthorityData getAuthorityDataByName(String objectId, String objectType, String menuId, String componentVersionId, String name) {
        return getDao().getAuthorityDataByName(objectId, objectType, menuId, componentVersionId, name);
    }

    /**
     * 保存用户或角色与数据权限关系
     * 
     * @param authorityData 数据权限
     * @param rowsValue 数据权限详情
     */
    @Transactional
    public void saveAuthorityData(AuthorityData authorityData, String rowsValue) {
        if (StringUtil.isEmpty(authorityData.getId())) {
            authorityData.setShowOrder(getMaxShowOrder(authorityData.getMenuId(), authorityData.getComponentVersionId()));
        }
        authorityData = getDao().save(authorityData);
        getService(AuthorityDataDetailService.class).saveAuthorityDataDetails(authorityData, rowsValue);
        // 清除缓存
        if (authorityData.getTableId().equals(authorityData.getControlTableIds())) {
            AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, authorityData.getObjectId(), authorityData.getObjectType(),
                    authorityData.getMenuId());
        } else {
            AuthorityUtil.getInstance().clearRelateAuthority(authorityData.getObjectId(), authorityData.getObjectType(), authorityData.getTableId(),
                    authorityData.getMenuId(), authorityData.getComponentVersionId());
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
        AuthorityData authorityData = getByID(idArr[0]);
        for (String authorityDataId : idArr) {
            getDao().delete(authorityDataId);
            getDaoFromContext(AuthorityDataDetailDao.class).deleteByAuthorityDataId(authorityDataId);
        }
        // 清除缓存
        AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, authorityData.getObjectId(), authorityData.getObjectType(),
                authorityData.getMenuId());
        AuthorityUtil.getInstance().clearRelateAuthority(authorityData.getObjectId(), authorityData.getObjectType(), authorityData.getMenuId());
    }

    /**
     * 根据菜单ID删除数据权限
     * 
     * @param menuId 菜单ID
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDaoFromContext(AuthorityDataDetailDao.class).deleteByMenuId(menuId);
        getDao().deleteByMenuId(menuId);
        // 移除缓存
        AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, menuId);
        AuthorityUtil.getInstance().clearRelateAuthority(menuId);
    }

    /**
     * 根据构件版本ID删除数据权限
     * 
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDaoFromContext(AuthorityDataDetailDao.class).deleteByComponentVersionId(componentVersionId);
        getDao().deleteByComponentVersionId(componentVersionId);
        // 移除缓存
        AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, componentVersionId);
        AuthorityUtil.getInstance().clearRelateAuthority(componentVersionId);
    }

    /**
     * 根据构件版本ID删除数据权限
     * 
     * @param tableId 表ID
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        getDaoFromContext(AuthorityDataDetailDao.class).deleteByTableId(tableId);
        getDao().deleteByTableId(tableId);
        // 移除缓存
        AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, tableId);
        AuthorityUtil.getInstance().clearRelateAuthority(tableId);
    }

    /**
     * qiucs 2014-1-9
     * <p>描述: 数据权限：优先取自身数据权限，其次为自身拥有的角色数据权限.</p>
     */
    @SuppressWarnings("unchecked")
    public String buildAuthorityFilter(String tableId, String componentVersionId, String menuId) {
        StringBuffer filter = new StringBuffer();
        // 存放多组过滤条件
        List<List<String[]>> filterList = new ArrayList<List<String[]>>();
        // 存放一组过滤条件
        List<String[]> list = null;
        String sql = null;
        SysUser user = CommonUtil.getUser();
        StringBuffer sb = new StringBuffer("select");
        sb.append(" c.column_name, t.value, t.relation, t.operator, t.left_parenthesis, t.right_parenthesis").append(" from t_xtpz_authority_data_detail t")
                .append(" join t_xtpz_column_define c on(t.column_id=c.id and c.table_id='").append(tableId).append("')")
                .append(" join t_xtpz_authority_data d on (t.authority_data_id = d.id)").append(" where d.table_id=d.control_table_ids and d.table_id='")
                .append(tableId).append("' and d.menu_id='").append(menuId).append("'");
        // 1. 当前用户自身的数据权限
        sql = String.valueOf(sb) + " and d.component_version_id='" + componentVersionId + "' and (d.object_id='" + user.getId()
                + "' and d.object_type='1')  order by t.show_order ";
        list = DatabaseHandlerDao.getInstance().queryForList(sql);
        if (CollectionUtils.isEmpty(list)) {
            // 2. 当前用户本菜单下的数据权限
            sql = String.valueOf(sb) + " and d.component_version_id='-1' and (d.object_id='" + user.getId()
                    + "' and d.object_type='1')  order by t.show_order ";
            list = DatabaseHandlerDao.getInstance().queryForList(sql);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            filterList.add(list);
        }
        // 如果用户本身没有数据权限，那么使用角色的数据权限的并集
        if (CollectionUtils.isEmpty(list)) {
            String roleIds = CommonUtil.getRoleIds();
            String[] roleIdArr = roleIds.split(",");
            if (StringUtil.isNotEmpty(roleIds)) {
                // 3. 当前用户拥有的角色的数据权限
                for (String roleId : roleIdArr) {
                    sql = String.valueOf(sb) + " and d.component_version_id='" + componentVersionId + "' and (d.object_id='" + roleId
                            + "' and d.object_type='0')  order by t.show_order ";
                    list = DatabaseHandlerDao.getInstance().queryForList(sql);
                    if (CollectionUtils.isNotEmpty(list)) {
                        filterList.add(list);
                    }
                }
            }
            if (CollectionUtils.isEmpty(filterList) && StringUtil.isNotEmpty(roleIds)) {
                // 4. 当前用户拥有的角色本菜单下的数据权限
                for (String roleId : roleIdArr) {
                    sql = String.valueOf(sb) + " and d.component_version_id='-1' and (d.object_id='" + roleId
                            + "' and d.object_type='0')  order by t.show_order ";
                    list = DatabaseHandlerDao.getInstance().queryForList(sql);
                    if (CollectionUtils.isNotEmpty(list)) {
                        filterList.add(list);
                    }
                }
            }

        }
        if (CollectionUtils.isNotEmpty(filterList)) {
            filter.append(AppDefineUtil.RELATION_AND).append("(");
            for (int j = 0; j < filterList.size(); j++) {
                list = filterList.get(j);
                if (j == 0) {
                    filter.append("(");
                } else {
                    filter.append(AppDefineUtil.RELATION_OR).append("(");
                }
                for (int i = 0; i < list.size(); i++) {
                    // c.column_name, t.value, t.relation, t.operator, t.left_parenthesis, t.left_parenthesis
                    Object[] obj = list.get(i);
                    if (i != 0) {
                        if (AppDefineUtil.RELATION_OR.trim().equals(StringUtil.null2empty(obj[2]).toUpperCase())) {
                            filter.append(AppDefineUtil.RELATION_OR);
                        } else {
                            filter.append(AppDefineUtil.RELATION_AND);
                        }
                    }
                    String value = authorityFilterConversion(StringUtil.null2empty(obj[1]));
                    filter.append(StringUtil.null2empty(obj[4]))
                            .append(AppDefineUtil.processColumnFilter(null, String.valueOf(obj[0]), String.valueOf(obj[3]), StringUtil.null2empty(value)))
                            .append(StringUtil.null2empty(obj[5]));
                }
                filter.append(")");
            }
            filter.append(")");
        }
        return String.valueOf(filter);
    }

    /***
     * 数据权限条件值转换
     * 
     * @param filter
     * @return String
     */
    @SuppressWarnings("unchecked")
    private String authorityFilterConversion(String filter) {
        Map<String, Object> map = (Map<String, Object>) EhcacheUtil.getCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId());
        if (map == null) {
            map = AuthorityUtil.getInstance().getAuthorityManager().getAuthorityDeptFilterModelData();
            EhcacheUtil.setCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId(), map);
        }
        return AuthorityUtil.getInstance().getAuthorityManager().authorityFilterConversion(map, filter);
    }

    /**
     * 获取关联表数据权限
     * <p>描述: 数据权限：优先取自身数据权限，其次为自身拥有的角色数据权限.</p>
     */
    public Map<String, Map<String, String>> buildRelateAuthorityFilter(String tableId, String componentVersionId, String menuId) {
        Map<String, Map<String, String>> relateDataAuthorityMap = new HashMap<String, Map<String, String>>();
        SysUser user = CommonUtil.getUser();
        // 1. 当前用户自身的数据权限
        List<AuthorityData> authorityDataList = getAuthorityDataList(user.getId(), "1", menuId, componentVersionId);
        if (CollectionUtils.isEmpty(authorityDataList)) {
            // 2. 当前用户本菜单下的数据权限
            authorityDataList = getAuthorityDataList(user.getId(), "1", menuId, "-1");
        }
        // 如果用户本身没有数据权限，那么使用角色的数据权限的并集
        if (CollectionUtils.isEmpty(authorityDataList)) {
            String roleIds = CommonUtil.getRoleIds();
            String[] roleIdArr = roleIds.split(",");
            if (StringUtil.isNotEmpty(roleIds)) {
                // 3. 当前用户拥有的角色的数据权限
                for (String roleId : roleIdArr) {
                    authorityDataList.addAll(getAuthorityDataList(roleId, "0", menuId, componentVersionId));
                }
            }
            if (CollectionUtils.isEmpty(authorityDataList) && StringUtil.isNotEmpty(roleIds)) {
                // 4. 当前用户拥有的角色本菜单下的数据权限
                for (String roleId : roleIdArr) {
                    authorityDataList.addAll(getAuthorityDataList(roleId, "0", menuId, "-1"));
                }
            }
        }
        StringBuilder filter = null;
        if (CollectionUtils.isNotEmpty(authorityDataList)) {
            for (AuthorityData authorityData : authorityDataList) {
                // 过滤掉控制表不是该表的权限
                if (!tableId.equals(authorityData.getTableId())) {
                    continue;
                }
                String controlTableIds = authorityData.getControlTableIds();
                // 过滤掉本表的权限
                if (authorityData.getTableId().equals(controlTableIds)) {
                    continue;
                }
                Map<String, String> controlTableFilterMap = new HashMap<String, String>();
                relateDataAuthorityMap.put(authorityData.getId(), controlTableFilterMap);
                String[] controlTableIdArr = controlTableIds.split(",");
                for (String controlTableId : controlTableIdArr) {
                    List<AuthorityDataDetail> authorityDataDetailList = getService(AuthorityDataDetailService.class).getByAuthorityDataIdAndTableId(
                            authorityData.getId(), controlTableId);
                    if (CollectionUtils.isNotEmpty(authorityDataDetailList)) {
                        filter = new StringBuilder();
                        filter.append(AppDefineUtil.RELATION_AND).append("(");
                        for (int i = 0; i < authorityDataDetailList.size(); i++) {
                            AuthorityDataDetail authorityDataDetail = authorityDataDetailList.get(i);
                            String columnName = getService(ColumnDefineService.class).getColumnNameById(authorityDataDetail.getColumnId());
                            if (i != 0) {
                                if (AppDefineUtil.RELATION_OR.trim().equals(StringUtil.null2empty(authorityDataDetail.getRelation()).toUpperCase())) {
                                    filter.append(AppDefineUtil.RELATION_OR);
                                } else {
                                    filter.append(AppDefineUtil.RELATION_AND);
                                }
                            }
                            String value = authorityFilterConversion(StringUtil.null2empty(authorityDataDetail.getValue()));
                            filter.append(StringUtil.null2empty(authorityDataDetail.getLeftParenthesis()))
                                    .append(AppDefineUtil.processColumnFilter(null, columnName, authorityDataDetail.getOperator(), StringUtil.null2empty(value)))
                                    .append(StringUtil.null2empty(authorityDataDetail.getRightParenthesis()));
                        }
                        filter.append(")");
                        controlTableFilterMap.put(controlTableId, filter.toString());
                    }
                }
            }
        }
        return relateDataAuthorityMap;
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
        List<AuthorityData> sourceAuthorityDataList = getAuthorityDataList(objectId, objectType, menuId, componentVersionId);
        List<AuthorityDataDetail> authorityDataDetailList = new ArrayList<AuthorityDataDetail>();
        AuthorityData authorityData = null;
        AuthorityDataDetail authorityDataDetail = null;
        if (StringUtil.isNotEmpty(roleIds) && CollectionUtils.isNotEmpty(sourceAuthorityDataList)) {
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
                List<AuthorityData> tempAuthorityDataList = getDao().getAuthorityDataList(roleId, "0", menuId, componentVersionId);
                if (CollectionUtils.isNotEmpty(tempAuthorityDataList)) {
                    for (AuthorityData tempAuthorityData : tempAuthorityDataList) {
                        getDaoFromContext(AuthorityDataDetailDao.class).deleteByAuthorityDataId(tempAuthorityData.getId());
                    }
                    getDao().deleteAuthorityData(roleId, "0", menuId, componentVersionId);
                }
                if (canCopyAuthorityDataRelation(roleId, "0", menuId)) {
                    for (AuthorityData sourceAuthorityData : sourceAuthorityDataList) {
                        authorityData = new AuthorityData();
                        authorityData.setObjectId(roleId);
                        authorityData.setObjectType("0");
                        authorityData.setMenuId(menuId);
                        authorityData.setComponentVersionId(componentVersionId);
                        authorityData.setName(sourceAuthorityData.getName());
                        authorityData.setTableId(sourceAuthorityData.getTableId());
                        authorityData.setControlTableIds(sourceAuthorityData.getControlTableIds());
                        authorityData.setShowOrder(sourceAuthorityData.getShowOrder());
                        authorityData = getDao().save(authorityData);
                        List<AuthorityDataDetail> authDataDetailList = getDaoFromContext(AuthorityDataDetailDao.class).getByAuthorityDataId(
                                sourceAuthorityData.getId());
                        if (CollectionUtils.isNotEmpty(authDataDetailList)) {
                            for (AuthorityDataDetail sourceAuthDataDetail : authDataDetailList) {
                                authorityDataDetail = new AuthorityDataDetail();
                                authorityDataDetail.setAuthorityDataId(authorityData.getId());
                                authorityDataDetail.setTableId(sourceAuthDataDetail.getTableId());
                                authorityDataDetail.setColumnId(sourceAuthDataDetail.getColumnId());
                                authorityDataDetail.setLeftParenthesis(sourceAuthDataDetail.getLeftParenthesis());
                                authorityDataDetail.setOperator(sourceAuthDataDetail.getOperator());
                                authorityDataDetail.setRelation(sourceAuthDataDetail.getRelation());
                                authorityDataDetail.setRightParenthesis(sourceAuthDataDetail.getRightParenthesis());
                                authorityDataDetail.setShowOrder(sourceAuthDataDetail.getShowOrder());
                                authorityDataDetail.setValue(sourceAuthDataDetail.getValue());
                                authorityDataDetailList.add(authorityDataDetail);
                            }
                        }
                    }
                }
                getDaoFromContext(AuthorityDataDetailDao.class).save(authorityDataDetailList);
                // 清除缓存
                AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, roleId, "0", menuId);
            }
        }
        if (StringUtil.isNotEmpty(userIds) && CollectionUtils.isNotEmpty(sourceAuthorityDataList)) {
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
                List<AuthorityData> tempAuthorityDataList = getDao().getAuthorityDataList(userId, "1", menuId, componentVersionId);
                if (CollectionUtils.isNotEmpty(tempAuthorityDataList)) {
                    for (AuthorityData tempAuthorityData : tempAuthorityDataList) {
                        getDaoFromContext(AuthorityDataDetailDao.class).deleteByAuthorityDataId(tempAuthorityData.getId());
                    }
                    getDao().deleteAuthorityData(userId, "1", menuId, componentVersionId);
                }
                if (canCopyAuthorityDataRelation(userId, "1", menuId)) {
                    for (AuthorityData sourceAuthorityData : sourceAuthorityDataList) {
                        authorityData = new AuthorityData();
                        authorityData.setObjectId(userId);
                        authorityData.setObjectType("1");
                        authorityData.setMenuId(menuId);
                        authorityData.setComponentVersionId(componentVersionId);
                        authorityData.setName(sourceAuthorityData.getName());
                        authorityData.setTableId(sourceAuthorityData.getTableId());
                        authorityData.setControlTableIds(sourceAuthorityData.getControlTableIds());
                        authorityData.setShowOrder(sourceAuthorityData.getShowOrder());
                        authorityData = getDao().save(authorityData);
                        List<AuthorityDataDetail> authDataDetailList = getDaoFromContext(AuthorityDataDetailDao.class).getByAuthorityDataId(
                                sourceAuthorityData.getId());
                        if (CollectionUtils.isNotEmpty(authDataDetailList)) {
                            for (AuthorityDataDetail sourceAuthDataDetail : authDataDetailList) {
                                authorityDataDetail = new AuthorityDataDetail();
                                authorityDataDetail.setAuthorityDataId(authorityData.getId());
                                authorityDataDetail.setTableId(sourceAuthDataDetail.getTableId());
                                authorityDataDetail.setColumnId(sourceAuthDataDetail.getColumnId());
                                authorityDataDetail.setLeftParenthesis(sourceAuthDataDetail.getLeftParenthesis());
                                authorityDataDetail.setOperator(sourceAuthDataDetail.getOperator());
                                authorityDataDetail.setRelation(sourceAuthDataDetail.getRelation());
                                authorityDataDetail.setRightParenthesis(sourceAuthDataDetail.getRightParenthesis());
                                authorityDataDetail.setShowOrder(sourceAuthDataDetail.getShowOrder());
                                authorityDataDetail.setValue(sourceAuthDataDetail.getValue());
                                authorityDataDetailList.add(authorityDataDetail);
                            }
                        }
                    }
                }
                getDaoFromContext(AuthorityDataDetailDao.class).save(authorityDataDetailList);
                // 清除缓存
                AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, userId, "0", menuId);
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
    private boolean canCopyAuthorityDataRelation(String objectId, String objectType, String menuId) {
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

    /**
     * 根据角色IDs和菜单IDs获取数据权限
     * 
     * @param roleIds 角色IDs
     * @param menuIds 菜单IDs
     * @return List<AuthorityData>
     */
    public List<AuthorityData> getByRoleIdsAndMenuIds(String roleIds, String menuIds) {
        List<AuthorityData> authorityDataList = new ArrayList<AuthorityData>();
        if (StringUtil.isNotEmpty(roleIds) && StringUtil.isNotEmpty(menuIds)) {
            String hql = "from AuthorityData t where t.objectType='0' and t.objectId in ('" + roleIds.replace(",", "','") + "') and t.menuId in ('"
                    + menuIds.replace(",", "','") + "')";
            authorityDataList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, AuthorityData.class);
        }
        return authorityDataList;
    }

    /**
     * 根据菜单IDs获取用户的数据权限
     * 
     * @param menuIds 菜单IDs
     * @return List<AuthorityData>
     */
    public List<AuthorityData> getByMenuIdsOfUser(String menuIds) {
        List<AuthorityData> authorityDataList = new ArrayList<AuthorityData>();
        if (StringUtil.isNotEmpty(menuIds)) {
            String hql = "from AuthorityData t where t.objectType='1' and t.menuId in ('" + menuIds.replace(",", "','") + "')";
            authorityDataList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, AuthorityData.class);
        }
        return authorityDataList;
    }
}

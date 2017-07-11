package com.ces.config.dhtmlx.service.authority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ces.config.dhtmlx.dao.authority.AuthorityDataDetailDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityData;
import com.ces.config.dhtmlx.entity.authority.AuthorityDataDetail;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.EhcacheUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;

/**
 * 数据权限详情Service
 * 
 * @author wanglei
 * @date 2014-09-25
 */
@Component
public class AuthorityDataDetailService extends ConfigDefineDaoService<AuthorityDataDetail, AuthorityDataDetailDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityDataDetailDao")
    @Override
    protected void setDaoUnBinding(AuthorityDataDetailDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据数据权限ID获取数据权限详情
     * 
     * @param authorityDataId 数据权限ID
     * @return List<AuthorityDataDetail>
     */
    public List<AuthorityDataDetail> getByAuthorityDataId(String authorityDataId) {
        return getDao().getByAuthorityDataId(authorityDataId);
    }

    /**
     * 根据数据权限ID和表ID获取数据权限详情
     * 
     * @param authorityDataId 数据权限ID
     * @param tableId 表ID
     * @return List<AuthorityDataDetail>
     */
    public List<AuthorityDataDetail> getByAuthorityDataIdAndTableId(String authorityDataId, String tableId) {
        return getDao().getByAuthorityDataIdAndTableId(authorityDataId, tableId);
    }

    /**
     * 保存数据权限详情
     * 
     * @param authorityData 数据权限
     * @param rowsValue 数据权限详情
     */
    @Transactional
    public void saveAuthorityDataDetails(AuthorityData authorityData, String rowsValue) {
        getDao().deleteByAuthorityDataId(authorityData.getId());
        String[] controlTableDetails = rowsValue.split("≡");
        List<AuthorityDataDetail> list = new ArrayList<AuthorityDataDetail>();
        for (String controlTableDetail : controlTableDetails) {
            String[] tempStrs = controlTableDetail.split("#");
            String controlTableId = tempStrs[0];
            String[] details = tempStrs[1].split(";");
            AuthorityDataDetail authorityDataDetail = null;
            int i = 0;
            for (String detail : details) {
                if (StringUtil.isNotEmpty(detail)) {
                    // 此处是防止detail字符串已,,,结尾而导致strs数组长度变短，加上", "使得数组长度+1
                    detail = detail + ", ";
                    String[] strs = detail.split(",");
                    authorityDataDetail = new AuthorityDataDetail();
                    authorityDataDetail.setAuthorityDataId(authorityData.getId());
                    authorityDataDetail.setTableId(controlTableId);
                    authorityDataDetail.setRelation(strs[0]);
                    authorityDataDetail.setLeftParenthesis(strs[1]);
                    authorityDataDetail.setColumnId(strs[2]);
                    authorityDataDetail.setOperator(strs[3]);
                    authorityDataDetail.setValue(strs[4]);
                    authorityDataDetail.setRightParenthesis(strs[5]);
                    authorityDataDetail.setShowOrder(i++);
                    list.add(authorityDataDetail);
                }
            }
        }
        getDao().save(list);
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
        AuthorityDataDetail authorityDataDetail = getByID(idArr[0]);
        AuthorityData authorityData = getService(AuthorityDataService.class).getByID(authorityDataDetail.getAuthorityDataId());
        for (String authorityDataDetailId : idArr) {
            getDao().delete(authorityDataDetailId);
        }
        // 清除缓存
        AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, authorityData.getObjectId(), authorityData.getObjectType(),
                authorityData.getMenuId());
        AuthorityUtil.getInstance().clearRelateAuthority(authorityData.getObjectId(), authorityData.getObjectType(), authorityData.getMenuId());
    }

    /***
     * 获取数据权限部门过滤条件数据模型
     * 
     * @return Map<String, Object>
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAuthorityDeptFilterModelData() {
        Map<String, Object> map = (Map<String, Object>) EhcacheUtil.getCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId());
        if (CollectionUtils.isEmpty(map)) {
            map = new HashMap<String, Object>();
            map.put(ConstantVar.CurrentValue.USER, "只查看当前用户");
            map.put(ConstantVar.CurrentValue.ORG, "只查看当前组织");
        }
        return map;
    }

    /***
     * 添加数据权限部门过滤条件下拉数据
     * 
     * @param key 键
     * @param value 值
     */
    @SuppressWarnings("unchecked")
    public void putAuthorityDeptFilterModelData(String key, Object value) {
        Map<String, Object> map = (Map<String, Object>) EhcacheUtil.getCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId());
        if (null == map) {
            map = new HashMap<String, Object>();
            EhcacheUtil.setCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId(), map);
        }
        map.put(key, value);
    }

    /***
     * 移除数据权限部门过滤条件下拉数据
     * 
     * @param key 键
     */
    @SuppressWarnings("unchecked")
    public void removeAuthorityDeptFilterModelData(String key) {
        Map<String, Object> map = (Map<String, Object>) EhcacheUtil.getCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId());
        if (null == map) {
            map = new HashMap<String, Object>();
            EhcacheUtil.setCache(AuthorityUtil.AUTHORITY_DEPT_DATA_MODEL, CommonUtil.getCurrentUserId(), map);
        }
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
            if (key.equals(iterator.next())) {
                iterator.remove();
            }
        }
    }

    /***
     * 数据权限条件值转换
     * 
     * @param filter
     * @return String
     */
    public String authorityFilterConversion(Map<String, Object> map, String filter) {
        for (Entry<String, Object> e : map.entrySet()) {
            if (filter.equals(e.getKey())) {
                if (filter.equals(ConstantVar.CurrentValue.ORG)) {
                    filter = CommonUtil.getCurrentDeptId();
                    break;
                } else if (filter.equals(ConstantVar.CurrentValue.USER)) {
                    filter = CommonUtil.getCurrentUserId();
                    break;
                }
            }
        }
        return filter;
    }

    /**
     * 根据角色IDs和菜单IDs获取数据权限详情
     * 
     * @param roleIds 角色IDs
     * @param menuIds 菜单IDs
     * @return List<AuthorityDataDetail>
     */
    public List<AuthorityDataDetail> getByRoleIdsAndMenuIds(String roleIds, String menuIds) {
        List<AuthorityDataDetail> authorityDataDetailList = new ArrayList<AuthorityDataDetail>();
        if (StringUtil.isNotEmpty(roleIds) && StringUtil.isNotEmpty(menuIds)) {
            String hql = "select d from AuthorityDataDetail d, AuthorityData t where d.authorityDataId=t.id and t.objectType='0' and t.objectId in ('"
                    + roleIds.replace(",", "','") + "') and t.menuId in ('" + menuIds.replace(",", "','") + "')";
            authorityDataDetailList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, AuthorityDataDetail.class);
        }
        return authorityDataDetailList;
    }

    /**
     * 根据菜单IDs获取用户的数据权限详情
     * 
     * @param menuIds 菜单IDs
     * @return List<AuthorityDataDetail>
     */
    public List<AuthorityDataDetail> getByMenuIdsOfUser(String menuIds) {
        List<AuthorityDataDetail> authorityDataDetailList = new ArrayList<AuthorityDataDetail>();
        if (StringUtil.isNotEmpty(menuIds)) {
            String hql = "select d from AuthorityDataDetail d, AuthorityData t where d.authorityDataId=t.id and t.objectType='1' and t.menuId in ('"
                    + menuIds.replace(",", "','") + "')";
            authorityDataDetailList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, AuthorityDataDetail.class);
        }
        return authorityDataDetailList;
    }
}

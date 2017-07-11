package com.ces.config.dhtmlx.service.authority;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.authority.AuthorityReportDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.authority.AuthorityReport;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.security.entity.SysUser;

/**
 * 报表按钮权限Service
 * 
 * @author liaomingsong
 * @date 2014-5-5
 */
@Component
public class AuthorityReportService extends ConfigDefineDaoService<AuthorityReport, AuthorityReportDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("authorityReportDao")
    @Override
    protected void setDaoUnBinding(AuthorityReportDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取当前用户无权限报表，如果用户配置了权限，直接使用用户本身的权限；如果没有，则使用用户绑定的角色的权限合集
     * 
     * @param tableId 表ID
     * @param componentVersionId 构件版本ID
     * @param menuId 菜单ID
     * @return List<String>
     */
    public List<String> notAuthorityReports(String tableId, String componentVersionId, String menuId) {
        SysUser user = CommonUtil.getUser();
        String hql = getNotAuthorityReportsQuery(user.getId(), "1", tableId, componentVersionId, menuId);
        List<String> list = DatabaseHandlerDao.getInstance().queryEntityForList(hql, String.class);
        if (CollectionUtils.isEmpty(list)) {
            String roleIds = CommonUtil.getRoleIds();
            if (StringUtil.isNotEmpty(roleIds)) {
                hql = getNotAuthorityReportsQuery(roleIds, "0", tableId, componentVersionId, menuId);
                list = DatabaseHandlerDao.getInstance().queryEntityForList(hql, String.class);
            }
        }
        return list;
    }

    /**
     * 获取无权限报表的查询语句
     * 
     * @param objectId 角色或用户ID
     * @param objectType 类型：0：角色 1：用户
     * @param tableId 表ID
     * @param componentVersionId 构件版本ID
     * @param menuId 菜单ID
     * @return String
     */
    private String getNotAuthorityReportsQuery(String objectId, String objectType, String tableId, String componentVersionId, String menuId) {
        String sql = "select t.reportId from AuthorityReport t where";
        if ("0".equals(objectType)) {
            sql += " t.objectId in ('" + objectId.replace(",", "','") + "')";
        } else {
            sql += " t.objectId='" + objectId + "'";
        }
        sql += " and t.objectType='" + objectType + "'" + " and t.tableId='" + tableId + "'" + " and t.componentVersionId='" + componentVersionId
                + "' and t.menuId='" + menuId + "'";
        return sql;
    }
}

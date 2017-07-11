package com.ces.config.dhtmlx.dao.authority;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.authority.AuthorityReport;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 自定义构件报表按钮权限DAO
 * 
 * @author wanglei
 * @date 2014-07-31
 */
public interface AuthorityReportDao extends StringIDDao<AuthorityReport> {

    /**
     * 根据表ID和构件版本ID删除自定义构件报表按钮权限
     * 
     * @param tableId 表ID
     * @param componentVersionId 构件版本ID
     */
    @Transactional
    @Modifying
    @Query("delete AuthorityReport t_as where t_as.tableId=?1 and t_as.componentVersionId=?2")
    public void deleteAllAuthReport(String tableId, String componentVersionId);
}

package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.AppReport;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface AppReportDao extends StringIDDao<AppReport> {

    /**
     * <p>描述: 根据模块ID获取报表信息 </p>
     * 
     * @return List<AppReport>
     */
    @Query("from AppReport t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4")
    public List<AppReport> findByFk(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * <p>描述: 获取未配置过报表信息</p>
     * 
     * @return List<Object[]> 返回类型
     */
    @Query(value = "select t_r.id, t_r.name from t_xtpz_report t_r "
            + "left join t_xtpz_app_report t_ar on(t_ar.table_id=?1 and t_ar.component_version_id=?2 and t_ar.menu_id=?3 and t_ar.user_id=?4 and t_r.id=t_ar.report_id) "
            + "left join t_xtpz_report_table t_rt on (t_r.id=t_rt.report_id) where t_rt.table_id=?1 and t_ar.report_id is null", nativeQuery = true)
    public List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * <p>描述: 获取配置过的报表信息</p>
     * 
     * @return List<Object[]> 返回类型
     */
    @Query(value = "select t_ar.id, t_r.id as reportId, t_r.name from t_xtpz_report t_r, t_xtpz_app_report t_ar,t_xtpz_report_table t_rt "
            + "where t_r.id=t_ar.report_id and t_r.id=t_rt.report_id and t_ar.table_id=?1 and t_rt.table_id=?1 and t_ar.component_version_id=?2 and t_ar.menu_id=?3 and t_ar.user_id=?4", nativeQuery = true)
    public List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * <p>描述: 删除报表配置</p>
     */
    @Transactional
    @Modifying
    @Query("delete AppReport t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4")
    public void deleteByFk(String tableId, String componentVersionId, String menuId, String userId);

    /**
     * <p>描述: 根据模块ID删除配置</p>
     * 
     * @param tableId 设定参数
     */
    @Transactional
    @Modifying
    @Query("delete AppReport t where t.tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * <p>描述: 根据报表ID删除配置</p>
     * 
     * @param reportId 设定参数
     */
    @Transactional
    @Modifying
    @Query("delete AppReport t where t.reportId=?1")
    public void deleteByReportId(String reportId);

    /**
     * <p>描述: 根据表ID删除配置</p>
     * 
     * @param tableId 设定参数
     */
    @Transactional
    @Modifying
    @Query("delete AppReport t where t.componentVersionId=?1")
    public void deleteByComponentVersionId(String componentVersionId);

    /**
     * <p>描述: 根据菜单ID删除配置</p>
     * 
     * @param tableId 设定参数
     */
    @Transactional
    @Modifying
    @Query("delete AppReport t where t.menuId=?1")
    public void deleteByMenuId(String menuId);

    /**
     * <p>描述: 根据表ID、模块Id、用户ID统计记录</p>
     */
    @Query("select count(id) from AppReport t where t.tableId=?1 and t.componentVersionId=?2 and t.menuId=?3 and t.userId=?4")
    public Long count(String tableId, String componentVersionId, String menuId, String userId);
}

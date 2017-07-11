package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import javax.persistence.Transient;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ReportTableRelation;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ReportTableRelationDao extends StringIDDao<ReportTableRelation> {

    /**
     * <p>标题: findByReportId</p>
     * <p>描述: </p>
     * 
     * @param @param reportId
     * @return List<Object[]> 返回类型
     *         [0-tableId,1-columnId,
     *         2-relateTableId,3-relateColumnId,
     *         4-tableComment,5-tableName,
     *         6-columnComment,7-columnName,
     *         8-relateTableComment,9-relateTableName,
     *         10-relateColumnComment,11-relateColumnName]
     * @throws
     */
    @Query(value = "SELECT TD_O.ID AS TAB_ID_O, CD_O.ID AS COL_ID_O, " + " TD_R.ID AS TAB_ID_R, CD_R.ID AS COL_ID_R, "
            + " TD_O.SHOW_NAME AS NAME_O, TD_O.TABLE_NAME AS TABLE_NAME_O, " + " CD_O.SHOW_NAME AS SHOW_NAME_O, CD_O.COLUMN_NAME AS COLUMN_NAME_O, "
            + " TD_R.SHOW_NAME AS NAME_R, TD_R.TABLE_NAME AS TABLE_NAME_R, " + " CD_R.SHOW_NAME AS SHOW_NAME_R, CD_R.COLUMN_NAME AS COLUMN_NAME_R  "
            + " FROM T_XTPZ_REPORT_TABLE_RELATION T_TR " + " JOIN T_XTPZ_PHYSICAL_TABLE_DEFINE TD_O ON (T_TR.TABLE_ID = TD_O.ID) "
            + " JOIN T_XTPZ_PHYSICAL_TABLE_DEFINE TD_R ON (T_TR.RELATE_TABLE_ID = TD_R.ID) " + " JOIN T_XTPZ_COLUMN_DEFINE CD_O ON (T_TR.COLUMN_ID = CD_O.ID) "
            + " JOIN T_XTPZ_COLUMN_DEFINE CD_R ON (T_TR.RELATE_COLUMN_ID = CD_R.ID) " + " WHERE T_TR.REPORT_ID = ?1 " + " ORDER BY T_TR.SHOW_ORDER ", nativeQuery = true)
    public List<Object[]> findByReportId(String reportId);

    /**
     * 根据报表ID获取该报表下表关系
     * 
     * @param reportId 报表ID
     * @return List<ReportTableRelation>
     */
    public List<ReportTableRelation> getByReportId(String reportId);

    /**
     * <p>标题: deleteByReportId</p>
     * <p>描述: </p>
     * 
     * @param reportId
     * @return void 返回类型
     * @throws
     */
    @Modifying
    @Transient
    @Query("DELETE FROM ReportTableRelation WHERE reportId=?1")
    public void deleteByReportId(String reportId);

    /**
     * qiucs 2013-8-12
     * <p>标题: countReportTables</p>
     * <p>描述: </p>
     * 
     * @param reportId
     * @return List<ReportTable> 返回类型
     * @throws
     */
    @Query(value = "SELECT COUNT(RT.ID) FROM T_XTPZ_REPORT_TABLE RT WHERE RT.REPORT_ID=?1", nativeQuery = true)
    public Object countReportTables(String reportId);

    /**
     * qiucs 2013-8-12
     * <p>标题: findReportTablesNotInRelation</p>
     * <p>描述: </p>
     * 
     * @param reportId
     * @return List<Object[]> 返回类型
     * @throws
     */
    @Query(value = "SELECT RT.TABLE_COMMENT " + " FROM T_XTPZ_REPORT_TABLE RT " + " WHERE RT.REPORT_ID=?1 "
            + " AND NOT EXISTS(SELECT 1 FROM T_XTPZ_REPORT_TABLE_RELATION RTR " + " WHERE RTR.REPORT_ID=?1 AND (RTR.TABLE_ID=RT.TABLE_ID "
            + " OR RTR.RELATE_TABLE_ID=RT.TABLE_ID)) ", nativeQuery = true)
    public List<String> findReportTablesNotInRelation(String reportId);

    /**
     * qiucs 2013-8-12
     * <p>标题: findTableRelation</p>
     * <p>描述: 查找已定义好的表关系</p>
     * 
     * @param reportId
     * @return List<Object[]> 返回类型
     * @throws
     */
    @Query(value = "SELECT TR.TABLE_ID, TR.COLUMN_ID, TR.RELATE_TABLE_ID, TR.RELATE_COLUMN_ID " + " FROM T_XTPZ_TABLE_RELATION TR "
            + " JOIN T_XTPZ_REPORT_TABLE RT1 ON(RT1.REPORT_ID=?1 AND RT1.TABLE_ID=TR.TABLE_ID) "
            + " JOIN T_XTPZ_REPORT_TABLE RT2 ON(RT2.REPORT_ID=?1 AND RT2.TABLE_ID=TR.RELATE_TABLE_ID) "
            + " WHERE NOT EXISTS(SELECT RTR.ID FROM T_XTPZ_REPORT_TABLE_RELATION RTR "
            + " WHERE RTR.REPORT_ID=?1 AND (RTR.TABLE_ID=TR.TABLE_ID OR RTR.TABLE_ID=TR.RELATE_TABLE_ID)) " + " ORDER BY TR.TABLE_ID ", nativeQuery = true)
    public List<Object[]> findTableRelation(String reportId);

    /**
     * qiucs 2013-11-27
     * <p>描述: 根据表ID删除配置</p>
     * 
     * @param tableId 设定参数
     */
    @Transactional
    @Modifying
    @Query("delete ReportTableRelation t where t.tableId=?1")
    public void deleteByTableId(String tableId);

    /**
     * qiucs 2013-11-27
     * <p>描述: 根据表ID删除配置</p>
     * 
     * @param tableId 设定参数
     */
    @Transactional
    @Modifying
    @Query("delete ReportTableRelation t where t.relateTableId=?1")
    public void deleteByRelateTableId(String tableId);

    /**
     * qiucs 2013-11-27
     * <p>描述: 根据字段ID删除配置</p>
     * 
     * @param tableId 设定参数
     */
    @Transactional
    @Modifying
    @Query("delete ReportTableRelation t where t.columnId=?1")
    public void deleteByColumnId(String columnId);

    /**
     * qiucs 2013-11-27
     * <p>描述: 根据字段ID删除配置</p>
     * 
     * @param tableId
     */
    @Transactional
    @Modifying
    @Query("delete ReportTableRelation t where t.relateColumnId=?1")
    public void deleteByRelateColumnId(String columnId);
}

package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import javax.persistence.Transient;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ReportDataSource;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ReportDataSourceDao extends StringIDDao<StringIDEntity> {

    /**
     * qiucs 2013-8-9 
     * <p>标题: findByReportIdOfOracle</p>
     * <p>描述: </p>
     * @param  reportId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" SELECT TAB.TABLE_ID, COL.COLUMN_ID, " +
    		" TAB.TABLE_COMMENT, TAB.TABLE_NAME, " +
    		" COL.COLUMN_COMMENT, COL.COLUMN_NAME " +
    		" FROM T_XTPZ_REPORT_TABLE TAB " +
    		" JOIN T_XTPZ_REPORT_COLUMN COL ON TAB.TABLE_ID=COL.TABLE_ID AND COL.REPORT_ID=?1 " +
    		" WHERE TAB.REPORT_ID=?1 " +
    		" ORDER BY TAB.SHOW_ORDER, COL.SHOW_ORDER ", nativeQuery=true)
    public List<Object[]> findByReportId(String reportId);
    
    /**
     * 根据报表ID获取ReportDataSource
     * 
     * @param reportId 报表ID
     * @return List<ReportDataSource>
     */
    @Query("from ReportDataSource where reportId=?1")
    public List<ReportDataSource> getByReportId(String reportId);
    
    /**
     * qiucs 2013-8-9 
     * <p>标题: deleteReportTableByReportId</p>
     * <p>描述: </p>
     * @param  reportId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Modifying
    @Transient
    @Query("delete from ReportTable where reportId=?1")
    public void deleteReportTableByReportId(String reportId);
    
    /**
     * qiucs 2013-8-9 
     * <p>标题: deleteReportColumnByReportId</p>
     * <p>描述: </p>
     * @param  reportId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Modifying
    @Transient
    @Query("delete from ReportColumn where reportId=?1")
    public void deleteReportColumnByReportId(String reportId);
    
    /**
     * qiucs 2013-8-7 
     * <p>标题: getReportTables</p>
     * <p>描述: 获取指定报表下的所有表和视图</p>
     * @param  reportId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" SELECT TAB.TABLE_ID, TAB.TABLE_COMMENT, TAB.TABLE_NAME " +
            " FROM T_XTPZ_REPORT_TABLE TAB " +
            " WHERE TAB.REPORT_ID=?1 " +
            " ORDER BY TAB.SHOW_ORDER ",
            nativeQuery=true)
    public List<Object[]> getReportTables(String reportId);
    
    
    /**
     * <p>标题: getReportTableColunms</p>
     * <p>描述: </p>
     * @param  reportId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" SELECT T_RC.COLUMN_ID, T_RC.COLUMN_COMMENT " + 
            " FROM T_XTPZ_REPORT_COLUMN T_RC " +
            " WHERE T_RC.REPORT_ID=?1 AND T_RC.TABLE_ID=?2 ",
            nativeQuery=true)
    public List<Object[]> getReportTableColunms(String reportId, String tableId);
    
    /**
     * qiucs 2013-10-16 
     * <p>描述: </p>
     * @param  tableId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="SELECT R.ID, R.NAME, RB.SHOW_ORDER FROM T_XTPZ_REPORT R " +
    		"JOIN T_XTPZ_REPORT_TABLE T ON(R.ID=T.REPORT_ID) " +
    		"LEFT JOIN T_XTPZ_REPORT_BINDING RB ON (RB.REPORT_ID=R.ID AND RB.TABLE_ID=?1 AND RB.MODULE_ID=?2) " +
    		"WHERE T.TABLE_ID=?1", nativeQuery=true)
    public List<Object[]> getReportByTableId(String tableId, String moduleId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete ReportDataSource t where t.tableId=?1")
    public void deleteByTableId(String tableId);
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据字段ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete ReportDataSource t where t.columnId=?1")
    public void deleteByColumnId(String columnId);
}

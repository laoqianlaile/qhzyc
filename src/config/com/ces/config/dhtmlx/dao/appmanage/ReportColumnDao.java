package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ReportColumn;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ReportColumnDao extends StringIDDao<ReportColumn> {

    /**
     * qiucs 2013-8-14 
     * <p>标题: resetCellColumn</p>
     * <p>描述: 重置报表数据源字段</p>
     * @param  reportId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE ReportColumn SET inCelled='N', rowIndex=null, colIndex=null " +
    		" WHERE reportId=?1 ")
    public void resetCellColumn(String reportId);
    
    /**
     * qiucs 2013-8-14 
     * <p>标题: updateCellColumn</p>
     * <p>描述: 更新字段在CELL报表中的位置</p>
     * @param  reportId
     * @param  columnId
     * @param  rowIndex
     * @param  colIndex    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE ReportColumn SET inCelled='Y', rowIndex=?3, colIndex=?4 " +
            " WHERE reportId=?1 AND columnId=?2 ")
    public void updateCellColumn(String reportId, String columnId, Integer rowIndex, Integer colIndex);
    
    /**
     * qiucs 2013-8-19 
     * <p>标题: findBindedColumns</p>
     * <p>描述: </p>
     * @param  reportId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    //@Query(value=" SELECT COLUMN_ID, ROW_INDEX, COL_INDEX FROM T_XTPZ_REPORT_COLUMN " +
    //		" WHERE REPORT_ID=?1 AND IN_CELLED='Y' ", nativeQuery=true)
    @Query("from  ReportColumn t where t.reportId=?1 and t.inCelled='Y' ")
    public List<ReportColumn> findBindedColumns(String reportId);
    
    /**
     * qiucs 2013-8-20 
     * <p>标题: findColumnComboByReportId</p>
     * <p>描述: 根据报表ID查找字段信息（下拉框用的）</p>
     * @param  reportId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="SELECT RC.COLUMN_ID, RT.TABLE_COMMENT, RC.COLUMN_COMMENT " +
    		" FROM T_XTPZ_REPORT_COLUMN RC " +
    		" JOIN T_XTPZ_REPORT_TABLE RT ON RC.TABLE_ID=RT.TABLE_ID AND RT.REPORT_ID=?1 " +
    		" WHERE RC.REPORT_ID=?1 ORDER BY RC.SHOW_ORDER ", nativeQuery=true)
    public List<Object[]> findColumnComboByReportId(String reportId);
    
    /**
     * qiucs 2013-8-20 
     * <p>标题: deleteByReportId</p>
     * <p>描述: 根据报表ID删除数据</p>
     * @param  reportId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Modifying
    @Transactional
    @Query(" DELETE FROM ReportColumn WHERE reportId=?1 ")
    public void deleteByReportId(String reportId);
    
    /**
     * <p>描述: 根据报表ID查找字段信息</p>
     * @param reportId
     * @return
     * @author Administrator
     * @date 2013-10-28  10:07:22
     */
    public List<ReportColumn> findByReportId(String reportId);
    
    /**
     * <p>描述: 根据报表ID查找要打印的字段信息</p>
     * @param reportId
     * @return
     * @author Administrator
     * @date 2013-10-28  10:08:42
     */
    @Query(" from ReportColumn where reportId=?1 and IN_CELLED='Y' group by rowIndex,colIndex ORDER BY rowIndex,colIndex")
    public List<ReportColumn> findPrintByReportId(String reportId);
    
    /**
     * <p>描述: 根据报表ID查找要打印的字段但不是排序字段的行、列号以及列名、别名等信息</p>
     * @param reportId
     * @return
     * @author Administrator
     * @date 2013-10-28  10:09:33
     */
    @Query(value="select t.row_index,t.col_index,t.table_id,t.column_name,t.column_alias " +
    		" from t_xtpz_report_column t where t.report_id=?1 and t.in_celled='Y' " +
    		" and t.id not in(select t2.id from t_xtpz_report_print_setting t1 " +
    		" join t_xtpz_report_column t2 on (t1.report_id=t2.report_id and t1.column_id=t2.column_id) " +
    		" where t1.report_id=?1 and t1.type='2')" +
    		" group by t.row_index,t.col_index,t.table_id,t.column_name,t.column_alias " +
    		" order by t.row_index,t.col_index",nativeQuery=true)
    public List<Object[]> getIndexSettigs(String reportId); 
    
    /**
     * <p>描述: 根据报表ID查找要打印的字段的行、列号以及列名、别名等信息</p>
     * @param reportId
     * @return
     * @author Administrator
     * @date 2013-10-28  10:19:05
     */
    @Query(value="select t.row_index,t.col_index,t.table_id,t.column_name,t.column_alias " +
    		" from t_xtpz_report_column t where t.report_id=?1 and t.in_celled='Y' " +
    		" group by t.row_index,t.col_index,t.table_id,t.column_name,t.column_alias " +
    		" order by t.row_index,t.col_index",nativeQuery=true)
    public List<Object[]> getLocationSettigs(String reportId);
    
    /**
     * <p>描述: 根据报表ID查找要打印的字段的字段名和字段类型等信息</p>
     * @param reportId
     * @return
     * @author Administrator
     * @date 2013-10-29  13:02:03
     */
    @Query(value="select t1.column_name,t2.data_type from t_xtpz_report_column t1 " +
    		" join t_xtpz_column_define t2 on t1.column_id=t2.id " +
    		" where t1.report_id=?1 and t1.in_celled='Y' ",nativeQuery=true)
    public List<Object[]> findDataTypeByReportId(String reportId);
    
    /**
     * <p>描述: 根据报表ID查找要打印的字段的字段名和编码类型等信息</p>
     * @param reportId
     * @return
     * @author Administrator
     * @date 2013-10-30  09:59:55
     */
    @Query(value="select t1.column_name,t2.code_type_code from t_xtpz_report_column t1 " +
    		" join t_xtpz_column_define t2 on t1.column_id=t2.id " +
    		" where t1.report_id=?1 and t1.in_celled='Y' ",nativeQuery=true)
    public List<Object[]> findCodeTypeByReportId(String reportId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete ReportColumn t where t.tableId=?1")
    public void deleteByTableId(String tableId);
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据字段ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete ReportColumn t where t.columnId=?1")
    public void deleteByColumnId(String columnId);
}

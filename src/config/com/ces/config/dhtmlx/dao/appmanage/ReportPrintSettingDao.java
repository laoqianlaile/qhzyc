package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ReportPrintSetting;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ReportPrintSettingDao extends StringIDDao<ReportPrintSetting>{

    /**
     * qiucs 2013-8-20 
     * <p>标题: deleteByReportIdAndType</p>
     * <p>描述: 根据报表ID、类型删除数据</p>
     * @param  reportId
     * @param  type    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM ReportPrintSetting WHERE reportId=?1 AND type=?2")
    public void deleteByReportIdAndType(String reportId, String type);
    
    /**
     * qiucs 2013-8-20 
     * <p>标题: deleteByReportId</p>
     * <p>描述: 根据报表ID删除数据</p>
     * @param  reportId
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM ReportPrintSetting WHERE reportId=?1 ")
    public void deleteByReportId(String reportId);
    
    /**
     * <p>描述: 根据报表ID获取打印设置数据</p>
     * @param reportId
     * @return
     * @author Administrator
     * @date 2013-10-28  10:25:13
     */
    @Query(value="select t1.type,t1.value,t2.table_id,t2.column_name,t2.row_index,t2.col_index " +
    		" from t_xtpz_report_print_setting t1 join t_xtpz_report_column t2 " +
    		" on (t1.report_id=t2.report_id and t1.column_id=t2.column_id) " +
    		" where t1.report_id=?1 order by t1.type, t1.show_order",nativeQuery=true)
    public List<Object[]> getSettigs(String reportId);
    
    /**
     * <p>描述: 根据报表ID获取打印设置数据</p>
     * @param reportId
     * @return
     * @author Administrator
     * @date 2013-10-28  10:25:13
     */
    @Query(value="select t1.type,t1.value,t2.table_id,t2.column_name,t2.row_index,t2.col_index " +
    		" from t_xtpz_report_print_setting t1 join t_xtpz_report_column t2 " +
    		" on (t1.report_id=t2.report_id and t1.column_id=t2.column_id) " +
    		" where t1.report_id=?1 and t1.type=?2 order by t1.show_order",nativeQuery=true)
    public List<Object[]> getSettigs(String reportId, String type);
    
    /**
     * 根据报表ID获取打印设置数据
     * 
     * @param reportId 报表ID
     * @return List<ReportPrintSetting>
     */
    public List<ReportPrintSetting> findByReportId(String reportId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据字段ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete ReportPrintSetting t where t.columnId=?1")
    public void deleteByColumnId(String columnId);
}

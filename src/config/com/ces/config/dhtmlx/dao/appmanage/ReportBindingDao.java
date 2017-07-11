package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import javax.persistence.Transient;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ReportBinding;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ReportBindingDao extends StringIDDao<ReportBinding> {

    /**
     * <p>标题: getDefaultBindingModule</p>
     * <p>描述: </p>
     * @param  reportId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" SELECT T_D.ID, T_D.NAME " + 
    		" FROM T_XTPZ_MODULE T_D " +
    		" LEFT JOIN T_XTPZ_REPORT_BINDING T_RB ON (T_RB.REPORT_ID = ?1 AND T_D.ID = T_RB.MODULE_ID) " +
    		" WHERE T_D.TYPE = '1' AND T_RB.ID IS NULL " +
    		" ORDER BY T_D.PARENT_ID ",
            nativeQuery=true)
    public List<Object[]> getDefaultBindingModule(String reportId);
    
    /**
     * <p>标题: getDefineBindingModule</p>
     * <p>描述: </p>
     * @param  reportId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" SELECT T_D.ID, T_D.NAME " + 
            " FROM T_XTPZ_MODULE T_D " +
            " JOIN T_XTPZ_REPORT_BINDING T_RB ON (T_D.ID = T_RB.MODULE_ID) " +
            " WHERE T_D.TYPE='1' AND T_RB.REPORT_ID = ?1 " +
            " ORDER BY T_RB.SHOW_ORDER ",
            nativeQuery=true)
    public List<Object[]> getDefineBindingModule(String reportId);
    
    /**
     * <p>标题: deleteByReportId</p>
     * <p>描述: </p>
     * @param  reportId
     * @return void    返回类型   
     * @throws
     */
    @Modifying
    @Transient
    @Query("delete from ReportBinding where reportId=?1")
    public void deleteByReportId(String reportId);
    
    /**
     * qiucs 2013-10-16 
     * <p>描述: </p>
     * @param  tableId
     * @param  moduleId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Modifying
    @Transient
    @Query("DELETE FROM ReportBinding WHERE tableId=?1 AND moduleId=?2")
    public void deleteByFk(String tableId, String moduleId);
    
    /**
     * qiucs 2013-10-16 
     * <p>描述: </p>
     * @param  tableId
     * @param  moduleId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="select r.id, r.name from t_xtpz_report_binding a " +
    		"join t_xtpz_report r on(r.id=a.report_id) " +
    		"where a.table_id=?1 and a.module_id=?2 order by r.show_order", nativeQuery=true)
    public List<Object[]> getReportByFk(String tableId, String moduleId);
    
    /**
     * 根据表ID和模块ID获取和报表的绑定关系
     * 
     * @param tableId 表ID
     * @param moduleId 模块ID
     * @return List<ReportBinding> 返回类型
     */
    public List<ReportBinding> findByTableIdAndModuleId(String tableId, String moduleId);
    
    /**
     * qiucs 2013-10-16 
     * <p>描述: </p>
     * @param  tableId
     * @param  moduleId
     * @return List<String>    返回类型   
     * @throws
     */
    @Query("SELECT T.reportId FROM ReportBinding T where T.tableId=?1 AND T.moduleId=?2 ORDER BY T.showOrder")
    public List<String> getReportIdsByFk(String tableId, String moduleId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete ReportBinding t where t.moduleId=?1")
    public void deleteByModuleId(String moduleId);
}

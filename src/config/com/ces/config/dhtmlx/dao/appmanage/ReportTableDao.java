package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ReportTable;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ReportTableDao extends StringIDDao<ReportTable> {
    
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
    @Query(" DELETE FROM ReportTable WHERE reportId=?1 ")
    public void deleteByReportId(String reportId);
    
    /**
     * qiucs 2013-8-27 
     * <p>描述: 查找报表关联的表信息</p>
     * @param  reportId
     * @return List<ReportTable>    返回类型   
     * @throws
     */
    public List<ReportTable> findByReportId(String reportId);
    
    /**
     * qiucs 2013-8-27 
     * <p>描述: 查找报表关联的表信息</p>
     * @param  tableId
     * @return List<ReportTable>    返回类型   
     * @throws
     */
    public List<ReportTable> findByTableId(String tableId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete ReportTable t where t.tableId=?1")
    public void deleteByTableId(String tableId);
}

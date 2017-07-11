package com.ces.config.dhtmlx.dao.appmanage;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ReportDefine;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ReportDefineDao extends StringIDDao<ReportDefine> {

    /**
     * qiucs 2013-8-20 
     * <p>标题: findByReportId</p>
     * <p>描述: </p>
     * @param  reportId
     * @return ReportDefine    返回类型   
     * @throws
     */
    public ReportDefine findByReportId(String reportId);
    
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
    @Query(" DELETE FROM ReportDefine WHERE reportId=?1 ")
    public void deleteByReportId(String reportId);
    
}

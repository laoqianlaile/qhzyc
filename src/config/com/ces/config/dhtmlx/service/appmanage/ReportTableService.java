package com.ces.config.dhtmlx.service.appmanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.ReportTableDao;
import com.ces.config.dhtmlx.entity.appmanage.ReportTable;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

@Component
public class ReportTableService extends ConfigDefineDaoService<ReportTable, ReportTableDao> {

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("reportTableDao")
    @Override
    protected void setDaoUnBinding(ReportTableDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 查找报表关联的表信息
     * 
     * @param reportId 报表ID
     * @return List<ReportTable> 返回类型
     */
    public List<ReportTable> findByReportId(String reportId) {
        return getDao().findByReportId(reportId);
    }
    
    /**
     * qiucs 2013-8-27 
     * <p>描述: 查找报表关联的表信息</p>
     * @param  tableId
     * @return List<ReportTable>    返回类型   
     * @throws
     */
    public List<ReportTable> findByTableId(String tableId) {
        return getDao().findByTableId(tableId);
    }

}

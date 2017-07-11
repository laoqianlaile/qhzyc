package com.ces.config.dhtmlx.service.appmanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.ReportColumnDao;
import com.ces.config.dhtmlx.entity.appmanage.ReportColumn;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

@Component
public class ReportColumnService extends ConfigDefineDaoService<ReportColumn, ReportColumnDao> {

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("reportColumnDao")
    @Override
    protected void setDaoUnBinding(ReportColumnDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 根据报表ID查找字段信息
     * 
     * @param reportId 报表ID
     * @return List<ReportColumn>
     */
    public List<ReportColumn> findByReportId(String reportId) {
        return getDao().findByReportId(reportId);
    }
}

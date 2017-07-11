package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.ces.config.dhtmlx.entity.appmanage.Report;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ReportPrintDao extends StringIDDao<Report> {    
    /**
     * <p>描述: 根据报表ID查询关联表的信息</p>
     * @param reportId
     * @return
     * @author Administrator
     * @date 2013-10-28  10:26:42
     */
    @Query(value="select t.table_id,t.column_id,t.relate_table_id,t.relate_column_id from t_xtpz_report_table_relation t where t.report_id=?1",nativeQuery=true)
    public List<Object[]> getReportRelation(String reportId);
}

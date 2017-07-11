package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_REPORT_BINDING")
public class ReportBinding extends StringIDEntity{

    private static final long serialVersionUID = -8732661950942165818L;

    private String reportId;
    
    private String tableId;
    
    private String moduleId;
    
    private Short showOrder;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
    
    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public Short getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Short showOrder) {
        this.showOrder = showOrder;
    }

    public void setOneRowValue(String reportId, String moduleId) {
        //Assert.state(StringUtil.isNotEmpty(oneRowValue), "实体属性不能为空！");
        
        //Assert.state(columnsValue.length == 2, "长度不匹配");

        setModuleId(moduleId);
        
        setReportId(reportId);
    }

}

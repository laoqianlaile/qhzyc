package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_REPORT_TABLE_RELATION")
public class ReportTableRelation extends StringIDEntity{

    private static final long serialVersionUID = -4450757339770087271L;

    private String reportId;
    
    private Short showOrder;
    
    private String tableId;
    
    private String columnId;
    
    private String relateTableId;
    
    private String relateColumnId;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Short getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Short showOrder) {
        this.showOrder = showOrder;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getRelateTableId() {
        return relateTableId;
    }

    public void setRelateTableId(String relateTableId) {
        this.relateTableId = relateTableId;
    }

    public String getRelateColumnId() {
        return relateColumnId;
    }

    public void setRelateColumnId(String relateColumnId) {
        this.relateColumnId = relateColumnId;
    }

    public void setOneRowValue(String reportId, String oneRowValue) {
        //Assert.state(StringUtil.isNotEmpty(oneRowValue), "实体属性不能为空！");
        
        String[] columnsValue = oneRowValue.split(",");
        
        //Assert.state(columnsValue.length == 2, "长度不匹配");

        setTableId(columnsValue[0]);
        setColumnId(columnsValue[1]);
        setRelateTableId(columnsValue[2]);
        setRelateColumnId(columnsValue[3]);
        
        setReportId(reportId);
    }

}

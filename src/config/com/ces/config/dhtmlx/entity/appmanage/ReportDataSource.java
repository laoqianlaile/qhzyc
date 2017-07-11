package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_REPORT_DATA_SOURCE")
public class ReportDataSource extends StringIDEntity{

    private static final long serialVersionUID = -4450757339770087271L;

    private String reportId;
    
    private String type; // 0-表 1-视图
    
    private String tableId; // 表ID（如果是视图，则tableId=null）
    
    private String tableName;
    
    private String tableComment;
    
    private String columnId;
    
    private String columnName;
    
    private String columnComment;
    
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

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public Short getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Short showOrder) {
        this.showOrder = showOrder;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public void setOneRowValue(String reportId, String oneRowValue) {
        //Assert.state(StringUtil.isNotEmpty(oneRowValue), "实体属性不能为空！");
        String[] columnsValue = oneRowValue.split(",");
        
        Assert.state(columnsValue.length == 7, "【报表数据源实体】长度不匹配");

        setTableId(columnsValue[0]);
        setColumnId(columnsValue[1]);
        
        setType(columnsValue[2]);
        
        setTableComment(columnsValue[3]);
        setTableName(columnsValue[4]);
        
        setColumnComment(columnsValue[5]);
        setColumnName(columnsValue[6]);
        
        setReportId(reportId);
    }

}

package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_REPORT_COLUMN")
public class ReportColumn extends StringIDEntity {

    private static final long serialVersionUID = -4450757339770087271L;
    /** 外键：报表ID*/
    private String reportId;
    /** 显示顺序*/
    private Integer showOrder;
    /** 外键：表ID*/
    private String tableId; 
    /** 外键：字段ID*/
    private String columnId;
    /** 字段名称*/
    private String columnName;
    /** 字段注释*/
    private String columnComment;
    /** 字段别名*/
    private String columnAlias;
    /** 是否为CELL报表中的字段：Y-是  N-否*/
    private String inCelled = "N";
    /** 在CELL报表中的行索引*/
    private Integer rowIndex;
    /** 在CELL报表中的列索引*/
    private Integer colIndex;
    /** 是否作为过滤条件：Y-是 N-否*/
    //private String conditioned;
    /** 字段作为过滤条件时，接收参数索引位置（从0开始）*/
    private Integer conditionIndex;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
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

    public String getColumnName() {
        return columnName == null ? "" : columnName.toUpperCase();
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

    public String getColumnAlias() {
        return columnAlias;
    }

    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    public String getInCelled() {
        return inCelled;
    }

    public void setInCelled(String inCelled) {
        this.inCelled = inCelled;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColIndex() {
        return colIndex;
    }

    public void setColIndex(Integer colIndex) {
        this.colIndex = colIndex;
    }

    public Integer getConditionIndex() {
        return conditionIndex;
    }

    public void setConditionIndex(Integer conditionIndex) {
        this.conditionIndex = conditionIndex;
    }

    /**
     * qiucs 2013-8-9 
     * <p>标题: setOneRowValue</p>
     * <p>描述: 设置属性值</p>
     * @param  reportId
     * @param  oneRowValue    设定参数   
     * @return void    返回类型   
     * @throws
     */
    public void setOneRowValue(String reportId, String oneRowValue) {
        String[] propsValue = oneRowValue.split(",");

        Assert.state(propsValue.length == 4, "【报表数据源字段实体】属性值数组长度不匹配");
        setTableId(propsValue[0]);
        setColumnId(propsValue[1]);
        setColumnComment(propsValue[2]);
        setColumnName(propsValue[3]);
        
        Assert.state(getShowOrder() != null, "【报表数据源字段实体】显示顺序不可为空");
        
        setColumnAlias("C_" + StringUtil.fillZero(3, getShowOrder()));

        setReportId(reportId);
    }

}

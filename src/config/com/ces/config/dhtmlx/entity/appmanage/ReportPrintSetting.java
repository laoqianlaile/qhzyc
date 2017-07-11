package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name="T_XTPZ_REPORT_PRINT_SETTING")
public class ReportPrintSetting extends StringIDEntity{

    private static final long serialVersionUID = 6733402430191053383L;
    
    private String reportId;
    /** 类型: 1-条件字段  2-分组字段  3-排序字段*/
    private String type;
    
    private Integer showOrder;
    
    private String columnId;
    
    private String value;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * qiucs 2013-8-19 
     * <p>标题: setOneRowValue</p>
     * <p>描述: 设置属性值</p>
     * @param  reportId
     * @param  type
     * @param  oneRowValue    设定参数   
     * @return void    返回类型   
     * @throws
     */
    public void setOneRowValue(String reportId, String type, String oneRowValue) {
        // 
        setReportId(reportId);
        setType(type);
        // 
        String[] propsValue = oneRowValue.split(",");
        setColumnId(propsValue[0]);
        if (propsValue.length > 1) {
            setValue(propsValue[1]);
        }
    }

}

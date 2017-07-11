package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_REPORT_DEFINE")
public class ReportDefine extends StringIDEntity{

    private static final long serialVersionUID = -8732661950942165818L;

    private String reportId;
    /** 表头行号：开始行号*/
    private Integer headerStart; 
    /** 表头行号：结束行号*/
    private Integer headerEnd;
    /** 循环行号：开始行号*/
    private Integer cycleStart;
    /** 循环行号：结束行号*/
    private Integer cycleEnd;
    /** 表尾行号：开始行号*/
    private Integer tailStart;
    /** 表尾行号：结束行号*/
    private Integer tailEnd;
    /** 末页行号：开始行号*/
    private Integer lastStart;
    /** 末页行号：结束行号*/
    private Integer lastEnd;
    /** 条件类型：0-全部 1-个别*/
    //private String conditionType;
    /** 序号列：行索引*/
    private Integer rnRowIndex;
    /** 序号列：列索引*/
    private Integer rnColIndex;
    
    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Integer getHeaderStart() {
        return headerStart;
    }

    public void setHeaderStart(Integer headerStart) {
        this.headerStart = headerStart;
    }

    public Integer getHeaderEnd() {
        return headerEnd;
    }

    public void setHeaderEnd(Integer headerEnd) {
        this.headerEnd = headerEnd;
    }

    public Integer getCycleStart() {
        return cycleStart;
    }

    public void setCycleStart(Integer cycleStart) {
        this.cycleStart = cycleStart;
    }

    public Integer getCycleEnd() {
        return cycleEnd;
    }

    public void setCycleEnd(Integer cycleEnd) {
        this.cycleEnd = cycleEnd;
    }

    public Integer getTailStart() {
        return tailStart;
    }

    public void setTailStart(Integer tailStart) {
        this.tailStart = tailStart;
    }

    public Integer getTailEnd() {
        return tailEnd;
    }

    public void setTailEnd(Integer tailEnd) {
        this.tailEnd = tailEnd;
    }

    public Integer getLastStart() {
        return lastStart;
    }

    public void setLastStart(Integer lastStart) {
        this.lastStart = lastStart;
    }

    public Integer getLastEnd() {
        return lastEnd;
    }

    public void setLastEnd(Integer lastEnd) {
        this.lastEnd = lastEnd;
    }

    public Integer getRnRowIndex() {
        return rnRowIndex;
    }

    public void setRnRowIndex(Integer rnRowIndex) {
        this.rnRowIndex = rnRowIndex;
    }

    public Integer getRnColIndex() {
        return rnColIndex;
    }

    public void setRnColIndex(Integer rnColIndex) {
        this.rnColIndex = rnColIndex;
    }

    /*public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }//*/
    
}

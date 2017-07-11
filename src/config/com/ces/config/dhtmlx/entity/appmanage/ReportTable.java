package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
/**
 * <p>描述: 报表自定义（绑定物理表或视图）</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-8-9 上午9:39:13
 */
@Entity
@Table(name = "T_XTPZ_REPORT_TABLE")
public class ReportTable extends StringIDEntity {

    private static final long serialVersionUID = -4450757339770087271L;
    /** 外键：报表ID*/
    private String reportId;
    /** 显示顺序*/
    private Integer showOrder;
    /** 外键：表ID*/
    private String tableId; // 表ID（如果是视图，则tableId=tableName）
    /** 表名称*/
    private String tableName;
    /** 表注释*/
    private String tableComment;
    /** 表别名*/
    private String tableAlias;

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

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
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

        Assert.state(propsValue.length == 3, "【报表数据源表实体】属性值数组长度不匹配");
        setTableId(propsValue[0]);
        setTableComment(propsValue[1]);
        setTableName(propsValue[2]);
        
        Assert.state(getShowOrder() != null, "【报表数据源字段实体】显示顺序不可为空");
        setTableAlias("T_" + StringUtil.fillZero(3, getShowOrder()));

        setReportId(reportId);
    }

}

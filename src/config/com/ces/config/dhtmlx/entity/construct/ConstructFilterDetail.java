package com.ces.config.dhtmlx.entity.construct;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 数据过滤条件详情实体类
 * 
 * @author wanglei
 * @date 2015-05-20
 */
@Entity
@Table(name = "T_XTPZ_CONSTRUCT_FILTER_DETAIL")
public class ConstructFilterDetail extends StringIDEntity {

    private static final long serialVersionUID = -6288647935714319812L;

    /** 数据过滤条件ID[ConstructFilter.id] */
    private String constructFilterId;

    /** 过滤字段ID */
    private String columnId;

    /** 运算符：GT:大于；EQ:等于；LT:小于；LIKE:包含；NOT:不等于；GTE:大于等于；LTE:小于等于； */
    private String operator;

    /** 过滤值 */
    private String value;

    /** 显示顺序 */
    private Integer showOrder;

    /** 关系：and/or */
    private String relation;

    /** 左括号 */
    private String leftParenthesis;

    /** 右括号 */
    private String rightParenthesis;

    public String getConstructFilterId() {
        return constructFilterId;
    }

    public void setConstructFilterId(String constructFilterId) {
        this.constructFilterId = constructFilterId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getLeftParenthesis() {
        return leftParenthesis;
    }

    public void setLeftParenthesis(String leftParenthesis) {
        this.leftParenthesis = leftParenthesis;
    }

    public String getRightParenthesis() {
        return rightParenthesis;
    }

    public void setRightParenthesis(String rightParenthesis) {
        this.rightParenthesis = rightParenthesis;
    }

    @Transient
    public String getOperatesArea() {
        return "删除^javascript:deleteFilterDetailRow(\"" + id + "\")^_self";
    }

}

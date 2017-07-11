package com.ces.config.dhtmlx.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 数据权限详情实体类（三权分立）
 * 
 * @author wanglei
 * @date 2015-07-21
 */
@Entity
@Table(name = "T_XTPZ_AUTH_DATA_DETAIL_COPY")
public class AuthorityDataDetailCopy extends StringIDEntity implements Comparable<AuthorityDataDetailCopy> {

    private static final long serialVersionUID = -6288647935714319812L;

    /** 数据权限ID[AuthorityData.id] * */
    private String authorityDataId;

    /** 表ID */
    private String tableId;

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

    public String getAuthorityDataId() {
        return authorityDataId;
    }

    public void setAuthorityDataId(String authorityDataId) {
        this.authorityDataId = authorityDataId;
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
        return "删除^javascript:deleteAuthorityDataDetailRow(\"" + id + "\")^_self";
    }

    @Override
    public int compareTo(AuthorityDataDetailCopy o) {
        if (o == null || !this.getTableId().equals(o.getTableId())) {
            return 0;
        } else {
            return this.getShowOrder().compareTo(o.getShowOrder());
        }
    }

}

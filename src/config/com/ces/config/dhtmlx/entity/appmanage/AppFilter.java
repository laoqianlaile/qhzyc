package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name="T_XTPZ_APP_FILTER")
public class AppFilter extends StringIDEntity {

    private static final long serialVersionUID = -3295821326458620201L;
    /** 关联表ID*/
    private String tableId;
    /** 关联自定义构件ID*/
    private String componentVersionId;
    /** 关联菜单ID*/
    private String menuId;
    /** 显示顺序*/
    private Integer showOrder;
    /** 关联表中字段ID*/
    private String columnId;
    /** 查询方式： >:大于;=: 等于;<:小于;like:包含;<>:不等于；>=:大于等于；<=:小于等于 .*/
    private String filterType;
    /** 过滤值.*/
    private String value;
    /** 字段英文名称.*/
    private String columnName;
    /** 条件关系：and/or；拼条件时，放在字段前面位置（如果 and name='qiucs'）.*/
    private String relation;
    /** 左括号.*/
    private String leftParenthesis;
    /** 右括号.*/
    private String rightParenthesis;
    
    public String getTableId() {
        return tableId;
    }
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
    public String getComponentVersionId() {
        return componentVersionId;
    }
    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
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
    public String getFilterType() {
        return filterType;
    }
    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
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
    /************************(辅助属性-不保存数据库)*************************/
    private String showName;
    
    private String dataType;
    
    private String codeTypeCode;
    @Transient
    public String getShowName() {
        return showName;
    }
    public void setShowName(String showName) {
        this.showName = showName;
    }
    @Transient
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    @Transient
    public String getCodeTypeCode() {
        return codeTypeCode;
    }
    public void setCodeTypeCode(String codeTypeCode) {
        this.codeTypeCode = codeTypeCode;
    }
}

package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_APP_SORT")
public class AppSort extends StringIDEntity {

    private static final long serialVersionUID = -5749490162215532139L;

    /** 关联表ID*/
    private String tableId;
    /** 关联模块菜单ID*/
    private String componentVersionId;
    
    private String menuId;
    /** 用户ID*/
    private String userId;
    /** 显示顺序*/
    private Integer showOrder;
    /** 关联表中字段ID*/
    private String columnId;
    /** 排序方式：asc-升序 desc-降序*/
    private String sortType;
    /** 字段英文名称.*/
    private String columnName;
    /** 字段中文名称.*/
    private String showName;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
    
    @Transient
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Transient
    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public void setOneRowValue(String oneRowValue) {
        if (null == oneRowValue || "".equals(oneRowValue)) {
            return;
        }
        String[] columnsValue = oneRowValue.split(",");
        if (columnsValue.length != 2) return;
        
        setColumnId(columnsValue[0]);
        setSortType(columnsValue[1]);
    }

}

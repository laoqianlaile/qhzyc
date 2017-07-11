package com.ces.config.dhtmlx.entity.component;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件版本、构件表、关联字段三者关联关系实体类
 * 
 * @author wanglei
 * @date 2013-08-16
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_TABLE_COLUMN")
public class ComponentTableColumnRelation extends StringIDEntity {

    private static final long serialVersionUID = 7409124375675734064L;

    /** * 构件版本ID */
    private String componentVersionId;

    /** * 构件关联表ID */
    private String tableId;

    /** * 构件关联表字段 */
    private String columnId;

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
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

}

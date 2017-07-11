package com.ces.config.dhtmlx.entity.construct;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 数据过滤条件实体类
 * 
 * @author wanglei
 * @date 2015-05-20
 */
@Entity
@Table(name = "T_XTPZ_CONSTRUCT_FILTER")
public class ConstructFilter extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 上层的组合构件的版本ID */
    private String topComVersionId;

    /** * 控制权限的构件版本ID */
    private String componentVersionId;

    /** * 表ID */
    private String tableId;

    public String getTopComVersionId() {
        return topComVersionId;
    }

    public void setTopComVersionId(String topComVersionId) {
        this.topComVersionId = topComVersionId;
    }

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
}
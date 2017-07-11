package com.ces.config.dhtmlx.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_AUTHORITY_REPORT")
public class AuthorityReport extends StringIDEntity {

    private static final long serialVersionUID = 4309112662738863828L;

    /** 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID. */
    private String objectId;

    /** 类型：0-角色 1-人员. */
    private String objectType;

    /** 不可用的报表ID. */
    private String reportId;

    /** 表ID. */
    private String tableId;

    /** 包含该报表的菜单ID **/
    private String menuId;

    /** 对应构件版本ID **/
    private String componentVersionId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }
}

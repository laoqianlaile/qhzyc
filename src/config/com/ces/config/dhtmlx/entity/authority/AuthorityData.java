package com.ces.config.dhtmlx.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 数据权限实体类
 * 
 * @author wanglei
 * @date 2014-09-25
 */
@Entity
@Table(name = "T_XTPZ_AUTHORITY_DATA")
public class AuthorityData extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** 本菜单默认权限：componentVersionId默认值 */
    public static final String DEFAULT_ID = "-1";

    /** 数据权限名称 */
    private String name;

    /** 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID. */
    private String objectId;

    /** 类型：0-角色 1-用户 */
    private String objectType;

    /** 对应菜单ID **/
    private String menuId;

    /** 构件版本Id */
    private String componentVersionId;

    /** 表ID */
    private String tableId;

    /** 控制表IDs，对应AuthorityDataDetail中tableId */
    private String controlTableIds;

    /** 显示顺序 */
    private Integer showOrder;

    /** 表名称 */
    private String tableName;

    /** 控制表名称s */
    private String controlTableNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getControlTableIds() {
        return controlTableIds;
    }

    public void setControlTableIds(String controlTableIds) {
        this.controlTableIds = controlTableIds;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    @Transient
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Transient
    public String getControlTableNames() {
        return controlTableNames;
    }

    public void setControlTableNames(String controlTableNames) {
        this.controlTableNames = controlTableNames;
    }

}
package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_XTPZ_APP_SEARCH_PANEL")
public class AppSearchPanel extends StringIDEntity implements Cloneable{

    private static final long serialVersionUID = 2808135897397565590L;
    /** 表ID */
    private String tableId;
    /** 模块ID */
    private String componentVersionId;
    /** 菜单ID 默认为-1（表示按构件配置），否则为按菜单配置 */
    private String menuId;
    /** 模块ID */
    private String userId;
    /** 占用列数 */
    private Integer colspan = 1;

    @JsonIgnore
    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @JsonIgnore
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

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

	@Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
}

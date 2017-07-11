package com.ces.config.dhtmlx.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 开发的构件按钮权限实体类
 * 
 * @author wanglei
 * @date 2014-07-31
 */
@Entity
@Table(name = "T_XTPZ_AUTHORITY_BUTTON_COM")
public class AuthorityComponentButton extends StringIDEntity {

    private static final long serialVersionUID = -5044398255688671937L;

    /** 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID */
    private String objectId;

    /** 类型：0-角色 1-人员 */
    private String objectType;

    /** 绑定构件的菜单ID */
    private String menuId;

    /** 开发的构件版本ID */
    private String componentVersionId;

    /** 开发构件中按钮ID */
    private String componentButtonId;

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

    public String getComponentButtonId() {
        return componentButtonId;
    }

    public void setComponentButtonId(String componentButtonId) {
        this.componentButtonId = componentButtonId;
    }
}

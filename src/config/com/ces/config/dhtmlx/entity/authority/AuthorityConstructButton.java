package com.ces.config.dhtmlx.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件组装按钮权限实体类
 * 
 * @author wanglei
 * @date 2014-05-08
 */
@Entity
@Table(name = "T_XTPZ_AUTHORITY_BUTTON_CON")
public class AuthorityConstructButton extends StringIDEntity {

    private static final long serialVersionUID = -1267439902511576941L;

    /** 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID */
    private String objectId;

    /** 类型：0-角色 1-人员 */
    private String objectType;

    /** 绑定组合构件的菜单ID */
    private String menuId;

    /** 组合构件版本ID **/
    private String componentVersionId;

    /** 预留区和构件绑定关系ID */
    private String constructDetailId;

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

    public String getConstructDetailId() {
        return constructDetailId;
    }

    public void setConstructDetailId(String constructDetailId) {
        this.constructDetailId = constructDetailId;
    }
}

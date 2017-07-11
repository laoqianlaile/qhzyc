package com.ces.config.dhtmlx.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 菜单权限实体类
 * 
 * @author wanglei
 * @date 2014-09-25
 */
@Entity
@Table(name = "T_XTPZ_AUTHORITY")
public class Authority extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** 角色类型值 */
    public static final String OT_ROLE = "0";

    /** 用户类型值 */
    public static final String OT_USER = "1";

    /** 对象ID */
    private String objectId;

    /** 类型：0-角色 1-人员 2-部门 */
    private String objectType;

    /** 菜单ID */
    private String menuId;

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

}

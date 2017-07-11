package com.ces.config.dhtmlx.entity.release;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 系统发布菜单实体类
 * 
 * @author wanglei
 * @date 2014-10-13
 */
@Entity
@Table(name = "T_XTPZ_SYSTEM_RELEASE_MENU")
public class ReleaseMenu extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** * 系统发布ID */
    private String systemReleaseId;

    /** * 菜单ID */
    private String menuId;

    /** * 菜单名称 */
    private String menuName;

    /** * 系统ID */
    private String rootMenuId;

    /** * 父菜单ID */
    private String parentMenuId;

    public String getSystemReleaseId() {
        return systemReleaseId;
    }

    public void setSystemReleaseId(String systemReleaseId) {
        this.systemReleaseId = systemReleaseId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getRootMenuId() {
        return rootMenuId;
    }

    public void setRootMenuId(String rootMenuId) {
        this.rootMenuId = rootMenuId;
    }

    public String getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

}
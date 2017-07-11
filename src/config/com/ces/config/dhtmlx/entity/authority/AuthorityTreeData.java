package com.ces.config.dhtmlx.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 树数据权限实体类（静态字段节点的过滤条件合集作为表节点的过滤条件）
 * 
 * @author wanglei
 * @date 2015-06-08
 */
@Entity
@Table(name = "T_XTPZ_AUTHORITY_TREE_DATA")
public class AuthorityTreeData extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** 对象ID * */
    private String objectId;

    /** 类型：0-角色 1-人员 2-部门 * */
    private String objectType;

    /** 构件版本Id * */
    private String componentVersionId;

    /** 包含自定义构件的菜单ID * */
    private String menuId;

    /** 树权限作为数据权限 0：不作为 1：作为 * */
    private String controlDataAuth;

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

    public String getControlDataAuth() {
        return controlDataAuth;
    }

    public void setControlDataAuth(String controlDataAuth) {
        this.controlDataAuth = controlDataAuth;
    }

}

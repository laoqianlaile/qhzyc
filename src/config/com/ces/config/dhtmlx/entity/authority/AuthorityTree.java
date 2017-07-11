package com.ces.config.dhtmlx.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 树权限实体类
 * 
 * @author wanglei
 * @date 2014-09-25
 */
@Entity
@Table(name = "T_XTPZ_AUTHORITY_TREE")
public class AuthorityTree extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** 对象ID * */
    private String objectId;

    /** 类型：0-角色 1-人员 2-部门 * */
    private String objectType;

    /** 树节点ID * */
    private String treeNodeId;

    /** 构件版本Id * */
    private String componentVersionId;

    /** 包含自定义构件的菜单ID * */
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

    public String getTreeNodeId() {
        return treeNodeId;
    }

    public void setTreeNodeId(String treeNodeId) {
        this.treeNodeId = treeNodeId;
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

}

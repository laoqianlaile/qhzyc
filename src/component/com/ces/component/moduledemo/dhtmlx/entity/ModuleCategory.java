package com.ces.component.moduledemo.dhtmlx.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_COMP_MODULE_CATEGORY")
public class ModuleCategory extends StringIDEntity {

    private static final long serialVersionUID = -1084471451507575346L;

    /** * 父节点ID */
    private String parentId;

    /** * 名称 */
    private String name;

    /** * 是否包含子类别 */
    private Boolean hasChild;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

}

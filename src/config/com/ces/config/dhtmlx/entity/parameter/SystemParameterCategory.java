package com.ces.config.dhtmlx.entity.parameter;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 系统参数类别实体类
 * 
 * @author wanglei
 * @date 2013-08-12
 */
@Entity
@Table(name = "T_XTPZ_SYSTEM_PARAM_CATEGORY")
public class SystemParameterCategory extends StringIDEntity {

    private static final long serialVersionUID = -1084471451507575346L;

    /** * 父节点ID */
    private String parentId;

    /** * 系统参数类别名称 */
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

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

}

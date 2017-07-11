package com.ces.config.dhtmlx.entity.completecomponent;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 成品构件分类实体类
 * 
 * @author wanglei
 * @date 2014-02-17
 */
@Entity
@Table(name = "T_XTPZ_COMP_COMPONENT_AREA")
public class CompleteComponentArea extends StringIDEntity {

    private static final long serialVersionUID = -7886312178670223328L;

    /** * 构件分类名称 */
    private String name;

    /** * 显示顺序 */
    private Integer showOrder;

    /** * 父分类ID */
    private String parentId;

    /** * 是否包含子分类 */
    private Boolean hasChild;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }
}

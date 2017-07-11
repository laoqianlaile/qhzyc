package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_WORKFLOW_TREE")
public class WorkflowTree extends StringIDEntity {

    private static final long serialVersionUID = 1078599883584203924L;
    // 父节点
    private String parentId;
    // 名称
    private String name;
    
    private Integer showOrder;
    
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

    public Integer getShowOrder() {
        return showOrder;
    }
    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }
}

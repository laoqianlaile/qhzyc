package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_TABLE_TREE")
public class TableTree extends StringIDEntity {

    private static final long serialVersionUID = 1078599883584203924L;
    // 父节点
    private String parentId;
    // 名称
    private String name; 
    // 类型：0-逻辑表；1-物理表；2-逻辑表组；3-物理表组 4-视图
    private String nodeType;
    // 代码（预留字段）
    private String code;
    // 分类标签
    private String typeLabel;
    // 表分类代码
    private String classification;
    // 显示顺序
    private Integer showOrder;
    // 物理表前缀
    private String tablePrefix;
    
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
    public String getNodeType() {
        return nodeType;
    }
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
    public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Integer getShowOrder() {
        return showOrder;
    }
    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getTablePrefix() {
		return tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
}

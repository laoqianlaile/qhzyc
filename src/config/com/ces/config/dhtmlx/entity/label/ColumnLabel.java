package com.ces.config.dhtmlx.entity.label;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_COLUMN_LABEL")
public class ColumnLabel extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** 分类Id **/
    private String categoryId;

    /** 属性编码 **/
    private String code;

    /** 属性名称 **/
    private String name;

    /** 显示顺序 **/
    private Integer showOrder;
    
    /** 默认关联编码 **/
    private String codeTypeCode;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

	public String getCodeTypeCode() {
		return codeTypeCode;
	}

	public void setCodeTypeCode(String codeTypeCode) {
		this.codeTypeCode = codeTypeCode;
	}

}

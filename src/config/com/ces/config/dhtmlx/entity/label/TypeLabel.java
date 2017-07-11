package com.ces.config.dhtmlx.entity.label;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_TYPE_LABEL")
public class TypeLabel extends StringIDEntity {

    private static final long serialVersionUID = 1L;

    /** 编码 **/
    private String code;

    /** 名称 **/
    private String name;

    /** 显示顺序 **/
    private Integer showOrder;
    
    /** 菜单ID **/
    private String menuId;

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

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	@Transient
    public Boolean getChild() {
        return false;
    }
}

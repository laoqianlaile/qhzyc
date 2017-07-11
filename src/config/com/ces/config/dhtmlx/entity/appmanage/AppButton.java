package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_APP_BUTTON")
public class AppButton extends StringIDEntity {

    private static final long serialVersionUID = -5749490162215532139L;
    /** 0-表单按钮*/
    public static final String BUTTON_FORM = "0";
    /** 1-列表按钮*/
    public static final String BUTTON_GRID = "1";
    /** 关联表ID*/
    private String tableId;
    /** 版本构件ID*/
    private String componentVersionId;
    /** 菜单ID*/
    private String menuId;
    /** 按钮构件ID*/
    /** 类型:0-表单按钮 1-列表按钮*/
    private String buttonType;
    /** 按钮编码*/
    private String buttonCode;
    /** 按钮名称*/
    private String buttonName;
    /** 按钮样式*/
    private String buttonClass;
    /** 按钮显示名称*/
    private String showName;
    /** 显示顺序*/
    private Integer showOrder;
    /** 是否显示: 0-否;1-是*/
    private String display = "1";
    /** 备注*/
    private String remark;
    
    public String getTableId() {
        return tableId;
    }
    public void setTableId(String tableId) {
        this.tableId = tableId;
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
	public String getButtonType() {
        return buttonType;
    }
    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }
    public String getButtonCode() {
        return buttonCode;
    }
    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }
    public String getButtonName() {
        return buttonName;
    }
    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }
    public String getButtonClass() {
		return buttonClass;
	}
	public void setButtonClass(String buttonClass) {
		this.buttonClass = buttonClass;
	}
	public String getShowName() {
        return showName;
    }
    public void setShowName(String showName) {
        this.showName = showName;
    }
    public Integer getShowOrder() {
        return showOrder;
    }
    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }
    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}

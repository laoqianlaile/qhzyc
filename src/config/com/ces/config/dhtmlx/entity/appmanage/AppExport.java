package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_APP_EXPORT")
public class AppExport extends StringIDEntity {

	private static final long serialVersionUID = 1L;

	private String tableId;// 关联表ID

	private String componentVersionId;// 版本构件ID

	private String menuId;// 关联模块ID

	private String columnId;// 字段ID
	
	private String columnName;// 字段名称
	
	private String otherShowName;// 中文别名
	
	private String otherColName;// 字段别名(导入其他系统时字段名称)

	private Integer showOrder;

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

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getOtherShowName() {
		return otherShowName;
	}

	public void setOtherShowName(String otherShowName) {
		this.otherShowName = otherShowName;
	}

	public String getOtherColName() {
		return otherColName;
	}

	public void setOtherColName(String otherColName) {
		this.otherColName = otherColName;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}
	
	// 辅助属性不保存数据库
	private String showName;

	@Transient
	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
}

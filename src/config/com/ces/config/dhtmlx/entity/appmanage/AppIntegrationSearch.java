package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_APP_INTEGRATION_SEARCH")
public class AppIntegrationSearch extends StringIDEntity implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	/** 表ID */
	private String tableId;
	
	/** 版本构件ID */
	private String componentVersionId;
	
	/** 菜单ID */
	private String menuId;
	
	/** 关联字段ID */
	private String columnId;
	
	/** 显示顺序 */
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

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}
	
	// 辅助属性不保存数据库
		private String columnName;
		private String showName;

		@Transient
		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		@Transient
		public String getShowName() {
			return showName;
		}

		public void setShowName(String showName) {
			this.showName = showName;
		}

		@Override
		public Object clone() {
			try {
				return super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return null;
		}

}
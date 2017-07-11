package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 逻辑表在逻辑表组中的表关系表
 * 
 * @author qiujinwei
 */
@Entity
@Table(name = "T_XTPZ_LOGIC_TABLE_RELATION")
public class LogicTableRelation extends StringIDEntity {

	private static final long serialVersionUID = 1L;

	/** 逻辑表组编码 */
	private String groupCode;

	/** 逻辑表编码 */
	private String tableCode;

	/** 逻辑表关系列 */
	private String columnId;

	/** 父逻辑表编码 */
	private String parentTableCode;

	/** 父逻辑表关系列 */
	private String parentColumnId;

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getParentTableCode() {
		return parentTableCode;
	}

	public void setParentTableCode(String parentTableCode) {
		this.parentTableCode = parentTableCode;
	}

	public String getParentColumnId() {
		return parentColumnId;
	}

	public void setParentColumnId(String parentColumnId) {
		this.parentColumnId = parentColumnId;
	}
	
	public void setOneRowValue(String oneRowValue) {
        String[] columnsValue = oneRowValue.split("'");
        setColumnId(columnsValue[1]);
        setParentColumnId(columnsValue[0]);
    }

}

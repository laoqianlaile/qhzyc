package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_TABLE_RELATION")
public class TableRelation extends StringIDEntity{

	private static final long serialVersionUID = -6352319787034117540L;
	/** 表ID * */
	private String tableId;
	/** 表字段ID * */
	private String columnId;
	/** 关联表ID * */
	private String relateTableId;
	/** 关联表字段ID * */
	private String relateColumnId;

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getRelateTableId() {
		return relateTableId;
	}

	public void setRelateTableId(String relateTableId) {
		this.relateTableId = relateTableId;
	}

	public String getRelateColumnId() {
		return relateColumnId;
	}

	public void setRelateColumnId(String relateColumnId) {
		this.relateColumnId = relateColumnId;
	}
	
	
    public void setOneRowValue(String oneRowValue) {
        String[] columnsValue = oneRowValue.split("'");
        setColumnId(columnsValue[0]);
        setRelateColumnId(columnsValue[1]);
    }
	
	

}

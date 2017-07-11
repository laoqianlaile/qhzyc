package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 字段截取
 * @author wang
 *
 */

@Entity
@Table(name = "T_XTPZ_COLUMN_SPLIT")
public class ColumnSplit extends StringIDEntity{
	private static final long serialVersionUID = 1L;
	
	/*字段关系ID	*/
	private String columnRelationId;
	/*	表ID*/
	private String tableId;
	/*名称	*/
	private String name;
	/*源字段ID	*/
	private String fromColumnId;
	/*目标字段ID	*/
	private String toColumnId;
	/*起始位置	*/
	private Integer startPosition;
	/*结束位置	*/
	private Integer endPosition;
	
	
	
	
	public String getColumnRelationId() {
		return columnRelationId;
	}
	public void setColumnRelationId(String columnRelationId) {
		this.columnRelationId = columnRelationId;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFromColumnId() {
		return fromColumnId;
	}
	public void setFromColumnId(String fromColumnId) {
		this.fromColumnId = fromColumnId;
	}
	public String getToColumnId() {
		return toColumnId;
	}
	public void setToColumnId(String toColumnId) {
		this.toColumnId = toColumnId;
	}
	public Integer getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(Integer startPosition) {
		this.startPosition = startPosition;
	}
	public Integer getEndPosition() {
		return endPosition;
	}
	public void setEndPosition(Integer endPosition) {
		this.endPosition = endPosition;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endPosition == null) ? 0 : endPosition.hashCode());
        result = prime * result + ((fromColumnId == null) ? 0 : fromColumnId.hashCode());
        result = prime * result + ((startPosition == null) ? 0 : startPosition.hashCode());
        result = prime * result + ((tableId == null) ? 0 : tableId.hashCode());
        result = prime * result + ((toColumnId == null) ? 0 : toColumnId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ColumnSplit other = (ColumnSplit) obj;
        if (endPosition == null) {
            if (other.endPosition != null)
                return false;
        } else if (!endPosition.equals(other.endPosition))
            return false;
        if (fromColumnId == null) {
            if (other.fromColumnId != null)
                return false;
        } else if (!fromColumnId.equals(other.fromColumnId))
            return false;
        if (startPosition == null) {
            if (other.startPosition != null)
                return false;
        } else if (!startPosition.equals(other.startPosition))
            return false;
        if (tableId == null) {
            if (other.tableId != null)
                return false;
        } else if (!tableId.equals(other.tableId))
            return false;
        if (toColumnId == null) {
            if (other.toColumnId != null)
                return false;
        } else if (!toColumnId.equals(other.toColumnId))
            return false;
        return true;
    }
	
}

package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;
/**
 * 字段运算
 * @author wang
 *
 */

@Entity
@Table(name = "T_XTPZ_COLUMN_OPERATION")
public class ColumnOperation extends StringIDEntity{
	private static final long serialVersionUID = 1L;
	
	public static final String INHERIT = "0"; // 继承
    public static final String SUM     = "1"; // 求和
    public static final String MOST    = "2"; // 最值
	
	
	/*	字段关系ID */
	private String columnRelationId;
	/*	目标表ID */
	private String tableId;
	/*	名称*/
	private String name;
	/*	类别：0-继承 1-求和 2-最值 */
	private String type;
	/*	目标源字段ID */
	private String columnId;
	/*	源表ID */
	private String originTableId;
	/*	源字段ID*/
	private String originColumnId;
	/*	算子：type=1: 0-值累计  1-行统计;type=2: 0-最小  1-最大;*/
	private String operator;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getOriginTableId() {
		return originTableId;
	}
	public void setOriginTableId(String originTableId) {
		this.originTableId = originTableId;
	}
	public String getOriginColumnId() {
		return originColumnId;
	}
	public void setOriginColumnId(String originColumnId) {
		this.originColumnId = originColumnId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((columnId == null) ? 0 : columnId.hashCode());
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((originColumnId == null) ? 0 : originColumnId.hashCode());
        result = prime * result + ((originTableId == null) ? 0 : originTableId.hashCode());
        result = prime * result + ((tableId == null) ? 0 : tableId.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        final ColumnOperation other = (ColumnOperation) obj;
        if (columnId == null) {
            if (other.columnId != null)
                return false;
        } else if (!columnId.equals(other.columnId))
            return false;
        if (operator == null) {
            if (other.operator != null)
                return false;
        } else if (!operator.equals(other.operator))
            return false;
        if (originColumnId == null) {
            if (other.originColumnId != null)
                return false;
        } else if (!originColumnId.equals(other.originColumnId))
            return false;
        if (originTableId == null) {
            if (other.originTableId != null)
                return false;
        } else if (!originTableId.equals(other.originTableId))
            return false;
        if (tableId == null) {
            if (other.tableId != null)
                return false;
        } else if (!tableId.equals(other.tableId))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}

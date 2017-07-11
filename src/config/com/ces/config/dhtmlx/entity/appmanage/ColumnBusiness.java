package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;
/**
 * 字段业务字段
 * @author wang
 *
 */

@Entity
@Table(name = "T_XTPZ_COLUMN_BUSINESS")
public class ColumnBusiness extends StringIDEntity{
	private static final long serialVersionUID = 1L;
	/*	字段关系ID*/
	private String columnRelationId;
	/*	表ID*/
	private String tableId;
	/*	名称*/
	private String name;
	/*件号字段ID	*/
	private String itemColumnId;
	/*	页数字段ID*/
	private String pagesColumnId;
	/*页号字段ID	*/
	private String pagenoColumnId;
	/*方式：0-由页数计算页号 1-由页号计算页数	*/
	private Integer ptype;
	
	
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
	public String getItemColumnId() {
		return itemColumnId;
	}
	public void setItemColumnId(String itemColumnId) {
		this.itemColumnId = itemColumnId;
	}
	public String getPagesColumnId() {
		return pagesColumnId;
	}
	public void setPagesColumnId(String pagesColumnId) {
		this.pagesColumnId = pagesColumnId;
	}
	public String getPagenoColumnId() {
		return pagenoColumnId;
	}
	public void setPagenoColumnId(String pagenoColumnId) {
		this.pagenoColumnId = pagenoColumnId;
	}
	@Column(name = "P_TYPE")
	public Integer getPtype() {
		return ptype;
	}
	public void setPtype(Integer ptype) {
		this.ptype = ptype;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((itemColumnId == null) ? 0 : itemColumnId.hashCode());
        result = prime * result + ((pagenoColumnId == null) ? 0 : pagenoColumnId.hashCode());
        result = prime * result + ((pagesColumnId == null) ? 0 : pagesColumnId.hashCode());
        result = prime * result + ((ptype == null) ? 0 : ptype.hashCode());
        result = prime * result + ((tableId == null) ? 0 : tableId.hashCode());
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
        final ColumnBusiness other = (ColumnBusiness) obj;
        if (itemColumnId == null) {
            if (other.itemColumnId != null)
                return false;
        } else if (!itemColumnId.equals(other.itemColumnId))
            return false;
        if (pagenoColumnId == null) {
            if (other.pagenoColumnId != null)
                return false;
        } else if (!pagenoColumnId.equals(other.pagenoColumnId))
            return false;
        if (pagesColumnId == null) {
            if (other.pagesColumnId != null)
                return false;
        } else if (!pagesColumnId.equals(other.pagesColumnId))
            return false;
        if (ptype == null) {
            if (other.ptype != null)
                return false;
        } else if (!ptype.equals(other.ptype))
            return false;
        if (tableId == null) {
            if (other.tableId != null)
                return false;
        } else if (!tableId.equals(other.tableId))
            return false;
        return true;
    }
	
}

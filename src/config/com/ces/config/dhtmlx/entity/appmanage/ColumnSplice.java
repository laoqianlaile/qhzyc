package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 字段拼接
 * @author wang
 *
 */


@Entity
@Table(name = "T_XTPZ_COLUMN_SPLICE")
public class ColumnSplice extends StringIDEntity{
	private static final long serialVersionUID = 1L;
	
	
	/*	字段关系ID */
	private String columnRelationId;
	/*	表ID */
	private String tableId;
	/*	名称 */
	private String name;
	/*	存储字段 */
	private String storeColumnId;
	/*	段数 */
	private Integer columnNum;
	/*	补零位数 */
	//private Integer fillingNum;
	/*	前缀字符 */
	private String prefix;
	/*	后缀字符 */
	private String suffix;
	/* 补位字符 */
    private String fill;
	
	/*	分隔符1 */
	private String seperator1;  
	/*	 分隔符2*/
	private String seperator2;
	/*	 分隔符3*/
	private String seperator3;
	/*	 分隔符4*/
	private String seperator4;
	
	/*	拼接字段1ID */
	private String column1Id;
	/*	拼接字段2ID */
	private String column2Id;
	/*	拼接字段3ID */
	private String column3Id;
	/*	拼接字段4ID */
	private String column4Id;
    /*  拼接字段5ID */
    private String column5Id;

    /*  字段1补零位数 */
    private Integer fillingNum1;
    /*  字段2补零位数 */
    private Integer fillingNum2;
    /*  字段3补零位数 */
    private Integer fillingNum3;
    /*  字段4补零位数 */
    private Integer fillingNum4;
    /*  字段5补零位数 */
    private Integer fillingNum5;
	
	
	
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
	public String getStoreColumnId() {
		return storeColumnId;
	}
	public void setStoreColumnId(String storeColumnId) {
		this.storeColumnId = storeColumnId;
	}
	public Integer getColumnNum() {
		return columnNum;
	}
	public void setColumnNum(Integer columnNum) {
		this.columnNum = columnNum;
	}

	/*public Integer getFillingNum() {
		return fillingNum;
	}
	public void setFillingNum(Integer fillingNum) {
		this.fillingNum = fillingNum;
	}*/
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getFill() {
        return fill;
    }
    public void setFill(String fill) {
        this.fill = fill;
    }
    public String getSeperator1() {
		return seperator1;
	}
	public void setSeperator1(String seperator1) {
		this.seperator1 = seperator1;
	}
	public String getSeperator2() {
		return seperator2;
	}
	public void setSeperator2(String seperator2) {
		this.seperator2 = seperator2;
	}
	public String getSeperator3() {
		return seperator3;
	}
	public void setSeperator3(String seperator3) {
		this.seperator3 = seperator3;
	}
//	public String getSerialColumnId() {
//		return serialColumnId;
//	}
//	public void setSerialColumnId(String serialColumnId) {
//		this.serialColumnId = serialColumnId;
//	}
	@Column(name = "COLUMN1_ID")
	public String getColumn1Id() {
		return column1Id;
	}
	public void setColumn1Id(String column1Id) {
		this.column1Id = column1Id;
	}
	@Column(name = "COLUMN2_ID")
	public String getColumn2Id() {
		return column2Id;
	}
	public void setColumn2Id(String column2Id) {
		this.column2Id = column2Id;
	}
	@Column(name = "COLUMN3_ID")
	public String getColumn3Id() {
		return column3Id;
	}
	public void setColumn3Id(String column3Id) {
		this.column3Id = column3Id;
	}
	@Column(name = "COLUMN4_ID")
	public String getColumn4Id() {
		return column4Id;
	}
	public void setColumn4Id(String column4Id) {
		this.column4Id = column4Id;
	}
    @Column(name = "COLUMN5_ID")
    public String getColumn5Id() {
        return column5Id;
    }
    public void setColumn5Id(String column5Id) {
        this.column5Id = column5Id;
    }
	public String getSeperator4() {
		return seperator4;
	}
	public void setSeperator4(String seperator4) {
		this.seperator4 = seperator4;
	}

    public Integer getFillingNum1() {
        return fillingNum1;
    }
    public void setFillingNum1(Integer fillingNum1) {
        this.fillingNum1 = fillingNum1;
    }
    public Integer getFillingNum2() {
        return fillingNum2;
    }
    public void setFillingNum2(Integer fillingNum2) {
        this.fillingNum2 = fillingNum2;
    }
    public Integer getFillingNum3() {
        return fillingNum3;
    }
    public void setFillingNum3(Integer fillingNum3) {
        this.fillingNum3 = fillingNum3;
    }
    public Integer getFillingNum4() {
        return fillingNum4;
    }
    public void setFillingNum4(Integer fillingNum4) {
        this.fillingNum4 = fillingNum4;
    }
    public Integer getFillingNum5() {
        return fillingNum5;
    }
    public void setFillingNum5(Integer fillingNum5) {
        this.fillingNum5 = fillingNum5;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((column1Id == null) ? 0 : column1Id.hashCode());
        result = prime * result + ((column2Id == null) ? 0 : column2Id.hashCode());
        result = prime * result + ((column3Id == null) ? 0 : column3Id.hashCode());
        result = prime * result + ((column4Id == null) ? 0 : column4Id.hashCode());
        result = prime * result + ((column5Id == null) ? 0 : column5Id.hashCode());
        result = prime * result + ((columnNum == null) ? 0 : columnNum.hashCode());
        //result = prime * result + ((fillingNum == null) ? 0 : fillingNum.hashCode());
        result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
        result = prime * result + ((seperator1 == null) ? 0 : seperator1.hashCode());
        result = prime * result + ((seperator2 == null) ? 0 : seperator2.hashCode());
        result = prime * result + ((seperator3 == null) ? 0 : seperator3.hashCode());
        result = prime * result + ((seperator4 == null) ? 0 : seperator4.hashCode());
        result = prime * result + ((storeColumnId == null) ? 0 : storeColumnId.hashCode());
        result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
        result = prime * result + ((tableId == null) ? 0 : tableId.hashCode());
        result = prime * result + ((fillingNum1 == null) ? 0 : fillingNum1.hashCode());
        result = prime * result + ((fillingNum2 == null) ? 0 : fillingNum2.hashCode());
        result = prime * result + ((fillingNum3 == null) ? 0 : fillingNum3.hashCode());
        result = prime * result + ((fillingNum4 == null) ? 0 : fillingNum4.hashCode());
        result = prime * result + ((fillingNum5 == null) ? 0 : fillingNum5.hashCode());
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
        final ColumnSplice other = (ColumnSplice) obj;
        if (column1Id == null) {
            if (other.column1Id != null)
                return false;
        } else if (!column1Id.equals(other.column1Id))
            return false;
        if (column2Id == null) {
            if (other.column2Id != null)
                return false;
        } else if (!column2Id.equals(other.column2Id))
            return false;
        if (column3Id == null) {
            if (other.column3Id != null)
                return false;
        } else if (!column3Id.equals(other.column3Id))
            return false;
        if (column4Id == null) {
            if (other.column4Id != null)
                return false;
        } else if (!column4Id.equals(other.column4Id))
            return false;
        if (column5Id == null) {
            if (other.column5Id != null)
                return false;
        } else if (!column5Id.equals(other.column5Id))
            return false;
        if (columnNum == null) {
            if (other.columnNum != null)
                return false;
        } else if (!columnNum.equals(other.columnNum))
            return false;
        /*if (fillingNum == null) {
            if (other.fillingNum != null)
                return false;
        } else if (!fillingNum.equals(other.fillingNum))
            return false;*/
        if (prefix == null) {
            if (other.prefix != null)
                return false;
        } else if (!prefix.equals(other.prefix))
            return false;
        if (seperator1 == null) {
            if (other.seperator1 != null)
                return false;
        } else if (!seperator1.equals(other.seperator1))
            return false;
        if (seperator2 == null) {
            if (other.seperator2 != null)
                return false;
        } else if (!seperator2.equals(other.seperator2))
            return false;
        if (seperator3 == null) {
            if (other.seperator3 != null)
                return false;
        } else if (!seperator3.equals(other.seperator3))
            return false;
        if (seperator4 == null) {
            if (other.seperator4 != null)
                return false;
        } else if (!seperator4.equals(other.seperator4))
            return false;
        if (storeColumnId == null) {
            if (other.storeColumnId != null)
                return false;
        } else if (!storeColumnId.equals(other.storeColumnId))
            return false;
        if (suffix == null) {
            if (other.suffix != null)
                return false;
        } else if (!suffix.equals(other.suffix))
            return false;
        if (tableId == null) {
            if (other.tableId != null)
                return false;
        } else if (!tableId.equals(other.tableId))
            return false;
        if (fillingNum1 == null) {
            if (other.fillingNum1 != null)
                return false;
        } else if (!fillingNum1.equals(other.fillingNum1))
            return false;
        if (fillingNum2 == null) {
            if (other.fillingNum2 != null)
                return false;
        } else if (!fillingNum2.equals(other.fillingNum2))
            return false;
        if (fillingNum3 == null) {
            if (other.fillingNum3 != null)
                return false;
        } else if (!fillingNum3.equals(other.fillingNum3))
            return false;
        if (fillingNum4 == null) {
            if (other.fillingNum4 != null)
                return false;
        } else if (!fillingNum4.equals(other.fillingNum4))
            return false;
        if (fillingNum5 == null) {
            if (other.fillingNum5 != null)
                return false;
        } else if (!fillingNum5.equals(other.fillingNum5))
            return false;
        return true;
    }
	
}

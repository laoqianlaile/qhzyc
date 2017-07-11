package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 逻辑表定义表
 * 
 * @author qiujinwei
 */
@Entity
@Table(name = "T_XTPZ_LOGIC_TABLE_DEFINE")
public class LogicTableDefine extends StringIDEntity {

	private static final long serialVersionUID = 1L;

	/** 中文显示名称 */
	private String showName;

	/** 逻辑表代码（唯一性） */
	private String code;

	/** 显示顺序 */
	private Integer showOrder;

	/** 备注 */
	private String remark;

	/** 状态:'0'未应用，'1'已应用 */
	private String status;
	
	/** 表分类树ID */
	private String tableTreeId;

	/** 逻辑表分类 */
	private String classification;

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getTableTreeId() {
		return tableTreeId;
	}

	public void setTableTreeId(String tableTreeId) {
		this.tableTreeId = tableTreeId;
	}

	@Transient
	public String getSelected() {
		return "0";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		LogicTableDefine other = (LogicTableDefine) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}

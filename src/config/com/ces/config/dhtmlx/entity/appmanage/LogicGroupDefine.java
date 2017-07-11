package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 逻辑表组定义表
 * 
 * @author qiujinwei
 */
@Entity
@Table(name = "T_XTPZ_LOGIC_GROUP_DEFINE")
public class LogicGroupDefine extends StringIDEntity {

	private static final long serialVersionUID = 1L;

	/** 逻辑表组名称 */
	private String groupName;

	/** 逻辑表组CODE */
	private String code;

	/** 显示顺序 */
	private Integer showOrder;

	/** 备注 */
	private String remark;

	/** 状态:'0'未应用，'1'已应用 */
	private String status;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		LogicGroupDefine other = (LogicGroupDefine) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}

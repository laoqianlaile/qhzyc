package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 物理表组与物理表关系表
 * 
 * @author qiujinwei
 * @date 2014-11-20
 */
@Entity
@Table(name = "T_XTPZ_PHYSICAL_GROUP_RELATION")
public class PhysicalGroupRelation extends StringIDEntity {
	
	private static final long serialVersionUID = 1L;
	
	/** 物理组ID * */
	private String groupId;
	
	/** 物理表ID * */
	private String tableId;
	
	/** 显示顺序 * */
	private Integer showOrder;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

}

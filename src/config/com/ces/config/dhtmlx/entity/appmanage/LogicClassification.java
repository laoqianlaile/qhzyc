package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 逻辑表分类表
 * 
 * @author qiujinwei
 */
@Entity
@Table(name = "T_XTPZ_LOGIC_CLASSIFICATION")
public class LogicClassification extends StringIDEntity {

	private static final long serialVersionUID = 1L;

	/** 名称 */
	private String name;

	/** 代码 */
	private String code;

	/** 显示顺序 */
	private String showOrder;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(String showOrder) {
		this.showOrder = showOrder;
	}

}

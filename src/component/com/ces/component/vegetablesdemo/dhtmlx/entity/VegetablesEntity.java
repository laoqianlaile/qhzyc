package com.ces.component.vegetablesdemo.dhtmlx.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name="T_VEGETABLES")
public class VegetablesEntity extends StringIDEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String moduleId;
	private String vegeName;
	private String vegeType;
	private Double vegePrice;
	private int showOrder;
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getVegeName() {
		return vegeName;
	}
	public void setVegeName(String vegeName) {
		this.vegeName = vegeName;
	}
	public String getVegeType() {
		return vegeType;
	}
	public void setVegeType(String vegeType) {
		this.vegeType = vegeType;
	}
	public Double getVegePrice() {
		return vegePrice;
	}
	public void setVegePrice(Double vegePrice) {
		this.vegePrice = vegePrice;
	}
	public int getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	
	
	

	
}

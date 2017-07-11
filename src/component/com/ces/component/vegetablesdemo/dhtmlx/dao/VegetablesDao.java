package com.ces.component.vegetablesdemo.dhtmlx.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.vegetablesdemo.dhtmlx.entity.VegetablesEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface VegetablesDao extends StringIDDao<VegetablesEntity>{

	/**
	 * 根据蔬菜的名称获得蔬菜的信息
	 * @param name
	 * @return
	 */
	public VegetablesEntity getByVegeName(String name);
	
	public VegetablesEntity getByModuleId(String moduleId);
	
	public List<VegetablesEntity> getByVegeType(String type);
	
	public List<VegetablesEntity> getByShowOrderBetweenAndModuleId(Integer start, Integer end, String moduleId);
	
	 @Query("select max(showOrder) from VegetablesEntity where moduleId=?")
	public Integer getMaxShowOrder(String moduleId);
}

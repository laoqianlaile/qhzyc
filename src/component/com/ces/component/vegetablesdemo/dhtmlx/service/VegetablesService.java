package com.ces.component.vegetablesdemo.dhtmlx.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.component.pagedemo.dhtmlx.dao.PageDemoDao;
import com.ces.component.vegetablesdemo.dhtmlx.dao.VegetablesDao;
import com.ces.component.vegetablesdemo.dhtmlx.entity.VegetablesEntity;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
@Component("vegetablesService")
public class VegetablesService extends ConfigDefineDaoService<VegetablesEntity, VegetablesDao> {
	@Autowired
    @Qualifier("vegetablesDao")
    @Override
	 protected void setDaoUnBinding(VegetablesDao dao) {
	        super.setDaoUnBinding(dao);
	    }
	public VegetablesEntity getByVegeName(String name){
		return getDao().getByVegeName(name);
	}
	
	public List<VegetablesEntity> getByShowOrderBetweenAndModuleId(Integer start, Integer end, String moduleId){
		return getDao().getByShowOrderBetweenAndModuleId(start, end, moduleId);
	}
	public Integer getMaxShowOrder(String moduleId){
		 Integer maxShowOrder = getDao().getMaxShowOrder(moduleId);
		 if(maxShowOrder==null){
			 maxShowOrder=new Integer(0);
		 }
		 return maxShowOrder;
	}

}

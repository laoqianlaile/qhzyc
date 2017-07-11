package com.ces.component.cyqyda.service;

import org.springframework.stereotype.Component;

import com.ces.component.cyqyda.dao.CyqydaDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class CyqydaService extends TraceShowModuleDefineDaoService<StringIDEntity, CyqydaDao> {
	public String getIdByQybm(){
    	String qybm = SerialNumberUtil.getInstance().getCompanyCode();
    	return getDao().getIdByQybm(qybm);
    }
    
}

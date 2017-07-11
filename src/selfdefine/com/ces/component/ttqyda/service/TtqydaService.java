package com.ces.component.ttqyda.service;

import org.springframework.stereotype.Component;

import com.ces.component.qyda.dao.QydaDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.ttqyda.dao.TtqydaDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TtqydaService extends TraceShowModuleDefineDaoService<StringIDEntity, TtqydaDao> {
	 public String getIdByQybm(){
	    	String qybm = SerialNumberUtil.getInstance().getCompanyCode();
	    	return getDao().getIdByQybm(qybm);
	    }
    
}

package com.ces.component.tzqyda.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.tzqyda.dao.TzqydaDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TzqydaService extends TraceShowModuleDefineDaoService<StringIDEntity, TzqydaDao> {

	public String getIdByQybm() {
		String qybm = SerialNumberUtil.getInstance().getCompanyCode();
		return getDao().getIdByQybm(qybm);
	}

	public Object getQyda(String prefix) {
		String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
    	String tzcmc = CompanyInfoUtil.getInstance().getCompanyName(prefix,tzcbm);
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("tzcbm",tzcbm);
    	map.put("tzcmc",tzcmc);
    	return map;
	}
}

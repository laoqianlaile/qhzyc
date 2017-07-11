package com.ces.component.rpspxx.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class RpspxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
	
    public String getSpmc(String spbm){
    	String sql = "select T.SPMC from t_Common_Rpspxx T where T.SPBM = '"+spbm+"'";
    	Map<String,Object> map = (Map<String,Object>)DatabaseHandlerDao.getInstance().queryForMap(sql);
    	return (String) map.get("SPMC");
    }
}
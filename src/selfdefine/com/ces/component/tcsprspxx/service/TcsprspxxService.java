package com.ces.component.tcsprspxx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TcsprspxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	public Map<String,Object> getJcspxx(){
    	String sql = "SELECT T.SPBM,T.SPMC,T.PFSMC,T.ZL,T.DJ,T.JE,T.ZSPZH,T.CDMC,T.GHSCMC FROM T_PR_RPJCXX T WHERE T.ZT = '1' ORDER BY T.SPBM,T.JCRQ";
		List<Map<String,Object>> li = new ArrayList<Map<String,Object>>();
		li = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		if(li.size()<1){
			return null;
		}
		Map map = new HashMap();
		map.put("data", li);
		return map;
    }
}
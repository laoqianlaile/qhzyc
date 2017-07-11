package com.ces.component.tcsprjcy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TcsprjcyService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Map<String,Object> getJcyxx(){
    	String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
    	String sql = "SELECT T.GZRYBH,T.XM,T.GW FROM T_PR_GZRYDA T where T.TZCBM = '"+tzcbm+"' ORDER BY T.GZRYBH DESC";
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
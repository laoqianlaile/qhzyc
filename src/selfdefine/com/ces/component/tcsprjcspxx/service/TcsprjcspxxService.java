package com.ces.component.tcsprjcspxx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ces.coral.lang.StringUtil;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TcsprjcspxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	 public Map<String,Object> getJcspxx(String zspzh){
	    	String sql = "SELECT T.SPBM,T.SPMC FROM T_PR_RPJCXX T WHERE 1=1";
	    	if(StringUtil.isNotBlank(zspzh)){
	    		sql+=" AND T.ZSPZH = '"+zspzh+"'";
	    	}
	    	sql += " AND T.PFSCBM = '" + SerialNumberUtil.getInstance().getCompanyCode() + "' AND T.ZT=1 ORDER BY T.SPBM DESC";
	    	
			List<Map<String,Object>> li = new ArrayList<Map<String,Object>>();
			li = DatabaseHandlerDao.getInstance().queryForMaps(sql);
			if(li.size()<1){
				return null;
			}
			Map map = new HashMap();
			map.put("data", li);
			return map;
	    }
	 public String getJhpch(String zspzh){
		 String sql = "SELECT T.JHPCH FROM T_PR_RPJCXX T WHERE T.ZSPZH='"+zspzh+"'";
		 List li = new ArrayList();
		 li = DatabaseHandlerDao.getInstance().queryForList(sql);
		 if(li.size()>0)
			 return (String) li.get(0);
		 else
			 return null;
	 }
}
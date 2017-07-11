package com.ces.component.tzrpjyjyxz.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.tzrpjyjyxz.dao.TzrpjyjyxzDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TzrpjyjyxzService extends TraceShowModuleDefineDaoService<StringIDEntity, TzrpjyjyxzDao> {

	public Object getJyzhGridByHzbm(String hzbm){
		String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select T.SZCDJYZH,T.HZBM,T.HZMC,T.CJRQ,T.ID from T_TZ_SZJYXX T where T.TZCBM LIKE '%" + tzcbm 
				+ "%' and T.HZBM LIKE '%"+hzbm+"%' and T.JYZT = '1' order by T.SZCDJYZH desc";
		List<Map<String,Object>> map = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return map;
	}
	
	public Object getSzjyxx(String szcdjyzh){
		return getDao().getSzjyxx(szcdjyzh);
	}
	
	public void setJyzt(String szcdjyzh){
		String sql = "update T_TZ_SZJYXX T set T.JYZT = '2' where T.SZCDJYZH = '" + szcdjyzh + "'";
		DatabaseHandlerDao.getInstance().executeSql(sql);
	}
}

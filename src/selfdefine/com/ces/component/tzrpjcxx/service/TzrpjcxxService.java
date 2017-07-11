package com.ces.component.tzrpjcxx.service;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.tzrpjcxx.dao.TzrpjcxxDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TzrpjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TzrpjcxxDao> {

	@Override
	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
		StringBuilder condition=new StringBuilder();
		condition.append(AppDefineUtil.RELATION_AND);
		condition.append("TZCBM=");
		condition.append(SerialNumberUtil.getInstance().getCompanyCode());
		//condition += " and JYZT=1";
        return condition.toString();//过滤屠宰场编码和检疫状态
    }
	
	public Object getJyzhGridByHzbm(String hzbm) {
		String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select T.SZCDJYZH,T.HZBM,T.HZMC,T.CJRQ,T.ID from T_TZ_SZJYXX T where T.TZCBM LIKE '%" + tzcbm 
				+ "%' and T.HZBM LIKE '%"+hzbm+"%'";
		List<Map<String,Object>> map = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return map;
    }

	public Object getJyzhGrid(){
		String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select T.SZCDJYZH,T.HZBM,T.HZMC,T.CJRQ,T.ID from T_TZ_SZJYXX T where T.TZCBM LIKE '%" + tzcbm
				+ "%' ";
		List<Map<String,Object>> map = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return map;
	}
	
	public Object getSzjyxx(String szcdjyzh) {
    	return getDao().getSzjyxx(szcdjyzh);
    }

}

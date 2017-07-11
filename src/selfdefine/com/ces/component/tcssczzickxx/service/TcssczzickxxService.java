package com.ces.component.tcssczzickxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TcssczzickxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	public Object getIckxxGridData(){
		String sql = "select t.ICKKH,t.XM from T_ZZ_ICKXXGL t where t.QYBM = ? and t.IS_DELETE <>1" ;
		List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{getJdbh()});
		return getGridTypeData(list);
	}

	public String getJdbh(){
		return SerialNumberUtil.getInstance().getCompanyCode();
	}
	public Object getGridTypeData(Object o ){
		Map<String,Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data" , o);
		return dataMap;
	}
	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code=getJdbh();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode= AppDefineUtil.RELATION_AND+" is_delete <> '1' "+AppDefineUtil.RELATION_AND+" QYBM = '"+code+"' ";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
	                                     String componentVersionId, String moduleId, String menuId,
	                                     Map<String, Object> paramMap) {
		// 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
		return defaultCode();
	}
}
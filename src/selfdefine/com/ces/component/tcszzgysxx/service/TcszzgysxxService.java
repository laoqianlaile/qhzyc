package com.ces.component.tcszzgysxx.service;

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
public class TcszzgysxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code= SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode= AppDefineUtil.RELATION_AND+" QYBM = '"+code+"' ";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
	                                     String componentVersionId, String moduleId, String menuId,
	                                     Map<String, Object> paramMap) {
		// 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
		return defaultCode();
	}

	public Object searchGysxxData(){
		String sql = "select t.gysbh, t.gysmc, c.sjmc gyslb  " +
				" from t_zz_gysxx t, t_common_sjlx_code c " +
				" where c.sjbm = t.gyslb " +
				"   and c.lxbm = 'GYSLB' " + defaultCode();
		return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
	}

	/**
	 * 获取供应商下拉列表的值
	 * @return
	 */
	public Object searchGysxxDataGrid(String gysbh){
		String sql = "select * from t_zz_gysxx t where t.gysbh="+gysbh;
		return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
	}
	public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
		Map<String,Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data",list);
		return  dataMap;
	}
}
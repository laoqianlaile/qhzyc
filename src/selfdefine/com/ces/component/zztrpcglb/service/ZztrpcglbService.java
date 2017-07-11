package com.ces.component.zztrpcglb.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class ZztrpcglbService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {


	@Transactional
	public Map<String, String> save(String entityJson) {
		String tableName = "T_ZZ_TRPCGGL"  ;
		JsonNode entityNode = JsonUtil.json2node(entityJson);
		Map<String, String> dataMap = node2map(entityNode);
		String id = dataMap.get("ID");
		if(id == "" || "".equals(id)){//是新增操作
			String rklsh = SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZRKBH", false);
			dataMap.put("RKLSH",rklsh);
//			dataMap.put("TRPBH",rklsh);
		}
		dataMap.put("KCSL",dataMap.get("RKSL"));
		id = saveOne(tableName, dataMap);
		dataMap.put(AppDefineUtil.C_ID, id);
		return dataMap;
	}
	public Map<String,Object> getTrpCgxx(String id){
		String sql = "select * from t_zz_trpcggl where id ='"+id+"'";
		return DatabaseHandlerDao.getInstance().queryForMap(sql);
	}

	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code= SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode= AppDefineUtil.RELATION_AND+" QYBM = '"+code+"' and is_delete = '0'";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
	                                     String componentVersionId, String moduleId, String menuId,
	                                     Map<String, Object> paramMap) {
		// 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
		return defaultCode();
	}

	public Object searchBzggData(String trpbh){
		String sql="select * from t_zz_trpjbxx where trpbh = '"+trpbh+"' and qybm ='"+SerialNumberUtil.getInstance().getCompanyCode()+"' and is_delete <> '1'";
		return DatabaseHandlerDao.getInstance().queryForMap(sql);
	}
}
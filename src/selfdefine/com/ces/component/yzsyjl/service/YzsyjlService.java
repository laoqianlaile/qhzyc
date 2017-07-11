package com.ces.component.yzsyjl.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class YzsyjlService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@Override
	public Map<String, String> save(String tableId, String entityJson,
			Map<String, Object> paramMap) {
		String tableName = "T_YZ_SYXX";
		entityJson=entityJson.replace("A_", "");
    	JsonNode entityNode = JsonUtil.json2node(entityJson);
    	Map<String, String> dataMap = node2map(entityNode);
    	dataMap.remove("B_SYZS");
    	dataMap.remove("B_JLRQ");
    	dataMap.remove("B_PZTYM");
    	String dataId=dataMap.get("ID");
    	if(dataId!=null && !"".equals(dataId)){
    		dataId=dataId.split("_")[0];
    		dataMap.put("ID", dataId);
    	}
    	String id = save(tableName, dataMap, paramMap);
    	dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
	}

	@Override
	public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
		 // 1. 获取所有关联表的要删除的IDS
        String tableName = "T_YZ_SYXX";
        String[] idDatas=ids.split(",");
        StringBuffer newIds=new StringBuffer("");
        for (int i = 0; i < idDatas.length; i++) {
        	if(i >0 && i<=idDatas.length-1){
				newIds.append(",");
			}
			String string = idDatas[i];
			newIds.append("'"+string.split("_")[0]+"'");
			
		}
        String filter    = "DELETE FROM "+tableName + " WHERE ID IN (" + newIds.toString() + ")";
        DatabaseHandlerDao.getInstance().executeSql(filter);
	}
	
	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode=AppDefineUtil.RELATION_AND+" A_QYBM = '"+code+"' ";
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
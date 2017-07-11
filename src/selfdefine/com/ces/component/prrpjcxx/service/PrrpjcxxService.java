package com.ces.component.prrpjcxx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class PrrpjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
//        return AppDefineUtil.RELATION_AND+"PFSCBM="+SerialNumberUtil.getInstance().getCompanyCode()+" AND ZT='1'";
        return AppDefineUtil.RELATION_AND+"PFSCBM="+SerialNumberUtil.getInstance().getCompanyCode();
    }
	
	/**
	 * 获取供应商信息
	 * @return
	 */
	public Map<String,Object> getGysData(String id){
		String sql = "select * from T_CS_RPJCXX where ID='"+id+"'";
		Map<String,Object> map = (Map<String,Object>)DatabaseHandlerDao.getInstance().queryForMap(sql);		
		return map;
	}
	
	public Map<String,Object> getRpspxx(){
		String sql = "SELECT T.SPBM,T.SPMC,T.SPDM,T.SPBM2 FROM T_COMMON_RPSPXX T ";
		List<Map<String,Object>> li = new ArrayList<Map<String,Object>>();
		li = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		if(li.size()<1){
			return null;
		}
		Map map = new HashMap();
		map.put("data", li);
		return map;
	}
	
	public Map<String,Object> getTzjyxx(String tztmh){
		String sql = "select T.MZBM,T.MZMC, T.ZSM,T.SPMC,T.SPBM,T.ZL,T.DJ,T.JE,P.CDMC,P.CDBM,T.TZCMC,T.JCPCH from T_TZ_JYXX T,T_TZ_SZJCXX P where T.SZCDJYZH = P.SZCDJYZH and T.JYTMH = '"+tztmh+"' and T.IS_IN=0";
		Map<String,Object> map = (Map<String,Object>)DatabaseHandlerDao.getInstance().queryForMap(sql);
		return map;
	}
	
	public Map<String,Object> getPrjyxx(String prtmh){
		String sql = "select T.LSSMC,T.LSSBM ,T.ZSM,T.JHPCH,T.SPMC,T.SPBM,T.ZL,T.DJ,T.JE,P.CDMC,P.CDBM,T.PFSCMC from T_PR_JYXX T,T_PR_RPJCXX P where T.ZSPZH = P.ZSPZH and T.JYTMH = '"+prtmh+"' and T.IS_IN=0";
		Map<String,Object> map = (Map<String,Object>)DatabaseHandlerDao.getInstance().queryForMap(sql);
		return map;
	}
	@Override
	@Transactional
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
    	String tableName = getTableName(tableId);
    	JsonNode entityNode = JsonUtil.json2node(entityJson);
    	Map<String, String> dataMap = node2map(entityNode);
    	String id = save(tableName, dataMap, paramMap);
    	dataMap.put(AppDefineUtil.C_ID, id);
    	/*****同步追溯信息*****/
    	TCsptZsxxEntity entity = new TCsptZsxxEntity();
    	entity.setJypzh(dataMap.get("ZSPZH"));
    	entity.setJhpch(dataMap.get("JHPCH"));
    	entity.setQybm(dataMap.get("PFSCBM"));
    	entity.setQymc(dataMap.get("PFSCMC"));
    	entity.setJyzbm(dataMap.get("PFSBM"));
    	entity.setJyzmc(dataMap.get("PFSMC"));
    	entity.setXtlx("5");
    	entity.setRefId(dataMap.get("ID"));
    	TraceChainUtil.getInstance().syncZsxx(entity);
    	/********结束*******/
    	
    	/******** 同步上家交易信息 ********/
	    String barCode = ServletActionContext.getRequest().getParameter("barCode");
	    if (barCode != null && !"".equals(barCode)) {
		    String prefix = barCode.substring(0,2);
		    if (prefix.equalsIgnoreCase("TZ")) {
				String sql = "update T_TZ_JYXX t set t.is_in = '1' where t.JYTMH = '" + barCode + "'";
			    DatabaseHandlerDao.getInstance().executeSql(sql);
		    } else if (prefix.equalsIgnoreCase("PR")) {
			    String sql = "update T_PR_JYXX t set t.is_in = '1' where t.JYTMH = '" + barCode + "'";
			    DatabaseHandlerDao.getInstance().executeSql(sql);
		    }
	    }
	    /******** 同步上家交易信息 ********/
        return dataMap;
    }
}
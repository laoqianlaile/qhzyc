package com.ces.component.tzszjcxx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class TzszjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
			
	@Override
	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
		String condition="";
		condition += AppDefineUtil.RELATION_AND;
		condition += "TZCBM=";
		condition += SerialNumberUtil.getInstance().getCompanyCode();
		//condition += " and JYZT=1";
        return condition;//过滤屠宰场编码和检疫状态
    }
	
	public Map<String,Object> getJcpcxx(String barCode){//barCode为出栏批次号，与生猪产地检疫证号一一对应
		String qybm = barCode.substring(2, 11);
		String condition = "P.QYBM = '" +qybm+ "' and T.CCTMH = '" +barCode+ "' and T.IS_IN=0 ";
		String sql = "select P.YZCMC,P.LSXZQ as CDMC,P.LSXZQHDM as CDBM,T.SZCDJYZH,T.SL,T.YSCPH,T.PCH,T.ZSM,T.ZZL from T_YZ_CLXX T,T_YZ_CDDA P where " + condition ;
		Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
		return map;
	}
	
///获取页面上都能动物产地检疫证号和数据库的检疫证号进行比较
	public Object getszjcxx(String szcdjyzh){
		String sql="select t.szcdjyzh  from t_tz_SZJCXX t where t.szcdjyzh = '" + szcdjyzh + "' and is_delete <> '1'";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return list;
		
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
    	entity.setJypzh(dataMap.get("JYPZH"));
    	entity.setJhpch(dataMap.get("JCPCH"));
    	entity.setQybm(dataMap.get("TZCBM"));
    	entity.setQymc(dataMap.get("TZCMC"));
    	entity.setJyzbm(dataMap.get("HZBM"));
    	entity.setJyzmc(dataMap.get("HZMC"));
    	entity.setXtlx("4");
    	entity.setRefId(dataMap.get("ID"));
    	TraceChainUtil.getInstance().syncZsxx(entity);
    	/********结束*******/
    	
    	/******** 同步养殖出栏信息 start********/
	    String barCode = ServletActionContext.getRequest().getParameter("barCode");
	    if (barCode != null && !"".equals(barCode)) {
		    String prefix = barCode.substring(0,2);
		    if (prefix.equalsIgnoreCase("YZ")) {
				String sql = "update T_YZ_CLXX T set T.IS_IN = '1' where t.CCTMH = '"+barCode+"'";
			    DatabaseHandlerDao.getInstance().executeSql(sql);
		    }
	    }
	    /******** 同步养殖出栏信息 end********/
	    
        return dataMap;
    }
}
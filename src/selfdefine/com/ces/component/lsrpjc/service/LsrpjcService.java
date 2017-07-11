package com.ces.component.lsrpjc.service;

import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.lsjcxxxz.dao.LsjcxxxzDao;
import com.ces.component.lsrpjc.dao.LsrpjcDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class LsrpjcService extends TraceShowModuleDefineDaoService<StringIDEntity, LsrpjcDao> {

	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode=" AND LSSCBM = '"+code+"' ";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		  // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
	}
	
	//根据条码获取屠宰交易信息
	public Map<String,Object> getTzjyxx(String tztmh){
		String sql = "select  T.MZBM,T.MZMC,T.ZSM,T.SPMC,T.SPBM,T.ZL,T.DJ,T.JE,T.TZCMC,T.JCPCH from T_TZ_JYXX T where T.JYTMH = '"+tztmh+"' ";//AND T.IS_IN = 0
		Map<String,Object> map = (Map<String,Object>)DatabaseHandlerDao.getInstance().queryForMap(sql);
		return map;
	}
	
	//根据条码获取批肉交易信息
	public Map<String,Object> getPrjyxx(String prtmh){
		String sql = "select T.LSSMC,T.LSSBM,T.ZSM,T.SPMC,T.SPBM,T.ZL,T.DJ,T.JE,T.PFSCMC,T.JHPCH from T_PR_JYXX T where T.JYTMH = '"+prtmh+"' AND T.IS_IN = 0";
		Map<String,Object> map = (Map<String,Object>)DatabaseHandlerDao.getInstance().queryForMap(sql);
		return map;
	}
	
	public List  getSpmc(){
    	List list =  getDao().getAllRpmc();
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
    	entity.setJypzh(dataMap.get("ZSPZH"));
    	entity.setJhpch(dataMap.get("JHPCH"));
    	entity.setQybm(dataMap.get("LSSCBM"));
    	entity.setQymc(dataMap.get("LSSCMC"));
    	entity.setJyzbm(dataMap.get("LSSBM"));
    	entity.setJyzmc(dataMap.get("LSSMC"));
    	entity.setXtlx("8");
    	entity.setRefId(dataMap.get("ID"));
    	TraceChainUtil.getInstance().syncZsxx(entity);
    	/********结束*******/
    	/******** 同步上家交易信息 ********/
        String barCode = ServletActionContext.getRequest().getParameter("barCode");
        if (barCode != null && !"".equals(barCode)) {
            String prefix = barCode.substring(0,2);
            if (prefix.equalsIgnoreCase("PC")) {//原批发市场交易数据更改为已用状态
                String sql = "update t_pc_jyxx t set t.is_in = '1' where t.JYTMH = '" + barCode + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            } else if (prefix.equalsIgnoreCase("ZZ")) {//原种植场出场数据更给为已用状态
                String sql = "update t_zz_ccxx t set t.is_in = '1' where upper(t.cctmh) = '" + barCode + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }else if (prefix.equalsIgnoreCase("YZ")) {//原养殖场出场数据更给为已用状态
                String sql = "update t_yz_clxx t set t.is_in = '1' where upper(t.cctmh) = '" + barCode + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }else if (prefix.equalsIgnoreCase("TZ")) {//原屠宰场交易数据更给为已用状态
                String sql = "update t_tz_jyxx t set t.is_in = '1' where t.zsm = '" + dataMap.get("ZSPZH")+ "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }else if (prefix.equalsIgnoreCase("PR")) {//原肉品批发市场交易数据更给为已用状态
                String sql = "update t_pr_jyxx t set t.is_in = '1' where t.jytmh = '" + barCode + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }
        }
        /******** 同步上家交易信息 ********/
        return dataMap;
    }
    
}

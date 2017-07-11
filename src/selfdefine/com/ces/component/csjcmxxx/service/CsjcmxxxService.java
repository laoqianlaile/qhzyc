package com.ces.component.csjcmxxx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class CsjcmxxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@Transactional
    @Override
    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
    	JsonNode entity = JsonUtil.json2node(entityJson);
    	Map<String, String> dataMap = node2map(entity);//主表信息
    	MessageModel message =  (MessageModel) super.saveAll(tableId, entityJson, dTableId, dEntitiesJson, paramMap);
    	Map<String,Object> map = (Map) message.getData();
    	List<Map<String,String>> detail = (List<Map<String, String>>) map.get("detail");//从表信息
		String csmc = CompanyInfoUtil.getInstance().getCompanyName("CS",dataMap.get("CSBM"));
    	for(Map<String,String> m :detail){
    		/*****同步追溯信息*****/
        	TCsptZsxxEntity zsEntity = new TCsptZsxxEntity();
        	zsEntity.setJhpch(m.get("JHPCH"));
        	zsEntity.setJypzh(m.get("LSPZH"));
        	zsEntity.setQymc(csmc);
        	zsEntity.setQybm(dataMap.get("CSBM"));
        	zsEntity.setJyzbm(dataMap.get("GYSBM"));
        	zsEntity.setJyzmc(dataMap.get("GYSMC"));
        	zsEntity.setXtlx("7");
        	zsEntity.setRefId(m.get("ID"));
        	TraceChainUtil.getInstance().syncZsxx(zsEntity);
        	/********结束*******/
    	}
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

    	return message;
    }
	
	public Map<String,Object> getZzccxx(String zztmh){
//    	System.out.println("字符串截取结果为：=" +newPch);
//    	System.out.println("字符串替换结果为：=" +ccpch.replace("PC", ""));
		String qybm = zztmh.substring(2,11);
		String sql= " select T.A_CDZMH as CDZMH,T.B_SPMC as SPMC,T.B_SPBM as SPBM ,T.A_PCH as CCPCH,T.A_ZL as ZL,P.CDMC as CDMC,T.A_ZSM as ZSM from V_ZZ_CCXX_ZZ_ZPXX T,T_ZZ_CDDA P where T.A_CCTMH='"
				+zztmh+"' and t.a_is_in = '0' and P.QYBM = '"+qybm+"'";
    	Map<String,Object> map=DatabaseHandlerDao.getInstance().queryForMap(sql);
    	return map;
    }
    
    public Map<String,Object> getPcjyxx(String pctmh){
    	String qybm = pctmh.substring(2,11);//企业编码
     	String lhxxSql = "select P.LSSMC,P.LSSBM,T.CDZMH,T.CDMC from T_PC_JCLHXX T,T_PC_JYXX P where P.JYTMH = '"
						 +pctmh+"' and P.JCLHBH = T.JCLHBH and P.IS_IN='0' ";//理货信息查询
    	String jymxxxSql = "select T.SPMC,T.SPBM,T.ZL,T.DJ,T.JE,T.JHPCH,T.ZSM from T_PC_JYMXXX T left join T_PC_JYXX P on T.T_PC_JYXX_ID = P.ID where P.is_in = '0' and P.JYTMH = '"+pctmh+"'";
    	Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(lhxxSql);
    	List<Map<String,Object>> listMap = DatabaseHandlerDao.getInstance().queryForMaps(jymxxxSql);
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("lhxx", map);
    	resultMap.put("jymxxx", listMap);
    	return resultMap;
    }
}
package com.ces.component.zzcczp.service;

import java.util.Map;

import com.ces.component.trace.utils.CompanyInfoUtil;
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
public class ZzcczpService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@Override
	@Transactional
	public Map<String, String> save(String tableId, String entityJson,
			Map<String, Object> paramMap) {
		String tableName = "T_ZZ_CCXX";
		String copyJson=entityJson;
		entityJson=entityJson.replace("A_", "");
    	JsonNode entityNode = JsonUtil.json2node(entityJson);
    	Map<String, String> dataMap = node2map(entityNode);
    	dataMap.remove("B_SYCZ");
    	dataMap.remove("B_SYCT");
    	dataMap.remove("B_ZPRQ");
    	dataMap.remove("B_SPMC");
    	dataMap.remove("B_SYCTBH");
    	String dataId=dataMap.get("ID");
    	//判读是为修改操作
    	if(dataId!=null && !"".equals(dataId)){//是修改操作，把ID提取出来
    		dataId=dataId.split("_")[0];
    		dataMap.put("ID", dataId);
    	}
    	String id = save(tableName, dataMap, paramMap);
    	//重新构造Map数据
    	entityNode=JsonUtil.json2node(copyJson);
    	Map<String, String> dataM=node2map(entityNode);
    	//dataM.put("",);
    	/*****同步追溯信息*****/
    	TCsptZsxxEntity entity = new TCsptZsxxEntity();
    	entity.setZsm(dataMap.get("ZSM"));
    	entity.setJhpch(dataMap.get("PCH"));
    	entity.setQybm(dataMap.get("QYBM"));
    	entity.setQymc(CompanyInfoUtil.getInstance().getCompanyName("ZZ", dataMap.get("QYBM")));
    	entity.setXtlx("1");
    	entity.setRefId(dataMap.get("ID"));
    	entity.setZZYZPCH(dataMap.get("ZZPCH"));
    	TraceChainUtil.getInstance().syncZsxx(entity);
    	/********结束*******/
    	String zzpch=dataMap.get("ZZPCH");
    	String zzpchId=getZzpch_id(zzpch);
    	dataM.put(AppDefineUtil.C_ID, id+"_"+zzpchId);
        return dataM;
	}

	public String getZzpch_id(String zzpch){
		String sql ="select id from t_zz_zpxx where zzpch='"+zzpch+"' and QYBM=" +SerialNumberUtil.getInstance().getCompanyCode();;
		return (String) DatabaseHandlerDao.getInstance().queryForObject(sql);
	}
	@Override
	public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
		 // 1. 获取所有关联表的要删除的IDS
        String tableName = "T_ZZ_CCXX";
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
			defaultCode=AppDefineUtil.RELATION_AND+" A_QYBM = '"+code+"' "+AppDefineUtil.RELATION_AND+" B_QYBM = '"+code+"' ";
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
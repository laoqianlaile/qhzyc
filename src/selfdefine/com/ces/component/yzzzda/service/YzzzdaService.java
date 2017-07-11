package com.ces.component.yzzzda.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class YzzzdaService extends
		TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@PersistenceContext
	private EntityManager entityManager;

	public Object getZzdaByzzbh( String zzpch){
		String sql = "select t.ZZPCH, t.PZTYM ,t.PZQC,t.YJRQ,t.SL from T_YZ_ZZDA t where t.zzpch = '"+zzpch +"' "+ defaultCode() ;
		return DatabaseHandlerDao.getInstance().queryForMap(sql);
		
	}
	public Object getSurplusSl(String zzpch) {
		// 获得总共的头数
		String sql = " select t.SL from T_YZ_ZZDA T where T.QYZT=1 AND t.ZZPCH='"
				+ zzpch + "' " + defaultCode();
		int totalNum = Integer.parseInt(DatabaseHandlerDao.getInstance()
				.queryForObject(sql).toString());
		sql = " select NVL(SUM(T.SL),0) from T_YZ_JLXX T where t.ZZPCH='"
				+ zzpch + "' " + defaultCode();
		int hasBeenFarmedNum = Integer.parseInt(DatabaseHandlerDao
				.getInstance().queryForObject(sql).toString());
		return totalNum - hasBeenFarmedNum;
	}

	public Map<String, Object> getZzda() {
		String sql = "select T.ZZPCH,T.PZTYM,T.PZQC,T.YJRQ from T_YZ_ZZDA T where T.QYZT=1 "
				+ defaultCode();
		sql += " order by QYZT ASC,ZZPCH DESC ";
		List<Map<String, String>> list = (List<Map<String, String>>) entityManager
				.createNativeQuery(sql).unwrap(SQLQuery.class)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getResultData(list);
	}

	public Map<String, Object> getResultData(List<Map<String, String>> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", data);
		return result;
	}

	/**
	 * 默认权限过滤
	 * 
	 * @return
	 */
	public String defaultCode() {
		String code = SerialNumberUtil.getInstance().getCompanyCode();
		String defaultCode = " ";
		if (code != null && !"".equals(code))
			defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code
					+ "' ";
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
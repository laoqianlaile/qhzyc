package com.ces.component.zzzpxx.service;

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
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class ZzzpxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 	获得复合框下拉列表数据
	 * 
	 * @return
	 */
	public Map<String, Object> getZpxx() {
		String sql = "select T.ZZPCH,T.ZPRQ,T.SYCT,T.SYCZ from T_ZZ_ZPXX T WHERE T.QYZT=1 "+defaultCode()+" ORDER BY T.ZZPCH DESC";
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
	 * @return
	 */
	public  String defaultCode(){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode=AppDefineUtil.RELATION_AND+" QYBM = '"+code+"' ";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		  // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
	}
	
	
	public Object getZpxxFzr(String zzpch) {
		String sql = "select T.FZR,T.FZRBH,T.SYCT,T.SYCZ,T.ZPRQ,T.SPMC,T.SYCTBH from T_ZZ_ZPXX T WHERE T.QYZT=1 AND T.zzpch="+zzpch+" "+defaultCode()+" ORDER BY T.ZPRQ DESC";
		List<Map<String, String>> list = (List<Map<String, String>>) entityManager
				.createNativeQuery(sql).unwrap(SQLQuery.class)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
}
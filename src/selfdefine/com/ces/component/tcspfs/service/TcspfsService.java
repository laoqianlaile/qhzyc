package com.ces.component.tcspfs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TcspfsService extends
		TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void setDao(TraceShowModuleDao dao) {
		super.setDao(dao);
	}

	public Map<String, Object> getJyzda(String zt) {
		StringBuffer sql = new StringBuffer("select T.JYZBM as LYYBH,T.JYZMC as LYY,S.NAME as JYLX from T_PC_JYZDA T left join T_XTPZ_CODE S on T.JYLX = S.VALUE where 1=1 and S.code_type_code = 'JYLX' ");
		if(!"".equals(zt) && zt!=null)
			sql.append(" and  T.zt="+zt);
		sql.append(defaultCode());
		List<Map<String, String>> list = (List<Map<String, String>>) entityManager
				.createNativeQuery(sql.toString()).unwrap(SQLQuery.class)
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
			defaultCode = AppDefineUtil.RELATION_AND + " BAPFSCBM = '" + code
					+ "' ";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		// 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
		return AppDefineUtil.RELATION_AND + " ZT=1 " + defaultCode();
	}

	public Object getJyzGrid(){
//		StringBuffer sql = new StringBuffer("select T.JYZBM as LYYBH,T.JYZMC as LYY,S.NAME as JYLX from T_PC_JYZDA T left join T_XTPZ_CODE S on T.JYLX = S.VALUE where 1=1 and S.code_type_code = 'JYLX' ");
		String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select T.SZCDJYZH,T.HZBM,T.HZMC,T.CJRQ,T.ID from T_TZ_SZJYXX T where T.TZCBM LIKE '%" + tzcbm
				+ "%' ";
		List<Map<String, String>> list = (List<Map<String, String>>) entityManager
				.createNativeQuery(sql).unwrap(SQLQuery.class)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getResultData(list);
	}
}
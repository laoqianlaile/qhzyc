package com.ces.component.yzslda.service;

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
public class YzsldaService extends
		TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@PersistenceContext
	private EntityManager entityManager;

	public Map<String, Object> getSlda(String self) {
		String sql = "select T.SLBH,T.SLTYM,T.SLQC,T.RKRQ from T_YZ_SlDA T where 1=1  ";
        if (!self.isEmpty()) {
            sql += "and ((T.QYZT=1 AND to_char(sysdate,'yyyy-mm-dd')<=T.DQRQ ) or slbh = '"+self+"') ";
        } else {
            sql += " and T.QYZT=1 AND to_char(sysdate,'yyyy-mm-dd')<=T.DQRQ ";
        }
        sql += defaultCode();
		sql += " ORDER BY T.RKRQ DESC , T.SLBH DESC";
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
}
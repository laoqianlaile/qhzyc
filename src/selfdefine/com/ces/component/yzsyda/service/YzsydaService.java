package com.ces.component.yzsyda.service;

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
public class YzsydaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	
	@PersistenceContext
	private EntityManager entityManager;

	public Map<String, Object> getSyda() {

		String sql = "SELECT T.SYBH,T.SYTYM,T.SYQC,T.RKRQ FROM T_YZ_SYDA T where T.QYZT=1 AND to_char(sysdate,'yyyy-mm-dd')<T.DQRQ "
				+ defaultCode();
		sql += " ORDER BY T.RKRQ DESC,T.SYBH DESC";
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
	 * 	根据兽药编号获得兽药的休药期天数
	 * @param sybh
	 * @return
	 */
	public Object getSydaXyq(String sybh){
		String sql="SELECT T.XYQ FROM T_YZ_SYDA T where T.SYBH='"+sybh+"' "
				+ defaultCode();
		return DatabaseHandlerDao.getInstance().queryForList(sql);
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
	
	
}
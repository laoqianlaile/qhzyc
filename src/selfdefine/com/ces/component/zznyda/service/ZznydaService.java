package com.ces.component.zznyda.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.zznyda.dao.ZznydaDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class ZznydaService extends TraceShowModuleDefineDaoService<StringIDEntity, ZznydaDao> {

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 	获得复合框下拉列表数据
	 * 
	 * @return
	 */
	public Map<String, Object> getNyda() {
		String sql = "SELECT T.NYBH,T.NYTYM,T.NYQC,T.RKRQ,T.NCQ from T_ZZ_NYDA T WHERE T.QYZT=1 "+defaultCode()+" ORDER BY T.NYBH DESC";
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
	public Object getNcqData(String qybm,String nybh){
		return getDao().getNcqData(qybm, nybh);
		
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

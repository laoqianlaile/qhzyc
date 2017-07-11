package com.ces.component.tzjyzda.service;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.tzjyzda.dao.TzjyzdaDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TzjyzdaService extends TraceShowModuleDefineDaoService<StringIDEntity, TzjyzdaDao> {

	@Override
	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
		String condition="";
		condition += AppDefineUtil.RELATION_AND;
		condition += "BALTJDBM=";
		condition += SerialNumberUtil.getInstance().getCompanyCode();
		//condition += " and ZT=1";
        return condition;//过滤屠宰场编码和检疫状态
    }
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public Object getJyzda(String jyzbh){
    	String sql = "select T.JYZMC,T.GSZCDJZHHSFZH,T.JYZXZ,T.FRDB,T.SJHM,T.JYZBM from T_TZ_JYZDA T where T.JYZBM ='"+jyzbh+"'";
    	List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return map;
    }
	
	public String getDddByJyzbh(String jyzbh){
		String ddd = getDao().getDddByJyzbh(jyzbh)!=null?getDao().getDddByJyzbh(jyzbh):"";
		return ddd;
	}
}

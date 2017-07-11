package com.ces.component.lsscjcxx.service;

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
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class LsscjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
	@PersistenceContext
	private EntityManager entityManager;

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
	
    public Map<String,Object>  getJcbh(){
    	String code=SerialNumberUtil.getInstance().getCompanyCode();
    	String sql = "select T.ID,T.SCJCBH,T.PFSBM,T.PFSMC,T.JCRQ from T_LS_SCJCXX T where T.LSSCBM = '"+code+"'";
    	List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return getResultData(list);
    }
    
    public List<Map<String, String>>  getJcbhByPfsbm(String pfsbm){
    	String sql = "select T.ID,T.SCJCBH,T.PFSBM,T.PFSMC,T.JCRQ from T_LS_SCJCXX T where T.PFSBM = '"+pfsbm+"'";
    	List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return list;
    }
    
    public Map<String,Object> getResultData(List<Map<String,String>> data){
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("data", data);
    	return result;
    }
    
}
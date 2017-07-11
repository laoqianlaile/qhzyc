package com.ces.component.lsjyzda.service;

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
public class LsjyzdaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void setDao(TraceShowModuleDao dao){
		super.setDao(dao);
	}
	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode=" AND BALTJDBM = '"+code+"'";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		  // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
	}
	public List<Map<String, String>>  getJyzda(String gljyzbm){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
    	String sql = "select T.JYZBM,T.JYZMC,T.JYZXZ,T.FRDB,T.SJHM,T.GSZCDJZHHSFZH,S.NAME as JYLX from T_LS_JYZDA T left join T_XTPZ_CODE S on T.JYLX = S.VALUE where ((JYZLX=1 OR JYZLX=3)) and T.zt=1  and  S.code_type_code = 'LSJYLX' and T.baltjdbm ='"+code+"' ";
    	if(null != gljyzbm){
    		sql += "and T.JYZBM <> "+gljyzbm ;
    	}
    	sql += " ORDER BY T.JYZBM DESC";
    	List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return list;
    }
    public List<Map<String, String>>  getCpJyzda(){
        String code=SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select T.JYZBM,T.JYZMC,T.JYZXZ,T.FRDB,T.SJHM,T.GSZCDJZHHSFZH,S.NAME as JYLX from T_LS_JYZDA T left join T_XTPZ_CODE S on T.JYLX = S.VALUE where  T.zt=1  and  S.code_type_code = 'LSJYLX' and T.baltjdbm ='"+code+"' ";
        sql += " ORDER BY T.JYZBM DESC";
        List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }
    public Map<String,Object> getResultData(List<Map<String,String>> data){
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("data", data);
    	return result;
    }

	public List<Map<String, String>>  getPfs(String gljyzbm){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select T.JYZBM,T.JYZMC,T.JYZXZ,T.FRDB,T.SJHM,T.GSZCDJZHHSFZH,S.NAME as JYLX from T_LS_JYZDA T left join T_XTPZ_CODE S on T.JYLX = S.VALUE where ((JYZLX=2 OR JYZLX=3)) and T.zt=1  and  S.code_type_code = 'LSJYLX' and T.baltjdbm ='"+code+"' ";
		if(null != gljyzbm){
			sql += "and T.JYZBM <> "+gljyzbm ;
		}
        sql += " order by T.JYZBM desc";
		List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}


	public Object getJyzdaByJyzbh(String jyzbh){
    	String sql = "select T.JYZMC,T.GSZCDJZHHSFZH,T.JYZXZ,T.FRDB,T.SJHM,T.JYZBM from T_LS_JYZDA T where T.JYZBM ='"+jyzbh+"'order by T.JYZBM desc";
    	List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return map;
    }

    public String  getJyzlx(String jylxValue){
        String sql = "select s.remark from T_XTPZ_CODE S where  S.code_type_code = 'LSJYLX' and  S.VALUE='"+jylxValue+"'";
        return entityManager.createNativeQuery(sql).getSingleResult().toString();
    }
}
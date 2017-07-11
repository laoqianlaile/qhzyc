package com.ces.component.csjyzda.service;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.csjyzda.dao.CsjyzdaDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class CsjyzdaService extends TraceShowModuleDefineDaoService<StringIDEntity, CsjyzdaDao> {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void setDao(CsjyzdaDao dao){
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
	
	public List<Map<String, String>>  getJyzda(){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
    	String sql = "select T.JYZBM,T.JYZMC,T.JYZXZ,T.FRDB,T.SJHM,T.GSZCDJZHHSFZH,S.NAME as JYLX from T_CS_JYZDA T left join T_XTPZ_CODE S on T.JYLX = S.VALUE where T.zt=1 and S.code_type_code = 'JYLX' and T.baltjdbm ='"+code+"'";
    	List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return list;
    }
	public Object getJyzdaByJyzbh(String jyzbh){
    	String sql = "select jyzmc, gszcdjzhhsfzh, jyzxz, frdb, sjhm, a.jyzbm" +
				" from T_COMMON_JYZ a" +
				" left join T_COMMON_JYZ_DETAIL b" +
				" on a.ID = b.PID" +
				" where b.xtlx = 'CS'" +
				" and a.jyzbm = '"+jyzbh+"'";
    	List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return map;
    }
    
}

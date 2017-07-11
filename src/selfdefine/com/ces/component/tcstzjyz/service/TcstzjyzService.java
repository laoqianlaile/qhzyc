package com.ces.component.tcstzjyz.service;

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
public class TcstzjyzService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@PersistenceContext
	private EntityManager entityManager;
	
	public Map<String,Object>  getJyzda(String opponent){
		String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select T.JYZBM,T.JYZMC,T.FRDB,T.SJHM,T.GSZCDJZHHSFZH,S.NAME as JYLX from T_TZ_JYZDA T left join T_XTPZ_CODE S on T.JYLX = S.VALUE where T.zt=1 and S.code_type_code = 'TZJYLX' and T.BALTJDBM = '"
				 +tzcbm+"'  ";
		if((opponent!=null)&&(!opponent.isEmpty())){
			sql += " and T.JYZBM <> '"+opponent+"'";
		}
		sql += " order by T.JYZBM DESC ";
    	List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return getResultData(list);
    }
	
	public Map<String,Object> getResultData(List<Map<String,String>> data){
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("data", data);
    	return result;
    }
}
package com.ces.component.xuqiugenzongjuzhengoujian.service;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Component
public class XuqiugenzongjuzhengoujianService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
    public void setDao(TraceShowModuleDao dao){
		super.setDao(dao);
	}		
	
	public Object getJcxqmc(String xqmc,String xh){
		String sql ;
//		if("".equals(xqmc)){
//			sql= "select X.* from XQGZJZ X where X.XH != "+xh+" and X.XQMC is null";
//		}else{
			sql= "select X.* from XQGZJZ X where X.XH != "+xh+" and X.XQMC = '"+xqmc+"'";
//		}
		List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return map;
	}
	
	
	public Object getPdbcorxg(String xh){
		String sql ;
			sql= "select X.* from XQGZJZ X where X.XH = "+xh;
		List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return map;
	}			
	
}
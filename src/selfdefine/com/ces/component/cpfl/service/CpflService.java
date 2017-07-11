package com.ces.component.cpfl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.cpfl.dao.CpflDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class CpflService extends TraceShowModuleDefineDaoService<StringIDEntity, CpflDao> {

    @PersistenceContext
    private EntityManager entityManager;
	
    public Map<String, Object>  getCpfl(){
        String mdid = SerialNumberUtil.getInstance().getIdByCompanyCode();
    	String sql = "select distinct  T.SPBM,T.SPMC from T_QYPT_SCSPXX_ZHGL T where t.mdid = '" + mdid + "' order by SPBM ASC ";
    	List<Map<String, Object>> map =  (List<Map<String, Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return getResultData(map);
    }

    public String getSpmc(String spbm){
    	return getDao().getSpmc(spbm);
    }
    
    public Map<String,Object> getResultData(List<Map<String,Object>> data){
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("data", data);
    	return result;
    }
}

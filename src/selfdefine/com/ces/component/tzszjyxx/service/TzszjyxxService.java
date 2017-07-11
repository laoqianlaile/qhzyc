package com.ces.component.tzszjyxx.service;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.component.tzszjyxx.dao.TzszjyxxDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class TzszjyxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TzszjyxxDao> {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
		String condition="";
		condition += AppDefineUtil.RELATION_AND;
		condition += "TZCBM=";
		condition += SerialNumberUtil.getInstance().getCompanyCode();
		//condition += " and ZT=2";
        return condition;//过滤屠宰场编码
    }
	
    public Object getJyyGrid() {
    	String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
    	String sql = "select T.GZRYBH as LYYBH,T.XM as LYY,T.GW from T_TZ_GZRYDA T where T.TZCBM LIKE '" +tzcbm+ "'";
		List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return map;
    }
	
    public Object getSzjcxx(String szcdjyzh) {
    	return getDao().getSzjcxx(szcdjyzh);
    }
    
    public Object getSzjyByHzbm(String hzbm) {
		String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select T.SZCDJYZH,T.HZBM,T.HZMC,T.SZJCRQ,T.ID from T_TZ_SZJCXX T where T.TZCBM LIKE '%" + tzcbm + "%' and T.JYZT = '1' and T.HZBM LIKE '%" + hzbm + "%' order by T.SZCDJYZH desc";
		List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return map;
    }
    
    public void setJyzt(String jyzh) {
		String sql = "update T_TZ_SZJCXX T set T.JYZT='1' where T.SZCDJYZH = '" +jyzh+ "'";
		DatabaseHandlerDao.getInstance().executeSql(sql);
	}
}

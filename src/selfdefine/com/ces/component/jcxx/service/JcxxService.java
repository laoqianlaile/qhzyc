package com.ces.component.jcxx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import com.ces.component.jcxx.dao.JcxxDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class JcxxService extends
		TraceShowModuleDefineDaoService<StringIDEntity, JcxxDao> {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public Object getJclhbhByPfsbm(String pfsbm){
		Date date = new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String compareDate = df.format(new Date(date.getTime()-4*24*60*60*1000));
		String sql = "select T.JCLHBH,T.PFSMC,T.PFSBM,T.JCRQ,T.ID from T_PC_JCLHXX T where T.PFSBM LIKE '%" + pfsbm + "%' and T.JCRQ>='"+compareDate+"'" +defaultCode()+" order by T.JCLHBH desc";
		List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return map;
	}

	//获取进场理货PID
	public Object getJclhxxpid(String jclhbh){
		String sql = "";
		sql = "select distinct  b.pid from t_pc_jclhxx a,T_PC_JCLHMXXX b where a.id = b.pid and a.jclhbh = '"+jclhbh+"'";
		Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
		return map;
	}
	
	//获取商品名称
	public Object getSpmcByPid(String id){
		String sql = "select distinct T.SPBM,T.SPMC from T_PC_JCLHMXXX T where T.PID LIKE '%" + id + "%' order by t.spbm asc";
		List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		Map<String,Object> dataMap = new HashMap<String, Object>();
		dataMap.put("data",map);
		return dataMap;
	}
	
	//根据理货编号获取理货信息ID
	public Object getIdByJclhbh(String jclhbh){
		Object result = getDao().getIdByJclhbh(jclhbh);
		return result;
	}
	
	//
	public List<Map<String,Object>> getPfsmcByJclhbh(String jclhbh){
		String sql = "select T.PFSMC,T.PFSBM from T_PC_JCLHXX T where T.JCLHBH ='" + jclhbh + "' " +defaultCode();
		List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return map;
	}
	
	public Object getJypzh(String jhpch){
		return getDao().getJypzh(jhpch);
	}
	
	
	/**
	 * 默认权限过滤
	 * 
	 * @return
	 */
	 public String defaultCode() {
		String code = SerialNumberUtil.getInstance().getCompanyCode();
		String defaultCode = " ";
		if (code != null && !"".equals(code))
			defaultCode = AppDefineUtil.RELATION_AND + " PFSCBM = '" + code
					+ "' ";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		// 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
		return defaultCode();
	}

    public Object getLhbhById(String id) {
        String sql = "select jclhbh from t_pc_jcxx where id = ?";
        return DatabaseHandlerDao.getInstance().queryForObject(sql,new Object[]{id});
    }
}

package com.ces.component.tcsjclhpc.service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TcsjclhpcService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
	
	@PersistenceContext
	private EntityManager entityManager;


    public Map<String,Object>  getJclhpc(){
    	String sql = "select T.ID,T.JCLHBH,T.PFSBM,T.PFSMC,T.JCRQ from T_PC_JCLHXX T where 1=1 "+ defaultCode();
    	List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return getResultData(list);
    }
    
    public Map<String,Object> getResultData(List<Map<String,String>> data){
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("data", data);
    	return result;
    }
    /**
	 * 默认权限过滤
	 * 
	 * @return
	 */
	public String defaultCode() {
		Date date = new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String compareDate = df.format(new Date(date.getTime()-4*24*60*60*1000));
		String code = SerialNumberUtil.getInstance().getCompanyCode();
		String defaultCode = " ";
		if (code != null && !"".equals(code))
			defaultCode = AppDefineUtil.RELATION_AND + " PFSCBM = '" + code+ "' and JCRQ>='"+compareDate+"'";
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
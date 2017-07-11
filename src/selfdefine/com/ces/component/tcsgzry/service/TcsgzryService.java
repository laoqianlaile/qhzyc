package com.ces.component.tcsgzry.service;

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
public class TcsgzryService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@PersistenceContext
	private EntityManager entityManager;
	
	//获取工作人员(检测员)
	public Object getGzryda(String zt){
		StringBuffer sql = new StringBuffer("select T.XM,T.GZRYBH,T.GW from T_PC_GZRYDA T where 1=1 ");
		if(!"".equals(zt))
			sql.append(" and ZT="+1);
		sql.append(defaultCode());
		sql.append("  ORDER BY T.GZRYBH DESC");
		List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getResultData(map);
	}
	
	public Map<String,Object> getResultData(List<Map<String,Object>> data){
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
			String code = SerialNumberUtil.getInstance().getCompanyCode();
			String defaultCode = " ";
			if (code != null && !"".equals(code))
				defaultCode = AppDefineUtil.RELATION_AND + " PFSCBM = '" + code+ "' ";
			return defaultCode;
		}

		@Override
		protected String buildCustomerFilter(String tableId,
				String componentVersionId, String moduleId, String menuId,
				Map<String, Object> paramMap) {
			// 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
			return AppDefineUtil.RELATION_AND+" ZT=1 "+defaultCode();
		}
}
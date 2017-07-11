package com.ces.component.zzctda.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class ZzctdaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@Override
	public void setDao(TraceShowModuleDao dao){
		super.setDao(dao);
	}
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 对种植田的空闲和使用的状态进行修改
	 * @param zhbh 所属账户编号
	 * @param ctbh 需要操作的账户下的菜田编号
	 * @param zzzt 需要改变成的状态  1.种植、2.空闲
	 */
	public  void updCtzzzt(String ctbh,String zzzt){
		String sql="update T_ZZ_CTDA set zzzt="+zzzt+" where ctbh='"+ctbh +"' " + defaultCode();
		entityManager.createNativeQuery(sql).executeUpdate();
	}
	
	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode=AppDefineUtil.RELATION_AND+" QYBM = '"+code+"' ";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		  // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
	}
    
	/**
	 * 	获得复合框下拉列表数据
	 * 
	 * @return
	 */
	public Map<String, Object> getCtda() {
		String sql = "select T.CTBH,T.CTMC from T_ZZ_CTDA T WHERE T.QYZT=1 AND t.ZZZT=2 "+defaultCode()+" ORDER BY T.CTBH DESC";
		List<Map<String, String>> list = (List<Map<String, String>>) entityManager
				.createNativeQuery(sql).unwrap(SQLQuery.class)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return getResultData(list);
	}

	public Map<String, Object> getResultData(List<Map<String, String>> data) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", data);
		return result;
	}
	
	public Object getCtdaFzr(String ctbh) {
		String sql = "select T.FZR,T.FZRBH  from T_ZZ_CTDA T WHERE T.ctbh="+ctbh +defaultCode();
		List<Map<String, String>> list = (List<Map<String, String>>) entityManager
				.createNativeQuery(sql).unwrap(SQLQuery.class)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

}
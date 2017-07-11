package com.ces.component.yzjlxx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class YzjlxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@PersistenceContext
	private EntityManager entityManager;
	
	
	
	
    public Map<String,Object>  getJlxx(String qyzt,String self){
    	StringBuffer sql = new StringBuffer("select T.YZPCH,T.JLRQ,T.SYZS,T.ZZPCH,T.PZTYM from T_YZ_JLXX T where 1=1 ");
        sql.append(" and (T.QYZT="+ qyzt);
        sql.append(" or T.YZPCH='"+ self +"') ");
    	sql.append(defaultCode());
    	sql.append(" order by t.JLRQ desc , T.YZPCH DESC");
    	List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return getResultData(list);
    }
   
    public Object getJlxxFzr(String yzpch){
    	//获得养殖批次号 进栏日期、使用猪舍 仔猪批次号 品种通用名
    	String sql = "select T.YZPCH,T.JLRQ,T.SYZS,T.ZZPCH,T.PZTYM,T.FZR,T.FZRBH,T.SL,T.PZQC from T_YZ_JLXX T where T.YZPCH='"+yzpch+"' " + defaultCode();
    	return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }
    
    public void updateJlzt(String yzpch){
    	//修改控制养殖批次失效
    	String sql="update T_YZ_JLXX set qyzt = 2 where YZPCH='"+yzpch +"' "+ defaultCode();
    	DatabaseHandlerDao.getInstance().executeSql(sql);
    	//猪舍为使用中变成空闲状态
    	sql="update T_YZ_ZSDA set SYZT = 2 where ZSBH in(select t.syzs from  T_YZ_JLXX t where t.YZPCH='"+yzpch +"') "+ defaultCode();
    	DatabaseHandlerDao.getInstance().executeSql(sql);
    }
    
	public Map<String,Object> getResultData(List<Map<String,String>> data){
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("data", data);
    	return result;
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
}
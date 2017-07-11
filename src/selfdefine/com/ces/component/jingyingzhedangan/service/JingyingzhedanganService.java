package com.ces.component.jingyingzhedangan.service;

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
public class JingyingzhedanganService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@PersistenceContext
	private EntityManager entityManager;
	
    public Object getMaxId(){
    	String number=SerialNumberUtil.getInstance().getSerialNumber("PC","JHPCH",true);
    	return null;
    }
    public String modifyNumber(String number){
    	String nenwYgbh = Integer.parseInt(number==null?"0":(String)number)+1+"";
    	//String nenwYgbh = 1+"";
    	if(nenwYgbh.length()<3){
    		if(1 == nenwYgbh.length()){
    			nenwYgbh = "00" + nenwYgbh;
    		}
    		else{
    			nenwYgbh = "0" + nenwYgbh;
    		}
    	}
    	return nenwYgbh;
    }
    
    public Object getJyzda(String jyzbh){
    	String sql = "select T.JYZMC,T.GSZCDJZHHSFZH,T.JYZXZ,T.FRDB,T.SJHM,T.JYZBM from T_PC_JYZDA T where T.JYZBM ='"+jyzbh+"' " + defaultCode();
    	List<Map<String,Object>> map = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return map;
    }
    public Object getFilterJyzda(String jyzbm){
    	String sql = " select T.JYZBM,T.JYZMC,T.JYZXZ,T.FRDB,T.SJHM,T.GSZCDJZHHSFZH,S.NAME as JYLX  from T_PC_JYZDA T left join T_XTPZ_CODE S on T.JYLX = S.VALUE where 1=1 and S.code_type_code = 'JYLX'  and  T.zt=1 AND T.JYZBM <>'"+jyzbm+"' " + defaultCode() +" ORDER BY T.JYZBM DESC";
    	List<Map<String,Object>> li = (List<Map<String,Object>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	if(li.size()<1){
			return null;
		}
		Map map = new HashMap();
		map.put("data", li);
    	return map;
    }
    
    /**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode=AppDefineUtil.RELATION_AND+" BAPFSCBM = '"+code+"' ";
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
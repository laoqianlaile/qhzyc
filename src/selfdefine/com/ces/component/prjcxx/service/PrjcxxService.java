package com.ces.component.prjcxx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class PrjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	/**
	 * 猪肉批发获取经营者备案信息
	 * @return
	 */
	public Map<String,Object>  getJyzda(String opponent){
		String sql = "select T.JYZBM as PFSBM,T.JYZMC as PFSMC,S.NAME as JYLX from T_PR_JYZDA T left join T_XTPZ_CODE S on T.JYLX = S.VALUE where T.zt=1 and T.BALTJDBM='"
				+ SerialNumberUtil.getInstance().getCompanyCode()
				+ "' and S.code_type_code = 'PRJYZLX'";
		if((opponent!=null)&&(!opponent.isEmpty())){
			sql += " and T.JYZBM <> '"+opponent+"'";
		}
		sql += " ORDER BY T.JYZBM DESC";
    	List<Map<String, Object>> list =  DatabaseHandlerDao.getInstance().queryForMaps(sql);
    	return getResultData(list);
    }
    
    public Map<String,Object> getResultData(List<Map<String,Object>> data){
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("data", data);
    	return result;
    }
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
    	return AppDefineUtil.RELATION_AND + "PFSCBM='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
    }
}
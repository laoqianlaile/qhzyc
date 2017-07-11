package com.ces.component.prjyzda.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class PrjyzdaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@Override
	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
		String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        return  AppDefineUtil.RELATION_AND +"BALTJDBM='"+qybm+"'";
    }
	/**
	 * 根据经营者编码获取商品到达地
	 * @param jyzbm
	 * @return
	 */
	public Map getJyzda(String jyzbm){
		String sql = "select t.spddd from t_pr_jyzda t where t.jyzbm = '"+jyzbm+"'";
		return DatabaseHandlerDao.getInstance().queryForMap(sql);
	}
	/**
	 * 获取经营者档案信息
	 * @param jyzbm
	 * @return
	 */
	public Map getJyzdaInfo(String jyzbm){
		String sql = "select t.* from t_pr_jyzda t where t.jyzbm='"+jyzbm+"'";
		return DatabaseHandlerDao.getInstance().queryForMap(sql);
	}
}
package com.ces.component.prjcxx2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.coral.lang.StringUtil;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class Prjcxx2Service extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
		return AppDefineUtil.RELATION_AND + "PFSCBM='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
    }
	public Map<String,Object> getZspzhList(String pfsbm){
		String sql = "SELECT T.ZSPZH,T.PFSBM,T.PFSMC,T.JCRQ FROM T_PR_RPJCXX T where T.ZT='1' and T.PFSBM LIKE '%"
				     +pfsbm+"%' and T.PFSCBM = '"+SerialNumberUtil.getInstance().getCompanyCode()+"' order by T.ZSPZH desc";
		List<Map<String,Object>> li = new ArrayList<Map<String,Object>>();
		li = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		if(li.size()<1){
			return null;
		}
		Map map = new HashMap();
		map.put("data", li);
	return map;
}
	/**
	 * 获取商品信息
	 * @param zspzh
	 * @return
	 */
	public Map getSpxx(String zspzh){
		String sql = "SELECT T.SPBM,T.SPMC FROM T_PR_JCXX2 T where T.ZSPZH='"+zspzh+"'";
		return DatabaseHandlerDao.getInstance().queryForMap(sql);
	}
	/**
	 * 根据ID回去追溯凭证号
	 * @param id
	 * @return
	 */
	public String getZspzh(String id){
		String sql = "SELECT T.ZSPZH FROM T_PR_JCXX2 T WHERE T.ID = '"+id+"'";
		Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
		return (String) map.get("ZSPZH");
	}
	/**
	 * 新增时修改进场肉品信息状态为2
	 * @param zspzh
	 * @return
	 */
	public int updateZspzh(String zspzh){
		String sql = "UPDATE T_PR_RPJCXX SET ZT = '2' WHERE ZSPZH = '"+zspzh+"'";
		return DatabaseHandlerDao.getInstance().executeSql(sql);
	}
	/**
	 * 修改或删除时修改进场肉品信息状态为1
	 * @param zspzh
	 * @return
	 */
	public int updateOldZspzh(String zspzh){
		String sql = "UPDATE T_PR_RPJCXX SET ZT = '1' WHERE ZSPZH = '"+zspzh+"'";
		return DatabaseHandlerDao.getInstance().executeSql(sql);
	}
	/**
	 * 新增或修改检测信息
	 * @param tableId
	 * @param entityJson
	 * @param paramMap
	 * @param oldZspzh
	 * @param zspzh
	 * @return
	 */
	@Transactional
    public Map<String, String> saveJcxx(String tableId, String entityJson, Map<String, Object> paramMap,String oldZspzh,String zspzh) {
		if (StringUtil.isNotBlank(oldZspzh))
			this.updateOldZspzh(oldZspzh);
		this.updateZspzh(zspzh);
		return super.save(tableId, entityJson, paramMap);
	}
	public Map<String,Object> getJyzxxByZspzh(String zspzh) {
		String sql = "select T.PFSMC,T.PFSBM from T_PR_RPJCXX T where T.ZSPZH = '"+zspzh+"'";
		Map<String,Object> map = (Map<String,Object>)DatabaseHandlerDao.getInstance().queryForMap(sql);
		return map;
	}
}
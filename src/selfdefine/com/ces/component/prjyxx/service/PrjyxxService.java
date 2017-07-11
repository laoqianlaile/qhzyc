package com.ces.component.prjyxx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.coral.lang.StringUtil;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class PrjyxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
	
	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		// 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
		// return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
		return AppDefineUtil.RELATION_AND + "PFSCBM='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
	}
	/**
	 * 根据ID回去追溯凭证号
	 * @param id
	 * @return
	 */
	public String getZspzh(String id){
		String sql = "SELECT T.ZSPZH FROM T_PR_JYXX T WHERE T.ID = '"+id+"'";
		Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
		return (String) map.get("ZSPZH");
	}
	public Map<String,Object> getZspzhList(String pfsbm){
		String sql = "SELECT T.ZSPZH,T.PFSBM,T.PFSMC,T.JCRQ,T.SPMC,T.SPBM FROM T_PR_JCXX2 T where T.ZT='1' AND T.JCJG='1' and T.PFSBM LIKE '%"
					 +pfsbm+"%' and T.PFSCBM = '" +SerialNumberUtil.getInstance().getCompanyCode()+ "' order by T.ZSPZH desc";
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
	 * 新增时修改进场肉品信息状态为2
	 * @param zspzh
	 * @return
	 */
	public int updateZspzh(String zspzh){
		String sql = "UPDATE T_PR_JCXX2 SET ZT = '2' WHERE ZSPZH = '"+zspzh+"'";
		return DatabaseHandlerDao.getInstance().executeSql(sql);
	}
	/**
	 * 修改或删除时修改进场肉品信息状态为1
	 * @param zspzh
	 * @return
	 */
	public int updateOldZspzh(String zspzh){
		String sql = "UPDATE T_PR_JCXX2 SET ZT = '1' WHERE ZSPZH = '"+zspzh+"'";
		return DatabaseHandlerDao.getInstance().executeSql(sql);
	}
	/**
	 * 根据追溯凭证号获取总的重量（除去当前修改的重量）
	 * @param zspzh
	 * @param id
	 * @return
	 */
	public float getSumZlByZspzh(String zspzh,String id){
		StringBuffer sql = new StringBuffer("SELECT NVL(SUM(T.ZL),0) AS TOTAL FROM T_PR_JYXX T WHERE T.ZSPZH = '"+zspzh+"'");
		if(StringUtil.isNotBlank(id)){
			sql.append(" AND T.ID <> '"+id+"'");
		}
		Map map = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
		return Float.parseFloat(map.get("TOTAL").toString());
	}
	/**
	 * 根据追溯凭证号获取检测信息中肉的重量
	 * @param zspzh
	 * @return
	 */
	public float getJcxx2ZlByZspzh(String zspzh){
		String sql = "SELECT NVL(SUM(T.ZL),0) AS TOTAL FROM T_PR_RPJCXX T WHERE T.ZSPZH = '"+zspzh+"'";
		Map map = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
		return Float.parseFloat(map.get("TOTAL").toString());
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
    public Map<String, String> saveJyxx(String tableId, String entityJson, Map<String, Object> paramMap,String oldZspzh,String zspzh,float total,float oldTotal) {
		float jcZl = this.getJcxx2ZlByZspzh(zspzh);
		if(jcZl-total==0){//判断当前追溯凭证号重量
			this.updateZspzh(zspzh);
		}else{
			this.updateOldZspzh(zspzh);
		}
		if (StringUtil.isNotBlank(oldZspzh)){//若追溯凭证号修改了，判断修改之前的
			float oldJcZl = this.getJcxx2ZlByZspzh(oldZspzh);
			if(oldJcZl-oldTotal==0){
				this.updateZspzh(oldZspzh);
			}else{
				this.updateOldZspzh(oldZspzh);
			}
		}
		Map<String,String> dataMap = super.save(tableId, entityJson, paramMap);
		/*****同步追溯信息*****/
    	TCsptZsxxEntity entity = new TCsptZsxxEntity();
    	entity.setJypzh(dataMap.get("ZSPZH"));
    	entity.setJhpch(dataMap.get("JHPCH"));
    	entity.setZsm(dataMap.get("ZSM"));
    	entity.setQybm(dataMap.get("PFSCBM"));
    	entity.setQymc(dataMap.get("PFSCMC"));
    	entity.setJyzbm(dataMap.get("PFSBM"));
    	entity.setJyzmc(dataMap.get("PFSMC"));
    	entity.setMjmc(dataMap.get("LSSMC"));
    	entity.setMjbm(dataMap.get("LSSBM"));
    	entity.setXtlx("5");
    	entity.setRefId(dataMap.get("ID"));
    	TraceChainUtil.getInstance().syncZsxx(entity);
    	/********结束*******/
		return dataMap;
	}
	
	public Map<String,Object> getPfsxxByZspzh(String zspzh){
		String sql = "select T.PFSMC,T.PFSBM from T_PR_JCXX2 T where T.ZSPZH = '"+zspzh+"'";
		Map<String,Object> map = (Map<String,Object>)DatabaseHandlerDao.getInstance().queryForMap(sql);
		return map;
	}

	public String validateWeight(String zspzh, int weight) {
		String sql = "select T.zl as totalWeight,dealWeight from t_Pr_Rpjcxx T,(Select NVL(sum(s.zl),0) as dealWeight from t_Pr_Jyxx s where s.zspzh='"+zspzh+"') where T.zspzh='"+zspzh+"'";
		Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
		int totalWeight = Integer.parseInt(map.get("TOTALWEIGHT").toString());
		int dealWeight = Integer.parseInt(map.get("DEALWEIGHT").toString());
		if (weight+dealWeight>totalWeight) {
			return String.valueOf(totalWeight-dealWeight);
		}
		return "TRUE";
	}
}
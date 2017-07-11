package com.ces.component.sczzpzxx.service;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bdz on 2015/8/12.
 */
@Component
public class SczzpzxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@Override
	@Autowired
	public void setDao(TraceShowModuleDao dao) {
		super.setDao(dao);
	}

	/**
	 * 验证品类编码的唯一性
	 * @param plbh
	 * @return
	 * @author zhaoben
	 */
	public boolean uniqueCheck(String plbh){
		String sql = "select T.PLBH from T_ZZ_DPZXX T where T.PLBH= '"+plbh+"' and T.is_delete = '0' and T.QYBM = ?";
    	List<Map<String,Object>> listData = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{SerialNumberUtil.getInstance().getCompanyCode()});
    	Map<String,Object> dataMap = new HashMap<String, Object>();
    	dataMap.put("data", listData);
    	if(listData.size()>0){
    		return true;
    	}
		return false;
	}
	/**
	 * 获取品类信息
	 *
	 * @return
	 */
	public List getPlxx() {
		String sql = "SELECT T.PLBH,T.PL FROM T_ZZ_DPZXX T WHERE T.is_delete = '0' and T.QYBM = ? ORDER BY T.PLBH";
		//获取基地下所有品类
		List<Map<String, Object>> li = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{SerialNumberUtil.getInstance().getCompanyCode()});
		List dataList = new ArrayList();
		//转换tree格式
		for (Map m : li) {
			Map map = new HashMap<String, String>();
			sql = "SELECT T.ID,T.PZ FROM T_ZZ_XPZXX T WHERE T.is_delete = '0' and T.PLBH= ? AND T.QYBM=? ORDER BY T.PZBH";
			List<Map<String, Object>> plList = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{m.get("PLBH").toString(), SerialNumberUtil.getInstance().getCompanyCode()});
			List children = new ArrayList();
			for (Map plMap : plList) {
				Map seMap = new HashMap();
				seMap.put("id", plMap.get("ID"));
				seMap.put("name", plMap.get("PZ"));
				seMap.put("pzzjd", "pzzjd");
				children.add(seMap);
			}
			map.put("id", m.get("PLBH").toString());
			map.put("name", m.get("PL").toString());
			map.put("pzjd", "pzjd");
			map.put("children", children);
			dataList.add(map);
		}
		return dataList;
	}

	/**
	 * 根据品类获取底下品种信息
	 *
	 * @param plbh
	 */
	public List<Map<String, Object>> getPzxxByPlxx(String plbh) {
		String sql = "select t.*,s.tpbcmc from t_zz_xpzxx t, t_zz_pltp s where t.id = s.zbid(+) and t.plbh = ? and t.qybm = ? and t.is_delete = '0'";
		return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{plbh, SerialNumberUtil.getInstance().getCompanyCode()});
	}

	/**
	 * 删除品种信息
	 * @param plbh
	 * @param pzids
	 */
	@Transactional
	public List<String> delPzxxByPlxx(String plbh, String[] pzids) {
		List<String> pzList = new ArrayList<String>();
		//检查品种信息是否被使用过了，如果使用过了则不删除
		for (String pzid : pzids) {
			String sql = "select * from t_zz_xpzxx t where t.id = ?";
			Map<String, Object> pzxxMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{pzid});
			String pzPlbh = String.valueOf(pzxxMap.get("PLBH"));
			String pzPzbh = String.valueOf(pzxxMap.get("PZBH"));
			String pz = String.valueOf(pzxxMap.get("PZ"));
			if (checkPzUsed(pzPlbh, pzPzbh)) {
				pzList.add(pz);
			}
		}
		if (!pzList.isEmpty()) {
			return pzList;
		}
		StringBuffer sql = new StringBuffer();
		List<Object> sqlParam = new ArrayList<Object>();
		sqlParam.add(plbh);
		sqlParam.add(SerialNumberUtil.getInstance().getCompanyCode());
		sql.append("update t_zz_xpzxx t set t.is_delete = '1' where t.plbh = ? and t.qybm = ? and t.id in (");
		for (String pzid : pzids) {
			sql.append("?,");
			sqlParam.add(pzid);
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		DatabaseHandlerDao.getInstance().executeSql(sql.toString(), sqlParam.toArray());
		return pzList;
	}

	/**
	 * 检测品类品种信息是否已经被使用
	 * @param pzbh
	 * @param pzbh
	 * @return
	 */
	public boolean checkPzUsed(String plbh, String pzbh) {
		//产品信息检测
		String cpSql = "select count(*) as count from t_zz_cpxxgl t, t_zz_cpxxglplxx s where t.id = s.pid and s.dpzbh = ? and s.pzbh = ? and t.is_delete = '0'";
		Object cpCount = DatabaseHandlerDao.getInstance().queryForObject(cpSql, new Object[]{plbh, pzbh});
		if (Integer.parseInt(String.valueOf(cpCount)) > 0) {
			return true;
		}
		//种植方案信息检测
		String faSql = "select count(*) as count from t_zz_fapz t where t.plbh = ? and t.pzbh = ? and t.is_delete = '0'";
		Object faCount = DatabaseHandlerDao.getInstance().queryForObject(faSql, new Object[]{plbh, pzbh});
		if (Integer.parseInt(String.valueOf(faCount)) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取品类编号
	 * @return
	 */
	public String getPlbh() {
		return SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZPLBH", false);
	}
}
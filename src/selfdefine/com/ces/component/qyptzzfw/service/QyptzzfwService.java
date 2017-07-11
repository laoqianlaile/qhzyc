package com.ces.component.qyptzzfw.service;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.service.appmanage.ShowModuleService;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.web.listener.XarchListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class QyptzzfwService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	/**
	 * 根据组织UUID获取增值服务信息
	 *
	 * @return
	 */
	public List<Map<String, Object>> getIncrementServiceById(String uuid) {

		String querySql = "select t.zhbh from T_QYPT_ZHGL t where t.id = '" + uuid + "'";
		Object orgId = DatabaseHandlerDao.getInstance().queryForObject(querySql);
		String sql = "select t.zzfwbh,t.zzfwz,t.qysj,t.dqsj from T_QYPT_ZZFWXX t where t.sfdqfw = '1' and t.zhbh = '" + orgId + "'";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return list == null ? new ArrayList<Map<String, Object>>() : list;
	}

	/**
	 * 获取所有的增值服务信息
	 *
	 * @return
	 */
	public List<Map<String, Object>> getAllIncrementServices() {
		String sql = "select * from T_QYPT_FWGL t where t.fwlx = '2'";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		for (Map<String, Object> map : list) {
			map.put("GGMC", CodeUtil.getInstance().getCodeName("QYPTZZFWGG", String.valueOf(map.get("GG"))));
		}
		return list;
	}

	/**
	 * 授予增值服务
	 */
	@Transactional
	public void setIncrementService(String uuid, Map<String, String> services) {
		String querySql = "select t.zhbh from T_QYPT_ZHGL t where t.id = '" + uuid + "'";
		Object orgId = DatabaseHandlerDao.getInstance().queryForObject(querySql);
		String updateSql = "update T_QYPT_ZZFWXX t set t.sfdqfw = '2' where t.zhbh = '" + orgId + "'";
		DatabaseHandlerDao.getInstance().executeSql(updateSql);
		Map<String, String> map = new HashMap<String, String>();
		map.put("ZHBH", String.valueOf(orgId));
		String qysj = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		map.put("QYSJ",qysj);
		map.put("SFDQFW","1");
		for (String sid : services.keySet()) {
			map.remove(AppDefineUtil.C_ID);
			map.put("ZZFWBH", sid);
			map.put("ZZFWZ", services.get(sid));
			XarchListener.getBean(ShowModuleService.class).save("T_QYPT_ZZFWXX", map, null);
		}
	}

}
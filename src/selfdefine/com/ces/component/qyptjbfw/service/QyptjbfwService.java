package com.ces.component.qyptjbfw.service;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.JSON;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.service.appmanage.ShowModuleService;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.web.listener.XarchListener;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QyptjbfwService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	/**
	 * 获取所有基本服务信息
	 *
	 * @return
	 */
	public List<Map<String, Object>> getAllBaseServices() {
		String sql = "select t.id,t.fwbh,t.fwmc from T_QYPT_FWGL t where t.fwlx = '1'";
		return DatabaseHandlerDao.getInstance().queryForMaps(sql);

	}

	/**
	 * 根据组织ID获取基本服务信息
	 *
	 * @return
	 */
	public List<Map<String, Object>> getBaseServicesByOrgId(String orgId) {
		String sql = "select m.fwbh,m.fwmc,nvl(n.fwsl,'0') as fwsl, n.qysj,n.dqsj from "
				+ " t_qypt_fwgl m, "
				+ " (select t.fwbh,t.qysj,t.dqsj,t.fwsl from t_qypt_jbfwxx t, t_qypt_zhgl s where t.zhbh = s.zhbh and t.zhbh = '" + orgId + "' and t.sfdqfw = '1') n "
				+ " where m.fwlx = '1' and m.qyzt = '1' and m.fwbh = n.fwbh(+)";

		//String sql = "select t.fwbh,t.qysj,t.dqsj,nvl(t.fwsl,'0') as fwsl from t_qypt_jbfwxx t where t.sfdqfw = '1' and t.qybh = '" + orgId + "'";
		List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return dataList;
	}

	/**
	 * 授予组织用户服务
	 *
//	 * @param orgId
//	 * @param serviceId
	 * @return
	 */
//	services.push({
//		zhbh : $.ns('namespaceId${idSuffix}').zhbh,
//				fwbh : rowData.FWBH,
//				fwsl : rowData.FWSL,
//				qysj : qysj,
//				dqsj : dqsj
//	});
	@Transactional
	public void setBaseService(String zhbh, String services) {
		String upSql = "update T_QYPT_JBFWXX t set t.sfdqfw = '2' where t.zhbh = '" + zhbh + "'";
		DatabaseHandlerDao.getInstance().executeSql(upSql);
		List<Map<String, String>> list = JSON.fromJSON(services, new TypeReference<List<Map<String, String>>>() {
		});
		if(list!=null){for (Map<String, String> map : list) {
			map.put("sfdqfw", "1");
			save("T_QYPT_JBFWXX", map, null);
		}}

	}

	/**
	 * 获取所有的增值服务信息
	 *
	 * @return
	 */
	public List<Map<String, Object>> getAllIncrementServices() {
		String sql = "select t.fwbh,t.fwmc,t.gg from T_QYPT_FWGL t where t.fwlx = '2'";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		for (Map<String, Object> map : list) {
			map.put("GGMC", CodeUtil.getInstance().getCodeName("QYPTZZFWGG", String.valueOf(map.get("GG"))));
		}
		return list;
	}

	/**
	 * 根据组织ID获取增值服务信息
	 *
	 * @return
	 */
	public List<Map<String, Object>> getIncrementServiceByOrgId(String orgId) {

		String sql = "select t.zzfwbh,t.zzfwz,t.qysj,t.dqsj from T_QYPT_ZZFWXX t where t.qybh = '" + orgId + "'";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return list == null ? new ArrayList<Map<String, Object>>() : list;
	}

	/**
	 * 授予增值服务
	 */
	@Transactional
	public void setIncrementService(String orgId, Map<String, String> services) {
		String delSql = "delete from T_QYPT_ZZFWXX t where t.qybh = '" + orgId + "'";
		DatabaseHandlerDao.getInstance().executeSql(delSql);
		Map<String, String> map = new HashMap<String, String>();
		map.put("QYBH", orgId);
		for (String sid : services.keySet()) {
			map.remove(AppDefineUtil.C_ID);
			map.put("ZZFWBH", sid);
			map.put("ZZFWZ", services.get(sid));
			XarchListener.getBean(ShowModuleService.class).save("T_QYPT_ZZFWXX", map, null);
		}
	}

	/**
	 * 查看申请企业的服务信息
	 *
	 * @param orgId
	 * @return
	 */
	public Map<String, Object> orgServiceView(String orgId) {
		String sql = "select * from V_QYPT_QYFWXX v where v.qybh = '" + orgId + "'";
		List<Map<String, Object>> services = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		Map<String, Object> data = new HashMap<String, Object>();
		if (services.size() == 0) {
			String orgName = FacadeUtil.getOrgInfoFacade().findByID(orgId).getName();
			data.put("QYBH", orgId);
			data.put("QYMC", orgName);
			return data;
		}
		List<Map<String, Object>> incrementServices = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> service : services) {
			data.put("QYBH", service.get("QYBH"));
			data.put("QYMC", service.get("QYMC"));
			if ("1".equals(String.valueOf(service.get("FWLX")))) {
				data.put("JBFWBH", service.get("FWBH"));
				data.put("JBFWLX", service.get("FWLX"));
				data.put("JBFWMC", service.get("FWMC"));
				data.put("JBFWNR", service.get("FWNR"));
			} else if ("2".equals(String.valueOf(service.get("FWLX")))) {
				Map<String, Object> incrementService = new HashMap<String, Object>();
				incrementService.put("ZZFWBH", service.get("FWBH"));
				incrementService.put("ZZFWLX", service.get("FWLX"));
				incrementService.put("ZZFWMC", service.get("FWMC"));
				incrementService.put("ZZFWNR", service.get("FWNR"));
				incrementService.put("ZZFWZ", service.get("ZZFWZ"));
				incrementService.put("ZZFWGG", CodeUtil.getInstance().getCodeName("QYPTZZFWGG", String.valueOf(service.get("GG"))));
				incrementServices.add(incrementService);
			}
		}
		data.put("ZZFW", incrementServices);
		return data;
	}

	/**
	 * 查看基本服务内容
	 *
	 * @return
	 */
	public Object getBaseServiceContent(String serviceId) {
		String sql = "select t.fwnr from T_QYPT_FWGL t where t.fwbh = '" + serviceId + "'";
		return DatabaseHandlerDao.getInstance().queryForObject(sql);
	}

	/**
	 * 根据账户的UUID获取基本服务编号
	 *
	 * @return
	 */
	public Object getBaseServicesValue(String uuid) {
		String sql = "select t.zhbh from t_qypt_zhgl t where t.id = '" + uuid + "'";
		Object zhbh = DatabaseHandlerDao.getInstance().queryForObject(sql);
		sql = "select * from t_qypt_jbfwxx s where s.sfdqfw = '1' and s.zhbh = '" + String.valueOf(zhbh) + "'";
		Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
		if (map.size() == 0) {
			map.put("ZHBH", zhbh);
		}
		return map;
	}

}
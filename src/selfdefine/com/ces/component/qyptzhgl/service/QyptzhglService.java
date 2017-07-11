package com.ces.component.qyptzhgl.service;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.facade.OrgInfoFacade;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.TableNameUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class QyptzhglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@Override
	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
		return AppDefineUtil.RELATION_AND + "AUTH_PARENT_ID = '-1'";
	}

	/**
	 * 修改所有的企业名称
	 * dataMap中存在企业名称时即代表了修改了企业名称
	 */
	@Override
	@Transactional
	public String save(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
		String zhbh = dataMap.get("ZHBH");
		String qymc = dataMap.get("QYMC");
		//修改了企业名称
		if (qymc != null && !"".equals(qymc)) {
			updateAllQymc(getChildMdbhs(zhbh), qymc);
		}
		return super.save(tableName, dataMap, paramMap);
	}

	/**
	 * 获取企业底下所有的门店编号
	 * @param zhbh 企业账户编号
	 * @throws SQLException
	 */
	private List<String> getChildMdbhs(String zhbh) {
		String sql = "select t.auth_id from t_qypt_zhgl t where t.zhbh = '" + zhbh + "'";
		Object orgId = DatabaseHandlerDao.getInstance().queryForObject(sql);
		OrgInfoFacade orgInfo = CommonUtil.getOrgInfoFacade();
		List<OrgInfo> childOrgs =  orgInfo.findChildsByParentId(String.valueOf(orgId));
		List<String> mdbhs = new ArrayList<String>();
		for (OrgInfo childOrg : childOrgs) {
			sql = "select t.zhbh from t_qypt_zhgl t where t.auth_id = '" + childOrg.getId() + "'";
			mdbhs.add(String.valueOf(DatabaseHandlerDao.getInstance().queryForObject(sql)));
		}
		return mdbhs;
	}

	/**
	 * 修改所有的企业名称
	 *
	 * @param zhbh 门店账户编号
	 * @param qymc 企业名称
	 * @return
	 */
	@Transactional
	public void updateAllQymc(List<String> zhbhs, String qymc) {
		if (zhbhs == null || zhbhs.isEmpty()) {
			return;
		}
		StringBuffer zhbhBuffer = new StringBuffer();
		for (String zhbh : zhbhs) {
			zhbhBuffer.append(",'").append(zhbh).append("'");
		}
		zhbhBuffer.deleteCharAt(0);
		for (String tableName : TableNameUtil.getAllSystemQydaTableName()) {
			if (tableName == null) {
				continue;
			}
			StringBuffer buffer = new StringBuffer();
			buffer.append("update ").append(tableName).append(" set qymc = '").append(qymc).append("' where qybm in (").append(zhbhBuffer.toString()).append(")");
			DatabaseHandlerDao.getInstance().executeSql(buffer.toString());
		}
	}

	/**
	 * 根据uuid获取账户编号
	 * @param uuid
	 * @return
	 */
	public Object getZhbhByUUID(String uuid) {
		String sql = "select t.zhbh from t_qypt_zhgl t where t.id = '" + uuid + "'";
		return DatabaseHandlerDao.getInstance().queryForObject(sql);
	}
}
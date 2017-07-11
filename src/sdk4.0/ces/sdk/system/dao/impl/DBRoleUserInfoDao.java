package ces.sdk.system.dao.impl;

import java.math.BigDecimal;
import java.util.*;

import ces.sdk.util.StringUtil;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;

import com.ces.xarch.plugins.common.global.Constants;

import ces.sdk.system.bean.RoleUserInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.RoleUserInfoDao;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.JdbcUtil;

public class DBRoleUserInfoDao extends DBBaseDao implements RoleUserInfoDao{

	/**
	 * 查询参数
	 */
	private static String ROLE_USER_SELECT_PARAM = null;
	/**
	 * 更新参数
	 */
	private static String ROLE_USER_UPDATE_PARAM = null;

	static {
		String[] columnsNameArray = ROLE_USER_COLUMNS_NAME.split(",");
		String[] param = JdbcUtil.getSelectParamAndUpdateParam(columnsNameArray);
		ROLE_USER_SELECT_PARAM = param[0];
		ROLE_USER_UPDATE_PARAM = param[1];
	}
	
	@Override
	public RoleUserInfo save(RoleUserInfo roleUserInfo) {
		
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardInsertSql(ROLE_USER_COLUMNS_NAME, ROLE_USER_TABLE_NAME, JdbcUtil.generatorPlaceHolder(roleUserInfo.getClass().getDeclaredFields()).toString());
		
		roleUserInfo.setId(super.generateUUID());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,JdbcUtil.getColumnsValueByBean(ROLE_USER_COLUMNS_NAME, roleUserInfo,ADD_STATUS));

		return roleUserInfo;
	}


	@Override
	public void delete(String id) {
		Object[] idsArray = id.split(",");
		String sql = StandardSQLHelper.standardDeleteSql(ROLE_USER_TABLE_NAME, "id in " + JdbcUtil.generatorPlaceHolder(idsArray));

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,idsArray);
		
	}

	
	@Override
	public int delete(String roleId, String userId, String system_id,String orgId) {
		List<String> condition = new ArrayList<String>();
		if(StringUtils.isNotBlank(roleId)){
			condition.add("role_id = '"+roleId+"'");
		}
		if(StringUtils.isNotBlank(userId)){
			condition.add("user_id = '"+userId+"'");
		}
		if(StringUtils.isNotBlank(system_id)){
			condition.add("system_id = '"+system_id+"'");
		}
		if(StringUtils.isNotBlank(orgId)){
			condition.add("org_id = '"+orgId+"'");
		}
		condition.add("role_id not in ('"+Constants.Role.DEFAULT_ROLE_ID+"','"+Constants.Role.ROLE_INFO_ID+"')");
		String sql = StandardSQLHelper.standardDeleteSql(ROLE_USER_TABLE_NAME, condition.toArray());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		return qr.update(sql);
	}


	@Override
	public Set<String> findSystemIdsByUserId(String userId) {
		String sql = StandardSQLHelper.standardSelectSql("system_id", ROLE_USER_TABLE_NAME, "", "user_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<String> systemIds = qr.query(sql, new ColumnListHandler<String>(),userId);
		HashSet<String> distinctSystemIds = new HashSet<String>(systemIds);
		return distinctSystemIds;
	}


	@Override
	public Set<String> findRoleIdsByUserId(String userId) {
		String sql = StandardSQLHelper.standardSelectSql("role_id", ROLE_USER_TABLE_NAME, "", "user_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<String> roleIds = qr.query(sql, new ColumnListHandler<String>(),userId);
		HashSet<String> distinctRoleIds = new HashSet<String>(roleIds);
		return distinctRoleIds;
	}

	@Override
	public int findByConditionTotal(Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		List<String> conditionParam = new ArrayList<String>();
		if (null != param && param.size() != 0) {
			String roleId = param.get("roleId");
			if (StringUtil.isNotBlank(roleId)) {
				condition.add("role_id = ?");
				conditionParam.add(roleId);
			}
			String userId = param.get("userId");
			if (StringUtil.isNotBlank(userId)) {
				condition.add("user_id = ?");
				conditionParam.add(userId);
			}
			String orgId = param.get("orgId");
			if (StringUtil.isNotBlank(orgId)) {
				condition.add("org_id = ?");
				conditionParam.add(orgId);
			}
		}
		String sql = StandardSQLHelper.standardSelectTotalSql(ROLE_USER_TABLE_NAME, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql.toString(), new ScalarHandler<BigDecimal>(), conditionParam.toArray());
		if (result != null) {
			return result.intValue();
		}
		return 0;
	}

	@Override
	public List<RoleUserInfo> findByConditionPage(Map<String, String> param, int currentPage, int pageSize) {
		List<String> condition = new ArrayList<String>();
		List<String> conditionParam = new ArrayList<String>();
		if (null != param && param.size() != 0) {
			String roleId = param.get("roleId");
			if (StringUtil.isNotBlank(roleId)) {
				condition.add("role_id = ?");
				conditionParam.add(roleId);
			}
			String userId = param.get("userId");
			if (StringUtil.isNotBlank(userId)) {
				condition.add("user_id = ?");
				conditionParam.add(userId);
			}
			String orgId = param.get("orgId");
			if (StringUtil.isNotBlank(orgId)) {
				condition.add("org_id = ?");
				conditionParam.add(orgId);
			}
		}
		String sql = StandardSQLHelper.standardSelectSql(ROLE_USER_SELECT_PARAM, ROLE_USER_TABLE_NAME, null, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<RoleUserInfo> roleUserInfos = qr.query(sql, new BeanListHandler<RoleUserInfo>(RoleUserInfo.class), conditionParam.toArray());
		return roleUserInfos;
	}

}

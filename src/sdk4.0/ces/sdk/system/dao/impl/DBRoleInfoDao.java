package ces.sdk.system.dao.impl;

import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.RoleInfoDao;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.ArrayUtil;
import ces.sdk.util.JdbcUtil;
import ces.sdk.util.StringUtil;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/6/29.
 */
public class DBRoleInfoDao extends DBBaseDao implements RoleInfoDao {

	/**
	 * 查询参数
	 */
	private static String ROLE_SELECT_PARAM = null;
	/**
	 * 更新参数
	 */
	private static String ROLE_UPDATE_PARAM = null;

	static {
		String[] columnsNameArray = ROLE_COLUMNS_NAME.split(",");
		String[] param = JdbcUtil.getSelectParamAndUpdateParam(columnsNameArray);
		ROLE_SELECT_PARAM = param[0];
		ROLE_UPDATE_PARAM = param[1];
	}

	@Override
	public List<RoleInfo> findRolesBySystemId(String systemId, Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		List<String> conditionParam = new ArrayList<String>();
		condition.add("sr.system_id = ?");
		conditionParam.add(systemId);
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.add("r.name = ?");
				conditionParam.add(name);
			}
		}
		String[] selectParamArray = ROLE_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "r." + selectParamArray[i];
		}
		String selectParam = ArrayUtil.toString(selectParamArray);
		String fromTable = " " + ROLE_TABLE_NAME + " r inner join " + SYSTEM_ROLE_TABLE_NAME + " sr on r.id = sr.role_id";
		String sql = StandardSQLHelper.standardSelectSql(selectParam, fromTable, ORDER_BY_SHOWORDER, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<RoleInfo> roleInfos = qr.query(sql, new BeanListHandler<RoleInfo>(RoleInfo.class), conditionParam.toArray());
		return roleInfos;
	}

	/**
	 * 本方法 不提供任何功能
	 * 
	 * */
	@Override
	public List<RoleInfo> findRoleInfosByUserId(String userId, String sysCode) throws SystemFacadeException {
		List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setId("1");
		roleInfo.setName("超级管理员");
		roleInfo.setRoleKey("superrole");
		roleInfo.setComments("超级管理员");
		roleInfos.add(roleInfo);
		return roleInfos;
	}

	@Override
	public List<RoleInfo> findRoleInfosByUserId(String userId) throws SystemFacadeException {
		String[] selectParamArray = ROLE_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "r." + selectParamArray[i];
		}
		String selectParam = ArrayUtil.toString(selectParamArray);
		String fromTable = " " + ROLE_TABLE_NAME + " r inner join " + ROLE_USER_TABLE_NAME + " ru on r.id = ru.role_id";
		
		String sql = StandardSQLHelper.standardSelectSql(selectParam, fromTable, ORDER_BY_SHOWORDER, "user_id = ?");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<RoleInfo> roleInfos = qr.query(sql, new BeanListHandler<RoleInfo>(RoleInfo.class), userId);
		return roleInfos;
	}

	@Override
	public List<RoleInfo> find(Map<String, String> paramMap, int currentPage, int pageSize) {
		return null;
	}

	@Override
	public List<RoleInfo> findByCondition(Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		List<String> conditionParam = new ArrayList<String>();
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.add("name = ?");
				conditionParam.add(name);
			}
		}
		String sql = StandardSQLHelper.standardSelectSql(ROLE_SELECT_PARAM, ROLE_TABLE_NAME, ORDER_BY_SHOWORDER, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<RoleInfo> roleInfos = qr.query(sql, new BeanListHandler<RoleInfo>(RoleInfo.class), conditionParam.toArray());
		return roleInfos;
	}

	@Override
	public RoleInfo save(RoleInfo roleInfo) {
		String sql = StandardSQLHelper.standardInsertSql(ROLE_COLUMNS_NAME, ROLE_TABLE_NAME, JdbcUtil.generatorPlaceHolder(roleInfo.getClass().getDeclaredFields()).toString());
		roleInfo.setId(generateUUID());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, JdbcUtil.getColumnsValueByBean(ROLE_COLUMNS_NAME, roleInfo, ADD_STATUS));
		return roleInfo;
	}

	@Override
	public long findMaxOrderNo(String systemId) {
		String fromTable = " " + ROLE_TABLE_NAME + " r inner join " + SYSTEM_ROLE_TABLE_NAME + " sr on r.id = sr.role_id";
		String sql = StandardSQLHelper.standardSelectMaxSql("r.show_order", fromTable, "system_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(), systemId);
		if (result != null) {
			return result.longValue();
		}
		return -1L;
	}

	@Override
	public RoleInfo findByID(String id) {
		String sql = StandardSQLHelper.standardSelectSql(ROLE_SELECT_PARAM, ROLE_TABLE_NAME, null, "id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		RoleInfo roleInfo = qr.query(sql, new BeanHandler<RoleInfo>(RoleInfo.class), id);
		return roleInfo;
	}

	@Override
	public RoleInfo update(RoleInfo roleInfo) {
		String sql = StandardSQLHelper.standardUpdateSql(ROLE_UPDATE_PARAM, ROLE_TABLE_NAME, "id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, JdbcUtil.getColumnsValueByBean(ROLE_COLUMNS_NAME, roleInfo, UPDATE_STATUS));
		return roleInfo;
	}

	@Override
	public void delete(String id) {
		Object[] idsArray = id.split(",");
		String sql = StandardSQLHelper.standardDeleteSql(ROLE_TABLE_NAME, "id in " + JdbcUtil.generatorPlaceHolder(idsArray));
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, idsArray);
	}

	@Override
	public int findTotal(String systemId, Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		List<Object> conditionParam = new ArrayList<Object>();
		condition.add("sr.system_id = ?");
		conditionParam.add(systemId);
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.add("r.name like ? ");
				conditionParam.add("%" + name + "%");
			}
		}
		String fromTable = " " + ROLE_TABLE_NAME + " r inner join " + SYSTEM_ROLE_TABLE_NAME + " sr on r.id = sr.role_id";
		String sql = StandardSQLHelper.standardSelectTotalSql(fromTable, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(), conditionParam.toArray());
		if (result != null) {
			return result.intValue();
		}
		return 0;
	}

	@Override
	public List<RoleInfo> findRolePageBySystemId(String systemId, Map<String, String> param, int currentPage, int pageSize) {
		List<Object> queryParam = new ArrayList<Object>();
		StringBuffer condition = new StringBuffer();
		condition.append(" where t.id = s.role_id ");
		if (StringUtil.isNotBlank(systemId)) {
			condition.append(" and s.system_id = ? ");
			queryParam.add(systemId);
		}
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.append(" and t.name like ? ");
				queryParam.add("%" + name + "%");
			}
		}
		queryParam.add((currentPage - 1) * pageSize + 1);
		queryParam.add(currentPage * pageSize);
		String sql = MessageFormat.format(sql_Properties.getProperty("roleInfoFacade_find"), condition);
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<RoleInfo> roleInfos = qr.query(sql, new BeanListHandler<RoleInfo>(RoleInfo.class), queryParam.toArray());
		return roleInfos;
	}

	@Override
	public String findSystemIdByRole(String roleId) {
		String sql = StandardSQLHelper.standardSelectSql("system_id", SYSTEM_ROLE_TABLE_NAME, null, "role_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		String result = qr.query(sql, new ScalarHandler<String>(), roleId);
		return result;
	}


	@Override
	public int findResourceTotal(String roleId, Map<String, String> param) {
		StringBuilder condition = new StringBuilder();
		condition.append("and r.id = rs.role_id");
		List<String> conditionParam = new ArrayList<String>();
		conditionParam.add(roleId);
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.append(" and r.name like ? ");
				conditionParam.add(name);
			}
		}
		String fromTable = " " + ROLE_TABLE_NAME + " r," + ROLE_RES_TABLE_NAME + " rs ";
		String sql = StandardSQLHelper.standardSelectTotalSql(fromTable, "rs.role_id = ?");
		sql += condition.toString();
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(),conditionParam.toArray());
		if (result != null) {
			return result.intValue();
		}
		return 0;
	}

	@Override
	public int findUserInfosByRoleIdTotal(Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		List<String> conditionParam = new ArrayList<String>();
		condition.add("u.id = ru.user_id");
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.add("name like ?");
				conditionParam.add(name);
			}
			String roleId = param.get("roleId");
			if (StringUtil.isNotBlank(roleId)) {
				condition.add("role_id = ?");
				conditionParam.add(roleId);
			}
		}
		String fromTable = " " + USER_TABLE_NAME + " u," + ROLE_USER_TABLE_NAME + " ru ";
		String sql = StandardSQLHelper.standardSelectTotalSql(fromTable, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql.toString(), new ScalarHandler<BigDecimal>(), conditionParam.toArray());
		if (result != null) {
			return result.intValue();
		}
		return 0;
	}

	@Override
	public List<UserInfo> findUserInfosByRoleIdPage(String roleId, Map<String, String> param, int currentPage, int pageSize) {
		List<Object> queryParam = new ArrayList<Object>();
		StringBuilder condition = new StringBuilder();
		condition.append(" WHERE t.id = ru.user_id and ru.org_id = o.id");
		if (StringUtil.isNotBlank(roleId)) {
			condition.append(" and ru.role_id = ? ");
			queryParam.add(roleId);
		}
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.append(" and t.name like ? ");
				queryParam.add("%" + name + "%");
			}
		}
		queryParam.add((currentPage - 1) * pageSize + 1);
		queryParam.add(currentPage * pageSize);
		String sql = MessageFormat.format(sql_Properties.getProperty("roleUser_find"), condition);
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<UserInfo> userInfos = qr.query(sql.toString(), new BeanListHandler<UserInfo>(UserInfo.class), queryParam.toArray());
		return userInfos;
	}

	@Override
	public boolean hasRole(String roleId, String userId, String orgId) {
		String sql = StandardSQLHelper.standardSelectTotalSql(ROLE_USER_TABLE_NAME,"role_id = ?","user_id = ?","org_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(), roleId, userId, orgId);
		int totalNum = 0;
		if (result != null) {
			totalNum = result.intValue();
		}
		return totalNum > 0 ? true : false;
	}

	@Override
	public boolean hasResource(String roleId, String resourceId) {
		String sql = StandardSQLHelper.standardSelectTotalSql(ROLE_RES_TABLE_NAME,"role_id = ?","resource_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(), roleId, resourceId);
		int totalNum = 0;
		if (result != null) {
			totalNum = result.intValue();
		}
		return totalNum > 0 ? true : false;
	}

	@Override
	public void removeResource(String roleId, String id) {
		List<String> param = new ArrayList<String>();
		param.add(roleId);
		param.addAll(Arrays.asList(id.split(",")));
		String sql = StandardSQLHelper.standardDeleteSql(ROLE_RES_TABLE_NAME, "role_id = ?", "resource_id in" + JdbcUtil.generatorPlaceHolder(id.split(",")));
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, param.toArray());
	}

	@Override
	public List<RoleInfo> findRolesBysystemIdAndOrgId(String systemId, String parentOrgId) {
		String[] selectParamArray = ROLE_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "r."+selectParamArray[i];
		}
		String selectParam = ArrayUtil.toString(selectParamArray);
		String fromTable = " "+ROLE_TABLE_NAME+" r inner join "+ORG_ROLE_TABLE_NAME+" t_or on r.id = t_or.role_id";
		
		String sql = StandardSQLHelper.standardSelectSql(selectParam, fromTable, ORDER_BY_SHOWORDER, "t_or.system_id = ? and t_or.org_id = ?");

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		
		List<RoleInfo> roleInfos = qr.query(sql, new BeanListHandler<RoleInfo>(RoleInfo.class),systemId,parentOrgId);
		return roleInfos;
	}

}

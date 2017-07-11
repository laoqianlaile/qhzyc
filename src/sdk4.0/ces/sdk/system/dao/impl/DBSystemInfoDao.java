package ces.sdk.system.dao.impl;

import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.SystemInfoDao;
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
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/6/29.
 */
public class DBSystemInfoDao extends DBBaseDao implements SystemInfoDao {

	/**
	 * 查询参数
	 */
	private static String SYSTEM_SELECT_PARAM = null;
	/**
	 * 更新参数
	 */
	private static String SYSTEM_UPDATE_PARAM = null;

	static {
		String[] columnsNameArray = SYSTEM_COLUMNS_NAME.split(",");
		String[] param = JdbcUtil.getSelectParamAndUpdateParam(columnsNameArray);
		SYSTEM_SELECT_PARAM = param[0];
		SYSTEM_UPDATE_PARAM = param[1];
	}

	@Override
	public List<SystemInfo> find(Map<String, String> param, int currentPage, int pageSize) {
		StringBuilder condition = new StringBuilder();
		List<Object> queryParam = new ArrayList<Object>();
		queryParam.add((currentPage - 1) * pageSize + 1);
		queryParam.add(currentPage * pageSize);
		condition.append(" where 1=1 ");
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.append(" and name like ? ");
				queryParam.add("%" + name + "%");
			}
		}
		String sql = MessageFormat.format(sql_Properties.getProperty("systemFacade_find"), condition);
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<SystemInfo> systemInfos = qr.query(sql, new BeanListHandler<SystemInfo>(SystemInfo.class), queryParam.toArray());
		return systemInfos;
	}

	@Override
	public int findTotal(Map<String, String> param) {
		List<Object> queryParam = new ArrayList<Object>();
		List<String> condition = new ArrayList<String>();
		if (null != param && param.size() != 0) {
			String id = param.get("id");
			if (StringUtil.isNotBlank(id)) {
				condition.add(" id = ? ");
				queryParam.add(id);
			}
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.add(" name like ? ");
				queryParam.add("%" + name + "%");
			}
		}
		String sql = StandardSQLHelper.standardSelectTotalSql(SYSTEM_TABLE_NAME, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(), queryParam.toArray());
		if (result != null) {
			return result.intValue();
		}
		return 0;
	}

	@Override
	public SystemInfo findByID(String id) {
		String sql = StandardSQLHelper.standardSelectSql(SYSTEM_SELECT_PARAM, SYSTEM_TABLE_NAME, null, "id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		SystemInfo systemInfo = qr.query(sql, new BeanHandler<SystemInfo>(SystemInfo.class), id);
		return systemInfo;
	}

	@Override
	public SystemInfo save(SystemInfo systemInfo) {
		String sql = StandardSQLHelper.standardInsertSql(SYSTEM_COLUMNS_NAME, SYSTEM_TABLE_NAME, JdbcUtil.generatorPlaceHolder(systemInfo.getClass().getDeclaredFields()).toString());
		systemInfo.setId(generateUUID());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, JdbcUtil.getColumnsValueByBean(SYSTEM_COLUMNS_NAME, systemInfo, ADD_STATUS));
		return systemInfo;
	}

	@Override
	public SystemInfo update(SystemInfo systemInfo) {
		String sql = StandardSQLHelper.standardUpdateSql(SYSTEM_UPDATE_PARAM, SYSTEM_TABLE_NAME, "id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, JdbcUtil.getColumnsValueByBean(SYSTEM_COLUMNS_NAME, systemInfo, UPDATE_STATUS));
		return systemInfo;
	}

	@Override
	public void delete(String id) {
		Object[] idsArray = id.split(",");
		String sql = StandardSQLHelper.standardDeleteSql(SYSTEM_TABLE_NAME, "id in " + JdbcUtil.generatorPlaceHolder(idsArray));
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,idsArray);
	}

	@Override
	public List<SystemInfo> findByCondition(Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		List<String> conditionParam = new ArrayList<String>();
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.add("name = ?");
				conditionParam.add(name);
			}
		}
		String sql = StandardSQLHelper.standardSelectSql(SYSTEM_SELECT_PARAM, SYSTEM_TABLE_NAME, ORDER_BY_SHOWORDER, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<SystemInfo> systemInfos = qr.query(sql.toString(), new BeanListHandler<SystemInfo>(SystemInfo.class), conditionParam.toArray());
		return systemInfos;
	}

	@Override
	public long findMaxOrderNo() {
		String sql = StandardSQLHelper.standardSelectMaxSql("show_order", SYSTEM_TABLE_NAME);
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql.toString(), new ScalarHandler<BigDecimal>());
		if(result != null){
			return result.longValue();
		}
		return -1L;
	}

	@Override
	public boolean hasResource(String systemId) {
		String sql = StandardSQLHelper.standardSelectTotalSql(SYSTEM_RES_TABLE_NAME, "system_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(),systemId);
		int totalNum = 0;
		if (result != null) {
			totalNum = result.intValue();
		}
		return totalNum > 0 ? true : false;
	}

	@Override
	public boolean hasRole(String systemId) {
		String sql = StandardSQLHelper.standardSelectTotalSql(SYSTEM_ROLE_TABLE_NAME, "system_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(),systemId);
		int totalNum = 0;
		if (result != null) {
			totalNum = result.intValue();
		}
		return totalNum > 0 ? true : false;
	}

	@Override
	public List<SystemInfo> findAll() {
		String sql = StandardSQLHelper.standardSelectSql(SYSTEM_SELECT_PARAM, SYSTEM_TABLE_NAME, ORDER_BY_SHOWORDER, "");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<SystemInfo> systemInfos = qr.query(sql, new BeanListHandler<SystemInfo>(SystemInfo.class));
		return systemInfos;
	}

	@Override
	public List<SystemInfo> findSystemsByOrgId(String parentOrgId) {
		String[] selectParamArray = SYSTEM_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "s."+selectParamArray[i];
		}
		String selectParam = ArrayUtil.toString(selectParamArray);
		String fromTable = " "+SYSTEM_TABLE_NAME+" s inner join "+ORG_ROLE_TABLE_NAME+" t_or on s.id = t_or.system_id";
		
		String sql = StandardSQLHelper.standardSelectSql("distinct "+selectParam, fromTable, ORDER_BY_SHOWORDER, "t_or.org_id = ?");

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		
		List<SystemInfo> systemInfos = qr.query(sql, new BeanListHandler<SystemInfo>(SystemInfo.class),parentOrgId);
		return systemInfos;
	}

	@Override
	public SystemInfo findSystemsByCode(String code) {
		
		String sql = "select "+SYSTEM_SELECT_PARAM +" from "+SYSTEM_TABLE_NAME +" where code = ?";
		

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		
		SystemInfo systemInfo = qr.query(sql, new BeanHandler<SystemInfo>(SystemInfo.class),code);
		return systemInfo;
	}
}

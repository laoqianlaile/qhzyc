package ces.sdk.system.dao.impl;

import ces.sdk.system.bean.ResourceInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.ResourceInfoDao;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.ArrayUtil;
import ces.sdk.util.JdbcUtil;
import ces.sdk.util.StringUtil;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.ces.xarch.plugins.common.global.Constants;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/6/29.
 */
public class DBResourceInfoDao extends DBBaseDao implements ResourceInfoDao {

	/**
	 * 查询参数
	 */
	private static String RESOURCE_SELECT_PARAM = null;
	/**
	 * 更新参数
	 */
	private static String RESOURCE_UPDATE_PARAM = null;

	static {
		String[] columnsNameArray = RESOURCE_COLUMNS_NAME.split(",");
		String[] param = JdbcUtil.getSelectParamAndUpdateParam(columnsNameArray);
		RESOURCE_SELECT_PARAM = param[0];
		RESOURCE_UPDATE_PARAM = param[1];
	}

	@Override
	public ResourceInfo findByID(String id) {
		String sql = StandardSQLHelper.standardSelectSql(RESOURCE_SELECT_PARAM, RESOURCE_TABLE_NAME, null, "id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		ResourceInfo resourceInfo = qr.query(sql, new BeanHandler<ResourceInfo>(ResourceInfo.class), id);
		return resourceInfo;
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
			String parentId = param.get("parentId");
			if (StringUtil.isNotBlank(parentId)) {
				condition.add(" parent_id = ? ");
				queryParam.add(parentId);
			}
		}
		String sql = StandardSQLHelper.standardSelectTotalSql(RESOURCE_TABLE_NAME, condition.toArray());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(), queryParam.toArray());
		if (result != null) {
			return result.intValue();
		}
		return 0;
	}

	@Override
	public List<ResourceInfo> find(Map<String, String> param, int currentPage, int pageSize) {
		StringBuilder condition = new StringBuilder();
		List<Object> queryParam = new ArrayList<Object>();
		condition.append(" where 1=1 ");
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.append(" and name like ? ");
				queryParam.add("%" + name + "%");
			}
			String parentId = param.get("parentId");
			if (StringUtil.isNotBlank(parentId)) {
				condition.append(" and parent_id = ? ");
				queryParam.add(parentId);
			}
		}
		queryParam.add((currentPage - 1) * pageSize + 1);
		queryParam.add(currentPage * pageSize);
		String sql = MessageFormat.format(sql_Properties.getProperty("resourceInfoFacade_find"), condition);
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<ResourceInfo> resourceInfos = qr.query(sql, new BeanListHandler<ResourceInfo>(ResourceInfo.class), queryParam.toArray());
		return resourceInfos;
	}

	@Override
	public void delete(String id) {
		Object[] idsArray = id.split(",");
		String sql = StandardSQLHelper.standardDeleteSql(RESOURCE_TABLE_NAME, "id in " + JdbcUtil.generatorPlaceHolder(idsArray));
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, idsArray);
	}

	@Override
	public ResourceInfo save(ResourceInfo resourceInfo) {
		String sql = StandardSQLHelper.standardInsertSql(RESOURCE_COLUMNS_NAME, RESOURCE_TABLE_NAME, JdbcUtil.generatorPlaceHolder(resourceInfo.getClass().getDeclaredFields()).toString());

		if (StringUtil.isBlank(resourceInfo.getId())) {
			resourceInfo.setId(generateUUID());
		}

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, JdbcUtil.getColumnsValueByBean(RESOURCE_COLUMNS_NAME, resourceInfo, ADD_STATUS));
		return resourceInfo;
	}

	@Override
	public List<ResourceInfo> findChildsByParentId(String parentId) {
		String sql = StandardSQLHelper.standardSelectSql(RESOURCE_SELECT_PARAM, RESOURCE_TABLE_NAME, ORDER_BY_SHOWORDER, "parent_id = ?");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<ResourceInfo> resourceInfos = qr.query(sql, new BeanListHandler<ResourceInfo>(ResourceInfo.class), parentId);
		return resourceInfos;
	}

	@Override
	public ResourceInfo update(ResourceInfo resourceInfo) {
		String sql = StandardSQLHelper.standardUpdateSql(RESOURCE_UPDATE_PARAM, RESOURCE_TABLE_NAME, "id = ?");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, JdbcUtil.getColumnsValueByBean(RESOURCE_COLUMNS_NAME, resourceInfo, UPDATE_STATUS));
		return resourceInfo;
	}

	@Override
	public long findMaxOrderNo(String parentId) {
		String sql = StandardSQLHelper.standardSelectMaxSql("show_order", RESOURCE_TABLE_NAME, "parent_id = ? ");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(), parentId);
		if (result != null) {
			return result.longValue();
		}
		return -1L;
	}

	@Override
	public List<ResourceInfo> findByCondition(Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		List<String> conditionParam = new ArrayList<String>();
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.add("name = ?");
				conditionParam.add(name);
			}
			String parentId = param.get("parentId");
			if (StringUtil.isNotBlank(parentId)) {
				condition.add("parent_id = ?");
				conditionParam.add(parentId);
			}
			String resoureceKey = param.get("resoureceKey");
			if (StringUtil.isNotBlank(resoureceKey)) {
				condition.add("resourece_key = ?");
				conditionParam.add(resoureceKey);
			}
		}
		String sql = StandardSQLHelper.standardSelectSql(RESOURCE_SELECT_PARAM, RESOURCE_TABLE_NAME, ORDER_BY_SHOWORDER, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<ResourceInfo> resourceInfos = qr.query(sql, new BeanListHandler<ResourceInfo>(ResourceInfo.class), conditionParam.toArray());
		return resourceInfos;
	}

	@Override
	public List<ResourceInfo> findResInfosByRoleId(String roleId) throws SystemFacadeException {
		String[] selectParamArray = RESOURCE_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "r." + selectParamArray[i].trim();
		}
		String selectParam = ArrayUtil.toString(selectParamArray);
		String fromTable = " " + RESOURCE_TABLE_NAME + " r inner join " + ROLE_RES_TABLE_NAME + " rr on r.id = rr.resource_id";
		String sql = StandardSQLHelper.standardSelectSql(selectParam, fromTable, ORDER_BY_SHOWORDER, "rr.role_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<ResourceInfo> resourceInfos = qr.query(sql, new BeanListHandler<ResourceInfo>(ResourceInfo.class), roleId);
		return resourceInfos;
	}

	@Override
	public int findResourcesTotalBySystemId(String systemId, Map<String, String> param) {
		List<String> condition = new ArrayList<String>();
		List<String> conditionParam = new ArrayList<String>();
		condition.add("r.id = sr.resource_id");
		condition.add("sr.system_id = ?");
		conditionParam.add(systemId);
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.add("name like ?");
				conditionParam.add(name);
			}
			String parentId = param.get("parentId");
			if (StringUtil.isNotBlank(parentId)) {
				condition.add("parent_id = ?");
				conditionParam.add(parentId);
			}
		}
		String fromTable = " " + RESOURCE_TABLE_NAME + " r," + SYSTEM_RES_TABLE_NAME + " sr ";
		String sql = StandardSQLHelper.standardSelectTotalSql(fromTable, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>(), conditionParam.toArray());
		if (result != null) {
			return result.intValue();
		}
		return 0;
	}

	@Override
	public List<ResourceInfo> findResourcesPageBySystemId(String systemId, Map<String, String> param, int currentPage, int pageSize) {
		List<Object> queryParam = new ArrayList<Object>();
		StringBuffer condition = new StringBuffer();
		condition.append(" where t.id = tsr.resource_id ");
		if (StringUtil.isNotBlank(systemId)) {
			condition.append(" and tsr.system_id = ? ");
			queryParam.add(systemId);
		}
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.append(" and t.name like ? ");
				queryParam.add("%" + name + "%");
			}
			String parentId = param.get("parentId");
			if (StringUtil.isNotBlank(parentId)) {
				condition.append(" and t.parent_id = ? ");
				queryParam.add(parentId);
			}
		}
		queryParam.add((currentPage - 1) * pageSize + 1);
		queryParam.add(currentPage * pageSize);
		String sql = MessageFormat.format(sql_Properties.getProperty("resourceSystem_find"), condition);
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<ResourceInfo> resourceInfos = qr.query(sql, new BeanListHandler<ResourceInfo>(ResourceInfo.class), queryParam.toArray());
		return resourceInfos;
	}

	@Override
	public List<ResourceInfo> findResourcesBySystemId(String systemId, Map<String, String> param) {
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
			String parentId = param.get("parentId");
			if (StringUtil.isNotBlank(parentId)) {
				condition.add("r.parent_id = ?");
				conditionParam.add(parentId);
			}
		}
		String[] selectParamArray = RESOURCE_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "r." + selectParamArray[i];
		}
		String selectParam = ArrayUtil.toString(selectParamArray);
		String fromTable = " " + RESOURCE_TABLE_NAME + " r inner join " + SYSTEM_RES_TABLE_NAME + " sr on r.id = sr.resource_id";
		String sql = StandardSQLHelper.standardSelectSql(selectParam, fromTable, ORDER_BY_SHOWORDER, condition.toArray());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<ResourceInfo> resourceInfos = qr.query(sql, new BeanListHandler<ResourceInfo>(ResourceInfo.class), conditionParam.toArray());
		return resourceInfos;
	}

	@Override
	public String findSystemIdByResource(String resourceId) {
		String sql = StandardSQLHelper.standardSelectSql("system_id", SYSTEM_RES_TABLE_NAME, null, "resource_id = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		String result = qr.query(sql, new ScalarHandler<String>(), resourceId);
		return result;
	}

	@Override
	public void moveResource(String newPid, String[] resIds) {
		StringBuilder sql = new StringBuilder();
		sql.append("update " + RESOURCE_TABLE_NAME + " t set t.parent_id = ? where t.id in " + JdbcUtil.generatorPlaceHolder(resIds));
		System.out.println(sql);
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<Object> updateParam = new ArrayList<Object>();
		updateParam.add(newPid);
		for (String resId : resIds) {
			updateParam.add(resId);
		}
		qr.update(sql.toString(), updateParam.toArray());
	}

	@Override
	public List<ResourceInfo> findResourcesByUserIdAndSystemId(String userId, String systemId) {
		StringBuilder sql = new StringBuilder();
		List<Object> valueParams = new ArrayList<Object>();
		String[] selectParamArray = RESOURCE_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "t_r." + selectParamArray[i];
		}
		String selectParam = ArrayUtil.toString(selectParamArray);
		
		sql.append(" select distinct "+selectParam+" from "+ROLE_USER_TABLE_NAME+" t_r_u ")
			.append(" inner join "+ROLE_RES_TABLE_NAME+" t_r_r on t_r_r.role_id = t_r_u.role_id ")
			.append(" inner join "+RESOURCE_TABLE_NAME+" t_r on t_r_r.resource_id = t_r.id ")
			.append(" where 1=1 ");
		if(StringUtil.isNotBlank(userId)){
			sql.append(" and t_r_u.user_id = ? ");
			valueParams.add(userId);
		}
		if(StringUtil.isNotBlank(systemId)){
			sql.append(" and t_r_u.system_id = ? ");
			valueParams.add(systemId);
		}
		sql.append(" and t_r.id not in('"+Constants.Resource.AUTHSYSTEM+"','"+Constants.Resource.TOP+"') ");
		sql.append(ORDER_BY_SHOWORDER);
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<ResourceInfo> resourceInfos = qr.query(sql.toString(), new BeanListHandler<ResourceInfo>(ResourceInfo.class),valueParams.toArray());
		return resourceInfos;
	}

	@Override
	public List<ResourceInfo> findResourcesPageByRoleId(String roleId, Map<String, String> param, int currentPage, int pageSize) {
		List<Object> queryParam = new ArrayList<Object>();
		StringBuffer condition = new StringBuffer();
		condition.append(" where t.id = r.resource_id ");
		if (StringUtil.isNotBlank(roleId)) {
			condition.append(" and r.role_id = ? ");
			queryParam.add(roleId);
		}
		if (null != param && param.size() != 0) {
			String name = param.get("name");
			if (StringUtil.isNotBlank(name)) {
				condition.append(" and t.name like ? ");
				queryParam.add(name);
			}
		}
		queryParam.add((currentPage - 1) * pageSize + 1);
		queryParam.add(currentPage * pageSize);
		String sql = MessageFormat.format(sql_Properties.getProperty("resourceRole_find"), condition);
		System.out.println(sql);
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<ResourceInfo> resourceInfos = qr.query(sql.toString(), new BeanListHandler<ResourceInfo>(ResourceInfo.class), queryParam.toArray());
		return resourceInfos;
	}

	@Override
	public ResourceInfo findByKey(String resourceKey) {
		String sql = StandardSQLHelper.standardSelectSql(RESOURCE_SELECT_PARAM, RESOURCE_TABLE_NAME, null, "resourece_key = ?");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		ResourceInfo resourceInfo = qr.query(sql, new BeanHandler<ResourceInfo>(ResourceInfo.class), resourceKey);
		return resourceInfo;
	}

	@Override
	public List<ResourceInfo> findResourceByUserIdAndSystemCode(String userId, String systemCode) {
		StringBuilder sql = new StringBuilder();
		List<Object> valueParams = new ArrayList<Object>();
		String[] selectParamArray = RESOURCE_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "t_r." + selectParamArray[i];
		}
		String selectParam = ArrayUtil.toString(selectParamArray);

		sql.append(" select distinct "+selectParam+" from "+ROLE_USER_TABLE_NAME+" t_r_u ")
				.append(" inner join "+ROLE_RES_TABLE_NAME+" t_r_r on t_r_r.role_id = t_r_u.role_id ")
				.append(" inner join "+RESOURCE_TABLE_NAME+" t_r on t_r_r.resource_id = t_r.id ")
				.append(" inner join "+SYSTEM_RES_TABLE_NAME+" t_s_r on t_s_r.resource_id = t_r.id ")
				.append(" inner join "+SYSTEM_TABLE_NAME+" t_s on t_s.id = t_s_r.system_id ")
				.append(" where 1=1 ");
		if(StringUtil.isNotBlank(userId)){
			sql.append(" and t_r_u.user_id = ? ");
			valueParams.add(userId);
		}
		if(StringUtil.isNotBlank(systemCode)){
			sql.append(" and t_s.code = ? ");
			valueParams.add(systemCode);
		}
		sql.append(" and t_r.id not in('"+ Constants.Resource.AUTHSYSTEM+"','"+Constants.Resource.TOP+"') ");
		sql.append(ORDER_BY_SHOWORDER);

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<ResourceInfo> resourceInfos = qr.query(sql.toString(), new BeanListHandler<ResourceInfo>(ResourceInfo.class),valueParams.toArray());
		return resourceInfos;

	}

	@Override
	public List<ResourceInfo> findResourceByUserIdAndParentId(String userId, String parentId) {
		StringBuilder sql = new StringBuilder();
		List<Object> valueParams = new ArrayList<Object>();
		String[] selectParamArray = RESOURCE_SELECT_PARAM.split(",");
		for (int i = 0; i < selectParamArray.length; i++) {
			selectParamArray[i] = "t_r." + selectParamArray[i];
		}
		String selectParam = ArrayUtil.toString(selectParamArray);

		sql.append(" select distinct " + selectParam + " from " + ROLE_USER_TABLE_NAME + " t_r_u ")
				.append(" inner join "+ROLE_RES_TABLE_NAME+" t_r_r on t_r_r.role_id = t_r_u.role_id ")
				.append(" inner join "+RESOURCE_TABLE_NAME+" t_r on t_r_r.resource_id = t_r.id ")
				.append(" where 1=1 ");
		if(StringUtil.isNotBlank(userId)){
			sql.append(" and t_r_u.user_id = ? ");
			valueParams.add(userId);
		}
		if(StringUtil.isNotBlank(parentId)){
			sql.append(" and t_r.parent_id = ? ");
			valueParams.add(parentId);
		}
		sql.append(" and t_r.id not in('"+ Constants.Resource.AUTHSYSTEM+"','"+Constants.Resource.TOP+"') ");
		sql.append(ORDER_BY_SHOWORDER);

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<ResourceInfo> resourceInfos = qr.query(sql.toString(), new BeanListHandler<ResourceInfo>(ResourceInfo.class),valueParams.toArray());
		return resourceInfos;
	}
}

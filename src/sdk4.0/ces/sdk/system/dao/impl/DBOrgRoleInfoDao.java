package ces.sdk.system.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;

import ces.sdk.system.bean.OrgRoleInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.OrgRoleInfoDao;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.JdbcUtil;

public class DBOrgRoleInfoDao extends DBBaseDao implements OrgRoleInfoDao{

	@Override
	public OrgRoleInfo save(OrgRoleInfo orgRoleInfo) {
		//建立一条标准的sql
		String sql = StandardSQLHelper.standardInsertSql(ORG_ROLE_COLUMNS_NAME, ORG_ROLE_TABLE_NAME, JdbcUtil.generatorPlaceHolder(orgRoleInfo.getClass().getDeclaredFields()).toString());
		
		orgRoleInfo.setId(super.generateUUID());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,JdbcUtil.getColumnsValueByBean(ORG_ROLE_COLUMNS_NAME, orgRoleInfo,ADD_STATUS));

		return orgRoleInfo;
	}

	@Override
	public void delete(String id) {
		Object[] idsArray = id.split(",");
		String sql = StandardSQLHelper.standardDeleteSql(ORG_ROLE_COLUMNS_NAME, "id in " + JdbcUtil.generatorPlaceHolder(idsArray));

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql,idsArray);
	}

	@Override
	public int delete(String orgId, String roleId, String systemId) {
		List<String> condition = new ArrayList<String>();
		if(StringUtils.isNotBlank(orgId)){
			condition.add("org_id = '"+orgId+"'");
		}
		if(StringUtils.isNotBlank(roleId)){
			condition.add("role_id = '"+roleId+"'");
		}
		if(StringUtils.isNotBlank(systemId)){
			condition.add("system_id = '"+systemId+"'");
		}
		
		String sql = StandardSQLHelper.standardDeleteSql(ORG_ROLE_TABLE_NAME, condition.toArray());

		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		return qr.update(sql);
	}

	@Override
	public int findTotal(String orgId, String roleId, String systemId) {
		
		List<String> condition = new ArrayList<String>();
		if(StringUtils.isNotBlank(orgId)){
			condition.add("org_id = '"+orgId+"'");
		}
		if(StringUtils.isNotBlank(roleId)){
			condition.add("role_id = '"+roleId+"'");
		}
		if(StringUtils.isNotBlank(systemId)){
			condition.add("system_id = '"+systemId+"'");
		}
		String sql = StandardSQLHelper.standardSelectTotalSql(ORG_ROLE_TABLE_NAME, condition.toArray());
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		BigDecimal result = qr.query(sql, new ScalarHandler<BigDecimal>());
		int totalNum = 0;
		if(result != null){
			totalNum = result.intValue();
		}
		return totalNum;
	}

	@Override
	public void save(String orgId, String systemId, String roleId) {
		String sql = StandardSQLHelper.standardInsertSql(ORG_ROLE_COLUMNS_NAME, ORG_ROLE_TABLE_NAME, JdbcUtil.generatorPlaceHolder(OrgRoleInfo.class.getDeclaredFields()).toString());
		String id = super.generateUUID();
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, id,orgId,roleId,systemId);
	}

	@Override
	public Set<String> findSystemIdsByOrgId(String orgId) {
		String sql = StandardSQLHelper.standardSelectSql("system_id", ORG_ROLE_TABLE_NAME, "", "org_id = ?");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<String> systemIds = qr.query(sql, new ColumnListHandler<String>(),orgId);
		
		HashSet<String> distinctSystemIds = new HashSet<String>(systemIds);
		return distinctSystemIds;
	}
	
	@Override
	public Set<String> findRoleIdsByOrgId(String orgId) {
		String sql = StandardSQLHelper.standardSelectSql("role_id", ORG_ROLE_TABLE_NAME, "", "org_id = ?");
		
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		List<String> roleIds = qr.query(sql, new ColumnListHandler<String>(),orgId);
		
		HashSet<String> distinctRoleIds = new HashSet<String>(roleIds);
		return distinctRoleIds;
	}
}

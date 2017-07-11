package ces.sdk.system.dao.impl;

import ces.sdk.system.bean.RoleResInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.RoleResInfoDao;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.JdbcUtil;

/**
 * Created by 黄翔宇 on 15/6/29.
 */
public class DBRoleResInfoDao extends DBBaseDao implements RoleResInfoDao {

	@Override
	public void delete(String id, RoleResIdType roleResIdType) {
		Object[] idsArray = id.split(",");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		String sql = null;
		switch (roleResIdType) {
			case ROLE:
				sql = StandardSQLHelper.standardDeleteSql(ROLE_RES_TABLE_NAME, "role_id in " + JdbcUtil.generatorPlaceHolder(idsArray));
				break;
			case RES:
				sql = StandardSQLHelper.standardDeleteSql(ROLE_RES_TABLE_NAME, "resource_id in " + JdbcUtil.generatorPlaceHolder(idsArray));
				break;
		}
		qr.update(sql,idsArray);
	}

	@Override
	public RoleResInfo save(RoleResInfo roleResInfo) {
		String sql = StandardSQLHelper.standardInsertSql(ROLE_RES_COLUMNS_NAME, ROLE_RES_TABLE_NAME, JdbcUtil.generatorPlaceHolder(roleResInfo.getClass().getDeclaredFields()).toString());
		roleResInfo.setId(generateUUID());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, JdbcUtil.getColumnsValueByBean(ROLE_RES_COLUMNS_NAME, roleResInfo, ADD_STATUS));
		return roleResInfo;
	}


}

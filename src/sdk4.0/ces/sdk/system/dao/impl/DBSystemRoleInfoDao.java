package ces.sdk.system.dao.impl;

import ces.sdk.system.bean.SystemRoleInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.SystemRoleInfoDao;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.JdbcUtil;

/**
 * Created by 黄翔宇 on 15/6/29.
 */
public class DBSystemRoleInfoDao extends DBBaseDao implements SystemRoleInfoDao {

	@Override
	public void delete(String id, SystemRoleIdType systemRoleIdType) {
		Object[] idsArray = id.split(",");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		String sql = null;
		switch (systemRoleIdType) {
			case SYSTEM :
				sql = StandardSQLHelper.standardDeleteSql(SYSTEM_ROLE_TABLE_NAME, "system_id in " + JdbcUtil.generatorPlaceHolder(idsArray));
				break;
			case ROLE:
				sql = StandardSQLHelper.standardDeleteSql(SYSTEM_ROLE_TABLE_NAME, "role_id in " + JdbcUtil.generatorPlaceHolder(idsArray));
				break;
		}
		qr.update(sql,idsArray);
	}

	@Override
	public SystemRoleInfo save(SystemRoleInfo systemRoleInfo) {
		String sql = StandardSQLHelper.standardInsertSql(SYSTEM_ROLE_COLUMNS_NAME, SYSTEM_ROLE_TABLE_NAME, JdbcUtil.generatorPlaceHolder(systemRoleInfo.getClass().getDeclaredFields()).toString());
		systemRoleInfo.setId(generateUUID());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, JdbcUtil.getColumnsValueByBean(SYSTEM_ROLE_COLUMNS_NAME, systemRoleInfo, ADD_STATUS));
		return systemRoleInfo;
	}
}

package ces.sdk.system.dao.impl;

import ces.sdk.system.bean.SystemResInfo;
import ces.sdk.system.common.StandardSQLHelper;
import ces.sdk.system.common.decorator.SdkQueryRunner;
import ces.sdk.system.dao.SystemResInfoDao;
import ces.sdk.system.factory.SdkQueryRunnerFactory;
import ces.sdk.util.JdbcUtil;

/**
 * Created by 黄翔宇 on 15/6/29.
 */
public class DBSystemResInfoDao extends DBBaseDao implements SystemResInfoDao {

	@Override
	public void delete(String id, SystemResIdType systemResIdType) {
		Object[] idsArray = id.split(",");
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		String sql = null;
		switch (systemResIdType) {
			case SYSTEM :
				sql = StandardSQLHelper.standardDeleteSql(SYSTEM_RES_TABLE_NAME, "system_id in " + JdbcUtil.generatorPlaceHolder(idsArray));
				break;
			case RES:
				sql = StandardSQLHelper.standardDeleteSql(SYSTEM_RES_TABLE_NAME, "resource_id in " + JdbcUtil.generatorPlaceHolder(idsArray));
				break;
		}
		qr.update(sql,idsArray);
	}

	@Override
	public SystemResInfo save(SystemResInfo systemResInfo) {
		String sql = StandardSQLHelper.standardInsertSql(SYSTEM_RES_COLUMNS_NAME, SYSTEM_RES_TABLE_NAME, JdbcUtil.generatorPlaceHolder(systemResInfo.getClass().getDeclaredFields()).toString());
		systemResInfo.setId(generateUUID());
		SdkQueryRunner qr = SdkQueryRunnerFactory.createSdkQueryRunner();
		qr.update(sql, JdbcUtil.getColumnsValueByBean(SYSTEM_RES_COLUMNS_NAME, systemResInfo, ADD_STATUS));
		return systemResInfo;
	}
}

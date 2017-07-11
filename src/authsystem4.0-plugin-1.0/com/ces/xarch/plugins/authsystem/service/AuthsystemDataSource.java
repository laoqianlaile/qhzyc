/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.plugins.authsystem.service</p>
 * <p>文件名:AuthsystemDataSource.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2013-07-23 17:27:20
 */
package com.ces.xarch.plugins.authsystem.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import ces.sdk.sdk.db.SdkDataSource;

/**
 * 系统管理平台数据源架构整合实现类.
 * <p>描述:sdk 数据源</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Reamy(杨木江 yangmujiang@sohu.com)
 * @date 2013-07-23  17:27:20
 * @version 1.0.2013.0723
 */
@Component("authsystemPluginFitDS")
public class AuthsystemDataSource implements SdkDataSource {
	@Resource(name="authsystemDataSource")
	private DataSource dataSource;
	
	/* (non-Javadoc)
	 * @see ces.sdk.sdk.db.SdkDataSource#getConnection()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-07-23 17:28:42
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}

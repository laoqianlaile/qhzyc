package ces.sdk.system.facade;

import java.sql.Connection;

import ces.sdk.system.bean.UserInfo;

/**
 * 操作上下文。<br>
 * @author Administrator
 *
 */
public class SystemFacadeContext {
	private Connection sqlConn;

	private UserInfo userInfo;

	/**
	 * 获取 链接
	 * @return
	 */
	public Connection getSqlConn() {
		return sqlConn;
	}

	/**
	 * 设置 链接
	 * @param sqlConn
	 */
	public void setSqlConn(Connection sqlConn) {
		this.sqlConn = sqlConn;
	}

	/**
	 * 获取 用户信息
	 * @return
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}

	/**
	 * 设置 用户
	 * @param userInfo
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}

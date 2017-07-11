package ces.sdk.util;

import java.sql.Connection;
import java.sql.SQLException;

import ces.sdk.sdk.db.SdkDataSource;
import ces.sdk.system.factory.SystemFacadeFactory;


/**
 * 有关数据库连接的工具类
 */
public class ConnectionUtil {
	/**
	 * 获取当前连接的是哪种数据库(mysql/sqlserver/oracle)
	 * 其他数据库需要扩展
	 * */
	private static String dataBaseType = null;
	public static String getDataBaseType(){
		if(null == dataBaseType){
			SdkDataSource sdkDataSource = SystemFacadeFactory.newInstance()
			.createSdkDataSource();
			Connection conn =  null;;
			try {
				conn = sdkDataSource.getConnection();
				String url = conn.getMetaData().getURL();
				dataBaseType = url.contains("oracle")?"oracle":
							   url.contains("mysql")?"mysql":
							   url.contains("sqlserver")?"mssqlserver":
							   url.contains("dm")?"dm":
							   "oracle";
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage()+"\r\n 在获取自定义sql配置文件过程中解析数据源连接时出错");
			} finally {
				if(null!=conn)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		}
		return dataBaseType;
	}
}
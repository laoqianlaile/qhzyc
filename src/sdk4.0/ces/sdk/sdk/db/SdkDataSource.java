package ces.sdk.sdk.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SdkDataSource 数据源接口。<br>
 * 获取数据库连接
 * @author 胡东平
 *
 */
public interface  SdkDataSource {
	/**
	 * 
	 * @return connection 数据库链接
	 * @throws SQLException
	 */
    public Connection getConnection() throws SQLException ;
}

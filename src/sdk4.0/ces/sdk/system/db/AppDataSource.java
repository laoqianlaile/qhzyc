package ces.sdk.system.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ces.sdk.sdk.db.SdkDataSource;
import ces.sdk.util.PropertyUtil;

/**
 * sdk 数据源
 * @author Administrator
 */
public class AppDataSource implements SdkDataSource {
	private Log log = LogFactory.getLog(this.getClass());
	private static Properties db_properties = PropertyUtil.getInstance().getDB_Properties();
	/**
	 * 获得数据库连接
	 */
	public Connection getConnection() {
		Connection conn = null;
		String driveClass = db_properties.getProperty("connection.driver_class");
		String url = db_properties.getProperty("connection.url");
		String userName = db_properties.getProperty("connection.username");
		String password = db_properties.getProperty("connection.password");
		try {
			Class.forName(driveClass).newInstance();
			conn= DriverManager.getConnection(url,userName,password); 
		} catch (InstantiationException e) {
			log.error(e.getMessage(),e);
			throw new RuntimeException(e.getMessage()+"\r\n 驱动注册出错");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			throw new RuntimeException(e.getMessage()+"/\r\n 数据源连接信息出错");
		} 
		return conn;
	}
}

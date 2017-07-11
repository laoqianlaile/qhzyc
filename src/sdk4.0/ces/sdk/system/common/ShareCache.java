package ces.sdk.system.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ces.sdk.util.JdbcUtil;

/**
 * 用于处理多个sdk, 却访问同一个数据库的缓存问题
 * 
 * <p>描述:</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015年7月29日 下午6:26:57
 * @version 1.0.2015.0601
 */
public class ShareCache {

	/** 每个系统下的sdk都有唯一的sdk标识 */
	public static String sdkUniqueCode;
	
	private static final String SHARE_CACHE_TABLE = "t_auth_share_cache";
	/**
	 * 是否需要清缓存
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月29日 下午6:26:44
	 * @return
	 */
	public static boolean isNeedClearCache(){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionManager.getConnection();
			String sql = "select is_need_clear_cache from "+SHARE_CACHE_TABLE+" where sdk_unique_code = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, sdkUniqueCode);
			ResultSet resultRet = ps.executeQuery();
			if(resultRet.next()){
				if(CommonConst.ShareCache.NEED_CLEAR_CACHE.equals(resultRet.getString(1))){
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			JdbcUtil.close(conn, ps, null);
		}
		return false;
	}
	
	public static void noticeOtherSdkNeedClearCache(){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionManager.getConnection();
			String sql = "update "+SHARE_CACHE_TABLE+" set is_need_clear_cache = '1' where sdk_unique_code != ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, sdkUniqueCode);
			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			JdbcUtil.close(conn, ps, null);
		}
	}
	
	public static void clearCacheFinished(){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionManager.getConnection();
			String sql = "update "+SHARE_CACHE_TABLE+" set is_need_clear_cache = '0' where sdk_unique_code = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, sdkUniqueCode);
			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			JdbcUtil.close(conn, ps, null);
		}
	}
	
	public static void initSdkUniqueCodeToDataBase(){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			conn = ConnectionManager.getConnection();
			String sql = "select count(*) from "+SHARE_CACHE_TABLE+" where sdk_unique_code = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, sdkUniqueCode);
			resultSet = ps.executeQuery();
			if(resultSet.next()){
				if(resultSet.getInt(1) == 0){
					String insert_sql = "insert into "+SHARE_CACHE_TABLE+"(sdk_unique_code,is_need_clear_cache) values(?,?)";
					ps = conn.prepareStatement(insert_sql);
					ps.setString(1, sdkUniqueCode);
					ps.setInt(2, 0);
					ps.execute();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			JdbcUtil.close(conn, ps, resultSet);
		}
	}
	
}

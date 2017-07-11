package ces.sdk.system.common.decorator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.sdk.system.common.CommonConst;
import ces.sdk.system.common.ConnectionManager;
import ces.sdk.system.common.FormatSqlHelper;
import ces.sdk.system.common.ShareCache;

/**
 * sdk的jdbc执行类, 在Apache common DbUtils的基础进行一层装饰封装. 并且SdkQueryRunner是单例的
 * 
 * <p>描述:在Apache common DbUtils的基础进行一层装饰封装. 用于公共的处理异常, 和关闭连接. 并简洁代码.</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015年6月25日 下午11:37:43
 * @version 1.0.2015.0601
 */
public class SdkQueryRunner {
	
	/** SdkQueryRunner */
	private static SdkQueryRunner instance = new SdkQueryRunner();  
	 /** 日志 */
	private final Log log = LogFactory.getLog(this.getClass());
	/** 缓存对象 */
	private static Map<String, Object> cache = null;
	/** 缓存访问数量 */
	private static AtomicLong visitedCount = new AtomicLong(0);
    /** 缓存的命中数量 */
	private static AtomicLong hitCount = new AtomicLong(0);
	
	
	private SdkQueryRunner(){};
	
	/**
	 * 获取SdkQueryRunner实例
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月7日 上午10:10:16
	 * @return
	 */
	public static SdkQueryRunner getInstance(){
		if(cache == null){
			initCache();
		}
		return instance;
	}
	
	/**
	 * 查询 (对应任何select语句)
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月25日 下午11:44:04
	 * @param sql
	 * @param rsh
	 * @return
	 */
	public <T> T query(String sql,ResultSetHandler<T> rsh){
		return this.<T>query(sql, rsh, (Object[]) null);
	}
	
	/**
	 * 查询(对应任何select语句)
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月25日 下午11:44:04
	 * @param sql
	 * @param rsh
	 * @return
	 */
	public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params){
		T t = null;
		try {
			Connection conn = ConnectionManager.getConnection();
			t = this.<T>query(conn, sql, rsh, params);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return t;
	}
	
	/**
	 * 查询(对应任何select语句)
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月25日 下午11:44:04
	 * @param sql
	 * @param rsh
	 * @return
	 */
	public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh){
		return this.<T>query(conn, sql, rsh, (Object[]) null);
	}
	
	/**
	 * 主入口, 查询
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月25日 下午11:44:04
	 * @param sql
	 * @param rsh
	 * @return
	 */
	public <T> T query(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params){
		String realSql = FormatSqlHelper.formatSql(sql, params);
		QueryRunner qr = new QueryRunner();
		T t = null;
		try {			
//			visitedCount.incrementAndGet();
//			if(ShareCache.isNeedClearCache()){
//				clearCache();
//				ShareCache.clearCacheFinished();
//			}
//			if (cache.containsKey(realSql)) {
//				log.debug("缓存命中, 命中率: " + (100.0 * getHitCount() / getVisitedCount()));
//				hitCount.incrementAndGet();
//				return (T) cache.get(realSql);
//			}
			log.debug(realSql);
			t = qr.query(conn, sql, rsh,params);
//			cache.put(realSql, t);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally{
			DbUtils.closeQuietly(conn);
		}
		return t;
	} 

	/**
	 * 增, 删, 改 (对应insert, update , delete语句)
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 上午12:29:34
	 * @param sql
	 * @return
	 */
	public int update(String sql){
		return this.update(sql, (Object[]) null);
	}
	
	/**
	 * 增, 删, 改 (对应insert, update , delete语句)
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 上午12:29:34
	 * @param sql
	 * @return
	 */
	public int update(String sql, Object... params){
		try {
			Connection conn = ConnectionManager.getConnection();
			return this.update(conn, sql, params);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 增, 删, 改 (对应insert, update , delete语句)
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 上午12:29:34
	 * @param sql
	 * @return
	 */
	public int update(Connection conn, String sql){
		return this.update(conn, sql, (Object[]) null);
	}
	
	/**
	 * 主入口, 增, 删, 改 (对应insert, update , delete语句)
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月26日 上午12:29:34
	 * @param sql
	 * @return
	 */
	public int update(Connection conn, String sql, Object... params){
		log.debug(FormatSqlHelper.formatSql(sql, params));
//		System.out.println(FormatSqlHelper.formatSql(sql, params)); //输出sql
		QueryRunner qr = new QueryRunner();
		try {
			ShareCache.noticeOtherSdkNeedClearCache();
			return qr.update(conn,sql,params);
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally{
			clearCache();
			DbUtils.closeQuietly(conn);
		}
	}
	
	
	/**
	 * 清除所有缓存
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月7日 上午12:28:35
	 */
    public void clearCache() {
        cache.clear();
    }
    
    /**
     * 获取访问量
     * <p>Company:上海中信信息发展股份有限公司</p>
     * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
     * @comments:<p> </p>
     * @date 2015年7月7日 上午12:28:53
     * @return
     */
   public long getVisitedCount() {
        return visitedCount.get();
    }

   /**
    * 获取命中量
    * <p>Company:上海中信信息发展股份有限公司</p>
    * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
    * @comments:<p> </p>
    * @date 2015年7月7日 上午12:29:05
    * @return
    */
    public long getHitCount() {
        return hitCount.get();
    }

    /**
     * 获取缓存大小
     * <p>Company:上海中信信息发展股份有限公司</p>
     * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
     * @comments:<p> </p>
     * @date 2015年7月7日 上午12:29:36
     * @return
     */
    public int getCacheSize() {
        return cache.size();
    }
    
    public Set<Entry<String,Object>> getCacheEntry() {
        return cache.entrySet();
    }
    
    /**
     * 初始化缓存
     * <p>Company:上海中信信息发展股份有限公司</p>
     * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
     * @comments:<p> </p>
     * @date 2015年7月7日 上午12:52:45
     */
    public static void initCache(){
    	cache = Collections.synchronizedMap(new LRUMap(1000000));
    }
}

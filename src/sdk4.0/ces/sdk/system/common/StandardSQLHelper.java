package ces.sdk.system.common;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 公用的拼接标准的sql处理类 (兼容多种数据库)
 * <p>描述:处理标准的sql</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015年6月22日 下午8:14:44
 * @version 1.0.2015.0601
 */
public class StandardSQLHelper {

	/**
	 * 标准的查询语句, 需要传递查询参数, 表名和查询条件
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月22日 下午8:15:40
	 * @param params 查询参数 (例: user_id, user_name as userName )
	 * @param table 表名 (例: t_user )
	 * @param condition 查询条件 (例: id = 1 )
	 * @return 拼接好的sql, 可直接在jdbc执行
	 */
	public static String standardSelectSql(String params,String table,String order,Object... condition){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(params)
		.append(" FROM ").append(table)
		.append(" WHERE 1=1 ");
		if(condition.length > 0 && !"".equals(condition[0])){
			for (int i = 0; i < condition.length; i++) {
				sql.append(" AND ").append(condition[i]);
			}
		}
		if(order != null && !"".equals(order)){
			sql.append(" ").append(order);
		}
		return sql.toString();
	}
	
	/**
	 * 标准的插入语句, 需要传递插入参数, 表名和插入参数值
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015-6-22 20:50:11
	 * @param params 插入参数 (例: user_id, user_name )
	 * @param table 表名 (例: t_user )
	 * @param values 查询条件 (例: (1,'张三')或(?,?) )
	 * @return 拼接好的sql, 可直接在jdbc执行
	 */
	public static String standardInsertSql(String params,String table,String values){
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(table).append("(").append(params).append(") values").append(values);
		return sql.toString();
	}
	
	/**
	 * 标准的更新语句, 需要传递更新参数, 表名和更新条件
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月22日 下午8:15:40
	 * @param params 更新参数 (例: user_id = ?, user_name = ? )
	 * @param table 表名 (例: t_user )
	 * @param condition 查询条件 (例: id = 1 )
	 * @return 拼接好的sql, 可直接在jdbc执行
	 */
	public static String standardUpdateSql(String params,String table,Object... condition){
		StringBuilder sql = new StringBuilder();
		 sql.append("UPDATE ").append(table)
		 .append(" SET ").append(params)
		 .append(" WHERE 1=1");
		if(condition.length > 0 && !"".equals(condition[0])){
			for (int i = 0; i < condition.length; i++) {
				sql.append(" AND ").append(condition[i]);
			}
		}
		return sql.toString();
	}
	
	
	/**
	 * 标准的删除语句, 需要 表名和删除条件
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月22日 下午8:15:40
	 * @param table 表名 (例: t_user )
	 * @param condition 查询条件 (例: id = 1 )
	 * @return 拼接好的sql, 可直接在jdbc执行
	 */
	public static String standardDeleteSql(String table,Object... condition){
		StringBuilder sql = new StringBuilder();
		 sql.append("DELETE FROM ").append(table)
		 .append(" WHERE 1=1");
		if(condition.length > 0 && !"".equals(condition[0])){
			for (int i = 0; i < condition.length; i++) {
				sql.append(" AND ").append(condition[i]);
			}
		}
		return sql.toString();
	}
	
	/**
	 * 标准的查询总数, 需要 表名和查询条件
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015-6-23 12:45:24
	 * @param table 表名 (例: t_user )
	 * @param condition 查询条件 (例: id = 1 )
	 * @return 拼接好的sql, 可直接在jdbc执行
	 */
	public static String standardSelectTotalSql(String table,Object... condition){
		StringBuilder sql = new StringBuilder();
		 sql.append(" SELECT COUNT(*) as totalNum ")
		 .append(" FROM ").append(table)
		 .append(" WHERE 1=1");
		if(condition.length > 0 && !"".equals(condition[0])){
			for (int i = 0; i < condition.length; i++) {
				sql.append(" AND ").append(condition[i]);
			}
		}
		return sql.toString();
	}
	
	/**
	 * 标准的查询最大值, 需要 查询字段名, 表名和查询条件
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015-6-23 12:45:24
	 * @param param 表名 (例: show_order )
	 * @param table 表名 (例: t_user )
	 * @param condition 查询条件 (例: id = 1 )
	 * @return 拼接好的sql, 可直接在jdbc执行
	 */
	public static String standardSelectMaxSql(String param, String table,Object... condition){
		StringBuilder sql = new StringBuilder();
		 sql.append(" SELECT MAX(").append(param).append(") as maxOrder ")
		 .append(" FROM ").append(table)
		 .append(" WHERE 1=1");
		if(condition.length > 0 && !"".equals(condition[0])){
			for (int i = 0; i < condition.length; i++) {
				sql.append(" AND ").append(condition[i]);
			}
		}
		return sql.toString();
	}
}

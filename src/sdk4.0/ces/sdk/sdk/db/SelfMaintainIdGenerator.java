package ces.sdk.sdk.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.sdk.util.JdbcUtil;
/**
 * 用数据库中指定表的信息维护应用中ID的生成
 * 为了支持JDBC，暂时不支持hibernate
 * @author 
 * @version 
 */
public class SelfMaintainIdGenerator {
	private String tableName = "t_id_sequence";
	private String nameFieldName = "id_name";
	private String valueFieldName = "maxVal";
	private String maxValue = "999999999999999";
	private String minValue="1";
	private String step = "1";
	private String defaultValue = "1";
	private int next;	
	private String sql;
	
	/** 操作日志ID. */
	public static final String OPERATELOG_ID = "operatelog_id";
	/** 系统日志ID. */
	public static final String SYSLOG_ID = "syslog_id";
	/** 组织ID*/
	public static final String ORGANIZE_ID = "organize_id";
	/** 资源ID*/
	public static final String RESOURCE_ID = "resource_id";
	/** 角色ID*/
	public static final String ROLE_ID = "role_id";
	/** 用户ID*/
	public static final String USER_ID = "user_id";
	/** 会话ID*/
	public static final String USERSESSION_ID = "usersession_id";
	/** ID*/
	public static final String ID = "id";
	
	/** 组织用户ID*/
	public static final String ORGUSER_ID = "orguser_id";
	private static final Log log= LogFactory.getLog(SelfMaintainIdGenerator.class);
	public int getNext(Connection conn,String recordName) throws SQLException{
		synchronized (recordName) {
			sql="select "+valueFieldName+ " from "+tableName+ " where " + nameFieldName + " =?";
			log.debug("fetching initial value: " + sql);
			//PreparedStatement st = conn.prepareStatement(sql);
			PreparedStatement upSt = conn.prepareStatement(sql);
			ResultSet rs = null;
			upSt.setString(1, recordName);
			boolean noValue=false;
			try{
				rs=upSt.executeQuery();
				if(rs.next())
				{
					next = rs.getInt(1) + Integer.parseInt(step);
					if(rs.wasNull())
					{
						next = Integer.parseInt(step);
					}
				} else
				{
					noValue=true;
					next = Integer.parseInt(defaultValue);
				}

				if (next > Long.parseLong(maxValue)){
					throw new SQLException("The generated Id "+next+" have beyond the maxValue "+maxValue);        		
				}
				if (next < Long.parseLong(minValue)){
					throw new SQLException("The generated Id "+next+" have beyond the minValue "+minValue);
				}

				if (noValue){
					StringBuffer updateSql=new StringBuffer();
					updateSql.append("insert into ");
					updateSql.append(tableName);
					updateSql.append("(");
					updateSql.append(nameFieldName);
					updateSql.append(",");
					updateSql.append(valueFieldName);
					updateSql.append(") values(?,?)");        		
					upSt=conn.prepareStatement(updateSql.toString());
					upSt.setString(1, recordName);
					upSt.setLong(2, next);
				}else{
					StringBuffer updateSql=new StringBuffer();
					updateSql.append("update ");
					updateSql.append(tableName);
					updateSql.append(" set ");
					updateSql.append(valueFieldName);
					updateSql.append("=? where ");
					updateSql.append(nameFieldName);
					updateSql.append("=?");
					upSt=conn.prepareStatement(updateSql.toString());
					upSt.setLong(1, next);
					upSt.setString(2, recordName);
				}
				upSt.execute();
				conn.commit();   	
				return next;
			}finally{
				JdbcUtil.closeResultSet(rs);
				JdbcUtil.closePreparedStatement(upSt);
			}
		}
	}
}
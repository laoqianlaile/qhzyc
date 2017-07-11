package com.ces.component.trace.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;


public class EjDatabaseUtil {
	 private static Logger log = Logger.getLogger(EjDatabaseUtil.class);

	    private static Properties props = new Properties();
	public static void getConnectionSqlServer(){
		String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL = "jdbc:sqlserver://218.57.143.185:8081;databasename=ReportServer";// 1433是端口，"USCSecondhandMarketDB"是数据库名称  
		String userName = "sa";// 用户名  
		String userPwd = "1qaz!QAZ";// 密码  
		Connection dbConn=null;
		try {

			Class.forName(driverName).newInstance();
		}catch(Exception ex){
			System.out.println("驱动加载失败");
			ex.printStackTrace();
		}
		try{
			dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
			System.out.println("成功连接数据库！");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(dbConn!=null)
					dbConn.close();
			}catch(SQLException  e){
				// TODO Auto-generated catch block  
				e.printStackTrace();
			}
		}
	}
	    /**
	     * 加载配置文件中的属性
	     * 
	     * @param profile 环境，值为development或production
	     */
	    public static void loadProperties(String profile) {
	        try {
	            props.load(new FileInputStream(ComponentFileUtil.getConfigPath() + "db/centerplat.properties"));
	        } catch (IOException e) {
	            log.error("加载数据库配置失败！", e);
	        }
	    }

	    public static String getValue(String key) {
	        return props.getProperty(key);
	    }

	    /**
	     * 获取数据库连接
	     * 
	     * @return Connection
	     */
	    public static Connection getConnection() {
	        Connection conn = null;
	        try {
	           // Class.forName(getValue("centerplat.jdbc.driver"));
	        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	          /* conn = DriverManager.getConnection(getValue("centerplat.jdbc.url"), getValue("centerplat.jdbc.username"), getValue("centerplat.jdbc.password"));*/

	        	conn = DriverManager.getConnection("jdbc:sqlserver://218.57.143.185:8081;DatabaseName=ReportServer;SelectMethod=Cursor", "sa", "1qaz!QAZ");
	        } catch (ClassNotFoundException e) {
	            log.error("加载数据库驱动失败！", e);
	            throw new RuntimeException("请检查数据库配置是否正确或数据库服务是否开启(ClassNotFoundException)！");
	        } catch (SQLException e) {
	            log.error("创建数据库连接失败！", e);
	            throw new RuntimeException("数据库连接失败，请检查数据库配置是否正确或数据库服务是否开启(SQLException)！");
	        }
	        return conn;
	    }

	    /**
	     * 获取数据库类型
	     * 
	     * @return String
	     */
	    public static String getDatabaseType() {
	        String driver = getValue("centerplat.jdbc.driver");
	        String type = "";
	        if (driver.indexOf("oracle") != -1) {
	            type = DatabaseUtil.DB_TYPE_ORACLE;
	        } else if (driver.indexOf("sqlserver") != -1) {
	            type = DatabaseUtil.DB_TYPE_SQLSERVER;
	        } else if (driver.indexOf("DmDriver") != -1) {
	            type = DatabaseUtil.DB_TYPE_DAMENG;
	        }else if (driver.indexOf("mysql") != -1) {
	            type = DatabaseUtil.DB_TYPE_MYSQL;
	        }
	        return type;
	    }

	    /**
	     * 判断表是否存在
	     * 
	     * @param tableName 表名
	     */
	    @SuppressWarnings("unchecked")
	    public static boolean tableExists(String tableName) {
	        String sql = null;
	        if (DatabaseUtil.DB_TYPE_ORACLE.equals(getDatabaseType())) {
	            sql = "SELECT COUNT(*) FROM USER_TABLES T WHERE T.TABLE_NAME='" + tableName.toUpperCase() + "'";
	        } else if (DatabaseUtil.DB_TYPE_SQLSERVER.equals(getDatabaseType())) {
	            sql = "SELECT COUNT(*) FROM SYSOBJECTS T WHERE T.NAME='" + tableName.toUpperCase() + "'";
	        }
	        List<Object[]> rlt = queryForListObj(sql);
	        if (null != rlt && !rlt.isEmpty()) {
	            Object[] objs = rlt.get(0);
	            if (Integer.parseInt(StringUtil.null2zero(objs[0])) > 0)
	                return true;
	        }
	        return false;
	    }

	    /**
	     * 判断表中字段是否存在
	     * 
	     * @param tableName 表名
	     * @param columnName 字段名
	     */
	    @SuppressWarnings("rawtypes")
	    public static boolean columnExists(String tableName, String columnName) {
	        String sql = null;
	        if (DatabaseUtil.DB_TYPE_ORACLE.equals(getDatabaseType())) {
	            sql = "SELECT COUNT(*) FROM USER_TAB_COLUMNS T WHERE T.TABLE_NAME='" + tableName.toUpperCase() + "' AND T.COLUMN_NAME='" + columnName.toUpperCase()
	                    + "' ";
	        } else if (DatabaseUtil.DB_TYPE_SQLSERVER.equals(getDatabaseType())) {
	            sql = "SELECT COUNT(*) FROM SYSCOLUMNS T WHERE T.ID=OBJECT_ID('" + tableName.toUpperCase() + "') AND T.NAME='" + columnName.toUpperCase() + "' ";
	        } else if (DatabaseUtil.DB_TYPE_MYSQL.equals(getDatabaseType())) {
	        	sql = "SELECT COUNT(*) FROM information_schema.COLUMNS T WHERE T.TABLE_NAME='" + tableName.toUpperCase() + "' AND T.COLUMN_NAME='" + columnName.toUpperCase() + "' ";
	        	
	        }
	        List rlt = queryForList(sql);
	        if (null != rlt && !rlt.isEmpty()) {
	            Integer count = Integer.valueOf(rlt.get(0).toString());
	            if (count.intValue() > 0)
	                return true;
	        }
	        return false;
	    }

	    /**
	     * 查询
	     * 
	     * @param sql
	     * @throws SQLException
	     */
	    @SuppressWarnings({"rawtypes", "unchecked"})
	    public static List queryForListObj(String sql) {
	        List list = new ArrayList();
	        Connection conn = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try {
	            ps = getConnection().prepareStatement(sql);
	            rs = ps.executeQuery();
	            if (rs != null) {
	                // 得到结果集(rs)的结构信息，比如字段数、字段名等
	                ResultSetMetaData md = rs.getMetaData();
	                // 返回此 ResultSet 对象中的列数
	                int columnCount = md.getColumnCount();
	                if (columnCount == 1) {
	                    while (rs.next()) {
	                        list.add(rs.getObject(1));
	                    }
	                } else {
	                    Object[] objs = null;
	                    while (rs.next()) {
	                        objs = new Object[columnCount];
	                        for (int i = 1; i <= columnCount; i++) {
	                            objs[i - 1] = rs.getObject(i);
	                        }
	                        list.add(objs);
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            if (rs != null) {
	                try {
	                    rs.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (ps != null) {
	                try {
	                    ps.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return list;
	    }
	    /**
	     * 查询
	     *
	     * @param sql
	     * @throws SQLException
	     */
	    @SuppressWarnings({"rawtypes", "unchecked"})
	    public static List<Map<String,Object>> queryForList(String sql) {
	        List list = new ArrayList();
	        Connection conn = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try {
	            ps = getConnection().prepareStatement(sql);
	            rs = ps.executeQuery();
	            if (rs != null) {

	                // 得到结果集(rs)的结构信息，比如字段数、字段名等
	                ResultSetMetaData md = rs.getMetaData();
	                // 返回此 ResultSet 对象中的列数
	                int columnCount = md.getColumnCount();
	                //获取所有列名
	                String[] columnNames=new String[columnCount];
	                for(int i=0;i<columnCount;i++) {
	                    columnNames[i]=md.getColumnName(i+1);
	                }
	                while (rs.next()) {
	                    Map<String,Object> dataMap = new HashMap<String, Object>();
	                    for (int i = 1; i <= columnCount; i++) {
	                        dataMap.put(columnNames[i-1],rs.getObject(i));
	                    }
	                    list.add(dataMap);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            if (rs != null) {
	                try {
	                    rs.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (ps != null) {
	                try {
	                    ps.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return list;
	    }
	    public static Map<String,Object> queryForMap(String sql) {
	        Connection conn = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try {
	            ps = getConnection().prepareStatement(sql);
	            rs = ps.executeQuery();
	            if (rs != null) {

	                // 得到结果集(rs)的结构信息，比如字段数、字段名等
	                ResultSetMetaData md = rs.getMetaData();
	                // 返回此 ResultSet 对象中的列数
	                int columnCount = md.getColumnCount();
	                //获取所有列名
	                String[] columnNames=new String[columnCount];
	                for(int i=0;i<columnCount;i++) {
	                    columnNames[i]=md.getColumnName(i+1);
	                }
	                while (rs.next()) {
	                    Map<String,Object> dataMap = new HashMap<String, Object>();
	                    for (int i = 1; i <= columnCount; i++) {
	                        dataMap.put(columnNames[i-1],rs.getObject(i));
	                    }
	                    return dataMap;
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            if (rs != null) {
	                try {
	                    rs.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (ps != null) {
	                try {
	                    ps.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return null;
	    }
	    public static int executeSql(String sql) {
	        int iResult = 0;
	        Connection conn = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try {
	            ps = getConnection().prepareStatement(sql);
	            iResult = ps.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            if (rs != null) {
	                try {
	                    rs.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (ps != null) {
	                try {
	                    ps.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return iResult;
	    }
}

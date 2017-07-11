package com.ces.config.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.dhtmlx.entity.database.Database;

public class DatabaseUtil {

    /**
     * ORACLE 数据库类型
     */
    public static final String DB_TYPE_ORACLE = "0";

    /**
     * SQL SERVER 数据库类型
     */
    public static final String DB_TYPE_SQLSERVER = "1";

    /**
     * 国产达梦 数据库类型
     */
    public static final String DB_TYPE_DAMENG = "2";

    /**
     * ORACLE 数据库连接驱动
     */
    public static final String DRIVER_NAME_ORACLE = "oracle.jdbc.driver.OracleDriver";

    /**
     * SQL SERVER 数据库连接驱动
     */
    public static final String DRIVER_NAME_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    /**
     * 国产达梦 数据库连接驱动
     */
    public static final String DRIVER_NAME_DAMENG = "dm.jdbc.driver.DmDriver";

    /**
     * 缩进
     */
    public static final String INDENT = "    ";

    /**
     * 根据数据库信息获取数据库连接
     * 
     * @param database
     * @return Connection
     */
    public static Connection getConnectionByDatabase(Database database) {
        Connection conn = null;
        try {
            initDatabaseDriver(database);
            conn = DriverManager
                    .getConnection(getDatabaseUrl(database), database.getUserName(), database.getPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("自定义数据库（" + database.getName()
                    + "）连接失败，请检查数据库配置是否正确或数据库服务是否开启(ClassNotFoundException)！");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("自定义数据库（" + database.getName() + "）连接失败，请检查数据库配置是否正确或数据库服务是否开启(SQLException)！");
        }
        return conn;
    }

    /**
     * 组装数据连接的URL
     * 
     * @param database 数据库
     * @return String
     */
    public static String getDatabaseUrl(Database database) {
        String url = "";
        String type = database.getType();
        if (DatabaseUtil.DB_TYPE_SQLSERVER.equals(type)) {
            url = "jdbc:sqlserver://" + database.getIp() + ":" + database.getPort() + ";DatabaseName="
                    + database.getInstanceName();
        } else if (DatabaseUtil.DB_TYPE_ORACLE.equals(type)) {
            url = "jdbc:oracle:thin:@" + database.getIp() + ":" + database.getPort() + ":" + database.getInstanceName();
        } else if (DatabaseUtil.DB_TYPE_DAMENG.equals(type)) {
            url = "jdbc:dm://" + database.getIp() + ":" + database.getPort();
        }
        return url;
    }

    /**
     * 初始化数据库驱动
     * 
     * @param database 数据库
     * @throws ClassNotFoundException
     */
    private static void initDatabaseDriver(Database database) throws ClassNotFoundException {
        if (DatabaseUtil.DB_TYPE_SQLSERVER.indexOf(database.getType()) > -1) {
            Class.forName(DatabaseUtil.DRIVER_NAME_SQLSERVER);
        } else if (DatabaseUtil.DB_TYPE_ORACLE.indexOf(database.getType()) > -1) {
            Class.forName(DatabaseUtil.DRIVER_NAME_ORACLE);
        } else if (DatabaseUtil.DB_TYPE_DAMENG.indexOf(database.getType()) > -1) {
            Class.forName(DatabaseUtil.DRIVER_NAME_DAMENG);
        }
    }

    /**
     * 获取数据库驱动
     * 
     * @param type 数据库类型
     * @return String
     * @throws ClassNotFoundException
     */
    public static String getDatabaseDriver(String type) throws ClassNotFoundException {
        String databaseDriver = "";
        if (DatabaseUtil.DB_TYPE_SQLSERVER.equals(type)) {
            databaseDriver = DatabaseUtil.DRIVER_NAME_SQLSERVER;
        } else if (DatabaseUtil.DB_TYPE_ORACLE.equals(type)) {
            databaseDriver = DatabaseUtil.DRIVER_NAME_ORACLE;
        } else if (DatabaseUtil.DB_TYPE_DAMENG.equals(type)) {
            databaseDriver = DatabaseUtil.DRIVER_NAME_DAMENG;
        }
        return databaseDriver;
    }

    /**
     * 关闭数据库连接
     * 
     * @param conn Connection
     * @param stmt Statement
     * @param rset ResultSet
     */
    public static void close(Connection conn, Statement stmt, ResultSet rset) {
        try {
            if (null != conn && !conn.isClosed()) {
                conn.close();
            }
            if (null != stmt) {
                stmt.close();
            }
            if (null != rset) {
                rset.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("关闭数据库连接失败！");
        }
    }

    /**
     * 根据自定义数据库信息执行SQL语句
     * 
     * @param database 数据库
     * @param sql sql
     */
    public static boolean execute(Database database, String sql) {
        boolean isExecute = false;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnectionByDatabase(database);
            stmt = conn.createStatement();
            isExecute = stmt.execute(sql);
        } catch (SQLException e) {
            // logger.error("DatabaseUtil.execute method occur SQLException: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("自定义数据库执行脚本失败(SQLException)！");
        } finally {
            close(conn, stmt, null);
        }

        return isExecute;
    }

    /**
     * 根据自定义数据库信息执行SQL语句
     * 
     * @param database 数据库
     * @param sqlList sql列表
     */
    public static void execute(Database database, List<String> sqlList) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnectionByDatabase(database);
            stmt = conn.createStatement();
            for (String sql : sqlList) {
                System.out.println(sql);
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            // logger.error("DatabaseUtil.execute method occur SQLException: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("自定义数据库执行脚本失败(SQLException)！");
        } finally {
            close(conn, stmt, null);
        }
    }

    /**
     * 根据自定义数据库信息执行SQL语句
     * 
     * @param database 数据库
     * @param sql sql
     */
    public static int queryForInt(Database database, String sql) {
        int count = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        try {
            conn = getConnectionByDatabase(database);
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            if (rset.next()) {
                count = rset.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("自定义数据库统计出错(SQLException)！SQL语句[" + sql + "]");
        } finally {
            close(conn, stmt, rset);
        }

        return count;
    }

    /**
     * 根据自定义数据库查询执行SQL语句
     * 
     * @param database 数据库
     * @param sql sql
     * @return List<Map<String, String>>
     */
    public static List<Map<String, Object>> queryForList(Database database, String sql) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        try {
            conn = getConnectionByDatabase(database);
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            while (rset.next()) {
                Map<String, Object> columnMap = new HashMap<String, Object>();
                ResultSetMetaData metaData = rset.getMetaData();
                int columnNum = metaData.getColumnCount() + 1;
                for (int i = 1; i < columnNum; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = rset.getString(columnName);
                    columnMap.put(columnName, columnValue);
                }
                list.add(columnMap);
            }
        } catch (SQLException e) {
            throw new RuntimeException("自定义数据库查询出错(SQLException)！SQL语句[" + sql + "]", e);
        } finally {
            close(conn, stmt, rset);
        }

        return list;
    }

    /**
     * 测试数据库连接
     * 
     * @param database 数据库
     * @return boolean
     */
    public static boolean connDatabase(Database database) {
        boolean connSuccess = false;
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnectionByDatabase(database);
            if (conn != null) {
                connSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.close(conn, null, null);
        }
        return connSuccess;
    }

    /**
     * 根据数据库信息，判断表结构在数据库中是否存在
     * 
     * @param database 数据库信息
     * @param tableName 表名
     * @return boolean
     */
    public static boolean isTableExist(Database database, String tableName) {
        boolean tableExist = false;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        try {
            conn = DatabaseUtil.getConnectionByDatabase(database);
            stmt = conn.createStatement();
            String query = "select 1 from " + tableName + " where 1 <> 1";
            rset = stmt.executeQuery(query);
            tableExist = true;
        } catch (Exception e) {
        } finally {
            DatabaseUtil.close(conn, stmt, rset);
        }
        return tableExist;
    }

}
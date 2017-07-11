package com.ces.config.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuthDatabaseUtil {

    private static Log log = LogFactory.getLog(AuthDatabaseUtil.class);

    private static Properties props = new Properties();

    /**
     * 加载配置文件中的属性
     * 
     * @param profile 环境，值为development或production
     */
    public static void loadProperties(String profile) {
        try {
            props.load(new FileInputStream(ComponentFileUtil.getConfigPath() + "db/db.properties"));
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
            Class.forName(getValue("authsystem.jdbc.driver"));
            conn = DriverManager.getConnection(getValue("authsystem.jdbc.url"), getValue("authsystem.jdbc.username"), getValue("authsystem.jdbc.password"));
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
        String driver = getValue("authsystem.jdbc.driver");
        String type = "";
        if (driver.indexOf("oracle") != -1) {
            type = DatabaseUtil.DB_TYPE_ORACLE;
        } else if (driver.indexOf("sqlserver") != -1) {
            type = DatabaseUtil.DB_TYPE_SQLSERVER;
        } else if (driver.indexOf("DmDriver") != -1) {
            type = DatabaseUtil.DB_TYPE_ORACLE;
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
        List<Object[]> rlt = queryForList(sql);
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
        }
        List rlt = queryForList(sql);
        if (null != rlt && !rlt.isEmpty()) {
            BigDecimal count = (BigDecimal) rlt.get(0);
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
    public static List queryForList(String sql) {
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
}

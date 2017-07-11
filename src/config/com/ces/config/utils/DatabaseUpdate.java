package com.ces.config.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DatabaseUpdate {

    private static Log log = LogFactory.getLog(DatabaseUpdate.class);

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
            Class.forName(getValue("jdbc.driver"));
            conn = DriverManager.getConnection(getValue("jdbc.url"), getValue("jdbc.username"), getValue("jdbc.password"));
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
        String driver = getValue("jdbc.driver");
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
     * 更新数据库
     */
    public static void updateDataBase() {
        String path = ComponentFileUtil.getProjectPath() + "docs/";
        Connection conn = null;
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        try {
            System.out.println("正在更新数据库...");
            conn = getConnection();
            if (DatabaseUtil.DB_TYPE_ORACLE.equals(getDatabaseType())) {
                String oracleUpdateFile = path + "oracle/update_table_data.sql";
                String oracleUpdateSql = loadSql(oracleUpdateFile).replaceAll("\\r\\n", " ").trim();
                if (!"".equals(oracleUpdateSql)) {
                    prepareStatement = conn.prepareStatement(oracleUpdateSql);
                    prepareStatement.execute();
                }
            } else if (DatabaseUtil.DB_TYPE_SQLSERVER.equals(getDatabaseType())) {
                String sqlServerUpdateTableFile = path + "sqlserver/update_table.sql";
                String sqlServerUpdateDataFile = path + "sqlserver/update_data.sql";
                String sqlServerUpdateTableSql = loadSql(sqlServerUpdateTableFile).replaceAll("\\r\\n", " ").trim();
                if (!"".equals(sqlServerUpdateTableSql)) {
                    prepareStatement = conn.prepareStatement(sqlServerUpdateTableSql);
                    prepareStatement.execute();
                }
                String sqlServerUpdateDataSql = loadSql(sqlServerUpdateDataFile).replaceAll("\\r\\n", " ").trim();
                if (!"".equals(sqlServerUpdateDataSql)) {
                    prepareStatement = conn.prepareStatement(sqlServerUpdateDataSql);
                    prepareStatement.execute();
                }
            }
            System.out.println("更新数据库完毕！");
        } catch (SQLException e) {
            log.error("更新数据库补丁失败！", e);
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.error("关闭resultSet失败！", e);
                }
            }
            if (null != prepareStatement) {
                try {
                    prepareStatement.close();
                } catch (SQLException e) {
                    log.error("关闭prepareStatement失败！", e);
                }
            }
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("关闭conn失败！", e);
                }
            }
        }
    }

    /**
     * 读取oracle和sqlserver脚本
     * 
     * @return String
     */
    public static String loadSql(String sqlFile) {
        InputStream sqlFileIn = null;
        try {
            File file = new File(sqlFile);
            if (!file.exists()) {
                System.out.println(sqlFile + " 文件不存在！");
                return "";
            }
            sqlFileIn = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(sqlFileIn, "UTF-8"));
            StringBuffer sqlSb = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("--")) {
                    continue;
                }
                sqlSb.append(line);
            }
            return sqlSb.toString();
        } catch (Exception e) {
            log.error("加载数据库补丁脚本失败！", e);
            throw new RuntimeException(e);
        } finally {
            if (sqlFileIn != null) {
                try {
                    sqlFileIn.close();
                } catch (IOException e) {
                    log.error("加载数据库补丁脚本失败！", e);
                }
            }
        }
    }
}

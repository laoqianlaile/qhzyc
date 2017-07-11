package com.ces.config.dhtmlx.dao.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.jdbc.core.ControlResultSetHandler;
import com.ces.config.jdbc.core.ResultSetHandler;
import com.ces.config.jdbc.core.RowArrayHandler;
import com.ces.config.jdbc.core.RowMapHandler;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CfgCommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.web.listener.XarchListener;
/**
 * <p>描述: 用JPA实现类操作数据库</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs
 * @date 2013-6-27 下午8:38:15
 *
 */
@Component
public class DatabaseHandlerDao {

    private static Log log = LogFactory.getLog(DatabaseHandlerDao.class);

    /** oracle数据库 */
    public final static String DB_ORACLE = "oracle";
    /** sqlserver数据库 */
    public final static String DB_SQLSERVER = "sqlserver";
    /** mysql数据库 */
    public final static String DB_MYSQL = "mysql";
    /** 国产达梦数据库 */
    public final static String DB_DAMING = "dm";
    /** 国产瀚高数据库 */
    public final static String DB_HIGHGO = "highgo";
    /** 数据库连接地址*/
    private static String connectionUrl = null;

    private static String DB_TYPE = null;

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public static DatabaseHandlerDao getInstance() {
        return XarchListener.getBean(DatabaseHandlerDao.class);
        //return (DatabaseHandlerDao)SpringContext.getBean("databaseHandlerDao");
    }
    /**
     * qiucs 2013-8-6
     * <p>描述: 删除表结构</p>
     * @param  tableName    设定参数
     */
    public void dropTable(String tableName) {
        String sql = "drop table " + tableName;
        getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    /**
     * qiucs 2013-10-9
     * <p>描述: 创建一张指定表名的表（默认只有一个ID主键字段，且数据类型为字符，长度为32）</p>
     * @param  tableName    设定参数
     */
    public void createTableWithStringId(String tableName) {
        String sql = "create table " + tableName + "(" + AppDefineUtil.C_ID + " "
                + getDataType(ConstantVar.DataType.CHAR, 32)
                + " PRIMARY KEY)";
        getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    /**
     * <p>描述: 创建一张指定表名的备份表（默认有一个ID主键字段，F_ID,P_ID外键关联字段， 且数据类型为字符，长度为32）</p>
     * @param  tableName    设定参数
     */
    public void createBakTable(String tableName) {
        String sql = "create table " + tableName + "(" + AppDefineUtil.C_ID + " "
                + getDataType(ConstantVar.DataType.CHAR, 32)
                + " PRIMARY KEY, F_ID " + getDataType(ConstantVar.DataType.CHAR, 32)
                + ", P_ID " + getDataType(ConstantVar.DataType.CHAR, 32)
                + ", BAK_NEW_VALUE " + getDataType(ConstantVar.DataType.CHAR, 32)
                + ", BAK_REASON " + getDataType(ConstantVar.DataType.CHAR, 1000) + ")";
        getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    /**
     * qiucs 2014-12-26 下午12:35:47
     * <p>描述: 创建视图  </p>
     * @return void
     */
    public void createView(String viewName, String sql) {
        if (isSqlserver()) {
            createSqlserverView(viewName, sql);
        } else if (isOracle()) {
            createOracleView(viewName, sql);
        }
    }

    /**
     * qiucs 2014-12-26 下午12:35:20
     * <p>描述: 删除视图 </p>
     * @return void
     */
    public void dropView(String viewName) {
        boolean exists = viewExists(viewName);
        if (exists) {
            String sql = "drop view " + viewName;
            executeSql(sql);
        }
    }



    /**
     * qiucs 2014-12-26 下午12:33:25
     * <p>描述: 删除ORACLE视图 </p>
     * @return void
     */
    private void createOracleView(String viewName, String sql) {
        StringBuilder sb = new  StringBuilder();
        sb.append("create or replace view ");
        sb.append(viewName).append(" as ").append(sql);
        executeSql(String.valueOf(sb));
    }

    /**
     * qiucs 2014-12-26 下午12:30:05
     * <p>描述: 删除SQLSERVER视图 </p>
     * @return Object
     */
    private void createSqlserverView(String viewName, String sql) {
        boolean exists = viewExists(viewName);
        StringBuilder sb = new  StringBuilder();
        if (exists) sb.append("alert view ");
        else  sb.append("create view ");
        sb.append(viewName).append(" as ").append(sql);
        executeSql(String.valueOf(sb));
    }

    /**
     * qiucs 2015-3-4 下午10:59:43
     * <p>描述: 向指定表增加一个字段 </p>
     * @return void
     */
    public void addColumn(String tableName, String columnName, String type, Integer length, String defaultValue) {
        addColumn(tableName, columnName, type, length, null, defaultValue);
    }

    /**
     * qiucs 2013-8-6
     * <p>描述: 向指定表增加一个字段</p>
     * @param  tableName
     * @param  columnName
     * @param  type
     * @param  length
     * @param  precistion   --精度
     * @param  defaultValue
     *
     */
    public void addColumn(String tableName, String columnName, String type, Integer length, Integer precistion, String defaultValue) {
        String sql = null;
        //if (DB_ORACLE.equals(getDbType()) || DB_SQLSERVER.equals(getDbType())) {
        sql = "alter table " + tableName + " add " + columnName + " "
                + getDataType(type, length, precistion);
        if (StringUtil.isNotEmpty(defaultValue)) {
            if (ConstantVar.DataType.NUMBER.equals(type)) {
                sql += " default(" + StringUtil.null2zero(defaultValue) + ")";
            } else {
                sql += " default('" + defaultValue + "')";
            }
        }
        /*} else if (DB_MYSQL.equals(getDbType())) {

        } else if (DB_DAMING.equals(getDbType())) {

        }*/
        getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    /**
     * qiucs 2013-8-6
     * <p>描述: 字段重命名</p>
     * @param  tableName
     * @param  oldColumnName
     * @param  newColumnName
     */
    public void renameColumn(String tableName, String oldColumnName, String newColumnName) {
        String sql = null;
        if (DB_ORACLE.equals(getDbType())) {
            sql = "alter table " + tableName + " rename column " + oldColumnName + " to " + newColumnName;
        } else if (DB_SQLSERVER.equals(getDbType())) {
            sql = "exec sp_rename '" + tableName + ".[" + oldColumnName + "]', '" + newColumnName + "', 'column'";
        } else if (DB_MYSQL.equals(getDbType())) {

        } else if (DB_DAMING.equals(getDbType())) {

        }
        getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    /**
     * qiucs 2013-8-6
     * <p>描述: 删除物理表中的字段</p>
     * @param  tableName
     * @param  columnName    设定参数
     */
    public void dropColumn(String tableName, String columnName) {
        String sql = null;
        //if (DB_ORACLE.equals(getDbType()) || DB_SQLSERVER.equals(getDbType())) {
        sql = "alter table " + tableName + " drop column " + columnName ;
        /*} else if (DB_MYSQL.equals(getDbType())) {

        } else if (DB_DAMING.equals(getDbType())) {

        }*/
        getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    /**
     * qiucs 2015-3-4 下午11:01:23
     * <p>描述: 修改字段类型 </p>
     * @return void
     */
    public void alterColumn(String tableName, String columnName, String type, Integer length, String defaultValue) {
        alterColumn(tableName, columnName, type, length, null, defaultValue);
    }

    /**
     * qiucs 2013-8-6
     * <p>描述: 修改字段类型</p>
     * @param  tableName
     * @param  columnName
     * @param  type
     * @param  length
     */
    public void alterColumn(String tableName, String columnName, String type, Integer length, Integer precision, String defaultValue) {
        String sql = null;
        if (DB_ORACLE.equals(getDbType())) {
            sql = "alter table " + tableName + " modify " + columnName + " " + getDataType(type, length, precision);
            if (StringUtil.isNotEmpty(defaultValue)) {
                if (ConstantVar.DataType.NUMBER.equals(type)) {
                    sql += " default(" + StringUtil.null2zero(defaultValue) + ")";
                } else {
                    sql += " default('" + defaultValue + "')";
                }
            }
        } else if (DB_SQLSERVER.equals(getDbType())) {
            sql = "alter table " + tableName + " alter column " + columnName + " " + getDataType(type, length);
            if (StringUtil.isNotEmpty(defaultValue)) {
                if (ConstantVar.DataType.NUMBER.equals(type)) {
                    sql += " default(" + StringUtil.null2zero(defaultValue) + ")";
                } else {
                    sql += " default('" + defaultValue + "')";
                }
            }
        } else if (DB_HIGHGO.equals(getDbType())) {
            sql = "alter table " + tableName + " alter column " + columnName + " type " + getDataType(type, length);
            if (StringUtil.isNotEmpty(defaultValue)) {
                getEntityManager().createNativeQuery("alter table " + tableName + " alter column " + columnName + " set default '" + defaultValue + "'").executeUpdate();
            } else {
                getEntityManager().createNativeQuery("alter table " + tableName + " alter column " + columnName + " drop default").executeUpdate();
            }
        } else if (DB_MYSQL.equals(getDbType())) {

        } else if (DB_DAMING.equals(getDbType())) {

        }
        getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    /**
     * qiucs 2015-7-15 上午10:41:02
     * <p>描述: 指定表中是否存在数据 </p>
     * @return boolean
     *         --false为存在数据，true为不存在数据
     */
    public boolean isEmptyData(String tableName) {
        StringBuilder sb = new StringBuilder(50);

        sb.append("SELECT COUNT(*) AS NUM_ FROM ").append(tableName);
        Object obj = queryForObject(sb.toString());

        return "0".equals(obj.toString());

    }

    /**
     * qiucs 2015-7-15 上午10:41:02
     * <p>描述: 指定表中指定字段是否存在数据 </p>
     * @return boolean
     *         --false为存在数据，true为不存在数据
     */
    public boolean isEmptyData(String tableName, String columnName, String dataType) {
        StringBuilder sb = new StringBuilder(50);

        if ("n".equals(dataType)) {
            sb.append("select count(*) as num_ from ").append(tableName).append(" t where ")
                    .append("t.").append(columnName).append(" is not null");
        } else {
            sb.append("select count(*) as num_ from ").append(tableName).append(" t where ")
                    .append("t.").append(columnName).append(" is not null or t.").append(columnName).append(" != ''");
        }

        Object obj = queryForObject(sb.toString());

        return "0".equals(obj.toString());

    }

    /**
     * qiucs 2013-11-28
     * <p>描述: 判断表是否存在</p>
     * @param  tableName
     * @param  columnName
     */
    @SuppressWarnings("unchecked")
    public boolean tableExists(String tableName) {
        String sql = null;
        if (DB_ORACLE.equals(getDbType())) {
            sql = "select count(*) from user_tables t where t.table_name='" + tableName.toUpperCase() + "'";
        } else if (DB_SQLSERVER.equals(getDbType())) {
            sql = "select count(*) from sysobjects t where t.name='" + tableName.toUpperCase() + "'";
        } else if (DB_HIGHGO.equals(getDbType())) {
            sql = "select count(*) from pg_tables t where t.tablename='" + tableName.toLowerCase() + "'";
        } else if (DB_MYSQL.equals(getDbType())) {

        } else if (DB_DAMING.equals(getDbType())) {

        }
        List<Long> rlt = queryForList(sql);
        if (null != rlt && !rlt.isEmpty()) {
            if (Integer.parseInt(StringUtil.null2zero(rlt.get(0))) > 0) return true;
        }
        return false;
    }

    /**
     * qiucs 2014-12-26 下午12:00:21
     * <p>描述: 判断视图是否存在 </p>
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public boolean viewExists(String viewName) {
        String sql = null;
        if (DB_ORACLE.equals(getDbType())) {
            sql = "select count(*) from user_views t where t.view_name='" + viewName.toUpperCase() + "'";
        } else if (DB_SQLSERVER.equals(getDbType())) {
            sql = "select count(*) from sysobjects t where t.name='" + viewName.toUpperCase() + "'";
        } else if (DB_HIGHGO.equals(getDbType())) {
            sql = "select count(*) from pg_views t where t.viewname='" + viewName.toLowerCase() + "'";
        } else if (DB_MYSQL.equals(getDbType())) {

        } else if (DB_DAMING.equals(getDbType())) {

        }
        List<Long> rlt = queryForList(sql);
        if (null != rlt && !rlt.isEmpty()) {
            if (Integer.parseInt(StringUtil.null2zero(rlt.get(0))) > 0) return true;
        }
        return false;
    }

    /**
     * qiucs 2013-10-9
     * <p>描述: 判断表中字段是否存在</p>
     * @param  tableName
     * @param  columnName
     */
    @SuppressWarnings("unchecked")
    public boolean columnExists(String tableName, String columnName) {
        String sql = null;
        if (DB_ORACLE.equals(getDbType())) {
            sql = "select count(*) from user_tab_columns t where t.table_name='" + tableName.toUpperCase() + "' and t.column_name='" + columnName.toUpperCase() + "' ";
        } else if (DB_SQLSERVER.equals(getDbType())) {
            sql = "select count(*) from syscolumns t where t.id=object_id('" + tableName.toUpperCase() + "') and t.name='" + columnName.toUpperCase() + "' ";
        } else if (DB_HIGHGO.equals(getDbType())) {
            sql = "select count(*) from pg_class c,pg_attribute attr where c.oid = attr.attrelid and c.relname  = '" + tableName.toLowerCase() + "' and attr.attname='" + columnName.toLowerCase() + "'";
        } else if (DB_MYSQL.equals(getDbType())) {

        } else if (DB_DAMING.equals(getDbType())) {

        }
        List<Long> rlt = queryForList(sql);
        if (null != rlt && !rlt.isEmpty()) {
            if (Integer.parseInt(StringUtil.null2zero(rlt.get(0))) > 0) return true;
        }
        return false;
    }

    /**
     * <p>描述: 获取表中字段的长度</p>
     * @param  tableName 表名
     * @param  columnName 列名
     */
    @SuppressWarnings("unchecked")
    public int getColumnLength(String tableName, String columnName) {
        String sql = null;
        if (DB_ORACLE.equals(getDbType())) {
            sql = "select data_length from user_tab_columns t where t.table_name='" + tableName.toUpperCase() + "' and t.column_name='" + columnName.toUpperCase() + "' ";
        } else if (DB_SQLSERVER.equals(getDbType())) {
            sql = "select length from syscolumns t where t.id=object_id('" + tableName.toUpperCase() + "') and t.name='" + columnName.toUpperCase() + "' ";
        } else if (DB_HIGHGO.equals(getDbType())) {
            sql = "select (case when t.character_maximum_length is null then t.numeric_precision else t.character_maximum_length end) as col_len "
                    + "from information_schema.columns "
                    + "t where table_schema = 'public' and t.table_name='" + tableName.toLowerCase() + "' "
                    + "and t.column_name='" + columnName.toLowerCase() + "'";
        } else if (DB_MYSQL.equals(getDbType())) {

        } else if (DB_DAMING.equals(getDbType())) {

        }
        List<Long> rlt = queryForList(sql);
        if (null != rlt && !rlt.isEmpty()) {
            return Integer.parseInt(StringUtil.null2zero(rlt.get(0)));
        }
        return 0;
    }

    /**
     * qiucs 2013-8-6
     * <p>描述: 执行一句SQL语句</p>
     */
    @Transactional
    public int executeSql(String sql) {
        return getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    /**
     * qiucs 2015-8-14 下午1:48:37
     * <p>描述: 执行一句SQL语句 </p>
     * @return int
     */
    @Transactional
    public int executeSql(String sql, Object[] paramArr) {
        Query query = getEntityManager().createNativeQuery(sql);
        if (null != paramArr && paramArr.length > 0) {
            int i = 0, len = paramArr.length;
            for (; i < len; i++) {
                query.setParameter(i + 1, paramArr[i]);
            }
        }
        return query.executeUpdate();
    }

    /**
     * qiucs 2013-11-15
     * <p>描述: 索引名称</p>
     * @param  tableName
     */
    private String getIndexName(String indexPrefix, String tableName, String columnName) {

//        String name = "TRE";
//        name += tableName.substring(1) + "_" + columnName;
//        if (name.length() > 30) {
//			int step = name.length() - 30;
//			if (step % 2 == 1) {
//				name = "TRE" + tableName.substring(1, tableName.length() - ((step + 1) / 2)) + columnName.substring((step - 1) / 2, columnName.length());
//			} else {
//				name = "TRE" + tableName.substring(1, tableName.length() / 2) + columnName.substring(0, columnName.length() / 2);
//			}
//		}
        return indexPrefix + "_" + UUIDGenerator.uuid().substring(12);
    }

    /**
     * qiucs 2013-11-15
     * <p>描述: 创建单列索引</p>
     * @param  indexPrefix
     * @param  tableName
     * @param  columnName --单列字段名称
     */
    public void createOneColumnIndex(String indexPrefix, String tableName, String columnName) {
        if (!hasColumnCreateIndex(tableName, columnName)) {
            String indexName = getIndexName(indexPrefix, tableName, columnName);
            createIndex(indexName, tableName, columnName);
        }
    }

    /**
     * qiucs 2013-11-15
     * <p>描述: 删除单列索引</p>
     * @param  indexPrefix
     * @param  tableName
     * @param  columnName --单列字段名称
     */
    public void dropOneColumnIndex(String indexPrefix, String tableName, String columnName) {
        String indexName = getIndexName(indexPrefix, tableName, columnName);
        if (hasIndexCreated(indexName)) {
            dropIndex(indexName, tableName);
        }
    }

    /**
     * qiucs 2013-10-23
     * <p>描述: 创建索引</p>
     * @param  indexName
     * @param  tableName
     * @param  columnNames    设定参数
     */
    public void createIndex(String indexName, String tableName, String columnNames) {
        int cnt = countIndex(tableName);
        if (cnt > CfgCommonUtil.getIndexNumber()) {
            log.error("表（" + tableName + "）索引个数已经达到 " + cnt + " 个，再增加索引则会影响性能！");
            return;
        }
        String sql = "create index " + indexName + " on " + tableName + "(" + columnNames + ")";
        executeSql(sql);
    }

    /**
     * qiucs 2013-10-23
     * <p>描述: 删除索引</p>
     * @param  indexName    设定参数
     */
    public void dropIndex(String indexName, String tableName) {
        if (DB_TYPE.equals(DB_ORACLE)) {
            dropOracleIndex(indexName);
        } else if (DB_TYPE.equals(DB_SQLSERVER)) {
            dropSqlserverIndex(indexName, tableName);
        }else if (DB_TYPE.equals(DB_HIGHGO)) {
            dropOracleIndex(indexName); //TODO 缺少测试
        }
    }

    /**
     * qiucs 2013-12-3
     * <p>描述: 删除索引(ORACLE)</p>
     * @param  indexName    索引名称
     */
    public void dropOracleIndex(String indexName) {
        String sql = "drop index " + indexName;
        executeSql(sql);
    }

    /**
     * qiucs 2013-12-3
     * <p>描述: 删除索引(sqlserver)</p>
     * @param  indexName    索引名称
     * @param  tableName    表名
     */
    public void dropSqlserverIndex(String indexName, String tableName) {
        String sql = "drop index " + indexName + " on " + tableName;
        executeSql(sql);
    }

    /**
     * qiucs 2013-10-23
     * <p>描述: 判断指定列在指定表中是否已建索引(统一入口)</p>
     * @param  tableName
     * @param  columnName
     * @return Boolean   true--已建索引, false--未建索引
     */
    public Boolean hasColumnCreateIndex(String tableName, String columnName) {
        if (DB_ORACLE.equals(getDbType())) {
            return hasColumnCreateOracleIndex(tableName, columnName);
        } else if (DB_SQLSERVER.equals(getDbType())) {
            return hasColumnCreateSqlserverIndex(tableName, columnName);
        }
        return Boolean.FALSE;
    }

    /**
     * qiucs 2013-10-23
     * <p>描述: 判断指定列在指定表中是否已建索引(oracle)</p>
     * @param  tableName
     * @param  columnName
     * @return Boolean   true--已建索引, false--未建索引
     */
    protected Boolean hasColumnCreateOracleIndex(String tableName, String columnName) {
        String sql = "select count(*) from user_ind_columns t where t.table_name=upper('" + tableName + "') and t.column_name = upper('" + columnName + "')";
        long count = Long.parseLong(queryForList(sql).get(0).toString());
        return (count == 0 ? Boolean.FALSE : Boolean.TRUE);
    }

    /**
     * qiucs 2013-10-23
     * <p>描述: 判断指定列在指定表中是否已建索引(sqlserver)</p>
     * @param  tableName
     * @param  columnName
     * @return Boolean   true--已建索引, false--未建索引
     */
    protected Boolean hasColumnCreateSqlserverIndex(String tableName, String columnName) {
        String sql = "select count(*) from sys.indexes idx " +
                "join sys.index_columns idxc on (idx.object_id = idxc.object_id and idx.index_id = idxc.index_id) " +
                "join sys.objects o on(idx.object_id = o.object_id) " +
                "join sys.columns c on(o.object_id = c.object_id and o.type='U' and o.is_ms_shipped=0 and idxc.column_id = c.column_id) " +
                "where o.name=upper('" + tableName + "') and c.name = upper('" + columnName + "')";
        long count = Long.parseLong(queryForList(sql).get(0).toString());
        return (count == 0 ? Boolean.FALSE : Boolean.TRUE);
    }

    /**
     * qiucs 2013-10-23
     * <p>描述: 根据索引名称判断索引是否已建（统一入口）</p>
     * @param  indexName
     * @return Boolean    true--已建, false--未建
     * @throws
     */
    public Boolean hasIndexCreated(String indexName) {
        if (DB_ORACLE.equals(getDbType())) {
            return hasOracleIndexCreated(indexName);
        } else if (DB_SQLSERVER.equals(getDbType())) {
            return hasSqlserverIndexCreated(indexName);
        }
        return Boolean.FALSE;
    }

    /**
     * qiucs 2013-10-23
     * <p>描述: 根据索引名称判断索引是否已建（oracle）</p>
     * @param  indexName
     * @return Boolean    true--已建, false--未建
     * @throws
     */
    protected Boolean hasOracleIndexCreated(String indexName) {
        String sql = "select count(*) from user_indexes t where t.index_name=upper('" + indexName + "')";
        long count = Long.parseLong(queryForList(sql).get(0).toString());
        return (count == 0 ? Boolean.FALSE : Boolean.TRUE);
    }

    /**
     * qiucs 2013-10-23
     * <p>描述: 根据索引名称判断索引是否已建（sqlserver）</p>
     * @param  indexName
     * @return Boolean    true--已建, false--未建
     * @throws
     */
    protected Boolean hasSqlserverIndexCreated(String indexName) {
        String sql = "select count(*) from sys.indexes t where t.name=upper('" + indexName + "')";
        long count = Long.parseLong(queryForList(sql).get(0).toString());
        return (count == 0 ? Boolean.FALSE : Boolean.TRUE);
    }

    /**
     * qiucs 2014-3-6
     * <p>描述: 统计指定表的索引数量</p>
     * @param  tableName
     */
    public int countIndex(String tableName) {
        if (DB_ORACLE.equals(getDbType())) {
            return countIndexOfOracle(tableName);
        } else if (DB_SQLSERVER.equals(getDbType())) {
            return countIndexOfSqlserver(tableName);
        } else if (DB_DAMING.equals(getDbType())) {

        } else if (DB_MYSQL.equals(getDbType())) {

        }

        return 0;
    }

    /**
     * qiucs 2014-3-6
     * <p>描述: 统计表中索引总数(oracle)</p>
     * @param  tableName 表英文名称
     */
    protected int countIndexOfOracle (String tableName) {
        String sql = "select count(*) num_00 from user_indexes t where t.table_name = upper('" + tableName + "') ";
        return Integer.parseInt(queryForList(sql).get(0).toString());
    }

    /**
     * qiucs 2014-3-6
     * <p>描述: 统计表中索引总数(sqlserver)</p>
     * @param  tableName 表英文名称
     */
    protected int countIndexOfSqlserver (String tableName) {
        String sql = "select count(*) num_00 from sys.indexes idx " +
                "join sys.objects o on(idx.object_id = o.object_id) " +
                "where o.name = upper('" + tableName + "') ";
        return Integer.parseInt(queryForList(sql).get(0).toString());
    }

    /**
     * qiucs 2015-6-9 上午11:09:16
     * <p>描述: 查询 </p>
     * @return List
     */
    @SuppressWarnings("rawtypes")
    public List queryForList(String sql, Object[] paramArr) {
        Query query = getEntityManager().createNativeQuery(sql);
        if (null != paramArr && paramArr.length > 0) {
            int i = 0, len = paramArr.length;
            for (; i < len; i++) {
                query.setParameter(i+1, paramArr[i]);
            }
        }
        return query.getResultList();
    }

    /**
     * qiucs 2013-8-6
     * <p>描述: 查询</p>
     * @param  sql
     */
    @SuppressWarnings("rawtypes")
    public List queryForList(String sql) {
        return getEntityManager().createNativeQuery(sql).getResultList();
    }

    /**
     * qiucs 2015-6-9 上午11:12:20
     * <p>描述: 查询一条记录 </p>
     * @return Object
     */
    public Object queryForObject(String sql, Object[] paramArr) {
        Query query = getEntityManager().createNativeQuery(sql);
        if (null != paramArr && paramArr.length > 0) {
            int i = 0, len = paramArr.length;
            for (; i < len; i++) {
                query.setParameter(i+1, paramArr[i]);
            }
        }
        return query.getSingleResult();
    }

    /**
     * qiucs 2014-1-9
     * <p>描述: </p>
     * @param  sql
     * @param  resultClass
     * @return T    返回类型
     */
    public Object queryForObject(String sql) {
        return getEntityManager().createNativeQuery(sql).getSingleResult();
    }

    /**
     * qiucs 2015-6-9 上午11:14:25
     * <p>描述: 查询 </p>
     * @return Map<String,Object>
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryForMap(String sql, Object[] paramArr) {
        Query query = getEntityManager().createNativeQuery(sql);
        if (null != paramArr && paramArr.length > 0) {
            int i = 0, len = paramArr.length;
            for (; i < len; i++) {
                query.setParameter(i+1, paramArr[i]);
            }
        }
        Object data = query.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
        return (null != data) ? (Map<String, Object>) data : new HashMap<String, Object>();
    }

    /**
     * qiucs 2015-4-10 下午2:38:52
     * <p>描述: 查询单个Map对象 </p>
     * @return Map<String,Object>
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryForMap(String sql) {
        Object data = getEntityManager().createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
        return (null != data) ? (Map<String, Object>) data : new HashMap<String, Object>();
    }

    /**
     * qiucs 2015-6-9 上午11:16:04
     * <p>描述: 查询 </p>
     * @return List<Map<String,Object>>
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Map<String, Object>> queryForMaps(String sql, Object[] paramArr) {
        Query query = getEntityManager().createNativeQuery(sql);
        if (null != paramArr && paramArr.length > 0) {
            int i = 0, len = paramArr.length;
            for (; i < len; i++) {
                query.setParameter(i+1, paramArr[i]);
            }
        }
        List list = query.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return (null != list && !list.isEmpty()) ? (List<Map<String, Object>>) list
                : new ArrayList<Map<String, Object>>();
    }

    /**
     * qiucs 2015-4-10 下午2:39:26
     * <p>描述: 查询Map对象列表 </p>
     * @return List<Map<String,Object>>
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Map<String, Object>> queryForMaps(String sql) {
        List list = getEntityManager().createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return (null != list && !list.isEmpty()) ? (List<Map<String, Object>>) list
                : new ArrayList<Map<String, Object>>();
    }

    /**
     * qiucs 2014-1-7
     * <p>描述: </p>
     * @param  hql
     * @param  resultClass
     * @return List<T>    返回类型
     * @throws
     */
    public <T> List<T> queryEntityForList(String hql, Class<T> resultClass) {
        return getEntityManager().createQuery(hql, resultClass).getResultList();
    }

    /**
     * qiucs 2014-1-24
     * <p>描述: 分页</p>
     * @param  sql
     * @param  begin
     * @param  end
     * @return List    返回类型
     * @throws
     */
    @SuppressWarnings("rawtypes")
    public List page(String sql, int begin, int end) {
        Query query = getEntityManager().createNativeQuery(sql);
        query.setFirstResult(begin);
        query.setMaxResults(end);
        return query.getResultList();
    }

    /**
     * qiucs 2015-4-10 下午3:45:58
     * <p>描述: 分页查询（List<Map>） </p>
     * @return List<Map<String,Object>>
     */
    @SuppressWarnings({ "rawtypes"})
    public List pageMaps(String sql, int begin, int end) {
        Query query = getEntityManager().createNativeQuery(sql);
        query.setFirstResult(begin);
        query.setMaxResults(end);
        return query.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();

    }

    /**
     * qiucs 2014-1-7
     * <p>描述: </p>
     * @param  hql
     * @param  resultClass
     * @return T    返回类型
     */
    public <T> T queryEntityForObject(String hql, Class<T> resultClass) {
        return getEntityManager().createQuery(hql, resultClass).getSingleResult();
    }

    /**
     * qiucs 2013-8-6
     * <p>描述: 对应数据库的数据类型</p>
     * @param  databaseType
     * @param  type
     * @param  length
     */
    public String getDataType(String databaseType, String type, Integer length) {
        String dataType = null;
        if (DB_ORACLE.equals(databaseType)) {
            /*if (ConstantVar.DataType.CHAR.equals(type) || ConstantVar.DataType.ENUM.equals(type)
                    || ConstantVar.DataType.USER.equals(type) || ConstantVar.DataType.PART.equals(type)) {
                dataType = "varchar2(" + length + ")";
            } else if (ConstantVar.DataType.NUMBER.equals(type)) {
                dataType = length == null ? "number" : "number(" + length + ")";
            } else if (ConstantVar.DataType.DATE.equals(type)) {
                dataType = "date";
            }//*/
            if (ConstantVar.DataType.NUMBER.equals(type)) {
                dataType = "number(" + (null == length ? "18" : length) + ")";
            } else {
                dataType = "varchar2(" + (null == length ? "50" : length) + ")";
            }
        } else if (DB_SQLSERVER.equals(databaseType)) {
            /*if (ConstantVar.DataType.CHAR.equals(type) || ConstantVar.DataType.ENUM.equals(type)
                    || ConstantVar.DataType.USER.equals(type) || ConstantVar.DataType.PART.equals(type)) {
                dataType = "varchar(" + length + ")";
            } else if (ConstantVar.DataType.NUMBER.equals(type)) {
                dataType = "int";
            } else if (ConstantVar.DataType.DATE.equals(type)) {
                dataType = "datetime";
            }//*/
            if (ConstantVar.DataType.NUMBER.equals(type)) {
                dataType = "decimal(" + length + ")";
            } else {
                dataType = "nvarchar(" + (null == length ? "50" : length) + ")";
            }
        } else if (DB_HIGHGO.equals(databaseType)) {
            if (ConstantVar.DataType.NUMBER.equals(type)) {
                dataType = "decimal(" + length + ")";
            } else {
                dataType = "varchar(" + (null == length ? "50" : length) + ")";
            }
        } else if (DB_MYSQL.equals(databaseType)) {

        } else if (DB_DAMING.equals(databaseType)) {

        }
        return dataType;
    }

    /**
     * qiucs 2015-3-4 下午10:54:54
     * <p>描述: 对应数据库的数据类型 </p>
     * @return String
     */
    private String getDataType(String type, Integer length) {
        return getDataType(type, length, null);
    }

    /**
     * qiucs 2013-8-6
     * <p>描述: 对应数据库的数据类型(数字类型含精度格式)</p>
     * @param  type
     * @param  length
     */
    private String getDataType(String type, Integer length, Integer precision) {
        String dataType = null;
        if (DB_ORACLE.equals(getDbType())) {
            /*if (ConstantVar.DataType.CHAR.equals(type) || ConstantVar.DataType.ENUM.equals(type)
                    || ConstantVar.DataType.USER.equals(type) || ConstantVar.DataType.PART.equals(type)) {
                dataType = "varchar2(" + length + ")";
            } else if (ConstantVar.DataType.NUMBER.equals(type)) {
                dataType = length == null ? "number" : "number(" + length + ")";
            } else if (ConstantVar.DataType.DATE.equals(type)) {
                dataType = "date";
            }//*/
            if (ConstantVar.DataType.NUMBER.equals(type)) {
                dataType = "number";
                if (null != precision && precision > 0) {
                    dataType += "(" + (null == length ? 18 : length) + "," + precision + ")";
                } else {
                    dataType += (null == length ? "" : ("(" + length + ")"));
                }
            } else {
                dataType = "varchar2(" + (null == length ? "100" : length) + ")";
            }
        } else if (DB_SQLSERVER.equals(getDbType())) {
            /*if (ConstantVar.DataType.CHAR.equals(type) || ConstantVar.DataType.ENUM.equals(type)
                    || ConstantVar.DataType.USER.equals(type) || ConstantVar.DataType.PART.equals(type)) {
                dataType = "varchar(" + length + ")";
            } else if (ConstantVar.DataType.NUMBER.equals(type)) {
                dataType = "int";
            } else if (ConstantVar.DataType.DATE.equals(type)) {
                dataType = "datetime";
            }//*/
            if (ConstantVar.DataType.NUMBER.equals(type)) {
                dataType = "decimal";
                if (null != precision && precision > 0) {
                    dataType += "(" + (null == length ? 18 : length) + "," + precision + ")";
                } else {
                    dataType += (null == length ? "" : ("(" + length + ")"));
                }
            } else {
                dataType = "varchar(" + (null == length ? "100" : length) + ")";
            }
        } else if (DB_HIGHGO.equals(getDbType())) {
            if (ConstantVar.DataType.NUMBER.equals(type)) {
                dataType = "decimal";
                if (null != precision && precision > 0) {
                    dataType += "(" + (null == length ? 18 : length) + "," + precision + ")";
                } else {
                    dataType += (null == length ? "" : ("(" + length + ")"));
                }
            } else {
                dataType = "varchar(" + (null == length ? "50" : length) + ")";
            }
        } else if (DB_MYSQL.equals(getDbType())) {

        } else if (DB_DAMING.equals(getDbType())) {

        }
        return dataType;
    }
    /**
     * qiucs 2013-8-6
     * <p>描述: 获取数据库连接</p>
     */
    private static String getConnectionUrl() {
        if (StringUtil.isEmpty(connectionUrl)) {
            DataSource dsource = (DataSource)XarchListener.getBean("dataSource");
            Connection conn = null;
            try {
                conn = dsource.getConnection();
                return dsource.getConnection().getMetaData().getURL().toLowerCase();
            } catch (SQLException e) {
                log.error("获取数据库连接地址出错！", e);
            } finally {
                try {
                    if (null != conn) conn.close();
                } catch (SQLException e) {
                    log.error("关闭数据库连接地址出错！", e);
                }
            }
        }
        return connectionUrl;
    }

    /**
     * qiucs 2014-9-10
     * <p>描述: 获取数据连接</p>
     * @return Connection    返回类型
     * @throws
     */
    private Connection getConnection() {
        return getEntityManager().unwrap(SessionImpl.class).connection();
    }

    /**
     * qiucs 2015-4-28 下午4:37:20
     * <p>描述: TODO(这里用一句话描述这个方法的作用) </p>
     * @return Connection
     */
    private Connection newConnection() {
        DataSource dsource = (DataSource)XarchListener.getBean("dataSource");
        Connection conn = null;
        try {
            conn = dsource.getConnection();
        } catch (SQLException e) {
            log.error("获取数据库连接地址出错！", e);
        }
        return conn;
    }

    /**
     * qiucs 2015-3-9 下午3:06:18
     * <p>描述: JDBC查询 </p>
     * @return void
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public void jdbcQuery(String sql, RowMapHandler handler) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = newConnection();
            ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int i=0, len = md.getColumnCount();
            String key, val;
            Map<String, Object> rowMap = null;
            while (rs.next()) {
                rowMap = new HashMap<String, Object>();
                for (i = 1; i <= len; i ++) {
                    key = md.getColumnName(i);
                    val = rs.getString(i);
                    rowMap.put(key, val);
                }
                handler.processRowData(rowMap);
            }
        } catch (SQLException e) {
            log.error("JDBC查询（RowMapHandler）出错", e);
        } finally {
            close(conn, ps, rs);
        }
    }

    /**
     * qiucs 2015-3-9 下午3:34:37
     * <p>描述: JDBC查询 </p>
     * @return void
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public void jdbcQuery(String sql, RowArrayHandler handler) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = newConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int i=0, len = md.getColumnCount();
            Object[] objArr = null;
            while (rs.next()) {
                objArr = new Object[len];
                for (i = 1; i <= len; i ++) {
                    objArr[i - 1] = rs.getString(i);
                }
                handler.processRowData(objArr);
            }
        } catch (SQLException e) {
            log.error("JDBC查询（RowArrayHandler）出错", e);
        } finally {
            close(conn, ps, rs);
        }
    }

    /**
     * qiucs 2015-3-9 下午3:40:17
     * <p>描述: JDBC查询 </p>
     * @return void
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public void jdbcQuery(String sql, ResultSetHandler handler) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = newConnection();
            ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            rs.setFetchSize(500);
            while (rs.next()) {
                handler.processRowData(rs);
            }
        } catch (SQLException e) {
            log.error("JDBC查询（ResultSetHandler）出错", e);
        } finally {
            close(conn, ps, rs);
        }
    }

    /**
     * qiucs 2015-5-11 下午5:20:02
     * <p>描述: JDBC查询 </p>
     * @return void
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public void jdbcQuery(String sql, ControlResultSetHandler handler) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = newConnection();
            ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            //rs.setFetchSize(100);
            while (rs.next()) {
                if (!handler.processRowData(rs)) {
                    break;
                }
            }
        } catch (SQLException e) {
            log.error("JDBC查询（ControlResultSetHandler）出错", e);
        } finally {
            close(conn, ps, rs);
        }
    }

    /**
     * qiucs 2015-4-28 下午9:50:08
     * <p>描述: 关闭数据库连接 </p>
     * @return void
     */
    private void close(Connection conn, Statement stmt, ResultSet rset) {
        try {
            if (null != stmt) stmt.close();
            if (null != rset) rset.close();
            if (null != conn) conn.close();
            rset = null;
            rset = null;
            conn = null;
        } catch (SQLException e) {
            log.error("关闭JDBC查询（stmt/rset/conn）出错", e);
        }
    }

    /**
     * qiucs 2015-3-9 下午4:00:23
     * <p>描述: JDBC批量执行SQL </p>
     * @return int[]
     */
    @Transactional
    public int[] jdbcExecuteBatch(String[] sqls) {
        Statement stmt = null;
        int[] iArr = null;
        try {
            stmt = getConnection().createStatement();
            for (int i = 0, len = sqls.length; i < len; i++) {
                stmt.addBatch(sqls[i]);
            }
            iArr = stmt.executeBatch();
        } catch (SQLException e) {
            log.error("JDBC批量执行SQL出错", e);
        } finally {
            try {
                if (null != stmt) stmt.close();
            } catch (SQLException e) {
                log.error("关闭JDBC批量执行SQL出错", e);
            }
        }
        return iArr;
    }

    /**
     * qiucs 2015-3-9 下午4:09:57
     * <p>描述: JDBC执行SQL </p>
     * @return void
     */
    @Transactional
    public void jdbcExecuteSql(String sql) {
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            log.error("JDBC执行SQL出错", e);
        } finally {
            try {
                if (null != ps) ps.close();
            } catch (SQLException e) {
                log.error("关闭JDBC执行SQL出错", e);
            }
        }
    }

    /**
     * qiucs 2013-8-6
     * <p>标题: getDbType</p>
     * <p>描述: 获取当前是哪种数据库</p>
     */
    public static String getDbType() {
        if (null == DB_TYPE) {
            if (getConnectionUrl().indexOf(DB_ORACLE) > -1) {
                DB_TYPE = DB_ORACLE;
            } else if (getConnectionUrl().indexOf(DB_SQLSERVER) > -1) {
                DB_TYPE = DB_SQLSERVER;
            } else if (getConnectionUrl().indexOf(DB_MYSQL) > -1) {
                DB_TYPE = DB_MYSQL;
            } else if (getConnectionUrl().indexOf(DB_DAMING) > -1) {
                DB_TYPE = DB_ORACLE;
            } else if (getConnectionUrl().indexOf(DB_HIGHGO) > -1) {
                DB_TYPE = DB_HIGHGO;
            }
        }
        return DB_TYPE;
    }

    /**
     * qiucs 2014-3-31
     * <p>描述: 真实数据库类型</p>
     * @return String    返回类型
     */
    public static String getRealDbType() {
        if (getConnectionUrl().indexOf(DB_DAMING) > -1) {
            return DB_DAMING;
        }
        return getDbType();
    }

    /**
     * qiucs 2014-2-19
     * <p>描述: 给表加注释</p>
     * @param  tableName
     * @param  comment    设定参数
     */
    public void commentTable(String tableName, String comment) {
        StringBuffer sql = new StringBuffer();
        if (getDbType().equals(DB_ORACLE) || getDbType().equals(DB_HIGHGO)) {
            sql.append("comment on table ").append(tableName).append(" is '").append(comment).append("'");
        } else if (getDbType().equals(DB_SQLSERVER)) {
            sql.append("execute sp_addextendedproperty N'MS_Description', N'").append(comment)
                    .append("', N'user', N'dbo', N'table', N'").append(tableName).append("', NULL, NULL ");
        }
        if (sql.length() > 0) {
            executeSql(sql.toString());
        }
    }

    /**
     * qiucs 2014-2-19
     * <p>描述: 给字段加注释</p>
     * @param  tableName
     * @param  columnName
     * @param  comment    设定参数
     */
    public void commentColumn(String tableName, String columnName, String comment) {
        StringBuffer sql = new StringBuffer();
        if (getDbType().equals(DB_ORACLE) || getDbType().equals(DB_HIGHGO)) {
            sql.append("comment on column ").append(tableName).append(".").append(columnName).append(" is '").append(comment).append("'");
        } else if (getDbType().equals(DB_SQLSERVER)) {
            if (existsCommentOfSqlserver(tableName, columnName)) {
                sql.append("execute sp_updateextendedproperty");
            } else {
                sql.append("execute sp_addextendedproperty");
            }
            sql.append(" N'MS_Description', N'").append(comment)
                    .append("', N'user', N'dbo', N'table', N'").append(tableName).append("', N'column', N'").append(columnName).append("' ");
        }
        if (sql.length() > 0) {
            executeSql(sql.toString());
        }
    }

    /**
     * qiucs 2014-3-24
     * <p>描述: 判断字段备注是否存在（SQLSERVER）</p>
     * @param  tableName
     * @param  columnName
     * @return boolean    返回类型   true--存在，false--不存在
     */
    @SuppressWarnings("rawtypes")
    private boolean existsCommentOfSqlserver(String tableName, String columnName) {
        String sql = "select 1 FROM syscolumns a " +
                "join sysobjects d on a.id=d.id " +
                "left join sys.extended_properties g on a.id=g.major_id and a.colid=g.minor_id " +
                "where d.name='" + tableName + "' and a.name='" + columnName + "' and isnull(g.[value],'') <> ''";
        List list = queryForList(sql);
        if (null == list || list.isEmpty()) return false;

        return true;
    }

    public static boolean isOracle() {
        return getDbType().equals(DB_ORACLE);
    }

    public static boolean isSqlserver() {
        return getDbType().equals(DB_SQLSERVER);
    }

    public static boolean isHighgo() {
        return getDbType().equals(DB_HIGHGO);
    }

    /**
     * qiucs 2014-12-26 下午12:36:55
     * <p>描述: 获取数据连接符 </p>
     * @return String
     */
    public static String getSeperator() {
        if (isSqlserver()) {
            return " + ";
        }
        if (isOracle()) {
            return " || ";
        }
        return " ";
    }

}

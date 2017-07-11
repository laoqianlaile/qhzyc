package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface PhysicalTableDefineDao extends StringIDDao<PhysicalTableDefine> {
    /**
     * qiucs 2013-8-15 
     * <p>标题: getViewsOfOracle</p>
     * <p>描述: 获取表定义中不存在的，但在数据库中存在的视图(ORACLE)</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="SELECT UT.TABLE_NAME AS TABLE_NAME, NVL(UT.COMMENTS, UT.TABLE_NAME) AS COMMENTS " +
            " FROM USER_TAB_COMMENTS UT" +
            " LEFT JOIN T_XTPZ_PHYSICAL_TABLE_DEFINE TD ON (UT.TABLE_NAME=TD.TABLE_NAME AND TD.TABLE_TYPE='1')" +
            " WHERE UT.TABLE_TYPE='VIEW' AND TD.ID IS NULL ", nativeQuery=true)
    public List<Object[]> getViewsOfOracle();

    /**
     * qiucs 2013-8-15 
     * <p>标题: getViewsOfSqlserver</p>
     * <p>描述: 获取表定义中不存在的，但在数据库中存在的视图(SQLSERVER)</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" SELECT T.NAME AS TABLE_NAME, CAST(ISNULL(P.VALUE, T.NAME) AS NVARCHAR(255)) AS COMMENTS " +
            " FROM SYSOBJECTS T " +
            " LEFT JOIN SYS.EXTENDED_PROPERTIES P ON T.ID=P.MAJOR_ID AND P.MINOR_ID=0 " +
            " LEFT JOIN T_XTPZ_PHYSICAL_TABLE_DEFINE TD ON (T.NAME=TD.TABLE_NAME AND TD.TABLE_TYPE='1')" +
            " WHERE T.XTYPE='V' AND T.NAME<>'dtproperties' AND TD.ID IS NULL ", nativeQuery=true)
    public List<Object[]> getViewsOfSqlserver();

    /**
     * qiucs 2016-8-18 
     * <p>描述: 获取表定义中不存在的，但在数据库中存在的视图(HIGHGO)</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" select upper(t.viewname) as table_name, upper(t.viewname) as comments " +
            " from pg_views t " +
            " left join t_xtpz_physical_table_define td on (upper(t.viewname)=td.table_name and td.table_type='1')" +
            " where t.schemaname='public' and td.id is null ", nativeQuery=true)
    public List<Object[]> getViewsOfHighgo();

    /**
     * qiucs 2013-8-15 
     * <p>标题: getColumnsOfOracleView</p>
     * <p>描述: 根据视图名称获取视图中的字段(ORACLE)</p>
     * @param  name
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="SELECT C.COLUMN_NAME, C.DATA_TYPE, C.DATA_LENGTH, NVL(P.COMMENTS, C.COLUMN_NAME) AS COMMENTS " +
            " FROM USER_TAB_COLS C " +
            " JOIN USER_COL_COMMENTS P ON (C.COLUMN_NAME=P.COLUMN_NAME AND P.TABLE_NAME=?1) " +
            " WHERE C.TABLE_NAME=?1", nativeQuery=true)
    public List<Object[]> getColumnsOfOracleView(String name);

    /**
     * qiucs 2013-8-15 
     * <p>标题: getColumnsOfSqlserverView</p>
     * <p>描述: 根据视图名称获取视图中的字段(SQLSERVER)</p>
     * @param  name
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" SELECT C.NAME AS COLUMN_NAME, B.NAME AS DATA_TYPE, COLUMNPROPERTY(C.ID,C.NAME,'PRECISION') AS DATA_LENGTH, ISNULL(P.VALUE, C.NAME) AS COMMENTS " +
            " FROM SYSCOLUMNS C " +
            " LEFT JOIN SYSTYPES B ON C.XUSERTYPE=B.XUSERTYPE " +
            " LEFT JOIN SYSOBJECTS T ON C.ID=T.ID AND T.XTYPE='V' AND T.NAME<>'dtproperties' " +
            " LEFT JOIN SYS.EXTENDED_PROPERTIES P ON C.ID=P.MAJOR_ID AND C.COLID=P.MINOR_ID " +
            " WHERE T.NAME=?1 " +
            " ORDER BY C.COLORDER ", nativeQuery=true)
    public List<Object[]> getColumnsOfSqlserverView(String name);

    /**
     * qiucs 2016-8-18 
     * <p>描述: 根据视图名称获取视图中的字段(HIGHGO)</p>
     * @param  name
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" select c.column_name, c.data_type, " +
            " (case when c.character_maximum_length is null then c.numeric_precision else c.character_maximum_length end) as data_length, c.column_name as comments " +
            " from information_schema.columns c " +
            " where upper(c.table_name)=?1 " +
            " order by c.ordinal_position ", nativeQuery=true)
    public List<Object[]> getColumnsOfHighgoView(String name);

    /**
     * qiucs 2013-8-15 
     * <p>标题: getNotExistsViewOfOracle</p>
     * <p>描述: 查找表定义记录中有的，但在数据中不存在的视图（ORACLE）</p>
     * @return List<String>    返回类型   
     * @throws
     */
    @Query(value=" SELECT TD.ID, TD.SHOW_NAME FROM T_XTPZ_PHYSICAL_TABLE_DEFINE TD " +
            " WHERE TD.TABLE_TYPE='1' " +
            " AND NOT EXISTS(SELECT 1 FROM USER_VIEWS UV WHERE UV.VIEW_NAME=TD.TABLE_NAME) ", nativeQuery=true)
    public List<Object[]> getNotExistsViewOfOracle();

    /**
     * qiucs 2013-8-15 
     * <p>标题: getNotExistsViewOfOracle</p>
     * <p>描述: 查找表定义记录中有的，但在数据中不存在的视图（SQLSERVER）</p>
     * @return List<String>    返回类型   
     * @throws
     */
    @Query(value=" SELECT TD.ID, TD.SHOW_NAME FROM T_XTPZ_PHYSICAL_TABLE_DEFINE TD " +
            " WHERE TD.TABLE_TYPE='1' " +
            " AND NOT EXISTS(SELECT 1 FROM SYSOBJECTS UV WHERE UV.XTYPE='V' AND UV.NAME=TD.TABLE_NAME) ", nativeQuery=true)
    public List<Object[]> getNotExistsViewOfSqlserver();

    /**
     * qiucs 2016-8-18 
     * <p>描述: 查找表定义记录中有的，但在数据中不存在的视图（HIGHGO）</p>
     * @return List<String>    返回类型   
     * @throws
     */
    @Query(value=" select td.id, td.show_name from t_xtpz_physical_table_define td " +
            " where td.table_type='1' " +
            " and not exists(select 1 from pg_views uv where upper(uv.viewname)=td.table_name) ", nativeQuery=true)
    public List<Object[]> getNotExistsViewOfHighgo();

    /**
     * qiucs 2013-8-15 
     * <p>标题: getColumnIdsNotInViewOfOracle</p>
     * <p>描述: 查找字段定义记录中存在，但在数据库视图中不存在的字段ID（ORACLE）</p>
     * @return List<String>    返回类型   
     * @throws
     */
    @Query(value=" select cd.id from t_xtpz_column_define cd " +
            " join t_xtpz_physical_table_define td on td.id=cd.table_id and td.table_type='1' " +
            " left join user_col_comments vc on td.table_name=vc.table_name and vc.column_name=cd.column_name " +
            " where td.table_type='1' and vc.column_name is null ", nativeQuery=true)
    public List<String> getColumnIdsNotInViewOfOracle();

    /**
     * qiucs 2013-8-15 
     * <p>标题: getColumnIdsNotInViewOfSqlserver</p>
     * <p>描述: 查找字段定义记录中存在，但在数据库视图中不存在的字段ID（SQLSERVER）</p>
     * @return List<String>    返回类型   
     * @throws
     */
    @Query(value=" select cd.id from t_xtpz_column_define cd " +
            " join t_xtpz_physical_table_define td on td.id=cd.table_id and td.table_type='1' " +
            " left join sysobjects t on t.xtype='v' and t.name=td.table_name " +
            " left join syscolumns c on c.id=t.id and c.name=cd.column_name " +
            " where td.table_type='1' and c.id is null ", nativeQuery=true)
    public List<String> getColumnIdsNotInViewOfSqlserver();

    /**
     * qiucs 2016-8-18 
     * <p>描述: 查找字段定义记录中存在，但在数据库视图中不存在的字段ID（HIGHGO）</p>
     * @return List<String>    返回类型   
     * @throws
     */
    @Query(value=" select cd.id from t_xtpz_column_define cd " +
            " join t_xtpz_physical_table_define td on td.id=cd.table_id and td.table_type='1' " +
            " left join pg_views t on upper(t.viewname)=td.table_name " +
            " left join information_schema.columns c on c.table_name=t.viewname and upper(c.column_name)=cd.column_name " +
            " where td.table_type='1' and c.column_name is null ", nativeQuery=true)
    public List<String> getColumnIdsNotInViewOfHighgo();

    /**
     * qiucs 2013-8-15 
     * <p>标题: getViewColumnsNotInRecordsOfOracle</p>
     * <p>描述: 查找数据库视图中存在的，但在字段定义中不存在的字段（ORACLE）</p>
     * @return List<String>    返回类型   
     * @throws
     */
    @Query(value="select c.column_name, c.data_type, c.data_length, nvl(p.comments, c.column_name) as comments, td.id, td.show_name " +
            " from user_tab_cols c " +
            " join user_col_comments p on (c.column_name=p.column_name and p.table_name=c.table_name) " +
            " join t_xtpz_physical_table_define td on (td.table_type='1' and td.table_name=c.table_name) " +
            " left join t_xtpz_column_define cd on (cd.table_id=td.id and cd.column_name=c.column_name) " +
            " where cd.id is null ", nativeQuery=true)
    public List<Object[]> getViewColumnsNotInRecordsOfOracle();

    /**
     * qiucs 2013-8-15 
     * <p>标题: getViewColumnsNotInRecordsOfSqlserver</p>
     * <p>描述: 查找数据库视图中存在的，但在字段定义中不存在的字段（SQLSERVER）</p>
     * @return List<String>    返回类型   
     * @throws
     */
    @Query(value=" select c.name as column_name, b.name as data_type, columnproperty(c.id,c.name,'precision') as data_length, cast(isnull(p.value, c.name) as nvarchar(255)) as comments, td.id, td.show_name " +
            " from syscolumns c " +
            " join sysobjects t on c.id=t.id and t.xtype='v' and t.name<>'dtproperties' " +
            " join t_xtpz_physical_table_define td on (td.table_type='1' and td.table_name=t.name) " +
            " left join t_xtpz_column_define cd on (cd.table_id=td.id and cd.column_name=c.name) " +
            " left join systypes b on c.xusertype=b.xusertype " +
            " left join sys.extended_properties p on c.id=p.major_id and c.colid=p.minor_id " +
            " where cd.id is null " +
            " order by c.colorder ", nativeQuery=true)
    public List<Object[]> getViewColumnsNotInRecordsOfSqlserver();

    /**
     * qiucs 2016年8月17日 下午5:58:51
     * <p>描述: 查找数据库视图中存在的，但在字段定义中不存在的字段（highgo）</p>
     * @return List<Object[]>
     */
    @Query(value=" select c.column_name, c.data_type," +
            " (case when c.character_maximum_length is null then c.numeric_precision else c.character_maximum_length end) as data_length," +
            " c.column_name as comments, td.id, td.show_name " +
            " from information_schema.columns c " +
            " join pg_views t on c.table_name=t.viewname and t.schemaname='public' " +
            " join t_xtpz_physical_table_define td on (td.table_type='1' and td.table_name=upper(t.viewname)) " +
            " left join t_xtpz_column_define cd on (cd.table_id=td.id and cd.column_name=upper(c.column_name)) " +
            " where cd.id is null " +
            " order by t.viewname, c.ordinal_position ", nativeQuery=true)
    public List<Object[]> getViewColumnsNotInRecordsOfHighgo();

    /**
     * qiucs 2013-9-12 
     * <p>标题: getTableNameById</p>
     * <p>描述: 根据ID获取表全名</p>
     * @param  id
     * @return String    返回类型   
     * @throws
     */
    @Query("select tableName from PhysicalTableDefine where id=?1")
    public String getTableNameById(String id);

    /**
     * qiucs 2013-9-18 
     * <p>标题: getMaxShowOrderByTableTreeId</p>
     * <p>描述: </p>
     * @return Object    返回类型   
     * @throws
     */
    @Query("SELECT MAX(T.showOrder) FROM PhysicalTableDefine T WHERE tableTreeId=?1")
    public Integer getMaxShowOrderByTableTreeId(String tableTreeId);

    /**
     * qiujinwei 2014-11-19 
     * <p>标题: getMaxShowOrderByTableType</p>
     * <p>描述: </p>
     * @return Object    返回类型   
     * @throws
     */
    @Query("SELECT MAX(T.showOrder) FROM PhysicalTableDefine T WHERE tableType=?1")
    public Integer getMaxShowOrderByTableType(String tableType);

    /**
     * qiucs 2013-9-18 
     * <p>标题: getShowOrderById</p>
     * <p>描述: 根据ID获取显示顺序</p>
     * @param  id
     * @return Integer    返回类型   
     * @throws
     */
    @Query("SELECT showOrder from PhysicalTableDefine WHERE ID=?1")
    public Integer getShowOrderById(String id);

    /**
     * qiucs 2013-9-18 
     * <p>标题: updateShowOrderById</p>
     * <p>描述: 根据ID更新显示顺序</p>
     * @param  id
     * @param  increaseNum    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE PhysicalTableDefine SET showOrder=(showOrder + ?2) WHERE id=?1")
    public void updateShowOrderById(String id, Integer increaseNum);

    /**
     * qiucs 2013-9-18 
     * <p>标题: upShowOrder</p>
     * <p>描述: 显示顺序批量更新(begin, end)开区间</p>
     * @param  parentId
     * @param  begin
     * @param  end
     * @param  increaseNum    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE PhysicalTableDefine SET showOrder = (showOrder + ?4) WHERE tableTreeId=?1 AND showOrder > ?2 AND showOrder < ?3")
    public void batchUpdateShowOrder(String tableTreeId, Integer begin, Integer end, Integer increaseNum);

    /**
     * qiucs 2013-9-26 
     * <p>标题: getTableShowName</p>
     * <p>描述: 获取表的显示名称</p>
     * @param  id
     * @return String    返回类型   
     * @throws
     */
    @Query("SELECT showName from PhysicalTableDefine WHERE id=?1")
    public String getTableShowName(String id);

    /**
     * wanglei 2013-11-11 
     * <p>标题: getByTableName</p>
     * <p>描述: 根据表全名获取表定义实体</p>
     * @param tableName 表全名
     * @return PhysicalTableDefine 返回类型
     */
    public PhysicalTableDefine getByTableName(String tableName);

    /**
     * wanglei 2013-11-13
     * <p>标题: getByTextAndTableTreeId</p>
     * <p>描述: 根据表名称和父节点ID获取表定义实体</p>
     * @param showName 表名称
     * @param tableTreeId 父节点ID
     * @return PhysicalTableDefine 返回类型
     */
    public PhysicalTableDefine getByShowNameAndTableTreeId(String showName, String tableTreeId);

    /**
     * <p>标题: getByClassificationAndType</p>
     * <p>描述: 根据类型获取表定义实体</p>
     * @param classification 分类
     * @return List<TableDefine> 返回类型
     */
    public List<PhysicalTableDefine> getByClassification(String classification);

    /**
     * 根据表id，获取电子文件级配置信息
     * @return
     */
    @Query(value="select " +
            " td.id, td.show_name, td.table_tree_id, td.table_type, td.show_order, td.table_name, td.created, td.classification, td.table_prefix, td.table_code, td.logic_table_code, td.remark " +
            " from t_xtpz_physical_table_define t" +
            " inner join t_xtpz_table_relation tr on t.id = tr.table_id" +
            " inner join t_xtpz_table_define td on tr.relate_table_id = td.id" +
            " where t.id = ?1 " +
            " UNION all " +
            " select " +
            " td.id, td.show_name, td.table_tree_id, td.table_type, td.show_order, td.table_name, td.created, td.classification, td.table_prefix, td.table_code, td.logic_table_code, td.remark " +
            " from t_xtpz_physical_table_define t" +
            " inner join t_xtpz_table_relation tr on t.id = tr.relate_table_id" +
            " inner join t_xtpz_table_define td on tr.table_id = td.id" +
            " where t.id = ?1 ", nativeQuery=true)
    public PhysicalTableDefine getDomcumentPhysicalTableDefineByTableId(String tableId);

    /**
     * qiucs 2014-11-26 
     * <p>描述: 获取物理表对应的逻辑表编码</p>
     */
    @Query("select t.logicTableCode from PhysicalTableDefine t where t.id=?1")
    public String getLogicTableCode(String id);

    /**
     * qiujinwei 2015-01-13 
     * <p>描述: 根据逻辑表编码获取物理表名</p>
     */
    @Query("select t.tableName from PhysicalTableDefine t where t.logicTableCode=?1")
    public String getTableNameByLogicTableCode(String logicTableCode);

    /**
     * qiujinwei 2014-12-01
     * <p>标题: getLTExcludingLG</p>
     * <p>描述: 获取物理表组下的物理表</p>
     * @param  groupCode
     * @return List<LogicTableDefine>    返回类型   
     * @throws
     */
    @Query("from PhysicalTableDefine t " +
            "where exists (select t2.id from PhysicalGroupRelation t2 where t2.tableId = t.id and t2.groupId = ?1)")
    public List<PhysicalTableDefine> getPTIncludingPG(String groupId);

    /**
     * qiujinwei 2014-12-01
     * <p>标题: getByLogicTableCode</p>
     * <p>描述: 获取逻辑表的关联物理表</p>
     * @param  logicTableCode
     * @return List<LogicTableDefine>    返回类型   
     * @throws
     */
    @Query("from PhysicalTableDefine t where t.logicTableCode = ?1")
    public List<PhysicalTableDefine> getByLogicTableCode(String logicTableCode);

    /**
     * 批量修改逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    @Modifying
    @Query("update PhysicalTableDefine set logicTableCode=?2 WHERE logicTableCode=?1")
    public void batchUpdateLogicTableCode(String oldLogicTableCode, String newLogicTableCode);

    /**
     * qiujinwei 2014-12-25
     * <p>标题: getLTExcludingLG</p>
     * <p>描述: 根据物理表组ID和物理表对应逻辑表获取物理表ID</p>
     * @param  groupId
     * @param  logicTableCode
     * @return String    返回类型   
     * @throws
     */
    @Query(value="select t.id from t_xtpz_physical_table_define t " +
            " inner join t_xtpz_physical_group_relation g on t.id = g.table_id " +
            " where t.logic_table_code = ?1 and g.group_id = ?2", nativeQuery=true)
    public String getIdByGroupIdAndLogicTableCode(String logicTableCode, String groupId);

    public List<PhysicalTableDefine> getByTableTreeId(String tableTreeId);

    /**
     * qiujinwei 2015-08-18
     * <p>标题: getByLTCodeExcludeGrpRelt</p>
     * <p>描述: 获取未关联物理表组的逻辑表的关联物理表</p>
     * @param  logicTableCode
     * @return List<LogicTableDefine>    返回类型   
     * @throws
     */
    @Query("from PhysicalTableDefine t where t.logicTableCode = ?1 and t.id not in (select r.tableId from PhysicalGroupRelation r)")
    public List<PhysicalTableDefine> getByLTCodeExcludeGrpRelt(String logicTableCode);

}

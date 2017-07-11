package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.TableRelation;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface TableRelationDao extends StringIDDao<TableRelation>{

	@Query(value="select cd.id,cd.show_name,cd.column_name from t_xtpz_column_define cd where  cd.table_id=?", nativeQuery=true)
	public List<Object[]> getCheckDefineCoumn(String tableId);
	
	/**
	 * 源表所有字段          ---add
	 * @param tableId
	 * @return
	 */
	@Query(value="select t_cd.id, t_cd.show_name,t_cd.column_name from t_xtpz_column_define t_cd " +
			"where t_cd.table_id=?1", nativeQuery=true)
	public List<Object[]> getAddyTableColumn(String tableId);
	
	/**
	 * 查询所有表关系   --UPDATE
	 * @param tableId
	 * @return
	 */
	
	@Query(value="select rlt.id," +
	"org_t.show_name  as ot_show_name, org_t.table_name  as o_table_name, org_c.show_name  as oc_show_name, org_c.column_name  as o_column_name," +
	"dist_t.show_name as dt_show_name, dist_t.table_name as d_table_name, dist_c.show_name as dc_show_name, dist_c.column_name as d_column_name," +
	"rlt.relate_table_id,rlt.table_id, " +
	"org_t.table_prefix as opre, dist_t.table_prefix as tpre from " +
	"t_xtpz_table_relation rlt " +
	"join t_xtpz_physical_table_define org_t on (org_t.id = rlt.table_id) " +
	"join t_xtpz_column_define org_c on(org_c.table_id = ?1 and org_c.id = rlt.column_id) " +
	"join t_xtpz_physical_table_define dist_t on (rlt.relate_table_id = dist_t.id) " +
	"join t_xtpz_column_define dist_c on(rlt.relate_column_id = dist_c.id) " +
	"where rlt.table_id=?1 "+
	"union all "+
	"select rlt.id, " +
    "org_t.show_name  as ot_show_name, org_t.table_name  as o_table_name, org_c.show_name  as oc_show_name, org_c.column_name  as o_column_name," +
    "dist_t.show_name as dt_show_name, dist_t.table_name as d_table_name, dist_c.show_name as dc_show_name, dist_c.column_name as d_column_name," +
	"rlt.table_id, rlt.relate_table_id, "+
	"org_t.table_prefix as opre, dist_t.table_prefix as tpre from "+
	"t_xtpz_table_relation rlt "+
	"join t_xtpz_physical_table_define org_t on (org_t.id = rlt.relate_table_id ) "+ 
	"join t_xtpz_column_define org_c on(org_c.table_id = ?1 and org_c.id = rlt.relate_column_id) "+ 
	"join t_xtpz_physical_table_define dist_t on (rlt.table_id = dist_t.id) "+
	"join t_xtpz_column_define dist_c on(rlt.column_id = dist_c.id ) "+  
	"where rlt.relate_table_id=?1 " +
	"order by d_table_name", nativeQuery=true)
	public List<Object[]> getAllTableRelations(String tableId);
	
	
	/**
	 * 源表字段检索   --UPDATE
	 * @param tableId
	 * @return
	 */
	@Query(value="select t_cd.id, t_cd.show_name ,t_cd.column_name from t_xtpz_column_define t_cd "
				+"left join t_xtpz_table_relation t_ac on(t_ac.table_id=?1 and t_cd.id=t_ac.column_id)" 
				+"where t_cd.table_id=?1  and t_ac.table_id is null", nativeQuery=true)
	public List<Object[]> getyTableColumn(String tableId);
	
	
	/**
	 * 目标表字段检索  --UPDATE
	 * @param tableId
	 * @return
	 */
	@Query(value="select t_cd.id,  t_cd.show_name ,t_cd.column_name from t_xtpz_column_define t_cd "
		+"left join t_xtpz_table_relation t_ac on(t_ac.relate_table_id=?1 and t_cd.id=t_ac.column_id)" 
		+"where t_cd.table_id=?1  and t_ac.table_id is null", nativeQuery=true)
	public List<Object[]> getmbTableColumn(String tableId);
	
	/**
	 * 源表、目标表检索  --UPDATE
	 * @param tableId
	 * @return
	 */
	
	 /*@Query(value="select t_cd.id,  t_cd.show_name ,t_cd.column_name from t_xtpz_column_define t_cd "
		 		+" join t_xtpz_table_relation t_ac on(t_ac.table_id=?1 and t_cd.id=t_ac.column_id) "
		 		+" where t_cd.table_id=?1", nativeQuery=true)*/
	@Query(value="select rlt.id, "+
	        "org_c.show_name as yShowName, org_c.column_name as yColumName,dist_c.show_name, dist_c.column_name, "+
	        "rlt.column_id,rlt.relate_column_id  "+
	        "from t_xtpz_table_relation rlt "+
	        "join t_xtpz_physical_table_define org_t on (org_t.id = rlt.table_id) "+ 
	        "join t_xtpz_column_define org_c on(org_c.id = rlt.column_id) "+
	        "join t_xtpz_physical_table_define dist_t on (rlt.relate_table_id = dist_t.id) "+  
	        "join t_xtpz_column_define dist_c on(rlt.relate_column_id = dist_c.id) "+
	        "where rlt.table_id=?1 and rlt.relate_table_id=?2 "+
	        "union all "+
	        "select rlt.id, "+
	        "org_c.show_name as yShowName, org_c.column_name as yColumName,dist_c.show_name, dist_c.column_name, "+
	        "rlt.relate_column_id ,rlt.column_id "+
	        "from t_xtpz_table_relation rlt "+
	        "join t_xtpz_physical_table_define org_t on (org_t.id = rlt.relate_table_id) "+ 
	        "join t_xtpz_column_define org_c on(org_c.id = rlt.relate_column_id) "+
	        "join t_xtpz_physical_table_define dist_t on (rlt.table_id = dist_t.id) "+
	        "join t_xtpz_column_define dist_c on(rlt.column_id = dist_c.id) "+
	        "where rlt.table_id=?2 and rlt.relate_table_id=?1", nativeQuery=true)
     public List<Object[]> getRelationColumn(String tableId, String relTableId);
     
     /**
      * 删除关系表中已有的数据
      * @param tableId
      * @Query("delete TableRelation t_as where t_as.tableId=?1")
      */
     @Transactional
     @Modifying
     @Query("delete TableRelation t_as where t_as.id=?1")
     public void delymTableRelation(String Id);
     
     
     /**
      * 目标表下来选择表，只能选择非自身表
      * @param tableId
      */
     @Transactional
     @Modifying
     @Query(value="select t.id,t.show_name from t_xtpz_physical_table_define t where t.created = '1' " +
     		"and t.id<>?1 and t.id not in ( select t.relate_table_id from T_XTPZ_TABLE_RELATION t where t.table_id = ?1 union select t.table_id from T_XTPZ_TABLE_RELATION t where t.relate_table_id = ?1 ) ", nativeQuery=true)
     public List<Object[]> checkMbTable(String tableId);
     
     /**
      * 目标表下来选择表，只能选择非自身表，只能选择当前表组下的物理表
      * @param tableId
      */
     @Transactional
     @Modifying
     @Query(value="select t.id,t.show_name from t_xtpz_physical_table_define t where t.created = '1' " +
     		"and t.id<>?1 and t.id not in ( select t.relate_table_id from T_XTPZ_TABLE_RELATION t where t.table_id = ?1 union select t.table_id from T_XTPZ_TABLE_RELATION t where t.relate_table_id = ?1 ) " +
     		"and t.id in ( select g.table_id from T_XTPZ_PHYSICAL_GROUP_RELATION g where g.group_id=?2) ", nativeQuery=true)
     public List<Object[]> checkMbTable(String tableId, String gourpId);
     
     /**
 	 * 查询表关系数目
 	 * @param tableId
 	 * @return
 	 */	
 	@Query(value="select count(*) from t_xtpz_table_relation t where (t.table_id=?1 and t.relate_table_id=?2) "+
        " or (t.table_id=?2 and t.relate_table_id=?1)",nativeQuery=true)
 	public Object getTotalTableRelation(String tableId, String tableId2);
     
     /**
      * 删除关系表中已有的数据
      * @param tableId
      */
     @Transactional
     @Modifying
     @Query("delete TableRelation t_as where (t_as.relateTableId=?1 and t_as.tableId=?2) or (t_as.relateTableId=?2 and t_as.tableId=?1)")
     public void delTableRelationList(String rId, String tableId);
     
     /**
      * qiucs 2013-9-9 
      * <p>标题: findRelateTablesByTableId</p>
      * <p>描述: 获取指定表的所有关联表</p>
      * @param  tableId
      * @return List<Object[]>    返回类型   
      * @throws
      */
     @Query(value="select t.id, t.show_name from t_xtpz_physical_table_define t " +
     		" where exists(select r.id from t_xtpz_table_relation r where r.relate_table_id=t.id and r.table_id=?1) " +
     		" or exists(select r.id from t_xtpz_table_relation r where r.table_id=t.id and r.relate_table_id=?1) ", nativeQuery=true)
     public List<Object[]> findRelateTablesByTableId(String tableId);
     
     @Query(value=" select org_c.column_name as colum_name, dist_c.column_name as relate_column_name " +
             "from t_xtpz_table_relation rlt " +
             "join t_xtpz_column_define org_c on(org_c.table_id=?1 and org_c.id = rlt.column_id) " +
             "join t_xtpz_column_define dist_c on(dist_c.table_id=?2 and rlt.relate_column_id = dist_c.id) " +
             "where rlt.table_id=?1 and rlt.relate_table_id=?2 ", nativeQuery=true)
      public List<Object[]> getTableRelationColumns(String tableId, String relateTableId);

      
      /**
       *  根据表ID查询关联表的信息
       * @param tableId
       */
      @Query(value=" select t.id,t.show_name from t_xtpz_physical_table_define t where t.id in("+
    	      " select t.relate_table_id  from t_xtpz_table_relation t where t.table_id=?1"+
    	      " union  "+
    	      " select t.table_id from t_xtpz_table_relation t where t.relate_table_id=?1)",nativeQuery=true)
	public List<Object[]> queryRelationTableByTableID(String tableId);

	/** gmh 2013-10-21
     *  用于字段关联查询两表之间的关联字段
     * @param child_tid
     * @param parent_tid
     * */
	@Query(value=" select "+ 
			  		" t.column_id , t.relate_column_id "+ 
				" from "+ 
				       " T_XTPZ_TABLE_RELATION t "+ 
				" where "+ 
				       " t.table_id = ?1 "+
				" and "+ 
				       " t.relate_table_id = ?2 "+
				" union "+
				" select "+ 
				  " t.relate_column_id , t.column_id "+ 
				" from "+
				       " T_XTPZ_TABLE_RELATION t "+ 
				" where "+ 
				       " t.table_id = ?2 "+
				" and "+ 
				       " t.relate_table_id = ?1 ",nativeQuery=true)
	public List<Object[]> queryRelationCols(String child_tid, String parent_tid);
	
	/**
	 * qiucs 2013-10-23 
	 * <p>描述: 判断索引是否存在</p>
	 * @param  indexName
	 */
	@Query(value="SELECT COUNT(*) FROM USER_INDEXES T WHERE T.INDEX_NAME=UPPER(?1)", nativeQuery=true)
	public Object countOracleIndex(String indexName);
	
	@Query(value="drop index ?1", nativeQuery=true)
	public void dropIndex(String indexName);
    
    @Query(value="create index ?1 on ?2 (?3)", nativeQuery=true)
    public void createIndex(String indexName, String tableName, String columnNames);


    /**
     * 判断该表根据表关系判断，找出电子全文表
     * @param tableId
     * @return
     */
    @Query(value="select t.id, t.show_name, t.table_name from t_xtpz_physical_table_define t " +
     		" where  t.logic_table_code='" + ConstantVar.Labels.Document.CODE + "' and (" +
     				"exists(select r.id from t_xtpz_table_relation r where r.relate_table_id=t.id and r.table_id=?1) or " +
     		        "exists(select r.id from t_xtpz_table_relation r where r.table_id=t.id and r.relate_table_id=?1)" +
     		        ") ", nativeQuery=true)
     public List<Object[]> findDocumentTableByTableId(String tableId);
	
	
	/**
	 * 查询表关系
	 * 
	 * @param tableId 表ID
	 * @param columnId 表字段ID
	 * @param relateTableId 关联表ID
	 * @param relateColumnId 关联表字段ID
	 * @return TableRelation
	 */
     @Query("from TableRelation where tableId=? and columnId=? and relateTableId=? and relateColumnId=?")
	public TableRelation getTableRelation(String tableId, String columnId, String relateTableId, String relateColumnId);
	
    /**
     * 根据表ID获取表关系
     * 
     * @param tableId 表ID
     * @return List<TableRelation>
     */
    public List<TableRelation> findByTableId(String tableId);
    
    /**
     * 根据关联表ID获取表关系
     * 
     * @param relateTableId 关联表ID
     * @return List<TableRelation>
     */
    public List<TableRelation> findByRelateTableId(String relateTableId);
    
    /**
     * qiucs 2013-11-28 
     * <p>描述: 根据表ID删除表关系</p>
     * @param  tableId    设定参数   
     * @return void    返回类型   
     * @throws
     */
	@Modifying
	@Transactional
	@Query("delete from TableRelation where tableId=?1 or relateTableId=?1")
	public void deleteByTableId(String tableId);

    /**
     * qiucs 2013-11-28 
     * <p>描述: 根据字段ID删除表关系</p>
     * @param  tableId    设定参数   
     * @return void   
     * @throws
     */
    @Modifying
    @Transactional
    @Query("delete from TableRelation where columnId=?1 or relateColumnId=?1")
    public void deleteByColumnId(String columnId);
    
    /**
     * 获取所有建立了字段关系的表id
     * 
     * @return List<Object>
     */
    @Query("select tableId from TableRelation group by tableId")
    public List<String> getAllTableId();
	
}

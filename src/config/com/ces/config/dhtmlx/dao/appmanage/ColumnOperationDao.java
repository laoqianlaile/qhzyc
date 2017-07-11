package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.ColumnOperation;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ColumnOperationDao extends StringIDDao<ColumnOperation> {

	@Query(value = " select * from t_xtpz_table_define t where t.id=?1", nativeQuery = true)
	public String getOriginTableByName(String Id);
	
	

	@Query(value = " select t_cd.column_name  from  t_xtpz_column_define t_cd "
			+ " where t_cd.id=?1", nativeQuery = true)
	public String getOperationById(String id);
	
	
	
    @Query(value=" select * from t_xtpz_column_operation t  " +
    		"where t.table_id=?1", nativeQuery=true)
    public List<Object[]> getOperationBytableId(String tableId);
    
    
    
    @Query(value=" select * from t_xtpz_column_operation t  " +
    		"where t.origin_table_id=?1", nativeQuery=true)
    public List<Object[]> getOperationByOriginId(String originTableId);
    
    /**
     * 继承  为父表
     * @param originTableId
     * @return
     */
    @Query(value=" select * from t_xtpz_column_operation t  where t.origin_table_id=?1 " +
    		"and t.type='0'", nativeQuery=true)
    public List<Object[]> getOperationExtendByOriginTableId(String originTableId);
    
    
    /**
     * 继承  为子表
     * @param originTableId
     * @return
     */
    @Query(value=" select * from t_xtpz_column_operation t  where t.table_id=?1 " +
    		"and t.type='0'", nativeQuery=true)
    public List<Object[]> getOperationExtendBytableId(String originTableId);
    
    /**
     * 根据tableId 查找父表Id
     * @param tableId
     * @return
     */
    @Query(value=" select t.origin_table_id from t_xtpz_column_operation t where t.table_id=?1", nativeQuery=true)
    public List<Object[]> getTablefindOrgTableId(String tableId);
    
	/**
	 * 查询表关系
	 * @param tableId
	 * @return
	 */
	@Query(value="select rlt.id," +
			"  org_t.table_name as tName, org_c.show_name as showCName, org_c.column_name as cName," +
			"  dist_t.table_name, dist_c.show_name, dist_c.column_name,rlt.relate_table_id,rlt.table_id" +
			"  from t_xtpz_table_relation rlt " +
			"  join t_xtpz_table_define org_t on (org_t.id = rlt.table_id) " +
			"  join t_xtpz_column_define org_c on(org_c.id = rlt.column_id) " +
			"  join t_xtpz_table_define dist_t on (rlt.relate_table_id = dist_t.id) " +
			"  join t_xtpz_column_define dist_c on(rlt.relate_column_id = dist_c.id) " +
			"   where rlt.relate_table_id=?1", nativeQuery=true)
	public List<Object[]> getOriginTableRelation(String tableId);

	/**
	 * 查询表关系数目
	 * @param tableId
	 * @return
	 */	
	@Query(value="select count(*) from ColumnOperation T where (T.tableId =?1  and T.originTableId = ?2) "+
       " or (T.tableId = ?2 and T.originTableId = ?1)")
	public Long getTotalTableRelation(String tableId, String tableId2);
	
	/**
	 * qiucs 2013-10-18 
	 * <p>描述: tableId作为子表的所继承配置</p>
	 * @param  tableId
	 * @return List<ColumnOperation>    返回类型   
	 * @throws
	 */
	@Query("FROM ColumnOperation T WHERE T.type=?2 AND T.tableId=?1")
	public List<ColumnOperation> findByTableId(String tableId, String type);

	/**
     * <p>描述: 根据tableId获取继承、求和、最值</p>
     * @param tableId 表ID
     * @return List<ColumnOperation> 返回类型
     */
    public List<ColumnOperation> findByTableId(String tableId);
    
	/**
	 * qiucs 2013-10-18 
	 * <p>描述: tableId作为父表的所继承配置</p>
	 * @param  tableId
	 * @return List<ColumnOperation>    返回类型   
	 * @throws
	 */
    @Query("FROM ColumnOperation T WHERE T.type='0' AND T.originTableId=?1")
    public List<ColumnOperation> findByOriginTableId(String tableId);
    
    /**
     * qiucs 2013-10-22 
     * <p>描述: </p>
     * @param  columnRelationId
     */
    @Query("FROM ColumnOperation T WHERE T.columnRelationId = ?1")
    public ColumnOperation findByColumnRelationId(String columnRelationId);

    
    /**
     * 根据表ID获取该表下所有继承、求和、最值 （该方法供构件固化导出使用）
     * 
     * @param tableId 表ID
     * @return List<Object[]>
     */
    @Query(value="select co.id,co.column_relation_id,co.name,co.type,co.table_id,co.column_id," +
    		"td.table_name as origin_table_name,cd.column_name as origin_column_name,co.operator" +
    		" from t_xtpz_column_operation co, t_xtpz_physical_table_define td, t_xtpz_column_define cd" +
    		" where co.origin_table_id=td.id and co.origin_column_id=cd.id and co.table_id=?", nativeQuery=true)
    public List<Object[]> findForExport(String tableId);
    
    /**
     * qiucs 2013-11-28 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Modifying
    @Transactional
    @Query("delete from ColumnOperation where tableId=?1 or originTableId=?1")
    public void deleteByTableId(String tableId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnOperation set columnId=null where columnId=?1")
    public void updateByColumnId(String columnId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID更新配置</p>
     * @param  columnId    字段ID   
     */
    @Modifying
    @Transactional
    @Query("update ColumnOperation set originColumnId=null where originColumnId=?1")
    public void updateByOriginColumnId(String columnId);
}

package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.LogicTableRelation;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 逻辑表在逻辑表组中的表关系表dao层
 * 
 * @author qiujinwei
 */
public interface LogicTableRelationDao extends StringIDDao<LogicTableRelation> {
	
    /**
     * qiujinwei 2014-12-22
     * <p>标题: getRelationByCode</p>
     * <p>描述: 根据逻辑表组编码和源逻辑表编码获取表关系</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" select ltr.id, lt1.show_name as lt1_show_name, lt1.code as lt1_code, c1.show_name as c1_show_name, c1.column_name as c1_column_name, " +
    		" lt2.show_name as lt2_show_name, lt2.code as lt2_code, c2.show_name as c2_show_name, c2.column_name as c2_column_name, " +
    		" c1.id as c1_id, c2.id as c2_id " +
    		" from t_xtpz_logic_table_relation ltr " +
    		" inner join t_xtpz_logic_table_define lt1 on lt1.code = ltr.parent_table_code " +
    		" inner join t_xtpz_column_define c1 on c1.id = ltr.parent_column_id " +
    		" inner join t_xtpz_logic_table_define lt2 on lt2.code = ltr.table_code " +
    		" inner join t_xtpz_column_define c2 on c2.id = ltr.column_id" +
    		" where ltr.parent_table_code = ?1 and ltr.group_code = ?2", nativeQuery=true)
    public List<Object[]> getRelationByCode(String tableCode, String groupCode);
    
    /**
     * qiujinwei 2014-12-30
     * <p>标题: getRelationByCode</p>
     * <p>描述: 根据逻辑表组编码和目标逻辑表编码获取表关系</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value=" select ltr.id, lt1.show_name as lt1_show_name, lt1.code as lt1_code, c1.show_name as c1_show_name, c1.column_name as c1_column_name, " +
    		" lt2.show_name as lt2_show_name, lt2.code as lt2_code, c2.show_name as c2_show_name, c2.column_name as c2_column_name, " +
    		" c1.id as c1_id, c2.id as c2_id " +
    		" from t_xtpz_logic_table_relation ltr " +
    		" inner join t_xtpz_logic_table_define lt1 on lt1.code = ltr.parent_table_code " +
    		" inner join t_xtpz_column_define c1 on c1.id = ltr.parent_column_id " +
    		" inner join t_xtpz_logic_table_define lt2 on lt2.code = ltr.table_code " +
    		" inner join t_xtpz_column_define c2 on c2.id = ltr.column_id" +
    		" where ltr.table_code = ?1 and ltr.group_code = ?2", nativeQuery=true)
    public List<Object[]> getRelationByRelationCode(String tableCode, String groupCode);
    
    /**
     * 删除逻辑关系表中已有的数据
     * @param tableId
     */
    @Transactional
    @Modifying
    @Query("delete LogicTableRelation ltr where (ltr.tableCode=?1 and ltr.parentTableCode=?2 and ltr.groupCode=?3) or (ltr.parentTableCode=?2 and ltr.tableCode=?1 and ltr.groupCode=?3)")
    public void delTableRelationList(String rId, String tableId, String groupCode);
    
    /**
     * 查询逻辑表组的表关系
     * @param groupCode
     * @return
     */
    @Query("FROM LogicTableRelation WHERE groupCode=?1")
    public List<LogicTableRelation> getTableRelationsByGroupId(String groupCode);
    
    /**
     * 查询逻辑表组的表关系
     * @return LogicTableRelation
     */
    @Query("FROM LogicTableRelation WHERE groupCode=?1 and tableCode=?2 and columnId=?3 and parentTableCode=?4 and parentColumnId=?5")
    public LogicTableRelation getTableRelation(String groupCode, String tableCode, String columnId, String parentTableCode, String parentColumnId);     
    
    /**
     * 批量修改逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    @Modifying
    @Query("update LogicTableRelation set tableCode=?2 WHERE tableCode=?1")
    public void batchUpdateTableCode(String oldLogicTableCode, String newLogicTableCode);
    
    /**
     * 批量修改父逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    @Modifying
    @Query("update LogicTableRelation set parentTableCode=?2 WHERE parentTableCode=?1")
    public void batchUpdateParentTableCode(String oldLogicTableCode, String newLogicTableCode);
    
    /**
     * 批量修改逻辑表组编码
     * @param oldLogicGroupCode 旧的逻辑表组编码
     * @param newLogicGroupCode 新的逻辑表组编码
     */
    @Transactional
    @Modifying
    @Query("update LogicTableRelation set groupCode=?2 WHERE groupCode=?1")
    public void batchUpdateLogicGroupCode(String oldLogicGroupCode, String newLogicGroupCode);
    
    /**
     * <p>描述: 根据字段ID删除表关系</p>
     * @param  columnId    设定参数   
     * @return void   
     * @throws
     */
    @Modifying
    @Transactional
    @Query("delete from LogicTableRelation where columnId=?1 or parentColumnId=?1")
    public void deleteByColumnId(String columnId);
    
    /**
     * <p>描述: 根据字段ID查找表关系</p>
     * @param  columnId    设定参数   
     * @return LogicTableRelation   
     * @throws
     */
    @Query("from LogicTableRelation where columnId=?1 or parentColumnId=?1")
    public List<LogicTableRelation> getByColumnId(String columnId);
}

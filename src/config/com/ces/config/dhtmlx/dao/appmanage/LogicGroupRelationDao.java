package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.LogicGroupRelation;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 逻辑表组和逻辑表关系表dao层
 * 
 * @author qiujinwei
 */
public interface LogicGroupRelationDao extends StringIDDao<LogicGroupRelation> {
	
	/**
     * qiujinwei 2014-11-20 
     * <p>标题: getMaxShowOrder</p>
     * <p>描述: 获取最大显示顺序</p>
     * @param  null
     * @return Integer    返回类型   
     * @throws
     */
    @Query("SELECT MAX(LGR.showOrder) from LogicGroupRelation LGR WHERE LGR.groupCode=?1")
    public Integer getMaxShowOrder(String groupCode);
    
    /**
     * qiucs 2014-11-28 
     * <p>描述: 获取逻辑表组中的主逻辑表</p>
     * @param  groupCode
     * @return String    返回类型   
     */
    @Query("select t.tableCode from LogicGroupRelation t where t.groupCode=?1 and t.showOrder=1")
    public String getMainLogicGroupTableCode(String groupCode);
    
    /**
     * 根据逻辑表组编码获取逻辑表组下逻辑表的关联关系
     * 
     * @param groupCode 逻辑表组Code
     * @return List<LogicGroupRelation>
     */
    public List<LogicGroupRelation> getByGroupCode(String groupCode);
    /**
     * qiujinwei 2014-12-08
     * <p>标题: getShowOrderById</p>
     * <p>描述: 根据ID获取显示顺序</p>
     * @param  id
     * @return Integer    返回类型   
     * @throws
     */
    @Query("SELECT showOrder from LogicGroupRelation WHERE id=?1")
    public Integer getShowOrderById(String id);
    
    /**
     * qiujinwei 2014-12-08
     * <p>标题: updateShowOrderById</p>
     * <p>描述: 根据ID更新显示顺序</p>
     * @param  id
     * @param  increaseNum    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE LogicGroupRelation SET showOrder=(showOrder + ?2) WHERE id=?1")
    public void updateShowOrderById(String id, Integer increaseNum);
    
    /**
     * qiujinwei 2014-12-08
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
    @Query("UPDATE LogicGroupRelation SET showOrder = (showOrder + ?3) WHERE showOrder > ?1 AND showOrder < ?2 AND groupCode = ?4")
    public void batchUpdateShowOrder(Integer begin, Integer end, Integer increaseNum, String groupCode);
    
    /**
     * qiujinwei 2014-12-08
     * <p>标题: getRelationFromBeginToEnd</p>
     * <p>描述: 显示已更新(begin, end)开区间中的表</p>
     * @param  begin
     * @param  end    
     * @return List<LogicGroupRelation>    返回类型   
     * @throws
     */
    @Query("from LogicGroupRelation where showOrder > ?1 and showOrder < ?2 and groupCode = ?3")
    public List<LogicGroupRelation> getRelationFromBeginToEnd(Integer begin, Integer end, String groupCode);
    
    /**
     * qiujinwei 2014-12-08
     * <p>标题: getShowOrderByCode</p>
     * <p>描述: 根据表Code获取逻辑表在当前表组的showOrder</p>
     * @param  code    设定参数   
     * @return Integer    返回类型   
     * @throws
     */
    @Query("select LG.showOrder from LogicGroupRelation LG where LG.tableCode = ?1 and LG.groupCode = ?2")
    public Integer getShowOrderByCode(String tableCode, String groupCode);
    
    /**
     * 获取逻辑表组和逻辑表关系表
     * 
     * @param groupCode 逻辑表组编码
     * @param tableCode 逻辑表编码
     * @return LogicGroupRelation
     */
    public LogicGroupRelation getByGroupCodeAndTableCode(String groupCode, String tableCode);
    
    /**
     * 根据逻辑表组编码获取逻辑表组下的逻辑表信息
     * 
     * @param groupCode 逻辑表组编码
     * @return List<Object>
     */
    @Query(value="select LT.code, LT.show_name, LG.show_order from t_xtpz_logic_group_relation LG " +
    		"inner join t_xtpz_logic_table_define LT on LG.table_code=LT.code " +
    		"where LG.group_code=?1 order by LG.show_order", nativeQuery=true)
    public List<Object[]> getLogicTablesByGroupCode(String groupCode);
    
    /**
     * 根据逻辑表组编码和父逻辑表编码获取逻辑表下拉单信息
     * 
     * @param groupCode 逻辑表组编码
     * @param parentTableCode 父逻辑表编码
     * @return List<Object>
     */
    @Query(value=" select lt.code, lt.show_name from t_xtpz_logic_group_relation lgr " +
    		" inner join t_xtpz_logic_table_define lt on lgr.table_code = lt.code " +
    		" where lgr.group_code = ?1 and lgr.parent_table_code = ?2 and lgr.table_code not in " +
    		" (select table_code from t_xtpz_logic_table_relation where parent_table_code = ?2 and group_code = ?1)", nativeQuery=true)
    public List<Object[]> getComboOfLogicTable(String groupCode, String parentTableCode);
    
    /**
     * 批量修改逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    @Modifying
    @Query("update LogicGroupRelation set tableCode=?2 WHERE tableCode=?1")
    public void batchUpdateTableCode(String oldLogicTableCode, String newLogicTableCode);
    
    /**
     * 批量修改父逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    @Modifying
    @Query("update LogicGroupRelation set parentTableCode=?2 WHERE parentTableCode=?1")
    public void batchUpdateParentTableCode(String oldLogicTableCode, String newLogicTableCode);
    
    /**
     * 批量修改逻辑表组编码
     * @param oldLogicGroupCode 旧的逻辑表组编码
     * @param newLogicGroupCode 新的逻辑表组编码
     */
    @Transactional
    @Modifying
    @Query("update LogicGroupRelation set groupCode=?2 WHERE groupCode=?1")
    public void batchUpdateLogicGroupCode(String oldLogicGroupCode, String newLogicGroupCode);

}

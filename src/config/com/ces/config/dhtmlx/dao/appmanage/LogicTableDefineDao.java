package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 逻辑表定义表dao层
 * 
 * @author qiujinwei
 */
public interface LogicTableDefineDao extends StringIDDao<LogicTableDefine> {
	
	/**
	 * 根据编码获取逻辑表
	 * 
	 * @param code 编码
	 * @return LogicTableDefine
	 */
	public LogicTableDefine getByCode(String code);
    
    /**
     * qiujinwei 2014-11-20 
     * <p>标题: getShowOrderById</p>
     * <p>描述: 根据ID获取显示顺序</p>
     * @param  id
     * @return Integer    返回类型   
     * @throws
     */
    @Query("SELECT showOrder from LogicTableDefine WHERE id=?1")
    public Integer getShowOrderById(String id);
    
    /**
     * qiujinwei 2014-11-20 
     * <p>标题: updateShowOrderById</p>
     * <p>描述: 根据ID更新显示顺序</p>
     * @param  id
     * @param  increaseNum    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE LogicTableDefine SET showOrder=(showOrder + ?2) WHERE id=?1")
    public void updateShowOrderById(String id, Integer increaseNum);
    
    /**
     * qiujinwei 2014-11-04 
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
    @Query("UPDATE LogicTableDefine SET showOrder = (showOrder + ?3) WHERE showOrder > ?1 AND showOrder < ?2")
    public void batchUpdateShowOrder(Integer begin, Integer end, Integer increaseNum);
    
    /**
     * qiujinwei 2014-11-20 
     * <p>标题: getComboOfTables2TabelCopy</p>
     * <p>描述: 获取逻辑表下所有数据</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    @Query(value="SELECT T.CODE AS ID,T.SHOW_NAME AS TEXT FROM T_XTPZ_LOGIC_TABLE_DEFINE T",nativeQuery = true)
    public List<Object[]> getLogicTables();
    
    /**
     * qiujinwei 2014-11-20 
     * <p>标题: getMaxShowOrder</p>
     * <p>描述: 获取最大显示顺序</p>
     * @param  null
     * @return Integer    返回类型   
     * @throws
     */
    @Query("SELECT MAX(LT.showOrder) from LogicTableDefine LT")
    public Integer getMaxShowOrder();
    
    /**
     * qiujinwei 2014-11-28 
     * <p>标题: getLTExcludingLG</p>
     * <p>描述: 获取不在选中逻辑表组的逻辑表</p>
     * @param  groupCode
     * @return List<LogicTableDefine>    返回类型   
     * @throws
     */
    @Query("from LogicTableDefine t " + 
    		 		"where  not exists (select t2.id from LogicGroupRelation t2 where t2.tableCode = t.code and t2.groupCode = ?1)")
    public List<LogicTableDefine> getLTExcludingLG(String groupCode);
    
    /**
     * qiujinwei 2014-12-08 
     * <p>标题: getLTExcludingLG</p>
     * <p>描述: 获取获取对应逻辑表组下的父逻辑表选项</p>
     * @param  groupCode
     * @return List<LogicTableDefine>    返回类型   
     * @throws
     */
    @Query("from LogicTableDefine t " + 
    		 		"where exists (select t2.id from LogicGroupRelation t2 where t2.tableCode = t.code and t2.groupCode = ?1 and t2.showOrder < ?2)")
    public List<LogicTableDefine> getLTIncludingLG(String groupCode, int showOrder);
    
    /**
     * 根据逻辑表组Code获取其下的逻辑表
     * 
     * @param logicTableGroupCode 逻辑表组Code
     * @return List<LogicTableDefine>
     */
    @Query("select ltd from LogicTableDefine ltd, LogicGroupRelation lgr where ltd.code=lgr.tableCode and lgr.groupCode=? order by lgr.showOrder")
    public List<LogicTableDefine> getByLogicTableGroupCode(String logicTableGroupCode);
    
    /**
     * 根据逻辑表组Code和父逻辑表Code获取逻辑表
     * 
     * @param logicTableGroupCode 逻辑表组Code
     * @param parentTableCode 父逻辑表Code
     * @return List<LogicTableDefine>
     */
    @Query("select ltd from LogicTableDefine ltd, LogicGroupRelation lgr where ltd.code=lgr.tableCode and lgr.groupCode=? and lgr.parentTableCode=? order by lgr.showOrder")
    public List<LogicTableDefine> getByGroupIdAndParentTableCode(String logicTableGroupCode, String parentTableCode);
    
    /**
     * qiujinwei 2014-11-28 
     * <p>标题: getLTExcludingLG</p>
     * <p>描述: 获取物理表组关联逻辑表组的逻辑表</p>
     * @param  groupCode
     * @return List<LogicTableDefine>    返回类型   
     * @throws
     */
    @Query("from LogicTableDefine t " + 
    	   "where exists (select t2.id from LogicGroupRelation t2 where t2.tableCode = t.code and t2.groupCode = " + 
    	   "(select t3.logicGroupCode from PhysicalGroupDefine t3 where t3.id = ?1)) order by t.showOrder")
    public List<LogicTableDefine> getLogicTablesByPhysicalGroup(String groupId);

    @Query("from LogicTableDefine order by showOrder")
    public List<LogicTableDefine> getAllLogicTableDefine();
    
    /**
     * <p>标题: getTableInGroupRelation</p>
     * <p>描述: 判断逻辑表是否被逻辑表组应用</p>
     * @param  code    逻辑表编码  
     * @return List<LogicTableDefine>    返回类型   
     * @throws
     */
    @Query("from LogicTableDefine lt where exists(select 1 from LogicGroupRelation lgr where lgr.tableCode = ?1)")
    public List<LogicTableDefine> getTableInGroupRelation(String code);
    
    /**
     * qiujinwei 2015-04-22
     * <p>描述: 根据逻辑表组code获取第一张逻辑表(没有父逻辑表的)</p>
     * 
     * @return LogicTableDefine 返回类型
     * @throws
     */
    @Query("from LogicTableDefine lt where lt.code = (select lgr.tableCode from LogicGroupRelation lgr where lgr.groupCode = ?1 and showOrder = 1)")
    public LogicTableDefine getFirstByGroupCode(String groupCode);
}

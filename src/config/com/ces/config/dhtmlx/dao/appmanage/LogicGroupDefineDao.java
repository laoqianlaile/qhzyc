package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.LogicGroupDefine;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 逻辑表组定义表dao层
 * 
 * @author qiujinwei
 */
public interface LogicGroupDefineDao extends StringIDDao<LogicGroupDefine> {

	/**
     * qiujinwei 2014-11-14 
     * <p>标题: getShowOrderById</p>
     * <p>描述: 根据ID获取显示顺序</p>
     * @param  id
     * @return Integer    返回类型   
     * @throws
     */
    @Query("SELECT showOrder from LogicGroupDefine WHERE ID=?1")
    public Integer getShowOrderById(String id);
    
    /**
     * qiujinwei 2014-11-14 
     * <p>标题: updateShowOrderById</p>
     * <p>描述: 根据ID更新显示顺序</p>
     * @param  id
     * @param  increaseNum    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE LogicGroupDefine SET showOrder=(showOrder + ?2) WHERE id=?1")
    public void updateShowOrderById(String id, Integer increaseNum);
    
    /**
     * qiujinwei 2014-11-14 
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
    @Query("UPDATE LogicGroupDefine SET showOrder = (showOrder + ?3) WHERE showOrder > ?1 AND showOrder < ?2")
    public void batchUpdateShowOrder(Integer begin, Integer end, Integer increaseNum);
    
    /**
     * qiujinwei 2014-11-20 
     * <p>标题: getMaxShowOrder</p>
     * <p>描述: 获取最大显示顺序</p>
     * @param  null
     * @return Integer    返回类型   
     * @throws
     */
    @Query("SELECT MAX(LG.showOrder) from LogicGroupDefine LG")
    public Integer getMaxShowOrder();
    
    /**
     * <p>标题: getAllLogicGroupDefines</p>
     * <p>描述: 获取所有的逻辑表组</p>
     * @return List<LogicGroupDefine>    返回类型   
     */
    @Query("from LogicGroupDefine order by showOrder")
    public List<LogicGroupDefine> getAllLogicGroupDefines();
    
    /**
     * <p>标题: getByCode</p>
     * <p>描述: 根据编码获取逻辑表组</p>
     * @param code 逻辑表组编码
     * @return LogicGroupDefine    返回类型   
     */
    public LogicGroupDefine getByCode(String code);
    
    /**
     * <p>标题: getRelationInModule</p>
     * <p>描述: 判断逻辑表组是否被构件应用</p>
     * @param  code    逻辑表编码  
     * @return List<LogicTableDefine>    返回类型   
     * @throws
     */
    @Query("from LogicGroupDefine lg where exists(select 1 from Module m where m.logicTableGroupCode = ?1)")
    public List<LogicGroupDefine> getRelationInModule(String code);
	
}

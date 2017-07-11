package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupDefine;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 物理表组定义表dao层
 * 
 * @author qiujinwei
 */
public interface PhysicalGroupDefineDao extends StringIDDao<PhysicalGroupDefine> {
	
	/**
     * qiujinwei 2014-11-17 
     * <p>标题: getShowOrderById</p>
     * <p>描述: 根据ID获取显示顺序</p>
     * @param  id
     * @return Integer    返回类型   
     * @throws
     */
    @Query("SELECT showOrder from PhysicalGroupDefine WHERE id=?1")
    public Integer getShowOrderById(String id);
    
    /**
     * qiujinwei 2014-11-17 
     * <p>标题: updateShowOrderById</p>
     * <p>描述: 根据ID更新显示顺序</p>
     * @param  id
     * @param  increaseNum    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    @Modifying
    @Query("UPDATE PhysicalGroupDefine SET showOrder=(showOrder + ?2) WHERE id=?1")
    public void updateShowOrderById(String id, Integer increaseNum);
    
    /**
     * qiujinwei 2014-11-17 
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
    @Query("UPDATE PhysicalGroupDefine SET showOrder = (showOrder + ?3) WHERE showOrder > ?1 AND showOrder < ?2")
    public void batchUpdateShowOrder(Integer begin, Integer end, Integer increaseNum);
    
    /**
     * qiujinwei 2014-12-08 
     * <p>标题: getMaxShowOrder</p>
     * <p>描述: </p>
     * @return Object    返回类型   
     * @throws
     */
    @Query("SELECT MAX(PG.showOrder) FROM PhysicalGroupDefine PG")
    public Integer getMaxShowOrder();
    
    /**
     * 批量修改逻辑表组编码
     * @param oldLogicGroupCode 旧的逻辑表组编码
     * @param newLogicGroupCode 新的逻辑表组编码
     */
    @Transactional
    @Modifying
    @Query("update PhysicalGroupDefine set logicGroupCode=?2 WHERE logicGroupCode=?1")
    public void batchUpdateLogicGroupCode(String oldLogicGroupCode, String newLogicGroupCode);
    
    /**
     * 根据物理表ID获取物理表组ID
     * @param tableId 物理表ID
     * @return physicalTableDefine
     */
    @Query("select id from PhysicalGroupDefine where id=(select groupId from PhysicalGroupRelation where tableId=?1)")
    public String getByTableId(String tableId);

    /**
     * 根据逻辑表组code获取物理表组
     * 
     * @param logicGroupCode 物理表组所属逻辑表组CODE 
     * @return List<PhysicalGroupDefine>
     */
    public List<PhysicalGroupDefine> getByLogicGroupCode(String logicGroupCode);
}

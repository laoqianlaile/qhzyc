package com.ces.config.dhtmlx.dao.appmanage;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.WorkflowTree;

import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface WorkflowTreeDao extends StringIDDao<WorkflowTree>{
    
    /**
     * qiucs 2013-8-21 
     * <p>描述: 根据父节点获取最大显示顺序</p>
     */
    @Query("SELECT MAX(T.showOrder) FROM WorkflowTree T WHERE T.parentId=?1")
    public Integer getMaxShowOrderByParentId(String parentId);
    
    /**
     * qiucs 2013-8-22 
     * <p>描述: 根据ID获取显示顺序</p>
     * @param  id
     */
    @Query("SELECT showOrder from WorkflowTree WHERE ID=?1")
    public Integer getShowOrderById(String id);
    
    /**
     * qiucs 2013-8-22 
     * <p>描述: 根据ID更新显示顺序</p>
     * @param  id
     * @param  increaseNum    设定参数   
     */
    @Transactional
    @Modifying
    @Query("UPDATE WorkflowTree SET showOrder=(showOrder + ?2) WHERE id=?1")
    public void updateShowOrderById(String id, Integer increaseNum);
    
    /**
     * qiucs 2013-8-22 
     * <p>描述: 显示顺序批量更新(begin, end)开区间</p>
     * @param  parentId
     * @param  begin
     * @param  end    
     * @param  increaseNum    设定参数   
     */
    @Transactional
    @Modifying
    @Query("UPDATE WorkflowTree SET showOrder = (showOrder + ?4) WHERE parentId=?1 AND showOrder > ?2 AND showOrder < ?3")
    public void batchUpdateShowOrder(String parentId, Integer begin, Integer end, Integer increaseNum);
    

    /**
     * qiucs 2013-9-9 
     * <p>描述: 统计节点下子节点数</p>
     * @param  id
     */
    @Query("select count(id) from WorkflowTree where parentId=?1")
    public Long countByParentId(String parentId);
    
    /**
     * wl 2015-3-17 
     * <p>描述: 根据父ID和名称获取节点</p>
     * @param parentId
     * @param name
     * @return
     */
    public WorkflowTree getByParentIdAndName(String parentId, String name);
}

package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface WorkflowDefineDao extends StringIDDao<WorkflowDefine>{
    
    /**
     * qiucs 2013-8-21 
     * <p>描述: 根据父节点获取最大显示顺序</p>
     */
    @Query("SELECT MAX(T.showOrder) FROM WorkflowDefine T WHERE T.workflowTreeId=?1")
    public Integer getMaxShowOrderByWorkflowTreeId(String workflowTreeId);
    
    /**
     * qiucs 2013-8-22 
     * <p>描述: 根据ID获取显示顺序</p>
     * @param  id
     */
    @Query("SELECT showOrder from WorkflowDefine WHERE ID=?1")
    public Integer getShowOrderById(String id);
    
    /**
     * qiucs 2013-8-22 
     * <p>描述: 根据ID更新显示顺序</p>
     * @param  id
     * @param  increaseNum    设定参数   
     */
    @Transactional
    @Modifying
    @Query("UPDATE WorkflowDefine SET showOrder=(showOrder + ?2) WHERE id=?1")
    public void updateShowOrderById(String id, Integer increaseNum);
    
    /**
     * qiucs 2013-8-22 
     * <p>描述: 显示顺序批量更新(begin, end)开区间</p>
     * @param  workflowTreeId
     * @param  begin
     * @param  end    
     * @param  increaseNum    设定参数   
     */
    @Transactional
    @Modifying
    @Query("UPDATE WorkflowDefine SET showOrder = (showOrder + ?4) WHERE workflowTreeId=?1 AND showOrder > ?2 AND showOrder < ?3")
    public void batchUpdateShowOrder(String workflowTreeId, Integer begin, Integer end, Integer increaseNum);
    
    /**
     * qiucs 2013-8-22 
     * <p>描述: 获取节点下的所有子节点[ORACLE]</p>
     * @param  id
     */
    @Query(value="SELECT T.ID, T.PARENT_ID, T.TYPE, T.SHOW_ORDER, " +
    		" T.NAME, T.STARTED, T.REMARK FROM T_XTPZ_WORKFLOW_DEFINE T " +
    		" START WITH T.ID=?1 " +
    		" CONNECT BY PRIOR T.ID=T.PARENT_ID ", nativeQuery=true)
    public List<WorkflowDefine> getAllChildByIdOfOracle(String id);
    
    /**
     * qiucs 2013-8-22 
     * <p>描述: 获取节点下的所有子节点[SQLSERVER]</p>
     * @param  id
     */
    @Query(value="WITH RTU_1 AS (SELECT * FROM T_XTPZ_WORKFLOW_DEFINE ), " +
            " RTU_2 AS (SELECT * FROM RTU_1 WHERE ID=?1" +
            " UNION ALL " +
            " SELECT RTU_1.* FROM RTU_2 " +
            " INNER JOIN RTU_1 ON RTU_2.ID=RTU_1.PARANT_ID) " +
            " SELECT * FROM RTU_2 ", nativeQuery=true)
    public List<WorkflowDefine> getAllChildByIdOfSqlserver(String id);

    /**
     * qiucs 2013-9-9 
     * <p>描述: 统计节点下子节点数</p>
     * @param  id
     */
    @Query("select count(id) from WorkflowDefine where workflowTreeId=?1")
    public Long countByWorkflowTreeId(String workflowTreeId);
    
    /**
     * qiucs 2013-10-8 
     * <p>描述: 根据表ID查询工作流配置信息</p>
     * @param  tableId
     */
    @Query("FROM WorkflowDefine T WHERE T.businessTableId=?1")
    public List<WorkflowDefine> findWorkflowByTableId(String tableId);
    
    /**
     * qiucs 2013-10-8 
     * <p>描述: 根据工作流编码查询工作流配置信息</p>
     * @param  workflowCode
     */
    @Query("FROM WorkflowDefine T WHERE T.workflowCode=?1")
    public WorkflowDefine findByWorkflowCode(String workflowCode);
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: </p>
     * @param  tableId
     * @return List<String>    返回类型   
     * @throws
     */
    @Query("SELECT T.id FROM WorkflowDefine T WHERE T.businessTableId=?1")
    public List<String> findWorkflowIdsByTableId(String tableId);

    /**
     * wanglei 2013-11-25
     * <p>描述:根据表ID、包ID和流程ID获取相关的工作流</p>
     * @param tableId 表ID
     * @param packageId 包ID
     * @param processId 流程ID
     * @return Workflow 返回类型
     */
    @Query("from WorkflowDefine where tableId=? and packageId=? and processId=?")
    public WorkflowDefine getWorkflow(String tableId, String packageId, String processId);
    
    @Modifying
    @Transactional
    @Query("update WorkflowDefine set businessTableId=null where businessTableId=?1")
    public void updateByTableId(String tableId);
}

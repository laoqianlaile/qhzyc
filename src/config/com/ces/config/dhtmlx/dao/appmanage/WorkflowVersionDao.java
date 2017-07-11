package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.appmanage.WorkflowVersion;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface WorkflowVersionDao extends StringIDDao<WorkflowVersion> {

    /**
     * qiucs 2104-14-12-16
     * <P>描述：</p>
     */
    @Query("select max(t.showOrder) from WorkflowVersion t where t.workflowId=?1")
    public Integer getMaxShowOrder(String workflowId);

    /**
     * 获取工作流版本
     * 
     * @param workflowId 工作流定义ID
     * @return List<WorkflowVersion>
     */
    public List<WorkflowVersion> getByWorkflowId(String workflowId);
    
    /**
     * 获取工作流版本
     * 
     * @param workflowId 工作流定义ID
     * @return WorkflowVersion
     */
    public WorkflowVersion getByWorkflowIdAndVersion(String workflowId, String version);
}

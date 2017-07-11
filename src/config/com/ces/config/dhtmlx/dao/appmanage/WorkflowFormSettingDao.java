package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.WorkflowFormSetting;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface WorkflowFormSettingDao extends StringIDDao<WorkflowFormSetting> {
    
    /**
     * qiucs 2013-9-5 
     * <p>标题: deleteSetting</p>
     * <p>描述: 删除指定工作流中的一个流程节点的一个模块表单配置</p>
     * @return void    返回类型   
     */
    @Modifying
    @Transactional
    @Query("delete from WorkflowFormSetting where workflowVersionId=?1 and activityId=?2")
    public void deleteByFk(String workflowVersionId, String activityId);
    
    /**
     * qiucs 2013-10-14 
     * <p>描述: 获取工作流程节点的表单配置栏位信息</p>
     * @return List<String>    返回类型   
     */
    @Query("from WorkflowFormSetting t where t.workflowVersionId=?1 and t.activityId=?2")
    public List<WorkflowFormSetting> getFormSettingList(String workflowVersionId, String activityId);

    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据工作流ID删除配置</p>
     * @param  workflowVersionId
     */
    @Transactional
    @Modifying
    @Query("delete WorkflowFormSetting t where t.workflowVersionId=?1")
    public void deleteByWorkflowVersionId(String workflowVersionId);
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID删除配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete WorkflowFormSetting t where t.columnId=?1")
    public void deleteByColumnId(String columnId);
}

package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.WorkflowButtonSetting;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface WorkflowButtonSettingDao extends StringIDDao<WorkflowButtonSetting> {
    
    /**
     * qiucs 2014-12-23 下午6:01:40
     * <p>标题: deleteSetting</p>
     * <p>描述: 删除指定工作流中的一个流程节点的按钮配置</p>
     * @return void    返回类型   
     */
    @Modifying
    @Transactional
    @Query("delete from WorkflowButtonSetting where workflowVersionId=?1 and activityId=?2 and buttonType=?3")
    public void deleteByFk(String workflowVersionId, String activityId, String buttonType);
    
    /**
     * qiucs 2014-12-23 下午6:05:22
     * <p>描述: 获取工作流程节点按钮配置</p>
     * @return List<String>    返回类型   
     */
    @Query("select t.buttonCode from WorkflowButtonSetting t " +
    		"where t.workflowVersionId=?1 and t.activityId=?2 and t.buttonType=?3")
    public List<String> getHiddenButtons(String workflowVersionId, String activityId, String buttonType);

    /**
     * qiucs 2014-12-23 下午6:07:41
     * <p>描述: 根据工作流ID删除配置</p>
     * @param  workflowVersionId
     */
    @Transactional
    @Modifying
    @Query("delete WorkflowButtonSetting t where t.workflowVersionId=?1")
    public void deleteByWorkflowVersionId(String workflowVersionId);
    
    /**
     * qiucs 2014-12-23 下午6:11:36
     * <p>描述: 根据工作流节点删除配置</p>
     * @param  columnId    设定参数   
     */
    @Transactional
    @Modifying
    @Query("delete WorkflowButtonSetting t where t.workflowVersionId=?1 and t.activityId=?2")
    public void deleteByFk(String workflowVersionId, String activityId);
}

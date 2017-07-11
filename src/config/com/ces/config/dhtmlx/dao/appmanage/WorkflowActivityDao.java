package com.ces.config.dhtmlx.dao.appmanage;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.WorkflowActivity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface WorkflowActivityDao extends StringIDDao<WorkflowActivity> {

    /**
     * qiucs 2013-9-4 
     * <p>描述: 根据流程版本删除节点</p>
     * @throws
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM WorkflowActivity WHERE packageId=?1 and packageVersion=?2 and processId=?3")
    public void deleteByFk(String packageId, String packageVersion, String processId);
    
    /**
     * qiucs 2015-4-13 下午6:20:47
     * <p>描述: 根据流程删除节点 </p>
     * @return void
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM WorkflowActivity WHERE packageId=?1 and processId=?2")
    public void deleteByFk(String packageId, String processId);

    
    /**
     * qiucs 2014-9-23 
     * <p>描述: 获取开始流程节点</p>
     */
    @Query("from WorkflowActivity t WHERE t.packageId=?1 and t.packageVersion=?2 and t.processId=?3 and t.activityType='START'")
    public WorkflowActivity getStartActivity(String packageId, String packageVersion, String processId);
    
}

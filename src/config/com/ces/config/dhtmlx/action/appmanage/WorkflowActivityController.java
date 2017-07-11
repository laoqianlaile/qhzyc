package com.ces.config.dhtmlx.action.appmanage;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowActivityDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowActivity;
import com.ces.config.dhtmlx.service.appmanage.WorkflowActivityService;

public class WorkflowActivityController extends ConfigDefineServiceDaoController<WorkflowActivity, WorkflowActivityService, WorkflowActivityDao>{

    private static final long serialVersionUID = -8127081313555549480L;
    
    /*
     * (非 Javadoc)   
     * <p>标题: setService</p>   
     * <p>描述: 注入服务层(Service)</p>   
     * @param service   
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("workflowActivityService")
    protected void setService(WorkflowActivityService service) {
        super.setService(service);
    }

    @Override
    protected void initModel() {
        setModel(new WorkflowActivity());
    }
    
    /**
     * qiucs 2013-9-3 
     * <p>标题: cfActivities</p>
     * <p>描述: 指定工作流的流程节点数据</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object cfActivities() {
        String workflowId = getParameter("Q_workflowId");
        try {
            //setReturnData(getService().getActivities(workflowId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2013-8-23 
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整顺序</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object adjustShowOrder() {
        String workflowId = getParameter("P_workflowId");
        String sourceIds  = getParameter("P_sourceIds");
        String targetId   = getParameter("P_targetId");
        //getService().adjustShowOrder(workflowId, sourceIds, targetId);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
}

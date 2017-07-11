package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowButtonSettingDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowButtonSetting;
import com.ces.config.dhtmlx.service.appmanage.WorkflowButtonSettingService;

public class WorkflowButtonSettingController extends ConfigDefineServiceDaoController<WorkflowButtonSetting, WorkflowButtonSettingService, WorkflowButtonSettingDao> {

    private static final long serialVersionUID = -8629061728666590589L;
    
    private static Log log = LogFactory.getLog(WorkflowButtonSettingController.class);

    @Override
    protected void initModel() {
        setModel(new WorkflowButtonSetting());
    }
    
    /**
     * qiucs 2014-12-23 下午6:09:49
     * <p>描述: 保存按钮个性化配置 </p>
     * @return Object
     */
    public Object save() {
        try {
            String workflowVersionId = getParameter("P_workflowVersionId");
            String activityId = getParameter("P_activityId");
            String buttonType = getParameter("P_buttonType");
            String rowsValue  = getParameter("P_rowsValue");
            setReturnData(getService().save(workflowVersionId, activityId, buttonType, rowsValue));
        } catch (Exception e) {
            log.error("保存按钮个性化配置出错", e);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2014-12-23 下午6:09:05
     * <p>描述: 获取工作流不显示的按钮 </p>
     * @return Object
     */
    public Object hiddenButtons() {
        try {
            String workflowVersionId = getParameter("P_workflowVersionId");
            String activityId = getParameter("P_activityId");
            String buttonType = getParameter("P_buttonType");
            setReturnData(getService().getHiddenButtons(workflowVersionId, activityId, buttonType));
        } catch (Exception e) {
        	log.error("获取工作流不显示的按钮出错", e);
        }
        
        return NONE;
    }

}

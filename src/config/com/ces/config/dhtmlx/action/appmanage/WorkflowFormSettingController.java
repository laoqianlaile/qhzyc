package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowFormSettingDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowFormSetting;
import com.ces.config.dhtmlx.service.appmanage.WorkflowFormSettingService;

public class WorkflowFormSettingController extends ConfigDefineServiceDaoController<WorkflowFormSetting, WorkflowFormSettingService, WorkflowFormSettingDao> {

    private static final long serialVersionUID = -8629061728666590589L;
    
    private static Log log = LogFactory.getLog(WorkflowFormSettingController.class);

    @Override
    protected void initModel() {
        setModel(new WorkflowFormSetting());
    }
    
    /**
     * qiucs 2013-9-5 
     * <p>描述: 保存工作流表单个性化设置</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object save() {
        try {
            String workflowVersionId = getParameter("P_workflowVersionId");
            String activityId = getParameter("P_activityId");
            String rowsValue  = getParameter("P_rowsValue");
            setReturnData(getService().save(workflowVersionId, activityId, rowsValue));
        } catch (Exception e) {
            log.error("保存工作流表单个性化设置出错", e);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2013-10-14 
     * <p>描述: 获取工作流程节点的表单配置栏位信息</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object formSetting() {
        try {
            String workflowVersionId = getParameter("P_workflowVersionId");
            String activityId = getParameter("P_activityId");
            setReturnData(getService().getFormSettingMap(workflowVersionId, activityId));
        } catch (Exception e) {
        	log.error("获取被置灰的表单项出错", e);
        }
        
        return NONE;
    }

}

/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2014-12-23 下午10:17:10
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.config.dhtmlx.action.appmanage;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import com.ces.config.dhtmlx.action.base.ConfigDefineServiceController;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowAssistOpinion;
import com.ces.config.dhtmlx.service.appmanage.WorkflowAssistOpinionService;

public class WorkflowAssistOpinionController extends ConfigDefineServiceController<WorkflowAssistOpinion, WorkflowAssistOpinionService> {

	private static final long serialVersionUID = 2114363239835390604L;
	
	private static Log log = LogFactory.getLog(WorkflowAssistOpinionController.class);
	
	@Override
    @Autowired
    @Qualifier("workflowAssistOpinionService")
    protected void setService(WorkflowAssistOpinionService service) {
        super.setService(service);
    }
	
	public void initModel() {
		setModel(new WorkflowAssistOpinion());
	}
	
	/**
	 * qiucs 2014-12-23 下午10:20:50
	 * <p>描述: 保存辅助意见 </p>
	 * @return Object
	 */
	public Object save() {
		try {
			getService().save(model);
		} catch (Exception e) {
			log.error("保存辅助意见出错", e);
		}
		return NONE;
	}
	
	/**
	 * qiucs 2014-12-24 上午10:20:26
	 * <p>描述: 打开辅助意见 </p>
	 * @return Object
	 */
	public Object edit() {
		try {
			String workflowVersionId = getParameter("P_workflowVersionId");
			model = getService().findById(workflowVersionId, getId());
		} catch (Exception e) {
			log.error("打开辅助意见出错", e);
		}
		return NONE;
	}
	
	/**
	 * qiucs 2014-12-23 下午11:12:36
	 * <p>描述: 删除辅助意见 </p>
	 * @return Object
	 */
	public Object destroy() {
		try {
			String workflowId = getParameter("P_workflowId");
			setReturnData(getService().delete(workflowId, getId()));
		} catch (Exception e) {
			log.error("删除辅助意见出错", e);
		}
		
		return NONE;
	}
	
	/**
	 * qiucs 2014-12-23 下午11:11:58
	 * <p>描述: 查询辅助意见 </p>
	 * @return Object
	 */
	public Object search() {
		try {
			String workflowId = getParameter("P_workflowVersionId");
			String activityId = getParameter("P_activityId");
			setReturnData(getService().find(workflowId, activityId));
		} catch (Exception e) {
			log.error("查询辅助意见出错", e);
		}
		
		return NONE;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ces.config.action.base.StringIDConfigDefineServiceDaoController#combobox()
	 */
    public Object combobox()  {
        try {
        	String workflowId = getParameter("P_workflowId");
    		String workitemId = getParameter("P_workitemId");
    		String activityId = getParameter("P_activityId");
    		setReturnData(getService().combobox(workflowId, workitemId, activityId));
		} catch (Exception e) {
			log.error("获取辅助意见下拉框数据出错", e);
		}
		
		return NONE;
    }
	
	
}

/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-1-5 下午11:10:42
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowConfirmOpinionDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowConfirmOpinion;
import com.ces.config.dhtmlx.service.appmanage.WorkflowConfirmOpinionService;

public class WorkflowConfirmOpinionController extends
		ConfigDefineServiceDaoController<WorkflowConfirmOpinion, WorkflowConfirmOpinionService, WorkflowConfirmOpinionDao> {

	private static final long serialVersionUID = -4575630808110076301L;
	
	private static Log log = LogFactory.getLog(WorkflowConfirmOpinionController.class);
	
	public void initModel() {
		setModel(new WorkflowConfirmOpinion());
	}
	
	/**
	 * qiucs 2015-1-23 下午11:17:48
	 * <p>描述: 查询审批意见 </p>
	 * @return Object
	 */
	public Object search() {
		try {
			String workflowId = getParameter("P_workflowId");
			String dataId = getParameter("P_dataId");
			setReturnData(getService().findOpinion(workflowId, dataId));
		} catch (Exception e) {
			log.error("查询审批意见出错", e);
		}
		
		return NONE;
	}

}

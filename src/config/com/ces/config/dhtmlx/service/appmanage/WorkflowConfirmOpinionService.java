/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-1-5 下午11:09:10
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.config.dhtmlx.service.appmanage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.workflow.wapi.ClientAPI;
import ces.workflow.wapi.Coflow;
import ces.workflow.wapi.ProcessInstance;
import ces.workflow.wapi.WFException;
import ces.workflow.wapi.Workitem;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowConfirmOpinionDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowConfirmOpinion;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.config.utils.WorkflowUtil;

@Component
public class WorkflowConfirmOpinionService extends
		ConfigDefineDaoService<WorkflowConfirmOpinion, WorkflowConfirmOpinionDao> {
	
	
	/*
	 * (non-Javadoc)
	 * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
	 */
	@Transactional
	public WorkflowConfirmOpinion save(WorkflowConfirmOpinion entity) {
		
    	if (StringUtil.isEmpty(entity.getTableName())) return null;
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append("insert into ").append(entity.getTableName()).append("(")
    	  .append("id, data_id, process_instance_id, activity_id, workitem_id, user_id, user_name, confirm_time, confirm_result, opinion_text, opinion_type")
    	  .append(") values (")
    	  .append("'").append(entity.getId()).append("',")
    	  .append("'").append(entity.getDataId()).append("',")
    	  .append("'").append(entity.getProcessInstanceId()).append("',")
    	  .append("'").append(entity.getActivityId()).append("',")
    	  .append("'").append(entity.getWorkitemId()).append("',")
    	  .append("'").append(entity.getUserId()).append("',")
    	  .append("'").append(entity.getUserName()).append("',")
    	  .append("'").append(entity.getConfirmTime()).append("',")
    	  .append("'").append(entity.getConfirmResult()).append("',")
    	  .append("'").append(entity.getOpinionText()).append("',")
    	  .append("'").append(entity.getOpinionType()).append("'")
    	  .append(")");
    	DatabaseHandlerDao.getInstance().executeSql(sb.toString());
    	return entity;
	}
	
	/**
	 * qiucs 2015-1-6 下午4:22:36
	 * <p>描述: 保存审批意见（提交、退回、阅毕） </p>
	 * @return WorkflowConfirmOpinion
	 */
    @Transactional
    public WorkflowConfirmOpinion save(String workflowId, String dataId, Workitem wi, String opinion, String type) {
    	WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(workflowId);
    	if (!StringUtil.isBooleanTrue(flowDefine.getEnableConfirmTable())) {
    		return null;
    	}
    	String tableName = WorkflowUtil.getConfirmTableName(flowDefine.getWorkflowCode());
    	
    	String result = null;
    	
    	if (WorkflowConfirmOpinion.TYPE_COMPLETE.equals(type)) {
    		result = WorkflowConfirmOpinion.AGREE;
    		if (StringUtil.isNotEmpty(opinion)) {
    			result = opinion.substring(0, 1);
    			opinion= opinion.substring(1);
    		}
    	}
    	
    	WorkflowConfirmOpinion entity = newInstance(dataId, 
                String.valueOf(wi.getProcessInstanceId()), 
                String.valueOf(wi.getId()), 
                String.valueOf(wi.getActivityId()), 
                opinion, 
                type,
                result);
    	entity.setTableName(tableName);
    	
    	return save(entity);
    }
    
    /**
     * qiucs 2015-1-6 下午4:21:42
     * <p>描述: 创建一个实例 </p>
     * @return WorkflowConfirmOpinion
     */
    private WorkflowConfirmOpinion newInstance(String dataId, String proccessInstanceId, 
    		String workitemId, String activityId, String opinion, String type, String result) {
        Date time = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        WorkflowConfirmOpinion entity = new WorkflowConfirmOpinion();
        entity.setId(UUIDGenerator.uuid());
        entity.setDataId(dataId);
        entity.setProcessInstanceId(proccessInstanceId);
        entity.setWorkitemId(workitemId);
        entity.setActivityId(activityId);
        entity.setUserId(CommonUtil.getUser().getId());
        entity.setUserName(CommonUtil.getUser().getName());
        entity.setConfirmTime(formatter.format(time));
        entity.setOpinionText(opinion);
        entity.setOpinionType(type);
        entity.setConfirmResult(result);
        return entity;
    }

	/**
	 * qiucs 2015-1-5 下午11:15:01
	 * <p>描述: 查询审批意见 </p>
	 * @return Object
	 */
	public String findOpinion(String workflowId, String dataId) {
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(workflowId);
		String tableName = WorkflowUtil.getConfirmTableName(flowDefine.getWorkflowCode());
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("select t.user_name, t.confirm_time, t.opinion_text, t.opinion_type, t.confirm_result from ")
		  .append(tableName).append(" t ")
		  .append("where t.data_id = '").append(dataId)
		  .append("' order by t.confirm_time");
		
		List<Object[]> objList = DatabaseHandlerDao.getInstance().queryForList(String.valueOf(sb));

        final String pre  = "　";
        final String half = "　";
        StringBuilder opinions = new StringBuilder();
        String userName, confirmTime, opinionText, opinionType, confirmResult;
        Object[] objArr;
        int len = objList.size();
        if (len > 0) {
        	opinions.append("<div style=\"float:left;width:87.5%;\">");
        }
		for (int i = 0; i < len; i++) {
			objArr = objList.get(i);
			userName = StringUtil.null2empty(objArr[0]);
			confirmTime = StringUtil.null2empty(objArr[1]);
			opinionText = StringUtil.null2empty(objArr[2]);
			opinionType = StringUtil.null2empty(objArr[3]);
			confirmResult= getConfirmResultText(objArr[4]);
			if(WorkflowConfirmOpinion.TYPE_UNTREAD.equals(opinionType)) {
				opinionText = opinionText + "（<font color='red'>退回</font>）";
        	} else if (WorkflowConfirmOpinion.TYPE_HASREAD.equals(opinionType)) {
        		opinionText = opinionText + "（<font color='green'>阅毕</font>）";
            } else {
            	opinionText = confirmResult + (StringUtil.isEmpty(opinionText) ? "" : "，" + opinionText);
        	}
			opinions.append("<div class=\"app-confirm-opinion-item-text\">")
			        .append(opinionText)
			        .append("</div>")
			        .append("<div class=\"app-confirm-opinion-item-sign\">")
			        .append(userName)//.append(half).append(confirmTime.substring(0, 10))
			        .append("</div>")
			        .append("<div class=\"app-confirm-opinion-item-sign\">")
			        .append(confirmTime.substring(0, 10))
			        .append("</div>")
			        .append("<hr class=\"app-confirm-opinion-item-split\"/>");
			
			/*if(WorkflowConfirmOpinion.TYPE_UNTREAD.equals(opinionType)) {
                opinions.append(pre).append(confirmTime.substring(0, 10)).append(half).append(userName).append("（<font color='red'>退回</font>）：").append(opinionText).append("<br/>");
        	} else if (WorkflowConfirmOpinion.TYPE_HASREAD.equals(opinionType)) {
                opinions.append(pre).append(confirmTime.substring(0, 10)).append(half).append(userName).append("（<font color='green'>阅毕</font>）：").append(opinionText).append("<br/>");
            } else {
                opinions.append(pre).append(confirmTime.substring(0, 10)).append(half).append(userName).append("：").append(opinionText).append("<br/>");
        	}*/
		}
		if (len > 0) opinions.append("</div>");
		
		return String.valueOf(opinions);
	}
	/**
	 * qiucs 2015-1-7 下午8:22:19
	 * <p>描述: 审批结果转换为中文 </p>
	 * @return String
	 */
	private String getConfirmResultText(Object obj) {
		//if ("1".equals(obj)) return "同意";
		if ("0".equals(String.valueOf(obj))) return "不同意";
		return "同意";
	}
	
	/**
	 * qiucs 2015-7-19 下午2:56:42
	 * <p>描述: 判断审批意见表中是否存在不同意 </p>
	 * @return boolean
	 */
	private boolean existDisagree(String confirmTableName, long processInstanceId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("select count(ID) from ").append(confirmTableName)
		  .append(" t where t.process_instance_id='").append(processInstanceId).append("' and t.opinion_type='1' and t.confirm_result='0' ");
		
		Object cnt = DatabaseHandlerDao.getInstance().queryForObject(sb.toString());		
		
		return Integer.parseInt(cnt.toString()) > 0;
	}
    
	/**
	 * qiucs 2015-7-19 下午2:27:02
	 * <p>描述: 判断审批意见表中是否存在不同意 </p>
	 * @return MessageModel
	 *                 -- {success: true,  status: 0}  表示当前流程存在不同意见
	 *                 -- {success: false, status: -1} 表示当前流程未配置审批意见表
	 *                 -- {success: false, status: 0}  表示当前流程不存在不同意见
	 * @throws WFException 
	 */
    public MessageModel existDisagree(long processInstanceId) throws WFException {    	
    	
    	ClientAPI client = Coflow.getClientAPI(CommonUtil.getCurrentUserId());    	
    	ProcessInstance pi = client.getProcessInstance(processInstanceId);    	
    	String packageId = pi.getPackageId();    	
    	String workflowCode = WorkflowUtil.getWorkflowCodeByPackageId(packageId);    	
    	String confirmTableName = WorkflowUtil.getConfirmTableName(workflowCode);    	
    	if (!DatabaseHandlerDao.getInstance().tableExists(confirmTableName)) {
    		return MessageModel.falseInstance(-1, "流程未配置审批意见表！");
    	}
    	
    	return MessageModel.newInstance(existDisagree(confirmTableName, processInstanceId), "OK");
    }

}

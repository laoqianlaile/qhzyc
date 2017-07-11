/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2014-12-23 下午10:15:55
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.workflow.wapi.ClientAPI;
import ces.workflow.wapi.Coflow;
import ces.workflow.wapi.WFException;
import ces.workflow.wapi.Workitem;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowAssistOpinion;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowVersion;
import com.ces.config.dhtmlx.service.base.ConfigService;
import com.ces.config.jdbc.core.RowArrayHandler;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.config.utils.WorkflowUtil;
import com.ces.xarch.core.security.entity.SysUser;

@Component
public class WorkflowAssistOpinionService extends ConfigService<WorkflowAssistOpinion> {

	/* (non-Javadoc)
	 * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
	 */
	@Transactional
	public WorkflowAssistOpinion save(WorkflowAssistOpinion entity) {
		String workflowVersionId = entity.getWorkflowVersionId();
		WorkflowVersion version = getService(WorkflowVersionService.class).getByID(workflowVersionId);
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(version.getWorkflowId());
		return save(entity, flowDefine.getId());
	}
	
	/**
	 * qiucs 2014-12-23 下午11:05:56
	 * <p>描述: 新增辅助意见 </p>
	 * @return WorkflowAssistOpinion
	 */
	@Transactional
	private WorkflowAssistOpinion insert(String tableName, WorkflowAssistOpinion entity) {
		StringBuffer sb = new StringBuffer();
		entity.setId(UUIDGenerator.uuid());
		sb.append("insert into ").append(tableName).append(" (")
		      .append("id, workflow_version_id, activity_id, show_order, opinion_text, remark")
		  .append(") values (")
		      .append("'").append(entity.getId()).append("',")
		      .append("'").append(entity.getWorkflowVersionId()).append("',")
		      .append("'").append(entity.getActivityId()).append("',")
		      .append("'").append(entity.getShowOrder()).append("',")
		      .append("'").append(entity.getOpinionText()).append("',")
		      .append("'").append(entity.getRemark()).append("'")
		  .append(")");
		DatabaseHandlerDao.getInstance().executeSql(String.valueOf(sb));
		return entity;
	}
	
	/**
	 * qiucs 2014-12-23 下午11:05:32
	 * <p>描述: 更新辅助意见 </p>
	 * @return WorkflowAssistOpinion
	 */
	@Transactional
	private WorkflowAssistOpinion update(String tableName, WorkflowAssistOpinion entity) {
		StringBuffer sb = new StringBuffer();
		sb.append("update ").append(tableName)
		  .append(" set ")
		      .append("workflow_version_id = '").append(entity.getWorkflowVersionId()).append("',")
		      .append("activity_id = '").append(entity.getActivityId()).append("',")
		      .append("show_order = '").append(entity.getShowOrder()).append("',")
		      .append("opinion_text = '").append(entity.getOpinionText()).append("',")
		      .append("remark = '").append(entity.getRemark()).append("'")
		  .append(" where ")
		  .append("id = '").append(entity.getId()).append("',");
		DatabaseHandlerDao.getInstance().executeSql(String.valueOf(sb));
		return entity;
	}
	
	/**
	 * qiucs 2014-12-24 上午10:16:22
	 * <p>描述: 根据ID查询辅助意见 </p>
	 * @return WorkflowAssistOpinion
	 */
	public WorkflowAssistOpinion findById(String workflowVersionId, String id) {
		WorkflowAssistOpinion entity = new WorkflowAssistOpinion();
		WorkflowVersion flowVersion = getService(WorkflowVersionService.class).getByID(workflowVersionId);
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(flowVersion.getWorkflowId());
		String tableName = WorkflowUtil.getAssistTableName(flowDefine.getWorkflowCode());
		StringBuffer sb = new StringBuffer();
		
		sb.append("select t.id, t.workflow_version_id, t.activity_id, t.show_order, t.opinion_text, t.remark from ")
		  .append(tableName).append(" t ")
		  .append("where t.id = '").append(id).append("'");
		
		Object[] objArr = (Object[])DatabaseHandlerDao.getInstance().queryForObject(String.valueOf(sb));
			
		entity.setId(String.valueOf(objArr[0]));
		entity.setWorkflowVersionId(String.valueOf(objArr[1]));
		entity.setActivityId(String.valueOf(objArr[2]));
		entity.setShowOrder(Integer.parseInt(StringUtil.null2empty(objArr[3])));
		entity.setOpinionText(String.valueOf(objArr[4]));
		entity.setRemark(StringUtil.null2empty(objArr[5]));
		
		return entity;
	}
	
	public List<WorkflowAssistOpinion> findByWorkflowVersionId(String workflowVersionId) {
		List<WorkflowAssistOpinion> list = new ArrayList<WorkflowAssistOpinion>();
		
		WorkflowVersion version = getService(WorkflowVersionService.class).getByID(workflowVersionId);
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(version.getWorkflowId());
		String tableName = WorkflowUtil.getAssistTableName(flowDefine.getWorkflowCode());
		StringBuffer sb = new StringBuffer();
		
		sb.append("select t.id, t.activity_id, t.show_order, t.opinion_text, t.remark from ")
		  .append(tableName).append(" t ")
		  .append("where t.workflow_version_id = '").append(workflowVersionId).append("' order by t.show_order");
		
		List<Object[]> objList = DatabaseHandlerDao.getInstance().queryForList(String.valueOf(sb));
		
		for (Object[] objArr : objList) {
			WorkflowAssistOpinion item = new WorkflowAssistOpinion();
			
			item.setId(String.valueOf(objArr[0]));
			item.setWorkflowVersionId(workflowVersionId);
			item.setActivityId(String.valueOf(objArr[1]));
			item.setShowOrder(Integer.parseInt(StringUtil.null2empty(objArr[2])));
			item.setOpinionText(String.valueOf(objArr[3]));
			item.setRemark(StringUtil.null2empty(objArr[4]));
			
			list.add(item);
		}
		
		return list;
	}
	
	/**
	 * qiucs 2014-12-23 下午11:04:52
	 * <p>描述: 辅助意见查询 </p>
	 * @return List<WorkflowAssistOpinion>
	 */
	public List<WorkflowAssistOpinion> find(String workflowVersionId, String activityId) {
		List<WorkflowAssistOpinion> list = new ArrayList<WorkflowAssistOpinion>();

		WorkflowVersion version = getService(WorkflowVersionService.class).getByID(workflowVersionId);
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(version.getWorkflowId());
		String tableName = WorkflowUtil.getAssistTableName(flowDefine.getWorkflowCode());
		StringBuffer sb = new StringBuffer();
		
		sb.append("select t.id, t.activity_id, t.show_order, t.opinion_text, t.remark from ")
		  .append(tableName).append(" t ")
		  .append("where t.workflow_version_id = '").append(workflowVersionId)
		  .append("' and t.activity_id = '").append(activityId).append("' order by t.show_order");
		
		List<Object[]> objList = DatabaseHandlerDao.getInstance().queryForList(String.valueOf(sb));
		
		for (Object[] objArr : objList) {
			WorkflowAssistOpinion item = new WorkflowAssistOpinion();
			
			item.setId(String.valueOf(objArr[0]));
			item.setWorkflowVersionId(workflowVersionId);
			item.setActivityId(activityId);
			item.setShowOrder(Integer.parseInt(StringUtil.null2empty(objArr[2])));
			item.setOpinionText(String.valueOf(objArr[3]));
			item.setRemark(StringUtil.null2empty(objArr[4]));
			
			list.add(item);
		}
		
		return list;
	}
	
	/**
	 * qiucs 2014-12-23 下午11:04:18
	 * <p>描述: 获取最大显示顺序 </p>
	 * @return Integer
	 */
	private Integer getMaxShowOrder(String tableName, String activityId) {
		String sql = "select max(t.show_order) from " + tableName + " t where t.activity_id='" + activityId + "'";
		Object obj = DatabaseHandlerDao.getInstance().queryForObject(sql);
		return (null == obj ? null : Integer.parseInt(obj.toString()));
	}
	
	/**
	 * qiucs 2014-12-23 下午11:03:45
	 * <p>描述: 删除辅助意见 </p>
	 * @return MessageModel
	 */
	public MessageModel delete(String workflowVersionId, String id) {
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(workflowVersionId);
		String tableName = WorkflowUtil.getAssistTableName(flowDefine.getWorkflowCode());
		String sql = "delete from " + tableName + " t where t.id in('" + id.replace(",", "','") + "')";
		DatabaseHandlerDao.getInstance().executeSql(sql);
		return MessageModel.trueInstance("OK");
	}
	
	/**
	 * qiucs 2015-1-5 下午10:27:34
	 * <p>描述: 辅助意见下拉框数据 </p>
	 * @return Object
	 */
	public Object combobox(String workflowId, String workitemId, String activityId) throws NumberFormatException, WFException {
		SysUser user = CommonUtil.getUser();
        ClientAPI client = Coflow.getClientAPI(user.getId());
        Workitem wi = client.getWorkitem(Long.parseLong(workitemId));
        WorkflowVersion version = getService(WorkflowVersionService.class).findOne("EQ_workflowId=" + workflowId + ";EQ_version=" + wi.getPackageVersion());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        Map<String, Object> item = null;
		item = new HashMap<String, Object>();
		item.put("value", "");
		item.put("text", "请选择");
		list.add(item);
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(workflowId);
		String tableName = WorkflowUtil.getAssistTableName(flowDefine.getWorkflowCode());
		StringBuffer sb = new StringBuffer();
		
		sb.append("select t.id, t.opinion_text from ")
		  .append(tableName).append(" t ")
		  .append("where t.workflow_version_id = '").append(version.getId())
		  .append("' and t.activity_id = '").append(activityId).append("' order by t.show_order");
		
		List<Object[]> objList = DatabaseHandlerDao.getInstance().queryForList(String.valueOf(sb));
		
		for (Object[] objArr : objList) {
			item = new HashMap<String, Object>();
			item.put("value", objArr[0]);
			item.put("text", objArr[1]);
			list.add(item);
		}
		
		return list;
	}

	/**
	 * qiucs 2015-4-28 上午9:49:22
	 * <p>描述: 辅助审批意见（从一个版本复制到另一个版本） </p>
	 * @return void
	 */
	@Transactional
	public void copyWorkflow(String fromVersionId, final String toVersionId) {
		WorkflowVersion version = getService(WorkflowVersionService.class).getByID(fromVersionId);
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(version.getWorkflowId());
		if (!"1".equals(flowDefine.getEnableAssistTable())) return;
		String tableName = WorkflowUtil.getAssistTableName(flowDefine.getWorkflowCode());
		StringBuffer sb = new StringBuffer();
		
		sb.append("select t.id, t.activity_id, t.show_order, t.opinion_text, t.remark from ")
		  .append(tableName).append(" t ")
		  .append("where t.workflow_version_id = '").append(fromVersionId).append("' order by t.show_order");
		final String workflowId = flowDefine.getId();
		DatabaseHandlerDao.getInstance().jdbcQuery(String.valueOf(sb), new RowArrayHandler() {
			private WorkflowAssistOpinion entity;
			@Override
			@Transactional
			public void processRowData(Object[] objArr) {
				entity = new WorkflowAssistOpinion();
				entity.setWorkflowVersionId(toVersionId);
				entity.setActivityId(String.valueOf(objArr[1]));
				entity.setShowOrder(Integer.parseInt(StringUtil.null2empty(objArr[2])));
				entity.setOpinionText(String.valueOf(objArr[3]));
				entity.setRemark(StringUtil.null2empty(objArr[4]));
				save(entity, workflowId);
			}
		});
	}
	
	/**
	 * qiucs 2015-4-30 下午1:40:41
	 * <p>描述: 保存 </p>
	 * @return WorkflowAssistOpinion
	 */
	@Transactional
	public WorkflowAssistOpinion save(WorkflowAssistOpinion entity, String workflowId) {
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(workflowId);
		String tableName = WorkflowUtil.getAssistTableName(flowDefine.getWorkflowCode());
		if (StringUtil.isEmpty(entity.getId())) {
			Integer showOrder = getMaxShowOrder(tableName, entity.getActivityId());
			if (null == showOrder) showOrder = 0;
			entity.setShowOrder(++showOrder);
			insert(tableName, entity);
		} else {
			update(tableName, entity);
		}
			
		return entity;
	}
	
}

/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2014-12-23 下午10:11:58
 * <p>
 * 描述: 工作流辅助意见辅助类，没有固定的表名
 * </p>
 */
package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name="T_XTPZ_WORKFLOW_DEFINE")
public class WorkflowAssistOpinion extends StringIDEntity{

	private static final long serialVersionUID = 5396547996096919725L;
	
	private String workflowVersionId;
	
	private String activityId;
	
	private Integer showOrder;
	
	private String opinionText;
	
	private String remark;
	
	@Transient
	public String getWorkflowVersionId() {
		return workflowVersionId;
	}

	public void setWorkflowVersionId(String workflowVersionId) {
		this.workflowVersionId = workflowVersionId;
	}

	@Transient
	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	@Transient
	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	@Transient
	public String getOpinionText() {
		return opinionText;
	}

	public void setOpinionText(String opinionText) {
		this.opinionText = opinionText;
	}

	@Transient
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

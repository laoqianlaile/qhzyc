/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2014-12-23 下午5:19:23
 * <p>描述: 工作流待办箱按钮配置（表单与列表）</p>
 */
package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name="T_XTPZ_WORKFLOW_BUTTON_SETTING")
public class WorkflowButtonSetting extends StringIDEntity{

	private static final long serialVersionUID = -3261732728981651920L;

	private String workflowVersionId;
	
	private String activityId;
	
	private String buttonCode;
	
	private String buttonType;

	public String getWorkflowVersionId() {
		return workflowVersionId;
	}

	public void setWorkflowVersionId(String workflowVersionId) {
		this.workflowVersionId = workflowVersionId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getButtonCode() {
		return buttonCode;
	}

	public void setButtonCode(String buttonCode) {
		this.buttonCode = buttonCode;
	}

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}
}

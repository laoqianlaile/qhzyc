package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * <p>描述: 工作流定义中表单置灰项配置</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-12-23 上午16:39:53
 *
 */
@Entity
@Table(name="T_XTPZ_WORKFLOW_FORM_SETTING")
public class WorkflowFormSetting extends StringIDEntity {

    private static final long serialVersionUID = 3128985389578633134L;
    /** 工作流版本ID.**/
    private String workflowVersionId;
    /** 流程节点ID [Activity.id].**/
    private String activityId;
    /** 
     * (被置灰的表单项)表单字段ID 
     * [AppFormElement.columnId].
     **/
    private String columnId;

    private String disabled;
    
    private String defaultValue;
    
    
    public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

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

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }
}

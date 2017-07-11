/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-1-5 下午11:05:58
 * <p>
 * 描述: 工作流审批意见辅助类，没有固定的表名
 * </p>
 */
package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name="T_XTPZ_WORKFLOW_DEFINE")
public class WorkflowConfirmOpinion extends StringIDEntity {

    private static final long serialVersionUID = -228275362738719060L;
    // 退回
    public static final String TYPE_UNTREAD  = "-1";
    // 提交
    public static final String TYPE_COMPLETE = "1";
    // 阅毕
    public static final String TYPE_HASREAD  = "0";
    // 不同意
    public static final String DISAGREE = "0";
    // 同意
    public static final String AGREE = "1";
    /* 数据ID.*/
    private String dataId;
    /* 流程实例ID.*/
    private String processInstanceId;
    /* 工作项ID.*/
    private String workitemId;
    /* 节点ID.*/
    private String activityId;
    /* 用户ID.*/
    private String userId;
    /* 用户名称.*/
    private String userName;
    /* 时间.*/
    private String confirmTime;
    /* 意见.*/
    private String opinionText;
    /* 意见类型：1：审批意见  0-阅毕意见 -1：退回意见. */
    private String opinionType = TYPE_COMPLETE;
    /* 审批结果：1：同意  0-不同意. */
    private String confirmResult;
    
    @Transient
    public String getDataId() {
        return dataId;
    }
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
    @Transient
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    @Transient
    public String getWorkitemId() {
        return workitemId;
    }
    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }
    @Transient
    public String getActivityId() {
        return activityId;
    }
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
    @Transient
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Transient
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Transient
    public String getConfirmTime() {
        return confirmTime;
    }
    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }
    @Transient
    public String getOpinionText() {
        return opinionText;
    }
    public void setOpinionText(String opinionText) {
        this.opinionText = opinionText;
    }
    @Transient
	public String getOpinionType() {
		return opinionType;
	}
	public void setOpinionType(String opinionType) {
		this.opinionType = opinionType;
	}
    @Transient
	public String getConfirmResult() {
		return confirmResult;
	}
	public void setConfirmResult(String confirmResult) {
		this.confirmResult = confirmResult;
	}

	private String tableName;
    @Transient
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}

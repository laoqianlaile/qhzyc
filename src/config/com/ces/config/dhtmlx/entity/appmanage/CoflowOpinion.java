package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name="T_XTPZ_COFLOW_OPINION")
public class CoflowOpinion extends StringIDEntity {

    private static final long serialVersionUID = -228275362738719060L;
//    public static final String TYPE_RECALL   = "-1";
    // 退回
    public static final String TYPE_UNTREAD  = "-1";
    // 提交
    public static final String TYPE_COMPLETE = "1";
    // 阅毕
    public static final String TYPE_HASREAD  = "0";
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
    private String confirmOpinion;
    /* 意见类型：1：审批意见  0-阅毕意见 -1：退回意见. */
    private String type = "1";
    
    public String getDataId() {
        return dataId;
    }
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    public String getWorkitemId() {
        return workitemId;
    }
    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }
    public String getActivityId() {
        return activityId;
    }
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getConfirmTime() {
        return confirmTime;
    }
    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }
    public String getConfirmOpinion() {
        return confirmOpinion;
    }
    public void setConfirmOpinion(String confirmOpinion) {
        this.confirmOpinion = confirmOpinion;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

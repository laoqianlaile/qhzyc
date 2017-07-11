package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;
/**
 * <p>描述: 工作流节点信息</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-9-3 下午8:48:08
 */
@Entity
@Table(name="T_XTPZ_WORKFLOW_ACTIVITY")
public class WorkflowActivity extends StringIDEntity {

    private static final long serialVersionUID = -6315456293337484294L;
    
    public static final String START_ACTIVITY = "__0__";
    
    /** 工作流包ID**/
    private String packageId;
    /** 工作流版本号**/
    private String packageVersion;
    /** 工作流流程ID**/
    private String processId;
    /** 显示顺序**/
    private Integer showOrder;
    /** 流程节点ID**/
    private String activityId;
    /** 流程节点名称**/
    private String activityName;
    /** 流程节点类型：  START-开始节点  NORMAL-普通节点  AUTO-自动节点 FINISH-结束节点 **/
    private String activityType;
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getPackageVersion() {
		return packageVersion;
	}
	public void setPackageVersion(String packageVersion) {
		this.packageVersion = packageVersion;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public Integer getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
}

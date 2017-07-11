package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.coflow.web.util.Const;
import com.ces.config.utils.WorkflowUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_WORKFLOW_VERSION")
public class WorkflowVersion extends StringIDEntity {

    private static final long serialVersionUID = 1942962875166089136L;
    // 默认版本号（工作流定义时生成用的）
    public static String DEFAULT_VERSION = "1.0";
    // 工作流程ID
    private String workflowId;
    // 显示顺序
    private Integer showOrder;
    // 版本号（同一流程版本号唯一性）
    private String version;
    // 状态：UNDEFINED-未定义，LOCAL-本地，UNREGIST-未注册，RUNNING-运行中，UPDATED-已修改并运行，STOPPED-已停止，ERROR-出错
    private String status = Const.UNDEFINED;
    // 备注
    private String remark;

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Transient
    public String getStatusStr() {
    	return WorkflowUtil.getStatusText(status);
    }

}

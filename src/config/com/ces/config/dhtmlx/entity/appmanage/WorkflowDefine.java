package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;
/**
 * <p>描述: 工作流定义实体</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-8-20 下午11:12:43
 */
@Entity
@Table(name="T_XTPZ_WORKFLOW_DEFINE")
public class WorkflowDefine extends StringIDEntity{

    private static final long serialVersionUID = -2142266200631885367L;
    /** 工作流分类节点ID*/
    private String workflowTreeId;
    /** 
     * 工作流名称（作用：
     * 1. 作为流程流程文件中工作流包名
     * 2. 作为流程流程文件中流程名称）
     * */
    private String workflowName;
    /**
     * 工作流编码（唯一性，作用：如编码为BORROW时，
     * 1. 作为流程文件的文件名；
     * 2. 作为审批意见表和辅助意见表的表名组成部分，如：T_CF_CONFIRM_BORROW / T_CF_ASSIST_BORROW；
     * 3. 作为包ID和流程ID组成部分，如：包ID(PACKAGE_BORROW)、流程ID(PROCESS_ BORROW)）
     * */
    private String workflowCode;
    /** 工作流对应的业务*/
    private String businessTableId;
    /** 工作流对应的附件表*/
    private String enableDocumentTable;
    /** 是否有审批意见表：0-否，1-是*/
    private String enableConfirmTable;
    /** 是否有辅助意见表：0-否，1-是*/
    private String enableAssistTable;
    /** 工作箱JSON格式：{applyfor:申请箱;todo:待办箱;...}*/
    private String workflowBoxes;
    /** 显示顺序*/
    private Integer showOrder;
    /** 备注*/
    private String remark;

    public String getWorkflowTreeId() {
        return workflowTreeId;
    }

    public void setWorkflowTreeId(String workflowTreeId) {
        this.workflowTreeId = workflowTreeId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowCode() {
        return workflowCode;
    }

    public void setWorkflowCode(String workflowCode) {
        this.workflowCode = StringUtil.null2empty(workflowCode).toUpperCase();
    }

    public String getBusinessTableId() {
        return businessTableId;
    }

    public void setBusinessTableId(String businessTableId) {
        this.businessTableId = businessTableId;
    }

    public String getEnableDocumentTable() {
        return enableDocumentTable;
    }

    public void setEnableDocumentTable(String enableDocumentTable) {
        this.enableDocumentTable = enableDocumentTable;
    }

    public String getEnableConfirmTable() {
        return enableConfirmTable;
    }

    public void setEnableConfirmTable(String enableConfirmTable) {
        this.enableConfirmTable = enableConfirmTable;
    }

    public String getEnableAssistTable() {
        return enableAssistTable;
    }

    public void setEnableAssistTable(String enableAssistTable) {
        this.enableAssistTable = enableAssistTable;
    }

    public String getWorkflowBoxes() {
        return workflowBoxes;
    }

    public void setWorkflowBoxes(String workflowBoxes) {
        this.workflowBoxes = workflowBoxes;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**********************(工作流辅助属性)***********************/
    private String boxApplyfor;
    
    private String boxTodo;
    
    private String boxHasdone;
    
    private String boxComplete;
    
    private String boxToread;
    
    private String boxHasread;
    
    private String nameApplyfor;
    
    private String nameTodo;
    
    private String nameHasdone;
    
    private String nameComplete;
    
    private String nameToread;
    
    private String nameHasread;
    
    private String businessTableText;

    /**
     * JsonNode中的属性对应工作箱编码，值对应工作箱名称
     */
    private JsonNode node = null;

    /**
     * 把布局配置信息转成JsonNode
     * 
     * @return JsonNode
     */
    public JsonNode boxes2node() {
        String str = getWorkflowBoxes();
        if (StringUtil.isEmpty(str))
            return null;
        return JsonUtil.json2node(str);
    }

    @Transient
    public String getBoxApplyfor() {
    	if (StringUtil.isNotEmpty(boxApplyfor)) return boxApplyfor;
        return (hasJsonProp(WorkflowUtil.Box.applyfor) ? "1" : "0");
    }

    public void setBoxApplyfor(String boxApplyfor) {
        this.boxApplyfor = boxApplyfor;
    }

    @Transient
    public String getBoxTodo() {
        if (StringUtil.isNotEmpty(boxTodo)) return boxTodo;
        return (hasJsonProp(WorkflowUtil.Box.todo) ? "1" : "0");
    }

    public void setBoxTodo(String boxTodo) {
        this.boxTodo = boxTodo;
    }

    @Transient
    public String getBoxHasdone() {
        if (StringUtil.isNotEmpty(boxHasdone)) return boxHasdone;
        return (hasJsonProp(WorkflowUtil.Box.hasdone) ? "1" : "0");
    }

    public void setBoxHasdone(String boxHasdone) {
        this.boxHasdone = boxHasdone;
    }

    @Transient
    public String getBoxComplete() {
    	if (StringUtil.isNotEmpty(boxComplete)) return boxComplete;
        return (hasJsonProp(WorkflowUtil.Box.complete) ? "1" : "0");
    }

    public void setBoxComplete(String boxComplete) {
        this.boxComplete = boxComplete;
    }

    @Transient
    public String getBoxToread() {
    	if (StringUtil.isNotEmpty(boxToread)) return boxToread;
        return (hasJsonProp(WorkflowUtil.Box.toread) ? "1" : "0");
    }

    public void setBoxToread(String boxToread) {
        this.boxToread = boxToread;
    }

    @Transient
    public String getBoxHasread() {
    	if (StringUtil.isNotEmpty(boxHasread)) return boxHasread;
        return (hasJsonProp(WorkflowUtil.Box.hasread) ? "1" : "0");
    }

    public void setBoxHasread(String boxHasread) {
        this.boxHasread = boxHasread;
    }

    @Transient
    public String getNameApplyfor() {
        if (StringUtil.isNotEmpty(nameApplyfor)) {
    		return nameApplyfor;
    	}
    	return obtainJsonValue(WorkflowUtil.Box.applyfor);
    }

    public void setNameApplyfor(String nameApplyfor) {
        this.nameApplyfor = nameApplyfor;
    }

    @Transient
    public String getNameTodo() {
    	if (StringUtil.isNotEmpty(nameTodo)) {
    		return nameTodo;
    	}
    	return obtainJsonValue(WorkflowUtil.Box.todo);
    }

    public void setNameTodo(String nameTodo) {
        this.nameTodo = nameTodo;
    }

    @Transient
    public String getNameHasdone() {
        if (StringUtil.isNotEmpty(nameHasdone)) {
    		return nameHasdone;
    	}
    	return obtainJsonValue(WorkflowUtil.Box.hasdone);
    }

    public void setNameHasdone(String nameHasdone) {
        this.nameHasdone = nameHasdone;
    }

    @Transient
    public String getNameComplete() {
        if (StringUtil.isNotEmpty(nameComplete)) {
    		return nameComplete;
    	}
    	return obtainJsonValue(WorkflowUtil.Box.complete);
    }

    public void setNameComplete(String nameComplete) {
        this.nameComplete = nameComplete;
    }

    @Transient
    public String getNameToread() {
        if (StringUtil.isNotEmpty(nameToread)) {
    		return nameToread;
    	}
    	return obtainJsonValue(WorkflowUtil.Box.toread);
    }

    public void setNameToread(String nameToread) {
        this.nameToread = nameToread;
    }

    @Transient
    public String getNameHasread() {
    	if (StringUtil.isNotEmpty(nameHasread)) {
    		return nameHasread;
    	}
    	return obtainJsonValue(WorkflowUtil.Box.hasread);
    }

    public void setNameHasread(String nameHasread) {
        this.nameHasread = nameHasread;
    }

    @Transient
    public String getBusinessTableText() {
		return businessTableText;
	}

	public void setBusinessTableText(String businessTableText) {
		this.businessTableText = businessTableText;
	}

	/**
     * 获取JsonNode中对应属性值
     * 
     * @return String
     */
    private String obtainJsonValue(String prop) {
        if (null == node)
            node = boxes2node();
        if (null != node && hasJsonProp(prop)) {
            return node.get(prop).asText();
        }
        return "";
    }

    /**
     * 获取JsonNode中对应属性值
     * 
     * @return String
     */
    private boolean hasJsonProp(String prop) {
        if (null == node)
            node = boxes2node();
        if (null != node) {
            return node.has(prop);
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WorkflowDefine other = (WorkflowDefine) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}

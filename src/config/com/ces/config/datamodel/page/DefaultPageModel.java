package com.ces.config.datamodel.page;

/**
 * <p>描述: 自定义展现模型</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-11 下午7:09:26
 *
 */
public class DefaultPageModel {
    
    private String tableId;
    private String moduleId;
    private String menuId;
    private String componentVersionId;
    private String workflowId;
    private String box;
    
    private Object data;
    
    public void init() {}
    
    public String getTableId() {
        return tableId;
    }
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
    public String getModuleId() {
        return moduleId;
    }
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
    public String getComponentVersionId() {
        return componentVersionId;
    }
    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}

	public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}

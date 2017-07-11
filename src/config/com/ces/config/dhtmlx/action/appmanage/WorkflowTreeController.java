package com.ces.config.dhtmlx.action.appmanage;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowTreeDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowTree;
import com.ces.config.dhtmlx.service.appmanage.WorkflowTreeService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
import com.ces.xarch.core.exception.FatalException;

public class WorkflowTreeController extends ConfigDefineServiceDaoController<WorkflowTree, WorkflowTreeService, WorkflowTreeDao>{

    private static final long serialVersionUID = 3874214983831418535L;
    
    private static Log log = LogFactory.getLog(WorkflowTreeController.class);

    @Override
    protected void initModel() {
        setModel(new WorkflowTree());
    }

    @Override
    /*
     * (非 Javadoc)   
     * <p>标题: processTree</p>   
     * <p>描述: </p>   
     * @throws FatalException   
     * @see com.ces.config.action.base.StringIDConfigDefineServiceDaoController#processTree()
     */
    protected void processTree() throws FatalException {
        String id = getId();
        Object item = null;        
        if ("-1".equals(id) || id.startsWith("-CT")) {
            // get type and coflow nodes
        	item = getService().getTreeNode((id.length() > 3 ? id.substring(3) : id), "-CT");
        } else if (id.startsWith("-CF")) {
            // get version nodes
            item = getService(WorkflowVersionService.class).getTreeNode(id.substring(3), "-CV");
        } 
        setReturnData(toTreeNode(id, item));
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 封装为树节点JSON</p>
     * @return Object    返回类型   
     * @throws
     */
    private Object toTreeNode(String id, Object item) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", id);
        data.put("item", item);
        return data;
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 删除校验（判断是否有子节点）</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object checkDelete() {
    	try {
    		setReturnData(getService().checkDelete(getId()));
    	} catch (Exception e) {
    		log.error("删除校验（判断是否有子节点）出错", e);
    	}
    	 
    	return NONE;
    }
}
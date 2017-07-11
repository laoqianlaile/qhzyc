package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.LogicClassificationDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicClassification;
import com.ces.config.dhtmlx.service.appmanage.LogicClassificationService;

public class LogicClassificationController extends ConfigDefineServiceDaoController<LogicClassification, LogicClassificationService, LogicClassificationDao> {

private static final long serialVersionUID = -1L;
    
    private static Log log = LogFactory.getLog(LogicClassificationController.class);

    @Override
    protected void initModel() {
        setModel(new LogicClassification());
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 获取物理表分类节点</p>
     * @return Object    返回类型   
     */
    public Object treeNode() {
        try {
            setReturnData(getService().getTreeNode());
        } catch (Exception e) {
            log.error("获取物理表分类节点出错", e);
        }
        
        return NONE;
    }
	
}

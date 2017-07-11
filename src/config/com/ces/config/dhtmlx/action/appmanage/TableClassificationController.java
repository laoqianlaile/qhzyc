package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.TableClassificationDao;
import com.ces.config.dhtmlx.entity.appmanage.TableClassification;
import com.ces.config.dhtmlx.service.appmanage.TableClassificationService;

public class TableClassificationController extends ConfigDefineServiceDaoController<TableClassification, TableClassificationService, TableClassificationDao> {

    private static final long serialVersionUID = -2971113252269702900L;
    
    private static Log log = LogFactory.getLog(TableClassificationController.class);

    @Override
    protected void initModel() {
        setModel(new TableClassification());
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
    
    /**
     * qiujinwei 2015-08-24 
     * <p>描述: 获取物理表分类节点</p>
     * @return Object    返回类型   
     */
    public Object openedTreeNode() {
        try {
            setReturnData(getService().getOpenedTreeNode());
        } catch (Exception e) {
            log.error("获取物理表分类节点出错", e);
        }
        
        return NONE;
    }
    
    /**
     * qiujinwei 2014-12-30 
     * <p>描述: 获取视图分类节点</p>
     * @return Object    返回类型   
     */
    public Object treeNodeOfView() {
        try {
            setReturnData(getService().getTreeNodeOfView());
        } catch (Exception e) {
            log.error("获取视图分类节点出错", e);
        }
        
        return NONE;
    }

}

package com.ces.config.dhtmlx.action.appmanage;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.TableTreeDao;
import com.ces.config.dhtmlx.entity.appmanage.TableTree;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupRelationService;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupRelationService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.TableTreeService;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;

public class TableTreeController extends ConfigDefineServiceDaoController<TableTree, TableTreeService, TableTreeDao>{

    private static final long serialVersionUID = 8805326621099349572L;
    
    private static Log log = LogFactory.getLog(TableTreeController.class);

    @Override
    protected void initModel() {
        setModel(new TableTree());
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
        if (id.equals("-LG")) {
			item = getService(LogicGroupDefineService.class).getTreeNode("-LG", true);
		} else if (id.startsWith("-LG")) {
			item = getService(LogicGroupRelationService.class).getTreeNode(id.substring(3));
		} else if ("-PG".equals(id)) {
    		item = getService(PhysicalGroupDefineService.class).getTreeNode("-PG");
        } else if (id.startsWith("-PG")) {
        	item = getService(PhysicalGroupRelationService.class).getTreeNode(id.substring(3));
		} else if (id.startsWith("-V")) {
			item = getService(PhysicalTableDefineService.class).getViewTreeNode(id);
		} else {
            // 物理表
            item = getService().getTreeNode(buildSpecification(), id);
        }
        
        setReturnData(toTreeNode(id, item));
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 封闭为树节点JSON</p>
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
     * qiucs 2014-12-10 
     * <p>描述: 按逻辑表展现物理表树</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object logicTableTree() {
        try {
            String id = getId();
            Object item = null;        
            if ("-1".equals(id)) {
                // 逻辑表组树节点
                item = getService(LogicGroupDefineService.class).getTreeNode("-LG", true);
            } else if (id.startsWith("-LG")) {
                // 逻辑表组下的逻辑表
                item = getService(LogicTableDefineService.class).getTreeNode(id.substring(3), "-LT");
            } else if (id.startsWith("-LT")) {
                // 逻辑表对应物理表
                item = getService(PhysicalTableDefineService.class).getTreeNode(id.substring(3), null);
            }
            setReturnData(toTreeNode(id, item));
        } catch (Exception e) {
            log.error("", e);
        }
        
        return NONE;
    }

    /**
     * qiujinwei 2014-12-12 
     * <p>描述: 获取物理表分类树</p>
     * @return     返回类型   
     * @throws
     */
    public void getPhysicalTree(){
    	Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", getId());
        data.put("item", getService().getPhysicalTreeNode(buildSpecification()));
        setReturnData(data);
    }
    
    /**
     * qiujinwei 2014-12-12 
     * <p>描述: 构造物理表分类节点假树</p>
     * @return     返回类型   
     * @throws
     */
    public void comboOfTableTree(){
    	setReturnData(getService().comboOfTableTree());
    }
    
    /**
     * qiujinwei 2014-12-15 
     * <p>描述: 根据表分类Id获取表前缀</p>
     * @return     返回类型   
     * @throws
     */
    public void getClassification(){
    	String id = getParameter("P_id");
    	setReturnData(getService().getByID(id).getClassification());
    }
    
    /**
     * qiujinwei 2014-12-15 
     * <p>描述: 获取字段复制时显示的物理表树</p>
     * @return     返回类型   
     * @throws
     */
    public void getCopyToPhysicalTableTree(){
    	try {
            String id = getId();
            String columnId = getParameter("P_columnIds").split(",")[0];
            String tableId = getService(ColumnDefineService.class).getByID(columnId).getTableId();
            Object item = null;
            if (StringUtil.isEmpty(tableId)) {
            	item = getService().getTreeNode(buildSpecification(), id);
			} else {
				item = getService().getTreeNode(buildSpecification(), id, tableId);
			}
            setReturnData(toTreeNode(id, item));
        } catch (Exception e) {
            log.error("", e);
        }
    }
    
    /**
     * qiujinwei 2014-12-15 
     * <p>描述: 获取字段复制时显示的逻辑表树</p>
     * @return     返回类型   
     * @throws
     */
    public void getCopyToLogicTableTree(){
    	try {
            String id = getId();
            String columnId = getParameter("P_columnIds").split(",")[0];
            String tableCode = getService(ColumnDefineService.class).getByID(columnId).getTableId();
            Object item = null;
            if (StringUtil.isEmpty(tableCode)) {
            	getService(LogicTableDefineService.class).getTreeNode("-LT"); 
			} else {
				item = getService(LogicTableDefineService.class).getTreeNode2(tableCode); 
			}
            setReturnData(toTreeNode(id, item));
        } catch (Exception e) {
            log.error("", e);
        }
    }
    
    /**
     * qiujinwei 2015-03-24 
     * <p>描述: 节点删除校验</p>
     * @return     返回类型   
     * @throws
     */
    public void checkDelete(){
    	try {
			String id = getParameter("P_tableTreeId");
			setReturnData(getService().checkDelete(id));
		} catch (Exception e) {
			log.error("", e);
		}
    }
    
    /**
     * qiujinwei 2015-05-26 
     * <p>描述: 获取物理表前缀</p>
     * @return     返回类型   
     * @throws
     */
    public void getTablePrefix(){
    	try {
    		String tableTreeId = getParameter("P_tableTreeId");
            setReturnData(getService().getTablePrefix(tableTreeId));
        } catch (Exception e) {
            log.error("", e);
        }
    }
    
    /**
     * 更新物理表分类
     * 
     * @return Object
     */
    public Object updateTableTreeId() {
    	String start = getParameter("start");
        String targetId = getParameter("targetId");
        getService(PhysicalTableDefineService.class).updateTableTreeId(start, targetId);
        setReturnData("改变物理表分类成功!");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
}

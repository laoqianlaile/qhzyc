package com.ces.config.dhtmlx.action.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalGroupDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupDefine;
import com.ces.config.dhtmlx.entity.appmanage.TableTree;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.TableTreeService;
import com.ces.config.utils.GroupUtil;
import com.ces.xarch.core.exception.FatalException;

public class PhysicalGroupDefineController extends ConfigDefineServiceDaoController<PhysicalGroupDefine, PhysicalGroupDefineService, PhysicalGroupDefineDao> {
	
    private static final long serialVersionUID = 1L;
    
    private static Log log = LogFactory.getLog(PhysicalGroupDefineController.class);
	
	@Override
    protected void initModel() {
        setModel(new PhysicalGroupDefine());
    }

    /*
     * (非 Javadoc)
     * <p>标题: setService</p>
     * <p>描述: 注入服务层(Service)</p>
     * @param service
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("physicalGroupDefineService")
    protected void setService(PhysicalGroupDefineService service) {
        super.setService(service);
    }
    
    /**
     * qiujinwei 2014-11-17 
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整顺序</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object adjustShowOrder() throws FatalException {
        try {
            String sourceIds = getParameter("P_sourceIds");
            String targetId = getParameter("P_targetId");
            getService().adjustShowOrder(sourceIds, targetId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2014-11-28 
     * <p>描述: 获取指定物理表组下的物理表</p>
     */
    public Object getGroupTables() {
        try {
            setReturnData(getService().getGroupTables(getId()));
        } catch (Exception e) {
            log.error("获取指定物理表组下的物理表出错", e);
        }
        return NONE;
    }
    
    /**
     * qiujinwei 2014-12-01
     * <p>标题: getTreeOfGroup</p>
     * <p>描述: 构造物理表组树</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object getTreeOfGroup(){
    	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    	String groupId = getParameter("P_groupId");
    	try {
    		data.addAll(getService(LogicTableDefineService.class).getGroupTreeNode(groupId));
            setReturnData(data);
        } catch (Exception e) {
        	log.error("获取物理表分类节点出错", e);
        }
    	return NONE;
    }
    
    public Object getTreeNodes(){
    	String groupId = getParameter("P_groupId");
        Object item = null;     
        item = getService(PhysicalTableDefineService.class).getSelectedTreeNode(groupId, getId());
        setReturnData(toTreeNode(getId(), item));
        return NONE;
    }
    
    private Object toTreeNode(String id, Object item) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", id);
        data.put("item", item);
        return data;
    }
    
    /**
     * qiucs 2014-12-2 
     * <p>描述: 获取指定物理表组对应的逻辑表组代码</p>
     * @return Object    返回类型   
     */
    public Object getLogicGroupCode() {
        try {
            setReturnData(GroupUtil.getLogicGroupCode(getId()));
        } catch (Exception e) {
            log.error("获取指定物理表组对应的逻辑表组代码出错", e);
        }
        return NONE;
    }
    
    /**
     * qiujinwei 2014-12-15
     * <p>标题: saveAll</p>
     * <p>描述: 根据逻辑表组生成物理表组和物理表</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object saveAll(){
    	try {
    		//保存物理表组
    		String logicGroupCode = getParameter("logicGroupCode");
    		String groupName = getParameter("groupName");
    		String code = getParameter("code");
    		String remark = getParameter("remark");
    		PhysicalGroupDefine groupDefine = getService().autoMadeEntity(logicGroupCode, groupName, code, remark);
    		//创建物理表分类节点
    		String tableTreeId = getParameter("tableTreeId");
    		String classification = getParameter("classification");
    		String tablePrefix = getParameter("tablePrefix");
    		TableTree tableTree = getService(TableTreeService.class).save(tableTreeId, groupName, classification, tablePrefix);
    		//批量建表
    		tablePrefix = getService(TableTreeService.class).getTablePrefix(tableTree.getId());
    		if (tablePrefix.equals("0")) {
				tablePrefix = getParameter("defaultTablePrefix");
			}
    		getService(PhysicalTableDefineService.class).saveAll(logicGroupCode, tableTree.getId(), classification, tablePrefix, code, groupDefine.getId());    		
			setReturnData(new MessageModel(true, "success"));
		} catch (Exception e) {
			e.printStackTrace();
            throw new RuntimeException(e.getMessage());
		}
    	return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

}

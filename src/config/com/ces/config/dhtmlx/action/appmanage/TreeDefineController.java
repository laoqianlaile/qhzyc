package com.ces.config.dhtmlx.action.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.TreeDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.service.appmanage.ShowModuleService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.dhtmlx.service.appmanage.TriggerService;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.logger.Logger;

public class TreeDefineController extends 
		ConfigDefineServiceDaoController<TreeDefine, TreeDefineService, TreeDefineDao> {
    
	private static final long serialVersionUID = -4609059474518520871L;
	
	private static Log log = LogFactory.getLog(TreeDefineController.class);
	
	@Override
	protected void initModel() {
		setModel(new TreeDefine());
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
    @Qualifier("treeDefineService")
    protected void setService(TreeDefineService service) {
        super.setService(service);
    }
    
    /*
     * (非 Javadoc)   
     * <p>描述: 复写新增保存把保存后的ID传回模型</p>   
     * @return
     * @throws FatalException   
     * @see com.ces.xarch.core.web.struts2.BaseController#create()
     */
    @Override
    public Object create() throws FatalException {
        String auto = getParameter("P_auto");
        model = getService().save(model, "1".equals(auto));
        /*if (TreeDefine.T_COLUMN.equals(model.getType()) && "1".equals(model.getNodeRule()) && StringUtil.isNotEmpty(model.getTable1Id())) {
            getService(TriggerService.class).generateDynamicNodeTrigger(model.getTable1Id());
        }*/
        trigger(null, model, null);
        return SUCCESS;
    }
    
    /**
     * qiucs 2013-8-27 
     * <p>标题: parentLayout</p>
     * <p>描述: 继承上层的布局</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object parentLayout() {
        try {
            String parentId = getParameter("P_parentId");
            setReturnData(getService().findParentLayout(parentId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
 
    /**
     * qiucs 2013-8-27 
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整顺序</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object adjustShowOrder() {
        try {
            String parentId  = getParameter("P_parentId");
            String sourceIds = getParameter("P_sourceIds");
            String targetId  = getParameter("P_targetId");
            getService().adjustShowOrder(parentId, sourceIds, targetId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2013-9-8 
     * <p>标题: comboOfTree</p>
     * <p>描述: </p>
     * @return Object    返回类型   
     * @throws
     */
    public Object comboOfTrees() {
        try {
            setReturnData(getService().getComboOfTrees());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2014-1-8 
     * <p>描述: 按权限展现树节点</p>
     */
    public Object treeWithAuthority() throws FatalException {
        try {
            processTreeWithAuthority();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-1-8 
     * <p>描述: 获取当前用户的树节点</p>
     */
    protected void processTreeWithAuthority() throws FatalException {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        List<TreeDefine> treeDefineList = new ArrayList<TreeDefine>();
        String parentId = getId();
        boolean isDynamicChild = parentId.endsWith("_NT4");
        if (parentId.indexOf("_") < 0 || (isDynamicChild && !parentId.startsWith("NR2_"))) {
        	// 静态节点 或是 字段节点（跨表）动态节点下的子节点都是从数据库查询
        	String[] idArr = parentId.split("_");
    		treeDefineList = getService().find("EQ_nodeRule=0;EQ_parentId=" + idArr[0], new Sort("showOrder"));
    		if (isDynamicChild) {
    			treeDefineList = processDynamicColumnLabelChildren(treeDefineList, idArr);
    		}
        }
        if (CollectionUtils.isNotEmpty(treeDefineList)) {
            if (parentId.equals(treeDefineList.get(0).getRootId())) {
                HttpSession session = ServletActionContext.getRequest().getSession();
                session.setAttribute(parentId, new HashMap<String, TreeDefine>());
            }
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("moduleId", getParameter("P_moduleId"));
        paramMap.put("menuId", getParameter("P_menuId"));
        paramMap.put("showNodeCount", getParameter("P_showNodeCount"));
        if (parentId.startsWith("NT4_")) {
        	// 动态节点-字段节点（跨表）
        	treeDefineList.addAll(getService().getDynamicEmptyColumnChildren(parentId));
        } else {
        	List<TreeDefine> treeNodeList = getService().getByNodeRule2(parentId, paramMap);
            treeDefineList.addAll(treeNodeList);
        }
        list.setData(beforeProcessTreeDataWithAuthority(treeDefineList));
        
        if (FRAME_NAME_CORAL.equals(getFrameName())) {
            if (null == list.getData()) setReturnData(new ArrayList<TreeDefine>());
            else setReturnData(list.getData());
        }
    }
    
    /**
     * qiucs 2014-12-18 下午12:56:12
     * <p>描述: 重新拼接ID，规则：原ID+"_"+对应动态字段节点（跨表）的值+"_NT4" </p>
     * @return List<TreeDefine>
     */
    private List<TreeDefine> processDynamicColumnLabelChildren(List<TreeDefine> list, String[] idArr) {
    	for (TreeDefine entity : list) {
    		entity.setId(entity.getId() + "_" + idArr[1] + "_NT4");
    	}
    	return list;
    }
    
    /**
     * qiucs 2014-1-8 
     * <p>描述: 树节点处理</p>
     */
    protected List<TreeDefine> beforeProcessTreeDataWithAuthority(List<TreeDefine> data) {
        if (null == data || data.isEmpty()) return data;
        String treeIds = getParameter("P_treeIds");
        /*String moduleId = getParameter("P_moduleId");
        String componentVersionId = getParameter("P_componentVersionId");
        String showNodeCount = getParameter("P_showNodeCount");*/
        // System.out.println("P_authorityTreeIds=" + treeIds);
        if (StringUtil.isNotEmpty(treeIds)) {
            for (int i = data.size() - 1; i > -1; i--) {
                TreeDefine tree = data.get(i);
                if (!(("1".equals(tree.getDynamic()) && treeIds.indexOf(tree.getDynamicFromId()) > -1) 
                        || ("1".equals(tree.getDynamic()) && treeIds.indexOf(tree.getId()) > -1 ) 
                        || treeIds.indexOf(tree.getId()) > -1)) {
                    data.remove(i);
                }
            }
        }
        /*if (StringUtil.isBooleanTrue(showNodeCount)) {
            for (int i = 0; i < data.size(); i++) {
                TreeDefine tree = data.get(i);
                String total = "0";
                TreeDefine distinationTree = new TreeDefine();
                BeanUtils.copy(tree, distinationTree);
                String name = distinationTree.getName();
                if (StringUtil.isNotEmpty(tree.getTable1Id()) && StringUtil.isBooleanTrue(tree.getShowNodeCount())) {
                    total = String.valueOf(getService(ShowModuleService.class).countRecords(distinationTree, moduleId, componentVersionId));
                    distinationTree.setName(name + " (" + total + ")");
                }
                data.remove(i); data.add(i, distinationTree);
            }
        }*/
        
        return data;
    }
    
    /**
     * 获取树节点对应列表的记录条数
     * 
     * @return Object
     */
    public Object getTreeNodeRecordCount() {
        String treeNodeId = getParameter("P_treeNodeId");
        String moduleId   = getParameter("P_moduleId");
        String componentVersionId = getParameter("P_componentVersionId");
        String menuId     = getParameter("P_menuId");
        TreeDefine treeDefine = getService().getByID(treeNodeId);
        String total = String.valueOf(getService(ShowModuleService.class).countRecords(treeDefine, componentVersionId, moduleId, menuId));
        setReturnData(total);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

	/* (non-Javadoc)
	 * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
	 * @author Administrator
	 * @date 2013-10-23 16:28:34
	 */
	@Override
	@Logger(action = "删除", logger = "${id}")
	public Object destroy() throws FatalException {
        try {
            List<String> list = new ArrayList<String>();
            List<TreeDefine> entities = new ArrayList<TreeDefine>();
            String[] idArr = getId().split(",");
            for (int i = 0; i < idArr.length; i++) {
                TreeDefine entity = getService().getByID(idArr[i]);
                if (TreeDefine.T_COLUMN_TAB.equals(entity.getType()) && "1".equals(entity.getNodeRule())) {
                    if ("0".equals(entity.getDataSource())) {
                        if (list.contains(entity.getTableId())) continue;
                        list.add(entity.getTableId()); 
                    } 
                    entities.add(entity);
                }
            }
            list.clear(); list = null;
            getService().delete(getId());
            Boolean triggerSuccess = Boolean.TRUE;
            try {
                trigger(null, null, entities);
            } catch (Exception e) {
                e.printStackTrace();
                triggerSuccess = Boolean.FALSE;
            }
            entities.clear(); entities = null;
            if (triggerSuccess.booleanValue() == false) {
                setReturnData(new MessageModel(2, "节点删除成功，但重新编译动态节点触发器出问题！"));
            }
            setReturnData(new MessageModel(0, "删除成功！"));
        } catch (RuntimeException e) {
            e.printStackTrace();
            setReturnData(new MessageModel(1, e.getMessage()));
        }
        return NONE;
	}

	/**
     * 树节点的相互复制功能
     * @return
     * @date 2014-4-25  12:57:25
     */
	public Object treeNodeCopy() {
		String nodeIdCopyTo = getParameter("P_parentId");//复制到节点ID
    	String treeNodeId = getParameter("P_nodeId");//被复制节点ID
    	try {
    		getService().copyTreeNode(nodeIdCopyTo,treeNodeId);
    	} catch (Exception e) {
            e.printStackTrace();
        }
		return NONE;
	}
	
	
    @Override
    public Object update() throws FatalException {
        try {
            TreeDefine oldEntity = new TreeDefine();
            BeanUtils.copy(getService().getByID(model.getId()), oldEntity);
            getService().save(model);
            if (TreeDefine.RULE_TRIGGER.equals(oldEntity.getNodeRule())) {
                //if ()
            }
            trigger(oldEntity, model, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
    
    /**
     * qiucs 2014-1-2 
     * <p>描述: 生成动态节点触发器</p>
     * @param  @param oldEntity
     * @param  @param entity   
     * @param  @param entities    
     */
    protected void trigger(TreeDefine oldEntity, TreeDefine entity, List<TreeDefine> entities) {
        // update
        if (null != oldEntity) {
            if (TreeDefine.T_COLUMN_TAB.equals(oldEntity.getType()) && "1".equals(oldEntity.getNodeRule())) {
                /* 1. 动态节点改静态节点
                 * 2. 数据来源被改动
                 * 3. 数据表被改动
                 * 4. 表字段被改动
                 */
                if (!"1".equals(entity.getNodeRule()) || !oldEntity.getDataSource().equals(entity.getDataSource())
                        || ("0".equals(oldEntity.getDataSource()) && !oldEntity.getTableId().equals(entity.getTableId()))
                        || !oldEntity.getDbId().equals(entity.getDbId())) {
                    if ("0".equals(oldEntity.getDataSource())) {
                        getService(TriggerService.class).generateDynamicNodeTrigger(oldEntity.getTableId());
                    } else {
                        getService(TriggerService.class).generateDynamicNodeTrigger();
                    }
                }
            }
        }
        // create or update
        if (null != entity) {
            if (TreeDefine.T_COLUMN_TAB.equals(entity.getType()) && "1".equals(entity.getNodeRule())) {
                if ("0".equals(entity.getDataSource())) {
                    getService(TriggerService.class).generateDynamicNodeTrigger(entity.getTableId());
                } else {
                    getService(TriggerService.class).generateDynamicNodeTrigger();
                }
            }
        }
        // delete
        if (null != entities && !entities.isEmpty()) {
            boolean hasCode = false;
            for (TreeDefine pojo : entities) {
                if ("0".equals(pojo.getDataSource())) {
                    getService(TriggerService.class).generateDynamicNodeTrigger(pojo.getTableId());
                } else {
                    hasCode = true;
                }
            }
            if (hasCode) getService(TriggerService.class).generateDynamicNodeTrigger();
        }
    }
    
    /**
     * 校验上级所有的字段标签节点是否在表中
     * @return
     * @author Administrator
     * @date 2013-11-19  12:57:25
     */
    public Object checkColumnLabel(){
    	String parentId=getParameter("P_parentId");
    	String tableId=getParameter("P_tableId");
		try {
	        setReturnData(getService().checkColumnLabel(parentId, tableId));
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException(e.getMessage());
	    }
		return new DefaultHttpHeaders(SUCCESS).disableCaching();    	
    }
    
    /**
     * 校验字段标签节点修改时，字段标签是否存在于他的子节点（表节点或表组节点）中
     * @date 2013-12-17  09:45:42
     */
    public Object checkTableNode(){
    	String columnLabel = getParameter("P_columnLabel");
    	try {
            setReturnData(getService().checkTableNode(getId(), columnLabel));
        } catch (Exception e) {
            log.error("校验字段标签节点修改时，字段标签是否存在于他的子节点（表节点或表组节点）中出错", e);
        }
   	 	return new DefaultHttpHeaders(SUCCESS).disableCaching();    
    }
    
    /**
     * 校验树节点相互复制的条件
     * @return
     * @author lmson
     * @date 2014-4-28  12:57:25
     */
    public Object checkTreeNodeCopy(){
    	String nodeIdCopyTo = getParameter("P_parentId");//复制到节点ID
    	String treeNodeId = getParameter("P_nodeId");//被复制节点ID
		try {
	        setReturnData(getService().checkTreeNodeCopy(nodeIdCopyTo,treeNodeId));
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException(e.getMessage());
	    }
		return new DefaultHttpHeaders(SUCCESS).disableCaching();    	
    }
    
    /**
     * qiucs 2014-9-29 
     * <p>描述: 查找是否有配置显示数量节点</p>
     * @return Object    返回类型   
     */
    public Object hasShowNodeCount() {
        try {
            String rootId = getParameter("P_treeId");
            setReturnData(getService().hasShowNodeCount(rootId));
        } catch (Exception e) {
            log.error("查找是否有配置显示数量节点出错", e);
        }
        
        return NONE;
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 获取树的根节点（注意有隐藏根节点）</p>
     */
    public Object rootNodes() {
        try {
            list = getDataModel(getModelTemplate());
            processFilter(list);
            List<TreeDefine> rootNodes = getService().getRootNodes(getId());
            // 过滤节点权限
            String menuId = getParameter("P_menuId");
            String componentVersionId = getParameter("P_componentVersionId");
            String treeNodeIds = AuthorityUtil.getInstance().getTreeAuthority(menuId, componentVersionId);
            if (StringUtil.isNotEmpty(treeNodeIds) && CollectionUtils.isNotEmpty(rootNodes)) {
                for (int i = rootNodes.size() - 1; i > -1; i--) {
                    TreeDefine tree = rootNodes.get(i);
                    String treeId = tree.getId();
                    if (treeId.indexOf("_") != -1) {
                        treeId = treeId.substring(0, treeId.indexOf("_"));
                    }
                    if (treeNodeIds.indexOf(treeId) == -1) {
                        rootNodes.remove(i);
                    }
                }
            }
            list.setData(rootNodes);
            
            if (FRAME_NAME_CORAL.equals(getFrameName())) {
                if (null == list.getData()) setReturnData(new ArrayList<TreeDefine>());
                else setReturnData(list.getData());
            }
        } catch (Exception e) {
            log.error("前台展现获取树的根节点出错", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-11-24 
     * <p>描述: 校验物理表组是否有具体物理表</p>
     */
    public Object checkTableGroup() {
        
        try {
            String groupId = getParameter("P_groupId");
            setReturnData(getService().checkTableGroup(groupId));
        } catch (Exception e) {
            log.error("校验物理表组是否有具体物理表出错", e);
        }
        
        return NONE;
    }
}

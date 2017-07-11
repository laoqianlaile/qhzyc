package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.workflow.wapi.Coflow;
import ces.workflow.wapi.define.DefineXmlFile;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowFormSettingDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowFormSetting;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FormUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;

@Component
public class WorkflowDefineService extends ConfigDefineDaoService<WorkflowDefine, WorkflowDefineDao> {
    
    private static Log log = LogFactory.getLog(WorkflowDefineService.class);

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("workflowDefineDao")
    @Override
    protected void setDaoUnBinding(WorkflowDefineDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (非 Javadoc)   
     * <p>描述: 保存前，先设置显示顺序值</p>   
     * @param entity
     * @return   
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public WorkflowDefine save(WorkflowDefine entity) {
    	WorkflowDefine oldEntity = null;
        if (StringUtil.isEmpty(entity.getId())) {
            Integer showOrder = getDao().getMaxShowOrderByWorkflowTreeId(entity.getWorkflowTreeId());
            if (null == showOrder) {
                showOrder = 0;
            }
            entity.setShowOrder(showOrder + 1);
        } else {
        	oldEntity = getByID(entity.getId());
        }
        // 工作箱处理
        processWorkflowBoxes(entity);
        // 工作流开启的相关表处理
        processEnableTable(entity, oldEntity);
        // 工作流业务表字段处理
        MessageModel mm = getService(PhysicalTableDefineService.class).exists(entity.getBusinessTableId());
        if (mm.getSuccess()) {
            getService(ColumnDefineService.class).createCoflowRelateColumns(entity.getBusinessTableId());
        }
        entity = getDao().save(entity);
        //
        createBusinessView(entity, false);
        // 生成默认版本1.0
        getService(WorkflowVersionService.class).addDefaultVersion(entity.getId());
        // 
        entity.setBusinessTableText(TableUtil.getTableText(entity.getBusinessTableId()));
        // 向缓存添加数据
        WorkflowUtil.addWorkflowEntity(entity);
        
        return (entity);
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 工作箱处理</p>
     * @return WorkflowDefine
     */
    private WorkflowDefine processWorkflowBoxes(WorkflowDefine entity) {
    	Map<String, String> boxes = new HashMap<String, String>();
    	if (StringUtil.isBooleanTrue(entity.getBoxApplyfor())) {
    		boxes.put(WorkflowUtil.Box.applyfor, entity.getNameApplyfor());
    	}
    	if (StringUtil.isBooleanTrue(entity.getBoxTodo())) {
    		boxes.put(WorkflowUtil.Box.todo, entity.getNameTodo());
    	}
    	if (StringUtil.isBooleanTrue(entity.getBoxHasdone())) {
    		boxes.put(WorkflowUtil.Box.hasdone, entity.getNameHasdone());
    	}
    	if (StringUtil.isBooleanTrue(entity.getBoxComplete())) {
    		boxes.put(WorkflowUtil.Box.complete, entity.getNameComplete());
    	}
    	if (StringUtil.isBooleanTrue(entity.getBoxToread())) {
    		boxes.put(WorkflowUtil.Box.toread, entity.getNameToread());
    	}
    	if (StringUtil.isBooleanTrue(entity.getBoxHasread())) {
    		boxes.put(WorkflowUtil.Box.hasread, entity.getNameHasread());
    	}
    	entity.setWorkflowBoxes(JsonUtil.bean2json(boxes));
    	return entity;
    }

    /**
     * qiucs 2014-12-15 
     * <p>描述: 处理工作流开启的相关表</p>
     */
    @Transactional
    private void processEnableTable(WorkflowDefine entity, WorkflowDefine oldEntity) {
    	PhysicalTableDefine table = getService(PhysicalTableDefineService.class).getByID(entity.getBusinessTableId());
    	String code = entity.getWorkflowCode();
    	String logicTableCode = null;
    	// 附件表
    	if (StringUtil.isBooleanTrue(entity.getEnableDocumentTable()) &&
    			(null == oldEntity || !StringUtil.isBooleanTrue(oldEntity.getEnableDocumentTable()))) {
    		// create document table
    		logicTableCode = ConstantVar.Labels.Document.CODE;
    		createEnableTable(entity, table, logicTableCode, WorkflowUtil.getDocumentTableName(code), "电子全文表");
    		//getService(ColumnDefineService.class).copyFromLogicTable(logicTableCode, enableTable);
    	} else if (null != oldEntity && StringUtil.isBooleanTrue(oldEntity.getEnableDocumentTable()) && 
    			!StringUtil.isBooleanTrue(entity.getEnableDocumentTable())) {
    		// drop document table
    		String tableId = TableUtil.getTableId(WorkflowUtil.getDocumentTableName(code));
    		if (StringUtil.isNotEmpty(tableId)) {
    			getService(PhysicalTableDefineService.class).delete(tableId);
    		}
    	}
    	// 辅助意见表
    	if (StringUtil.isBooleanTrue(entity.getEnableAssistTable()) &&
    			(null == oldEntity || !StringUtil.isBooleanTrue(oldEntity.getEnableAssistTable()))) {
    		// create assist table
    		logicTableCode = ConstantVar.Labels.AssistOpinion.CODE;
    		createEnableTable(entity, table, logicTableCode, WorkflowUtil.getAssistTableName(code), "辅助意见表");
    		//getService(ColumnDefineService.class).copyFromLogicTable(logicTableCode, enableTable);
    	} else if (null != oldEntity && StringUtil.isBooleanTrue(oldEntity.getEnableAssistTable()) && 
    			!StringUtil.isBooleanTrue(entity.getEnableAssistTable())) {
    		// drop assist table
    		String tableId = TableUtil.getTableId(WorkflowUtil.getAssistTableName(code));
    		if (StringUtil.isNotEmpty(tableId)) {
    			getService(PhysicalTableDefineService.class).delete(tableId);
    		}
    	}
    	// 审批意见表
    	if (StringUtil.isBooleanTrue(entity.getEnableConfirmTable()) &&
    			(null == oldEntity || !StringUtil.isBooleanTrue(oldEntity.getEnableConfirmTable()))) {
    		// create confirm table
    		logicTableCode = ConstantVar.Labels.ConfirmOpinion.CODE;
    		createEnableTable(entity, table, logicTableCode, WorkflowUtil.getConfirmTableName(code), "审批意见表");
    		//getService(ColumnDefineService.class).copyFromLogicTable(logicTableCode, enableTable);
    	} else if (null != oldEntity && StringUtil.isBooleanTrue(oldEntity.getEnableConfirmTable()) && 
    			!StringUtil.isBooleanTrue(entity.getEnableConfirmTable())) {
    		// drop confirm table
    		String tableId = TableUtil.getTableId(WorkflowUtil.getConfirmTableName(code));
    		if (StringUtil.isNotEmpty(tableId)) {
    			getService(PhysicalTableDefineService.class).delete(tableId);
    		}
    	}
    }

    /**
     * qiucs 2014-12-15 
     * <p>描述: 创建工作流相关表</p>
     */
    @Transactional
    private PhysicalTableDefine createEnableTable(WorkflowDefine flowDefine, PhysicalTableDefine table, String logicTableCode, String tableName, String showName) {
    	PhysicalTableDefine enableTable = getService(PhysicalTableDefineService.class).getByTableName(tableName);
    	if (null == enableTable) {
    		enableTable = new PhysicalTableDefine();
    		BeanUtils.copy(table, enableTable);
    		enableTable.setId(null);
    		enableTable.setShowName(showName+ "[" + flowDefine.getWorkflowCode() + "]");
    		enableTable.setTablePrefix(tableName.substring(0, 5));
    		enableTable.setTableCode(tableName.substring(5));
    		enableTable.setLogicTableCode(logicTableCode);
    		enableTable.setCreated("0");
    		enableTable.setRemark("由工作流定义自动生成（" + flowDefine.getWorkflowName() + "[" + flowDefine.getWorkflowCode() + "]）");
    	} else if (table.getTableTreeId().equals(enableTable.getTableTreeId())) {
			Integer showOrder = getService(PhysicalTableDefineService.class).getMaxShowOrder(table.getTableTreeId());
			enableTable.setTableTreeId(table.getTableTreeId());
			enableTable.setShowOrder(showOrder);
    	}
    	// 工作流开启用的表与业务表挂在同表分类节点下
    	enableTable = getService(PhysicalTableDefineService.class).save(enableTable);
    	// 复制对应的字段
    	getService(ColumnDefineService.class).copyFromLogicTable(logicTableCode, enableTable);
    	
    	return (enableTable);
    }
    
    /**
     * qiucs 2015-8-11 下午2:27:47
     * <p>描述: 字段保存时，同时同步相关流程视图字段 </p>
     * @return void
     */
    @Transactional
    public void syncBusinessViewByTableId(String tableId) {
    	List<WorkflowDefine> list = find("EQ_businessTableId=" + tableId);
    	for (int i = 0, len = list.size(); i < len; i++) {
    		createBusinessView(list.get(i), false);
    	}
    }
    
    /**
     * qiucs 2015-1-22 下午10:25:26
     * <p>描述: 同步业务表视图 </p>
     * @return void
     */
    @Transactional
    public void syncBusinessView(String id) {
    	WorkflowDefine entity = WorkflowUtil.getWorkflowEntity(id);
    	createBusinessView(entity, false);
    }    
    
    /**
     * qiucs 2014-12-26 上午9:44:13
     * <p>描述: 创建业务表视图 </p>
     * @return void
     */
    @Transactional
    protected void createBusinessView(WorkflowDefine entity, boolean forceRebuild) {
    	String tableId = entity.getBusinessTableId();
    	List<ColumnDefine> clist = getService(ColumnDefineService.class).find("EQ_tableId=" + tableId, new Sort("showOrder"));
    	
    	StringBuilder sb = new StringBuilder("SELECT ");
    	// ID
    	sb.append("A.ID" + DatabaseHandlerDao.getSeperator() + "'_'" + DatabaseHandlerDao.getSeperator() + "B.ID AS ID");
    	//sb.append("A.ID");
    	// 业务表字段
    	for (ColumnDefine col : clist) {
    		if (ConstantVar.ColumnName.ID.equals(col.getColumnName())) {
    			continue;
    		} else if (WorkflowUtil.C_PROCESS_INSTANCE_ID.equals(col.getColumnName())) {
    			sb.append(",A.").append(col.getColumnName()).append(" AS ").append(WorkflowUtil.Alias.processInstanceId);
    		} else {
    			sb.append(",A.").append(col.getColumnName());
    		}
    	}
    	// 工作项字段
    	List<Map<String, String>> wlist = getCoflowColumMap();
    	for (Map<String, String> item : wlist) {
    		sb.append(",").append(item.get("tableAlias")).append(".").append(item.get("columnName")).append(" AS ").append(item.get("columnAlias"));
    	}
    	sb.append(" FROM ")
    	  .append(TableUtil.getTableName(tableId)).append(" A ")
    	  .append("LEFT JOIN T_WF_WORKITEM B ON(A.PROCESS_INSTANCE_ID=B.PROCESS_INSTANCE_ID)")
    	  .append("LEFT JOIN T_XTPZ_WORKFLOW_ACTIVITY C ON(C.PACKAGE_ID=B.PACKAGE_ID AND C.PACKAGE_VERSION=B.PACKAGE_VERSION AND C.PROCESS_ID=B.PROCESS_ID AND C.ACTIVITY_ID=B.ACTIVITY_ID)")
    	  .append(" WHERE ")
    	  .append(" A.").append(WorkflowUtil.C_BELONG_WORKFLOW_CODE).append(" = '").append(entity.getWorkflowCode()).append("' ")
    	  .append(" AND A.IS_DELETE = '0' ");
    	// 创建业务表与工作流引擎的视图
    	String viewName = WorkflowUtil.getViewName(entity.getWorkflowCode());
    	String showName = entity.getWorkflowName() + "（视图）";
    	DatabaseHandlerDao.getInstance().createView(viewName, String.valueOf(sb));
    	// 在表定义中，生成相应的记录
    	createCoflowView(viewName, showName, clist, forceRebuild);
    }
    
    /**
     * qiucs 2014-12-26 下午1:41:25
     * <p>描述: 创建视图 </p>
     * @return PhysicalTableDefine
     */
    @Transactional
    private PhysicalTableDefine createCoflowView(String viewName, String showName, List<ColumnDefine> clist, boolean forceRebuild) {
    	boolean isCreate = false;
    	PhysicalTableDefine entity = getService(PhysicalTableDefineService.class).getByTableName(viewName.toUpperCase());
    	List<ColumnDefine> oldColumnList = null;
    	if (null == entity) {
    		entity = new PhysicalTableDefine();
    		entity.setTableTreeId("-VA");
    		entity.setClassification("V");
    		entity.setCreated("1");
    		entity.setTableType("1");
    		entity.setTablePrefix(viewName.substring(0, 5));
    		entity.setTableCode(viewName.substring(5));
    		entity.setRemark("由工作流定义自动生成");
    		entity.setShowName(showName);
    		getService(PhysicalTableDefineService.class).save(entity);
    		isCreate = true;
    	} else {
    		/*if (forceRebuild) getService(ColumnDefineService.class).deleteByTableId(entity.getId());
    		else */
    		oldColumnList = getService(ColumnDefineService.class).find("EQ_tableId=" + entity.getId(), new Sort("showOrder"));
    	}
    	List<ColumnDefine> viewColumnList = new ArrayList<ColumnDefine>();
    	Integer showOrder = 0;
    	for (ColumnDefine col : clist) {
    		if (WorkflowUtil.C_PROCESS_INSTANCE_ID.equals(col.getColumnName())) {
    			continue;
    		}
    		ColumnDefine viewColumn = null;
    		if (!isCreate) {
    			String filters = "EQ_tableId=" + entity.getId() + ";EQ_columnName=" + col.getColumnName();
    			viewColumn = getService(ColumnDefineService.class).findOne(filters);
    		}
    		if (null == viewColumn || isCreate) {
    			viewColumn = new ColumnDefine();
    			BeanUtils.copy(col, viewColumn);
    			viewColumn.setId(null);
    			viewColumn.setTableId(entity.getId());
    			if (ConstantVar.ColumnName.ID.equals(col.getColumnName())) {
    				viewColumn.setShowName("ID由（业务表ID_工作项ID）组成");
    			}
    		} else {
    			viewColumn.setShowName(col.getShowName());
    		}
    		viewColumn.setShowOrder(++showOrder);
    		
    		viewColumnList.add(viewColumn);
    	}
    	// 工作项字段
    	List<Map<String, String>> wlist = getCoflowColumMap();
    	for (Map<String, String> item : wlist) {
    		ColumnDefine viewColumn = null;
    		if (!isCreate) {
    			String filters = "EQ_tableId=" + entity.getId() + ";EQ_columnName=" + item.get("columnAlias");
    			viewColumn = getService(ColumnDefineService.class).findOne(filters);
    		}
    		if (null == viewColumn || isCreate) {
    			viewColumn = newViewColumnDefine(entity.getId(), item, ++showOrder);
    		}
    		
    		viewColumnList.add(viewColumn);
    	}
    	// 删除不存在的字段
    	if (CollectionUtils.isNotEmpty(oldColumnList)) {
    		for (ColumnDefine col : clist) {
    			if (WorkflowUtil.C_PROCESS_INSTANCE_ID.equals(col.getColumnName())) {
    				removeExistColumn(WorkflowUtil.Alias.processInstanceId, oldColumnList);
    			} else {
    				removeExistColumn(col.getColumnName(), oldColumnList);
    			}
    		}
    		for (Map<String, String> item : wlist) {
    			removeExistColumn(item.get("columnAlias"), oldColumnList);
    		}
    		if (CollectionUtils.isNotEmpty(oldColumnList)) {
    			getService(ColumnDefineService.class).delete(oldColumnList);
    		}
    	}
    	// 保存新添加的字段
    	getService(ColumnDefineService.class).save(viewColumnList);
    	
    	return entity;
    }
    
    /**
     * qiucs 2014-12-26 下午5:47:06
     * <p>描述: 移除字段名相同的字段 </p>
     * @return void
     */
    private void removeExistColumn(String columnName, List<ColumnDefine> columnList) {
    	for (int i = 0; i < columnList.size(); i++) {
			if (columnName.equals(columnList.get(i).getColumnName())) {
				columnList.remove(i); break;
			}
		}
    }
    
    /**
     * qiucs 2014-12-26 下午5:46:46
     * <p>描述: 创建一个字段实例 </p>
     * @return ColumnDefine
     */
    private ColumnDefine newViewColumnDefine(String tableId, Map<String, String> item, Integer showOrder) {
    	ColumnDefine entity = new ColumnDefine();
    	
    	entity.setTableId(tableId);
    	entity.setShowOrder(showOrder);
    	entity.setColumnName(item.get("columnAlias"));
    	entity.setShowName(item.get("showName"));
    	entity.setColumnType("0");
    	entity.setLength(Integer.parseInt(item.get("length")));
    	entity.setDataType(ConstantVar.DataType.CHAR);
    	entity.setCreated("1");
    	entity.setInputable("0");
    	entity.setUpdateable("0");
    	entity.setListable("1");
    	entity.setSearchable("1");
    	entity.setSortable("1");
    	entity.setAlign("left");
    	entity.setFilterType("EQ");
    	entity.setWidth(80);
    	entity.setRemark("由工作流定义创建");
    	if (item.containsKey("codeTypeCode")) {
    		entity.setCodeTypeCode(item.get("codeTypeCode"));
    		entity.setInputType(FormUtil.CoralInputType.COMBOBOX);
    	} else {
    		entity.setInputType(FormUtil.CoralInputType.TEXTBOX);
    	}
    	return entity;
    }
    
    /**
     * qiucs 2014-12-26 下午5:46:22
     * <p>描述: 工作项表字段 </p>
     * @return List<Map<String,String>>
     */
    private List<Map<String, String>> getCoflowColumMap() {
    	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    	Map<String, String> item = new HashMap<String, String>();
    	item.put("tableAlias", "C");
    	item.put("columnName", "ACTIVITY_NAME");
    	item.put("columnAlias", "WI_ACTIVITY_NAME_");
    	item.put("showName", "当前节点");
    	item.put("length", "100");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "CREATED_TIME");
    	item.put("columnAlias", "WI_CREATED_TIME_");
    	item.put("showName", "发送时间");
    	item.put("length", "32");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "STATUS");
    	item.put("columnAlias", "WI_STATUS_");
    	item.put("showName", "节点状态");
    	item.put("codeTypeCode", "WORKITEM_STATUS");
    	item.put("length", "20");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "PRE_OWNER_ID");
    	item.put("columnAlias", "WI_PRE_OWNER_ID_");
    	item.put("showName", "发送人员");
    	item.put("codeTypeCode", "AUTH_USER");
    	item.put("length", "32");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "TIME_VALUE1");
    	item.put("columnAlias", "WI_TIME_VALUE1_");
    	item.put("showName", "期限时间");
    	item.put("length", "40");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "COMPLETED_TIME");
    	item.put("columnAlias", "WI_COMPLETED_TIME_");
    	item.put("showName", "完成时间");
    	item.put("length", "32");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "SUMMARY");
    	item.put("columnAlias", "WI_SUMMARY_");
    	item.put("showName", "节点摘要");
    	item.put("length", "500");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "NOTE");
    	item.put("columnAlias", "WI_NOTE_");
    	item.put("showName", "节点说明");
    	item.put("length", "500");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "OWNER_ID");
    	item.put("columnAlias", "WI_OWNER_ID_");
    	item.put("showName", "办理人员");
    	item.put("codeTypeCode", "AUTH_USER");
    	item.put("length", "32");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "ID");
    	item.put("columnAlias", "WI_ID_"); // 业务表.ID + "_" + T_WF_WORKITEM.ID
    	item.put("showName", "工作项ID");
    	item.put("length", "100");
    	list.add(item);
    	/*item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "PROCESS_INSTANCE_ID");
    	item.put("columnAlias", "PROCESS_INSTANCE_ID_");
    	item.put("showName", "流程实例ID");
    	item.put("length", "32");
    	list.add(item);//*/
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "ACTIVITY_INSTANCE_ID");
    	item.put("columnAlias", "WI_ACTIVITY_INSTANCE_ID_");
    	item.put("showName", "节点实例ID");
    	item.put("length", "32");
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "PACKAGE_ID");
    	item.put("columnAlias", "WI_PACKAGE_ID_");
    	item.put("showName", "包ID");
    	item.put("length", "32");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "PACKAGE_VERSION");
    	item.put("columnAlias", "WI_PACKAGE_VERSION_");
    	item.put("showName", "包版本号");
    	item.put("length", "20");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "PROCESS_ID");
    	item.put("columnAlias", "WI_PROCESS_ID_");
    	item.put("showName", "流程ID");
    	item.put("length", "32");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "ACTIVITY_ID");
    	item.put("columnAlias", "WI_ACTIVITY_ID_");
    	item.put("showName", "节点ID");
    	item.put("length", "32");
    	list.add(item);
    	item = new HashMap<String, String>();
    	item.put("tableAlias", "B");
    	item.put("columnName", "PRE_STATUS");
    	item.put("columnAlias", "WI_PRE_STATUS_");
    	item.put("showName", "前一节点状态");
    	item.put("codeTypeCode", "WORKITEM_STATUS");
    	item.put("length", "32");
    	list.add(item);
    	
    	return list;
    }
    
    /**
     * qiucs 2014-12-15 
     * <p>描述: 封装为树节点json</p>
     * @param treeId
     * @param idPre  ID前缀符
     * @return
     */
    public List<Map<String, Object>> getTreeNode(String treeId, String idPre) {
    	String filters = "EQ_workflowTreeId=" + treeId; // 多个用英文";"分隔
        List<WorkflowDefine> list = find(filters, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        idPre = StringUtil.null2empty(idPre);        		
        for (WorkflowDefine entity : list) {
            data.add(beanToTreeNode(entity, idPre));
        }
        return data;
    }
    
    /**
     * qiucs 2014-12-15 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> beanToTreeNode(WorkflowDefine entity, String idPre) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "1");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "tableId");
        item.put("content", entity.getBusinessTableId());
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "viewId");
        item.put("content", WorkflowUtil.getViewId(entity.getId()));
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "enableAssistTable");
        item.put("content", entity.getEnableAssistTable());
        userdata.add(item);
        
        data.put("id", idPre.concat(entity.getId()));
        data.put("text", entity.getWorkflowName());
        data.put("child", Boolean.TRUE);
        data.put("userdata", userdata);
        return data;
    }

    /**
     * qiucs 2013-8-22 
     * <p>描述: 拖拽调整显示顺序</p>
     * @param  beginIds
     * @param  endId    设定参数   
     */
    @Transactional
    public void adjustShowOrder(String parentId, String sourceIds, String targetId) {
        String[] idArr = sourceIds.split(",");
        int len = idArr.length;
        
        int sBeginShowOrder   = getDao().getShowOrderById(idArr[0]);
        int sEndShowOrder     = (len > 1) ? getDao().getShowOrderById(idArr[len - 1]) : sBeginShowOrder;
        if (sBeginShowOrder > sEndShowOrder) {
            sBeginShowOrder = sBeginShowOrder^sEndShowOrder;
            sEndShowOrder   = sBeginShowOrder^sEndShowOrder;
            sBeginShowOrder = sBeginShowOrder^sEndShowOrder;
        }
        int tShowOrder        = getDao().getShowOrderById(targetId);
        
        int increaseNum = 0, differLen = 0, begin = 0, end = 0;
        boolean isUp = false;
        if (sBeginShowOrder > tShowOrder) {
            isUp = true;
        }
        if (isUp) { 
            // 向上拖动
            //getDao().upShowOrder(parentId, eShowOrder, bShowOrder);
            increaseNum = len;
            differLen = tShowOrder - sBeginShowOrder;
            // tShowOrder-1 < showOrder < sBeginShowOrder
            begin = tShowOrder-1;
            end   = sBeginShowOrder;
        } else { 
            // 向下拖动
            //getDao().downShowOrder(parentId, bShowOrder, eShowOrder);
            increaseNum = -len;
            differLen = tShowOrder - sEndShowOrder;
            // sEndShowOrder < showOrder < tShowOrder + 1
            begin = sEndShowOrder;
            end   = tShowOrder + 1;
        }
        // update between sourceIds and targetId
        getDao().batchUpdateShowOrder(parentId, begin, end, increaseNum);
        // update sourceIds
        for (int i = 0; i < len; i++) {
            getDao().updateShowOrderById(idArr[i], differLen);
        }
    }


    
    /*
     * (非 Javadoc)   
     * <p>描述: </p>   
     * @param ids   
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            // 删除相关配置
            deleteRelation(id);
            getDao().delete(id);
            // 
            WorkflowUtil.removeWorkflowEntity(id);
        }
    }
    
    /**
     * qiucs 2014-12-18 下午7:38:20
     * <p>描述: 删除检查 </p>
     * @return MessageModel
     */
    public MessageModel checkDelete(String id) {
    	long cnt = getService(WorkflowVersionService.class).count("EQ_workflowId=" + id);
    	if (cnt > 0) return MessageModel.falseInstance("请删除该流程下的版本信息，再删除！");
    	return MessageModel.trueInstance("OK");
    }

    /**
     * 根据ID获取所有子节点
     */
    protected List<WorkflowDefine> getAllChildById(String id) {
        if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getAllChildByIdOfOracle(id);
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getAllChildByIdOfSqlserver(id);
        }
        return null;
    }
    
    /**
     * qiucs 2013-10-11 
     * <p>描述: 根据表ID获取流程ID</p>
     * @param  tableId
     */
    public String getProcessId(String tableId) {
        WorkflowDefine entity = getWorkflowByTableId(tableId);
        if (null == entity) return null;
        return WorkflowUtil.getProcessIdByCode(entity.getWorkflowCode());
    }
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: </p>
     * @param  tableId
     * @return boolean    返回类型
     *             true：存在，false：不存在   
     * @throws
     */
    public boolean existsWorkflow(String tableId) {
        List<String> list = getDao().findWorkflowIdsByTableId(tableId);
        if (null == list || list.isEmpty()) return false;
        return true;
    }
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: </p>
     * @param  tableId
     * @return Workflow    返回类型   
     * @throws
     */
    public WorkflowDefine getWorkflowByTableId(String tableId) {
        List<WorkflowDefine> list = getDao().findWorkflowByTableId(tableId);
        if (null == list || list.isEmpty()) return null;
        
        return list.get(0);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 删除相关配置</p>
     * @param  id
     */
    @Transactional
    protected void deleteRelation(String id) {
        //删除工作流节点信息
        //getService(WorkflowActivityService.class).deleteByWorkflowId(id);
        //删除工作流绑定信息
        //getService(WorkflowBindingService.class).deleteByWorkflowId(id);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID更新工作流</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    public void updateByTableId(String tableId) {
//        List<Workflow> list = getDao().findWorkflowByTableId(tableId);
//        for (Workflow entity : list) {
//            deleteRelation(entity.getId());
//        }
//        getDao().updateByTableId(tableId);
    }
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID删除配置</p>
     * @param  columnId    字段ID
     */
    @Transactional
    public void deleteByColumnId(String columnId) {
        getDao(WorkflowFormSettingDao.class, WorkflowFormSetting.class).deleteByColumnId(columnId);
        
    }
    
    /**
     * qiucs 2014-10-20 
     * <p>描述: 检查工作流</p>
     * @return MessageModel    返回类型   
     * @throws
     */
    public MessageModel checkCoflow(WorkflowDefine entity) {
        try {
            List<DefineXmlFile> list = Coflow.getDefineManager().getDefineXmlFiles();
            for (DefineXmlFile file : list) {
                if (entity.getWorkflowCode().equals(file.getFileName())) {
                    if (DefineXmlFile.UNREGIST.equals(file.getStatus())) {
                        return MessageModel.falseInstance("流程文件（" + file.getFileName() + "）未注册，请先注册，并同步相关数据！");
                    }
                    try {
                        String rdTablename = Coflow.getDefineManager().getReventDatetablename(
                                file.getPackageId(), 
                                file.getPackageVersion(),
                                WorkflowUtil.getProcessIdByCode(entity.getWorkflowCode()));
                        if (StringUtil.isEmpty(rdTablename)) {
                            return MessageModel.falseInstance("该流程未同步相关数据，请先同步相关数据再配置！");
                        }
                    } catch (Exception e) {
                        return MessageModel.falseInstance("该流程未同步相关数据，请先同步相关数据再配置！");
                    }
                    
                    return MessageModel.trueInstance("OK");
                }
            }
        } catch (Exception e) {
            log.error("检查工作流出错", e);
        }
        return MessageModel.falseInstance("流程文件（" + entity.getWorkflowCode() + "）不存在，请检查！");
    }
    
    /**
     * qiucs 2014-12-22 上午11:11:07
     * <p>描述: 根据表ID获取工作箱 </p>
     * @return List<String>
     */
    public List<String> getCoflowBoxes(String tableId) {
    	
    	WorkflowDefine entity = getWorkflowByTableId(tableId);
    	
    	return getCoflowBoxes(entity);
    }
    
    /**
     * qiucs 2014-12-23 上午11:11:07
     * <p>描述: 根据表ID获取工作箱 </p>
     * @return List<String>
     */
    public List<String> getCoflowBoxes(WorkflowDefine entity) {
    	List<String> list = new ArrayList<String>();
    	
    	if (StringUtil.isBooleanTrue(entity.getBoxApplyfor())) {
    		list.add(WorkflowUtil.Box.applyfor);
    	}
    	if (StringUtil.isBooleanTrue(entity.getBoxTodo())) {
    		list.add(WorkflowUtil.Box.todo);
    	}
    	if (StringUtil.isBooleanTrue(entity.getBoxHasdone())) {
    		list.add(WorkflowUtil.Box.hasdone);
    	}
    	if (StringUtil.isBooleanTrue(entity.getBoxComplete())) {
    		list.add(WorkflowUtil.Box.complete);
    	}
    	if (StringUtil.isBooleanTrue(entity.getBoxToread())) {
    		list.add(WorkflowUtil.Box.toread);
    	}
    	if (StringUtil.isBooleanTrue(entity.getBoxHasread())) {
    		list.add(WorkflowUtil.Box.hasread);
    	}
    	
    	return list;
    }
    
    
    /**
     * qiucs 2015-4-1 下午8:47:37
     * <p>描述: 根据流程编码检查是否有正在运行的流程版本 </p>
     * @return boolean
     */
    public boolean checkRunningByCode(String code) {
    	
    	WorkflowDefine entity = findOne("EQ_workflowCode=" + code);
    	
    	if (null == entity) {
    		log.warn("工作流编码（" + code + "）的流程不存在，请检查！");
    		return false;
    	}
    	
    	return getService(WorkflowVersionService.class).checkRunning(entity.getId());
    }
    
}

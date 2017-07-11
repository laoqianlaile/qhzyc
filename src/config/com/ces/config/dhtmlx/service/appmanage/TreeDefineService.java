package com.ces.config.dhtmlx.service.appmanage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.ColumnDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalGroupRelationDao;
import com.ces.config.dhtmlx.dao.appmanage.TreeDefineDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxComboOption;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.GroupUtil;
import com.ces.config.utils.ModuleUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;
import com.google.common.collect.Lists;

@Component
public class TreeDefineService  extends ConfigDefineDaoService<TreeDefine, TreeDefineDao>{
    
    private static Log log = LogFactory.getLog(TreeDefineService.class);

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("treeDefineDao")
    @Override
    protected void setDaoUnBinding(TreeDefineDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (非 Javadoc)   
     * <p>标题: save</p>   
     * <p>描述: </p>   
     * @param entity
     * @return   
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
	@Override
    @Transactional
    public TreeDefine save(TreeDefine entity) {
        // 1. if type is column, create index
        // createIndex(entity);
        // 2. save entity
        TreeDefine parentEntity = null;
        TreeDefine oldEntity    = new TreeDefine();
        // 设置所有父节点的拼接
        String       parentIds    = "";
        if (!"-1".equals(entity.getParentId())) {
            parentEntity = getByID(entity.getParentId()); 
            parentIds += StringUtil.null2empty(parentEntity.getParentIds()) + "," + entity.getParentId();
        } else {  
            parentIds += entity.getParentId(); 
        }  
        entity.setParentIds(parentIds);
        boolean isCreate = StringUtil.isEmpty(entity.getId());
        if (isCreate) {
            // 设置显示顺序
            Integer maxShowOrder = getDao().getMaxShowOrderByParentId(entity.getParentId());
            if (null == maxShowOrder) {
                maxShowOrder = 0;
            }
            entity.setShowOrder(maxShowOrder + 1);
            if (TreeDefine.T_COFLOW.equals(entity.getType())) {
            	// 工作流节点为父节点，有工作箱子节点（前台展现时，自动从工作流定义中获取）
            	entity.setChild(Boolean.TRUE);
            } else {
            	entity.setChild(Boolean.FALSE);
            }
            // 更新父节点是否有子节点标记
            if (!"-1".equals(entity.getParentId())) {
                if (!parentEntity.getChild()) {
                    parentEntity.setChild(Boolean.TRUE);
                    super.save(parentEntity);
                }
            }
            // 字段节点且是根据数据表中的字段动态生成字段节点的
            /*if (TreeDefine.T_COLUMN_TAB.equals(entity.getType()) && "1".equals(entity.getNodeRule())) {
                entity = getDao().save(entity);
                generateDistinctedNode(entity);
                return updateTreeDefine(parentEntity, oldEntity, entity);
            } else if (TreeDefine.T_COLUMN_TAB.equals(entity.getType()) && "2".equals(entity.getNodeRule())) {
                entity = getDao().save(entity);
                return updateTreeDefine(parentEntity, oldEntity, entity);
            } else {
                entity.setDynamic("0");
                entity.setNodeRule("0");
            }*/
            if (!(TreeDefine.T_COLUMN_TAB.equals(entity.getType()) && entity.getNodeRule().matches("1|2"))
            		&& !(TreeDefine.T_COLUMN_EMP.equals(entity.getType()) && "2".equals(entity.getNodeRule()))
            		&& !(TreeDefine.T_LOGIC_GROUP.equals(entity.getType()) && "2".equals(entity.getNodeRule()))) {
            	entity.setDynamic("0");
                entity.setNodeRule("0");
            }
        } else {    
        	BeanUtils.copy(getByID(entity.getId()), oldEntity);
        }
        entity = getDao().save(entity);
        if (isCreate && (TreeDefine.T_COLUMN_TAB.equals(entity.getType()) && "1".equals(entity.getNodeRule()))) {
            // 字段节点且是根据数据表中的字段动态生成字段节点的
            generateDistinctedNode(entity);
        }
        // 更新columnNames和columnValues
        return updateTreeDefine(parentEntity, oldEntity, entity);
    }

	/**
	 * 保存后修改其columnNames和columnValues，以及更新操作
	 * @param arcTree
	 * @author Administrator
	 * @date 2013-12-17  09:54:05
	 */
	@Transactional
    private TreeDefine updateTreeDefine(TreeDefine parentEntity, TreeDefine oldEntity, TreeDefine entity) {
	    //
	    setColumnNamesAndColumnValues(parentEntity, entity);
	    // 
	    if (!entity.getColumnNames().equals(oldEntity.getColumnNames()) || !entity.getColumnValues().equals(oldEntity.getColumnValues())) {
            String childParentIds = entity.getParentIds().concat(",").concat(entity.getId());
            if (entity.getChild().booleanValue() && (TreeDefine.T_COLUMN_TAB.equals(entity.getType()) 
                    || TreeDefine.T_COLUMN_EMP.equals(entity.getType()))) {
                if (!StringUtil.null2empty(entity.getDbId()).equals(StringUtil.null2empty(oldEntity.getDbId())) 
                        || !StringUtil.null2empty(entity.getValue()).equals(StringUtil.null2empty(oldEntity.getValue()))) {
                    String oldColumnNames  = StringUtil.null2empty(oldEntity.getColumnNames() );
                    String oldColumnValues = StringUtil.null2empty(oldEntity.getColumnValues());
                    String newColumnNames  = entity.getColumnNames();
                    String newColumnValues = entity.getColumnValues();
                    getDao().updateColumnNamesAndColumnValues(oldColumnNames, newColumnNames, oldColumnValues, newColumnValues, childParentIds + "%");
                }
            }
            if (entity.getChild().booleanValue() &&  
                    !StringUtil.null2empty(entity.getTableId()).equals(StringUtil.null2empty(oldEntity.getTableId()))) {
                getDao().updateTableId(entity.getTableId(), childParentIds + "%");
            }
	        getDao().save(entity);
	    }
	    if (TreeDefine.T_COLUMN_TAB.equals(entity.getType()) && StringUtil.isNotEmpty(oldEntity.getId()) 
	    		&& !entity.getDbId().equals(oldEntity.getDbId())) {
	        dropIndex(oldEntity);
	    }
        createIndex(entity);
        return (entity);
    }
	
	/**
	 * qiucs 2014-3-21 
	 * <p>描述: 设置columnNames/columnValues值</p>
	 * @return TreeDefine    返回类型   
	 */
	private TreeDefine setColumnNamesAndColumnValues(TreeDefine parentEntity, TreeDefine entity) {
	    String newColumnNames = "";
	    String newColumnValues = "";
	    if (TreeDefine.T_ROOT.equals(entity.getType())) {
	        newColumnNames  = TreeDefine.EMP_PREFIX + entity.getId();
            newColumnValues = TreeDefine.EMP_PREFIX + entity.getId();
	    } else if (TreeDefine.T_EMPTY.equals(entity.getType()) 
                || TreeDefine.T_COFLOW.endsWith(entity.getType())
                || TreeDefine.T_BOX.endsWith(entity.getType())) {
            newColumnNames  = StringUtil.null2empty(parentEntity.getColumnNames()) + "," + TreeDefine.EMP_PREFIX + entity.getId();
            newColumnValues = StringUtil.null2empty(parentEntity.getColumnValues()) + "," + TreeDefine.EMP_PREFIX + entity.getId();
        } else if (TreeDefine.T_TABLE.equals(entity.getType()) 
                || TreeDefine.T_GROUP.equals(entity.getType())) {
            newColumnNames  = StringUtil.null2empty(parentEntity.getColumnNames()) + "," + TreeDefine.TAB_PREFIX + entity.getId();
            newColumnValues = StringUtil.null2empty(parentEntity.getColumnValues()) + "," + entity.getTableId();
        } else if (TreeDefine.T_COLUMN_TAB.equals(entity.getType()) 
                || TreeDefine.T_COLUMN_EMP.equals(entity.getType())) {
            newColumnNames  = StringUtil.null2empty(parentEntity.getColumnNames()) + "," + getColumnName(entity);
            newColumnValues = StringUtil.null2empty(parentEntity.getColumnValues()) + ","
                    + (("1".equals(entity.getNodeRule())) ? TreeDefine.V_RULE : entity.getValue());
        }
	    entity.setColumnNames(newColumnNames);
        entity.setColumnValues(newColumnValues);
	    return entity;
	}
    /**
     * 获取TreeDefine的columnName
     * @param treeDefine
     * @return
     * @author Administrator
     * @date 2013-12-17  09:51:45
     */
    private String getColumnName(TreeDefine entity) {
        String columnName = "";
        if (TreeDefine.T_COLUMN_TAB.equals(entity.getType())) {
            columnName = getService(ColumnDefineService.class).getColumnNameById(entity.getDbId());
        } else if (TreeDefine.T_COLUMN_EMP.equals(entity.getType())) {
            columnName = entity.getDbId();
        }
        return columnName;
    }

    /**
     * qiucs 2013-12-25 
     * <p>描述: 根据创建的动态节点规则生成动态节点</p>
     * @param  entity    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private void generateDistinctedNode(TreeDefine entity) {
        try {
            String tableName = TableUtil.getTableName(entity.getTableId());
            if (StringUtil.isEmpty(tableName)) return;
            ColumnDefine column = getService(ColumnDefineService.class).getByID(entity.getDbId());
            if (null == column) return;
            
            // 判断父节点是否为根据DISTINCT来生成节点
            TreeDefine parentEntity = getByID(entity.getParentId());
            if ("1".equals(parentEntity.getNodeRule())) { // yes
                List<TreeDefine> pList = getDao().getDynamicNodes(parentEntity.getParentId(), parentEntity.getId());
                if (null == pList || pList.isEmpty()) return;
                
                if ("0".equals(entity.getDataSource())) {
                    // 业务表
                    for (int i = 0; i < pList.size(); i++) {
                        String condition = getCondition(pList.get(i), null);
                        String sql = "SELECT DISTINCT " + column.getColumnName() + " FROM " + tableName + 
                                (StringUtil.isEmpty(condition) ? "" : (" WHERE " + condition)) + " ORDER BY " + column.getColumnName();
                        List<Object> list = DatabaseHandlerDao.getInstance().queryForList(sql);
                        generateDistinctedNode(pList.get(i), entity, column, list);
                    }
                } else if (StringUtil.isNotEmpty(column.getCodeTypeCode())){
                    // 编码表
                    for (int i = 0; i < pList.size(); i++) {
                        List<Code> list = CodeUtil.getInstance().getCodeList(column.getCodeTypeCode());
                        generateDistinctedNode(pList.get(i), entity, null, list);
                    }
                }
            } else { // no
                if ("0".equals(entity.getDataSource())) {
                    // 业务表
                    String condition = getCondition(parentEntity, null);
                    String sql = "SELECT DISTINCT " + column.getColumnName() + " FROM " + tableName + 
                            (StringUtil.isEmpty(condition) ? "" : (" WHERE " + condition)) + " ORDER BY " + column.getColumnName();
                    List<Object> list = DatabaseHandlerDao.getInstance().queryForList(sql);
                    generateDistinctedNode(parentEntity, entity, column, list);
                } else {
                    // 编码表
                    List<Code> list = CodeUtil.getInstance().getCodeList(column.getCodeTypeCode());
                    generateDistinctedNode(parentEntity, entity, null, list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * qiucs 2013-10-25 
     * <p>描述: 根据业务表数据来生成动态节点</p>
     * @param  parentEntity
     * @param  entity
     * @param  column
     * @param  list    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @SuppressWarnings("rawtypes")
    @Transactional
    private void generateDistinctedNode(TreeDefine parentEntity, TreeDefine entity, ColumnDefine column, List list) {
        try {
            if (null == list || list.isEmpty()) return;
            List<TreeDefine> entities = new ArrayList<TreeDefine>();
            Integer maxShowOrder = getDao().getMaxShowOrderByParentId(parentEntity.getId());
            if (null == maxShowOrder) {
                maxShowOrder = 0;
            }
            if (parentEntity.getId().equals(entity.getParentId())) ++maxShowOrder;
            boolean isAsc = TreeDefine.T_ASC.equals(entity.getSortType());
            int len = list.size() - 1, idx;
            for (int i = 0; i < list.size(); i++) {
            	idx = isAsc ? i : len - i;
            	Object obj = list.get(idx);
                String name, value;
                if (obj instanceof Code) {
                    Code code = (Code)obj;
                    name = code.getName();
                    value= code.getValue();
                } else {
                    name = value = String.valueOf(obj);
                    if (StringUtil.isEmpty(name)) continue;
                    if (ConstantVar.DataType.ENUM.equals(column.getDataType())) {
                        name = CommonUtil.getCodeName(column.getCodeTypeCode(), value);
                    }
                    if (StringUtil.isEmpty(name)) {
                        name = "编码值[" + value + "]未找到对应的编码名称";
                    }
                }
                TreeDefine distinctedEntity = new TreeDefine();
                BeanUtils.copy(entity, distinctedEntity);
                distinctedEntity.setId(null);
                distinctedEntity.setParentId(parentEntity.getId());
                distinctedEntity.setParentIds(parentEntity.getParentIds().concat(",").concat(parentEntity.getId()));
                distinctedEntity.setName(name);
                distinctedEntity.setValue(value);
                distinctedEntity.setDynamic("1");
                distinctedEntity.setNodeRule("0");
                distinctedEntity.setShowOrder(++maxShowOrder);
                distinctedEntity.setChild(Boolean.FALSE);
                distinctedEntity.setRemark("由(" + entity.getName() + ")生成的节点");
                distinctedEntity.setDynamicFromId(entity.getId());
                distinctedEntity.setSortType(null);
                entities.add(distinctedEntity);
            }
            // 保存DISTINCTED节点
            getDao().save(entities);
            for (TreeDefine updateEntity : entities) {
                updateTreeDefine(parentEntity, new TreeDefine(), updateEntity);
            }
            // 更新父节点
            if (null != parentEntity.getChild() && false == parentEntity.getChild().booleanValue()) {
                parentEntity.setChild(Boolean.TRUE);
                getDao().save(parentEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * qiucs 2013-10-23 
     * <p>描述: 自动生成工作箱</p>
     * @param  entity
     * @param  auto
     * @return TreeDefine    返回类型   
     */
    @Transactional
    public TreeDefine save(TreeDefine entity, boolean auto) {
        entity = save(entity);
        if (auto) {
            getDao().save(newTreeDefine(entity.getId(), WorkflowUtil.Box.applyfor));
            getDao().save(newTreeDefine(entity.getId(), WorkflowUtil.Box.todo));
            getDao().save(newTreeDefine(entity.getId(), WorkflowUtil.Box.hasdone));
            getDao().save(newTreeDefine(entity.getId(), WorkflowUtil.Box.complete));
            getDao().save(newTreeDefine(entity.getId(), WorkflowUtil.Box.toread));
            // 更新父节点是否有子节点标记
            entity.setChild(Boolean.TRUE);
            getDao().save(entity);
        }
        return (entity);
    }
    
    /**
     * qiucs 2015-7-23 下午5:34:32
     * <p>描述: 获取节点的过滤条件 </p>
     * @return String
     */
    protected String getCondition(TreeDefine entity, String[] idArr) {
        StringBuffer condition = new StringBuffer();
        String[] columnArr = entity.getColumnNames().split(",");
        String[] valueArr  = entity.getColumnValues().split(",");
        String tableId = entity.getTableId();
        // 记录字段节点（跨表）对应的动态节点的值，因为动态字段节点（跨表）下的子节点为模板节点
        String val = (null == idArr ? null : idArr[idArr.length - 2]);
        boolean isLabel = true;
        String columnFilter = null;
        for (int i = 0; i < columnArr.length; i++) {
        	if (isLabel && columnArr[i].startsWith(TreeDefine.TAB_PREFIX)) isLabel = false;
        	if (columnArr[i].startsWith(TreeDefine.TAB_PREFIX)) {
        		// 物理表组或物理表下的过滤条件
        		String idStr = columnArr[i].replaceFirst(TreeDefine.TAB_PREFIX, "");
        		TreeDefine tabEntity = getByID(idStr);
        		if (null != tabEntity && StringUtil.isNotEmpty(tabEntity.getColumnFilter())) {
        			columnFilter = AppDefineUtil.processComplexFilterItem(tabEntity.getColumnFilter());
        		}
        	} else if (!columnArr[i].startsWith(TreeDefine.EMP_PREFIX)) {
        		// 跨表字段和本表字段的过滤条件
                String columnName = getColumnName(columnArr[i], tableId, isLabel);
                String columnValue= ((isLabel && null != val) ? val : valueArr[i]);
                if (i < valueArr.length) {
                    condition.append(AppDefineUtil.RELATION_AND).append(columnName).append("='").append(columnValue).append("'");
                } else {
                    condition.append(AppDefineUtil.RELATION_AND).append(AppDefineUtil.processNull(columnName, "0"));
                }
            }
        }
        if (StringUtil.isNotEmpty(columnFilter)) condition.insert(0, columnFilter);
        if (condition.length() > 0) condition.delete(0, AppDefineUtil.RELATION_AND.length());
        return String.valueOf(condition);
    }
    
    /**
     * qiucs 2014-11-26 
     * <p>描述: </p>
     * @param  column  --字段名称或字段标签
     * @param  tableId --表ID
     * @param  isLabel --是否为字段标签
     * @throws
     */
    private String getColumnName(String column, String tableId, boolean isLabel) {
        if (!isLabel) return column;
        return getService(ColumnDefineService.class).getColumnNameByColumnLabelAndTableId(column, tableId);
    }
    
    /**
     * qiucs 2013-10-23 
     * <p>描述: 创建工作箱对象</p>
     * @param  parentId
     * @param  box
     * @return TreeDefine    返回类型   
     */
    private TreeDefine newTreeDefine(String parentId, String box) {
        TreeDefine tree = new TreeDefine();
        tree.setType("9");
        tree.setParentId(parentId);
        tree.setName(WorkflowUtil.getBoxMap().get(box));
        tree.setValue(box);
        tree.setChild(Boolean.FALSE);
        tree.setShowOrder(WorkflowUtil.getBoxIndex(box));
        return tree;
    }
    
    /**
     * qiucs 2013-8-27 
     * <p>描述: 字段布局继承父层布局</p>
     * @param  parentId
     * @return Object    返回类型   
     */
    public Object findParentLayout(String parentId) {
        TreeDefine entity = getByID(parentId);
        if (null == entity ||
                TreeDefine.T_TABLE.equals(entity.getType()) || TreeDefine.T_COLUMN_TAB.equals(entity.getType())) {
            return entity;
        }
        return findParentLayout(entity.getParentId());
    }
	
	/**
	 * qiucs 2013-8-27 
	 * <p>描述: 拖拽调整显示顺序</p>
	 * @param  parentId
	 * @param  sourceIds
	 * @param  targetId    设定参数   
	 * @return void    返回类型   
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
            increaseNum = len;
            differLen = tShowOrder - sBeginShowOrder;
            // tShowOrder-1 < showOrder < sBeginShowOrder
            begin = tShowOrder-1;
            end   = sBeginShowOrder;
        } else { 
            // 向下拖动
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

	/**
	 * qiucs 2013-9-8 
	 * <p>描述: 根节点树下拉框列表</p>
	 * @return Object    返回类型   
	 */
    public Object getComboOfTrees() {
        List<DhtmlxComboOption> opts = Lists.newArrayList();
        try {
            List<Object[]> list = getDao().findRootTrees();
            for (Object[] obj : list) {
                DhtmlxComboOption option = new DhtmlxComboOption();
                option.setValue(StringUtil.null2empty(obj[0]));
                option.setText(StringUtil.null2empty(obj[1]));
                
                opts.add(option);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return opts;
    }
    
    /**
     * qiucs 2013-9-26 
     * <p>描述: 获取指定结点下的所有子节点</p>
     * @param  rootId
     * @param  list
     * @return List<TreeDefine>    返回类型   
     */
    public List<TreeDefine> getAllChildren(String rootId, List<TreeDefine> list) {
        
        if (null == list) list = Lists.newArrayList();
        
        List<TreeDefine> rlt = getDao().getByParentId(rootId);
        if (null == rlt || rlt.isEmpty()) return list;
        for (TreeDefine tree : rlt) {
            list.add(tree);
            getAllChildren(tree.getId(), list);
        }
        
        return list;
    }
    
    /**
     * qiucs 2013-11-5 
     * <p>描述: 获取当前节点的所有父节点</p>
     * @param  id
     * @param  list
     * @return List<TreeDefine>    返回类型   
     */
    public List<TreeDefine> getAllParents(String id, List<TreeDefine> list) {
        if (null == list) list = Lists.newArrayList();
        TreeDefine entity = getByID(id);
        if (null == entity) return list;
        list.add(0, entity);
        return getAllParents(entity.getParentId(), list);
    }
      
    /**
	 * 查询源表和关联表记录数目
	 * @param tableId, relateTableId
	 */
    @Transactional
	public long getTotalTableRelation(String tableId,String relateTableId){
		return getDao().getTotalTableRelation(tableId, relateTableId);
	}

	/* (non-Javadoc)
	 * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
	 * @author Administrator
	 * @date 2013-10-23 16:30:36
	 */
	@Override
	@Transactional
	public void delete(String ids) {
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            Long cnt = getDao().countByParentId(id);
            if (null != cnt && cnt.longValue() > 0) {
                throw new RuntimeException(id);
            } else {
                // 1. if parent node has only one child, set parent node's child=false
                TreeDefine entity = getByID(id);
                if ("1".equals(entity.getNodeRule())) {
                    // 当动态节点规则被删除时，把对应的节点转化为普通节点
                    getDao().updateDynamicNodes(id);
                }
                Long pcnt = getDao().countByParentId(entity.getParentId());
                if (pcnt == 1 && !"-1".equals(entity.getParentId())) {
                    TreeDefine parentEntity = getByID(entity.getParentId());
                    parentEntity.setChild(Boolean.FALSE);
                    super.save(parentEntity);
                }
                // 2. delete current note
                getDao().delete(id);
                // 3. drop index;
                dropIndex(entity);
                // 4. if root node, update module
                if (TreeDefine.T_ROOT.equals(entity.getType())) {
                    getService(ModuleService.class).updateByTreeId(entity.getId());
                } else if (TreeDefine.T_TABLE.equals(entity.getType())) {
                    /*List<String> componentVersionIdList = getDao().getComponentVersionIdsByRootId(entity.getRootId());
                    if (CollectionUtils.isNotEmpty(componentVersionIdList)) {
                        for (String componentVersionId : componentVersionIdList) {
                            deleteReserveZone(componentVersionId, entity.getTable1Id(), entity.getArea1Id(), 1);
                            deleteReserveZone(componentVersionId, entity.getTable2Id(), entity.getArea2Id(), 2);
                            deleteReserveZone(componentVersionId, entity.getTable3Id(), entity.getArea3Id(), 3);
                        }
                    }*/
                }
            }
        }
	}
	
	/**
	 * qiucs 2013-10-30 
	 * <p>描述: 查找用来生成动态节点(业务表)的所有记录</p>
	 * @param  tableId
	 * @return List<TreeDefine>    返回类型   
	 */
	public List<TreeDefine> getRuleTreeNodes(String tableId) {
	    if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
	        return getDao().getRuleTreeNodesOfOracle(tableId);
	    } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
	        return getDao().getRuleTreeNodesOfSqlserver(tableId);
	    }
	    return null;
	}
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 查找用来生成动态节点(编码表)的所有记录</p>
     * @param  tableId
     * @return List<TreeDefine>    返回类型   
     */
    public List<TreeDefine> getCodeRuleTreeNodes() {
        if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getCodeRuleTreeNodesOfOracle();
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getCodeRuleTreeNodesOfSqlserver();
        }
        return null;
    }
	
	/**
	 * qiucs 2013-11-15 
	 * <p>描述: 为树节中的字段节点建索引</p>
	 * @param  entity
	 */
	@Transactional
    protected void createIndex(TreeDefine entity) {
        if (TreeDefine.T_COLUMN_TAB.equals(entity.getType())) {
            String tableName = TableUtil.getTableName(entity.getTableId());
            String columnName = getService(ColumnDefineService.class).getColumnNameById(entity.getDbId());
            DatabaseHandlerDao.getInstance().createOneColumnIndex(ConstantVar.IndexPrefix.TREE, tableName, columnName);
        } else if (TreeDefine.T_TABLE.equals(entity.getType()) || TreeDefine.T_GROUP.equals(entity.getType())) {
            List<String> list = getColumnNodes(entity);
            String tableName = TableUtil.getTableName(entity.getTableId());
            for (int i = 0; i < list.size(); i++) {
                String columnName = list.get(i);
                DatabaseHandlerDao.getInstance().createOneColumnIndex(ConstantVar.IndexPrefix.TREE, tableName, columnName);
            }
        }
    }
    
    /**
     * qiucs 2013-11-15 
     * <p>描述: 删除树节中的字段节点索引</p>
     * @param  entity
     */
    @Transactional
    protected void dropIndex(TreeDefine entity) {
    	if (StringUtil.isEmpty(entity.getTableId())) return;
        String tableName = TableUtil.getTableName(entity.getTableId());
        if (StringUtil.isEmpty(tableName)) return;
        if (TreeDefine.T_COLUMN_TAB.equals(entity.getType())) {
            String columnName = getService(ColumnDefineService.class).getColumnNameById(entity.getDbId());
            DatabaseHandlerDao.getInstance().dropOneColumnIndex(ConstantVar.IndexPrefix.TREE, tableName, columnName);
        } else if (TreeDefine.T_TABLE.equals(entity.getType())) {
            List<String> list = getColumnNodes(entity);
            for (int i = 0; i < list.size(); i++) {
                String columnName = list.get(i);
                DatabaseHandlerDao.getInstance().dropOneColumnIndex(ConstantVar.IndexPrefix.TREE, tableName, columnName);
            }
        }
    }
    
    /**
     * qiucs 2013-11-15 
     * <p>描述: 查找表节点的所有父节点中所有为字段节点</p>
     * @param  tableNodeEntity
     * @return List<String>    返回类型   
     */
    protected List<String> getColumnNodes(TreeDefine tableNodeEntity) {
        List<String> list = new ArrayList<String>();
        String[] columnArr = tableNodeEntity.getColumnNames().split(",");
        for (int i = 0; i < columnArr.length; i++) {
            if (!columnArr[i].startsWith(TreeDefine.EMP_PREFIX) && !columnArr[i].startsWith(TreeDefine.TAB_PREFIX)) {
                String columnName = getService(ColumnDefineService.class).getColumnNameByColumnLabelAndTableId(columnArr[i], tableNodeEntity.getTableId());
                list.add(columnName);
            }
        }
        return list;
    }
	/**
	 * 验证上级所有的字段节点是否在表中
	 * @param parentId
	 * @param tableId
	 * @return
	 * @author Administrator
	 * @date 2013-11-19  12:52:50
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public MessageModel checkColumnLabel(String parentId, String tableId) {
	    TreeDefine arcTree = getDao().findOne(parentId);
        try {
            if (null == arcTree || TreeDefine.T_ROOT.equals(arcTree.getType())) {
                return MessageModel.trueInstance("OK");
            } else if (TreeDefine.T_COLUMN_EMP.equals(arcTree.getType())) {
                String columnName = getService(ColumnDefineService.class).getColumnNameByColumnLabelAndTableId(arcTree.getDbId(), tableId);
                if (StringUtil.isEmpty(columnName)) {
                    return MessageModel.falseInstance("字段标签节点【" + arcTree.getName() + "】的字段名称与所选择的表不匹配");
                }
            } 
        } catch (Exception e) {
            log.error("验证上级所有的字段节点是否在表中出错", e);
        }
        return checkColumnLabel(arcTree.getParentId(), tableId);
    }
	 /**
	 * 遍历上级所有结点中的表结点
	 * @param parentId
	 * @return
	 * @author Administrator
	 * @date 2013-11-19  12:53:26
	 */
    public TreeDefine checkTable(String parentId) {
        TreeDefine aTree = getDao().findByParentId(parentId);
        if (null == aTree) {
            return new TreeDefine();
        } else if (TreeDefine.T_ROOT.equals(aTree.getType())) {
            aTree = new TreeDefine();
        } else if (!TreeDefine.T_TABLE.equals(aTree.getType())) {
            aTree = checkTable(aTree.getId());
        }
        return aTree;
    }
	 /**
	 * 遍历上级所有结点中的字段结点
	 * @param parentId
	 * @param table1Id
	 * @return
	 * @author Administrator
	 * @date 2013-11-19  12:54:20
	 */
    public TreeDefine checkColumn(String parentId, String table1Id) {
        TreeDefine arcTree = getByID(parentId);
        if (null == arcTree) {
            return new TreeDefine();
        } else {
            String dbId = arcTree.getDbId();
            String columnName = dbId;
            if (StringUtil.isNotEmpty(arcTree.getTableId())) {
                columnName = getDao(ColumnDefineDao.class, ColumnDefine.class).findColumnNameById(dbId);
            }
            Long cnt = getDao(ColumnDefineDao.class, ColumnDefine.class).countByColumnNameAndTableId(columnName,
                    table1Id);
            if (TreeDefine.T_ROOT.equals(arcTree.getType())) {
                arcTree = new TreeDefine();
            } else if (cnt != 1
                    && (TreeDefine.T_COLUMN_TAB.equals(arcTree.getType()) || TreeDefine.T_COLUMN_EMP.equals(arcTree
                            .getType()))) {
                return arcTree;
            } else {
                arcTree = checkColumn(arcTree.getParentId(), table1Id);
            }
        }
        return arcTree;
    }
    
    /**
     * 根据树根节点名称获取树根节点
     * 
     * @param name 树根节点名称
     * @return TreeDefine 返回类型
     */
    public TreeDefine getTreeByName(String name) {
        return getDao().getTreeByName(name);
    }
	
	/**
	 * qiucs 2013-11-28 
	 * <p>描述: 根据表ID更新树配置</p>
	 * @param  tableId    设定参数   
	 */
	@Transactional
	public void updateByTableId(String tableId) {
	    getDao().updateByDbId(tableId);
	    getDao().updateByTableId(tableId);
	}
	
	/**
	 * qiucs 2014-11-26 
	 * <p>描述: 检查字段标签是否在子节点（表节点或表组节点）中</p>
	 * @return MessageModel    返回类型   
	 * @throws
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
	public MessageModel checkTableNode(String id, String columnLabel) {
	    List<TreeDefine> list = getAllChildren(id, null);
	    for (TreeDefine entity : list) {
	        if (TreeDefine.T_TABLE.equals(entity.getType()) || TreeDefine.T_GROUP.equals(entity.getType())) {
	            String columnName = getService(ColumnDefineService.class).getColumnNameByColumnLabelAndTableId(columnLabel, entity.getTableId());
	            if (StringUtil.isEmpty(columnName)) {
	                return MessageModel.falseInstance("所选择字段标签节点不在节点【" + entity.getName() + "】表中！");
	            }
	        }
	    }
		return MessageModel.trueInstance("OK");
	}

	/**
	 * 树节点复制功能
	 * @param nodeIdCopyTo 复制到的树节点
	 * @param treeNodeId 被复制的树节点
	 */
	@Transactional
	public void copyTreeNode(String nodeIdCopyTo, String treeNodeId) {
		TreeDefine arcTree =getByID(treeNodeId);//原来被复制的父节点
		TreeDefine clone = (TreeDefine)arcTree.clone();
		clone.setParentId(nodeIdCopyTo);
		save(clone); 
		String newId = clone.getId();
		
		List<TreeDefine> list = getDao().getByParentId(treeNodeId);
		if(list.size() > 0){
			for(TreeDefine td : list){
				copyTreeNode(newId,td.getId());
			}
		}
	}
	
	/**
	 * 描述：判断该树节点的上层或者下层节点中是否存在表节点
	 * @param parentId
	 * @return
	 */
	private boolean isHaveTableNode(String parentId, String upOrDown){
		/*判断该节点的上层节点是否有表节点*/
		if("up".equals(upOrDown)){
			List<TreeDefine> list = getAllParents(parentId,null);
			if(list.size() > 0){
				for(TreeDefine td : list){
					if(TreeDefine.T_TABLE.equals(td.getType())){
						return true;
					}
				}
			}
		}else if("down".equals(upOrDown)){/*判断该节点的下层节点是否有表节点*/
			List<TreeDefine> list = getAllChildren(parentId,null);
			if(list.size() > 0){
				for(TreeDefine td : list){
					if(TreeDefine.T_TABLE.equals(td.getType())){
						return true;
					}
				}
				
			}
		}
		return false;
	}

	/**
	 * 判断是否满足复制条件，复制到的节点称为节点1，被复制的节点称为节点2
	 * @param nodeIdCopyTo
	 * @param treeNodeId
	 * @return
	 */
	public Object checkTreeNodeCopy(String nodeIdCopyTo, String treeNodeId) {
		TreeDefine newTree = getDao().findOne(nodeIdCopyTo);
		TreeDefine oldTree = getDao().findOne(treeNodeId);
		if(TreeDefine.T_BOX.equals(newTree.getType())){/*若节点1为工作箱，则节点2必须要是该表的工作箱*/
			if(!TreeDefine.T_BOX.equals(oldTree.getType())){
				return (new MessageModel(Boolean.FALSE, "不能复制！"));
			}else{
				if(!StringUtil.null2empty(newTree.getTableId()).equals(StringUtil.null2empty(oldTree.getTableId()))){
					return (new MessageModel(Boolean.FALSE, "不能复制！"));
				}
			}
		}else if(TreeDefine.T_EMPTY.equals(newTree.getType())){/*若节点1为空节点*/
			if(isHaveTableNode(nodeIdCopyTo,"up")){/*若节点1的上层节点有无表节点*/
				/*若节点2为表节点、空字段节点、工作箱，则终止复制*/
				if(TreeDefine.T_TABLE.equals(oldTree.getType()) || TreeDefine.T_COLUMN_EMP.equals(oldTree.getType()) || TreeDefine.T_BOX.equals(oldTree.getType())){
					return (new MessageModel(Boolean.FALSE, "不能复制！"));
				}
				/*若节点2为表字段节点，则必须是同表*/
				if(TreeDefine.T_COLUMN_TAB.equals(oldTree.getType())){
					if(!StringUtil.null2empty(newTree.getTableId()).equals(StringUtil.null2empty(oldTree.getTableId()))){//是否同一张表
						return (new MessageModel(Boolean.FALSE, "不能复制！"));
					}
				}
				/*若节点2为空节点，则先判断上层是否有表节点*/
				if(TreeDefine.T_EMPTY.equals(oldTree.getType())){
					/*先判断上层是否有表节点*/
					if(isHaveTableNode(treeNodeId,"up")){
						if(!StringUtil.null2empty(newTree.getTableId()).equals(StringUtil.null2empty(oldTree.getTableId()))){//是否同一张表
							return (new MessageModel(Boolean.FALSE, "不能复制！"));
						}
					}
					/*先判断下层是否有表节点*/
					if(isHaveTableNode(treeNodeId,"down")){
						return (new MessageModel(Boolean.FALSE, "不能复制！"));
					}else{
						for(TreeDefine td : getAllChildren(treeNodeId,null)){
							if(TreeDefine.T_COLUMN_TAB.equals(td.getType()) || TreeDefine.T_BOX.equals(td.getType())){
								return (new MessageModel(Boolean.FALSE, "不能复制！"));
							}
						}
					}
				}
			}
		}else if(TreeDefine.T_COLUMN_EMP.equals(newTree.getType())){/*若节点1为空字段节点，则只能复制空节点、空字段节点、表节点*/
			if(TreeDefine.T_COLUMN_TAB.equals(oldTree.getType()) || TreeDefine.T_BOX.equals(oldTree.getType())){
				return (new MessageModel(Boolean.FALSE, "不能复制！"));
			}
			if(TreeDefine.T_EMPTY.equals(oldTree.getType())){/*如果节点1是空节点，则判断上层是否有表节点或者工作箱*/
				for(TreeDefine td : getAllParents(treeNodeId,null)){
					if(TreeDefine.T_TABLE.equals(td.getType()) || TreeDefine.T_BOX.equals(td.getType())){
						return (new MessageModel(Boolean.FALSE, "不能复制！"));
					}
				}
			}
		}else if(TreeDefine.T_TABLE.equals(newTree.getType())){/*如果节点1是表节点*/
			if(checkIsSameTable(oldTree,newTree)){
				return (new MessageModel(Boolean.FALSE, "不能复制！"));
			}
		}else if(TreeDefine.T_COLUMN_TAB.equals(newTree.getType())){/*如果节点1是表字段节点*/
			if(checkIsSameTable(oldTree,newTree)){
				return (new MessageModel(Boolean.FALSE, "不能复制！"));
			}
		}else if(TreeDefine.T_ROOT.equals(newTree.getType())){/*如果节点1是根节点，则不能复制根节点和表字段节点*/
			if(TreeDefine.T_ROOT.equals(oldTree.getType()) || TreeDefine.T_COLUMN_TAB.equals(oldTree.getType())){
				return (new MessageModel(Boolean.FALSE, "不能复制！"));
			}
		}
		return (new MessageModel(Boolean.TRUE, "OK"));
	}
	
	/*判断被复制的节点是否是该表的字段节点或空节点（若是空节点，则下层无子节点或者一定要是空节点||该表的节点）*/
	private boolean checkIsSameTable(TreeDefine oldTree,TreeDefine newTree){
		if(TreeDefine.T_EMPTY.equals(oldTree.getType())){
			for(TreeDefine td : getAllChildren(oldTree.getId(),null)){
				if(!StringUtil.null2empty(td.getTableId()).equals(StringUtil.null2empty(newTree.getTableId()))){
					return true;
				}
			}
			return false;
		}else if(TreeDefine.T_COLUMN_TAB.equals(oldTree.getType())){
			if(!StringUtil.null2empty(newTree.getTableId()).equals(StringUtil.null2empty(oldTree.getTableId()))){//是否同一张表
				return true;
			}
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 获取实时动态节点（生成树时直接从数据库中查询出的）
	 * 
	 * @param parentId 父节点ID
	 * @return List<TreeDefine>
	 */
    @SuppressWarnings("unchecked")
    public List<TreeDefine> getByNodeRule2(String parentId, Map<String, Object> paramMap) {
	    List<TreeDefine> treeDefineList = new ArrayList<TreeDefine>();
	    String parentNodeId = null;
	    TreeDefine parentNode = null;
	    if (parentId.startsWith("NR2_")) {
	    	// 实时动态节点（多层时），获取动态节点对应动态父节点
    	    String[] strs = parentId.split("_");
    	    parentNodeId = strs[1];
    	    TreeDefine dbParentNode = getDao().findOne(parentNodeId);
    	    HttpSession session = ServletActionContext.getRequest().getSession();
            Map<String, TreeDefine> map = (Map<String, TreeDefine>) session.getAttribute(dbParentNode.getRootId());
            parentNode = map.get(parentId);
            // 如果是字段节点（跨表）动态节点下子节点的实时动态节点
            if (parentId.endsWith("_NT4")) paramMap.put("_NT4", strs);
	    } else {
	    	if (parentId.endsWith("_NT4")) {
	    		String[] strs = parentId.split("_");
	    		paramMap.put("_NT4", strs);
	    		parentNodeId = strs[0];
	    	} else {
	    		parentNodeId = parentId;
	    	}
	    }
	    List<TreeDefine> list = getDao().getByParentIdAndNodeRule(parentNodeId, "2");
	    if (CollectionUtils.isNotEmpty(list)) {
	        for (TreeDefine treeDefine : list) {
	        	if (TreeDefine.T_COLUMN_EMP.equals(treeDefine.getType())) {
	        		// 字段节点（跨表）
	        		treeDefineList.addAll(getColumnLabelNodes(treeDefine));
	        	} else {
	        		treeDefineList.addAll(generateDistinctedNode2(treeDefine, parentNode, paramMap));
	        	}
            }
	    }
	    return treeDefineList;
	}
	
	/**
     * qiucs 2013-12-25 
     * <p>描述: 获取实时动态节点（生成树时直接从数据库中查询出的）</p>
     * @param  entity    设定参数   
     * @return List<TreeDefine>    返回类型   
     * @throws
     */
    @SuppressWarnings("unchecked")
    @Transactional
    private List<TreeDefine> generateDistinctedNode2(TreeDefine entity, TreeDefine parentEntity, Map<String, Object> paramMap) {
        List<TreeDefine> treeDefineList = new ArrayList<TreeDefine>();
        try {
            String tableName = TableUtil.getTableName(entity.getTableId());
            if (StringUtil.isEmpty(tableName)) return treeDefineList;
            ColumnDefine column = getService(ColumnDefineService.class).getByID(entity.getDbId());
            if (null == column) return treeDefineList;
            
            // 判断父节点是否为根据DISTINCT来生成节点
            if (parentEntity == null) {
                parentEntity = getByID(entity.getParentId());
            }
            
            String filter = getAuthorityFilter(column.getTableId(), paramMap);
            
            String[] idArr = null;
            if (paramMap.containsKey("_NT4")) idArr = (String[])paramMap.get("_NT4");
            
            if ("1".equals(parentEntity.getNodeRule())) { // yes
                List<TreeDefine> pList = getDao().getDynamicNodes(parentEntity.getParentId(), parentEntity.getId());
                if (null == pList || pList.isEmpty()) return treeDefineList;
                
                if ("0".equals(entity.getDataSource())) {
                    // 业务表
                    for (int i = 0; i < pList.size(); i++) {
                        String condition = getCondition(pList.get(i), idArr);
                        if (StringUtil.isEmpty(condition)) condition = " 1=1 ";
                        condition = condition + filter;
                        String sql = "SELECT " + column.getColumnName() + ", COUNT(*) FROM " + tableName + 
                                (StringUtil.isEmpty(condition) ? "" : (" WHERE " + condition)) + " GROUP BY " + column.getColumnName();
                        List<Object[]> list = DatabaseHandlerDao.getInstance().queryForList(sql);
                        treeDefineList = generateDistinctedNode2(pList.get(i), entity, column, list, paramMap);
                    }
                } else if (StringUtil.isNotEmpty(column.getCodeTypeCode())){
                    // 编码表
                    for (int i = 0; i < pList.size(); i++) {
                        List<Code> list = CodeUtil.getInstance().getCodeList(column.getCodeTypeCode());
                        treeDefineList = generateDistinctedNode2(pList.get(i), entity, null, list, paramMap);
                    }
                }
            } else { // no
                if ("0".equals(entity.getDataSource())) {
                    // 业务表
                    String condition = getCondition(parentEntity, idArr);
                    if (StringUtil.isEmpty(condition)) condition = " 1=1 ";
                    condition = condition + filter;
                    String sql = "SELECT " + column.getColumnName() + ", COUNT(*) FROM " + tableName + 
                            (StringUtil.isEmpty(condition) ? "" : (" WHERE " + condition)) + " GROUP BY " + column.getColumnName() + 
                            " ORDER BY " + column.getColumnName();
                    List<Object[]> list = DatabaseHandlerDao.getInstance().queryForList(sql);
                    treeDefineList = generateDistinctedNode2(parentEntity, entity, column, list, paramMap);
                } else {
                    // 编码表
                    List<Code> list = CodeUtil.getInstance().getCodeList(column.getCodeTypeCode());
                    treeDefineList = generateDistinctedNode2(parentEntity, entity, column, list, paramMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return treeDefineList;
    }
    
    /**
     * qiucs 2013-10-25 
     * <p>描述: 获取实时动态节点（生成树时直接从数据库中查询出的）</p>
     * @param  parentEntity
     * @param  entity
     * @param  column
     * @param  list    设定参数   
     * @return List<TreeDefine>    返回类型   
     * @throws
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional
    private List<TreeDefine> generateDistinctedNode2(TreeDefine parentEntity, TreeDefine entity, ColumnDefine column, List list, Map<String, Object> paramMap) {
        List<TreeDefine> treeDefineList = new ArrayList<TreeDefine>();
        try {
        	boolean isDynamicChild = paramMap.containsKey("_NT4");
        	String[] idArr = null;
        	String ecSuffix = null;
        	if (isDynamicChild) {
        		idArr = (String[])paramMap.get("_NT4");
        		ecSuffix = "_" + idArr[idArr.length - 2] + "_NT4";
        	}
            if (null == list || list.isEmpty()) return treeDefineList;
            Integer maxShowOrder = getDao().getMaxShowOrderByParentId(parentEntity.getId());
            if (null == maxShowOrder) {
                maxShowOrder = 0;
            }
            if (parentEntity.getId().equals(entity.getParentId())) ++maxShowOrder;
            Boolean isAsc = TreeDefine.T_ASC.equals(entity.getSortType());
            int len = list.size() - 1, idx = 0;
            for (int i = 0; i < list.size(); i++) {
            	idx = isAsc ? i : (len - i);
            	Object obj = list.get(idx);
                String name, value;
                if (obj instanceof Code) {
                    Code code = (Code)obj;
                    name = code.getName();
                    value= code.getValue();
                } else {
                    Object[] objArr = (Object[])obj;
                    name = value = StringUtil.null2empty(objArr[0]);
                    //if (StringUtil.isEmpty(name)) continue;
                    if (ConstantVar.DataType.ENUM.equals(column.getDataType())) {
                        name = CommonUtil.getCodeName(column.getCodeTypeCode(), value);
                    }
                    if (StringUtil.isEmpty(name)) {
                        name = "未知名称";
                    }
                    if (StringUtil.isBooleanTrue(entity.getShowNodeCount()) 
                            && StringUtil.isBooleanTrue(String.valueOf(paramMap.get("showNodeCount")))) {
                        name = name.concat(" (").concat(String.valueOf(objArr[1])).concat(")");
                    }
                }
                TreeDefine distinctedEntity = new TreeDefine();
                BeanUtils.copy(entity, distinctedEntity);
                String nodeId = "NR2_" + entity.getId() + "_" + StringUtil.null2empty(parentEntity.getValue()) + "_" + value;
                if (isDynamicChild) nodeId += ecSuffix;
                distinctedEntity.setId(nodeId);
                distinctedEntity.setParentId(parentEntity.getId());
                distinctedEntity.setParentIds(parentEntity.getParentIds().concat(",").concat(parentEntity.getId()));
                distinctedEntity.setName(name);
                distinctedEntity.setValue(value);
                distinctedEntity.setColumnNames(parentEntity.getColumnNames() + "," + column.getColumnName());
                distinctedEntity.setColumnValues(parentEntity.getColumnValues() + "," + value);
                distinctedEntity.setDbId(entity.getDbId());
                distinctedEntity.setDynamic("1");
                distinctedEntity.setNodeRule("0");
                distinctedEntity.setShowOrder(++maxShowOrder);
                distinctedEntity.setRemark("由(" + entity.getName() + ")生成的节点");
                distinctedEntity.setDynamicFromId(entity.getId());
                distinctedEntity.setSortType(null);
                distinctedEntity.setChild(entity.getChild());
                treeDefineList.add(distinctedEntity);
                HttpSession session = ServletActionContext.getRequest().getSession();
                Map<String, TreeDefine> map = (Map<String, TreeDefine>) session.getAttribute(entity.getRootId());
                map.put(nodeId, distinctedEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return treeDefineList;
    }
    
    /**
     * qiucs 2014-9-28 
     * <p>描述: 获取树节点过滤条件</p>
     * @param  tableId
     * @return String    返回类型   
     * @throws
     */
    public String getAuthorityFilter(String tableId, Map<String, Object> paramMap) {
        String filter = "";
        try {
            String componentVersionId = String.valueOf(paramMap.get("componentVersionId"));
            String moduleId = String.valueOf(paramMap.get("moduleId"));
            String menuId   = String.valueOf(paramMap.get("menuId"));
            Module m = ModuleUtil.getModule(moduleId);
            StringBuilder componentName = new StringBuilder(m.getComponentClassName());
            StringBuilder serviceName   = componentName.replace(0, 1, String.valueOf(Character.toLowerCase(componentName.charAt(0)))).append("Service");
            Object service = null;
            try {
                service = getService(String.valueOf(serviceName));
            } catch (Exception e) {
                log.warn("构件（" + m.getName() + "）后台service（" + serviceName + "）类不存在或未编译，请检查！");
            }
            if (null == service) {
                return getService(ShowModuleService.class).buildControlFilter(tableId, componentVersionId, moduleId, menuId, new HashMap<String, Object>());
            }
            Class<?> clazz = service.getClass();
            Method buildControlFilter = clazz.getDeclaredMethod("buildControlFilter", String.class, String.class, String.class, String.class, Map.class);
            filter = (String)buildControlFilter.invoke(service, tableId, componentVersionId, moduleId, menuId, new HashMap<String, Object>());
        } catch (Exception e) {
            log.error("获取树节点过滤条件出错", e);
        }
        return String.valueOf(filter);
    }
    
    /**
     * qiucs 2014-9-29 
     * <p>描述: 是否有配置数据数量节点</p>
     * @param  rootId
     * @return MessageModel true:有 , false: 无
     * @throws
     */
    public MessageModel hasShowNodeCount(String rootId) {
        Long cnt = getDao().countShowNodeCount(rootId);
        if (cnt > 0)  return MessageModel.trueInstance("OK");
        return MessageModel.falseInstance("OK");
    }
    
    /**
     * qiucs 2014-10-22 
     * <p>描述: 根据父节点获取子节点</p>
     */
    public List<TreeDefine> getByParentId(String parentId) {
        return getDao().getByParentId(parentId);
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 获取根节点</p>
     */
    public List<TreeDefine> getRootNodes(String id) {
        TreeDefine entity = getByID(id);
        List<TreeDefine> roots = new ArrayList<TreeDefine>();
        roots.add(entity);
        if (StringUtil.isBooleanTrue(entity.getShowRoot())) {
            return roots;
        }
        // 如果是工作流节点直接返回工作箱
        if (TreeDefine.T_COFLOW.equals(entity.getType()))  {
        	return getWorkflowBoxNodes(entity);
        }
        
        List<TreeDefine> children = getByParentId(entity.getId());
        if (null == children || children.isEmpty()) {
            return roots;
        }
        for (int i = children.size() - 1; i > -1 ; i--) {
        	TreeDefine e = children.get(i);
        	if (TreeDefine.RULE_TRIGGER.equals(e.getNodeRule())) {
        		children.remove(i); 
        	} else if (TreeDefine.RULE_REALTIME.equals(e.getNodeRule())) {
        		children.remove(i);
        		children.addAll(i, getColumnLabelNodes(e));
        	} else if (TreeDefine.T_LOGIC_GROUP.equals(e.getType())) {
        		children.remove(i);
        		children.addAll(i, getLogicGroupNodes(e));
        	}
        }
        return children;
    }
    
    /**
     * qiucs 2015-10-15 下午4:51:37
     * <p>描述: 获取逻辑表节点对应的物理表组节点 </p>
     * @return List<TreeDefine>
     */
    private List<TreeDefine> getLogicGroupNodes(TreeDefine entity) {
    	List<TreeDefine> list = new ArrayList<TreeDefine>();
    	
    	String logicGroupCode = entity.getDbId();
    	
    	List<Object[]> gList = getService(PhysicalGroupRelationService.class).getPhysicalGroupNode(logicGroupCode);
    	TreeDefine item;
    	Object objArr[];
    	for (int i = 0; i < gList.size(); i++) {
    		objArr = gList.get(i);
    		item = new TreeDefine();
    		item.setId("NT7_" + logicGroupCode + "_" + StringUtil.fillZero(3, i));
    		item.setName(String.valueOf(objArr[1]));
    		item.setType(TreeDefine.T_GROUP);
    		item.setDbId(String.valueOf(objArr[0]));
    		item.setTableId(String.valueOf(objArr[2]));
    		item.setChild(Boolean.FALSE);
    		
    		list.add(item);    		
    	}
    	
    	return list;
    }
    
    /**
     * qiucs 2014-12-18 上午10:24:16
     * <p>描述: 字段节点(跨表)生成动态节点 </p>
     * @return List<TreeDefine>
     */
    private List<TreeDefine> getColumnLabelNodes(TreeDefine entity/*实时动态节点*/) {
    	List<TreeDefine> list = new ArrayList<TreeDefine>();
    	String codeTypeCode = entity.getValue();
    	List<Code> clist = CodeUtil.getInstance().getCodeList(codeTypeCode);
    	Integer showOrder = 0;
    	boolean isAsc = TreeDefine.T_ASC.equals(entity.getSortType());
    	int len, idx;
    	if (CollectionUtils.isNotEmpty(clist)) {
    		len = clist.size() - 1;
    		for (int i = 0; i < clist.size(); i++) {
    			idx = isAsc ? i : len - i;
    			list.add(toTreeDefine(entity, clist.get(idx), ++showOrder));
    		}
    	}
    	return list;
    }
    
    /**
     * qiucs 2014-12-18 上午10:24:12
     * <p>描述: 封装成树节点 </p>
     * @return TreeDefine
     */
    private TreeDefine toTreeDefine(TreeDefine e, Code c, Integer showOrder) {
    	TreeDefine distEntity = new TreeDefine();
    	
    	String cvs = e.getColumnValues();
    	String[] cvArr = cvs.split(",");
    	
    	cvArr[cvArr.length-1] = c.getValue();
    	for (String cv : cvArr) cvs += "," + cv;
    	
    	BeanUtils.copy(e, distEntity);
    	// NT4: node type is 4
    	distEntity.setId("NT4_" + e.getId() + "_" + c.getValue());
    	distEntity.setShowOrder(showOrder);
    	distEntity.setDynamicFromId(e.getId());
    	distEntity.setName(c.getName());
    	distEntity.setValue(c.getValue());
    	distEntity.setNodeRule("0");
    	distEntity.setDynamic("1");
    	distEntity.setColumnValues(cvs.substring(1));
    	
    	/*// 缓存由实时动态节点产生的节点
    	HttpSession session = ServletActionContext.getRequest().getSession();
        @SuppressWarnings("unchecked")
		Map<String, TreeDefine> map = (Map<String, TreeDefine>) session.getAttribute(e.getRootId());
        map.put(distEntity.getId(), distEntity);*/
        
    	return distEntity;
    }

    /**
     * qiucs 2014-11-24 
     * <p>描述: 校验物理表组是否有具体物理表</p>
     */
    public Object checkTableGroup(String groupId) {
        String logicGroupCode = GroupUtil.getLogicGroupCode(groupId);
        String mainLogicTableCode = getService(LogicGroupRelationService.class).getMainLogicTableCode(logicGroupCode);
        if (StringUtil.isEmpty(mainLogicTableCode)) {
            return MessageModel.falseInstance("该物理表组对应的逻辑表组未绑定逻辑表，请检查");
        }
        return getService(PhysicalGroupDefineService.class).getMainPhysicalTableId(groupId, mainLogicTableCode);
    }
    
    /**
     * 获取某棵树下某种类型的节点
     * @param rootId 树根节点ID
     * @param type 树节点类型
     * @return List<TreeDefine>
     */
    public List<TreeDefine> getByRootIdAndType(String rootId, String type) {
        return getDao().getByRootIdAndType(rootId, type);
    }
    
    /**
     * qiucs 2014-12-18 下午4:06:15
     * <p>描述: 获取字段节点（跨表）类型的动态节点下的第一层子节点 </p>
     * @return List<TreeDefine>
     */
    public List<TreeDefine> getDynamicEmptyColumnChildren(String id) {
    	String[] idArr = id.split("_");
    	String parentId= idArr[1];
    	String value   = idArr.length > 1 ? idArr[2] : "";
    	List<TreeDefine> list = find("EQ_parentId=" + parentId + ";EQ_nodeRule=0");
    	
    	for (TreeDefine e : list) {
    		e.setId(e.getId() + "_" + value + "_NT4");
    	}
    	
    	return list;
    }
	
	/**
	 * qiucs 2015-10-15 下午6:21:00
	 * <p>描述: 获取实时动态节点（生成树时直接从数据库中查询出的） </p>
	 * @return List<TreeDefine>
	 */
    @SuppressWarnings("unchecked")
    public List<TreeDefine> getByNodeRule2(String parentId, TreeDefine entity, Map<String, Object> paramMap) {
	    List<TreeDefine> treeDefineList = new ArrayList<TreeDefine>();
	    String parentNodeId = null;
	    TreeDefine parentNode = null;
	    if (parentId.startsWith("NR2_")) {
	    	// 实时动态节点（多层时），获取动态节点对应动态父节点
    	    String[] strs = parentId.split("_");
    	    parentNodeId = strs[1];
    	    TreeDefine dbParentNode = getDao().findOne(parentNodeId);
    	    HttpSession session = ServletActionContext.getRequest().getSession();
            Map<String, TreeDefine> map = (Map<String, TreeDefine>) session.getAttribute(dbParentNode.getRootId());
            parentNode = map.get(parentId);
            // 如果是字段节点（跨表）动态节点下子节点的实时动态节点
            if (parentId.endsWith("_NT4")) paramMap.put("_NT4", strs);
	    } else {
	    	if (parentId.endsWith("_NT4")) {
	    		String[] strs = parentId.split("_");
	    		paramMap.put("_NT4", strs);
	    		parentNodeId = strs[0];
	    	} else {
	    		parentNodeId = parentId;
	    	}
	    }
    	if (TreeDefine.T_COLUMN_EMP.equals(entity.getType())) {
    		// 字段节点（跨表）
    		treeDefineList.addAll(getColumnLabelNodes(entity));
    	} else if (TreeDefine.T_LOGIC_GROUP.equals(entity.getType())) {
    		// 
    		treeDefineList.addAll(getLogicGroupNodes(entity));
    	} else {
    		treeDefineList.addAll(generateDistinctedNode2(entity, parentNode, paramMap));
    	}
	    return treeDefineList;
	}
    
    /**
     * qiucs 2014-12-18 下午4:23:38
     * <p>描述: 获取前台展现树节点 </p>
     * @return List<TreeDefine>
     */
    public List<TreeDefine> getTreeNodes(String parentId, Map<String, Object> paramMap) {
    	List<TreeDefine> treeDefineList = new ArrayList<TreeDefine>();
        boolean isDynamicChild = parentId.endsWith("_NT4");
        if (parentId.indexOf("_") < 0 || (isDynamicChild && !parentId.startsWith("NR2_"))) {
        	// 静态节点 或是 字段节点（跨表）动态节点下的子节点都是从数据库查询
        	String[] idArr = parentId.split("_");
        	
    		treeDefineList = find("EQ_parentId=" + idArr[0], new Sort("showOrder"));
    		
    		if (CollectionUtils.isNotEmpty(treeDefineList)) {
    			if (parentId.equals(treeDefineList.get(0).getRootId())) {
    				HttpSession session = ServletActionContext.getRequest().getSession();
    				session.setAttribute(parentId, new HashMap<String, TreeDefine>());
    			}
    			TreeDefine entity;
    			String nodeRule;
    			for (int i = treeDefineList.size() - 1; i > -1; i--) {
    				entity = treeDefineList.get(i);
    				nodeRule = entity.getNodeRule();
    				if (isDynamicChild) {
    					entity.setId(entity.getId() + "_" + idArr[1] + "_NT4");
    				}
    				if (!TreeDefine.RULE_STATIC.equals(nodeRule)) {
    					treeDefineList.remove(i);
    					if (TreeDefine.RULE_REALTIME.equals(nodeRule)) {
    						treeDefineList.addAll(i, getByNodeRule2(parentId, entity, paramMap));
    					}
    				}
        		}
    		} else if (1 == idArr.length) {
        		TreeDefine pEntity = getByID(idArr[0]);
        		// 获取工作流对应的工作箱
        		if (TreeDefine.T_COFLOW.equals(pEntity.getType())) {
        			treeDefineList = getWorkflowBoxNodes(pEntity);
        		}
    		}
        } 
        if (parentId.startsWith("NT4_")) {
        	// 动态节点-字段节点（跨表）
        	treeDefineList.addAll(getDynamicEmptyColumnChildren(parentId));
        }
        
        return treeDefineList;
    }
    
    /**
     * qiucs 2014-12-23 上午10:53:03
     * <p>描述: 获取工作流对应的工作箱 </p>
     * @return List<TreeDefine>
     */
    private List<TreeDefine> getWorkflowBoxNodes(TreeDefine entity) {
    	List<TreeDefine> list = new ArrayList<TreeDefine>();
    	String workflowId = entity.getDbId();
    	WorkflowDefine flowDefine = getService(WorkflowDefineService.class).getByID(workflowId);
    	List<String> boxes = getService(WorkflowDefineService.class).getCoflowBoxes(flowDefine);
    	//String viewId = WorkflowUtil.getViewId(workflowId);
    	for (String box : boxes) {
    		TreeDefine item = new TreeDefine();
    		item.setId(entity.getId().concat("_").concat(box));
    		item.setType(TreeDefine.T_BOX);
    		item.setDbId(workflowId);
    		item.setName(WorkflowUtil.getBoxName(flowDefine, box));
    		item.setValue(box);
    		item.setTableId(flowDefine.getBusinessTableId()); // 业务表ID;视图ID
    		item.setChild(Boolean.FALSE);
    		list.add(item);
    	}
    	return list;
    }
    
    /**
     * 获取树上所有的物理表
     * 
     * @param treeId 树ID
     * @return Set<PhysicalTableDefine>
     */
    public Set<PhysicalTableDefine> getAllPhysicalTableSet(String treeId) {
        Set<PhysicalTableDefine> physicalTableSet = new HashSet<PhysicalTableDefine>();
        List<TreeDefine> treeDefineList = getAllChildren(treeId, null);
        if (CollectionUtils.isNotEmpty(treeDefineList)) {
            for (TreeDefine treeDefine : treeDefineList) {
                if (TreeDefine.T_TABLE.equals(treeDefine.getType())) {
                    physicalTableSet.add(getService(PhysicalTableDefineService.class).getByID(treeDefine.getTableId()));
                } else if (TreeDefine.T_GROUP.equals(treeDefine.getType())) {
                    List<PhysicalTableDefine> physicalTableDefineList = getDaoFromContext(PhysicalGroupRelationDao.class).getPhysicalTableDefineByGroupId(treeDefine.getDbId());
                    if (CollectionUtils.isNotEmpty(physicalTableDefineList)) {
                        physicalTableSet.addAll(physicalTableDefineList);
                    }
                }
            }
        }
        return physicalTableSet;
    }
    
    /**
     * 获取多个树节点
     * 
     * @param treeDefineIds 根据IDs获取树节点
     * @return List<TreeDefine>
     */
    public List<TreeDefine> getTreeDefinesByIds(String treeDefineIds) {
        List<TreeDefine> list = new ArrayList<TreeDefine>();
        if (StringUtil.isNotEmpty(treeDefineIds)) {
            DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
            String hql = "select t from TreeDefine t where t.id in('" + treeDefineIds.replace(",", "','") + "')";
            list = dao.queryEntityForList(hql, TreeDefine.class);
        }
        return list;
    }
}

package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.datamodel.option.OptionModel;
import com.ces.config.dhtmlx.dao.appmanage.ColumnDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.LogicTableRelationDao;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalTableDefineDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.service.authority.AuthorityDataCopyService;
import com.ces.config.dhtmlx.service.authority.AuthorityDataService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.IndexCommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.google.common.collect.Lists;

@Component
public class PhysicalTableDefineService extends ConfigDefineDaoService<PhysicalTableDefine, PhysicalTableDefineDao> {
	
	/*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Override
    @Autowired
    @Qualifier("physicalTableDefineDao")
    protected void setDaoUnBinding(PhysicalTableDefineDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    @Override
    @Transactional
    public void delete(String id) {
        PhysicalTableDefine entity = getByID(id);
        if (null == entity) {return;}
        // 1. if table is created in database, drop it.
        if ("0".equals(entity.getTableType())) {
            // 1-1. delete ColumnDefine's records
            getDao(ColumnDefineDao.class, ColumnDefine.class).deleteByTableId(entity.getId());
            // 1-2. drop table in database(视图和模板不需要删除表结构)
            if ("1".equals(entity.getCreated())) {
                DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();//(DatabaseHandlerDao)getDaoFromContext("databaseHandlerDao");
                String tableName = entity.getTableName();
                if (dao.tableExists(tableName)) { dao.dropTable(tableName);}
            }
            //删除相关联信息
            deleteRelation(id);
        }
        // delete current record.
        super.delete(id);
        // 清除缓存中表名
        TableUtil.removeTableEntity(id);
        AuthorityUtil.getInstance().clearAuthority(AuthorityUtil.AUTHORITY_DATA, id);
        // 移除索引配置
        IndexCommonUtil.removeIndexConfig(entity);
    }

    @Override
    @Transactional
	public PhysicalTableDefine save(PhysicalTableDefine entity) {
        if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getShowOrder())) {
            Integer maxShowOrder = getDao().getMaxShowOrderByTableTreeId(entity.getTableTreeId());
            if (null == maxShowOrder) {
                maxShowOrder = new Integer(0);
            }
            entity.setShowOrder((maxShowOrder+1));
            entity.setTableCode(entity.getTableCode().toUpperCase());
            entity.setTableName(entity.getTableName());
            entity = getDao().save(entity);
        } else {
            entity = getDao().save(entity);
        }
		//  插入表（模板表与视图不需要建对应物理表结构）且是未创建表结构的
		if(("0").equals(entity.getTableType())  &&  entity.getTableName() != null 
                && !ConstantVar.TableClassification.VIEW.equals(entity.getClassification())) {
			DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();//(DatabaseHandlerDao)getDaoFromContext("databaseHandlerDao");
			if (!"1".equals(entity.getCreated())) {
			    dao.createTableWithStringId(entity.getTableName());        // create table
	            // 1. 更新是否已建表标记
	            entity.setCreated("1");
	            getDao().save(entity);
	            // 2. 向字段定义表中插入相应字段记录
	            ColumnDefine column = new ColumnDefine();
	            column.setShowName("UUID主键");
	            column.setShowOrder(1);
	            column.setColumnName("ID");
	            column.setColumnType("1");// 0-业务字段  1-系统字段
	            column.setDataType(ConstantVar.DataType.CHAR); // 字段数据类型
	            column.setLength(32); // 字段长度
	            column.setTableId(entity.getId());
	            column.setCreated("1");
	            column.setRemark("创建物理表时，自动插入ID字段记录");
	            getDao(ColumnDefineDao.class, ColumnDefine.class).save(column);
	            dao.commentColumn(entity.getTableName(), column.getColumnName(), column.getShowName());
	            // 3. 插入公共字段
	            getService(ColumnDefineService.class).addCommonColumns(entity.getId());
			}
			// 3. 给表添加注释
            dao.commentTable(entity.getTableName(), entity.getShowName()); // add comment
			// 4. 向缓存中添加表名
			TableUtil.addTableEntity(entity);
		}
		// 向索引库中添加配置
		IndexCommonUtil.putIndexConfig(entity);
		return (entity);
	}
	
	/**
	 * qiucs 2013-8-26 
	 * <p>标题: getComboTables</p>
	 * <p>描述: 所有物理表下拉框数据</p>
	 * @return Object    返回类型   
	 * @throws
	 */
	public Object getComboOfTables(Boolean includeView) {
        List<OptionModel> opts = Lists.newArrayList();
	    List<PhysicalTableDefine> list = getTableList(includeView);
	    OptionModel option = new OptionModel();
        option.setValue("");
        option.setText("请选择物理表");
        opts.add(option);
	    for (PhysicalTableDefine table : list) {
	        option = new OptionModel();
	        option.setValue(table.getId());
	        option.setText(table.getShowName());
	        opts.add(option);
	    }
	    return opts;
	}
	
	/**
	 * qiucs 2015-4-15 上午11:52:52
	 * <p>描述: 根据物理表组获取物理表 </p>
	 * @return Object
	 */
	public Object getComboOfTables(String groupId) {
		List<OptionModel> opts = Lists.newArrayList();
		List<PhysicalGroupRelation> list = getService(PhysicalGroupRelationService.class).findByGroupId(groupId);
	    OptionModel option = new OptionModel();
        option.setValue("");
        option.setText("请选择物理表");
        opts.add(option);
	    for (PhysicalGroupRelation grelation : list) {
	        option = new OptionModel();
	        option.setValue(grelation.getTableId());
	        option.setText(TableUtil.getTableEntity(grelation.getTableId()).getShowName());
	        opts.add(option);
	    }
	    return opts;
	}
	
    /**
     * qiucs 2013-8-15 
     * <p>标题: synViews</p>
     * <p>描述: 与数据库中的视图同步</p>
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public MessageModel synViews() {
        String message = "";
        // 1. 更新已有的视图
        MessageModel rmu = updateExistsViews();
        // 2. 删除数据库不存在的视图
        MessageModel rmd = deleteNotExistsViews();
        // 3. 查找新增加视图
        MessageModel rma = addNewViews();
        message = rmd.getMessage() + (StringUtil.isEmpty(rmd.getMessage()) ? "" : "<br/>") +
        		rmu.getMessage() + (StringUtil.isEmpty(rmu.getMessage()) ? "" : (StringUtil.isEmpty(rma.getMessage()) ? "" : "<br/>")) + 
                rma.getMessage();
        if (message.length() == 0) message = "没有需要更新的视图";
        message = "同步成功！<br/>" + message;
        return new MessageModel(Boolean.TRUE, message);
    }
    
    /**
     * qiucs 2013-8-15 
     * <p>标题: deleteNotExistsViews</p>
     * <p>描述:  删除数据库不存在的视图</p>
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    protected MessageModel deleteNotExistsViews() {
        List<Object[]> tps = getNotExistsViews();// 表id,表名称列表
        
        if (null == tps || tps.isEmpty()) { return new MessageModel(Boolean.TRUE, ""); }
        String message = "";
        for (Object[] tpArr : tps) {
            // 删除视图字段
            String id = StringUtil.null2empty((tpArr[0]));
            message += ",".concat(StringUtil.null2empty(tpArr[1]));
            //getDao(ColumnDefineDao.class, ColumnDefine.class).deleteByTableId(id);
            delete(id);
        }
        if (message.length() > 1) {
            message = "删除 " + tps.size() + " 个视图（" + message.substring(1) + "）";
        }
        return new MessageModel(Boolean.TRUE, message);
    }
    /**
     * qiucs 2013-8-15 
     * <p>标题: updateExistsViews</p>
     * <p>描述: 更新已有的视图</p>
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public MessageModel updateExistsViews() {
        Map<String, Integer> tsMap = new HashMap<String, Integer>();
        // 1. 删除不存在的字段
        List<String> colIds = getColumnIdsNotInView();
        if (null != colIds && !colIds.isEmpty()) {
            for (String id : colIds) {
                //getDao(ColumnDefineDao.class, ColumnDefine.class).delete(id);
                getService(ColumnDefineService.class).delete(id);
            }
        }
        // 2. 添加新增加的视图字段
        List<Object[]> columnInfos = getViewColumnsNotInRecord();
        List<ColumnDefine> list = Lists.newArrayList();
        String message = "";
        for (Object[] obj : columnInfos) {
            String tableId = String.valueOf(obj[4]);
            Integer maxShowOrder = null;
            if (tsMap.containsKey(tableId)) {
                maxShowOrder = tsMap.get(tableId);
            } else {
                maxShowOrder = getDao(ColumnDefineDao.class, ColumnDefine.class).getMaxShowOrder(tableId);
                if (null == maxShowOrder) maxShowOrder = 0;
                message += ",".concat(String.valueOf(obj[5]));
            }
            maxShowOrder ++;
            list.add(newColumnDefine(tableId, obj, maxShowOrder));
            tsMap.put(tableId, maxShowOrder);
        }
        getDao(ColumnDefineDao.class, ColumnDefine.class).save(list);
        if (message.length() > 1) {
            message = "更新 " + tsMap.size() + " 个视图（" + message.substring(1) + "）";
        }
        // 3. 比对，更新系统自动生成视图的字段信息
        List<String> tableIdList = getService(TableRelationService.class).getAllTableId();
        for (String tableId : tableIdList) {
			getService(ColumnDefineService.class).updateViews(tableId);
		}
        
        return new MessageModel(Boolean.TRUE, message);
    }
    /**
     * qiucs 2013-8-15 
     * <p>标题: addNewViews</p>
     * <p>描述: 添加新视图</p>
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    protected MessageModel addNewViews() {
        List<Object[]> list = getNewViews();
        if (null == list || list.isEmpty()) return new MessageModel(Boolean.TRUE, "");
        String message = "";
        // 保存视图
        Integer showOrder = getDao().getMaxShowOrderByTableType("1");
        if (null == showOrder) {
            showOrder = Integer.parseInt("1");
        }
        for (int i = 0; i < list.size(); i++) {
            Object[] obj = list.get(i);
            PhysicalTableDefine tab = newPhysicalTableDefine(obj, showOrder++);
            tab.setTableTreeId("-VO");
            tab.setClassification("V");
            tab.setTableType("1");
            tab.setCreated("1");
            tab.setShowOrder(Integer.parseInt(String.valueOf(i+1)));
            tab.setTableCode(StringUtil.null2empty(obj[0]));
            tab.setShowName(StringUtil.null2empty(obj[1]));
            tab = getDao().save(tab);
            message += ",".concat(tab.getShowName());
            // 保存视图中的字段
            List<Object[]> cols = getColumnsOfView(tab.getTableName());
            List<ColumnDefine> clist = Lists.newArrayList();
            Integer maxColShowOrder = getDao(ColumnDefineDao.class, ColumnDefine.class).getMaxShowOrder(tab.getId());
            if (null == maxColShowOrder) maxColShowOrder = 0;
            for (int j = 0; j < cols.size(); j++) {
                obj = cols.get(j);
                clist.add(newColumnDefine(tab.getId(), obj, maxColShowOrder ++));
            }
            getDao(ColumnDefineDao.class, ColumnDefine.class).save(clist);
        }
        if (message.length() > 1) {
            message = "新增 " + list.size() + " 个视图（" + message.substring(1)+ "）";
        }
        
        return new MessageModel(Boolean.TRUE, message);
    }
    /*
     * 创建一个表定义实例
     */
    private PhysicalTableDefine newPhysicalTableDefine(Object[] obj, int showOrder) {
    	PhysicalTableDefine table = new PhysicalTableDefine();
        table.setTableType("1");
        table.setCreated("1");
        table.setShowOrder(showOrder);
        table.setTableCode(StringUtil.null2empty(obj[0]));
        //table.setText(StringUtil.null2empty(obj[1]));
        table.setShowName(StringUtil.null2empty(obj[1]));
        return table;
    }
    /*
     * 创建一个字段定义实例
     */
    private ColumnDefine newColumnDefine(String tableId, Object[] obj, Integer showOrder) {
        ColumnDefine col = new ColumnDefine();
        int len = Integer.parseInt(StringUtil.null2zero(obj[2]));
        col.setCreated("1");
        col.setTableId(tableId);
        col.setColumnName(StringUtil.null2empty(obj[0]).toUpperCase());
        col.setDataType(TableUtil.getDataType(StringUtil.null2empty(obj[1])));
        col.setLength(0 == len ? 50 : len);
        col.setShowName(StringUtil.null2empty(obj[3]));
        col.setShowOrder(showOrder);
        col.setListable("1");
        col.setSortable("1");
        col.setSearchable("1");
        col.setAlign("left");
        col.setWidth(Integer.parseInt("80"));
        col.setFilterType("LIKE");
        col.setRemark("自动从数据库中读取的视图字段");
        return col;
    }
    
    /**
     * qiucs 2013-8-15 
     * <p>标题: getNotExistsViews</p>
     * <p>描述: 获取在表定义中存在，但在数据库中不存在的视图IDs</p>
     * @return List<String>    返回类型   
     * @throws
     */
    protected List<Object[]> getNotExistsViews() {
        if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getNotExistsViewOfOracle();
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getNotExistsViewOfSqlserver();
        } else if (DatabaseHandlerDao.isHighgo()) {
            return getDao().getNotExistsViewOfHighgo();
        } 
        return null;
    }
    /**
     * qiucs 2013-8-15 
     * <p>标题: getColumnIdsNotInView</p>
     * <p>描述: 获取在数据库视图中不存在，但在字段定义中存在的字段IDs</p>
     * @return List<String>    返回类型   
     * @throws
     */
    protected List<String> getColumnIdsNotInView() {
        if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getColumnIdsNotInViewOfOracle();
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getColumnIdsNotInViewOfSqlserver();
        } else if (DatabaseHandlerDao.isHighgo()) {
            return getDao().getColumnIdsNotInViewOfHighgo();
        } 
        return null;
    }
    /**
     * qiucs 2013-8-15 
     * <p>标题: getViewColumnsNotInRecord</p>
     * <p>描述: 获取在字段定义中不存在，但在视图中存在的字段信息</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    protected List<Object[]> getViewColumnsNotInRecord() {
        if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getViewColumnsNotInRecordsOfOracle();
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getViewColumnsNotInRecordsOfSqlserver();
        } else if (DatabaseHandlerDao.isHighgo()) {
        	return getDao().getViewColumnsNotInRecordsOfHighgo();
        }
        return null;
    }
    /**
     * qiucs 2013-8-15 
     * <p>标题: getNewViews</p>
     * <p>描述: 获取在表定义中不存在，数据库中有的所有视图</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    protected List<Object[]> getNewViews() {
        if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getViewsOfOracle();
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getViewsOfSqlserver();
        }  else if (DatabaseHandlerDao.isHighgo()) {
        	return getDao().getViewsOfHighgo();
        }
        return null;
    }
    /**
     * qiucs 2013-8-15 
     * <p>标题: getColumnsOfView</p>
     * <p>描述: 根据视图名称，获取视图中的字段</p>
     * @param  name
     * @return List<Object[]>    返回类型   
     * @throws
     */
    protected List<Object[]> getColumnsOfView(String name) {
        if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getColumnsOfOracleView(name);
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            return getDao().getColumnsOfSqlserverView(name);
        } else if (DatabaseHandlerDao.isHighgo()) {
            return getDao().getColumnsOfHighgoView(name);
        } 
        return null;
    }
    
    /**
     * qiucs 2013-9-12 
     * <p>标题: getRealTableName</p>
     * <p>描述: 根据ID获取表全名（ShowModuleService使用）</p>
     * @param  id
     * @return String    返回类型   
     * @throws
     */
    public String getTableName(String id) {
        return getDao().getTableNameById(id);
    }

    /**
     * qiucs 2013-9-18 
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整显示顺序</p>
     * @param  beginIds
     * @param  endId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void adjustShowOrder(String tableTreeId, String sourceIds, String targetId) {
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
            //getDao().upShowOrder(tableTreeId, eShowOrder, bShowOrder);
            increaseNum = len;
            differLen = tShowOrder - sBeginShowOrder;
            // tShowOrder-1 < showOrder < sBeginShowOrder
            begin = tShowOrder-1;
            end   = sBeginShowOrder;
        } else { 
            // 向下拖动
            //getDao().downShowOrder(tableTreeId, bShowOrder, eShowOrder);
            increaseNum = -len;
            differLen = tShowOrder - sEndShowOrder;
            // sEndShowOrder < showOrder < tShowOrder + 1
            begin = sEndShowOrder;
            end   = tShowOrder + 1;
        }
        // update between sourceIds and targetId
        getDao().batchUpdateShowOrder(tableTreeId, begin, end, increaseNum);
        // update sourceIds
        for (int i = 0; i < len; i++) {
            getDao().updateShowOrderById(idArr[i], differLen);
        }
    }
    
    /**
     * qiucs 2013-9-26 
     * <p>标题: getTableShowName</p>
     * <p>描述: 根据ID获取表显示名称</p>
     * @param  id
     * @return String    返回类型   
     * @throws
     */
    public String getTableShowName(String id) {
        return getDao().getTableShowName(id);
    }

    /**
     * wanglei 2013-11-11 
     * <p>标题: getByTableName</p>
     * <p>描述: 根据ID获取表全名</p>
     * @param tableName 表全名
     * @return PhysicalTableDefine 返回类型
     */
    public PhysicalTableDefine getByTableName(String tableName) {
        return getDao().getByTableName(tableName.toUpperCase());
    }

    /**
     * wanglei 2013-11-11 
     * <p>标题: getByTableName</p>
     * <p>描述: 根据ID获取表全名</p>
     * @param tableNames 表全名s
     * @return List<PhysicalTableDefine> 返回类型
     */
    public List<PhysicalTableDefine> getByTableNames(String tableNames) {
        List<PhysicalTableDefine> tableDefineList = new ArrayList<PhysicalTableDefine>();
        if (StringUtil.isNotEmpty(tableNames)) {
            String hql = "from PhysicalTableDefine t where t.tableName in ('" + tableNames.replace(",", "','") + "')";
            tableDefineList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, PhysicalTableDefine.class);
        }
        return tableDefineList;
    }

    /**
     * wanglei 2013-11-13
     * <p>标题: getByTextAndParentId</p>
     * <p>描述: 根据表名称和父节点ID获取表定义实体</p>
     * @param text 表名称
     * @param parentId 父节点ID
     * @return PhysicalTableDefine 返回类型
     */
    public PhysicalTableDefine getByShowNameAndTableTree(String showName, String tableTreeId) {
        return getDao().getByShowNameAndTableTreeId(showName, tableTreeId);
    }
    
    /**
     * <p>标题: getByClassificationAndType</p>
     * <p>描述: 根据类型获取表定义实体</p>
     * @param classification 类型
     * @return List<PhysicalTableDefine> 返回类型
     */
    public List<PhysicalTableDefine> getByClassification(String classification) {
        return getDao().getByClassification(classification);
    }
    
    /**
     * qiucs 2013-11-28 
     * <p>描述: 删除或更新表相关信息</p>
     * @param  id    设定参数   
     */
    @Transactional
    protected void deleteRelation(String id) {
        //表关系
        getService(TableRelationService.class).deleteByTableId(id);
        //字段关系
        getService(ColumnRelationService.class).deleteByTableId(id);
        //应用配置
        getService(AppDefineService.class).deleteByTableId(id);
        //数据权限
        getService(AuthorityDataService.class).deleteByTableId(id);
        getService(AuthorityDataCopyService.class).deleteByTableId(id);
        //按钮权限
        //getService(AuthorityButtonService.class).deleteByTableId(id);
        //报表配置
        getService(ReportService.class).updateByTableId(id);
        //树配置
        getService(TreeDefineService.class).updateByTableId(id);
        //工作流配置
        getService(WorkflowDefineService.class).updateByTableId(id);
        //模块配置
        //getService(ModuleService.class).updateByTableId(id);
    }

    /**
     * 根据案件级表id，查找电子文件配置信息
     * @param tableId
     * @return 电子文件配置信息
     */
    @Transactional
    public PhysicalTableDefine getDomcumentPhysicalTableDefineByTableId(String tableId){
        return getDao().getDomcumentPhysicalTableDefineByTableId(tableId);
    }
    
    /**
     * qiucs 2014-12-10 
     * <p>描述: 封闭为树节点json</p>
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getTreeNode(String tableTreeId) {
        List<PhysicalTableDefine> list = getByTableTreeId(tableTreeId);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (PhysicalTableDefine entity : list) {
            data.add(beanToTreeNode(entity, ""));
        }
        return data;
    }
    
    /**
     * qiucs 2014-12-10 
     * <p>描述: 封装为树节点json</p>
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getTreeNode(String logicTableCode, String idPre) {
        idPre = StringUtil.null2empty(idPre);
        List<PhysicalTableDefine> list = getDao().getByLogicTableCode(logicTableCode);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (PhysicalTableDefine entity : list) {
            data.add(beanToTreeNode(entity, idPre));
        }
        return data;
    }
    
    /**
     * qiujinwei 2014-12-15 
     * <p>描述: 封闭为树节点json(for getCopyToPhysicalTableTree)</p>
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getTreeNode2(String tableTreeId, String tableId) {
        List<PhysicalTableDefine> list = getByTableTreeId(tableTreeId);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (PhysicalTableDefine entity : list) {
        	if (!StringUtil.isEmpty(entity.getLogicTableCode())) {
        		if (entity.getId().equals(tableId)) {
    				continue;
    			}
			}
            data.add(beanToTreeNode(entity, ""));
        }
        return data;
    }
    
    /**
     * qiujinwei 2014-12-23
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getViewTreeNode(String idPre) {
    	String filter = "EQ_tableType=1;EQ_tableTreeId=" + idPre;
        List<PhysicalTableDefine> list = find(filter, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (PhysicalTableDefine entity : list) {
            data.add(beanToViewTreeNode(entity));
        }
        return data;
    }
    
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    public Map<String, Object> beanToTreeNode(PhysicalTableDefine entity, String idPre) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "1");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", entity.getClassification());
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "draggable");
        item.put("content", "1");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "tableName");
        item.put("content", entity.getTableName());
        userdata.add(item);
        
        data.put("id", idPre.concat(entity.getId()));
        data.put("text", entity.getShowName());
        data.put("type", "1");
        data.put("child", Boolean.FALSE);
        data.put("userdata", userdata);
        return data;
    }
    
    /**
     * qiujinwei 2014-12-23 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    public Map<String, Object> beanToViewTreeNode(PhysicalTableDefine entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "1");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", "V");
        userdata.add(item);
        
        data.put("id", entity.getId());
        data.put("text", entity.getShowName());
        data.put("type", "1");
        data.put("child", Boolean.FALSE);
        data.put("userdata", userdata);
        return data;
    }
    
    /**
     * qiujinwei 2014-11-21 
     * <p>描述: 根据tableTreeId查找</p>
     * @return List<PhysicalTableDefine>    返回类型   
     * @throws
     */
    public List<PhysicalTableDefine> getByTableTreeId(String tableTreeId) {
        return find("EQ_tableTreeId=" + tableTreeId, new Sort("showOrder"));
    }
    
    /**
     * qiucs 2014-11-26 
     * <p>描述: 获取物理表对应的逻辑表编码</p>
     */
    public String getLogicTableCode(String id) {
        return getDao().getLogicTableCode(id);
    }
    
    /**
     * qiucs 2014-12-1 
     * <p>描述: 根据表的显名称来查询</p>
     * @param  showName
     * @return List<PhysicalTableDefine>    返回类型   
     * @throws
     */
    public List<PhysicalTableDefine> findByName(String showName) {
        return find("EQ_showName=" + showName, new Sort("showOrder"));
    }
    
    /**
     * qiucs 2014-12-3 
     * <p>描述: 获取所有表</p>
     * @param includeView --是否包含视图
     * @return List<PhysicalTableDefine>    返回类型   
     */
    public List<PhysicalTableDefine> getTableList(boolean includeView) {
        if (includeView) return find("EQ_created=1");
        return find("EQ_created=1;EQ_tableType=0");
    }
    
    /**
     * qiujinwei 2014-12-01 
     * <p>描述: 获取物理表组下的物理表</p>
     * @return List<PhysicalTableDefine>    返回类型   
     * @throws
     */
    public List<PhysicalTableDefine> getPTIncludingPG(String groupId) {
        return getDao().getPTIncludingPG(groupId);
    }
    
    /**
     * qiujinwei 2014-12-01 
     * <p>描述: groupTree封闭为树节点json</p>
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getGroupTreeNode(String code) {
        List<PhysicalTableDefine> list = getDao().getByLogicTableCode(code);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (PhysicalTableDefine entity : list) {
            data.add(getPhysicalTablesOfGroupToTreeNode(entity));
        }
        return data;
    }
    
    /**
     * qiujinwei 2014-12-01 
     * <p>描述: groupTree实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> getPhysicalTablesOfGroupToTreeNode(PhysicalTableDefine entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "1");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", entity.getLogicTableCode());
        userdata.add(item);
        
        data.put("id", entity.getId());
        data.put("text", entity.getShowName() + "(" + entity.getTableName() + ")");
        data.put("type", "1");
        data.put("child", Boolean.FALSE);
        data.put("userdata", userdata);
        return data;
    }
    
    /**
     * qiujinwei 2014-12-08 
     * <p>描述: 已选中的物理表节点封闭为树节点json</p>
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getSelectedTreeNode(String id, String code) {
    	List<PhysicalGroupRelation> list = getService(PhysicalGroupRelationService.class).findByGroupId(id);
    	List<PhysicalTableDefine> entityList = getDao().getByLogicTableCode(code);
    	List<PhysicalTableDefine> tables = getDao().getByLTCodeExcludeGrpRelt(code);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (PhysicalTableDefine entity : entityList) {
        	for (PhysicalGroupRelation item : list) {
        		if (item.getTableId().equals(entity.getId())) {
        			data.add(getSelectedToTreeNode(entity));
				}
            }
        }
        for (PhysicalTableDefine entity : tables) {
        	data.add(getPhysicalTablesOfGroupToTreeNode(entity));
        }
        return data;
    }
    
    /**
     * qiujinwei 2014-12-08 
     * <p>描述: 构造已选中的物理表节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> getSelectedToTreeNode(PhysicalTableDefine entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "1");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", entity.getLogicTableCode());
        userdata.add(item);
        
        data.put("id", entity.getId());
        data.put("text", entity.getShowName() + "(" + entity.getTableName() + ")");
        data.put("type", "1");
        data.put("checked", Boolean.TRUE);
        data.put("child", Boolean.FALSE);
        data.put("userdata", userdata);
        return data;
    }
    
    /**
     * qiujinwei 2014-12-15 
     * <p>描述: 批量检查表唯一性</p>
     * @return MessageModel    返回类型   
     * @throws
     */
    public MessageModel checkAllUnique(String tableNames){
    	String[] tableNameArr = tableNames.split(",");
    	for (int i = 0; i < tableNameArr.length; i++) {
    		PhysicalTableDefine entity = getDao().getByTableName(tableNameArr[i]);
    		if (entity != null) {
				return new MessageModel(false, "表" + entity.getShowName() + "已存在!");
			}
		}
    	return new MessageModel(true,"success");
    }
    /**
     * qiujinwei 2014-12-24 
     * <p>描述: 批量建表</p>
     */
    @Transactional
    public List<PhysicalTableDefine> saveAll(String logicGroupCode, String tableTreeId, String classification, String tablePrefix, String groupCode, String groupId){
    	List<LogicGroupRelation> groupData = getService(LogicGroupRelationService.class).getByGroupCode(logicGroupCode);
    	List<PhysicalTableDefine> list = new ArrayList<PhysicalTableDefine>();
    	StringBuffer tableIds = new StringBuffer();
    	for (int i = 0; i < groupData.size(); i++) {
    		PhysicalTableDefine entity = new PhysicalTableDefine();
        	entity.setTableType("0");
        	entity.setTableTreeId(tableTreeId);
        	entity.setCreated("0");
        	entity.setClassification(classification);
        	entity.setLogicTableCode(groupData.get(i).getTableCode());
        	entity.setShowName(getService(LogicTableDefineService.class).getByCode(groupData.get(i).getTableCode()).getShowName());
        	entity.setTablePrefix(tablePrefix);
        	entity.setTableCode(groupCode.toUpperCase() + "_" + groupData.get(i).getTableCode().toUpperCase());
        	entity.setShowOrder(groupData.get(i).getShowOrder());
        	entity.setReleaseWithData("0");
        	entity.setRemark("根据物理表组批量生成");
        	PhysicalTableDefine table = save(entity);
        	//复制逻辑表字段
        	getService(ColumnDefineService.class).proceTableCopy(table.getLogicTableCode(), table.getId());
        	//拼接物理表Id
			tableIds.append("," + entity.getId());
        	list.add(table);
		}
    	//保存物理表组和物理表关系
    	getService(PhysicalGroupRelationService.class).save(tableIds.toString(), groupId);
    	//复制对应逻辑表字段关系
    	for (int i = 0; i < list.size(); i++) {
    		List<Object[]> relation = getDao(LogicTableRelationDao.class,LogicTableRelation.class).getRelationByRelationCode(list.get(i).getLogicTableCode(), logicGroupCode);
        	if (relation == null || relation.isEmpty()) continue;
        	Object[] objects = relation.get(0);
        	String mTableId = "";
        	for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getLogicTableCode().equals(String.valueOf(objects[2]))) {
					mTableId = list.get(j).getId();
				}
			}
    		StringBuffer rowsValue = new StringBuffer();
        	for (int j = 0; j  < relation.size(); j++) {
    			objects = relation.get(j);
    			String columnId = getService(ColumnDefineService.class).getColumnIdByTableIdAndColumnName(mTableId, String.valueOf(objects[4]));
    			String mColumnId = getService(ColumnDefineService.class).getColumnIdByTableIdAndColumnName(list.get(i).getId(), String.valueOf(objects[8]));
    			rowsValue.append(";" + columnId + "'" + mColumnId);
    		}
        	getService(TableRelationService.class).saveColumn(rowsValue.toString().substring(1), mTableId, list.get(i).getId());
		}
    	return list;
    }
    /**
     * qiucs 2014-12-3 
     * <p>描述: 根据表分类树ID获取最大显示顺序</p>
     */
    public Integer getMaxShowOrder(String tableTreeId) {
    	return getDao().getMaxShowOrderByTableTreeId(tableTreeId);
    }

	/**
	 * qiucs 2015-2-13 上午9:46:36
	 * <p>描述: 判断是否可以创建索引库 </p>
	 * @return Object
	 */
	public MessageModel canCreateIndex(String id) {
		PhysicalTableDefine entity = getByID(id);
		if (ConstantVar.Judgment.YES.equals(entity.getCreateIndex())) return MessageModel.trueInstance("OK");
		return MessageModel.falseInstance("OK");
	}
	
	/**
     * qiujinwei 2015-03-04 
     * <p>描述: 根据两表ID查找视图 </p>
     * @return 
     */
    @Transactional
    public PhysicalTableDefine getViewByRelation(String tableId, String mTableId){
    	if (!getDao().getByLogicTableCode(tableId + "'" + mTableId).isEmpty()) {
			return getDao().getByLogicTableCode(tableId + "'" + mTableId).get(0);
		} else if (!getDao().getByLogicTableCode(mTableId + "'" + tableId).isEmpty()) {
			return getDao().getByLogicTableCode(mTableId + "'" + tableId).get(0);
		}
    	return null;
    }
    
    /**
     * qiujinwei 2015-03-24 
     * <p>描述: 根据tableTreeId判断是否存在物理表 </p>
     * @return 
     */
    public boolean checkTable(String tableTreeId){
    	if (null == getDao().getByTableTreeId(tableTreeId) || getDao().getByTableTreeId(tableTreeId).isEmpty()) return false;
    	else return true;
    }
    
    /**
     * 批量修改逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    public void batchUpdateLogicTableCode(String oldLogicTableCode, String newLogicTableCode) {
    	getDao().batchUpdateLogicTableCode(oldLogicTableCode, newLogicTableCode);
    }
    
    /**
     * 获取与该表同一表组下的指定表ID
     * @param tableId 表ID
     * @param 指定表的逻辑表CODE
     */
    public String getAppointedTableId(String tableId, String code){
    	String groupId = getService(PhysicalGroupDefineService.class).getByTableId(tableId);
    	List<PhysicalGroupRelation> list = getService(PhysicalGroupRelationService.class).findByGroupId(groupId);
    	for (PhysicalGroupRelation entity : list) {
    		if (TableUtil.getLogicTableCode(entity.getTableId()).equals(code)) {
				return entity.getTableId();
			}
		}
    	return null;
    }
    
    /**
     * 根据物理表组ID获取表组下的指定表ID
     * @param tableId 表ID
     * @param 指定表的逻辑表CODE
     */
    public String getAppointedTableIdByGroupId(String groupId, String code){
    	List<PhysicalGroupRelation> list = getService(PhysicalGroupRelationService.class).findByGroupId(groupId);
    	for (PhysicalGroupRelation entity : list) {
    		if (TableUtil.getLogicTableCode(entity.getTableId()).equals(code)) {
				return entity.getTableId();
			}
		}
    	return null;
    }
    
    /**
     * qiujinwei 2015-06-08
     * <p>标题: getByLogicTableCode</p>
     * <p>描述: 获取逻辑表的关联物理表</p>
     * @param  logicTableCode
     * @return List<LogicTableDefine>    返回类型   
     * @throws
     */
    public List<PhysicalTableDefine> getByLogicTableCode(String logicTableCode) {
    	return getDao().getByLogicTableCode(logicTableCode);
    }
    
    /**
     * 更新物理表分类
     * @param tableId 表ID
     * @param 指定表的逻辑表CODE
     */
    @Transactional
    public void updateTableTreeId (String tableId, String tableTreeId) {
    	PhysicalTableDefine entity = TableUtil.getTableEntity(tableId);
    	entity.setTableTreeId(tableTreeId);
    	save(entity);
    }
    
    /**
     * <p>标题: addExistsViews</p>
     * <p>描述: 添加新增加的视图字段</p>
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public MessageModel addExistsViews() {
    	Map<String, Integer> tsMap = new HashMap<String, Integer>();
        List<Object[]> columnInfos = getViewColumnsNotInRecord();
        List<ColumnDefine> list = Lists.newArrayList();
        String message = "";
        for (Object[] obj : columnInfos) {
            String tableId = String.valueOf(obj[4]);
            Integer maxShowOrder = null;
            if (tsMap.containsKey(tableId)) {
                maxShowOrder = tsMap.get(tableId);
            } else {
                maxShowOrder = getDao(ColumnDefineDao.class, ColumnDefine.class).getMaxShowOrder(tableId);
                if (null == maxShowOrder) maxShowOrder = 0;
                message += ",".concat(String.valueOf(obj[5]));
            }
            maxShowOrder ++;
            list.add(newColumnDefine(tableId, obj, maxShowOrder));
            tsMap.put(tableId, maxShowOrder);
        }
        getDao(ColumnDefineDao.class, ColumnDefine.class).save(list);
        if (message.length() > 1) {
            message = "更新 " + tsMap.size() + " 个视图（" + message.substring(1) + "）";
        }
        
        return new MessageModel(Boolean.TRUE, message);
    }
}

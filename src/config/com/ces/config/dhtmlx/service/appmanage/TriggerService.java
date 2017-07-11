package com.ces.config.dhtmlx.service.appmanage;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.TriggerDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnOperation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplice;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplit;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.service.appmanage.trigger.TriggerOfOracleService;
import com.ces.config.dhtmlx.service.appmanage.trigger.TriggerOfSqlserverService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.FileUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 生成字段处理触发器
 * 
 * @author wang
 * 
 */
@Component
public class TriggerService extends
		ConfigDefineDaoService<StringIDEntity, TriggerDao> {

	/* (非 Javadoc)   
	 * <p>描述: </p>   
	 * @param dao   
	 * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
	 */
	@Autowired
	@Qualifier("triggerDao")
	@Override
	protected void setDaoUnBinding(TriggerDao dao) {
		super.setDaoUnBinding(dao);
	}
	
	/**
	 * qiucs 2013-10-21 
	 * <p>描述: 生成字段关系触发器(一张表一个触发器)</p>
	 * @param  tableId    设定参数   
	 * @return void    返回类型   
	 * @throws
	 */
	public void generateColumnRelationTrigger(String tableId) {
	    /*if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
	        getService(TriggerOfOracleService.class).generateColumnRelationTrigger(tableId);
	    } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
	        getService(TriggerOfSqlserverService.class).generateColumnRelationTrigger(tableId);
	    } else if (DatabaseHandlerDao.DB_DAMING.equals(DatabaseHandlerDao.getDbType())) {
	        
	    }*/
	    Map<String, String> sqlMap = new HashMap<String, String>();
	    String tableName = TableUtil.getTableName(tableId);
        TreeMap<Integer, Map<String, Object>> treeMap = new TreeMap<Integer, Map<String, Object>>();
        List<ColumnRelation> crlist = getService(ColumnRelationService.class).findByTableId(tableId); // getDaoFromContext(ColumnRelationDao.class).findByTableId(tableId);
        Map<String, Integer> crMap = new HashMap<String, Integer>();
        for(ColumnRelation cr : crlist){
            crMap.put(cr.getId(), cr.getShowOrder());
        }
        List<ColumnSplice> splicelist = getService(ColumnSpliceService.class).findByTableId(tableId); // getDaoFromContext(ColumnSpliceDao.class).findByTableId(tableId);
        for(ColumnSplice entity : splicelist){
            assembleTreeMap(treeMap, crMap.get(entity.getColumnRelationId()), ColumnRelation.SPLICE, entity);
        }
        List<ColumnSplit> splitlist = getService(ColumnSplitService.class).findByTableId(tableId); // getDaoFromContext(ColumnSplitDao.class).findByTableId(tableId);
        for(ColumnSplit entity : splitlist){
            assembleTreeMap(treeMap, crMap.get(entity.getColumnRelationId()), ColumnRelation.SPLIT, entity);
        }
        List<ColumnOperation> inheritlist = getService(ColumnOperationService.class).findByTableId(tableId, ColumnOperation.INHERIT); //getDaoFromContext(ColumnOperationDao.class).findByTableId(tableId, "0");
        for(ColumnOperation entity : inheritlist){
            assembleTreeMap(treeMap, crMap.get(entity.getColumnRelationId()), ColumnRelation.INHERIT, entity);
        }
        List<ColumnOperation> sumlist = getService(ColumnOperationService.class).findByTableId(tableId, ColumnOperation.SUM); //getDaoFromContext(ColumnOperationDao.class).findByTableId(tableId, "1");
        for(ColumnOperation entity : sumlist){
            assembleTreeMap(treeMap, crMap.get(entity.getColumnRelationId()), ColumnRelation.SUM, entity);
        }
        List<ColumnOperation> mostlist = getService(ColumnOperationService.class).findByTableId(tableId, ColumnOperation.MOST); //getDaoFromContext(ColumnOperationDao.class).findByTableId(tableId, "2");
        for(ColumnOperation entity : mostlist){
            assembleTreeMap(treeMap, crMap.get(entity.getColumnRelationId()), ColumnRelation.MOST, entity);
        }
        //
        assembleTreeMap(treeMap, getService(ColumnOperationService.class).findByOriginTableId(tableId));
        // 生成各种数据库触发器脚本
        sqlMap.put(DatabaseHandlerDao.DB_ORACLE   , 
                StringUtil.null2empty(getService(TriggerOfOracleService.class).getColumnRelationTriggerSql(treeMap, tableName)));
        sqlMap.put(DatabaseHandlerDao.DB_SQLSERVER, 
                StringUtil.null2empty(getService(TriggerOfSqlserverService.class).getColumnRelationTriggerSql(treeMap, tableName)));
        // 生成当前数据库触发器
        String sql = sqlMap.get(DatabaseHandlerDao.getDbType());
        if (StringUtil.isNotEmpty(sql))
            DatabaseHandlerDao.getInstance().executeSql(sql);
        // 把脚本写到文件中
        writeTriggerFile(tableName, sqlMap, 0);
	}
	/**
	 * qiucs 2014-9-18 
	 * <p>描述: 组装字段关联关系</p>
	 * @return TreeMap<Integer,Map<String,Object>>    返回类型   
	 * @throws
	 */
    private TreeMap<Integer, Map<String, Object>> assembleTreeMap(TreeMap<Integer, Map<String, Object>> treeMap,
            Integer showOrder, String type, StringIDEntity entity) {
        Map<String, Object> entityMap = new HashMap<String, Object>();
        entityMap.put("type", type);
        entityMap.put("entity", entity);
        treeMap.put(showOrder, entityMap);
        return treeMap;
    }
    /**
     * qiucs 2014-9-18 
     * <p>描述: 组装字段关联关系</p>
     * @return TreeMap<Integer,Map<String,Object>>    返回类型   
     * @throws
     */
    private TreeMap<Integer, Map<String, Object>> assembleTreeMap(TreeMap<Integer, Map<String, Object>> treeMap, List<ColumnOperation> list) {
        Map<String, Object> entityMap = new HashMap<String, Object>();
        entityMap.put("list", list);
        treeMap.put(-1, entityMap);
        return treeMap;
    }
	
	/**
	 * qiucs 2013-11-5 
	 * <p>描述: 生成动态节点触发器(业务表)</p>
	 * @param  tableId    设定参数   
	 * @return void    返回类型   
	 */
	public void generateDynamicNodeTrigger(String tableId) {
	    List<TreeDefine> list = getService(TreeDefineService.class).getRuleTreeNodes(tableId);
	    String tableName = TableUtil.getTableName(tableId);
	    for (int i = list.size() -1; i > -1; i--) {
	        TreeDefine entity = list.get(i);
	        if (!tableId.equals(entity.getTableId())) list.remove(i);
	    }
        Map<String, String> sqlMap = new HashMap<String, String>();
        sqlMap.put(DatabaseHandlerDao.DB_ORACLE, getService(TriggerOfOracleService.class).getDynamicNodeTriggerSqlOfData(list, tableName));
        sqlMap.put(DatabaseHandlerDao.DB_SQLSERVER, getService(TriggerOfSqlserverService.class).getDynamicNodeTriggerSqlOfData(list, tableName));
        // 生成当前数据的触发器
        String sql = sqlMap.get(DatabaseHandlerDao.getDbType());
        if (StringUtil.isNotEmpty(sql)) {
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
        // 生成各种数据库触发器脚本
        writeTriggerFile(tableName, sqlMap, 1);
	    /*if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
            getService(TriggerOfOracleService.class).generateDynamicNodeTrigger(tableId);
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            getService(TriggerOfSqlserverService.class).generateDynamicNodeTrigger(tableId);
        } else if (DatabaseHandlerDao.DB_DAMING.equals(DatabaseHandlerDao.getDbType())) {
            
        }*/
    }
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 生成动态节点触发器(编码表)</p>
     * @return void    返回类型   
     */
    public void generateDynamicNodeTrigger() {
        List<TreeDefine> list = getService(TreeDefineService.class).getCodeRuleTreeNodes();
        Map<String, String> sqlMap = new HashMap<String, String>();
        sqlMap.put(DatabaseHandlerDao.DB_ORACLE, getService(TriggerOfOracleService.class).getDynamicNodeTriggerSqlOfCode(list));
        sqlMap.put(DatabaseHandlerDao.DB_SQLSERVER, getService(TriggerOfSqlserverService.class).getDynamicNodeTriggerSqlOfCode(list));
        // 生成当前数据的触发器
        String sql = sqlMap.get(DatabaseHandlerDao.getDbType());
        if (StringUtil.isNotEmpty(sql)) {
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
        // 生成各种数据库触发器脚本
        writeTriggerFile("T_XTPZ_CODE", sqlMap, 1);
        /*if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
            getService(TriggerOfOracleService.class).generateDynamicNodeTrigger();
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            getService(TriggerOfSqlserverService.class).generateDynamicNodeTrigger();
        } else if (DatabaseHandlerDao.DB_DAMING.equals(DatabaseHandlerDao.getDbType())) {
            
        }*/
    }
    
    /**
     * qiucs 2014-9-17 
     * <p>描述: 生成各种数据库触发器脚本</p>
     * @param  tableName
     * @param  sqlMap
     * @param  triggerType    设定参数   
     * @return void    返回类型   
     * @throws
     */
    protected void writeTriggerFile(String tableName, Map<String, String> sqlMap, int triggerType) {
        Set<String> set = sqlMap.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String sql = sqlMap.get(key);
            if (DatabaseHandlerDao.DB_SQLSERVER.equals(key) && sql.startsWith("alter")) {
                sql = sql.replaceFirst("alter", "create");
            }
            FileUtil.write(getTriggerFile(tableName, key, triggerType), sql);
        }
    }
    /**
     * qiucs 2014-9-17 
     * <p>描述: 获取触发器脚本文件名称</p>
     * @param  tableName  表名
     * @param  dbType   数据库类型（DatabaseHandleDao中有对应类型）
     * @param  triggerType 0-字段关联 1-树动态节点
     * @return File    返回类型   
     * @throws
     */
    public static File getTriggerFile(String tableName, String dbType, int triggerType) {
        String path = CommonUtil.getAppRootPath() + "WEB-INF/sql";
        File file = new File(path + "/" + dbType);
        if (!file.exists()) file.mkdirs();
        String name = tableName;
        switch (triggerType) {
            case 0:
                name += "_column.sql";
                break;
            case 1:
                name += "_tree.sql";
                break;

            default:
                break;
        }
        file = new File(path + "/" + dbType + "/" + name);
        return file;
    }
    
    public static class TriggerFileUtil {
        /**
         * qiucs 2014-9-17 
         * <p>描述: 获取触发器脚本文件名称</p>
         * @param  tableName  表名
         * @param  dbType   数据库类型（DatabaseHandleDao中有对应类型）
         * @param  triggerType 0-字段关联 1-树动态节点
         * @return File    返回类型   
         * @throws
         */
        public static File getTriggerFile(String tableName, String dbType, int triggerType) {
            String path = CommonUtil.getAppRootPath() + "WEB-INF/sql";
            File file = new File(path + "/" + dbType);
            if (!file.exists()) file.mkdirs();
            String name = tableName;
            switch (triggerType) {
                case 0:
                    name += "_column.sql";
                    break;
                case 1:
                    name += "_tree.sql";
                    break;

                default:
                    break;
            }
            file = new File(path + "/" + dbType + "/" + name);
            return file;
        }
    }
}

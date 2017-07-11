package com.ces.config.utils;

import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;

import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.label.ColumnLabel;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.TableRelationService;
import com.ces.config.dhtmlx.service.label.ColumnLabelService;
import com.ces.xarch.core.web.listener.XarchListener;

public class TableUtil {

    /** * 在Ehcache中的cache名称 */
    private static final String TABLE_ENTITY = "TABLE_ENTITY";
    /** * 表关系在Ehcache中的cache名称 */
    private static final String TABLE_RELATION = "TABLE_RELATION";
    /** * 表标签在Ehcache中的cache名称 */
    private static final String LOGIC_TABLE_CODE = "LOGIC_TABLE_CODE";
    /** * 字段标签在Ehcache中的cache名称 */
    private static final String COLUMN_LABEL = "COLUMN_LABEL";
    
    /**
     * qiucs 2013-8-13 
     * <p>描述: 获取表名</p>
     * @param  tablePrefix
     * @param  tableCode
     */
    public static String getTableName(String tablePrefix, String tableCode) {
        return (StringUtil.null2empty(tablePrefix) + tableCode).toUpperCase();
    }
    /**
     * qiucs 2013-8-23 
     * <p>标题: getDataType</p>
     * <p>描述: </p>
     * @param  type
     * @return String    返回类型   
     * @throws
     */
    public static String getDataType(String type) {
        if (type.toUpperCase().startsWith("VARCHAR")) {
            return ConstantVar.DataType.CHAR;
        } else if (type.toUpperCase().startsWith("NUMBER") || 
                type.toUpperCase().startsWith("INT")) {
            return ConstantVar.DataType.NUMBER;
        }
        return ConstantVar.DataType.CHAR;
    }
    /**
     * qiucs 2013-8-23 
     * <p>标题: getTablePrefix</p>
     * <p>描述: 获取默认表前缀</p>
     * @param  classification
     * @return String    返回类型   
     * @throws
     */
    public static String getTablePrefix(String classification) {
        if (ConstantVar.TableClassification.ARCHIVE.equals(classification)) {
            return (ConstantVar.TablePrefix.PRE_AR).toUpperCase();
        } else if (ConstantVar.TableClassification.DEFINE.equals(classification)) {
            return (ConstantVar.TablePrefix.PRE_DF).toUpperCase();
        } else if (ConstantVar.TableClassification.PRESET.equals(classification)) {
            return (ConstantVar.TablePrefix.PRE_PS).toUpperCase();
        }
        
        return null;
    }
    
    /**
     * qiucs 2014-2-8 
     * <p>描述: 根据表ID获取表名</p>
     * @param  tableId
     * @return 返回表英文名称 
     */
    public static String getTableName(String tableId) {
    	PhysicalTableDefine entity = getTableEntity(tableId);
    	if (null == entity) {
            return "";
        }
        return entity.getTableName();
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 根据表ID获取表中文名称</p>
     * @param  tableId
     * @return 返回表中文名称 
     */
    public static String getTableText(String tableId) {
    	PhysicalTableDefine entity = getTableEntity(tableId);
        if (null == entity) {
            return "";
        }
        return entity.getShowName();
    }
    /**
     * qiucs 2014-11-28 
     * <p>描述: 根据表名获取表ID</p>
     * @param  tableName
     * @return String    返回类型   
     */
    public static String getTableId(String tableName) {
        String tableId = null;
        Cache cache = EhcacheUtil.getCache(TABLE_ENTITY);
        @SuppressWarnings("unchecked")
        List<String> cacheKeys = cache.getKeysNoDuplicateCheck();
        PhysicalTableDefine table = null;
        tableName = tableName.toUpperCase();
        for (String key : cacheKeys) {
        	table = getTableEntity(key);
        	if (tableName.equals(table.getTableName())) {
        		return key;
        	}
        }
        table = XarchListener.getBean(PhysicalTableDefineService.class).getByTableName(tableName);
        if (null != table) {
            tableId = table.getId();
            addTableEntity(table);
        }
        return tableId;
    }
    
    /**
     * qiujinwei 2014-01-09 
     * <p>描述: 根据表名获取表实体</p>
     * @param  tableName
     */
    public static PhysicalTableDefine getTableEntityByTableName(String tableName) {
        String tableId = getTableId(tableName);
        return getTableEntity(tableId);
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 添加表ID对应的表英文名称</p>
     * @param  tableId
     * @param  tableName
     */
    public static PhysicalTableDefine getTableEntity(String tableId) {
    	PhysicalTableDefine entity = null;
    	Object obj = EhcacheUtil.getCache(TABLE_ENTITY, tableId);
    	if (null == obj) {
    		entity = XarchListener.getBean(PhysicalTableDefineService.class).getByID(tableId);
    		if (null != entity) {
    			addTableEntity(entity);
    		}
    	} else {
    		entity = (PhysicalTableDefine) obj;
    	}
        return entity;
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 添加表ID对应的表英文名称</p>
     * @param  tableId
     * @param  tableName
     */
    public static void addTableEntity(PhysicalTableDefine entity) {
        EhcacheUtil.setCache(TABLE_ENTITY, entity.getId(), entity);
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 根据表ID移除表英文名称</p>
     * @param  tableId    表ID
     */
    public static void removeTableEntity(String tableId) {
        EhcacheUtil.removeCache(TABLE_ENTITY, tableId);
    }
    
    /**
     * qiucs 2014-2-8 
     * <p>描述: 根据两表ID获取表关系</p>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, List<String>> getTableRelation(String tableId, String relateTableId) {
        String akey = tableId + relateTableId;
        String rkey = relateTableId + tableId;
        Map<String, List<String>> map = (Map<String, List<String>>) EhcacheUtil.getCache(TABLE_RELATION, akey);
        if (null == map) {
            map = (Map<String, List<String>>) EhcacheUtil.getCache(TABLE_RELATION, rkey);
        } else {
            return map;
        }
        if (null == map) {
            map =  XarchListener.getBean(TableRelationService.class).getTableRelateColumns(tableId, relateTableId);
            EhcacheUtil.setCache(TABLE_RELATION, akey, map);
        } 
        
        return map;
    }
    
    /**
     * qiucs 2014-2-8 
     * <p>描述: 移除表关系</p>
     */
    public static void removeTableRelation(String tableId, String relateTableId) {
        EhcacheUtil.removeCache(TABLE_RELATION, tableId + relateTableId);
        EhcacheUtil.removeCache(TABLE_RELATION, relateTableId + tableId);
    }
    
    /**
     * qiucs 2014-10-28 
     * <p>描述: 根据表ID获取表标签</p>
     */
    public static String getLogicTableCode(String tableId) {
        Cache cache = EhcacheUtil.getCache(LOGIC_TABLE_CODE);
        if (cache.isKeyInCache(tableId)) {
            return String.valueOf(EhcacheUtil.getCache(LOGIC_TABLE_CODE, tableId));
        }
        String logicTableCode = XarchListener.getBean(PhysicalTableDefineService.class).getLogicTableCode(tableId);
        addLogicTableCode(tableId, logicTableCode);
        return logicTableCode;
    }
    
    /**
     * qiucs 2014-10-28 
     * <p>描述: 向EHCACHE中添加表标签</p>
     */
    public static void addLogicTableCode(String tableId, String logicTableCode) {
        EhcacheUtil.setCache(LOGIC_TABLE_CODE, tableId, logicTableCode);
    }
    
    /**
     * qiucs 2014-10-28 
     * <p>描述: 删除EHCACHE中的表标签</p>
     */
    public static void removeLogicTableCode(String tableId) {
        EhcacheUtil.removeCache(LOGIC_TABLE_CODE, tableId);
    }
    
    /**
     * qiucs 2014-12-4 
     * <p>描述: 从EHCACHE中获取字段标签</p>
     * @param  code
     * @return ColumnLabel    返回类型   
     * @throws
     */
    public static ColumnLabel getColumnLabel(String code) {
        Cache cache = EhcacheUtil.getCache(COLUMN_LABEL);
        if (cache.isKeyInCache(code)) {
            return (ColumnLabel) EhcacheUtil.getCache(COLUMN_LABEL, code);
        }
        ColumnLabel label = XarchListener.getBean(ColumnLabelService.class).getByCode(code);
        EhcacheUtil.setCache(COLUMN_LABEL, code, label);
        return label;
    }
    /**
     * qiucs 2014-12-4 
     * <p>描述: 向EHCACHE中添加字段标签</p>
     */
    public static void addColumnLabel(String code, ColumnLabel label) {
        EhcacheUtil.setCache(COLUMN_LABEL, code, label);
    }
    
    /**
     * qiucs 2014-12-4 
     * <p>描述: 删除EHCACHE中的字段标签</p>
     */
    public static void removeColumnLabel(String code) {
        EhcacheUtil.removeCache(COLUMN_LABEL, code);
    }
    
}

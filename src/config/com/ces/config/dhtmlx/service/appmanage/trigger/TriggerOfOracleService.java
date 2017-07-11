package com.ces.config.dhtmlx.service.appmanage.trigger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.trigger.TriggerOfOracleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.ColumnOperation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplice;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplit;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.appmanage.ColumnOperationService;
import com.ces.config.dhtmlx.service.appmanage.ColumnSpliceService;
import com.ces.config.dhtmlx.service.appmanage.ColumnSplitService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class TriggerOfOracleService extends ConfigDefineDaoService<StringIDEntity, TriggerOfOracleDao> {

    /*
     * (非 Javadoc)   
     * <p>描述: </p>   
     * @param dao   
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("triggerOfOracleDao")
    @Override
    protected void setDaoUnBinding(TriggerOfOracleDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiucs 2013-10-21 
     * <p>描述: 生成字段关系触发器(一张表一个触发器)</p>
     * @param  tableId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    /*public void generateColumnRelationTrigger(String tableId) {
        String sql = columnRelationTriggerBody(tableId);
        DatabaseHandlerDao.getInstance().executeSql(sql);
        // 生成脚本
        FileUtil.write(TriggerFileUtil.getTriggerFile(TableUtil.getTableName(tableId), DatabaseHandlerDao.DB_ORACLE, 0), sql);
        // 生成sqlserver触发器脚本
        getService(TriggerOfSqlserverService.class).writeColumnRelationFile(tableId);
    }*/
    /**
     * qiucs 2014-9-17 
     * <p>描述: 生成字段关联触发器脚本</p>
     * @throws
     */
    /*public void writeColumnRelationFile(String tableId) {
        FileUtil.write(TriggerFileUtil.getTriggerFile(TableUtil.getTableName(tableId), DatabaseHandlerDao.DB_ORACLE, 0), 
                columnRelationTriggerBody(tableId));
    }*/
    
    /**
     * qiucs 2014-9-17 
     * <p>描述: 生成字段关联触发器的SQL语句</p>
     * @return String    返回类型   
     * @throws
     */
    /*public String columnRelationTriggerBody(String tableId) {
        StringBuffer sb = new StringBuffer();
        String tableName = TableUtil.getTableName(tableId);
        String triggerName = "TC_" + tableName;
        sb.append("CREATE OR REPLACE TRIGGER ").append(triggerName)     
                .append("\n    BEFORE INSERT OR UPDATE OR DELETE ON  ").append(tableName)
                .append("\n    REFERENCING old AS old_row new AS new_row")
                .append("\nFOR EACH ROW");
        // 变量块
        StringBuffer declare = new StringBuffer("\nDECLARE");
        declare.append("\n    PRAGMA AUTONOMOUS_TRANSACTION;")
               .append("\n    tempNumber NUMBER;")
               .append("\n    tempVarchar VARCHAR2(255);");
        // 实现块
        StringBuffer body = new StringBuffer();
        // 0. columnRelation
        TreeMap<Integer, String[]> showOrders = new TreeMap<Integer, String[]>();
        List<ColumnRelation> crlist = getDaoFromContext(ColumnRelationDao.class).findByTableId(tableId);
        Map<String, ColumnRelation> crMap = new HashMap<String, ColumnRelation>();
        for(ColumnRelation cr : crlist){
            crMap.put(cr.getId(), cr);
        }
        List<ColumnSplice> splicelist = getDaoFromContext(ColumnSpliceDao.class).findByTableId(tableId);
        Map<String, ColumnSplice> spliceMap = new HashMap<String, ColumnSplice>();
        for(ColumnSplice cs : splicelist){
            String[] strs = new String[2];
            strs[0] = "1";
            strs[1] = cs.getId();
            showOrders.put(crMap.get(cs.getColumnRelationId()).getShowOrder(), strs);
            spliceMap.put(cs.getId(), cs);
        }
        List<ColumnSplit> splitlist = getDaoFromContext(ColumnSplitDao.class).findByTableId(tableId);
        Map<String, ColumnSplit> splitMap = new HashMap<String, ColumnSplit>();
        for(ColumnSplit cs : splitlist){
            String[] strs = new String[2];
            strs[0] = "2";
            strs[1] = cs.getId();
            showOrders.put(crMap.get(cs.getColumnRelationId()).getShowOrder(), strs);
            splitMap.put(cs.getId(), cs);
        }
        List<ColumnOperation> inheritlist = getDaoFromContext(ColumnOperationDao.class).findByTableId(tableId, "0");
        Map<String, ColumnOperation> inheritMap = new HashMap<String, ColumnOperation>();
        for(ColumnOperation cs : inheritlist){
            String[] strs = new String[2];
            strs[0] = "3";
            strs[1] = cs.getId();
            showOrders.put(crMap.get(cs.getColumnRelationId()).getShowOrder(), strs);
            inheritMap.put(cs.getId(), cs);
        }
        List<ColumnOperation> sumlist = getDaoFromContext(ColumnOperationDao.class).findByTableId(tableId, "1");
        Map<String, ColumnOperation> sumMap = new HashMap<String, ColumnOperation>();
        for(ColumnOperation cs : sumlist){
            String[] strs = new String[2];
            strs[0] = "4";
            strs[1] = cs.getId();
            showOrders.put(crMap.get(cs.getColumnRelationId()).getShowOrder(), strs);
            sumMap.put(cs.getId(), cs);
        }
        List<ColumnOperation> mostlist = getDaoFromContext(ColumnOperationDao.class).findByTableId(tableId, "2");
        Map<String, ColumnOperation> mostMap = new HashMap<String, ColumnOperation>();
        for(ColumnOperation cs : mostlist){
            String[] strs = new String[2];
            strs[0] = "5";
            strs[1] = cs.getId();
            showOrders.put(crMap.get(cs.getColumnRelationId()).getShowOrder(), strs);
            mostMap.put(cs.getId(), cs);
        }
        Iterator<Integer> it = showOrders.keySet().iterator();
        while (it.hasNext()) {
            String[] keyValue = showOrders.get(it.next());
            if("1".equals(keyValue[0])){// 1. splice
                body.append(columnSpliceTriggerBody2(spliceMap.get(keyValue[1])));
            }else if("2".equals(keyValue[0])){// 2. split
                body.append(columnSplitTriggerBody2(splitMap.get(keyValue[1])));
            }else if("3".equals(keyValue[0])){// 3. inherit
                body.append(columnInheritTriggerBody2(inheritMap.get(keyValue[1])));
            }else if("4".equals(keyValue[0])){// 4. sum
                body.append(columnSumTriggerBody2(sumMap.get(keyValue[1])));
            }else if("5".equals(keyValue[0])){// 5. most value
                body.append(columnMostValueTriggerBody2(mostMap.get(keyValue[1])));
            }
        }
        body.append(columnInheritTriggerBodyParent(tableId, tableName));
        if (StringUtil.isEmpty(body)) {
            sb.delete(0, sb.length()).append("DROP TRIGGER ").append(triggerName);
        } else {
            sb.append(declare).append("\nBEGIN").append(body).append("\n    COMMIT;").append("\nEND;");
        }
        
        return ("BEGIN\n    EXECUTE IMMEDIATE('" + String.valueOf(sb) + "');\nEND;");
    }*/
    /**
     * qiucs 2014-9-18 
     * <p>描述: 生成字段关联触发器脚本</p>
     * @param  treeMap
     * @param  tableName
     * @return String    返回类型   
     * @throws
     */
    @SuppressWarnings("unchecked")
    public String getColumnRelationTriggerSql(TreeMap<Integer, Map<String, Object>> treeMap, String tableName) {
        StringBuffer sb = new StringBuffer();
        String triggerName = "TC_" + tableName;
        sb.append("CREATE OR REPLACE TRIGGER ").append(triggerName)     
                .append("\n    BEFORE INSERT OR UPDATE OR DELETE ON  ").append(tableName)
                .append("\n    REFERENCING old AS old_row new AS new_row")
                .append("\nFOR EACH ROW");
        // 变量块
        StringBuffer declare = new StringBuffer("\nDECLARE");
        declare.append("\n    PRAGMA AUTONOMOUS_TRANSACTION;")
            .append("\n    tempNumber NUMBER;")
            .append("\n    tempVarchar VARCHAR2(255);")
            .append("\n    tempMostValue1 NUMBER;")
            .append("\n    tempMostValue2 NUMBER;")
            .append("\n    cnt NUMBER;");
        // 实现块
        StringBuffer    body = new StringBuffer();  
        Set<Integer>     set = treeMap.keySet();
        Iterator<Integer> it = set.iterator();
        String pInheritSql = null;
        while (it.hasNext()) {
            Integer key = (Integer) it.next();
            Map<String, Object> entityMap = treeMap.get(key);
            if (-1 == key) {
                pInheritSql = columnParentInheritTriggerBody((List<ColumnOperation>)entityMap.get("list"));
            } else {
                String type = (String)entityMap.get("type");
                if (ColumnRelation.SPLICE.equals(type)) {
                    body.append(columnSpliceTriggerBody((ColumnSplice)entityMap.get("entity")));
                } else if (ColumnRelation.SPLIT.equals(type)) {
                    body.append(columnSplitTriggerBody((ColumnSplit)entityMap.get("entity")));
                } else if (ColumnRelation.INHERIT.equals(type)) {
                    body.append(columnChildInheritTriggerBody((ColumnOperation)entityMap.get("entity")));
                } else if (ColumnRelation.SUM.equals(type)) {
                    body.append(columnSumTriggerBody((ColumnOperation)entityMap.get("entity")));
                } else if (ColumnRelation.MOST.equals(type)) {
                    body.append(columnMostValueTriggerBody((ColumnOperation)entityMap.get("entity")));
                }
            }
        }
        
        if (StringUtil.isNotEmpty(pInheritSql)) body.append(pInheritSql);
        
        if (StringUtil.isEmpty(body)) {
            //sb.delete(0, sb.length()).append("DROP TRIGGER ").append(triggerName);
            return "";
        } else {
            sb.append(declare).append("\nBEGIN").append(body).append("\n    COMMIT;").append("\nEND;");
        }
        
        return ("BEGIN\n    EXECUTE IMMEDIATE('" + String.valueOf(sb) + "');\nEND;");
    }
    
    
    
    
    
    /**
     * qiucs 2013-10-21 
     * <p>描述: 生成字段关系触发器(一张表一个触发器)</p>
     * @param  tableId    设定参数   
     * @return void    返回类型   
     * @throws
     
    public void generateColumnRelationTrigger(String tableId) {
        StringBuffer sb = new StringBuffer();
        String tableName = TableUtil.getTableName(tableId);
        String triggerName = "TC_" + tableName;
        sb.append("CREATE OR REPLACE TRIGGER ").append(triggerName)     
                .append("\n    BEFORE INSERT OR UPDATE OR DELETE ON  ").append(tableName)
                .append("\n    REFERENCING old AS old_row new AS new_row")
                .append("\nFOR EACH ROW");
        // 变量块
        StringBuffer declare = new StringBuffer("\nDECLARE");
        declare.append("\n    PRAGMA AUTONOMOUS_TRANSACTION;")
               .append("\n    tempNumber NUMBER;")
               .append("\n    tempVarchar VARCHAR2(255);");
        // 实现块
        StringBuffer body = new StringBuffer();
        // 1. splice
        body.append(columnSpliceTriggerBody(tableId, tableName));
        // 2. split
        body.append(columnSplitTriggerBody(tableId, tableName));
        // 3. inherit
        body.append(columnInheritTriggerBody(tableId, tableName));
        // 4. sum
        body.append(columnSumTriggerBody(tableId, tableName));
        // 5. most value
        body.append(columnMostValueTriggerBody(tableId, tableName));
        
        if (StringUtil.isEmpty(body)) {
            sb.delete(0, sb.length()).append("DROP TRIGGER ").append(triggerName);
        } else {
            sb.append(declare).append("\nBEGIN").append(body).append("\n    COMMIT;").append("\nEND;");
        }
        
        //System.out.println(sb);
        
        DatabaseHandlerDao.getInstance().executeSql("BEGIN\n    EXECUTE IMMEDIATE('" + String.valueOf(sb) + "');\nEND;");
    }*/
    /*************************************************(一种类型生成一个触发器--开始)***************************************************/
    /**
     * qiucs 2013-10-18 
     * <p>描述: 拼接触发器</p>
     * @param  tableId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    public void columnSpliceTrigger(String tableId) {
        StringBuffer sb = new StringBuffer();
        String tableName = TableUtil.getTableName(tableId);
        sb.append("CREATE OR REPLACE TRIGGER T_inherit_").append(tableName)     
                .append("\n    BEFORE INSERT OR UPDATE ON  ").append(tableName)
                .append("\n    REFERENCING old AS old_row new AS new_row")
                .append("\nFOR EACH ROW");
        String body = columnSpliceTriggerBody(tableId, tableName);
        if (StringUtil.isEmpty(body)) {
            return;
        }
        sb.append("\nBEGIN").append(body).append("\nEND;");
        
        System.out.println(sb);
        
        DatabaseHandlerDao.getInstance().executeSql("BEGIN EXECUTE IMMEDIATE('" + String.valueOf(sb) + "'); \n END;");
        
    }
    
    /**
     * qiucs 2013-10-21 
     * <p>描述: 字段截取</p>
     * @param  tableId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    public void columnSplitTrigger(String tableId) {
        StringBuffer sb = new StringBuffer();
        String tableName = TableUtil.getTableName(tableId);
        sb.append("CREATE OR REPLACE TRIGGER T_inherit_").append(tableName)     
                .append("\n    BEFORE INSERT OR UPDATE ON  ").append(tableName)
                .append("\n    REFERENCING old AS old_row new AS new_row")
                .append("\nFOR EACH ROW");
        String body = columnSplitTriggerBody(tableId, tableName);
        if (StringUtil.isEmpty(body)) {
            return;
        }
        sb.append("\nBEGIN").append(body).append("\nEND;");
        
        System.out.println(sb);
        
        DatabaseHandlerDao.getInstance().executeSql("BEGIN EXECUTE IMMEDIATE('" + String.valueOf(sb) + "'); \n END;");
    }
    
    /**
     * qiucs 2013-10-18 
     * <p>描述: 继承触发器</p>
     * @param  tableId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    public void columnInheritTrigger(String tableId) {
        StringBuffer sb = new StringBuffer();
        String tableName = TableUtil.getTableName(tableId);
        sb.append("CREATE OR REPLACE TRIGGER T_inherit_").append(tableName)     
            .append("\n    BEFORE INSERT OR UPDATE ON  ").append(tableName)
            .append("\n    REFERENCING old AS old_row new AS new_row")
            .append("\nFOR EACH ROW");
        StringBuffer declare = new StringBuffer("\nDECLARE\n    tempNumber NUMBER;");
        String body = columnInheritTriggerBody(tableId, tableName);
        
        if (StringUtil.isEmpty(body)) {
            return;
        }
        sb.append(declare).append("\nBEGIN").append(body).append("\nEND;");
        System.out.println(sb);
        DatabaseHandlerDao.getInstance().executeSql("BEGIN EXECUTE IMMEDIATE('" + String.valueOf(sb) + "'); \n END;");
    }
    /**
     * qiucs 2013-10-21 
     * <p>描述: 求和</p>
     * @param  tableId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    public void columnSumTrigger(String tableId) {
        StringBuffer sb = new StringBuffer();
        String tableName = TableUtil.getTableName(tableId);
        sb.append("CREATE OR REPLACE TRIGGER T_sum_").append(tableName)     
            .append("\n    BEFORE INSERT OR UPDATE OR DELETE ON  ").append(tableName)
            .append("\n    REFERENCING old AS old_row new AS new_row")
            .append("\nFOR EACH ROW");
        StringBuffer declare = new StringBuffer("\nDECLARE\n    PRAGMA AUTONOMOUS_TRANSACTION;\n    tempNumber NUMBER;");
        String body = columnSumTriggerBody(tableId, tableName);
        if (StringUtil.isEmpty(body)) return;
        sb.append(declare).append("\nBEGIN").append(body).append("\nCOMMIT;\nEND;");
        System.out.println(sb);
        DatabaseHandlerDao.getInstance().executeSql("BEGIN EXECUTE IMMEDIATE('" + String.valueOf(sb) + "'); \n END;");
    }

    /**
     * qiucs 2013-10-21 
     * <p>描述: 最值触发器</p>
     * @param  tableId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    public void columnMostValueTrigger(String tableId) {
        StringBuffer sb = new StringBuffer();
        String tableName = TableUtil.getTableName(tableId);
        sb.append("CREATE OR REPLACE TRIGGER T_most_").append(tableName)     
            .append("\n    BEFORE INSERT OR UPDATE OR DELETE ON  ").append(tableName)
            .append("\n    REFERENCING old AS old_row new AS new_row")
            .append("\nFOR EACH ROW");
        StringBuffer declare = new StringBuffer("\nDECLARE\n    PRAGMA AUTONOMOUS_TRANSACTION;\n    tempNumber NUMBER;\n    tempVarchar VARCHAR2(255);");
        String body = columnMostValueTriggerBody(tableId, tableName);
        sb.append(declare).append("\nBEGIN").append(body).append("\n    COMMIT;\nEND;");
        System.out.println(sb);
        DatabaseHandlerDao.getInstance().executeSql("BEGIN EXECUTE IMMEDIATE('" + String.valueOf(sb) + "'); \n END;");
    }
    /*************************************************(一种类型生成一个触发器--结束)***************************************************/
    /**
     * qiucs 2013-10-21 
     * <p>描述: 生成字段拼接触发器内容</p>
     * @param  tableId
     * @param  tableName
     * @return String    返回类型   
     * @throws
     */
    protected String columnSpliceTriggerBody(String tableId, String tableName) {
        List<ColumnSplice> list = getService(ColumnSpliceService.class).findByTableId(tableId);
        StringBuffer body = new StringBuffer();
        if (null != list && !list.isEmpty()) {
            for (ColumnSplice splice : list) {
                body.append(columnSpliceTriggerBody(splice));
            }
            if (StringUtil.isNotEmpty(body)) {
                body.insert(0, "\n    /********** column splice trigger(" + list.size() + "). **********/\n    IF inserting OR updating THEN ").append("\n    END IF;");
            }
        }
        return String.valueOf(body);
    }
    protected String columnSpliceTriggerBody(ColumnSplice splice) {
        StringBuffer body = new StringBuffer();
        if(null != splice){
            int num = splice.getColumnNum();
            body.append("\n    IF inserting OR updating(''").append(columnName(splice.getColumn1Id())).append("'')");
            if (num > 1) body.append(" OR updating(''").append(columnName(splice.getColumn2Id())).append("'')");
            if (num > 2) body.append(" OR updating(''").append(columnName(splice.getColumn3Id())).append("'')");
            if (num > 3) body.append(" OR updating(''").append(columnName(splice.getColumn4Id())).append("'')");
            if (num > 4) body.append(" OR updating(''").append(columnName(splice.getColumn5Id())).append("'')");
            body.append(" THEN ");
            body.append("\n        :new_row.").append(columnName(splice.getStoreColumnId())).append(" := ");
            if (1 == num) {
                body.append(" ''").append(StringUtil.null2empty(splice.getPrefix())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum1()) + ", :new_row.").append(columnName(splice.getColumn1Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSuffix())).append("'';");
            } else if (2 == num) {
                body.append(" ''").append(StringUtil.null2empty(splice.getPrefix())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum1()) + ", :new_row.").append(columnName(splice.getColumn1Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSeperator1())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum2()) + ", :new_row.").append(columnName(splice.getColumn2Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSuffix())).append("'';");
            } else if (3 == num) {
                body.append(" ''").append(StringUtil.null2empty(splice.getPrefix())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum1()) + ", :new_row.").append(columnName(splice.getColumn1Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSeperator1())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum2()) + ", :new_row.").append(columnName(splice.getColumn2Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSeperator2())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum3()) + ", :new_row.").append(columnName(splice.getColumn3Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSuffix())).append("'';");
            } else if (4 == num) {
                body.append(" ''").append(StringUtil.null2empty(splice.getPrefix())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum1()) + ", :new_row.").append(columnName(splice.getColumn1Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSeperator1())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum2()) + ", :new_row.").append(columnName(splice.getColumn2Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSeperator2())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum3()) + ", :new_row.").append(columnName(splice.getColumn3Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSeperator3())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum4()) + ", :new_row.").append(columnName(splice.getColumn4Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSuffix())).append("'';");
            } else if (5 == num) {
                body.append(" ''").append(StringUtil.null2empty(splice.getPrefix())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum1()) + ", :new_row.").append(columnName(splice.getColumn1Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSeperator1())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum2()) + ", :new_row.").append(columnName(splice.getColumn2Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSeperator2())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum3()) + ", :new_row.").append(columnName(splice.getColumn3Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSeperator3())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum4()) + ", :new_row.").append(columnName(splice.getColumn4Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSeperator4())).append("'' || fillZero(" + StringUtil.null2zero(splice.getFillingNum4()) + ", :new_row.").append(columnName(splice.getColumn5Id())).append(",''" + splice.getFill() + "''")
                    .append(") || ''").append(StringUtil.null2empty(splice.getSuffix())).append("'';");
            }
            body.append("\n    END IF;");
        }
        return String.valueOf(body);
    }
    /**
     * qiucs 2013-10-21 
     * <p>描述: 生成字段截取触发器内容</p>
     * @param  tableId
     * @param  tableName
     * @return String    返回类型   
     * @throws
     */
    protected String columnSplitTriggerBody(String tableId, String tableName) {
        StringBuffer body = new StringBuffer();
        List<ColumnSplit> list = getService(ColumnSplitService.class).findByTableId(tableId);
        if (null != list && !list.isEmpty()) {
            for (ColumnSplit split : list) {
                body.append(columnSplitTriggerBody(split));
            }
            if (StringUtil.isNotEmpty(body)) {
                body.insert(0, "\n    /********** column split trigger(" + list.size() + "). **********/");
            }
        }
        return String.valueOf(body);
    }
    protected String columnSplitTriggerBody(ColumnSplit split) {
        StringBuffer body = new StringBuffer();
        if (null != split) {
            String fromColumnName = columnName(split.getFromColumnId());
            String toColumnName   = columnName(split.getToColumnId());
            StringBuffer oneTrigger = new StringBuffer();
            oneTrigger.append("\n    IF inserting OR updating(''").append(fromColumnName).append("'') THEN");
            oneTrigger.append("\n        :new_row.").append(toColumnName).append(" := substr(:new_row.").append(fromColumnName).append(", ").append(split.getStartPosition()).append(", ").append((split.getEndPosition() - split.getStartPosition() + 1)).append(");");
            oneTrigger.append("\n    END IF;");
            body.append(oneTrigger);
        }
        return String.valueOf(body);
    }

    /**
     * qiucs 2013-10-21 
     * <p>描述: 生成继承触发器内容</p>
     * @param  tableId
     * @param  tableName
     * @return String    返回类型   
     * @throws
     */
    protected String columnInheritTriggerBody(String tableId, String tableName) {
        StringBuffer body = new StringBuffer();
        // tableId作为子表
        List<ColumnOperation> list = getService(ColumnOperationService.class).findByTableId(tableId, ColumnOperation.INHERIT);;
        body.append(columnChildInheritTriggerBody(list));
        // tableId作为父表
        list = getService(ColumnOperationService.class).findByOriginTableId(tableId);
        body.append(columnParentInheritTriggerBody(list));
        
        return String.valueOf(body);
    }
    protected String columnChildInheritTriggerBody(List<ColumnOperation> list) {
        // tableId作为父表
        StringBuffer body = new StringBuffer();
        if (null != list && !list.isEmpty()) {
            StringBuffer sbody = new StringBuffer();
            for (ColumnOperation inherit : list) {
                sbody.append(columnChildInheritTriggerBody(inherit));
            }
            if (StringUtil.isNotEmpty(body)) {
                body.append("\n    /********** column inherit(as sun table) trigger(").append(list.size()).append("). **********/")
                    .append(sbody);
            }
        }
        
        return String.valueOf(body);
    }
    protected String columnChildInheritTriggerBody(ColumnOperation inherit) {
        // tableId作为子表
        StringBuffer body = new StringBuffer();
        if (null != inherit) {
                String fTableId = inherit.getOriginTableId(); // 父表ID
                String fTableName = TableUtil.getTableName(fTableId); // 父表表名
                String fColumnName = columnName(inherit.getOriginColumnId()); 
                String sTableId = inherit.getTableId();
                String sColumnName = columnName(inherit.getColumnId());
                StringBuffer oneTrigger = new StringBuffer();
                Map<String, List<String>> relation = TableUtil.getTableRelation(fTableId, sTableId);
                
                List<String> fColumns = relation.get(fTableId);
                List<String> sColumns = relation.get(sTableId);
                StringBuffer filter = new StringBuffer();
                for (int i = 0; i < fColumns.size(); i++  ) {
                    String fColumn = fColumns.get(i);
                    String sColumn = sColumns.get(i);
                    filter.append(fColumn).append(" = :new_row.").append(sColumn);
                    if (i < fColumns.size() - 1) {
                        filter.append(" AND ");
                    }
                }
                oneTrigger.append("\n        SELECT COUNT(*) INTO tempNumber FROM ").append(fTableName).append(" WHERE ").append(filter).append(";");
                oneTrigger.append("\n        IF tempNumber>0 THEN");
                oneTrigger.append("\n            SELECT ").append(fColumnName).append(" INTO :new_row.").append(sColumnName).append(" FROM ")
                                                      .append(fTableName).append(" WHERE ").append(filter).append(";");
                oneTrigger.append("\n        END IF;");

                body.append("\n    IF inserting THEN ");
                body.append(oneTrigger);
                body.append("\n    END IF;");
            }
        
        return String.valueOf(body);
    }
    protected String columnParentInheritTriggerBody(List<ColumnOperation> list) {
        // tableId作为父表
        StringBuffer body = new StringBuffer();
        if (null != list && !list.isEmpty()) {
            StringBuffer fbody = new StringBuffer();
            for (ColumnOperation inherit : list) {
                fbody.append(columnParentInheritTriggerBody(inherit));
            }
            if (StringUtil.isNotEmpty(body)) {
                body.append("\n    /********** column inherit(as father table) trigger(").append(list.size()).append("). **********/")
                    .append(fbody);
            }
        }
        
        return String.valueOf(body);
    }
    protected String columnParentInheritTriggerBody(ColumnOperation inherit) {
        // tableId作为父表
        StringBuffer body = new StringBuffer();
        if (null != inherit) {
            // 父表
            String fTableId = inherit.getOriginTableId();
            //String fTableName = tableName;
            String fColumnName = columnName(inherit.getOriginColumnId());
            // 子表
            String sTableId = inherit.getTableId();
            String sTableName = TableUtil.getTableName(sTableId);
            String sColumnName = columnName(inherit.getColumnId());
            Map<String, List<String>> relation = TableUtil.getTableRelation(fTableId, sTableId);
            StringBuffer oneTrigger = new StringBuffer();
            oneTrigger.append("\n    IF UPDATING(''").append(fColumnName).append("'') THEN ");
            oneTrigger.append("\n        UPDATE ").append(sTableName).append(" SET ").append(sColumnName).append(" = :new_row.").append(fColumnName)
                .append(" WHERE ");
            List<String> fColumns = relation.get(fTableId);
            List<String> sColumns = relation.get(sTableId);
            for (int i = 0; i < sColumns.size(); i++  ) {
                String sColumn = sColumns.get(i);
                oneTrigger.append(sColumn).append(" = :new_row.").append(fColumns.get(i));
                if (i < sColumns.size() - 1) {
                    oneTrigger.append(" AND ");
                }
            }
            oneTrigger.append(";");
            oneTrigger.append("\n    END IF;");
            body.append(oneTrigger);
        }
        
        return String.valueOf(body);
    }
    /**
     * qiucs 2013-10-21 
     * <p>描述: 生成求和触发器内容</p>
     * @param  tableId
     * @param  tableName
     * @return String    返回类型   
     * @throws
     */
    protected String columnSumTriggerBody(String tableId, String tableName) {
        StringBuffer body = new StringBuffer();
        List<ColumnOperation> list = getService(ColumnOperationService.class).findByTableId(tableId, ColumnOperation.SUM);
        if (null != list && !list.isEmpty()) {
            for (ColumnOperation operation : list) {
                body.append(columnSumTriggerBody(operation));
            }
            if (StringUtil.isNotEmpty(body)) {
                body.insert(0, "\n    /********** column sum trigger(" + list.size() + "). **********/");
            }
        }
        return String.valueOf(body);
    }
    protected String columnSumTriggerBody(ColumnOperation operation) {
        StringBuffer body = new StringBuffer();
        if (null != operation) {
            String fTableId = operation.getOriginTableId(); // 父表ID
            String fTableName = TableUtil.getTableName(fTableId); // 父表表名
            String fColumnName = columnName(operation.getOriginColumnId()); 
            String sTableId = operation.getTableId();
            String sTableName = TableUtil.getTableName(sTableId);
            String sColumnName = columnName(operation.getColumnId());
            StringBuffer oneTrigger = new StringBuffer();
            Map<String, List<String>> relation = TableUtil.getTableRelation(fTableId, sTableId);
            //SELECT QW1 INTO :N_V.QW1 FROM T_AR_XX WHERE DEC_NO= :N_V.DEC_NO;
            
            List<String> fColumns = relation.get(fTableId);
            List<String> sColumns = relation.get(sTableId);
            StringBuffer updateFilter = new StringBuffer();
            StringBuffer queryFilter  = new StringBuffer();
            for (int i = 0; i < fColumns.size(); i++  ) {
                String fColumn = fColumns.get(i);
                String sColumn = sColumns.get(i);
                // (^=^) 占位符用了
                updateFilter.append(fColumn).append(" = (^=^).").append(sColumn);
                queryFilter .append(sColumn).append(" = (^=^).").append(sColumn);
                if (i < fColumns.size() - 1) {
                    updateFilter.append(" AND ");
                    queryFilter .append(" AND ");
                }
            }
            if ("0".equals(operation.getOperator())) { //  0-值累计
                // INSERT
                oneTrigger.append("\n    IF inserting OR updating(''").append(sColumnName).append("'') THEN");
                oneTrigger.append("\n        SELECT NVL(SUM(").append(sColumnName).append("),0) + NVL(:new_row.").append(sColumnName).append(",0)")
                          .append(" INTO tempNumber FROM ").append(sTableName)
                          .append(" WHERE ").append(queryFilter.toString().replace("(^=^)", ":new_row")).append(" AND ID != :new_row.ID AND (IS_DELETE!=1 OR IS_DELETE IS NULL);");
                //oneTrigger.append("\n    tempNumber = tempNumber + :new_row.").append(sColumnName).append(";");
                oneTrigger.append("\n        UPDATE ").append(fTableName).append(" SET ").append(fColumnName).append("=tempNumber")
                          .append(" WHERE ").append(updateFilter.toString().replace("(^=^)", ":new_row")).append(";");
                oneTrigger.append("\n    END IF;");
                // 删除
                oneTrigger.append("\n    IF deleting THEN");
                oneTrigger.append("\n        SELECT NVL(SUM(").append(sColumnName).append("),0) INTO tempNumber FROM ").append(sTableName)
                          .append(" WHERE ").append(queryFilter.toString().replace("(^=^)", ":old_row")).append(" AND ID != :old_row.ID AND (IS_DELETE!=1 OR IS_DELETE IS NULL);");
                //oneTrigger.append("\n    tempNumber = tempNumber + :old_row.").append(sColumnName).append(";");
                oneTrigger.append("\n        UPDATE ").append(fTableName).append(" SET ").append(fColumnName).append("=tempNumber")
                          .append(" WHERE ").append(updateFilter.toString().replace("(^=^)", ":old_row")).append(";");
                oneTrigger.append("\n    END IF;");
            } else { // 1-行统计
                // 新增
                oneTrigger.append("\n    IF inserting THEN");
                oneTrigger.append("\n        SELECT (COUNT(").append(sColumnName).append(") + 1) INTO tempNumber FROM ").append(sTableName)
                          .append(" WHERE ").append(queryFilter.toString().replace("(^=^)", ":new_row AND (IS_DELETE!=1 OR IS_DELETE IS NULL)")).append(";");
                //oneTrigger.append("\n    tempNumber = tempNumber + 1;");
                oneTrigger.append("\n        UPDATE ").append(fTableName).append(" SET ").append(fColumnName).append("=tempNumber")
                          .append(" WHERE ").append(updateFilter.toString().replace("(^=^)", ":new_row")).append(";");
                oneTrigger.append("\n    END IF;");
                // 删除
                oneTrigger.append("\n    IF deleting THEN");
                oneTrigger.append("        SELECT (COUNT(").append(sColumnName).append(") - 1) INTO tempNumber FROM ").append(sTableName)
                          .append(" WHERE ").append(queryFilter.toString().replace("(^=^)", ":old_row AND (IS_DELETE!=1 OR IS_DELETE IS NULL)")).append(";");
                //oneTrigger.append("\n    tempNumber = tempNumber - 1;");
                oneTrigger.append("\n        UPDATE ").append(fTableName).append(" SET ").append(fColumnName).append("=tempNumber")
                          .append(" WHERE ").append(updateFilter.toString().replace("(^=^)", ":old_row")).append(";");
                oneTrigger.append("\n    END IF;");
            }
                
                body.append(oneTrigger);
            }
        return String.valueOf(body);
    }
    /**
     * qiucs 2013-10-21 
     * <p>描述: 生成最值触发器内容</p>
     * @param  tableId
     * @param  tableName
     * @return String    返回类型   
     * @throws
     */
    protected String columnMostValueTriggerBody(String tableId, String tableName) {
        StringBuffer body = new StringBuffer();
        List<ColumnOperation> list = getService(ColumnOperationService.class).findByTableId(tableId, ColumnOperation.MOST);
        if (null != list && !list.isEmpty()) {
            for (ColumnOperation operation : list) {
                body.append(columnMostValueTriggerBody(operation));
            }
            if (StringUtil.isNotEmpty(body)) {
                body.insert(0, "\n    /********** column most value trigger(" + list.size() + "). **********/");
            }
        }
        
        return String.valueOf(body);

    }

    protected String columnMostValueTriggerBody(ColumnOperation operation) {
        StringBuffer body = new StringBuffer();
        if (null != operation) {
            String fTableId = operation.getOriginTableId(); // 父表ID
            String fTableName = TableUtil.getTableName(fTableId); // 父表表名
            String fColumnName = columnName(operation.getOriginColumnId());
            String sTableId = operation.getTableId();
            String sTableName = TableUtil.getTableName(sTableId);
            ColumnDefine columnDefine = getService(ColumnDefineService.class).getByID(operation.getColumnId());
            String sColumnName = columnDefine.getColumnName();
            String sDataType = columnDataType(operation.getColumnId());
            String codeTypeCode = columnDefine.getCodeTypeCode();
            StringBuffer oneTrigger = new StringBuffer();
            Map<String, List<String>> relation = TableUtil.getTableRelation(fTableId, sTableId);

            List<String> fColumns = relation.get(fTableId);
            List<String> sColumns = relation.get(sTableId);
            StringBuffer updateFilter = new StringBuffer();
            StringBuffer queryFilter = new StringBuffer();
            for (int i = 0; i < fColumns.size(); i++) {
                String fColumn = fColumns.get(i);
                String sColumn = sColumns.get(i);
                // (^=^) 占位符用了
                updateFilter.append(fColumn).append(" = (^=^).").append(sColumn);
                queryFilter.append("t.").append(sColumn).append(" = (^=^).").append(sColumn);
                if (i < fColumns.size() - 1) {
                    updateFilter.append(" AND ");
                    queryFilter.append(" AND ");
                }
            }
            String operateName = "0".equals(operation.getOperator()) ? "MIN" : "MAX"; // 0-最小
            String orderFlag = "0".equals(operation.getOperator()) ? "asc" : "desc"; // 0-最小
            String tempVar = ConstantVar.DataType.NUMBER.equals(sDataType) ? "tempNumber" : "tempVarchar";
            if (StringUtil.isNotEmpty(codeTypeCode)) {
                // INSERT OR UPDATE
                oneTrigger.append("\n    IF (inserting OR updating(''").append(sColumnName).append("'') OR updating(''IS_DELETE'')) AND :new_row.").append(sColumnName).append(" IS NOT NULL THEN");
                oneTrigger.append("\n        SELECT COUNT(*) INTO cnt FROM ").append(sTableName).append(" t WHERE ")
                        .append(queryFilter.toString().replace("(^=^)", ":new_row")).append(" AND t.ID != :new_row.ID AND (t.IS_DELETE!=1 OR t.IS_DELETE IS NULL) AND t.")
                        .append(sColumnName).append(" IS NOT NULL;");
                oneTrigger.append("\n        IF cnt = 0 THEN");
                oneTrigger.append("\n            tempVarchar := :new_row.").append(sColumnName).append(";");
                oneTrigger.append("\n        ELSE");
                oneTrigger.append("\n            SELECT ").append(sColumnName).append(",MOST_VALUE_SHOW_ORDER INTO ").append(tempVar)
                        .append(",tempMostValue1 FROM ").append("(SELECT t.").append(sColumnName).append(",c.MOST_VALUE_SHOW_ORDER FROM ").append(sTableName)
                        .append(" t, T_XTPZ_CODE c WHERE t.").append(sColumnName).append("=c.value AND ")
                        .append(queryFilter.toString().replace("(^=^)", ":new_row")).append(" AND t.ID != :new_row.ID AND (t.IS_DELETE!=1 OR t.IS_DELETE IS NULL)")
                        .append(" AND c.CODE_TYPE_CODE=''").append(codeTypeCode).append("'' ORDER BY c.MOST_VALUE_SHOW_ORDER ").append(orderFlag)
                        .append(") WHERE ROWNUM=1;");
                oneTrigger.append("\n            SELECT MOST_VALUE_SHOW_ORDER INTO tempMostValue2 FROM T_XTPZ_CODE WHERE CODE_TYPE_CODE=''")
                        .append(codeTypeCode).append("'' AND VALUE=:new_row.").append(sColumnName).append(";");
                oneTrigger.append("\n            IF tempVarchar IS NULL OR tempMostValue2 > tempMostValue1 THEN");
                oneTrigger.append("\n                tempVarchar := :new_row.").append(sColumnName).append(";");
                oneTrigger.append("\n            END IF;");
                oneTrigger.append("\n        END IF;");
                oneTrigger.append("\n        UPDATE ").append(fTableName).append(" SET ").append(fColumnName).append(" = ").append(tempVar).append(" WHERE ")
                        .append(updateFilter.toString().replace("(^=^)", ":new_row")).append(";");
                oneTrigger.append("\n    END IF;");
                // DELETE
                oneTrigger.append("\n    IF deleting AND :old_row.").append(sColumnName).append(" IS NOT NULL THEN");
                oneTrigger.append("\n        SELECT COUNT(*) INTO cnt FROM ").append(sTableName).append(" t WHERE ")
                        .append(queryFilter.toString().replace("(^=^)", ":old_row")).append(" AND t.ID != :old_row.ID AND (t.IS_DELETE!=1 OR t.IS_DELETE IS NULL) AND　t.")
                        .append(sColumnName).append(" IS NOT NULL;");
                oneTrigger.append("\n        IF cnt = 0 THEN");
                oneTrigger.append("\n            tempVarchar := NULL;");
                oneTrigger.append("\n        ELSE");
                oneTrigger.append("\n            SELECT ").append(sColumnName).append(" INTO ").append(tempVar).append(" FROM ").append("(SELECT t.")
                        .append(sColumnName).append(" FROM ").append(sTableName).append(" t, T_XTPZ_CODE c WHERE t.").append(sColumnName)
                        .append("=c.value AND ").append(queryFilter.toString().replace("(^=^)", ":old_row"))
                        .append(" AND t.ID != :old_row.ID AND (t.IS_DELETE!=1 OR t.IS_DELETE IS NULL)").append(" AND c.CODE_TYPE_CODE=''").append(codeTypeCode)
                        .append("'' ORDER BY c.MOST_VALUE_SHOW_ORDER ").append(orderFlag).append(") WHERE ROWNUM=1;");
                oneTrigger.append("\n        END IF;");
                oneTrigger.append("\n        UPDATE ").append(fTableName).append(" SET ").append(fColumnName).append(" = ").append(tempVar).append(" WHERE ")
                        .append(updateFilter.toString().replace("(^=^)", ":old_row")).append(";");
                oneTrigger.append("\n    END IF;");
            } else {
                // INSERT OR UPDATE
                oneTrigger.append("\n    IF inserting OR updating(''").append(sColumnName).append("'') OR updating(''IS_DELETE'') THEN");
                oneTrigger.append("\n        SELECT ").append(operateName).append("(").append(sColumnName).append(") INTO ").append(tempVar).append(" FROM ")
                        .append(sTableName).append(" t WHERE ").append(queryFilter.toString().replace("(^=^)", ":new_row"))
                        .append(" AND ID != :new_row.ID AND (t.IS_DELETE!=1 OR t.IS_DELETE IS NULL);");
                oneTrigger.append("\n        IF ").append(tempVar).append(" IS NULL OR ").append(tempVar).append(" > :new_row.").append(sColumnName)
                        .append(" THEN");
                oneTrigger.append("\n            ").append(tempVar).append(" := :new_row.").append(sColumnName).append(";");
                oneTrigger.append("\n        END IF; ");
                oneTrigger.append("\n        UPDATE ").append(fTableName).append(" SET ").append(fColumnName).append(" = ").append(tempVar).append(" WHERE ")
                        .append(updateFilter.toString().replace("(^=^)", ":new_row")).append(";");
                oneTrigger.append("\n    END IF;");
                // DELETE
                oneTrigger.append("\n    IF deleting THEN");
                oneTrigger.append("\n        SELECT ").append(operateName).append("(").append(sColumnName).append(") INTO ").append(tempVar).append(" FROM ")
                        .append(sTableName).append(" t WHERE ").append(queryFilter.toString().replace("(^=^)", ":old_row"))
                        .append(" AND ID != :new_row.ID AND (t.IS_DELETE!=1 OR t.IS_DELETE IS NULL);");
                oneTrigger.append("\n        UPDATE ").append(fTableName).append(" SET ").append(fColumnName).append(" = ").append(tempVar).append(" WHERE ")
                        .append(updateFilter.toString().replace("(^=^)", ":old_row")).append(";");
                oneTrigger.append("\n    END IF;");
            }

            body.append(oneTrigger);
        }
        return String.valueOf(body);
    }
    /**
     * qiucs 2013-11-5 
     * <p>描述: 动态节点触发器</p>
     * @param  tableId    设定参数   
     * @return void    返回类型   
     */
    public void generateDynamicNodeTrigger(String tableId) {
        List<TreeDefine> list = getService(TreeDefineService.class).getRuleTreeNodes(tableId);
        String tableName = TableUtil.getTableName(tableId);
        DatabaseHandlerDao.getInstance().executeSql(getDynamicNodeTriggerSqlOfData(list, tableName));
    }
    
    /**
     * qiucs 2014-9-17 
     * <p>描述: 数据表数据生成动态节点触发器SQL</p>
     * @param  list
     * @param  tableName
     * @return String    返回类型   
     * @throws
     */
    public String getDynamicNodeTriggerSqlOfData(List<TreeDefine> list, String tableName) {
        StringBuffer body = new StringBuffer();
        String triggerName = "TT_" + tableName;
        StringBuffer sql = new StringBuffer();
        sql .append("\nCREATE OR REPLACE TRIGGER ").append(triggerName)
            .append("\n    BEFORE INSERT OR UPDATE OR DELETE ON ").append(tableName)
            .append("\n    REFERENCING old AS old_row new AS new_row")
            .append("\n    FOR EACH ROW")
            .append("\nDECLARE")
            .append("\n    PRAGMA AUTONOMOUS_TRANSACTION;")
            .append("\n    n_data_number     NUMBER := 0;")
            .append("\n    v_new_text        VARCHAR2(50);")
            .append("\n    v_new_value       VARCHAR2(50);")
            .append("\n    v_old_value       VARCHAR2(50);")
            .append("\n    v_parent_id       VARCHAR2(32);")
            .append("\n    v_parent_ids      VARCHAR2(255);")
            .append("\n    v_dynamic_node_id VARCHAR2(32);")
            .append("\n    v_column_values   VARCHAR2(255);")
            .append("\nBEGIN");
        if (null != list && !list.isEmpty()) {
            for (TreeDefine entity : list) {
                body.append(dynamicNodeTriggerTemplate(entity, tableName));
            }
        }
        if (StringUtil.isEmpty(body)) {
            sql.delete(0, sql.length()).append("DROP TRIGGER " + triggerName);
        } else {
            sql.append(body).append("\nEND;");
        }
        //System.out.println(sql);
        
       return ("BEGIN\n    EXECUTE IMMEDIATE('" + String.valueOf(sql) + "');\nEND;");
    }
    
    /**
     * qiucs 2013-11-5 
     * <p>描述: </p>
     * @param  cNode
     * @param  tableName
     * @return String    返回类型   
     */
    private String dynamicNodeTriggerTemplate(TreeDefine cNode, String tableName) {
        StringBuffer body = new StringBuffer();
        try {
            String dynamicId = cNode.getId();
            String regex     = "(^=^)";
            //boolean isSame   = StringUtil.isEmpty(cNode.getNameRuleId()) || cNode.getDbId().equals(cNode.getNameRuleId());
            ColumnDefine columnEntity = getService(ColumnDefineService.class).getByID(cNode.getDbId());
            String cColumnName = columnEntity.getColumnName();
            String codeTypeCode = columnEntity.getCodeTypeCode();
            boolean isEnum = StringUtil.isNotEmpty(codeTypeCode);
            String isCode  = isEnum ? "1": "0";
            // 0-升序，1-降序
            int sort = TreeDefine.T_DESC.equals(cNode.getSortType()) ? 1 : 0;
            
            String columnValues = assembleColumnValues(cNode.getColumnNames());
            String columnfilter = assembleColumnFilter(cNode.getColumnNames());
            
            body.append("\n    n_data_number:=0;v_new_text:=null;v_new_value:=null;v_old_value:=null;v_parent_id:=null;")
                .append("\n    --动态节点ID")
                .append("\n    v_dynamic_node_id := ''").append(dynamicId).append("'';")
                .append("\n    if inserting or updating then ")
                .append("\n        v_new_value := :new_row.").append(cColumnName).append(";");
            
            //if (isSame) {
                body.append("\n        v_new_text := getCodeName(").append(isCode).append(", v_new_value, ''").append(codeTypeCode).append("'');");
            //} else {
            //    body.append("\n        v_new_text := :new_row.").append(nColumnName);
            //}
            
            body.append("\n        v_column_values := ").append(columnValues.replace(regex, "new_row")).append(";")
                .append("\n    else ")
                .append("\n        v_column_values := ").append(columnValues.replace(regex, "old_row")).append(";")
                .append("\n    end if;")
                .append("\n    --父节点ID")
                .append("\n    select count(*) into n_data_number from t_xtpz_tree_define t where t.column_values=v_column_values;")
                .append("\n    if n_data_number > 0 then")
                .append("\n        select t.id, t.parent_ids into v_parent_id, v_parent_ids from t_xtpz_tree_define t where t.column_values=v_column_values;")
                .append("\n    end if;")
                .append("\n    if updating or deleting then")
                .append("\n        v_old_value := :old_row.").append(cColumnName).append(";")
                .append("\n        select count(*) into n_data_number from ").append(tableName).append(" where ID != :old_row.ID ").append(columnfilter).append(";")
                .append("\n    else ")
                .append("\n        n_data_number := 0;")
                .append("\n    end if;")
                .append("\n    pt_t_xtpz_tree_define(inserting, updating, deleting, n_data_number, v_dynamic_node_id, v_parent_id, v_parent_ids, v_column_values, v_new_text, v_new_value, v_old_value, " + sort + ");")
                .append("\n    --commit;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(body);
    }
    
    /**
     * qiucs 2013-12-11 
     * <p>描述: 目标是为获取动态节点的父节点</p>
     * @param  columnNames
     * @return String
     * @throws
     */
    private String assembleColumnValues(String columnNames) {
        if (StringUtil.isEmpty(columnNames)) return "''''";
        StringBuffer values = new StringBuffer(); // column values
        String[] columnArr = columnNames.split(",");
        // 找父节点不包含最后一个字段值（即当前字段节点）
        for (int i = 0; i < columnArr.length - 1; i++) {
            String column = columnArr[i];
            if (column.startsWith(TreeDefine.TAB_PREFIX) || column.startsWith(TreeDefine.EMP_PREFIX)) {
                values.append(" || '','' || ''").append(column).append("''");
            } else {
                values.append(" || '','' || :(^=^).").append(column);
            }
        }
        values.delete(0, 12);
        return String.valueOf(values);
    }

    /**
     * qiucs 2013-12-11 
     * <p>描述: 把字段组装成过滤条件(以 and 结尾)</p>
     * @param  columnNames
     * @return String
     * @throws
     */
    private String assembleColumnFilter(String columnNames) {
        if (StringUtil.isEmpty(columnNames)) return "";
        StringBuffer filter = new StringBuffer(); // column filters
        String[] columnArr = columnNames.split(",");
        // 与sqlserver不一样（sqlserver是拼接到倒二个字段）
        String columnFilter = null;
        for (int i = 0; i < columnArr.length; i++) {
            String column = columnArr[i];
            if (column.startsWith(TreeDefine.TAB_PREFIX)) {
            	columnFilter = getColumnFilter(column.replaceFirst(TreeDefine.TAB_PREFIX, ""));
            } else if (!column.startsWith(TreeDefine.EMP_PREFIX)) {
                filter.append(" and ").append(column).append(" = :old_row.").append(column);
            }
        }
        
        if (StringUtil.isNotEmpty(columnFilter)) filter.append(columnFilter);
        
        return String.valueOf(filter);
    }
    
    /**
     * qiucs 2015-7-23 下午6:19:39
     * <p>描述: 物理表组节点或物理表节点上的过滤条件 </p>
     * @return String
     */
    private String getColumnFilter(String id) {
    	String columnFilter = null;
    	TreeDefine entity = getService(TreeDefineService.class).getByID(id);
    	
    	if (null != entity || StringUtil.isNotEmpty(entity.getColumnFilter())) {
    		columnFilter = AppDefineUtil.processComplexFilterItem(entity.getColumnFilter());
    	}
    	
    	return columnFilter;
    }
    
    /**
     * qiucs 2013-12-26
     * <p>描述: 动态节点触发器(编码表)</p>
     * @return void    返回类型   
     */
    public void generateDynamicNodeTrigger() {
        List<TreeDefine> list = getService(TreeDefineService.class).getCodeRuleTreeNodes();
        DatabaseHandlerDao.getInstance().executeSql(getDynamicNodeTriggerSqlOfCode(list));
    }
    
    /**
     * qiucs 2014-9-17 
     * <p>描述: 编码表数据生成动态节点触发器SQL</p>
     * @param  list
     * @param  tableName
     * @return String    返回类型   
     * @throws
     */
    public String getDynamicNodeTriggerSqlOfCode(List<TreeDefine> list) {
        StringBuffer body = new StringBuffer();
        String triggerName = "TT_T_XTPZ_CODE";
        StringBuffer sql = new StringBuffer();
        sql .append("\nCREATE OR REPLACE TRIGGER ").append(triggerName)
            .append("\n    BEFORE INSERT OR UPDATE OR DELETE ON T_XTPZ_CODE")
            .append("\n    REFERENCING old AS old_row new AS new_row")
            .append("\n    FOR EACH ROW")
            .append("\nDECLARE")
            .append("\n    PRAGMA AUTONOMOUS_TRANSACTION;")
            .append("\n    v_new_text        VARCHAR2(50);")
            .append("\n    v_new_value       VARCHAR2(50);")
            .append("\n    v_old_value       VARCHAR2(50);")
            .append("\n    v_dynamic_node_id VARCHAR2(32);")
            .append("\nBEGIN");
        if (null != list && !list.isEmpty()) {
            for (TreeDefine entity : list) {
                body.append(dynamicNodeTriggerTemplate(entity));
            }
        }
        if (StringUtil.isEmpty(body)) {
            sql.delete(0, sql.length()).append("DROP TRIGGER " + triggerName);
        } else {
            sql.append(body).append("\nEND;");
        }
        //System.out.println(sql);
        
        return ("BEGIN\n    EXECUTE IMMEDIATE('" + String.valueOf(sql) + "');\nEND;");
    }
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 编码表动态节点生成触发器体</p>
     * @param  cNode
     * @return String    返回类型   
     * @throws
     */
    private String dynamicNodeTriggerTemplate(TreeDefine cNode) {
        StringBuffer body = new StringBuffer();
        try {
            if (StringUtil.isEmpty(cNode.getDbId())) return null;
            ColumnDefine columnEntity = getService(ColumnDefineService.class).getByID(cNode.getDbId());
            if (null == columnEntity || StringUtil.isEmpty(columnEntity.getCodeTypeCode())) return null; 
            String dynamicId = cNode.getId();
            TreeDefine parentNode = getService(TreeDefineService.class).getByID(cNode.getParentId());
            // 0-升序，1-降序
            int sort = TreeDefine.T_DESC.equals(cNode.getSortType()) ? 1 : 0;
            String filter = null;
            if ("1".equals(parentNode.getNodeRule())) {
                filter = "t.parent_id=''" + parentNode.getParentId() + "'' and t.dynamic_from_id=''" + parentNode.getId() + "''";
            } else {
                filter = "t.id=''" + parentNode.getId() + "''";
            }
            body.append("\n    if (((inserting or updating) and :new_row.code_type_code = ''").append(columnEntity.getCodeTypeCode()).append("'') or (deleting and :old_row.code_type_code=''").append(columnEntity.getCodeTypeCode()).append("'')) then ")
                .append("\n        v_new_text:=null;v_new_value:=null;v_old_value:=null;")
                .append("\n        --动态节点ID")
                .append("\n        v_dynamic_node_id := ''").append(dynamicId).append("'';")
                .append("\n        if inserting or updating then ")
                .append("\n            v_new_value := :new_row.value;")
                .append("\n            v_new_text  := :new_row.name;")
                .append("\n        end if;")
                .append("\n        if updating or deleting then")
                .append("\n            v_old_value := :old_row.value;")
                .append("\n        end if;")
                .append("\n        for rec in (select t.id, t.parent_ids, t.column_values from t_xtpz_tree_define t where ").append(filter).append(") loop")
                .append("\n            pt_t_xtpz_tree_define(inserting, updating, deleting, 0, v_dynamic_node_id, rec.id, rec.parent_ids, rec.column_values, v_new_text, v_new_value, v_old_value, " + sort + ");")
                .append("\n        end loop;")
                .append("\n    end if;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(body);
    }
    
    /**
     * qiucs 2013-10-18 
     * <p>描述: 根据ID获取字段英文名称</p>
     * @param  id
     * @return String    返回类型   
     * @throws
     */
    protected String columnName(String columnId) {
        return getService(ColumnDefineService.class).getColumnNameById(columnId);
    }
    /**
     * qiucs 2013-10-22 
     * <p>描述: 字段数据类型</p>
     * @param  columnId
     * @return String    返回类型   
     * @throws
     */
    protected String columnDataType(String columnId) {
        return getService(ColumnDefineService.class).getColumnDataType(columnId);
    }
}

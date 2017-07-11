package com.ces.config.dhtmlx.service.appmanage.trigger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.trigger.TriggerOfSqlserverDao;
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
public class TriggerOfSqlserverService extends ConfigDefineDaoService<StringIDEntity, TriggerOfSqlserverDao> {

    /*
     * (非 Javadoc)   
     * <p>描述: </p>   
     * @param dao   
     * @see com.ces.xarch.core.service.AbstractService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.BaseDao)
     */
    @Autowired
    @Qualifier("triggerOfSqlserverDao")
    @Override
    protected void setDaoUnBinding(TriggerOfSqlserverDao dao) {
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
        String tableName = TableUtil.getTableName(tableId);
        String triggerName = "TC_" + tableName;
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        boolean isCreate = true; 
        sb.append("if exists (select 1 from sysobjects where id = object_id(N''[dbo].[").append(triggerName).append("]'') and objectproperty(id, N''IsTrigger'') = 1)")  
                .append("\nbegin")        
                .append("\n    drop trigger [dbo].[").append(triggerName).append("];")
                .append("\nend;");
        // if trigger exists, drop it;
        String sql = "select 1 from sysobjects where id = object_id(N'[dbo].[" + triggerName + "]') and objectproperty(id, N'IsTrigger') = 1";
        @SuppressWarnings("rawtypes")
        List list = dao.queryForList(sql);
        if (null != list && !list.isEmpty()) {
            isCreate = false;
        }
        sql = columnRelationTriggerBody(tableId, isCreate);
        
        if (StringUtil.isNotEmpty(sql)) {
           dao.executeSql(sql);
           FileUtil.write(TriggerFileUtil.getTriggerFile(TableUtil.getTableName(tableId), DatabaseHandlerDao.DB_SQLSERVER, 0), 
                   sql);
        }
    }*/

    /**
     * qiucs 2014-9-17 
     * <p>描述: 生成字段关联触发器脚本</p>
     * @throws
     */
    /*public void writeColumnRelationFile(String tableId) {
        FileUtil.write(TriggerFileUtil.getTriggerFile(TableUtil.getTableName(tableId), DatabaseHandlerDao.DB_SQLSERVER, 0), 
                columnRelationTriggerBody(tableId, true));
    }*/
    
    /*protected String columnRelationTriggerBody(String tableId, boolean isCreate) {
        String tableName = TableUtil.getTableName(tableId);
        String triggerName = "TC_" + tableName;
        StringBuffer sb = new StringBuffer();
        // create trigger
        sb.append((isCreate ? "create" : "alter") + " trigger ").append(triggerName)
                .append("\non dbo.").append(tableName)
                .append("\nafter insert, update, delete")
                .append("\nas")
                .append("\nbegin")
                .append("\n    declare @insert_num  int;")
                .append("\n    declare @delete_num  int;")
                .append("\n    select @insert_num = count(*) from inserted;")
                .append("\n    select @delete_num = count(*) from deleted;")
                ;
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
                body.append(columnSpliceTriggerBody2(tableId, tableName, spliceMap.get(keyValue[1])));
            }else if("2".equals(keyValue[0])){// 2. split
                body.append(columnSplitTriggerBody2(tableId, tableName, splitMap.get(keyValue[1])));
            }else if("3".equals(keyValue[0])){// 3. inherit
                body.append(columnInheritTriggerBody2(tableId, tableName, inheritMap.get(keyValue[1])));
            }else if("4".equals(keyValue[0])){// 4. sum
                body.append(columnSumTriggerBody2(tableId, tableName, sumMap.get(keyValue[1])));
            }else if("5".equals(keyValue[0])){// 5. most value
                body.append(columnMostValueTriggerBody2(tableId, tableName, mostMap.get(keyValue[1])));
            }
        }
        body.append(columnInheritTriggerBodyParent(tableId, tableName));
        
        if (StringUtil.isNotEmpty(body)) {
           sb.append(body).append("\nend;");
           return ("exec('" + String.valueOf(sb) + "');");
        }
        return "";
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
        String triggerName = "TC_" + tableName;
        StringBuffer sb = new StringBuffer();
        boolean isCreate = true; 
        if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            String sql = "select 1 from sysobjects where id = object_id(N'[dbo].[" + triggerName + "]') and objectproperty(id, N'IsTrigger') = 1";
            @SuppressWarnings("rawtypes")
            List list = DatabaseHandlerDao.getInstance().queryForList(sql);
            if (null != list && !list.isEmpty()) {
                isCreate = false;
            }
        }
        // create trigger
        sb.append((isCreate ? "create" : "alter") + " trigger ").append(triggerName)
                .append("\non dbo.").append(tableName)
                .append("\nafter insert, update, delete")
                .append("\nas")
                .append("\nbegin")
                .append("\n    declare @insert_num  int;")
                .append("\n    declare @delete_num  int;")
                .append("\n    declare @tempNumber  int;")
                .append("\n    declare @tempVarchar  nvarchar(255);")
                .append("\n    declare @tempMostValue1  int;")
                .append("\n    declare @tempMostValue2  int;")
                .append("\n    declare @cnt  int;")
                .append("\n    select @insert_num = count(*) from inserted;")
                .append("\n    select @delete_num = count(*) from deleted;")
                ;
        // 实现块
        StringBuffer body = new StringBuffer();
        
        Set<Integer>     set = treeMap.keySet();
        Iterator<Integer> it = set.iterator();
        String pInheritSql = null;
        while (it.hasNext()) {
            Integer key = (Integer) it.next();
            Map<String, Object> entityMap = treeMap.get(key);
            if (-1 == key) {
                pInheritSql = columnParentInheritTriggerBody((List<ColumnOperation>)entityMap.get("list"));
            } else {
                String type   = (String)entityMap.get("type");
                Object entity = entityMap.get("entity");
                if (ColumnRelation.SPLICE.equals(type)) {
                    body.append(columnSpliceTriggerBody((ColumnSplice)entity));
                } else if (ColumnRelation.SPLIT.equals(type)) {
                    body.append(columnSplitTriggerBody((ColumnSplit)entity));
                } else if (ColumnRelation.INHERIT.equals(type)) {
                    body.append(columnChildInheritTriggerBody((ColumnOperation)entity));
                } else if (ColumnRelation.SUM.equals(type)) {
                    body.append(columnSumTriggerBody((ColumnOperation)entity));
                } else if (ColumnRelation.MOST.equals(type)) {
                    body.append(columnMostValueTriggerBody((ColumnOperation)entity));
                }
            }
        }
        
        if (StringUtil.isNotEmpty(pInheritSql)) body.append(pInheritSql);
        
        if (StringUtil.isNotEmpty(body)) {
            sb.append(body).append("\nend;");
            return ("exec('" + String.valueOf(sb) + "');");
         }
        return null;
    }
    
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
                body.insert(0, "\n    /********** column splice trigger(" + list.size() + "). **********/\n    if @insert_num > 0 begin ").append("\n    end;");
            }
        }
        return String.valueOf(body);
    }
    protected String columnSpliceTriggerBody(ColumnSplice splice) {
        StringBuffer body = new StringBuffer();
        if (null != splice) {
            String tableName = TableUtil.getTableName(splice.getTableId());
            int num = splice.getColumnNum();
            body.append("\n    if @insert_num > 0 begin ");
            body.append("\n        update t set t.").append(columnName(splice.getStoreColumnId())).append(" = ");
            if (1 == num) {
                body.append(" ''").append(StringUtil.null2empty(splice.getPrefix())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum1()) + ", t.").append(columnName(splice.getColumn1Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSuffix())).append("'' ");
            } else if (2 == num) {
                body.append(" ''").append(StringUtil.null2empty(splice.getPrefix())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum1()) + ", t.").append(columnName(splice.getColumn1Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSeperator1())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum2()) + ", t.").append(columnName(splice.getColumn2Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSuffix())).append("'' ");
            } else if (3 == num) {
                body.append(" ''").append(StringUtil.null2empty(splice.getPrefix())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum1()) + ", t.").append(columnName(splice.getColumn1Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSeperator1())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum2()) + ", t.").append(columnName(splice.getColumn2Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSeperator2())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum3()) + ", t.").append(columnName(splice.getColumn3Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSuffix())).append("'' ");
            } else if (4 == num) {
                body.append(" ''").append(StringUtil.null2empty(splice.getPrefix())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum1()) + ", t.").append(columnName(splice.getColumn1Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSeperator1())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum2()) + ", t.").append(columnName(splice.getColumn2Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSeperator2())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum3()) + ", t.").append(columnName(splice.getColumn3Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSeperator3())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum4()) + ", t.").append(columnName(splice.getColumn4Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSuffix())).append("'' ");
            } else if (5 == num) {
                body.append(" ''").append(StringUtil.null2empty(splice.getPrefix())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum1()) + ", t.").append(columnName(splice.getColumn1Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSeperator1())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum2()) + ", t.").append(columnName(splice.getColumn2Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSeperator2())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum3()) + ", t.").append(columnName(splice.getColumn3Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSeperator3())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum4()) + ", t.").append(columnName(splice.getColumn4Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSeperator4())).append("'' + dbo.fillZero(" + StringUtil.null2zero(splice.getFillingNum4()) + ", t.").append(columnName(splice.getColumn5Id())).append(",''" + splice.getFill() + "''")
                    .append(") + ''").append(StringUtil.null2empty(splice.getSuffix())).append("'' ");
            }
            
            body.append("\n            from ").append(tableName).append(" t ")
                .append("\n            join inserted i on(t.id=i.id);");
            body.append("\n    end; ");
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
            String tableName = TableUtil.getTableName(split.getTableId());
            String fromColumnName = columnName(split.getFromColumnId());
            String toColumnName   = columnName(split.getToColumnId());
            StringBuffer oneTrigger = new StringBuffer();
            oneTrigger.append("\n    if @insert_num > 0 begin");
            oneTrigger.append("\n        update t set t.").append(toColumnName).append(" = substring(t.").append(fromColumnName).append(", ").append(split.getStartPosition()).append(", ").append((split.getEndPosition() - split.getStartPosition() + 1)).append(")")
                      .append("\n               from ").append(tableName).append(" t ")
                      .append("\n               join inserted i on(t.id=i.id);");
            oneTrigger.append("\n    end;");
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
        List<ColumnOperation> list = getService(ColumnOperationService.class).findByTableId(tableId, ColumnOperation.INHERIT);
        body.append(columnChildInheritTriggerBody(list));
        // tableId作为父表
        list = getService(ColumnOperationService.class).findByOriginTableId(tableId);
        body.append(columnParentInheritTriggerBody(list));
        
        return String.valueOf(body);
    }
    protected String columnChildInheritTriggerBody(List<ColumnOperation> list) {
        StringBuffer body = new StringBuffer();
        if (null != list && !list.isEmpty()) {
            StringBuffer sbody = new StringBuffer();
            for (ColumnOperation inherit : list) {
                sbody.append(columnChildInheritTriggerBody(inherit));
            }
            if (StringUtil.isNotEmpty(body)) {
                body.append("\n    /********** column inherit(as sun table) trigger(")
                    .append(list.size())
                    .append("). **********/\n    if @insert_num > 0 begin ").append(sbody).append("\n    end;");
            }
        }
        return String.valueOf(body);
    }
    protected String columnChildInheritTriggerBody(ColumnOperation inherit) {
        // tableId作为子表
        StringBuffer sBody = new StringBuffer();
        if (null != inherit) {
            String fTableId = inherit.getOriginTableId(); // 父表ID
            String fTableName = TableUtil.getTableName(fTableId); // 父表表名
            String fColumnName = columnName(inherit.getOriginColumnId()); 
            String sTableId = inherit.getTableId();
            String sTableName = TableUtil.getTableName(sTableId);
            String sColumnName = columnName(inherit.getColumnId());
            StringBuffer oneTrigger = new StringBuffer();
            Map<String, List<String>> relation = TableUtil.getTableRelation(fTableId, sTableId);
            //SELECT QW1 INTO :N_V.QW1 FROM T_AR_XX WHERE DEC_NO= :N_V.DEC_NO;
            
            List<String> fColumns = relation.get(fTableId);
            List<String> sColumns = relation.get(sTableId);
            StringBuffer filter = new StringBuffer();
            for (int i = 0; i < fColumns.size(); i++  ) {
                String fColumn = fColumns.get(i);
                String sColumn = sColumns.get(i);
                filter.append(" f.").append(fColumn).append(" = s.").append(sColumn);
                if (i < fColumns.size() - 1) {
                    filter.append(" and ");
                }
            }
            sBody.append("\n    if @insert_num > 0 begin ");
            oneTrigger.append("\n        update s set s.").append(sColumnName).append(" = ").append(" f.").append(fColumnName)
                      .append("\n                from ").append(sTableName).append(" s ")
                      .append("\n                join inserted i on(s.id=i.id)")
                      .append("\n                join ").append(fTableName).append(" f on(").append(filter).append(");");
            
            sBody.append(oneTrigger);
            sBody.append("\n    end;");
        }
        return String.valueOf(sBody);
    }
    
    protected String columnParentInheritTriggerBody(List<ColumnOperation> list) {
        StringBuffer body = new StringBuffer();
        // tableId作为父表
        StringBuffer fbody = new StringBuffer();
        if (null != list && !list.isEmpty()) {
            for (ColumnOperation inherit : list) {
                fbody.append(columnParentInheritTriggerBody(inherit));
            }
            if (StringUtil.isNotEmpty(fbody)) {
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
            String fTableName = TableUtil.getTableName(fTableId);
            String fColumnName = columnName(inherit.getOriginColumnId());
            // 子表
            String sTableId = inherit.getTableId();
            String sTableName = TableUtil.getTableName(sTableId);
            String sColumnName = columnName(inherit.getColumnId());
            Map<String, List<String>> relation = TableUtil.getTableRelation(fTableId, sTableId);
            List<String> fColumns = relation.get(fTableId);
            List<String> sColumns = relation.get(sTableId);
            StringBuffer filter = new StringBuffer();
            for (int i = 0; i < sColumns.size(); i++  ) {
                String sColumn = sColumns.get(i);
                filter.append(" s.").append(sColumn).append(" = f.").append(fColumns.get(i));
                if (i < sColumns.size() - 1) {
                    filter.append(" and ");
                }
            }
            
            body.append("\n    if @insert_num > 0 and @delete_num > 0 and update(").append(fColumnName).append(") begin ")
                      .append("\n        update s set s.").append(sColumnName).append(" = f.").append(fColumnName)
                      .append("\n                from ").append(fTableName).append(" f ")
                      .append("\n                join inserted i on (f.id = i.id) ")
                      .append("\n                join ").append(sTableName).append(" s on (").append(filter).append(");")
                      .append("\n    end;");
            
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
            String sTableName = TableUtil.getTableName(sTableId);;
            String sColumnName = columnName(operation.getColumnId());
            StringBuffer oneTrigger = new StringBuffer();
            Map<String, List<String>> relation = TableUtil.getTableRelation(fTableId, sTableId);
            //SELECT QW1 INTO :N_V.QW1 FROM T_AR_XX WHERE DEC_NO= :N_V.DEC_NO;
            
            List<String> fColumns = relation.get(fTableId);
            List<String> sColumns = relation.get(sTableId);
            StringBuffer queryFilter  = new StringBuffer();
            StringBuffer sTableFilter = new StringBuffer();
            StringBuffer sGroupByCols = new StringBuffer();
            for (int i = 0; i < fColumns.size(); i++  ) {
                String fColumn = fColumns.get(i);
                String sColumn = sColumns.get(i);
                sGroupByCols.append(",s.").append(sColumn);
                sTableFilter.append(" s.").append(sColumn).append(" = temp.").append(sColumn);
                queryFilter.append(" f.").append(fColumn).append(" = t.").append(sColumn);
                if (i < fColumns.size() - 1) {
                    sTableFilter.append(" and ");
                    queryFilter.append(" and ");
                }
            }
            sGroupByCols = sGroupByCols.deleteCharAt(0);
            if ("0".equals(operation.getOperator())) { //  0-值累计
                // insert or update
                oneTrigger.append("\n    if ((@insert_num > 0 and @delete_num = 0) or (@insert_num > 0 and @delete_num > 0 and update(").append(sColumnName).append("))) begin")
                          .append("\n         update f set f.").append(fColumnName).append(" = t.total_value ")
                          .append("\n                 from ").append(fTableName).append(" f,")
                          .append("\n                 (select ").append(sGroupByCols).append(", sum(isnull(s.").append(sColumnName).append(",0)) as total_value ")
                          .append("\n                         from ").append(sTableName).append(" s ")
                          .append("\n                         join inserted temp on(").append(sTableFilter).append(")").append(" where isnull(s.is_delete, 0)!=1 group by ").append(sGroupByCols).append(") t ")
                          .append("\n                 where ").append(queryFilter)
                          .append("\n    end;");
                // delete
                oneTrigger.append("\n    if @insert_num = 0 and @delete_num > 0 begin")
                          .append("\n         update f set f.").append(fColumnName).append(" = t.total_value ")
                          .append("\n                 from ").append(fTableName).append(" f,")
                          .append("\n                 (select ").append(sGroupByCols).append(", sum(isnull(s.").append(sColumnName).append(",0)) as total_value ")
                          .append("\n                         from ").append(sTableName).append(" s ")
                          .append("\n                         join deleted temp on(").append(sTableFilter).append(")").append(" where isnull(s.is_delete, 0)!=1 group by ").append(sGroupByCols).append(") t ")
                          .append("\n                 where ").append(queryFilter)
                          .append("\n    end;");
            } else { // 1-行统计
                // insert
                oneTrigger.append("\n    if @insert_num > 0 and @delete_num = 0 begin")
                          .append("\n         update f set f.").append(fColumnName).append(" = t.count_value ")
                          .append("\n                 from ").append(fTableName).append(" f,")
                          .append("\n                 (select ").append(sGroupByCols).append(", count(*) as count_value ")
                          .append("\n                         from ").append(sTableName).append(" s ")
                          .append("\n                         join inserted temp on(").append(sTableFilter).append(")").append(" where isnull(s.is_delete, 0)!=1 group by ").append(sGroupByCols).append(") t ")
                          .append("\n                 where ").append(queryFilter)
                          .append("\n    end;");
                // delete
                oneTrigger.append("\n    if @insert_num = 0 and @delete_num > 0 begin")
                          .append("\n         update f set f.").append(fColumnName).append(" = t.count_value ")
                          .append("\n                 from ").append(fTableName).append(" f,")
                          .append("\n                 (select ").append(sGroupByCols).append(", count(*) as count_value ")
                          .append("\n                         from ").append(sTableName).append(" s ")
                          .append("\n                         join deleted temp on(").append(sTableFilter).append(")").append(" where isnull(s.is_delete, 0)!=1 group by ").append(sGroupByCols).append(") t ")
                          .append("\n                 where ").append(queryFilter)
                          .append("\n    end;");
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
            String fTableId    = operation.getOriginTableId(); // 父表ID
            String fTableName  = TableUtil.getTableName(fTableId); // 父表表名
            String fColumnName = columnName(operation.getOriginColumnId()); 
            String sTableId    = operation.getTableId();
            String sTableName  = TableUtil.getTableName(sTableId);
            ColumnDefine columnDefine = getService(ColumnDefineService.class).getByID(operation.getColumnId());
            String sColumnName = columnDefine.getColumnName();
            String sDataType = columnDataType(operation.getColumnId());
            String codeTypeCode = columnDefine.getCodeTypeCode();
            StringBuffer oneTrigger = new StringBuffer();
            Map<String, List<String>> relation = TableUtil.getTableRelation(fTableId, sTableId);
            
            List<String> fColumns = relation.get(fTableId);
            List<String> sColumns = relation.get(sTableId);
            StringBuffer sTableFilter = new StringBuffer();
            StringBuffer queryFilter = new StringBuffer();
            StringBuffer sGroupByCols = new StringBuffer();
            for (int i = 0; i < fColumns.size(); i++  ) {
                String fColumn = fColumns.get(i);
                String sColumn = sColumns.get(i);
                sGroupByCols.append(",s.").append(sColumn);
                sTableFilter.append(" s.").append(sColumn).append(" = temp.").append(sColumn);
                queryFilter .append(" f.").append(fColumn).append(" = t.").append(sColumn);
                if (i < fColumns.size() - 1) {
                    sTableFilter.append(" and ");
                    queryFilter .append(" and ");
                }
            }
            sGroupByCols = sGroupByCols.deleteCharAt(0);
                    
            String operateName = "0".equals(operation.getOperator()) ? "min" : "max"; //  0-最小
            String orderFlag = "0".equals(operation.getOperator()) ? "asc" : "desc"; // 0-最小
            String tempVar = ConstantVar.DataType.NUMBER.equals(sDataType) ? "@tempNumber" : "@tempVarchar";
            if (StringUtil.isNotEmpty(codeTypeCode)) {
                // insert or update
                oneTrigger
                    .append("\n    IF ((@insert_num > 0 and @delete_num = 0) or (@insert_num > 0 and @delete_num > 0 and (update(").append(sColumnName).append(") or update(IS_DELETE))))")
                    .append("\n    BEGIN")
                    .append("\n        SELECT @cnt=COUNT(*) FROM ").append(sTableName).append(" s, inserted temp WHERE ")
                        .append(sTableFilter.toString()).append(" AND s.ID != temp.ID AND (isnull(s.is_delete, 0)!=1) AND s.")
                        .append(sColumnName).append(" IS NOT NULL;")
                    .append("\n        IF @cnt = 0")
                    .append("\n        BEGIN")
                    .append("\n            select ").append(tempVar).append("=temp.").append(sColumnName).append(" from inserted temp;")
                    .append("\n        END")
                    .append("\n        ELSE")
                    .append("\n        BEGIN")
                    .append("\n            SELECT ").append(tempVar).append("=").append(sColumnName).append(",@tempMostValue1=MOST_VALUE_SHOW_ORDER FROM ")
                        .append("(SELECT top 1 s.").append(sColumnName).append(",c.MOST_VALUE_SHOW_ORDER FROM ").append(sTableName)
                        .append(" s, T_XTPZ_CODE c, inserted temp WHERE s.").append(sColumnName).append("=c.value AND ")
                        .append(sTableFilter).append(" AND s.ID != temp.ID AND isnull(s.is_delete, 0)!=1")
                        .append(" AND c.CODE_TYPE_CODE=''").append(codeTypeCode).append("'' ORDER BY c.MOST_VALUE_SHOW_ORDER ")
                        .append(orderFlag).append(") t;")
                    .append("\n            SELECT @tempMostValue2=c.MOST_VALUE_SHOW_ORDER FROM T_XTPZ_CODE c, inserted temp WHERE c.CODE_TYPE_CODE=''")
                        .append(codeTypeCode).append("'' AND VALUE=temp.").append(sColumnName).append(";")
                    .append("\n            IF ").append(tempVar).append(" IS NULL OR @tempMostValue2 > @tempMostValue1")
                    .append("\n            BEGIN")
                    .append("\n                select ").append(tempVar).append(" = temp.").append(sColumnName).append(" from inserted temp;")
                    .append("\n            END")
                    .append("\n        END")
                    .append("\n        UPDATE ").append(fTableName).append(" SET ").append(fColumnName).append("=").append(tempVar)
                        .append(" FROM ").append(fTableName).append(" f, inserted t").append(" WHERE ")
                        .append(queryFilter.toString()).append(";")
                    .append("\n    END;")
                // delete
                    .append("\n    IF @insert_num = 0 and @delete_num > 0")
                    .append("\n    BEGIN")
                    .append("\n        SELECT @cnt=COUNT(*) FROM ").append(sTableName).append(" s, deleted temp WHERE ")
                        .append(sTableFilter.toString()).append(" AND s.ID != temp.ID AND isnull(s.is_delete, 0)!=1 AND s.")
                        .append(sColumnName).append(" IS NOT NULL;")
                    .append("\n        IF @cnt = 0 BEGIN")
                    .append("\n            set ").append(tempVar).append("=NULL;")
                    .append("\n        END")
                    .append("\n        ELSE")
                    .append("\n        BEGIN")
                    .append("\n            SELECT ").append(tempVar).append("=").append(sColumnName).append(" FROM ")
                        .append("(SELECT top 1 s.").append(sColumnName).append(" FROM ").append(sTableName)
                        .append(" s, T_XTPZ_CODE c, deleted temp WHERE s.").append(sColumnName).append("=c.value AND ")
                        .append(sTableFilter.toString()).append(" AND s.ID != temp.ID AND isnull(s.is_delete, 0)!=1")
                        .append(" AND c.CODE_TYPE_CODE=''").append(codeTypeCode).append("'' ORDER BY c.MOST_VALUE_SHOW_ORDER ")
                        .append(orderFlag).append(") t;")
                    .append("\n        END")
                    .append("\n        UPDATE ").append(fTableName).append(" SET ").append(fColumnName).append("=").append(tempVar)
                        .append(" FROM ").append(fTableName).append(" f, deleted t").append(" WHERE ")
                        .append(queryFilter.toString()).append(";")
                    .append("\n    END;");
            } else {
                // insert or update
                oneTrigger.append("\n    IF ((@insert_num > 0 and @delete_num = 0) or (@insert_num > 0 and @delete_num > 0 and (update(").append(sColumnName).append(") or update(IS_DELETE)))) begin")
                    .append("\n         update f set f.").append(fColumnName).append(" = t.most_value ")
                    .append("\n                 from ").append(fTableName).append(" f,")
                    .append("\n                 (select ").append(sGroupByCols).append(",").append(operateName).append("(s.").append(sColumnName).append(") as most_value ")
                    .append("\n                         from ").append(sTableName).append(" s ")
                    .append("\n                         left join inserted temp on(").append(sTableFilter).append(")").append(" where isnull(s.is_delete, 0)!=1 group by ").append(sGroupByCols).append(") t ")
                    .append("\n                 where ").append(queryFilter)
                    .append("\n    end;");
                // delete
                oneTrigger.append("\n    if @insert_num = 0 and @delete_num > 0 begin")
                    .append("\n         update f set f.").append(fColumnName).append(" = t.most_value ")
                    .append("\n                 from ").append(fTableName).append(" f,")
                    .append("\n                 (select ").append(sGroupByCols).append(",").append(operateName).append("(s.").append(sColumnName).append(") as most_value ")
                    .append("\n                         from ").append(sTableName).append(" s ")
                    .append("\n                         left join deleted temp on(").append(sTableFilter).append(")").append(" where isnull(s.is_delete, 0)!=1 group by ").append(sGroupByCols).append(") t ")
                    .append("\n                 where ").append(queryFilter)
                    .append("\n    end;");
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
     * <p>描述: 业务表数据生成动态节点触发器SQL</p>
     * @param  list
     * @param  tableName
     * @return String    返回类型   
     * @throws
     */
    public String getDynamicNodeTriggerSqlOfData(List<TreeDefine> list, String tableName) {
        StringBuffer body = new StringBuffer();
        String triggerName = "TT_" + tableName;
        StringBuffer sb = new StringBuffer();
        
        boolean isCreate = true; 
        // if trigger exists, drop it;
        if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            String sql = "select 1 from sysobjects where id = object_id(N'[dbo].[" + triggerName + "]') and objectproperty(id, N'IsTrigger') = 1";
            @SuppressWarnings("rawtypes")
            List tgList = DatabaseHandlerDao.getInstance().queryForList(sql);
            if (null != tgList && !tgList.isEmpty()) {
                isCreate = false;
            }
        }
        // create trigger
        //sb = new StringBuffer();
        sb.append((isCreate ? "create" : "alter") + " trigger ").append(triggerName)
                .append("\non dbo.").append(tableName)
                .append("\nafter insert, update, delete")
                .append("\nas")
                .append("\nbegin")
                .append("\n    declare @insert_number  int;")
                .append("\n    declare @delete_number  int;")
                .append("\n    declare @dynamic_node_id nvarchar(32);")
                .append("\n    declare @new_text    nvarchar(255);")
                .append("\n    declare @new_value   nvarchar(255);")
                .append("\n    declare @old_value   nvarchar(255);")
                .append("\n    declare @parent_id   nvarchar(32);")
                .append("\n    declare @parent_ids  nvarchar(255);")
                .append("\n    declare @id          nvarchar(32);")
                .append("\n    declare @column_values  nvarchar(200);")
                .append("\n    declare @data_number int;")
                .append("\n    declare new_cursor cursor local scroll for select id from inserted;")
                .append("\n    declare old_cursor cursor local scroll for select id from deleted;")
                .append("\n    select @insert_number = count(*) from inserted;")
                .append("\n    select @delete_number = count(*) from deleted;")
                .append("\n    open new_cursor;")
                .append("\n    open old_cursor;")
                .append("\n    --动态节点ID")
                .append("\n    if (@insert_number > 0) begin fetch first from new_cursor into @id; end")
                .append("\n    if (@delete_number > 0) begin fetch first from old_cursor into @id; end")
                .append("\n    while (@@fetch_status=0) begin");
        
        if (null != list && !list.isEmpty()) {
            for (TreeDefine entity : list) {
                body.append(dynamicNodeTriggerTemplate(entity, tableName));
            }
        }
        if (StringUtil.isEmpty(body) && !isCreate) {
            sb.delete(0, sb.length()).append("drop trigger " + triggerName);
        } else {
            sb.append(body)
                    .append("\n        if (@insert_number > 0) begin fetch next from new_cursor into @id; end;")
                    .append("\n        if (@delete_number > 0) begin fetch next from new_cursor into @id; end;")
                    .append("\n    end;")
                    .append("\n    close new_cursor;")
                    .append("\n    close old_cursor;")
                    .append("\n    deallocate new_cursor;")
                    .append("\n    deallocate old_cursor;")
                    .append("\nend;");
            
        }
        //System.out.println(sb);
        return ("exec('" + String.valueOf(sb) + "')");
    }
    
    /**
     * qiucs 2013-12-6 
     * <p>描述: </p>
     * @param  cNode
     * @param  tableName
     * @return String    返回类型   
     */
    private String dynamicNodeTriggerTemplate(TreeDefine cNode, String tableName) {
        StringBuffer body = new StringBuffer();
        try {
            String dynamicId = cNode.getId();
            if (StringUtil.isEmpty(cNode.getDbId())) return null;
            ColumnDefine columnEntity = getService(ColumnDefineService.class).getByID(cNode.getDbId());
            if (null == columnEntity) return null;
            String columnName = columnEntity.getColumnName();
            //String updating  = info[2];
            String codeTypeCode = columnEntity.getCodeTypeCode();
            boolean isEnum = StringUtil.isNotEmpty(codeTypeCode);
            String isCode  = isEnum ? "1": "0";
            String values = assembleColumnValues(cNode.getColumnNames());
            String filter = assembleColumnFilter(cNode.getColumnNames());
            int sort = TreeDefine.T_DESC.equals(cNode.getSortType()) ? 1 : 0;
            //body.append("\n    --动态节点ID")
                //.append("\n    if (@insert_number > 0) begin fetch first from new_cursor into @id; end")
                //.append("\n    if (@delete_number > 0) begin fetch first from old_cursor into @id; end")
                //.append("\n    while (@@fetch_status=0) begin")
            body.append("\n        set @dynamic_node_id=''").append(dynamicId).append("'';set @data_number = 0;set @new_value = null;set @new_text = null;set @old_value = null;")
                .append("\n        if (@insert_number > 0) begin select @column_values=").append(values).append(" from inserted where id=@id; end")
                .append("\n        else begin select @column_values=").append(values).append(" from deleted where id=@id; end")
                .append("\n        select @parent_id=t.id, @parent_ids=t.parent_ids from t_xtpz_tree_define t where t.column_values=@column_values;")
                .append("\n        if @parent_id is not null begin ")
                .append("\n            if @insert_number > 0 begin -- insert or update")
                .append("\n                select @new_value = ").append(columnName).append(" from inserted where id=@id;")
                .append("\n                set @new_text = dbo.getCodeName(").append(isCode).append(", @new_value, ''").append(codeTypeCode).append("'');")
                .append("\n            end;")
                .append("\n            if @delete_number > 0 begin -- update or delete")
                .append("\n                select @old_value = ").append(columnName).append(" from deleted where id=@id;")
                .append("\n                select @data_number = count(*) from ").append(tableName).append(" where ").append(filter).append(columnName).append(" = @old_value;")
                .append("\n            end;")
                .append("\n            exec pt_t_xtpz_tree_define @insert_number, @delete_number, @data_number, @dynamic_node_id, @parent_id, @parent_ids, @column_values, @new_text, @new_value, @old_value, " + sort + ";")
                .append("\n        end;");
                //.append("\n        if (@insert_number > 0) begin fetch next from new_cursor into @id; end;")
                //.append("\n        if (@delete_number > 0) begin fetch next from new_cursor into @id; end;")
                //.append("\n    end;");
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
                values.append(" + '','' + ''").append(column).append("''");
            } else {
                values.append(" + '','' + isnull(cast(").append(column).append(" as nvarchar), '''')");
            }
        }
        values.delete(0, 10);
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
        // @column_values不包含最后一个字段值
        String columnFilter = null;
        for (int i = 0; i < columnArr.length - 1; i++) {
            String column = columnArr[i];
            if (column.startsWith(TreeDefine.TAB_PREFIX)) {
            	columnFilter = getColumnFilter(column.replaceFirst(TreeDefine.TAB_PREFIX, ""));
            } else if (!column.startsWith(TreeDefine.EMP_PREFIX)) {
                filter.append(column).append(" = dbo.splitSection(").append(i).append(",@column_values)").append(" and ");
            }
        }
        if (StringUtil.isNotEmpty(columnFilter)) {
        	columnFilter = columnFilter.replaceFirst(AppDefineUtil.RELATION_AND, "").concat(AppDefineUtil.RELATION_AND);
        	filter.append(columnFilter);
        }
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
    	
    	if (null != entity && StringUtil.isNotEmpty(entity.getColumnFilter())) {
    		columnFilter = AppDefineUtil.processComplexFilterItem(entity.getColumnFilter());
    	}
    	
    	return columnFilter;
    }
    
    /**
     * qiucs 2013-12-25 
     * <p>描述: 动态节点触发器(编码表)</p>
     * @return void    返回类型   
     * @throws
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
        StringBuffer sb = new StringBuffer();
        boolean isCreate = true;
        if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            String sql = "select 1 from sysobjects where id = object_id(N'[dbo].[" + triggerName + "]') and objectproperty(id, N'IsTrigger') = 1";
            @SuppressWarnings("rawtypes")
            List tgList = DatabaseHandlerDao.getInstance().queryForList(sql);
            if (null != tgList && !tgList.isEmpty()) {
                isCreate = false;
            }
        }
        // create trigger
        sb.append((isCreate ? "create" : "alter") + " trigger ").append(triggerName)
                .append("\non dbo.T_XTPZ_CODE")
                .append("\nafter insert, update, delete")
                .append("\nas")
                .append("\nbegin")
                .append("\n    declare @insert_number  int;")
                .append("\n    declare @delete_number  int;")
                .append("\n    declare @dynamic_node_id nvarchar(32);")
                .append("\n    declare @new_text    nvarchar(255);")
                .append("\n    declare @new_value   nvarchar(255);")
                .append("\n    declare @old_value   nvarchar(255);")
                .append("\n    declare @parent_id   nvarchar(32);")
                .append("\n    declare @parent_ids  nvarchar(255);")
                .append("\n    declare @column_values  nvarchar(200);")
                .append("\n    select @insert_number = count(*) from inserted;")
                .append("\n    select @delete_number = count(*) from deleted;");
        
        if (null != list && !list.isEmpty()) {
            for (TreeDefine entity : list) {
                body.append(dynamicNodeTriggerTemplate(entity));
            }
        }
        if (StringUtil.isEmpty(body) && !isCreate) {
            sb.delete(0, sb.length()).append("drop trigger " + triggerName);
        } else {
            sb.append(body).append("\nend;");
        }
        return ("exec('" + String.valueOf(sb) + "')");
    }
    
    /**
     * qiucs 2013-12-25 
     * <p>描述: 根据编码表来生成动态节点的触发器body</p>
     * @param  cNode
     * @return String    返回类型   
     * @throws
     */
    private String dynamicNodeTriggerTemplate(TreeDefine cNode) {
        StringBuffer body = new StringBuffer();
        try {
            String dynamicId = cNode.getId();
            if (StringUtil.isEmpty(cNode.getDbId())) return null;
            ColumnDefine columnEntity = getService(ColumnDefineService.class).getByID(cNode.getDbId());
            TreeDefine parentNode = getService(TreeDefineService.class).getByID(cNode.getParentId());
            if (null == columnEntity) return null;
            int sort = TreeDefine.T_DESC.equals(cNode.getSortType()) ? 1 : 0;
            StringBuffer filter = new StringBuffer();
            if ("1".equals(parentNode.getNodeRule())) {
                filter.append("t.parent_id=''").append(parentNode.getParentId()).append("'' and t.dynamic_from_id=''").append(parentNode.getId()).append("''");
            } else {
                filter.append("t.id=''").append(parentNode.getId()).append("''");
            }
            body.append("\n    --动态节点ID")
                .append("\n    set @dynamic_node_id=''").append(dynamicId).append("''; set @new_value = null; set @new_text = null; set @old_value = null;")
                .append("\n    declare new_cursor cursor local for select value, name from inserted where code_type_code=''").append(columnEntity.getCodeTypeCode()).append("'';")
                .append("\n    declare old_cursor cursor local for select value from deleted where code_type_code=''").append(columnEntity.getCodeTypeCode()).append("'';")
                .append("\n    open new_cursor;open old_cursor;")
                .append("\n    if (@insert_number > 0) begin fetch next from new_cursor into @new_value, @new_text; end")
                .append("\n    if (@delete_number > 0) begin fetch next from old_cursor into @old_value; end")
                .append("\n    while (@@fetch_status=0) begin")
                .append("\n        declare parent_cursor cursor local for select t.id, t.parent_ids, t.column_values from t_xtpz_tree_define t where ").append(filter).append(";")
                .append("\n        open parent_cursor;")
                .append("\n        fetch parent_cursor into @parent_id, @parent_ids, @column_values;")
                .append("\n        while (@@fetch_status=0) begin")
                .append("\n            exec pt_t_xtpz_tree_define @insert_number, @delete_number, 0, @dynamic_node_id, @parent_id, @parent_ids, @column_values, @new_text, @new_value, @old_value, " + sort + ";")
                .append("\n            fetch parent_cursor into @parent_id, @parent_ids, @column_values;")
                .append("\n        end;")
                .append("\n        close parent_cursor; deallocate parent_cursor;")
                .append("\n        if (@insert_number > 0) begin fetch next from new_cursor into @new_value, @new_text; end;")
                .append("\n        if (@delete_number > 0) begin fetch next from old_cursor into @old_value; end;")
                .append("\n    end;")
                .append("\n    close new_cursor; close old_cursor; deallocate new_cursor; deallocate old_cursor;");
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

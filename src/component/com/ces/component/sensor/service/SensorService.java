package com.ces.component.sensor.service;

import com.ces.component.herb.utils.JSONUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.config.utils.UUIDGenerator;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SensorService  {

  public void insertOrUpdate(String data){
              JSONObject jsonObject = JSONObject.fromObject(data);
        try {
            List<Map<String,String>> dataMap = JSONUtil.jsonStringToList(jsonObject.getJSONArray("data"));
            for(Map<String,String> map:dataMap){
                saveOne("T_SDZYC_TAB_HISTORYDATA",map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
  }

    @Transactional
    protected String saveOne(String tableName, Map<String, String> dataMap) {
        String id = dataMap.get(AppDefineUtil.C_ID);
        if (StringUtil.isEmpty(id)) {
            //新增
            id = UUIDGenerator.uuid();
            dataMap.put("id", id);
            insert(tableName, dataMap);
        } else {
            update(tableName, dataMap);
        }
        return id;
    }

    /**
     * 插入数据操作
     * @param tableName
     * @param dataMap
     */
    protected void insert(String tableName, Map<String, String> dataMap) {
        String tableId = TableUtil.getTableId(tableName);
        Set<String> set = dataMap.keySet();
        Iterator<String> it = set.iterator();
        StringBuffer cSql = new StringBuffer("");
        StringBuffer vSql = new StringBuffer("");
        while(it.hasNext()) {
            String key = it.next();
            String val = dataMap.get(key);
            cSql.append(",").append(key);
            vSql.append(",'").append(StringUtil.null2empty(val)).append("'");
        }
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ")
                .append(tableName).append("(").append(cSql.substring(1)).append(") values (")
                .append(vSql.substring(1)).append(")");
        //System.out.println(sql);
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        dao.executeSql(sql.toString());
    }

    /** 根据ID修改指定表中数据 */
    @Transactional
    protected void update(String tableName, Map<String, String> dataMap) {
        //
        String id = dataMap.get(AppDefineUtil.C_ID);
        dataMap.remove(AppDefineUtil.C_ID);
        //
        String tableId = TableUtil.getTableId(tableName);
//        if (StringUtil.isNotEmpty(tableId)) {
//            long cnt = 0;
//            if (!dataMap.containsKey("UPDATE_USER")) {
//                getService(ColumnDefineService.class).count("EQ_columnName=UPDATE_USER;EQ_tableId=" + tableId);
//
//            }
//            if (!dataMap.containsKey("UPDATE_TIME")) {
//                cnt = getService(ColumnDefineService.class).count("EQ_columnName=UPDATE_TIME;EQ_tableId=" + tableId);
//                if (cnt > 0) {
//                    dataMap.put("UPDATE_TIME", DateUtil.currentTime());
//                }
//            }
//        }
        //
        Set<String> set = dataMap.keySet();
        Iterator<String> it = set.iterator();
        StringBuffer cSql = new StringBuffer("");
        while(it.hasNext()) {
            String key = it.next();
            String val = dataMap.get(key);
            cSql.append(",").append(key).append("='").append(StringUtil.null2empty(val)).append("'");
        }

        StringBuffer sql = new StringBuffer();
        sql.append("update ")
                .append(tableName).append(" set ").append(cSql.substring(1))
                .append(" where ")
                .append(AppDefineUtil.C_ID).append("='").append(id).append("'");
        //System.out.println(sql);
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        dao.executeSql(sql.toString());

        dataMap.put(AppDefineUtil.C_ID, id);
    }
}

package com.ces.component.hjtds.service;

import com.ces.component.herb.dto.HerbDao;
import com.ces.component.herb.utils.JSONUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.utils.*;
import com.ces.xarch.core.entity.StringIDEntity;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2016/7/26.
 */
@Component
public class HjtdsService extends TraceShowModuleDefineDaoService<StringIDEntity, HerbDao> {
    public String tab_ypscxx = "T_SDZYC_JJG_YPSCXX";//饮片生产表
    public String tab_yycrkxx = "T_SDZYC_JJG_YYCRKXX";//原药材入库表
    /**
     * 处理从宏济堂系统传递过来的JSON数据,并拆分后
     * 分别保存到精加工饮片生产信息和原药材入库信息表中
     * @param data
     */
    public Map<String,String> processHjtdsJsonData(String data){
        JSONObject jsonObject = JSONObject.fromObject(data);//将前台传过来的数据转换为object
//        JSONObject pdata = jsonObject.getJSONObject("pdata");
        Map<String,String> dataMap = new HashMap<String, String>();
        try{
            dataMap = JSONUtil.jsonStringToMap(jsonObject);//转换为Map<String,String>
            String id = UUIDGenerator.uuid();
            Map<String,String> ypMap = new HashMap<String, String>();//饮片
            //数据拆分
            ypMap.put("ID",id);
            ypMap.put("YPBM",dataMap.get("PLNBEZ"));
            ypMap.put("MS",dataMap.get("MAKTX"));
            ypMap.put("SCPCH",dataMap.get("CHARG"));
            ypMap.put("SCRQ",dataMap.get("HSDAT"));
            ypMap.put("DQR",dataMap.get("VFDAT"));
            Map<String,String> yycMap = new HashMap<String, String>();//原药材
            ypMap.put("ID",id);
            yycMap.put("YCDM",dataMap.get("MATNR_YCL"));
            yycMap.put("YCMC",dataMap.get("MAKTX_YCL"));
            yycMap.put("PCH",dataMap.get("CHARG_YCL"));
            yycMap.put("ZFLAG",dataMap.get("ZFLAG_ZH"));

            Map<String, Object> qdata = queryOne(tab_ypscxx, dataMap.get("PLNBEZ"));
            if(!StringUtil.isNotEmpty(qdata)){
                insert(tab_ypscxx,ypMap);
                insert(tab_yycrkxx,yycMap);
            }else{
                String uid = String.valueOf(qdata.get("id"));
                update(tab_ypscxx,ypMap,uid);
                update(tab_yycrkxx,yycMap,uid);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return dataMap;
    }

    public Map<String, Object> queryOne( String tabname,String ypbm ){
        String sql = " select * from "+tabname +" where ypbm="+ypbm +"'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    protected void insert(String tableName, Map<String, String> dataMap) {
        String tableId = TableUtil.getTableId(tableName);
        if (StringUtil.isNotEmpty(tableId)) {
            long cnt = 0;
            if (!dataMap.containsKey("CREATE_USER")) {
                getService(ColumnDefineService.class).count("EQ_columnName=CREATE_USER;EQ_tableId=" + tableId);

            }
            if (!dataMap.containsKey("CREATE_TIME")) {
                cnt = getService(ColumnDefineService.class).count("EQ_columnName=CREATE_TIME;EQ_tableId=" + tableId);
                if (cnt > 0) {
                    dataMap.put("CREATE_TIME", DateUtil.currentTime());
                }
            }
        }
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
    protected void update(String tableName, Map<String, String> dataMap,String id) {
        //
//        String id = dataMap.get(AppDefineUtil.C_ID);
        dataMap.remove(AppDefineUtil.C_ID);
        //
        String tableId = TableUtil.getTableId(tableName);
        if (StringUtil.isNotEmpty(tableId)) {
            long cnt = 0;
            if (!dataMap.containsKey("UPDATE_USER")) {
                getService(ColumnDefineService.class).count("EQ_columnName=UPDATE_USER;EQ_tableId=" + tableId);

            }
            if (!dataMap.containsKey("UPDATE_TIME")) {
                cnt = getService(ColumnDefineService.class).count("EQ_columnName=UPDATE_TIME;EQ_tableId=" + tableId);
                if (cnt > 0) {
                    dataMap.put("UPDATE_TIME", DateUtil.currentTime());
                }
            }
        }
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

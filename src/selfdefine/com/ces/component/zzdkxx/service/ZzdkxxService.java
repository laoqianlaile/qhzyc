package com.ces.component.zzdkxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZzdkxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public int isExistZzdyxx(String ids){
        String sql = "select ZZDYBH from t_zz_dy d where d.dkbh in(select t.DKBH from t_zz_dkxx t where t.id in('" + ids.replace(",", "','") + "')) and d.is_delete <> '1' and qybm='"+ SerialNumberUtil.getInstance().getCompanyCode()+"' ";
        List<Map<String,Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        String existName = "";
        if(dataMap!=null && !dataMap.isEmpty()){
            return dataMap.size();
        }
        return 0;
    }

    public Object getCGQZ(){
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String sql="select T.BH,T.SBSBH AS MC,T.PP,T.XH FROM T_QYPT_SBGL T WHERE T.QYBM = '" + code + "' AND T.IS_DELETE <> '1' AND T.LB = 'CGQ'";
        List<Map<String,Object>> list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data" , list);
        return dataMap;
    }

    @Override
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        return super.save(tableId, entityJson, paramMap);
    }

    @Override
    @Transactional
    public String save(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
        StringBuilder sql;
        //如果是修改，先修改以前被使用的传感器组的使用状态
        if(!"".equals(String.valueOf(dataMap.get("ID")))){
            sql = new StringBuilder("SELECT T.CGQZ FROM T_ZZ_DKXX T WHERE T.ID = '" + String.valueOf(dataMap.get("ID")) + "'");
            String oldCgqs [] = String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql.toString()).get("CGQZ")).split(",") ;
            sql = new StringBuilder("UPDATE T_QYPT_SBGL T SET T.SYZT = '0' WHERE T.SBSBH IN ('!'");
            for(int i = 0 ; i < oldCgqs.length ; i++){
                sql.append(",'" + oldCgqs[i] + "'");
            }
            sql.append(")");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        }
        //修改修改之后传感器的使用状态
        if(!"".equals(String.valueOf(dataMap.get("CGQZ"))) && !"null".equals(String.valueOf(dataMap.get("CGQZ")))){
            String cgqs[] = dataMap.get("CGQZ").split(",");
            sql = new StringBuilder("UPDATE T_QYPT_SBGL T SET T.SYZT = '1' WHERE T.SBSBH IN ('!'");
            for(int i = 0 ; i < cgqs.length ; i++){
                sql.append(",'" + cgqs[i] + "'");
            }
            sql.append(")");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        }else{
            dataMap.put("CGQZ","");
        }
         return super.save(tableName, dataMap, paramMap);
    }



    public Object validCgqz(String id, String cgqz){
        StringBuilder sql = new StringBuilder("SELECT T.CGQZ FROM T_ZZ_DKXX T WHERE T.ID = '" + id + "'");
        Map<String,Object> oldCgqzMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
        String cgqs[] = cgqz.split(",");
        String oldCgqs[] = String.valueOf(oldCgqzMap.get("CGQZ")).split(",");
        for(int i = 0; i < cgqs.length ; i++){
           for(int j = 0; j < oldCgqs.length ; j++){
               if(cgqs[i].equals(oldCgqs[j])){
                   cgqs[i] = "!";
               }
           }
        }
        sql = new StringBuilder("SELECT count(*) AS CT FROM T_QYPT_SBGL T WHERE T.SBSBH IN ('!'");
        for(int i = 0; i < cgqs.length ; i++){
            sql.append(",'" + cgqs[i] + "'");
        }
        sql.append(") AND T.SYZT = '1' AND T.IS_DELETE <> '1'");
        Map<String,Object> ctMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
        return ctMap.get("CT");
    }
}
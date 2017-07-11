package com.ces.component.sdzycyljyxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycyljyxx.dao.SdzycyljyxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycyljyxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycyljyxxDao> {
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String xsddh = StatisticalCodeUtil.getInstance().getTwentyFivePcm("ZZ","SDZYC", "SDZYCXSDDH");
            String qyxsddh = xsddh.substring(xsddh.length()-11,xsddh.length());
            dataMap.put("QYXSDDH",qyxsddh);
            dataMap.put("XSDDH", xsddh);
        }
        id = save(tableName, dataMap, paramMap);
        String updSql ="update t_sdzyc_csglxx set sfjy='1' where  qycspch=?";
        DatabaseHandlerDao.getInstance().executeSql(updSql,new String[]{String.valueOf(dataMap.get("QYPCH"))});
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
    public void delete(String cspch,String id){

                String Updatesql = "update t_sdzyc_csglxx  set sfjy='0' where cspch=?";
                DatabaseHandlerDao.getInstance().executeSql(Updatesql, new Object[]{cspch});
                String sql = " delete from t_sdzyc_yljyxx where id  = '" + id + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
    }
    public Map<String,Object> searchDataByCspch(String cspch){
        String sql ="select * from t_sdzyc_yljyxx where cspch='"+cspch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
}


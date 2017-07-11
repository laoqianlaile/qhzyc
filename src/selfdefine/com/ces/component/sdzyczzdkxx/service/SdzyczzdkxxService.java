package com.ces.component.sdzyczzdkxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyczzdkxx.dao.SdzyczzdkxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyczzdkxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyczzdkxxDao> {
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String dkbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCDKBH", false);
            dataMap.put("DKBH",dkbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

    public Map<String,Object> getJdmc() {
        String sql = "select JDBH,JDMC,DZ,MJ from T_SDZYC_JDXX where " + defaultCode() + "order by QYBM ASC,JDBH ASC";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * 根据id  获取地块信息
     * @return
     */
    public Map<String,Object> getDkxx(String id){
        //String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql ="select * from T_SDZYC_ZZDKXX where id = '"+id+"'  ";
        return  getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * 获取基地面积
     * @param jdbh
     * @return
     */
    public  Map<String,Object> getJdxx(String jdbh){
        String sql = "select * from T_SDZYC_JDXX   where jdbh='"+jdbh+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Map<String,Object> getSbsbh(String cgqz){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql ="select T.BH,T.SBSBH ,T.PP,T.XH FROM T_QYPT_SBGL T WHERE T.QYBM = '" + qybm + "' AND T.IS_DELETE <> '1' AND T.SBSBH = '"+cgqz+"'";
        return  getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode =" QYBM = '" + code + "' and is_delete <> '1'";
        return defaultCode;
    }
}

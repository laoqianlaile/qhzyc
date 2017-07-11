package com.ces.component.sdzyccjgyldbxx.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyccjgyldbxx.dao.SdzyccjgyldbxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class SdzyccjgyldbxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgyldbxxDao> {
    @Transactional
    @Override
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        //原料调拨信息保存成功，修改原料数据中所在仓库信息
        updataYlxx(dataMap);
        return dataMap;
    }

    public int updataYlxx(Map<String,String> dataMap){
        String sql = "update T_SDZYC_CJG_YLRKXX set SLCK= ? where PCH = ?";
        return  DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{dataMap.get("DRCK"),dataMap.get("PCH")});
    }
}

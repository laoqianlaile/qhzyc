package com.ces.component.ickxx.service;

import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.component.trace.utils.DatabaseUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.ickxx.dao.ICkxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class ICkxxService extends TraceShowModuleDefineDaoService<StringIDEntity, ICkxxDao> {

    @Transactional
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String khmc = dataMap.get("KHMC");
        String khdm = dataMap.get("KHDM");
        String sql="update t_QH_PFSHXX set icbdzt= '1'WHERE SHMC='"+khmc+"' and SHBM='"+khdm+"'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        String id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
}

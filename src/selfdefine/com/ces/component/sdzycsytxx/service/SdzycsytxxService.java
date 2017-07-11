package com.ces.component.sdzycsytxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycsytxx.dao.SdzycsytxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class SdzycsytxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycsytxxDao> {

    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String dkbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCSYTDKBH", false);
            dataMap.put("DKBH",dkbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
}

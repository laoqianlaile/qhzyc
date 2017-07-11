package com.ces.component.sdzycymxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycymxx.dao.SdzycymxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class SdzycymxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycymxxDao> {

    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String ymbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCYMBH", false);
            dataMap.put("YMBH",ymbh);
            dataMap.put("QYBM",SerialNumberUtil.getInstance().getCompanyCode());
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
}

package com.ces.component.sdzycjgcjxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjgcjxx.dao.SdzycjgcjxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class SdzycjgcjxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjgcjxxDao> {

    /**
     * 改写save方法  保存时 自动生成车间编号
     * @param tableId
     * @param entityJson
     * @param paramMap --参数Map（具体参数要求请查看ShowModuleDefineServiceDaoController.getMarkParamMap方法说明）
     * @return
     */
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String jjgcjbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGCJBH", false);
            dataMap.put("CJBH",jjgcjbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
}

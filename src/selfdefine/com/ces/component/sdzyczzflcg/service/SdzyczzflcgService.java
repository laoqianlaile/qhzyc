package com.ces.component.sdzyczzflcg.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyczzflcg.dao.SdzyczzflcgDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyczzflcgService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyczzflcgDao> {
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String flcgbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCFLCGBH", false);
            dataMap.put("FLCGBH",flcgbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

    public Object getFlxx() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();//000000824
        String sql="select t.FLBH,t.FLBZMC from T_SDZYC_FLXX t where t.qybm = ? and t.is_delete = '0'";
        // List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[] {qybm});
        // List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data",maps);
        return resultMap;
    }
}

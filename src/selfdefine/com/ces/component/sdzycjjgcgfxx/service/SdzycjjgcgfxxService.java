package com.ces.component.sdzycjjgcgfxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjjgcgfxx.dao.SdzycjjgcgfxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycjjgcgfxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgcgfxxDao> {
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if (StringUtil.isEmpty(id)) {//是新增操作
            String flbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGCGFXX", false);
            dataMap.put("CGSBH", flbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

    public Object getGzryda() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();//000000824
        String sql="select t.CGSBH,t.CGSMC from T_SDZYC_JJG_CGFXX t where t.qybm = ? and t.is_delete = '0'";
        // List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[] {qybm});
        // List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data",maps);
        return resultMap;
    }
    
}

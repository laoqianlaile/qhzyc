package com.ces.component.sdzycflxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycflxx.dao.SdzycflxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.List;
import java.util.Map;

@Component
public class SdzycflxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycflxxDao> {

    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String flbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCFLBH", false);
            dataMap.put("FLBH",flbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
    /**
     * 根据id获取类型
     * @return
     */
    public Object getLxById(String id) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select lx from t_sdzyc_flxx  where  qybm=? and id=?";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm,id});
        return map;
    }
    /**
     * 获取类型
     * @return
     */
    public Object getLx() {
        String sql = "select distinct sjbm as value, sjmc as text from t_common_sjlx_code  where sjbm = 'FL'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }
}

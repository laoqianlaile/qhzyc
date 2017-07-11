package com.ces.component.sdzycjjgckxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjjgckxx.dao.SdzycjjgckxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
@Component
public class SdzycjjgckxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgckxxDao> {
    /**
     * 获取山东中药材精加工仓库信息
     * @return
     */
    public Map<String,Object> getCkxx(){
        String sql = "select t.CKBH,t.CKMC from T_SDZYC_JJG_CKXX t where t.qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"' ";
        List<Map<String,Object>> listData = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data", listData);
        return dataMap;
    }

    /**
     * 山东中药材精加工仓库编号自动生成 保存
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
            String jjgckbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGCKBH", false);
            dataMap.put("CKBH",jjgckbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
}

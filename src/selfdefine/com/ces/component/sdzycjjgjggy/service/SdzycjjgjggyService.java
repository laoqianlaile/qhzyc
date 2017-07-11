package com.ces.component.sdzycjjgjggy.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjjgjggy.dao.SdzycjjgjggyDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycjjgjggyService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgjggyDao> {

    /**
     * 精加工加工工艺应用药材下拉框
     * @return
     */
    public Map<String,Object> getJggyGrid(){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from T_SDZYC_JJG_YYCRKXX t where t.qybm= '"+companyCode+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> searchjggyxxBygybh(){
        String sql = "select * from t_sdzyc_jjg_jggy where qybm=?";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return getGridTypeData(list);
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    /**
     * 山东中药材精加工工艺编号自动生成 保存
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
            String jjgjgbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGGYBH", false);
            dataMap.put("GYBH",jjgjgbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
}

package com.ces.component.sdzyccjgckxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyccjgckxx.dao.SdzyccjgckxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyccjgckxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgckxxDao> {
    /**
     *
     * @param tableId--获取仓库编号
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
            String cjgckbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCJGCKBH", false);
            dataMap.put("CKBH",cjgckbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    } /**
     *默认权限过滤
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }

    /**
     * 获取仓库信息下拉列表数据
     * @return
     */
    public Map<String,Object> searchckxxComboGridData(){
        String sql = "select * from t_sdzyc_cjg_ckxx where 1=1 "+defaultCode();
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> searchdbckxxComboGridData(String slck){
        String sql = "select * from t_sdzyc_cjg_ckxx where 1=1 "+defaultCode();
        if(!StringUtil.isEmpty(slck)){
            sql += " and  ckmc <> '"+slck+"' ";
            //sql += "order by ckbh des";
        }
        sql+=" order by ckbh desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    public Map<String,Object> searchKhxxByckbh(String ckbh){
        String sql =  "select * from t_sdzyc_cjg_ckxx where ckbh=? "+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{ckbh});
    }
}
    


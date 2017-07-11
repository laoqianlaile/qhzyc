package com.ces.component.sdzyccjgcjxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyccjgcjxx.dao.SdzyccjgcjxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyccjgcjxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgcjxxDao> {
    /**
     *
     * @param tableId--获取粗加工车间编号
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
            String cjgcjbh= SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCJGCJBH", false);
            dataMap.put("CJBH",cjgcjbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }



    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    public Map<String,Object> searchcjxxBycjbh(){
        String sql = "select * from t_sdzyc_cjg_cjxx where qybm=?";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return getGridTypeData(list);
    }

    /**
     *
     * @return
     */
    public Map<String,Object> searchcdzmxxComboGridData(){
        String sql = "select * from t_sdzyc_cjg_Cdzm where 1=1";///+ " order by llsj desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * 获取保存图片名称  通过上传图片
     * @param sctpmc
     * @return
     */
    public Map<String,Object> searchcdzmxxData(String sctpmc){
        String sql ="select * from t_sdzyc_cjg_Cdzm where sctpmc='"+sctpmc+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    //    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
//        Map<String,Object> dataMap = new HashMap<String, Object>();
//        dataMap.put("data",list);
//        return  dataMap;
//    }
    public Map<String,Object> searchcdzmxxBylldh(String sctpmc){
        String sql =  "select * from t_sdzyc_cjg_Cdzm";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{sctpmc});
    }
    /**
     *默认权限过滤
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" SCTPMC= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
}


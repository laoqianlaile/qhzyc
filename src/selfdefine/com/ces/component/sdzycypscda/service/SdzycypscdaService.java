package com.ces.component.sdzycypscda.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycypscda.dao.SdzycypscdaDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycypscdaService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycypscdaDao> {


    /**
     * 查询生产档案信息
     *
     *
     * @return
     */
    public Map<String, Object> searchTrpscdaxx() {
        String sql = "select * from t_sdzyc_jjg_scfazb   ";
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    /**
     * combobox与combogrid数据格式化处理
     *
     * @param dataMap
     * @return
     */
    public Map<String, Object> getGridAndListData(List<Map<String, Object>> dataMap) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", dataMap);
        return data;
    }

    /**
     * 保存出库信息
     *
     * @param entityJson
     * @param dEntitiesJson
     * @return
     */
    @Transactional
    public Object saveTrplyxx(String entityJson,  String dEntitiesJson) {
        Map<String, Object> returnData = new HashMap<String, Object>();
        boolean inserted = false;
        //父表json数据
        JsonNode entity = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entity);
        if (StringUtil.isEmpty(dataMap.get(AppDefineUtil.C_ID)) || dataMap.get(AppDefineUtil.C_ID).startsWith("tem_")) {
            dataMap.put(AppDefineUtil.C_ID, UUIDGenerator.uuid());
            inserted = true;
        }
        //Map<String, String> relateDateMap = getRelateDateMap(tableId, dTableId, dataMap);
        String tableName = "T_SDZYC_YPSCFA",  dTableName = "T_SDZYC_JJG_SCFAZB";
        // 保存明细记录
        List<Map<String, String>> dList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> pList = new ArrayList<Map<String, String>>();
        String id = null;
        JsonNode entities = JsonUtil.json2node(dEntitiesJson);
       // JsonNode pEntities = JsonUtil.json2node(pEntitiesJson);
        Map<String, String> dMap = new HashMap<String, String>();
        Map<String, String> pMap = new HashMap<String, String>();
        //删除历史数据
        //updataTrpcgkcl(String.valueOf(dataMap.get("ID")));
        //循环遍历生产档案管理列表数据进行保存
        for (int i = 0, len = entities.size(); i < len; i++) {
            pMap = node2map(entities.get(i));
            pMap.put("ID", dataMap.get("ID"));
//            pMap.remove("CZ");
            id = pMap.get(AppDefineUtil.C_ID);
           if (StringUtil.isNotEmpty(id) && id.startsWith("UNSAVE_")) {
                pMap.remove(AppDefineUtil.C_ID);
           }
            pMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
            //保存生产档案的一条记录
            id = saveOne(dTableName, pMap);
            pList.add(pMap);
            //循环遍历投入品列表信息进行保存操作
//            for (int j = 0, clen = entities.size(); j < clen; j++) {//需要删除保存的历史
//                String cid = "";
//                dMap = node2map(entities.get(j));
//                //如果保存的是属于同一个农事操作项就进行保存
//                if (dMap.get("NSX").equals(pMap.get("NSX"))) {
//                    dMap.put("PID", id);
//                    dMap.remove("CZ");
//                    cid = dMap.get(AppDefineUtil.C_ID);
//                    if (StringUtil.isNotEmpty(cid) && cid.startsWith("UNSAVE_")) {
//                        dMap.remove(AppDefineUtil.C_ID);
//                    }
//                    dMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
//                    cid = saveOne(dTableName, dMap);
//                    String trpSql = " update t_zz_trpcggl set KCSL=(KCSL-" + dMap.get("CKSL") + ") where TRPBH='" + dMap.get("TRPBH") + "'" + defaultCode();
//                    DatabaseHandlerDao.getInstance().executeSql(trpSql);
//                    dList.add(dMap);
//                }
//
//            }
        }

        dataMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
        // 保存主表记录
        saveOne(tableName, dataMap, inserted);
        //
        returnData.put("master", dataMap);
        returnData.put("pDetail", pList);
        returnData.put("dDetail", dList);
        return returnData;
    }
    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code + "'" + AppDefineUtil.RELATION_AND + " is_delete != '1' ";
        return defaultCode;
    }

    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String ssfabh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGSCFABH", true);
            dataMap.put("SCFABH",ssfabh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

}

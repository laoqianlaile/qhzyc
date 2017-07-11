package com.ces.component.cjgylllxxxz.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.*;
import com.ces.xarch.core.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.cjgylllxxxz.dao.CjgylllxxxzDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CjgylllxxxzService extends TraceShowModuleDefineDaoService<StringIDEntity, CjgylllxxxzDao> {

    @Override
    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
        Map<String, Object> returnData = new HashMap<String, Object>();
        boolean inserted = false;
        JsonNode entity = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entity);
        if (StringUtil.isEmpty(dataMap.get(AppDefineUtil.C_ID))) {
            inserted = true;
            dataMap.put(AppDefineUtil.C_ID, UUIDGenerator.uuid());
            String zyclldh= SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCLLDH", false);
            dataMap.put("LLDH",zyclldh);
        }
        Map<String, String> relateDateMap = getRelateDateMap(tableId, dTableId, dataMap);
        String tableName = getTableName(tableId), dTableName = getTableName(dTableId);
        // 保存明细记录
        List<Map<String, String>> detailList   = saveDetail(dTableName, dEntitiesJson, dataMap, relateDateMap, paramMap);
        // 保存明细后业务逻辑处理
        processMiddleSaveAll(tableName, dTableName, dataMap, detailList, paramMap);
        // 保存主表记录
        saveOne(tableName, dataMap, inserted);
        // 保存主表和明细后业务逻辑处理
        processAfterSaveAll(tableName, dTableName, dataMap, detailList, paramMap);
        //
        returnData.put("master", dataMap);
        returnData.put("detail", detailList);

        return MessageModel.trueInstance(returnData);
    }
    /**
     * qiucs 2015-2-28 上午11:24:51
     * <p>描述: 获取从表与主表关联关系的字段值 </p>
     * @return Map<String,String>
     */
    private Map<String, String> getRelateDateMap(String tableId, String dTableId, Map<String, String> dMap) {
        Map<String, List<String>> relationMap = TableUtil.getTableRelation(tableId, dTableId);
        if (null == relationMap || relationMap.isEmpty()) {
            throw new BusinessException("未获取到表（" + TableUtil.getTableName(tableId) + "）与表（" + TableUtil.getTableName(dTableId) + "）之间的关系字段，请检查这两张表的表关系配置！");
        }
        List<String> mList = relationMap.get(tableId);
        List<String> dList = relationMap.get(dTableId);

        Map<String, String> relateDateMap = new HashMap<String, String>();

        for (int i = 0, len = mList.size(); i < len; i++) {
            relateDateMap.put(dList.get(i), dMap.get(mList.get(i)));
        }

        return relateDateMap;
    }

    /**
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
     * 获取原料领料的原料批次号
     * @return
     */
    public Map<String,Object> searchylllxxComboGridData(){
        String sql = "select * from t_sdzyc_cjg_ylllxxxx where 1=1 "+defaultCode();
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    public Map<String,Object> searchylllxxByllpch(String pch){
        String sql =  "select * from t_sdzyc_cjg_ylllxxxx where pch=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{pch});
    }

  public Map<String,Object> searchylllxxxxComboGridData(String lldh) {
      String sql = "select * from t_sdzyc_cjg_ylllxxxx where lldh='"+ lldh +"'"+ defaultCode();
      return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
  }

}




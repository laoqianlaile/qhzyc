package com.ces.component.sdzycjjgscllxz.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.*;
import com.ces.xarch.core.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjjgscllxz.dao.SdzycjjgscllxzDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycjjgscllxzService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgscllxzDao> {

    /**
     * 重写保存生产领料订单号的方法,自动生成领料订单号,并保存
     * qiucs 2015-2-28 上午11:25:58
     * <p>描述: 保存主从表的所有数据 </p>
     * @return Object
     */
    @Transactional
    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
        Map<String, Object> returnData = new HashMap<String, Object>();
        boolean inserted = false;
        JsonNode entity = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entity);
        if (StringUtil.isEmpty(dataMap.get(AppDefineUtil.C_ID))) {
            inserted = true;
            dataMap.put(AppDefineUtil.C_ID, UUIDGenerator.uuid());
            String xxddhh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGSCLLDH", true);
            dataMap.put("LLDH",xxddhh);
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
    @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        // 1. 获取所有关联表的要删除的IDS
        String tableName = "T_SDZYC_JJG_SCLLXX";
        String[] idDatas=ids.split(",");
        StringBuffer newIds=new StringBuffer("");
        for (int i = 0; i < idDatas.length; i++) {
            if(i >0 && i<=idDatas.length-1){
                newIds.append(",");
            }
            String string = idDatas[i];
            newIds.append("'"+string.split("_")[0]+"'");
        }
        String filter    = "DELETE FROM "+tableName + " WHERE ID IN (" + newIds.toString() + ")";
        DatabaseHandlerDao.getInstance().executeSql(filter);
    }
}

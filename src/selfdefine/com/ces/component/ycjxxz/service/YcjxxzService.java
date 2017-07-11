package com.ces.component.ycjxxz.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.QRCodeUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.*;
import com.ces.xarch.core.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TraceEntity;
import enterprise.entity.TradeOutEntity;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.ycjxxz.dao.YcjxxzDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class YcjxxzService extends TraceShowModuleDefineDaoService<StringIDEntity, YcjxxzDao> {

    public List<Map<String, Object>> searchYcpch(String qypch){
        // from v_sdzyc_jjg_ylkcglxx where kc > 0
        String sql = "select PCH,YCDM,YCMC,CSPCH,QYPCH,RKSJ,RKZL from t_sdzyc_jjg_yycrkxx t where qypch='"+qypch+"'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    /**
     * 重写保存销售订单的方法,自动生成销售订单号,病保存
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
            String xxddhh = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG","SDZYC", "SDZYCJJGYCJYXSDDH");
            String qyxsddh = xxddhh.substring(xxddhh.length()-11,xxddhh.length());
            dataMap.put("QYXSDDH",qyxsddh);
            dataMap.put("XSDDH",xxddhh);
            try {
                //根据采收批次号进行随附单生成
                QRCodeUtil.encode(xxddhh, ServletActionContext.getServletContext().getRealPath("/qrCode/jjg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dataMap.remove("JYDJ");
        dataMap.remove("JYZL");
        dataMap.remove("YCMC");
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
     * qiucs 2015-2-28 上午11:24:30
     * <p>描述: 保存明细表数据 </p>
     * @return Object
     */
    @Transactional
    protected List<Map<String, String>> saveDetail(String tableName, String entitiesJson,
                                                   Map<String, String> masterMap,
                                                   Map<String, String> relateDateMap,
                                                   Map<String, Object> paramMap) {
        ;        List<Map<String, String>> dList = new ArrayList<Map<String, String>>();

        String id = null;
        JsonNode entities = JsonUtil.json2node(entitiesJson);
        Map<String, String> dataMap = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = entities.size(); i < len; i++) {
            dataMap = node2map(entities.get(i));
            dataMap.putAll(relateDateMap);
            id = dataMap.get(AppDefineUtil.C_ID);
            if (StringUtil.isNotEmpty(id) && id.startsWith("UNSAVE_")) {
                dataMap.remove(AppDefineUtil.C_ID);
                processBeforeSaveOneDetail(tableName, dataMap, masterMap, paramMap);
                dataMap.put("YPTZSM", dataMap.get("XSDDH")+dataMap.get("PCH"));
                id = saveOne(tableName, dataMap);
                sb.append(",'").append(id).append("'");
                processAfterSaveOneDetail(tableName, dataMap, masterMap, paramMap);
                dList.add(dataMap);
                sendCreatetTraceOutService(dataMap, masterMap);
            }else {
                processBeforeSaveOneDetail(tableName, dataMap, masterMap, paramMap);
                id = saveOne(tableName, dataMap);
                sb.append(",'").append(id).append("'");
                processAfterSaveOneDetail(tableName, dataMap, masterMap, paramMap);
                dList.add(dataMap);
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            deleteOverDetail(tableName, sb.toString(), relateDateMap);
        }
        return dList;
    }

    private void sendCreatetTraceOutService(Map<String, String> dataMap, Map<String, String> masterMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = new TradeOutEntity();
        tradeOutEntity.setId(dataMap.get("ID"));
        tradeOutEntity.setBatch_no(dataMap.get("PCH"));
        tradeOutEntity.setOrder_no(dataMap.get("XSDDH"));
        tradeOutEntity.setHerb_name(dataMap.get("YPMC"));
        tradeOutEntity.setHerb_code(dataMap.get("YCDM"));
        tradeOutEntity.setIs_tested(dataMap.get("SFJY"));
        tradeOutEntity.setWeight(Float.parseFloat(dataMap.get("JYZL")));
        tradeOutEntity.setPrice(Float.parseFloat(dataMap.get("JYDJ")));
        tradeOutEntity.setDate(dateToLong(masterMap.get("JYSJ")));
        tradeOutEntity.setBuyer(masterMap.get("CGF"));
        tradeOutEntity.setComp_code(dataMap.get("QYBM"));
        tradeOutEntity.setComp_type(dataMap.get("2"));
        tradeOutEntity.setHerb_name_detail(("ycdetail"));
        //tradeOutEntity.setTraceEntities((List<TraceEntity>) traceMap.get("traceEntity"));
        service.createTradeOut(tradeOutEntity);
    }


    private Long dateToLong(String str){
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        long t = 0;
        try{
            if (StringUtil.isNotEmpty(str)) {
                date = dd.parse(str);
                t = date.getTime();
            }
        }catch(ParseException e){
            e.printStackTrace();
        }
        return t;
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
        String tableName = "T_SDZYC_JJG_YCJYXXX";
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

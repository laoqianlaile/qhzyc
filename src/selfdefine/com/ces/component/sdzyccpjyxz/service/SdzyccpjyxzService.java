package com.ces.component.sdzyccpjyxz.service;

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

import com.ces.component.sdzyccpjyxz.dao.SdzyccpjyxzDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SdzyccpjyxzService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccpjyxzDao> {
    /**
     * 精加工饮片交易信息批次号下拉列表
     * @return
     */
    public  Map<String,Object> searchYpjypchGridData(){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from T_SDZYC_JJG_YPRKXX t where t.qybm= '"+companyCode+"'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return dataMap;
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
            String xxddhh = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG","SDZYC", "SDZYCJJGYPJYXSDDH");
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
                sendCreatetTraceOutService(dataMap, masterMap, createTraceInfo(dataMap));
            }else {
                processBeforeSaveOneDetail(tableName, dataMap, masterMap, paramMap);
                dataMap.put("YPTZSM", dataMap.get("XSDDH")+dataMap.get("PCH"));
                id = saveOne(tableName, dataMap);
                sb.append(",'").append(id).append("'");
                processAfterSaveOneDetail(tableName, dataMap, masterMap, paramMap);
                dList.add(dataMap);
                sendModifyTraceOutService(id,dataMap, masterMap, createTraceInfo(dataMap));
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            deleteOverDetail(tableName, sb.toString(), relateDateMap);
        }
        return dList;
    }

    private void sendCreatetTraceOutService(Map<String, String> dataMap, Map<String, String> masterMap, Map<String, Object> traceMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = traceOutService(dataMap,masterMap,traceMap);
        service.createTradeOut(tradeOutEntity);
    }
    private void sendModifyTraceOutService(String id, Map<String, String> dataMap, Map<String, String> masterMap, Map<String, Object> traceMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = traceOutService(dataMap,masterMap,traceMap);
        service.modifyTradeOut(id,tradeOutEntity);
    }
    private void sendDelTraceOutService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteTradeOut(id);
    }
    private TradeOutEntity traceOutService(Map<String, String> dataMap, Map<String, String> masterMap, Map<String, Object> traceMap)
    {
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
        tradeOutEntity.setComp_type("3");
        tradeOutEntity.setHerb_name_detail(("ycdetail"));
        tradeOutEntity.setBzpch(dataMap.get("BZPCH"));
        String sql = "select t.id  from t_sdzyc_jjg_ypbzxx t where t.bzpch=?";
        Map<String, Object> pMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("BZPCH")});
        tradeOutEntity.setPack_id(pMap.get("ID").toString());
        tradeOutEntity.setTraceEntities((List<TraceEntity>) traceMap.get("traceEntity"));
        return tradeOutEntity;
    }
    public Map<String, Object> createTraceInfo(Map<String, String> dataMap){
        String sql = "select t.id, t.cspch, t.scpch from t_sdzyc_jjg_ypscxx t where t.scpch=?";
        Map<String, Object> traceMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("PCH")});
        String sql1 = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        traceMap.put("ycdetail",DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("YCDM")}).get("YCXMNAME"));
        List<TraceEntity> traceEntities = new ArrayList<TraceEntity>();
        sql1 = "select t.yptzsm from t_sdzyc_jjg_yycrkxx t where t.cspch=? and t.cspch<>''";
        Map<String, Object> rkMap = DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("CSPCH")});
        String sqlp = "select t.id  from t_sdzyc_jjg_ypbzxx t where t.bzpch=?";
        Map<String, Object> pMap = DatabaseHandlerDao.getInstance().queryForMap(sqlp, new String[]{dataMap.get("BZPCH")});
        TraceEntity traceEntity = new TraceEntity();
        traceEntity.setId((String) rkMap.get("ID"));
        traceEntity.setRef_id(dataMap.get("ID"));
        traceEntity.setAtom(dataMap.get("CSPCH"));
        traceEntity.setIn_trace_code((String) rkMap.get("YPTZSM"));
        traceEntity.setOut_trace_code(dataMap.get("YPTZSM"));
        traceEntity.setProcess_id((String) traceMap.get("ID"));
        traceEntity.setComp_code(dataMap.get("QYBM"));
        traceEntity.setComp_type("3");
        traceEntity.setPack_id(pMap.get("ID").toString());
        traceEntities.add(traceEntity);
        traceMap.put("traceEntity", traceEntities);
        return traceMap;
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
        String tableName = "T_SDZYC_JJG_CPJYXX";
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
        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            sendDelTraceOutService(id[i]);
        }
    }
    public Map<String,Object> getKcxxByQypch(String qypch){
        String sql =" select * from v_sdzyc_jjg_kcglxx where pch= ? and qybm =? and kc > 0";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{qypch,SerialNumberUtil.getInstance().getCompanyCode()});
    }
}

package com.ces.component.cjgycjyxxxz.service;

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

import com.ces.component.cjgycjyxxxz.dao.CjgycjyxxxzDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CjgycjyxxxzService extends TraceShowModuleDefineDaoService<StringIDEntity, CjgycjyxxxzDao> {

    @Override
    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
        Map<String, Object> returnData = new HashMap<String, Object>();
        boolean inserted = false;
        JsonNode entity = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entity);
        if (StringUtil.isEmpty(dataMap.get(AppDefineUtil.C_ID))) {
            inserted = true;
            dataMap.put(AppDefineUtil.C_ID, UUIDGenerator.uuid());
            String ycxsddh= StatisticalCodeUtil.getInstance().getTwentyFivePcm("CJG","SDZYC","SDZYCYCXSDDH");
             String qyxsddh = ycxsddh.substring(ycxsddh.length() - 11, ycxsddh.length());
            dataMap.put("QYXSDDH",qyxsddh);
            dataMap.put("XSDDH",ycxsddh);
            try {
                //根据交易信息进行随附单生成
                QRCodeUtil.encode(ycxsddh, ServletActionContext.getServletContext().getRealPath("/qrCode/cjg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
 //       if (sb.length() > 1) {
//            sb.deleteCharAt(0);
//            deleteOverDetail(tableName, sb.toString(), relateDateMap);
//        }
        //验证所选批次是否入库 ps:入库入的是整个批次
       /* String pch = String.valueOf(dataMap.get("PCH"));
        String sfrk_sql = "select * from T_sdzyc_cjg_ycrkxx where pch='"+pch+"'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sfrk_sql);
        if(list.size() != 0){//如果已经入库 改状态为1
            DatabaseHandlerDao.getInstance().executeSql("update t_sdzyc_cjg_ycjyxxxx set sfrk='1' where pch='"+pch+"'");
        }*/
        return dList;
    }

    private void sendCreatetTraceOutService(Map<String, String> dataMap, Map<String, String> masterMap, Map<String, Object> traceMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = tradeOutService(dataMap,  masterMap, traceMap);
        service.createTradeOut(tradeOutEntity);
    }
    private void sendModifyTraceOutService(String id,Map<String, String> dataMap, Map<String, String> masterMap, Map<String, Object> traceMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = tradeOutService(dataMap,  masterMap, traceMap);
        service.createTradeOut(tradeOutEntity);
    }
  private TradeOutEntity tradeOutService(Map<String, String> dataMap, Map<String, String> masterMap, Map<String, Object> traceMap)
    {
        TradeOutEntity tradeOutEntity = new TradeOutEntity();
        tradeOutEntity.setId(dataMap.get("ID"));
        tradeOutEntity.setBatch_no(dataMap.get("PCH"));
        tradeOutEntity.setOrder_no(dataMap.get("XSDDH"));
        tradeOutEntity.setHerb_name(dataMap.get("YCMC"));
        tradeOutEntity.setHerb_code(dataMap.get("YCDM"));
        tradeOutEntity.setIs_tested(dataMap.get("SFJY"));
        tradeOutEntity.setWeight(Float.parseFloat(dataMap.get("JYZL")));
        tradeOutEntity.setPrice(Float.parseFloat(dataMap.get("JYDJ")));
        tradeOutEntity.setDate(dateToLong(masterMap.get("JYSJ")));
        tradeOutEntity.setBuyer(masterMap.get("CGF"));
        tradeOutEntity.setComp_code(dataMap.get("QYBM"));
        tradeOutEntity.setComp_type(dataMap.get("2"));
        tradeOutEntity.setHerb_name_detail((String) traceMap.get("ycdetail"));
        tradeOutEntity.setTraceEntities((List<TraceEntity>) traceMap.get("traceEntity"));
        return tradeOutEntity;
    }
    public Map<String, Object> createTraceInfo(Map<String, String> dataMap){
        String sql = "select t.ID, t.YLPCH, t.JGPCH from t_sdzyc_cjg_ycjgxx t where t.jgpch=? and t.jyjg='1'";
        Map<String, Object> traceMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("PCH")});
        String sql1 = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        traceMap.put("ycdetail",DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("YCDM")}).get("YCXMNAME"));
        sql1 = "select t.ID from t_sdzyc_cjg_ylrkxx t where t.pch=?";
        Map<String, Object> rkMap = DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("CSPCH")});
        List<TraceEntity> traceEntities = new ArrayList<TraceEntity>();
        TraceEntity traceEntity = new TraceEntity();
        traceEntity.setIn_id((String) rkMap.get("ID"));
        traceEntity.setRef_id(dataMap.get("ID"));
        traceEntity.setAtom((String) traceMap.get("YLPCH"));
        traceEntity.setIn_trace_code(dataMap.get("CSPCH"));
        traceEntity.setOut_trace_code(dataMap.get("YPTZSM"));
        traceEntity.setProcess_id((String) traceMap.get("ID"));
        traceEntity.setComp_code(dataMap.get("QYBM"));
        traceEntity.setComp_type("2");
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

    public Map<String,Object> getCSPCH(String pch) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql =  "select t.ylpch from t_sdzyc_cjg_ycjgxx t where t.jgpch=? and qybm=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{pch,qybm});
    }

    public Map<String,Object> getKcxxByQypch(String qypch){
        String sql =" select * from v_sdzyc_cjg_kcglxx where pch= ? and qybm =? and kc > 0";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{qypch,SerialNumberUtil.getInstance().getCompanyCode()});
    }

}


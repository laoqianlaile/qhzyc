package com.ces.component.qhpfsc.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TraceEntity;
import enterprise.entity.TradeOutEntity;
import org.springframework.stereotype.Component;

import com.ces.component.qhpfsc.dao.QhpfscDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class QhpfscService extends TraceShowModuleDefineDaoService<StringIDEntity, QhpfscDao> {


    public Object getCcpch() {
        String qybm= SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from T_qh_PFCCXX where  is_delete ='0'and  qybm='"+qybm+"'";
        List<Map<String,Object>> list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data" , list);
        return  dataMap;
    }
    @Transactional
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String ids =dataMap.get("ID");
        if(StringUtil.isEmpty(ids)){
            String id = save(tableName, dataMap, paramMap);
            dataMap.put(AppDefineUtil.C_ID, id);
            sendCreatetTraceOutService(dataMap,createTraceInfo(dataMap));
        }else{
            save(tableName, dataMap, paramMap);
            sendModifyTraceOutService(ids,dataMap,createTraceInfo(dataMap));
        }

        return dataMap;
    }


    /**
     * 省平台数据新增
     * @param dataMap
     * @param
     *
     */
    private void sendCreatetTraceOutService(Map<String, String> dataMap, Map<String, Object> traceMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = traceOutService(dataMap,traceMap);
        service.createTradeOut(tradeOutEntity);
    }

    /**
     * 省平台数据修改
     * @param id
     * @param dataMap
     *
     */
    private void sendModifyTraceOutService(String id, Map<String, String> dataMap,Map<String,Object>traceMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = traceOutService(dataMap,traceMap);
        service.modifyTradeOut(id,tradeOutEntity);
    }

    /**
     * 省平台删除
     * @param id
     */
    private void sendDelTraceOutService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteTradeOut(id);
    }
    private TradeOutEntity  traceOutService(Map<String, String> dataMap,Map<String,Object>traceMap)
    {
        //EnterpriseService service =new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = new TradeOutEntity();
        tradeOutEntity.setId(dataMap.get("ID"));
        tradeOutEntity.setBatch_no(dataMap.get("PCH"));
        tradeOutEntity.setOrder_no(dataMap.get("XSDDH"));
        tradeOutEntity.setHerb_name(dataMap.get("YCMC"));
        tradeOutEntity.setHerb_code(dataMap.get("YCDM"));
        tradeOutEntity.setIs_tested(dataMap.get("SFJY"));
        tradeOutEntity.setWeight(Float.parseFloat(dataMap.get("JYZL")));
        tradeOutEntity.setPrice(Float.parseFloat(dataMap.get("JYDJ")));
        /*tradeOutEntity.setDate(dateToLong(masterMap.get("JYSJ")));
        tradeOutEntity.setBuyer(masterMap.get("CGF"));*/
        tradeOutEntity.setComp_code(dataMap.get("QYBM"));
        tradeOutEntity.setComp_type("3");
        tradeOutEntity.setHerb_name_detail(("ycdetail"));
       /* tradeOutEntity.setBzpch(dataMap.get("BZPCH"));
        String sql = "select t.id  from t_sdzyc_jjg_ypbzxx t where t.bzpch=?";
        Map<String, Object> pMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("BZPCH")});*/
      /*  tradeOutEntity.setPack_id(pMap.get("ID").toString());*/
        tradeOutEntity.setTraceEntities((List<TraceEntity>) traceMap.get("traceEntity"));
        //service.createTradeOut(tradeOutEntity);
        return tradeOutEntity;
    }

    public Map<String, Object> createTraceInfo(Map<String, String> dataMap){
        String sql = "select t.ID, t.YLPCH, t.JGPCH from t_sdzyc_cjg_ycjgxx t where t.jgpch=? and t.jyjg='1'";
        Map<String, Object> traceMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("PCH")});
        String sql1 = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        traceMap.put("ycdetail",DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("YCDM")}).get("YCXMNAME"));
        sql1 = "select t.ID from t_sdzyc_cjg_ylrkxx t where t.pch=?";
        Map<String, Object> rkMap = DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("PCH")});
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



    public void delete(String id){
        String sql="delete from t_qh_pfccxx where id= '"+id+"'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        sendDelTraceOutService(id);
    }
}

package com.ces.component.sdzycjjgyphpsc.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.*;
import com.ces.xarch.core.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.PrecisionEntity;
import enterprise.entity.PrecisionLlEntity;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjjgyphpsc.dao.SdzycjjgyphpscDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SdzycjjgyphpscService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgyphpscDao> {
    String zsmpch = "";
    String sycd = "";

    /**
     * 混批生产保存
     *
     * @param tableId
     * @param entityJson
     * @param dTableId
     * @param dEntitiesJson
     * @param paramMap
     * @return
     */
    @Override
    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
        Map<String, Object> returnData = new HashMap<String, Object>();
        boolean inserted = false;
        JsonNode entity = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entity);
        String id = dataMap.get(AppDefineUtil.C_ID);
        String scpch = dataMap.get("SCPCH");
        if (StringUtil.isEmpty(id)) {
            inserted = true;
            id = UUIDGenerator.uuid();
            dataMap.put(AppDefineUtil.C_ID, id);
            scpch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG", "SDZYC", "SDZYCJJGYPSCPCH");
            String qyscpch = scpch.substring(scpch.length() - 11, scpch.length());
            String jyjg = "0";
            dataMap.put("SCPCH", scpch);
           /* if (!(dataMap.get("QYBM").equalsIgnoreCase("000000807") || dataMap.get("QYBM").equalsIgnoreCase("000000824"))) {
                dataMap.put("QYSCPCH", qyscpch);
            }*/
        }
        Map<String, String> relateDateMap = getRelateDateMap(tableId, dTableId, dataMap);
        String tableName = getTableName(tableId), dTableName = getTableName(dTableId);
        List<Map<String, String>> detailList;
        // 保存明细记录
        List<PrecisionLlEntity> predataList = new ArrayList<PrecisionLlEntity>();
        if (inserted) {//新增进行数据同步操作
            detailList = saveDetail(dTableName, dEntitiesJson, dataMap, relateDateMap, paramMap, predataList, id, scpch);
            dataMap.put("CDMC", sycd);
            //***********同步省平台数据开始****************//
            //同步饮片生产主表信息
            sendCreatePrecisionService(dataMap,predataList);
            //***********同步省平台数据结束****************//
        } else {
            detailList = saveDetail(dTableName, dEntitiesJson, dataMap, relateDateMap, paramMap, predataList, id, scpch);
            dataMap.put("CDMC", sycd);
            sendModifyPrecisionService(id,dataMap,predataList);
        }

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
     *
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
     * qiucs 2015-2-28 上午11:24:30
     * <p>描述: 保存明细表数据 </p>
     *
     * @return Object
     */
    @Transactional
    protected List<Map<String, String>> saveDetail(String tableName, String entitiesJson,
                                                   Map<String, String> masterMap,
                                                   Map<String, String> relateDateMap,
                                                   Map<String, Object> paramMap, List<PrecisionLlEntity> dataList, String pid, String scpch) {
        List<Map<String, String>> dList = new ArrayList<Map<String, String>>();

        String id = null;
        JsonNode entities = JsonUtil.json2node(entitiesJson);
        Map<String, String> dataMap = null;
        StringBuilder sb = new StringBuilder();
        double weight = 0;
        String cd = "";
        for (int i = 0, len = entities.size(); i < len; i++) {
            dataMap = node2map(entities.get(i));
            dataMap.putAll(relateDateMap);
            id = dataMap.get(AppDefineUtil.C_ID);
            processBeforeSaveOneDetail(tableName, dataMap, masterMap, paramMap);
            double syWeight = Double.parseDouble(dataMap.get("JGZZL"));
            if (syWeight >= weight) {
                zsmpch = dataMap.get("YYCPCH");
                String sql = "select CD from t_sdzyc_jjg_yycrkxx where pch = ? and qybm = ?";
                Map<String, Object> mapData = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{String.valueOf(dataMap.get("YYCPCH")), SerialNumberUtil.getInstance().getCompanyCode()});
                cd = String.valueOf(mapData.get("CD"));
                sycd = cd;
                weight = syWeight;
            }
            if (StringUtil.isNotEmpty(id) && id.startsWith("UNSAVE_")) {
                dataMap.remove(AppDefineUtil.C_ID);
                id = saveOne(tableName, dataMap);
                //处理饮片生产所需要的多个原料信息
                EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
                PrecisionLlEntity pe = new PrecisionLlEntity();
                pe.setId(dataMap.get("ID"));
                pe.setCspch(dataMap.get("CSPCH"));
                pe.setLldh(dataMap.get("LLDH"));
                pe.setLlmc(dataMap.get("YCMC"));
                pe.setLlsj(dataMap.get("LLSJ"));
                pe.setLlzzl(dataMap.get("JGZZL"));
                pe.setProcess_id(pid);//
                pe.setQypch(dataMap.get("QYPCH"));
                pe.setScpch(scpch);
                pe.setCd(String.valueOf(dataMap.get("CD")));
                //pe.setWlgg("");
                pe.setYlpch(dataMap.get("YYCPCH"));
                dataList.add(pe);
            } else {
                id = saveOne(tableName, dataMap);
            }
            String sql = "update t_sdzyc_jjg_scllxx set issc='1' where lldh=?";
            DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{dataMap.get("LLDH")});
            sb.append(",'").append(id).append("'");
            processAfterSaveOneDetail(tableName, dataMap, masterMap, paramMap);
            dList.add(dataMap);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            deleteOverDetail(tableName, sb.toString(), relateDateMap);
        }
        return dList;
    }
    private void sendCreatePrecisionService(Map<String, String> dataMap,List<PrecisionLlEntity> predataList){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        PrecisionEntity pe =precisionService(dataMap,predataList);
        service.createPrecision(pe);
    }
    private void sendModifyPrecisionService(String id,Map<String, String> dataMap,List<PrecisionLlEntity> predataList){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        PrecisionEntity pe =precisionService(dataMap,predataList);
        service.modifyPrecision(id,pe);
    }
    private PrecisionEntity precisionService(Map<String, String> dataMap,List<PrecisionLlEntity> predataList)
    {
        PrecisionEntity pe = new PrecisionEntity();
        pe.setId(dataMap.get("ID"));
        pe.setComp_code(SerialNumberUtil.getInstance().getCompanyCode());
        pe.setManufact_plan_no(String.valueOf(dataMap.get("SCFABH")));
        pe.setManufact_batch_no(String.valueOf(dataMap.get("SCPCH")));
        pe.setHerb_code(String.valueOf(dataMap.get("YCDM")));
        pe.setHerb_name(String.valueOf(dataMap.get("YPMC")));
        pe.setDate(dateToLong(dataMap.get("SCRQ")));
        String jgzzl = String.valueOf(dataMap.get("JGZZL"));
        if(""!=jgzzl&&jgzzl!="null"&&jgzzl!=null&&!jgzzl.isEmpty())
        {
            pe.setWeight(Float.parseFloat(jgzzl));
        }
        else
        {
            pe.setWeight(0);
        }
        pe.setStandard(String.valueOf(dataMap.get("ZXBZ")));
        pe.setCraft_man(String.valueOf(dataMap.get("GYY")));
        pe.setManager(String.valueOf(dataMap.get("SCJL")));
        pe.setImage(String.valueOf(dataMap.get("XCTP")));
        pe.setBatch_no(zsmpch);
        pe.setCrafts(String.valueOf(dataMap.get("JGGY")));
        pe.setProintroduce(String.valueOf(dataMap.get("YPJS")));
        //放入多个批次信息
        pe.setPrecisionlls(predataList);
        pe.setPack_spec(String.valueOf(dataMap.get("SCGG")));
        return pe;
    }
    private Long dateToLong(String str){
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        long t = 0;
        try{
            if(StringUtil.isNotEmpty(str)) {
                date = dd.parse(str);
            }else{
                date = new Date();
            }
            t = date.getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return t;
    }
}

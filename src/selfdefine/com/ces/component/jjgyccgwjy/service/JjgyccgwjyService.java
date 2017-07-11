package com.ces.component.jjgyccgwjy.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.jjgyccgwjy.dao.JjgyccgwjyDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TraceEntity;
import enterprise.entity.TradeInEntity;
import enterprise.entity.TradeOutEntity;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class JjgyccgwjyService extends TraceShowModuleDefineDaoService<StringIDEntity, JjgyccgwjyDao> {

    @Override
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        dataMap.put("YPTZSM",dataMap.get("CSPCH")+dataMap.get("PCH"));
        dataMap.put("CGLX","自产");
        String id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        //****************数据保存成功后进行省平台**********************//
        sendCreatetTraceOutService(dataMap);
        return dataMap;
    }
    public void sendCreatetTraceOutService(Map<String,String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = new TradeOutEntity();
        //根据无交易环节，默认处理交易主表数据
        tradeOutEntity.setId(dataMap.get("ID"));
        tradeOutEntity.setBatch_no(dataMap.get("CSPCH"));
        tradeOutEntity.setOrder_no("");
        tradeOutEntity.setHerb_name(dataMap.get("YCMC"));
        tradeOutEntity.setHerb_code(dataMap.get("YCDM"));
        tradeOutEntity.setIs_tested("");
        tradeOutEntity.setWeight(Float.parseFloat(dataMap.get("RKZL")));
        tradeOutEntity.setPrice(0);
        tradeOutEntity.setBuyer(CompanyInfoUtil.getInstance().getCompanyName_sdzyc(SerialNumberUtil.getInstance().getCompanyCode(),"JJGQY"));
        tradeOutEntity.setComp_code(dataMap.get("QYBM"));
        tradeOutEntity.setComp_type("2");
        tradeOutEntity.setHerb_name_detail(dataMap.get("YCMC"));
        //根据无交易环节，默认处理交易详细数据
        String sql = "select t.ID, t.YLPCH, t.JGPCH from t_sdzyc_cjg_ycjgxx t where t.jgpch=? and t.jyjg='1'";
        Map<String, Object> traceMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("PCH")});
        String sql1 = "select t.ID from t_sdzyc_cjg_ylrkxx t where t.pch=?";
        Map<String, Object> rkMap = DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("CSPCH")});
        TraceEntity traceEntity = new TraceEntity();
        traceEntity.setComp_type("2");
        traceEntity.setComp_code(SerialNumberUtil.getInstance().getCompanyCode());
        traceEntity.setAtom(dataMap.get("CSPCH"));//采收批次号
        traceEntity.setId("");//详细数据id
        traceEntity.setIn_trace_code(dataMap.get("CSPCH"));//外购入口数据

        traceEntity.setOut_trace_code(dataMap.get("YPTZSM"));//采收批次号+生产批次号
        traceEntity.setProcess_id((String) traceMap.get("ID"));//生产id
        traceEntity.setIn_id((String) rkMap.get("ID"));//
        traceEntity.setRef_id(dataMap.get("ID"));//
        List<TraceEntity> dataList = new ArrayList<TraceEntity>();
        dataList.add(traceEntity);
        tradeOutEntity.setTraceEntities(dataList);
        tradeOutEntity.setDate(dateToLong(dataMap.get("RKSJ")));
        service.createTradeOut(tradeOutEntity);

        TradeInEntity inEntity = new TradeInEntity();
        inEntity.setId(dataMap.get("ID"));
        inEntity.setBatch_no(dataMap.get("PCH"));
        inEntity.setHerb_name(dataMap.get("YCMC"));
        if(StringUtil.isNotEmpty(dataMap.get("GYS"))){
            inEntity.setOrigin(dataMap.get("GYS"));
        }else{
            inEntity.setOrigin(dataMap.get("CD"));
        }
        inEntity.setWeight(Float.parseFloat(dataMap.get("RKZL")));
        inEntity.setDate(dateToLong(dataMap.get("RKSJ").toString()));
        inEntity.setTest_link(dataMap.get("JYXXLJ").toString());
        inEntity.setPerson_in_charge(dataMap.get("FZR").toString());
        inEntity.setComp_code(dataMap.get("QYBM"));
        inEntity.setComp_type("3");
       // inEntity.setChannel(dataMap.get("LYZT"));
        inEntity.setCglx(dataMap.get("CGLX").toString());
        String sql2 = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        inEntity.setHerb_name_detail(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql2, new String[]{dataMap.get("YCDM")}).get("ycxmname")));
        service.createTradeIn(inEntity);

    }


    /**
     * ͨ������Ž������Ϣ����ҳ����
     * @param rkbh
     * @return
     */
    public List<Map<String, Object>> getDataByrkbh(String rkbh) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_sdzyc_cjg_ycrkxx a " +
                "LEFT JOIN (select DISTINCT llmc,ycdm from t_sdzyc_cjg_ylllxxxx ) b on a.ycmc = b.llmc " +
                "where a.rkbh='" + rkbh + "' and a.qybm='" + qybm + "'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    /**
     * ���³���״̬
     * @param rkbh
     */
    public int processSfck(String rkbh){
        String sql = "update t_sdzyc_cjg_ycrkxx  set sfck='1' where rkbh=? and qybm=?";
       return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{rkbh,SerialNumberUtil.getInstance().getCompanyCode()});
    }

    /**
     * һ���κ�һ���  ����ѯ
     * @param pch
     * @return
     */
    public Map<String,Object> searchKcByPch(String pch){
        String sql = " select * from t_sdzyc_cjg_ycrkxx t where t.pch ='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    private Long dateToLong(String str){
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        long t = 0;
        try{
            date =  dd.parse(str);
            t = date.getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return t;
    }
}

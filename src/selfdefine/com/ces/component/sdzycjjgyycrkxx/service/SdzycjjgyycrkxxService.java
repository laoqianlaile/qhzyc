package com.ces.component.sdzycjjgyycrkxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TradeInEntity;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjjgyycrkxx.dao.SdzycjjgyycrkxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycjjgyycrkxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgyycrkxxDao> {

    /**
     *
     * @param tableId--获取粗加工原料入库编号
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
            String ylrkbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGYCZCRKBH", true);
            dataMap.put("RKBH",ylrkbh);
            dataMap.put("CGLX","自产");
            id = save(tableName, dataMap, paramMap);
            sendCreateTradeInService(dataMap);
        }else {
            if(dataMap.get("PCH").toString().startsWith("W")) {
                dataMap.put("CGLX", "外购");
            }else{
                dataMap.put("CGLX","自产");
            }
            id = save(tableName, dataMap, paramMap);
            sendModifyTradeInService(id,dataMap);
        }
        dataMap.put(AppDefineUtil.C_ID, id);
        //保存成功同步处理粗加工企业更改为已入库状态
        String sql = "update t_sdzyc_cjg_ycjyxxxx set SFRK='1' where PCH=?";
        DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataMap.get("PCH")});
        return dataMap;
    }

    private void sendCreateTradeInService(Map<String, String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = tradeInService(dataMap);
        service.createTradeIn(inEntity);
    }
    private void sendModifyTradeInService(String id, Map<String, String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = tradeInService(dataMap);
        service.modifyTradeIn(id,inEntity);
    }
    public void sendDelTradeInService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteTradeIn(id);
    }
    private TradeInEntity tradeInService(Map<String, String> dataMap)
    {
        TradeInEntity inEntity = new TradeInEntity();
        inEntity.setId(dataMap.get("ID"));
        inEntity.setBatch_no(dataMap.get("PCH"));
        inEntity.setHerb_name(dataMap.get("YCMC"));
        inEntity.setOrigin(dataMap.get("CD").toString());
        inEntity.setWeight(Float.parseFloat(dataMap.get("RKZL").toString()));
        inEntity.setDate(dateToLong(dataMap.get("RKSJ").toString()));
        inEntity.setTest_link(dataMap.get("JYXXLJ").toString());
        inEntity.setPerson_in_charge(dataMap.get("FZR").toString());
        inEntity.setComp_code(dataMap.get("QYBM"));
        inEntity.setComp_type("3");
        //  inEntity.setChannel(dataMap.get("LYZT").toString());
        inEntity.setCglx(dataMap.get("CGLX").toString());
        String sql = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        inEntity.setHerb_name_detail(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("YCDM")}).get("ycxmname")));
        return inEntity;
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

    /**
     * 精加工药材采购一批次号一库存  库存查询
     * @param pch
     * @return
     */
    public Map<String,Object> searchDataByPch(String pch){
        String sql = " select * from t_sdzyc_cjg_ycrkxx t where t.pch ='"+pch+"' oder by pch desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    /**保存导入数据
     * @param dataList
     * @return
     */
    @Transactional
    public Object importXls(List<Map<String,String>> dataList){
        Map<String,String> resultMap = new HashMap<String, String>();
        Map<String,Object> shareMap;
        if(dataList.size() == 0){
            resultMap.put("RESULT","ERROR");
            resultMap.put("MSG","模版里无数据！");
            return resultMap;
        }
        resultMap.put("RESULT","SUCCESS");
        resultMap.put("MSG","成功导入数据！");
        StringBuilder sql ;
        if("SUCCESS".equals(resultMap.get("RESULT"))){
            for(Map<String,String> map:dataList){
                saveOne("t_sdzyc_jjg_yycrkxx", map);
            }
        }
        return resultMap;
    }
    public void delete(String pch,String id){
        String Updatesql = "update t_sdzyc_cjg_ycjyxxxx  set sfrk='0' where pch=?";
        DatabaseHandlerDao.getInstance().executeSql(Updatesql, new Object[]{pch});
        String sql = " delete from t_sdzyc_jjg_yycrkxx where id  = '" + id + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }
}

//    public Map<String,Object> searchYclComboGridData() {
//        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
//        String sql = "select * from T_SDZYC_JJG_YYCRKXX t where t.qybm= '"+companyCode+"'";
//        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
//    }


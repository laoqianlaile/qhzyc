package com.ces.component.qhpfrcxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.DataTypeConvertUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TradeInEntity;
import org.springframework.stereotype.Component;

import com.ces.component.qhpfrcxx.dao.QhpfrcxxDao;
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
public class QhpfrcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, QhpfrcxxDao> {
    @Transactional
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
         String ids =dataMap.get("ID");
        if(StringUtil.isEmpty(ids)){
            String id = save(tableName, dataMap, paramMap);
            dataMap.put(AppDefineUtil.C_ID, id);
            sendCreateTradeInService(dataMap);
        }else{
            save(tableName, dataMap, paramMap);
            sendModifyTradeInService(ids,dataMap);

        }
        return dataMap;
    }

    /**
     * 上传入场省平台数据
     * @param dataMap
     */
    private TradeInEntity TradeInService(Map<String, String> dataMap){
       // EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = new TradeInEntity();
        inEntity.setId(dataMap.get("ID"));
        inEntity.setBatch_no(dataMap.get("PCH"));
        inEntity.setHerb_name(dataMap.get("YCMC"));
        if(StringUtil.isNotEmpty(dataMap.get("GYS"))){
            inEntity.setOrigin(dataMap.get("GYS"));
        }else{
            inEntity.setOrigin(dataMap.get("CD"));
        }
        inEntity.setWeight(Float.parseFloat(dataMap.get("RKZL").toString()));
        inEntity.setDate(dateToLong(dataMap.get("RKSJ").toString()));
        //inEntity.setTest_link(dataMap.get("JYXXLJ").toString());
//        inEntity.setPerson_in_charge(dataMap.get("FZR").toString());
        inEntity.setComp_code(dataMap.get("QYBM"));
        inEntity.setComp_type("3");
        // inEntity.setChannel(dataMap.get("LYZT"));
        //inEntity.setCglx(dataMap.get("CGLX").toString());
        /*String sql = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        inEntity.setHerb_name_detail(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("YCDM")}).get("ycxmname")));*/
        //service.createTradeIn(inEntity);
        return inEntity;
    }

    /**
     * 上传省平台新增
     * @param dataMap
     */
    private void sendCreateTradeInService(Map<String, String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = TradeInService(dataMap);
        service.createTradeIn(inEntity);
    }

    /**
     * 上传省平台修改
     * @param id
     * @param dataMap
     */
    private void sendModifyTradeInService(String id,Map<String, String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = TradeInService(dataMap);
        service.modifyTradeIn(id,inEntity);
    }

    /**
     * 省平台数据删除
     * @param id
     */

    private void sendDelTradeInService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteTradeIn(id);
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


    public void delete(String id){
        String sql = " delete from t_qh_PFRCXX t where id  = '" + id + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        sendDelTradeInService(id);
    }

    public Object getrcpch() {
        String qybm= SerialNumberUtil.getInstance().getCompanyCode();
        String sql ="select *from t_qh_PFRCXX where is_delete='0' and qybm='"+qybm+"'  ";
        List<Map<String,Object>> list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data" , list);
        return dataMap;
    }





}

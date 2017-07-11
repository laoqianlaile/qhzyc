package com.ces.component.sdzycjjgycrkwgxz.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TradeInEntity;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjjgycrkwgxz.dao.SdzycjjgycrkwgxzDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycjjgycrkwgxzService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgycrkwgxzDao> {
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
            String wgpch = "W"+StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG", "SDZYC", "SDZYCJJGWGPCH");
            String ylrkbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGYCZCRKBH", true);
            dataMap.put("RKBH",ylrkbh);
            dataMap.put("PCH",wgpch);
            //dataMap.put("QYPCH",wgpch);
            dataMap.put("QYBM", companyCode);
            dataMap.put("CGLX","外购");
            id = save(tableName, dataMap, paramMap);
            sendCreateTradeInService(dataMap);
        }else {
            id = save(tableName, dataMap, paramMap);
        }
        dataMap.put(AppDefineUtil.C_ID, id);
        String sql = "update T_SDZYC_JJG_YYCRKXX set LYZT=1 where PCH=?";
        DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataMap.get("PCH")});
        return dataMap;
    }

    private void sendCreateTradeInService(Map<String, String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
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
        inEntity.setTest_link(dataMap.get("JYXXLJ").toString());
        inEntity.setPerson_in_charge(dataMap.get("FZR").toString());
        inEntity.setComp_code(dataMap.get("QYBM"));
        inEntity.setComp_type("3");
       // inEntity.setChannel(dataMap.get("LYZT"));
        inEntity.setCglx(dataMap.get("CGLX").toString());
        String sql = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        inEntity.setHerb_name_detail(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("YCDM")}).get("ycxmname")));
        service.createTradeIn(inEntity);
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
    public Map<String,Object> searchycmcComboGridData() {
        String sql = "select * from T_SDZYC_ZYCSPBM where qylx='JJG' " + defaultCode();
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
}

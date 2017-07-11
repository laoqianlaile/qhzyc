package com.ces.component.sdzyccjgycjgxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.sdzyccjgycjgxx.dao.SdzyccjgycjgxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.ImprecisionEntity;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyccjgycjgxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgycjgxxDao> {

    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String jgpch = SerialNumberUtil.getInstance().getSerialNumber("SDZYC","SDZYCJGPCH", false);
            String qyjgpch = jgpch.substring(jgpch.length() - 11, jgpch.length());
            dataMap.put("JGPCH",jgpch);
          //  dataMap.put("QYJGPCH",qyjgpch);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
    public void sendCreateImprecisionService(Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        ImprecisionEntity  pe = ImprecisionServie(map);
        service.createImprecision(pe);
    }
    public void sendModifyImprecisionService(String id,Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        ImprecisionEntity  pe = ImprecisionServie(map);
        service.modifyImprecision(id,pe);
    }
    public void sendDelImprecisionService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteImprecision(id);
    }
    private ImprecisionEntity ImprecisionServie(Map<String, String> map)
    {
        ImprecisionEntity pe = new ImprecisionEntity();
        pe.setComp_code((String) map.get("QYBM"));
        pe.setId((String) map.get("ID"));
        pe.setProcess_no((String) map.get("JGPCH"));
        pe.setRaw_no((String) map.get("YLPCH"));
        pe.setWeight(Float.parseFloat(String.valueOf(map.get("JGZZL"))));
        pe.setWorkshop((String) map.get("JGCJ"));
        pe.setImage((String) map.get("XCTP"));
        pe.setDate(dateToLong(String.valueOf(map.get("SCRQ"))));
        pe.setPerson_in_charge((String) map.get("JGFZR"));
        pe.setCrafts((String) map.get("JGGY"));
        // pe.setCraft_man((String) map.get("GYY"));
        // pe.setManager((String) map.get("SCJL"));
        pe.setHerb_name((String) map.get("YLMC"));
        pe.setHerb_code((String) map.get("YLDM"));
        pe.setRaw_in_date(dateToLong((String) map.get("SCRQ")));
        pe.setBase64((String) map.get("base64"));
        return pe;
    }
    /**
     *默认权限过滤
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
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
     * 获取仓库信息下拉列表数据
     * @return
     */
    public Map<String,Object> searchycjgxxComboGridData(){
        String sql = "select DISTINCT t.jgpch,t.ylpch,t.ylmc,t.jgzzl,m.ycdm,T.QYPCH,T.QYJGPCH from t_Sdzyc_Cjg_Ycjgxx t inner join t_sdzyc_cjg_ylrkxx m on t.ylpch=m.pch and t.qybm=? and t.is_delete='0' order by jgpch desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()}));
    }

    /**
     * 获取合格的药材加工信息进行包装及入库
     * @return
     */
    public Map<String,Object> searchycjgxxhgComboGridData(){
        //String sql = "select DISTINCT t.jgpch,t.ylpch,t.qybm,t.ylmc,t.jgzzl,m.ycdm,T.QYPCH,T.QYJGPCH,n.bzgg from t_Sdzyc_Cjg_Ycjgxx t inner join t_sdzyc_cjg_ylrkxx m on t.ylpch=m.pch and t.qybm=? and t.is_delete='0' inner join T_SDZYC_CJG_DYFMXX  n on n.scpch=t.jgpch where t.jyjg='1' and t.sffm='0' order by jgpch desc";
        String sql = "select DISTINCT t.jgpch,t.ylpch,t.qybm,t.ylmc,t.jgzzl,m.ycdm,T.QYPCH,T.QYJGPCH,t.scrq from t_Sdzyc_Cjg_Ycjgxx t inner join t_sdzyc_cjg_ylrkxx m on t.ylpch=m.pch and t.qybm=? and t.is_delete='0' and t.jyjg='1' and t.sffm='0' order by t.scrq desc";

        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()}));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    public Map<String,Object> searchycjgxxByckbh(String jgpch){
        String sql =  "select * from t_sdzyc_cjg_ycjgxx where jgpch=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{jgpch});
    }


    public Map<String,Object> setqyzjpch(){
        String sql = "select DISTINCT t.jgpch,t.ylpch,t.ylmc,t.jgzzl,m.ycdm,T.QYPCH,T.QYJGPCH,t.scrq from t_Sdzyc_Cjg_Ycjgxx t inner join t_sdzyc_cjg_ylrkxx m on t.ylpch=m.pch and t.qybm=? and t.is_delete='0'and t.jyjg='0' order by t.scrq desc";

        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()}));
    }

    /**
     * 根据Id查询粗加工加工信息
     * @param id
     * @return
     */
    public Map<String ,Object> searchById(String id ){
        String sql =  "select * from t_sdzyc_cjg_ycjgxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    /**
     * 修改图片信息
     * @param id
     * @return
     */
    public int updateXctp(String id ){
        String sql =  "update t_sdzyc_cjg_ycjgxx set xctp='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }
    public void delete(String lldh,String id){
        String Updatesql = "update t_sdzyc_cjg_ylllxx t set issc='0' where lldh=?";
        DatabaseHandlerDao.getInstance().executeSql(Updatesql, new Object[]{lldh});
        String sql = " delete from t_sdzyc_cjg_ycjgxx where id  = '" + id + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }
    public Map<String,Object> queryPch(String pch){
        String sql =  "select sffm,sfrk,jyjg from t_sdzyc_cjg_ycjgxx where jgpch='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
}
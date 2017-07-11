package com.ces.component.sdzycyprkxx.service;

import com.ces.component.sdzycyprkxx.dao.SdzycyprkxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class SdzycyprkxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycyprkxxDao> {

    /**
     * 获精加工饮片入库下拉框数据
     * @return
     */
    public Map<String,Object> getJjgyprkGrid(){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "";
        if(companyCode.equalsIgnoreCase("000000809")||companyCode.equalsIgnoreCase("000000012")) {
            //阿胶 不检验也能入库
            sql = "select distinct t.cspch, t.scpch,t.ypmc,t.qybm,t.jgzzl,t.ycdm,t.qyscpch qypch from T_SDZYC_JJG_YPSCXX t   where  is_delete='0' and t.qybm= '" + companyCode + "'";
        }else {
            sql = "select t.bzpch, t.scpch,t.ypmc,t.qybm,t.bzzl,t.ycdm,t.qypch,t.qybzpch from t_sdzyc_jjg_ypbzxx t where sfrk='0' and t.qybm='" + companyCode + "'";
           // sql = "select distinct t.cspch, t.scpch,t.ypmc,t.qybm,t.jgzzl,t.ycdm,m.qypch from T_SDZYC_JJG_YPSCXX t inner join  T_SDZYC_JJG_YPJYJCXX m  on t.scpch=m.pch  where m.jyjg='1' and t.qybm= '" + companyCode + "'";
        }
            return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    /**
     * 获精加工饮片包装入库下拉框数据
     * @return
     */
    public Map<String,Object> getJjgypbzrkGrid(){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "";
        if(companyCode.equalsIgnoreCase("000000809")) {
            //阿胶 不检验也能入库
            sql = "select distinct t.bzpch,t.qybzpch, t.scpch,t.qypch,t.ypmc, t.ycdm,t.qybm,t.bzzl,t.bzgg,t.bzsj from t_sdzyc_jjg_ypbzxx t   where  is_delete='0' and t.qybm= '" + companyCode + "' order by bzsj desc";
        }else {
            sql = "select distinct t.bzpch,t.qybzpch, t.scpch,t.qypch,t.ypmc, t.ycdm,t.qybm,t.bzzl,t.bzgg,t.bzsj from t_sdzyc_jjg_ypbzxx t inner join  T_SDZYC_JJG_YPJYJCXX m  on t.scpch=m.pch  where m.jyjg='1' and t.sfrk='0' and t.qybm= '" + companyCode + "'  order by bzsj desc";
        }
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    //企业批次号下拉框数据   药材名称  入库重量   库存(不为零)
    public Map<String,Object> findJjgQypchGridData(){
        String sql = "select * from v_sdzyc_jjg_kcglxx where kc > 0 and qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return getGridTypeData(list);
    }
    //包装批次号下拉框数据   药材名称  入库重量   库存(不为零)
    public Map<String,Object> findJjgBzpchGridData(){
       // String sql = "select * from v_sdzyc_jjg_kcglxx where kc > 0 and qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        String sql = "select pch,bzpch,qybzpch,(rkzzl/1000) rkzzl,(kc/1000) kc,ypmc,ckmc,bzgg,qypch,(jyzl/1000) jyzl,qybm,rksj from v_sdzyc_jjg_kcglxx where kc > 0 and qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"' order by rksj desc";
       // String sql = "select * from v_sdzyc_jjg_kcglxx where kc > 0 and qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"' order by rksj desc";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return getGridTypeData(list);
    }
    /**
     * 获取精加工饮片交易中批次号下拉框中的数据
     * @return
     */
    public Map<String,Object> getPch(){
        String sql = "select * from T_SDZYC_JJG_Ypbzxx t where t.qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        return  getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    /**
     * 山东中药材精加工饮片入库编号自动生成 保存
     * @param tableId
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
            String jjggysbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGYPRKBH", true);
            dataMap.put("RKBH",jjggysbh);
        }
        id = save(tableName, dataMap, paramMap);
        String sql = "update t_sdzyc_jjg_ypbzxx set SFRK='1' where BZPCH=?";
        DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataMap.get("BZPCH")});
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
    /**保存导入数据
     * @param dataList
     * @return
     */
   /* @Transactional
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
                saveOne("t_sdzyc_jjg_yprkxx", map);
            }
        }
        return resultMap;
    }*/

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
                saveOne("t_sdzyc_jjg_yprkxx", map);
               // saveOne("t_sdzyc_jjg_cpjyxx", map);
              //  saveOne("t_sdzyc_jjg_cpjyxxxx", map);
               // saveOne("t_cs_scjcxx", map);
               // saveOne("t_cs_scjcmxxx", map);
            }
        }
        return resultMap;
    }
    public void delete(String pch,String id){
        String sql = " delete from t_sdzyc_jjg_yprkxx where id  = '" + id + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        String Updatesql = "update t_sdzyc_jjg_ypbzxx  set sfrk='0' where bzpch=?";
        DatabaseHandlerDao.getInstance().executeSql(Updatesql, new Object[]{pch});

    }
}
